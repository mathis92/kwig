package com.example.martinhudec.kwigBA.map;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by martinhudec on 01/04/15.
 */
public class MarkerDetails {
    Marker marker;
    Stop stop;

    public MarkerDetails(Marker marker, Stop stop) {
        this.marker = marker;
        this.stop = stop;
    }

    public Marker getMarker() {
        return marker;
    }

    public Stop getStop() {
        return stop;
    }
}
