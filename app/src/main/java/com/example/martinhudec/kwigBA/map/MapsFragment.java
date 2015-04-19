package com.example.martinhudec.kwigBA.map;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.martinhudec.kwigBA.MainActivity;
import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.stopDetail.StopDetailsActivity;
import com.gc.materialdesign.views.ButtonFlat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MainActivity mainActivity;
    public InputStream stopsIS;
    public Location startLocation = null;

    private View mapLayout = null;
    private List<MarkerDetails> currentlyDisplayedVehicles = null;
    private List<MarkerDetails> currentlyDisplayedStops = null;
    private UpdateVehiclePositions updateVehiclePositions;
    private Boolean locationManagerOn;
    private HashMap<Marker, Object> markerObjectHashMap = new HashMap<>();


    public MapsFragment() {
        // Required empty public constructor

    }

    public static MapsFragment getInstance(int position) {
        MapsFragment mapsFragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        mapsFragment.setArguments(args);
        return mapsFragment;

    }

    public void setInputStream(InputStream stopsIS) {
        this.stopsIS = stopsIS;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("MAPSFRAGMENT", "onPause");
        stopUpdatingVehiclePositions();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MAPSFRAGMENT", "onResume");
        if (locationManagerOn == true) {
            startUpdatingVehiclePositions();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        mapLayout = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment smf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapview);
        smf.getMapAsync(this);

        Log.d("MapsFragment", " On create view runing");
        currentlyDisplayedVehicles = new CopyOnWriteArrayList<>();
        currentlyDisplayedStops = new CopyOnWriteArrayList<>();
        locationManagerOn = false;

        return mapLayout;


    }


    public void startLocManager() {
        Log.d("MapsFragment", " starting Location Manager");


        Location loc = mainActivity.getLastKnownLocation();
        if (loc != null) {
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.162222,17.123807),15));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 15));
            startLocation = loc;
        }
        mMap.setInfoWindowAdapter(new InfoWindowAdapter(mainActivity, markerObjectHashMap, mMap));

        OnCameraChangeListener onCameraChangeListener = new OnCameraChangeListener(mMap, stopsIS, currentlyDisplayedVehicles, currentlyDisplayedStops,markerObjectHashMap, this);
        this.mMap.setOnCameraChangeListener(onCameraChangeListener);
        startUpdatingVehiclePositions();
        locationManagerOn = true;


    }

    public void stopUpdatingVehiclePositions() {
        updateVehiclePositions.stopUpdate();
    }

    public void startUpdatingVehiclePositions() {
        updateVehiclePositions = new UpdateVehiclePositions(mMap, currentlyDisplayedVehicles, markerObjectHashMap);
        mainActivity.runOnUiThread(updateVehiclePositions);
    }

    public void updatePositions() {
        updateVehiclePositions.updateMapReference(mMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        mMap = googleMap;
        startLocManager();

    }



}


