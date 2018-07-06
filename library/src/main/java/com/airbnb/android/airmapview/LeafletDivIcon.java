package com.airbnb.android.airmapview;

public class LeafletDivIcon {
  private final String html;
  private final int width;
  private final int height;

  public LeafletDivIcon(String html, int width, int height) {
    this.html = html;
    this.width = width;
    this.height = height;
  }

  public String getHtml() {
    return html;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
