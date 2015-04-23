package com.example.martinhudec.kwigBA.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.stopDetail.StopDetailsActivity;
import com.example.martinhudec.kwigBA.vehicleDetail.VehicleDetailsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import android.widget.TextView;

/**
 * Created by martinhudec on 18/04/15.
 */
public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    Vehicle vehicle;
    HashMap<Marker, Object> markerObjectHashMap;
    GoogleMap mMap;
    Activity mainActivity;



    public InfoWindowAdapter(Activity mainActivity,  HashMap<Marker, Object> markerObjectHashMap, GoogleMap mMap) {
        this.markerObjectHashMap = markerObjectHashMap;
        this.mMap = mMap;
        this.mainActivity = mainActivity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = null;
        if(markerObjectHashMap.get(marker).getClass().equals(Vehicle.class)){
          //  Log.d("VEHICLE", ((Vehicle)markerObjectHashMap.get(marker)).shortName);
             vehicle = (Vehicle)markerObjectHashMap.get(marker);

            view = mainActivity.getLayoutInflater().inflate(R.layout.vehicle_marker_info_window, null);
            TextView vehicleShortName = (TextView) view.findViewById(R.id.vehicleMarkerTitle);
            TextView vehicleHeadingTo = (TextView) view.findViewById(R.id.vehicleMarkerHeadingto);

            vehicleShortName.setText(vehicle.shortName);
            vehicleHeadingTo.setText("Heading to -> " + vehicle.headingTo);

        }else {
        //    Log.d("STOP", ((Stop) markerObjectHashMap.get(marker)).stopName);
            Stop stop = (Stop) markerObjectHashMap.get(marker);
           view = mainActivity.getLayoutInflater().inflate(R.layout.marker_info_window, null);
            TextView title = (TextView) view.findViewById(R.id.markerTitle);
            TextView stopVehicles = (TextView) view.findViewById(R.id.markerStopVehicles);
            title.setText(marker.getTitle());
       //  Log.d("stopVehicles", stop.getVehicles());

            stopVehicles.setText(stop.getVehicles());
        }
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
            //        Log.d("MapsFragment", "Button on click listener ");
                    if (markerObjectHashMap.get(marker).getClass().equals(Vehicle.class)) {
                        Intent vehicleDetailsActivity = new Intent(mainActivity, VehicleDetailsActivity.class);
                        Bundle b = new Bundle();
                        b.putString("vehicle", marker.getTitle()); //Your id
                        b.putSerializable("vehicleObject", vehicle);
                        vehicleDetailsActivity.putExtras(b); //Put your id to your next Intent
                        mainActivity.startActivity(vehicleDetailsActivity);
                        mainActivity.overridePendingTransition(R.anim.activity_animation, R.anim.activity_animation2);
                    } else {
                        Intent stopDetailsActivity = new Intent(mainActivity, StopDetailsActivity.class);
                        Bundle b = new Bundle();
                        b.putString("stopName", marker.getTitle()); //Your id
                        stopDetailsActivity.putExtras(b); //Put your id to your next Intent
                        mainActivity.startActivity(stopDetailsActivity);
                        mainActivity.overridePendingTransition(R.anim.activity_animation, R.anim.activity_animation2);
                    }
                }
            });

        return view;

    }

}
