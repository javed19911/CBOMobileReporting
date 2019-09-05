package com.cbo.cbomobilereporting.ui_new.dcr_activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.databaseHelper.Call.Db.MainDB;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import async.CBOFinalTask_New;
import async.CBOFinalTasks;
import locationpkg.Const;
import services.MyAPIService;
import utils_new.AppAlert;
import utils_new.CustomTextToSpeech;
import utils_new.Custom_Variables_And_Method;
import utils_new.GPS_Timmer_Dialog;
import utils_new.GetVersionCode;
import utils.networkUtil.AppPrefrences;
import utils.networkUtil.NetworkUtil;
import utils_new.Service_Call_From_Multiple_Classes;


public class FinalSubmitDcr_new extends CustomActivity {


    EditText remark, loc,Late_Submit_remark;
    NetworkUtil networkUtil;
    Button submit, show;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    LinearLayout final_layout;
    String dcr_id = "";
    String address = "";


    String DCR_ID = "", dr_id = "";
    CBO_DB_Helper cbohelp;
    String mRemark,back_allowed="Y";
    String DCR_REMARK_NA;

    Service_Call_From_Multiple_Classes service;
    MyCustomMethod customMethod;
    private Location currentBestLocation;


    private CBOFinalTask_New cboFinalTask_new;
    Map<String, String> dcr_latCommit = new HashMap<>();
    Map<String, String> dcr_Commititem = new HashMap<>();
    Map<String, String> dcr_Commit_rx = new HashMap<>();
    Map<String, String> dcr_CommitDr = new HashMap<>();
    Map<String, String> dcr_ChemistCommit = new HashMap<>();
    Map<String, String> dcr_StkCommit = new HashMap<>();
    Map<String, String> dcr_CommitDr_Reminder = new HashMap<>();
    Map<String, String> Lat_Long_Reg = new HashMap<>();
    Map<String, String> dcr_Dairy = new HashMap<>();
    Map<String, String> dcr_RxCalls= new HashMap<>();
    Map<String, String> dcr_CommitChem_Reminder = new HashMap<>();

    String sb_DCRLATCOMMIT_KM, sb_DCRLATCOMMIT_LOC_LAT, sb_sDCRLATCOMMIT_IN_TIME, sDCRLATCOMMIT_ID, sDCRLATCOMMIT_LOC;
    String sDCRITEM_DR_ID, sDCRITEM_ITEMIDIN, sDCRITEM_ITEM_ID_ARR, sDCRITEM_QTY_ARR, sDCRITEM_ITEM_ID_GIFT_ARR, sDCRITEM_QTY_GIFT_ARR, sDCRITEM_POB_QTY, sDCRITEM_POB_VALUE, sDCRITEM_VISUAL_ARR,sDCRITEM_NOC_ARR;
    String sDCRDR_DR_ID, sDCRDR_WW1, sDCRDR_WW2, sDCRDR_WW3, sDCRDR_LOC, sDCRDR_IN_TIME, sDCRDR_BATTERY_PERCENT, sDCRDR_REMARK, sDCRDR_KM, sDCRDR_SRNO,sDCRDR_FILE,sDCRDR_CALLTYPE,sDR_REF_LAT_LONG;
    String sDCRCHEM_CHEM_ID, sDCRCHEM_POB_QTY, sDCRCHEM_POB_AMT, sDCRCHEM_ITEM_ID_ARR, sDCRCHEM_QTY_ARR, sDCRCHEM_LOC, sDCRCHEM_IN_TIME, sDCRCHEM_SQTY_ARR, sDCRCHEM_ITEM_ID_GIFT_ARR, sDCRCHEM_QTY_GIFT_ARR, sDCRCHEM_BATTERY_PERCENT, sDCRCHEM_KM, sDCRCHEM_SRNO,sDCRCHEM_REMARK,sDCRCHEM_FILE,sCHEM_REF_LAT_LONG,sCHEM_STATUS,sCOMPETITOR_REMARK;
    String sDCRSTK_STK_ID, sDCRSTK_POB_QTY, sDCRSTK_POB_AMT, sDCRSTK_ITEM_ID_ARR, sDCRSTK_QTY_ARR, sDCRSTK_LOC, sDCRSTK_IN_TIME, sDCRSTK_SQTY_ARR, sDCRSTK_ITEM_ID_GIFT_ARR, sDCRSTK_QTY_GIFT_ARR, sDCRSTK_BATTERY_PERCENT, sDCRSTK_KM, sDCRSTK_SRNO,sDCRSTK_REMARK,sDCRSTK_FILE,sSTK_REF_LAT_LONG;
    String sDCRRC_IN_TIME, sDCRRC_LOC, sDCRRC_DR_ID, sDCRRC_KM, sDCRRC_SRNO,sDCRRC_BATTERY_PERCENT,sDCRRC_REMARK,sDCRRC_FILE,sRC_REF_LAT_LONG;
    String sDCR_DR_RX, sDCR_ITM_RX;
    String sFinalKm;
    String DCS_ID_ARR, LAT_LONG_ARR, DCS_TYPE_ARR, DCS_ADD_ARR, DCS_INDES_ARR;

    String DCRSTK_RATE, DCRDR_RATE, DCRCHEM_RATE;


    String sDAIRY_ID, sSTRDAIRY_CPID,sDCRDAIRY_LOC,sDCRDAIRY_IN_TIME,sDCRDAIRY_BATTERY_PERCENT,sDCRDAIRY_REMARK,sDCRDAIRY_KM,sDCRDAIRY_SRNO,sDAIRY_REF_LAT_LONG;
    String sDCRDAIRYITEM_DAIRY_ID,sDCRDAIRYITEM_ITEM_ID_ARR,sDCRDAIRYITEM_QTY_ARR,sDCRDAIRYITEM_ITEM_ID_GIFT_ARR,sDCRDAIRYITEM_QTY_GIFT_ARR;
    String sDCRDAIRYITEM_POB_QTY,sDAIRY_FILE,sDCRDAIRY_INTERSETEDYN;

    String aDCRDRRX_DOC_DATE,aDCRDRRX_DR_ID,aDCRDRRX_ITEM_ID,aDCRDRRX_QTY,aDCRDRRX_AMOUNT;

    String sDCRRC_CHEM_ID,sDCRRC_LOC_CHEM,sDCRRC_IN_TIME_CHEM,sDCRRC_KM_CHEM,sDCRRC_SRNO_CHEM;
    String sDCRRC_BATTERY_PERCENT_CHEM,sDCRRC_REMARK_CHEM,sDCRRC_FILE_CHEM,sRC_REF_LAT_LONG_CHEM;

    public AppPrefrences appPrefrences;

    private static final int MESSAGE_INTERNET_FINAL_SUBMIT=0;
    private  static final int GPS_TIMMER=4;

    ProgressDialog commitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.final_submit);



        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);

        TextView Dcr_date = (TextView) findViewById(R.id.DCR_Date);
        TextView Work_type = (TextView) findViewById(R.id.working_type);

        hader_text.setText("Final Submit");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        Intent intent=getIntent();
        if (intent.getStringExtra("Back_allowed")!=null){
            back_allowed=intent.getStringExtra("Back_allowed");
        }

        remark = (EditText) findViewById(R.id.final_remark);
        Late_Submit_remark = (EditText) findViewById(R.id.Late_Submit_remark);
        loc = (EditText) findViewById(R.id.loc_final);
        submit = (Button) findViewById(R.id.save_final);
        show = (Button) findViewById(R.id.show);
        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        networkUtil = new NetworkUtil(this);
        cboFinalTask_new = new CBOFinalTask_New(context);

        //get batterry level
        customVariablesAndMethod.getbattrypercentage(context);

        dcr_id = dcr_id + Custom_Variables_And_Method.DCR_ID;
        final_layout = (LinearLayout) findViewById(R.id.final_layout);
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"MethodCallFinal", "Y");
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"final_km", "0");
        Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);

        cbohelp = new CBO_DB_Helper(context);
        service = new Service_Call_From_Multiple_Classes();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        DCR_REMARK_NA = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_REMARK_NA");

        Dcr_date.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DATE_NAME"));
        Work_type.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_head"));
        //Toast.makeText(this,"javed "+DCR_REMARK_NA,Toast.LENGTH_LONG).show();
        mRemark = remark.getText().toString();
        if (DCR_REMARK_NA.toLowerCase().equals("y")){
            remark.setVisibility(View.INVISIBLE);
            mRemark="Remark Not Required";
        }

        if(!customVariablesAndMethod.IsCallAllowedToday(context)){
            Late_Submit_remark.setVisibility(View.GONE);
        }else {
            Late_Submit_remark.setVisibility(View.GONE);
        }


        customMethod = new MyCustomMethod(FinalSubmitDcr_new.this);
        appPrefrences = new AppPrefrences(FinalSubmitDcr_new.this);
        loc.setText(""+Custom_Variables_And_Method.GLOBAL_LATLON);

        switch (Custom_Variables_And_Method.location_required) {
            case "N":
                final_layout.setVisibility(View.GONE);
                break;
            case "Y":
                final_layout.setVisibility(View.VISIBLE);
                break;
            default:
                final_layout.setVisibility(View.GONE);
                break;
        }


        submit.setOnClickListener(new View.OnClickListener() {


            //@TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!networkUtil.internetConneted(FinalSubmitDcr_new.this)) {
                    customVariablesAndMethod.Connect_to_Internet_Msg(context);

                } else {

                    if (Custom_Variables_And_Method.GLOBAL_LATLON.equalsIgnoreCase("0.0,0.0")) {

                        Custom_Variables_And_Method.GLOBAL_LATLON =  customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
                    }
                    setAddressToUI();


                    if (loc.getText().toString().equals("")) {
                        loc.setText("UnKnown Location");
                    }
                    address = loc.getText().toString();

                    if (DCR_REMARK_NA.toLowerCase().equals("y")) {
                        mRemark = "Remark Not Required";
                    } else {
                        mRemark = remark.getText().toString();
                    }

                    if (mRemark.equals("")) {
                        customVariablesAndMethod.msgBox(context,"Enter Remark....");
                    }/*else if(customVariablesAndMethod.IsCallAllowedToday(context) && Late_Submit_remark.equals("") ){
                        customVariablesAndMethod.msgBox(context,"Enter Late Submit Remark....");
                    }*/else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false)) {
                        customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                        LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                                new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                    } else {

                        new GPS_Timmer_Dialog(context,mHandler,"Final Submit in Process...",GPS_TIMMER).show();

                    }

                }
            }
        });


    }

    private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contex, Intent intent) {
            Location location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);
            new GPS_Timmer_Dialog(context,mHandler,"Final Submit in Process...",GPS_TIMMER).show();

        }
    };

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_FINAL_SUBMIT:
                    //parser6(msg.getData());
                    break;
                case GPS_TIMMER:
                    //finalSubmit();
                    finalSubmitNew();
                    /*Thread thread=new Thread(){
                        @Override
                        public void run() {
                            try{


                                sleep(10);

                            }
                            catch (InterruptedException e){
                                e.printStackTrace();
                            }  finally {

                                finalSubmitNew();
                            }

                        }
                    };
                    thread.start();*/
                    break;
                case 99:
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    commitDialog.dismiss();
                    break;


            }
        }
    };


    private void finalSubmitNew(){


        ///disable submitbtn to be clicked twice
        submit.setEnabled(false);





        String fmcg_Live_Km = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"live_km");

        String routeValue;
        routeValue = appPrefrences.getRouteValue(context);

        currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation",Location.class);

        String locExtra="";

        if (currentBestLocation!=null) {
            locExtra = "Lat_Long " + currentBestLocation.getLatitude() + "," + currentBestLocation.getLongitude() + ", Accuracy " + currentBestLocation.getAccuracy() + ", Time " + currentBestLocation.getTime() + ", Speed " + currentBestLocation.getSpeed() + ", Provider " + currentBestLocation.getProvider();
        }


        if (fmcg_Live_Km.equalsIgnoreCase("Y") || fmcg_Live_Km.equalsIgnoreCase("5") || fmcg_Live_Km.equalsIgnoreCase("Y5")) {
            customMethod.stopAlarm10Sec();
            customMethod.stopAlarm10Minute();
            customMethod.backgroundData();
            dcr_latCommit = customMethod.dataToServer(null);

        }

        new CustomTextToSpeech().stopTextToSpeech();

        if ((dcr_latCommit.isEmpty()) || (dcr_latCommit.size() == 0)) {

            sb_DCRLATCOMMIT_KM = "";
            sb_DCRLATCOMMIT_LOC_LAT = "";
            sb_sDCRLATCOMMIT_IN_TIME = "";
            sDCRLATCOMMIT_ID = "";
            sDCRLATCOMMIT_LOC = "";
        } else {

            sb_DCRLATCOMMIT_KM = dcr_latCommit.get("sb_DCRLATCOMMIT_KM");
            sb_DCRLATCOMMIT_LOC_LAT = dcr_latCommit.get("sb_DCRLATCOMMIT_LOC_LAT");
            sb_sDCRLATCOMMIT_IN_TIME = dcr_latCommit.get("sb_sDCRLATCOMMIT_IN_TIME");
            sDCRLATCOMMIT_ID = dcr_latCommit.get("sDCRLATCOMMIT_ID");
            sDCRLATCOMMIT_LOC = dcr_latCommit.get("sDCRLATCOMMIT_LOC");
        }

        dcr_Commit_rx = cboFinalTask_new.drRx_Save(null);
        if ((dcr_Commit_rx.isEmpty()) || (dcr_Commit_rx.size() == 0)) {

            sDCR_DR_RX = "";
            sDCR_ITM_RX = "";


        } else {

            sDCR_DR_RX = dcr_Commit_rx.get("sDCRRX_DR_ARR");
            sDCR_ITM_RX = dcr_Commit_rx.get("sDCRRX_ITEMID_ARR");


        }


        dcr_Commititem = cboFinalTask_new.drItemSave(null);
        if ((dcr_Commititem.isEmpty()) || (dcr_Commititem.size() == 0)) {

            sDCRITEM_DR_ID = "";
            sDCRITEM_ITEMIDIN = "";
            sDCRITEM_ITEM_ID_ARR = "";
            sDCRITEM_QTY_ARR = "";
            sDCRITEM_ITEM_ID_GIFT_ARR = "";
            sDCRITEM_QTY_GIFT_ARR = "";
            sDCRITEM_POB_QTY = "";
            sDCRITEM_POB_VALUE = "";
            sDCRITEM_VISUAL_ARR = "";
            sDCRITEM_NOC_ARR = "";
            DCRDR_RATE = "";

        } else {
            sDCRITEM_DR_ID = dcr_Commititem.get("sb_sDCRITEM_DR_ID");
            sDCRITEM_ITEMIDIN = dcr_Commititem.get("sb_sDCRITEM_ITEMIDIN");
            sDCRITEM_ITEM_ID_ARR = dcr_Commititem.get("sb_sDCRITEM_ITEM_ID_ARR");
            sDCRITEM_QTY_ARR = dcr_Commititem.get("sb_sDCRITEM_QTY_ARR");
            sDCRITEM_ITEM_ID_GIFT_ARR = dcr_Commititem.get("sb_sDCRITEM_ITEM_ID_GIFT_ARR");
            sDCRITEM_QTY_GIFT_ARR = dcr_Commititem.get("sb_sDCRITEM_QTY_GIFT_ARR");
            sDCRITEM_POB_QTY = dcr_Commititem.get("sb_sDCRITEM_POB_QTY");
            sDCRITEM_POB_VALUE = dcr_Commititem.get("sb_sDCRITEM_POB_VALUE");
            sDCRITEM_VISUAL_ARR = dcr_Commititem.get("sb_sDCRITEM_VISUAL_ARR");
            sDCRITEM_NOC_ARR = dcr_Commititem.get("sb_sDCRITEM_NOC_ARR");
            DCRDR_RATE = dcr_Commititem.get("sb_DCRDR_RATE");

        }
        dcr_CommitDr = cboFinalTask_new.dcr_doctorSave(null);
        if ((dcr_CommitDr.isEmpty()) || (dcr_CommitDr.size() == 0)) {
            sDCRDR_DR_ID = "";
            sDCRDR_WW1 = "";
            sDCRDR_WW2 = "";
            sDCRDR_WW3 = "";
            sDCRDR_LOC = "";
            sDCRDR_IN_TIME = "";
            sDCRDR_BATTERY_PERCENT = "";
            sDCRDR_REMARK = "";
            sDCRDR_KM = "";
            sDCRDR_SRNO="";
            sDCRDR_FILE="";
            sDCRDR_CALLTYPE="";
            sDR_REF_LAT_LONG = "";
        } else {
            sDCRDR_DR_ID = dcr_CommitDr.get("sb_sDCRDR_DR_ID");
            sDCRDR_WW1 = dcr_CommitDr.get("sb_sDCRDR_WW1");
            sDCRDR_WW2 = dcr_CommitDr.get("sb_sDCRDR_WW2");
            sDCRDR_WW3 = dcr_CommitDr.get("sb_sDCRDR_WW3");
            sDCRDR_LOC = dcr_CommitDr.get("sb_sDCRDR_LOC");
            sDCRDR_IN_TIME = dcr_CommitDr.get("sb_sDCRDR_IN_TIME");
            sDCRDR_BATTERY_PERCENT = dcr_CommitDr.get("sb_sDCRDR_BATTERY_PERCENT");
            sDCRDR_REMARK = dcr_CommitDr.get("sb_sDCRDR_Remark");
            sDCRDR_KM = dcr_CommitDr.get("sb_sDCRDR_KM");
            sDCRDR_SRNO=dcr_CommitDr.get("sb_sDCRDR_SRNO");
            sDCRDR_FILE=dcr_CommitDr.get("sb_sDCRDR_FILE");
            sDCRDR_CALLTYPE=dcr_CommitDr.get("sb_sDCRDR_CALLTYPE");
            sDR_REF_LAT_LONG = dcr_CommitDr.get("sb_sDR_REF_LAT_LONG");
        }

        dcr_ChemistCommit = cboFinalTask_new.dcr_chemSave(null);
        if ((dcr_ChemistCommit.isEmpty()) || (dcr_ChemistCommit.size() == 0)) {
            sDCRCHEM_CHEM_ID = "";
            sDCRCHEM_POB_QTY = "";
            sDCRCHEM_POB_AMT = "";
            sDCRCHEM_ITEM_ID_ARR = "";
            sDCRCHEM_QTY_ARR = "";
            sDCRCHEM_LOC = "";
            sDCRCHEM_IN_TIME = "";
            sDCRCHEM_SQTY_ARR = "";
            sDCRCHEM_ITEM_ID_GIFT_ARR = "";
            sDCRCHEM_QTY_GIFT_ARR = "";
            sDCRCHEM_BATTERY_PERCENT = "";
            sDCRCHEM_KM = "";
            sDCRCHEM_SRNO="";
            sDCRCHEM_REMARK="";
            sDCRCHEM_FILE="";
            sCHEM_REF_LAT_LONG = "";
            DCRCHEM_RATE = "";

            sCHEM_STATUS="";
            sCOMPETITOR_REMARK="";
        } else {
            sDCRCHEM_CHEM_ID = dcr_ChemistCommit.get("sb_sDCRCHEM_CHEM_ID");
            sDCRCHEM_POB_QTY = dcr_ChemistCommit.get("sb_sDCRCHEM_POB_QTY");
            sDCRCHEM_POB_AMT = dcr_ChemistCommit.get("sb_sDCRCHEM_POB_AMT");
            sDCRCHEM_ITEM_ID_ARR = dcr_ChemistCommit.get("sb_sDCRCHEM_ITEM_ID_ARR");
            sDCRCHEM_QTY_ARR = dcr_ChemistCommit.get("sb_sDCRCHEM_QTY_ARR");
            sDCRCHEM_LOC = dcr_ChemistCommit.get("sb_sDCRCHEM_LOC");
            sDCRCHEM_IN_TIME = dcr_ChemistCommit.get("sb_sDCRCHEM_IN_TIME");
            sDCRCHEM_SQTY_ARR = dcr_ChemistCommit.get("sb_sDCRCHEM_SQTY_ARR");
            sDCRCHEM_ITEM_ID_GIFT_ARR = dcr_ChemistCommit.get("sb_sDCRCHEM_ITEM_ID_GIFT_ARR");
            sDCRCHEM_QTY_GIFT_ARR = dcr_ChemistCommit.get("sb_sDCRCHEM_QTY_GIFT_ARR");
            sDCRCHEM_BATTERY_PERCENT = dcr_ChemistCommit.get("sb_sDCRCHEM_BATTERY_PERCENT");
            sDCRCHEM_KM = dcr_ChemistCommit.get("sb_sDCRCHEM_KM");
            sDCRCHEM_SRNO=dcr_ChemistCommit.get("sb_sDCRCHEM_SRNO");
            sDCRCHEM_REMARK= dcr_ChemistCommit.get("sb_sDCRCHEM_REMARK");
            sDCRCHEM_FILE= dcr_ChemistCommit.get("sb_sDCRCHEM_FILE");
            sCHEM_REF_LAT_LONG = dcr_ChemistCommit.get("sb_sCHEM_REF_LAT_LONG");
            DCRCHEM_RATE = dcr_ChemistCommit.get("sb_DCRCHEM_RATE");


            sCHEM_STATUS= dcr_ChemistCommit.get("sCHEM_STATUS");
            sCOMPETITOR_REMARK= dcr_ChemistCommit.get("sCOMPETITOR_REMARK");
        }


        dcr_StkCommit = cboFinalTask_new.dcr_stkSave(null);
        if ((dcr_StkCommit.isEmpty()) || (dcr_StkCommit.size() == 0)) {

            sDCRSTK_STK_ID = "";

            sDCRSTK_POB_QTY = "";
            sDCRSTK_POB_AMT = "";
            sDCRSTK_ITEM_ID_ARR = "";
            sDCRSTK_QTY_ARR = "";
            sDCRSTK_LOC = "";
            sDCRSTK_IN_TIME = "";
            sDCRSTK_SQTY_ARR = "";
            sDCRSTK_ITEM_ID_GIFT_ARR = "";
            sDCRSTK_QTY_GIFT_ARR = "";
            sDCRSTK_BATTERY_PERCENT = "";
            sDCRSTK_KM = "";
            sDCRSTK_SRNO = "";
            sDCRSTK_REMARK="";
            sDCRSTK_FILE="";
            sSTK_REF_LAT_LONG = "";
            DCRSTK_RATE = "";

        } else {
            sDCRSTK_STK_ID = dcr_StkCommit.get("sb_sDCRSTK_STK_ID");
            sDCRSTK_POB_QTY = dcr_StkCommit.get("sb_sDCRSTK_POB_QTY");
            sDCRSTK_POB_AMT = dcr_StkCommit.get("sb_sDCRSTK_POB_AMT");
            sDCRSTK_ITEM_ID_ARR = dcr_StkCommit.get("sb_sDCRSTK_ITEM_ID_ARR");
            sDCRSTK_QTY_ARR = dcr_StkCommit.get("sb_sDCRSTK_QTY_ARR");
            sDCRSTK_LOC = dcr_StkCommit.get("sb_sDCRSTK_LOC");
            sDCRSTK_IN_TIME = dcr_StkCommit.get("sb_sDCRSTK_IN_TIME");
            sDCRSTK_SQTY_ARR = dcr_StkCommit.get("sb_sDCRSTK_SQTY_ARR");
            sDCRSTK_ITEM_ID_GIFT_ARR = dcr_StkCommit.get("sb_sDCRSTK_ITEM_ID_GIFT_ARR");
            sDCRSTK_QTY_GIFT_ARR = dcr_StkCommit.get("sb_sDCRSTK_QTY_GIFT_ARR");
            sDCRSTK_BATTERY_PERCENT = dcr_StkCommit.get("sb_sDCRSTK_BATTERY_PERCENT");
            sDCRSTK_KM = dcr_StkCommit.get("sb_sDCRSTK_KM");
            sDCRSTK_SRNO = dcr_StkCommit.get("sb_sDCRSTK_SRNO");
            sDCRSTK_REMARK= dcr_StkCommit.get("sb_sDCRSTK_REMARK");
            sDCRSTK_FILE= dcr_StkCommit.get("sb_sDCRSTK_FILE");
            sSTK_REF_LAT_LONG = dcr_StkCommit.get("sb_sSTK_REF_LAT_LONG");
            DCRSTK_RATE = dcr_StkCommit.get("sb_DCRSTK_RATE");
        }


        dcr_CommitDr_Reminder = cboFinalTask_new.dcr_DrReminder(null);
        if ((dcr_CommitDr_Reminder.isEmpty()) || (dcr_CommitDr_Reminder.size() == 0)) {

            sDCRRC_IN_TIME = "";
            sDCRRC_LOC = "";
            sDCRRC_DR_ID = "";
            sDCRRC_KM = "";
            sDCRRC_SRNO = "";
            sDCRRC_BATTERY_PERCENT="";
            sDCRRC_REMARK="";
            sDCRRC_FILE="";
            sRC_REF_LAT_LONG = "";
        } else {

            sDCRRC_DR_ID = dcr_CommitDr_Reminder.get("sb_sDCRRC_DR_ID");
            sDCRRC_LOC = dcr_CommitDr_Reminder.get("sb_sDCRRC_LOC");
            sDCRRC_IN_TIME = dcr_CommitDr_Reminder.get("sb_sDCRRC_IN_TIME");
            sDCRRC_KM = dcr_CommitDr_Reminder.get("sb_sDCRRC_KM");
            sDCRRC_SRNO = dcr_CommitDr_Reminder.get("sb_sDCRRC_SRNO");
            sDCRRC_BATTERY_PERCENT = dcr_CommitDr_Reminder.get("sb_sDCRRC_BATTERY_PERCENT");
            sDCRRC_REMARK=dcr_CommitDr_Reminder.get("sb_sDCRRC_REMARK");
            sDCRRC_FILE=dcr_CommitDr_Reminder.get("sb_sDCRRC_FILE");
            sRC_REF_LAT_LONG = dcr_CommitDr_Reminder.get("sb_sRC_REF_LAT_LONG");
        }

        Lat_Long_Reg = cboFinalTask_new.get_Lat_Long_Reg("0");
        if ((Lat_Long_Reg.isEmpty()) || (Lat_Long_Reg.size() == 0)) {

            DCS_ID_ARR = "";
            LAT_LONG_ARR = "";
            DCS_TYPE_ARR = "";
            DCS_ADD_ARR = "";
            DCS_INDES_ARR = "";
        } else {

            DCS_ID_ARR = Lat_Long_Reg.get("DCS_ID_ARR");
            LAT_LONG_ARR = Lat_Long_Reg.get("LAT_LONG_ARR");
            DCS_TYPE_ARR = Lat_Long_Reg.get("DCS_TYPE_ARR");
            DCS_ADD_ARR = Lat_Long_Reg.get("DCS_ADD_ARR");
            DCS_INDES_ARR = Lat_Long_Reg.get("DCS_INDES_ARR");
        }




        dcr_Dairy = cboFinalTask_new.get_phdairy_dcr(null);


        if ((dcr_Dairy.isEmpty()) || (dcr_Dairy.size() == 0)) {

            sDAIRY_ID= "";
            sSTRDAIRY_CPID ="";
            sDCRDAIRY_LOC = "";
            sDCRDAIRY_IN_TIME= "";
            sDCRDAIRY_BATTERY_PERCENT= "";
            sDCRDAIRY_REMARK= "";
            sDCRDAIRY_KM= "";
            sDCRDAIRY_SRNO= "";
            sDCRDAIRYITEM_DAIRY_ID= "";
            sDCRDAIRYITEM_ITEM_ID_ARR= "";
            sDCRDAIRYITEM_QTY_ARR= "";
            sDCRDAIRYITEM_ITEM_ID_GIFT_ARR= "";
            sDCRDAIRYITEM_QTY_GIFT_ARR= "";
            sDCRDAIRYITEM_POB_QTY= "";
            sDAIRY_FILE= "";
            sDCRDAIRY_INTERSETEDYN = "";
            sDAIRY_REF_LAT_LONG = "";
        } else {

            sDAIRY_ID = dcr_Dairy.get("sDAIRY_ID");
            sSTRDAIRY_CPID  = dcr_Dairy.get("sSTRDAIRY_CPID");
            sDCRDAIRY_LOC  = dcr_Dairy.get("sDCRDAIRY_LOC");
            sDCRDAIRY_IN_TIME = dcr_Dairy.get("sDCRDAIRY_IN_TIME");
            sDCRDAIRY_BATTERY_PERCENT = dcr_Dairy.get("sDCRDAIRY_BATTERY_PERCENT");
            sDCRDAIRY_REMARK = dcr_Dairy.get("sDCRDAIRY_REMARK");
            sDCRDAIRY_KM = dcr_Dairy.get("sDCRDAIRY_KM");
            sDCRDAIRY_SRNO = dcr_Dairy.get("sDCRDAIRY_SRNO");
            sDCRDAIRYITEM_DAIRY_ID = dcr_Dairy.get("sDAIRY_ID");
            sDCRDAIRYITEM_ITEM_ID_ARR = dcr_Dairy.get("sDCRDAIRYITEM_ITEM_ID_ARR");
            sDCRDAIRYITEM_QTY_ARR = dcr_Dairy.get("sDCRDAIRYITEM_QTY_ARR");
            sDCRDAIRYITEM_ITEM_ID_GIFT_ARR = dcr_Dairy.get("sDCRDAIRYITEM_ITEM_ID_GIFT_ARR");
            sDCRDAIRYITEM_QTY_GIFT_ARR = dcr_Dairy.get("sDCRDAIRYITEM_QTY_GIFT_ARR");
            sDCRDAIRYITEM_POB_QTY = dcr_Dairy.get("sDCRDAIRYITEM_POB_QTY");
            sDAIRY_FILE = dcr_Dairy.get("sDAIRY_FILE");
            sDCRDAIRY_INTERSETEDYN = dcr_Dairy.get("sDCRDAIRY_INTERSETEDYN");
            sDAIRY_REF_LAT_LONG = dcr_Dairy.get("sDAIRY_REF_LAT_LONG");
        }



        dcr_RxCalls = cboFinalTask_new.get_dcr_RxCalls(null);


        if ((dcr_RxCalls.isEmpty()) || (dcr_RxCalls.size() == 0)) {

            aDCRDRRX_DOC_DATE= "";
            aDCRDRRX_DR_ID ="";
            aDCRDRRX_ITEM_ID = "";
            aDCRDRRX_QTY= "";
            aDCRDRRX_AMOUNT= "";

        } else {

            aDCRDRRX_DOC_DATE= dcr_RxCalls.get("aDCRDRRX_DOC_DATE");
            aDCRDRRX_DR_ID =dcr_RxCalls.get("aDCRDRRX_DR_ID");
            aDCRDRRX_ITEM_ID = dcr_RxCalls.get("aDCRDRRX_ITEM_ID");
            aDCRDRRX_QTY= dcr_RxCalls.get("aDCRDRRX_QTY");
            aDCRDRRX_AMOUNT= dcr_RxCalls.get("aDCRDRRX_AMOUNT");
        }



        dcr_CommitChem_Reminder = cboFinalTask_new.dcr_ChemReminder(null);
        if ((dcr_CommitChem_Reminder.isEmpty()) || (dcr_CommitChem_Reminder.size() == 0)) {

            sDCRRC_CHEM_ID = "";
            sDCRRC_LOC_CHEM = "";
            sDCRRC_IN_TIME_CHEM = "";
            sDCRRC_KM_CHEM = "";
            sDCRRC_SRNO_CHEM = "";
            sDCRRC_BATTERY_PERCENT_CHEM="";
            sDCRRC_REMARK_CHEM="";
            sDCRRC_FILE_CHEM="";
            sRC_REF_LAT_LONG_CHEM = "";
        } else {

            sDCRRC_CHEM_ID = dcr_CommitChem_Reminder.get("sb_sDCRRC_CHEM_ID");
            sDCRRC_LOC_CHEM = dcr_CommitChem_Reminder.get("sb_sDCRRC_LOC_CHEM");
            sDCRRC_IN_TIME_CHEM = dcr_CommitChem_Reminder.get("sb_sDCRRC_IN_TIME_CHEM");
            sDCRRC_KM_CHEM = dcr_CommitChem_Reminder.get("sb_sDCRRC_KM_CHEM");
            sDCRRC_SRNO_CHEM = dcr_CommitChem_Reminder.get("sb_sDCRRC_SRNO_CHEM");
            sDCRRC_BATTERY_PERCENT_CHEM = dcr_CommitChem_Reminder.get("sb_sDCRRC_BATTERY_PERCENT_CHEM");
            sDCRRC_REMARK_CHEM =dcr_CommitChem_Reminder.get("sb_sDCRRC_REMARK_CHEM");
            sDCRRC_FILE_CHEM =dcr_CommitChem_Reminder.get("sb_sDCRRC_FILE_CHEM");
            sRC_REF_LAT_LONG_CHEM = dcr_CommitChem_Reminder.get("sb_sRC_REF_LAT_LONG_CHEM");
        }


        //customMethod.getDataFromFromAllTables();
        //sFinalKm = mycon.getDataFrom_FMCG_PREFRENCE("final_km");
        // ArrayList<String> array=customMethod.kmWithWayPoint();
        sFinalKm ="0"; // array.get(0);
        String sAPI_Pattern="0";  // array.get(1);

        if (Custom_Variables_And_Method.DCR_ID.equals("0")) {

            Custom_Variables_And_Method.DCR_ID =customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_ID");
        }

        String ACTUALFARE=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"ACTUALFARE");
        if (ACTUALFARE.equals(""))
            ACTUALFARE=""+0;




        HashMap<String, String> request = new HashMap<>();

        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iDcrId", MyCustumApplication.getInstance().getUser().getDCRId());
        request.put("iNoChemist", "1");
        request.put("iNoStockist", "1");
        request.put("sChemistRemark", "");
        request.put("sStockistREmark", "");
        request.put("iPob", "0.0");
        request.put("iPobQty", "0");
        request.put("iActualFareAmt", ACTUALFARE);
        request.put("sDatype", "NA");
        request.put("iDistanceId", "99999");
        request.put("sRemark", remark.getText().toString());
        request.put("sLoc", Custom_Variables_And_Method.GLOBAL_LATLON +"@"+locExtra+ "!^" + address); //mLatLong +"@"+locExtra+ "!^" + mAddress
        request.put("iOuttime", "99");

        request.put("sRouteYn", routeValue);

        request.put("sDCRLATCOMMIT_ID", sDCRLATCOMMIT_ID);
        request.put("sDCRLATCOMMIT_IN_TIME", sb_sDCRLATCOMMIT_IN_TIME);
        request.put("sDCRLATCOMMIT_LOC_LAT", sb_DCRLATCOMMIT_LOC_LAT);
        request.put("sDCRLATCOMMIT_LOC", sDCRLATCOMMIT_LOC);
        request.put("sDCRLATCOMMIT_KM", sb_DCRLATCOMMIT_KM);

        request.put("sDCRITEM_DR_ID", sDCRITEM_DR_ID);
        request.put("sDCRITEM_ITEMIDIN", sDCRITEM_ITEMIDIN);
        request.put("sDCRITEM_ITEM_ID_ARR", sDCRITEM_ITEM_ID_ARR);
        request.put("sDCRITEM_QTY_ARR", sDCRITEM_QTY_ARR);
        request.put("sDCRITEM_ITEM_ID_GIFT_ARR", sDCRITEM_ITEM_ID_GIFT_ARR);
        request.put("sDCRITEM_QTY_GIFT_ARR", sDCRITEM_QTY_GIFT_ARR);

        request.put("sDCRITEM_POB_QTY", sDCRITEM_POB_QTY);
        request.put("sDCRITEM_POB_VALUE", sDCRITEM_POB_VALUE);
        request.put("sDCRITEM_VISUAL_ARR", sDCRITEM_VISUAL_ARR);
        request.put("sDCRITEM_NOC_ARR", sDCRITEM_NOC_ARR);

        request.put("sDCRDR_DR_ID", sDCRDR_DR_ID);
        request.put("sDCRDR_WW1", sDCRDR_WW1);
        request.put("sDCRDR_WW2", sDCRDR_WW2);
        request.put("sDCRDR_WW3", sDCRDR_WW3);
        request.put("sDCRDR_LOC", sDCRDR_LOC);
        request.put("sDCRDR_IN_TIME", sDCRDR_IN_TIME);
        request.put("sDCRDR_BATTERY_PERCENT", sDCRDR_BATTERY_PERCENT);
        request.put("sDCRDR_REMARK", sDCRDR_REMARK);
        request.put("sDCRDR_KM", sDCRDR_KM);
        request.put("sDCRDR_SRNO", sDCRDR_SRNO);
        request.put("sDCRDR_FILE", sDCRDR_FILE);
        request.put("sDCRDR_CALLTYPE", sDCRDR_CALLTYPE);

        request.put("sDCRCHEM_CHEM_ID", sDCRCHEM_CHEM_ID);
        request.put("sDCRCHEM_POB_QTY", sDCRCHEM_POB_QTY);
        request.put("sDCRCHEM_POB_AMT", sDCRCHEM_POB_AMT);
        request.put("sDCRCHEM_ITEM_ID_ARR", sDCRCHEM_ITEM_ID_ARR);
        request.put("sDCRCHEM_QTY_ARR", sDCRCHEM_QTY_ARR);
        request.put("sDCRCHEM_LOC", sDCRCHEM_LOC);
        request.put("sDCRCHEM_IN_TIME", sDCRCHEM_IN_TIME);
        request.put("sDCRCHEM_SQTY_ARR", sDCRCHEM_SQTY_ARR);
        request.put("sDCRCHEM_ITEM_ID_GIFT_ARR", sDCRCHEM_ITEM_ID_GIFT_ARR);
        request.put("sDCRCHEM_QTY_GIFT_ARR", sDCRCHEM_QTY_GIFT_ARR);
        request.put("sDCRCHEM_BATTERY_PERCENT", sDCRCHEM_BATTERY_PERCENT);
        request.put("sDCRCHEM_KM", sDCRCHEM_KM);
        request.put("sDCRCHEM_SRNO", sDCRCHEM_SRNO);
        request.put("sDCRCHEM_REMARK", sDCRCHEM_REMARK);
        request.put("sDCRCHEM_FILE", sDCRCHEM_FILE);


        request.put("sDCRSTK_STK_ID", sDCRSTK_STK_ID);
        request.put("sDCRSTK_POB_QTY", sDCRSTK_POB_QTY);
        request.put("sDCRSTK_POB_AMT", sDCRSTK_POB_AMT);
        request.put("sDCRSTK_ITEM_ID_ARR", sDCRSTK_ITEM_ID_ARR);
        request.put("sDCRSTK_QTY_ARR", sDCRSTK_QTY_ARR);
        request.put("sDCRSTK_LOC", sDCRSTK_LOC);
        request.put("sDCRSTK_IN_TIME", sDCRSTK_IN_TIME);
        request.put("sDCRSTK_SQTY_ARR", sDCRSTK_SQTY_ARR);
        request.put("sDCRSTK_ITEM_ID_GIFT_ARR", sDCRSTK_ITEM_ID_GIFT_ARR);
        request.put("sDCRSTK_QTY_GIFT_ARR", sDCRSTK_QTY_GIFT_ARR);
        request.put("sDCRSTK_BATTERY_PERCENT", sDCRSTK_BATTERY_PERCENT);
        request.put("sDCRSTK_KM", sDCRSTK_KM);
        request.put("sDCRSTK_SRNO", sDCRSTK_SRNO);
        request.put("sDCRSTK_REMARK", sDCRSTK_REMARK);
        request.put("sDCRSTK_FILE", sDCRSTK_FILE);

        request.put("sDCRRC_DR_ID", sDCRRC_DR_ID);
        request.put("sDCRRC_LOC", sDCRRC_LOC);
        request.put("sDCRRC_IN_TIME", sDCRRC_IN_TIME);
        request.put("sDCRRC_KM", sDCRRC_KM);
        request.put("sDCRRC_SRNO", sDCRRC_SRNO);
        request.put("sDCRRC_BATTERY_PERCENT", sDCRRC_BATTERY_PERCENT);
        request.put("sDCRRC_REMARK", sDCRRC_REMARK);
        request.put("sDCRRC_FILE", sDCRRC_FILE);



        request.put("sDCRRX_DR_ARR", sDCR_DR_RX);
        request.put("sDCRRX_ITEMID_ARR", sDCR_ITM_RX);

        request.put("iFinalKM", sFinalKm);
        request.put("iPaId",  MyCustumApplication.getInstance().getUser().getID());

        request.put("sGCM_TOKEN", MyCustumApplication.getInstance().getUser().getGCMToken());
        request.put("sAPI_PATTERN", sAPI_Pattern);
        request.put("sBATTERY_PERCENT", MyCustumApplication.getInstance().getUser().getBattery());

        request.put("REG_ID_ARR", DCS_ID_ARR);
        request.put("REG_LAT_LONG_ARR", LAT_LONG_ARR);
        request.put("REG_TYPE_ARR", DCS_TYPE_ARR);
        request.put("REG_ADD_ARR", DCS_ADD_ARR);
        request.put("REG_INDES_ARR", DCS_INDES_ARR);

        request.put("DCS_ID_ARR", DCS_ID_ARR);
        request.put("LAT_LONG_ARR", LAT_LONG_ARR);
        request.put("DCS_TYPE_ARR", DCS_TYPE_ARR);
        request.put("DCS_ADD_ARR", DCS_ADD_ARR);
        request.put("DCS_INDES_ARR", DCS_INDES_ARR);


        request.put("sDAIRY_ID", sDAIRY_ID);
        request.put("sSTRDAIRY_CPID", sSTRDAIRY_CPID);
        request.put("sDCRDAIRY_LOC", sDCRDAIRY_LOC);
        request.put("sDCRDAIRY_IN_TIME", sDCRDAIRY_IN_TIME);
        request.put("sDCRDAIRY_BATTERY_PERCENT", sDCRDAIRY_BATTERY_PERCENT);


        request.put("sDCRDAIRY_REMARK", sDCRDAIRY_REMARK);
        request.put("sDCRDAIRY_KM", sDCRDAIRY_KM);

        request.put("sDCRDAIRY_SRNO", sDCRDAIRY_SRNO);
        request.put("sDCRDAIRYITEM_DAIRY_ID", sDCRDAIRYITEM_DAIRY_ID);
        request.put("sDCRDAIRYITEM_ITEM_ID_ARR", sDCRDAIRYITEM_ITEM_ID_ARR);
        request.put("sDCRDAIRYITEM_QTY_ARR", sDCRDAIRYITEM_QTY_ARR);
        request.put("sDCRDAIRYITEM_ITEM_ID_GIFT_ARR", sDCRDAIRYITEM_ITEM_ID_GIFT_ARR);
        request.put("sDCRDAIRYITEM_QTY_GIFT_ARR", sDCRDAIRYITEM_QTY_GIFT_ARR);
        request.put("sDCRDAIRYITEM_POB_QTY", sDCRDAIRYITEM_POB_QTY);
        request.put("sDAIRY_FILE", sDAIRY_FILE);
        request.put("sDCRDAIRY_INTERSETEDYN", sDCRDAIRY_INTERSETEDYN);


        request.put("SDCR_DATE", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DATE"));
        request.put("sDR_REF_LAT_LONG", sDR_REF_LAT_LONG);
        request.put("sCHEM_REF_LAT_LONG", sCHEM_REF_LAT_LONG);
        request.put("sSTK_REF_LAT_LONG", sSTK_REF_LAT_LONG);
        request.put("sDAIRY_REF_LAT_LONG", sDAIRY_REF_LAT_LONG);
        request.put("sRC_REF_LAT_LONG", sRC_REF_LAT_LONG);

        request.put("DCRSTK_RATE", DCRSTK_RATE);
        request.put("DCRDR_RATE", DCRDR_RATE);
        request.put("DCRCHEM_RATE", DCRCHEM_RATE);

        request.put("sCHEM_STATUS", sCHEM_STATUS);
        request.put("sCOMPETITOR_REMARK", sCOMPETITOR_REMARK);

        request.put("aDCRDRRX_DOC_DATE", aDCRDRRX_DOC_DATE);
        request.put("aDCRDRRX_DR_ID", aDCRDRRX_DR_ID);
        request.put("aDCRDRRX_ITEM_ID", aDCRDRRX_ITEM_ID);
        request.put("aDCRDRRX_QTY", aDCRDRRX_QTY);
        request.put("aDCRDRRX_AMOUNT", aDCRDRRX_AMOUNT);
        request.put("ISSUPPORTLOGIN", MyCustumApplication.getInstance().getUser().getLoggedInAsSupport()?"Y":"N");



        request.put("sDCRRC_CHEM_ID", sDCRRC_CHEM_ID);
        request.put("sDCRRC_LOC_CHEM", sDCRRC_LOC_CHEM);
        request.put("sDCRRC_IN_TIME_CHEM", sDCRRC_IN_TIME_CHEM);
        request.put("sDCRRC_KM_CHEM", sDCRRC_KM_CHEM);
        request.put("sDCRRC_SRNO_CHEM", sDCRRC_SRNO_CHEM);
        request.put("sDCRRC_BATTERY_PERCENT_CHEM", sDCRRC_BATTERY_PERCENT_CHEM);
        request.put("sDCRRC_REMARK_CHEM", sDCRRC_REMARK_CHEM);
        request.put("sDCRRC_FILE_CHEM", sDCRRC_FILE_CHEM);
        request.put("sRC_REF_LAT_LONG_CHEM", sRC_REF_LAT_LONG_CHEM);



        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(-1);



       /* commitDialog = new ProgressDialog(FinalSubmitDcr_new.this);
        commitDialog.setMessage("Please Wait..");
        commitDialog.setCanceledOnTouchOutside(false);
        commitDialog.setCancelable(false);
        commitDialog.show();


        new CboServices(this, mHandler).customMethodForAllServices(request, "DCRCommitFinal_New_18", MESSAGE_INTERNET_FINAL_SUBMIT, tables);
*/


        new MyAPIService(context)
                .execute(new ResponseBuilder("DCRCommitFinal_New_23", request)
                        .setTables(tables)
                        .setDescription("Please Wait..\n" +
                                "Final Submit in process......")
                        .setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) throws JSONException {

                                submit.setEnabled(true);

                                String table0 = message.getString("Tables0");
                                JSONArray jsonArray1 = new JSONArray(table0);
                                JSONObject c = jsonArray1.getJSONObject(0);
                                if ( c.getString("STATUS").equals("Y")) {
                                    customMethod.stopAlarm10Sec();
                                    customMethod.stopAlarm10Minute();
                                    customMethod.stopDOB_DOA_Remainder();
                                    new CustomTextToSpeech().stopTextToSpeech();

                                    new MainDB().delete(null);
                                    //MyCustumApplication.getInstance().updateUser();

                                    new CBOFinalTasks(FinalSubmitDcr_new.this).releseResources();
                                    /*Intent i = new Intent(getApplicationContext(), LoginFake.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);*/

                                    if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"ASKUPDATEYN","N").equals("Y")) {
                                        new GetVersionCode(FinalSubmitDcr_new.this).execute();
                                    }

                                    stopLoctionService();
                                    customVariablesAndMethod.msgBox(context,"DCR Saved Sucessfully..");
                                    MyCustumApplication.getInstance().Logout((Activity) context);
                                    /*finish();*/
                                }else if(c.getString("MESSAGE").toUpperCase().contains("[REPLAN]")) {

                                    AppAlert.getInstance().Alert(context, "Alert !!!", c.getString("MESSAGE"), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(context, GetDCR.class));
                                        }
                                    });

                                } else {

                                    AppAlert.getInstance().getAlert(context,"Alert !!!", c.getString("MESSAGE"), c.getString("URL"));

                                }

                            }

                            @Override
                            public void onResponse(Bundle response) throws JSONException {
                                parser6(response);
                            }

                            @Override
                            public void onError(String s, String s1) {
                                AppAlert.getInstance().getAlert(context,s, s1);
                                submit.setEnabled(true);
                            }


                        })
                );
        //End of call to service

    }

    private void parser6(Bundle result) throws JSONException {

        customVariablesAndMethod.SetLastCallLocation(context);
        String table0 = result.getString("Tables0");
        JSONArray jsonArray1 = new JSONArray(table0);
        JSONObject c = jsonArray1.getJSONObject(0);


        if ( c.getString("STATUS").equals("Y")) {
            String table1 = result.getString("Tables1");
            JSONArray jsonArray2 = new JSONArray(table1);
            service.parseFMCG(context,jsonArray1,jsonArray2);

        }

    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {
            if (back_allowed.equals("Y")){
                finish();
            }else {
                customVariablesAndMethod.getAlert(context,"Please Submit","Please complete your Final Submit");
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void setAddressToUI() {
        if ((Custom_Variables_And_Method.global_address != null) && (!Custom_Variables_And_Method.global_address.equals(""))) {
            loc.setText(Custom_Variables_And_Method.global_address);
        } else {
            loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
        }


    }

    @Override
    public void onBackPressed() {

        if (back_allowed.equals("Y")){
            finish();
        }else {
            customVariablesAndMethod.getAlert(context,"Please Submit","Please complete your Final Submit");
        }

    }
}
