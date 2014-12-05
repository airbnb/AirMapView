package com.airbnb.android.airmapview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.List;

public class AirMapView extends FrameLayout {

    protected AirMapInterface mMapInterface;
    private MapCallbacks mMapCallbacks;
    private boolean mZOrderOnTop;

    public AirMapView(Context context) {
        super(context);
        init();
    }

    public AirMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AirMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.map_view, this);
    }

    public void onCreateView(FragmentManager fm, boolean zOrderOnTop, MapCallbacks callbacks) {
        if (callbacks == null) {
            throw new NullPointerException("Map callbacks may not be null");
        }
        mMapCallbacks = callbacks;
        mMapInterface = (AirMapInterface) fm.findFragmentById(R.id.map_frame);
        mZOrderOnTop = zOrderOnTop;

        if (mMapInterface == null) {
            mMapInterface = createMapInterface();

            fm.beginTransaction()
                    .replace(R.id.map_frame, (Fragment) mMapInterface)
                    .commit();

            fm.executePendingTransactions();
        }

        mMapInterface.setOnMapLoadedListener(new OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                mMapInterface.init();

                if (isInitialized()) {
                    // only send map Initialized callback if map initialized successfully
                    // initialization can fail if the map leaves the screen before it loads

                    mMapCallbacks.onMapInitialized();

                    mMapInterface.setOnCameraChangeListener(new WebViewMapFragment.OnCameraChangeListener() {
                        @Override
                        public void onCameraChanged(LatLng latLng, int zoom) {
                            mMapCallbacks.onCameraChanged(latLng, zoom);
                        }
                    });
                }
            }
        });
    }

    public final AirMapInterface getMapInterface() {
        return mMapInterface;
    }

    /**
     * Override this if you wish to add more map types
     */
    protected AirMapInterface createMapInterface() {
        if (ConnectionResult.SUCCESS == GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext())) {
            return AirGoogleMapFragment.newInstance(new GoogleMapOptions().zOrderOnTop(mZOrderOnTop));
        } else {
            return WebViewMapFragment.newInstance(new GoogleWebMapType());
        }
    }

    public void onDestroyView() {
        if (isInitialized()) {
            mMapInterface.setMyLocationEnabled(false);
        }
    }

    public int getZoom() {
        if (isInitialized()) {
            return mMapInterface.getZoom();
        }

        return -1;
    }

    public LatLng getCenter() {
        if (isInitialized()) {
            return mMapInterface.getCenter();
        }
        return null;
    }

    public boolean setCenter(LatLng latLng) {
        if (isInitialized()) {
            mMapInterface.setCenter(latLng);
            return true;
        }
        return false;
    }

    public void animateCenter(LatLng latLng) {
        if (isInitialized()) {
            mMapInterface.animateCenter(latLng);
        }
    }

    public void setZoom(int zoom) {
        if (isInitialized()) {
            mMapInterface.setZoom(zoom);
        }
    }

    public boolean setCenterZoom(LatLng latLng, int zoom) {
        if (isInitialized()) {
            mMapInterface.setCenterZoom(latLng, zoom);
            return true;
        }
        return false;
    }

    public boolean animateCenterZoom(LatLng latLng, int zoom) {
        if (isInitialized()) {
            mMapInterface.animateCenterZoom(latLng, zoom);
            return true;
        }
        return false;
    }

    public boolean setBounds(LatLngBounds latLngBounds, int boundsPadding) {
        if (isInitialized()) {
            mMapInterface.setCenter(latLngBounds, boundsPadding);
            return true;
        }
        return false;
    }

    public void getScreenBounds(OnMapBoundsCallback callback) {
        if (isInitialized()) {
            mMapInterface.getMapScreenBounds(callback);
        }
    }

    public void drawCircle(LatLng latLng, int radius) {
        if (isInitialized()) {
            mMapInterface.drawCircle(latLng, radius);
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (isInitialized()) {
            mMapInterface.setPadding(left, top, right, bottom);
        }
    }

    public void setOnMarkerClickListener(OnMapMarkerClickListener listener) {
        if (isInitialized()) {
            mMapInterface.setOnMarkerClickListener(listener);
        }
    }

    public void setOnMapClickListener(OnMapClickListener listener) {
        if (isInitialized()) {
            mMapInterface.setOnMapClickListener(listener);
        }
    }

    public void setInfoWindowAdapter(GoogleMap.InfoWindowAdapter adapter, InfoWindowCreator creator) {
        if (isInitialized()) {
            mMapInterface.setInfoWindowCreator(adapter, creator);
        }
    }

    public void setOnInfoWindowClickListener(OnInfoWindowClickListener listener) {
        if (isInitialized()) {
            mMapInterface.setOnInfoWindowClickListener(listener);
        }
    }

    public void clearMarkers() {
        if (isInitialized()) {
            mMapInterface.clearMarkers();
        }
    }

    public Polyline addPolyline(List<LatLng> points, int width, int color) {
        Polyline polyline = null;
        if (isInitialized()) {
            polyline = mMapInterface.addPolyline(points, width, color);
        }
        return polyline;
    }

    public void removePolyline(Polyline polyline) {
        if (isInitialized()) {
            mMapInterface.removePolyline(polyline);
        }
    }

    public boolean isInitialized() {
        return mMapInterface != null && mMapInterface.isInitialized();
    }

    public boolean addMarker(AirMapMarker marker) {
        if (isInitialized()) {
            mMapInterface.addMarker(marker);
            return true;
        }
        return false;
    }

    public interface MapCallbacks {
        public void onMapInitialized();

        public void onCameraChanged(LatLng center, int zoom);
    }

    public interface OnMapLoadedListener {
        public void onMapLoaded();
    }

    public interface OnMapMarkerClickListener {
        public void onMapMarkerClick(long id);

        public void onMapMarkerClick(Marker marker);
    }

    public interface OnMapClickListener {
        void onMapClick(LatLng latLng);
    }

    public interface OnInfoWindowClickListener {
        public void onInfoWindowClick(long id);

        public void onInfoWindowClick(Marker marker);
    }

    public interface InfoWindowCreator {
        public View createInfoWindow(long id);
    }

    public interface OnMapBoundsCallback {
        public void onMapBoundsReady(LatLngBounds bounds);
    }

    // TODO extract these to our abstraction layer
    //    /**
    //     * This method is for GOOGLE Native maps only
    //     * adds a map marker
    //     *
    //     * @param listing
    //     * @return map marker if marker was added
    //     */
    //    public Marker addGoogleListingMarker(Listing listing, int markerColor) {
    //        if (isInitialized()) {
    //            return GoogleMapMarkerManager.createListingMarker(getContext(), ((AirGoogleMapFragment) mMapInterface).getGoogleMap(), listing, markerColor);
    //        }
    //        return null;
    //    }
    //
    //    public Marker addGoogleLocalAttractionMarker(LocalAttraction attraction) {
    //        if (isInitialized()) {
    //            return GoogleMapMarkerManager.createLocalAttractionMarker(getContext(), ((AirGoogleMapFragment) mMapInterface).getGoogleMap(), attraction);
    //        }
    //        return null;
    //    }
    //
    //    public boolean addMarker(LatLng latLng, int icon, String title) {
    //        if (isInitialized()) {
    //            mMapInterface.addMarker(latLng, icon, title);
    //            return true;
    //        }
    //        return false;
    //    }
    //
    //    /**
    //     * This method is for WEB maps only
    //     * adds a map marker
    //     *
    //     * @param listing
    //     * @return true if added
    //     */
    //    public boolean addWebListingMarker(Listing listing) {
    //        if (isInitialized()) {
    //            LatLng latLng = new LatLng(listing.getLatitude(), listing.getLongitude());
    //            mMapInterface.addMarker(latLng, listing.getId());
    //            return true;
    //        }
    //        return false;
    //    }
    //
    //    public boolean addWebLocalAttractionMarker(LocalAttraction attraction) {
    //        if (isInitialized()) {
    //            LatLng latLng = new LatLng(attraction.getLatitude(), attraction.getLongitude());
    //            mMapInterface.addMarker(latLng, attraction.getResourceId());
    //            return true;
    //        }
    //        return false;
    //    }
    //
    //    public void highlightWebListingMarker(Listing prevListing, Listing currListing) {
    //        if (isInitialized()) {
    //            long prevListingId = prevListing != null ? prevListing.getId() : -1;
    //            long currListingId = currListing != null ? currListing.getId() : -1;
    //            mMapInterface.highlightListingMarker(prevListingId, currListingId);
    //        }
    //    }
}
