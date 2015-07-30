package com.airbnb.android.airmapview.listeners;

import android.view.View;

public interface InfoWindowCreator {
  // TODO: this should take an AirMapMarker instead of just the ID
  View createInfoWindow(long id);
}
