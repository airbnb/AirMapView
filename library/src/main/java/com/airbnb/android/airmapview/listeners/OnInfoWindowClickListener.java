package com.airbnb.android.airmapview.listeners;

import com.google.android.gms.maps.model.Marker;

public interface OnInfoWindowClickListener {

  void onInfoWindowClick(long id);

  void onInfoWindowClick(Marker marker);
}
