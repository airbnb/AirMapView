package com.airbnb.android.airmapview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

public class AirMapView extends FrameLayout {

    private static final int INVALID_ZOOM = -1;

    protected AirMapInterface mMapInterface;
    private OnCameraMoveListener mOnCameraMoveListener;
    private boolean mOnCameraMoveTriggered;

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
        mMapInterface = new DefaultAirMapViewBuilder(getContext()).builder().build();
    }

    public void setMapInterface(FragmentManager fragmentManager, AirMapInterface mapInterface) {
        mMapInterface = mapInterface;

        fragmentManager.beginTransaction()
                .replace(R.id.map_frame, (Fragment) mMapInterface)
                .commit();

        fragmentManager.executePendingTransactions();
    }

    /**
     * Used for initialization of the underlying map provider
     *
     * @param fragmentManager        required for initialization
     * @param mapInitializedListener required {@link OnMapInitializedListener} callback
     *                               so the calling class knows when the map has been initialized
     */
    public void onCreateView(FragmentManager fragmentManager,
                             final OnMapInitializedListener mapInitializedListener) {
        if (mapInitializedListener == null) {
            throw new IllegalArgumentException("mapInitializedListener must not be null");
        }

        AirMapInterface mapInterface = (AirMapInterface) fragmentManager.findFragmentById(R.id.map_frame);

        if (mapInterface != null) {
            mMapInterface = mapInterface;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.map_frame, (Fragment) mMapInterface)
                .commit();

        fragmentManager.executePendingTransactions();

        mMapInterface.setOnMapLoadedListener(new OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                if (isInitialized()) {
                    // only send map Initialized callback if map initialized successfully
                    // initialization can fail if the map leaves the screen before it loads

                    mapInitializedListener.onMapInitialized();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (mOnCameraMoveListener != null && !mOnCameraMoveTriggered) {
                mOnCameraMoveListener.onCameraMove();
                mOnCameraMoveTriggered = true;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            mOnCameraMoveTriggered = false;
        }

        return super.dispatchTouchEvent(ev);
    }

    public void setOnCameraChangeListener(OnCameraChangeListener onCameraChangeListener) {
        if (isInitialized()) {
            mMapInterface.setOnCameraChangeListener(onCameraChangeListener);
        }
    }

    /**
     * Sets the map {@link com.airbnb.android.airmapview.AirMapView.OnCameraMoveListener}
     *
     * @param onCameraMoveListener The OnCameraMoveListener to be set
     */
    public void setOnCameraMoveListener(OnCameraMoveListener onCameraMoveListener) {
        mOnCameraMoveListener = onCameraMoveListener;
    }

    public final AirMapInterface getMapInterface() {
        return mMapInterface;
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

    /**
     * This event is triggered once as soon as the map camera starts moving and then is not triggered
     * again until the next time the user moves the map camera again.
     * This is handled by AirMapView instead of the actual GoogleMap implementation since this is not
     * supported by them.
     */
    public interface OnCameraMoveListener {
        void onCameraMove();
    }
}
