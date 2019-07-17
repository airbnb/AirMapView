package com.airbnb.android.airmapview.listeners;

import com.airbnb.android.airmapview.AirMapMarker;

public interface OnMapMarkerClickListener {
  void onMapMarkerClick(AirMapMarker<?> airMarker);

  boolean onMakerClickEventIsHandled();
}
