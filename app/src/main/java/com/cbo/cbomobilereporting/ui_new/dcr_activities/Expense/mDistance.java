package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import java.io.Serializable;

public class mDistance implements Serializable {
    private String Id ="";
    private String Name = "--Select--";
    private Double Km = 0D;
    private String MANUAL_TAYN ="";
    private String MANUAL_TAYN_MANDATORY ="";


    //getter


    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public Double getKm() {
        return Km;
    }

    public String getMANUAL_TAYN() {
        return MANUAL_TAYN;
    }

    public String getMANUAL_TAYN_MANDATORY() {
        return MANUAL_TAYN_MANDATORY;
    }

    //setter

    public mDistance setId(String id) {
        Id = id;
        return this;
    }

    public mDistance setName(String name) {
        Name = name;
        return this;
    }

    public mDistance setKm(Double km) {
        Km = km;
        return this;
    }

    public mDistance setMANUAL_TAYN(String MANUAL_TAYN) {
        this.MANUAL_TAYN = MANUAL_TAYN;
        return this;
    }

    public mDistance setMANUAL_TAYN_MANDATORY(String MANUAL_TAYN_MANDATORY) {
        this.MANUAL_TAYN_MANDATORY = MANUAL_TAYN_MANDATORY;
        return this;
    }
}
