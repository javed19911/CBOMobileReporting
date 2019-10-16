package bill.BillReport;

import android.content.Context;

import java.util.ArrayList;

import bill.Cart.mCustomer;
import bill.mBillOrder;

public interface IBill {
    void getReferencesById();
    void setActvityTitle(String title);
    String getActivityTitle();
    void onBillListlistchange(ArrayList<mBill> billlist);
    void updateTotBillAmt(Double totamt);
    void onBillDeleted(Context context);
    void setCompanyName(String CompanyName);
    String getCompanyName();
    void setBillTitle(String titile);
    void setmCompany(mCompany company);
    void showBillDetail(mBillOrder order, mCustomer customer);




}
