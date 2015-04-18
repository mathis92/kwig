package com.example.martinhudec.kwigBA.serverConnection;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.stopDetail.RouteDetail;
import com.example.martinhudec.kwigBA.stopDetail.StopDetailsAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by martinhudec on 14/04/15.
 */
public class RequestStopDetails {
    String stopName;
    List<RouteDetail> routeData = new ArrayList<>();
    Boolean done = false;

    public RequestStopDetails(String stopName) {
        this.stopName = stopName;
    }

    public List<RouteDetail> request() {
        RequestQueue requestQueue = VolleySingleton.getsInstance().getmRequestQueue();
        String url = "http://bpbp.ctrgn.net/api/device";

        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        ;
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
                            done = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "ERROR");
                        done = true;
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
        while (!done) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return routeData;
    }

}
