package com.airbnb.android.airmapview;

import android.os.Bundle;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NativeAirMapViewBuilderTest {
    @Test
    public void shouldBuildNativeAirMapView() {
        NativeAirMapViewBuilder builder = new NativeAirMapViewBuilder();
        AirGoogleMapOptions options = mock(AirGoogleMapOptions.class);
        when(options.toBundle()).thenReturn(new Bundle());
        assertThat(builder.withOptions(options).build(), instanceOf(AirGoogleMapFragment.class));
    }
}
