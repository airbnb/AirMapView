package com.airbnb.airmapview.sample;

import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.airbnb.android.airmapview.AirMapGeoJsonLayer;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

public class GeoJsonDemoFragment extends BaseDemoFragment {

  private static final String TAG = GeoJsonDemoFragment.class.getSimpleName();

  @Override
  protected void startDemo() {
    // Draws a layer on top of Australia
    String geoJsonString = Util.readFromRawResource(getContext(), R.raw.google);
    AirMapGeoJsonLayer layer = new AirMapGeoJsonLayer.Builder(geoJsonString)
        .strokeColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark))
        .strokeWidth(10)
        .fillColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_light))
        .build();

    try {
      map.getMapInterface().setGeoJsonLayer(layer);
    } catch (JSONException e) {
      Log.e(TAG, "Failed to add GeoJson layer", e);
    }

    getMap().animateCenterZoom(new LatLng(-25., 133.), 4);
  }
}
