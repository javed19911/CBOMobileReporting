package bill.Cart;


import bill.NewOrder.mBillItem;
import bill.mBillOrder;

public interface ICompanyCart {
    void getReferencesById();
    String getActvityttitle();
    void  setTitle(String title);
    void onItemAdded(mBillItem item);
    void onItemEdit(mBillItem item);
    mBillOrder getOrder();
    void updateOrder(mBillOrder order);
}
