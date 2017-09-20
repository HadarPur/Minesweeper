package com.example.hadar.minesweeper;
import android.util.Log;

import com.example.hadar.minesweeper.quaries.CallData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonData implements Serializable {
    private static final String TAG = "data";
    // private String data;
    private UserInfo user;
    private UserInfo readed;
    //DatabaseReference rootRef;
    DatabaseReference myRefEasy;
    DatabaseReference myRefMedium;
    DatabaseReference myRefHard;
    private ArrayList<UserInfo> easyUsers;
    private ArrayList<UserInfo> normalUsers;
    private ArrayList<UserInfo> hardUsers;

    public JsonData() {

        FirebaseDatabase date = FirebaseDatabase.getInstance();
        myRefEasy = date.getReference("EasyRecords");
        myRefMedium = date.getReference("MediumRecords");
        myRefHard = date.getReference("HardRecords");
        easyUsers = new ArrayList<>();
        normalUsers = new ArrayList<>();
        hardUsers = new ArrayList<>();
    }

    public void writeUserToDataBase(UserInfo user, int level) {

        switch (level) {
            case 0:
                myRefEasy.push().setValue(user);
                break;
            case 1:
                myRefMedium.push().setValue(user);
                break;
            case 2:
                myRefHard.push().setValue(user);
                break;
        }

    }

    public void readResults(final CallData queryCallback,int level) {

        Log.d(TAG, "start reading");
        switch (level) {
            case 0: callingEvent(myRefEasy,queryCallback,level); break;
            case 1: callingEvent(myRefMedium,queryCallback,level); break;
            case 2:callingEvent(myRefHard,queryCallback,level); break;
        }
    }

    private void callingEvent(DatabaseReference myRef, final CallData queryCallback, final int level){

        easyUsers.clear();
        normalUsers.clear();
        hardUsers.clear();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();
                while(itr.hasNext()) {
                    readed = itr.next().getValue(UserInfo.class);
                    switch (level) {
                        case 0: easyUsers.add(readed); break;
                        case 1: normalUsers.add(readed); break;
                        case 2: hardUsers.add(readed); break;
                    }
                }
                switch (level) {
                    case 0: queryCallback.performQuary(easyUsers); break;
                    case 1: queryCallback.performQuary(normalUsers); break;
                    case 2: queryCallback.performQuary(hardUsers); break;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}