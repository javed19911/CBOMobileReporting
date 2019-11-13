package com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.M_TeamMonthDivision;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.mDivision;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.mMissedFilter;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.mMonth;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.mUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import services.CboServices;
import utils_new.CustomDialog.Spinner_Dialog;
import utils_new.CustomError;
import utils_new.Custom_Variables_And_Method;

public class VM_TeamMonthDivision extends ViewModel{
    private Custom_Variables_And_Method customVariablesAndMethod;
    private M_TeamMonthDivision mTeamMonthDivision = null;
    private OnResultlistener resultlistner = null;
    ArrayList<mUser>rptName= new ArrayList<>();
    ArrayList<mMonth>monthname=new ArrayList<>();
    ArrayList<mMissedFilter> missedFiletr =new ArrayList<>();


    CBO_DB_Helper cboDbHelper;
    Context context;

    mMonth month;
    mUser user;
    mDivision division;
    mMissedFilter missedFilter;

    private Boolean DP_UserReq = true;
    private Boolean DP_MonthReq = true;
    private Boolean DP_DivisionReq = true;
    private Boolean DP_MissedTypeReq = true;

    public Boolean getDP_UserReq() {
        return DP_UserReq;
    }

    public VM_TeamMonthDivision setDP_UserReq(Boolean DP_UserReq) {
        this.DP_UserReq = DP_UserReq;
        if(iTeam!=null){
            iTeam.onNameVisibilityChanged( !DP_UserReq ? View.GONE : View.VISIBLE);
        }
        return this;
    }

    public Boolean getDP_MonthReq() {
        return DP_MonthReq;
    }

    public VM_TeamMonthDivision setDP_MonthReq(Boolean DP_MonthReq) {
        this.DP_MonthReq = DP_MonthReq;
        if(iTeam!=null){
            iTeam.onMonthVisibilityChanged( !DP_MonthReq ? View.GONE : View.VISIBLE);
        }
        return this;
    }

    public Boolean getDP_DivisionReq() {
        return DP_DivisionReq;
    }

    public VM_TeamMonthDivision setDP_DivisionReq(Boolean DP_DivisionReq) {
        this.DP_DivisionReq = DP_DivisionReq;
        if(iTeam!=null){
            iTeam.onDivisionVisibilityChanged( !DP_DivisionReq ? View.GONE : View.VISIBLE);
        }
       return this;
    }

    public Boolean getDP_MissedTypeReq() {
        return DP_MissedTypeReq;

    }

    public VM_TeamMonthDivision setDP_MissedTypeReq(Boolean DP_MissedTypeReq) {
        this.DP_MissedTypeReq = DP_MissedTypeReq;
        if(iTeam!=null){
            iTeam.onMissedTypeVisibilityChanged( !DP_MissedTypeReq ? View.GONE : View.VISIBLE);
        }
        return this;
    }




    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;


    public VM_TeamMonthDivision() {
        super();
      customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
        
    }
    public  interface  ITeam{
        void onNameVisibilityChanged(int visibility);
        void onMonthVisibilityChanged(int visibility);
        void onMissedTypeVisibilityChanged(int visibility);
        void onDivisionVisibilityChanged(int visibility);

        void OnMonthSelected(mMonth mmonth);
        void OnNameSelected(mUser muser);
        void OnMissedTypeSelected(mMissedFilter missedFilter);
        void OnDivisionSelected(mDivision division);

    }
    ITeam iTeam=null;
    public  void setListner(ITeam iTeam){
        this.iTeam=iTeam;

    }
    public interface OnResultlistener{
        void onSuccess(M_TeamMonthDivision teamMonthDivision);
        void onError(String Title,String error);
    }
    public void GET_NAME_MONTH(Context context,OnResultlistener listener){

        if (mTeamMonthDivision == null){
            mTeamMonthDivision = new M_TeamMonthDivision();

            ShowNameMonth((AppCompatActivity) context,listener);
        }else{
            if(resultlistner!=null){
                resultlistner.onSuccess(mTeamMonthDivision);
            }
        }

    }





    private void ShowNameMonth(AppCompatActivity context, OnResultlistener listener) {
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
        progress1 = new ProgressDialog(context);
        cboDbHelper = new CBO_DB_Helper(context);
        resultlistner = listener;
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cboDbHelper.getCompanyCode());
        request.put("sPaId", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sMonthType","");

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);
        tables.add(1);
        tables.add(2);
        tables.add(3);

        progress1.setMessage("Please Wait..\n" +
                " Fetching data");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(context,mHandler).customMethodForAllServices(request,"TEAMMONTHDIVISION_MOBILE",MESSAGE_INTERNET,tables);

        //End of call to service



    }


    private void setMonth(mMonth month) {
        this.month = month;
        if(iTeam!=null){
            iTeam.OnMonthSelected(month);
        }
    }

    public void setUser(mUser user) {
        this.user = user;

        if (getMissed_call_Filter(user).size() >= 1){
            setMissedFilter(getMissed_call_Filter(user).get(0));
        }

        if(iTeam!=null){
            iTeam.OnNameSelected(user);
        }
    }

    private void setDivision(mDivision division) {
        this.division = division;
        if(iTeam!=null){
            iTeam.OnDivisionSelected(division);
        }
    }

    private void setMissedFilter(mMissedFilter missedFilter) {
        this.missedFilter = missedFilter;
        if(iTeam!=null){
            iTeam.OnMissedTypeSelected(missedFilter);
        }
    }

    public mMonth getMonth() {
        return month;
    }

    public mUser getUser() {
        return user;
    }

    public mDivision getDivision() {
        return division;
    }

    public mMissedFilter getMissedFilter() {
        return missedFilter;
    }

    public CustomError validate(){

        CustomError customError = new CustomError();
        if (user == null){
         return    customError.setDescription("Please select Name....");
        }else  if (month == null){
            return     customError.setDescription("Please select Month....");
        }else  if (missedFilter == null){
            return     customError.setDescription("Please select Missed Call Type....");
        }
        return customError.setCode(CustomError.Code.ok) ;
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        parser_worktype(msg.getData());
                    }
                    break;
                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        //customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        resultlistner.onError("Alert!!!", msg.getData().getString("Error"));
                    }
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };
    public void parser_worktype(Bundle result ) {
        if (result!=null ) {

            try {

                String table3 = result.getString("Tables3");
                JSONArray jsonArray3 = new JSONArray(table3);
                missedFiletr.clear();
                for (int i = 0; i < jsonArray3.length(); i++) {
                    JSONObject c = jsonArray3.getJSONObject(i);
                    mMissedFilter missedFilter = new mMissedFilter(c.getString("PA_NAME"),c.getString("PA_ID"));
                    missedFilter.setDESIG_ID(c.getInt("TO_DESIG_ID"));
                    missedFiletr.add(missedFilter);
                }
                mTeamMonthDivision.setMissed_call_Filter(missedFiletr);
                /*if (missedFiletr.size() >= 1){
                  setMissedFilter(missedFiletr.get(0));
                }*/


                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                rptName.clear();
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    // rptName.add(new DropDownModel(c.getString("PA_NAME"),c.getString("PA_ID")));
                    mUser user = new mUser(c.getString("PA_NAME"),c.getString("PA_ID"));
                    user.setDESIG_ID(c.getInt("DESIG_ID"));
                    rptName.add(user);

                }

                mTeamMonthDivision.setUsers(rptName);


                if (rptName.size()==1){
                    setUser( rptName.get(0));
                }


                String table2 = result.getString("Tables2");
                JSONArray jsonArray2 = new JSONArray(table2);
                monthname.clear();
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);
                    monthname.add(new mMonth(c.getString("MONTH_NAME"),c.getString("MONTH")));
                }

                mTeamMonthDivision.setMonths(monthname);



                String date= customVariablesAndMethod.currentDate().substring(0,2);
                for (int i=0;i<monthname.size();i++){
                    if (monthname.get(i).getId().substring(0,2).equals(date)){
                        setMonth(monthname.get(i));
                        break;
                    }
                }




                progress1.dismiss();
                if(resultlistner!=null){
                    resultlistner.onSuccess(mTeamMonthDivision);

                }


            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                //CboServices.getAlert(context,"Missing field error",(R.string.service_unavilable) +e.toString());
                resultlistner.onError("Missing field error", e.toString());
                e.printStackTrace();

            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }

    private ArrayList<mMissedFilter> getMissed_call_Filter(mUser user){
        ArrayList<mMissedFilter> missedFiletrs = new ArrayList<>();

        for (mMissedFilter missedFiletr : mTeamMonthDivision.getMissed_call_Filter()) {
            if (missedFiletr.getDESIG_ID()>= user.getDESIG_ID())
                missedFiletrs.add(missedFiletr);
        }
        return missedFiletrs;
    }

    public void onMissedTypeClicked(Context context){
        new Spinner_Dialog(context,getMissed_call_Filter(getUser()), item -> setMissedFilter((mMissedFilter) item)).show();
    }

    public void onClickName(Context context){

        new Spinner_Dialog(context,mTeamMonthDivision.getUsers(), item -> setUser((mUser) item)).show();
    }

    public void onClickMonth(Context  context){

        new Spinner_Dialog(context,mTeamMonthDivision.getMonths(), item -> setMonth((mMonth) item)).show();
    }


}
