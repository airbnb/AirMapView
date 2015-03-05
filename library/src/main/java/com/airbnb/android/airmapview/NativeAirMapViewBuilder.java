package com.airbnb.android.airmapview;

import com.google.android.gms.maps.GoogleMapOptions;

public class NativeAirMapViewBuilder
        implements AirMapViewBuilder<AirGoogleMapFragment, AirGoogleMapOptions> {

    private AirGoogleMapOptions options;

    @Override
    public AirMapViewBuilder<AirGoogleMapFragment, AirGoogleMapOptions> withOptions(
            AirGoogleMapOptions options) {
        this.options = options;
        return this;
    }

    @Override
    public AirGoogleMapFragment build() {
        if (options == null) {
            options = new AirGoogleMapOptions(new GoogleMapOptions());
        }
        return AirGoogleMapFragment.newInstance(options);
    }
}
