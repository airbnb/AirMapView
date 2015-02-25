package com.airbnb.android.airmapview;

/**
 * Interface for defining maps to be used with {@link WebViewMapFragment}
 */
public interface AirMapType {
    /**
     * @return the name of the HTML file in /assets
     */
    String getFileName();

    /**
     * @return the base URL for a maps API
     */
    String getMapUrl();

    /**
     * @return domain of the maps API to use
     */
    String getDomain();
}
