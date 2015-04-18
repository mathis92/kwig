package com.example.martinhudec.kwigBA.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by martinhudec on 26/03/15.
 */
public class Stop implements Comparable {

    Long stopId = null;
    String stopName = null;
    Double lat = null;
    Double lon = null;
    LatLng latLng = null;
    Float distanceTo = null;
    String vehicles = null;

    public Stop(Long stopId, String stopName, Double lat, Double lon, String vehicles) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.lat = lat;
        this.lon = lon;
        this.latLng = new LatLng(lat, lon);
        this.vehicles = vehicles;
    }
    public Stop(Long stopId, String stopName, Double lat, Double lon) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.lat = lat;
        this.lon = lon;
        this.latLng = new LatLng(lat, lon);
    }

    public String getVehicles() {
        return vehicles;
    }

    public Float getDistanceTo() {
        return distanceTo;
    }

    public void setDistanceTo(Float distanceTo) {
        this.distanceTo = distanceTo;
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


    @Override
    public int compareTo(Object o) {

        Stop stop = (Stop) o;

        if (distanceTo.floatValue() > stop.distanceTo.floatValue()) {
            return 1;
        } else if (distanceTo.floatValue() < stop.distanceTo.floatValue()){
            return -1;
        }
        else{
            return 0;
        }
    }
}