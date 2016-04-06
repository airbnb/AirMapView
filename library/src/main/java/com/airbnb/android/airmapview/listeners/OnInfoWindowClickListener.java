package com.airbnb.android.airmapview.listeners;

import com.airbnb.android.airmapview.AirMapMarker;

public interface OnInfoWindowClickListener {
  void onInfoWindowClick(AirMapMarker<?> airMarker);
}
