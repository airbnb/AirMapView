package com.airbnb.android.airmapview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.airbnb.android.airmapview.listeners.InfoWindowCreator;
import com.airbnb.android.airmapview.listeners.OnCameraChangeListener;
import com.airbnb.android.airmapview.listeners.OnCameraMoveListener;
import com.airbnb.android.airmapview.listeners.OnInfoWindowClickListener;
import com.airbnb.android.airmapview.listeners.OnLatLngScreenLocationCallback;
import com.airbnb.android.airmapview.listeners.OnMapBoundsCallback;
import com.airbnb.android.airmapview.listeners.OnMapClickListener;
import com.airbnb.android.airmapview.listeners.OnMapInitializedListener;
import com.airbnb.android.airmapview.listeners.OnMapLoadedListener;
import com.airbnb.android.airmapview.listeners.OnMapMarkerClickListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

public class AirMapView extends FrameLayout
    implements OnCameraChangeListener, OnMapClickListener,
               OnMapMarkerClickListener, OnMapLoadedListener, OnInfoWindowClickListener {

  private static final int INVALID_ZOOM = -1;

  protected AirMapInterface mapInterface;
  private OnCameraMoveListener onCameraMoveListener;
  private OnCameraChangeListener onCameraChangeListener;
  private boolean mOnCameraMoveTriggered;
  private OnMapInitializedListener onMapInitializedListener;
  private OnMapClickListener onMapClickListener;
  private OnMapMarkerClickListener onMapMarkerClickListener;
  private OnInfoWindowClickListener onInfoWindowClickListener;

  public AirMapView(Context context) {
    super(context);
    inflateView();
  }

  public AirMapView(Context context, AttributeSet attrs) {
    super(context, attrs);
    inflateView();
  }

  public AirMapView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    inflateView();
  }

  private void inflateView() {
    LayoutInflater.from(getContext()).inflate(R.layout.map_view, this);
  }

  public void initialize(FragmentManager fragmentManager, AirMapInterface mapInterface) {
    if (mapInterface == null || fragmentManager == null) {
      throw new IllegalArgumentException("Either mapInterface or fragmentManager is null");
    }

    this.mapInterface = mapInterface;
    this.mapInterface.setOnMapLoadedListener(this);

    fragmentManager.beginTransaction()
        .replace(getId(), (Fragment) this.mapInterface)
        .commit();

    fragmentManager.executePendingTransactions();
  }

  /**
   * Used for initialization of the underlying map provider.
   *
   * @param fragmentManager required for initialization
   */
  public void initialize(FragmentManager fragmentManager) {
    AirMapInterface
        mapInterface =
        (AirMapInterface) fragmentManager.findFragmentById(R.id.map_frame);

    if (mapInterface != null) {
      initialize(fragmentManager, mapInterface);
    } else {
      initialize(fragmentManager, new DefaultAirMapViewBuilder(getContext()).builder().build());
    }
  }

  public void setOnMapInitializedListener(OnMapInitializedListener mapInitializedListener) {
    onMapInitializedListener = mapInitializedListener;
  }

  @Override
  public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
    if (ev.getAction() == MotionEvent.ACTION_MOVE) {
      if (onCameraMoveListener != null && !mOnCameraMoveTriggered) {
        onCameraMoveListener.onCameraMove();
        mOnCameraMoveTriggered = true;
      }
    } else if (ev.getAction() == MotionEvent.ACTION_UP) {
      mOnCameraMoveTriggered = false;
    }

    return super.dispatchTouchEvent(ev);
  }

  public void setOnCameraChangeListener(OnCameraChangeListener onCameraChangeListener) {
    this.onCameraChangeListener = onCameraChangeListener;
  }

  /**
   * Sets the map {@link com.airbnb.android.airmapview.listeners.OnCameraMoveListener}
   *
   * @param onCameraMoveListener The OnCameraMoveListener to be set
   */
  public void setOnCameraMoveListener(OnCameraMoveListener onCameraMoveListener) {
    this.onCameraMoveListener = onCameraMoveListener;
  }

  public final AirMapInterface getMapInterface() {
    return mapInterface;
  }

  public void onDestroyView() {
    if (isInitialized()) {
      mapInterface.setMyLocationEnabled(false);
    }
  }

  public int getZoom() {
    if (isInitialized()) {
      return mapInterface.getZoom();
    }

    return INVALID_ZOOM;
  }

  public LatLng getCenter() {
    if (isInitialized()) {
      return mapInterface.getCenter();
    }
    return null;
  }

  public boolean setCenter(LatLng latLng) {
    if (isInitialized()) {
      mapInterface.setCenter(latLng);
      return true;
    }
    return false;
  }

  public boolean animateCenter(LatLng latLng) {
    if (isInitialized()) {
      mapInterface.animateCenter(latLng);
      return true;
    }
    return false;
  }

  public boolean setZoom(int zoom) {
    if (isInitialized()) {
      mapInterface.setZoom(zoom);
      return true;
    }
    return false;
  }

  public boolean setCenterZoom(LatLng latLng, int zoom) {
    if (isInitialized()) {
      mapInterface.setCenterZoom(latLng, zoom);
      return true;
    }
    return false;
  }

  public boolean animateCenterZoom(LatLng latLng, int zoom) {
    if (isInitialized()) {
      mapInterface.animateCenterZoom(latLng, zoom);
      return true;
    }
    return false;
  }

  public boolean setBounds(LatLngBounds latLngBounds, int boundsPadding) {
    if (isInitialized()) {
      mapInterface.setCenter(latLngBounds, boundsPadding);
      return true;
    }
    return false;
  }

  public void getScreenBounds(OnMapBoundsCallback callback) {
    if (isInitialized()) {
      mapInterface.getMapScreenBounds(callback);
    }
  }

  public void getMapMarkerScreenLocation(LatLng latLng, OnLatLngScreenLocationCallback callback) {
    if (isInitialized()) {
      mapInterface.getScreenLocation(latLng, callback);
    }
  }

  public void drawCircle(LatLng latLng, int radius) {
    if (isInitialized()) {
      mapInterface.drawCircle(latLng, radius);
    }
  }

  public void drawCircle(LatLng latLng, int radius, int strokeColor) {
    if (isInitialized()) {
      mapInterface.drawCircle(latLng, radius, strokeColor);
    }
  }

  public void drawCircle(LatLng latLng, int radius, int strokeColor, int strokeWidth) {
    if (isInitialized()) {
      mapInterface.drawCircle(latLng, radius, strokeColor, strokeWidth);
    }
  }

  public void drawCircle(LatLng latLng, int radius, int strokeColor, int strokeWidth,
                         int fillColor) {
    if (isInitialized()) {
      mapInterface.drawCircle(latLng, radius, strokeColor, strokeWidth, fillColor);
    }
  }

  public void setPadding(int left, int top, int right, int bottom) {
    if (isInitialized()) {
      mapInterface.setPadding(left, top, right, bottom);
    }
  }

  public void setOnMarkerClickListener(OnMapMarkerClickListener listener) {
    onMapMarkerClickListener = listener;
  }

  public void setOnMapClickListener(OnMapClickListener listener) {
    onMapClickListener = listener;
  }

  public void setInfoWindowAdapter(GoogleMap.InfoWindowAdapter adapter, InfoWindowCreator creator) {
    if (isInitialized()) {
      mapInterface.setInfoWindowCreator(adapter, creator);
    }
  }

  public void setOnInfoWindowClickListener(OnInfoWindowClickListener listener) {
    onInfoWindowClickListener = listener;
  }

  public void clearMarkers() {
    if (isInitialized()) {
      mapInterface.clearMarkers();
    }
  }

  public boolean addPolyline(AirMapPolyline polyline) {
    if (isInitialized()) {
      mapInterface.addPolyline(polyline);
      return true;
    }
    return false;
  }

  public void setMapType(MapType mapType) {
    mapInterface.setMapType(mapType);
  }

  public boolean removePolyline(AirMapPolyline polyline) {
    if (isInitialized()) {
      mapInterface.removePolyline(polyline);
      return true;
    }
    return false;
  }

  public boolean isInitialized() {
    return mapInterface != null && mapInterface.isInitialized();
  }

  public boolean addMarker(AirMapMarker marker) {
    if (isInitialized()) {
      mapInterface.addMarker(marker);
      return true;
    }
    return false;
  }

  public void setMyLocationEnabled(boolean trackUserLocation) {
    mapInterface.setMyLocationEnabled(trackUserLocation);
  }

  @Override public void onCameraChanged(LatLng latLng, int zoom) {
    if (onCameraChangeListener != null) {
      onCameraChangeListener.onCameraChanged(latLng, zoom);
    }
  }

  @Override
  public void onMapClick(LatLng latLng) {
    if (onMapClickListener != null) {
      onMapClickListener.onMapClick(latLng);
    }
  }

  @Override public void onMapMarkerClick(long id) {
    if (onMapMarkerClickListener != null) {
      onMapMarkerClickListener.onMapMarkerClick(id);
    }
  }

  @Override public void onMapMarkerClick(Marker marker) {
    if (onMapMarkerClickListener != null) {
      onMapMarkerClickListener.onMapMarkerClick(marker);
    }
  }

  @Override public void onMapLoaded() {
    if (isInitialized()) {
      mapInterface.setOnCameraChangeListener(this);
      mapInterface.setOnMapClickListener(this);
      mapInterface.setOnMarkerClickListener(this);
      mapInterface.setOnInfoWindowClickListener(this);

      if (onMapInitializedListener != null) {
        // only send map Initialized callback if map initialized successfully
        // initialization can fail if the map leaves the screen before it loads
        onMapInitializedListener.onMapInitialized();
      }
    }
  }

  @Override public void onInfoWindowClick(long id) {
    if (onInfoWindowClickListener != null) {
      onInfoWindowClickListener.onInfoWindowClick(id);
    }
  }

  @Override public void onInfoWindowClick(Marker marker) {
    if (onInfoWindowClickListener != null) {
      onInfoWindowClickListener.onInfoWindowClick(marker);
    }
  }
}
