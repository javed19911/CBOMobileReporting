package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.pobmail;

import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.ChemistFragment;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mChemist;

import java.util.ArrayList;

public interface iPobMail {
    void getReferencesById();
    String getActivityTitle();
    void setTile(String var1);
    public void onChemistListUpdated(ChemistFragment chemistFragment, ArrayList<mChemist> chemestsList, String filterType);

}
