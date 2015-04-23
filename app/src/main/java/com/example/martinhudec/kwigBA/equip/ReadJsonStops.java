package com.example.martinhudec.kwigBA.equip;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.example.martinhudec.kwigBA.findStop.FindStopActivity;
import com.example.martinhudec.kwigBA.map.Stop;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by martinhudec on 11/04/15.
 */
public class ReadJsonStops extends AsyncTask<String, Void, Void> {
    List<Stop> stopList = new ArrayList<>();
    List<Stop> stopListDetailed = new ArrayList<>();
    InputStream stream;
    FindStopActivity activity;

    public ReadJsonStops(InputStream stream, Activity activity) {
        this.stream = stream;
        this.activity = (FindStopActivity) activity;
    }

    public ReadJsonStops(InputStream stream) {
        this.stream = stream;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            stopList = readJsonStream(stream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (activity != null) {
            activity.fillRecyclerViewWithFoundStops("");
        }
    }

    public List<Stop> getStopList() {
        return stopList;
    }

    public List<Stop> getBoundsList(LatLngBounds bounds) {
        List<Stop> boundsStopList = new ArrayList<>();
      //  Log.d("getBoundsList", "bounds");
        for (Stop stop : stopList) {
            if (bounds.contains(stop.getLatLng())) {
        //        Log.d("getBoundsList", stop.getStopName());
                boundsStopList.add(stop);
            }
        }
        return boundsStopList;
    }


    public List<Stop> getNearStops(Location location) {
        final List<Stop> nearStops = new ArrayList<>();
        final List<Stop> farStops = new ArrayList<>();
        //Log.d("READ JSON STOPS", ((Integer) stopList.size()).toString());
        for (Stop stop : stopList) {
            Location currentStopLocation = new Location("SYSTEM");
            currentStopLocation.setLatitude(stop.getLat());
            currentStopLocation.setLongitude(stop.getLon());
            Float distance = location.distanceTo(currentStopLocation);
            if (distance <= 500) {

                stop.setDistanceTo(distance);
                //  Log.d("stopName", stop.getStopName() + " " + stop.getDistanceTo());
                nearStops.add(stop);
            } else if (distance < 2000 && distance > 500) {
                stop.setDistanceTo(distance);
                farStops.add(stop);
                //  Log.d("stopName", stop.getStopName() + " " + stop.getDistanceTo());
            }
        }
        if (nearStops.size() < 3) {
            Collections.sort(farStops);
            int index = 0;
            for (int i = nearStops.size(); i < 3; i++) {
                nearStops.add(farStops.get(index));
            }
            index++;
        }
        Collections.sort(nearStops);

        List<Stop> availableStops = nearStops.subList(0, 3);

        /*for (Stop stop : availableStops) {
            Log.d("READ JSON STOPS", stop.getStopName() + " " + (stop.getDistanceTo().toString()));
        }*/
        return availableStops;
    }


    public List<String> getStopSuggestions(final String s) {
        final List<String> suggestedStops = new ArrayList<>();
        for (Stop stop : stopList) {
            String stopName = removeAccents(stop.getStopName());
            stopName = stopName.toLowerCase();
            if (stopName.regionMatches(true, 0, s, 0, s.length()) || stopName.contains(s)) {
               // Log.d(stopName, stopName);
                suggestedStops.add(stop.getStopName());
            }

        }
        return suggestedStops;
    }

    public static String removeAccents(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public List readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readStopsObject(reader);
        } finally {
            reader.close();
        }
    }

    public List readStopsObject(JsonReader reader) throws IOException {
        List stops = new ArrayList();
        reader.beginObject();
        while (reader.hasNext()) {
            String objName = reader.nextName();
            if (objName.equals("stops")) {
                stops = readStopsArray(reader);
            }
        }
        reader.close();
        return stops;
    }

    public List readStopsArray(JsonReader reader) throws IOException {
        List stops = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            stops.add(readStops(reader));
        }
        reader.endArray();
        return stops;
    }

    public Stop readStops(JsonReader reader) throws IOException {
        String id = null;
        String name = null;
        Double lat = null;
        Double lon = null;
        String vehicle = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String objName = reader.nextName();
            if (objName.equals("GtfsStopsId")) {
                id = reader.nextString();
            } else if (objName.equals("name")) {
                name = reader.nextString();
            } else if (objName.equals("lat")) {
                lat = reader.nextDouble();
            } else if (objName.equals("lon")) {
                lon = reader.nextDouble();
            } else if (objName.equals("vehicles")) {
                vehicle = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Stop(id, name, lat, lon, vehicle);
    }


}
