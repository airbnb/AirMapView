package com.airbnb.android.airmapview;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class LeafletWebViewMapFragment extends WebViewMapFragment {
  public static LeafletWebViewMapFragment newInstance(AirMapType mapType) {
    return (LeafletWebViewMapFragment) new LeafletWebViewMapFragment().setArguments(mapType);
  }

  @Override
  public void setMapType(MapType type) {
    String webType = null;
    switch (type) {
      case MAP_TYPE_NORMAL:
        webType = "Normal";
        break;
      case MAP_TYPE_SATELLITE:
        webType = "Satellite";
        break;
      case MAP_TYPE_TERRAIN:
        webType = "Terrain";
        break;
    }
    webView.loadUrl(String.format(Locale.US, "javascript:setMapTypeId('%1$s');", webType));
  }

  @Override public void addMarker(AirMapMarker<?> marker) {
    if (marker == null || marker.getDivIcon() == null || marker.getDivIcon().getHtml() == null) {
      super.addMarker(marker);
    } else {
      LatLng latLng = marker.getLatLng();
      markers.put(marker.getId(), marker);
      webView.loadUrl(
          String.format(Locale.US,
              "javascript:addMarkerWithId(%1$f, %2$f, %3$d, '%4$s', '%5$s', %6$b, '%7$s', %8$d, " +
                  "%9$d);",
              latLng.latitude, latLng.longitude, marker.getId(), marker.getTitle(),
              marker.getSnippet(), marker.getMarkerOptions().isDraggable(),
              marker.getDivIcon().getHtml(), marker.getDivIcon().getWidth(),
              marker.getDivIcon().getHeight()));
    }
  }
}
