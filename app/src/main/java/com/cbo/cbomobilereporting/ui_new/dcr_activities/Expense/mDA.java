package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import java.io.Serializable;

public class mDA implements Serializable {
    private int Id = 0;
    private String Name="--Select--";
    private String Code="";
    private double multipleFactor = 1f;
    private double DAAmount =0f;
    private double TA_Rate = 0f;
    private double TA_Km = 0f;
    private double TAAmount =0f;
    private String MANUAL_DISTANCEYN ="";
    private String MANUAL_TAYN="";
    private String MANUAL_TAYN_MANDATORY="";

    //getter


    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getCode() {
        return Code;
    }

    public double getMultipleFactor() {
        return multipleFactor;
    }

    public double getDAAmount() {
        return DAAmount;
    }

    public double getTA_Rate() {
        return TA_Rate;
    }

    public double getTA_Km() {
        return TA_Km;
    }

    public double getTAAmount() {
        return TAAmount;
    }

    public String getMANUAL_DISTANCEYN() {
        return MANUAL_DISTANCEYN;
    }

    public String getMANUAL_TAYN() {
        return MANUAL_TAYN;
    }

    public String getMANUAL_TAYN_MANDATORY() {
        return MANUAL_TAYN_MANDATORY;
    }

    //setter


    public mDA setId(int id) {
        Id = id;
        return this;
    }

    public mDA setName(String name) {
        Name = name;
        return this;
    }

    public mDA setCode(String code) {
        Code = code;
        return this;
    }

    public mDA setMultipleFactor(double multipleFactor) {
        this.multipleFactor = multipleFactor;
        return this;
    }

    public mDA setDAAmount(double DAAmount) {
        this.DAAmount = DAAmount;
        return this;
    }

    public mDA setTA_Rate(double TA_Rate) {
        this.TA_Rate = TA_Rate;
        setTAAmount(CalculateTA());
        return this;
    }

    public mDA setTA_Km(double TA_Km) {
        this.TA_Km = TA_Km * getMultipleFactor();
        setTAAmount(CalculateTA());
        return this;
    }

    public double CalculateTA(){
        return getTA_Km() * getTA_Rate();
    }

    public mDA setTAAmount(double TAAmount) {
        this.TAAmount = TAAmount;
        return this;
    }

    public mDA setMANUAL_DISTANCEYN(String MANUAL_DISTANCEYN) {
        this.MANUAL_DISTANCEYN = MANUAL_DISTANCEYN;
        return this;
    }

    public mDA setMANUAL_TAYN(String MANUAL_TAYN) {
        this.MANUAL_TAYN = MANUAL_TAYN;
        return this;
    }

    public mDA setMANUAL_TAYN_MANDATORY(String MANUAL_TAYN_MANDATORY) {
        this.MANUAL_TAYN_MANDATORY = MANUAL_TAYN_MANDATORY;
        return this;
    }
}
