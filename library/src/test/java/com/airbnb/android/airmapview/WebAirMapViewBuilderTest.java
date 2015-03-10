package com.airbnb.android.airmapview;

import android.os.Bundle;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WebAirMapViewBuilderTest {
    @Test @Ignore("Can't really test this right now since we can' mock the Bundle")
    public void shouldReturnGoogleWebAirMapViewByDefault() {
        WebAirMapViewBuilder factory = new WebAirMapViewBuilder();
        assertThat(factory.build(), instanceOf(GoogleWebViewMapFragment.class));
    }

    @Test
    public void shouldBuildGoogleWebAirMapViewWithOptions() {
        WebAirMapViewBuilder factory = new WebAirMapViewBuilder();
        GoogleWebMapType mapType = mock(GoogleWebMapType.class);
        when(mapType.toBundle()).thenReturn(new Bundle());
        assertThat(factory.withOptions(mapType).build(), instanceOf(GoogleWebViewMapFragment.class));
    }

    @Test
    public void shouldBuildGoogleChinaWebAirMapViewWithOptions() {
        WebAirMapViewBuilder factory = new WebAirMapViewBuilder();
        GoogleChinaMapType mapType = mock(GoogleChinaMapType.class);
        when(mapType.toBundle()).thenReturn(new Bundle());
        assertThat(factory.withOptions(mapType).build(), instanceOf(GoogleChinaWebViewMapFragment.class));
    }
}
