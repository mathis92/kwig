package com.example.martinhudec.kwigBA.equip;

import android.location.Location;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.map.Stop;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by martinhudec on 11/04/15.
 */
public class ReadJsonStops extends AsyncTask<String, Void, Void> {
    List<Stop> stopList = new ArrayList<>();
    InputStream stream;

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

    public List<Stop> getStopList() {
        return stopList;
    }

    public List<Stop> getNearStops(Location location) {
        final List<Stop> nearStops = new ArrayList<>();
        final List<Stop> farStops = new ArrayList<>();
        Log.d("READ JSON STOPS", ((Integer) stopList.size()).toString()
        );
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

        List<Stop> availableStops = nearStops.subList(0,3);

        for (Stop stop : availableStops) {
            Log.d("READ JSON STOPS", stop.getStopName() + " " + (stop.getDistanceTo().toString()));
        }
        return availableStops;
    }



    public List<String> getStopSuggestions(final String s) {
        final List<String> suggestedStops = new ArrayList<>();
        for (Stop stop : stopList) {
            String stopName = removeAccents(stop.getStopName());
            stopName = stopName.toLowerCase();
            if (stopName.regionMatches(true, 0, s, 0, s.length()) || stopName.contains(s)) {
                Log.d(stopName, stopName);
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
        long id = -1;
        String name = null;
        Double lat = null;
        Double lon = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String objName = reader.nextName();
            if (objName.equals("id")) {
                id = reader.nextLong();
            } else if (objName.equals("name")) {
                name = reader.nextString();
            } else if (objName.equals("lat")) {
                lat = reader.nextDouble();
            } else if (objName.equals("lon")) {
                lon = reader.nextDouble();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Stop(id, name, lat, lon);
    }

}
