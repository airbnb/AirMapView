package com.airbnb.android.airmapview;

/**
 * AirMapView map that uses the web based Mapbox implementation.
 */
public class MapboxWebMapViewBuilder implements AirMapViewBuilder<WebViewMapFragment, AirMapType> {

  private AirMapType options;
  private final String accessToken;
  private final String mapId;

  /**
   * Constructor
   *
   * @param accessToken Mapbox Access Token
   * @param mapId Mapbox Map Id
   */
  public MapboxWebMapViewBuilder(String accessToken, String mapId) {
    super();
    this.accessToken = accessToken;
    this.mapId = mapId;
  }

  @Override
  public AirMapViewBuilder<WebViewMapFragment, AirMapType> withOptions(AirMapType options) {
    this.options = options;
    return this;
  }

  /**
   * Build the map fragment with the requested options
   *
   * @return The {@link WebViewMapFragment} map fragment.
   */
  @Override
  public WebViewMapFragment build() {
    if (options == null) {
      options = new MapboxWebMapType(accessToken, mapId);
    }
    if (options instanceof MapboxWebMapType) {
      return MapboxWebViewMapFragment.newInstance(options);
    }
    throw new IllegalStateException("Unable to build MapboxWebMapViewFragment." +
        "  options == '" + options + "'");
  }
}
