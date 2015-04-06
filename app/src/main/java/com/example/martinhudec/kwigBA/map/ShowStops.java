package com.example.martinhudec.kwigBA.map;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.martinhudec.kwigBA.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinhudec on 26/03/15.
 */
public class ShowStops extends AsyncTask<Object, List<Stop>, List<Stop>> {
    GoogleMap mMap = null;
    List<MarkerDetails> markerList = null;
    List<Marker> currentlyDisplayed = null;
    List stopList = null;
    CameraPosition cameraPosition = null;

    public ShowStops(GoogleMap mMap) {
        this.mMap = mMap;
        currentlyDisplayed = new ArrayList<>();

    }


    @Override
    protected List<Stop> doInBackground(Object... params) {
        // List<Stop> stopList = (List<Stop>) params[0];
        cameraPosition = (CameraPosition) params[0];
        try {
            if (stopList == null) {
                stopList = readJsonStream((InputStream) params[1]);
            }
            publishProgress(stopList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(List<Stop>... values) {
        if (markerList == null) {
            markerList = new ArrayList<>();
            for (Stop stop : values[0]) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_icon2))
                        .title(stop.getStopName())
                        .position(stop.getLatLng())
                        .visible(false));
                markerList.add(new MarkerDetails(marker, stop));

            }
            drawStops(cameraPosition.zoom);
        }
        //      mMap.setOnCameraChangeListener(getCameraChangeListener(values[0]));
        mMap.setOnMarkerClickListener(getOnMarkerClickListener());
    }

    public GoogleMap.OnCameraChangeListener getCameraChangeListener(final List<Stop> values) {

        return new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                Log.d("Zoom", "Zoom: " + position.zoom);
                LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                Log.d("bounds", "Bounds " + bounds.getCenter() + " " + bounds.southwest + " " + bounds.northeast);
                drawStops(position.zoom);
            }
        };
    }

    public GoogleMap.OnMarkerClickListener getOnMarkerClickListener() {
        return new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();

                return false;
            }
        };
    }


    public void drawStops(float zoom) {
        if (zoom > 14) {
            for (MarkerDetails marker : markerList) {
                marker.getMarker().setVisible(true);
            }
        } else {
            for (MarkerDetails marker : markerList) {
                marker.getMarker().setVisible(false);
            }
        }
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
