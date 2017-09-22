package com.example.hadar.minesweeper;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private static final int EASY=0, NORMAL=1, HARD=2;
    private int key;
    private String name;
    private double latitude;
    private double longitude;
    private int points;
    private String level;

    public UserInfo(){
    }

    public UserInfo(int key,String name,double latitude,double longitude,int points,int level){
        this.key=key;
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.points=points;

        switch (level){
            case EASY:
                this.level="Easy";
                break;
            case NORMAL:
                this.level="medium";
                break;
            case HARD:
                this.level="hard";
                break;
        }
    }

    public int getKey() {
        return key;
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

    public void setKey(int key){ this.key=key; }

    public String toString(){
        String objectString=" ";
        objectString += " Name: " +this.name+ "  , Time: "+this.points+" sec";
        return objectString;
    }

}
