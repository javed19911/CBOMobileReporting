package com.cbo.cbomobilereporting.ui_new;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.GPSTracker;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.ChemistCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DCR_Summary_new;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DairyCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Doctor_Sample;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DrCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DrPrescription;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.PospondFarmerMeeting;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.area.Dcr_Open_New;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.area.Expense;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.DCR_Root_new;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.ExpenseRoot;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.FinalSubmitDcr_new;
import com.cbo.cbomobilereporting.ui.LoginMain;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.NonListedCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.ReminderCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.StockistCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Work_Feedback_Of_Managers;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.GetDCR;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Farmer_registration_form;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsStates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


import locationpkg.Const;
import services.CboServices;
import services.ServiceHandler;
import utils.CBOUtils.Constants;
import utils.clearAppData.MyCustumApplication;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.GetVersionCode;
import utils.adapterutils.DcrMenu_Grid_Adapter;
import utils.networkUtil.NetworkUtil;

public class DcrmenuInGrid extends android.support.v4.app.Fragment {


    private static final int MESSAGE_INTERNET_DCR_PLAN = 1;
    private static final int MESSAGE_INTERNET_IS_CALL_UNLOCKED = 2;
    View v;
    String fmcg;
    GridView gridView;
    NetworkUtil networkUtil;
    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cboDbHelper;
    Animation anim;
    String GPS_STATUS_IS;
    MyCustomMethod myCustomMethod;
    ServiceHandler serviceHandler;
    int PA_ID;
    private final int REQUEST_CHECK_SETTINGS = 1000;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    String tpPendingDate="", empIs, marQueeeText, DCR_ID,Doctor_id_for_POB;
    ArrayList<String> listOfAllTab;
    ArrayList<Integer> count;
    ArrayList<String> getKeyList = new ArrayList<>();
    Map<String, String> keyValue = new LinkedHashMap<String, String>();
    Context context;
    public ProgressDialog progress1;
    String CheckType,nameOnClickGlobal;
    //Boolean FlagCancelled=true;


    ArrayList<Map<String, String>> Appraisal_list=null;
    ArrayList<Map<String, String>> mandatory_pending_exp_head=null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.grid_menu_forall, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context=getActivity();
        progress1 = new ProgressDialog(context);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance(context);

        cboDbHelper = customVariablesAndMethod.get_cbo_db_instance();
        networkUtil = new NetworkUtil(getActivity());
        anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_alfa_2016);
        myCustomMethod = new MyCustomMethod(getActivity());
        serviceHandler = new ServiceHandler(getActivity());
        PA_ID = Integer.parseInt(cboDbHelper.getPaid());
        fmcg = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"fmcg_value");
        GPS_STATUS_IS = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"gps_needed");
        DCR_ID = Custom_Variables_And_Method.DCR_ID;
        addAllTab();
        Custom_Variables_And_Method.CURRENTTAB=((ViewPager_2016) getActivity()).getTabIndex();

        gridView = (GridView) v.findViewById(R.id.grid_view_example);

        gridView.setAdapter(new DcrMenu_Grid_Adapter(getActivity(), listOfAllTab, getKeyList,count));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  TextView tagText = (TextView) view.findViewById(R.id.text_src);
                //String nameOnClick = tagText.getText().toString();
                String nameOnClick = getKeyList.get(position);
                nameOnClickGlobal = nameOnClick;
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 22);
                    // LoginMain.this.checkAndRequestPermission();
                }else {
                    String url = new CBO_DB_Helper(getActivity()).getMenuUrl("DCR", nameOnClick);
                    if (url != null && !url.equals("")) {
                       /* Intent i = new Intent(getActivity(), CustomWebView.class);
                        i.putExtra("A_TP", url);
                        i.putExtra("Title", listOfAllTab.get(position));
                        startActivity(i);*/
                        MyCustumApplication.getInstance().LoadURL(listOfAllTab.get(position),url);
                    } else if (customVariablesAndMethod.IsGPS_GRPS_ON(context)){
                        OnGridItemClick(nameOnClick,false);
                    }
                }
            }

        });

    }


    private void OnGridItemClick(String nameOnClick,boolean SkipLocationVarification){
        switch (nameOnClick) {

            case "D_DP": {
                if(customVariablesAndMethod.checkIfCallLocationValid(context,true,SkipLocationVarification)) {
                    setLetLong(nameOnClick);
                    onClickDayPlanning();
                    if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "ASKUPDATEYN", "N").equals("Y")) {
                        new GetVersionCode(getActivity()).execute();
                    }
                }else {
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                }
                break;
            }
            case "D_DRCALL": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                } else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code", "W").equals("CSC")){
                    String working_type=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_head");
                    customVariablesAndMethod.getAlert(context,"Call Not Allowed","You have planed your DCR as\n \""
                            +working_type+"\" \n you can't any Doctor..");
                }else {
                    setLetLong(nameOnClick);
                    onClickDrCall();
                }
                break;
            }
            case "D_DR_RX": {
                onClickDrPrescrtion();
                break;
            }

            case "D_RCCALL": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                }else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code", "W").equals("CSC")){
                    String working_type=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_head");
                    customVariablesAndMethod.getAlert(context,"Call Not Allowed","You have planed your DCR as\n \""
                            +working_type+"\" \n you can't any Doctor..");
                }else {
                    setLetLong(nameOnClick);
                    onClickReminder();
                }

                break;
            }
            case "D_DRSAM": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                } else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code", "W").equals("CSC")){
                    String working_type=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_head");
                    customVariablesAndMethod.getAlert(context,"Call Not Allowed","You have planed your DCR as\n \""
                            +working_type+"\" \n you can't any Doctor..");
                }else {
                    onClickDoctorSample();
                }

                break;
            }

            case "D_DAIRY": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                } else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                }else {
                    setLetLong(nameOnClick);
                    onClickDairy("D");
                }
                break;
            }
            case "D_POULTRY": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                } else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                }else {
                    setLetLong(nameOnClick);
                    onClickDairy("P");
                }
                break;
            }

            case "D_RETCALL": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                } else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                }else {
                    setLetLong(nameOnClick);
                    onClickChemistCall();
                }
                break;
            }
            case "D_CHEMCALL": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                } else {
                    setLetLong(nameOnClick);
                    onClickChemistCall();
                }
                break;
            }

            case "D_STK_CALL": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                } else {
                    setLetLong(nameOnClick);
                    onClickStockist();
                }
                break;
            }

            case "D_NLC_CALL": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                } else {
                    onClickNonlist();
                }
                break;

            }
            case "D_AP": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                } else {
                    onClickApprasil();
                }
                break;

            }
            case "D_FAR": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                } else {
                    onClickFarmerRegistor();
                }
                break;
            }
            case "D_EXP": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                } else {
                    onClickExpanse();
                }
                break;

            }

            case "D_SUM": {

                onClickSummary();

                break;
            }
            case "D_FINAL": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                } else {
                    setLetLong(nameOnClick);
                    // }
                    if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "MISSED_CALL_OPTION", "N").equals("D") || checkForDoctorPOB()) {
                        onClickFinalSubmit();
                    } else {
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View dialogLayout = inflater.inflate(R.layout.alert_view, null);
                        final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
                        final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
                        final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
                        Alert_title.setText("Pending!!!");
                        Alert_message.setText("Call to some Planed Doctor");

                        final TextView pa_id_txt = (TextView) dialogLayout.findViewById(R.id.PA_ID);
                        pa_id_txt.setText("" + Custom_Variables_And_Method.PA_ID);

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


                        final AlertDialog dialog = builder1.create();

                        dialog.setView(dialogLayout);
                        Alert_Positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(context, DrCall.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("id", Doctor_id_for_POB);
                                i.putExtra("remark", "Call Pending");
                                startActivity(i);
                                dialog.dismiss();
                            }
                        });
                        dialog.setCancelable(false);
                        dialog.show();

                    }
                }
                break;
            }
            default: {

                    Toast.makeText(getActivity(), "Page Under Development", Toast.LENGTH_LONG).show();

            }
        }
    }

    private boolean checkForDoctorPOB() {
        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code", "W").equals("CSC")){
            return true;
        }
        ArrayList<String> drlist = new ArrayList<String>();
        Cursor c = cboDbHelper.getDoctorListLocal("1",null);
        if (c.moveToFirst()) {
            do {
                drlist.add(c.getString(c.getColumnIndex("dr_id")));
            } while (c.moveToNext());
        }

        ArrayList<String> Doctor_list= cboDbHelper.getDoctor();
        for (int i=0;i<drlist.size();i++) {
            if (!Doctor_list.contains(drlist.get(i))) {
               Doctor_id_for_POB=drlist.get(i);
                return false;
            }
        }
        return true;
    }




    @Override
    public void onStart(){
        super.onStart();
        addAllTab();
        cboDbHelper.getDetailsForOffline();
        DCR_ID= Custom_Variables_And_Method.DCR_ID;
        gridView.setAdapter(new DcrMenu_Grid_Adapter(getActivity(), listOfAllTab, getKeyList,count));

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contex, Intent intent) {
            Location location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE);
            OnGridItemClick(nameOnClickGlobal,true);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);
            nameOnClickGlobal = "";

        }
    };
    private void onClickDrPrescrtion() {
        if (!networkUtil.internetConneted(getActivity())) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {
            Intent i = new Intent(getActivity(), DrPrescription.class);
            startActivity(i);
        }
    }

    private void onClickDayPlanning() {
        v.startAnimation(anim);
        if (!networkUtil.internetConneted(getActivity())) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {

            if (GPS_STATUS_IS.equals("Y")) {

                int mode = new MyCustomMethod(getActivity()).getLocationMode(getActivity());

                if (!myCustomMethod.checkGpsEnable() || mode != 3) {
                    // showSettings();
                    customVariablesAndMethod.msgBox(context,"Please Swicth ON your GPS");
                    customVariablesAndMethod.getGpsSetting(context);
                } else {
                   // getActivity().startService(new Intent(getActivity(), MyLoctionService.class));
                    ((CustomActivity) getActivity()).startLoctionService(true);
                    new Thread(threadConvertAddress).start();
                   /* if (Custom_Variables_And_Method.DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {*/
                        //new Doback3().execute(PA_ID);

                    //if (customVariablesAndMethod.IsLocationTooOld(0)) {
                        dcr_plan();
                   /* }else{
                        customVariablesAndMethod.getAlert(context,"Error !!! ","Please Switch ON/OFF your GPS");
                    }*/

                   /* }else {
                        //if data or dcr are pending
                        startActivity(new Intent(getActivity(), GetDCR.class));
                    }*/
                }
            } else {
                // new Thread(threadConvertAddress).start();
                /*if (Custom_Variables_And_Method.DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {*/
                    //new Doback3().execute(PA_ID);
                    dcr_plan();
                /*}else {
                    //if data or dcr are pending
                    startActivity(new Intent(getActivity(), GetDCR.class));
                }*/
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 22) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //capture_Image();
                customVariablesAndMethod.msgBox(context,"Permission granted");
            }
        }
    }

    Runnable threadConvertAddress = new Runnable() {
        @Override
        public void run() {
            myCustomMethod.convertAddress();
        }
    };

    //==================================================================dcr planing===========================
    void dcr_plan(){
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cboDbHelper.getCompanyCode());
        request.put("sPaId", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sMobileVersion", "" + Custom_Variables_And_Method.VERSION);
        request.put("iDCR_ID", "" + Custom_Variables_And_Method.DCR_ID);

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(-1);

        progress1.setMessage("Please Wait.. \n" +
                "Checking your DCR Status");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(context,mHandler).customMethodForAllServices(request,"DCR_DAYPLAN_LOAD_1",MESSAGE_INTERNET_DCR_PLAN,tables);

        //End of call to service
    }



    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_DCR_PLAN:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_dcr_plan(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_IS_CALL_UNLOCKED:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_is_call_unlocked(msg.getData());

                    }
                    break;

                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };

    private void parser_dcr_plan(Bundle result) {

        if (result!=null ) {

            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray0 = new JSONArray(table0);

                if (jsonArray0.length()>0 && (jsonArray0.getJSONObject(0).getString("STATUS_CODE").equals("Y") ||
                        jsonArray0.getJSONObject(0).getString("STATUS_CODE").equals("W"))) {

                    if (jsonArray0.getJSONObject(0).getString("STATUS_CODE").equals("W")){
                        AppAlert.getInstance().setNagativeTxt("Later").DecisionAlert(context,
                                jsonArray0.getJSONObject(0).getString("TITLE"),
                                jsonArray0.getJSONObject(0).getString("MSG"),
                                new AppAlert.OnClickListener() {
                                    @Override
                                    public void onPositiveClicked(View item, String result1) {
                                        try {
                                            if (!jsonArray0.getJSONObject(0).getString("URL").equalsIgnoreCase("")){
                                                /*Intent i = new Intent(getActivity(), CustomWebView.class);
                                                i.putExtra("A_TP", jsonArray0.getJSONObject(0).getString("URL"));
                                                i.putExtra("Title", jsonArray0.getJSONObject(0).getString("TITLE"));
                                                startActivity(i);*/
                                                MyCustumApplication.getInstance().LoadURL(jsonArray0.getJSONObject(0).getString("TITLE"),jsonArray0.getJSONObject(0).getString("URL"));
                                            }else{
                                                openDCR(result);
                                            }
                                        } catch (JSONException e) {
                                            CboServices.getAlert(context,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onNegativeClicked(View item, String result1) {
                                        try {
                                            openDCR(result);
                                        } catch (JSONException e) {
                                            CboServices.getAlert(context,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }else{
                        openDCR(result);
                    }


                }else if(jsonArray0.length()>0 && jsonArray0.getJSONObject(0).getString("STATUS_CODE").equals("R")){
                    new ResetTaskResinedEmp().execute();
                }else{
                    customVariablesAndMethod.getAlert(context,jsonArray0.getJSONObject(0).getString("TITLE"),jsonArray0.getJSONObject(0).getString("STATUS"),jsonArray0.getJSONObject(0).getString("URL"));
                }

                progress1.dismiss();
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }

    private void openDCR( Bundle result)throws JSONException {
        String table0 = result.getString("Tables0");
        JSONArray jsonArray0 = new JSONArray(table0);
        if (Custom_Variables_And_Method.DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "dcr_date_real").equals("")) {

            for (int j = 0; j < jsonArray0.length(); j++) {
                JSONObject c = jsonArray0.getJSONObject(j);
                Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT = c.getString("DCR_DATE");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DCR_DATE", c.getString("DCR_DATE"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DATE_NAME", c.getString("DATE_NAME"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "CUR_DATE", c.getString("CUR_DATE"));
                Custom_Variables_And_Method.DCR_DATE = c.getString("DATE_NAME");
                //customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DATE_NAME",c.getString("DATE_NAME"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "mark", c.getString("NewFlash"));
            }

            String table1 = result.getString("Tables1");
            JSONArray jsonArray1 = new JSONArray(table1);
            Custom_Variables_And_Method.DcrPending_datesList.clear();
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObjectPendingDates = jsonArray1.getJSONObject(i);
                Custom_Variables_And_Method.DcrPending_datesList.add(jsonObjectPendingDates.getString("DATE_NAME"));
            }


            Custom_Variables_And_Method.ROOT_NEEDED = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"root_needed", "N");
            if (Custom_Variables_And_Method.ROOT_NEEDED.equals("Y")) {

                Intent intent = new Intent(getActivity(), DCR_Root_new.class);
                intent.putExtra("plan_type", "p");
                startActivity(intent);
            } else {

                Intent intent = new Intent(getActivity(), Dcr_Open_New.class);
                intent.putExtra("plan_type", "p");
                startActivity(intent);
            }


        } else {
            //if data or dcr are pending
            startActivity(new Intent(getActivity(), GetDCR.class));
        }
    }

    private void parser_is_call_unlocked(Bundle result) {

        if (result!=null ) {

            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray0 = new JSONArray(table0);
                if (CheckType.equals("A")){
                    if (jsonArray0.length() > 0 && jsonArray0.getJSONObject(0).getString("CALL_UNLOCK").contains("[DIVERT_UNLOCK]")) {

                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DIVERTLOCKYN","" );
                        customVariablesAndMethod.getAlert(context, "Approved !!!", "Your Calls have been Approved \nYou can please proceed");
                    } else {

                        customVariablesAndMethod.getAlert(context, "Approval !!!", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,
                                "APPROVAL_MSG","Your Route Approval is Pending... \nYou Route must be approved first !!!\n" +
                                        "Please contact your Head-Office for APPROVAL"));
                        // customVariablesAndMethod.getAlert(context,"CALL LOCKED","Your Calls has not been Unlocked yet \nPlease contact your administrator to proceed");
                    }
                }else {
                    if (jsonArray0.length() > 0 && jsonArray0.getJSONObject(0).getString("CALL_UNLOCK").contains("[CALL_UNLOCK]")) {
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "CALL_UNLOCK_STATUS", "[CALL_UNLOCK]");
                        customVariablesAndMethod.getAlert(context, "CALL UNLOCKED", "Your Calls have been Unlocked \nYou can please proceed");
                    } else {
                        Float FIRST_CALL_LOCK_TIME = Float.valueOf(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "FIRST_CALL_LOCK_TIME", "0"));
                        customVariablesAndMethod.getAlert(context, "CALL LOCKED", "Your Calls has been Locked... \nYou must have made your first Call before " + FIRST_CALL_LOCK_TIME + " O'clock\n" +
                                "Please contact your Head-Office to UNLOCK");
                        // customVariablesAndMethod.getAlert(context,"CALL LOCKED","Your Calls has not been Unlocked yet \nPlease contact your administrator to proceed");
                    }
                }

                progress1.dismiss();
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }



    //====================================================AsyncTask for dcr planing ===========================================================


    class ResetTaskResinedEmp extends AsyncTask<String, String, String> {

        ProgressDialog myProgress;


        String resetSatus;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgress = new ProgressDialog(getActivity());
            myProgress.setTitle("CBO");
            myProgress.setMessage("Please Wait...");
            myProgress.setCanceledOnTouchOutside(false);
            myProgress.setCancelable(false);
            myProgress.show();


        }


        @Override
        protected String doInBackground(String... params) {

            String result;
            try {
                cboDbHelper = new CBO_DB_Helper(getActivity());

                String companyCode = cboDbHelper.getCompanyCode();
                serviceHandler = new ServiceHandler(getActivity());

                result = serviceHandler.getResponse_ResetTask(companyCode, Custom_Variables_And_Method.DCR_ID);

                cboDbHelper.deleteLogin();
                cboDbHelper.close();
            }catch (Exception e){
                return "ERROR apk "+e;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if ((s == null) || (s.toLowerCase().contains("error"))) {
                customVariablesAndMethod.msgBox(context,"Failed to Reset Please Try Again");
                myProgress.dismiss();


            } else {
                try {

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("Tables0");
                    JSONObject c = jsonArray.getJSONObject(0);
                    resetSatus = c.getString("DCRID");
                } catch (JSONException e) {
                }
                if (resetSatus.equals("RESET")) {
                    myProgress.dismiss();
                    customVariablesAndMethod.msgBox(context,"Dcr Day Successfully Reset ");
                    Intent i = new Intent(getActivity(), LoginMain.class);
                    startActivity(i);
                } else {
                    myProgress.dismiss();
                    customVariablesAndMethod.msgBox(context,"Day plan First......");
                }

            }

        }


    }

    ///====================================Dairy call=====================================
    private void onClickDairy(String DOC_TYPE){
        if (IsCallAllowed()){
            Intent i = new Intent(getActivity().getApplicationContext(), DairyCall.class);
            i.putExtra("DOC_TYPE", DOC_TYPE);
            startActivity(i);
        }

    }

    private Boolean IsCallAllowed(){
        if (!customVariablesAndMethod.IsCallAllowedToday(context)) {
            if (DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {

                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
            } else {
                customVariablesAndMethod.msgBox(context,"You Need to Final submit Your Pending Dcr First" + "\n" + "...Then Visit Again....");
            }
        }else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"MOBILEDATAYN", "N").equals("Y") &&!networkUtil.internetConneted(getActivity())) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            if(customVariablesAndMethod.IsGPS_GRPS_ON(context)){
                if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                    return true;
                }
            }

        }
        return false;
    }

    ////////////////////////Chemist Call///////////////////////////

    private void onClickChemistCall() {


        v.startAnimation(anim);

        if (!customVariablesAndMethod.IsCallAllowedToday(context)) {
            if (DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {

                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
            } else {
                customVariablesAndMethod.msgBox(context,"You Need to Final submit Your Pending Dcr First" + "\n" + "...Then Visit Again....");
            }
        }else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"MOBILEDATAYN", "N").equals("Y") &&!networkUtil.internetConneted(getActivity())) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {


            if (GPS_STATUS_IS.equals("Y")) {

                int mode = new MyCustomMethod(getActivity()).getLocationMode(getActivity());

                if (!myCustomMethod.checkGpsEnable() || mode != 3) {

                    customVariablesAndMethod.getGpsSetting(context);
                    //  showSettings();

                } else {


                    if (Custom_Variables_And_Method.INTERNET_REQ.equals("Y")) {

                        if (!networkUtil.internetConneted(getActivity())) {
                            customVariablesAndMethod.Connect_to_Internet_Msg(context);

                        } else {

                            new Thread(threadConvertAddress).start();
                            if (!DCR_ID.equals("0")) {
                                if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                                    Intent i = new Intent(getActivity().getApplicationContext(), ChemistCall.class);
                                    startActivity(i);
                                }

                            } else {
                                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                            }
                        }
                    } else {
                        if (!DCR_ID.equals("0")) {
                            if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                                Intent i = new Intent(getActivity(), ChemistCall.class);
                                startActivity(i);
                            }
                        } else {
                            customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                        }
                    }
                }


            } else {


                if (Custom_Variables_And_Method.INTERNET_REQ.equals("Y")) {
                    if (!networkUtil.internetConneted(getActivity())) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);

                    } else {
                        if (!DCR_ID.equals("0")) {

                            if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                                Intent i = new Intent(getActivity(), ChemistCall.class);
                                startActivity(i);
                            }
                        } else {
                            customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                        }
                    }
                } else {
                    if (!DCR_ID.equals("0")) {

                        if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                            Intent i = new Intent(getActivity(), ChemistCall.class);
                            startActivity(i);
                        }
                    } else {
                        customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                    }
                }

            }

        }


    }

    ///////////
    //////////////////////Stockist Call////////////////


    private void onClickStockist() {


        v.startAnimation(anim);

        if (!customVariablesAndMethod.IsCallAllowedToday(context)) {
            if (DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {

                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
            } else {
                customVariablesAndMethod.msgBox(context,"You Need to Final submit Your Pending Dcr First" + "\n" + "...Then Visit Again....");
            }
        }else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"MOBILEDATAYN", "N").equals("Y") &&!networkUtil.internetConneted(getActivity())) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {


            if (GPS_STATUS_IS.equals("Y")) {

                int mode = new MyCustomMethod(getActivity()).getLocationMode(getActivity());

                if (!myCustomMethod.checkGpsEnable() || mode != 3) {
                    //GPS Enabled
                    customVariablesAndMethod.getGpsSetting(context);
                    // showSettings();

                } else

                {
                    if (Custom_Variables_And_Method.INTERNET_REQ.equals("Y")) {

                        if (!networkUtil.internetConneted(getActivity())) {
                            customVariablesAndMethod.Connect_to_Internet_Msg(context);

                        } else {
                            new Thread(threadConvertAddress).start();
                            if (!DCR_ID.equals("0")) {
                                if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                                    startActivity(new Intent(getActivity(), StockistCall.class));
                                }
                            } else {
                                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                            }
                        }
                    } else {
                        if (!DCR_ID.equals("0")) {
                            if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                                startActivity(new Intent(getActivity(), StockistCall.class));
                            }
                        } else {
                            customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                        }
                    }
                }


            } else {


                if (Custom_Variables_And_Method.INTERNET_REQ.equals("Y")) {
                    if (!networkUtil.internetConneted(getActivity())) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);

                    } else {
                        if (!DCR_ID.equals("0")) {
                            if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                                startActivity(new Intent(getActivity(), StockistCall.class));
                            }

                        } else {
                            customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                        }
                    }
                } else {
                    if (!DCR_ID.equals("0")) {
                        if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                            startActivity(new Intent(getActivity(), StockistCall.class));
                        }
                    } else {
                        customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                    }
                }

            }


        }


    }

    //////////////////////onClick Expanse////////////////
    private void onClickExpanse() {

        v.startAnimation(anim);


        if (!networkUtil.internetConneted(getActivity())) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {

            if (!DCR_ID.equals("0")) {

                if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                    // TODO Auto-generated method stub
                    try {

                        SharedPreferences pref = getActivity().getSharedPreferences(Custom_Variables_And_Method.FMCG_PREFRENCE, getActivity().MODE_PRIVATE);
                        Custom_Variables_And_Method.ROOT_NEEDED = pref.getString("root_needed", null);
                        if (Custom_Variables_And_Method.ROOT_NEEDED != null) {
                            if (Custom_Variables_And_Method.ROOT_NEEDED.equals("Y")) {
                                startActivity(new Intent(getActivity(), ExpenseRoot.class));
                            } else {
                                startActivity(new Intent(getActivity(), Expense.class));
                            }


                        }
                    } catch (Exception e) {

                    }

                }
            } else {
                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first.....");
            }
        }


    }

    //////////////
    /////////////////////onClick Summary///////////

    private void onClickSummary() {

        v.startAnimation(anim);
        if (!DCR_ID.equals("0")) {
            //new synchronizingData().execute();
            Intent i = new Intent(getActivity(), DCR_Summary_new.class);
            i.putExtra("who",0);
                startActivity(i);
        } else {
            customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
        }
    }

    ///////////

    ////////////onClick Nonlsited///////////

    private void onClickNonlist() {
        v.startAnimation(anim);

        if (!DCR_ID.equals("0")) {
            if (networkUtil.internetConneted(getActivity())) {
                startActivity(new Intent(getActivity(), NonListedCall.class));
            } else {
                customVariablesAndMethod.Connect_to_Internet_Msg(context);
            }
        } else {
            customVariablesAndMethod.msgBox(context,"Please Day Plan First..");
        }
    }

    ///////////
    ///////////////////onClick FinalSubmit////////////
    private void onClickFinalSubmit() {


        v.startAnimation(anim);
        ArrayList farmer_Visited = new ArrayList<String>();
        farmer_Visited = cboDbHelper.collect_all_data();
        String mWork_val = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_head");

        Appraisal_list=cboDbHelper.get_Appraisal("0","");
        mandatory_pending_exp_head=cboDbHelper.get_mandatory_pending_exp_head();

       /* result+=cboDbHelper.getmenu_count("phdcrdr_rc");
        result+=cboDbHelper.getmenu_count("tempdr");
        result+=cboDbHelper.getmenu_count("chemisttemp");
        result+=cboDbHelper.getmenu_count("phdcrstk");
        */
        int chemist_status = cboDbHelper.getmenu_count("chemisttemp");
        int stockist_status = cboDbHelper.getmenu_count("phdcrstk");
        String expence_status = getmydata().get(2);
        ArrayList<String> drInLocal = new ArrayList<String>();
        drInLocal = cboDbHelper.tempDrListForFinalSubmit();

        if (!customVariablesAndMethod.IsCallAllowedToday(context)) {

            if (DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first.....");
            }else if (drInLocal.size() <= 0 && (cboDbHelper.getCountphdairy_dcr("D") == 0 && (cboDbHelper.getCountphdairy_dcr("P") == 0) &&
                    (chemist_status == 0) && (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHEMIST_NOT_VISITED").equals("Y")) &&
                            (stockist_status == 0) && (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"STOCKIST_NOT_VISITED").equals("Y")))) {

                //customVariablesAndMethod.getAlert(context,"No Calls found !!!","Your DCR has-been Locked, please reset your DCR from Utilites");

                ShowAlertforNOCalls();
            }/*else  if (chemist_status == 0 && (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code", "W").equals("OCC")||
                    customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").equals("CSC"))) {
                customVariablesAndMethod.msgBox(context,"Please Select Not Visited In Chemist Call");
            }else  if (stockist_status == 0 && (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code", "W").equals("OSC")||
                    customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").equals("CSC"))) {
                customVariablesAndMethod.msgBox(context,"Please Select Not Visited In Stockist Call");
            }*/else if (expence_status.equals("") && (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Expense_NOT_REQUIRED").equals("Y"))) {

                customVariablesAndMethod.msgBox(context,"Please submit Your Expense First... ");

            } else if (!networkUtil.internetConneted(getActivity())) {
                customVariablesAndMethod.Connect_to_Internet_Msg(context);

            } else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"APPRAISALMANDATORY").equals("Y") && Appraisal_list.size() != 0) {

                String pending_list="";
                for (int i = 0; i < Appraisal_list.size(); i++) {
                    pending_list+=Appraisal_list.get(i).get("PA_NAME")+"\n";
                }
                customVariablesAndMethod.getAlert(context,"Appraisal Pending",pending_list);

            }else if (mandatory_pending_exp_head.size() != 0) {

                String pending_list="";
                for (int i = 0; i < mandatory_pending_exp_head.size(); i++) {
                    pending_list+=mandatory_pending_exp_head.get(i).get("PA_NAME")+"\n";
                }
                customVariablesAndMethod.getAlert(context,"Expenses Pending",pending_list);

            } else if( IsExpCriteriaFulfiled(drInLocal.size())){
                new Thread(threadConvertAddress).start();
                startActivity(new Intent(getActivity(), FinalSubmitDcr_new.class));
                getActivity().overridePendingTransition(R.anim.fed_in, R.anim.fed_out);
            }

        } else if (farmer_Visited.size() == 0 && (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FARMERREGISTERYN").equals("Y"))) {
           // customVariablesAndMethod.msgBox(context,"Please Visit atleast One " +cboDbHelper.getMenu("DCR", "D_FAR").get("D_FAR"));
            ShowAlertforNOFarmerMeetingCalls();
        } else if ((mWork_val.equalsIgnoreCase("Working and Group Metting of Dairy Farmer"))
                && (!DCR_ID.equals("0")) && (farmer_Visited.size() == 0)) {

            customVariablesAndMethod.msgBox(context,"Please Visit atleast One Farmer \n Just Go back Main Menu in Transtion and Add...");

        }else if ((mWork_val.equalsIgnoreCase("Group Metting of Dairy farmer"))
                && (!DCR_ID.equals("0")) && (farmer_Visited.size() == 0)) {

            customVariablesAndMethod.msgBox(context,"Please Visit atleast One Farmer \n Just Go back Main Menu in Transtion and Add...");

        } else {


            if (!networkUtil.internetConneted(getActivity())) {

                customVariablesAndMethod.Connect_to_Internet_Msg(context);

            } else {

                if (GPS_STATUS_IS.equals("Y")) {


                    if (!myCustomMethod.checkGpsEnable()) {

                        customVariablesAndMethod.getGpsSetting(context);
                        // showSettings();

                    } else {


                        if (!networkUtil.internetConneted(getActivity())) {
                            customVariablesAndMethod.Connect_to_Internet_Msg(context);

                        } else {

                            if (!DCR_ID.equals("0")) {
                                try {

                                    result4FinalSubmit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                            }

                        }


                    }


                } else {


                    if (!networkUtil.internetConneted(getActivity())) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);

                    } else {

                        if (!DCR_ID.equals("0")) {
                            try {
                                result4FinalSubmit();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                        }

                    }

                }

            }


        }


    }

    public void result4FinalSubmit() {
        String chemist_status = getmydata().get(0);
        String stockist_status = getmydata().get(1);
        String expence_status = getmydata().get(2);
        ArrayList<String> drInLocal = new ArrayList<String>();

        ArrayList farmer_Visited = new ArrayList<String>();
        farmer_Visited = cboDbHelper.collect_all_data();

        String Hide_status = Constants.getSIDE_STATUS(getActivity());



        if (Hide_status != null) {


            if (Hide_status.equalsIgnoreCase("N") || (Hide_status.equals(""))) {

                drInLocal = cboDbHelper.tempDrListForFinalSubmit();
                int dr_call_size = drInLocal.size();
                if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code", "W").equals("OCC")||
                        customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").equals("OSC")||
                        customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").equals("CSC")||
                        (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").contains("NR")
                                && customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").contains("W"))){
                    dr_call_size=1;
                }

                if (drInLocal.size() <= 0 && customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").contains("NR") &&
                        (cboDbHelper.getmenu_count("chemisttemp") == 0 && (cboDbHelper.getmenu_count("phdcrstk") == 0))
                        && (cboDbHelper.getCountphdairy_dcr("D") == 0 && (cboDbHelper.getCountphdairy_dcr("P") == 0))) {

                    customVariablesAndMethod.getAlert(context,"No Calls found !!!","Please make atleast One Call....");

                }else if (dr_call_size <= 0) {
                    customVariablesAndMethod.msgBox(context,"Please Select Not Visited In Doctor Call");
                } else if ((chemist_status.equals("")) && (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHEMIST_NOT_VISITED").equals("Y"))) {
                    customVariablesAndMethod.msgBox(context,"Please Select Not Visited In Chemist Call");
                } else if ((stockist_status.equals("")) && (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"STOCKIST_NOT_VISITED").equals("Y"))) {
                    customVariablesAndMethod.msgBox(context,"Please Select Not Visited In Stockist Call");
                } else if (farmer_Visited.size() == 0 && (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FARMERREGISTERYN").equals("Y"))) {
                    customVariablesAndMethod.msgBox(context,"Please Visit atleast One " +cboDbHelper.getMenu("DCR", "D_FAR").get("D_FAR"));
                } else if (expence_status.equals("") && (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Expense_NOT_REQUIRED").equals("Y"))) {

                    customVariablesAndMethod.msgBox(context,"Please submit Your Expense First... ");

                } else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"APPRAISALMANDATORY").equals("Y") && Appraisal_list.size() != 0) {

                    String pending_list="";
                    for (int i = 0; i < Appraisal_list.size(); i++) {
                       pending_list+=Appraisal_list.get(i).get("PA_NAME")+"\n";
                    }
                    customVariablesAndMethod.getAlert(context,"Appraisal Pending",pending_list);

                }else if (mandatory_pending_exp_head.size() != 0) {

                    String pending_list="";
                    for (int i = 0; i < mandatory_pending_exp_head.size(); i++) {
                        pending_list+=mandatory_pending_exp_head.get(i).get("PA_NAME")+"\n";
                    }
                    customVariablesAndMethod.getAlert(context,"Expenses Pending",pending_list);

                }else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Tenivia_NOT_REQUIRED").equals("N") && (Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("1"))) {
                    if (cboDbHelper.getmenu_count("Tenivia_traker")>0 && IsExpCriteriaFulfiled(drInLocal.size())) {
                        startActivity(new Intent(getActivity(), FinalSubmitDcr_new.class));
                        getActivity().overridePendingTransition(R.anim.fed_in, R.anim.fed_out);
                    } else {
                        customVariablesAndMethod.msgBox(context,"Please Visit "+cboDbHelper.getMenu("DCR", "D_DR_RX").get("D_DR_RX")+"..");
                    }
                } else if( IsExpCriteriaFulfiled(drInLocal.size())){
                    startActivity(new Intent(getActivity(), FinalSubmitDcr_new.class));
                    getActivity().overridePendingTransition(R.anim.fed_in, R.anim.fed_out);
                }
            } else {

                if ((cboDbHelper.getmenu_count("chemisttemp") == 0 && (cboDbHelper.getmenu_count("phdcrstk") == 0))
                        && (cboDbHelper.getCountphdairy_dcr("D") == 0 && (cboDbHelper.getCountphdairy_dcr("P") == 0))) {

                    customVariablesAndMethod.getAlert(context,"No Calls found !!!","Please make atleast One Call....");

                }else if ((chemist_status.equals("")) && (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHEMIST_NOT_VISITED").equals("Y"))) {
                    customVariablesAndMethod.msgBox(context,"Please Select Not Visited In Retailer Call");
                } else if ((stockist_status.equals("")) && (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"STOCKIST_NOT_VISITED").equals("Y"))) {
                    customVariablesAndMethod.msgBox(context,"Please Select Not Visited In Stockist Call");
                } else if (expence_status.equals("") && (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Expense_NOT_REQUIRED").equals("Y"))) {
                    customVariablesAndMethod.msgBox(context,"Please submit Your Expense First... ");
                } else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"APPRAISALMANDATORY").equals("Y") && Appraisal_list.size() != 0) {

                    String pending_list="";
                    for (int i = 0; i < Appraisal_list.size(); i++) {
                        pending_list+=Appraisal_list.get(i).get("PA_NAME")+"\n";
                    }
                    customVariablesAndMethod.getAlert(context,"Appraisal Pending",pending_list);

                }else if ( mandatory_pending_exp_head.size() != 0) {

                    String pending_list="";
                    for (int i = 0; i < mandatory_pending_exp_head.size(); i++) {
                        pending_list+=mandatory_pending_exp_head.get(i).get("PA_NAME")+"\n";
                    }
                    customVariablesAndMethod.getAlert(context,"Expenses Pending",pending_list);

                }else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Tenivia_NOT_REQUIRED").equals("N") && (Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("1"))) {
                    if (cboDbHelper.getmenu_count("Tenivia_traker")>0 && IsExpCriteriaFulfiled(drInLocal.size())) {
                        startActivity(new Intent(getActivity(), FinalSubmitDcr_new.class));
                        getActivity().overridePendingTransition(R.anim.fed_in, R.anim.fed_out);
                    } else {
                        customVariablesAndMethod.msgBox(context,"Please Visit "+cboDbHelper.getMenu("DCR", "D_DR_RX").get("D_DR_RX")+"..");
                    }
                } else if( IsExpCriteriaFulfiled(drInLocal.size())){
                    startActivity(new Intent(getActivity(), FinalSubmitDcr_new.class));
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.fed_in, R.anim.fed_out);
                }
            }


        } else {
            customVariablesAndMethod.msgBox(context,"Please Check Something Missing..");
        }

    }

    public ArrayList<String> getmydata() {
        ArrayList<String> raw = new ArrayList<String>();
        StringBuilder chm = new StringBuilder();
        StringBuilder stk = new StringBuilder();
        StringBuilder exp = new StringBuilder();
        Cursor c = cboDbHelper.getFinalSubmit();
        if (c.moveToFirst()) {
            do {
                chm.append(c.getString(c.getColumnIndex("chemist")));
                stk.append(c.getString(c.getColumnIndex("stockist")));
                exp.append(c.getString(c.getColumnIndex("exp")));
            } while (c.moveToNext());

        }
        cboDbHelper.close();
        raw.add(chm.toString());
        raw.add(stk.toString());
        raw.add(exp.toString());
        return raw;
    }

    ////////////


    ///////////onClickFarmerRegistor/////////////

    private void onClickFarmerRegistor() {

        Cursor c = cboDbHelper.getFTPDATA();
        String myDcrId = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_ID");

        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            if ((!myDcrId.equals("N")) && (!myDcrId.equals(null)) && (!myDcrId.equals("0"))) {
                if (c.moveToFirst()) {
                    startActivity(new Intent(getActivity(), Farmer_registration_form.class));
                } else {

                    customVariablesAndMethod.msgBox(context,"Please Upload/Download First...");

                }
            } else {

                customVariablesAndMethod.msgBox(context,"Please DayPlan First....");

            }
        }
    }


    ////////////////////////////////////////////////////////////
/////////////////setOnclick Apprasial///////

    private void onClickApprasil() {
        v.startAnimation(anim);

        if (!DCR_ID.equals("0")) {
            startActivity(new Intent(getActivity(), Work_Feedback_Of_Managers.class));
        } else {
            customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
        }


    }
///////////////////
    /////////////////////onClick onReminder.//////////////////


    private void onClickReminder() {

        v.startAnimation(anim);
        if (!customVariablesAndMethod.IsCallAllowedToday(context)) {
            if (DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {

                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
            } else {
                customVariablesAndMethod.msgBox(context,"You Need to Final submit Your Pending Dcr First" + "\n" + "...Then Visit Again....");
            }
        } else {
        //if (!DCR_ID.equals("0")) {

            if (GPS_STATUS_IS.equals("Y")) {

                int mode = new MyCustomMethod(getActivity()).getLocationMode(getActivity());

                if (!myCustomMethod.checkGpsEnable() || mode != 3) {

                    customVariablesAndMethod.getGpsSetting(context);
                    //GPS Enabled
                    //  showSettings();

                } else {
                    if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                        new Thread(threadConvertAddress).start();
                        Intent i = new Intent(getActivity(), ReminderCall.class);
                        startActivity(i);
                    }
                }

            } else {
                if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                    Intent i = new Intent(getActivity(), ReminderCall.class);
                    startActivity(i);
                }
            }
        } /*else {
            customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
        }*/

    }

    //////////////////onClickDoctorSample//////////

    private void onClickDoctorSample() {
        v.startAnimation(anim);

        if (Custom_Variables_And_Method.INTERNET_REQ.equals("Y")) {
            if (!networkUtil.internetConneted(getActivity())) {
                customVariablesAndMethod.Connect_to_Internet_Msg(context);

            } else {
                if (!DCR_ID.equals("0")) {
                    Intent i = new Intent(getActivity(), Doctor_Sample.class);
                    startActivity(i);
                } else {
                    customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                }
            }
        } else {
            if (!DCR_ID.equals("0")) {
                Intent i = new Intent(getActivity(), Doctor_Sample.class);
                startActivity(i);
            } else {
                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
            }
        }
    }
    /////////

    ////////////////onClick Doctor Calll//////////////////

    private void onClickDrCall() {

        v.startAnimation(anim);
        if (!customVariablesAndMethod.IsCallAllowedToday(context)) {

            if (DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {

                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
            } else {

                customVariablesAndMethod.msgBox(context,"You Need to Final submit Your Pending Dcr First" + "\n" + "...Then Visit Again....");
            }
        } else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"MOBILEDATAYN", "N").equals("Y") &&!networkUtil.internetConneted(getActivity())) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        }else {

            if (GPS_STATUS_IS.equals("Y")) {

                int mode = new MyCustomMethod(getActivity()).getLocationMode(getActivity());

                if (!myCustomMethod.checkGpsEnable() || mode != 3) {

                    customVariablesAndMethod.getGpsSetting(context);
                    //GPS Enabled
                    //  showSettings();

                } else {
                    //if (!customVariablesAndMethod.isBackgroundServiceRunning(context)) {
                       // context.startService(new Intent(context, MyLoctionService.class));
                    ((CustomActivity) context).startLoctionService();
                   // }

                    if (Custom_Variables_And_Method.INTERNET_REQ.equals("Y")) {

                        if (!networkUtil.internetConneted(getActivity())) {
                            customVariablesAndMethod.Connect_to_Internet_Msg(context);

                        } else {


                            new Thread(threadConvertAddress).start();
                            if (!DCR_ID.equals("0")) {
                                if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                                    Intent i = new Intent(getActivity(), DrCall.class);
                                    startActivity(i);
                                }

                                //new Doback4().execute();
                            } else {
                                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                            }
                        }
                    } else {

                        if (!DCR_ID.equals("0")) {

                            if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                                Intent i = new Intent(getActivity(), DrCall.class);
                                startActivity(i);
                            }
                        }
                        //new Doback4().execute();
                        else {
                            customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                        }
                    }
                }


            } else {


                if (Custom_Variables_And_Method.INTERNET_REQ.equals("Y")) {
                    if (!networkUtil.internetConneted(getActivity())) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);

                    } else {

                        if (!DCR_ID.equals("0")) {

                            if (IsAllowedToCallAtThisTime()) {
                                Intent i = new Intent(getActivity(), DrCall.class);
                                startActivity(i);
                            }
                        } else {
                            customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                        }
                    }
                } else {

                    if (!DCR_ID.equals("0")) {
                        if (IsAllowedToCallAtThisTime() && IsRouteApproved()) {
                            Intent i = new Intent(getActivity(), DrCall.class);
                            startActivity(i);
                        }
                    } else {
                        customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                    }
                }

            }


        }

    }

    ////////////////////////

    private void addAllTab() {


        keyValue = cboDbHelper.getMenu("DCR","");
        listOfAllTab = new ArrayList<String>();
        count = new ArrayList<Integer>();
        for (String key : keyValue.keySet()) {
            getKeyList.add(key);
            count.add(get_count(key));
        }
        if(!getKeyList.contains("D_CHEMCALL") && !getKeyList.contains("D_RETCALL")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHEMIST_NOT_VISITED","Y");
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHEMIST_NOT_REQUIRED","Y");
        }else{
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHEMIST_NOT_REQUIRED","N");
        }
        if(!getKeyList.contains("D_STK_CALL")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"STOCKIST_NOT_VISITED","Y");
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"STOCKIST_NOT_REQUIRED","Y");
        }else{
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"STOCKIST_NOT_REQUIRED","N");
        }
        if(!getKeyList.contains("D_NLC_CALL")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"NonListed_NOT_REQUIRED","Y");
        }else{
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"NonListed_NOT_REQUIRED","N");
        }
        if(!getKeyList.contains("D_DRCALL")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Doctor_NOT_REQUIRED","Y");
        }else{
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Doctor_NOT_REQUIRED","N");
        }
        if(!getKeyList.contains("D_RCCALL")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Doctor_RC_NOT_REQUIRED","Y");
        }else{
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Doctor_RC_NOT_REQUIRED","N");
        }
        if(!getKeyList.contains("D_AP")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Appraisal_NOT_REQUIRED","Y");
        }else{
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Appraisal_NOT_REQUIRED","N");
        }

        if(!getKeyList.contains("D_EXP")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Expense_NOT_REQUIRED","Y");
        }else{
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Expense_NOT_REQUIRED","N");
        }
        if(!getKeyList.contains("D_DR_RX")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Tenivia_NOT_REQUIRED","Y");
        }else {
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "Tenivia_NOT_REQUIRED", "N");
        }
        if(!getKeyList.contains("D_DAIRY")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Dairy_NOT_REQUIRED","Y");
        }else {
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "Dairy_NOT_REQUIRED", "N");
        }
        if(!getKeyList.contains("D_POULTRY")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Polutary_NOT_REQUIRED","Y");
        }else {
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "Polutary_NOT_REQUIRED", "N");
        }

        for (int i = 0; i < keyValue.size(); i++) {
            listOfAllTab.add(keyValue.get(getKeyList.get(i)));
        }
    }

    private int get_count(String menu){
        int result=0;
        String table="";
        Boolean flag=false;
        switch (menu){
            case "D_RCCALL":
                flag=true;
                table="phdcrdr_rc";
                result=1;
                break;
            case "D_DRCALL":
                flag=true;
                table="tempdr";
                result=1;
                break;
            case "D_CHEMCALL":
                flag=true;
                table="chemisttemp";
                result=1;
                break;
            case "D_RETCALL":
                flag=true;
                table="chemisttemp";
                result=1;
                break;
            case "D_STK_CALL":
                flag=true;
                table="phdcrstk";
                result=1;
                break;
            case "D_NLC_CALL":
                flag=true;
                table="NonListed_call";
                result=1;
                break;
            case "D_EXP":
                flag=true;
                table="Expenses";
                result=1;
                break;
            case "D_DR_RX":
                flag=true;
                table="Tenivia_traker";
                result=1;
                break;
            case "D_AP":
                flag=false;
                result=cboDbHelper.get_Appraisal("1","").size();
                break;
            case "D_DAIRY":
                flag=false;
                result=cboDbHelper.getCountphdairy_dcr("D");
                break;
            case "D_POULTRY":
                flag=false;
                result=cboDbHelper.getCountphdairy_dcr("P");
                break;
            case "D_FAR":
                flag=false;
                result = cboDbHelper.collect_all_data().size();
                break;

        }
        if(flag && !table.equals("")){
            result=cboDbHelper.getmenu_count(table);
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent();
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        // mycon.msgBox("You Press Ok..");
                        //onLocationChanged();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        // mycon.msgBox("You Press Cancel..");
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    public void setLetLong(String nameOnClick) {

        //customVariablesAndMethod.checkIfCallLocationValid(context,true);

        if (customVariablesAndMethod.IsGPS_GRPS_ON(context)){
            if (Custom_Variables_And_Method.GLOBAL_LATLON.equalsIgnoreCase("0.0,0.0") || customVariablesAndMethod.IsLocationTooOld(context, 0)) {
                GPSTracker tracker = new GPSTracker(context);
                progress1 = new ProgressDialog(context);
                progress1.setMessage("Please Wait.. ");
                progress1.setCancelable(false);
                progress1.show();
                try {


                    for (int i = 0; i <= 10; i++) {
                        // Location loc= customVariablesAndMethod.latLongFromInternet(context);
                        Location loc = tracker.getLocation();
                        if (loc != null) {

                            Custom_Variables_And_Method.GLOBAL_LATLON = loc.getLatitude() + "," + loc.getLongitude();

                            if (customVariablesAndMethod.IsValidLocation(context, Custom_Variables_And_Method.GLOBAL_LATLON, 0)) {
                                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLatLong", Custom_Variables_And_Method.GLOBAL_LATLON);
                                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "last_location_update_time_in_minites", customVariablesAndMethod.get_currentTimeStamp());
                                customVariablesAndMethod.putObject(context, "currentBestLocation", loc);
                                customVariablesAndMethod.putObject(context, "currentBestLocation_Validated", loc);
                                break;
                            }

                        }

                        if (10 == i) {
                            Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLatLong");

                        }
                    }
                }finally {
                    tracker.stopUsingGPS();
                    progress1.dismiss();
                }

            }
        }

    }

    private Boolean checkforCalls(){
        int result=0;
        result+=cboDbHelper.getmenu_count("phdcrdr_rc");
        result+=cboDbHelper.getmenu_count("tempdr");
        result+=cboDbHelper.getmenu_count("chemisttemp");
        result+=cboDbHelper.getmenu_count("phdcrstk");

        if (result==0){
            return false;
        }else {
            return true;
        }
    }


    private void ShowAlertforNOFarmerMeetingCalls(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //customVariablesAndMethod.msgBox(context,"Please Visit atleast One " +cboDbHelper.getMenu("DCR", "D_FAR").get("D_FAR"));
        final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
        final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
        final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.nagative);
        Alert_Nagative.setText("Ok");
        Alert_Positive.setText("Postpond ?");
        Alert_title.setText("Activity found !!!");
        Alert_message.setText("Today You have an activity for "+cboDbHelper.getMenu("DCR", "D_FAR").get("D_FAR"));

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PospondFarmerMeeting.class);
                startActivity(i);
                dialog.dismiss();
            }
        });
        Alert_Nagative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void ShowAlertforNOCalls(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
        final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
        final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.nagative);
        Alert_Nagative.setText("NO");
        Alert_Positive.setText("YES");
        Alert_title.setText("No Calls found !!!");
        Alert_message.setText("Your DCR has-been Locked, Do You want to reset your DCR?");

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DCR_Summary_new.class);
                i.putExtra("who", 2);
                startActivity(i);
                dialog.dismiss();
            }
        });
        Alert_Nagative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private Boolean IsAllowedToCallAtThisTime(){
        Float FIRST_CALL_LOCK_TIME= Float.valueOf(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FIRST_CALL_LOCK_TIME","0"));
        if (FIRST_CALL_LOCK_TIME==0) return true;

        if (checkforCalls()) return true;
        Float current_time= Float.valueOf(customVariablesAndMethod.getCurrentBestTime(context));

        if (current_time>FIRST_CALL_LOCK_TIME && !customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CALL_UNLOCK_STATUS","[CALL_UNLOCK]").contains("[CALL_UNLOCK]")){
            if (!networkUtil.internetConneted(getActivity())) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
                final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
                final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
                final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
                final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.nagative);
                Alert_Nagative.setText("Cancel");
                Alert_Positive.setText("Check");
                Alert_title.setText("Call Locked");
                Alert_message.setText("Your Calls have been Locked... \nYou must have made your first Call before " + FIRST_CALL_LOCK_TIME + " O'Clock\n" +
                        "Switch ON your Internet and Check if Unlocked...");

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

                final AlertDialog dialog = builder1.create();

                dialog.setView(dialogLayout);
                Alert_Positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckIfCallsUnlocked("C");
                        dialog.dismiss();
                    }
                });
                Alert_Nagative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }else{
                CheckIfCallsUnlocked("C");
            }
            return false;
        }

        return true;
    }


    private Boolean IsExpCriteriaFulfiled(int NoOfDrCalled){
        String NO_DR_CALL_REQ= customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"NO_DR_CALL_REQ","0");
        if (NO_DR_CALL_REQ.equals("0")) return true;

        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code", "W").equals("OCC")||
                customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").equals("OSC")||
                customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").equals("CSC")||
                (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").contains("NR")
                        && customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").contains("W"))){
            return true;
        }
        if (NoOfDrCalled < Integer.parseInt(NO_DR_CALL_REQ)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
            final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
            final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
            final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
            final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.nagative);
            Alert_Nagative.setText("Cancel");
            Alert_Positive.setText("OK");
            Alert_title.setText("Incomplete Dr Calls !!!");
            Alert_message.setText("You have not completed "+NO_DR_CALL_REQ+" Dr. call of this route\nSo you are not eligible for TA/DA !!!");

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

            final AlertDialog dialog = builder1.create();

            dialog.setView(dialogLayout);
            Alert_Positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), FinalSubmitDcr_new.class));
                    getActivity().overridePendingTransition(R.anim.fed_in, R.anim.fed_out);
                    dialog.dismiss();
                }
            });
            Alert_Nagative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
            return false;
        }else{
            return true;
        }
    }

    private Boolean IsRouteApproved(){
        String DIVERTLOCKYN= customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DIVERTLOCKYN","");
        if (!DIVERTLOCKYN.equals("Y")) return true;
            if (!networkUtil.internetConneted(getActivity())) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
                final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
                final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
                final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
                final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.nagative);
                Alert_Nagative.setText("Cancel");
                Alert_Positive.setText("Check");
                Alert_title.setText("Approval !!!");
                /*Alert_message.setText("Your Route Approval is Pending... \nYou Route must be approved first !!!\n" +
                        "Switch ON your Internet and Check if Approved...");*/

                Alert_message.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,
                        "APPROVAL_MSG","Your Route Approval is Pending... \nYou Route must be approved first !!!\n" +
                        "Switch ON your Internet and Check if Approved..."));

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

                final AlertDialog dialog = builder1.create();

                dialog.setView(dialogLayout);
                Alert_Positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckIfCallsUnlocked("A");
                        dialog.dismiss();
                    }
                });
                Alert_Nagative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }else{
                CheckIfCallsUnlocked("A");
            }


        return false;
    }

    private void CheckIfCallsUnlocked(String type) {


        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cboDbHelper.getCompanyCode());
        request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
        request.put("iDCR_ID", "" + Custom_Variables_And_Method.DCR_ID);
        request.put("sTYPE",type);
        CheckType=type;
        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait.. \n Fetching your worktype");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(context, mHandler).customMethodForAllServices(request, "CallUnlockStatus", MESSAGE_INTERNET_IS_CALL_UNLOCKED, tables);

        //End of call to service
    }


    ///////////////



}
