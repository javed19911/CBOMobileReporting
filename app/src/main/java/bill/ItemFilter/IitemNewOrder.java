package bill.ItemFilter;

import java.util.ArrayList;

import bill.NewOrder.mBillItem;
import bill.mBillOrder;

public interface IitemNewOrder {

    void getReferencesById();

    String getPartyID();

    String getUserID();

    String getCompanyCode();

    String getActivityTitle();

    void setTile(String var1);

    void onItemsChanged(ArrayList<mBillItem> var1);

    void onOrderChanged(mBillOrder var1);
}
