package com.cbo.cbomobilereporting.ui_new.dcr_activities.lead;

import java.util.ArrayList;

import utils.adapterutils.Dcr_Workwith_Model;
import utils.adapterutils.PobModel;

/**
 * Created by cboios on 10/03/19.
 */

public interface iLead {
    void getReferencesById();
    void setTile();
    void onSendRespose(ArrayList<PobModel> model);
}
