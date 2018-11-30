package com.cbo.cbomobilereporting.ui_new.approval_activities.Remainder;

import java.util.ArrayList;

/**
 * Created by cboios on 20/11/18.
 */

public interface IApprovalRemainder {

    void onListUpdated(ArrayList<mApprovalRemainder> mApprovalRemainders);
    void onError(String Title,String error);
}
