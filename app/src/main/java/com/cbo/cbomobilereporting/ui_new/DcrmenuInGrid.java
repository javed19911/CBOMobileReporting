package com.cbo.cbomobilereporting.ui_new;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.databaseHelper.Controls;
import com.cbo.cbomobilereporting.emp_tracking.GPSTracker;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.ChemistCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Customer.CustomerCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DCRCall.DCRCallActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DCRCall.mChemistRc;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DCR_Summary_new;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DairyCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Doctor_Sample;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DrCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DrPrescription;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DrRXActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.OthExpenseDB;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mExpHead;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Location.CentroidLocation;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.PospondFarmerMeeting;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.area.Dcr_Open_New;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.pobmail.PobMail;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.DCR_Root_new;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.FinalSubmitDcr_new;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.NonListedCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.ReminderCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.StockistCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Work_Feedback_Of_Managers;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.GetDCR;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Farmer_registration_form;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsStates;
import com.uenics.javed.CBOLibrary.CBOException;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.Response;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


import locationpkg.Const;
import services.MyAPIService;
import services.ServiceHandler;
import utils.CBOUtils.Constants;
import com.cbo.cbomobilereporting.MyCustumApplication;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.GetVersionCode;
import utils.adapterutils.DcrMenu_Grid_Adapter;
import utils.networkUtil.NetworkUtil;
import utils_new.Service_Call_From_Multiple_Classes;

public class DcrmenuInGrid extends Fragment {


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
    String CheckType,menuCodeOnClickGlobal,menuNameOnClickGlobal;
    //Boolean FlagCancelled=true;


    ArrayList<Map<String, String>> Appraisal_list=null;
    //ArrayList<Map<String, String>> mandatory_pending_exp_head=null;
    ArrayList<mExpHead> mandatory_pending_exp_head=null;


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
                String menuCodeOnClick = getKeyList.get(position);
                String menuNameOnClick = listOfAllTab.get(position);
                menuCodeOnClickGlobal = menuCodeOnClick;
                menuNameOnClickGlobal = menuNameOnClick;
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE
                    }, 22);
                    // LoginMain.this.checkAndRequestPermission();
                }else if (Controls.getInstance().IsGPSRequired() &&
                        (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(context,
                                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )){
                    ActivityCompat.requestPermissions((Activity) context, new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 22);

                }else {
                    String url = new CBO_DB_Helper(getActivity()).getMenuUrl("DCR", menuCodeOnClick);
                    if (url != null && !url.equals("")) {
                       /* Intent i = new Intent(getActivity(), CustomWebView.class);
                        i.putExtra("A_TP", url);
                        i.putExtra("Title", listOfAllTab.get(position));
                        startActivity(i);*/
                        MyCustumApplication.getInstance().LoadURL(listOfAllTab.get(position),url);
                    } else if (customVariablesAndMethod.IsGPS_GRPS_ON(context)){

                        String work_type_Selected= customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_type_Selected","w");
                        if (menuCodeOnClick.equalsIgnoreCase("D_DP")){
                            work_type_Selected = "X";
                        }
                        switch (work_type_Selected){
                            case "l":
                                Intent intent = new Intent(context, FinalSubmitDcr_new.class);
                                intent.putExtra("Back_allowed","N");
                                startActivity(intent);
                                break;
                            case "n":
                                //startActivity(new Intent(getActivity(), com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.Expense.class));
                                Intent intent1 = new Intent(context, com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.Expense.class);
                                intent1.putExtra("Back_allowed","N");
                                intent1.putExtra("FinalSubmit","Y");
                                startActivity(intent1);

                                break;
                            default:
                                OnGridItemClick(menuCodeOnClick,menuNameOnClick,false);
                        }

                    }
                }
            }

        });

    }


    private void OnGridItemClick(String menuCodeOnClick,String menuNameOnClickGlobal,boolean SkipLocationVarification){
        switch (menuCodeOnClick) {
            case "D_LOC_TEST": {
                Intent i = new Intent(getActivity(), CentroidLocation.class);
                i.putExtra("title","Location Test");
                startActivity(i);
                break;
            }
            case "D_DP": {
                if(customVariablesAndMethod.checkIfCallLocationValid(context,true,SkipLocationVarification)) {
                    setLetLong(menuCodeOnClick);
                    MyCustumApplication.getInstance().startLoctionService(true);
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
                    setLetLong(menuCodeOnClick);
                    onClickDrCall();
                }
                break;
            }
            case "D_RX_GEN_NA":
            case "D_RX_GEN": {
                if (DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                } else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                }else {
                    onClickRxGen();
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
                    setLetLong(menuCodeOnClick);
                    onClickReminder();
                }

                break;
            }

            case "D_CHEM_RCCALL": {
                if (DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                } else if (!customVariablesAndMethod.checkIfCallLocationValid(context, false, SkipLocationVarification)) {
                    customVariablesAndMethod.msgBox(context, "Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                } else {

                    onClickChemistReminder(menuNameOnClickGlobal);

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
                    setLetLong(menuCodeOnClick);
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
                    setLetLong(menuCodeOnClick);
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
                    setLetLong(menuCodeOnClick);
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
                    setLetLong(menuCodeOnClick);
                    onClickChemistCall();
                }
                break;
            }
            case "D_CUST_CALL": {
                if (IsCallAllowed(true)){
                    setLetLong(menuCodeOnClick);
                    Intent i = new Intent(getActivity(), CustomerCall.class);
                    i.putExtra("title",cboDbHelper.getMenu("DCR", "D_CUST_CALL").get("D_CUST_CALL"));
                    startActivity(i);
                }
                break;
            }
            case "D_CS_SMS": {
                if (IsCallAllowed(true)){
                    setLetLong(menuCodeOnClick);
                    Intent i = new Intent(getActivity(), PobMail.class);
                    i.putExtra("title",cboDbHelper.getMenu("DCR", "D_CS_SMS").get("D_CS_SMS"));
                    startActivity(i);
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
                    setLetLong(menuCodeOnClick);
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
                    setLetLong(menuCodeOnClick);
                    // }
                    if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "MISSED_CALL_OPTION", "N").equals("D") || checkForDoctorPOB()) {
                        onClickFinalSubmit();
                    } else {

                        AppAlert.getInstance().Alert(context, "Pending!!!", "Call to some Planed Doctor",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(context, DrCall.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.putExtra("id", Doctor_id_for_POB);
                                        i.putExtra("remark", "Call Pending");
                                        startActivity(i);
                                    }
                                });


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
            OnGridItemClick(menuCodeOnClickGlobal,menuNameOnClickGlobal,true);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);
            menuCodeOnClickGlobal = "";

        }
    };

    private void onClickRxGen() {
        if (!Custom_Variables_And_Method.internetConneted(getActivity())) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {
            Intent i = new Intent(getActivity(), DrRXActivity.class);
            startActivity(i);
        }
    }
    private void onClickDrPrescrtion() {
        if (!Custom_Variables_And_Method.internetConneted(getActivity())) {
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
                    if (mode !=0){
                       customVariablesAndMethod.RequestGPSFromSetting(context);

                    }else{
                        customVariablesAndMethod.getGpsSetting(context);
                    }

                } else {

                    new Thread(threadConvertAddress).start();

                        dcr_plan();

                }
            } else {

                    dcr_plan();

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
        request.put("ISSUPPORTUSER", MyCustumApplication.getInstance().getUser().getLoggedInAsSupport()?"Y":"N");

        new MyAPIService(context)
                .execute(new ResponseBuilder("DCR_DAYPLAN_LOAD_2", request)
                        .setDescription("Please Wait....\nChecking your DCR Status..").setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) throws JSONException {
                                parser_dcr_plan(message);

                            }

                            @Override
                            public void onResponse(Bundle response) throws JSONException {
                            }

                            @Override
                            public void onError(String title, String description) {
                                AppAlert.getInstance().getAlert(context,title,description);
                            }


                        })
                );

        //End of call to service
    }




    private void parser_dcr_plan(Bundle result) throws JSONException {

        if (result!=null ) {

//            try {
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
                                    public void onPositiveClicked(View item, String result1)  {
                                        try {
                                            if (!jsonArray0.getJSONObject(0).getString("URL").equalsIgnoreCase("")){

                                                MyCustumApplication.getInstance().LoadURL(jsonArray0.getJSONObject(0).getString("TITLE"),jsonArray0.getJSONObject(0).getString("URL"));
                                            }else{
                                                openDCR(result);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            throw new CBOException("Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                                            //CboServices.getAlert(context,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());

                                        }
                                    }

                                    @Override
                                    public void onNegativeClicked(View item, String result1){
                                            openDCR(result);
                                    }
                                });
                    }else{
                        openDCR(result);
                    }


                }else if(jsonArray0.length()>0 && jsonArray0.getJSONObject(0).getString("STATUS_CODE").equals("R")){
                    //new ResetTaskResinedEmp().execute();

                    new Service_Call_From_Multiple_Classes().resetDCR(context, new Response() {
                        @Override
                        public void onSuccess(Bundle bundle) {
                            //customVariablesAndMethod.msgBox(context,"Data Downloded Sucessfully...");
                        }

                        @Override
                        public void onError(String message, String description) {
                            AppAlert.getInstance().getAlert(context,message,description);
                        }
                    });
                }else{
                    customVariablesAndMethod.getAlert(context,jsonArray0.getJSONObject(0).getString("TITLE"),jsonArray0.getJSONObject(0).getString("STATUS"),jsonArray0.getJSONObject(0).getString("URL"));
                }

                //progress1.dismiss();
//            } catch (JSONException e) {
//                Log.d("MYAPP", "objects are: " + e.toString());
//                CboServices.getAlert(context,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
//                e.printStackTrace();
//            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
       // progress1.dismiss();

    }

    private void openDCR( Bundle result) {

        try {
            String table2 = result.getString("Tables2");
            JSONArray jsonArray2 = new JSONArray(table2);
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject c = jsonArray2.getJSONObject(i);

                MyCustumApplication.getInstance().getDCR()
                        .setShowRouteAsPerTP(c.getString("DCR_TP_AREA_AUTOYN"))
                        .setShowWorkWithAsPerTP(c.getString("DCR_LOCKWW"))
                        .setNoOfAddAreaAlowed(c.getString("DCR_NOADDAREA"))
                        .setWorkWithTitle(c.getString("WW_TITLE"))
                        .setRouteTitle(c.getString("ROUTE_TITLE"))
                        .setAreaTitle(c.getString("AREA_TITLE"))
                        .setAdditionalAreaApprovalReqd(c.getString("ADDAREA_APPYN"))
                        .setAdditionalAreaValidationReqd(c.getString("ADDITIONALAREA_MENDETYN"))
                        .setAttachmentTilte(c.getString("SELFIE_TITLE"))
                        .setAttachmentMandatory(c.getString("SELFIE_MANDATORYYN"));
            }



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
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CBOException("Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
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
        return IsCallAllowed(false);
    }

    private Boolean IsCallAllowed(Boolean ckeckForMobileData){
        if (!customVariablesAndMethod.IsCallAllowedToday(context)) {
            if (DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {

                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
            } else {
                customVariablesAndMethod.msgBox(context,"You Need to Final submit Your Pending Dcr First" + "\n" + "...Then Visit Again....");
            }
        }else if ((customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"MOBILEDATAYN", "N").equals("Y") ||
                ckeckForMobileData ) &&!networkUtil.internetConneted(getActivity())) {
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

                    customVariablesAndMethod.msgBox(context,"Please Swicth ON your GPS");
                    if (mode !=0){
                        customVariablesAndMethod.RequestGPSFromSetting(context);
                    }else{
                        customVariablesAndMethod.getGpsSetting(context);
                    }
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
                    customVariablesAndMethod.msgBox(context,"Please Swicth ON your GPS");
                    if (mode !=0){
                        customVariablesAndMethod.RequestGPSFromSetting(context);
                    }else{
                        customVariablesAndMethod.getGpsSetting(context);
                    }

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

//                        SharedPreferences pref = getActivity().getSharedPreferences(Custom_Variables_And_Method.FMCG_PREFRENCE, getActivity().MODE_PRIVATE);
//                        Custom_Variables_And_Method.ROOT_NEEDED = pref.getString("root_needed", null);
                        Custom_Variables_And_Method.ROOT_NEEDED =  customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"root_needed","Y");
                        //if (Custom_Variables_And_Method.ROOT_NEEDED != null) {
                            if (Custom_Variables_And_Method.ROOT_NEEDED.equals("Y")) {
                                //startActivity(new Intent(getActivity(), ExpenseRoot.class));
                                startActivity(new Intent(getActivity(), com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.Expense.class));
                            } else {
                                //startActivity(new Intent(getActivity(), Expense.class));
                                startActivity(new Intent(getActivity(), com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.Expense.class));

                            }

                        //startActivity(new Intent(getActivity(), Expense.class));



                        //}
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
        //mandatory_pending_exp_head=cboDbHelper.get_mandatory_pending_exp_head();
        mandatory_pending_exp_head = new OthExpenseDB(context).getMandatoryPendingExpHead();

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

        if (!networkUtil.internetConneted(getActivity())) {

            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else if (!customVariablesAndMethod.IsCallAllowedToday(context)) {

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
                    //pending_list+=mandatory_pending_exp_head.get(i).get("PA_NAME")+"\n";
                    pending_list+=mandatory_pending_exp_head.get(i).getName()+"\n";
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

            int mode = new MyCustomMethod(getActivity()).getLocationMode(getActivity());

            if ((!myCustomMethod.checkGpsEnable() || mode != 3)  && GPS_STATUS_IS.equals("Y")) {

                customVariablesAndMethod.msgBox(context,"Please Swicth ON your GPS");
                if (mode !=0){
                    customVariablesAndMethod.RequestGPSFromSetting(context);
                }else{
                    customVariablesAndMethod.getGpsSetting(context);
                }
                // showSettings();

            } else {

                if (!DCR_ID.equals("0")) {
                    try {

                        //result4FinalSubmit();
                        PreFinalSubmit();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    customVariablesAndMethod.msgBox(context,"Please open your DCR Days first....");
                }


            }
        }


    }


    public void PreFinalSubmit() {
        IfDrConditionFulfiled(new Response() {
            @Override
            public void onSuccess(Bundle bundle) {

                IfCustomerConditionFulfiled(new Response() {
                    @Override
                    public void onSuccess(Bundle bundle) {
                        IfChemistConditionFulfiled(new Response() {
                            @Override
                            public void onSuccess(Bundle bundle) {
                                IfStokistConditionFulfiled(new Response() {
                                    @Override
                                    public void onSuccess(Bundle bundle) {
                                        IfFarmerConditionFulfiled(new Response() {
                                            @Override
                                            public void onSuccess(Bundle bundle) {
                                                IfApprasialConditionFulfiled(new Response() {
                                                    @Override
                                                    public void onSuccess(Bundle bundle) {

                                                        IfRxConditionFulfiled(new Response() {
                                                            @Override
                                                            public void onSuccess(Bundle bundle) {

                                                                IfAllowOfflineCall(new Response() {
                                                                    @Override
                                                                    public void onSuccess(Bundle bundle) {
                                                                        IfDR_SaleRequired(new Response() {
                                                                            @Override
                                                                            public void onSuccess(Bundle bundle) {
                                                                                IfExpenseConditionFulfiled(new Response() {
                                                                                    @Override
                                                                                    public void onSuccess(Bundle bundle) {
                                                                                        if( IsExpCriteriaFulfiled(cboDbHelper.tempDrListForFinalSubmit().size())){
                                                                                            startActivity(new Intent(getActivity(), FinalSubmitDcr_new.class));
                                                                                            getActivity().overridePendingTransition(R.anim.fed_in, R.anim.fed_out);
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onError(String s, String s1) {
                                                                                        AppAlert.getInstance().DecisionAlert(context, s, s1, new AppAlert.OnClickListener() {
                                                                                            @Override
                                                                                            public void onPositiveClicked(View item, String result) {
                                                                                                OnGridItemClick("D_EXP",null,true);
                                                                                            }

                                                                                            @Override
                                                                                            public void onNegativeClicked(View item, String result) {

                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                            }

                                                                            @Override
                                                                            public void onError(String s, String s1) {
                                                                                AppAlert.getInstance().setNagativeTxt("No").setPositiveTxt("Yes").DecisionAlert(context, s, s1, new AppAlert.OnClickListener() {
                                                                                    @Override
                                                                                    public void onPositiveClicked(View item, String result) {

                                                                                        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("DRSALEMSG_FINALSUBMITYN","");
                                                                                        MyCustumApplication.getInstance().LoadURL("Doctor Sale",MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DR_SALE_URL","")+"&MONTH="+customVariablesAndMethod.currentDate());
                                                                                    }

                                                                                    @Override
                                                                                    public void onNegativeClicked(View item, String result) {
                                                                                        onSuccess(null);
                                                                                    }
                                                                                });

                                                                            }
                                                                        });
                                                                    }

                                                                    @Override
                                                                    public void onError(String s, String s1) {
                                                                        AppAlert.getInstance().setNagativeTxt("No").setPositiveTxt("Yes").DecisionAlert(context, s, s1, new AppAlert.OnClickListener() {
                                                                            @Override
                                                                            public void onPositiveClicked(View item, String result) {

                                                                                MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("CALL_TYPE","3");
                                                                                OnGridItemClick("D_DRCALL",null, true);

                                                                            }

                                                                            @Override
                                                                            public void onNegativeClicked(View item, String result) {
                                                                                onSuccess(null);
                                                                            }
                                                                        });
                                                                    }
                                                                });



                                                            }

                                                            @Override
                                                            public void onError(String s, String s1) {
                                                                AppAlert.getInstance().DecisionAlert(context, s, s1, new AppAlert.OnClickListener() {
                                                                    @Override
                                                                    public void onPositiveClicked(View item, String result) {

                                                                        OnGridItemClick(MyCustumApplication.getInstance().getTaniviaTrakerMenuCode(),null,true);
                                                                    }

                                                                    @Override
                                                                    public void onNegativeClicked(View item, String result) {

                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onError(String s, String s1) {
                                                        AppAlert.getInstance().DecisionAlert(context, s, s1, new AppAlert.OnClickListener() {
                                                            @Override
                                                            public void onPositiveClicked(View item, String result) {
                                                                OnGridItemClick("D_AP",null,true);
                                                            }

                                                            @Override
                                                            public void onNegativeClicked(View item, String result) {

                                                            }
                                                        });
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onError(String s, String s1) {
                                                AppAlert.getInstance().DecisionAlert(context, s, s1, new AppAlert.OnClickListener() {
                                                    @Override
                                                    public void onPositiveClicked(View item, String result) {
                                                        OnGridItemClick("D_FAR",null,true);
                                                    }

                                                    @Override
                                                    public void onNegativeClicked(View item, String result) {

                                                    }
                                                });
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(String s, String s1) {
                                        AppAlert.getInstance().DecisionAlert(context, s, s1, new AppAlert.OnClickListener() {
                                            @Override
                                            public void onPositiveClicked(View item, String result) {
                                                OnGridItemClick("D_STK_CALL",null,true);
                                            }

                                            @Override
                                            public void onNegativeClicked(View item, String result) {

                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onError(String s, String s1) {
                                AppAlert.getInstance().DecisionAlert(context, s, s1, new AppAlert.OnClickListener() {
                                    @Override
                                    public void onPositiveClicked(View item, String result) {
                                        OnGridItemClick("D_CHEMCALL",null,true);
                                    }

                                    @Override
                                    public void onNegativeClicked(View item, String result) {

                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().DecisionAlert(context, s, s1, new AppAlert.OnClickListener() {
                            @Override
                            public void onPositiveClicked(View item, String result) {
                                OnGridItemClick("D_CUST_CALL",null,true);
                            }

                            @Override
                            public void onNegativeClicked(View item, String result) {

                            }
                        });
                    }
                });

            }

            @Override
            public void onError(String s, String s1) {
                AppAlert.getInstance().DecisionAlert(context, s, s1, new AppAlert.OnClickListener() {
                    @Override
                    public void onPositiveClicked(View item, String result) {
                        if (!s1.equalsIgnoreCase("Please make atleast One Call....")) {
                            OnGridItemClick("D_DRCALL",null, true);
                        }
                    }

                    @Override
                    public void onNegativeClicked(View item, String result) {

                    }
                });
            }
        });
    }



    //// check the condition for all the menus in dcr

    private void IfDrConditionFulfiled(Response listener){

        String Hide_status = Constants.getSIDE_STATUS(getActivity());

        ArrayList<String>  drInLocal = cboDbHelper.tempDrListForFinalSubmit();
        int dr_call_size = drInLocal.size();
        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code", "W").equals("OCC")||
                customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").equals("OSC")||
                customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").equals("CSC")||
                (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").contains("NR")
                        && customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").contains("W"))){
            dr_call_size=1;
        }

        // valid for only  working_code that contains NR i.e its for only new concept of working type
        // new concept of working type is if any validation for final submit is to be skiped for a menu then
        //workingcode = NR
        //
        if (((drInLocal.size() <= 0 &&
                !customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").equals("CSC")) && !MyCustumApplication.getInstance().IsSubmitDCR_WithoutCalls() ) &&
                (cboDbHelper.getmenu_count("chemisttemp") == 0 && (cboDbHelper.getmenu_count("phdcrstk") == 0))
                && (cboDbHelper.getCountphdairy_dcr("D") == 0 && (cboDbHelper.getCountphdairy_dcr("P") == 0))) {
            if (listener != null) {
                listener.onError("No Calls found !!!", "Please make atleast One Call....");
            }

        }else if (dr_call_size <= 0 && !MyCustumApplication.getInstance().IsSubmitDCR_WithoutCalls() &&
                (Hide_status.equalsIgnoreCase("N") &&
                        !customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Doctor_NOT_REQUIRED").equals("Y"))) {
            if (listener != null) {
                listener.onError("No Doctor Called!!!!", "Please make atleast One Doctor Call.... ");
            }
        }else{
            if (listener != null){
                listener.onSuccess(null);
            }
        }
    }

    private void IfChemistConditionFulfiled(Response listener){

        int chemist_status = cboDbHelper.getmenu_count("chemisttemp");
        String menuName = cboDbHelper.getMenu("DCR", "D_CHEMCALL").get("D_CHEMCALL");

        if ((chemist_status == 0) && !MyCustumApplication.getInstance().IsSubmitDCR_WithoutCalls() &&
                (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHEMIST_NOT_VISITED").equals("Y") &&
                        !customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHEMIST_NOT_REQUIRED").equals("Y"))) {
            if (listener != null) {
                listener.onError("No "+menuName+"!!!","Please Select Not Visited In " + menuName);
            }
        }else{
            if (listener != null){
                listener.onSuccess(null);
            }
        }
    }

    private void IfCustomerConditionFulfiled(Response listener){

        int chemist_status = cboDbHelper.getmenu_count("chemisttemp");
        String menuName = cboDbHelper.getMenu("DCR", "D_CUST_CALL").get("D_CUST_CALL");

        String working_code = MyCustumApplication.getInstance().getDCR().getWorkTypeId();
        if ((chemist_status == 0) && !MyCustumApplication.getInstance().IsSubmitDCR_WithoutCalls() &&
                !customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CUSTOMER_NOT_REQUIRED").equals("Y")
                && (working_code.equals("OCC") || (working_code.contains("NR") && !working_code.contains("C")))) {
            if (listener != null) {
                listener.onError("No "+menuName+"!!!","Please make atleast One " + menuName);
            }
        }else{
            if (listener != null){
                listener.onSuccess(null);
            }
        }
    }


    private void IfStokistConditionFulfiled(Response listener){

        int stockist_count = cboDbHelper.getmenu_count("phdcrstk");

        if ((stockist_count == 0) && !MyCustumApplication.getInstance().IsSubmitDCR_WithoutCalls() &&
                (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"STOCKIST_NOT_VISITED").equals("Y") &&
                        !customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"STOCKIST_NOT_REQUIRED").equals("Y"))) {
            if (listener != null) {
                listener.onError("No Stockist Called!!!!", "Please Select Not Visited In Stockist Call");
            }
        } else{
            if (listener != null){
                listener.onSuccess(null);
            }
        }
    }


    private void IfFarmerConditionFulfiled(Response listener){

        ArrayList farmer_Visited  = cboDbHelper.collect_all_data();
        String menuName = cboDbHelper.getMenu("DCR", "D_FAR").get("D_FAR");

        if (farmer_Visited.size() == 0 && (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FARMERREGISTERYN").equals("Y"))) {
            if (listener != null) {
                listener.onError("No " + menuName + "!!!", "Please Visit atleast One " + menuName);
            }
        }  else{
            if (listener != null){
                listener.onSuccess(null);
            }
        }
    }


    private void IfApprasialConditionFulfiled(Response listener){


        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"APPRAISALMANDATORY").equals("Y") && Appraisal_list.size() != 0) {

            String pending_list="";
            for (int i = 0; i < Appraisal_list.size(); i++) {
                pending_list+=Appraisal_list.get(i).get("PA_NAME")+"\n";
            }
            if (listener != null) {
                listener.onError("Appraisal Pending!!!", pending_list);
            }

        }  else{
            if (listener != null){
                listener.onSuccess(null);
            }
        }
    }


    private void IfDR_SaleRequired(Response listener){

         if (! MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DRSALEMSG_FINALSUBMITYN","").isEmpty()
                && !MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DR_SALE_URL","").isEmpty()) {

             if (listener != null) {
                 listener.onError("Alert !!!",  MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DRSALEMSG_FINALSUBMITYN",""));
             }

        }else{
            if (listener != null){
                listener.onSuccess(null);
            }
        }
    }


    private void IfAllowOfflineCall(Response listener){

        if (Controls.getInstance().IsOfflineCallAllowed()
                && !MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("CALL_TYPE","").equalsIgnoreCase("3")
                && !MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("IsBackDate","1").equalsIgnoreCase("0")) {

            ArrayList<String> drlist = new ArrayList<String>();
            Cursor c = cboDbHelper.getDoctorListLocal("1",null);
            if (c.moveToFirst()) {
                do {
                    drlist.add(c.getString(c.getColumnIndex("dr_id")));
                } while (c.moveToNext());
            }

            ArrayList<String> Doctor_list= cboDbHelper.getDoctor();
            int  totCalledDoctors = Doctor_list.size();
            int totPlannedDoctors = drlist.size();
            int totUnCalledDoctors = totPlannedDoctors - totCalledDoctors;
            if (listener != null) {
                if (totPlannedDoctors == totCalledDoctors ){
                    listener.onSuccess(null);
                }else {

                  /*  String alert = "Total Planned Doctors : "+totPlannedDoctors+"\n"+
                            "Total Called Doctors : "+totCalledDoctors+"\n"+
                            "Total Pending Doctors : "+totUnCalledDoctors+"\n"+
                            "Do you want to make offline calls to remaining doctors?";*/
                    listener.onError("Alert !!!", "Have you forgotten to report any met Doctor Today?");
                }
            }

        }else{
            if (listener != null){
                listener.onSuccess(null);
            }
        }
    }


    private void IfExpenseConditionFulfiled(Response listener){

        String expence_status = getmydata().get(2);

        if (expence_status.equals("") && (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Expense_NOT_REQUIRED").equals("Y"))) {
            if (listener != null) {
                listener.onError("Expense Pending!!!","Please submit Your Expense First... ");
            }
        } else if ( mandatory_pending_exp_head.size() != 0) {

            String pending_list = "";
            for (int i = 0; i < mandatory_pending_exp_head.size(); i++) {
               // pending_list += mandatory_pending_exp_head.get(i).get("PA_NAME") + "\n";
                pending_list+=mandatory_pending_exp_head.get(i).getName()+"\n";
            }
            if (listener != null) {
                listener.onError("Expenses Pending", pending_list);
            }
        }else{
            if (listener != null){
                listener.onSuccess(null);
            }
        }
    }


    private void IfRxConditionFulfiled(Response listener){

        if (IsTeniviaMenuAvilable() && Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("1")) {

            /// 0 - not mendetory
            /// 1 - all mendetory
            /// 2 - only those where RXGENYN = 1 are mendetory

            String RxValidateCondition = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DR_RXGEN_VALIDATE","0");
            int NoRxCalls = cboDbHelper.getmenu_count("Tenivia_traker");
            int NoRxCallsReqd = 0;//cboDbHelper.getDoctorName(RxValidateCondition).getCount();

            switch (RxValidateCondition){
                case "2":
                    NoRxCallsReqd = 1;

                    break;
                case "1":
                    NoRxCallsReqd = cboDbHelper.getDoctorName(RxValidateCondition).getCount();
                    break;
                default:
                    NoRxCallsReqd = 0;
            }

            if (NoRxCalls == 0) {
                if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "working_code", "W").equals("OCC") ||
                        customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "working_code", "W").equals("OSC") ||
                        customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "working_code", "W").equals("CSC") ||
                        (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "working_code", "W").contains("NR")
                                && customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "working_code", "W").contains("W"))) {

                    NoRxCalls = NoRxCallsReqd;
                }
            }

            if (NoRxCalls>= NoRxCallsReqd) {
                if (listener != null){
                    listener.onSuccess(null);
                }
            }else{
                if (listener != null) {
                    listener.onError(MyCustumApplication.getInstance().getTaniviaTrakerMenuName() + " Pending!!!","Please Visit "+ MyCustumApplication.getInstance().getTaniviaTrakerMenuName()+"..");
                }
            }

        }else{
            if (listener != null){
                listener.onSuccess(null);
            }
        }
    }



    private Boolean IsTeniviaMenuAvilable(){
        return customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Tenivia_NOT_REQUIRED","Y").equals("N") ||
                customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Rx_NOT_REQUIRED","Y").equals("N");
    }

    private Boolean IsExpCriteriaFulfiled(int NoOfDrCalled){
        String NO_DR_CALL_REQ= customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"NO_DR_CALL_REQ","0");
        if (NO_DR_CALL_REQ.equals("0")) return true;

        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code", "W").equals("OCC")||
                customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").equals("OSC")||
                customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").equals("CSC")||
                (
                        customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").contains("NR")
                                && customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").contains("W")
                )
                ){
            return true;
        }
        if (NoOfDrCalled < Integer.parseInt(NO_DR_CALL_REQ)) {

            AppAlert.getInstance().setNagativeTxt("Cancel").setPositiveTxt("OK").DecisionAlert(context,
                    "Incomplete Dr Calls !!!", "You have not completed "+NO_DR_CALL_REQ+" Dr. call of this route\nSo you are not eligible for TA/DA !!!",
                    new AppAlert.OnClickListener() {
                        @Override
                        public void onPositiveClicked(View item, String result) {
                            startActivity(new Intent(getActivity(), FinalSubmitDcr_new.class));
                            getActivity().overridePendingTransition(R.anim.fed_in, R.anim.fed_out);
                        }

                        @Override
                        public void onNegativeClicked(View item, String result) {

                        }
                    });

            return false;
        }else{
            return true;
        }
    }




    ////====================================================================x

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

                    customVariablesAndMethod.msgBox(context,"Please Swicth ON your GPS");
                    if (mode !=0){
                        customVariablesAndMethod.RequestGPSFromSetting(context);
                    }else{
                        customVariablesAndMethod.getGpsSetting(context);
                    }
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

    private void onClickChemistReminder(String title){
        if (IsCallAllowed()){
            Intent intent = new Intent(getActivity(), DCRCallActivity.class);
            intent.putExtra("mCall",new mChemistRc());
            intent.putExtra("title",title);
            startActivity(intent);
        }

    }

    //////////////////onClickDoctorSample//////////

    private void onClickDoctorSample() {
        v.startAnimation(anim);

        if (Custom_Variables_And_Method.INTERNET_REQ.equals("Y")) {
            if (!networkUtil.internetConneted(getActivity())) {
                customVariablesAndMethod.Connect_to_Internet_Msg(context);

            } else {
                if (!DCR_ID.equals("0")) {

                    //Intent i = new Intent(getActivity(), TabbedCallActivity.class);
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

                    customVariablesAndMethod.msgBox(context,"Please Swicth ON your GPS");
                    if (mode !=0){
                        customVariablesAndMethod.RequestGPSFromSetting(context);
                    }else{
                        customVariablesAndMethod.getGpsSetting(context);
                    }
                    //GPS Enabled
                    //  showSettings();

                } else {
                    //if (!customVariablesAndMethod.isBackgroundServiceRunning(context)) {
                       // context.startService(new Intent(context, MyLoctionService.class));
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
        getKeyList = new ArrayList<String>();
        for (String key : keyValue.keySet()) {
            getKeyList.add(key);
            count.add(get_count(key));
        }


        if(!getKeyList.contains("D_CHEMCALL") && !getKeyList.contains("D_RETCALL")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHEMIST_NOT_REQUIRED","Y");
        }else{
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHEMIST_NOT_REQUIRED","N");
        }

        if(!getKeyList.contains("D_CUST_CALL")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CUSTOMER_NOT_REQUIRED","Y");
        }else{
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CUSTOMER_NOT_REQUIRED","N");
        }

        if(!getKeyList.contains("D_STK_CALL")){
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
        if(!getKeyList.contains("D_RX_GEN")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Rx_NOT_REQUIRED","Y");
        }else {
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "Rx_NOT_REQUIRED", "N");
        }
        if(!getKeyList.contains("D_RX_GEN_NA")){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Rx_NA_NOT_REQUIRED","Y");
        }else {
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "Rx_NA_NOT_REQUIRED", "N");
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

       /* listOfAllTab.add(0,"Location Test");
        getKeyList.add(0,"D_LOC_TEST");
        count.add(0,-1);*/
    }

    private int get_count(String menu){
        int result=-1;
        String table="";
        Boolean flag=false;
        switch (menu){
            case "D_RCCALL":
                flag=true;
                table="phdcrdr_rc";
                break;
            case "D_DRCALL":
                flag=true;
                table="tempdr";
                break;
            case "D_CUST_CALL":
            case "D_CHEMCALL":
                flag=true;
                table="chemisttemp";
                break;
            case "D_RETCALL":
                flag=true;
                table="chemisttemp";
                break;
            case "D_CHEM_RCCALL":
                flag=true;
                table="phdcrchem_rc";
                break;
            case "D_STK_CALL":
                flag=true;
                table="phdcrstk";
                break;
            case "D_NLC_CALL":
                flag=true;
                table="NonListed_call";
                break;
            case "D_EXP":
                flag=false;
                table="Expenses";
                result = new OthExpenseDB(context).getNoOfRow();
                break;
            case "D_DR_RX":
            case "D_RX_GEN":
                flag=true;
                table="Tenivia_traker";
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
        if (result == 0 ){
            switch (table){
                case "chemisttemp":
                    if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHEMIST_NOT_VISITED","N").equals("Y")){
                        result = -1;
                    }
                    break;
                case "phdcrstk":
                    if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"STOCKIST_NOT_VISITED","N").equals("Y")){
                        result = -1;
                    }
                    break;
                default:
                    result = -1;
            }
        }

        if (table.equalsIgnoreCase("Tenivia_traker") && result == 1){
            HashMap<String, ArrayList<String>> tenivia_traker = cboDbHelper.getCallDetail ("tenivia_traker", "-1", "1");
            if (!tenivia_traker.isEmpty () && ( tenivia_traker.get ("id").contains ("-1"))) {
                result = 0;
            }
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


        AppAlert.getInstance().setNagativeTxt("Ok").setPositiveTxt("Postpond ?").DecisionAlert(context,
                "Activity found !!!", "Today You have an activity for "+cboDbHelper.getMenu("DCR", "D_FAR").get("D_FAR"),
                new AppAlert.OnClickListener() {
                    @Override
                    public void onPositiveClicked(View item, String result) {
                        Intent i = new Intent(context, PospondFarmerMeeting.class);
                        startActivity(i);
                    }

                    @Override
                    public void onNegativeClicked(View item, String result) {

                    }
                });


    }

    private void ShowAlertforNOCalls(){


        AppAlert.getInstance().setNagativeTxt("NO").setPositiveTxt("YES").DecisionAlert(context,
                "No Calls found !!!", "Your DCR has-been Locked, Do You want to reset your DCR?",
                new AppAlert.OnClickListener() {
                    @Override
                    public void onPositiveClicked(View item, String result) {
                        Intent i = new Intent(context, DCR_Summary_new.class);
                        i.putExtra("who", 2);
                        startActivity(i);
                    }

                    @Override
                    public void onNegativeClicked(View item, String result) {

                    }
                });


    }

    private Boolean IsAllowedToCallAtThisTime(){
        Float FIRST_CALL_LOCK_TIME= Float.valueOf(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FIRST_CALL_LOCK_TIME","0"));
        if (FIRST_CALL_LOCK_TIME==0) return true;

        if (checkforCalls()) return true;
        Float current_time= Float.valueOf(customVariablesAndMethod.getCurrentBestTime(context));

        if (current_time>FIRST_CALL_LOCK_TIME && !customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CALL_UNLOCK_STATUS","[CALL_UNLOCK]").contains("[CALL_UNLOCK]")){
            if (!networkUtil.internetConneted(getActivity())) {

                AppAlert.getInstance().setNagativeTxt("Cancel").setPositiveTxt("Check").DecisionAlert(context,
                        "Call Locked", "Your Calls have been Locked... \nYou must have made your first Call before " + FIRST_CALL_LOCK_TIME + " O'Clock\n" +
                                "Switch ON your Internet and Check if Unlocked...",
                        new AppAlert.OnClickListener() {
                            @Override
                            public void onPositiveClicked(View item, String result) {
                                CheckIfCallsUnlocked("C");
                            }

                            @Override
                            public void onNegativeClicked(View item, String result) {

                            }
                        });

            }else{
                CheckIfCallsUnlocked("C");
            }
            return false;
        }

        return true;
    }





    private Boolean IsRouteApproved(){
        String DIVERTLOCKYN= customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DIVERTLOCKYN","");
        if (!DIVERTLOCKYN.equals("Y")) return true;
            if (!networkUtil.internetConneted(getActivity())) {

                AppAlert.getInstance().setNagativeTxt("Cancel").setPositiveTxt("Check").DecisionAlert(context,
                        "Incomplete Dr Calls !!!", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,
                                "APPROVAL_MSG","Your Route Approval is Pending... \nYou Route must be approved first !!!\n" +
                                        "Switch ON your Internet and Check if Approved..."),
                        new AppAlert.OnClickListener() {
                            @Override
                            public void onPositiveClicked(View item, String result) {
                                CheckIfCallsUnlocked("A");
                            }

                            @Override
                            public void onNegativeClicked(View item, String result) {

                            }
                        });



            }else{
                CheckIfCallsUnlocked("A");
            }


        return false;
    }

    private void CheckIfCallsUnlocked(String type) {

        new Service_Call_From_Multiple_Classes().CheckIfCallsUnlocked(context,type);
    }


    ///////////////



}
