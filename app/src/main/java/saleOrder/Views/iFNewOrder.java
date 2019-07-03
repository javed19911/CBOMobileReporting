package saleOrder.Views;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;

public interface iFNewOrder {
    String getCompanyCode();
    String getPartyId();
    String getUserId();
    void setItem(mItem item);
    mItem getItem();
    void setAddText(String text);
    int getNoOfDiscountAllowed();
    String getEditableDiscount();
    void setDetaileLayoutEnabled(Boolean enabled);
    void setItemName(String name);
    void ManualDiscountEnabled(Boolean enabled);
    void setManualDiscount(mDiscount discount);
    void setMiscDiscount(ArrayList<mDiscount> discounts);
    void ManagerDiscountEnabled(Boolean enabled);
    void setManagerDiscount(mDiscount discount);
    Boolean IsRemarkReqd();
    void setRemarkEnabled(Boolean required);
    String getRemarkTitle();
    void setRemark(String remark);
    void setQty(Double Qty);
    void setFocusQty(Boolean focusQty);
    void updateRate(Double rate);
    void updateAmt(Double amt);
    void updateDeal(mDeal deal);
    void setFreeQty(Double freeQty);
    void updateDiscountAmt(Double DisAmt);
    void updateDiscountStr(String DisStr);
    void updateTAX(mTax GST);
    void updateNetAmount(Double NetAmt);

}
