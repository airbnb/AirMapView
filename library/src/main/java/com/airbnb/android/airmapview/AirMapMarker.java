package com.airbnb.android.airmapview;

import android.text.TextUtils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Helper class for keeping record of data needed to
 * display map markers, as well as an object T associated with the marker
 */
public class AirMapMarker<T> {

    private T mObject;
    private long mId;
    private LatLng mLatLng;
    private String mTitle;
    private int mIconId;
    private Marker mGoogleMarker;

    public AirMapMarker(LatLng latLng, long id) {
        this(null, latLng, id);
    }

    public AirMapMarker(T object, LatLng latLng, long id) {
        mObject = object;
        mLatLng = latLng;
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

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getIconId() {
        return mIconId;
    }

    public void setIconId(int iconId) {
        mIconId = iconId;
    }

    public T getObject() {
        return mObject;
    }

    public void setObject(T object) {
        mObject = object;
    }

    /**
     * Add this marker to the given {@link GoogleMap} instance
     *
     * @param googleMap the {@link GoogleMap} instance to which the marker will be added
     */
    public void addToGoogleMap(GoogleMap googleMap) {
        MarkerOptions options = new MarkerOptions();

        options.position(mLatLng);

        if (!TextUtils.isEmpty(mTitle)) {
            options.title(mTitle);
        }

        if (mIconId > 0) {
            options.icon(BitmapDescriptorFactory.fromResource(mIconId));
        }

        // add the marker and keep a reference so it can be removed
        mGoogleMarker = googleMap.addMarker(options);
    }

    /**
     * Remove this polyline from a GoogleMap (if it was added).
     *
     * @return true if the {@link com.google.android.gms.maps.model.Polyline} was removed
     */
    public boolean removeFromGoogleMap() {
        if (mGoogleMarker != null) {
            mGoogleMarker.remove();
            return true;
        }
        return false;
    }
}
