package com.airbnb.android.airmapview;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AirMapUtils {

  public static String getStringFromFile(Resources resources, String filePath) {
    try {
      InputStream is = resources.getAssets().open(filePath);
      String ret = convertStreamToString(is);
      is.close();
      return ret;
    } catch (IOException e) {
      throw new RuntimeException("unable to load asset " + filePath);
    }
  }

  public static String convertStreamToString(InputStream is) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      sb.append(line).append("\n");
    }
    reader.close();
    return sb.toString();
  }
}
