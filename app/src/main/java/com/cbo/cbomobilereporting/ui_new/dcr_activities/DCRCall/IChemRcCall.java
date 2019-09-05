package com.cbo.cbomobilereporting.ui_new.dcr_activities.DCRCall;

import com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils.CallBuilder;

public interface IChemRcCall {

    CallBuilder getCallBuilder();
    void getRefrenceByid();
    void setActivityTitle(String title);
    String getActivityTitle();

    void onChemistSelect();
    void onChemistSelected(mChemistRc chemistRc);
    void setName(String name);
    void remarkReqd(Boolean required);
    void setRemark(String remark);
    void save(mChemistRc chemistRc);


}
