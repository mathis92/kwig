package com.example.martinhudec.kwigBA.stopDetail;

import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.serverConnection.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StopDetailsActivity extends ActionBarActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    Adapter adapter;
    String stopName = null;
    List<RouteDetail> routeData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        stopName = b.getString("stopName");

        setContentView(R.layout.stop_detail_activity);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        final Activity activity = this;

        RequestQueue requestQueue = VolleySingleton.getsInstance().getmRequestQueue();
        String url = "http://bpbp.ctrgn.net/api/device";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i<jsonArray.length(); i++){
                                RouteDetail current = new RouteDetail();
                                switch (jsonArray.getJSONObject(i).getInt("routeType")){
                                    case 0: current.vehicleTypeIcon = R.drawable.tram_icon;
                                        break;
                                    case 2: current.vehicleTypeIcon = R.drawable.bus_icon1;
                                        break;
                                    case 3: current.vehicleTypeIcon = R.drawable.bus_icon1;
                                        break;
                                }

                                current.vehicleId = jsonArray.getJSONObject(i).getString("routeId");
                                current.arrivalTime = jsonArray.getJSONObject(i).getString("arrivalTime");
                                current.delay =  jsonArray.getJSONObject(i).getString("delay");
                                current.headingTo = jsonArray.getJSONObject(i).getString("stopHeadSign");
                                routeData.add(current);
                            }
                            Log.d("StopDetialsActivity","routeData size " + routeData.size());
                            if(!routeData.isEmpty()) {
                                adapter = new Adapter(activity, routeData);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
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
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("requestContent", "CurrentStop");

                /*byte[] data = null;
                try {
                    data = stopName.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String base64StopName = Base64.encodeToString(data, Base64.DEFAULT);
                */
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
}