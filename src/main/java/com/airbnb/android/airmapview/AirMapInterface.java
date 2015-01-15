package com.airbnb.android.airmapview;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;

public interface AirMapInterface {

    static final int CIRCLE_FILL_COLOR = 0x00D1C1;
    static final int CIRCLE_STROKE_COLOR = 0x000000;
    static final int CIRCLE_STROKE_WIDTH = 0;

    /**
     * @return true if the map is fully loaded/initialized.
     */
    boolean isInitialized();

    /**
     * Clear all markers from the map
     */
    void clearMarkers();

    /**
     * Add the given marker to the map
     *
     * @param marker {@Link AirMapMarker} instance to add
     */
    void addMarker(AirMapMarker marker);

    /**
     * Remove the given marker from the map
     *
     * @param marker {@Link AirMapMarker} instance to remove
     */
    void removeMarker(AirMapMarker marker);

    /**
     * Set the callback for info window click events
     *
     * @param listener {@link AirMapView.OnInfoWindowClickListener} instance
     */
    void setOnInfoWindowClickListener(AirMapView.OnInfoWindowClickListener listener);

    /**
     * Specific to Google Play Services maps. Sets the {@link GoogleMap.InfoWindowAdapter} and {@link AirMapView.InfoWindowCreator}
     */
    public void setInfoWindowCreator(GoogleMap.InfoWindowAdapter adapter, AirMapView.InfoWindowCreator creator);

    /**
     * Draw a circle at the given LatLng, with the given radius
     *
     * @param latLng
     * @param radius
     */
    void drawCircle(LatLng latLng, int radius);

    /**
     * Draw a circle at the given LatLng, with the given radius and stroke width
     *
     * @param latLng
     * @param radius
     * @param strokeWidth
     */
    void drawCircle(LatLng latLng, int radius, int strokeWidth);

    /**
     * Draw a circle at the given LatLng, with the given radius, stroke width, and stroke color
     *
     * @param latLng
     * @param radius
     * @param strokeWidth
     * @param strokeColor
     */
    void drawCircle(LatLng latLng, int radius, int strokeWidth, int strokeColor);

    /**
     * Draw a circle at the given LatLng, with the given radius, stroke width, stroke and fill colors
     *
     * @param latLng
     * @param radius
     * @param strokeWidth
     * @param strokeColor
     * @param fillColor
     */
    void drawCircle(LatLng latLng, int radius, int strokeWidth, int strokeColor, int fillColor);

    /**
     * Returns the map screen bounds to the supplied {@link AirMapView.OnMapBoundsCallback}
     *
     * @param callback
     */
    void getMapScreenBounds(AirMapView.OnMapBoundsCallback callback);

    /**
     * Sets the given {@link LatLngBounds} on the map with the specified padding
     *
     * @param latLngBounds
     * @param boundsPadding
     */
    void setCenter(LatLngBounds latLngBounds, int boundsPadding);

    /**
     * Set the map zoom level
     *
     * @param zoom
     */
    void setZoom(int zoom);

    /**
     * Animate the map to center the given {@link LatLng}.
     * Web maps will currently only center the map (no animation).
     *
     * @param latLng
     */
    void animateCenter(LatLng latLng);

    /**
     * Center the map to the given {@link LatLng}
     *
     * @param latLng
     */
    void setCenter(LatLng latLng);

    /**
     * @return {@link LatLng} of the center of the map
     */
    LatLng getCenter();

    /**
     * @return the zoom level of the map
     */
    int getZoom();

    /**
     * Register a callback to be invoked when the camera of the map has changed
     *
     * @param onCameraChangeListener
     */
    void setOnCameraChangeListener(AirMapView.OnCameraChangeListener onCameraChangeListener);

    void setOnMapLoadedListener(AirMapView.OnMapLoadedListener onMapLoadedListener);

    /**
     * Set the center of the map, and zoom level
     *
     * @param latLng the {@link LatLng} to set as center
     * @param zoom the zoom level
     */
    void setCenterZoom(LatLng latLng, int zoom);

    /**
     * Animate the center of the map to the given location and zoom level
     *
     * @param latLng the {@link LatLng} to animate to center
     * @param zoom the zoom level
     */
    void animateCenterZoom(LatLng latLng, int zoom);

    /**
     * Register a callback to be invoked when a map marker is clicked
     *
     * @param listener {@link AirMapView.OnMapMarkerClickListener} callback
     */
    void setOnMarkerClickListener(AirMapView.OnMapMarkerClickListener listener);

    /**
     * Register a callback to be invoked when the map is clicked
     *
     * @param listener {@link AirMapView.OnMapClickListener} callback
     */
    void setOnMapClickListener(AirMapView.OnMapClickListener listener);

    /**
     * Set the map's padding.
     * Currently only works with Google Play Services maps.
     */
    void setPadding(int left, int top, int right, int bottom);

    /**
     * Enable an indicator for the user's location on the map.
     * This is currently only supported on Google Play Services maps
     */
    void setMyLocationEnabled(boolean enabled);

    /**
     * Construct a polyline with the given {@link LatLng} points
     *
     * @param {@link AirMapPolyline} instance
     */
    void addPolyline(AirMapPolyline polyline);

    /**
     * Remove the given {@link Polyline}
     *
     * @param polyline the {@link Polyline} to remove
     */
    void removePolyline(AirMapPolyline polyline);
}