package com.airbnb.android.airmapview;

public class GoogleWebViewMapFragment extends WebViewMapFragment {
  public static GoogleWebViewMapFragment newInstance(AirMapType mapType) {
    return (GoogleWebViewMapFragment) new GoogleWebViewMapFragment().setArguments(mapType);
  }

  @Override public void setMapType(MapType type) {
    String webType;
    if (type == MapType.MAP_TYPE_NORMAL) {
      webType = "google.maps.MapTypeId.ROADMAP";
    } else if (type == MapType.MAP_TYPE_SATELLITE) {
      webType = "google.maps.MapTypeId.SATELLITE";
    } else if (type == MapType.MAP_TYPE_TERRAIN) {
      webType = "google.maps.MapTypeId.TERRAIN";
    } else {
      webType = "google.maps.MapTypeId.ROADMAP";
    }
    webView.loadUrl(String.format("javascript:setMapTypeId(%1$s);", webType));
  }
}
