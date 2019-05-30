package com.cbo.cbomobilereporting.databaseHelper.Call;

/**
 * Created by cboios on 23/01/19.
 */

public class mDrRCCall extends mCall {


    private String DrColour = "#FFFFFF";

    private String DrClass = "";
    private String DrPotential = "";
    private String DrLastVisited = "";
    private String Dr_CRM = "";
    private String DRCAPM_GROUP="";

    public mDrRCCall() {
        super("DoctorRc");
    }


    ///getters
    public String getDrColour() {
        return DrColour;
    }

    public String getDrClass() {
        return DrClass;
    }

    public String getDrPotential() {
        return DrPotential;
    }

    public String getDrLastVisited() {
        return DrLastVisited;
    }

    public String getDr_CRM() {
        return Dr_CRM;
    }

    public String getDRCAPM_GROUP() {
        return DRCAPM_GROUP;
    }

    ///setters

    public mDrRCCall setDrColour(String drColour) {
        DrColour = drColour;
        return this;
    }

    public mDrRCCall setDrClass(String drClass) {
        DrClass = drClass;
        return this;
    }

    public mDrRCCall setDrPotential(String drPotential) {
        DrPotential = drPotential;
        return this;
    }

    public mDrRCCall setDrLastVisited(String drLastVisited) {
        DrLastVisited = drLastVisited;
        return this;
    }

    public mDrRCCall setDr_CRM(String dr_CRM) {
        Dr_CRM = dr_CRM;
        return this;
    }
    public mDrRCCall setDRCAPM_GROUP(String DRCAPM_GROUP) {
        this.DRCAPM_GROUP = DRCAPM_GROUP;
        return this;
    }
}
