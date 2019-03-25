package com.cbo.cbomobilereporting.ui_new.dcr_activities.Customer;

import com.cbo.cbomobilereporting.ui_new.Model.mWorkwith;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils.CallBuilder;

import java.util.ArrayList;

import utils.adapterutils.Dcr_Workwith_Model;
import utils.adapterutils.PobModel;
import utils.adapterutils.SpinnerModel;

/**
 * Created by cboios on 10/03/19.
 */

public interface iCustomerCall {
    void getReferencesById();
    String getCompanyCode();
    String getUserId();
    CallBuilder getCallBuilder();

    void setTile();

    void setLoctionUI();
    void showCallUI();
    void showSummaryUI();

    void openCustomerList(ArrayList<SpinnerModel> customers);
    void openWorkWithList(ArrayList<Dcr_Workwith_Model> workWiths);
    void openRemarkList(String remark);
    void openProduct(ArrayList<PobModel> products);
    void openGift(ArrayList<PobModel> gifts);
    void openLead(ArrayList<PobModel> leads);
    void openPOB(ArrayList<PobModel> pob);

    void setCustomer(mCustomerCall customerCall);
    void setWorkWith(ArrayList<Dcr_Workwith_Model> workWiths );
    void setRemark(String remark);
    void setProduct(ArrayList<PobModel> products);
    void setGift(ArrayList<PobModel> gifts);
    void setLeads(ArrayList<PobModel> leads);

}
