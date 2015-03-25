package com.airbnb.android.airmapview;

import android.os.Bundle;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AirMapTypeTest {

  @Test public void shouldConvertToBundle() {
    Bundle bundle = mock(Bundle.class);
    AirMapType mapType = new GoogleWebMapType();
    mapType.toBundle(bundle);

    verify(bundle).putString("map_domain", mapType.getDomain());
    verify(bundle).putString("map_url", mapType.getMapUrl());
    verify(bundle).putString("map_file_name", mapType.getFileName());
  }

  @Test public void shouldConstructFromBundle() {
    GoogleWebMapType mapType = new GoogleWebMapType();
    Bundle bundle = mock(Bundle.class);

    when(bundle.getString("map_domain", "")).thenReturn(mapType.getDomain());
    when(bundle.getString("map_url", "")).thenReturn(mapType.getMapUrl());
    when(bundle.getString("map_file_name", "")).thenReturn(mapType.getFileName());

    assertThat(AirMapType.fromBundle(bundle), IsEqual.<AirMapType>equalTo(mapType));
  }
}
