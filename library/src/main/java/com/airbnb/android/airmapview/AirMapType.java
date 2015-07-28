package com.airbnb.android.airmapview;

import android.content.res.Resources;
import android.os.Bundle;
import java.util.Locale;

/** Defines maps to be used with {@link com.airbnb.android.airmapview.WebViewMapFragment} */
public class AirMapType {

  private static final String ARG_MAP_DOMAIN = "map_domain";
  private static final String ARG_FILE_NAME = "map_file_name";
  private static final String ARG_MAP_URL = "map_url";
  private final String fileName;
  private final String mapUrl;
  private final String domain;

  public AirMapType(String fileName, String mapUrl, String domain) {
    this.fileName = fileName;
    this.mapUrl = mapUrl;
    this.domain = domain;
  }

  /** @return the name of the HTML file in /assets */
  String getFileName() {
    return fileName;
  }

  /** @return the base URL for a maps API */
  String getMapUrl() {
    return mapUrl;
  }

  /** @return domain of the maps API to use */
  String getDomain() {
    return domain;
  }

  public Bundle toBundle() {
    return toBundle(new Bundle());
  }

  public Bundle toBundle(Bundle bundle) {
    bundle.putString(ARG_MAP_DOMAIN, getDomain());
    bundle.putString(ARG_MAP_URL, getMapUrl());
    bundle.putString(ARG_FILE_NAME, getFileName());
    return bundle;
  }

  public static AirMapType fromBundle(Bundle bundle) {
    return new AirMapType(
        bundle.getString(ARG_FILE_NAME, ""),
        bundle.getString(ARG_MAP_URL, ""),
        bundle.getString(ARG_MAP_DOMAIN, ""));
  }

  public String getMapData(Resources resources) {
    return AirMapUtils.getStringFromFile(resources, fileName)
        .replace("MAPURL", mapUrl)
        .replace("LANGTOKEN", Locale.getDefault().getLanguage())
        .replace("REGIONTOKEN", Locale.getDefault().getCountry());
  }

  @SuppressWarnings("RedundantIfStatement")
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || !(o instanceof AirMapType)) {
      return false;
    }

    AirMapType that = (AirMapType) o;

    if (domain != null ? !domain.equals(that.domain) : that.domain != null) {
      return false;
    }

    if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) {
      return false;
    }

    if (mapUrl != null ? !mapUrl.equals(that.mapUrl) : that.mapUrl != null) {
      return false;
    }

    return true;
  }

  @Override public int hashCode() {
    int result = fileName != null ? fileName.hashCode() : 0;
    result = 31 * result + (mapUrl != null ? mapUrl.hashCode() : 0);
    result = 31 * result + (domain != null ? domain.hashCode() : 0);
    return result;
  }
}
