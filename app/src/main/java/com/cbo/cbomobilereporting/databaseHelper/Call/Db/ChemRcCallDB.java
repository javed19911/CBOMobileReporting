package com.cbo.cbomobilereporting.databaseHelper.Call.Db;

import com.cbo.cbomobilereporting.ui_new.dcr_activities.DCRCall.mChemistRc;

/**
 * Created by cboios on 23/01/19.
 */

public class ChemRcCallDB extends NewCallDB<mChemistRc> {

    public ChemRcCallDB() {
        super();
        setTable(new mChemistRc().getTitle());
    }


}
