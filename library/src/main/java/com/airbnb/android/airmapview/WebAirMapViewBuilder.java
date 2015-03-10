package com.airbnb.android.airmapview;

/**
 * AirMapView map that uses the web based Google Maps implementation.
 */
public class WebAirMapViewBuilder implements AirMapViewBuilder<WebViewMapFragment, AirMapType> {

    private AirMapType options;

    @Override
    public AirMapViewBuilder<WebViewMapFragment, AirMapType> withOptions(AirMapType options) {
        this.options = options;
        return this;
    }

    /**
     * Build the map fragment with the requested options
     * @return The {@link WebViewMapFragment} map fragment.
     */
    @Override
    public WebViewMapFragment build() {
        if (options == null) {
            options = new GoogleWebMapType();
        }
        if (options instanceof GoogleWebMapType) {
            return GoogleWebViewMapFragment.newInstance(options);
        }
        if (options instanceof GoogleChinaMapType) {
            return GoogleChinaWebViewMapFragment.newInstance(options);
        }
        return null;
    }
}
