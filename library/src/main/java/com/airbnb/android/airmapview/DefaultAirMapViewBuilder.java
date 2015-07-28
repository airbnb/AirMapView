package com.airbnb.android.airmapview;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Use this class to request an AirMapView builder.
 */
public class DefaultAirMapViewBuilder {

  private final boolean isGooglePlayServicesAvailable;
  private final Context context;

  /**
   * Default {@link DefaultAirMapViewBuilder} constructor.
   *
   * @param context The application context.
   */
  public DefaultAirMapViewBuilder(Context context) {
    this(context,
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS);
  }

  /**
   * @param isGooglePlayServicesAvailable Whether or not Google Play services is available on the
   *                                      device. If you set this to true and it is not available,
   *                                      bad things can happen.
   */
  public DefaultAirMapViewBuilder(Context context, boolean isGooglePlayServicesAvailable) {
    this.isGooglePlayServicesAvailable = isGooglePlayServicesAvailable;
    this.context = context;
  }

  /**
   * Returns the first/default supported AirMapView implementation in order of preference, as
   * defined by {@link AirMapViewTypes}.
   */
  public AirMapViewBuilder builder() {
    if (isGooglePlayServicesAvailable) {
      return new NativeAirMapViewBuilder();
    }
    return getWebMapViewBuilder();
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
        return getWebMapViewBuilder();
    }
    throw new UnsupportedOperationException("Requested map type is not supported");
  }

  /**
   * Decides what the Map Web provider should be used and generates a builder for it.
   *
   * @return The AirMapViewBuilder for the selected Map Web provider.
   */
  public AirMapViewBuilder getWebMapViewBuilder() {
    if (context != null) {
      try {
        ApplicationInfo ai = context.getPackageManager()
            .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        Bundle bundle = ai.metaData;
        String accessToken = bundle.getString("com.mapbox.ACCESS_TOKEN");
        String mapId = bundle.getString("com.mapbox.MAP_ID");

        if (!TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(mapId)) {
          return new MapboxWebMapViewBuilder(accessToken, mapId);
        }
      } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
      }
    }
    return new WebAirMapViewBuilder();
  }
}
