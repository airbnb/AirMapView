package com.airbnb.android.airmapview;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.airbnb.android.airmapview.listeners.InfoWindowCreator;
import com.airbnb.android.airmapview.listeners.OnCameraChangeListener;
import com.airbnb.android.airmapview.listeners.OnInfoWindowClickListener;
import com.airbnb.android.airmapview.listeners.OnLatLngScreenLocationCallback;
import com.airbnb.android.airmapview.listeners.OnMapBoundsCallback;
import com.airbnb.android.airmapview.listeners.OnMapClickListener;
import com.airbnb.android.airmapview.listeners.OnMapLoadedListener;
import com.airbnb.android.airmapview.listeners.OnMapMarkerClickListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public abstract class WebViewMapFragment extends Fragment implements AirMapInterface {

  private static final String TAG = WebViewMapFragment.class.getSimpleName();

  private WebView webView;
  private ViewGroup mLayout;
  private OnMapClickListener onMapClickListener;
  private OnCameraChangeListener onCameraChangeListener;
  private OnMapLoadedListener onMapLoadedListener;
  private OnMapMarkerClickListener onMapMarkerClickListener;
  private OnInfoWindowClickListener onInfoWindowClickListener;
  private InfoWindowCreator infoWindowCreator;
  private OnMapBoundsCallback onMapBoundsCallback;
  private OnLatLngScreenLocationCallback onLatLngScreenLocationCallback;
  private LatLng center;
  private int zoom;
  private boolean loaded;
  private boolean ignoreNextMapMove;
  private View infoWindowView;

  private boolean trackUserLocation = false;

  public WebViewMapFragment setArguments(AirMapType mapType) {
    setArguments(mapType.toBundle());
    return this;
  }

  public class GeoWebChromeClient extends WebChromeClient {
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin,
                                                   GeolocationPermissions.Callback callback) {
      // Always grant permission since the app itself requires location
      // permission and the user has therefore already granted it
      callback.invoke(origin, true, false);
    }
  }

  @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_webview, container, false);

    webView = (WebView) view.findViewById(R.id.webview);
    mLayout = (ViewGroup) view;

    WebSettings webViewSettings = webView.getSettings();
    webViewSettings.setSupportZoom(true);
    webViewSettings.setBuiltInZoomControls(false);
    webViewSettings.setJavaScriptEnabled(true);
    webViewSettings.setGeolocationEnabled(true);

    webView.setWebChromeClient(new GeoWebChromeClient());

    AirMapType mapType = AirMapType.fromBundle(getArguments());

    webView.loadDataWithBaseURL(mapType.getDomain(), mapType.getMapData(getResources()),
                                 "text/html", "base64", null);
    webView.addJavascriptInterface(new MapsJavaScriptInterface(), "AirMapView");

    return view;
  }

  public int getZoom() {
    return zoom;
  }

  public LatLng getCenter() {
    return center;
  }

  public void setCenter(LatLng latLng) {
    webView.loadUrl(String.format("javascript:centerMap(%1$f, %2$f);", latLng.latitude,
                                  latLng.longitude));
  }

  public void animateCenter(LatLng latLng) {
    setCenter(latLng);
  }

  public void setZoom(int zoom) {
    webView.loadUrl(String.format("javascript:setZoom(%1$d);", zoom));
  }

  public void drawCircle(LatLng latLng, int radius) {
    drawCircle(latLng, radius, CIRCLE_BORDER_COLOR);
  }

  @Override public void drawCircle(LatLng latLng, int radius, int borderColor) {
    drawCircle(latLng, radius, borderColor, CIRCLE_BORDER_WIDTH);
  }

  @Override public void drawCircle(LatLng latLng, int radius, int borderColor, int borderWidth) {
    drawCircle(latLng, radius, borderColor, borderWidth, CIRCLE_FILL_COLOR);
  }

  @Override public void drawCircle(LatLng latLng, int radius, int borderColor, int borderWidth,
                         int fillColor) {
    webView.loadUrl(String.format("javascript:addCircle(%1$f, %2$f, %3$d, %4$d, %5$d, %6$d);",
                                  latLng.latitude, latLng.longitude, radius, borderColor,
                                  borderWidth, fillColor));
  }

  public void highlightMarker(long markerId) {
    if (markerId != -1) {
      webView.loadUrl(String.format("javascript:highlightMarker(%1$d);", markerId));
    }
  }

  public void unhighlightMarker(long markerId) {
    if (markerId != -1) {
      webView.loadUrl(String.format("javascript:unhighlightMarker(%1$d);", markerId));
    }
  }

  @Override public boolean isInitialized() {
    return webView != null && loaded;
  }

  @Override public void addMarker(AirMapMarker marker) {
    LatLng latLng = marker.getLatLng();
    webView.loadUrl(String.format("javascript:addMarkerWithId(%1$f, %2$f, %3$d, '%4$s', '%5$s');",
                                   latLng.latitude, latLng.longitude, marker.getId(), marker.getTitle(), marker.getSnippet()));
  }

  @Override public void removeMarker(AirMapMarker marker) {
    webView.loadUrl(String.format("javascript:removeMarker(%1$d);", marker.getId()));
  }

  public void clearMarkers() {
    webView.loadUrl("javascript:clearMarkers();");
  }

  public void setOnCameraChangeListener(OnCameraChangeListener listener) {
    onCameraChangeListener = listener;
  }

  public void setOnMapLoadedListener(OnMapLoadedListener listener) {
    onMapLoadedListener = listener;
    if (loaded) {
      onMapLoadedListener.onMapLoaded();
    }
  }

  @Override public void setCenterZoom(LatLng latLng, int zoom) {
    setCenter(latLng);
    setZoom(zoom);
  }

  @Override public void animateCenterZoom(LatLng latLng, int zoom) {
    setCenterZoom(latLng, zoom);
  }

  public void setOnMarkerClickListener(OnMapMarkerClickListener listener) {
    onMapMarkerClickListener = listener;
  }

  @Override public void setPadding(int left, int top, int right, int bottom) {
    // no-op
  }

  @Override public void setMyLocationEnabled(boolean b) {
    this.trackUserLocation = b;
    if (b) {
      webView.loadUrl("javascript:startTrackingUserLocation();");
    } else {
      webView.loadUrl("javascript:stopTrackingUserLocation();");
    }
  }

  @Override
  public boolean isMyLocationEnabled() {
    return trackUserLocation;
  }

  @Override public void setMapToolbarEnabled(boolean enabled){
    // no-op
  }

  @Override public void addPolyline(AirMapPolyline polyline) {
    try {
      JSONArray array = new JSONArray();
      for (LatLng point : (List<LatLng>) polyline.getPoints()) {
        JSONObject json = new JSONObject();
        json.put("lat", point.latitude);
        json.put("lng", point.longitude);
        array.put(json);
      }

      webView.loadUrl(String.format(
          "javascript:addPolyline(" + array.toString() + ", %1$d, %2$d, %3$d);",
          polyline.getId(), polyline.getStrokeWidth(), polyline.getStrokeColor()));
    } catch (JSONException e) {
      Log.e(TAG, "error constructing polyline JSON", e);
    }
  }

  @Override public void removePolyline(AirMapPolyline polyline) {
    webView.loadUrl(String.format("javascript:removePolyline(%1$d);", polyline.getId()));
  }

  @Override public void setOnMapClickListener(final OnMapClickListener listener) {
    onMapClickListener = listener;
  }

  public void setOnInfoWindowClickListener(OnInfoWindowClickListener listener) {
    onInfoWindowClickListener = listener;
  }

  @Override public void setInfoWindowCreator(GoogleMap.InfoWindowAdapter adapter,
                                   InfoWindowCreator creator) {
    infoWindowCreator = creator;
  }

  @Override public void getMapScreenBounds(OnMapBoundsCallback callback) {
    onMapBoundsCallback = callback;
    webView.loadUrl("javascript:getBounds();");
  }

  @Override public void getScreenLocation(LatLng latLng, OnLatLngScreenLocationCallback callback) {
    onLatLngScreenLocationCallback = callback;
    webView.loadUrl(String.format("javascript:getScreenLocation(%1$f, %2$f);",
                                  latLng.latitude, latLng.longitude));
  }

  @Override public void setCenter(LatLngBounds bounds, int boundsPadding) {
    webView.loadUrl(String.format("javascript:setBounds(%1$f, %2$f, %3$f, %4$f);",
                                   bounds.northeast.latitude, bounds.northeast.longitude,
                                   bounds.southwest.latitude,
                                   bounds.southwest.longitude));
  }

  private class MapsJavaScriptInterface {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @JavascriptInterface public void onMapLoaded() {
      handler.post(new Runnable() {
        @Override
        public void run() {
          if (!loaded) {
            loaded = true;
            if (onMapLoadedListener != null) {
              onMapLoadedListener.onMapLoaded();
            }
          }
        }
      });
    }

    @JavascriptInterface public void mapClick(final double lat, final double lng) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          if (onMapClickListener != null) {
            onMapClickListener.onMapClick(new LatLng(lat, lng));
          }
          if (infoWindowView != null) {
            mLayout.removeView(infoWindowView);
          }
        }
      });
    }

    @JavascriptInterface public void getBoundsCallback(double neLat, double neLng, double swLat, double swLng) {
      final LatLngBounds bounds = new LatLngBounds(new LatLng(swLat, swLng),
                                                   new LatLng(neLat, neLng));
      handler.post(new Runnable() {
        @Override
        public void run() {
          onMapBoundsCallback.onMapBoundsReady(bounds);
        }
      });
    }

    @JavascriptInterface public void getLatLngScreenLocationCallback(int x, int y) {
      final Point point = new Point(x, y);
      handler.post(new Runnable() {
        @Override
        public void run() {
          onLatLngScreenLocationCallback.onLatLngScreenLocationReady(point);
        }
      });
    }

    @JavascriptInterface public void mapMove(double lat, double lng, int zoom) {
      center = new LatLng(lat, lng);
      WebViewMapFragment.this.zoom = zoom;

      handler.post(new Runnable() {
        @Override
        public void run() {
          if (onCameraChangeListener != null) {
            onCameraChangeListener.onCameraChanged(center, WebViewMapFragment.this.zoom);
          }

          if (ignoreNextMapMove) {
            ignoreNextMapMove = false;
            return;
          }

          if (infoWindowView != null) {
            mLayout.removeView(infoWindowView);
          }
        }
      });
    }

    @JavascriptInterface public void markerClick(final long markerId) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          if (onMapMarkerClickListener != null) {
            onMapMarkerClickListener.onMapMarkerClick(markerId);
          }

          if (infoWindowView != null) {
            mLayout.removeView(infoWindowView);
          }

          // TODO convert to custom dialog fragment
          if (infoWindowCreator != null) {
            infoWindowView = infoWindowCreator.createInfoWindow(markerId);
            int height = (int) getResources().getDimension(R.dimen.map_marker_height);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, height, Gravity.CENTER);
            layoutParams.bottomMargin = height;
            infoWindowView.setLayoutParams(layoutParams);
            mLayout.addView(infoWindowView);

            infoWindowView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(@NonNull View v) {
                mLayout.removeView(infoWindowView);
                if (onInfoWindowClickListener != null) {
                  onInfoWindowClickListener.onInfoWindowClick(markerId);
                }
              }
            });
          } else {
            webView.loadUrl(String.format("javascript:showDefaultInfoWindow(%1$d);", markerId));
          }

          ignoreNextMapMove = true;
        }
      });
    }

    @JavascriptInterface public void defaultInfoWindowClick(final long markerId) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          if(onInfoWindowClickListener != null){
            onInfoWindowClickListener.onInfoWindowClick(markerId);
          }
        }
      });
    }
  }
}