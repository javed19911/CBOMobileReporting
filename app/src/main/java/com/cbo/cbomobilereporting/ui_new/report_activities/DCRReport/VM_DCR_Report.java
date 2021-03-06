package com.cbo.cbomobilereporting.ui_new.report_activities.DCRReport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.F_TeamMonthDivision;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import async.RptShowTask;
import services.MyAPIService;
import services.TaskListener;
import com.cbo.cbomobilereporting.MyCustumApplication;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;


/**
 * Created by welcome on 3/22/2015.
 */
public class VM_DCR_Report extends ViewModel {


    private ArrayList<mDCR_Report> mDCR_reports = null;
    private String lastPaId,monthId;
    private F_TeamMonthDivision f_teamMonthDivision = null;


    public VM_DCR_Report() {
        super();
    }

    public interface OnResultListener{
         void onSuccess(ArrayList<mDCR_Report> item);
         void onError(String Title, String error);
    }

    public void getDCRReports(Context context, String lastPaId, String monthId, OnResultListener listener){
        if (this.lastPaId != lastPaId || this.monthId != monthId)
            mDCR_reports = null;
        this.lastPaId = lastPaId;
        this.monthId = monthId;
        if (mDCR_reports == null){
            showReportsToUI((Activity) context,listener);
        }else{
            listener.onSuccess(mDCR_reports);
        }

    }
    public  void setFragment(F_TeamMonthDivision f_teamMonthDivision){


        this.f_teamMonthDivision = f_teamMonthDivision;

        this.f_teamMonthDivision.getViewModel().setDP_DivisionReq(false)
                .setDP_MissedTypeReq(false);
    }

    private void showReportsToUI(final Activity context, OnResultListener listener){

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("sPaId", lastPaId);
        request.put("sMonth", monthId);
        request.put("sVerify", "0");
        request.put("sLoginPaId", MyCustumApplication.getInstance().getUser().getID());
        request.put("sEntryDate", "0");

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        new MyAPIService(context)
                .execute(new ResponseBuilder("DCRLISTWITHEXPENSEGRID_1", request)
                        .setTables(tables)
                        .setDescription("Please Wait..")
                        .setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) throws JSONException {

                                mDCR_reports = new ArrayList<> ();

                                String table0 = message.getString("Tables0");
                                JSONArray row = new JSONArray(table0);


                                for (int i = 0; i < row.length(); i++) {
                                    Map<String,String> datanum=new HashMap<String,String> ();
                                    JSONObject c = row.getJSONObject(i);
                                    mDCR_Report rptModel=new mDCR_Report ();


                                    String date=c.getString("DCR_DATE");
                                    rptModel.setDate(date);
                                    String station=c.getString("STATION");
                                    rptModel.setWith(station);
                                    String dr=c.getString("MVVVTotal");
                                    rptModel.setTtldr(dr);
                                    String chm=c.getString("NoOFChemist");
                                    rptModel.setTtlchm(chm);
                                    String stk=c.getString("NO_STOCKIST");
                                    rptModel.setTtlstk(stk);

                                    rptModel.setDairyCount(c.getString("DAIRY_COUNT"));

                                    rptModel.setPolutaryCount(c.getString("POULTRY_COUT"));

                                    String remark=c.getString("REMARK");
                                    rptModel.setRemark(remark);
                                    String missed_call=c.getString("MISS_DR");
                                    rptModel.setTtlMissedCall(missed_call);

                                    rptModel.setTtlNonDoctor(c.getString("NLC_TOTAL"));

                                    String nonListedDistributor=c.getString("DR_RC");
                                    rptModel.setTtlDrRiminder(nonListedDistributor);

                                    rptModel.setTtlTenivia(c.getString("DRRX_TOTAL"));

                                    String exp=c.getString("DA_TYPE");
                                    rptModel.setTtlexp(exp);
                                    mDCR_reports.add(rptModel);


                                }

                                listener.onSuccess(mDCR_reports);
                            }

                            @Override
                            public void onResponse(Bundle response) throws JSONException {

                            }

                            @Override
                            public void onError(String s, String s1) {
                                AppAlert.getInstance().getAlert(context,s, s1);

                            }


                        })
                );




    }



}

