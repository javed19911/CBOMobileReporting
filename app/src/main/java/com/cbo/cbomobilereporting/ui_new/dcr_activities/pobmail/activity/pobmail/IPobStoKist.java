package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.pobmail;

import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.ChemistFragment;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mChemist;

public interface IPobStoKist {

    public void getChemestList(ChemistFragment chemistFragment, String filterType);

    void updateChemistList(ChemistFragment chemistFragment, String filterType, mChemist selectStockist);
}
