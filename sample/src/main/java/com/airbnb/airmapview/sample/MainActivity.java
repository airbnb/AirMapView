package com.airbnb.airmapview.sample;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.android.airmapview.AirMapInterface;
import com.airbnb.android.airmapview.AirMapMarker;
import com.airbnb.android.airmapview.AirMapView;
import com.airbnb.android.airmapview.AirMapViewTypes;
import com.airbnb.android.airmapview.DefaultAirMapViewBuilder;
import com.airbnb.android.airmapview.listeners.OnCameraChangeListener;
import com.airbnb.android.airmapview.listeners.OnCameraMoveListener;
import com.airbnb.android.airmapview.listeners.OnInfoWindowClickListener;
import com.airbnb.android.airmapview.listeners.OnMapClickListener;
import com.airbnb.android.airmapview.listeners.OnMapInitializedListener;
import com.airbnb.android.airmapview.listeners.OnMapMarkerClickListener;


public class MainActivity extends ActionBarActivity
    implements OnCameraChangeListener, OnMapInitializedListener,
               OnMapClickListener, OnCameraMoveListener, OnMapMarkerClickListener,
               OnInfoWindowClickListener {

  private AirMapView map;
  private DefaultAirMapViewBuilder mapViewBuilder;
  private TextView textLogs;
  private ScrollView logsScrollView;
  private final LatLng latLng = new LatLng(37.771883, -122.405224); // Airbnb HQ

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mapViewBuilder = new DefaultAirMapViewBuilder(this);
    map = (AirMapView) findViewById(R.id.map);
    textLogs = (TextView) findViewById(R.id.textLogs);
    logsScrollView = (ScrollView) findViewById(R.id.logsScrollView);

    map.setOnMapClickListener(this);
    map.setOnCameraChangeListener(this);
    map.setOnCameraMoveListener(this);
    map.setOnMarkerClickListener(this);
    map.setOnMapInitializedListener(this);
    map.setOnInfoWindowClickListener(this);
    map.initialize(getSupportFragmentManager());
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    AirMapInterface airMapInterface = null;

    if (id == R.id.action_native_map) {
      airMapInterface = mapViewBuilder.builder(AirMapViewTypes.NATIVE).build();
    } else if (id == R.id.action_web_map) {
      airMapInterface = mapViewBuilder.builder(AirMapViewTypes.WEB).build();
    } else if (id == R.id.action_clear_logs) {
      textLogs.setText("");
    }

    if (airMapInterface != null) {
      map.initialize(getSupportFragmentManager(), airMapInterface);
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void onCameraChanged(LatLng latLng, int zoom) {
    appendLog("Map onCameraChanged triggered with lat: " + latLng.latitude + ", lng: "
              + latLng.longitude);
  }

  @Override public void onMapInitialized() {
    appendLog("Map onMapInitialized triggered");
    map.animateCenterZoom(latLng, 10);
    map.addMarker(new AirMapMarker(latLng, 1)
                      .setTitle("Airbnb HQ")
                      .setIconId(R.mipmap.icon_location_pin));
  }

  @Override public void onMapClick(LatLng latLng) {
    if (latLng != null) {
      appendLog(
          "Map onMapClick triggered with lat: " + latLng.latitude + ", lng: "
          + latLng.longitude);
    } else {
      appendLog("Map onMapClick triggered with null latLng");
    }
  }

  @Override public void onCameraMove() {
    appendLog("Map onCameraMove triggered");
  }

  private void appendLog(String msg) {
    textLogs.setText(textLogs.getText() + "\n" + msg);
    logsScrollView.fullScroll(View.FOCUS_DOWN);
  }

  @Override public void onMapMarkerClick(long id) {
    appendLog("Map onMapMarkerClick triggered with id " + id);
  }

  @Override public void onMapMarkerClick(Marker marker) {
    appendLog("Map onMapMarkerClick triggered with marker " + marker.getId());
  }

  @Override public void onInfoWindowClick(long id) {
    appendLog("Map onInfoWindowClick triggered with id " + id);
  }

  @Override public void onInfoWindowClick(Marker marker) {
    appendLog("Map onInfoWindowClick triggered with marker " + marker.getId());
  }
}
