package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import java.util.ArrayList;

public interface IOtherExpense {
    String getCompanyCode();
    String getDCRId();
    String getUserId();
    void getReferencesById();
    void loadExpenseHead(ArrayList<mExpHead> expHeads);
    void setRemarkCaption(String caption);
    void setAmountCaption(String caption);
    void setAmtHint(String hint);
    void onSendResponse(mOthExpense othExpense);
}
