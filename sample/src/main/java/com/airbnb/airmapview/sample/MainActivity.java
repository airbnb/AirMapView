package com.airbnb.airmapview.sample;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.android.airmapview.AirMapGeoJsonLayer;
import com.airbnb.android.airmapview.AirMapInterface;
import com.airbnb.android.airmapview.AirMapMarker;
import com.airbnb.android.airmapview.AirMapPolygon;
import com.airbnb.android.airmapview.AirMapPolyline;
import com.airbnb.android.airmapview.AirMapView;
import com.airbnb.android.airmapview.AirMapViewTypes;
import com.airbnb.android.airmapview.DefaultAirMapViewBuilder;
import com.airbnb.android.airmapview.GoogleChinaMapType;
import com.airbnb.android.airmapview.MapType;
import com.airbnb.android.airmapview.WebAirMapViewBuilder;
import com.airbnb.android.airmapview.listeners.OnCameraChangeListener;
import com.airbnb.android.airmapview.listeners.OnCameraMoveListener;
import com.airbnb.android.airmapview.listeners.OnInfoWindowClickListener;
import com.airbnb.android.airmapview.listeners.OnLatLngScreenLocationCallback;
import com.airbnb.android.airmapview.listeners.OnMapClickListener;
import com.airbnb.android.airmapview.listeners.OnMapInitializedListener;
import com.airbnb.android.airmapview.listeners.OnMapMarkerClickListener;
import com.airbnb.android.airmapview.listeners.OnSnapshotReadyListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
    implements OnCameraChangeListener, OnMapInitializedListener,
    OnMapClickListener, OnCameraMoveListener, OnMapMarkerClickListener,
    OnInfoWindowClickListener, OnLatLngScreenLocationCallback {

  private final LogsAdapter adapter = new LogsAdapter();

  private static final String TAG = MainActivity.class.getSimpleName();
  private AirMapView map;
  private DefaultAirMapViewBuilder mapViewBuilder;
  private RecyclerView logsRecyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mapViewBuilder = new DefaultAirMapViewBuilder(this);
    map = (AirMapView) findViewById(R.id.map);
    logsRecyclerView = (RecyclerView) findViewById(R.id.logs);
    ((LinearLayoutManager) logsRecyclerView.getLayoutManager()).setReverseLayout(true);
    logsRecyclerView.setAdapter(adapter);
    Button btnMapTypeNormal = (Button) findViewById(R.id.btnMapTypeNormal);
    Button btnMapTypeSattelite = (Button) findViewById(R.id.btnMapTypeSattelite);
    Button btnMapTypeTerrain = (Button) findViewById(R.id.btnMapTypeTerrain);

    btnMapTypeNormal.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(@NonNull View v) {
        map.setMapType(MapType.MAP_TYPE_NORMAL);
      }
    });

    btnMapTypeSattelite.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(@NonNull View v) {
        map.setMapType(MapType.MAP_TYPE_SATELLITE);
      }
    });

    btnMapTypeTerrain.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(@NonNull View v) {
        map.setMapType(MapType.MAP_TYPE_TERRAIN);
      }
    });

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

    switch (id) {
      case R.id.action_native_map:
        try {
          airMapInterface = mapViewBuilder.builder(AirMapViewTypes.NATIVE).build();
        } catch (UnsupportedOperationException e) {
          Toast.makeText(this, "Sorry, native Google Maps are not supported by this device. " +
                          "Please make sure you have Google Play Services installed.",
                  Toast.LENGTH_SHORT).show();
        }
        break;
      case R.id.action_mapbox_map:
        airMapInterface = mapViewBuilder.builder(AirMapViewTypes.WEB).build();
        break;
      case R.id.action_google_web_map:
        // force Google Web maps since otherwise AirMapViewTypes.WEB returns MapBox by default.
        airMapInterface = new WebAirMapViewBuilder().build();
        break;
      case R.id.action_google_china_web_map:
        airMapInterface = new WebAirMapViewBuilder().withOptions(new GoogleChinaMapType()).build();
        break;
      case R.id.action_clear_logs:
        adapter.clearLogs();
        break;
      case R.id.add_geojson_layer:
        // Draws a layer on top of Australia
        String geoJsonString = Util.readFromRawResource(this, R.raw.google);
        AirMapGeoJsonLayer layer = new AirMapGeoJsonLayer.Builder(geoJsonString)
                .strokeColor(getResources().getColor(android.R.color.holo_green_dark))
                .strokeWidth(10)
                .fillColor(getResources().getColor(android.R.color.holo_green_light))
                .build();
        try {
          map.getMapInterface().setGeoJsonLayer(layer);
        } catch (JSONException e) {
          Log.e(TAG, "Failed to add GeoJson layer", e);
        }

        break;
        case R.id.remove_geojson_layer:
          map.getMapInterface().clearGeoJsonLayer();
          break;
      case R.id.take_snapshot:
        map.getMapInterface().getSnapshot(new OnSnapshotReadyListener() {
          @Override
          public void onSnapshotReady(@Nullable Bitmap bitmap) {
            if (bitmap != null) {
              appendBitmap(bitmap);
            } else {
              appendLog("Null bitmap");
            }
          }
        });
        break;
      case R.id.enable_location:
        map.setMyLocationEnabled(true);
        break;
      case R.id.disable_location:
        map.setMyLocationEnabled(false);
      default:
        break;
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

    // Add Polylines
    LatLng[] latLngs = {
        new LatLng(37.77977, -122.38937),
        new LatLng(37.77811, -122.39160),
        new LatLng(37.77787, -122.38864) };

    map.addPolyline(new AirMapPolyline(Arrays.asList(latLngs), 5));

    // Add Polygons
    LatLng[] polygonLatLngs = {
            new LatLng(37.784, -122.405),
            new LatLng(37.784, -122.406),
            new LatLng(37.785, -122.406),
            new LatLng(37.785, -122.405)
    };
    map.addPolygon(new AirMapPolygon.Builder().add(polygonLatLngs).strokeWidth(3.f).build());

    // Add Circle
    map.drawCircle(new LatLng(37.78443, -122.40805), 1000);

    // enable my location
    map.setMyLocationEnabled(false);
  }

  private void addMarker(String title, LatLng latLng, int id) {
    map.addMarker(new AirMapMarker.Builder()
        .id(id)
        .position(latLng)
        .title(title)
        .iconId(R.mipmap.icon_location_pin)
        .build());
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
    adapter.addString(msg);
    logsRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
  }

  private void appendBitmap(Bitmap bitmap) {
    adapter.addBitmap(bitmap);
    logsRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
  }

  @Override public void onMapMarkerClick(AirMapMarker airMarker) {
    appendLog("Map onMapMarkerClick triggered with id " + airMarker.getId());
  }

  @Override public void onInfoWindowClick(AirMapMarker airMarker) {
    appendLog("Map onInfoWindowClick triggered with id " + airMarker.getId());
  }

  @Override public void onLatLngScreenLocationReady(Point point) {
    appendLog("LatLng location on screen (x,y): (" + point.x + "," + point.y + ")");
  }
}
