package com.airbnb.android.airmapview;

public class WebAirMapViewBuilder implements AirMapViewBuilder<WebViewMapFragment, AirMapType> {

    private AirMapType options;

    @Override
    public AirMapViewBuilder<WebViewMapFragment, AirMapType> withOptions(AirMapType options) {
        this.options = options;
        return this;
    }

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
