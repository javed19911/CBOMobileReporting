package com.cbo.cbomobilereporting.databaseHelper;


import com.cbo.cbomobilereporting.databaseHelper.User.mUser;

import FirebaseDatabase.FirebsaeDB;
import com.cbo.cbomobilereporting.MyCustumApplication;

public abstract class FirebaseDbhelper<T> extends FirebsaeDB<T> {

    private Boolean checkForSuppotLoggin = true;
    public FirebaseDbhelper() {
        super ("", "");
    }


    public Boolean getCheckForSuppotLoggin() {
        return checkForSuppotLoggin;
    }

    public void setCheckForSuppotLoggin(Boolean checkForSuppotLoggin) {
        this.checkForSuppotLoggin = checkForSuppotLoggin;
    }

    private Boolean isSessionActive(){
        mUser user = MyCustumApplication.getInstance().getUser();
        if (user!= null && MyCustumApplication.getInstance().FirebaseSyncYN.equalsIgnoreCase("Y")){
            if (
                    !(getCheckForSuppotLoggin() && user.getLoggedInAsSupport()) &&
                    (!user.getID().trim().equalsIgnoreCase("0") && !user.getCompanyCode().trim().equalsIgnoreCase(""))
                    ) {
                setBaseURL ("CBO/"+ user.getCompanyCode().trim()+"/"+user.getID().trim());
                return true;
            }
        }

        return false;
    }

    public void getDbRef(ILogin response) {
        if (isSessionActive()) {
            super.login(response);
        }
    }

    @Override
    public void insert(T model) {
        if (isSessionActive()) {
            super.insert(model);
        }
    }

    @Override
    public void insert(T model, String primerykey) {
        if (isSessionActive()) {
            super.insert(model, primerykey);
        }
    }

    @Override
    public void delete(T model) {
        if (isSessionActive()) {
            super.delete(model);
        }
    }

    @Override
    public void delete(T model, String primerykey) {
        if (isSessionActive()) {
            super.delete(model, primerykey);
        }
    }

}
