package com.airbnb.android.airmapview;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class DefaultAirMapViewBuilder {

    private boolean isGooglePlayServicesAvailable;

    public DefaultAirMapViewBuilder(Context context) {
        this(GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS);
    }

    public DefaultAirMapViewBuilder(boolean isGooglePlayServicesAvailable) {
        this.isGooglePlayServicesAvailable = isGooglePlayServicesAvailable;
    }

    /**
     * Returns the first supported AirMapView implementation in order of preference.
     */
    public AirMapViewBuilder builder() {
        if (isGooglePlayServicesAvailable)
            return new NativeAirMapViewBuilder();
        return new WebAirMapViewBuilder();
    }

    public AirMapViewBuilder builder(AirMapViewTypes mapType) {
        switch (mapType) {
            case NATIVE:
                if (isGooglePlayServicesAvailable)
                    return new NativeAirMapViewBuilder();
                break;
            case WEB:
                return new WebAirMapViewBuilder();
        }
        throw new UnsupportedOperationException("Requested map type is not supported");
    }

}
