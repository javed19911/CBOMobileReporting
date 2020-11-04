package com.cbo.cbomobilereporting.ui_new.report_activities.MissedDoctor;

import android.app.Activity;
import android.app.ProgressDialog;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.DrVisitedList;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.F_TeamMonthDivision;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import services.CboServices;
import com.cbo.cbomobilereporting.MyCustumApplication;
import utils_new.AppAlert;
import utils_new.CustomError;
import utils_new.Custom_Variables_And_Method;

public class VM_MissedGrid_Mobile  extends ViewModel{

    private Custom_Variables_And_Method customVariablesAndMethod;
    private OnResultlistener resultlistner = null;
 //   ArrayList<mMissedGrid> mMissedGridArrayList= new ArrayList<>();
    private ArrayList<mMissedGrid> mMissedGridArrayList = null;
    F_TeamMonthDivision f_teamMonthDivision = null;

    CBO_DB_Helper cboDbHelper;
    Context context;
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;
    private String lastPaId,lastmonthId,last_missedType;
    private String title;

    public VM_MissedGrid_Mobile() {
        super();
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
        cboDbHelper = new CBO_DB_Helper(MyCustumApplication.getInstance());
    }

    public String getTitle() {
        return cboDbHelper.getMenu("REPORTS", "R_DRWISE").get("R_DRWISE");
    }

    public ArrayList<mMissedGrid> getList() {
        return mMissedGridArrayList;
    }

    public interface OnResultlistener{
        void onSuccess(ArrayList<mMissedGrid> mMissedGrids);
        void onError(String Title,String error);
    }
      public  void GET_MISSEDGRID_LIST(Context context,OnResultlistener listener) {
          CustomError customError = f_teamMonthDivision.getViewModel().validate();
          if (customError.getCode() == CustomError.Code.ok) {
              if (this.lastPaId != f_teamMonthDivision.getViewModel().getUser().getId()
                      || this.lastmonthId != f_teamMonthDivision.getViewModel().getMonth().getId()
                      || this.last_missedType != f_teamMonthDivision.getViewModel().getMissedFilter().getId())
                  mMissedGridArrayList = null;

              this.lastPaId = f_teamMonthDivision.getViewModel().getUser().getId();
              this.lastmonthId = f_teamMonthDivision.getViewModel().getMonth().getId();
              this.last_missedType = f_teamMonthDivision.getViewModel().getMissedFilter().getId();

              if (mMissedGridArrayList == null) {
                  ShowMissedGrid((Activity) context, listener);
              } else {
                  listener.onSuccess(mMissedGridArrayList);
              }
          }else{
              AppAlert.getInstance().Alert(context,"Alert!!!",customError.getDescription(),null);
          }
      }
    public  void setFragment(F_TeamMonthDivision f_teamMonthDivision){
        this.f_teamMonthDivision = f_teamMonthDivision;
    }


    private void ShowMissedGrid(Activity context, OnResultlistener listener) {
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
        progress1 = new ProgressDialog(context);

        resultlistner = listener;
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cboDbHelper.getCompanyCode());
        request.put("iPA_ID", f_teamMonthDivision.getViewModel().getUser().getId());
        request.put("sMonth", f_teamMonthDivision.getViewModel().getMonth().getId());
        request.put("iCRMYM",f_teamMonthDivision.getViewModel().getMissedFilter().getId());

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait..\n" +
                " Fetching data");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(context,mHandler).customMethodForAllServices(request,"MISSEDDRGIRD_MOBILE",MESSAGE_INTERNET,tables);

        //End of call to service



    }

    public void openDetail(Context context,mMissedGrid doctor){
        Intent intent = new Intent(context,DrVisitedList.class);
        intent.putExtra("monthId",f_teamMonthDivision.getViewModel().getMonth().getId());
        intent.putExtra("dr_id",doctor.getId());
        intent.putExtra("monthName",f_teamMonthDivision.getViewModel().getMonth().getName());
        intent.putExtra("userId",f_teamMonthDivision.getViewModel().getUser().getId());
       context.startActivity(intent);
    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        parser_worktype(msg.getData(),context);
                    }
                    break;
                case 99:
                    progress1.dismiss();
                    resultlistner.onError("Alert!!!", msg.getData().getString("Error"));
                    /*if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                    }*/
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };
    public void parser_worktype(Bundle result , Context context) {
        if (result!=null ) {

            {
                try {
                    mMissedGridArrayList = new ArrayList<>();

                    String table0 = result.getString("Tables0");
                    JSONArray row1 = new JSONArray(table0);



                    for (int i = 0; i < row1.length(); i++) {
                        Map<String,String> datanum=new HashMap<String,String>();
                        JSONObject c = row1.getJSONObject(i);
                        mMissedGrid mMissedGrid=new mMissedGrid();
                        //String id=c.getString("DR_ID");

                        mMissedGrid.setId(c.getString("DR_ID"));
                        mMissedGrid.setName(c.getString("DR_NAME"));
                        mMissedGrid.setCode(c.getString("DR_CODE"));
                        mMissedGrid.setColor(c.getString("COLOR"));
                        mMissedGrid.setStatus(c.getString("NO_CALL"));
                        mMissedGrid.setBG_COLOR(c.getString("BG_COLOR"));
                        mMissedGrid.setCRMYN(c.getInt("CRMYN"));

                        mMissedGridArrayList.add(mMissedGrid);

                    }

                    resultlistner.onSuccess(mMissedGridArrayList);
                }catch (Exception e){
                    resultlistner.onError("Missing field error", e.toString());
                    /*Log.d("MYAPP", "objects are: " + e.toString());
                    CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                    e.printStackTrace();*/
                }
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }

    public int getTotalMissed(){

        return getTotalDoctors() - getTotalCall();
    }
    public int getTotalCRM(){

        int sum = 0;
        for (mMissedGrid item:
             mMissedGridArrayList) {
            sum += item.getCRMYN();
        }
        return sum;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            return mMissedGridArrayList.stream().mapToInt(mMissedGrid::getCRMYN).sum();
//        }
    }

    public int getTotalCall(){

        int sum = 0;
        for (mMissedGrid item:
                mMissedGridArrayList) {
            if(Integer.parseInt( item.getStatus()) > 0)
                sum += 1;//Integer.parseInt( item.getStatus());
        }
        return sum;
    }

    public int getTotalDoctors(){

        /*int sum = 0;
        for (mMissedGrid item:
                mMissedGridArrayList) {
            sum += Integer.parseInt( item.getStatus());
        }*/
        return mMissedGridArrayList.size();
    }
}
