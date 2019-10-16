package com.cbo.cbomobilereporting.ui_new.report_activities.Dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import saleOrder.ViewModel.CBOViewModel;
import services.CboServices;
import services.MyAPIService;
import utils_new.AppAlert;

public class vmDashboard extends CBOViewModel<iDashboard> {
    private int month =0;
    private ArrayList<Map<String, String>> data1 = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, String>> data2 = new ArrayList<Map<String, String>>();
    private HashMap<String, ArrayList<Map<String, String>>> dashboard_list;
    private final List<String> header_title=new ArrayList<>();


    @Override
    public void onUpdateView(AppCompatActivity context, iDashboard view) {
        dashboard_list=new LinkedHashMap<>();
        dashboard_list.put("Marketing",data1);
        dashboard_list.put("Sales",data2);
        for(String main_menu:dashboard_list.keySet()){
            header_title.add(main_menu);
        }

        view.getReferencesById();
        view.setOnClickListeners();
        update_page(context);

        view.setDashboard(dashboard_list,header_title,getDate("MMM"));
    }




    private String getDate(String date_format){
        SimpleDateFormat format = new SimpleDateFormat(date_format);//"yyyy.MM.dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        System.out.println(format.format(cal.getTime()));
        return format.format(cal.getTime());
    }

    public void showNext(Context context){
        month++;
        update_page(context);
    }

    public void showPrevious(Context context){
        month--;
        update_page(context);
    }


    private void update_page(Context context) {


        switch (getDate("MM")){
            case "04":
                view.setNextBtnVisibility(true);
                view.setPreBtnVisibility(false);
                break;
            case "03":
                view.setNextBtnVisibility(false);
                view.setPreBtnVisibility(true);
                break;
            default:
                view.setNextBtnVisibility(true);
                view.setPreBtnVisibility(true);
        }

        if (month>=0){
            month=0;
            view.setNextBtnVisibility(false);
        }else{
            view.setNextBtnVisibility(true);
        }

        view.setMonth(getDate("MMM-yyyy"));

        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPaId",  MyCustumApplication.getInstance().getUser().getID());
        request.put("sMONTH",getDate("MM/dd/yyyy"));

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        tables.add(1);

        new MyAPIService(context).execute(new ResponseBuilder("DashBoardFinal_1",request)
                .setTables(tables)
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {

                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {
                        parser_dashboard(bundle);
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                }));

        //End of call to service

    }


    private void parser_dashboard(Bundle result) throws JSONException {
        String table0 = result.getString("Tables0");
        JSONArray jsonArray1 = new JSONArray(table0);
        data1.clear();
        data2.clear();

        for (int j = 0; j < jsonArray1.length(); j++) {
            Map<String, String> datanum1 = new HashMap<String, String>();
            JSONObject jobj = jsonArray1.getJSONObject(j);

            datanum1.put("REMARK", jobj.getString("REMARK"));
            datanum1.put("AMOUNT", jobj.getString("AMOUNT"));
            datanum1.put("AMOUNT_CUMM", jobj.getString("AMOUNT_CUMM"));
            datanum1.put("URL", jobj.getString("URL"));
            data1.add(datanum1);

        }
        String table1 = result.getString("Tables1");
        JSONArray jsonArray2 = new JSONArray(table1);
        for (int k = 0; k < jsonArray2.length(); k++) {
            Map<String, String> datanum2 = new HashMap<String, String>();
            JSONObject jobj = jsonArray2.getJSONObject(k);
            datanum2.put("REMARK", jobj.getString("REMARK"));
            datanum2.put("AMOUNT", jobj.getString("AMOUNT"));
            datanum2.put("AMOUNT_CUMM", jobj.getString("AMOUNT_CUMM"));
            datanum2.put("URL", jobj.getString("URL"));
            data2.add(datanum2);

        }
    }


}
