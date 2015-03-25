package com.airbnb.android.airmapview.listeners;

import com.google.android.gms.maps.model.Marker;

public interface OnMapMarkerClickListener {

  void onMapMarkerClick(long id);

  void onMapMarkerClick(Marker marker);
}
