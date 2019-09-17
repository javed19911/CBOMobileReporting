package Bill.BillReport;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import saleOrder.ViewModel.CBOViewModel;
import services.MyAPIService;
import utils_new.CustomDatePicker;

public class vmBill extends CBOViewModel<IBill> {
   private  Context context;
   private  ArrayList<mBill> billArrayList= new ArrayList<>();
   private mCompany company= new mCompany();
   private  FBillFilter fBillFilters;
   private String Componey_ID;
   private  String FromDate="";
   private  String ToDate="";


    @Override
    public void onUpdateView(Activity context, IBill view) {
        if (view != null) {
            view.getReferencesById();
            view.getActivityTitle();
            view.setActvityTitle(view.getActivityTitle());
       //     view.setmCompany(company);


        }

    }





    public  void  setFragment(FBillFilter fBillFilters){
        this .fBillFilters=fBillFilters;
       this.fBillFilters.getViewModel();



   }

    public String getComponey_ID() {
        return Componey_ID;
    }

    public String getFromDate() {
        return FromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setComponey_ID(String componey_ID) {
        Componey_ID = componey_ID;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public void getmComponey(Context context,String componey_id, String fromdate, String  todate){
        this.Componey_ID=componey_id;
        this.FromDate=fromdate;
        this.ToDate=todate;
        GetBillGridList((Activity) context);

    }



    public void GetBillGridList(Activity context) {


        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        //chnge from here
        request.put("iCOMPANY_ID",Componey_ID);
        request.put("FDATE",FromDate);
        request.put("TDATE",ToDate );
        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyAPIService(context).execute(new ResponseBuilder("BILL_GRID_MOBILE",request)
                .setTables(tables)
//                    .setShowProgess(items.size() == 0 )
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        if (view != null) {
                           view.onBillListlistchange(billArrayList);
                        }
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {

                        parser2(bundle);
//{"Tables":[{"Tables0":[{"COMPANY_NAME":"DEMO TEST","POSTING_ID":"58473","BILL_PRINT":"STFM1920GTS017","DOC_DATE":"11/09/2019","NET_AMT":"554"}]}]}x
                    //    parser1(bundle);
                    }

                    @Override
                    public void onError(String s, String s1) {

                    }
                }));

    }
    private void parser2(Bundle result) throws Exception {
        {
            String table0 = result.getString("Tables0");
            JSONArray row = new JSONArray(table0);

            billArrayList.clear();
            for (int i = 0; i < row.length(); i++) {
                JSONObject c = row.getJSONObject(i);
                mBill recieptmodel=new mBill();
              /*  recieptmodel.setId(c.getInt("ID"));
                mParty party = new mParty(c.getString("PA_ID"),c.getString("PA_NAME"));
                recieptmodel.setParty(party);*/
                recieptmodel.setCOMPANY_NAME(c.getString("COMPANY_NAME"));
                recieptmodel.setPOSTING_ID(c.getString("POSTING_ID"));
                recieptmodel.setBILL_PRINT(c.getString("BILL_PRINT"));
                recieptmodel.setNET_AMT(c.getDouble("NET_AMT"));
                recieptmodel.setDOC_DATE(CustomDatePicker.getDate( c.getString("DOC_DATE"),CustomDatePicker.ShowFormatOld));


                billArrayList.add(recieptmodel);


            }


        }


    }



}
