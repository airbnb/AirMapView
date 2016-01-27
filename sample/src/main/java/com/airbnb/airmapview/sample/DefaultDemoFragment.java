package com.airbnb.airmapview.sample;

import com.airbnb.android.airmapview.AirMapMarker;
import com.airbnb.android.airmapview.AirMapPolygon;
import com.airbnb.android.airmapview.AirMapPolyline;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;

public class DefaultDemoFragment extends BaseDemoFragment {

  @Override
  protected void startDemo() {
    final LatLng airbnbLatLng = new LatLng(37.771883, -122.405224);
    addMarker("Airbnb HQ", airbnbLatLng, 1);
    addMarker("Performance Bikes", new LatLng(37.773975, -122.40205), 2);
    addMarker("REI", new LatLng(37.772127, -122.404411), 3);
    addMarker("Mapbox", new LatLng(37.77572, -122.41354), 4);
    getMap().animateCenterZoom(airbnbLatLng, 10);

    // Add Polylines
    LatLng[] latLngs = {
        new LatLng(37.77977, -122.38937),
        new LatLng(37.77811, -122.39160),
        new LatLng(37.77787, -122.38864)};

    getMap().addPolyline(new AirMapPolyline(Arrays.asList(latLngs), 5));

    // Add Polygons
    LatLng[] polygonLatLngs = {
        new LatLng(37.784, -122.405),
        new LatLng(37.784, -122.406),
        new LatLng(37.785, -122.406),
        new LatLng(37.785, -122.405)
    };
    getMap().addPolygon(new AirMapPolygon.Builder().add(polygonLatLngs).strokeWidth(3.f).build());

    // Add Circle
    getMap().drawCircle(new LatLng(37.78443, -122.40805), 1000);

    // enable my location
    getMap().setMyLocationEnabled(true);
  }

  private void addMarker(String title, LatLng latLng, int id) {
    getMap().addMarker(new AirMapMarker.Builder()
        .id(id)
        .position(latLng)
        .title(title)
        .iconId(R.mipmap.icon_location_pin)
        .build());
  }
}
