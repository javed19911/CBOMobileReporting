package com.cbo.cbomobilereporting.databaseHelper;

import com.cbo.cbomobilereporting.MyCustumApplication;

public class Controls {

    private static Controls ourInstance;

    public static Controls getInstance() {
        if (ourInstance == null)
        {
            ourInstance = new Controls();
        }
        return ourInstance;
    }

    private Controls() {
    }

    private Boolean GPSRequired;

    public Boolean IsGPSRequired() {
        if (GPSRequired == null){
            GPSRequired = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("gps_needed","N").equalsIgnoreCase("Y");
        }
        return GPSRequired;
    }

    public void setGpsRequired(Boolean GPSRequired) {
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("gps_needed",GPSRequired ? "Y":"N");
        this.GPSRequired = GPSRequired;
    }
}