package com.cbo.cbomobilereporting.ui_new.approval_activities.Remainder;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import services.CboServices;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by cboios on 20/11/18.
 */

public class VMApprovalRemainder extends ViewModel {

    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;
    CBO_DB_Helper cboDbHelper;
    private Custom_Variables_And_Method customVariablesAndMethod;



    private IApprovalRemainder listner = null;


    ArrayList<mApprovalRemainder> mApprovalRemainders;

    public  ArrayList<mApprovalRemainder> getApprovalRemainders() {
        return mApprovalRemainders;
    }

    public VMApprovalRemainder() {
        super();
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
        cboDbHelper = new CBO_DB_Helper(MyCustumApplication.getInstance());
    }

    public void setListener(IApprovalRemainder listener){
        this.listner = listener;
    }

    void getApprovalRemainders(Context context) {

        progress1 = new ProgressDialog(context);


        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cboDbHelper.getCompanyCode());
        request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
        request.put("FMCYN", "");

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);
        tables.add(1);

        progress1.setMessage("Please Wait..\n" +
                " Fetching data");
        progress1.setCancelable(false);

        if (context instanceof AppCompatActivity) {
            progress1.show();
        }


        new CboServices(context,mHandler).customMethodForAllServices(request,"APPROVAL_REMINDER",MESSAGE_INTERNET,tables);

        //End of call to service



    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    if ((null != msg.getData())) {
                        parser_ApprovalRemainder(msg.getData());
                    }
                    break;
                case 99:
                    progress1.dismiss();
                    listner.onError("Alert!!!", msg.getData().getString("Error"));
                    /*if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                    }*/
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };

    void parser_ApprovalRemainder(Bundle result ) {
        if (result!=null ) {

            {
                try {
                    mApprovalRemainders = new ArrayList<>();

                    String table0 = result.getString("Tables0");
                    JSONArray row1 = new JSONArray(table0);

                    String table1 = result.getString("Tables1");
                    JSONArray row2 = new JSONArray(table1);
                    String domain = "",params = "";
                    for (int i = 0; i < row2.length(); i++) {
                        JSONObject c = row2.getJSONObject(i);
                        domain = c.getString("DOMAIN");
                        params = c.getString("QUERY");
                    }

                    for (int i = 0; i < row1.length(); i++) {
                        Map<String,String> datanum=new HashMap<String,String>();
                        JSONObject c = row1.getJSONObject(i);
                        mApprovalRemainder approvalRemainder=new mApprovalRemainder();
                        //String id=c.getString("DR_ID");

                        //approvalRemainder.setID(c.getString("ID"));
                        approvalRemainder.setPARICULARS(c.getString("PARICULARS"));

                        approvalRemainder.setADD_VALUE(c.getString("ADD_VALUE"));
                        approvalRemainder.setADD_URL(c.getString("ADD_URL"));

                        approvalRemainder.setEDIT_VALUE(c.getString("EDIT_VALUE"));
                        approvalRemainder.setEDIT_URL(c.getString("EDIT_URL"));

                        approvalRemainder.setDELETE_VALUE(c.getString("DELETE_VALUE"));
                        approvalRemainder.setDELETE_URL(c.getString("DELETE_URL"));



                        mApprovalRemainder approvalRemainder_add= new mApprovalRemainder();
                        approvalRemainder_add.setADD_VALUE( c.getString("ADD_VALUE"));
                        approvalRemainder_add.setADD_URL(domain + c.getString("ADD_URL")  + params);
                        approvalRemainder_add.setPARICULARS( approvalRemainder.getPARICULARS());
                        if (!approvalRemainder.getADD_VALUE().equalsIgnoreCase("0") ) {
                            if (!approvalRemainder.getEDIT_VALUE().equalsIgnoreCase("0") &&
                                    !approvalRemainder.getDELETE_VALUE().equalsIgnoreCase("0")) {
                                approvalRemainder_add.setPARICULARS(approvalRemainder.getPARICULARS() + " (Add) ");
                            }
                            mApprovalRemainders.add(approvalRemainder_add);
                        }
                        mApprovalRemainder approvalRemainder_edit=new mApprovalRemainder();
                        if (!approvalRemainder.getEDIT_VALUE().equalsIgnoreCase("0") ){
                            approvalRemainder_edit.setPARICULARS( approvalRemainder.getPARICULARS() + " (Edit) ");
                            approvalRemainder_edit.setADD_VALUE(c.getString("EDIT_VALUE"));
                            approvalRemainder_edit.setADD_URL(domain + c.getString("EDIT_URL")  + params);
                            mApprovalRemainders.add(approvalRemainder_edit);
                        }

                        mApprovalRemainder approvalRemainder_delete= new mApprovalRemainder();
                        if (!approvalRemainder.getDELETE_VALUE().equalsIgnoreCase("0") ){
                            approvalRemainder_delete.setPARICULARS( approvalRemainder.getPARICULARS() + " (Delete) ");
                            approvalRemainder_delete.setADD_VALUE(c.getString("DELETE_VALUE"));
                            approvalRemainder_delete.setADD_URL(domain + c.getString("DELETE_URL")  + params);
                            mApprovalRemainders.add(approvalRemainder_delete);
                        }

                        //mApprovalRemainders.add(approvalRemainder);

                    }

                    if (listner != null)
                        listner.onListUpdated(mApprovalRemainders);
                }catch (Exception e){
                    if (listner != null)
                        listner.onError("Missing field error", e.toString());
                    /*Log.d("MYAPP", "objects are: " + e.toString());
                    CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                    e.printStackTrace();*/
                }
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        if (progress1 != null)
        progress1.dismiss();

    }
}
