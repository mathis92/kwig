package com.example.martinhudec.kwigBA.map;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.serverConnection.VolleySingleton;
import com.example.martinhudec.kwigBA.stopDetail.Adapter;
import com.example.martinhudec.kwigBA.stopDetail.RouteDetail;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by martinhudec on 06/04/15.
 */
public class ShowVehicles extends AsyncTask<LatLngBounds, List<Vehicle>, List<Vehicle>> {
    GoogleMap mMap = null;

    List<Vehicle> currentlyDisplayed = null;
    List<Vehicle> currentVehicleList = null;

    public ShowVehicles(GoogleMap mMap) {
        this.mMap = mMap;

        currentlyDisplayed = new ArrayList<>();

    }

    @Override
    protected List<Vehicle> doInBackground(LatLngBounds... params) {

        currentVehicleList = requestVehicles(params[0]);
        publishProgress();
        return currentVehicleList;
    }


    public GoogleMap.OnCameraChangeListener getCameraChangeListener() {

        return new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                currentVehicleList = requestVehicles(bounds);
                Log.d("bounds changed", bounds.toString());
            }
        };
    }
    @Override
    protected void onProgressUpdate(List<Vehicle>... values) {
        super.onProgressUpdate(values);
        Log.d("ON PROGRESS MADABIX", "");
     //   this.mMap.setOnCameraChangeListener(getCameraChangeListener());

    }

    public List<Vehicle> requestVehicles(final LatLngBounds bounds) {
        Log.d("requestVehicles", bounds.toString());
        RequestQueue requestQueue = VolleySingleton.getsInstance().getmRequestQueue();
        String url = "http://bpbp.ctrgn.net/api/device";
        final Double accuracyLat = (bounds.southwest.latitude - bounds.northeast.latitude)*2;
        final Double accuracyLon = (bounds.southwest.longitude - bounds.northeast.longitude)*2;
        final List<Vehicle> vehicleList = new ArrayList<>();


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Vehicle currentVehicle = new Vehicle();
                                switch (jsonArray.getJSONObject(i).getInt("vehicleType")) {
                                    case 0:
                                        currentVehicle.vehicleTypeIcon = R.drawable.tram_icon;
                                        break;
                                    case 2:
                                        currentVehicle.vehicleTypeIcon = R.drawable.bus_icon1;
                                        break;
                                    case 3:
                                        currentVehicle.vehicleTypeIcon = R.drawable.bus_icon1;
                                        break;
                                }

                                currentVehicle.lon = Float.parseFloat(jsonArray.getJSONObject(i).getString("lon"));
                                currentVehicle.lat = Float.parseFloat(jsonArray.getJSONObject(i).getString("lat"));
                                currentVehicle.delay = jsonArray.getJSONObject(i).getString("delay");
                                currentVehicle.speed = jsonArray.getJSONObject(i).getString("speed");
                                currentVehicle.headingTo = jsonArray.getJSONObject(i).getString("headingTo");
                                currentVehicle.shortName = jsonArray.getJSONObject(i).getString("shortName");
                                currentVehicle.lastStop = jsonArray.getJSONObject(i).getString("lastStop");
                                currentVehicle.nextStop = jsonArray.getJSONObject(i).getString("nextStop");
                                currentVehicle.arrivalTime = jsonArray.getJSONObject(i).getString("arrivalTime");
                                vehicleList.add(currentVehicle);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        publishProgress();
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
                params.put("requestContent", "vehiclesPositions");
                params.put("lon", ((Double) bounds.getCenter().longitude).toString());
                params.put("lat", ((Double)bounds.getCenter().latitude).toString());
                params.put("accLat", accuracyLat.toString());
                params.put("accLon", accuracyLon.toString());
                return params;
            }
        };
        requestQueue.add(postRequest);
        return vehicleList;
    }


}
