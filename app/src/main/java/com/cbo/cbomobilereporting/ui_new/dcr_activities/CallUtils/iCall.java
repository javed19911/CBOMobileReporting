package com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils;

import utils.adapterutils.SpinnerModel;

/**
 * Created by cboios on 10/03/19.
 */

public interface iCall {
    void getReferencesById();
    void setTile();
    void onSendRespose(SpinnerModel model);
}
