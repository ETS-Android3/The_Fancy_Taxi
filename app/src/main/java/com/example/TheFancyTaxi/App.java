package com.example.TheFancyTaxi;

import android.app.Application;

import com.example.TheFancyTaxi.db_sp.GPS;
import com.example.TheFancyTaxi.db_sp.MSPV3;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MSPV3.initHelper(this);
        GPS.initHelper(this);

    }
}
