package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending;

import java.util.ArrayList;

public interface iChemist {

    public void getReferencesById();
    public String getUserID();
    public String getCompanyCode();
    void oPendingListChange(ArrayList<mChemist> var1);
    public void setTile(String title);
    String getActivityTitle();

}
