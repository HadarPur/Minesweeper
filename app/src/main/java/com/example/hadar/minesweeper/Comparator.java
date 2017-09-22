package com.example.hadar.minesweeper;

public class Comparator implements java.util.Comparator<UserInfo>{

    //comparator for the records list
    @Override
    public int compare(UserInfo o1, UserInfo o2) {
        return o1.getPoints()-o2.getPoints();
    }
}
