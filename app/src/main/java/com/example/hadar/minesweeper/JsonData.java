package com.example.hadar.minesweeper;

import com.example.hadar.minesweeper.quaries.CallData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class JsonData implements Serializable {
    private static final int EASY=0, NORMAL=1, HARD=2;
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

    //write into fire base
    public void writeUserToDataBase(UserInfo user, int level) {
        switch (level) {
            case EASY:
                myRefEasy.child(" "+user.getKey()).setValue(user);
                break;
            case NORMAL:
                myRefMedium.child(" "+user.getKey()).setValue(user);
                break;
            case HARD:
                myRefHard.child(" "+user.getKey()).setValue(user);
                break;
        }
    }

    //replace user in fire base
    public void replaceUserInDataBase(UserInfo user, int level,ArrayList<UserInfo> users) {
        int key = users.get(users.size()-1).getKey();
        String keyStr = " "+key;
        user.setKey(key);
        switch (level) {
            case EASY:
                myRefEasy.child(keyStr).setValue(user);
                break;
            case NORMAL:
                myRefMedium.child(keyStr).setValue(user);
                break;
            case HARD:
                myRefHard.child(keyStr).setValue(user);
                break;
        }
    }

    //read from fire base
    public void readResults(final CallData queryCallback,int level) {
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

    //fire base event
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
                        Collections.sort(easyUsers, new Comparator());
                        queryCallback.performQuery(easyUsers);
                        break;
                    case NORMAL:
                        Collections.sort(normalUsers, new Comparator());
                        queryCallback.performQuery(normalUsers);
                        break;
                    case HARD:
                        Collections.sort(hardUsers, new Comparator());
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
