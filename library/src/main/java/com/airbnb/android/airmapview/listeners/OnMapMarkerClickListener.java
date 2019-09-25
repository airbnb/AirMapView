package com.airbnb.android.airmapview.listeners;

import com.airbnb.android.airmapview.AirMapMarker;

public interface OnMapMarkerClickListener {

  /*
  * Called when an airMarker has been clicked or tapped.
  * Return true if the listener has consumed the event (i.e., the default behavior should not occur);
  * false otherwise (i.e., the default behavior should occur).
  * The default behavior is for the camera to move to the marker and an info window to appear.
  * See: https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.OnMarkerClickListener
  * */
  boolean onMapMarkerClick(AirMapMarker<?> airMarker);
}
