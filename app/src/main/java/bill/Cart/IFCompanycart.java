package bill.Cart;


import bill.NewOrder.mBillItem;
import bill.mBillOrder;

public interface IFCompanycart {
    void getReferencesById();
    String getCompanyCode();
    String getUserId();
    void setOrder(mBillOrder order);
    void updateOrder(mBillOrder order);
    void addItem(mBillItem item);

    void setTile();
    void orderCommit();
    void CalculateTotal();
    void updateTotal();
    void addAttachment();
}
