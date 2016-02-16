package com.airbnb.android.airmapview.listeners;

import android.view.View;
import android.widget.FrameLayout;

import com.airbnb.android.airmapview.AirMapMarker;

public interface InfoWindowCreator {

  /**
   * Creates the content and outline of native-looking info windows for Web maps.
   * @param airMarker - the marker to create the info window for.
   * @return a <code>View</code> with the info window outline and contents.
   */
  View createInfoWindow(AirMapMarker<?> airMarker);

  /**
   * Update the layout and positioning of the info window. Implementations <strong>must</strong>
   * set the {@link android.widget.FrameLayout.LayoutParams#gravity} value to indicate positioning.
   * @param layoutParams - the current layout parameters for the Info Window.
   */
  void updateInfoWindowLayout(FrameLayout.LayoutParams layoutParams);
}
