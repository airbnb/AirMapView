package com.airbnb.android.airmapview;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptor;
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

  private AirMapMarker(T object, long id, LatLng position, String title, String snippet,
      float anchorU, float anchorV, boolean draggable, boolean visible, boolean flat,
      float rotation, float infoWindowAnchorU, float infoWindowAnchorV, float alpha,
      BitmapDescriptor icon) {
    this.object = object;
    this.id = id;
    this.markerOptions = new MarkerOptions()
        .title(title)
        .position(position)
        .icon(icon)
        .snippet(snippet)
        .anchor(anchorU, anchorV)
        .draggable(draggable)
        .visible(visible)
        .flat(flat)
        .rotation(rotation)
        .infoWindowAnchor(infoWindowAnchorU, infoWindowAnchorV)
        .alpha(alpha);
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
    private String title;
    private String snippet;
    private int iconId;
    private Bitmap bitmap;
    private float anchorU;
    private float anchorV;
    private float infoWindowAnchorU;
    private float infoWindowAnchorV;
    private boolean draggable;
    private boolean visible;
    private boolean flat;
    private float rotation;
    private float alpha;
    private LatLng position;

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
      this.position = position;
      return this;
    }

    public Builder<T> anchor(float u, float v) {
      this.anchorU = u;
      this.anchorV = v;
      return this;
    }

    public Builder<T> infoWindowAnchor(float u, float v) {
      this.infoWindowAnchorU = u;
      this.infoWindowAnchorV = v;
      return this;
    }

    public Builder<T> title(String title) {
      this.title = title;
      return this;
    }

    public Builder<T> snippet(String snippet) {
      this.snippet = snippet;
      return this;
    }

    public Builder<T> iconId(int iconId) {
      this.iconId = iconId;
      return this;
    }

    public Builder<T> bitmap(Bitmap bitmap) {
      this.bitmap = bitmap;
      return this;
    }

    public Builder<T> draggable(boolean draggable) {
      this.draggable = draggable;
      return this;
    }

    public Builder<T> visible(boolean visible) {
      this.visible = visible;
      return this;
    }

    public Builder<T> flat(boolean flat) {
      this.flat = flat;
      return this;
    }

    public Builder<T> rotation(float rotation) {
      this.rotation = rotation;
      return this;
    }

    public Builder<T> alpha(float alpha) {
      this.alpha = alpha;
      return this;
    }

    public AirMapMarker<T> build() {
      BitmapDescriptor icon;
      try {
        if (bitmap != null) {
          icon = BitmapDescriptorFactory.fromBitmap(bitmap);
        } else if (iconId > 0) {
          icon = BitmapDescriptorFactory.fromResource(iconId);
        } else {
          icon = null;
        }
      } catch (NullPointerException e) {
        // google play services is not available
        icon = null;
      }
      return new AirMapMarker<>(object, id, position, title, snippet, anchorU, anchorV, draggable,
          visible, flat, rotation, infoWindowAnchorU, infoWindowAnchorV, alpha, icon);
    }
  }
}
