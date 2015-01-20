package com.airbnb.android.airmapview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class WebViewMapFragment extends Fragment implements AirMapInterface {

    private static final String ARG_MAP_DOMAIN = "map_domain";
    private static final String ARG_FILE_NAME = "map_file_name";
    private static final String ARG_MAP_URL = "map_url";
    private static final String TAG = WebViewMapFragment.class.getSimpleName();

    public static WebViewMapFragment newInstance(AirMapType mapType) {
        WebViewMapFragment f = new WebViewMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MAP_DOMAIN, mapType.getDomain());
        args.putString(ARG_MAP_URL, mapType.getMapUrl());
        args.putString(ARG_FILE_NAME, mapType.getFileName());
        f.setArguments(args);
        return f;
    }

    private WebView mWebView;
    private ViewGroup mLayout;
    private AirMapView.OnCameraChangeListener mOnCameraChangeListener;
    private AirMapView.OnMapLoadedListener mOnMapLoadedListener;
    private AirMapView.OnMapMarkerClickListener mOnMarkerClickListener;
    private AirMapView.OnInfoWindowClickListener mOnInfoWindowClickListener;
    private AirMapView.InfoWindowCreator mInfoWindowCreator;
    private AirMapView.OnMapBoundsCallback mMapBoundsCallback;
    private LatLng mCenter;
    private int mZoom;
    private boolean mLoaded;
    private boolean mIgnoreNextMapMove;
    private View mInfoWindowView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        mWebView = (WebView) view.findViewById(R.id.webview);
        mLayout = (ViewGroup) view;

        WebSettings webViewSettings = mWebView.getSettings();
        webViewSettings.setSupportZoom(true);
        webViewSettings.setBuiltInZoomControls(false);
        webViewSettings.setJavaScriptEnabled(true);

        Bundle args = getArguments();
        String fileName = args.getString(ARG_FILE_NAME, "");
        String mapUrl = args.getString(ARG_MAP_URL, "");
        String mapDomain = args.getString(ARG_MAP_DOMAIN, "");

        String urlData = AirMapUtils.getStringFromFile(getResources(), fileName)
                .replace("MAPURL", mapUrl)
                .replace("LANGTOKEN", Locale.getDefault().getLanguage())
                .replace("REGIONTOKEN", Locale.getDefault().getCountry());

        mWebView.loadDataWithBaseURL(mapDomain, urlData, "text/html", "base64", null);

        mWebView.addJavascriptInterface(new MapsJavaScriptInterface(getActivity()), "Android");

        return view;
    }

    public int getZoom() {
        return mZoom;
    }

    public LatLng getCenter() {
        return mCenter;
    }

    public void setCenter(LatLng latLng) {
        mWebView.loadUrl(String.format("javascript:centerMap(%1$f, %2$f);", latLng.latitude, latLng.longitude));
    }

    public void animateCenter(LatLng latLng) {
        setCenter(latLng);
    }

    public void setZoom(int zoom) {
        mWebView.loadUrl(String.format("javascript:setZoom(%1$d);", zoom));
    }

    public void drawCircle(LatLng latLng, int radius) {
        drawCircle(latLng, radius, CIRCLE_BORDER_COLOR);
    }

    @Override
    public void drawCircle(LatLng latLng, int radius, int borderColor) {
        drawCircle(latLng, radius, borderColor, CIRCLE_BORDER_WIDTH);
    }

    @Override
    public void drawCircle(LatLng latLng, int radius, int borderColor, int borderWidth) {
        drawCircle(latLng, radius, borderColor, borderWidth, CIRCLE_FILL_COLOR);
    }

    @Override
    public void drawCircle(LatLng latLng, int radius, int borderColor, int borderWidth, int fillColor) {
        mWebView.loadUrl(String.format("javascript:addCircle(%1$f, %2$f, %3$d, %4$d, %5$d, %6$d);", latLng.latitude, latLng.longitude, radius, borderColor, borderWidth, fillColor));
    }

    public void highlightMarker(long markerId) {
        if (markerId != -1) {
            mWebView.loadUrl(String.format("javascript:highlightMarker(%1$d);", markerId));
        }
    }

    public void unhighlightMarker(long markerId) {
        if (markerId != -1) {
            mWebView.loadUrl(String.format("javascript:unhighlightMarker(%1$d);", markerId));
        }
    }

    @Override
    public boolean isInitialized() {
        return mWebView != null && mLoaded;
    }

    @Override
    public void addMarker(AirMapMarker marker) {
        LatLng latLng = marker.getLatLng();
        mWebView.loadUrl(String.format("javascript:addMarkerWithId(%1$f, %2$f, %3$d);", latLng.latitude, latLng.longitude, marker.getId()));
    }

    @Override
    public void removeMarker(AirMapMarker marker) {
        mWebView.loadUrl(String.format("javascript:removeMarker(%1$d);", marker.getId()));
    }

    public void clearMarkers() {
        mWebView.loadUrl("javascript:clearMarkers();");
    }

    public void setOnCameraChangeListener(AirMapView.OnCameraChangeListener listener) {
        mOnCameraChangeListener = listener;
    }

    public void setOnMapLoadedListener(AirMapView.OnMapLoadedListener listener) {
        mOnMapLoadedListener = listener;
        if (mLoaded) {
            mOnMapLoadedListener.onMapLoaded();
        }
    }

    @Override
    public void setCenterZoom(LatLng latLng, int zoom) {
        setCenter(latLng);
        setZoom(zoom);
    }

    @Override
    public void animateCenterZoom(LatLng latLng, int zoom) {
        setCenterZoom(latLng, zoom);
    }

    public void setOnMarkerClickListener(AirMapView.OnMapMarkerClickListener listener) {
        mOnMarkerClickListener = listener;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        // no-op
    }

    @Override
    public void setMyLocationEnabled(boolean b) {
        // no-op
    }

    @Override
    public void addPolyline(AirMapPolyline polyline) {
        try {
            JSONArray array = new JSONArray();
            for (LatLng point : (List<LatLng>)polyline.getPoints()) {
                JSONObject json = new JSONObject();
                json.put("lat", point.latitude);
                json.put("lng", point.longitude);
                array.put(json);
            }

            mWebView.loadUrl(String.format("javascript:addPolyline(" + array.toString() + ", %1$d, %2$d, %3$d);", polyline.getId(), polyline.getStrokeWidth(), polyline.getStrokeColor()));
        } catch (JSONException e) {
            Log.e(TAG, "error constructing polyline JSON", e);
        }
    }

    @Override
    public void removePolyline(AirMapPolyline polyline) {
        mWebView.loadUrl(String.format("javascript:removePolyline(%1$d);", polyline.getId()));
    }

    @Override
    public void setOnMapClickListener(final AirMapView.OnMapClickListener listener) {
        final GestureDetector clickDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                listener.onMapClick(null);
                return super.onSingleTapUp(e);
            }
        });

        mWebView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return clickDetector.onTouchEvent(event);
            }
        });
    }

    public void setOnInfoWindowClickListener(AirMapView.OnInfoWindowClickListener listener) {
        mOnInfoWindowClickListener = listener;
    }

    @Override
    public void setInfoWindowCreator(GoogleMap.InfoWindowAdapter adapter, AirMapView.InfoWindowCreator creator) {
        mInfoWindowCreator = creator;
    }

    public void getMapScreenBounds(AirMapView.OnMapBoundsCallback callback) {
        mMapBoundsCallback = callback;
        mWebView.loadUrl("javascript:getBounds();");
    }

    @Override
    public void setCenter(LatLngBounds bounds, int boundsPadding) {
        mWebView.loadUrl(String.format("javascript:setBounds(%1$f, %2$f, %3$f, %4$f);", bounds.northeast.latitude, bounds.northeast.longitude, bounds.southwest.latitude, bounds.southwest.longitude));
    }

    private class MapsJavaScriptInterface {

        Context mContext;

        public MapsJavaScriptInterface(Context c) {
            mContext = c;
        }


        @JavascriptInterface
        public void onMapLoaded() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (!mLoaded) {
                        mLoaded = true;
                        if (mOnMapLoadedListener != null){
                            mOnMapLoadedListener.onMapLoaded();
                        }
                    }
                }
            });
        }

        @JavascriptInterface
        public void mapClick() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (mInfoWindowView != null) {
                        mLayout.removeView(mInfoWindowView);
                    }
                }
            });
        }

        @JavascriptInterface
        public void getBoundsCallback(double neLat, double neLng, double swLat, double swLng) {
            final LatLngBounds bounds = new LatLngBounds(new LatLng(swLat, swLng), new LatLng(neLat, neLng));
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mMapBoundsCallback.onMapBoundsReady(bounds);
                }
            });
        }

        @JavascriptInterface
        public void mapMove(double lat, double lng, int zoom) {
            mCenter = new LatLng(lat, lng);
            mZoom = zoom;

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (mOnCameraChangeListener != null) {
                        mOnCameraChangeListener.onCameraChanged(mCenter, mZoom);
                    }

                    if (mIgnoreNextMapMove) {
                        mIgnoreNextMapMove = false;
                        return;
                    }

                    if (mInfoWindowView != null) {
                        mLayout.removeView(mInfoWindowView);
                    }
                }
            });
        }

        @JavascriptInterface
        public void markerClick(final long markerId) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (mOnMarkerClickListener != null) {
                        mOnMarkerClickListener.onMapMarkerClick(markerId);
                    }

                    if (mInfoWindowView != null) {
                        mLayout.removeView(mInfoWindowView);
                    }

                    // TODO convert to custom dialog fragment
                    if (mInfoWindowCreator != null) {
                        mInfoWindowView = mInfoWindowCreator.createInfoWindow(markerId);
                        int height = (int) getResources().getDimension(R.dimen.map_marker_height);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height, Gravity.CENTER);
                        layoutParams.bottomMargin = height;
                        mInfoWindowView.setLayoutParams(layoutParams);
                        mLayout.addView(mInfoWindowView);

                        mInfoWindowView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLayout.removeView(mInfoWindowView);
                                if (mOnInfoWindowClickListener != null) {
                                    mOnInfoWindowClickListener.onInfoWindowClick(markerId);
                                }
                            }
                        });
                    }

                    mIgnoreNextMapMove = true;
                }
            });

        }
    }
}