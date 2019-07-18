package com.airbnb.android.airmapview;

import android.graphics.Color;

import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class AirMapPolygon<T> {

  private static final int STROKE_WIDTH = 1;
  private static final int STROKE_COLOR = Color.BLUE;

  private final T object;
  private final long id;
  private final PolygonOptions polygonOptions;
  private Polygon googlePolygon;

  public AirMapPolygon(T object, long id, PolygonOptions polygonOptions) {
    this.object = object;
    this.id = id;
    this.polygonOptions = polygonOptions;
  }

  public T getObject() {
    return object;
  }

  public long getId() {
    return id;
  }

  public PolygonOptions getPolygonOptions() {
    return polygonOptions;
  }

  public Polygon getGooglePolygon() {
    return googlePolygon;
  }

  public void setGooglePolygon(Polygon googlePolygon) {
    this.googlePolygon = googlePolygon;
  }

  public static class Builder<T> {
    private final PolygonOptions polygonOptions = new PolygonOptions();
    private T object;
    private long id;

    public Builder() {
      polygonOptions.strokeWidth(STROKE_WIDTH);
      polygonOptions.strokeColor(STROKE_COLOR);
    }

    public Builder<T> object(T object) {
      this.object = object;
      return this;
    }

    public Builder<T> id(long id) {
      this.id = id;
      return this;
    }

    public Builder<T> strokeColor(int color) {
      polygonOptions.strokeColor(color);
      return this;
    }

    public Builder<T> strokeWidth(float width) {
      this.polygonOptions.strokeWidth(width);
      return this;
    }

    public Builder<T> fillColor(int color) {
      this.polygonOptions.fillColor(color);
      return this;
    }

    public Builder<T> geodesic(boolean geodesic) {
      this.polygonOptions.geodesic(geodesic);
      return this;
    }

    public Builder<T> zIndex(float zIndex) {
      this.polygonOptions.zIndex(zIndex);
      return this;
    }

    public Builder<T> visible(boolean visible) {
      this.polygonOptions.visible(visible);
      return this;
    }

    public Builder<T> add(LatLng point) {
      this.polygonOptions.add(point);
      return this;
    }

    public Builder<T> add(LatLng... points) {
      this.polygonOptions.add(points);
      return this;
    }

    public Builder<T> addAll(@NonNull Iterable<LatLng> points) {
      this.polygonOptions.addAll(points);
      return this;
    }

    public Builder<T> addHole(@NonNull Iterable<LatLng> points) {
      this.polygonOptions.addHole(points);
      return this;
    }

    public AirMapPolygon<T> build() {
      return new AirMapPolygon<>(object, id, polygonOptions);
    }
  }
}
