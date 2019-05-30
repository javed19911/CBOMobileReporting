package com.cbo.cbomobilereporting.databaseHelper.Call;

/**
 * Created by cboios on 23/01/19.
 */

public class mDairyCall extends mCall {

    private String interested;

    public mDairyCall(String title) {
        super(title);
    }


    public String getInterested() {
        return interested;
    }

    public mDairyCall setInterested(String interested) {
        this.interested = interested;
        return this;
    }
}
