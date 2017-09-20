package com.example.hadar.minesweeper;

import android.util.Log;

public class UserInfo {
    private static final String TAG ="user";
    private String name;
    private double latitude;
    private double longitude;
    private int points;
    private String level;

    public UserInfo(){

    }

    public UserInfo(String name,double latitude,double longitude,int points,int level){
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.points=points;

        switch (level){
            case 0:
                this.level="Easy";
                break;
            case 1:
                this.level="medium";
                break;
            case 2:
                this.level="hard";
                break;
        }
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getPoints() {
        return points;
    }

    public String getLevel() {
        return this.level;
    }

    public String toString(){
        String objectString=" ";
        objectString += "Player Name: " +this.name+ "  -------->  Score: "+this.points+" ";
        Log.d(TAG,"string = "+ objectString);
        return objectString;
    }

}
