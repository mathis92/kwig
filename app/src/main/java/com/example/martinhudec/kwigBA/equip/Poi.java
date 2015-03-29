package com.example.martinhudec.kwigBA.equip;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by martinhudec on 07/12/14.
 */
public class Poi {
    LatLng ll;
    String name;

    public Poi(JSONObject poi) throws JSONException {
        ll = new LatLng(Double.parseDouble(poi.getString("latitude")), Double.parseDouble(poi.getString("longitude")));
        System.out.println(ll.toString() + " position");
        name = poi.getString("name") + "\n" + ll.toString();
    }

    public LatLng getLl() {
        return ll;
    }

    public String getName() {
        return name;
    }
}

