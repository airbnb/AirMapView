package com.airbnb.android.airmapview;

public class GoogleWebMapType implements AirMapType {
    @Override
    public String getFileName() {
        return "google_map.html";
    }

    @Override
    public String getMapUrl() {
        return "https://maps.googleapis.com/maps/api/js";
    }

    @Override
    public String getDomain() {
        return "www.googleapis.com";
    }
}
