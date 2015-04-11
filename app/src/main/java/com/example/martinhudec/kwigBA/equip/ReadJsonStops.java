package com.example.martinhudec.kwigBA.equip;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.example.martinhudec.kwigBA.map.Stop;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
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
