package Bill.Cart;


import Bill.NewOrder.mBillItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;

public interface ICompanyCart {
    void getReferencesById();
    String getActvityttitle();
    void  setTitle(String title);
    void onItemAdded(mBillItem item);
    void onItemEdit(mBillItem item);
    mOrder getOrder();
    //void updateOrder(mOrder order);
}
