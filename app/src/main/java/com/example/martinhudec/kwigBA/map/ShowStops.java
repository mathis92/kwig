package com.example.martinhudec.kwigBA.map;

import android.os.AsyncTask;
import android.util.Log;

import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.equip.ReadJsonStops;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    ReadJsonStops jsonStopsDetailed;
    HashMap<Marker, Object> markerObjectHashMap;

    public ShowStops(GoogleMap mMap, List<MarkerDetails> currentlyDisplayed, ReadJsonStops jsonStops, ReadJsonStops jsonStopsDetailed, HashMap<Marker, Object> markerObjectHashMap) {
        this.mMap = mMap;
        this.currentlyDisplayed = currentlyDisplayed;
        this.jsonStops = jsonStops;
        this.jsonStopsDetailed = jsonStopsDetailed;
        this.markerObjectHashMap = markerObjectHashMap;


    }


    @Override
    protected List<Stop> doInBackground(Object... params) {

        cameraPosition = (CameraPosition) params[1];
        if (cameraPosition.zoom > 16.5) {

        //    Log.d("SHOW STOPS", "read json stops");
            currentStopList = jsonStopsDetailed.getBoundsList((LatLngBounds)params[0]);

        }else if(cameraPosition.zoom > 14){
            currentStopList = jsonStops.getBoundsList((LatLngBounds) params[0]);
    }else {
            currentStopList = new ArrayList<>();
        }
        return currentStopList;

    }

    @Override
    protected void onPostExecute(List<Stop> stops) {
        super.onPostExecute(stops);
      //  Log.d("ON POST EXECUTE", ((Integer) stops.size()).toString());
        for (MarkerDetails mark : currentlyDisplayed) {
            int solved = 0;
            for (Stop stop : stops) {
             //   Log.d("STOP ID", stop.getStopId());
                if (stop.getStopId().equals(mark.getStop().getStopId())) {
                    solved = 1;
                    break;

                    // mark.getMarker().setPosition(new LatLng(vehicle.lat, vehicle.lon));
                }
            }
            if (solved == 0) {
                mark.getMarker().remove();
                currentlyDisplayed.remove(mark);
            }
        }
        for (Stop stop : stops) {

            int found = 0;
            for (MarkerDetails mark : currentlyDisplayed) {
               // Log.d(mark.getStop(), stop.getStopId());
                if (mark.getStop().stopId.equals(stop.getStopId())) {
                    found = 1;
                    break;
                }
            }
          //  Log.d("SHOW STOPS", "found = " + found +" " + stop.getStopName());
            if (found == 0) {
            //    Log.d("SHOW STOPS", "found =0 " + stop.getStopName() + stop.getVehicles());
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
