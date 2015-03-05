package com.airbnb.android.airmapview;

import com.google.android.gms.maps.GoogleMapOptions;

public class NativeAirMapViewBuilder implements AirMapViewBuilder<AirGoogleMapFragment, GoogleMapOptions> {

    private GoogleMapOptions options;

    @Override
    public AirGoogleMapFragment build() {
        return AirGoogleMapFragment.newInstance(options);
    }

    @Override
    public AirMapViewBuilder<AirGoogleMapFragment, GoogleMapOptions> withOptions(GoogleMapOptions options) {
        this.options = options;
        return this;
    }
}
