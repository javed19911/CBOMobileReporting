package com.cbo.cbomobilereporting.databaseHelper.Location;

import android.location.Location;

import com.cbo.cbomobilereporting.databaseHelper.FirebaseDbhelper;


public class LocationDB extends FirebaseDbhelper<Location> {



    public LocationDB() {
        super();
        setTable ("Location");
    }

    public void setBaseURL(String baseURL) {
        super.setBaseURL (baseURL);
        setTable ("Location");
    }

}
