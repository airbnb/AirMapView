package com.airbnb.android.airmapview.listeners;

import android.graphics.Bitmap;
import androidx.annotation.Nullable;

public interface OnSnapshotReadyListener {
  void onSnapshotReady(@Nullable Bitmap bitmap);
}
