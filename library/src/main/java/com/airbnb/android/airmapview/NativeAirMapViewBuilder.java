package com.airbnb.android.airmapview;

import com.google.android.gms.maps.GoogleMapOptions;

public class NativeAirMapViewBuilder
        implements AirMapViewBuilder<NativeGoogleMapFragment, AirGoogleMapOptions> {

    private AirGoogleMapOptions options;

    @Override
    public AirMapViewBuilder<NativeGoogleMapFragment, AirGoogleMapOptions> withOptions(
            AirGoogleMapOptions options) {
        this.options = options;
        return this;
    }

    @Override
    public NativeGoogleMapFragment build() {
        if (options == null) {
            options = new AirGoogleMapOptions(new GoogleMapOptions());
        }
        return NativeGoogleMapFragment.newInstance(options);
    }
}
