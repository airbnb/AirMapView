package com.airbnb.android.airmapview;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class DefaultAirMapViewBuilderTest {

  @Before public void setUp() {
  }

  @Test public void shouldReturnNativeAirMapViewByDefault() {
    DefaultAirMapViewBuilder factory = new DefaultAirMapViewBuilder(null, true);
    assertThat(factory.builder(), instanceOf(NativeAirMapViewBuilder.class));
  }

  @Test public void shouldReturnWebAirMapViewIfDefaultNotSupported() {
    DefaultAirMapViewBuilder factory = new DefaultAirMapViewBuilder(null, false);
    assertThat(factory.builder(), instanceOf(WebAirMapViewBuilder.class));
  }

  @Test public void shouldReturnNativeAirMapViewWhenRequestedExplicitly() {
    DefaultAirMapViewBuilder factory = new DefaultAirMapViewBuilder(null, true);
    AirMapViewBuilder builder = factory.builder(AirMapViewTypes.NATIVE);
    assertThat(builder, instanceOf(NativeAirMapViewBuilder.class));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowWhenRequestedNativeWebViewAndNotSupported() {
    DefaultAirMapViewBuilder factory = new DefaultAirMapViewBuilder(null, false);
    factory.builder(AirMapViewTypes.NATIVE);
  }

  @Test public void shouldReturnWebAirMapViewWhenRequestedExplicitly() {
    DefaultAirMapViewBuilder factory = new DefaultAirMapViewBuilder(null, false);
    AirMapViewBuilder builder = factory.builder(AirMapViewTypes.WEB);
    assertThat(builder, instanceOf(WebAirMapViewBuilder.class));
  }
}
