package com.airbnb.android.airmapview;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

public class AirGoogleMapFragment extends SupportMapFragment implements AirMapInterface {

    private static final int CIRCLE_STROKE_WIDTH = 5;

    private GoogleMap mGoogleMap;
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutListener;
    private AirMapView.OnMapLoadedListener mOnMapLoadedListener;
    private int mCircleFillColor = Color.BLUE & 0xAA000000;
    private int mCircleBorderColor = Color.BLUE;

    public static AirGoogleMapFragment newInstance(GoogleMapOptions options) {
        AirGoogleMapFragment f = new AirGoogleMapFragment();
        Bundle args = new Bundle();
        // this is internal to SupportMapFragment
        args.putParcelable("MapOptions", options);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (mOnMapLoadedListener != null) {
                    mOnMapLoadedListener.onMapLoaded();
                }
            }
        };
        v.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutListener);

        if (savedInstanceState != null) {
            init();
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getView().getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutListener);
    }

    @Override
    public void init() {
        mGoogleMap = getMap();

        if (mGoogleMap != null) {
            UiSettings settings = mGoogleMap.getUiSettings();
            settings.setZoomControlsEnabled(false);
            settings.setMyLocationButtonEnabled(false);
            setMyLocationEnabled(true);
        }
    }

    public void setCircleBorderColor(int color) {
        mCircleBorderColor = color;
    }

    public void setCircleFillColor(int color) {
        mCircleFillColor = color;
    }

    @Override
    public boolean isInitialized() {
        return mGoogleMap != null;
    }

    @Override
    public void clearMarkers() {
        mGoogleMap.clear();
    }

    @Override
    public void addMarker(AirMapMarker airMarker) {
        airMarker.addToGoogleMap(mGoogleMap);
    }

    @Override
    public void removeMarker(AirMapMarker marker) {
        marker.removeFromGoogleMap();
    }

    @Override
    public void setOnInfoWindowClickListener(final AirMapView.OnInfoWindowClickListener listener) {
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                listener.onInfoWindowClick(marker);
            }
        });
    }

    @Override
    public void setInfoWindowCreator(GoogleMap.InfoWindowAdapter adapter, AirMapView.InfoWindowCreator creator) {
        mGoogleMap.setInfoWindowAdapter(adapter);
    }

    @Override
    public void drawCircle(LatLng latLng, int radius) {
        drawCircle(latLng, radius, CIRCLE_STROKE_WIDTH);
    }

    @Override
    public void drawCircle(LatLng latLng, int radius, int strokeWidth) {
        drawCircle(latLng, radius, strokeWidth, CIRCLE_STROKE_COLOR);
    }

    @Override
    public void drawCircle(LatLng latLng, int radius, int strokeWidth, int strokeColor) {
        drawCircle(latLng, radius, strokeWidth, strokeColor, CIRCLE_FILL_COLOR);
    }

    @Override
    public void drawCircle(LatLng latLng, int radius, int strokeWidth, int strokeColor, int fillColor) {
        mGoogleMap.addCircle(new CircleOptions()
                .center(latLng)
                .strokeColor(mCircleBorderColor)
                .strokeWidth(CIRCLE_STROKE_WIDTH)
                .fillColor(mCircleFillColor)
                .radius(radius));
    }

    @Override
    public void getMapScreenBounds(AirMapView.OnMapBoundsCallback callback) {
        final Projection projection = mGoogleMap.getProjection();
        int hOffset = getResources().getDimensionPixelOffset(R.dimen.map_horizontal_padding);
        int vOffset = getResources().getDimensionPixelOffset(R.dimen.map_vertical_padding);

        LatLngBounds.Builder builder = LatLngBounds.builder();
        builder.include(projection.fromScreenLocation(new Point(hOffset, vOffset))); // top-left
        builder.include(projection.fromScreenLocation(new Point(getView().getWidth() - hOffset, vOffset))); // top-right
        builder.include(projection.fromScreenLocation(new Point(hOffset, getView().getHeight() - vOffset))); // bottom-left
        builder.include(projection.fromScreenLocation(new Point(getView().getWidth() - hOffset, getView().getHeight() - vOffset))); // bottom-right

        callback.onMapBoundsReady(builder.build());
    }

    @Override
    public void setCenter(LatLngBounds latLngBounds, int boundsPadding) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, boundsPadding));
    }

    @Override
    public void setZoom(int zoom) {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mGoogleMap.getCameraPosition().target, zoom));
    }

    @Override
    public void animateCenter(LatLng latLng) {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void setCenter(LatLng latLng) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public LatLng getCenter() {
        return mGoogleMap.getCameraPosition().target;
    }

    @Override
    public int getZoom() {
        return (int) mGoogleMap.getCameraPosition().zoom;
    }

    @Override
    public void setOnCameraChangeListener(final AirMapView.OnCameraChangeListener onCameraChangeListener) {
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                // camera change can occur programatically.
                if (isResumed()) {
                    onCameraChangeListener.onCameraChanged(cameraPosition.target, (int) cameraPosition.zoom);
                }
            }
        });
    }

    @Override
    public void setOnMapLoadedListener(AirMapView.OnMapLoadedListener onMapLoadedListener) {
        mOnMapLoadedListener = onMapLoadedListener;
    }

    @Override
    public void setCenterZoom(LatLng latLng, int zoom) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void animateCenterZoom(LatLng latLng, int zoom) {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void setOnMarkerClickListener(final AirMapView.OnMapMarkerClickListener listener) {
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                listener.onMapMarkerClick(marker);
                return false;
            }
        });
    }

    @Override
    public void setOnMapClickListener(final AirMapView.OnMapClickListener listener) {
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                listener.onMapClick(latLng);
            }
        });
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mGoogleMap.setPadding(left, top, right, bottom);
    }

    @Override
    public void setMyLocationEnabled(boolean enabled) {
        mGoogleMap.setMyLocationEnabled(enabled);
    }

    @Override
    public void addPolyline(AirMapPolyline polyline) {
        polyline.addToGoogleMap(mGoogleMap);
    }

    @Override
    public void removePolyline(AirMapPolyline polyline) {
        polyline.removeFromGoogleMap();
    }

    /**
     * This method will return the google map if initialized. Will return null otherwise
     *
     * @return returns google map if initialized
     */
    public GoogleMap getGoogleMap() {
        return mGoogleMap;
    }
}
