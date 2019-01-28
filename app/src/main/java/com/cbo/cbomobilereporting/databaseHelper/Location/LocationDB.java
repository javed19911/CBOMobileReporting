package com.cbo.cbomobilereporting.databaseHelper.Location;

import android.location.Location;

import com.cbo.cbomobilereporting.databaseHelper.Call.mCall;
import com.cbo.cbomobilereporting.databaseHelper.FirebaseDbhelper;
import com.cbo.cbomobilereporting.databaseHelper.User.mUser;

import utils.clearAppData.MyCustumApplication;


public class LocationDB extends FirebaseDbhelper<mLocation> {



    public LocationDB() {
        super();
        setCheckForSuppotLoggin(true);
        setTable ("Location");
    }

    public void setBaseURL(String baseURL) {
        super.setBaseURL (baseURL);
        setTable ("Location");
    }


    public void insert(mCall call) {
        mUser user = MyCustumApplication.getInstance().getUser();
        mLocation location = new mLocation(user.getLocation())
                .setCall(call)
                .setDCR_ID(user.getDCRId());
        insert(location);
    }


}
