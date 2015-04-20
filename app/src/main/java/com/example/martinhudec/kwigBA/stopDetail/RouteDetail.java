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
    public String delayHMS = null;


    public String getDelayHMS() {
        if (delayHMS == null) {
            if (delay.equals("notStarted")) {
                return "not started yet";
            } else if (Integer.parseInt(delay) != 0) {
                int[] timeArray = secsToHMS(Integer.parseInt(delay));
                StringBuilder stringBuilder = new StringBuilder();
                String seconds = null;
                String minutes = null;
                String hours = null;
                for (int i = 2; i >= 0; i--) {
                    switch (i) {
                        case 0:
                            if (timeArray[i] != 0) {
                                hours = timeArray[i] + " hours";
                            } else hours = "";
                            break;
                        case 1:
                            if (timeArray[i] == 60) {
                                timeArray[0] += 1;
                            } else if (timeArray[i] != 0) {
                                minutes = timeArray[i] + " minutes";
                            } else {
                                minutes = "";
                            }

                            break;
                        case 2:
                            int s = getDelayLength(timeArray[i]);
                            if (s == 60) {
                                timeArray[1] += 1;
                            } else if (s != 0) {
                                seconds = s + " seconds";
                            } else {
                                seconds = "";
                            }
                            break;
                    }
                }
                return stringBuilder.append(hours).append(" ").append(minutes).append(" ").append(seconds).toString();
            } else {
                return "on time";
            }
        } else {
            return delayHMS;
        }
    }

    public int getDelayLength(int delay) {
        if (delay == 60) {
            return 60;
        } else if (delay > 45) {
            return 45;
        } else if (delay > 30) {
            return 30;
        } else if (delay > 15) {
            return 15;
        } else {
            return 0;
        }

    }

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

    public static int[] secsToHMS(int totalSecs) {
        int[] timeArray = new int[3];
        timeArray[0] = totalSecs / 3600; //hours
        timeArray[1] = (totalSecs % 3600) / 60; //minutes
        timeArray[2] = totalSecs % 60; //seconds
        return timeArray;
    }
}
