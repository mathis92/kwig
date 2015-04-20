package com.example.martinhudec.kwigBA.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.serverConnection.VolleySingleton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by martinhudec on 06/04/15.
 */
public class ShowVehicles extends AsyncTask<Object, List<Vehicle>, List<Vehicle>> {
    GoogleMap mMap = null;

    List<MarkerDetails> currentlyDisplayed = null;
    List<Vehicle> currentVehicleList = null;
    HashMap<Marker, Object> markerObjectHashMap;

    public ShowVehicles(GoogleMap mMap, List<MarkerDetails> currentlyDisplayed, HashMap<Marker, Object> markerObjectHashMap) {
        this.mMap = mMap;
        this.currentlyDisplayed = currentlyDisplayed;
        this.markerObjectHashMap = markerObjectHashMap;
    }

    @Override
    protected List<Vehicle> doInBackground(Object... params) {
    Log.d("FRAJERSKY","");
        if (((CameraPosition) params[1]).zoom > 14) {
            requestVehicles((LatLngBounds) params[0]);
        } else {
            currentVehicleList = new ArrayList<>();
        }
        while (currentVehicleList == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return currentVehicleList;
    }

    @Override
    protected void onProgressUpdate(List<Vehicle>... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<Vehicle> vehicles) {
        super.onPostExecute(vehicles);
        Log.d("ON POST EXECUTE", ((Integer) vehicles.size()).toString());
        for (MarkerDetails mark : currentlyDisplayed) {
            int solved = 0;
            for (Vehicle vehicle : vehicles) {
                if (mark.getVehicle().id.equals(vehicle.id)) {
                    solved = 1;
                   // mark.getMarker().setPosition(new LatLng(vehicle.lat, vehicle.lon));
                    animateMarker(mark.getMarker(),new LatLng(vehicle.lat, vehicle.lon), false);
                }
            }
            if (solved == 0) {
                mark.getMarker().remove();
                currentlyDisplayed.remove(mark);
            }
        }
        for (Vehicle vehicle : vehicles) {
            int found = 0;
            for (MarkerDetails mark : currentlyDisplayed) {
                if (mark.getVehicle().id.equals(vehicle.id)) {
                    found = 1;
                }
            }
            if (found == 0) {
                Integer icon = null;


                Marker marker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(vehicle.getVehicleTypeIcon()))
                        .position(new LatLng(vehicle.lat, vehicle.lon))
                        .title(vehicle.shortName));
                currentlyDisplayed.add(new MarkerDetails(marker, vehicle));
                markerObjectHashMap.put(marker,vehicle);
            }
        }

    }

    public void requestVehicles(final LatLngBounds bounds) {
        Log.d("requestVehicles", bounds.toString());
        RequestQueue requestQueue = VolleySingleton.getsInstance().getmRequestQueue();
        String url = "http://bpbp.ctrgn.net/api/device";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     //   Log.d("Response", response);
                        currentVehicleList = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Vehicle currentVehicle = new Vehicle();
                                currentVehicle.vehicleType = jsonArray.getJSONObject(i).getInt("vehicleType");
                                switch (jsonArray.getJSONObject(i).getInt("vehicleType")) {
                                    case 0:
                                        currentVehicle.vehicleTypeIcon = R.drawable.tram_icon_small;
                                        break;
                                    case 2:
                                        currentVehicle.vehicleTypeIcon = R.drawable.bus_icon_small;
                                        break;
                                    case 3:
                                        currentVehicle.vehicleTypeIcon = R.drawable.bus_icon_small;
                                        break;
                                }
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
                                currentVehicleList.add(currentVehicle);
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
                        Log.d("Error.Response", "error");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("requestContent", "vehiclesPositions");
                params.put("northLon", ((Double) bounds.northeast.longitude).toString());
                params.put("eastLat", ((Double) bounds.northeast.latitude).toString());
                params.put("westLat", ((Double) bounds.southwest.latitude).toString());
                params.put("southLon", ((Double) bounds.southwest.longitude).toString());
                return params;
            }
        };
        requestQueue.add(postRequest);
    }
    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }
}
