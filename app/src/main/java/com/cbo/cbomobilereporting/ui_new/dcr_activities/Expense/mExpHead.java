package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import java.io.Serializable;

public class mExpHead  implements Serializable {
    private int Id = 0;
    private String Name ="";
    private int MANDATORY = 0;
    private int DA_ACTION = 0;
    private String EXP_TYPE_STR = "0";
    private eExpanse EXP_TYPE = eExpanse.None;
    private int ATTACHYN = 0;
    private Double MAX_AMT = 0D;
    private int MasterValidate =0;

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

    public eExpanse getEXP_TYPE() {
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

    /// setter



    public mExpHead setMANDATORY(int MANDATORY) {
        this.MANDATORY = MANDATORY;
        return this;
    }

    public mExpHead setDA_ACTION(int DA_ACTION) {
        this.DA_ACTION = DA_ACTION;
        return this;
    }

    public mExpHead setEXP_TYPE(eExpanse EXP_TYPE) {
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
}
