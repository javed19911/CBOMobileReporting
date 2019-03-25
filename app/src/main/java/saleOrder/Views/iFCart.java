package saleOrder.Views;

/**
 * Created by cboios on 06/03/19.
 */

public interface iFCart {
    void getReferencesById();
    String getCompanyCode();
    String getUserId();

    void setTile();
    void CalculateTotal();
    void updateTotal();

}
