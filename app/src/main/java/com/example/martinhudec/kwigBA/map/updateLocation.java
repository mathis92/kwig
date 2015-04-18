package com.example.martinhudec.kwigBA.map;

import android.app.Fragment;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;
import java.util.Map;

/**
 * Created by martinhudec on 10/04/15.
 */
public class updateLocation implements Runnable {
    GoogleMap mMap;
    private MapDetailInterface mapsFragment;
    boolean action = true;
    List<MarkerDetails> currentlyDisplayed;

    public updateLocation(GoogleMap mMap, List<MarkerDetails> currentlyDisplayed) {
        this.currentlyDisplayed = currentlyDisplayed;
        this.mMap = mMap;
        this.mapsFragment = mapsFragment;
    }

    @Override
    public void run() {
        while(action){
           // ShowVehicles showVehicles = new ShowVehicles(mMap, currentlyDisplayed);
          //  showVehicles.execute( mapsFragment.getLatLngFromMap(),mapsFragment.getMapZoom());
            Log.d("UpdateVehicleLocation", "location");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
