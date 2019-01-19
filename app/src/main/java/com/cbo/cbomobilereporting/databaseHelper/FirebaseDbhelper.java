package com.cbo.cbomobilereporting.databaseHelper;


import FirebaseDatabase.FirebsaeDB;
import utils_new.Custom_Variables_And_Method;

public abstract class FirebaseDbhelper<T> extends FirebsaeDB<T> {

    public FirebaseDbhelper() {
        super ("", "");
        setBaseURL ("CBO/"+ Custom_Variables_And_Method.COMPANY_CODE+"/"+Custom_Variables_And_Method.PA_ID);
    }

}
