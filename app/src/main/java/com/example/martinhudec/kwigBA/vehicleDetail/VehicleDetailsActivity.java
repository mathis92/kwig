package com.example.martinhudec.kwigBA.vehicleDetail;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.map.InterfaceVehicle;
import com.example.martinhudec.kwigBA.map.Vehicle;
import com.example.martinhudec.kwigBA.stopDetail.StopDetailsAdapter;

import java.util.ArrayList;
import java.util.List;


public class VehicleDetailsActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {
    Toolbar toolbar;
    Activity activity;
    Vehicle vehicle;
    SwipeRefreshLayout mSwipeRefreshVehicleDetail;
    TextView headingTo;
    TextView shortName;
    TextView arrivalTime;
    TextView delay;
    TextView lastStop;
    TextView nextStop;
    ImageView vehicleImage;
    CardView cardView;
    RecyclerView recyclerView;
    VehicleDetailsAdapter vehicleDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        vehicle = (Vehicle) b.getSerializable("vehicleObject");
        this.setTitle(vehicle.getShortName());

        setContentView(R.layout.vehicle_detail_activity);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cardView = (CardView) findViewById(R.id.card_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.vehicle_detail_recycler_view);
        recyclerView.setLayoutManager(layoutManager);



        activity = this;
        mSwipeRefreshVehicleDetail = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshVehicleDetail);
        mSwipeRefreshVehicleDetail.setOnRefreshListener(this);

        showVehicleDetails();
    }

    public void showVehicleDetails() {

        vehicleDetailsAdapter = new VehicleDetailsAdapter(activity, vehicle);
        recyclerView.setAdapter(vehicleDetailsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        if (mSwipeRefreshVehicleDetail.isRefreshing()) {
            mSwipeRefreshVehicleDetail.setRefreshing(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return true;
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
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        showVehicleDetails();
    }


}
