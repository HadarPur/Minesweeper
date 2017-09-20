package com.example.hadar.minesweeper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hadar.minesweeper.quaries.CallData;
import java.util.ArrayList;

public class TableFrame extends Fragment implements CallData {
    private static final String TAG ="table";
    private ListView recordsList;
    private ArrayList<UserInfo> usersData;
    private ArrayList<String> users;
    private ArrayAdapter<String> adapter;
    private JsonData data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        usersData = new ArrayList<>();
        users = new ArrayList<>();
        data = new JsonData();

        data.readResults(this,0);
        recordsList=new ListView(getContext());
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,users);
        recordsList.setAdapter(adapter);
        recordsList.setBackgroundColor(Color.argb(200, 165,205,253));

        return recordsList;
    }

    @Override
    public void performQuery(ArrayList<UserInfo> usersInfo) {
        usersData.clear();
        usersData.addAll(usersInfo);
        for(int i=0; i<usersData.size();i++) {
            users.add(usersData.get(i).toString());
        }
        Log.d(TAG,"data arrived");
    }

    public void setList(int Level){
        usersData.clear();
        users.clear();
        adapter.notifyDataSetChanged();
        data.readResults(this,Level);
        adapter.notifyDataSetChanged();
    }

    public ListView getList() {
        return recordsList;
    }

    public ArrayList<UserInfo> getArray() {
        return usersData;
    }

}
