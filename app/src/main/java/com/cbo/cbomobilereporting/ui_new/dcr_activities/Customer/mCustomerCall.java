package com.cbo.cbomobilereporting.ui_new.dcr_activities.Customer;

import com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils.mCall;

import java.util.ArrayList;

import utils.adapterutils.PobModel;

/**
 * Created by cboios on 10/03/19.
 */

public class mCustomerCall extends mCall {

    private Boolean leadReqd = false;
    private ArrayList<PobModel> leads = new ArrayList<>();

    public mCustomerCall() {
        //setItemReqd(true);
    }

    public Boolean getLeadReqd() {
        return leadReqd;
    }

    public void setLeadReqd(Boolean leadReqd) {
        this.leadReqd = leadReqd;
    }

    public ArrayList<PobModel> getLeads() {
        return leads;
    }

    public void setLeads(ArrayList<PobModel> leads) {
        this.leads = leads;
    }
}
