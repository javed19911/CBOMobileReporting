package com.cbo.cbomobilereporting.databaseHelper.Call.Db;

import com.cbo.cbomobilereporting.databaseHelper.Call.mDrCall;

/**
 * Created by cboios on 23/01/19.
 */

public class DrCallDB extends CallDB<mDrCall> {

    public DrCallDB() {
        super();
        setTable(new mDrCall().getTitle());
    }
}
