package com.example.hadar.minesweeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int diff, isMute=1;
    private boolean firstAsk=true;
    private Intent intent;
    private MediaPlayer open_song;
    private GPSTracker gpsTracker ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpsTracker = new GPSTracker(this, firstAsk);
        findView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!gpsTracker.getGPSEnable())
            showSettingsAlert();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMute==1)
            startSong();
        else
            stopSong();
    }

    @Override
    protected void onPause() {
        super.onPause();
        open_song.stop();
    }

    public void findView(){
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordTableActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        findViewById(R.id.mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMute == 1)
                    stopSong();
                else if (isMute == 0)
                    startSong();
                isMute ^= 1;

            }
        });
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, InstructionsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    public void stopSong(){
        open_song.stop();
        findViewById(R.id.mute).setBackgroundResource(R.drawable.tableopen);
    }
    public void startSong(){
        open_song=MediaPlayer.create(this, R.raw.opensong);
        open_song.start();
        findViewById(R.id.mute).setBackgroundResource(R.drawable.table);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button:
                diff=0;
                break;
            case R.id.button2:
                diff=1;
                break;
            case R.id.button3:
                diff=2;
                break;
        }
        intent = new Intent(this, GameActivity.class);
        intent.putExtra("Difficulty", diff);
        intent.putExtra("Volume", isMute);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

   public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
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
