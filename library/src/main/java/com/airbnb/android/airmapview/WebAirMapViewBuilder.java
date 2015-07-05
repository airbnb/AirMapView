package com.airbnb.android.airmapview;

/**
 * AirMapView map that uses the web based Google Maps implementation.
 */
public class WebAirMapViewBuilder implements AirMapViewBuilder<WebViewMapFragment, AirMapType> {

  private AirMapType options;
  private String mapboxMapId;
  private String mapboxAccessToken;

  @Override public AirMapViewBuilder<WebViewMapFragment, AirMapType> withOptions(AirMapType options) {
    this.options = options;
    return this;
  }

  /**
   * Set Mapbox Access Token for Web map
   * @link https://www.mapbox.com/help/define-access-token/
   * @param mapboxAccessToken Mapbox Access Token
   * @return WebAirMapViewBuilder
   */
  public WebAirMapViewBuilder setMapboxAccessToken(String mapboxAccessToken) {
    this.mapboxAccessToken = mapboxAccessToken;
    return this;
  }

  /**
   * Set Mapbox Map Id
   * @link https://www.mapbox.com/help/define-map-id/
   * @param mapboxMapId
   * @return WebAirMapViewBuilder
   */
  public WebAirMapViewBuilder setMapboxMapId(String mapboxMapId) {
    this.mapboxMapId = mapboxMapId;
    return this;
  }

  /**
   * Build the map fragment with the requested options
   *
   * @return The {@link WebViewMapFragment} map fragment.
   */
  @Override public WebViewMapFragment build() {
    if (options == null) {
      options = new MapboxWebMapType(mapboxAccessToken, mapboxMapId);
//      options = new GoogleWebMapType();
    }
    if (options instanceof MapboxWebMapType) {
      return MapboxWebViewMapFragment.newInstance(options);
    }
    if (options instanceof GoogleChinaMapType) {
      return GoogleChinaWebViewMapFragment.newInstance(options);
    }
    return null;
  }
}
