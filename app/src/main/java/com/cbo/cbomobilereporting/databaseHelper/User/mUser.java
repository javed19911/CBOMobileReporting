package com.cbo.cbomobilereporting.databaseHelper.User;

import android.location.Location;

import com.cbo.cbomobilereporting.databaseHelper.Property;

import java.util.Date;

public class mUser {
    private String Name;
    private String ID;
    private String Desgination;
    private String DesginationID;
    private String CompanyCode;
    private String HQ;
    private String ManagerName;
    private String ManagerId;

    private String DCRId;
    private String DCRDate;
    private String DCRDateDisplay;

    private String IMEI;
    private String OS;
    private String battery;
    private String AppVersion = "20190412";
    private String time;
    private Location location;
    private Boolean LoggedInAsSupport = false;
    //private Boolean showLatLong = false;

    public mUser(String ID, String companyCode) {
        this.ID = ID.trim();
        CompanyCode = companyCode.trim();
    }



    //getter
    public String getName() {
        return Name;
    }

    public String getID() {
        return ID;
    }

    public String getDesgination() {
        return Desgination;
    }

    public String getDesginationID() {
        return DesginationID;
    }

    public String getCompanyCode() {
        return CompanyCode.toUpperCase();
    }

    public String getHQ() {
        return HQ;
    }

    public String getDCRId() {
        return DCRId;
    }

    public String getDCRDate() {
        return DCRDate;
    }

    public String getDCRDateDisplay() {
        return DCRDateDisplay;
    }

    public String getOS() {
        return OS;
    }

    public String getBattery() {
        return battery;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public String getTime() {
        time = ""+new Date().getTime();
        return time;
    }

    public Location getLocation() {
        return location;
    }

    public Boolean getLoggedInAsSupport() {
        return LoggedInAsSupport;
    }

    public String getManagerName() {
        return ManagerName;
    }

    public String getManagerId() {
        return ManagerId;
    }

   /* public Boolean getShowLatLong() {
        return showLatLong;
    }*/

    public String getIMEI() {
        return IMEI;
    }
///setter

    public mUser setName(String name) {
        Name = name;
        return this;
    }


    public mUser setDesgination(String desgination) {
        Desgination = desgination;
        return this;
    }

    public mUser setDesginationID(String desginationID) {
        DesginationID = desginationID;
        return this;
    }


    public mUser setHQ(String HQ) {
        this.HQ = HQ;
        return this;
    }

    public mUser setDCRId(String DCRId) {
        this.DCRId = DCRId;
        return this;
    }

    public mUser setDCRDate(String DCRDate) {
        this.DCRDate = DCRDate;
        return this;
    }

    public mUser setDCRDateDisplay(String DCRDateDisplay) {
        this.DCRDateDisplay = DCRDateDisplay;
        return this;
    }


    public mUser setOS(String OS) {
        this.OS = OS;
        return this;
    }

    public mUser setBattery(String battery) {
        this.battery = battery;
        return this;
    }


    public mUser setLocation(Location location) {
        this.location = location;
        return this;
    }

    public mUser setLoggedInAsSupport(Boolean loggedInAsSupport) {
        LoggedInAsSupport = loggedInAsSupport;
        return this;
    }

    public mUser setManagerName(String managerName) {
        ManagerName = managerName;
        return this;
    }

    public mUser setManagerId(String managerId) {
        ManagerId = managerId;
        return this;
    }

    public mUser setIMEI(String IMEI) {
        this.IMEI = IMEI;
        return this;
    }

    /*public void setShowLatLong(Boolean showLatLong) {
        this.showLatLong = showLatLong;
    }*/
}
