package com.example.martinhudec.kwigBA.map;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.martinhudec.kwigBA.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinhudec on 26/03/15.
 */
public class ShowStops extends AsyncTask<Object, List<Stop>, List<Stop>> {
    GoogleMap mMap = null;
    List<Marker> markerList = null;
    List<Marker> currentlyDisplayed = null;

    public ShowStops(GoogleMap mMap) {
        this.mMap = mMap;
        currentlyDisplayed = new ArrayList<>();

    }

    @Override
    protected List<Stop> doInBackground(Object... params) {
        List<Stop> stopList = (List<Stop>)(params[0]);

        publishProgress(stopList);
        return null;
    }


    @Override
    protected void onProgressUpdate(List<Stop>... values) {
        if (markerList == null) {
            markerList = new ArrayList<>();
            for (Stop stop : values[0]) {
                Marker mark = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_icon2))
                        .title(stop.getStopName())
                        .position(stop.getLatLng())
                        .visible(false));
                markerList.add(mark);
            }
        }
        mMap.setOnCameraChangeListener(getCameraChangeListener(values[0]));
        mMap.setOnMarkerClickListener(getOnMarkerClickListener());
    }

    public GoogleMap.OnCameraChangeListener getCameraChangeListener(final List<Stop> values) {

        return new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                Log.d("Zoom", "Zoom: " + position.zoom);
                LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                Log.d("bounds", "Bounds " + bounds.getCenter() + " " + bounds.southwest + " " + bounds.northeast);
                drawStops(position.zoom);
            }
        };
    }

    public GoogleMap.OnMarkerClickListener getOnMarkerClickListener(){
        return new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();

                return false;
            }
        };
    }


    public void drawStops(float zoom) {
        if (zoom > 14) {
            for (Marker marker : markerList) {
               marker.setVisible(true);
            }
        } else {
            for (Marker marker : markerList) {
                marker.setVisible(false);
            }
        }
    }

}
