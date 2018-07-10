package com.airbnb.android.airmapview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

public class MapboxWebViewMapFragment extends WebViewMapFragment {

  public static MapboxWebViewMapFragment newInstance(AirMapType mapType) {
    return (MapboxWebViewMapFragment) new MapboxWebViewMapFragment().setArguments(mapType);
  }

  @Override public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = super.onCreateView(inflater, container, savedInstanceState);

    MapboxWebMapType mapType = MapboxWebMapType.fromBundle(getArguments());
    webView.loadDataWithBaseURL(mapType.getDomain(), mapType.getMapData(getResources()),
            "text/html", "base64", null);

    return view;
  }

  @Override public void setMapType(MapType type) {
    String mapBoxType = null;
    switch (type) {
      case MAP_TYPE_NORMAL:
        mapBoxType = "mapbox.streets";
        break;
      case MAP_TYPE_SATELLITE:
        mapBoxType = "mapbox.satellite";
        break;
      case MAP_TYPE_TERRAIN:
        mapBoxType = "mapbox.outdoors";
        break;
    }
    webView.loadUrl(String.format(Locale.US, "javascript:setMapTypeId(\"%1$s\");", mapBoxType));
  }
}
