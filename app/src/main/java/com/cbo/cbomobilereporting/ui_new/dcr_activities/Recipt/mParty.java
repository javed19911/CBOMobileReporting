package com.cbo.cbomobilereporting.ui_new.dcr_activities.Recipt;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class mParty implements Serializable {

private String ID;
private String NAme;



    public mParty() {
    }

    public String getID() {
        return ID;
    }

    public mParty setID(String ID) {
        this.ID = ID;
        return  this;
    }

    public String getNAme() {
        return NAme;
    }

    public mParty setNAme(String NAme) {
        this.NAme = NAme;
        return  this;
    }

    protected mParty(Parcel in) {
        this.ID = in.readString();
        this.NAme = in.readString();
    }


}
