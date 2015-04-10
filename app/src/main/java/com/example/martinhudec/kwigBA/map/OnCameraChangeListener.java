package com.example.martinhudec.kwigBA.map;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by martinhudec on 06/04/15.
 */
public class OnCameraChangeListener implements GoogleMap.OnCameraChangeListener {
    GoogleMap mMap;
    InputStream stopsIS;
    ShowVehicles showVehicles = null;
    ShowStops showStops = null;
    List<MarkerDetails> currentlyDisplayed = null;

    public OnCameraChangeListener(GoogleMap mMap,InputStream stopsIS,List<MarkerDetails> currentlyDisplayed) {
        this.mMap = mMap;
        this.stopsIS = stopsIS;

        this.currentlyDisplayed = currentlyDisplayed;
  /*
        UpdateVehicleLocation vehicleLocation = new UpdateVehicleLocation(mMap,currentlyDisplayed);
        vehicleLocation.execute();
*/
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d("Zoom", "Zoom: " + cameraPosition.zoom);
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        Log.d("bounds", "Bounds " + bounds.getCenter() + " " + bounds.southwest + " " + bounds.northeast);
        //   drawStops(cameraPosition.zoom);
            showVehicles = new ShowVehicles(mMap, currentlyDisplayed);
            showVehicles.execute(bounds,cameraPosition);

        if (showStops == null) {
            showStops = new ShowStops(mMap);
            showStops.execute(cameraPosition,stopsIS);
        }else {
            showStops.drawStops(cameraPosition.zoom);
        }

    }
}
