package bill.BillReport;

import android.content.Context;

import java.util.ArrayList;

public interface IBill {
    void getReferencesById();
    void setActvityTitle(String title);
    String getActivityTitle();
    void onBillListlistchange(ArrayList<mBill> billlist);
    void onBillDeleted(Context context);
    void setCompanyName(String CompanyName);
    String getCompanyName();
    void setBillTitle(String titile);
    void setmCompany(mCompany company);




}
