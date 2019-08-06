package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import java.io.Serializable;

public class mRate implements Serializable {
    private Double rate=0D;
    private Double FKm = 0D;
    private Double TKm = 99999D;


    //getter

    public Double getRate() {
        return rate;
    }

    public Double getFKm() {
        return FKm;
    }

    public Double getTKm() {
        return TKm;
    }

    //setter

    public mRate setRate(Double rate) {
        this.rate = rate;
        return this;
    }

    public mRate setFKm(Double FKm) {
        this.FKm = FKm;
        return this;
    }

    public mRate setTKm(Double TKm) {
        this.TKm = TKm;
        return this;
    }
}
