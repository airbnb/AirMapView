package com.airbnb.android.airmapview;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Helper class for keeping record of data needed to display a polyline, as well as an optional
 * object T associated with the polyline.
 */
public class AirMapPolyline<T> {

  private static final int STROKE_WIDTH = 1;
  private static final int STROKE_COLOR = Color.BLUE;

  private T object;
  private int strokeWidth;
  private long id;
  private List<LatLng> points;
  private String title;
  private int strokeColor;
  private Polyline googlePolyline;

  public AirMapPolyline(List<LatLng> points, long id) {
    this(null, points, id);
  }

  public AirMapPolyline(T object, List<LatLng> points, long id) {
    this(object, points, id, STROKE_WIDTH, STROKE_COLOR);
  }

  public AirMapPolyline(T object, List<LatLng> points, long id, int strokeWidth, int strokeColor) {
    this.object = object;
    this.points = points;
    this.id = id;
    this.strokeWidth = strokeWidth;
    this.strokeColor = strokeColor;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public List<LatLng> getPoints() {
    return points;
  }

  public void setPoints(List<LatLng> points) {
    this.points = points;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public T getObject() {
    return object;
  }

  public void setObject(T object) {
    this.object = object;
  }

  public int getStrokeWidth() {
    return strokeWidth;
  }

  public int getStrokeColor() {
    return strokeColor;
  }

  /**
   * Add this polyline to the given {@link GoogleMap} instance
   *
   * @param googleMap the {@link GoogleMap} instance to which the polyline will be added
   */
  public void addToGoogleMap(GoogleMap googleMap) {
    // add the polyline and keep a reference so it can be removed
    googlePolyline = googleMap.addPolyline(new PolylineOptions()
        .addAll(points)
        .width(strokeWidth)
        .color(strokeColor));
  }

  /**
   * Remove this polyline from a GoogleMap (if it was added).
   *
   * @return true if the {@link Polyline} was removed
   */
  public boolean removeFromGoogleMap() {
    if (googlePolyline != null) {
      googlePolyline.remove();
      return true;
    }
    return false;
  }
}
