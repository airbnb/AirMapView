package com.airbnb.android.airmapview;

public class GoogleChinaMapType implements AirMapType {

    @Override
    public String getFileName() {
        return "google_map.html";
    }

    @Override
    public String getMapUrl() {
        return "http://ditu.google.cn/maps/api/js";
    }

    @Override
    public String getDomain() {
        return "www.google.cn";
    }
}
