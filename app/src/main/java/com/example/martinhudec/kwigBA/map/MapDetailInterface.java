package com.example.martinhudec.kwigBA.map;

import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by martinhudec on 10/04/15.
 */
public interface MapDetailInterface {

    LatLngBounds getLatLngFromMap();
    Float getMapZoom();

}
