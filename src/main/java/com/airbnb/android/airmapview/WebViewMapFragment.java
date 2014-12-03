package com.airbnb.android.airmapview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WebViewMapFragment extends Fragment implements AirMapInterface {

    private View mMarker; // TODO rename
    private static final String ARG_MAP_TYPE = "map_type";

    public static WebViewMapFragment newInstance(AirMapView.MapType mapType) {
        WebViewMapFragment f = new WebViewMapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MAP_TYPE, mapType.ordinal());
        f.setArguments(args);
        return f;
    }

    public interface OnCameraChangeListener {
        public void onCameraChanged(LatLng latLng, int zoom);
    }

    private WebView mWebView;
    private ViewGroup mLayout;

    private OnCameraChangeListener mOnCameraChangeListener;
    private AirMapView.OnMapLoadedListener mOnMapLoadedListener;
    private AirMapView.OnMapMarkerClickListener mOnMarkerClickListener;
    private AirMapView.OnInfoWindowClickListener mOnInfoWindowClickListener;
    private AirMapView.InfoWindowCreator mInfoWindowCreator;
    private AirMapView.OnMapBoundsCallback mMapBoundsCallback;

    private LatLng mCenter;
    private int mZoom;

    private boolean mLoaded;
    private boolean mIgnoreNextMapMove;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        mWebView = (WebView) view.findViewById(R.id.webview);
        mLayout = (ViewGroup) view;

        WebSettings webViewSettings = mWebView.getSettings();
        webViewSettings.setSupportZoom(true);
        webViewSettings.setBuiltInZoomControls(false);
        webViewSettings.setJavaScriptEnabled(true);

        AirMapView.MapType mapType = AirMapView.MapType.getState(getArguments().getInt(ARG_MAP_TYPE));

        String url = mapType.mUrl;
        String urlData = null;
        try {
            urlData = Utils.getStringFromFile(getResources(), url);
        } catch (IOException e) {
        }

        if (urlData == null) {
            // this should not happen
            return view;
        }

        urlData = urlData.replace("MAPURL", mapType.mMapUrl).replace("LANGTOKEN", Locale.getDefault().getLanguage()).replace("REGIONTOKEN", Locale.getDefault().getCountry());

        mWebView.loadDataWithBaseURL(mapType.mDomain, urlData, "text/html", "base64", null);

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
        mWebView.loadUrl(String.format("javascript:centerMap(%1$f, %2$f);", latLng.latitude, latLng.longitude));
    }

    public void setZoom(int zoom) {
        mWebView.loadUrl(String.format("javascript:setZoom(%1$d);", zoom));
    }

    public void drawCircle(LatLng latLng, int radius) {
        mWebView.loadUrl(String.format("javascript:addCircle(%1$f, %2$f, %3$d);", latLng.latitude, latLng.longitude, radius));
    }

    @Override
    public void init() {
        // no-op
    }

    @Override
    public boolean isInitialized() {
        return mWebView != null && mLoaded;
    }

    @Override
    public void addMarker(LatLng latLng, long id) {
        mWebView.loadUrl(String.format("javascript:addListingMarker(%1$f, %2$f, %3$d);", latLng.latitude, latLng.longitude, id));
    }

    public void highlightListingMarker(long prevListingId, long currListingId) {
        mWebView.loadUrl(String.format("javascript:highlightListingMarker(%1$d, %2$d);", prevListingId, currListingId));
    }

    @Override
    public void addMarker(LatLng latLng, int icon, String title) {
        mWebView.loadUrl(String.format("javascript:addMarker(%1$f, %2$f);", latLng.latitude, latLng.longitude));
    }

    public void clearMarkers() {
        mWebView.loadUrl("javascript:clearMarkers();");
    }

    public void setOnCameraChangeListener(OnCameraChangeListener listener) {
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

    @Override
    public Polyline addPolyline(List<LatLng> points, int width, int color) {
        try {
            JSONArray array = new JSONArray();
            for (LatLng point : points) {
                JSONObject json = new JSONObject();
                json.put("lat", point.latitude);
                json.put("lng", point.longitude);
                array.put(json);
            }

            mWebView.loadUrl(String.format("javascript:addPolyline(" + array.toString() + ", %1$d, %2$d);", width, color));
        } catch (JSONException e) {
        }

        return null;
    }

    @Override
    public void removePolyline(Polyline polyline) {
        mWebView.loadUrl(String.format("javascript:removeAllPolylines();"));
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
                    if (mMarker != null) {
                        mLayout.removeView(mMarker);
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

                    if (mMarker != null) {
                        mLayout.removeView(mMarker);
                    }
                }
            });
        }

        @JavascriptInterface
        public void markerClick(final long listingId) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (mOnMarkerClickListener != null) {
                        mOnMarkerClickListener.onMapMarkerClick(listingId);
                    }

                    if (mMarker != null) {
                        mLayout.removeView(mMarker);
                    }

                    // TODO convert to custom dialog fragment
                    if (mInfoWindowCreator != null) {
                        mMarker = mInfoWindowCreator.createInfoWindow(listingId);
                        int height = (int) getResources().getDimension(R.dimen.map_marker_height);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height, Gravity.CENTER);
                        layoutParams.bottomMargin = height;
                        mMarker.setLayoutParams(layoutParams);
                        mLayout.addView(mMarker);

                        mMarker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLayout.removeView(mMarker);
                                if (mOnInfoWindowClickListener != null) {
                                    mOnInfoWindowClickListener.onInfoWindowClick(listingId);
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