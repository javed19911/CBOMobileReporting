package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import com.cbo.cbomobilereporting.MyCustumApplication;

import java.io.Serializable;
import java.util.ArrayList;

import utils_new.Custom_Variables_And_Method;

public class mExpense implements Serializable {
    private String DA_TYPE = "";
    private String FARE = "";
    private String ACTUALFAREYN = "";
    private String ACTUALDA_FAREYN = "";
    private String ROUTE_CLASS ="";
    private ArrayList<mExpHead> expHeads = new ArrayList();
    private ArrayList<mOthExpense> othExpenses = new ArrayList();
    private Double DA_Amt = 0D;
    private Double TA_Amt = 0d;
    private String Attachment = "";


    ///getter


    public String getDA_TYPE() {
        return DA_TYPE;
    }

    public String getFARE() {
        return FARE;
    }

    public String getACTUALFAREYN() {
        return ACTUALFAREYN;
    }

    public String getACTUALDA_FAREYN() {
        return ACTUALDA_FAREYN;
    }

    public String getROUTE_CLASS() {
        return ROUTE_CLASS;
    }

    public ArrayList<mExpHead> getExpHeads() {
        return expHeads;
    }

    public ArrayList<mOthExpense> getOthExpenses() {
        return othExpenses;
    }

    public Double getDA_Amt() {
        return DA_Amt;
    }

    public Double getTA_Amt() {
        return TA_Amt;
    }

    public String getAttachment() {
        return Attachment;
    }

    /// setter


    public mExpense setDA_TYPE(String DA_TYPE) {
        this.DA_TYPE = DA_TYPE;
        return this;
    }

    public mExpense setFARE(String FARE) {
        this.FARE = FARE;
        return this;
    }

    public mExpense setACTUALFAREYN(String ACTUALFAREYN) {
        this.ACTUALFAREYN = ACTUALFAREYN;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("ACTUALFAREYN",ACTUALFAREYN);
        return this;
    }

    public mExpense setACTUALDA_FAREYN(String ACTUALDA_FAREYN) {
        this.ACTUALDA_FAREYN = ACTUALDA_FAREYN;
        return this;
    }

    public mExpense setROUTE_CLASS(String ROUTE_CLASS) {
        this.ROUTE_CLASS = ROUTE_CLASS;
        return this;
    }

    public mExpense setExpHeads(ArrayList<mExpHead> expHeads) {
        this.expHeads = expHeads;
        return this;
    }

    public mExpense setOthExpenses(ArrayList<mOthExpense> othExpenses) {
        this.othExpenses = othExpenses;
        return this;
    }

    public mExpense setDA_Amt(Double DA_Amt) {
        this.DA_Amt = DA_Amt;
        return this;
    }

    public mExpense setTA_Amt(Double TA_Amt) {
        this.TA_Amt = TA_Amt;
        return this;
    }

    public mExpense setAttachment(String attachment) {
        Attachment = attachment;
        return this;
    }
}
