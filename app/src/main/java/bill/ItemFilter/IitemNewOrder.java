package bill.ItemFilter;

import java.util.ArrayList;

import bill.NewOrder.mBillItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;

public interface IitemNewOrder {

    void getReferencesById();

    String getPartyID();

    String getUserID();

    String getCompanyCode();

    String getActivityTitle();

    void setTile(String var1);

    void onItemsChanged(ArrayList<mBillItem> var1);

    void onOrderChanged(mOrder var1);
}
