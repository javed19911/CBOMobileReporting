package bill.Dashboard;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import saleOrder.MyOrderAPIService;
import saleOrder.ViewModel.CBOViewModel;
import utils_new.AppAlert;

public class vmDashboard extends CBOViewModel<iDashboard> {

    private ArrayList<mDashboard> dataList = new ArrayList<>();
    private ArrayList<mDashboardNew> dataListNew = new ArrayList<>();

    @Override
    public void onUpdateView(AppCompatActivity context, iDashboard view) {
        view.getReferencesById();
        view.setOnClickListeners();
        view.setTitle(view.getActivityTitle());

        getDashbord(context);
    }


    public void getDashbord(Context context){
        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", view.getCompanyCode());
        request.put("iLOGIN_PA_ID", view.getUserId());
        request.put("sDOC_TYPE","");
        request.put("iLEVEL_NO","1");
        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("DASHBOARD_AURIC",request)
                .setTables(tables)
//                    .setShowProgess(items.size() == 0 )
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        if (view != null) {
                            //view.onListUpdated(dataList);
                            view.onListUpdatedNew(dataListNew);
                        }
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {

                        parserNew(bundle);
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                }));
    }

    private void parserNew(Bundle result) throws Exception {
        {
            String table0 = result.getString("Tables0");
            JSONArray row = new JSONArray(table0);

            dataListNew.clear();
            for (int i = 0; i < row.length(); i++) {
                JSONObject c = row.getJSONObject(i);
                mDashboardNew dashboard = isGroupAvialable(c.getString("GROUP_NAME"));
                if ( dashboard == null){
                    dashboard = new mDashboardNew();
                    dataListNew.add(dashboard);
                    dashboard.setDOC_TYPE(c.getString("DOC_TYPE"));
                    dashboard.setGROUP_NAME(c.getString("GROUP_NAME"));
                    dashboard.setBG_COLOR(c.getString("BG_COLOR"));
                }

                dashboard.getCOL_NAME().add(c.getString("COL_NAME"));
                dashboard.getCOL_VALUE().add(c.getString("COL_VALUE"));

            }



        }


    }

    private mDashboardNew isGroupAvialable(String GROUP_NAME){

        for (mDashboardNew dashBoardMain : dataListNew){
            if (GROUP_NAME.equalsIgnoreCase(dashBoardMain.getGROUP_NAME())){
                return dashBoardMain;
            }
        }

        return null;
    }

    private void parser2(Bundle result) throws Exception {
        {
            String table0 = result.getString("Tables0");
            JSONArray row = new JSONArray(table0);

            dataList.clear();
            for (int i = 0; i < row.length(); i++) {
                JSONObject c = row.getJSONObject(i);
                mDashboard dashboard=new mDashboard();
                dashboard.setDOC_TYPE(c.getString("DOC_TYPE"));
                dashboard.setDOC_CAPTION(c.getString("DOC_CAPTION"));
                dashboard.setCASH_SALE(c.getString("CASH_SALE"));
                dashboard.setNO_BILL(c.getString("NO_BILL"));
                dashboard.setTOTAL_SALE(c.getString("TOTAL_SALE"));
                dashboard.setOTHER_SALE(c.getString("OTHER_SALE"));
                dashboard.setBG_COLOR(c.getString("BG_COLOR"));




                dataList.add(dashboard);


            }


        }


    }
}
