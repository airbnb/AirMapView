package com.airbnb.android.airmapview;

import android.text.TextUtils;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AirMapMarker<T> {

    private T mObject;
    private long mId;
    private LatLng mLatLng;
    private String mTitle;
    private int mIconId;

    public AirMapMarker(LatLng latLng, long id) {
        mId = id;
        mLatLng = latLng;
    }

    public AirMapMarker(T object, LatLng location, long id) {
        mObject = object;
        mLatLng = location;
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getIconId() {
        return mIconId;
    }

    public T getObject() {
        return mObject;
    }

    public void setObject(T object) {
        mObject = object;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setIconId(int iconId) {
        mIconId = iconId;
    }

    public MarkerOptions getGoogleMarkerOptions() {
        MarkerOptions options = new MarkerOptions();

        options.position(mLatLng);

        if (!TextUtils.isEmpty(mTitle)) {
            options.title(mTitle);
        }

        if (mIconId > 0) {
            options.icon(BitmapDescriptorFactory.fromResource(mIconId));
        }

        return options;
    }
}
