package com.airbnb.android.airmapview.listeners;

import com.google.android.gms.maps.model.LatLng;

public interface OnScreenLocationLatLngCallback {
    void onScreenLocationLatLngReady(LatLng latLng);
}
