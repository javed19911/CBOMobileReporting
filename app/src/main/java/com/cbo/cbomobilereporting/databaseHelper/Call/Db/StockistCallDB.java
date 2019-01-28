package com.cbo.cbomobilereporting.databaseHelper.Call.Db;

import com.cbo.cbomobilereporting.databaseHelper.Call.mStockistCall;

/**
 * Created by cboios on 24/01/19.
 */

public class StockistCallDB extends CallDB<mStockistCall>{
    public StockistCallDB() {
        super();
        setTable(new mStockistCall().getTitle());
    }

}
