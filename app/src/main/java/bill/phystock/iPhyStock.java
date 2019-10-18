package bill.phystock;

import bill.NewOrder.mBillItem;
import bill.mBillOrder;

public interface iPhyStock {

    String getCompanyCode();
    String getUserId();
    void getReferencesById();
    String getActvityttitle();
    void  setTitle(String title);
    void onItemAdded(mBillItem item);
    void onItemEdit(mBillItem item);
    mBillOrder getOrder();
    void updateOrder(mBillOrder order);
    void onItemSynced();
}
