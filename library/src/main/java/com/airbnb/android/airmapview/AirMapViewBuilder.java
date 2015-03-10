package com.airbnb.android.airmapview;

public interface AirMapViewBuilder<T extends AirMapInterface, Q> {
    AirMapViewBuilder<T, Q> withOptions(Q arg);
    T build();
}
