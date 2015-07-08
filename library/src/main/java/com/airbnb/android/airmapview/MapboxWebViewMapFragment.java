package com.airbnb.android.airmapview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapboxWebViewMapFragment extends WebViewMapFragment {

  public static MapboxWebViewMapFragment newInstance(AirMapType mapType) {
    return (MapboxWebViewMapFragment) new MapboxWebViewMapFragment().setArguments(mapType);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = super.onCreateView(inflater, container, savedInstanceState);

    MapboxWebMapType mapType = MapboxWebMapType.fromBundle(getArguments());
    webView.loadDataWithBaseURL(mapType.getDomain(), mapType.getMapData(getResources()),
            "text/html", "base64", null);

    return view;
  }
}
