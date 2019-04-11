package saleOrder.Views;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;

public interface iFNewOrder {
    String getCompanyCode();
    String getPartyId();
    String getUserId();
    void setItem(mItem item);
    mItem getItem();
    void setItemName(String name);
    void setManualDiscount(mDiscount discount);
    void setMiscDiscount(ArrayList<mDiscount> discounts);
    void setManagerDiscount(mDiscount discount);
    void setQty(Double Qty);
    void setFocusQty(Boolean focusQty);

}
