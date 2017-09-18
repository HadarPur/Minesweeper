package com.example.hadar.minesweeper;

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
    private GPSTracker gpsTracker;
    private JsonData data;
    private Bundle ex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_table);
        JsonData firebaseData = new JsonData();

        gpsTracker = new GPSTracker(this);
        if(gpsTracker.getGPSEnable() && gpsTracker.getPosition()!=null){
            latitude=gpsTracker.getPosition().getLatitude();
            longitude=gpsTracker.getPosition().getLongitude();
            gpsTracker.initLocation();
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //gpsTracker = new GPSTracker(getApplicationContext());
        Map map = new Map(mapFragment,latitude,longitude);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  data = new JsonData();
        tableFragment= (TableFrame) getSupportFragmentManager().findFragmentById(R.id.table);
        tableFragment.SetText("someyhing");
        //  Log.d(TAG,"value is: "+ data.getData());
    }

    /*public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        alertDialog.show();
    }*/
}
