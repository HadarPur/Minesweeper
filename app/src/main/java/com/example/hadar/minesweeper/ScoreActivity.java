package com.example.hadar.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    private static final String TAG =ScoreActivity.class.getSimpleName();
    public final int WIN=1;
    private TextView nametv;
    private EditText info;
    private double latitude;
    private double longitude;
    private int points, level, isMute, index;
    private String name;
    private Button save, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        nametv = (TextView) findViewById(R.id.enterName);
        info = (EditText) findViewById(R.id.recordName);
        save = (Button) findViewById(R.id.sendName);
        cancel = (Button) findViewById(R.id.cancel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getResult();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getResult() {
        Intent intent=getIntent();
        Bundle ex=intent.getExtras();
        index=ex.getInt("Index");
        points=ex.getInt("Points");
        level=ex.getInt("Difficulty");
        isMute=ex.getInt("Volume");
        latitude=ex.getDouble("locationlat");
        longitude=ex.getDouble("locationlong");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=info.getText().toString();
                Log.d(TAG,"index : "+index);
                UserInfo user = new UserInfo(index,name,latitude,longitude,points,level);
                JsonData firebaseData = new JsonData();
                if (index>3)
                    firebaseData.replaceUserInDataBase(user, level);
                else
                    firebaseData.writeUserToDataBase(user,level);
                nextActivity();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity();
            }
        });
    }

    public void nextActivity() {
        Intent intent=new Intent( ScoreActivity.this,ResultActivity.class);
        intent.putExtra("Result", WIN);
        intent.putExtra("Points", points);
        intent.putExtra("Difficulty", level);
        intent.putExtra("Volume", isMute);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
