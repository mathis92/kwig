package com.example.martinhudec.kwigBA.stopDetail;

/**
 * Created by martinhudec on 29/03/15.
 */
public class RouteDetail {
    String headingTo;
    String vehicleId;
    Integer vehicleTypeIcon;
    String delay;
    String arrivalTime;

    public String getHeadingTo() {
        return headingTo;
    }

    public void setHeadingTo(String headingTo) {
        this.headingTo = headingTo;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getVehicleTypeIcon() {
        return vehicleTypeIcon;
    }

    public void setVehicleTypeIcon(Integer vehicleTypeIcon) {
        this.vehicleTypeIcon = vehicleTypeIcon;
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
