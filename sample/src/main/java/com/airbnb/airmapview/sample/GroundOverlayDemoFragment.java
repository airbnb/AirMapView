package com.airbnb.airmapview.sample;

import com.airbnb.android.airmapview.AirMapGroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class GroundOverlayDemoFragment extends BaseDemoFragment {
  @Override
  protected void startDemo() {
    getMap().addGroundOverlay(new AirMapGroundOverlay.Builder()
        .imageId(R.drawable.newark_nj_1922)
        .imageUrl("https://www.lib.utexas.edu/maps/historical/newark_nj_1922.jpg")
        .positionFromBounds(new LatLngBounds(new LatLng(40.712216, -74.22655), new LatLng(40.773941, -74.12544)))
        .build());

    getMap().animateCenterZoom(new LatLng(40.740, -74.18), 12);
  }
}
