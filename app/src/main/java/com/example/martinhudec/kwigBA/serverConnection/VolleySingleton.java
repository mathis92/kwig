package com.example.martinhudec.kwigBA.serverConnection;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by martinhudec on 29/03/15.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(ConnectionApplication.getAppContext());
    }

    public static VolleySingleton getsInstance() {
        if (sInstance == null) {
            sInstance = new VolleySingleton();
        }
        return sInstance;
    }

    public RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }
}
