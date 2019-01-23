package com.cbo.cbomobilereporting.ui_new.dcr_activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Doctor_registration_GPS;
import com.uenics.javed.CBOLibrary.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import locationpkg.Const;
import services.CboServices;
import services.Sync_service;
import utils.adapterutils.ExpandableListAdapter;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinAdapter_new;
import utils.adapterutils.SpinnerModel;
import utils.clearAppData.MyCustumApplication;
import utils.networkUtil.NetworkUtil;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.Dr_Workwith_Dialog;
import utils_new.GPS_Timmer_Dialog;
import utils_new.Report_Registration;
import utils_new.SendAttachment;
import utils_new.SendMailTask;
import utils_new.Service_Call_From_Multiple_Classes;


public class DrCall extends AppCompatActivity implements ExpandableListAdapter.Summary_interface {


    String latLong = "Not Found";
    String ref_latLong = "";


    private static final String TAG = DrCall.class.getSimpleName();

    EditText loc, workwithdr;
    Button drname, back,btn_remark;
    Button submit, getdr;
    Custom_Variables_And_Method customVariablesAndMethod;
    int PA_ID;
    SpinAdapter adapter;
    RelativeLayout locLayout;
    CBO_DB_Helper cbohelp;
    String dr_name = "",dr_name_reg = "", dr_id = "",dr_id_reg = "",dr_id_index = "", doc_name = "";
    String part1 = "", part2 = "", part3 = "";
    String workwith1 = "", workwith2 = "", workwith34 = "";
    String work_with_name = "", work_with_id = "";
    ArrayList<SpinnerModel> docList;
    List<String> data1;
    String network_status;
    SpinnerModel[] TitleName;
    ArrayList<SpinnerModel> array_sort;
    private AlertDialog myalertDialog = null;
    int textlength = 0;
    String drInTime;
    TextView batteryLevel;
    String myBatteryLevel;
    EditText dr_remark;
    ImageView spinImg,remark_img;
    Context context;
    String updated = "0";
    String drKm = "0";
    NetworkUtil networkUtil;
    MyCustomMethod myCustomMethod;
    String live_km;
    TextView remark;

    LinearLayout call_layout,detail_layout,Tab;
    LinearLayout dr_remarkLayout;
    ExpandableListView summary_layout;
    Button tab_call,tab_summary,tab_unplaned;
    HashMap<String, HashMap<String, ArrayList<String>>> summary_list=new HashMap<>();
    HashMap<String, ArrayList<String>> doctor_list_summary=new HashMap<>();
    HashMap<String, ArrayList<String>> doctor_list=new HashMap<>();
    ExpandableListAdapter listAdapter;
    TableLayout stk,doc_detail;
    String plan_type="1",AREA="",call_type="0";     //plan_type=1 for planed, 0 for unplaned
    CheckBox call_missed;
    Location currentBestLocation=null;
    String locExtra="";
    ArrayList<String> remark_list;
    private int showRegistrtion=1;

    Report_Registration alertdFragment;
    private  static final int GPS_TIMMER=4,MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL=1,WORK_WITH_DIALOG=0,MESSAGE_INTERNET_SEND_FCM= 2;
    public ProgressDialog progress1;

    boolean IsRefreshedClicked = true;

    Service_Call_From_Multiple_Classes service ;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.dr_call);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);
        hader_text.setText("Doctor Call");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }
        context = DrCall.this;
        progress1 = new ProgressDialog(this);
        service =  new Service_Call_From_Multiple_Classes();
        loc = (EditText) findViewById(R.id.loc);
        workwithdr = (EditText) findViewById(R.id.get_workwith);
        drname = (Button) findViewById(R.id.drname);
        btn_remark = (Button) findViewById(R.id.remark);
        back = (Button) findViewById(R.id.bkfinal_button);
        getdr = (Button) findViewById(R.id.getdcal);
        batteryLevel = (TextView) findViewById(R.id.battary_level);
        submit = (Button) findViewById(R.id.add);
        locLayout = (RelativeLayout) this.findViewById(R.id.layout2);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp = new CBO_DB_Helper(getApplicationContext());
        //latLong = "" + Custom_Variables_And_Method.GLOBAL_LATLON;
        data1 = new ArrayList<String>();
        dr_remarkLayout = (LinearLayout) findViewById(R.id.dr_remark_layout);
        dr_remark = (EditText) findViewById(R.id.dr_remark_edit);
        stk= (TableLayout) findViewById(R.id.last_pob);
        doc_detail= (TableLayout) findViewById(R.id.doc_detail);
        dr_remarkLayout.setVisibility(View.GONE);
        dr_remark.setVisibility(View.GONE);
        networkUtil = new NetworkUtil(context);
        myCustomMethod = new MyCustomMethod(context);
        live_km = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"live_km");
        call_missed= (CheckBox) findViewById(R.id.call_missed);
        remark= (TextView) findViewById(R.id.last_visited);

        Tab = (LinearLayout) findViewById(R.id.tab);
        call_layout = (LinearLayout) findViewById(R.id.call_layout);
        detail_layout = (LinearLayout) findViewById(R.id.detail_layout);
        detail_layout.setBackgroundColor(Color.TRANSPARENT);
        summary_layout = (ExpandableListView) findViewById(R.id.summary_layout);
        tab_call= (Button) findViewById(R.id.call);
        tab_summary= (Button) findViewById(R.id.summary);
        tab_unplaned= (Button) findViewById(R.id.call_unplaned);


        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DR_REMARKYN").equalsIgnoreCase("y")) {
            dr_remarkLayout.setVisibility(View.VISIBLE);
        }

        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLE_POB_INPUT_MANDATORY").contains("U")) {
            tab_unplaned.setVisibility(View.VISIBLE);
        }else {
            tab_unplaned.setVisibility(View.GONE);
            tab_call.setText("Call");
            Tab.setWeightSum(3);
        }

        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLE_POB_MANDATORY").equals("N")) {
            Custom_Variables_And_Method.SAMPLE_POB_MANDATORY="N";
        } else {
            Custom_Variables_And_Method.SAMPLE_POB_MANDATORY="Y";
        }

        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"MISSED_CALL_OPTION").contains("D") ) {
            call_missed.setVisibility(View.VISIBLE);
        }else  {
            call_missed.setVisibility(View.GONE);
        }

        call_missed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked && plan_type.equals("1")){
                    call_type="1";
                }else if ( plan_type.equals("1")){
                    call_type="0";

                    drname.setText("---Select---");
                    dr_id="";
                    doc_name="";



                    remark.setVisibility(View.GONE);
                    workwithdr.setText("");
                    dr_remark.setText("");
                    dr_remark.setVisibility(View.GONE);
                    btn_remark.setText("---Select Remark---");
                    stk.removeAllViews();
                    doc_detail.removeAllViews();
                    detail_layout.setBackgroundColor(Color.TRANSPARENT);

                }else{
                    call_type="2";
                }


            }
        });
        drname.setText("---Select---");
        btn_remark.setText("---Select Remark---");


        network_status = NetworkUtil.getConnectivityStatusString(getApplicationContext());

        showRegistrtion=Integer.parseInt(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"IsBackDate","1"));

        spinImg = (ImageView) findViewById(R.id.spinner_img_drCall);
        spinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickDrLoad();
            }
        });

        remark_img = (ImageView) findViewById(R.id.remark_img);
        remark_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDrCallRemark();
            }
        });


        if (Custom_Variables_And_Method.location_required.equals("N")) {
            locLayout.setVisibility(View.GONE);
        } else if (Custom_Variables_And_Method.location_required.equals("Y")) {
            locLayout.setVisibility(View.VISIBLE);
        } else {
            locLayout.setVisibility(View.GONE);
        }


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //====================================================================================================================================

        customVariablesAndMethod.getbattrypercentage(context);
        remark_list=cbohelp.get_Doctor_Call_Remark();

        drname.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickDrLoad();
            }
        });
        btn_remark.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDrCallRemark();
            }
        });

        getdr.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(DrCall.this, Dr_Workwith.class);
                startActivityForResult(i, WORK_WITH_DIALOG);*/
                new Dr_Workwith_Dialog(context,mHandler,null,WORK_WITH_DIALOG).show();
            }
        });


        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //drInTime = getTime();

                myBatteryLevel = Custom_Variables_And_Method.BATTERYLEVEL;
                setAddressToUI();
                if (loc.getText().toString().equals("") || loc.getText().toString().equals(null)) {
                    loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
                }
                if (work_with_id != null || work_with_id != "") {
                    String[] parts = work_with_id.split(",");
                    if (parts.length == 1) {
                        part1 = parts[0];
                    }
                    if (parts.length == 2) {
                        part1 = parts[0];
                        part2 = parts[1];
                    }
                    if (parts.length > 2) {
                        part1 = parts[0];
                        part2 = parts[1];
                        part3 = parts[2];
                    }
                    workwith1 = part1;
                    workwith2 = part2;
                    workwith34 = part3;
                }


//                if ((latLong == null) || (latLong.equals("0.0,0.0")) || (latLong.equals(""))) {
//                    latLong = Custom_Variables_And_Method.GLOBAL_LATLON;
//                }

                if (drname.getText().toString().equals("---Select---")) {
                    customVariablesAndMethod.msgBox(context,"Select  Doctor from List");
                } else if (docList.isEmpty()) {
                    customVariablesAndMethod.msgBox(context,"No Doctor in List");


                } else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"REMARK_WW_MANDATORY").contains("D") && (work_with_id==null || work_with_id.equals(""))) {
                    customVariablesAndMethod.msgBox(context,"Please select work-with");


                } else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"REMARK_WW_MANDATORY").contains("D") &&  dr_remark.getText().toString().equals("")) {
                    customVariablesAndMethod.msgBox(context,"Please enter remark");

                } else if (call_missed.isChecked()) {

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
                    final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);
                    final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
                    final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
                    final Button Alert_Nagative= (Button) dialogLayout.findViewById(R.id.nagative);
                    Alert_Nagative.setText("No");
                    Alert_Positive.setText("Yes");
                    Alert_title.setText("Call Missed !!!");
                    Alert_message.setText("You really missed the call to \n"+doc_name+" ?");


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


                    final AlertDialog dialog = builder1.create();

                    dialog.setView(dialogLayout);
                    Alert_Positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            submitDoctor(false);
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

                } else {

                    String getCurrentTime = customVariablesAndMethod.get_currentTimeStamp();
                    String planTime = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DcrPlantimestamp",customVariablesAndMethod.get_currentTimeStamp());
                    float t1 = Float.parseFloat(getCurrentTime);
                    float t2 = Float.parseFloat(planTime);

                    float t3 = t1 - t2;
                    if ((t3 >= 0) || (t3 >= -0.9)) {
                        submitDoctor(false);
                    } else {
                        customVariablesAndMethod.msgBox(context,"Your Plan Time can not be \n Higher Than Current time...");
                    }

                }
            }
        });

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ArrayList<String> header_title = new ArrayList<>();
        try {
            doctor_list_summary = cbohelp.getCallDetail("tempdr", "", "1");
            //expense_list=new CBO_DB_Helper(context).getCallDetail("tempdr");

            summary_list = new LinkedHashMap<>();
            summary_list.put(cbohelp.getMenu("DCR", "D_DRCALL").get("D_DRCALL"), doctor_list_summary);


            //final List<Integer> visible_status=new ArrayList<>();
            for (String main_menu : summary_list.keySet()) {
                header_title.add(main_menu);
                //visible_status.add(0);
            }


            listAdapter = new ExpandableListAdapter(summary_layout, this, header_title, summary_list);

            // setting list adapter
            summary_layout.setAdapter(listAdapter);
            summary_layout.setGroupIndicator(null);
            for (int i = 0; i < listAdapter.getGroupCount(); i++)
                summary_layout.expandGroup(i);
            //doctor.expandGroup(1);
        }catch (Exception e){
            List toEmailList = Arrays.asList("mobilereporting@cboinfotech.com".split("\\s*,\\s*"));
            new SendMailTask().execute("mobilereporting@cboinfotech.com",
                    "mreporting", toEmailList, Custom_Variables_And_Method.COMPANY_CODE + ": " + "Error in DR_Call_sample", context.getResources().getString(R.string.app_name) + "\n Company Code :" + Custom_Variables_And_Method.COMPANY_CODE + "\n DCR ID :" + Custom_Variables_And_Method.DCR_ID + "\n PA ID : " + Custom_Variables_And_Method.PA_ID + "\n App version : " + Custom_Variables_And_Method.VERSION   + "\n Error Alert :" + "Error in DR_sample" + "\n" + e.toString());

            CboServices.getAlert(context,"Error!!!", e.toString());
            e.printStackTrace();
        }

        summary_layout.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {

                summary_list.get(header_title.get(groupPosition)).get("visible_status").get(childPosition);
                if (summary_list.get(header_title.get(groupPosition)).get("visible_status").get(childPosition).equals("1")){
                    summary_list.get(header_title.get(groupPosition)).get("visible_status").set(childPosition,"0");
                }else {
                    summary_list.get(header_title.get(groupPosition)).get("visible_status").set(childPosition,"1");
                }
                listAdapter.notifyDataSetChanged();
                return false;
            }
        });

        tab_call.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                plan_type="1";
                call_type="0";
                if (call_missed.isChecked()){ // if call is to be missed
                    call_type="1";
                }

                if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"MISSED_CALL_OPTION").contains("D") ) {
                    call_missed.setVisibility(View.VISIBLE);
                }else  {
                    call_missed.setVisibility(View.GONE);
                }

                call_layout.setVisibility(View.VISIBLE);
                summary_layout.setVisibility(View.GONE);

                tab_call.setBackgroundResource(R.drawable.tab_selected);
                tab_summary.setBackgroundResource(R.drawable.tab_deselected);
                tab_unplaned.setBackgroundResource(R.drawable.tab_deselected);

               // new DoctorData().execute();

                drname.setText("---Select---");
                remark.setVisibility(View.GONE);
                workwithdr.setText("");
                dr_remark.setText("");
                call_missed.setChecked(false);
                stk.removeAllViews();
                doc_detail.removeAllViews();
                detail_layout.setBackgroundColor(Color.TRANSPARENT);
                drname.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickDrLoad();
                    }
                });

            }
        });

        tab_unplaned.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                plan_type="0";
                call_type="2";
                call_missed.setVisibility(View.GONE);
                call_layout.setVisibility(View.VISIBLE);
                summary_layout.setVisibility(View.GONE);
                tab_unplaned.setBackgroundResource(R.drawable.tab_selected);
                tab_summary.setBackgroundResource(R.drawable.tab_deselected);
                tab_call.setBackgroundResource(R.drawable.tab_deselected);

                //new DoctorData().execute();

                drname.setText("---Select---");
                remark.setVisibility(View.GONE);
                dr_remark.setText("");
                workwithdr.setText("");
                stk.removeAllViews();
                call_missed.setChecked(false);
                doc_detail.removeAllViews();
                detail_layout.setBackgroundColor(Color.TRANSPARENT);
                drname.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickDrLoad();
                    }
                });
            }
        });

        tab_summary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                summary_layout.setVisibility(View.VISIBLE);
                call_layout.setVisibility(View.GONE);
                tab_call.setBackgroundResource(R.drawable.tab_deselected);
                tab_summary.setBackgroundResource(R.drawable.tab_selected);
                tab_unplaned.setBackgroundResource(R.drawable.tab_deselected);
            }
        });



        Intent intent=getIntent();
        remark.setText("");
        if (intent.getStringExtra("id")!=null) {
            OnClickDrLoad();
        }

    }


    //===========================================================================================================================================





    public ArrayList<String> remAdded() {
        ArrayList<String> drlist = new ArrayList<String>();
        drlist.clear();
        Cursor c = cbohelp.getDoctorName1();
        if (c.moveToFirst()) {
            do {
                drlist.add(c.getString(c.getColumnIndex("dr_id")));
            } while (c.moveToNext());
        }
        return drlist;
    }

    public void submitDoctorInLocal() {

        long mydr = cbohelp.insertDoctorInLocal(dr_id, doc_name, workwith1, workwith2, workwith34, latLong + "!^" + loc.getText().toString(), customVariablesAndMethod.currentTime(context),locExtra);
        cbohelp.close();


        if (mydr > 0) {

            new Service_Call_From_Multiple_Classes().SendFCMOnCall(context, mHandler, MESSAGE_INTERNET_SEND_FCM,"D",dr_id,latLong);

        } else {
            customVariablesAndMethod.msgBox(context,"Insertion fail");
        }
    }


    public void setLocationToUI() {
        if (network_status.equals("Not connected to Internet")) {
            loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
        } else {

            loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
        }


    }

    public void setAddressToUI() {
        if (network_status.equals("Not connected to Internet")) {
            loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
        } else {
            //loc.setText(MyConnection.GLOBAL_LATLON);
            if (Custom_Variables_And_Method.global_address != null || Custom_Variables_And_Method.global_address != "") {
                loc.setText(Custom_Variables_And_Method.global_address);
            } else {
                loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
            }

        }
    }

    @Override
    public void Edit_Call(final String Dr_id, final String Dr_name) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        final Button Alert_Nagative= (Button) dialogLayout.findViewById(R.id.nagative);
        Alert_Nagative.setText("Cancel");
        Alert_Positive.setText("Edit");
        Alert_title.setText("Edit!!!");

       /* Intent i = new Intent(context, DrCall.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("id",Doctor_id_for_POB);
        i.putExtra("remark","Call Pending");
        startActivity(i);*/

        if (Dr_name.contains("(M)")){
            Alert_Positive.setText("Ok");
            Alert_message.setText("You can't edit for missed doctor ");
        }else {
            Alert_message.setText("Do you want to edit \n" + Dr_name + " ?");
        }


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Dr_name.contains("(M)")){
                    dialog.dismiss();
                }else {
                    Intent i = new Intent(context, Doctor_Sample.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("id",Dr_id);
                    //i.putExtra("remark","POB entry Pending");
                    startActivity(i);
                    dialog.dismiss();
                }

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

    @Override
    public void delete_Call(final String Dr_id, final String Dr_name) {


        HashMap<String, ArrayList<String>>  tenivia_traker=cbohelp.getCallDetail("tenivia_traker",Dr_id,"1");
        if (!tenivia_traker.isEmpty () && (tenivia_traker.get ("id").contains ("-99") )) {


            AppAlert.getInstance().setPositiveTxt("Delete").DecisionAlert(context, "Delete!!!", "Do you Really want to delete " + Dr_name + " ?", new AppAlert.OnClickListener() {
                @Override
                public void onPositiveClicked(View item, String result) {
                    cbohelp.delete_tenivia_traker(Dr_id);
                    // customVariablesAndMethod.msgBox(context,Dr_name+" sucessfully Deleted.");
                    cbohelp.delete_Doctor_from_local_all(Dr_id);
                    customVariablesAndMethod.msgBox(context, Dr_name + " sucessfully Deleted.");
                    finish();


                }

                @Override
                public void onNegativeClicked(View item, String result) {
                }
            });
        }else{
            customVariablesAndMethod.getAlert(context, MyCustumApplication.getInstance().getTaniviaTrakerMenuName() +"!!!","Please delete " +Dr_name+" in "+MyCustumApplication.getInstance().getTaniviaTrakerMenuName());
        }


    }



    class DoctorData extends AsyncTask<ArrayList<SpinnerModel>, String, ArrayList<SpinnerModel>> {
        ProgressDialog pd;
        //GPS_Timmer_Dialog dilog=new GPS_Timmer_Dialog(context);
        @SafeVarargs
        @Override
        protected final ArrayList<SpinnerModel> doInBackground(ArrayList<SpinnerModel>... params) {
            //getWorkWithDetails();

            try {
                docList.clear();

                Intent intent=getIntent();
                String caltype = null;
                if (intent.getStringExtra("id")!=null ) {
                    caltype = "0";
                }
                Cursor c = cbohelp.getDoctorListLocal(plan_type,caltype);
                // chemist.add(new SpinnerModel("--Select--",""));
                if (c.moveToFirst()) {
                    do {
                        docList.add(new SpinnerModel(c.getString(c.getColumnIndex("dr_name"))+"-"+c.getString(c.getColumnIndex("DR_AREA")), c.getString(c.getColumnIndex("dr_id")), c.getString(c.getColumnIndex("LAST_VISIT_DATE")),
                                c.getString(c.getColumnIndex("CLASS")), c.getString(c.getColumnIndex("POTENCY_AMT")),
                                c.getString(c.getColumnIndex("ITEM_NAME")),c.getString(c.getColumnIndex("ITEM_POB")),
                                c.getString(c.getColumnIndex("ITEM_SALE")), c.getString(c.getColumnIndex("DR_AREA")),
                                c.getString(c.getColumnIndex("PANE_TYPE")), c.getString(c.getColumnIndex("DR_LAT_LONG"))
                                , c.getString(c.getColumnIndex("FREQ")), c.getString(c.getColumnIndex("NO_VISITED")),
                                c.getString(c.getColumnIndex("DR_LAT_LONG2")), c.getString(c.getColumnIndex("DR_LAT_LONG3"))
                                , c.getString(c.getColumnIndex("COLORYN")), c.getString(c.getColumnIndex("CALLYN"))
                                , c.getString(c.getColumnIndex("CRM_COUNT")), c.getString(c.getColumnIndex("DRCAPM_GROUP"))));
                    } while (c.moveToNext());

                }


            } catch (Exception e) {
                //customVariablesAndMethod.msgBox(context,e.toString());
                e.printStackTrace();

            }
            return docList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(DrCall.this);
            pd.setTitle("CBO");
            pd.setMessage("Processing......." + "\n" + "please wait");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();

        }

        @Override
        protected void onPostExecute(ArrayList<SpinnerModel> s) {
            super.onPostExecute(s);
            pd.dismiss();
            setLocationToUI();
            try {
                pd.dismiss();
                if ((!s.isEmpty()) || (s.size() < 0)) {
                    TitleName = new SpinnerModel[s.size()];
                    for (int i = 0; i <= s.size()-1; i++) {
                        TitleName[i] = s.get(i);


                    }

                    array_sort = new ArrayList<SpinnerModel>(Arrays.asList(TitleName));

                    /*Intent intent=getIntent();
                    if (intent.getStringExtra("id")!=null && (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"GEO_FANCING_KM","0").equals("0") || showRegistrtion == 0 )) {

                        for (int i = 0; i < docList.size(); i++) {

                            if (docList.get(i).getId().equals(intent.getStringExtra("id").trim())) {
                                dr_id = docList.get(i).getId();
                                doc_name = docList.get(i).getName().split("-")[0];
                                drname.setText(doc_name);
                                if (intent.getStringExtra("remark") != null) {
                                    remark.setVisibility(View.VISIBLE);
                                    remark.setText(intent.getStringExtra("remark"));
                                }

                            }
                        }
                    }else{*/
                        onClickDrName();
                    //}
                } else {
                    customVariablesAndMethod.getAlert(context,"Data not found","No Doctor in Planned Dcr...");

                    spinImg.setClickable(false);
                    drname.setClickable(false);
                    //  spinImg.setClickable(false);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void submitDoctor(boolean Skip_Verification) {

        IsRefreshedClicked = false;
        if (remAdded().contains(dr_id)) {
            customVariablesAndMethod.msgBox(context,dr_name + " Allready Added...");

        }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,Skip_Verification)) {
            customVariablesAndMethod.msgBox(context,"Verifing Your Location");
            LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                    new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
        } else {

            customVariablesAndMethod.SetLastCallLocation(context);

           // if (Custom_Variables_And_Method.GLOBAL_LATLON.equalsIgnoreCase("0.0,0.0")) {

               // Custom_Variables_And_Method.GLOBAL_LATLON =  customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
           // }

            currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation",Location.class);


            long val = cbohelp.AddedDoctorMore
                    (dr_id, doc_name, customVariablesAndMethod.currentTime(context), workwith1, workwith2, workwith34, latLong + "!^" + Custom_Variables_And_Method.global_address);
            Log.e("doctor insterted more", "" + val);
/*
           String shareLat = mycon.getDataFrom_FMCG_PREFRENCE("shareLat");
           String shareLon = mycon.getDataFrom_FMCG_PREFRENCE("shareLon");
           String DayPlanLatLong = mycon.getDataFrom_FMCG_PREFRENCE("DayPlanLatLong");*/


            if (currentBestLocation!=null) {
                locExtra = "Lat_Long " + currentBestLocation.getLatitude() + "," + currentBestLocation.getLongitude() + ", Accuracy " + currentBestLocation.getAccuracy() + ", Time " + currentBestLocation.getTime() + ", Speed " + currentBestLocation.getSpeed() + ", Provider " + currentBestLocation.getProvider();
            }
            cbohelp.addTempDrInLocal(dr_id, doc_name, "" + customVariablesAndMethod.currentTime(context), myBatteryLevel,latLong, Custom_Variables_And_Method.global_address, dr_remark.getText().toString(), updated, drKm,customVariablesAndMethod.srno(context),work_with_name,AREA,"",call_type, locExtra,ref_latLong);
            submitDoctorInLocal();


        }

    }

    private void last_pob_layout(String[] sample_name, String[] sample_pob, String[] sale_qty) {


        stk.removeAllViews();
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);

        for (int i = 0; i < sample_name.length; i++) {
            if (!sample_pob[i].equals("")) {
                TableRow tbrow = new TableRow(context);
                TextView t1v = new TextView(context);
                t1v.setText("POB-" + sample_name[i]);
                t1v.setTextSize(11);
                t1v.setPadding(5, 5, 5, 0);
                t1v.setTextColor(Color.BLACK);
                t1v.setLayoutParams(params);
                tbrow.addView(t1v);

                TextView t3v = new TextView(context);
                t3v.setText(sample_pob[i]);
                t3v.setPadding(5, 5, 5, 0);
                t3v.setTextSize(11);
                t3v.setTextColor(Color.BLACK);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);
                stk.addView(tbrow);
            }
        }

        for (int i = 0; i < sample_name.length; i++) {
            if (!sale_qty[i].equals("") ) {
                TableRow tbrow = new TableRow(context);
                TextView t1v = new TextView(context);
                t1v.setText("Sale-" + sample_name[i]);
                t1v.setPadding(5, 5, 5, 0);
                t1v.setTextSize(11);
                t1v.setTextColor(Color.BLACK);
                t1v.setLayoutParams(params);
                tbrow.addView(t1v);

                TextView t3v = new TextView(context);
                t3v.setText(sale_qty[i]);
                t3v.setPadding(5, 5, 5, 0);
                t3v.setTextSize(11);
                t3v.setTextColor(Color.BLACK);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);
                stk.addView(tbrow);
            }
        }
    }

    private void Doc_Detail(String doc_class, String doc_potential, String doc_last_visited,String area, String CRM_COUNT,String DRCAPM_GROUP) {
        doc_detail.removeAllViews();

        //tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);

        if (!area.equals("")) {
            TableRow tbrow00 = new TableRow(context);
            TextView tv00 = new TextView(context);
            tv00.setText("Area");
            tv00.setTextSize(11);
            tv00.setPadding(5, 5, 5, 0);
            tv00.setTextColor(Color.BLACK);
            tv00.setTypeface(null, Typeface.BOLD);
            tv00.setLayoutParams(params);
            tbrow00.addView(tv00);

            TextView tv01 = new TextView(context);
            tv01.setText(area);
            tv01.setTextSize(11);
            tv01.setPadding(5, 5, 5, 0);
            tv01.setTextColor(Color.BLACK);
            tv01.setGravity(Gravity.RIGHT);
            tv01.setTypeface(null, Typeface.NORMAL);
            tv01.setLayoutParams(params);
            tbrow00.addView(tv01);

            doc_detail.addView(tbrow00);
        }

        if (!doc_class.equals("") ) {
            TableRow tbrow0 = new TableRow(context);
            TextView tv0 = new TextView(context);
            tv0.setText("Class");
            tv0.setTextSize(11);
            tv0.setPadding(5, 5, 5, 0);
            tv0.setTextColor(Color.BLACK);
            tv0.setTypeface(null, Typeface.BOLD);
            tv0.setLayoutParams(params);
            tbrow0.addView(tv0);

            TextView tv1 = new TextView(context);
            tv1.setText(doc_class);
            tv1.setTextSize(11);
            tv1.setPadding(5, 5, 5, 0);
            tv1.setTextColor(Color.BLACK);
            tv1.setGravity(Gravity.RIGHT);
            tv1.setTypeface(null, Typeface.NORMAL);
            tv1.setLayoutParams(params);
            tbrow0.addView(tv1);

            doc_detail.addView(tbrow0);
        }

        if (!doc_potential.equals("")) {
            TableRow tbrow01 = new TableRow(context);
            TextView tv01 = new TextView(context);
            tv01.setText("Potential");
            tv01.setTextSize(11);
            tv01.setPadding(5, 5, 5, 0);
            tv01.setTextColor(Color.BLACK);
            tv01.setTypeface(null, Typeface.BOLD);
            tv01.setLayoutParams(params);
            tbrow01.addView(tv01);

            TextView tv11 = new TextView(context);
            tv11.setText(doc_potential);
            tv11.setTextSize(11);
            tv11.setPadding(5, 5, 5, 0);
            tv11.setTextColor(Color.BLACK);
            tv11.setGravity(Gravity.RIGHT);
            tv11.setTypeface(null, Typeface.NORMAL);
            tv11.setLayoutParams(params);
            tbrow01.addView(tv11);

            doc_detail.addView(tbrow01);
        }

        if (!doc_last_visited.equals("")) {

            TableRow tbrow02 = new TableRow(context);
            TextView tv02 = new TextView(context);
            tv02.setText("Last Visited");
            tv02.setTextSize(11);
            tv02.setPadding(5, 5, 5, 0);
            tv02.setTextColor(Color.BLACK);
            tv02.setTypeface(null, Typeface.BOLD);
            tv02.setLayoutParams(params);
            tbrow02.addView(tv02);

            TextView tv12 = new TextView(context);
            tv12.setText(doc_last_visited);
            tv12.setPadding(5, 5, 5, 0);
            tv12.setTextSize(11);
            tv12.setTextColor(Color.BLACK);
            tv12.setGravity(Gravity.RIGHT);
            tv12.setTypeface(null, Typeface.NORMAL);
            tv12.setLayoutParams(params);
            tbrow02.addView(tv12);

            doc_detail.addView(tbrow02);
        }

        if (!CRM_COUNT.equals("")) {

            TableRow tbrow02 = new TableRow(context);
            TextView tv02 = new TextView(context);
            tv02.setText("Dr CRM");
            tv02.setTextSize(11);
            tv02.setPadding(5, 5, 5, 0);
            tv02.setTextColor(Color.BLACK);
            tv02.setTypeface(null, Typeface.BOLD);
            tv02.setLayoutParams(params);
            tbrow02.addView(tv02);

            TextView tv12 = new TextView(context);
            tv12.setText(CRM_COUNT);
            tv12.setPadding(5, 5, 5, 0);
            tv12.setTextSize(11);
            tv12.setTextColor(Color.BLACK);
            tv12.setGravity(Gravity.RIGHT);
            tv12.setTypeface(null, Typeface.NORMAL);
            tv12.setLayoutParams(params);
            tbrow02.addView(tv12);

            doc_detail.addView(tbrow02);
        }

        if (!DRCAPM_GROUP.equals("")) {

            TableRow tbrow02 = new TableRow(context);
            TextView tv02 = new TextView(context);
            tv02.setText("Campaign Group");
            tv02.setTextSize(11);
            tv02.setPadding(5, 5, 5, 0);
            tv02.setTextColor(Color.BLACK);
            tv02.setTypeface(null, Typeface.BOLD);
            tv02.setLayoutParams(params);
            tbrow02.addView(tv02);

            TextView tv12 = new TextView(context);
            tv12.setText(DRCAPM_GROUP);
            tv12.setPadding(5, 5, 5, 0);
            tv12.setTextSize(11);
            tv12.setTextColor(Color.BLACK);
            tv12.setGravity(Gravity.RIGHT);
            tv12.setTypeface(null, Typeface.NORMAL);
            tv12.setLayoutParams(params);
            tbrow02.addView(tv12);

            doc_detail.addView(tbrow02);
        }



    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(DrCall.this, ViewPager_2016.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();


        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {
            startActivity(new Intent(DrCall.this, ViewPager_2016.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void OnClickDrLoad(){
        spinImg.setEnabled(false);
        drname.setEnabled(false);
        docList = new ArrayList<SpinnerModel>();
        new GPS_Timmer_Dialog(context,mHandler,"Scanning Doctors...",GPS_TIMMER).show();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case GPS_TIMMER:
                    spinImg.setEnabled(true);
                    drname.setEnabled(true);
                    new DoctorData().execute();
                    break;
                case WORK_WITH_DIALOG:
                    Bundle b1 = msg.getData();
                    work_with_name = b1.getString("workwith_name");
                    work_with_id = b1.getString("workwith_id");
                    workwithdr.setText("" + work_with_name);
                    break;
                case MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL:
                    onDownloadAllResponse();
                    break;
                case MESSAGE_INTERNET_SEND_FCM:
                    customVariablesAndMethod.msgBox(context,"Dr. Added successfully");
                    if (networkUtil.internetConneted(context)) {
                        if (live_km.equalsIgnoreCase("Y")||(live_km.equalsIgnoreCase("Y5"))){
                            MyCustomMethod myCustomMethod= new MyCustomMethod(context);
                            myCustomMethod.stopAlarm10Minute();
                            myCustomMethod.startAlarmIn10Minute();
                        }else {
                            startService(new Intent(context, Sync_service.class));
                        }
                    }
                    if (!call_missed.isChecked() && customVariablesAndMethod.IsProductEntryReq(context)) {
                        Intent i = new Intent(getApplicationContext(), Doctor_Sample.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("id", dr_id);
                        startActivity(i);
                    }
                    finish();
                    break;
                case 99:
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    break;


            }
        }
    };


    private void onDownloadAllResponse(){
        Custom_Variables_And_Method.GPS_STATE_CHANGED=true;
        Custom_Variables_And_Method.GPS_STATE_CHANGED_TIME=customVariablesAndMethod.get_currentTimeStamp();
        new GPS_Timmer_Dialog(context,mHandler,"Scanning Doctors...",GPS_TIMMER).show();
    }
    private void onClickDrName() {
        IsRefreshedClicked = true;
        AlertDialog.Builder myDialog = new AlertDialog.Builder(DrCall.this);
        final EditText editText = new EditText(DrCall.this);
        final ListView listview = new ListView(DrCall.this);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0,  R.mipmap.ref3, 0);
        array_sort = new ArrayList<SpinnerModel>(Arrays.asList(TitleName));
        LinearLayout layout = new LinearLayout(DrCall.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter_new arrayAdapter = new SpinAdapter_new(DrCall.this, R.layout.spin_row, array_sort,
                call_type.equals("1")? 0: showRegistrtion);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                dr_id = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                doc_name = ((TextView) view.findViewById(R.id.spin_name)).getText().toString().split("-")[0];
                drname.setText(doc_name);

                ref_latLong = "";
                latLong  = "";

                if (((TextView) view.findViewById(R.id.distance)).getText().toString().equals("Registration pending...")){
                    if (!customVariablesAndMethod.IsGPS_GRPS_ON(context)) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);
                        drname.setText("---Select---");
                        dr_id="";
                        doc_name="";

                    } else {
                        Intent intent = new Intent(context, Doctor_registration_GPS.class);
                        intent.putExtra("id",dr_id);
                        intent.putExtra("name",doc_name);
                        intent.putExtra("type","D");
                        startActivity(intent);
                        finish();
                    }
                }else if(((TextView) view.findViewById(R.id.distance)).getText().toString().contains("Km Away")) {
                    //getAlert(context,"Not In Range","You are "+((TextView) view.findViewById(R.id.distance)).getText().toString()+" from "+doc_name,true);

                    FragmentManager fm = getSupportFragmentManager();
                    alertdFragment = new Report_Registration();
                    String km=((TextView) view.findViewById(R.id.distance)).getText().toString();
                    alertdFragment.setAlertLocation(array_sort.get(position).getLoc(),array_sort.get(position).getLoc2(),array_sort.get(position).getLoc3());
                    alertdFragment.setAlertData("Not In Range","You are "+km+" from "+doc_name);
                    alertdFragment.show(fm, "Alert Dialog Fragment");
                    km=km.replace("Km Away","").trim();

                    dr_id_reg = dr_id;
                    dr_id_index = "";
                    dr_name_reg=doc_name;
                    if(array_sort.get(position).getLoc2().equals("") && Float.parseFloat(km)< Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"RE_REG_KM","5"))){
                        dr_id_index = "2";
                    }else if(array_sort.get(position).getLoc3().equals("") && Float.parseFloat(km)< Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"RE_REG_KM","5"))){
                        dr_id_index = "3";
                    }

                    drname.setText("---Select---");
                    dr_id="";
                    doc_name="";


                }else if( Integer.parseInt(array_sort.get(position).getFREQ()) != 0 && Integer.parseInt(array_sort.get(position).getFREQ()) <= Integer.parseInt(array_sort.get(position).getNO_VISITED()) ) {
                    customVariablesAndMethod.getAlert(context,"Visit Freq. Exceeded",("For "+doc_name +"@ Allowed Freq. : " + array_sort.get(position).getFREQ() + "@ Visited       : "+array_sort.get(position).getNO_VISITED()).split("@"));
                    drname.setText("---Select---");
                    dr_id="";
                    doc_name="";

                }else {

                    ref_latLong = array_sort.get(position).getREF_LAT_LONG();
                    latLong  = arrayAdapter.latLong;

                        doctor_list = cbohelp.getCallDetail("tempdr", dr_id, "0");

                        if (!doctor_list.get("remark").get(0).equals("")) {
                            String remark=doctor_list.get("remark").get(0);
                            if (remark.contains("\u20B9"))
                                remark=remark.split("\\n")[1];
                            if (remark_list.contains(remark)){
                                btn_remark.setText(remark);
                                dr_remark.setVisibility(View.GONE);
                            }else{
                                btn_remark.setText(remark_list.get(remark_list.size()-1));
                                dr_remark.setVisibility(View.VISIBLE);
                            }
                            dr_remark.setText(remark);
                        } else {
                            dr_remark.setText("");
                            btn_remark.setText("---Select Remak---");
                            dr_remark.setVisibility(View.GONE);
                        }


                        if (!doctor_list.get("time").get(0).equals("")) {
                            remark.setVisibility(View.VISIBLE);
                            remark.setText("You have visited today");
                        } else {
                            remark.setVisibility(View.GONE);
                        }

                        if (!doctor_list.get("workwith").get(0).equals("")) {
                            workwithdr.setText(doctor_list.get("workwith").get(0));
                        } else {
                            workwithdr.setText("");
                        }

                        if (!doctor_list.get("name").get(0).equals("Yet to make your first Call")) {
                            if (doctor_list.get("name").get(0).contains("(M)")) {
                                remark.setText("You have missed the call today");
                                call_missed.setChecked(true);
                            } else {
                                call_missed.setChecked(false);
                            }
                        }

                        if (!doctor_list.get("workwith").get(0).equals("")) {
                            workwithdr.setText(doctor_list.get("workwith").get(0));
                        } else {
                            workwithdr.setText("");
                        }


                        detail_layout.setBackgroundResource(R.drawable.custom_square_transparent_bg);
                        String[] sample_name = array_sort.get(position).getITEM_NAME().split(",");
                        String[] sample_qty = array_sort.get(position).getITEM_SALE().split(",");
                        String[] sample_pob = array_sort.get(position).getITEM_POB().split(",");
                        AREA = array_sort.get(position).getAREA();

                        last_pob_layout(sample_name, sample_qty, sample_pob);

                        Doc_Detail(array_sort.get(position).getCLASS(), array_sort.get(position).getPOTENCY_AMT(), array_sort.get(position).getLastVisited(), AREA, array_sort.get(position).getCRM_COUNT(), array_sort.get(position).getDRCAPM_GROUP());
                }

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textlength = editText.getText().length();
                array_sort.clear();
                for (int i = 0; i < TitleName.length; i++) {
                    if (textlength <= TitleName[i].getName().length()) {

                        if (TitleName[i].getName().toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                            array_sort.add(TitleName[i]);
                        }
                    }
                }
                try {
                    listview.setAdapter(new SpinAdapter_new(DrCall.this, R.layout.spin_row, array_sort,showRegistrtion));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        myalertDialog.dismiss();
                        if(!customVariablesAndMethod.checkIfCallLocationValid(context,true,false)) {
                            customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                            IsRefreshedClicked = true;
                            LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                                    new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                        }else{
                            //new Service_Call_From_Multiple_Classes().DownloadAll(context, mHandler, MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL);
                            service.DownloadAll(context, new Response() {
                                @Override
                                public void onSuccess(Bundle bundle) {
                                    onDownloadAllResponse();
                                }

                                @Override
                                public void onError(String s, String s1) {
                                    AppAlert.getInstance().getAlert(context,s,s1);
                                }
                            });
                        }

                        Vibrator vbr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vbr.vibrate(100);
                        return true;
                    }
                }
                return false;
            }
        });
        myalertDialog = myDialog.show();
    }

    private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contex, Intent intent) {
            Location location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE);
            if ( IsRefreshedClicked ) {
                //new Service_Call_From_Multiple_Classes().DownloadAll(context, mHandler, MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL);
                service.DownloadAll(context, new Response() {
                    @Override
                    public void onSuccess(Bundle bundle) {
                        onDownloadAllResponse();
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                });
            }else{
                submitDoctor(true);
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);

        }
    };

    private void onClickDrCallRemark() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(DrCall.this);
        final ListView listview = new ListView(DrCall.this);
        LinearLayout layout = new LinearLayout(DrCall.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(listview);
        myDialog.setView(layout);
        //ArrayAdapter arrayAdapter = new ArrayAdapter(DrCall.this, R.layout.spin_row, cbohelp.get_Doctor_Call_Remark());
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, remark_list);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                btn_remark.setText(remark_list.get(position));
                if (remark_list.get(position).equalsIgnoreCase("other")){
                    dr_remark.setText("");
                    dr_remark.setVisibility(View.VISIBLE);
                }else{
                    dr_remark.setText(remark_list.get(position));
                    dr_remark.setVisibility(View.GONE);
                }
            }
        });

        myalertDialog = myDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                Bundle b1 = data.getExtras();
                work_with_name = b1.getString("workwith_name");
                work_with_id = b1.getString("workwith_id");
                workwithdr.setText("" + work_with_name);
                break;
            case Report_Registration.REQUEST_CAMERA :
                if (resultCode == RESULT_OK) {


                    File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO"+File.separator+ alertdFragment.filename);





                    if(!dr_id_index.equals("") && ( cbohelp.getDrRc().size()==0 || !cbohelp.getDrRc().contains(dr_id_reg)) && cbohelp.getCallDetail("tempdr", dr_id_reg, "0").get("time").get(0).equals("")){
                        cbohelp.updateLatLong(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON),dr_id_reg,"D",dr_id_index);
                        dr_id = dr_id_reg;
                        doc_name=dr_name_reg;
                        drname.setText(doc_name);

                        if (Custom_Variables_And_Method.internetConneted(context)) {
                            LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, new IntentFilter("SyncComplete"));
                            Sync_service.ReplyYN="Y";
                            progress1.setMessage("Please Wait..\n" +
                                    dr_name_reg +" is being Registered");
                            progress1.setCancelable(false);
                            progress1.show();
                            startService(new Intent(context, Sync_service.class));
                        }else{
                            customVariablesAndMethod.getAlert(context,"Registered",dr_name_reg+" Successfully Re-Registered("+dr_id_index+")");

                        }
                    }else if (file1.exists() && Custom_Variables_And_Method.internetConneted(context)){
                            Location currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation",Location.class);
                            new SendAttachment((Activity) context).execute(Custom_Variables_And_Method.COMPANY_CODE+": Out of Range Error report",context.getResources().getString(R.string.app_name)+"\n Company Code :"+Custom_Variables_And_Method.COMPANY_CODE+"\n DCR ID :"+Custom_Variables_And_Method.DCR_ID+"\n PA ID : "+Custom_Variables_And_Method.PA_ID+"\n App version : "+Custom_Variables_And_Method.VERSION+"\n massege : "+alertdFragment.Alertmassege+
                                    "\nLocation-timestamp : "+currentBestLocation.getTime()+"\nLocation-Lat : "+currentBestLocation.getLatitude()+
                                    "\nLocation-long : "+currentBestLocation.getLongitude()+"\n time : " +customVariablesAndMethod.currentTime(context)+"\nlatlong : "+ customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON),alertdFragment.compressImage(file1));

                    }else{
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);
                    }


                    /**/



                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(context,
                            "image capture cancelled ", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(context,
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }


    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context contex, Intent intent) {
            if (progress1 != null) {
                progress1.dismiss();
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
            if (intent.getStringExtra("message").equals("Y")) {
                customVariablesAndMethod.getAlert(context,"Registered",dr_name_reg+" Successfully Re-Registered("+dr_id_index+")");
            }

        }
    };



}

