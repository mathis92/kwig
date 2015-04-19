package com.example.martinhudec.kwigBA;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.martinhudec.kwigBA.findStop.FindStopActivity;
import com.example.martinhudec.kwigBA.map.MapsFragment;
import com.example.martinhudec.kwigBA.map.OnCameraChangeListener;
import com.example.martinhudec.kwigBA.nearStops.NearFragment;
import com.example.martinhudec.kwigBA.stopDetail.StopDetailsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.OnFragmentInteractionListener, MaterialTabListener, OnMapReadyCallback {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private MaterialTabHost tabHost;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private List<Fragment> fragmentList;
    private Location newLocation;
    public static android.app.FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fragmentManager = getFragmentManager();
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        setTitle("KWiG");
        fragmentList = new ArrayList<>();
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
    startLocManager();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void startLocManager() {
        Log.d("MapsFragment", " starting Location Manager");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                newLocation = location;

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 0, locationListener);
    }

    public Location getLastKnownLocation() {
        String provider = locationManager.getBestProvider(new Criteria(), true);
        Location loc = locationManager.getLastKnownLocation(provider);
        Log.d("MapsFragment", loc.toString() + " provider " + provider);
        return loc;
    }

    public Location getCurrentLocation(){
        return newLocation;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.navigate) {
            //startActivity(new Intent(this, SubActivity.class));
            startActivity(new Intent(this, FindStopActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition());
        switch (materialTab.getPosition()){
            case 1:{
                ((NearFragment)fragmentList.get(1)).showStopsInRecyclerView();
            }
        }
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {
        switch (materialTab.getPosition()){
            case 1:{
                ((NearFragment)fragmentList.get(1)).showStopsInRecyclerView();
            }
        }
    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        String[] tabs;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            InputStream stopsIS = getResources().openRawResource(R.raw.stops_json_object);
            switch (position) {
                case 0: {

                    MapsFragment mapsFragment = MapsFragment.getInstance(position);
                    mapsFragment.setInputStream(stopsIS);
                    fragment = (Fragment) mapsFragment;
                    fragmentList.add(fragment);
                    break;
                }
                case 1: {
                    fragment = NearFragment.getInstance(position);
                    fragmentList.add(fragment);
                    break;
                }
           /*     case 2: {
                    fragment = MyFragment.getInstance(position);
                    break;
                }
                */
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    public static class MyFragment extends Fragment {
        TextView textView;

        public static MyFragment getInstance(int position) {
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_my, container, false);
            textView = (TextView) layout.findViewById(R.id.position);
            Bundle bundle = getArguments();
            if (bundle != null) {
                textView.setText("selected tab " + bundle.getInt("position"));
            }

            return layout;
        }
    }

}
