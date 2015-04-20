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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.martinhudec.kwigBA.R;

import com.example.martinhudec.kwigBA.map.Vehicle;
import com.example.martinhudec.kwigBA.serverConnection.VolleySingleton;
import com.example.martinhudec.kwigBA.stopDetail.RouteDetail;
import com.example.martinhudec.kwigBA.stopDetail.StopDetailsAdapter;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VehicleDetailsActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {
    Toolbar toolbar;
    Activity activity;
    Vehicle vehicle;
    SwipeRefreshLayout mSwipeRefreshVehicleDetail;
    CardView cardView;
    RecyclerView recyclerView;
    VehicleDetailsAdapter vehicleDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        vehicle = (Vehicle) b.getSerializable("vehicleObject");
        String vehicleType = null;
        Integer vehicleLogo = null;
        switch (vehicle.getVehicleType()){
            case 0: vehicleType = " tram no. ";
                vehicleLogo = R.drawable.tram_icon_small_white;
                break;
            case 2: vehicleType = " trolley bus no. ";
                vehicleLogo = R.drawable.bus_icon_small_white;
                break;
            case 3: vehicleType = " bus no. ";
                vehicleLogo = R.drawable.bus_icon_small_white;
                break;
        }
        this.setTitle(vehicleType + vehicle.getShortName());

        setContentView(R.layout.vehicle_detail_activity);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(vehicleLogo);
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


        if (mSwipeRefreshVehicleDetail.isRefreshing()) {
            requestVehicles(vehicle);

        }else {
            vehicleDetailsAdapter = new VehicleDetailsAdapter(activity, vehicle);
            recyclerView.setAdapter(vehicleDetailsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
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

    public void requestVehicles(final Vehicle vehicle) {
        Log.d("requestCurrentVehicle", vehicle.getId());
        RequestQueue requestQueue = VolleySingleton.getsInstance().getmRequestQueue();
        String url = "http://bpbp.ctrgn.net/api/device";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Log.d("Response", response);
                        Vehicle currentVehicle = null;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                currentVehicle = new Vehicle();
                                currentVehicle.vehicleType = jsonArray.getJSONObject(i).getInt("vehicleType");
                                currentVehicle.id = (jsonArray.getJSONObject(i).getString("id"));
                                currentVehicle.lon = Float.parseFloat(jsonArray.getJSONObject(i).getString("lon"));
                                currentVehicle.lat = Float.parseFloat(jsonArray.getJSONObject(i).getString("lat"));
                                currentVehicle.delay = jsonArray.getJSONObject(i).getString("delay");
                                currentVehicle.speed = jsonArray.getJSONObject(i).getString("speed");
                                currentVehicle.headingTo = jsonArray.getJSONObject(i).getString("headingTo");
                                currentVehicle.shortName = jsonArray.getJSONObject(i).getString("shortName");
                                currentVehicle.lastStop = jsonArray.getJSONObject(i).getString("lastStop");
                                currentVehicle.nextStop = jsonArray.getJSONObject(i).getString("nextStop");
                                currentVehicle.arrivalTime = jsonArray.getJSONObject(i).getString("arrivalTime");
                            }
                            mSwipeRefreshVehicleDetail.setRefreshing(false);
                            vehicleDetailsAdapter = new VehicleDetailsAdapter(activity, currentVehicle);
                            recyclerView.setAdapter(vehicleDetailsAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", "error");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("requestContent", "currentVehicleDetail");
                params.put("vehicleId", vehicle.getId());
                return params;
            }
        };
        requestQueue.add(postRequest);
    }
}
