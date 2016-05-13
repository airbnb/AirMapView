package com.airbnb.android.airmapview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLngBounds;

public class AirMapGroundOverlay {

  private LatLngBounds bounds;

  private String imageUrl;
  private Bitmap bitmap;
  private @DrawableRes int imageId;

  private final float bearing;
  private final float zIndex;
  private final boolean visible;
  private GroundOverlay googleOverlay;
  private long id;

  private AirMapGroundOverlay(long id, LatLngBounds bounds, String imageUrl, @DrawableRes int imageId, Bitmap bitmap, float bearing, float zIndex, boolean visible) {
    this.id = id;
    this.bounds = bounds;
    this.imageUrl = imageUrl;
    this.imageId = imageId;
    this.bitmap = bitmap;
    this.bearing = bearing;
    this.zIndex = zIndex;
    this.visible = visible;
  }

  public GroundOverlayOptions getOverlayOptions() {
    return new GroundOverlayOptions()
        .positionFromBounds(bounds)
        .image(imageId != 0 ? BitmapDescriptorFactory.fromResource(imageId) : BitmapDescriptorFactory.fromBitmap(bitmap))
        .bearing(bearing)
        .zIndex(zIndex)
        .visible(visible);
  }



  void setGoogleOverlay(GroundOverlay googleOverlay) {
    this.googleOverlay = googleOverlay;
  }

  GroundOverlay getGoogleOverlay() {
    return googleOverlay;
  }

  public long getId() {
    return id;
  }

  public LatLngBounds getBounds() {
    return bounds;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public Bitmap getBitmap(Resources resources) {
    return imageId != 0 ? BitmapFactory.decodeResource(resources, imageId) : bitmap;
  }

  public static class Builder {
    private long id;
    private LatLngBounds bounds;
    private String imageUrl;
    private @DrawableRes int imageId;
    private Bitmap bitmap;
    private float bearing;
    private float zIndex;
    private boolean visible = true;

    public Builder() {

    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder positionFromBounds(LatLngBounds bounds) {
      this.bounds = bounds;
      return this;
    }

    // TODO: imageUrl will not work for native maps
    public Builder imageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
      return this;
    }

    public Builder imageId(@DrawableRes int imageId) {
      this.imageId = imageId;
      return this;
    }

    public Builder bitmap(Bitmap bitmap) {
      this.bitmap = bitmap;
      return this;
    }

    public Builder bearing(float bearing) {
      this.bearing = bearing;
      return this;
    }

    public Builder zIndex(float zIndex) {
      this.zIndex = zIndex;
      return this;
    }

    public Builder visible(boolean visible) {
      this.visible = visible;
      return this;
    }

    public AirMapGroundOverlay build() {
      if (imageId == 0 && imageUrl == null) {
        throw new IllegalStateException("You should provide an imageId or an imageUrl to your ground overlay.");
      }
      return new AirMapGroundOverlay(id, bounds, imageUrl, imageId, bitmap, bearing, zIndex, visible);
    }
  }
}
