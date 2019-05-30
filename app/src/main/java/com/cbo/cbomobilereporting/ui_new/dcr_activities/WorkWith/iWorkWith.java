package com.cbo.cbomobilereporting.ui_new.dcr_activities.WorkWith;

import java.util.ArrayList;

import utils.adapterutils.Dcr_Workwith_Model;

/**
 * Created by cboios on 10/03/19.
 */

public interface iWorkWith {
    void getReferencesById();
    void setTile();
    void onSendRespose(ArrayList<Dcr_Workwith_Model> model);
}
