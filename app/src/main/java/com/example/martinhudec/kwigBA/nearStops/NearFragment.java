package com.example.martinhudec.kwigBA.nearStops;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinhudec.kwigBA.MainActivity;
import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.RequestSend;
import com.example.martinhudec.kwigBA.map.UpdateVehicleLocation;
import com.example.martinhudec.kwigBA.map.MarkerDetails;
import com.gc.materialdesign.views.ButtonFlat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;
import java.util.List;

/**
 * Created by martinhudec on 29/03/15.
 */
/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class NearFragment extends Fragment implements LocationListener{

    private LocationManager locationManager;
    private LocationListener locationListener;
    private int camera = 0;

    private LatLng ll;
    private RequestSend rs;
    private UpdateVehicleLocation updateVehicleLocation = null;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MainActivity mainActivity;
    public InputStream stopsIS;
    public Location startLocation = null;
    private ViewGroup markerInfoWindow = null;
    private TextView infoWindowTitle = null;
    private ButtonFlat infoWindowButton = null;
    private View mapLayout = null;
    private List<MarkerDetails> markerDetailsList = null;

    public NearFragment() {
        // Required empty public constructor

    }

    public static NearFragment getInstance(int position) {
        NearFragment mapsFragment = new NearFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        mapsFragment.setArguments(args);
        return mapsFragment;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        mapLayout = inflater.inflate(R.layout.near_stops_fragment, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(mainActivity,"LOCATION " + location.getLatitude() + " " + location.getLongitude(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}