package com.example.hadar.minesweeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

public class RecordTableActivity extends AppCompatActivity  {
    private static final String TAG =RecordTableActivity.class.getSimpleName();
    private static final int EASY=0, NORMAL=1, HARD=2;
    private SupportMapFragment mapFragment;
    private TableFrame tableFragment;
    private double latitude, longitude;
    private GPSTracker gpsTracker;
    private boolean firstAsk=false;
    private Map map;
    private ArrayList<UserInfo> arrayList=new ArrayList<>();
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
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = new Map(mapFragment,latitude,longitude);
        tableFragment= (TableFrame) getSupportFragmentManager().findFragmentById(R.id.table);
        ListView list=tableFragment.getList();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                map.setMarkersOnMap(tableFragment.getArray().get(position));
            }
        });
        setButtons();
    }

    public void setButtons() {
        easyButton= (Button) findViewById(R.id.easy);
        mediumButton= (Button) findViewById(R.id.Normal);
        hardButton= (Button) findViewById(R.id.hard);

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage();
                easyButton.setBackgroundResource(R.drawable.tableopen);
                mediumButton.setBackgroundResource(R.drawable.table);
                hardButton.setBackgroundResource(R.drawable.table);
                tableFragment.setList(EASY);
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

    private void showMessage(){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, "click your location to see results", duration);
        toast.show();
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


