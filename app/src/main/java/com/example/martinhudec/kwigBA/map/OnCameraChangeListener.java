package com.example.martinhudec.kwigBA.map;

import android.util.Log;

import com.example.martinhudec.kwigBA.equip.ReadJsonStops;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by martinhudec on 06/04/15.
 */
public class OnCameraChangeListener implements GoogleMap.OnCameraChangeListener {
    GoogleMap mMap;
    InputStream stopsIS;
    ReadJsonStops jsonStops;
    ReadJsonStops jsonStopsDetailed;
    ShowVehicles showVehicles = null;
    ShowStops showStops = null;
    MapsFragment mapsFragment;
    List<MarkerDetails> currentlyDisplayedVehicles = null;
    List<MarkerDetails> currentlyDisplayedStops = null;
    HashMap<Marker, Object> markerObjectHashMap;

    public OnCameraChangeListener(GoogleMap mMap, InputStream stopsIS, InputStream stopsDetailed,  List<MarkerDetails> currentlyDisplayedVehicles,List<MarkerDetails> currentlyDisplayedStops,HashMap<Marker, Object> markerObjectHashMap, MapsFragment mapsFragment) {
        this.jsonStops = new ReadJsonStops(stopsIS);
        jsonStops.execute();
        this.jsonStopsDetailed = new ReadJsonStops(stopsDetailed);
        jsonStopsDetailed.execute();
        this.mMap = mMap;
        this.stopsIS = stopsIS;
        this.mapsFragment = mapsFragment;
        this.currentlyDisplayedStops = currentlyDisplayedStops;
        this.currentlyDisplayedVehicles = currentlyDisplayedVehicles;
        this.markerObjectHashMap = markerObjectHashMap;


    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
     //   Log.d("Zoom", "Zoom: " + cameraPosition.zoom);
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
     //   Log.d("bounds", "Bounds " + bounds.getCenter() + " " + bounds.southwest + " " + bounds.northeast);
        //   drawStops(cameraPosition.zoom);
        showVehicles = new ShowVehicles(mMap, currentlyDisplayedVehicles, markerObjectHashMap);
        showVehicles.execute(bounds, cameraPosition);
        mapsFragment.updatePositions();
        showStops = new ShowStops(mMap, currentlyDisplayedStops,jsonStops, jsonStopsDetailed, markerObjectHashMap);
        showStops.execute(bounds, cameraPosition);
/*
        if (showStops == null) {
            showStops = new ShowStops(mMap);
            showStops.execute(cameraPosition, stopsIS);
        } else {
            showStops.drawStops(cameraPosition.zoom);
        }
*/
    }
}
