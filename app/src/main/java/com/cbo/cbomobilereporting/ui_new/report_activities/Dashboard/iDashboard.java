package com.cbo.cbomobilereporting.ui_new.report_activities.Dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface iDashboard {
    void getReferencesById();
    void setOnClickListeners();
    String getCompanyCode();
    String getUserId();
    void setNextBtnVisibility(Boolean visible);
    void setPreBtnVisibility(Boolean visible);
    void setMonth(String Month);
    void setDashboard(HashMap<String, ArrayList<Map<String, String>>> dashboard_list, List<String> header, String Month);
    void onDashboardUpdated(HashMap<String, ArrayList<Map<String, String>>> dashboard_list);
}
