package com.example.ahmed.electivesubjectselection;

import java.util.Date;

/**
 * Created by Ahmed on 3/17/2018.
 */

public class UserOptionModel {
    String pref1 = "";
    String pref2 = "";
    String pref3 = "";
    String branch;
    String date = "";
    String rollNumber = "";
    String time = "";
    String url = "";
    String user = "";
    String password = "";

    public UserOptionModel(String pref1Sub, String pref2Sub, String pref3Sub, String branch,String rollNumber) {
        this.pref1 = pref1Sub;
        this.pref2 = pref2Sub;
        this.pref3 = pref3Sub;
        this.branch = branch;
        this.rollNumber=rollNumber;
        this.date = new Date().toLocaleString();
        this.time = String.valueOf(new Date().getTime());
        url = MyConstants.url;
        password = MyConstants.password;
        user = MyConstants.user;
    }
}
