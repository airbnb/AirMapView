package com.airbnb.airmapview.sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.android.airmapview.AirMapGeoJsonLayer;
import com.airbnb.android.airmapview.AirMapInterface;
import com.airbnb.android.airmapview.AirMapMarker;
import com.airbnb.android.airmapview.AirMapViewTypes;
import com.airbnb.android.airmapview.GoogleChinaMapType;
import com.airbnb.android.airmapview.WebAirMapViewBuilder;
import com.airbnb.android.airmapview.listeners.OnSnapshotReadyListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
  private DemoAdapter demoAdapter = new DemoAdapter();
  private List<Demo> demos = new ArrayList<>();
  private ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
        R.string.drawer_open, R.string.drawer_close);
    drawerLayout.addDrawerListener(drawerToggle);
    drawerToggle.syncState();

    listView = (ListView) findViewById(android.R.id.list);
    listView.setAdapter(demoAdapter);
    listView.setOnItemClickListener(this);

    addDemo("Default", new DemoFragmentFactory() {
      @Override
      public BaseDemoFragment make() {
        return new DefaultDemoFragment();
      }
    });
    addDemo("Ground Overlay", new DemoFragmentFactory() {
      @Override
      public BaseDemoFragment make() {
        return new GroundOverlayDemoFragment();
      }
    });

    startDemo(demos.get(0));
  }

  private interface DemoFragmentFactory {
    BaseDemoFragment make();
  }

    private void startDemo (Demo demo){
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.content, demo.fragmentFactory.make())
          .commit();
      ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
  }

    @Override
    public void onBackPressed () {
      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      if (drawer.isDrawerOpen(GravityCompat.START)) {
        drawer.closeDrawer(GravityCompat.START);
    } else {
        super.onBackPressed();
    }
  }

  private void addDemo(String demoName, DemoFragmentFactory fragmentFactory) {
    demos.add(new Demo(demoName, fragmentFactory));
  }

    @Override
    public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
      Demo demo = demoAdapter.getItem(position);
      startDemo(demo);
      listView.setSelection(position);
  }

    private class DemoAdapter extends BaseAdapter {
      @Override
      public int getCount() {
        return demos.size();
      }

      @Override
      public Demo getItem(int position) {
        return demos.get(position);
      }

      @Override
      public long getItemId(int position) {
        return position;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
          convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        Demo demo = getItem(position);
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(demo.demoName);
        return convertView;
      }
    }

    private static class Demo {
      private final String demoName;
      private final DemoFragmentFactory fragmentFactory;

      public Demo(String demoName, DemoFragmentFactory fragmentFactory) {
        this.demoName = demoName;
        this.fragmentFactory = fragmentFactory;
      }
  }
}
