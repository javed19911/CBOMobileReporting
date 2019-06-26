package com.cbo.cbomobilereporting.databaseHelper.Sync;

import com.cbo.cbomobilereporting.databaseHelper.FirebaseDbhelper;

import java.util.Map;

public class SyncDB extends FirebaseDbhelper<Map> {

    public SyncDB() {
        super();
        setCheckForSuppotLoggin(false);
        setPrimaryKeyAutoGenrate (false);
        setTable ("Sync");
    }


}
