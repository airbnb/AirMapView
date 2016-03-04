package com.airbnb.airmapview.sample;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

public class Util {

  private static final String LOGTAG = "Util";

  public static String readFromRawResource(Context context, int resourceId) {
    InputStream resourceReader = context.getResources().openRawResource(resourceId);
    Writer writer = new StringWriter();
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
      String line = reader.readLine();
      while (line != null) {
        writer.write(line);
        line = reader.readLine();
      }
    } catch (Exception e) {
      Log.e(LOGTAG, "Unhandled exception while reading from resource", e);
    } finally {
      try {
        resourceReader.close();
      } catch (Exception e) {
        Log.e(LOGTAG, "Unhandled exception while closing resource", e);
      }
    }

    return writer.toString();
  }

}
