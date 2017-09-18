package com.example.hadar.minesweeper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TableFrame extends Fragment {
    private TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tv=new TextView(getContext());
        return tv;
    }

    public void SetText(String txt){

        tv.setText(txt);
    }
}
