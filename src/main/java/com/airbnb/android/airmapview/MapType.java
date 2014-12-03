package com.airbnb.android.airmapview;

public interface MapType {
    /*
    public enum MapType {
        Google(null, null, null),
        GoogleWeb("google_map.html", "https://maps.googleapis.com/maps/api/js", "www.googleapis.com"),
        GoogleWebChina("google_map.html", "http://ditu.google.cn/maps/api/js", "www.google.cn"),
        AutoNavi("autonavi_map.html", "https://webapi.amap.com/maps", "www.amap.com");

        public String mUrl;
        public String mMapUrl;
        public String mDomain;

        private MapType(String url, String mapUrl, String domain) {
            mUrl = url;
            mMapUrl = mapUrl;
            mDomain = domain;
        }

        public static MapType getState(int index) {
            MapType[] values = values();
            if (index < 0 || index >= values.length) {
                return null;
            }
            return values[ index ];
        }
    }
     */

    String getAssetName();

    String getMapUrl();

    String getDomain();
}
