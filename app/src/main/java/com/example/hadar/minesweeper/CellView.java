package com.example.hadar.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CellView extends LinearLayout {
    public TextView txt;

    public CellView(Context context) {
        super(context);
        this.setOrientation(VERTICAL);

        txt = new TextView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        txt.setLayoutParams(layoutParams);
        txt.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        txt.setGravity(Gravity.CENTER);
        txt.setTextSize(25);
        txt.setTextColor(Color.BLACK);
        setBackgroundResource(R.drawable.table);

        this.addView(txt);

    }
}
