package com.example.hadar.minesweeper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.maps.SupportMapFragment;
import java.io.IOException;

public class RecordTableActivity extends AppCompatActivity  {
    private static final String TAG =RecordTableActivity.class.getSimpleName();
    private static final int EASY=0, NORMAL=1, HARD=2;
    private boolean firstAsk=false;
    private double latitude, longitude;
    private SupportMapFragment mapFragment;
    private TableFrame tableFragment;
    private GPSTracker gpsTracker;
    private Map map;
    private Button easyButton,mediumButton,hardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_table);
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

        if (!isNetworkAvailable(this))
            showConnectionInternetFailed();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = new Map(mapFragment,latitude,longitude, this);
        tableFragment= (TableFrame) getSupportFragmentManager().findFragmentById(R.id.table);
        ListView list=tableFragment.getList();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    map.setMarkersOnMap(tableFragment.getArray().get(position));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        setButtons();
    }

    //set on click buttons
    public void setButtons() {
        easyButton = (Button) findViewById(R.id.easy);
        mediumButton = (Button) findViewById(R.id.Normal);
        hardButton = (Button) findViewById(R.id.hard);

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage();
                easyButton.setBackgroundResource(R.drawable.tableopen);
                mediumButton.setBackgroundResource(R.drawable.table);
                hardButton.setBackgroundResource(R.drawable.table);
                tableFragment.setList(EASY);
                map.moveCameraToCurrentPos();

            }
        });

        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage();
                easyButton.setBackgroundResource(R.drawable.table);
                mediumButton.setBackgroundResource(R.drawable.tableopen);
                hardButton.setBackgroundResource(R.drawable.table);
                tableFragment.setList(NORMAL);
                map.moveCameraToCurrentPos();
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage();
                easyButton.setBackgroundResource(R.drawable.table);
                mediumButton.setBackgroundResource(R.drawable.table);
                hardButton.setBackgroundResource(R.drawable.tableopen);
                tableFragment.setList(HARD);
                map.moveCameraToCurrentPos();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        showMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //massage to see the record list
    private void showMessage(){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, "Click your location to see results", duration);
        toast.show();
    }

    //location unavailable massage
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

    //network unavailable massage
    public void showConnectionInternetFailed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Network Connection Failed");
        alertDialog.setMessage("Network is unavailable." +
                "\n"+
                "If you want to see record table you need a connection to the network");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }


    //checking network connection
    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null
                && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null
                && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        }
        else {
            return false;
        }
    }
}


