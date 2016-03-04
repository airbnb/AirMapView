package com.airbnb.android.airmapview;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Wrapper around {@link MarkerOptions}. Keeps record of data needed to display map markers, as
 * well as an object T associated with the marker.
 */
public class AirMapMarker<T> {

  private final T object;
  private final long id;
  private final MarkerOptions markerOptions;
  private Marker marker;

  private AirMapMarker(T object, long id, MarkerOptions markerOptions) {
    this.object = object;
    this.id = id;
    this.markerOptions = markerOptions;
  }

  public T object() {
    return object;
  }

  public long getId() {
    return id;
  }

  public LatLng getLatLng() {
    return markerOptions.getPosition();
  }

  void setLatLng(LatLng latLng) {
    markerOptions.position(latLng);
  }

  public String getTitle() {
    return markerOptions.getTitle();
  }

  public String getSnippet() {
    return markerOptions.getSnippet();
  }

  public MarkerOptions getMarkerOptions() {
    return markerOptions;
  }

  /** Sets a marker associated to this object */
  void setGoogleMarker(Marker marker) {
    this.marker = marker;
  }

  Marker getMarker() {
    return marker;
  }

  public static class Builder<T> {
    private T object;
    private long id;
    private final MarkerOptions markerOptions = new MarkerOptions();

    public Builder() {
    }

    public Builder<T> object(T object) {
      this.object = object;
      return this;
    }

    public Builder<T> id(long id) {
      this.id = id;
      return this;
    }

    public Builder<T> position(LatLng position) {
      markerOptions.position(position);
      return this;
    }

    public Builder<T> anchor(float u, float v) {
      markerOptions.anchor(u, v);
      return this;
    }

    public Builder<T> infoWindowAnchor(float u, float v) {
      markerOptions.infoWindowAnchor(u, v);
      return this;
    }

    public Builder<T> title(String title) {
      markerOptions.title(title);
      return this;
    }

    public Builder<T> snippet(String snippet) {
      markerOptions.snippet(snippet);
      return this;
    }

    public Builder<T> iconId(int iconId) {
      try {
        markerOptions.icon(BitmapDescriptorFactory.fromResource(iconId));
      } catch (NullPointerException ignored) {
        // google play services is not available
      }
      return this;
    }

    public Builder<T> bitmap(Bitmap bitmap) {
      try {
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
      } catch (NullPointerException ignored) {
        // google play services is not available
      }
      return this;
    }

    public Builder<T> draggable(boolean draggable) {
      markerOptions.draggable(draggable);
      return this;
    }

    public Builder<T> visible(boolean visible) {
      markerOptions.visible(visible);
      return this;
    }

    public Builder<T> flat(boolean flat) {
      markerOptions.flat(flat);
      return this;
    }

    public Builder<T> rotation(float rotation) {
      markerOptions.rotation(rotation);
      return this;
    }

    public Builder<T> alpha(float alpha) {
      markerOptions.alpha(alpha);
      return this;
    }

    public AirMapMarker<T> build() {
      return new AirMapMarker<>(object, id, markerOptions);
    }
  }
}
