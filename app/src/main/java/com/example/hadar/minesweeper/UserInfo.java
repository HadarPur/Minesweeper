package com.example.hadar.minesweeper;

public class UserInfo {

    private String name;
    private double latitude;
    private double longitude;
    private int points;

    public UserInfo(String name ,double latitude,double longitude ,int points){
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.points=points;
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
}
