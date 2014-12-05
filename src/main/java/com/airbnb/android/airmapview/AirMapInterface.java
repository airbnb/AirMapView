package com.airbnb.android.airmapview;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;

import java.util.List;

public interface AirMapInterface {

    void init();

    boolean isInitialized();

    void clearMarkers();

    void addMarker(AirMapMarker marker);

    void setOnInfoWindowClickListener(AirMapView.OnInfoWindowClickListener listener);

    public void setInfoWindowCreator(GoogleMap.InfoWindowAdapter adapter, AirMapView.InfoWindowCreator creator);

    void drawCircle(LatLng latLng, int radius);

    void getMapScreenBounds(AirMapView.OnMapBoundsCallback callback);

    void setCenter(LatLngBounds latLngBounds, int boundsPadding);

    void setZoom(int zoom);

    void animateCenter(LatLng latLng);

    void setCenter(LatLng latLng);

    LatLng getCenter();

    int getZoom();

    void setOnCameraChangeListener(WebViewMapFragment.OnCameraChangeListener onCameraChangeListener);

    void setOnMapLoadedListener(AirMapView.OnMapLoadedListener onMapLoadedListener);

    void setCenterZoom(LatLng latLng, int zoom);

    void animateCenterZoom(LatLng latLng, int zoom);

    void setOnMarkerClickListener(AirMapView.OnMapMarkerClickListener listener);

    void setOnMapClickListener(AirMapView.OnMapClickListener listener);

    void setPadding(int left, int top, int right, int bottom);

    void setMyLocationEnabled(boolean b);

    Polyline addPolyline(List<LatLng> points, int width, int color);

    void removePolyline(Polyline polyline);
}