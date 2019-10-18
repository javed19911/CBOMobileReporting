package com.cbo.cbomobilereporting.databaseHelper.Call.Db;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.databaseHelper.FirebaseDbhelper;

import bill.mBillOrder;

public class BillOrderDB extends FirebaseDbhelper<mBillOrder> {

    String TempId;

    public BillOrderDB(String DocType) {
        super();
        setCheckForSuppotLoggin(false);
        setPrimaryKeyAutoGenrate (false);
        setTable ("BillOrder/"+DocType);
        TempId = MyCustumApplication.getInstance().getUser().getIMEI();
    }




    @Override
    public void insert(mBillOrder model, String primerykey) {
        super.insert(model, model.getDocId());
    }

    @Override
    public void delete(mBillOrder model, String primerykey) {

        super.delete(model, model.getDocId());

    }


}
