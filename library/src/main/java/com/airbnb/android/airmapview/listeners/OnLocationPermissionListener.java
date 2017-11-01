package com.airbnb.android.airmapview.listeners;

/**
 * Interface which supplies callbacks when showing dynamic permission request dialogs
 * Location permission is requested when enable 'my location'.
 * {@link com.airbnb.android.airmapview.AirMapInterface#setMyLocationEnabled(boolean)}
 */
public interface OnLocationPermissionListener {
    /**
     * Called when location permission is granted
     */
    void onLocationPermissionGranted();

    /**
     * Called when location permission is denied
     */
    void onLocationPermissionDenied();

    /**
     * Called when location permission is denied, and the checkbox of 'Never Ask Again' is checked
     */
    void onLocationPermissionPermanentlyDenied();
}
