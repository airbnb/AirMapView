package com.airbnb.android.airmapview;

import com.google.android.gms.maps.GoogleMapOptions;

/**
 * AirMapView map that uses the native Google Maps implementation. IMPORTANT: In order to use this,
 * Google Play Services needs to be installed on the device.
 */
public class NativeAirMapViewBuilder
    implements AirMapViewBuilder<NativeGoogleMapFragment, AirGoogleMapOptions> {

  private AirGoogleMapOptions options;

  @Override public AirMapViewBuilder<NativeGoogleMapFragment, AirGoogleMapOptions> withOptions(
      AirGoogleMapOptions options) {
    this.options = options;
    return this;
  }

  /**
   * Build the map fragment with the requested options
   *
   * @return The {@link NativeGoogleMapFragment} map fragment.
   */
  @Override public NativeGoogleMapFragment build() {
    if (options == null) {
      options = new AirGoogleMapOptions(new GoogleMapOptions());
    }
    return NativeGoogleMapFragment.newInstance(options);
  }
}
