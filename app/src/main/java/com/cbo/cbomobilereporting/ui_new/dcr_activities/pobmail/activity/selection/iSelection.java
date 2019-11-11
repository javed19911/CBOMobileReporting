package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.selection;

import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mStockist;

import java.util.ArrayList;

public interface iSelection {
    void getReferencesById();
    String getActivityTitle();
    void setTile(String var1);
    void onPartyListUpdated(ArrayList<mStockist> stockists);
}
