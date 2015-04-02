package com.example.martinhudec.kwigBA.serverConnection;

import android.app.Application;
import android.content.Context;

/**
 * Created by martinhudec on 29/03/15.
 */
public class ConnectionApplication extends Application {
    private static ConnectionApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
    public static ConnectionApplication getInstance(){
        return sInstance;
    }
    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
