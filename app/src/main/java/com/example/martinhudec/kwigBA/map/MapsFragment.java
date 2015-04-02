package com.example.martinhudec.kwigBA.map;


import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.example.martinhudec.kwigBA.RequestSend;
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
import java.util.List;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback{

    private LocationManager locationManager;
    private LocationListener locationListener;
    private int camera = 0;

    private LatLng ll;
    private RequestSend rs;
    private LocateVehicle locateVehicle = null;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MainActivity mainActivity;
    public InputStream stopsIS;
    public Location startLocation = null;
    private ViewGroup markerInfoWindow = null;
    private TextView infoWindowTitle = null;
    private ButtonFlat infoWindowButton = null;
    private View mapLayout = null;
    private List<MarkerDetails> markerDetailsList = null;

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        mapLayout = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment smf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapview);
        smf.getMapAsync(this);

        Log.d("MapsFragment", " On create view runing");
        markerInfoWindow = (ViewGroup) mainActivity.getLayoutInflater().inflate(R.layout.marker_info_window, null);
        infoWindowButton = (ButtonFlat) markerInfoWindow.findViewById(R.id.markerButton);
        infoWindowTitle = (TextView) markerInfoWindow.findViewById(R.id.markerTitle);

        return mapLayout;


    }

    public void startLocManager() {
        Log.d("MapsFragment", " starting Location Manager");
        locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);


        String provider = locationManager.getBestProvider(new Criteria(), true);
        Location loc = locationManager.getLastKnownLocation(provider);
        Log.d("MapsFragment", loc.toString() + " provider " + provider);

        if (loc != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 15));
            startLocation = loc;
        }


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {

                // Getting view from the layout file info_window_layout


                // Getting reference to the TextView to set title
                TextView note = infoWindowTitle;

                note.setText(marker.getTitle());
                View button = infoWindowButton;
                Log.d("MapsFragment", "found button starting on click listener");


                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Log.d("MapsFragment", "Button on click listener ");
                        Intent stopDetailsActivity = new Intent(mainActivity, StopDetailsActivity.class);
                        Bundle b = new Bundle();
                        b.putString("stopName", marker.getTitle()); //Your id
                        stopDetailsActivity.putExtras(b); //Put your id to your next Intent
                        startActivity(stopDetailsActivity);


                        mainActivity.overridePendingTransition(R.anim.activity_animation, R.anim.activity_animation2);
                    }
                });

                // Returning the view containing InfoWindow contents
                return markerInfoWindow;

            }

        });


        ShowStops showStops = new ShowStops(mMap);
        try {
            List<Stop> list = readJsonStream(stopsIS);
            showStops.execute(list);

        } catch (IOException e) {
            e.printStackTrace();
        }


        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                ll = new LatLng(location.getLatitude(), location.getLongitude());

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        mMap = googleMap;
        startLocManager();
        //Onzo
    }


    public List readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readStopsObject(reader);
        } finally {
            reader.close();
        }
    }

    public List readStopsObject(JsonReader reader) throws IOException {
        List stops = new ArrayList();
        reader.beginObject();
        while (reader.hasNext()) {
            String objName = reader.nextName();
            if (objName.equals("stops")) {
                stops = readStopsArray(reader);
            }
        }
        reader.close();
        return stops;
    }

    public List readStopsArray(JsonReader reader) throws IOException {
        List stops = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            stops.add(readStops(reader));
        }
        reader.endArray();
        return stops;
    }

    public Stop readStops(JsonReader reader) throws IOException {
        long id = -1;
        String name = null;
        Double lat = null;
        Double lon = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String objName = reader.nextName();
            if (objName.equals("id")) {
                id = reader.nextLong();
            } else if (objName.equals("name")) {
                name = reader.nextString();
            } else if (objName.equals("lat")) {
                lat = reader.nextDouble();
            } else if (objName.equals("lon")) {
                lon = reader.nextDouble();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Stop(id, name, lat, lon);
    }

}

