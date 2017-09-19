package com.example.hadar.minesweeper;
import android.util.Log;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.Serializable;
import java.util.ArrayList;

public class JsonData implements Serializable {
    private static final String TAG ="data";
    // private String data;
    private UserInfo user;
    //DatabaseReference rootRef;
    DatabaseReference myRefEasy;
    DatabaseReference myRefMedium;
    DatabaseReference myRefHard;

    ArrayList<UserInfo> usersData;

    public JsonData() {

        FirebaseDatabase date=FirebaseDatabase.getInstance();
        myRefEasy=date.getReference("EasyRecords");
        myRefMedium=date.getReference("MediumRecords");
        myRefHard=date.getReference("HardRecords");
    }

    public void writeUserToDataBase(UserInfo user,int level) {

        switch (level){
            case 0: myRefEasy.push().setValue(user); break;
            case 1: myRefMedium.push().setValue(user); break;
            case 2: myRefHard.push().setValue(user); break;
        }

    }

    public void readFromDataBase( DatabaseReference myRef) {

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
