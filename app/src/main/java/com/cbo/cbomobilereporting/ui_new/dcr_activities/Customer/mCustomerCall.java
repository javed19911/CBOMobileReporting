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
    private String status = "";
    private String Competitor_Product ="";


    public mCustomerCall() {
        super("Customer");
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

    public mCustomerCall setLeads(ArrayList<PobModel> leads) {
        this.leads = leads;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public mCustomerCall setStatus(String status) {
        this.status = status;
        return this;
    }
    public String getCompetitor_Product() {
        return Competitor_Product;
    }

    public mCustomerCall setCompetitor_Product(String competitor_Product) {
        this.Competitor_Product = competitor_Product;
       return this;
    }

}
