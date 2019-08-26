package com.cbo.cbomobilereporting.ui_new.dcr_activities.Recipt;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class mParty implements Serializable {

private String ID;
private String Name;


    public mParty(String ID, String name) {
        this.ID = ID;
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public mParty setID(String ID) {
        this.ID = ID;
        return  this;
    }

    public String getName() {
        return Name;
    }

    public mParty setName(String NAme) {
        this.Name = NAme;
        return  this;
    }

    protected mParty(Parcel in) {
        this.ID = in.readString();
        this.Name = in.readString();
    }


}
