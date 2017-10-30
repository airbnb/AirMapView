package com.airbnb.android.airmapview.listeners;

public interface OnLocationPermissionListener {
    void onLocationPermissionGranted();
    void onLocationPermissionDenied();
    void onLocationPermissionPermanentlyDenied();
}
