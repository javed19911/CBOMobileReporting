package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import java.io.Serializable;

public class mExpHead  implements Serializable {
    private int Id = 0;
    private String Name ="";
    private int MANDATORY = 0;
    private int DA_ACTION = 0;
    private String EXP_TYPE_STR = "0";
    private eExpense EXP_TYPE = eExpense.None;
    private int ATTACHYN = 0;
    private Double MAX_AMT = 0D;
    private int MasterValidate =0;
    private Double RATE = 0D;
    private eExpense SHOW_IN_TA_DA = eExpense.None;
    private String KMYN ="0";
    private String HEADTYPE_GROUP = "0";

    public mExpHead(int id, String name) {
        Id = id;
        Name = name;
    }

    /// getter

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public int getMANDATORY() {
        return MANDATORY;
    }

    public int getDA_ACTION() {
        return DA_ACTION;
    }

    public String getEXP_TYPE_STR() {
        return EXP_TYPE_STR;
    }

    public eExpense getEXP_TYPE() {
        return EXP_TYPE;
    }

    public int getATTACHYN() {
        return ATTACHYN;
    }

    public Double getMAX_AMT() {
        return MAX_AMT;
    }

    public int getMasterValidate() {
        return MasterValidate;
    }

    public Double getRATE() {
        return RATE;
    }

    public eExpense getSHOW_IN_TA_DA() {
        return SHOW_IN_TA_DA;
    }

    public String getKMYN() {
        return KMYN;
    }

    public String getHEADTYPE_GROUP() {
        return HEADTYPE_GROUP;
    }

    /// setter



    public mExpHead setMANDATORY(int MANDATORY) {
        this.MANDATORY = MANDATORY;
        return this;
    }

    public mExpHead setDA_ACTION(int DA_ACTION) {
        this.DA_ACTION = DA_ACTION;
        return this;
    }

    public mExpHead setEXP_TYPE(eExpense EXP_TYPE) {
        this.EXP_TYPE = EXP_TYPE;
        return this;
    }

    public mExpHead setEXP_TYPE_STR(String EXP_TYPE_STR) {
        this.EXP_TYPE_STR = EXP_TYPE_STR;
        return this;
    }

    public mExpHead setATTACHYN(int ATTACHYN) {
        this.ATTACHYN = ATTACHYN;
        return this;
    }

    public mExpHead setMAX_AMT(Double MAX_AMT) {
        this.MAX_AMT = MAX_AMT;
        return this;
    }

    public mExpHead setMasterValidate(int masterValidate) {
        MasterValidate = masterValidate;
        return this;
    }

    public mExpHead setRATE(Double RATE) {
        this.RATE = RATE;
        return this;
    }

    public mExpHead setSHOW_IN_TA_DA(eExpense SHOW_IN_TA_DA) {
        this.SHOW_IN_TA_DA = SHOW_IN_TA_DA;
        return this;
    }

    public mExpHead setKMYN(String KMYN) {
        this.KMYN = KMYN;
        return this;
    }

    public mExpHead setHEADTYPE_GROUP(String HEADTYPE_GROUP) {
        this.HEADTYPE_GROUP = HEADTYPE_GROUP;
        return this;
    }
}
