package com.airbnb.android.airmapview.listeners;

/**
 * This event is triggered once as soon as the map camera starts moving and then is not triggered
 * again until the next time the user moves the map camera again. This is handled by AirMapView
 * instead of the actual GoogleMap implementation since this is not supported by them.
 */
public interface OnCameraMoveListener {
  void onCameraMove();
}
