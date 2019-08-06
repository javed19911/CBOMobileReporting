package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import java.util.ArrayList;

public interface IOtherExpense {
    String getCompanyCode();
    String getDCRId();
    String getUserId();
    void getReferencesById();
    void setTitle();
    void KmLayoutRequired(Boolean required);
    void loadExpenseHead(ArrayList<mExpHead> expHeads);
    void addAttachment();
    void setRemarkCaption(String caption);
    void setAmountCaption(String caption);
    void setAmtHint(String hint);

    void setRemark(String remark);
    void setAmount(Double amount);
    void setKm(Double km);
    void setRate(Double rate);
    void setAttachment(String path);
    void onSendResponse(mOthExpense othExpense);
}
