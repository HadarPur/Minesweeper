package com.example.hadar.minesweeper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class GPSTracker extends Service implements LocationListener {
    private final Activity activity;
    boolean isGPSEnabled =false;
    boolean isNetworkEnabled =false;
    private Location location;
    protected LocationManager locationManager;

    public GPSTracker(Activity activity){
        this.activity = activity;
        initLocation();
    }

    //Create a GetLocation Method //
    public  void initLocation() {
        try {

            locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
            isNetworkEnabled=locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // for new versions
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    requestLocation();
                } else {
                    ////

                    boolean alreadyAsked = getCounter() > 0;
                    if (!alreadyAsked) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 6756);
                    }
                }
            } else {
                // for older versions
                requestLocation();
            }

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private int getCounter() {
        return 0;
    }

    /**
     * Already permitted
     */
    @SuppressWarnings("MissingPermission")
    private void requestLocation() {
        if (isGPSEnabled) {
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }
        // if lcoation is not found from GPS than it will found from network //
        if (location == null) {
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

            }
        }

    }

    public boolean getGPSEnable(){

        return isGPSEnabled;
    }

    public Location getPosition(){
        return location;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}
}
