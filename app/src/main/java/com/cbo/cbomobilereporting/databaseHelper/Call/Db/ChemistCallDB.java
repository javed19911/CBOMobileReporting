package com.cbo.cbomobilereporting.databaseHelper.Call.Db;

import com.cbo.cbomobilereporting.databaseHelper.Call.mChemistCall;

/**
 * Created by cboios on 23/01/19.
 */

public class ChemistCallDB extends CallDB<mChemistCall> {

    public ChemistCallDB() {
        super();
        setTable(new mChemistCall().getTitle());
    }
}
