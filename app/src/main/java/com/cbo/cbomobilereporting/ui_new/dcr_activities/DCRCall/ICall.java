package com.cbo.cbomobilereporting.ui_new.dcr_activities.DCRCall;

import com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils.CallBuilder;

public interface ICall {

    CallBuilder getCallBuilder();

    void setChemistName(mChemistRc chemist);
    void getRefrenceByid();
    void setActivityTitle(String title);
    String getActivityTitle();

}
