package com.cbo.cbomobilereporting.databaseHelper.Call.Db;

import com.cbo.cbomobilereporting.databaseHelper.Call.mDrRCCall;

/**
 * Created by cboios on 23/01/19.
 */

public class DrRcCallDB extends CallDB<mDrRCCall> {

    public DrRcCallDB() {
        super();
        setTable(new mDrRCCall().getTitle());
    }
}
