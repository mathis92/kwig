package com.example.martinhudec.kwigBA.map;

import android.os.AsyncTask;


import com.example.martinhudec.kwigBA.equip.GetLatLng;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by martinhudec on 07/12/14.
 */
public class LocateVehicle extends AsyncTask<Object[], LatLng, LatLng> {

    private boolean active = true;
    LatLng latlng;
    GoogleMap mMap;
    Boolean mu = false;

    public LocateVehicle(GoogleMap mMap) {

        this.mMap = mMap;
    }

    LatLng ll;

    String[] arr;


    public boolean isActive() {
        return active;
    }

    public void startLocalizing() {
        active = true;
    }

    public void stopLocalizing() {
        active = false;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

    }

    @Override
    protected LatLng doInBackground(Object[]... params) {
        System.out.println("idem updatovat z locateVehicle");
        LatLng latlng = null;
        boolean action = true;
        while (action) {

            latlng = new GetLatLng().getLl();

            publishProgress(latlng);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return latlng;
    }



    @Override
    protected void onProgressUpdate(LatLng... values) {
        super.onProgressUpdate(values);

        if (mu == false) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(values[0], 16);
            mu = true;
            mMap.animateCamera(update);
        }

        mMap.addMarker(new MarkerOptions().position(values[0]).title("vehicle"));
        /*
        mMap.addCircle(new CircleOptions()
                .center(values[0])
                .radius(5)
                .strokeColor(Color.WHITE)
                .fillColor(Color.BLUE));
        //  mMap.addMarker(new MarkerOptions().position(values[0])).setTitle("shit");
    */
    }


}
