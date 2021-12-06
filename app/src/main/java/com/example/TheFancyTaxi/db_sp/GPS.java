package com.example.TheFancyTaxi.db_sp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.example.TheFancyTaxi.callback_interfaces.CallBack_Map;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class GPS {
    private static GPS me;
    Context context;
    private final FusedLocationProviderClient fusedLocationClient;

    public static GPS getMe() {
        return me;
    }

    private GPS(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static void initHelper(Context context) {
        if (me == null) {
            me = new GPS(context);
        }
    }

    private boolean checkPermission(){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    public void getLocation(CallBack_Map cb){
        if (checkPermission()) {
            fusedLocationClient.getLastLocation().addOnCompleteListener( task->{
                Location location = task.getResult();
                if(location!=null){
                    cb.showLocation(location.getLatitude(), location.getLongitude());
                }
                else{
                    cb.showLocation(0.0D, 0.0D);
                }
            });
        }
        else {
            cb.showLocation(0.0D, 0.0D);
        }
    }
}
