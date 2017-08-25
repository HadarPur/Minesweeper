package com.example.hadar.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView tv;
    private Button newGame, homePage;
    private int result, points, level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        findView();
        getTheResuult();
    }

    //set a view for the buttons
    public void findView(){
        homePage=(Button)findViewById(R.id.homepage);
        newGame=(Button) findViewById(R.id.newgame);

        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResultActivity.this, GameActivity.class);
                intent.putExtra("Difficulty", level);
                startActivity(intent);
            }
        });
    }

    //get the result from the GameActivity
    public void getTheResuult() {
        imageView=(ImageView) findViewById(R.id.res);
        tv = (TextView) findViewById(R.id.points);
        Intent intent=getIntent();
        Bundle ex=intent.getExtras();
        result=ex.getInt("Result");
        points=ex.getInt("Points");
        level=ex.getInt("Difficulty");

        if (result==0)
            lose();
        else
            win();

    }

    //img view for the loser
    public void lose() {
        imageView.setBackgroundResource(R.drawable.lose);
    }

    //img view for the winner with time winning
    public void win() {
        imageView.setBackgroundResource(R.drawable.winning);
        tv.setBackgroundResource(R.drawable.table);
        tv.setText("TIME: " +points+ " SEC");
        tv.bringToFront();
    }
}
