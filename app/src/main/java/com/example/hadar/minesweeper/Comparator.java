package com.example.hadar.minesweeper;


public class Comparator implements java.util.Comparator<UserInfo>{

    @Override
    public int compare(UserInfo o1, UserInfo o2) {
        if (o1.getPoints()>o2.getPoints())
            return 1;
        return -1;
    }
}
