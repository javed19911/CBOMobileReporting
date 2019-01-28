package com.cbo.cbomobilereporting.databaseHelper.User;

import android.app.Activity;

import com.cbo.cbomobilereporting.databaseHelper.FirebaseDbhelper;


public class UserDB  extends FirebaseDbhelper<mUser> {
    public UserDB() {
        super();
        setPrimaryKeyAutoGenrate (false);
        setTable ("User");
    }



}
