package com.example.martinhudec.kwigBA;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.martinhudec.kwigBA.equip.Delay;
import com.example.martinhudec.kwigBA.findStop.FindStopActivity;
import com.example.martinhudec.kwigBA.map.MapsFragment;
import com.example.martinhudec.kwigBA.nearStops.NearFragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.OnFragmentInteractionListener, MaterialTabListener {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private MaterialTabHost tabHost;
    private LocationManager locationManager;
    private MapsFragment mapsFragment;
    private NearFragment nearFragment;
    private LocationListener locationListener;
    private String MAPS_FRAGMENT_TAG = "mapFragment";
    private List<Fragment> fragmentList;
    private Location newLocation;
    public static android.app.FragmentManager fragmentManager;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Delay(this);
        this.savedInstanceState = savedInstanceState;

        Resources res = this.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale("sk".toLowerCase());
        res.updateConfiguration(conf, dm);



       // Log.d("MAIN ACTIVITY", "onCREATE");
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
        return loc;
    }

    public Location getCurrentLocation() {
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
        switch (materialTab.getPosition()) {
            case 1: {
                ((NearFragment) fragmentList.get(1)).showStopsInRecyclerView();
            }
        }
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {
        switch (materialTab.getPosition()) {
            case 1: {
                ((NearFragment) fragmentList.get(1)).showStopsInRecyclerView();
            }
        }
    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);


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
                    mapsFragment = MapsFragment.getInstance(position);
                    mapsFragment.setInputStream(stopsIS);
                    mapsFragment.setInputStreamDetailed(getResources().openRawResource(R.raw.stops_detailed));
                    fragment = (Fragment) mapsFragment;
                    fragmentList.add(fragment);

               /*
                    if (savedInstanceState == null) {
                        mapsFragment = MapsFragment.getInstance(position);
                        getSupportFragmentManager()
                                .beginTransaction().remove(mapsFragment)
                                .add(R.id.mainActivity, mapsFragment, MAPS_FRAGMENT_TAG)
                                .addToBackStack(MAPS_FRAGMENT_TAG).commit();

                        mapsFragment.setInputStream(stopsIS);
                        fragment = (Fragment) mapsFragment;
                        fragmentList.add(fragment);


                    }else {
                        mapsFragment = (MapsFragment) getSupportFragmentManager().findFragmentByTag(MAPS_FRAGMENT_TAG);
                        fragment = mapsFragment;
                    }
*/
                    break;
                }
                case 1: {
                    nearFragment = NearFragment.getInstance(position);
                    fragment = nearFragment;
                    fragmentList.add(fragment);
                    break;
                }

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

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MAIN ACTIVITY", "onPause");

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MAIN ACTIVITY", "onResume");

    }


}
