package com.airbnb.android.airmapview;

public class GoogleWebViewMapFragment extends WebViewMapFragment {

  public static GoogleWebViewMapFragment newInstance(AirMapType mapType) {
    return (GoogleWebViewMapFragment) new GoogleWebViewMapFragment().setArguments(mapType);
  }
}
