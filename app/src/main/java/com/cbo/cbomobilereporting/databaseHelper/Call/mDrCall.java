package com.cbo.cbomobilereporting.databaseHelper.Call;

/**
 * Created by cboios on 23/01/19.
 */

public class mDrCall extends mCall {



    private String DrColour = "#FFFFFF";

    private String DrClass = "";
    private String DrPotential = "";
    private String DrLastVisited = "";
    private String Dr_CRM = "";
    private String DRCAPM_GROUP="";

    private String call_type = "1";   //plan_type=1 for planed, 0 for unplaned

    private String sample_noc_Arr = "";


    private Boolean visual_Arr = false;
    private Boolean Rx_Arr = false;

    public mDrCall() {
        super("Doctor");
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

    public String getSample_noc_Arr() {
        return sample_noc_Arr;
    }

    public Boolean getVisual_Arr() {
        return visual_Arr;
    }

    public Boolean getRx_Arr() {
        return Rx_Arr;
    }

    public String getCall_type() {
        return call_type;
    }

    public String getDRCAPM_GROUP() {
        return DRCAPM_GROUP;
    }


    ///setters

    public mDrCall setDrColour(String drColour) {
        DrColour = drColour;
        return this;
    }

    public mDrCall setDrClass(String drClass) {
        DrClass = drClass;
        return this;
    }

    public mDrCall setDrPotential(String drPotential) {
        DrPotential = drPotential;
        return this;
    }

    public mDrCall setDrLastVisited(String drLastVisited) {
        DrLastVisited = drLastVisited;
        return this;
    }

    public mDrCall setDr_CRM(String dr_CRM) {
        Dr_CRM = dr_CRM;
        return this;
    }

    public mDrCall setSample_noc_Arr(String sample_noc_Arr) {
        this.sample_noc_Arr = sample_noc_Arr;
        return this;
    }

    public mDrCall setVisual_Arr(Boolean visual_Arr) {
        this.visual_Arr = visual_Arr;
        return this;
    }

    public mDrCall setRx_Arr(Boolean rx_Arr) {
        Rx_Arr = rx_Arr;
        return this;
    }

    public mDrCall setCall_type(String call_type) {
        this.call_type = call_type;
        return this;
    }

    public mDrCall setDRCAPM_GROUP(String DRCAPM_GROUP) {
        this.DRCAPM_GROUP = DRCAPM_GROUP;
        return this;
    }

}
