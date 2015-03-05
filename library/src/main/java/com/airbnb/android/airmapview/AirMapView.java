package com.airbnb.android.airmapview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

public class AirMapView extends FrameLayout {

    private static final int INVALID_ZOOM = -1;

    protected AirMapInterface mMapInterface;
    private boolean mZOrderOnTop;
    private OnCameraChangeListener mOnCameraChangeListener;

    public AirMapView(Context context) {
        super(context);
        init();
    }

    public AirMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AirMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.map_view, this);
    }

    /**
     * Used for initialization of the underlying map provider
     *
     * @param fragmentManager        required for initialization
     * @param zOrderOnTop            only is used for Google Play Services maps
     * @param mapInitializedListener required {@link OnMapInitializedListener} callback
     *                               so the calling class knows when the map has been initialized
     */
    public void onCreateView(FragmentManager fragmentManager, boolean zOrderOnTop,
                             final OnMapInitializedListener mapInitializedListener) {
        if (mapInitializedListener == null) {
            throw new IllegalArgumentException("mapInitializedListener must not be null");
        }

        mMapInterface = (AirMapInterface) fragmentManager.findFragmentById(R.id.map_frame);
        mZOrderOnTop = zOrderOnTop;

        if (mMapInterface == null) {
            mMapInterface = createMapFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.map_frame, (Fragment) mMapInterface)
                    .commit();

            fragmentManager.executePendingTransactions();
        }

        mMapInterface.setOnMapLoadedListener(new OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                if (isInitialized()) {
                    // only send map Initialized callback if map initialized successfully
                    // initialization can fail if the map leaves the screen before it loads

                    mapInitializedListener.onMapInitialized();

                    if (mOnCameraChangeListener != null) {
                        mMapInterface.setOnCameraChangeListener(mOnCameraChangeListener);
                    }
                }
            }
        });
    }

    public void setOnCameraChangeListener(OnCameraChangeListener onCameraChangeListener) {
        mOnCameraChangeListener = onCameraChangeListener;
    }

    public final AirMapInterface getMapInterface() {
        return mMapInterface;
    }

    /**
     * Override this if you wish to add more map types
     */
    protected AirMapInterface createMapFragment() {
//        if (ConnectionResult.SUCCESS == GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext())) {
//            return AirGoogleMapFragment.newInstance(new GoogleMapOptions().zOrderOnTop(mZOrderOnTop));
//        }
        return new DefaultAirMapViewBuilder(getContext()).builder().build();
    }

    public void onDestroyView() {
        if (isInitialized()) {
            mMapInterface.setMyLocationEnabled(false);
        }
    }

    public int getZoom() {
        if (isInitialized()) {
            return mMapInterface.getZoom();
        }

        return INVALID_ZOOM;
    }

    public LatLng getCenter() {
        if (isInitialized()) {
            return mMapInterface.getCenter();
        }
        return null;
    }

    public boolean setCenter(LatLng latLng) {
        if (isInitialized()) {
            mMapInterface.setCenter(latLng);
            return true;
        }
        return false;
    }

    public boolean animateCenter(LatLng latLng) {
        if (isInitialized()) {
            mMapInterface.animateCenter(latLng);
            return true;
        }
        return false;
    }

    public boolean setZoom(int zoom) {
        if (isInitialized()) {
            mMapInterface.setZoom(zoom);
            return true;
        }
        return false;
    }

    public boolean setCenterZoom(LatLng latLng, int zoom) {
        if (isInitialized()) {
            mMapInterface.setCenterZoom(latLng, zoom);
            return true;
        }
        return false;
    }

    public boolean animateCenterZoom(LatLng latLng, int zoom) {
        if (isInitialized()) {
            mMapInterface.animateCenterZoom(latLng, zoom);
            return true;
        }
        return false;
    }

    public boolean setBounds(LatLngBounds latLngBounds, int boundsPadding) {
        if (isInitialized()) {
            mMapInterface.setCenter(latLngBounds, boundsPadding);
            return true;
        }
        return false;
    }

    public void getScreenBounds(OnMapBoundsCallback callback) {
        if (isInitialized()) {
            mMapInterface.getMapScreenBounds(callback);
        }
    }

    public void drawCircle(LatLng latLng, int radius) {
        if (isInitialized()) {
            mMapInterface.drawCircle(latLng, radius);
        }
    }

    public void drawCircle(LatLng latLng, int radius, int strokeColor) {
        if (isInitialized()) {
            mMapInterface.drawCircle(latLng, radius, strokeColor);
        }
    }

    public void drawCircle(LatLng latLng, int radius, int strokeColor, int strokeWidth) {
        if (isInitialized()) {
            mMapInterface.drawCircle(latLng, radius, strokeColor, strokeWidth);
        }
    }

    public void drawCircle(LatLng latLng, int radius, int strokeColor, int strokeWidth, int fillColor) {
        if (isInitialized()) {
            mMapInterface.drawCircle(latLng, radius, strokeColor, strokeWidth, fillColor);
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (isInitialized()) {
            mMapInterface.setPadding(left, top, right, bottom);
        }
    }

    public void setOnMarkerClickListener(OnMapMarkerClickListener listener) {
        if (isInitialized()) {
            mMapInterface.setOnMarkerClickListener(listener);
        }
    }

    public void setOnMapClickListener(OnMapClickListener listener) {
        if (isInitialized()) {
            mMapInterface.setOnMapClickListener(listener);
        }
    }

    public void setInfoWindowAdapter(GoogleMap.InfoWindowAdapter adapter, InfoWindowCreator creator) {
        if (isInitialized()) {
            mMapInterface.setInfoWindowCreator(adapter, creator);
        }
    }

    public void setOnInfoWindowClickListener(OnInfoWindowClickListener listener) {
        if (isInitialized()) {
            mMapInterface.setOnInfoWindowClickListener(listener);
        }
    }

    public void clearMarkers() {
        if (isInitialized()) {
            mMapInterface.clearMarkers();
        }
    }

    public boolean addPolyline(AirMapPolyline polyline) {
        if (isInitialized()) {
            mMapInterface.addPolyline(polyline);
            return true;
        }
        return false;
    }

    public boolean removePolyline(AirMapPolyline polyline) {
        if (isInitialized()) {
            mMapInterface.removePolyline(polyline);
            return true;
        }
        return false;
    }

    public boolean isInitialized() {
        return mMapInterface != null && mMapInterface.isInitialized();
    }

    public boolean addMarker(AirMapMarker marker) {
        if (isInitialized()) {
            mMapInterface.addMarker(marker);
            return true;
        }
        return false;
    }

    public interface OnMapInitializedListener {
        void onMapInitialized();
    }

    public interface OnMapLoadedListener {
        void onMapLoaded();
    }

    public interface OnMapMarkerClickListener {
        void onMapMarkerClick(long id);

        void onMapMarkerClick(Marker marker);
    }

    public interface OnMapClickListener {
        void onMapClick(LatLng latLng);
    }

    public interface OnInfoWindowClickListener {
        void onInfoWindowClick(long id);

        void onInfoWindowClick(Marker marker);
    }

    public interface InfoWindowCreator {
        View createInfoWindow(long id);
    }

    public interface OnMapBoundsCallback {
        void onMapBoundsReady(LatLngBounds bounds);
    }

    public interface OnCameraChangeListener {
        void onCameraChanged(LatLng latLng, int zoom);
    }
}
