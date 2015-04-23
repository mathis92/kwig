package com.example.martinhudec.kwigBA.map;

import com.example.martinhudec.kwigBA.equip.Delay;

import java.io.Serializable;

/**
 * Created by martinhudec on 06/04/15.
 */
public class Vehicle implements Serializable {
    public String shortName;
    public String headingTo;
    public String delay;
    public Float lon;
    public Float lat;
    public int vehicleTypeIcon;
    public Integer vehicleType;
    public String lastStop;
    public String nextStop;
    public String arrivalTime;
    public String speed;
    public String id;
    public String delayHMS = null;


    public String getDelayHMS() {
        if (delayHMS == null) {
            delayHMS = Delay.getDelayHMS(delay, false);
            return delayHMS;
        } else {
            return delayHMS;
        }
    }

    public void updateVehicle(Vehicle vehicle) {
        this.delay = vehicle.delay;
        this.vehicleType = vehicle.vehicleType;
        this.arrivalTime = vehicle.arrivalTime;
        this.lon = vehicle.lon;
        this.lat = vehicle.lat;
        this.lastStop = vehicle.lastStop;
        this.nextStop = vehicle.nextStop;
        this.arrivalTime = vehicle.arrivalTime;
        this.speed = vehicle.speed;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

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
