package saleOrder.Views;

import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;

/**
 * Created by cboios on 06/03/19.
 */

public interface iFCart {
    void getReferencesById();
    String getCompanyCode();
    String getUserId();
    //void setOrder(mOrder order);
    void updateOrder(mOrder order);
    void addItem(mItem item);

    void setTile();
    void CalculateTotal();
    void updateTotal();
    void orderCommit();
    void addAttachment();

}
