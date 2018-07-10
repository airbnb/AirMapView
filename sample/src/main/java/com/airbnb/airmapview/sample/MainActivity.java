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
import com.airbnb.android.airmapview.LeafletBaiduMapType;
import com.airbnb.android.airmapview.LeafletGaodeMapType;
import com.airbnb.android.airmapview.LeafletGoogleChinaMapType;
import com.airbnb.android.airmapview.LeafletGoogleMapType;
import com.airbnb.android.airmapview.LeafletWebViewMapFragment;
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
      case R.id.action_leaflet_google_web_map:
        airMapInterface = new WebAirMapViewBuilder().withOptions(new LeafletGoogleMapType()).build();
        break;
      case R.id.action_leaflet_google_china_web_map:
        airMapInterface = new WebAirMapViewBuilder().withOptions(new LeafletGoogleChinaMapType()).build();
        break;
      case R.id.action_baidu_web_map:
        airMapInterface = new WebAirMapViewBuilder().withOptions(new LeafletBaiduMapType()).build();
        break;
      case R.id.action_gaode_web_map:
        airMapInterface = new WebAirMapViewBuilder().withOptions(new LeafletGaodeMapType()).build();
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
    if (map.getMapInterface() instanceof LeafletWebViewMapFragment) {
      // Baidu map is unavailable in the US, so we show points in China.
      final LatLng wfcLatLng = new LatLng(39.918786, 116.459273);
      addMarker("WFC", wfcLatLng, 1);
      addMarker("Chaoyang Gate", new LatLng(39.923823, 116.433666), 2);
      String image = "<img width=\"25\" height=\"41\" src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAABSCAMAAAAhFXfZAAAC91BMVEVMaXEzeak2f7I4g7g3g7cua5gzeKg8hJo3grY4g7c3grU0gLI2frE0daAubJc2gbQwd6QzeKk2gLMtd5sxdKIua5g1frA2f7IydaM0e6w2fq41fK01eqo3grgubJgta5cxdKI1f7AydaQydaMxc6EubJgvbJkwcZ4ubZkwcJwubZgubJcydqUydKIxapgubJctbJcubZcubJcvbJYubJcvbZkubJctbJctbZcubJg2f7AubJcrbZcubJcubJcua5g3grY0fq8ubJcubJdEkdEwhsw6i88vhswuhcsuhMtBjMgthMsrg8srgss6is8qgcs8i9A9iMYtg8spgcoogMo7hcMngMonf8olfso4gr8kfck5iM8jfMk4iM8he8k1fro7itAgesk2hs8eecgzfLcofssdeMg0hc4cd8g2hcsxeLQbdsgZdcgxeLImfcszhM0vda4xgckzhM4xg84wf8Yxgs4udKsvfcQucqhUndROmdM1fK0wcZ8vb5w0eqpQm9MzeKhXoNVcpdYydKNWn9VZotVKltJFjsIwcJ1Rms9OlslLmtH///8+kc9epdYzd6dbo9VHkMM2f7FHmNBClM8ydqVcpNY9hro3gLM9hLczealQmcw3fa46f7A8gLMxc6I3eagyc6FIldJMl9JSnNRSntNNl9JPnNJFi75UnM9ZodVKksg8kM45jc09e6ZHltFBk883gbRBh7pDk9EwcaBzn784g7dKkcY2i81Om9M7j85Llc81is09g7Q4grY/j9A0eqxKmdFFltBEjcXf6fFImdBCiLxJl9FGlNFBi78yiMxVndEvbpo6js74+vx+psPP3+o/ks5HkcpGmNCjwdZCkNDM3ehYoNJEls+lxNkxh8xHks0+jdC1zd5Lg6r+/v/H2ufz9/o3jM3t8/edvdM/k89Th61OiLBSjbZklbaTt9BfptdjmL1AicBHj8hGk9FAgK1dkLNTjLRekrdClc/k7fM0icy0y9tgp9c4jc2NtM9Dlc8zicxeXZn3AAAAQ3RSTlMAHDdTb4yPA+LtnEQmC4L2EmHqB7XA0d0sr478x4/Yd5i1zOfyPkf1sLVq4Nh3FvjxopQ2/STNuFzUwFIwxKaejILpIBEV9wAABhVJREFUeF6s1NdyFEcY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\"/>";
      addMarker("Dongzhi Gate", new LatLng(39.941823, 116.426319), 3, image, 25, 41);
      map.animateCenterZoom(wfcLatLng, 11);

      // Add Polylines
      LatLng[] latLngs = {
              new LatLng(39.918786, 116.459273),
              new LatLng(39.923823, 116.433666),
              new LatLng(39.919635, 116.448831) };

      map.addPolyline(new AirMapPolyline(Arrays.asList(latLngs), 5));

      LatLng[] polygonLatLngs = {
              new LatLng(39.902896, 116.42792),
              new LatLng(39.902896, 116.43892),
              new LatLng(39.913896, 116.43892),
              new LatLng(39.913896, 116.42792)
      };
      map.addPolygon(new AirMapPolygon.Builder().add(polygonLatLngs).strokeWidth(3.f).build());

      // Add Circle
      map.drawCircle(new LatLng(39.919635, 116.448831), 1000);
    } else {
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
  }

  private void addMarker(String title, LatLng latLng, int id) {
    addMarker(title, latLng, id, null, 0, 0);
  }

  private void addMarker(String title, LatLng latLng, int id,
                         String divIconHtml, int iconWidth, int iconHeight) {
    map.addMarker(new AirMapMarker.Builder()
        .id(id)
        .position(latLng)
        .title(title)
        .iconId(R.mipmap.icon_location_pin)
        .divIconHtml(divIconHtml)
        .divIconWidth(iconWidth)
        .divIconHeight(iconHeight)
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
