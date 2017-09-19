package com.example.hadar.minesweeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class RecordTableActivity extends AppCompatActivity  {
    private static final String TAG =RecordTableActivity.class.getSimpleName();
    private SupportMapFragment mapFragment;
    private TableFrame tableFragment;
    private LatLng sydney;
    private double latitude, longitude;
    private boolean firstAsk=false;
    private GPSTracker gpsTracker;
    private JsonData data;
    private Bundle ex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_table);
        JsonData firebaseData = new JsonData();

        gpsTracker = new GPSTracker(this, firstAsk);
        if(gpsTracker.getGPSEnable()&& gpsTracker.getPosition()!=null){
            latitude=gpsTracker.getPosition().getLatitude();
            longitude=gpsTracker.getPosition().getLongitude();
            gpsTracker.initLocation();
        }
        else {
            showSettingsAlert();
            latitude=0;
            longitude=0;
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //gpsTracker = new GPSTracker(getApplicationContext());
        Map map = new Map(mapFragment,latitude,longitude);
        tableFragment= (TableFrame) getSupportFragmentManager().findFragmentById(R.id.table);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Location is not available");
        alertDialog.setMessage("You must permit location to see the records");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        alertDialog.show();
    }
}
