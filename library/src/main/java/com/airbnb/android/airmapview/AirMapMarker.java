package com.airbnb.android.airmapview;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Bitmap;
import android.text.TextUtils;

/**
 * Helper class for keeping record of data needed to display map markers, as well as an object T
 * associated with the marker
 */
public class AirMapMarker<T> {

  private T object;
  private long id;
  private LatLng latLng;
  private String title;
  private int iconId;
  private Bitmap bitmap;
  private Marker googleMarker;

  public AirMapMarker(LatLng latLng, long id) {
    this(null, latLng, id);
  }

  public AirMapMarker(T object, LatLng latLng, long id) {
    this.object = object;
    this.latLng = latLng;
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public AirMapMarker<T> setId(long id) {
    this.id = id;
    return this;
  }

  public LatLng getLatLng() {
    return latLng;
  }

  public AirMapMarker<T> setLatLng(LatLng latLng) {
    this.latLng = latLng;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public AirMapMarker<T> setTitle(String title) {
    this.title = title;
    return this;
  }

  public int getIconId() {
    return iconId;
  }

  public AirMapMarker<T> setIconId(int iconId) {
    this.iconId = iconId;
    return this;
  }

  public AirMapMarker<T> setIcon(Bitmap bitmap) {
    this.bitmap = bitmap;
    return this;
  }

  public T getObject() {
    return object;
  }

  public AirMapMarker<T> setObject(T object) {
    this.object = object;
    return this;
  }

  /**
   * Add this marker to the given {@link com.google.android.gms.maps.GoogleMap} instance
   *
   * @param googleMap the {@link com.google.android.gms.maps.GoogleMap} instance to which the marker
   *                  will be added
   */
  public void addToGoogleMap(GoogleMap googleMap) {
    MarkerOptions options = new MarkerOptions();

    options.position(latLng);

    if (!TextUtils.isEmpty(title)) {
      options.title(title);
    }

    if (bitmap != null) {
      options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    } else if (iconId > 0) {
      options.icon(BitmapDescriptorFactory.fromResource(iconId));
    }

    // add the marker and keep a reference so it can be removed
    googleMarker = googleMap.addMarker(options);
  }

  /**
   * Remove this polyline from a GoogleMap (if it was added).
   *
   * @return true if the {@link com.google.android.gms.maps.model.Polyline} was removed
   */
  public boolean removeFromGoogleMap() {
    if (googleMarker != null) {
      googleMarker.remove();
      return true;
    }
    return false;
  }
}
