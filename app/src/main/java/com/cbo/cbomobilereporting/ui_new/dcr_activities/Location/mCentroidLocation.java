package com.cbo.cbomobilereporting.ui_new.dcr_activities.Location;

import android.location.Location;

import com.cbo.cbomobilereporting.ui_new.Model.mAddress;

import java.io.Serializable;

public class mCentroidLocation implements Serializable {
    private Location geoLocation = null;
    private Double Km = 0D;
    private mAddress address = null;

    public mCentroidLocation(Location geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getLatLong(){
        return getGeoLocation().getLatitude() + "," +getGeoLocation().getLongitude();
    }
    public Location getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(Location geoLocation) {
        this.geoLocation = geoLocation;
    }

    public Double getKm() {
        return Km;
    }

    public void setKm(Double km) {
        Km = km;
    }

    public mAddress getAddress() {
        return address;
    }

    public void setAddress(mAddress address) {
        this.address = address;
    }
}
