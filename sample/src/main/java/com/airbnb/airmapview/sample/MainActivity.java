package com.airbnb.airmapview.sample;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.android.airmapview.AirMapInterface;
import com.airbnb.android.airmapview.AirMapMarker;
import com.airbnb.android.airmapview.AirMapPolyline;
import com.airbnb.android.airmapview.AirMapView;
import com.airbnb.android.airmapview.AirMapViewTypes;
import com.airbnb.android.airmapview.DefaultAirMapViewBuilder;
import com.airbnb.android.airmapview.WebAirMapViewBuilder;
import com.airbnb.android.airmapview.listeners.OnCameraChangeListener;
import com.airbnb.android.airmapview.listeners.OnCameraMoveListener;
import com.airbnb.android.airmapview.listeners.OnLatLngScreenLocationCallback;
import com.airbnb.android.airmapview.listeners.OnInfoWindowClickListener;
import com.airbnb.android.airmapview.listeners.OnMapClickListener;
import com.airbnb.android.airmapview.listeners.OnMapInitializedListener;
import com.airbnb.android.airmapview.listeners.OnMapMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
    implements OnCameraChangeListener, OnMapInitializedListener,
               OnMapClickListener, OnCameraMoveListener, OnMapMarkerClickListener,
               OnInfoWindowClickListener, OnLatLngScreenLocationCallback {

  private AirMapView map;
  private DefaultAirMapViewBuilder mapViewBuilder;
  private TextView textLogs;
  private ScrollView logsScrollView;

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
    map.initialize(getSupportFragmentManager(), getString(R.string.mapboxAccessToken), getString(R.string.mapboxMapId));
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
      try {
        airMapInterface = mapViewBuilder.builder(AirMapViewTypes.NATIVE).build();
      } catch (UnsupportedOperationException e) {
        Toast.makeText(this, "Sorry, native Google Maps are not supported by this device. " +
                             "Please make sure you have Google Play Services installed.",
                       Toast.LENGTH_SHORT).show();
      }
    } else if (id == R.id.action_web_map) {

      airMapInterface = ((WebAirMapViewBuilder)mapViewBuilder.builder(AirMapViewTypes.WEB)).setMapboxAccessToken(getString(R.string.mapboxAccessToken)).setMapboxMapId(getString(R.string.mapboxMapId)).build();
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
    final LatLng airbnbLatLng = new LatLng(37.771883, -122.405224);
    addMarker("Airbnb HQ", airbnbLatLng, 1);
    addMarker("Performance Bikes", new LatLng(37.773975, -122.40205), 2);
    addMarker("REI", new LatLng(37.772127, -122.404411), 3);
    addMarker("Mapbox", new LatLng(37.77572, -122.41354), 4);
    map.animateCenterZoom(airbnbLatLng, 10);

    // Add Poly Line
    ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
    latLngs.add(new LatLng(37.77977, -122.38937));
    latLngs.add(new LatLng(37.77811, -122.39160));
    latLngs.add(new LatLng(37.77787, -122.38864));
    map.addPolyline(new AirMapPolyline(latLngs, 5));

    // Add Circle
    map.drawCircle(new LatLng(37.78443, -122.40805), 1000);
  }

  private void addMarker(String title, LatLng latLng, int id) {
    map.addMarker(new AirMapMarker(latLng, id)
            .setTitle(title)
            .setIconId(R.mipmap.icon_location_pin));
  }

  @Override public void onMapClick(LatLng latLng) {
    if (latLng != null) {
      appendLog(
          "Map onMapClick triggered with lat: " + latLng.latitude + ", lng: "
          + latLng.longitude);

      map.getMapInterface().getScreenLocation(latLng, this);
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

  @Override public void onLatLngScreenLocationReady(Point point) {
    appendLog("LatLng location on screen (x,y): (" + point.x + "," + point.y + ")");
  }
}
