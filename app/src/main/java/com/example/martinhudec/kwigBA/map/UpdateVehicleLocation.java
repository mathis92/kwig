package com.example.martinhudec.kwigBA.map;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;


import com.example.martinhudec.kwigBA.equip.GetLatLng;
import com.google.android.gms.internal.cu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by martinhudec on 07/12/14.
 */
public class UpdateVehicleLocation extends AsyncTask<Void, Void, Void> {

    GoogleMap mMap;
    boolean action = true;
    List<MarkerDetails> currentlyDisplayed;
    ShowVehicles showVehicles;
    public UpdateVehicleLocation(GoogleMap mMap, List<MarkerDetails> currentlyDisplayed) {
        this.currentlyDisplayed = currentlyDisplayed;
        this.mMap = mMap;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        action = false;
    }

    @Override
    protected Void doInBackground(Void... params) {
            publishProgress();
        return null;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
     //   mMap.moveCamera(CameraUpdateFactory.newLatLng(mMap.getCameraPosition().target));
       // UpdateLocation updateLocation = new UpdateLocation(mMap,currentlyDisplayed);
      //  new Thread(updateLocation).start();
       /* showVehicles = new ShowVehicles(mMap, currentlyDisplayed);
        showVehicles.execute(mMap.getProjection().getVisibleRegion().latLngBounds,mMap.getCameraPosition().zoom);
        */
        Log.d("UpdateVehicleLocation", "location");
    }

}
