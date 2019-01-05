package com.cbo.cbomobilereporting.ui_new.dcr_activities.root;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui.NonWorking_DCR;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.FinalSubmitDcr_new;
import com.cbo.cbomobilereporting.ui_new.for_all_activities.CustomWebView;
import com.cbo.cbomobilereporting.ui_new.personal_activities.Add_Delete_Leave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import locationpkg.Const;
import services.CboServices;
import utils.CBOUtils.SystemArchitecture;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.Area_Dialog;
import utils_new.CustomTextToSpeech;
import utils_new.Custom_Variables_And_Method;
import utils_new.GPS_Timmer_Dialog;
import utils_new.Route_Dialog;
import utils_new.Work_With_Dialog;

public class DCR_Root_new extends AppCompatActivity {

    EditText date, wwith, loc, root, edt_get_area,late_remark,divert_remark;
    Spinner work_type;

    Button save, Back, get_workwith, getRoot, get_area_again;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    SpinAdapter adapter;
    int PA_ID;
    CheckBox ROUTEDIVERTYN,DIVERTWWYN;

    LinearLayout lay1, areaLayout;
    LinearLayout rootLayout;

    String workwith1 = "", workwith2 = "", workwith34 = "", workWith4 = "", workWith5 = "", workWith6 = "", workWith7 = "", workWith8 = "", address = "", work_withme = "", work_name = "";

    String real_date = null;
    String work_val = "",work_type_code = "";

    String work_with_name = "", work_with_id = "", area_name = "", area_id = "";
    LinearLayout locationLayout;

    CBO_DB_Helper cbo_helper;
    ArrayList<SpinnerModel> getworkingType = new ArrayList<SpinnerModel>();
    String paid1 = "";
    String root_id = "";
    String root_name = "", myArea;
    String Root_Needed;
    LinearLayout dcrPendingDatesLayout;
    TextView dcrpendingDates, hader_text;
    MyCustomMethod customMethod;
    android.support.v7.widget.Toolbar toolbar;
    Intent intent;


    public ProgressDialog progress1;
    String work_type_Selected;
    private  static final int MESSAGE_INTERNET_WORKTYPE=1,MESSAGE_INTERNET_SUBMIT_WORKING=2,MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL=3,GPS_TIMMER=4;
    private  static final int WORK_WITH_DILOG=5,ROUTE_DILOG=6,AREA_DILOG=7;
    private Location currentBestLocation;
    String mLatLong;
    String mAddress,LocExtra="";

    String fmcg_Live_Km = "";
    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            synchronized (this) {
                customMethod.startAlarm10Sec();
            }
        }
    };
    Runnable r2 = new Runnable() {
        @Override
        public void run() {
            synchronized (this) {
                customMethod.startAlarmIn10Minute();

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcr__root_new);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        hader_text = (TextView) findViewById(R.id.hadder_text_1);
        hader_text.setText("Dcr Day Open");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }
        dcrpendingDates = (TextView) findViewById(R.id.dcr_pending_dates_route);
        dcrPendingDatesLayout = (LinearLayout) findViewById(R.id.pending_dcr_dates_layouts_route);
        date = (EditText) findViewById(R.id.rootdate);
        edt_get_area = (EditText) findViewById(R.id.edt_get_area);
        late_remark = (EditText) findViewById(R.id.late_remark);
        divert_remark = (EditText) findViewById(R.id.Divert_remark);
        wwith = (EditText) findViewById(R.id.rootworkwith);

        loc = (EditText) findViewById(R.id.rootloc_dcr_open);



        work_type = (Spinner) findViewById(R.id.root_worktype);
        save = (Button) findViewById(R.id.rootsave);
        locationLayout = (LinearLayout) findViewById(R.id.RootlocLayoutDopen);
        get_workwith = (Button) findViewById(R.id.rootget);
        lay1 = (LinearLayout) findViewById(R.id.root_open_layout1);
        areaLayout = (LinearLayout) findViewById(R.id.areaLayout_Dopen);
        ROUTEDIVERTYN= (CheckBox) findViewById(R.id.ROUTEDIVERTYN);
        DIVERTWWYN= (CheckBox) findViewById(R.id.DIVERTWWYN);

        rootLayout = (LinearLayout) findViewById(R.id.dopen_layout12);
        root = (EditText) findViewById(R.id.rootroot);
        getRoot = (Button) findViewById(R.id.rootgetroot);
        get_area_again = (Button) findViewById(R.id.get_ara_again);

        context=this;
        progress1 = new ProgressDialog(this);
        customVariablesAndMethod= Custom_Variables_And_Method.getInstance();
        new SystemArchitecture(context).getDEVICE_ID(context);
        Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);

        cbo_helper = new CBO_DB_Helper(context);
        Back = (Button) findViewById(R.id.rootback);

        customMethod = new MyCustomMethod(context);
        PA_ID = Custom_Variables_And_Method.PA_ID;
        paid1 = "" + PA_ID;

        currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation",Location.class);

        mLatLong = Custom_Variables_And_Method.GLOBAL_LATLON;
        mAddress = Custom_Variables_And_Method.global_address;

        if (currentBestLocation!=null) {
            LocExtra = "Lat_Long " + currentBestLocation.getLatitude() + "," + currentBestLocation.getLongitude() + ", Accuracy " + currentBestLocation.getAccuracy() + ", Time " + currentBestLocation.getTime() + ", Speed " + currentBestLocation.getSpeed() + ", Provider " + currentBestLocation.getProvider();
        }

        Custom_Variables_And_Method.ROOT_NEEDED =customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"root_needed");
        Root_Needed = Custom_Variables_And_Method.ROOT_NEEDED;
        fmcg_Live_Km =customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"live_km");

        Custom_Variables_And_Method.DCR_DATE = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DATE_NAME");
        date.setText(Custom_Variables_And_Method.DCR_DATE);
        Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DATE");
        real_date = Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT;

        ImageView imgSpin = (ImageView) findViewById(R.id.spinner_img);
        imgSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                work_type.performClick();
            }
        });

        if ((Custom_Variables_And_Method.DcrPending_datesList.size() == 1) || (Custom_Variables_And_Method.DcrPending_datesList.size() == 0)) {
            dcrPendingDatesLayout.setVisibility(View.GONE);
        } else {
            dcrPendingDatesLayout.setVisibility(View.VISIBLE);
            dcrpendingDates.setSelected(true);
            dcrpendingDates.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            dcrpendingDates.setText(Custom_Variables_And_Method.DcrPending_datesList.toString());

        }
        setAddressToUI();

        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_ADDAREANA").equalsIgnoreCase("Y")){
            areaLayout.setVisibility(View.GONE);
        }

        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"ROUTEDIVERTYN").equalsIgnoreCase("Y")){
            ROUTEDIVERTYN.setVisibility(View.VISIBLE);
            //areaLayout.setVisibility(View.GONE);
        }

        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DIVERTWWYN").equalsIgnoreCase("Y")){
            DIVERTWWYN.setVisibility(View.VISIBLE);
        }

        divert_remark.setText("");
        ROUTEDIVERTYN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b && customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DIVERT_REMARKYN","N").equalsIgnoreCase("Y")){
                    divert_remark.setVisibility(View.VISIBLE);
                    divert_remark.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"sDivert_Remark",""));
                }else {
                    divert_remark.setVisibility(View.GONE);
                }
            }
        });

        if (Custom_Variables_And_Method.location_required.equals("Y")) {
            locationLayout.setVisibility(View.VISIBLE);
        }


        intent=getIntent();
        if(intent.getStringExtra("plan_type").equals("p")) {

            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"area_name","");
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"work_with_name","");
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"work_with_individual_name","");
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"route_name","");
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"sDivert_Remark","");
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ROUTEDIVERTYN_Checked","N");
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DIVERTWWYN_Checked","0");
            if (!customVariablesAndMethod.IsBackDate(context) ) {
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"IsBackDate","1"); //not back date entry
            }else{
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"IsBackDate","0");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"BackDateReason","");
            }

            //Start of call to service

            HashMap<String, String> request = new HashMap<>();
            request.put("sCompanyFolder", cbo_helper.getCompanyCode());
            request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
            request.put("sDCR_DATE", "" + real_date);

            ArrayList<Integer> tables = new ArrayList<>();
            tables.add(0);

            progress1.setMessage("Please Wait.. \n Fetching your worktype");
            progress1.setCancelable(false);
            progress1.show();

            new CboServices(this, mHandler).customMethodForAllServices(request, "DCRWORKINGTYPE_MOBILE_2", MESSAGE_INTERNET_WORKTYPE, tables);

            //End of call to service
        }else {
            hader_text.setText("Dcr Day Replan");
            work_val=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_head","Working" );
            work_type_code=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code", "W");
            //getworkingType.add(new SpinnerModel("Working","W"));
            getworkingType.add(new SpinnerModel(work_val,work_type_code));

            adapter=new SpinAdapter(getApplicationContext(),R.layout.spin_row,getworkingType);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            work_type.setAdapter(adapter);
            work_type.setEnabled(false);

            work_with_name=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"route_Ww_Name");
            work_with_id=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"route_Ww_ID");
            root_name=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"route_Route_Name");
            root_id=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"route_Route_ID");
            area_name=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"route_area_Name");
            area_id=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"route_area_ID");

            if((work_with_name !=null)&&(area_name !=null))
            {
                wwith.setText(work_with_name);
                root.setText(root_name);
                edt_get_area.setText(area_name);
            }

        }

        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"IsBackDate","0").equals("1") ) {
            late_remark.setText("");
            late_remark.setVisibility(View.GONE);
        }else{
            late_remark.setVisibility(View.VISIBLE);
            late_remark.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"BackDateReason",""));
        }

        work_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View v,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                try {
                    work_val = ((TextView) v.findViewById(R.id.spin_name)).getText().toString();
                    work_type_code = ((TextView) v.findViewById(R.id.spin_id)).getText().toString();
                    Custom_Variables_And_Method.work_val = work_val;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String code = work_type_code;
                if (work_type_code.contains("NR")){
                    code ="W";
                }
                switch (code){
                    case "OCC" :
                    case "OSC" :
                    case "CSC" :
                    case "W" :
                        lay1.setVisibility(View.VISIBLE);
                        rootLayout.setVisibility(View.VISIBLE);
                        areaLayout.setVisibility(View.VISIBLE);

                        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"ROUTEDIVERTYN").equalsIgnoreCase("Y")){
                            ROUTEDIVERTYN.setVisibility(View.VISIBLE);
                        }
                        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DIVERTWWYN").equalsIgnoreCase("Y")){
                            DIVERTWWYN.setVisibility(View.VISIBLE);
                        }

                        break;
                    case "HM" :
                        lay1.setVisibility(View.GONE);
                        rootLayout.setVisibility(View.GONE);
                        areaLayout.setVisibility(View.GONE);
                        ROUTEDIVERTYN.setVisibility(View.GONE);
                        DIVERTWWYN.setVisibility(View.GONE);
                        break ;
                    case "LR" :
                        String url = cbo_helper.getMenuUrl("TRANSACTION", "T_LR1");
                        String url1 = cbo_helper.getMenuUrl("PERSONAL_INFO", "LEAVE");
                        if (url != null && !url.equals("")) {
                            if ( url.contains("?")) {
                                url = url +  "&DATE=" + Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT ;
                            }else{
                                url = url + "?DATE=" + Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT;
                            }
                            Intent i = new Intent(context, CustomWebView.class);
                            i.putExtra("A_TP", url);
                            i.putExtra("Title",  "Leave Request");
                            startActivity(i);
                        } else if (url1 != null && !url1.equals("")) {
                            if ( url1.contains("?")) {
                                url1 = url1 +  "&DATE=" + Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT ;
                            }else{
                                url1 = url1 + "?DATE=" + Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT;
                            }
                            Intent i = new Intent(context, CustomWebView.class);
                            i.putExtra("A_TP", url1);
                            i.putExtra("Title",  "Leave Request");
                            startActivity(i);
                        } else {
                            Intent leaveRequestActivity = new Intent(context, Add_Delete_Leave.class);
                            startActivity(leaveRequestActivity);
                        }
                        finish();
                        break;
                    case "M" :
                        lay1.setVisibility(View.VISIBLE);
                        rootLayout.setVisibility(View.VISIBLE);
                        areaLayout.setVisibility(View.VISIBLE);
                        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"ROUTEDIVERTYN").equalsIgnoreCase("Y")){
                            ROUTEDIVERTYN.setVisibility(View.VISIBLE);
                        }
                        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DIVERTWWYN").equalsIgnoreCase("Y")){
                            DIVERTWWYN.setVisibility(View.VISIBLE);
                        }
                        break ;
                    default:

                        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"ROUTEDIVERTYN").equalsIgnoreCase("Y")){
                            ROUTEDIVERTYN.setVisibility(View.VISIBLE);
                        }

                        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DIVERTWWYN").equalsIgnoreCase("Y")){
                            DIVERTWWYN.setVisibility(View.VISIBLE);
                        }

                        if (work_type_code.contains("_")){
                            if (work_type_code.contains("_W")) {
                                lay1.setVisibility(View.GONE);
                            }else{
                                lay1.setVisibility(View.VISIBLE);
                            }

                            if (work_type_code.contains("_R")) {
                                rootLayout.setVisibility(View.GONE);
                                ROUTEDIVERTYN.setVisibility(View.GONE);
                            }else{
                                rootLayout.setVisibility(View.VISIBLE);
                            }

                            if (work_type_code.contains("_A")) {
                                areaLayout.setVisibility(View.GONE);
                            }else{
                                areaLayout.setVisibility(View.VISIBLE);
                            }
                        }else{
                            lay1.setVisibility(View.VISIBLE);
                            rootLayout.setVisibility(View.VISIBLE);
                            areaLayout.setVisibility(View.VISIBLE);
                        }


                }

                ROUTEDIVERTYN.setChecked(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"ROUTEDIVERTYN_Checked","N").equals("Y"));
                DIVERTWWYN.setChecked(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DIVERTWWYN_Checked","0").equals("1"));

                if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_ADDAREANA").equalsIgnoreCase("Y")){
                    areaLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                lay1.setVisibility(View.VISIBLE);
                rootLayout.setVisibility(View.VISIBLE);

            }
        });

        //===============================================insert====================================================================================
        get_workwith.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //showSelectColoursDialog1();
                /*Intent i = new Intent(getApplicationContext(), Dcr_Workwith.class);
                i.putExtra("sDCR_DATE", "" + real_date);
                startActivityForResult(i, 0);*/
                Bundle b=new Bundle();
                b.putString("sDCR_DATE", "" + real_date);
                b.putString("header", "Work-With");
                b.putString("PlanType",intent.getStringExtra("plan_type"));
                b.putString("DIVERTWWYN",DIVERTWWYN.isChecked() ? "1":"0");
                b.putString("sWorking_Type",work_val);

                new Work_With_Dialog(context,mHandler,b,WORK_WITH_DILOG).show();
            }
        });

//====================================onclick of + sing for select  Area for work with area=================================================

        getRoot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                work_name = wwith.getText().toString();
                if (Custom_Variables_And_Method.pub_desig_id.equals("1") && checkforCalls()) {
                    customVariablesAndMethod.getAlert(context,"Call Found","Can not change Route !!! \nSome Calls found in your Day Summary.\n" +
                            "Else Reset your Day Plan from Constants");
                }else if (work_name.equals("") &&  !work_type_code.contains("_W")) {
                    customVariablesAndMethod.msgBox(context,"Please Select Work with First...");
                } else {

                    //Intent i = new Intent(context, DcrRoot.class);
                    String sAllYn="N",dcr_root_divert="0";
                    if(ROUTEDIVERTYN.isChecked()){
                        sAllYn="Y";
                        dcr_root_divert="1";
                    }
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"dcr_root_divert",dcr_root_divert);

                    Bundle b=new Bundle();
                    b.putString("sAllYn", sAllYn);
                    b.putString("header", "Route List");
                    new Route_Dialog(context,mHandler,b,ROUTE_DILOG).show();

                    /*i.putExtra("sAllYn",sAllYn);
                    startActivityForResult(i, 1);*/
                }


            }
        });

        get_area_again.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                work_name = wwith.getText().toString();
                root_name = root.getText().toString();
                if (work_name.equals("") && !work_type_code.contains("_W")) {
                    customVariablesAndMethod.msgBox(context,"Please Select Work with First...");
                } else if (root_name.equals("")) {
                    customVariablesAndMethod.msgBox(context,"Please Select Route Fisrt .....");
                } else {
                    Bundle b=new Bundle();
                    b.putString("sAllYn","0");
                    b.putString("header", "Additional Area List");
                    new Area_Dialog(context,mHandler,b,AREA_DILOG).show();
                }


            }
        });
        Back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setAddressToUI();
                if (loc.getText().toString().equals("")) {
                    loc.setText("UnKnown Location");
                }
                myArea = edt_get_area.getText().toString();
                if (myArea.equals("")) {
                    myArea = root.getText().toString();
                }
                address = loc.getText().toString();
                if (address.equals("")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setTitle("Network Error");
                    builder1.setMessage(" Slow Network Connection" + "\n" + "Please Re-Start Your Device And Try Again .....");
                    builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            finish();
                        }
                    });
                    builder1.show();
                }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                } else {
                    startSubmitDCR();
                }
            }


        });


    }


    private void startSubmitDCR(){
        /*cbo_helper.deletedcrFromSqlite();
        cbo_helper.deleteUtils();
        cbo_helper.deleteDCRDetails();*/

        if(intent.getStringExtra("plan_type").equals("p")) {
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"srno", "0");
            Float FIRST_CALL_LOCK_TIME= Float.valueOf(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FIRST_CALL_LOCK_TIME","0"));
            if (FIRST_CALL_LOCK_TIME==0) {
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CALL_UNLOCK_STATUS","[CALL_UNLOCK]");
            }else{
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CALL_UNLOCK_STATUS","");
            }

        }
/*
        long val = cbo_helper.insertUtils(Custom_Variables_And_Method.pub_area);
        Log.e("^^^^^^^^^^^^^^^", "" + val);
        long val2 = cbo_helper.insertDcrDetails(Custom_Variables_And_Method.DCR_ID, Custom_Variables_And_Method.pub_area);
        Log.e("getting dcr id", "" + val2);*/

        String routeCheck = root_id;
        int remarkLenght= Integer.parseInt(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_LETREMARK_LENGTH","0"));
        if (work_type_code.isEmpty()) {
            customVariablesAndMethod.getAlert(context,"WorkType !!","Please Select worktype.....");
        }else if (divert_remark.getVisibility()==View.VISIBLE && divert_remark.getText().toString().trim().isEmpty()) {
            customVariablesAndMethod.getAlert(context,"Divert Remark !!!","Please enter Divert Remark");
        }else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"IsBackDate","1").equals("0") && late_remark.getText().toString().length()< remarkLenght ) {
            customVariablesAndMethod.getAlert(context,"Back-Date entry !!!","Please enter the reason for Back-Date entry in not less then "+remarkLenght+" letters");
        }else if (routeCheck.contains("^")) {

            ArrayList<String> splitData = new ArrayList<String>();
            splitData = customVariablesAndMethod.splitRouteData(routeCheck);
            root_id = splitData.get(0);
            String freq = splitData.get(1);
            String visited = splitData.get(2);
            Integer f = Integer.parseInt(freq);
            Integer vis = Integer.parseInt(visited);
            if (f > 0) {
                if (vis >= f) {
                    customVariablesAndMethod.msgBox(context,"Route Visit Frequency is -" + f + " \n You already Visited - " + vis);
                } else {
                    new GPS_Timmer_Dialog(context,mHandler,"Day Plan in Process...",GPS_TIMMER).show();
                    //submitDCR();
                }
            }
        } else {
            new GPS_Timmer_Dialog(context,mHandler,"Day Plan in Process...",GPS_TIMMER).show();
            //submitDCR();
        }
    }

    //==========================================================  onCreate finish  ================================================

    private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contex, Intent intent) {
            Location location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);
            startSubmitDCR();

        }
    };

    private Boolean checkforCalls(){
        int result=0;
        //result+=cbo_helper.getmenu_count("phdcrdr_rc");
        result+=cbo_helper.getmenu_count("tempdr");
        result+=cbo_helper.getmenu_count("chemisttemp");
        //result+=cbo_helper.getmenu_count("phdcrstk");
        if (result==0){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(progress1 != null && progress1.isShowing()){
            progress1.dismiss();

        }

    }

    @Override
    public void onRestart() {
        super.onRestart();

        if(progress1 == null ){
           progress1 = new ProgressDialog(this);
        }

    }

    protected void onActivityResult(int reqcode, int rescode, Intent iob) {

        switch (reqcode) {

            case 0:
                if (rescode==RESULT_OK) {
                    Bundle b1 = iob.getExtras();
                    work_with_name = b1.getString("workwith_name");
                    work_with_id = b1.getString("workwith_id");

                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "route_Ww_Name", work_with_name);
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "route_Ww_ID", work_with_id);
                }
                break;

            case 1:
                if (rescode==RESULT_OK) {
                    Bundle b1 = iob.getExtras();
                    root_name = b1.getString("route_name");

                    root_id = b1.getString("route_id");
                    if ((root_id == null) || (root_id.equals(""))) {
                        root_id = "";
                    }


                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "route_Route_Name", root_name);
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "route_Route_ID", root_id);
                }
                break;
            case 2:
                if (rescode==RESULT_OK) {
                    Bundle b1 = iob.getExtras();
                    area_name = b1.getString("area_name");
                    area_id = b1.getString("area_id");


                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "route_area_Name", area_name);
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "route_area_ID", area_id);
                }
                break;

        }

        wwith.setText(work_with_name);
        root.setText(root_name);
        edt_get_area.setText(area_name);
    }

    //=====================================================================================================================
    public void getWorkWith() {
        String part1 = "", part2 = "", part3 = "", part4 = "", part5 = "", part6 = "", part7 = "", part8 = "";
        String[] parts = work_with_id.split(",");
        if (parts.length == 1) {
            part1 = parts[0];
        }
        if (parts.length == 2) {
            part1 = parts[0];
            part2 = parts[1];
        }
        if (parts.length == 3) {
            part1 = parts[0];
            part2 = parts[1];
            part3 = parts[2];
        }
        if (parts.length == 4) {
            part1 = parts[0];
            part2 = parts[1];
            part3 = parts[2];
            part4 = parts[3];
        }
        if (parts.length == 5) {
            part1 = parts[0];
            part2 = parts[1];
            part3 = parts[2];
            part4 = parts[3];
            part5 = parts[4];
        }
        if (parts.length == 6) {
            part1 = parts[0];
            part2 = parts[1];
            part3 = parts[2];
            part4 = parts[3];
            part5 = parts[4];
            part6 = parts[5];
        }
        if (parts.length == 7) {
            part1 = parts[0];
            part2 = parts[1];
            part3 = parts[2];
            part4 = parts[3];
            part5 = parts[4];
            part6 = parts[5];
            part7 = parts[6];
        }
        if (parts.length == 8) {
            part1 = parts[0];
            part2 = parts[1];
            part3 = parts[2];
            part4 = parts[3];
            part5 = parts[4];
            part6 = parts[5];
            part7 = parts[6];
            part8 = parts[7];
        }

        workwith1 = part1;

        workwith2 = part2;

        workwith34 = part3;
        workWith4 = part4;
        workWith5 = part5;
        workWith6 = part6;
        workWith7 = part7;
        workWith8 = part8;
    }

    public void submitWorking() {

            getWorkWith();
            /*cbo_helper.delete_phdoctor();
            cbo_helper.deleteChemist();*/

            String dcr_root_divert=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_root_divert");

            //Start of call to service

            HashMap<String,String> request=new HashMap<>();
            request.put("sCompanyFolder",cbo_helper.getCompanyCode());
            request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
            request.put("sDCR_DATE", "" + real_date);
            request.put("sSTATION",  "" + myArea);
            request.put("iTOTAL_DR", "1");
            request.put("iIN_TIME", "99");
            request.put("iOUT_TIME", "0.0");
            request.put("sM_E1", "");
            request.put("sM_E2", "");
            request.put("sM_E3", "");
            request.put("iIN_TIME1",  "0.0");
            request.put("iIN_TIME2",  "0.0");
            request.put("iIN_TIME3",  "0.0");
            request.put("iOUT_TIME1",  "0.0");
            request.put("iOUT_TIME2",  "0.0");
            request.put("iOUT_TIME3",  "0.0");
            request.put("iWORK_WITH1", workwith1);
            request.put("iWORK_WITH2", workwith2);
            request.put("iWORK_WITH3", workwith34);
            request.put("sDA_TYPE", "" + work_val);
            request.put("iDISTANCE_ID",  "" + root_id);
            request.put("sREMARK", "");
            request.put("sLOC1",  mLatLong +"@"+LocExtra+ "!^" + mAddress);
            request.put("iRETID", "0");
            request.put("sWorkingType", work_val);
            request.put("iWORK_WITH4", workWith4);
            request.put("iWORK_WITH5", workWith5);
            request.put("iWORK_WITH6", workWith6);
            request.put("iWORK_WITH7", workWith7);
            request.put("iWORK_WITH8", workWith8);
            request.put("iDivertYn", dcr_root_divert);
            request.put("sLATE_REMARK", late_remark.getText().toString());
            request.put("sMOBILE_TIME", customVariablesAndMethod.currentTime(context,false));
            request.put("sINDP_WW", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_with_individual_id",""));
            request.put("sDivert_Remark",divert_remark.getText().toString());
            request.put("iDIVERTWWYN",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DIVERTWWYN_Checked","0"));

            ArrayList<Integer> tables=new ArrayList<>();
            tables.add(0);

            progress1.setMessage("Please Wait.. \n" +
                    "Your Day is being Planed");
            progress1.setCancelable(false);
            progress1.show();

            new CboServices(this,mHandler).customMethodForAllServices(request,"DCR_COMMIT_ROUTE_9",MESSAGE_INTERNET_SUBMIT_WORKING,tables);

            //End of call to service
            work_type_Selected="w";

    }

    public void submitLeave(){

       /* cbo_helper.delete_phdoctor();
        cbo_helper.deleteChemist();*/
        String dcr_root_divert=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_root_divert");
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cbo_helper.getCompanyCode());
        request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sDCR_DATE", "" + real_date);
        request.put("sSTATION", "" + work_val);
        request.put("iTOTAL_DR", "1");
        request.put("iIN_TIME", "99");
        request.put("iOUT_TIME", "0.0");
        request.put("sM_E1", "");
        request.put("sM_E2", "");
        request.put("sM_E3", "");
        request.put("iIN_TIME1", "0.0");
        request.put("iIN_TIME2", "0.0");
        request.put("iIN_TIME3", "0.0");
        request.put("iOUT_TIME1", "0.0");
        request.put("iOUT_TIME2", "0.0");
        request.put("iOUT_TIME3", "0.0");
        request.put("iWORK_WITH1", "");
        request.put("iWORK_WITH2", "");
        request.put("iWORK_WITH3", "");
        request.put("sDA_TYPE", "" + work_val);
        request.put("iDISTANCE_ID", "0");
        request.put("sREMARK", "0");
        request.put("sLOC1",   mLatLong +"@"+LocExtra+ "!^" + mAddress);
        request.put("iRETID", "0");
        request.put("iWORK_WITH4", "0");
        request.put("iWORK_WITH5", "0");
        request.put("iWORK_WITH6", "0");
        request.put("iWORK_WITH7", "0");
        request.put("iWORK_WITH8", "0");
        request.put("sWorkingType", work_val);
        request.put("iDivertYn", dcr_root_divert);
        request.put("sLATE_REMARK", late_remark.getText().toString());
        request.put("sMOBILE_TIME", customVariablesAndMethod.currentTime(context,false));
        request.put("sINDP_WW", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_with_individual_id",""));
        request.put("sDivert_Remark",divert_remark.getText().toString());
        request.put("iDIVERTWWYN",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DIVERTWWYN_Checked","0"));

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait.. \n" +
                "Your Day is being Planed");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(this,mHandler).customMethodForAllServices(request,"DCR_COMMIT_ROUTE_9",MESSAGE_INTERNET_SUBMIT_WORKING,tables);

        //End of call to service
        work_type_Selected="l";
    }

    public void submitforMeeting(){
        getWorkWith();
       /* cbo_helper.delete_phdoctor();
        cbo_helper.deleteChemist();*/

        String dcr_root_divert=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_root_divert");

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cbo_helper.getCompanyCode());
        request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sDCR_DATE", "" + real_date);
        request.put("sSTATION",  "" + work_val);
        request.put("iTOTAL_DR", "1");
        request.put("iIN_TIME", "99");
        request.put("iOUT_TIME", "0.0");
        request.put("sM_E1", "");
        request.put("sM_E2", "");
        request.put("sM_E3", "");
        request.put("iIN_TIME1",  "0.0");
        request.put("iIN_TIME2",  "0.0");
        request.put("iIN_TIME3",  "0.0");
        request.put("iOUT_TIME1",  "0.0");
        request.put("iOUT_TIME2",  "0.0");
        request.put("iOUT_TIME3",  "0.0");
        request.put("iWORK_WITH1", workwith1);
        request.put("iWORK_WITH2", workwith2);
        request.put("iWORK_WITH3", workwith34);
        request.put("sDA_TYPE", "" + work_val);
        request.put("iDISTANCE_ID",  "" + root_id);
        request.put("sREMARK", "");
        request.put("sLOC1",  mLatLong +"@"+LocExtra+ "!^" + mAddress);
        request.put("iRETID", "0");
        request.put("sWorkingType", work_val);
        request.put("iWORK_WITH4", workWith4);
        request.put("iWORK_WITH5", workWith5);
        request.put("iWORK_WITH6", workWith6);
        request.put("iWORK_WITH7", workWith7);
        request.put("iWORK_WITH8", workWith8);
        request.put("iDivertYn", dcr_root_divert);
        request.put("sLATE_REMARK", late_remark.getText().toString());
        request.put("sMOBILE_TIME", customVariablesAndMethod.currentTime(context,false));
        request.put("sINDP_WW", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_with_individual_id",""));
        request.put("sDivert_Remark",divert_remark.getText().toString());
        request.put("iDIVERTWWYN",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DIVERTWWYN_Checked","0"));

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait.. \n" +
                "Your Day is being Planed");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(this,mHandler).customMethodForAllServices(request,"DCR_COMMIT_ROUTE_9",MESSAGE_INTERNET_SUBMIT_WORKING,tables);

        //End of call to service
        work_type_Selected="n";
    }


    public void setReultForNonWork() {
        if ((Custom_Variables_And_Method.DCR_ID .equals("0") ) || (Custom_Variables_And_Method.DCR_ID != null)) {
            startActivity(new Intent(getApplicationContext(), NonWorking_DCR.class));
        } else {
            customVariablesAndMethod.msgBox(context,"Data Insertion Fail.....");
        }
    }

    public void submitDCR() {
        work_name = wwith.getText().toString();
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ROUTEDIVERTYN_Checked",ROUTEDIVERTYN.isChecked() ? "Y":"N") ;
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DIVERTWWYN_Checked",DIVERTWWYN.isChecked() ? "1":"0") ;

        mLatLong=customVariablesAndMethod.get_best_latlong(context);
        String code = work_type_code;
        if (work_type_code.contains("NR")){
            code ="W";
        }
        switch (code){
            case "OCC" :
            case "OSC" :
            case "CSC" :
            case "W" :
                if (work_name.equals("")) {
                    Toast.makeText(context, "Select Work With", Toast.LENGTH_SHORT).show();
                } else if (root_name.equals("")) {
                    customVariablesAndMethod.msgBox(context,"Please Select Route Fisrt .....");
                } else {

                    submitWorking();

                }
                break;
            case "HM" :
                submitLeave();
                break ;
            case "M" :
                if (root.getText().toString().equals("")) {
                    customVariablesAndMethod.msgBox(context,"Select Your Route First...");
                } else {
                    getWorkWith();
                    submitforMeeting();
                }
                break ;
            /*case "WBZ" :
                getWorkWith();
                Custom_Variables_And_Method.SELECTED_AREA = area.getText().toString();
                submitNonWorking();
                break ;*/
            default:
                if (work_type_code.contains("_")){
                    work_name = wwith.getText().toString();
                    if (work_name.equals("") && !work_type_code.contains("_W")) {
                        Toast.makeText(context, "Select Work With", Toast.LENGTH_SHORT).show();
                    } else if (root_name.equals("") && !work_type_code.contains("_R")) {
                        customVariablesAndMethod.msgBox(context,"Please Select Route Fisrt .....");
                    } else {
                        getWorkWith();
                        submitforMeeting();
                    }
                }else{
                    if (root.getText().toString().equals("")) {
                        customVariablesAndMethod.msgBox(context,"Select Your Route First...");
                    } else {
                        getWorkWith();
                        submitforMeeting();
                    }
                }

        }

    }

    public String getArea() {
        String ActArea = "";
        if (work_val.equals("Working")) {

            ActArea = root.getText().toString();
        } else {
            ActArea = work_val;
        }
        return ActArea;
    }

    public void setAddressToUI() {

        if (Custom_Variables_And_Method.global_address != null || !Custom_Variables_And_Method.global_address.equals("")) {
            loc.setText(Custom_Variables_And_Method.global_address);
        } else if (loc.getText().toString().equals("")) {
            loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);

        } else {
            loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();

        super.onBackPressed();
    }



    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b1;
            switch (msg.what) {
                case MESSAGE_INTERNET_WORKTYPE:

                    if ((null != msg.getData())) {

                        parser_worktype(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_SUBMIT_WORKING:

                    if ((null != msg.getData())) {

                        parser_submit_for_working(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL:

                    if ((null != msg.getData())) {

                        parser_DCRCOMMIT_DOWNLOADALL(msg.getData());

                    }
                    break;
                case GPS_TIMMER:
                    submitDCR();
                    break;
                case WORK_WITH_DILOG:
                    b1 = msg.getData();
                    work_with_name = b1.getString("workwith_name");
                    work_with_id = b1.getString("workwith_id");

                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "route_Ww_Name", work_with_name);
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "route_Ww_ID", work_with_id);
                    wwith.setText(work_with_name);

                    break;
                case ROUTE_DILOG:
                    b1 = msg.getData();
                    root_name = b1.getString("route_name");

                    root_id = b1.getString("route_id");
                    if ((root_id == null) || (root_id.equals(""))) {
                        root_id = "";
                    }


                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "route_Route_Name", root_name);
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "route_Route_ID", root_id);
                    root.setText(root_name);
                    break;
                case AREA_DILOG:
                    b1 = msg.getData();
                    area_name = b1.getString("area_name");
                    area_id = b1.getString("area_id");


                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "route_area_Name", area_name);
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "route_area_ID", area_id);
                    edt_get_area.setText(area_name);

                    break;
                case 99:
                    if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}

            }
        }
    };

    public void parser_worktype(Bundle result) {
        if (result!=null ) {

            try {

                ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
                newlist.add(new SpinnerModel("--Select--", ""));

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    getworkingType.add(new SpinnerModel(c.getString("FIELD_NAME"),c.getString("WORKING_TYPE")));
                }


                adapter = new SpinAdapter(context, R.layout.spin_row, getworkingType);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                work_type.setAdapter(adapter);

                if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }

    public void parser_DCRCOMMIT_DOWNLOADALL(Bundle result) {

        if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}

        if (result!=null ) {

            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject one = jsonArray1.getJSONObject(0);

                String MyDaType = one.getString("DA_TYPE");
                String da_val="0";
                Float rate = Float.parseFloat(one.getString("FARE_RATE"));
                Float kms = Float.parseFloat(one.getString("KM"));

                if (MyDaType.equals("L")) {
                    da_val=one.getString("DA_L_RATE");
                } else if (MyDaType.equals("EX") || MyDaType.equals("EXS")) {
                    da_val=one.getString("DA_EX_RATE");
                } else if (MyDaType.equals("NSD") || MyDaType.equals("NS")) {
                    da_val=one.getString("DA_NS_RATE");
                }
                String distance_val="0";
                if (MyDaType.equals("EX") || MyDaType.equals("NSD")) {
                    distance_val="" + (kms * rate * 2);

                } else {
                    distance_val="" + (kms * rate);
                }

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DA_TYPE",MyDaType);
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"da_val",da_val);
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"distance_val",distance_val);

                String table1 = result.getString("Tables1");
                cbo_helper.delete_phdoctor();
                JSONArray jsonArray2 = new JSONArray(table1);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);
                    cbo_helper.insert_phdoctor(c.getInt("DR_ID"), c.getString("DR_NAME"), "", "",c.getInt("SPL_ID"),c.getString("LASTCALL"),
                            c.getString("CLASS"), c.getString("PANE_TYPE"),c.getString("POTENCY_AMT"),
                            c.getString("ITEM_NAME"), c.getString("ITEM_POB"), c.getString("ITEM_SALE"),c.getString("AREA"),c.getString("DR_LAT_LONG")
                            , c.getString("FREQ"),c.getString("NO_VISITED") , c.getString("DR_LAT_LONG2"),c.getString("DR_LAT_LONG3"),c.getString("COLORYN")
                            ,c.getString("CRM_COUNT"),c.getString("DRCAPM_GROUP"),c.getString("SHOWYN"),c.getInt("MAX_REG"));

                }

                String table2 = result.getString("Tables2");
                cbo_helper.deleteChemist();
                JSONArray jsonArray3 = new JSONArray(table2);
                for (int i = 0; i < jsonArray3.length(); i++) {
                    JSONObject c = jsonArray3.getJSONObject(i);
                    cbo_helper.insert_Chemist(c.getInt("CHEM_ID"), c.getString("CHEM_NAME"),
                            "", "",c.getString("LAST_VISIT_DATE"),c.getString("DR_LAT_LONG")
                            , c.getString("DR_LAT_LONG2"),c.getString("DR_LAT_LONG3"),c.getString("SHOWYN"));

                }

                String table3 = result.getString("Tables3");
                JSONArray jsonArray4 = new JSONArray(table3);
                cbo_helper.deleteDcrAppraisal();
                for (int i = 0; i < jsonArray4.length(); i++) {
                    JSONObject c = jsonArray4.getJSONObject(i);
                    cbo_helper.setDcrAppraisal(c.getString("PA_ID"), c.getString("PA_NAME"),c.getString("DR_CALL"), c.getString("DR_AVG"),c.getString("CHEM_CALL"), c.getString("CHEM_AVG"), "0", "", "", "", "", "","");

                }

                String table4 = result.getString("Tables4");
                JSONArray jsonArray5 = new JSONArray(table4);
                cbo_helper.delete_phdoctoritem();
                for (int b = 0; b<jsonArray5.length();b++){
                        JSONObject jasonObj2 = jsonArray5.getJSONObject(b);
                        cbo_helper.insertDoctorData(jasonObj2.getInt("DR_ID"), jasonObj2.getInt("ITEM_ID"),jasonObj2.getString("ITEM_NAME"));
                }

                String table5 = result.getString("Tables5");
                JSONArray jsonArray6 = new JSONArray(table5);
                cbo_helper.delete_Doctor_Call_Remark();
                for (int b = 0; b<jsonArray6.length();b++){
                    JSONObject jasonObj2 = jsonArray6.getJSONObject(b);
                    cbo_helper.insertDoctorCallRemark( jasonObj2.getString("PA_ID"),jasonObj2.getString("PA_NAME"));
                }


                String table6 = result.getString("Tables6");
                JSONArray jsonArray7 = new JSONArray(table6);
                cbo_helper.delete_phparty();
                for (int b = 0; b<jsonArray7.length();b++){
                    JSONObject jasonObj2 = jsonArray7.getJSONObject(b);
                    cbo_helper.insert_phparty(jasonObj2.getInt("PA_ID"), jasonObj2.getString("PA_NAME"),
                            jasonObj2.getInt("DESIG_ID"), jasonObj2.getString("CATEGORY"),
                            jasonObj2.getInt("HQ_ID"), jasonObj2.getString("PA_LAT_LONG"),
                            jasonObj2.getString("PA_LAT_LONG2"), jasonObj2.getString("PA_LAT_LONG3")
                            ,jasonObj2.getString("SHOWYN"));
                }

                String table7 = result.getString("Tables7");
                JSONArray jsonArray8 = new JSONArray(table7);
                cbo_helper.delete_phdairy();
                for (int b = 0; b<jsonArray8.length();b++){
                    JSONObject jasonObj2 = jsonArray8.getJSONObject(b);
                    cbo_helper.insert_phdairy(jasonObj2.getInt("ID"), jasonObj2.getString("DAIRY_NAME"),jasonObj2.getString("DOC_TYPE"),
                            "", jasonObj2.getString("DAIRY_LAT_LONG"),jasonObj2.getString("DAIRY_LAT_LONG2"),jasonObj2.getString("DAIRY_LAT_LONG3"));
                }


                String table8 = result.getString("Tables8");
                JSONArray jsonArray9 = new JSONArray(table8);
                cbo_helper.delete_phdairy_person();
                for (int b = 0; b<jsonArray9.length();b++){
                    JSONObject jasonObj2 = jsonArray9.getJSONObject(b);
                    cbo_helper.insert_phdairy_person( jasonObj2.getInt("DAIRY_ID"),jasonObj2.getInt("PERSON_ID"),jasonObj2.getString("PERSON_NAME"));
                }

                String table9 = result.getString("Tables9");
                JSONArray jsonArray10 = new JSONArray(table9);
                cbo_helper.delete_phdairy_reason();
                for (int b = 0; b<jsonArray10.length();b++){
                    JSONObject jasonObj2 = jsonArray10.getJSONObject(b);
                    cbo_helper.insert_phdairy_reason( jasonObj2.getInt("PA_ID"),jasonObj2.getString("PA_NAME"));
                }

                String table10 = result.getString("Tables10");
                JSONArray jsonArray11 = new JSONArray(table10);
                cbo_helper.delete_Item_Stock();
                for (int b = 0; b<jsonArray11.length();b++){
                    JSONObject jasonObj2 = jsonArray11.getJSONObject(b);
                    cbo_helper.insert_Item_Stock( jasonObj2.getString("ITEM_ID"),jasonObj2.getInt("STOCK_QTY"));
                }

                String table11 = result.getString("Tables11");
                JSONArray jsonArray12 = new JSONArray(table11);
                cbo_helper.delete_STk_Item();
                for (int b = 0; b<jsonArray12.length();b++){
                    JSONObject jasonObj2 = jsonArray12.getJSONObject(b);
                    cbo_helper.insert_STk_Item( jasonObj2.getString("STK_ID"),jasonObj2.getString("ITEM_ID"),jasonObj2.getString("RATE"));
                }
                customVariablesAndMethod.SetLastCallLocation(context);

                switch (work_type_Selected){
                    case "w":
                        finish();
                        break;
                    case "l":
                        Intent intent = new Intent(getApplicationContext(), FinalSubmitDcr_new.class);
                        startActivity(intent);
                        break;
                    case "n":
                        setReultForNonWork();
                        break;
                }
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"work_type_Selected",work_type_Selected);

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);


    }

    public void parser_submit_for_working(Bundle result) {

        if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}

        if (result!=null ) {

            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    Custom_Variables_And_Method.DCR_ID = c.getString("DCRID");

                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "FCMHITCALLYN", c.getString("FCMHITCALLYN") );
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "FARMERREGISTERYN", c.getString("FARMERREGISTERYN") );
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DIVERTLOCKYN", c.getString("DIVERTLOCKYN") );


                    if(!c.getString("FCMHITCALLYN").equals("") && !c.getString("FCMHITCALLYN").equals("N")){
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "MOBILEDATAYN", "Y" );
                    }

                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "APPROVAL_MSG", "" );
                    if(Custom_Variables_And_Method.DCR_ID.equals("0") &&  c.getString("DCRID")!=null){
                        Alert("Alert !!!",c.getString("MSG"));
                    }else if( c.getString("DIVERTLOCKYN").toUpperCase().equals("Y")){
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "APPROVAL_MSG", c.getString("MSG") );
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DCR_ID", c.getString("DCRID"));
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DcrPlanTime_server", c.getString("IN_TIME") );
                        Alert("Alert !!!",c.getString("MSG"));
                    }else if(c.getString("FARMERREGISTERYN").equals("Y")){
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "APPROVAL_MSG", c.getString("MSG") );
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DCR_ID", c.getString("DCRID"));
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DcrPlanTime_server", c.getString("IN_TIME") );
                        Alert("Alert !!!","Today You have an activity for "+cbo_helper.getMenu("DCR", "D_FAR").get("D_FAR"));
                    }else{

                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DCR_ID", c.getString("DCRID"));
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DcrPlanTime_server", c.getString("IN_TIME") );
                        DownloadAll();
                    }
                }



            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);


    }

    private void Alert(String title,String msg){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
        final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
        final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.nagative);
        Alert_Nagative.setVisibility(View.GONE);
        Alert_Positive.setText("OK");
        Alert_title.setText(title);
        Alert_message.setText(msg);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                DownloadAll();

            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void DownloadAll(){
        if (!(Custom_Variables_And_Method.DCR_ID.equals("0"))) {


            cbo_helper.deletedcrFromSqlite();
            cbo_helper.deleteUtils();
            cbo_helper.deleteDCRDetails();

            new CustomTextToSpeech().setTextToSpeech("");

            cbo_helper.putDcrId(Custom_Variables_And_Method.DCR_ID);
            long val = cbo_helper.insertUtils(Custom_Variables_And_Method.pub_area);
            long val2 = cbo_helper.insertDcrDetails(Custom_Variables_And_Method.DCR_ID, Custom_Variables_And_Method.pub_area);


            if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "dcr_date_real").equals("")){
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "OveAllKm", "0.0");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DayPlanLatLong", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLatLong", Custom_Variables_And_Method.GLOBAL_LATLON));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DcrPlantimestamp", customVariablesAndMethod.get_currentTimeStamp());
            }

            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"working_head", work_val);
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"working_code", work_type_code);

            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"BackDateReason", late_remark.getText().toString());
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"sDivert_Remark", divert_remark.getText().toString());


            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"dcr_date_real", real_date);
            cbo_helper.putDcrId(Custom_Variables_And_Method.DCR_ID);
            Custom_Variables_And_Method.GCMToken=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"GCMToken");

            //Start of call to service

            HashMap<String,String> request=new HashMap<>();
            request.put("sCompanyFolder",cbo_helper.getCompanyCode());
            request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
            request.put("sDcrId",Custom_Variables_And_Method.DCR_ID);
            request.put("sRouteYn", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"root_needed"));
            request.put("sGCM_TOKEN", Custom_Variables_And_Method.GCMToken);
            request.put("sMobileId",SystemArchitecture.COMPLETE_DEVICE_INFO);
            request.put("sVersion", Custom_Variables_And_Method.VERSION);

            ArrayList<Integer> tables=new ArrayList<>();
            tables.add(0);
            tables.add(1);
            tables.add(2);
            tables.add(3);
            tables.add(4);
            tables.add(5);
            tables.add(6);
            tables.add(7);
            tables.add(8);
            tables.add(9);
            tables.add(10);
            tables.add(11);

            progress1.setMessage("Please Wait..\n" +
                    " Fetching your Utilitis for the day");
            progress1.setCancelable(false);
            progress1.show();

            new CboServices(this,mHandler).customMethodForAllServices(request,"DCRCOMMIT_DOWNLOADALL",MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL,tables);

            //End of call to service


            if ((fmcg_Live_Km.equalsIgnoreCase("Y")) || (fmcg_Live_Km.equalsIgnoreCase("5"))||(fmcg_Live_Km.equalsIgnoreCase("Y5"))) {
                String lat, lon, time, km;
                customVariablesAndMethod.deleteFmcg_ByKey(context,"myKm1");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Tracking", "Y");
                lat = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLat");
                lon = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLon");
                time =customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareMyTime");
                km = "0.0";
                customMethod.insertDataInOnces_Minute(lat, lon, km, time);

                new Thread(r1).start();
                new Thread(r2).start();
            }


            if(intent.getStringExtra("plan_type").equals("p")) {
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Final_submit","N");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ACTUALFAREYN","");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ACTUALFARE","");
                cbo_helper.deleteAllRecord10();
                cbo_helper.delete_DCR_Item(null,null,null,null);
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "Dcr_Planed_Date", customVariablesAndMethod.currentDate());
            }
            //startActivity(new Intent(getApplicationContext(), ViewPager_2016.class));
        }
    }

}


