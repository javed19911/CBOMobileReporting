package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import java.util.ArrayList;

public interface IExpense {
    String getCompanyCode();
    String getDCRId();
    String getUserId();
    void getReferencesById();
    String getActivityTitle();
    void setTitle(String title);
    boolean IsRouteWise();
    void setRouteStatus(String Status);
    void setDA(String DA);
    void setManualDA(mDA da);
    void setManualTA(mDistance distance);
    void setDAType(String type);
    void setTADetail(String detail);
    void updateDAView();
    void setDistance(String Distance);
    void enableDA(Boolean enable);
    void ActualDAReqd(Boolean required);
    void ManualDA_TypeReqd(Boolean required);
    void ManualDAReqd(Boolean required);
    void ManualTAReqd(Boolean required);
    void ManualTAMandetory(Boolean required);
    void ManualStationReqd(Boolean required);
    void ManualDistanceReqd(Boolean required);
    void ActualFareReqd(Boolean required);
    void OnAddExpense(mExpense expense,mOthExpense othExpense,eExpense eExpense);
    void OnOtherExpenseAdded(mOthExpense othExpense);
    void OnOtherExpenseUpdated(ArrayList<mOthExpense> othExpenses);
    void OnTAExpenseAdded(mOthExpense othExpense);
    void OnTAExpenseUpdated(ArrayList<mOthExpense> othExpenses);
    void OnDAExpenseAdded(mOthExpense DAExpense);
    void OnDAExpenseUpdated(ArrayList<mOthExpense> DAExpenses);
    void OnFinalRemarkReqd(Boolean required);
    void OnExpenseCommit();
    void OnExpenseCommited();
}
