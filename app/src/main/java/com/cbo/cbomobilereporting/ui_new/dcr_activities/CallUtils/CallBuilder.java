package com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils;

import java.io.Serializable;

/**
 * Created by cboios on 11/03/19.
 */

public class CallBuilder implements Serializable{
    public enum CallType{
        Doctor("D"),
        DoctorRiminder("Dr"),
        Chemist("C"),
        ChemistRiminder("Cr"),
        Stockist("S"),
        Dairy("DA"),
        Poultry("P");

        private String value;
        CallType(String value){
            this.value = value;
        }
        public String getValue(){
            return value;
        }
    }



    private CallType type;
    private Boolean showDistance = true;
    private String title = "Select Party....";

    //getter


    public CallType getType() {
        return type;
    }

    public Boolean getShowDistance() {
        return showDistance;
    }

    public String getTitle() {
        return title;
    }

    //setter


    public CallBuilder setType(CallType type) {
        this.type = type;
        return this;
    }

    public CallBuilder setShowDistance(Boolean showDistance) {
        this.showDistance = showDistance;
        return this;
    }
    public CallBuilder settitle(String title) {
        this.title = title;
        return this;
    }
}
