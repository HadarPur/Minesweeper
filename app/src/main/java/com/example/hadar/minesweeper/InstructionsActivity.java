package com.example.hadar.minesweeper;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class InstructionsActivity extends AppCompatActivity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        findView();
    }

    public void findView() {
        tv=(TextView) findViewById(R.id.instruction);
        tv.setBackgroundColor(Color.argb(200, 165,205,253));
        tv.setText("\n"+
                "To play, you must allow the app to access your device's location.\n"+
                "To see the records table, you must allow the app to access your device's network.\n" +
                "\n" +
                "There are 3 levels: Easy, Normal and Hard\n" +
                "Easy - 5 mines, board 10X10. \n" +
                "Normal - 10 mines, board 10X10.\n" +
                "Hard - 10 mines, board 5X5.\n" +
                "\n" +
                "Instructions for the game:\n" +
                "1. To put a flag press and hold the cell.\n" +
                "2. To open a cell press the cell.\n" +
                "3. To start a new game click on the tree button.\n" +
                "\n" +
                "While you are playing, you can tilt your device in order to increase the amount of mines.\n" +
                "In order to stop increasing the amount of mines, bring your device to its initial location.\n" +
                "\n" +
                "You also can see the table records for each level from the home page or from the result.\n" +
                "\n" +
                "In the records you will see a map with your specific location.\n" +
                "\n" +
                "To see the list of the record you need to click on your location on the map, and after that you will see the record list.\n" +
                "\n" +
                "If you want to see where the record was broken you need to click on the specific record and the map take you to the specific location.\n" +
                "\n" +
                "Of course to see where it was, you need to click on location marker.\n" +
                "\n");
        tv.setMovementMethod(new ScrollingMovementMethod());
    }
}