package com.airbnb.android.airmapview;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.CameraPosition;

/**
 * Wrapper for the {@link GoogleMapOptions} class, which is final.
 */
public class AirGoogleMapOptions {

    private GoogleMapOptions options;

    public AirGoogleMapOptions(GoogleMapOptions options) {
        this.options = options;
    }

    public AirGoogleMapOptions zOrderOnTop(boolean zOrderOnTop) {
        options.zOrderOnTop(zOrderOnTop);
        return this;
    }

    public AirGoogleMapOptions useViewLifecycleInFragment(boolean useViewLifecycleInFragment) {
        options.useViewLifecycleInFragment(useViewLifecycleInFragment);
        return this;
    }

    public AirGoogleMapOptions mapType(int mapType) {
        options.mapType(mapType);
        return this;
    }

    public AirGoogleMapOptions camera(CameraPosition camera) {
        options.camera(camera);
        return this;
    }

    public AirGoogleMapOptions zoomControlsEnabled(boolean enabled) {
        options.zoomControlsEnabled(enabled);
        return this;
    }

    public AirGoogleMapOptions compassEnabled(boolean enabled) {
        options.compassEnabled(enabled);
        return this;
    }

    public AirGoogleMapOptions scrollGesturesEnabled(boolean enabled) {
        options.scrollGesturesEnabled(enabled);
        return this;
    }

    public AirGoogleMapOptions zoomGesturesEnabled(boolean enabled) {
        options.zoomGesturesEnabled(enabled);
        return this;
    }

    public AirGoogleMapOptions tiltGesturesEnabled(boolean enabled) {
        options.tiltGesturesEnabled(enabled);
        return this;
    }

    public AirGoogleMapOptions rotateGesturesEnabled(boolean enabled) {
        options.rotateGesturesEnabled(enabled);
        return this;
    }

    public AirGoogleMapOptions liteMode(boolean enabled) {
        options.liteMode(enabled);
        return this;
    }

    public AirGoogleMapOptions mapToolbarEnabled(boolean enabled) {
        options.mapToolbarEnabled(enabled);
        return this;
    }

    public Boolean getZOrderOnTop() {
        return options.getZOrderOnTop();
    }

    public Boolean getUseViewLifecycleInFragment() {
        return options.getUseViewLifecycleInFragment();
    }

    public int getMapType() {
        return options.getMapType();
    }

    public CameraPosition getCamera() {
        return options.getCamera();
    }

    public Boolean getZoomControlsEnabled() {
        return options.getZoomControlsEnabled();
    }

    public Boolean getCompassEnabled() {
        return options.getCompassEnabled();
    }

    public Boolean getScrollGesturesEnabled() {
        return options.getScrollGesturesEnabled();
    }

    public Boolean getZoomGesturesEnabled() {
        return options.getZoomGesturesEnabled();
    }

    public Boolean getTiltGesturesEnabled() {
        return options.getTiltGesturesEnabled();
    }

    public Boolean getRotateGesturesEnabled() {
        return options.getRotateGesturesEnabled();
    }

    public Boolean getLiteMode() {
        return options.getLiteMode();
    }

    public Boolean getMapToolbarEnabled() {
        return options.getMapToolbarEnabled();
    }

    public Bundle toBundle() {
        Bundle args = new Bundle();
        // this is internal to SupportMapFragment
        args.putParcelable("MapOptions", options);
        return args;
    }
}
