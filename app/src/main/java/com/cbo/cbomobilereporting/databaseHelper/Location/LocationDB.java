package com.cbo.cbomobilereporting.databaseHelper.Location;

import com.cbo.cbomobilereporting.databaseHelper.FirebaseDbhelper;
import com.cbo.cbomobilereporting.databaseHelper.User.mUser;

import com.cbo.cbomobilereporting.MyCustumApplication;


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


    public void insert(com.cbo.cbomobilereporting.databaseHelper.Call.mCall call) {
        mUser user = MyCustumApplication.getInstance().getUser();
        mLocation location = new mLocation<com.cbo.cbomobilereporting.databaseHelper.Call.mCall>(user.getLocation())
                .setCall(call)
                .setDCR_ID(user.getDCRId());
        insert(location);
    }


    public void insert(com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils.mCall call) {
        mUser user = MyCustumApplication.getInstance().getUser();
        mLocation location = new mLocation<com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils.mCall>(user.getLocation())
                .setCall(call)
                .setDCR_ID(user.getDCRId());
        insert(location);
    }


}
