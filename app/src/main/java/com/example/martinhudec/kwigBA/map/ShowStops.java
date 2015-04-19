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

import com.example.martinhudec.kwigBA.NavigationDrawerFragment;
import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.equip.ReadJsonStops;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by martinhudec on 26/03/15.
 */
public class ShowStops extends AsyncTask<Object, List<Stop>, List<Stop>> {
    GoogleMap mMap = null;
    List<MarkerDetails> currentlyDisplayed = null;
    List<Stop> currentStopList;
    List stopList = null;
    CameraPosition cameraPosition = null;
    ReadJsonStops jsonStops;
    HashMap<Marker, Object> markerObjectHashMap;

    public ShowStops(GoogleMap mMap, List<MarkerDetails> currentlyDisplayed, ReadJsonStops jsonStops, HashMap<Marker, Object> markerObjectHashMap) {
        this.mMap = mMap;
        this.currentlyDisplayed = currentlyDisplayed;
        this.jsonStops = jsonStops;
        this.markerObjectHashMap = markerObjectHashMap;


    }


    @Override
    protected List<Stop> doInBackground(Object... params) {

        cameraPosition = (CameraPosition) params[1];
        if (cameraPosition.zoom > 14) {
            Log.d("SHOW STOPS", "read json stops");

            currentStopList = jsonStops.getBoundsList((LatLngBounds) params[0]);

        } else {
            currentStopList = new ArrayList<>();
        }
        return currentStopList;

    }

    @Override
    protected void onPostExecute(List<Stop> stops) {
        super.onPostExecute(stops);
        Log.d("ON POST EXECUTE", ((Integer) stops.size()).toString());

        for (Stop stop : stops) {

            int found = 0;
            for (MarkerDetails mark : currentlyDisplayed) {
                if (mark.getStop().stopName.equals(stop.getStopName())) {
                    found = 1;
                }
            }
            Log.d("SHOW STOPS", "found = " + found +" " + stop.getStopName());
            if (found == 0) {
                Log.d("SHOW STOPS", "found =0 " + stop.getStopName() + stop.getVehicles());
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_icon2))
                        .title(stop.getStopName())
                        .position(stop.getLatLng())
                        .snippet(stop.getVehicles()));
                currentlyDisplayed.add(new MarkerDetails(marker, stop));
                markerObjectHashMap.put(marker,stop);
            }
        }
    }


}
