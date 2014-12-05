package com.airbnb.android.airmapview;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Helper class for keeping record of data needed to
 * display a polyline, as well as an optional object T
 * associated with the polyline.
 */
public class AirMapPolyline<T> {

    private static final int STROKE_WIDTH = 1;
    private static final int STROKE_COLOR = Color.BLUE;

    private T mObject;
    private int mStrokeWidth;
    private long mId;
    private List<LatLng> mPoints;
    private String mTitle;
    private int mStrokeColor;
    private Polyline mGooglePolyline;

    public AirMapPolyline(List<LatLng> points, long id) {
        this(null, points, id);
    }

    public AirMapPolyline(T object, List<LatLng> points, long id) {
        this(object, points, id, STROKE_WIDTH, STROKE_COLOR);
    }

    public AirMapPolyline(T object, List<LatLng> points, long id, int strokeWidth, int strokeColor) {
        mObject = object;
        mPoints = points;
        mId = id;
        mStrokeWidth = strokeWidth;
        mStrokeColor = strokeColor;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public List<LatLng> getPoints() {
        return mPoints;
    }

    public void setPoints(List<LatLng> points) {
        mPoints = points;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public T getObject() {
        return mObject;
    }

    public void setObject(T object) {
        mObject = object;
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    /**
     * Add this polyline to the given {@link GoogleMap} instance
     *
     * @param googleMap the {@link GoogleMap} instance to which the polyline will be added
     */
    public void addToGoogleMap(GoogleMap googleMap) {
        // add the polyline and keep a reference so it can be removed
        mGooglePolyline = googleMap.addPolyline(new PolylineOptions()
                                        .addAll(mPoints)
                                        .width(mStrokeWidth)
                                        .color(mStrokeColor));
    }

    /**
     * Remove this polyline from a GoogleMap (if it was added).
     *
     * @return true if the {@link Polyline} was removed
     */
    public boolean removeFromGoogleMap() {
        if (mGooglePolyline != null) {
            mGooglePolyline.remove();
            return true;
        }
        return false;
    }
}
