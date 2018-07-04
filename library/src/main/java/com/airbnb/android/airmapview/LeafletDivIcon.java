package com.airbnb.android.airmapview;

public class LeafletDivIcon {
  private String html;
  private int width;
  private int height;

  public LeafletDivIcon() {
  }

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

  public void setHtml(String html) {
    this.html = html;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }
}
