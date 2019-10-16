package bill.NewOrder;

import cbomobilereporting.cbo.com.cboorder.Model.mDeal;

public interface IFBillNewOrder {
    String getCompanyCode();
    String getPartyId();
    String getUserId();
    void setItem(mBillItem item);
    mBillItem getItem();
    void setAddText(String text);
    void setDetaileLayoutEnabled(Boolean enabled);
    void setItemName(String name);
    void setItemHint(String hint);
    void setBatch(String batch);
    void selectBatch(mBillItem item,Boolean selectForcefully);
    void setQty(Double Qty);
    void updateDeal(mDeal deal);
    void setFreeQty(Double freeQty);
    void setPack(String pack);
    void setStock(Double stock);
    void setMRP(Double MRP);
    void setDisc1(String disc1);
    void setDiscAmt(Double discAmt);
    void setFocusQty(Boolean focusQty);
    void updateRate(Double rate);
    void updateAmt(Double amt);


}
