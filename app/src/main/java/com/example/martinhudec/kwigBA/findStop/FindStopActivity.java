package com.example.martinhudec.kwigBA.findStop;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.equip.ReadJsonStops;
import com.example.martinhudec.kwigBA.serverConnection.VolleySingleton;

import com.example.martinhudec.kwigBA.stopDetail.StopDetailsAdapter;
import com.example.martinhudec.kwigBA.stopDetail.RouteDetail;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by martinhudec on 10/04/15.
 */
public class FindStopActivity extends ActionBarActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    StopDetailsAdapter stopDetailsAdapter;
    private TextView txtQuery;
    Activity activity;
    ReadJsonStops jsonStops;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_stop_activity);

        jsonStops = new ReadJsonStops(getResources().openRawResource(R.raw.stops_json_object));
        jsonStops.execute();
        // Enabling Back navigation on Action Bar icon
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // txtQuery = (TextView) findViewById(R.id.txtQuery);
        handleIntent(getIntent());
        Log.d("findStop activity ", "before recycler view call");
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) this.findViewById(R.id.find_stop_recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        activity = this;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<String> stopSuggestions = jsonStops.getStopSuggestions(s);

                Log.d("QUERY", stopSuggestions.toString());
                SuggestionsAdapter suggestionsAdapter = new SuggestionsAdapter(activity, stopSuggestions);
                recyclerView.setAdapter(suggestionsAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Log.d("query", query);
            getStopContent(query);
        }

    }

    public void getStopContent(final String stopName) {
        //final Activity activity = this;

        RequestQueue requestQueue = VolleySingleton.getsInstance().getmRequestQueue();
        String url = "http://bpbp.ctrgn.net/api/device";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<RouteDetail> routeData = new ArrayList<>();

                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                RouteDetail current = new RouteDetail();
                                switch (jsonArray.getJSONObject(i).getInt("routeType")) {
                                    case 0:
                                        current.setVehicleTypeIcon(R.drawable.tram_icon);
                                        break;
                                    case 2:
                                        current.setVehicleTypeIcon(R.drawable.bus_icon1);
                                        break;
                                    case 3:
                                        current.setVehicleTypeIcon(R.drawable.bus_icon1);
                                        break;
                                }

                                current.setVehicleId(jsonArray.getJSONObject(i).getString("routeId"));
                                current.setArrivalTime(jsonArray.getJSONObject(i).getString("arrivalTime"));
                                current.setDelay(jsonArray.getJSONObject(i).getString("delay"));
                                current.setHeadingTo(jsonArray.getJSONObject(i).getString("stopHeadSign"));
                                routeData.add(current);
                            }
                            Log.d("StopDetialsActivity", "routeData size " + routeData.size());
                            if (!routeData.isEmpty()) {
                                stopDetailsAdapter = new StopDetailsAdapter(activity, routeData);
                                recyclerView.removeAllViews();
                                recyclerView.setAdapter(stopDetailsAdapter);
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
                params.put("stopName", stopName);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }



}
