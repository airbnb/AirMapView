package com.airbnb.android.airmapview.listeners;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

public interface OnSnapshotReadyListener {

    void onSnapshotReady(@Nullable Bitmap bitmap);
}
