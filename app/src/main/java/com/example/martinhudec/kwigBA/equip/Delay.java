package com.example.martinhudec.kwigBA.equip;

import android.content.Context;

import com.example.martinhudec.kwigBA.R;

/**
 * Created by martinhudec on 21/04/15.
 */
public class Delay {
    private static Context context;
    public Delay(Context context){
        this.context = context;
    }

    public static String getDelayHMS(String delay, Boolean writeDelay) {
        String delayHMS;
        boolean ahead = false;
        Integer timeSet = 0;
        if (delay.equals("notStarted")) {
            return context.getResources().getString(R.string.not_started_yet);
        } else if (Integer.parseInt(delay) != 0) {
            int[] timeArray = secsToHMS(Integer.parseInt(delay));
            StringBuilder stringBuilder = new StringBuilder();
            String seconds = null;
            String minutes = null;
            String hours = null;
            for (int i = 2; i >= 0; i--) {
                if (timeArray[i] < 0) {
                    ahead = true;
                    timeArray[i] *= -1;
                }
                switch (i) {
                    case 0:
                        if (timeArray[i] != 0) {

                            hours = timeArray[i] + " " + context.getResources().getString(R.string.hours)+ " ";
                            timeSet++;
                        } else
                            hours = "";
                        break;
                    case 1:
                        if (timeArray[i] == 60) {
                            timeArray[0] += 1;
                        } else if (timeArray[i] != 0) {
                            minutes = timeArray[i] + " " + context.getResources().getString(R.string.mins)+ " ";
                            timeSet++;
                        } else {
                            minutes = "";
                        }

                        break;
                    case 2:
                        int s = getDelayLength(timeArray[i]);
                        if (s == 60) {
                            timeArray[1] += 1;
                        } else if (s != 0) {
                            seconds = s + " " + context.getResources().getString(R.string.secs)+ " ";
                            timeSet++;
                        } else {
                            seconds = "";
                        }
                        break;
                }
            }
            if (timeSet != 0) {
                if (ahead == true) {
                    return stringBuilder.append(context.getResources().getString(R.string.delay_ahead)+": ").append(hours).append(minutes).append(seconds).toString();
                } else {
                    if (writeDelay) {
                        return stringBuilder.append(context.getResources().getString(R.string.delay)+": ").append(hours).append(minutes).append(seconds).toString();
                    } else {
                        return stringBuilder.append(hours).append(minutes).append(seconds).toString();
                    }
                }
            } else {
                return context.getResources().getString(R.string.on_time);
            }
        } else {
            return context.getResources().getString(R.string.on_time);
        }

    }

    public static int getDelayLength(int delay) {
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

    public static int[] secsToHMS(int totalSecs) {
        int[] timeArray = new int[3];
        timeArray[0] = totalSecs / 3600; //hours
        timeArray[1] = (totalSecs % 3600) / 60; //minutes
        timeArray[2] = totalSecs % 60; //seconds
        return timeArray;
    }

}
