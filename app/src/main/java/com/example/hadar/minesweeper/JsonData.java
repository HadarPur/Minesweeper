package com.example.hadar.minesweeper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonData implements Serializable {
    private static final String TAG ="data";
    // private String data;
    private UserInfo user;
    DatabaseReference rootRef;
    DatabaseReference myRef;
    DatabaseReference myRefChild;

    ArrayList<HashMap<String, String>> contactList;

    public JsonData() {

        FirebaseDatabase date=FirebaseDatabase.getInstance();
        rootRef=date.getReference("records");
    }

    public void writeUserToDataBase(UserInfo user) {


        Log.d(TAG,"adding values");
        myRef=rootRef.push().child("record");
        myRef.setValue("record");
        myRefChild=myRef.child("name");
        myRefChild.setValue(user.getName());

        myRefChild=myRef.child("latitude");
        myRefChild.setValue(user.getLatitude());

        myRefChild=myRef.child("longitude");
        myRefChild.setValue(user.getLongitude());

        myRefChild=myRef.child("points");
        myRefChild.setValue(user.getPoints());

        Log.d(TAG,"values added");

        //myRef.setValue(myRefChild);

    }

    public void readFromDataBase( DatabaseReference myRef) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String value=dataSnapshot.getValue(String.class);
                // data = value;
                // Log.d(TAG, "value is: "+ value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

}
