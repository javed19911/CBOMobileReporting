package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

public class mDA {
    private int Id = 0;
    private String Name="";
    private String Code="";
    private double multipleFactor = 1f;
    private double DAAmount =0f;
    private double TA_Rate = 1f;
    private double TA_Km = 1f;
    private double TAAmount =0f;

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

    //setter


    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setMultipleFactor(double multipleFactor) {
        this.multipleFactor = multipleFactor;
    }

    public void setDAAmount(double DAAmount) {
        this.DAAmount = DAAmount;
    }

    public void setTA_Rate(double TA_Rate) {
        this.TA_Rate = TA_Rate;
        setTAAmount(CalculateTA());
    }

    public void setTA_Km(double TA_Km) {
        this.TA_Km = TA_Km;
        setTAAmount(CalculateTA());
    }

    public double CalculateTA(){
        return getTA_Km() * getTA_Rate() * getMultipleFactor();
    }

    public void setTAAmount(double TAAmount) {
        this.TAAmount = TAAmount;
    }

}
