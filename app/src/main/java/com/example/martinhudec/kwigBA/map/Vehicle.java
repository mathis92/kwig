package com.example.martinhudec.kwigBA.map;

import java.io.Serializable;

/**
 * Created by martinhudec on 06/04/15.
 */
public class Vehicle implements Serializable {
    String shortName;
    String headingTo;
    String delay;
    Float lon;
    Float lat;
    int vehicleTypeIcon;
    String lastStop;
    String nextStop;
    String arrivalTime;
    String speed;
    String id;


    public String getShortName() {
        return shortName;
    }

    public String getHeadingTo() {
        return headingTo;
    }

    public String getDelay() {
        return delay;
    }

    public Float getLon() {
        return lon;
    }

    public Float getLat() {
        return lat;
    }

    public int getVehicleTypeIcon() {
        return vehicleTypeIcon;
    }

    public String getLastStop() {
        return lastStop;
    }

    public String getNextStop() {
        return nextStop;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getSpeed() {
        return speed;
    }

    public String getId() {
        return id;
    }
}
