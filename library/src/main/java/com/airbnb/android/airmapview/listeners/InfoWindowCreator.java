package com.airbnb.android.airmapview.listeners;

import android.view.View;

import com.airbnb.android.airmapview.AirMapMarker;

public interface InfoWindowCreator {
  View createInfoWindow(AirMapMarker<?> airMarker);
}
