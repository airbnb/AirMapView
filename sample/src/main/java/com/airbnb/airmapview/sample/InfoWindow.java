package com.airbnb.airmapview.sample;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.android.airmapview.AirMapMarker;

/**
 * Simple Info Window contents with a title and description
 */
public class InfoWindow extends LinearLayout {

  private final String title;

  private final String description;

  public InfoWindow(Context context, String title, String description) {
    super(context);
    this.title = title;
    this.description = description;
    inflateView();
  }

  public InfoWindow(Context context, AttributeSet attrs, String title, String description) {
    super(context, attrs);
    this.title = title;
    this.description = description;
    inflateView();
  }

  public InfoWindow(Context context, AttributeSet attrs, int defStyleAttr, String title,
                    String description) {
    super(context, attrs, defStyleAttr);
    this.title = title;
    this.description = description;
    inflateView();
  }

  @TargetApi(21)
  public InfoWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes,
                    String title, String description) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.title = title;
    this.description = description;
    inflateView();
  }

  InfoWindow(Context context) {
    super(context);
    this.title = "Sample title";
    this.description = "Sample description";
    inflateView();
  }

  private void inflateView() {
    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
        .LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.info_window, this, true);
    TextView titleText = (TextView) this.findViewById(R.id.info_window_title);
    titleText.setText(title);
    TextView descText = (TextView) this.findViewById(R.id.info_window_desc);
    descText.setText(description);
  }
}
