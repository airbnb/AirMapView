package com.airbnb.android.airmapview.listeners;

import com.google.android.gms.maps.model.LatLng;

public interface OnCameraChangeListener {

  void onCameraChanged(LatLng latLng, int zoom);
}
