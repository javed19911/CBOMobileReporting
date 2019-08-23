package com.cbo.cbomobilereporting.ui_new.dcr_activities.Recipt;

import android.content.Context;

import java.util.ArrayList;

public interface IRecipt {
    void onRecieptlistchanged(ArrayList<mRecipt>reciptArrayList);
    void onRecpieptDeleted(Context context);
    String getActivityTitle();
    void setActivityTitle(String title);
    void getReferencesById();
    void OnTotalUpdated(Double total);

}
