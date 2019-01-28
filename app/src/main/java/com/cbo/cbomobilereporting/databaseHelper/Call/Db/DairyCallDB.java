package com.cbo.cbomobilereporting.databaseHelper.Call.Db;

import com.cbo.cbomobilereporting.databaseHelper.Call.mDairyCall;

/**
 * Created by cboios on 23/01/19.
 */

public class DairyCallDB extends CallDB<mDairyCall> {

    public DairyCallDB(String title) {
        super();
        setTable(title);
    }

}
