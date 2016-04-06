package com.airbnb.android.airmapview.listeners;

import com.google.android.gms.maps.model.LatLngBounds;

public interface OnMapBoundsCallback {
  void onMapBoundsReady(LatLngBounds bounds);
}
