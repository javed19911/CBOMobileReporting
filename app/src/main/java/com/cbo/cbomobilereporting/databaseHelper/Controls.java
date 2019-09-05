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

    private Boolean RouteWise;

    private Boolean giftCampaignWise;

    public Boolean IsGPSRequired() {
        if (GPSRequired == null){
            GPSRequired = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("gps_needed","N").equalsIgnoreCase("Y");
        }
        return GPSRequired;
    }

    public void setGpsRequired(String GPSRequired) {
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("gps_needed",GPSRequired );
        this.GPSRequired = null;
    }

    public Boolean IsRouteWise() {
        if (RouteWise == null){
            RouteWise = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("root_needed","N").equalsIgnoreCase("Y");
        }
        return RouteWise;
    }

    public void setRouteWise(String RouteWise) {
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("root_needed",RouteWise );
        this.RouteWise = null;
    }

    public Boolean IsGiftCampaignWiseReqd() {
        if (giftCampaignWise == null){
            giftCampaignWise = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("GIFTCAMPWISEYN","N").equalsIgnoreCase("Y");
        }
        return giftCampaignWise;
    }

    public void setGiftCampaignWiseReqd(String giftCampaignWise) {
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("GIFTCAMPWISEYN",giftCampaignWise );
        this.giftCampaignWise = null;
    }
}
