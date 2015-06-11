package com.airbnb.android.airmapview;

public class MapboxWebViewMapFragment extends WebViewMapFragment {

    public static MapboxWebViewMapFragment newInstance(AirMapType mapType) {
        return (MapboxWebViewMapFragment) new MapboxWebViewMapFragment().setArguments(mapType);
    }

}
