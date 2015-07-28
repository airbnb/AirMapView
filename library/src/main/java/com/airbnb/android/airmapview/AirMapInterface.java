package com.airbnb.android.airmapview;

import com.airbnb.android.airmapview.listeners.OnLatLngScreenLocationCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;

import com.airbnb.android.airmapview.listeners.InfoWindowCreator;
import com.airbnb.android.airmapview.listeners.OnCameraChangeListener;
import com.airbnb.android.airmapview.listeners.OnInfoWindowClickListener;
import com.airbnb.android.airmapview.listeners.OnMapBoundsCallback;
import com.airbnb.android.airmapview.listeners.OnMapClickListener;
import com.airbnb.android.airmapview.listeners.OnMapLoadedListener;
import com.airbnb.android.airmapview.listeners.OnMapMarkerClickListener;

public interface AirMapInterface {

  int CIRCLE_FILL_COLOR = 0x00D1C1;
  int CIRCLE_BORDER_COLOR = 0x000000;
  int CIRCLE_BORDER_WIDTH = 0;

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
   * @param listener {@link com.airbnb.android.airmapview.listeners.OnInfoWindowClickListener}
   *                 instance
   */
  void setOnInfoWindowClickListener(OnInfoWindowClickListener listener);

  /**
   * Specific to Google Play Services maps. Sets the {@link GoogleMap.InfoWindowAdapter} and {@link
   * com.airbnb.android.airmapview.listeners.InfoWindowCreator}
   */
  void setInfoWindowCreator(GoogleMap.InfoWindowAdapter adapter, InfoWindowCreator creator);

  /**
   * Draw a circle at the given LatLng, with the given radius
   */
  void drawCircle(LatLng latLng, int radius);

  /**
   * Draw a circle at the given LatLng, with the given radius and stroke width
   */
  void drawCircle(LatLng latLng, int radius, int borderColor);

  /**
   * Draw a circle at the given LatLng, with the given radius, stroke width, and stroke color
   */
  void drawCircle(LatLng latLng, int radius, int borderColor, int borderWidth);

  /**
   * Draw a circle at the given LatLng, with the given radius, stroke width, stroke and fill colors
   */
  void drawCircle(LatLng latLng, int radius, int borderColor, int borderWidth, int fillColor);

  /**
   * Returns the map screen bounds to the supplied
   * {@link com.airbnb.android.airmapview.listeners.OnMapBoundsCallback}
   */
  void getMapScreenBounds(OnMapBoundsCallback callback);

  /**
   * Returns the point coordinates of the LatLng in the container to the supplied
   * {@link OnLatLngScreenLocationCallback}
   */

  void getScreenLocation(LatLng latLng, OnLatLngScreenLocationCallback callback);

  /**
   * Sets the given {@link LatLngBounds} on the map with the specified padding
   */
  void setCenter(LatLngBounds latLngBounds, int boundsPadding);

  /**
   * Set the map zoom level
   */
  void setZoom(int zoom);

  /**
   * Animate the map to center the given {@link LatLng}. Web maps will currently only center the map
   * (no animation).
   */
  void animateCenter(LatLng latLng);

  /**
   * Center the map to the given {@link LatLng}
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
   */
  void setOnCameraChangeListener(OnCameraChangeListener onCameraChangeListener);

  void setOnMapLoadedListener(OnMapLoadedListener onMapLoadedListener);

  /**
   * Set the center of the map, and zoom level
   *
   * @param latLng the {@link LatLng} to set as center
   * @param zoom   the zoom level
   */
  void setCenterZoom(LatLng latLng, int zoom);

  /**
   * Animate the center of the map to the given location and zoom level
   *
   * @param latLng the {@link LatLng} to animate to center
   * @param zoom   the zoom level
   */
  void animateCenterZoom(LatLng latLng, int zoom);

  /**
   * Register a callback to be invoked when a map marker is clicked
   *
   * @param listener {@link com.airbnb.android.airmapview.listeners.OnMapMarkerClickListener}
   *                 callback
   */
  void setOnMarkerClickListener(OnMapMarkerClickListener listener);

  /**
   * Register a callback to be invoked when the map is clicked
   *
   * @param listener {@link com.airbnb.android.airmapview.listeners.OnMapClickListener} callback
   */
  void setOnMapClickListener(OnMapClickListener listener);

  /**
   * Set the map's padding. Currently only works with Google Play Services maps.
   */
  void setPadding(int left, int top, int right, int bottom);

  /**
   * Enable an indicator for the user's location on the map.
   */
  void setMyLocationEnabled(boolean enabled);

  /**
   * Check if the user location is being tracked and shown on te map.
   */
  boolean isMyLocationEnabled();

  /**
   * Enable a toolbar that displays various context-dependent actions.
   */
  void setMapToolbarEnabled(boolean enabled);

  /**
   * Construct a polyline with the given {@link LatLng} points
   */
  <T> void addPolyline(AirMapPolyline<T> polyline);

  /**
   * Remove the given {@link Polyline}
   *
   * @param polyline the {@link Polyline} to remove
   */
  void removePolyline(AirMapPolyline polyline);

  /** Sets the type of map tiles that should be displayed */
  void setMapType(MapType type);
}