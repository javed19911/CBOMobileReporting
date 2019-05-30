package com.cbo.cbomobilereporting.databaseHelper.Location;

import android.location.Location;

import com.cbo.cbomobilereporting.databaseHelper.Call.mCall;

import java.util.Date;

public class mLocation {

    private Location location;
    private mCall call = null;
    private String Address="";
    private String timestamp;
    private String DCR_ID;

    public mLocation(Location location) {
        this.location = location;
    }


    //getter
    public Location getLocation() {
        return location;
    }

    public mCall getCall() {
        return call;
    }

    public String getAddress() {
        return Address;
    }

    public String getTime() {
        timestamp = ""+new Date().getTime();
        return timestamp;
    }



    public String getDCR_ID() {
        return DCR_ID;
    }


    //setter

    public mLocation setLocation(Location location) {
        this.location = location;
        return this;
    }

    public mLocation setCall(mCall call) {
        this.call = call;
        return this;
    }

    public mLocation setAddress(String address) {
        Address = address;
        return this;
    }

    public mLocation setDCR_ID(String DCR_ID) {
        this.DCR_ID = DCR_ID;
        return this;
    }
}
