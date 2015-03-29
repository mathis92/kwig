package com.example.martinhudec.kwigBA;

import android.os.AsyncTask;


import com.example.martinhudec.kwigBA.equip.GetLatLng;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by martinhudec on 06/12/14.
 */
public class RequestSend extends AsyncTask<Object[], String, LatLng> {
    GoogleMap mMap;


    public RequestSend(GoogleMap mMap) {
        this.mMap = mMap;
    }

    private LatLng ll;


    @Override
    protected LatLng doInBackground(Object[]... params) {

        LatLng ll = new GetLatLng().getLl();

        return ll;
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        super.onPostExecute(latLng);
        System.out.println(latLng);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);

        mMap.animateCamera(update);
        mMap.addMarker(new MarkerOptions().position(latLng).title("tu je ten SIGAN BANAN")).showInfoWindow();


    }
}


