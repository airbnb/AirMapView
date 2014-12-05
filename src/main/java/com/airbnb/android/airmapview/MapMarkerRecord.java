package com.airbnb.android.airmapview;

import com.google.android.gms.maps.model.Marker;

public class MapMarkerRecord<T> {
    T thing;
    Marker marker = null;
    long id = -1;

    public MapMarkerRecord(T thing, Marker marker) {
        this.thing = thing;
        this.marker = marker;
    }

    public MapMarkerRecord(T thing, long id) {
        this.thing = thing;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MapMarkerRecord that = (MapMarkerRecord) o;

        if (id != that.id) {
            return false;
        }
        if (marker != null ? !marker.equals(that.marker) : that.marker != null) {
            return false;
        }
        if (thing != null ? !thing.equals(that.thing) : that.thing != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = thing != null ? thing.hashCode() : 0;
        result = 31 * result + (marker != null ? marker.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}