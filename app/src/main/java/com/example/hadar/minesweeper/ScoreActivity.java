package com.example.hadar.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {
    public final int WIN=1;
    public final int MAX_RECORDS=10;
    private TextView nametv;
    private EditText info;
    private double latitude;
    private double longitude;
    private int points, level, isMute, index;
    private String name;
    private Button save, cancel;
    private ArrayList<UserInfo> myList;

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

    //get the result
    public void getResult() {
        Intent intent=getIntent();
        if (intent!=null) {
            Bundle ex = intent.getExtras();
            myList = (ArrayList<UserInfo>) getIntent().getSerializableExtra("list");
            points = ex.getInt("Points");
            level = ex.getInt("Difficulty");
            isMute = ex.getInt("Volume");
            latitude = ex.getDouble("locationlat");
            longitude = ex.getDouble("locationlong");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=info.getText().toString();
                index=myList.size();
                UserInfo user = new UserInfo(index,name,latitude,longitude,points,level);
                JsonData firebaseData = new JsonData();
                if (index>=MAX_RECORDS) {
                    firebaseData.replaceUserInDataBase(user, level, myList);
                }
                else {
                    firebaseData.writeUserToDataBase(user, level);
                }
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

    //go to result activity
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
