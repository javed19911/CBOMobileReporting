package saleOrder.Views;

import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;

/**
 * Created by cboios on 06/03/19.
 */

public interface iCart {
    void getReferencesById();
    void setTitle(String title);
    void onItemAdded(mItem item);
    void onItemEdit(mItem item);
    mOrder getOrder();
    void updateOrder(mOrder order);

}
