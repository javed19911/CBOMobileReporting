package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending;

import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.selection.Selection;

import java.io.Serializable;

public class mStockist implements Serializable {

    private int ID=0;
    private String NAME="";
    private String EMAIL="";
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

}
