package com.airbnb.android.airmapview;

import android.view.View;

/**
 * Adapter interface that displays info windows for {@link AirMapMarker}.
 */
public interface AirInfoWindowAdapter {

  /**
   * Gets a custom <code>View</code> to contain the contents of the info window.
   *
   * @param marker the <code>AirMapMarker</code> that needs its info window.
   * @return a <code>View</code> to be used to style the info window container, or
   * <code>null</code> if the map provider's default should be used.
   */
  View getInfoWindow(AirMapMarker<?> marker);

  /**
   * Gets a <code>View</code> with the contents of the info window.
   * @param marker - the <code>AirMapMarker</code> that needs its info window.
   * @return a <code>View</code> with the contents of the info window.
   */
  View getInfoContents(AirMapMarker<?> marker);
}
