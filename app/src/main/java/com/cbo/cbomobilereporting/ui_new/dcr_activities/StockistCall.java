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
import com.cbo.cbomobilereporting.databaseHelper.Call.Db.StockistCallDB;
import com.cbo.cbomobilereporting.databaseHelper.Call.mChemistCall;
import com.cbo.cbomobilereporting.databaseHelper.Call.mStockistCall;
import com.cbo.cbomobilereporting.databaseHelper.Location.LocationDB;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui.LoginFake;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Doctor_registration_GPS;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.Response;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import locationpkg.Const;
import services.MyAPIService;
import services.Sync_service;
import utils.adapterutils.ExpandableListAdapter;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinAdapter_new;
import utils.adapterutils.SpinnerModel;
import com.cbo.cbomobilereporting.MyCustumApplication;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import utils.networkUtil.NetworkUtil;
import utils_new.AppAlert;
import utils_new.Chemist_Gift_Dialog;
import utils_new.Custom_Variables_And_Method;
import utils_new.GPS_Timmer_Dialog;
import utils_new.Report_Registration;
import utils_new.SendAttachment;
import utils_new.Service_Call_From_Multiple_Classes;
import utils_new.Stk_Sample_Dialog;


public class StockistCall extends AppCompatActivity implements ExpandableListAdapter.Summary_interface{

    Button save, products,gift;
    EditText pob, loc,remark;
    Button stk_name;
    CheckBox visited;
    Custom_Variables_And_Method customVariablesAndMethod;
    SpinAdapter adapter;
    ImageView spinImg;
    LinearLayout stk_layout,pob_layout;

    HashMap<String, ArrayList<String>> stockist_list=new HashMap<>();
    List<String> data1;
    int dcrid;
    double result = 0.0;
    String sample ="0.0",rate ="";
    CBO_DB_Helper cbohelp;
    String time;

    String name = "",resultList="";
    String chname = "", stk_id = "", stkst_name = "";
    String chm_ok = "", stk_ok = "", exp_ok = "";
    String name2 = "", name3 = "", name4 = "";
    String network_status;
    SpinnerModel[] TitleName;
    ArrayList<SpinnerModel> array_sort;
    private AlertDialog myalertDialog = null;
    int textlength = 0,showRegistrtion=1;
    TextView batteryLevel;
    NetworkUtil mNetworkUtil;
    Context context;
    String live_km;
    TableLayout stk,gift_layout;
    String sample_name="",sample_pob="",sample_sample="";
    String gift_name="",gift_qty="";


    String sample_name_previous="",sample_pob_previous="",sample_sample_previous="";
    String gift_name_previous="",gift_qty_previous="";

    String dr_id_reg = "",dr_id_index = "",dr_name_reg="";

    LinearLayout call_layout;
    ExpandableListView summary_layout;
    Button tab_call,tab_summary;
    HashMap<String, HashMap<String, ArrayList<String>>> summary_list=new HashMap<>();
    HashMap<String, ArrayList<String>> stockist_list_summary=new HashMap<>();
    ExpandableListAdapter listAdapter;

    ArrayList<SpinnerModel> stkDataList;
    private Location currentBestLocation;

    Report_Registration alertdFragment;
    private  static final int GPS_TIMMER=4, PRODUCT_DILOG=5,GIFT_DILOG=6,MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL=1,MESSAGE_INTERNET_SEND_FCM =2;
    public ProgressDialog progress1;
    boolean IsRefreshedClicked = true;


    String latLong = "";
    String ref_latLong = "";
    Service_Call_From_Multiple_Classes service ;

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



    ///firebase DB
    mStockistCall mstockistCall;
    StockistCallDB stockistCallDB;
    LocationDB locationDB;

    public void onCreate(Bundle b) {

        super.onCreate(b);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.stockist_call);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);
        hader_text.setText("Stockist Call");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        context = StockistCall.this;

        mNetworkUtil = new NetworkUtil(context);
        progress1 = new ProgressDialog(this);
        service =  new Service_Call_From_Multiple_Classes();
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        loc = (EditText) findViewById(R.id.loc_stock);
        pob = (EditText) findViewById(R.id.stk_pob);
        remark=(EditText) findViewById(R.id.remak);
        save = (Button) findViewById(R.id.stk_save);
        gift = (Button) findViewById(R.id.stk_gift);
        products = (Button) findViewById(R.id.stk_product);
        stk_name = (Button) findViewById(R.id.stk_name);
        visited = (CheckBox) findViewById(R.id.myck_stk);
        stk_layout = (LinearLayout) findViewById(R.id.stk_layout);
        stk= (TableLayout) findViewById(R.id.promotion);
        gift_layout = (TableLayout) findViewById(R.id.gift_layout);
        pob_layout= (LinearLayout) findViewById(R.id.pob_layout);

        call_layout = (LinearLayout) findViewById(R.id.call_layout);
        summary_layout = (ExpandableListView) findViewById(R.id.summary_layout);
        tab_call= (Button) findViewById(R.id.call);
        tab_summary= (Button) findViewById(R.id.summary);

        showRegistrtion=Integer.parseInt(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"IsBackDate","1"));

        String mDcrId = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_ID");
        dcrid = Integer.parseInt(mDcrId);
        data1 = new ArrayList<String>();
        cbohelp = new CBO_DB_Helper(StockistCall.this);
        pob.setText("" + result);
        batteryLevel = (TextView) findViewById(R.id.stk_battery_level);
        network_status = NetworkUtil.getConnectivityStatusString(getApplicationContext());
        setLocationToUI();
        live_km = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"live_km");

        stk_name.setText("--Select--");

        LinearLayout visited1= (LinearLayout) findViewById(R.id.visited);
//        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W").equals("OSC") ) {
//            visited1.setVisibility(View.GONE);
//        }

        String working_code = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"working_code","W");
        if (working_code.equals("OSC") || (working_code.contains("NR") && !working_code.contains("S"))) {

            visited1.setVisibility(View.GONE);
        }

        /*String table="phdcrstk";
        if (cbohelp.getmenu_count(table)>0){
            visited1.setVisibility(View.GONE);
        }*/


        locationDB = new LocationDB();
        stockistCallDB = new StockistCallDB();

        stkDataList = new ArrayList<SpinnerModel>();

        spinImg = (ImageView) findViewById(R.id.spinner_img_stockist);
        spinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSpinnerLoad();
            }
        });


        if (Custom_Variables_And_Method.location_required.equals("Y")) {
            stk_layout.setVisibility(View.VISIBLE);
        } else {
            stk_layout.setVisibility(View.GONE);
        }

        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DR_REMARKYN").equalsIgnoreCase("y")) {
            remark.setVisibility(View.VISIBLE);
        }else{
            remark.setVisibility(View.GONE);
        }

        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLE_POB_MANDATORY").equals("N")) {
            pob_layout.setVisibility(View.VISIBLE);
            Custom_Variables_And_Method.SAMPLE_POB_MANDATORY="N";
        } else {
            pob_layout.setVisibility(View.GONE);
            Custom_Variables_And_Method.SAMPLE_POB_MANDATORY="Y";
        }

         //new StokistData().execute();

        //time = getTime();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        visited.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                String table="phdcrstk";
                if (cbohelp.getmenu_count(table)>0 && buttonView.isChecked()){
                    //visited.setVisibility(View.GONE);
                    customVariablesAndMethod.msgBox(context,"Stockist already in the call-List");
                    buttonView.setChecked(false);
                }else if (buttonView.isChecked()) {

                    AppAlert.getInstance().DecisionAlert(context, "ALERT !!!",
                            "Are you sure to save as\n\"No " + "Stockist" + " for the day",
                            new AppAlert.OnClickListener() {
                                @Override
                                public void onPositiveClicked(View item, String result) {
                                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"STOCKIST_NOT_VISITED","Y");
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

        customVariablesAndMethod.getbattrypercentage(context);

        //getbattrypercentage(StockistCall.this);

        stk_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSpinnerLoad();
            }
        });

        if(customVariablesAndMethod.IsProductEntryReq(context)) {
            String ProductCaption = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "SAMPLE_BTN_CAPTION", "");
            if (!ProductCaption.isEmpty())
                products.setText(ProductCaption);

            products.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    String stkName = stk_name.getText().toString();

                    if (stkName.equalsIgnoreCase("--Select--")) {

                        customVariablesAndMethod.msgBox(context, "Please Select Stockist Name First..");
                    } else if (stkName.equalsIgnoreCase("Nothing Found")) {

                        customVariablesAndMethod.msgBox(context, "No Stockist in the List");
                    } else {
                   /* Intent i = new Intent(getApplicationContext(), Stk_Sample.class);

                    i.putExtra("intent_fromRcpaCAll","stk");
                    i.putExtra("sample_name",sample_name);
                    i.putExtra("sample_pob",sample_pob);
                    i.putExtra("sample_sample",sample_sample);

                    startActivityForResult(i, 0);*/

                        Bundle b = new Bundle();
                        b.putString("intent_fromRcpaCAll", "Chem");
                        b.putString("ID",stk_id);
                        b.putString("sample_name", sample_name);
                        b.putString("sample_pob", sample_pob);
                        b.putString("sample_sample", sample_sample);

                        b.putString("sample_name_previous", sample_name_previous);
                        b.putString("sample_pob_previous", sample_pob_previous);
                        b.putString("sample_sample_previous", sample_sample_previous);

                        new Stk_Sample_Dialog(context, mHandler, b, PRODUCT_DILOG).Show();


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

                    String stkName = stk_name.getText().toString();
                    if (stkName.equalsIgnoreCase("--Select--")) {

                        customVariablesAndMethod.msgBox(context, "Please Select Stockist Name First..");
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

                        b.putString("ID", stk_id);
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

               // if (Custom_Variables_And_Method.GLOBAL_LATLON.equalsIgnoreCase("0.0,0.0")) {
                    Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
               // }


                String getCurrentTime = customVariablesAndMethod.get_currentTimeStamp();
                String planTime = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DcrPlantimestamp",customVariablesAndMethod.get_currentTimeStamp());
                float t1 = Float.parseFloat(getCurrentTime);
                float t2 = Float.parseFloat(planTime);

                float t3 = t1 - t2;
                if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"REMARK_WW_MANDATORY").contains("S") &&  remark.getText().toString().equals("")) {
                    customVariablesAndMethod.msgBox(context,"Please enter remak");


                }else if ((t3 >= 0) || (t3 >= -0.9)) {

                    try {
                        setAddressToUI();
                        submitStockist(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    customVariablesAndMethod.msgBox(context,"Your Plan Time can not be \n Higher Than Current time...");
                }

            }

        });

        stockist_list_summary=cbohelp.getCallDetail("phdcrstk","","1");
        //expense_list=new CBO_DB_Helper(context).getCallDetail("tempdr");

        summary_list=new LinkedHashMap<>();
        summary_list.put("Stockist",stockist_list_summary);

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


    public void onRestart() {
        super.onRestart();
        cbohelp = new CBO_DB_Helper(getApplicationContext());
    }

   /* protected void onActivityResult(int reqcode, int rescode, Intent iob) {


        Bundle b1 = iob.getExtras();
        try {
            if(rescode==RESULT_OK) {
                if (b1 != null) {
                    name = b1.getString("val");//id
                    name2 = b1.getString("val2");//qty pob
                    result = b1.getDouble("resultpob");//pob value
                    sample = b1.getString("sampleQty");
                    resultList = b1.getString("resultList");
                    DecimalFormat f = new DecimalFormat("#.00");
                    if ("" + result != null) {
                        String result3 = f.format(result);
                        pob.setText(result3);
                    }

                    sample_name=resultList;
                    sample_sample=sample;
                    sample_pob=name2;

                    String[] sample_name1 = resultList.split(",");
                    String[] sample_qty1 = sample.split(",");
                    String[] sample_pob1 = name2.split(",");
                    init(sample_name1, sample_qty1, sample_pob1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


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

                if (cbohelp.searchStockist(Dr_id).contains(Dr_id)) {
                    stockist_list=cbohelp.getCallDetail("phdcrstk",Dr_id,"1");

                    stk_id = Dr_id;
                    stkst_name = Dr_name;
                    stk_name.setText(stkst_name);

                    mstockistCall = (mStockistCall) new mStockistCall()
                            .setId(Dr_id)
                            .setName(Dr_name)
                            .setDcr_id(MyCustumApplication.getInstance().getUser().getDCRId())
                            .setDcr_date(MyCustumApplication.getInstance().getUser().getDCRDate());


                    String remarktxt=stockist_list.get("remark").get(0);
                    if (remarktxt.contains("\u20B9")) {
                        //mstockistCall.setPOBAmt(remarktxt.split("\n")[0].replace("\u20B9",""));

                        /*if (remarktxt.split("\n").length>1) {
                            remarktxt = remarktxt.split("\n")[1];
                        } else {
                            remarktxt = "";
                        }*/
                        mstockistCall.setPOBAmt(remarktxt.substring(remarktxt.indexOf("\u20B9")+1,remarktxt.indexOf("\n")));
                        pob.setText(mstockistCall.getPOBAmt());
                        remarktxt = remarktxt.substring(remarktxt.indexOf("\n")+1);

                    }
                    remark.setText(remarktxt);

                    if (!stockist_list.get("sample_name").get(0).equals("")) {
                        String[] sample_name1 = stockist_list.get("sample_name").get(0).split(",");
                        String[] sample_qty1 = stockist_list.get("sample_qty").get(0).split(",");
                        String[] sample_pob1 = stockist_list.get("sample_pob").get(0).split(",");

                        sample_name = stockist_list.get("sample_name").get(0);
                        sample_sample = stockist_list.get("sample_qty").get(0);
                        sample_pob = stockist_list.get("sample_pob").get(0);


                       /* sample_name_previous=sample_name;
                        sample_pob_previous=sample_pob;
                        sample_sample_previous=sample_sample;*/


                        init(sample_name1, sample_qty1, sample_pob1);
                    }else{
                        sample_name = "";
                        sample_pob = "";
                        sample_sample = "";
                        stk.removeAllViews();
                    }

                    if (!stockist_list.get("gift_name").get(0).equals("")) {
                        String[] gift_name1 = stockist_list.get("gift_name").get(0).split(",");
                        String[] gift_qty1 = stockist_list.get("gift_qty").get(0).split(",");

                        gift_name = stockist_list.get("gift_name").get(0);
                        gift_qty = stockist_list.get("gift_qty").get(0);

                        gift_name_previous = gift_name;
                        gift_qty_previous = gift_qty;

                        init_gift(gift_layout, gift_name1, gift_qty1);
                    }else{
                        gift_name = "";
                        gift_qty = "";
                        gift_layout.removeAllViews();
                    }

                    save.setText("Update Stockist");
                }else{

                    sample_name="";
                    sample_pob="";
                    sample_sample="";


                    gift_name = "";
                    gift_qty = "";


                    stk.removeAllViews();
                    gift_layout.removeAllViews();

                    save.setText("Add Stockist");
                }

                sample_name_previous = sample_name;
                sample_pob_previous = sample_pob;
                sample_sample_previous = sample_sample;

                gift_name_previous = gift_name;
                gift_qty_previous = gift_qty;

                mstockistCall.setGift_name_Arr(gift_name)
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


        AppAlert.getInstance().setPositiveTxt("Delete").DecisionAlert(context, "Delete!!!", "Do you Really want to delete " + Dr_name + " ?", new AppAlert.OnClickListener() {
            @Override
            public void onPositiveClicked(View item, String result) {

                //Start of call to service

                HashMap<String,String> request=new HashMap<>();
                request.put("sCompanyFolder",  MyCustumApplication.getInstance ().getUser ().getCompanyCode ());
                request.put("iPaId",  MyCustumApplication.getInstance ().getUser ().getID ());
                request.put("iDCR_ID",  MyCustumApplication.getInstance ().getUser ().getDCRId ());
                request.put("iDR_ID", Dr_id);
                request.put("sTableName", "STOCKIST");


                ArrayList<Integer> tables=new ArrayList<>();
                tables.add(0);

                new MyAPIService(context)
                        .execute(new ResponseBuilder("DRCHEMDELETE_MOBILE",request)
                                .setDescription("Please Wait..." +
                                        "\nDeleting "+Dr_name+" from DCR...")
                                .setResponse(new CBOServices.APIResponse() {
                                    @Override
                                    public void onComplete(Bundle bundle) throws Exception {
                                        mstockistCall = (mStockistCall) new mStockistCall().setId(Dr_id);
                                        stockistCallDB.delete(mstockistCall);

                                        cbohelp.delete_Stokist_from_local_all(Dr_id);
                                        customVariablesAndMethod.msgBox(context,Dr_name+" sucessfully Deleted.");
                                        finish();
                                    }

                                    @Override
                                    public void onResponse(Bundle bundle) throws Exception {

                                    }

                                    @Override
                                    public void onError(String s, String s1) {
                                        AppAlert.getInstance().getAlert(context,s,s1);
                                    }
                                }));

                //End of call to service




            }

            @Override
            public void onNegativeClicked(View item, String result) {
            }
        });

    }


    //===========================================================================================================================================


    public void submitStockist(boolean Skip_Verification) {
        //String currentBatteryLevel = batteryLevel.getText().toString();
        String currentBatteryLevel = customVariablesAndMethod.BATTERYLEVEL;
        if (loc.getText().toString().equals("")) {
            loc.setText("UnKnown Location");
        }
        String address = loc.getText().toString();

        String PobAtm = pob.getText().toString();
        String PobAtm1=PobAtm;
        String name1=name;
        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLE_POB_INPUT_MANDATORY").contains("S") ) {
            PobAtm1="-";
            name1="-";
        }

        String AllItemId = "";
        String AllItemQty = "";
        String AllGiftId = "";
        String AllSampleQty = "";
        String AllGiftQty = "";
        String stkName = stk_name.getText().toString();
        if (stkDataList.isEmpty() && stk_id.equals("")) {
            customVariablesAndMethod.msgBox(context,"No Stockist in List....");
        } else if (stkName.equals("--Select--")) {
            customVariablesAndMethod.msgBox(context,"Please Select Stockist from List....");
        } else if (!(PobAtm1.equals("") || PobAtm1.equals("0")) && (sample_name.equals("")) && customVariablesAndMethod.IsProductEntryReq(context)) {
            //if (sample_name.equals("")) {
                customVariablesAndMethod.msgBox(context, "Please select Atleast 1 product first");
            /*}else{
                finish();
            }*/
        } else {

            //for (int i = 0; i < 1; i++) {
                AllItemId = AllItemId + name;
                AllItemQty = AllItemQty + name2;
           // }


            if (name3.equals("")) {
                AllGiftId = AllGiftId + "0";
                AllGiftQty = AllGiftQty + "0";
            } else {

                for (int i = 0; i < 1; i++) {
                    AllGiftId = AllGiftId + name3;
                    AllGiftQty = AllGiftQty + name4;
                }
            }


            Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
            currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation",Location.class);

            String locExtra="";

            if (currentBestLocation!=null) {
                locExtra = "Lat_Long " + currentBestLocation.getLatitude() + "," + currentBestLocation.getLongitude() + ", Accuracy " + currentBestLocation.getAccuracy() + ", Time " + currentBestLocation.getTime() + ", Speed " + currentBestLocation.getSpeed() + ", Provider " + currentBestLocation.getProvider();
            }


            mstockistCall.setRemark(remark.getText().toString());


            if (cbohelp.searchStockist(stk_id).contains(stk_id)) {
                int val = cbohelp.updateStockistInLocal(dcrid, stk_id, PobAtm, AllItemId, AllItemQty,
                        Custom_Variables_And_Method.GLOBAL_LATLON + "!^" + address,
                        customVariablesAndMethod.currentTime(context),sample,remark.getText().toString(),"",
                        AllGiftId,AllGiftQty,rate);
                Log.e("stockist updated", "" + val);
                customVariablesAndMethod.msgBox(context,stkst_name + "  successfully updated");

                new Service_Call_From_Multiple_Classes().SendFCMOnCall(context, mHandler, MESSAGE_INTERNET_SEND_FCM,"S",stk_id,"");
               /* Intent intent = new Intent(getApplicationContext(), LoginFake.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("EXIT", true);
                startActivity(intent);*/

                stockistCallDB.insert(mstockistCall);
                locationDB.insert(mstockistCall);

                finish();
            }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,Skip_Verification)) {
                customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                IsRefreshedClicked = false;
                LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                        new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
            } else {
                try {

                    mstockistCall.setSrno(customVariablesAndMethod.srno(context))
                            .setLOC_EXTRA(locExtra)
                            .setTime(customVariablesAndMethod.currentTime(context));

                    customVariablesAndMethod.SetLastCallLocation(context);

                    long val = cbohelp.submitStockitInLocal(dcrid, stk_id, PobAtm, AllItemId, AllItemQty,
                            latLong+ "!^" + address, customVariablesAndMethod.currentTime(context),
                            currentBatteryLevel,Custom_Variables_And_Method.GLOBAL_LATLON,"0","0",
                            mstockistCall.getSrno(),sample,remark.getText().toString(),"",
                            locExtra,AllGiftId,AllGiftQty,ref_latLong,rate);

                    cbohelp.addStockistInLocal(stk_id, stkst_name);
                    Log.e("stockist submitted", "" + val);
                    if (val != -1) {

                        chm_ok = getmydata().get(0);
                        stk_ok = getmydata().get(1);
                        exp_ok = getmydata().get(2);


                        if (stk_ok.equals("")) {
                            cbohelp.insertfinalTest(chm_ok, stk_id, exp_ok);
                        } else {
                            cbohelp.updatefinalTest(chm_ok, stk_id, exp_ok);
                        }
                        customVariablesAndMethod.msgBox(context,"Stockist Added Successfully");
                        pob.setText("");
                        Custom_Variables_And_Method.STOCKIST_NOT_VISITED = "Y";

                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"STOCKIST_NOT_VISITED","N");

                        new Service_Call_From_Multiple_Classes().SendFCMOnCall(context, mHandler, MESSAGE_INTERNET_SEND_FCM,"S",stk_id,Custom_Variables_And_Method.GLOBAL_LATLON);


                        stockistCallDB.insert(mstockistCall);
                        locationDB.insert(mstockistCall);
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
            if (Custom_Variables_And_Method.global_address != null || Custom_Variables_And_Method.global_address != "") {
                //loc.setText(Custom_Variables_And_Method.global_address);
                loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
            } else {
                loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
            }

        }
    }


    private void onClickSpinnerLoad(){
        spinImg.setEnabled(false);
        stk_name.setEnabled(false);
        new GPS_Timmer_Dialog(context,mHandler,"Scanning Stockist...",GPS_TIMMER).show();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b1;
            switch (msg.what) {

                case PRODUCT_DILOG :

                     b1 = msg.getData();
                    if (b1 != null) {
                        name = b1.getString("val");//id
                        name2 = b1.getString("val2");//qty pob
                        result = b1.getDouble("resultpob");//pob value
                        sample = b1.getString("sampleQty");
                        resultList = b1.getString("resultList");
                        rate = b1.getString("resultRate");
                        DecimalFormat f = new DecimalFormat("#.00");
                        if ("" + result != null) {
                            String result3 = f.format(result);
                            pob.setText(result3);
                        }

                        sample_name = resultList;
                        sample_sample = sample;
                        sample_pob = name2;

                        String[] sample_name1 = resultList.split(",");
                        String[] sample_qty1 = sample.split(",");
                        String[] sample_pob1 = name2.split(",");
                        init(sample_name1, sample_qty1, sample_pob1);

                        mstockistCall.setSample_name_Arr(sample_name)
                                .setSample_pob_Arr(sample_pob)
                                .setSample_qty_Arr(sample_sample);

                    }


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
                   // }

                    mstockistCall.setGift_name_Arr(gift_name)
                            .setGift_qty_Arr(gift_qty);
                    break;
                case GPS_TIMMER:
                    spinImg.setEnabled(true);
                    stk_name.setEnabled(true);
                    new StokistData().execute();
                    break;
                case MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL:
                    onDownloadAllResponse();
                    break;
                case MESSAGE_INTERNET_SEND_FCM:
                    if (mNetworkUtil.internetConneted(context)) {
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
        new GPS_Timmer_Dialog(context,mHandler,"Scanning Stockist...",GPS_TIMMER).show();

    }

    class StokistData extends AsyncTask<ArrayList<SpinnerModel>, String, ArrayList<SpinnerModel>> {
        ProgressDialog pd;
        @Override
        protected ArrayList<SpinnerModel> doInBackground(ArrayList<SpinnerModel>... params) {
            stkDataList = new ArrayList<SpinnerModel>();

            try {

                Cursor c = cbohelp.getStockistListLocal();


                if (c.moveToFirst()) {
                    do {

                        stkDataList.add(new SpinnerModel(c.getString(c.getColumnIndex("pa_name")), c.getString(c.getColumnIndex("pa_id")),"", c.getString(c.getColumnIndex("PA_LAT_LONG")), c.getString(c.getColumnIndex("PA_LAT_LONG2")), c.getString(c.getColumnIndex("PA_LAT_LONG3")), c.getString(c.getColumnIndex("CALLYN"))));
                    } while (c.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return stkDataList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(StockistCall.this);
            pd.setMessage("Processing......." + "\n" + "please wait");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<SpinnerModel> s) {
            super.onPostExecute(s);

            if ((!s.isEmpty()) || (s.size() < 0)) {

                stk_name.setText("--Select--");
                try {
                    pd.dismiss();

                    TitleName = new SpinnerModel[s.size()];
                    for (int i = 0; i < s.size(); i++) {
                        TitleName[i] = s.get(i);
                    }

                    array_sort = new ArrayList<SpinnerModel>(Arrays.asList(TitleName));
                    onClickSpinner();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                pd.dismiss();
                stk_name.setClickable(false);
                spinImg.setClickable(false);
                stk_name.setText("Nothing Found");
                customVariablesAndMethod.getAlert(context,"Stockist not Found","No  Stockist In Planed Dcr..");

            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickSpinner() {

        IsRefreshedClicked = true;

        AlertDialog.Builder myDialog = new AlertDialog.Builder(StockistCall.this);
        final EditText editText = new EditText(StockistCall.this);
        final ListView listview = new ListView(StockistCall.this);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, R.mipmap.ref3, 0);
        array_sort = new ArrayList<SpinnerModel>(Arrays.asList(TitleName));
        LinearLayout layout = new LinearLayout(StockistCall.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter_new arrayAdapter = new SpinAdapter_new(StockistCall.this, R.layout.spin_row, array_sort,showRegistrtion);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();

                mstockistCall = null;
                SpinnerModel model = array_sort.get(position);

                stk_id = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                stkst_name = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
                stk_name.setText(stkst_name);
                latLong = "";
                ref_latLong = "";
                if (((TextView) view.findViewById(R.id.distance)).getText().toString().equals("Registration pending...")){
                    if (!customVariablesAndMethod.IsGPS_GRPS_ON(context)) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);
                        stk_name.setText("---Select---");
                        stk_id="";
                        stkst_name="";
                    } else {
                        Intent intent = new Intent(context, Doctor_registration_GPS.class);
                        intent.putExtra("id",stk_id);
                        intent.putExtra("name",stkst_name);
                        intent.putExtra("type","S");
                        startActivity(intent);
                        finish();
                    }
                }else if(((TextView) view.findViewById(R.id.distance)).getText().toString().contains("Km Away")) {
                    //getAlert(context,"Not In Range","You are "+((TextView) view.findViewById(R.id.distance)).getText().toString()+" from "+chm_name,true);

                    FragmentManager fm = getSupportFragmentManager();
                    alertdFragment = new Report_Registration();
                    String km=((TextView) view.findViewById(R.id.distance)).getText().toString();
                    alertdFragment.setAlertLocation(array_sort.get(position).getLoc(),array_sort.get(position).getLoc2(),array_sort.get(position).getLoc3());
                    alertdFragment.setAlertData("Not In Range","You are "+km+" from "+stkst_name);
                    alertdFragment.show(fm, "Alert Dialog Fragment");

                    km=km.replace("Km Away","").trim();
                    dr_id_reg = stk_id;
                    dr_id_index = "";
                    dr_name_reg=stkst_name;
                    if(array_sort.get(position).getLoc2().equals("")  && Float.parseFloat(km)< Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"RE_REG_KM","5"))){
                        dr_id_index = "2";
                    }else if(array_sort.get(position).getLoc3().equals("")  && Float.parseFloat(km)< Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"RE_REG_KM","5"))){
                        dr_id_index = "3";
                    }

                    stk_name.setText("---Select---");
                    stk_id="";
                    stkst_name="";


                    mstockistCall = (mStockistCall) new mStockistCall()
                            .setId(model.getId())
                            .setName(model.getName())
                            .setArea(model.getAREA())
                            .setDcr_id(MyCustumApplication.getInstance().getUser().getDCRId())
                            .setDcr_date(MyCustumApplication.getInstance().getUser().getDCRDate())
                            .setRef_latlong(model.getREF_LAT_LONG())
                            .setLatLong(arrayAdapter.latLong)
                            .setBattery(MyCustumApplication.getInstance().getUser().getBattery());


                }else {


                    mstockistCall = (mStockistCall) new mStockistCall()
                            .setId(model.getId())
                            .setName(model.getName())
                            .setArea(model.getAREA())
                            .setDcr_id(MyCustumApplication.getInstance().getUser().getDCRId())
                            .setDcr_date(MyCustumApplication.getInstance().getUser().getDCRDate())
                            .setRef_latlong(model.getREF_LAT_LONG())
                            .setLatLong(arrayAdapter.latLong)
                            .setBattery(MyCustumApplication.getInstance().getUser().getBattery());

                    latLong = arrayAdapter.latLong;
                    ref_latLong = array_sort.get(position).getREF_LAT_LONG();




                    if (cbohelp.searchStockist(stk_id).contains(stk_id)) {
                        stockist_list=cbohelp.getCallDetail("phdcrstk",stk_id,"0");


                        String remarktxt=stockist_list.get("remark").get(0);
                        if (remarktxt.contains("\u20B9")) {
                            /*mstockistCall.setPOBAmt(remarktxt.split("\n")[0].replace("\u20B9",""));
                            pob.setText(mstockistCall.getPOBAmt());
                            if (remarktxt.split("\n").length>1) {
                                remarktxt = remarktxt.split("\n")[1];
                            } else {
                                remarktxt = "";
                            }*/
                            mstockistCall.setPOBAmt(remarktxt.substring(remarktxt.indexOf("\u20B9")+1,remarktxt.indexOf("\n")));
                            pob.setText(mstockistCall.getPOBAmt());
                            remarktxt = remarktxt.substring(remarktxt.indexOf("\n")+1);

                        }
                        remark.setText(remarktxt);
                        mstockistCall.setRemark(remarktxt);

                        if (!stockist_list.get("sample_name").get(0).equals("")) {
                            String[] sample_name1 = stockist_list.get("sample_name").get(0).split(",");
                            String[] sample_qty1 = stockist_list.get("sample_qty").get(0).split(",");
                            String[] sample_pob1 = stockist_list.get("sample_pob").get(0).split(",");

                            sample_name = stockist_list.get("sample_name").get(0);
                            sample_sample = stockist_list.get("sample_qty").get(0);
                            sample_pob = stockist_list.get("sample_pob").get(0);

                            init(sample_name1, sample_qty1, sample_pob1);
                        }else{
                            sample_name="";
                            sample_pob="";
                            sample_sample="";
                            stk.removeAllViews();
                        }

                        if (!stockist_list.get("gift_name").get(0).equals("")) {
                            String[] gift_name1 = stockist_list.get("gift_name").get(0).split(",");
                            String[] gift_qty1 = stockist_list.get("gift_qty").get(0).split(",");

                            gift_name = stockist_list.get("gift_name").get(0);
                            gift_qty = stockist_list.get("gift_qty").get(0);

                            init_gift(gift_layout, gift_name1, gift_qty1);
                        }else{
                            gift_name = "";
                            gift_qty = "";
                            gift_layout.removeAllViews();
                        }

                        save.setText("Update Stockist");
                    }else{

                        remark.setText("");
                        sample_name="";
                        sample_pob="";
                        sample_sample="";


                        gift_name = "";
                        gift_qty = "";

                        stk.removeAllViews();
                        gift_layout.removeAllViews();

                        save.setText("Add Stockist");
                    }

                    sample_name_previous=sample_name;
                    sample_pob_previous=sample_pob;
                    sample_sample_previous=sample_sample;

                    gift_name_previous = gift_name;
                    gift_qty_previous = gift_qty;

                    mstockistCall.setGift_name_Arr(gift_name)
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
                listview.setAdapter(new SpinAdapter_new(StockistCall.this, R.layout.spin_row, array_sort,showRegistrtion));
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
                submitStockist(true);
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);

        }
    };




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                try {
                    if(resultCode==RESULT_OK) {
                        Bundle b1 = data.getExtras();
                        if (b1 != null) {
                            name = b1.getString("val");//id
                            name2 = b1.getString("val2");//qty pob
                            result = b1.getDouble("resultpob");//pob value
                            sample = b1.getString("sampleQty");
                            rate = b1.getString("resultRate");
                            resultList = b1.getString("resultList");
                            DecimalFormat f = new DecimalFormat("#.00");

                            String result3 = f.format(result);
                            pob.setText(result3);


                            sample_name=resultList;
                            sample_sample=sample;
                            sample_pob=name2;

                            String[] sample_name1 = resultList.split(",");
                            String[] sample_qty1 = sample.split(",");
                            String[] sample_pob1 = name2.split(",");
                            init(sample_name1, sample_qty1, sample_pob1);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

                    if(!dr_id_index.equals("") && (cbohelp.searchStockist(dr_id_reg).size()==0 || !cbohelp.searchStockist(dr_id_reg).contains(dr_id_reg))){
                        cbohelp.updateLatLong(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON),dr_id_reg,"S",dr_id_index);

                        stk_id = dr_id_reg;
                        stkst_name=dr_name_reg;
                        stk_name.setText(stkst_name);


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

            LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
            if (intent.getStringExtra("message").equals("Y")) {
                customVariablesAndMethod.getAlert(context,"Registered",dr_name_reg+" Successfully Re-Registered("+dr_id_index+")");
            }
        }
    };



}



	

