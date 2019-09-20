package bill.NewOrder;

public interface IFBillNewOrder {
    String getCompanyCode();
    String getPartyId();
    String getUserId();
    void setItem(mBillItem item);
    mBillItem getItem();
    void setAddText(String text);
    void setDetaileLayoutEnabled(Boolean enabled);
    void setItemName(String name);
    void selectBatch(mBillItem item);
    void setQty(Double Qty);
    void setFocusQty(Boolean focusQty);
    void updateRate(Double rate);
    void updateAmt(Double amt);


}
