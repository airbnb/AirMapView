package com.airbnb.android.airmapview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.airbnb.android.airmapview.listeners.InfoWindowCreator;
import com.airbnb.android.airmapview.listeners.OnCameraChangeListener;
import com.airbnb.android.airmapview.listeners.OnCameraMoveListener;
import com.airbnb.android.airmapview.listeners.OnInfoWindowClickListener;
import com.airbnb.android.airmapview.listeners.OnMapBoundsCallback;
import com.airbnb.android.airmapview.listeners.OnMapClickListener;
import com.airbnb.android.airmapview.listeners.OnMapInitializedListener;
import com.airbnb.android.airmapview.listeners.OnMapLoadedListener;
import com.airbnb.android.airmapview.listeners.OnMapMarkerClickListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

public class AirMapView extends FrameLayout
        implements OnCameraChangeListener, OnMapClickListener,
        OnMapMarkerClickListener, OnMapLoadedListener, OnInfoWindowClickListener {

    private static final int INVALID_ZOOM = -1;

    protected AirMapInterface mMapInterface;
    private OnCameraMoveListener mOnCameraMoveListener;
    private OnCameraChangeListener mOnCameraChangeListener;
    private boolean mOnCameraMoveTriggered;
    private OnMapInitializedListener mOnMapInitializedListener;
    private OnMapClickListener mOnMapClickListener;
    private OnMapMarkerClickListener mOnMapMarkerClickListener;
    private OnInfoWindowClickListener mOnInfoWIndowClickListener;

    public AirMapView(Context context) {
        super(context);
        inflateView();
    }

    public AirMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView();
    }

    public AirMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflateView();
    }

    private void inflateView() {
        LayoutInflater.from(getContext()).inflate(R.layout.map_view, this);
    }

    public void initialize(FragmentManager fragmentManager, AirMapInterface mapInterface) {
        if (mapInterface == null || fragmentManager == null) {
            throw new IllegalArgumentException("Either mapInterface or fragmentManager is null");
        }

        mMapInterface = mapInterface;
        mMapInterface.setOnMapLoadedListener(this);

        fragmentManager.beginTransaction()
                .replace(R.id.map_frame, (Fragment) mMapInterface)
                .commit();

        fragmentManager.executePendingTransactions();
    }

    /**
     * Used for initialization of the underlying map provider. Uses the default preferred map
     * provider (currently Native Google Maps, then Web Google Maps).
     *
     * @param fragmentManager required for initialization
     */
    public void initialize(FragmentManager fragmentManager) {
        AirMapInterface mapInterface = (AirMapInterface) fragmentManager.findFragmentById(R.id.map_frame);

        if (mapInterface != null) {
            initialize(fragmentManager, mapInterface);
        } else {
            initialize(fragmentManager, new DefaultAirMapViewBuilder(getContext()).builder().build());
        }
    }

    public void setOnMapInitializedListener(OnMapInitializedListener mapInitializedListener) {
        mOnMapInitializedListener = mapInitializedListener;
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
        mOnCameraChangeListener = onCameraChangeListener;
    }

    /**
     * Sets the map {@link com.airbnb.android.airmapview.listeners.OnCameraMoveListener}
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
        mOnMapMarkerClickListener = listener;
    }

    public void setOnMapClickListener(OnMapClickListener listener) {
        mOnMapClickListener = listener;
    }

    public void setInfoWindowAdapter(GoogleMap.InfoWindowAdapter adapter, InfoWindowCreator creator) {
        if (isInitialized()) {
            mMapInterface.setInfoWindowCreator(adapter, creator);
        }
    }

    public void setOnInfoWindowClickListener(OnInfoWindowClickListener listener) {
        mOnInfoWIndowClickListener = listener;
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

    @Override
    public void onCameraChanged(LatLng latLng, int zoom) {
        if (mOnCameraChangeListener != null) {
            mOnCameraChangeListener.onCameraChanged(latLng, zoom);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mOnMapClickListener != null) {
            mOnMapClickListener.onMapClick(latLng);
        }
    }

    @Override
    public void onMapMarkerClick(long id) {
        if (mOnMapMarkerClickListener != null) {
            mOnMapMarkerClickListener.onMapMarkerClick(id);
        }
    }

    @Override
    public void onMapMarkerClick(Marker marker) {
        if (mOnMapMarkerClickListener != null) {
            mOnMapMarkerClickListener.onMapMarkerClick(marker);
        }
    }

    @Override
    public void onMapLoaded() {
        if (isInitialized()) {
            mMapInterface.setOnCameraChangeListener(this);
            mMapInterface.setOnMapClickListener(this);
            mMapInterface.setOnMarkerClickListener(this);
            mMapInterface.setOnInfoWindowClickListener(this);

            if (mOnMapInitializedListener != null) {
                // only send map Initialized callback if map initialized successfully
                // initialization can fail if the map leaves the screen before it loads
                mOnMapInitializedListener.onMapInitialized();
            }
        }
    }

    @Override
    public void onInfoWindowClick(long id) {
        if (mOnInfoWIndowClickListener != null) {
            mOnInfoWIndowClickListener.onInfoWindowClick(id);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (mOnInfoWIndowClickListener != null) {
            mOnInfoWIndowClickListener.onInfoWindowClick(marker);
        }
    }
}
