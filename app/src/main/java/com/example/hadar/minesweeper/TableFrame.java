package com.example.hadar.minesweeper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TableFrame extends Fragment {
    private ListView recordsList;
    private ArrayList<String> users;
    private  ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        users = new ArrayList<>();
        for(int i=0;i<10;i++) {
            users.add("keren"+i);
        }
        recordsList=new ListView(getContext());
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1 , users);
        recordsList.setAdapter(adapter);
        recordsList.setBackgroundColor(Color.WHITE);
        return recordsList;
    }

    public void addRecord(UserInfo user){


    }

}
