package com.example.martinhudec.kwigBA.map;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.List;

/**
 * Created by martinhudec on 10/04/15.
 */
public class UpdateVehiclePositions implements Runnable {
    GoogleMap mMap;

    boolean action = true;
    LatLngBounds bounds;
    CameraPosition position;
    List<MarkerDetails> currentlyDisplayed;
    HashMap<Marker, Object> markerObjectHashMap;

    public UpdateVehiclePositions(GoogleMap mMap, List<MarkerDetails> currentlyDisplayed, HashMap<Marker, Object> markerObjectHashMap) {
        this.currentlyDisplayed = currentlyDisplayed;
        this.mMap = mMap;
        bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        position = mMap.getCameraPosition();
        this.markerObjectHashMap = markerObjectHashMap;
    }

    public void stopUpdate(){
        action = false;
    }

    @Override
    public void run() {


        new Thread(new Runnable() {

            @Override
            public void run() {
                while (action) {
                    final ShowVehicles showVehicles = new ShowVehicles(mMap, currentlyDisplayed, markerObjectHashMap);
                    showVehicles.execute(bounds, position);

           //         Log.d("UpdateVehicleLocation", "location");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void updateMapReference(GoogleMap mMap) {
        bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        position = mMap.getCameraPosition();
    }
}
