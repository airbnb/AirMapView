package com.airbnb.android.airmapview.listeners;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public interface OnMapMarkerDragListener {
  void onMapMarkerDragStart(Marker marker);

  void onMapMarkerDrag(Marker marker);

  void onMapMarkerDragEnd(Marker marker);

  void onMapMarkerDragStart(long id, LatLng latLng);

  void onMapMarkerDrag(long id, LatLng latLng);

  void onMapMarkerDragEnd(long id, LatLng latLng);
}
