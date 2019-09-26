package com.airbnb.android.airmapview;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * FIXME: Remove this class when AppCompat bug is fixed.
 * In short, AppCompat 1.1.0 makes WebView crashes on Android 5.0~5.1.1 (Lollipop).
 * See https://stackoverflow.com/questions/41025200/android-view-inflateexception-error-inflating-class-android-webkit-webview.
 */
public class FixedWebView extends WebView {
  public FixedWebView(Context context) {
    super(fixedContext(context));
  }

  public FixedWebView(Context context, AttributeSet attrs) {
    super(fixedContext(context), attrs);
  }

  public FixedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(fixedContext(context), attrs, defStyleAttr);
  }

  private static Context fixedContext(Context context) {
    if (Build.VERSION_CODES.LOLLIPOP == Build.VERSION.SDK_INT ||
        Build.VERSION_CODES.LOLLIPOP_MR1 == Build.VERSION.SDK_INT) {
      return context.createConfigurationContext(new Configuration());
    }
    return context;
  }
}
