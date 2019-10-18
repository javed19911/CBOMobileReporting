package bill.Outlet;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import bill.Dashboard.mDashboard;
import bill.Dashboard.mDashboardNew;
import saleOrder.MyOrderAPIService;
import saleOrder.ViewModel.CBOViewModel;
import utils_new.AppAlert;

public class vmOutlet extends CBOViewModel<iOutlet> {

    private mOutlet TotOutlet = new mOutlet();
    private ArrayList<mOutlet> dataList = new ArrayList<>();
    private mDashboardNew dashboard = new mDashboardNew();

    public mDashboardNew getDashboard() {
        return dashboard;
    }

    public void setDashboard(mDashboardNew dashboard) {
        this.dashboard = dashboard;
    }

    @Override
    public void onUpdateView(AppCompatActivity context, iOutlet view) {
        view.getReferencesById();
        view.setOnClickListeners();
        view.setTitle(view.getActivityTitle());

        getOutlet(context);
    }

    public void getOutlet(Context context){
        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", view.getCompanyCode());
        request.put("iLOGIN_PA_ID", view.getUserId());
        request.put("sDOC_TYPE",dashboard.getDOC_TYPE());
        request.put("iLEVEL_NO","2");
        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("DASHBOARD_AURIC",request)
                .setTables(tables)
//                    .setShowProgess(items.size() == 0 )
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        if (view != null) {
                            view.onListUpdated(dataList);
                            view.onTotalUpdated(TotOutlet);
                        }
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {

                        parser2(bundle);
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                }));
    }

    private void parser2(Bundle result) throws Exception {
        {
            String table0 = result.getString("Tables0");
            JSONArray row = new JSONArray(table0);

            int totalSale= 0,otherSale= 0,totalCaseSale = 0;
            int totalBills =0;
            dataList.clear();
            for (int i = 0; i < row.length(); i++) {
                JSONObject c = row.getJSONObject(i);
                mOutlet outlet=new mOutlet();
                outlet.setCOMPANY_ID(c.getString("COMPANY_ID"));
                outlet.setCOMPANY_NAME(c.getString("COMPANY_NAME"));
                outlet.setCASH_SALE(c.getString("CASH_SALE"));
                outlet.setNO_BILL(c.getString("NO_BILL"));
                outlet.setTOTAL_SALE(c.getString("TOTAL_SALE"));
                outlet.setOTHER_SALE(c.getString("OTHER_SALE"));
                outlet.setFDATE(c.getString("FDATE"));
                outlet.setTDATE(c.getString("TDATE"));

                totalBills += c.getInt("NO_BILL");
                totalSale += c.getInt("TOTAL_SALE");
                otherSale += c.getInt("OTHER_SALE");
                totalCaseSale += c.getInt("CASH_SALE");

                TotOutlet.setTOTAL_SALE(""+totalSale);
                TotOutlet.setNO_BILL(""+totalBills);
                TotOutlet.setOTHER_SALE(""+otherSale);
                TotOutlet.setCASH_SALE(""+totalCaseSale);

                dataList.add(outlet);


            }


        }


    }
}
