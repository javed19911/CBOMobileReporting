package bill.BillReport;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import saleOrder.MyOrderAPIService;
import saleOrder.ViewModel.CBOViewModel;
import services.MyAPIService;
import utils_new.CustomDatePicker;

public class vmBill extends CBOViewModel<IBill> {
   private  ArrayList<mBill> billArrayList= new ArrayList<>();
    @Override
    public void onUpdateView(Activity context, IBill view) {
        if (view != null) {
            view.getReferencesById();
            view.getActivityTitle();
            view.setActvityTitle(view.getActivityTitle());


        }

    }




    public void getBills(Context context, mCompany company, Date FDate, Date  TDate){

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        //chnge from here
        request.put("iCOMPANY_ID",company.getId());
        request.put("FDATE",CustomDatePicker.formatDate(FDate,CustomDatePicker.CommitFormat));
        request.put("TDATE",CustomDatePicker.formatDate(TDate,CustomDatePicker.CommitFormat) );

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("BILL_GRID_MOBILE",request)
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
