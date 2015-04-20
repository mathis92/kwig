package com.example.martinhudec.kwigBA.map;

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
            int[] timeArray = secsToHMS(Integer.parseInt(delay));
            StringBuilder stringBuilder = new StringBuilder();
            String seconds = null;
            String minutes = null;
            String hours = null;
            for (int i = 2; i >= 0; i--) {
                switch (i) {
                    case 0:
                            if(timeArray[i] != 0){
                                hours = timeArray[i] + " hours";
                            }else hours = "";
                        break;
                    case 1:
                        if (timeArray[i] == 60){
                            timeArray[0] += 1;
                        }else if ( timeArray[i] != 0){
                           minutes = timeArray[i] + " minutes";
                        }else {
                            minutes = "";
                        }

                        break;
                    case 2:
                            int s = getDelayLength(timeArray[i]);
                            if(s == 60){
                                timeArray[1] += 1;
                            } else if (s != 0){
                                seconds = s + " seconds";
                            }else {
                                seconds = "";
                            }
                        break;
                }
            }
           return  stringBuilder.append(hours).append(" ").append(minutes).append(" ").append(seconds).toString();

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
        }else{
            return 0;
        }

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

    public static int[] secsToHMS(int totalSecs) {
        int[] timeArray = new int[3];
        timeArray[0] = totalSecs / 3600; //hours
        timeArray[1] = (totalSecs % 3600) / 60; //minutes
        timeArray[2] = totalSecs % 60; //seconds
        return timeArray;
    }

}
