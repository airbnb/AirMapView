package com.airbnb.android.airmapview;

import android.support.annotation.ColorInt;

import com.google.maps.android.geojson.GeoJsonPolygonStyle;

public class AirMapGeoJsonLayer {

  protected final String geoJson;
  protected final float strokeWidth;
  protected final int strokeColor;
  protected final int fillColor;

  private AirMapGeoJsonLayer(String geoJson, float strokeWidth, int strokeColor, int fillColor) {
    this.geoJson = geoJson;
    this.strokeWidth = strokeWidth;
    this.strokeColor = strokeColor;
    this.fillColor = fillColor;
  }

  public static class Builder {

    private final String json;

    // init with default styles
    private final GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();

    public Builder(String json) {
      this.json = json;
    }

    public Builder fillColor(@ColorInt int color) {
      style.setFillColor(color);
      return this;
    }

    public Builder strokeColor(@ColorInt int color) {
      style.setStrokeColor(color);
      return this;
    }

    public Builder strokeWidth(float width) {
      style.setStrokeWidth(width);
      return this;
    }

    public AirMapGeoJsonLayer build() {
      return new AirMapGeoJsonLayer(
          json, style.getStrokeWidth(), style.getStrokeColor(), style.getFillColor());
    }
  }

}
