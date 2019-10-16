package bill.Outlet;

import java.util.ArrayList;

public interface iOutlet {
    void getReferencesById();
    void setOnClickListeners();
    String getActivityTitle();
    void setTitle(String Title);
    String getCompanyCode();
    String getUserId();
    void onListUpdated(ArrayList<mOutlet> outlets);
    void onTotalUpdated(mOutlet TotalOutlet);
}
