package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import java.util.ArrayList;

public interface IExpense {
    String getCompanyCode();
    String getDCRId();
    String getUserId();
    void getReferencesById();
    String getActivityTitle();
    void setTitle(String title);
    void setRouteStatus(String Status);
    void setDA(String DA);
    void setDAType(String type);
    void updateDAView();
    void setDistance(String Distance);
    void enableDA(Boolean enable);
    void ActualDAReqd(Boolean required);
    void ActualFareReqd(Boolean required);
    void OnAddExpense(mExpense expense,mOthExpense othExpense);
    void OnOtherExpenseAdded(mOthExpense othExpense);
    void OnOtherExpenseUpdated(ArrayList<mOthExpense> othExpenses);
    void OnExpenseCommit();
    void OnExpenseCommited();
}
