package Bill.Cart;


import Bill.NewOrder.mBillItem;

public interface IFCompanycart {
    void getReferencesById();
    String getCompanyCode();
    String getUserId();
    //void setOrder(mOrder order);
  //  void updateOrder(mOrder order);
    void addItem(mBillItem item);

    void setTile();
    void orderCommit();
    /*void CalculateTotal();
    void updateTotal();
    void orderCommit();
    void addAttachment();*/
}
