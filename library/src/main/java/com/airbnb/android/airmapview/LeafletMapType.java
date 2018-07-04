package com.airbnb.android.airmapview;

public class LeafletMapType extends AirMapType {

  // For leaflet, we define some map provider in leaflet_map.html file.
  // So need to supply a provider name here. (like Google, GoogleChina, Baidu, Gaode)
  public LeafletMapType(String mapProvider) {
    super("leaflet_map.html", mapProvider, "");
  }

}

