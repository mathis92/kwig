package com.example.martinhudec.kwigBA.map;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by martinhudec on 06/04/15.
 */
public class OnCameraChangeListener implements GoogleMap.OnCameraChangeListener {
    GoogleMap mMap;
    InputStream stopsIS;
    ShowVehicles showVehicles = null;
    ShowStops showStops = null;


    public OnCameraChangeListener(GoogleMap mMap, InputStream stopsIS) {
        this.mMap = mMap;
        this.stopsIS = stopsIS;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d("Zoom", "Zoom: " + cameraPosition.zoom);
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        Log.d("bounds", "Bounds " + bounds.getCenter() + " " + bounds.southwest + " " + bounds.northeast);
        //   drawStops(cameraPosition.zoom);
        if (showVehicles == null) {
            showVehicles = new ShowVehicles(mMap);
            showVehicles.execute(bounds);
        } else {
            showVehicles.requestVehicles(bounds);
        }
        if (showStops == null) {
            showStops = new ShowStops(mMap);
            showStops.execute(cameraPosition, stopsIS);
        }else {
            showStops.drawStops(cameraPosition.zoom);
        }


    }
}
