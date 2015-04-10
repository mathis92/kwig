package com.example.martinhudec.kwigBA.map;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by martinhudec on 01/04/15.
 */
public class MarkerDetails {
    Marker marker;
    Stop stop;
    Vehicle vehicle;

    public MarkerDetails(Marker marker, Stop stop) {
        this.marker = marker;
        this.stop = stop;
    }

    public MarkerDetails(Marker marker,Vehicle vehicle) {
        this.vehicle = vehicle;
        this.marker = marker;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Marker getMarker() {
        return marker;
    }

    public Stop getStop() {
        return stop;
    }
}
