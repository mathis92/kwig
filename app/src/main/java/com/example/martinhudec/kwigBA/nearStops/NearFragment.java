package com.example.martinhudec.kwigBA.nearStops;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.martinhudec.kwigBA.MainActivity;
import com.example.martinhudec.kwigBA.R;

import com.example.martinhudec.kwigBA.equip.DividerItemDecoration;
import com.example.martinhudec.kwigBA.equip.ReadJsonStops;
import com.example.martinhudec.kwigBA.map.Stop;
import com.example.martinhudec.kwigBA.map.MarkerDetails;
import com.example.martinhudec.kwigBA.serverConnection.VolleySingleton;
import com.example.martinhudec.kwigBA.stopDetail.RouteDetail;
import com.gc.materialdesign.views.ButtonFlat;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by martinhudec on 29/03/15.
 */


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class NearFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private MainActivity mainActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View nearStopsLayout = null;
    private RecyclerView recyclerView;
    private ReadJsonStops jsonStops;


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


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        jsonStops = new ReadJsonStops(getResources().openRawResource(R.raw.stops_json_object));
        jsonStops.execute();
        mainActivity = (MainActivity) getActivity();
        nearStopsLayout = inflater.inflate(R.layout.near_stops_fragment, container, false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mainActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) nearStopsLayout.findViewById(R.id.near_stops_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        //      showStopsInRecyclerView();
        mSwipeRefreshLayout = (SwipeRefreshLayout) nearStopsLayout.findViewById(R.id.swipeRefreshNearStops);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return nearStopsLayout;
    }

    public void showStopsInRecyclerView() {
        final List<Stop> nearStops = jsonStops.getNearStops(mainActivity.getLastKnownLocation());
        final List<StopDetailsWithRoutes> stopDetailsWithRoutesList = new ArrayList<>();

        int i = 0;

        for (final Stop stop : nearStops) {
            RequestQueue requestQueue = VolleySingleton.getsInstance().getmRequestQueue();
            String url = "http://bpbp.ctrgn.net/api/device";
            final StopDetailsWithRoutes stopDetailsWithRoutes = new StopDetailsWithRoutes();

            final int finalI = i;
            StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            stopDetailsWithRoutes.stop = stop;
                  //          Log.d("Response", response);
                            List<RouteDetail> routeDetails = new ArrayList<>();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    RouteDetail currentRouteDetail = new RouteDetail();
                                    currentRouteDetail.setVehicleType(jsonArray.getJSONObject(i).getInt("routeType"));
                                    switch (jsonArray.getJSONObject(i).getInt("routeType")) {
                                        case 0:
                                            currentRouteDetail.setVehicleTypeIcon(R.drawable.transport_tram_512p);
                                            break;
                                        case 2:
                                            currentRouteDetail.setVehicleTypeIcon(R.drawable.transport_trolleybus_512p);
                                            break;
                                        case 3:
                                            currentRouteDetail.setVehicleTypeIcon(R.drawable.transport_bus_512p);
                                            break;
                                    }

                                    currentRouteDetail.setVehicleShortName(jsonArray.getJSONObject(i).getString("vehicleShortName"));
                                    currentRouteDetail.setArrivalTime(jsonArray.getJSONObject(i).getString("arrivalTime"));
                                    currentRouteDetail.setDelay(jsonArray.getJSONObject(i).getString("delay"));
                                    currentRouteDetail.setHeadingTo(jsonArray.getJSONObject(i).getString("stopHeadSign"));
                                    currentRouteDetail.setVehicleId(jsonArray.getJSONObject(i).getString("vehicleId"));
                                    routeDetails.add(currentRouteDetail);
                                }


                                stopDetailsWithRoutes.routeDetailList = routeDetails;
                                stopDetailsWithRoutesList.add(stopDetailsWithRoutes);
                             //   Log.d("StopDetialsActivity", "routeData size " + stop.getStopName() + " near stop size " + nearStops.size() + " finalI " + finalI + " stopDetalisWithRoutesListSize " + stopDetailsWithRoutesList.size());
                             //   if (finalI + 1 == nearStops.size() && stopDetailsWithRoutesList.size() == nearStops.size()) {
                                    if (stopDetailsWithRoutesList.size() == nearStops.size()) {
                                    NearStopsAdapter nearStopsAdapter = new NearStopsAdapter(mainActivity, stopDetailsWithRoutesList);
                                    recyclerView.setAdapter(nearStopsAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
                                    if (mSwipeRefreshLayout.isRefreshing()){
                                        mSwipeRefreshLayout.setRefreshing(false);
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
                            Log.d("NEAR FRAGMENT", "ERROR");

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("requestContent", "CurrentStop");
                    Log.d("STOP NAME", stop.getStopName());
                    params.put("stopName", stop.getStopName());
                    params.put("count", ((Integer)3).toString());
                    return params;
                }
            };
            requestQueue.add(postRequest);
            i++;
        }


    }


    @Override
    public void onRefresh() {
       // Toast.makeText(getActivity(),"ONREFRESH",Toast.LENGTH_SHORT).show();
        showStopsInRecyclerView();
    }
}