package bill.Dashboard;

import java.util.ArrayList;

public interface iDashboard {
    void getReferencesById();
    void setOnClickListeners();
    String getActivityTitle();
    void setTitle(String Title);
    String getCompanyCode();
    String getUserId();
    void onListUpdated(ArrayList<mDashboard> dashboards);
    void onListUpdatedNew(ArrayList<mDashboardNew> dashboards);
}

