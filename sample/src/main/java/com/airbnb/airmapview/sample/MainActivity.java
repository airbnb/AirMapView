package com.airbnb.airmapview.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.airbnb.android.airmapview.AirMapView;
import com.airbnb.android.airmapview.AirMapViewBuilder;
import com.airbnb.android.airmapview.AirMapViewTypes;
import com.airbnb.android.airmapview.DefaultAirMapViewBuilder;
import com.airbnb.android.airmapview.NativeAirMapViewBuilder;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends ActionBarActivity
        implements AirMapView.OnCameraChangeListener, AirMapView.OnMapInitializedListener,
        AirMapView.OnMapClickListener {

    private AirMapView map;
    private DefaultAirMapViewBuilder builder = new DefaultAirMapViewBuilder(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = (AirMapView) findViewById(R.id.map);

        map.onCreateView(getSupportFragmentManager(), false, this);
        map.setOnCameraChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_native_map) {
            NativeAirMapViewBuilder nativeBuilder = (NativeAirMapViewBuilder) builder.builder(AirMapViewTypes.NATIVE);
            nativeBuilder.build();
        }
        else if (id == R.id.action_web_map) {
            AirMapViewBuilder webBuilder = builder.builder(AirMapViewTypes.WEB);
            webBuilder.build();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCameraChanged(LatLng latLng, int zoom) {

    }

    @Override
    public void onMapInitialized() {
        map.setOnMapClickListener(this);
        map.animateCenter(new LatLng(37.771883, -122.405224));
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}
