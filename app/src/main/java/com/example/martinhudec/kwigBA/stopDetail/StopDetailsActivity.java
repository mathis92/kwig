package com.example.martinhudec.kwigBA.stopDetail;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.equip.DividerItemDecoration;
import com.example.martinhudec.kwigBA.serverConnection.VolleySingleton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StopDetailsActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {
    Toolbar toolbar;
    RecyclerView recyclerView;
    StopDetailsAdapter stopDetailsAdapter;
    String stopName = null;
    List<RouteDetail> routeData = new ArrayList<>();
    Activity activity;
    SwipeRefreshLayout mSwipeRefreshStopDetail;
    CircularProgressView mCircularProgressView;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        stopName = b.getString("stopName");
        this.setTitle(" " + stopName);
        setContentView(R.layout.stop_detail_activity);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.stop_icon_small_white);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCircularProgressView = (CircularProgressView) findViewById(R.id.progress_view);
        mCircularProgressView.setIndeterminate(true);
        mCircularProgressView.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));

        activity = this;
        mSwipeRefreshStopDetail = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshStopDetail);
        mSwipeRefreshStopDetail.setOnRefreshListener(this);

        requestStops();
    }

        public void requestStops(){
            //routeData = new ArrayList<>();
        RequestQueue requestQueue = VolleySingleton.getsInstance().getmRequestQueue();
        String url = "http://bpbp.ctrgn.net/api/device";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i<jsonArray.length(); i++){
                                RouteDetail current = new RouteDetail();
                                switch (jsonArray.getJSONObject(i).getInt("routeType")){
                                    case 0: current.vehicleTypeIcon = R.drawable.transport_tram_512p;
                                        break;
                                    case 2: current.vehicleTypeIcon = R.drawable.transport_trolleybus_512p;
                                        break;
                                    case 3: current.vehicleTypeIcon = R.drawable.transport_bus_512p;
                                        break;
                                }

                                current.vehicleShortName = jsonArray.getJSONObject(i).getString("vehicleShortName");
                                current.arrivalTime = jsonArray.getJSONObject(i).getString("arrivalTime");
                                current.delay =  jsonArray.getJSONObject(i).getString("delay");
                                current.headingTo = jsonArray.getJSONObject(i).getString("stopHeadSign");
                                current.setVehicleId(jsonArray.getJSONObject(i).getString("vehicleId"));
                                current.setVehicleType(jsonArray.getJSONObject(i).getInt("routeType"));
                                routeData.add(current);
                            }
                        //    Log.d("StopDetialsActivity","routeData size " + routeData.size());
                            if(!routeData.isEmpty()) {
                                mCircularProgressView.setIndeterminate(false);
                                mCircularProgressView.setVisibility(View.INVISIBLE);

                                stopDetailsAdapter = new StopDetailsAdapter(activity, routeData);
                                recyclerView.setAdapter(stopDetailsAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                                if(mSwipeRefreshStopDetail.isRefreshing()){
                                    mSwipeRefreshStopDetail.setRefreshing(false);

                                }
                            }
                            } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("STOP DETAILS ACTIVITY", "error");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("requestContent", "CurrentStop");
                params.put("stopName", stopName);

                return params;
            }
        };
        requestQueue.add(postRequest);


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
        requestStops();

    }
}
