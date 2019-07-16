package com.airbnb.android.airmapview

import androidx.core.view.doOnLayout

fun AirMapView.doWhenMapIsLaidOut(runnable: Runnable) = doOnLayout { runnable.run() }