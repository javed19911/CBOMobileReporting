package com.cbo.cbomobilereporting.ui_new.Model;

import java.io.Serializable;

/**
 * Created by cboios on 02/03/19.
 */

public class mAddress implements Serializable {
    private String LANE1 ="";
    private String LANE2="";
    private String CITY="";
    private String ZIP="";
    private String STATE="";
    private String DISTRICT="";
    private String COUNTRY="India";
    private String LAT_LONG= "0.0,0.0";
    private String FORMATED_ADDRESS ="";

    //getter

    public String getLANE1() {
        return LANE1;
    }

    public String getLANE2() {
        return LANE2;
    }

    public String getCITY() {
        return CITY;
    }

    public String getZIP() {
        return ZIP;
    }

    public String getLAT_LONG() {
        return LAT_LONG;
    }

    public String getSTATE() {
        return STATE;
    }

    public String getCOUNTRY() {
        return COUNTRY;
    }

    public String getDISTRICT() {
        return DISTRICT;
    }

    public String getFORMATED_ADDRESS() {
        return FORMATED_ADDRESS;
    }

    // setter

    public mAddress setLANE1(String LANE1) {
        this.LANE1 = LANE1;
        return this;
    }

    public mAddress setLANE2(String LANE2) {
        this.LANE2 = LANE2;
        return this;
    }

    public mAddress setCITY(String CITY) {
        this.CITY = CITY;
        return this;
    }

    public mAddress setZIP(String ZIP) {
        this.ZIP = ZIP;
        return this;
    }

    public mAddress setLAT_LONG(String LAT_LONG) {
        this.LAT_LONG = LAT_LONG;
        return this;
    }

    public mAddress setSTATE(String STATE) {
        this.STATE = STATE;
        return this;
    }

    public mAddress setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
        return this;
    }

    public mAddress setDISTRICT(String DISTRICT) {
        this.DISTRICT = DISTRICT;
        return this;
    }

    public mAddress setFORMATED_ADDRESS(String FORMATED_ADDRESS) {
        this.FORMATED_ADDRESS = FORMATED_ADDRESS;
        return this;
    }
}
