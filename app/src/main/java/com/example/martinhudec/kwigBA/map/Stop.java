package com.example.martinhudec.kwigBA.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by martinhudec on 26/03/15.
 */
public class Stop {

    Long stopId = null;
    String stopName = null;
    Double lat = null;
    Double lon = null;
    LatLng latLng = null;

    public Stop(Long stopId, String stopName, Double lat, Double lon){
        this.stopId = stopId;
        this.stopName = stopName;
        this.lat = lat;
        this.lon = lon;
        this.latLng = new LatLng(lat,lon);
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Long getStopId() {
        return stopId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getStopName() {
        return stopName;
    }

}
