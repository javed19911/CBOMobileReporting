package com.cbo.cbomobilereporting.databaseHelper.Call.Db;


import com.cbo.cbomobilereporting.databaseHelper.Call.mCall;
import com.cbo.cbomobilereporting.databaseHelper.FirebaseDbhelper;

/**
 * Created by cboios on 23/01/19.
 */

public abstract class CallDB<T> extends FirebaseDbhelper<T> {

    public CallDB() {
        super();
        setPrimaryKeyAutoGenrate(false);
    }



    @Override
    public void setBaseURL(String baseURL) {
        super.setBaseURL(baseURL+"/Call");
    }

    @Override
    public void insert(T model) {
        insert(model,((mCall)model).getId());
    }

    @Override
    public void insert(T model, String primerykey) {
        if (((mCall)model).getTitle() != null){
            super.insert(model, primerykey);
        }else{
            if (getResponse() != null){
                getResponse().onLoginError("Invalid!!!","Please enter Call Name...");
            }
        }

    }

    @Override
    public void delete(T model, String primerykey) {
        if (((mCall)model).getTitle() != null){
            super.delete(model, primerykey);
        }else{
            if (getResponse() != null){
                getResponse().onLoginError("Invalid!!!","Please enter Call Name...");
            }
        }
    }

    @Override
    public void delete(T model) {
        super.delete(model,((mCall)model).getId());
    }
}
