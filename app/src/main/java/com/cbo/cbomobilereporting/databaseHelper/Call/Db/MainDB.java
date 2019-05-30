package com.cbo.cbomobilereporting.databaseHelper.Call.Db;

import com.cbo.cbomobilereporting.databaseHelper.FirebaseDbhelper;
import com.cbo.cbomobilereporting.databaseHelper.User.mUser;

import com.cbo.cbomobilereporting.MyCustumApplication;

/**
 * Created by cboios on 25/01/19.
 */

public class MainDB extends FirebaseDbhelper<String> {


    public MainDB() {
        super();
        setCheckForSuppotLoggin(true);
    }

    @Override
    public void setBaseURL(String baseURL) {
        mUser user = MyCustumApplication.getInstance().getUser();
        super.setBaseURL("CBO/" + user.getCompanyCode().trim());
        setTable(user.getID().trim());
    }


}
