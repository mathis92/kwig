package com.example.martinhudec.kwigBA.stopDetail;

import com.example.martinhudec.kwigBA.equip.Delay;

import java.io.Serializable;

/**
 * Created by martinhudec on 29/03/15.
 */
public class RouteDetail implements Serializable{
    String headingTo;
    String vehicleShortName;
    Integer vehicleTypeIcon;
    Integer vehicleType;
    String vehicleId;
    String delay;
    String arrivalTime;
    public String delayHMS = null;


    public String getDelayHMS() {
        if (delayHMS == null) {
            delayHMS = Delay.getDelayHMS(delay,true);
            return delayHMS;
        } else {
            return delayHMS;
        }
    }

    public String getHeadingTo() {
        return headingTo;
    }

    public void setHeadingTo(String headingTo) {
        this.headingTo = headingTo;
    }

    public String getVehicleShortName() {
        return vehicleShortName;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setVehicleShortName(String vehicleShortName) {
        this.vehicleShortName = vehicleShortName;
    }

    public Integer getVehicleTypeIcon() {
        return vehicleTypeIcon;
    }

    public void setVehicleTypeIcon(Integer vehicleTypeIcon) {
        this.vehicleTypeIcon = vehicleTypeIcon;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }


}
