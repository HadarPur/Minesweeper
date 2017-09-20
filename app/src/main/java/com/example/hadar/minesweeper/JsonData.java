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
    private static final int EASY=0, NORMAL=1, HARD=2;
    private static final String TAG = "data";
    private UserInfo readed;
    private ArrayList<UserInfo> easyUsers;
    private ArrayList<UserInfo> normalUsers;
    private ArrayList<UserInfo> hardUsers;
    private DatabaseReference myRefEasy;
    private DatabaseReference myRefMedium;
    private DatabaseReference myRefHard;

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
            case EASY:
                myRefEasy.push().setValue(user);
                break;
            case NORMAL:
                myRefMedium.push().setValue(user);
                break;
            case HARD:
                myRefHard.push().setValue(user);
                break;
        }
    }

    public void readResults(final CallData queryCallback,int level) {
        Log.d(TAG, "start reading");
        switch (level) {
            case EASY:
                callingEvent(myRefEasy,queryCallback,level);
                break;
            case NORMAL:
                callingEvent(myRefMedium,queryCallback,level);
                break;
            case HARD:
                callingEvent(myRefHard,queryCallback,level);
                break;
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
                        case EASY:
                            easyUsers.add(readed);
                            break;
                        case NORMAL:
                            normalUsers.add(readed);
                            break;
                        case HARD:
                            hardUsers.add(readed);
                            break;
                    }
                }
                switch (level) {
                    case EASY:
                        queryCallback.performQuery(easyUsers);
                        break;
                    case NORMAL:
                        queryCallback.performQuery(normalUsers);
                        break;
                    case HARD:
                        queryCallback.performQuery(hardUsers);
                        break;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}