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
  public void setMarker(Marker marker) {
    this.marker = marker;
  }

  public Marker getMarker() {
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

    public Builder object(T object) {
      this.object = object;
      return this;
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder position(LatLng position) {
      this.position = position;
      return this;
    }

    public Builder anchor(float u, float v) {
      this.anchorU = u;
      this.anchorV = v;
      return this;
    }

    public Builder infoWindowAnchor(float u, float v) {
      this.infoWindowAnchorU = u;
      this.infoWindowAnchorV = v;
      return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder snippet(String snippet) {
      this.snippet = snippet;
      return this;
    }

    public Builder iconId(int iconId) {
      this.iconId = iconId;
      return this;
    }

    public Builder bitmap(Bitmap bitmap) {
      this.bitmap = bitmap;
      return this;
    }

    public Builder draggable(boolean draggable) {
      this.draggable = draggable;
      return this;
    }

    public Builder visible(boolean visible) {
      this.visible = visible;
      return this;
    }

    public Builder flat(boolean flat) {
      this.flat = flat;
      return this;
    }

    public Builder rotation(float rotation) {
      this.rotation = rotation;
      return this;
    }

    public Builder alpha(float alpha) {
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
