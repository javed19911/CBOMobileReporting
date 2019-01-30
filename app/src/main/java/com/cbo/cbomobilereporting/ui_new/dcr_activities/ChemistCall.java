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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.databaseHelper.Call.Db.ChemistCallDB;
import com.cbo.cbomobilereporting.databaseHelper.Call.mChemistCall;
import com.cbo.cbomobilereporting.databaseHelper.Location.LocationDB;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui.LoginFake;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Doctor_registration_GPS;
import com.flurry.android.FlurryAgent;
import com.uenics.javed.CBOLibrary.Response;

import java.io.File;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import locationpkg.Const;
import services.Sync_service;
import utils.CBOUtils.Constants;
import utils.adapterutils.ExpandableListAdapter;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinAdapter_new;
import utils.adapterutils.SpinnerModel;
import utils.clearAppData.MyCustumApplication;
import utils.networkUtil.NetworkUtil;
import utils_new.AppAlert;
import utils_new.Chemist_Gift_Dialog;
import utils_new.Custom_Variables_And_Method;
import utils_new.GPS_Timmer_Dialog;
import utils_new.Report_Registration;
import utils_new.Chm_Sample_Dialog;
import utils_new.SendAttachment;
import utils_new.Service_Call_From_Multiple_Classes;


public class ChemistCall extends AppCompatActivity implements ExpandableListAdapter.Summary_interface{

    ResultSet rs;
    NetworkUtil networkUtil;
    Button save, products, gift, refreshLocation, retail_save;
    EditText pob,remark;
    TextView loc, chem_name, chemist_not_visited;
    Button ch_name;
    CheckBox visit;
    Custom_Variables_And_Method customVariablesAndMethod;
    SpinAdapter adapter;
    List<String> data1;
    LinearLayout chm_layout,pob_layout;
    int PA_ID;
    String time, Hide_status;
    ArrayList<SpinnerModel> chemist;

    CBO_DB_Helper cbohelp;
    double result = 0.0;
    String sample ="0.0",rate ="";
    String name = "", chm_id = "", chm_name = "",resultList="",dr_name_reg="",dr_id_reg = "",dr_id_index = "";
    String name2 = "", name3 = "", name4 = "";
    String chname = "";
    String chm_ok = "", stk_ok = "", exp_ok = "";
    String sample_name="",sample_pob="",sample_sample="";
    String gift_name="",gift_qty="";

    String sample_name_previous="",sample_pob_previous="",sample_sample_previous="";
    String gift_name_previous="",gift_qty_previous="";

    HashMap<String, ArrayList<String>> chemist_list=new HashMap<>();
    String network_status;
    SpinnerModel[] TitleName;
    ArrayList<SpinnerModel> array_sort;
    private AlertDialog myalertDialog = null;
    int textlength = 0,showRegistrtion=1;
    TextView chm_BatteryLevel;
    ImageView spinImg;
    NetworkUtil mNetworkUtil;
    Context context ;
    String updated="0";
    String chem_km ="0";
    String live_km,head;

    TableLayout stk,gift_layout;
    LinearLayout call_layout;
    ExpandableListView summary_layout;
    Button tab_call,tab_summary;
    HashMap<String, HashMap<String, ArrayList<String>>> summary_list=new HashMap<>();
    HashMap<String, ArrayList<String>> chemist_list_summary=new HashMap<>();
    ExpandableListAdapter listAdapter;
    private Location currentBestLocation=null;

    Report_Registration alertdFragment;
    private  static final int GPS_TIMMER=4, GIFT_DILOG=6,PRODUCT_DILOG=5, MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL=1,MESSAGE_INTERNET_SEND_FCM=2;;
    public ProgressDialog progress1;
    boolean IsRefreshedClicked = true;

    String latLong = "";
    String ref_latLong = "";

    Service_Call_From_Multiple_Classes service ;


    ///firebase DB
    mChemistCall mchemistCall;
    ChemistCallDB chemistCallDB;
    LocationDB locationDB;

  /*  public String getTime() {
        String mytime = "";
        Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        mytime = String.format("%02d.%02d", mHour, mMinute);
        return mytime;
    }*/

    public ArrayList<String> getmydata() {
        ArrayList<String> raw = new ArrayList<String>();
        StringBuilder chm = new StringBuilder();
        StringBuilder stk = new StringBuilder();
        StringBuilder exp = new StringBuilder();
        Cursor c = cbohelp.getFinalSubmit();
        if (c.moveToFirst()) {
            do {
                chm.append(c.getString(c.getColumnIndex("chemist")));
                stk.append(c.getString(c.getColumnIndex("stockist")));
                exp.append(c.getString(c.getColumnIndex("exp")));
            } while (c.moveToNext());

        }
        raw.add(chm.toString());
        raw.add(stk.toString());
        raw.add(exp.toString());
        return raw;
    }



    public ArrayList<SpinnerModel> getChemistListLocal(int id) {
        ArrayList<SpinnerModel> chemist = new ArrayList<SpinnerModel>();
        Cursor c = cbohelp.getChemistListLocal();

        if (c.moveToFirst()) {
            do {
                data1.add(c.getString(c.getColumnIndex("chem_name")) + "\n\n\n" + c.getString(c.getColumnIndex("chem_id")));
                chemist.add(new SpinnerModel(c.getString(c.getColumnIndex("chem_name")), c.getString(c.getColumnIndex("chem_id"))));
            } while (c.moveToNext());
        }

        return chemist;
    }

    public void setChemistList() {
        ArrayList<SpinnerModel> chemlist = new Doback2().doInBackground(PA_ID);
        adapter = new SpinAdapter(getApplicationContext(), R.layout.spin_row, chemlist);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

    }


    public void onCreate(Bundle b) {
        super.onCreate(b);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.chemist_call);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        FlurryAgent.logEvent("Chemist Call");


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        context = ChemistCall.this;
        progress1 = new ProgressDialog(this);
        networkUtil = new NetworkUtil(context);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance(context);
        service =  new Service_Call_From_Multiple_Classes();
        loc = (TextView) findViewById(R.id.loc_chem);
        chem_name = (TextView) findViewById(R.id.chemist_name);
        chemist_not_visited = (TextView) findViewById(R.id.not_visited);
        pob = (EditText) findViewById(R.id.chm_pob);
        remark=(EditText) findViewById(R.id.remak);
        save = (Button) findViewById(R.id.chm_save);
        retail_save = (Button) findViewById(R.id.retail_save);
        visit = (CheckBox) findViewById(R.id.myck);
        chm_layout = (LinearLayout) findViewById(R.id.chm_layout);

        pob_layout= (LinearLayout) findViewById(R.id.pob_layout);
        Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);

        gift = (Button) findViewById(R.id.chm_gift);
        data1 = new ArrayList<String>();
        products = (Button) findViewById(R.id.chm_product);
        ch_name = (Button) findViewById(R.id.chm_name);
        cbohelp = customVariablesAndMethod.get_cbo_db_instance();
        PA_ID = Custom_Variables_And_Method.PA_ID;
        pob.setText("" + result);
        chm_BatteryLevel = (TextView) findViewById(R.id.chm_battery_level);
        mNetworkUtil = new NetworkUtil(context);
        live_km =customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"live_km");

        stk= (TableLayout) findViewById(R.id.promotion);

        gift_layout = (TableLayout) findViewById(R.id.gift_layout);
        call_layout = (LinearLayout) findViewById(R.id.call_layout);
        summary_layout = (ExpandableListView) findViewById(R.id.summary_layout);
        tab_call= (Button) findViewById(R.id.call);
        tab_summary= (Button) findViewById(R.id.summary);

        LinearLayout visited= (LinearLayout) findViewById(R.id.visited);

        String working_code = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W");
        if (working_code.equals("OCC") || (working_code.contains("NR") && !working_code.contains("C"))) {

            visited.setVisibility(View.GONE);
        }



        locationDB = new LocationDB();
        chemistCallDB = new ChemistCallDB();

        ch_name.setText("---Select---");


        spinImg = (ImageView) findViewById(R.id.spinner_img_chemist_call);
        spinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSpinnerLoad();
            }
        });

        showRegistrtion=Integer.parseInt(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"IsBackDate","1"));

        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLE_POB_MANDATORY").equals("N")) {
            pob_layout.setVisibility(View.VISIBLE);
            Custom_Variables_And_Method.SAMPLE_POB_MANDATORY="N";
        } else {
            pob_layout.setVisibility(View.GONE);
            Custom_Variables_And_Method.SAMPLE_POB_MANDATORY="Y";
        }

        network_status = NetworkUtil.getConnectivityStatusString(context);
        setLocationToUI();

        if (hader_text != null) {
            head=cbohelp.getMenu("DCR", "D_CHEMCALL").get("D_CHEMCALL");
            hader_text.setText(head);
        }

        Hide_status = Constants.getSIDE_STATUS(ChemistCall.this);
        if (Hide_status != null) {
            if ((Hide_status.equalsIgnoreCase("N")) || (Hide_status.equals(""))) {
                chem_name.setText(head.split(" ")[0]+" Name");
                chemist_not_visited.setText(head.split(" ")[0]+" Not Visited");
                retail_save.setVisibility(View.GONE);
            } else {
                chem_name.setText(head.split(" ")[0]+" Name");
                chemist_not_visited.setText(head.split(" ")[0]+" Not Visited");
                save.setVisibility(View.GONE);
                retail_save.setVisibility(View.VISIBLE);
            }
        }

        //get batterry level
        customVariablesAndMethod.getbattrypercentage(context);
        //getbattrypercentage(ChemistCall.this);


        //time = getTime();
        if (Custom_Variables_And_Method.location_required.equals("Y")) {
            chm_layout.setVisibility(View.VISIBLE);
        }

        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DR_REMARKYN").equalsIgnoreCase("y")) {
            remark.setVisibility(View.VISIBLE);
        }else{
            remark.setVisibility(View.GONE);
        }
        chemist = new ArrayList<SpinnerModel>();
       /* new ChemistData().execute();*/


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        visit.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                String table="chemisttemp";
                if (cbohelp.getmenu_count(table)>0 && buttonView.isChecked()){
                    //visited.setVisibility(View.GONE);
                    customVariablesAndMethod.msgBox(context,"Chemist already in the call-List");
                    buttonView.setChecked(false);
                }else if (buttonView.isChecked()) {
                    AppAlert.getInstance().DecisionAlert(context, "ALERT !!!",
                            "Are you sure to save as\n\"No " + head + " for the day\"",
                            new AppAlert.OnClickListener() {
                                @Override
                                public void onPositiveClicked(View item, String result) {
                                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHEMIST_NOT_VISITED","Y");
                                    finish();
                                }

                                @Override
                                public void onNegativeClicked(View item, String result) {
                                    buttonView.setChecked(false);
                                }
                            });

                }

            }
        });


        //========================================sample_item==============================================================================

        ch_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSpinnerLoad();

            }
        });

        if (customVariablesAndMethod.IsProductEntryReq(context)) {
            String ProductCaption = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "SAMPLE_BTN_CAPTION", "");
            if (!ProductCaption.isEmpty())
                products.setText(ProductCaption);
            products.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String chemName = ch_name.getText().toString();
                    if (chemName.equalsIgnoreCase("---Select---")) {

                        customVariablesAndMethod.msgBox(context, "Please Select " + head + " First..");
                    } else {
                    /*Intent i = new Intent(getApplicationContext(), Chm_Sample.class);
                    i.putExtra("intent_fromRcpaCAll","chem");
                    i.putExtra("sample_name",sample_name);
                    i.putExtra("sample_pob",sample_pob);
                    i.putExtra("sample_sample",sample_sample);
                    startActivityForResult(i, 0);*/


                        Bundle b = new Bundle();
                        b.putString("intent_fromRcpaCAll", "Chem");
                        b.putString("sample_name", sample_name);
                        b.putString("sample_pob", sample_pob);
                        b.putString("sample_sample", sample_sample);

                        b.putString("sample_name_previous", sample_name_previous);
                        b.putString("sample_pob_previous", sample_pob_previous);
                        b.putString("sample_sample_previous", sample_sample_previous);

                        new Chm_Sample_Dialog(context, mHandler, b, PRODUCT_DILOG).Show();
                    }

                }
            });

            String GiftCaption = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "GIFT_BTN_CAPTION", "");
            if (!GiftCaption.isEmpty())
                gift.setText(GiftCaption);

            gift.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    String chemName = ch_name.getText().toString();
                    if (chemName.equalsIgnoreCase("---Select---")) {

                        customVariablesAndMethod.msgBox(context, "Please Select " + head + " First..");
                    } else {
                    /*Intent i = new Intent(getApplicationContext(), Chemist_Gift.class);
                    i.putExtra("intent_fromRcpaCAll","dr");
                    i.putExtra("gift_name",gift_name);
                    i.putExtra("gift_qty",gift_qty);
                    startActivityForResult(i, 1);*/
                        Bundle b = new Bundle();
                        b.putString("intent_fromRcpaCAll", "dr");
                        b.putString("gift_name", gift_name);
                        b.putString("gift_qty", gift_qty);

                        b.putString("gift_name_previous", gift_name_previous);
                        b.putString("gift_qty_previous", gift_qty_previous);

                        b.putString("ID", chm_id);
                        new Chemist_Gift_Dialog(context, mHandler, b, GIFT_DILOG).Show();
                    }
                }
            });
        }else{
            gift.setVisibility(View.GONE);
            products.setVisibility(View.GONE);
        }

        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String getCurrentTime = customVariablesAndMethod.get_currentTimeStamp();
                String planTime = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DcrPlantimestamp",customVariablesAndMethod.get_currentTimeStamp());
                float t1 = Float.parseFloat(getCurrentTime);
                float t2 = Float.parseFloat(planTime);

                float t3 = t1 - t2;
                if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"REMARK_WW_MANDATORY").contains("C") &&  remark.getText().toString().equals("")) {
                    customVariablesAndMethod.msgBox(context,"Please enter remak");


                }else if ((t3 >= 0) || (t3 >= -0.9)) {

                    setAddressToUI();
                    submitChemist(false);


                } else {
                    customVariablesAndMethod.msgBox(context,"Your Plan Time can not be \n Higher Than Current time...");
                }

            }

        });

        retail_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                    setAddressToUI();
                    submitChemist(false);

            }
        });


        chemist_list_summary=cbohelp.getCallDetail("chemisttemp","","1");


        summary_list=new LinkedHashMap<>();
        summary_list.put(head,chemist_list_summary);

        final ArrayList<String> header_title=new ArrayList<>();
        //final List<Integer> visible_status=new ArrayList<>();
        for(String main_menu:summary_list.keySet()){
            header_title.add(main_menu);
            //visible_status.add(0);
        }



        listAdapter = new ExpandableListAdapter(summary_layout,this, header_title, summary_list);

        // setting list adapter
        summary_layout.setAdapter(listAdapter);
        summary_layout.setGroupIndicator(null);
        for(int i=0; i < listAdapter.getGroupCount(); i++)
            summary_layout.expandGroup(i);
        //doctor.expandGroup(1);

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
                call_layout.setVisibility(View.VISIBLE);
                summary_layout.setVisibility(View.GONE);
                tab_call.setBackgroundResource(R.drawable.tab_selected);
                tab_summary.setBackgroundResource(R.drawable.tab_deselected);
            }
        });

        tab_summary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                summary_layout.setVisibility(View.VISIBLE);
                call_layout.setVisibility(View.GONE);
                tab_call.setBackgroundResource(R.drawable.tab_deselected);
                tab_summary.setBackgroundResource(R.drawable.tab_selected);
            }
        });

    }


    //===========================================onCreate end==============================================================================

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, "M3GXGNKRRC8F9VPNYYY4");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void onResume() {
        super.onResume();
        if (loc.getText().toString().equals("")) {
            loc.setText(Custom_Variables_And_Method.global_address);
        }

    }




    private void init(String[] sample_name, String[] sample_qty, String[] sample_pob) {
        //TableLayout stk= (TableLayout) findViewById(R.id.promotion);
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        TableRow tbrow0 = new TableRow(context);
        tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText("Product");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.WHITE);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(" Sample ");
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextColor(Color.WHITE);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(context);
        tv2.setPadding(5, 5, 5, 0);
        tv2.setText(" POB ");
        tv2.setTextColor(Color.WHITE);
        tv2.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv2);
        stk.removeAllViews();
        stk.addView(tbrow0);
        for (int i = 0; i < sample_name.length; i++) {
            TableRow tbrow = new TableRow(context);
            TextView t1v = new TextView(context);
            t1v.setText(sample_name[i]);
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);
            TextView t2v = new TextView(context);
            t2v.setText(sample_qty[i]);
            t2v.setPadding(5, 5, 5, 0);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(context);
            t3v.setText(sample_pob[i]);
            t3v.setPadding(5, 5, 5, 0);
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            stk.addView(tbrow);
        }




    }


    private void init_gift(TableLayout stk1, String[] gift_name, String[] gift_qty) {
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        TableRow tbrow0 = new TableRow(context);
        tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText("Gift");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.WHITE);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(" Qty. ");
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextColor(Color.WHITE);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        stk1.removeAllViews();
        stk1.addView(tbrow0);
        for (int i = 0; i < gift_name.length; i++) {
            TableRow tbrow = new TableRow(context);
            TextView t1v = new TextView(context);
            t1v.setText(gift_name[i]);
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);
            TextView t2v = new TextView(context);
            t2v.setText(gift_qty[i]);
            t2v.setPadding(5, 5, 5, 0);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            stk1.addView(tbrow);
        }
        if(gift_name.length==1 && gift_name[0].equals("0")){
            stk1.removeAllViews();
        }

    }


    class ChemistData extends AsyncTask<ArrayList<SpinnerModel>, String, ArrayList<SpinnerModel>> {
        ProgressDialog pd;


        @Override
        protected ArrayList<SpinnerModel> doInBackground(ArrayList<SpinnerModel>... params) {
            chemist = new ArrayList<SpinnerModel>();

            try {
                Cursor c = cbohelp.getChemistListLocal();

                if (c.moveToFirst()) {
                    do {
                        chemist.add(new SpinnerModel(c.getString(c.getColumnIndex("chem_name")), c.getString(c.getColumnIndex("chem_id")), c.getString(c.getColumnIndex("LAST_VISIT_DATE")), c.getString(c.getColumnIndex("DR_LAT_LONG")), c.getString(c.getColumnIndex("DR_LAT_LONG2")), c.getString(c.getColumnIndex("DR_LAT_LONG3")), c.getString(c.getColumnIndex("CALLYN"))));
                    } while (c.moveToNext());
                }
            } catch (Exception e) {
            }


            return chemist;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ChemistCall.this);
            pd.setMessage("Processing......." + "\n" + "please wait");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            /*double t1 = Double.parseDouble(PageLoadtime);
            double t2 = Double.parseDouble(customVariablesAndMethod.get_currentTimeStamp());
            *//*double diff=t2-t1;
            customVariablesAndMethod.getAlert(context,"time",t1 +" "+t2 + " "+diff);*//*
            if ( t2-t1 < 2*60*1000 && ( !Custom_Variables_And_Method.GPS_STATE_CHANGED || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "GEO_FANCING_KM", "0").equals("0") || showRegistrtion == 0)){
                pd.setMessage("Processing......." + "\n" + "please wait");
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                pd.show();
            }else{
                PageLoadtime=customVariablesAndMethod.get_currentTimeStamp();
                dilog.show();
            }*/

        }

        @Override
        protected void onPostExecute(ArrayList<SpinnerModel> s) {
            super.onPostExecute(s);
            try {
                pd.dismiss();
                if ((!s.isEmpty()) || (s.size() < 0)) {
                    //new Doback().execute(gps);
                    TitleName = new SpinnerModel[s.size()];
                    for (int i = 0; i <= s.size()-1; i++) {
                        TitleName[i] = s.get(i);
                    }


                    array_sort = new ArrayList<SpinnerModel>(Arrays.asList(TitleName));
                    onClickSpinner();
                } else {
                    ch_name.setClickable(false);
                    spinImg.setClickable(false);
                    ch_name.setText("Nothing Found");
                    customVariablesAndMethod.getAlert(context,head+" not Found","No  "+head+" In Planed Dcr..");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Doback2 extends AsyncTask<Integer, Void, ArrayList<SpinnerModel>> {
        ProgressDialog pd;


        protected ArrayList<SpinnerModel> doInBackground(Integer... params) {
            ArrayList<SpinnerModel> getchem = getChemistListLocal(params[0]);

            return getchem;


        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ChemistCall.this);
            pd.setTitle("CBO");
            pd.setMessage("Processing......." + "\n" + "please wait");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<SpinnerModel> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            setChemistList();
            pd.dismiss();


        }
    }

    public void  submitChemist(boolean Skip_Verification) {
        //String currentBatterLevel = chm_BatteryLevel.getText().toString();
        String currentBatterLevel = customVariablesAndMethod.BATTERYLEVEL;
        if (loc.getText().toString().equals("")) {
            loc.setText("UnKnown Location");
        }
        String chm_name = ch_name.getText().toString();

        String address = loc.getText().toString();
        String dcrid = Custom_Variables_And_Method.DCR_ID;
        String PobAmt = pob.getText().toString();
        String AllItemId = "";
        String AllItemQty = "";
        String AllGiftId = "";
        String AllSampleQty = "";
        String AllGiftQty = "";
        if (chemist.isEmpty() && chm_id.equals("")) {
            if (Hide_status.equals("N")) {
                customVariablesAndMethod.msgBox(context,"No Chemist in List....");
            } else {
                customVariablesAndMethod.msgBox(context,"No Retailer in List....");


            }
        } else if (chm_name.equals("---Select---")) {
            if (Hide_status.equals("N")) {
                customVariablesAndMethod.msgBox(context,"Please Select  Chemist from List....");
            } else {
                customVariablesAndMethod.msgBox(context,"Please Select  Retailer from List....");
            }

        } else if (!(PobAmt.equals("") || PobAmt.equals("0")) && (sample_name.equals("")) && customVariablesAndMethod.IsProductEntryReq(context)) {
            //if (sample_name.equals("")) {
                customVariablesAndMethod.msgBox(context, "Please select Atleast 1 product first");
            /*}else{
                finish();
            }*/
        } else {
            for (int i = 0; i < 1; i++) {
                AllItemId = AllItemId + name;
                AllItemQty = AllItemQty + name2;
            }

            if (name3.equals("")) {
                AllGiftId = AllGiftId + "0";
                AllGiftQty = AllGiftQty + "0";
            } else {

                for (int i = 0; i < 1; i++) {
                    AllGiftId = AllGiftId + name3;
                    AllGiftQty = AllGiftQty + name4;
                }
            }


            mchemistCall.setRemark(remark.getText().toString());


            if (cbohelp.searchChemist(chm_id).contains(chm_id)) {
                int val = cbohelp.updateChemistInLocal(dcrid, chm_id, PobAmt, AllItemId, AllItemQty,
                        Custom_Variables_And_Method.GLOBAL_LATLON + "!^" + address, AllGiftId, AllGiftQty,
                        time,sample,remark.getText().toString(),"",rate);
                Log.e("chemist updated", "" + val);
                customVariablesAndMethod.msgBox(context,chm_name + "  successfully updated");


                chemistCallDB.insert(mchemistCall);
                locationDB.insert(mchemistCall);


                new Service_Call_From_Multiple_Classes().SendFCMOnCall(context, mHandler, MESSAGE_INTERNET_SEND_FCM,"C",chm_id,"");

            } else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,Skip_Verification)) {
                customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                IsRefreshedClicked = false;
                LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                        new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
            }else {
                try {
                    Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
                    currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation",Location.class);

                    customVariablesAndMethod.SetLastCallLocation(context);

                    String locExtra="";

                    if (currentBestLocation!=null) {
                        locExtra = "Lat_Long " + currentBestLocation.getLatitude() + "," + currentBestLocation.getLongitude() + ", Accuracy " + currentBestLocation.getAccuracy() + ", Time " + currentBestLocation.getTime() + ", Speed " + currentBestLocation.getSpeed() + ", Provider " + currentBestLocation.getProvider();
                    }


                    mchemistCall.setSrno(customVariablesAndMethod.srno(context))
                            .setLOC_EXTRA(locExtra)
                            .setTime(customVariablesAndMethod.currentTime(context));

                    long val = cbohelp.submitChemistInLocal(dcrid, chm_id, PobAmt, AllItemId, AllItemQty,
                            latLong + "!^" + address, AllGiftId, AllGiftQty, customVariablesAndMethod.currentTime(context),
                            currentBatterLevel,sample,remark.getText().toString(),"",locExtra,ref_latLong,rate);
                    cbohelp.addChemistInLocal(chm_id, chm_name,""+customVariablesAndMethod.currentTime(context), latLong, Custom_Variables_And_Method.global_address,updated,chem_km,mchemistCall.getSrno(),locExtra);
                    Log.e("chemist submit in local", "" + val);
                    Log.e("chemist details", dcrid + "," + chm_id + "," + PobAmt + "," + AllItemId + "," + AllItemQty + "," + address + "," + AllGiftId + "," + AllGiftQty);
                    if (val != -1) {

                        chm_ok = getmydata().get(0);
                        stk_ok = getmydata().get(1);
                        exp_ok = getmydata().get(2);


                        if (chm_ok.equals("")) {
                            cbohelp.insertfinalTest(chm_id, stk_ok, exp_ok);
                        } else {
                            cbohelp.updatefinalTest(chm_id, stk_ok, exp_ok);
                        }
                        if (Hide_status.equals("N")) {
                            customVariablesAndMethod.msgBox(context,"Chemist Added Successfully");
                        } else {
                            customVariablesAndMethod.msgBox(context,"Retailer Added Successfully");
                        }

                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHEMIST_NOT_VISITED","N");
                        pob.setText("");
                        Custom_Variables_And_Method.CHEMIST_NOT_VISITED = "Y";

                        chemistCallDB.insert(mchemistCall);
                        locationDB.insert(mchemistCall);

                        new Service_Call_From_Multiple_Classes().SendFCMOnCall(context, mHandler, MESSAGE_INTERNET_SEND_FCM,"C",chm_id,Custom_Variables_And_Method.GLOBAL_LATLON);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {

            //startActivity(new Intent(ChemistCall.this, ViewPager_2016.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
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
        Alert_message.setText("Do you want to edit "+Dr_name+" ?");


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                call_layout.setVisibility(View.VISIBLE);
                summary_layout.setVisibility(View.GONE);
                tab_call.setBackgroundResource(R.drawable.tab_selected);
                tab_summary.setBackgroundResource(R.drawable.tab_deselected);


                chm_id = Dr_id;
                chm_name = Dr_name;
                Custom_Variables_And_Method.CHEMIST_ID = chm_id;
                ch_name.setText(chm_name);


                mchemistCall = (mChemistCall) new mChemistCall()
                        .setId(Dr_id)
                        .setName(Dr_name)
                        .setDcr_id(MyCustumApplication.getInstance().getUser().getDCRId())
                        .setDcr_date(MyCustumApplication.getInstance().getUser().getDCRDate());


                TextView last_visited= (TextView) findViewById(R.id.last_visited);
                for (int i=0;i<chemist.size();i++){
                    if (chemist.get(i).getId().equals(Dr_id)){
                        last_visited.setVisibility(View.VISIBLE);
                        last_visited.setText("Last visited on : "+chemist.get(i).getLastVisited());
                        break;
                    }
                }

                if (cbohelp.searchChemist(Dr_id).contains(Dr_id)) {
                    chemist_list=cbohelp.getCallDetail("chemisttemp",Dr_id,"0");


                    String remarkWithPOB = chemist_list.get("remark").get(0);
                    if (remarkWithPOB.contains("\u20B9"))
                        remarkWithPOB = remarkWithPOB.substring(remarkWithPOB.indexOf("\n"));
                    remark.setText(remarkWithPOB);


                    if (!chemist_list.get("sample_name").get(0).equals("")) {
                        String[] sample_name1= chemist_list.get("sample_name").get(0).split(",");
                        String[] sample_qty1= chemist_list.get("sample_qty").get(0).split(",");
                        String[] sample_pob1= chemist_list.get("sample_pob").get(0).split(",");

                        sample_name=chemist_list.get("sample_name").get(0);
                        sample_sample=chemist_list.get("sample_qty").get(0);
                        sample_pob=chemist_list.get("sample_pob").get(0);

                        init(sample_name1, sample_qty1, sample_pob1);
                    }else{
                        sample_name = "";
                        sample_pob = "";
                        sample_sample = "";
                        stk.removeAllViews();
                    }

                    if (!chemist_list.get("gift_name").get(0).equals("")) {
                        String[] gift_name1= chemist_list.get("gift_name").get(0).split(",");
                        String[] gift_qty1= chemist_list.get("gift_qty").get(0).split(",");

                        gift_name=chemist_list.get("gift_name").get(0);
                        gift_qty=chemist_list.get("gift_qty").get(0);

                        init_gift(gift_layout,gift_name1,gift_qty1);
                    }else{
                        gift_name = "";
                        gift_qty = "";
                        gift_layout.removeAllViews();
                    }



                    save.setText("Update Chemist");
                    retail_save.setText("Update Retailer");
                }else{

                    sample_name = "";
                    sample_pob = "";
                    sample_sample = "";

                    gift_name = "";
                    gift_qty = "";
                }

                sample_name_previous=sample_name;
                sample_pob_previous=sample_pob;
                sample_sample_previous=sample_sample;

                gift_name_previous = gift_name;
                gift_qty_previous = gift_qty;


                mchemistCall.setGift_name_Arr(gift_name)
                        .setGift_qty_Arr(gift_qty)
                        .setSample_name_Arr(sample_name)
                        .setSample_pob_Arr(sample_pob)
                        .setSample_qty_Arr(sample_sample);


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

    @Override
    public void delete_Call(final String Dr_id, final String Dr_name) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        final Button Alert_Nagative= (Button) dialogLayout.findViewById(R.id.nagative);
        Alert_title.setText("Delete!!!");
        Alert_message.setText("Do you Really want to delete "+Dr_name+" ?");
        Alert_Nagative.setText("Cancel");
        Alert_Positive.setText("Delete");

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                mchemistCall = (mChemistCall) new mChemistCall().setId(Dr_id);
                chemistCallDB.delete(mchemistCall);

                cbohelp.delete_Chemist_from_local_all(Dr_id);
                customVariablesAndMethod.msgBox(context,Dr_name+" sucessfully Deleted.");
                finish();
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

    private void onClickSpinnerLoad(){
        spinImg.setEnabled(false);
        ch_name.setEnabled(false);
        new GPS_Timmer_Dialog(context,mHandler,"Scanning "+head+"...",GPS_TIMMER).show();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b1;
            switch (msg.what) {
                case PRODUCT_DILOG:
                    b1 = msg.getData();
                    name = b1.getString("val");//id
                    name2 = b1.getString("val2");//score or pob
                    result = b1.getDouble("resultpob");
                    sample = b1.getString("sampleQty");// sample
                    rate = b1.getString("resultRate");
                    DecimalFormat f = new DecimalFormat("#.00");
                    resultList = b1.getString("resultList");
                    String result3 = f.format(result);
                    pob.setText(result3);

                    sample_name=resultList;
                    sample_pob=name2;
                    sample_sample=sample;

                    mchemistCall.setSample_name_Arr(sample_name)
                            .setSample_pob_Arr(sample_pob)
                            .setSample_qty_Arr(sample_sample);

                    String[] sample_name1 = resultList.split(",");
                    String[] sample_qty1 = sample.split(",");
                    String[] sample_pob1 = name2.split(",");
                    init(sample_name1, sample_qty1, sample_pob1);

                    break;
                case GIFT_DILOG:
                    b1 = msg.getData();
                    name3 = b1.getString("giftid");
                    name4 = b1.getString("giftqan");
                   // if (!b1.getString("giftname").equals("")) {
                        String[] gift_name1= b1.getString("giftname").split(",");
                        String[] gift_qty1= b1.getString("giftqan").split(",");

                        gift_name=b1.getString("giftname");
                        gift_qty=b1.getString("giftqan");

                        init_gift(gift_layout,gift_name1,gift_qty1);


                    mchemistCall.setGift_name_Arr(gift_name)
                            .setGift_qty_Arr(gift_qty);
                    //}
                    break;
                case GPS_TIMMER:
                    spinImg.setEnabled(true);
                    ch_name.setEnabled(true);
                    new ChemistData().execute();
                    break;
                case MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL:
                    onDownloadAllResponse();
                    break;
                case MESSAGE_INTERNET_SEND_FCM:
                    if(mNetworkUtil.internetConneted(context)){

                        if (live_km.equalsIgnoreCase("Y")||(live_km.equalsIgnoreCase("Y5"))){
                            MyCustomMethod myCustomMethod= new MyCustomMethod(context);
                            myCustomMethod.stopAlarm10Minute();
                            myCustomMethod.startAlarmIn10Minute();
                        }else {
                            startService(new Intent(context, Sync_service.class));
                        }
                    }

                    Intent intent = new Intent(getApplicationContext(), LoginFake.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);

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
        new GPS_Timmer_Dialog(context,mHandler,"Scanning "+head+"...",GPS_TIMMER).show();
    }

    public void onClickSpinner() {
        IsRefreshedClicked = true;
        AlertDialog.Builder myDialog = new AlertDialog.Builder(ChemistCall.this);
        final EditText editText = new EditText(ChemistCall.this);
        final ListView listview = new ListView(ChemistCall.this);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0,  R.mipmap.ref3, 0);
        array_sort = new ArrayList<SpinnerModel>(Arrays.asList(TitleName));
        LinearLayout layout = new LinearLayout(ChemistCall.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter_new arrayAdapter = new SpinAdapter_new(ChemistCall.this, R.layout.spin_row, array_sort,showRegistrtion);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();

                mchemistCall = null;
                SpinnerModel model = array_sort.get(position);


                chm_id = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                chm_name = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();

                ref_latLong = "";
                latLong  = "";

                if (((TextView) view.findViewById(R.id.distance)).getText().toString().equals("Registration pending...")){
                    if (!customVariablesAndMethod.IsGPS_GRPS_ON(context)) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);
                        ch_name.setText("---Select---");
                        chm_id="";
                        chm_name="";
                    } else {
                        Intent intent = new Intent(context, Doctor_registration_GPS.class);
                        intent.putExtra("id",chm_id);
                        intent.putExtra("name",chm_name);
                        intent.putExtra("type","C");
                        startActivity(intent);
                        finish();
                    }
                }else if(((TextView) view.findViewById(R.id.distance)).getText().toString().contains("Km Away")) {
                    //getAlert(context,"Not In Range","You are "+((TextView) view.findViewById(R.id.distance)).getText().toString()+" from "+chm_name,true);

                    FragmentManager fm = getSupportFragmentManager();
                    alertdFragment = new Report_Registration();
                    String km=((TextView) view.findViewById(R.id.distance)).getText().toString();

                    alertdFragment.setAlertLocation(array_sort.get(position).getLoc(),array_sort.get(position).getLoc2(),array_sort.get(position).getLoc3());
                    alertdFragment.setAlertData("Not In Range","You are "+km+" from "+chm_name);
                    alertdFragment.show(fm, "Alert Dialog Fragment");
                    km=km.replace("Km Away","").trim();

                    dr_id_reg = chm_id;
                    dr_id_index = "";
                    dr_name_reg=chm_name;
                    if(array_sort.get(position).getLoc2().equals("")  && Float.parseFloat(km)< Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"RE_REG_KM","5"))){
                        dr_id_index = "2";
                    }else if(array_sort.get(position).getLoc3().equals("")  && Float.parseFloat(km)< Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"RE_REG_KM","5"))){
                        dr_id_index = "3";
                    }

                    ch_name.setText("---Select---");
                    chm_id="";
                    chm_name="";
                }else {
                    Custom_Variables_And_Method.CHEMIST_ID = chm_id;
                    ch_name.setText(chm_name);


                    mchemistCall = (mChemistCall) new mChemistCall()
                            .setId(model.getId())
                            .setName(model.getName())
                            .setArea(model.getAREA())
                            .setDcr_id(MyCustumApplication.getInstance().getUser().getDCRId())
                            .setDcr_date(MyCustumApplication.getInstance().getUser().getDCRDate())
                            .setRef_latlong(model.getREF_LAT_LONG())
                            .setLatLong(arrayAdapter.latLong)
                            .setBattery(MyCustumApplication.getInstance().getUser().getBattery());

                    ref_latLong = array_sort.get(position).getREF_LAT_LONG();
                    latLong  = arrayAdapter.latLong;

                    TextView last_visited = (TextView) findViewById(R.id.last_visited);
                    if (!chemist.get(position).getLastVisited().equals("")) {
                        last_visited.setVisibility(View.VISIBLE);
                        last_visited.setText("Last visited on : " + chemist.get(position).getLastVisited());
                    } else {
                        last_visited.setVisibility(View.GONE);
                    }



                    if (cbohelp.searchChemist(chm_id).contains(chm_id)) {

                        chemist_list = cbohelp.getCallDetail("chemisttemp", chm_id, "0");

                        String remarktxt=chemist_list.get("remark").get(0);
                        if (remarktxt.contains("\u20B9")) {
                            if (remarktxt.split("\\n").length>1) {
                                remarktxt = remarktxt.split("\\n")[1];
                            } else {
                                remarktxt = "";
                            }

                        }
                        remark.setText(remarktxt);

                        if (!chemist_list.get("sample_name").get(0).equals("")) {
                            String[] sample_name1 = chemist_list.get("sample_name").get(0).split(",");
                            String[] sample_qty1 = chemist_list.get("sample_qty").get(0).split(",");
                            String[] sample_pob1 = chemist_list.get("sample_pob").get(0).split(",");

                            sample_name = chemist_list.get("sample_name").get(0);
                            sample_sample = chemist_list.get("sample_qty").get(0);
                            sample_pob = chemist_list.get("sample_pob").get(0);

                            init(sample_name1, sample_qty1, sample_pob1);
                        }else{
                            sample_name = "";
                            sample_pob = "";
                            sample_sample = "";
                            stk.removeAllViews();
                        }

                        if (!chemist_list.get("gift_name").get(0).equals("")) {
                            String[] gift_name1 = chemist_list.get("gift_name").get(0).split(",");
                            String[] gift_qty1 = chemist_list.get("gift_qty").get(0).split(",");

                            gift_name = chemist_list.get("gift_name").get(0);
                            gift_qty = chemist_list.get("gift_qty").get(0);

                            init_gift(gift_layout, gift_name1, gift_qty1);
                        }else{
                            gift_name = "";
                            gift_qty = "";
                            gift_layout.removeAllViews();
                        }



                        save.setText("Update Chemist");
                        retail_save.setText("Update Retailer");
                    } else {
                        sample_name = "";
                        sample_pob = "";
                        sample_sample = "";

                        gift_name = "";
                        gift_qty = "";

                        remark.setText("");

                        stk.removeAllViews();
                        gift_layout.removeAllViews();
                        save.setText("Add Chemist");
                        retail_save.setText("Add Retailer");
                    }


                    sample_name_previous=sample_name;
                    sample_pob_previous=sample_pob;
                    sample_sample_previous=sample_sample;

                    gift_name_previous = gift_name;
                    gift_qty_previous = gift_qty;


                    mchemistCall.setGift_name_Arr(gift_name)
                            .setGift_qty_Arr(gift_qty)
                            .setSample_name_Arr(sample_name)
                            .setSample_pob_Arr(sample_pob)
                            .setSample_qty_Arr(sample_sample);

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
               // editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textlength = editText.getText().length();

                array_sort.clear();
                for (int i = 0; i < TitleName.length; i++) {
                    if (textlength <= TitleName[i].getName().length()) {

                        if (TitleName[i].getName().toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                            array_sort.add(TitleName[i]);
                        }
                    }
                }
                listview.setAdapter(new SpinAdapter_new(ChemistCall.this, R.layout.spin_row, array_sort,showRegistrtion));
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
                submitChemist(true);
            }//new Service_Call_From_Multiple_Classes().DownloadAll(context,mHandler,MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);

        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if(resultCode==RESULT_OK) {
                    Bundle b1 = data.getExtras();
                    name = b1.getString("val");//id
                    name2 = b1.getString("val2");//score or pob
                    result = b1.getDouble("resultpob");
                    sample = b1.getString("sampleQty");// sample
                    DecimalFormat f = new DecimalFormat("#.00");
                    resultList = b1.getString("resultList");
                    String result3 = f.format(result);
                    pob.setText(result3);

                    sample_name=resultList;
                    sample_pob=name2;
                    sample_sample=sample;

                    String[] sample_name1 = resultList.split(",");
                    String[] sample_qty1 = sample.split(",");
                    String[] sample_pob1 = name2.split(",");
                    init(sample_name1, sample_qty1, sample_pob1);
                }
                break;

            case 1:
                if(resultCode==RESULT_OK) {
                    Bundle b1 = data.getExtras();
                    name3 = b1.getString("giftid");
                    name4 = b1.getString("giftqan");
                    if (!b1.getString("giftname").equals("")) {
                        String[] gift_name1= b1.getString("giftname").split(",");
                        String[] gift_qty1= b1.getString("giftqan").split(",");

                        gift_name=b1.getString("giftname");
                        gift_qty=b1.getString("giftqan");

                        init_gift(gift_layout,gift_name1,gift_qty1);
                    }
                }
                break;
            case Report_Registration.REQUEST_CAMERA :
                if (resultCode == RESULT_OK) {


                    File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO"+File.separator+ alertdFragment.filename);

                    if(!dr_id_index.equals("") && (cbohelp.searchChemist(dr_id_reg).size()==0 || !cbohelp.searchChemist(dr_id_reg).contains(dr_id_reg))){
                        cbohelp.updateLatLong(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON),dr_id_reg,"C",dr_id_index);

                        chm_id = dr_id_reg;
                        chm_name=dr_name_reg;
                        ch_name.setText(chm_name);


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
            if (intent.getStringExtra("message").equals("Y")) {
                customVariablesAndMethod.getAlert(context,"Registered",dr_name_reg+" Successfully Re-Registered("+dr_id_index+")");
            }
        }
    };


}





