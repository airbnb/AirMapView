package com.airbnb.android.airmapview;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.content.Context;

/**
 * Use this class to request an AirMapView builder.
 */
public class DefaultAirMapViewBuilder {

  private boolean isGooglePlayServicesAvailable;

  /**
   * Default {@link DefaultAirMapViewBuilder} constructor.
   *
   * @param context The application context.
   */
  public DefaultAirMapViewBuilder(Context context) {
    this(GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS);
  }

  /**
   * @param isGooglePlayServicesAvailable Whether or not Google Play services is available on the
   *                                      device. If you set this to true and it is not available,
   *                                      bad things can happen.
   */
  public DefaultAirMapViewBuilder(boolean isGooglePlayServicesAvailable) {
    this.isGooglePlayServicesAvailable = isGooglePlayServicesAvailable;
  }

  /**
   * Returns the first/default supported AirMapView implementation in order of preference, as
   * defined by {@link AirMapViewTypes}.
   */
  public AirMapViewBuilder builder() {
    if (isGooglePlayServicesAvailable) {
      return new NativeAirMapViewBuilder();
    }
    return new WebAirMapViewBuilder();
  }

  /**
   * Returns the AirMapView implementation as requested by the mapType argument. Use this method if
   * you need to request a specific AirMapView implementation that is not necessarily the preferred
   * type. For example, you can use it to explicit request a web-based map implementation.
   *
   * @param mapType Map type for the requested AirMapView implementation.
   * @return An {@link AirMapViewBuilder} for the requested {@link AirMapViewTypes} mapType.
   */
  public AirMapViewBuilder builder(AirMapViewTypes mapType) {
    switch (mapType) {
      case NATIVE:
        if (isGooglePlayServicesAvailable) {
          return new NativeAirMapViewBuilder();
        }
        break;
      case WEB:
        return new WebAirMapViewBuilder();
    }
    throw new UnsupportedOperationException("Requested map type is not supported");
  }

}
