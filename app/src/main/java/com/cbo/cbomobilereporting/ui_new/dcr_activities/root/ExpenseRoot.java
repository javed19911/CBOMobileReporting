package com.cbo.cbomobilereporting.ui_new.dcr_activities.root;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.aDA;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.eExpense;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mDA;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mExpHead;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import services.CboServices;
import services.ServiceHandler;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.GalleryUtil;
import utils.adapterutils.Expenses_Adapter;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.interfaces.RecycleViewOnItemClickListener;
import utils_new.up_down_ftp;

public class ExpenseRoot extends AppCompatActivity implements Expenses_Adapter.Expense_interface,up_down_ftp.AdapterCallback {

    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    private final int REQUEST_CAMERA=201;
    private static final int OTHER_EXPENSE = 10;
    String picturePath="";
    private File output=null;
    String filename="";
    TableLayout DA_layout;
    private ArrayList<mDA> DA_Types = new ArrayList<>();

    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1,MESSAGE_INTERNET_SAVE_EXPENSE=2,MESSAGE_INTERNET_DCR_COMMITEXP=3,MESSAGE_INTERNET_DCR_DELETEEXP=4;


    Spinner exphead;
    EditText daAmt,da_root, distAmt, exhAmt, rem, rem_final;
    Button save, save_exp,add_exp;
    ListView mylist;
    TextView datype,distAmt1, distanse, textRemark,attach_txt,routeStausTxt;
    Expenses_Adapter sm;
    LinearLayout mainlayout,actual_fare_layout,actual_DA_layout,manual_DA_layout;
    SpinAdapter adapter,adapter1;
    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    int PA_ID;
    CBO_DB_Helper cbohelp;
    String paid = "", ttl_distance = "", exp_id = "",exp_hed = "",my_Amt="",my_rem="",id="";
    String DistRate = "", datype_val = "";
    String dcr_id = "";
    String dist_id3 = "";
    ArrayList<String> station, exp_head;
    String chm_ok = "", stk_ok = "", exp_ok = "";
    ArrayList<String> rootdata = new ArrayList<String>();
    ServiceHandler myServiceHandler;
    String value;
    Boolean resultTrue, myval=false;
    ImageView attach_img,attachnew;
    String ROUTE_CLASS = "",ACTUALDA_FAREYN = "",ACTUALFAREYN_MANDATORY="";
    Double ACTUALFARE_MAXAMT = 0D;
    Button btn_DaType;
    ImageView DaType_img;

    ArrayList<Map<String, String>> data = null;
    AlertDialog myalertDialog = null;


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


    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.expense_root);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        textView.setText("Expense ");

        progress1 = new ProgressDialog(this);

        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

        mainlayout = (LinearLayout) this.findViewById(R.id.layout1_root);
        actual_fare_layout = findViewById(R.id.actual_fare_layout);
        actual_DA_layout = findViewById(R.id.actual_DA_layout);
        manual_DA_layout = findViewById(R.id.manual_DA_layout);

        datype = (TextView) findViewById(R.id.da_type_root);
        distanse = (TextView) findViewById(R.id.da_distance_root);
        exphead = (Spinner) findViewById(R.id.exp_head_root);
        daAmt = (EditText) findViewById(R.id.ex_da_root);
        da_root = (EditText) findViewById(R.id.da_root);
        distAmt = (EditText) findViewById(R.id.ex_dis_root);
        distAmt1 = (TextView) findViewById(R.id.ex_dis_root1);
        save_exp = (Button) findViewById(R.id.save_back1_root);
        mylist = (ListView) findViewById(R.id.list_exp_root);
        attachnew = (ImageView) findViewById(R.id.attachnew);
        attach_txt = findViewById(R.id.attach_txt);
        routeStausTxt = findViewById(R.id.ROUTE_CLASS);

        add_exp = (Button) findViewById(R.id.add_exp);
        DA_layout = (TableLayout) findViewById(R.id.DA_layout);

        cbohelp = new CBO_DB_Helper(context);

        dcr_id = Custom_Variables_And_Method.DCR_ID;
        PA_ID = Custom_Variables_And_Method.PA_ID;
        paid = "" + PA_ID;
        myServiceHandler = new ServiceHandler(context);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cbohelp.getCompanyCode());
        request.put("iPaId",paid);
        request.put("iDcrId", dcr_id);

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);
        tables.add(1);
        tables.add(2);
        tables.add(3);

        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(this,mHandler).customMethodForAllServices(request,"DCREXPDDLALLROUTE_MOBILE",MESSAGE_INTERNET,tables);

        //End of call to service


        //======================================================================================================================
        exp_head = new ArrayList<String>();

        DaType_img = findViewById(R.id.DaType_img);
        DaType_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickManualDaType();
            }
        });

        btn_DaType = findViewById(R.id.btn_DaType);
        btn_DaType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickManualDaType();
            }
        });


        add_exp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //capture_Image();
                Add_expense("0","","","","","");

            }
        });


        distAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ACTUALFARE_MAXAMT>0){
                    if ((distAmt.getText().toString().trim().isEmpty() ? 0D :Double.parseDouble( distAmt.getText().toString())) > ACTUALFARE_MAXAMT){
                        AppAlert.getInstance().Alert(context, "Alert!!!",
                                "Your AcutaFare cannot exceed " + AddToCartView.toCurrency(String.format("%.2f", (ACTUALFARE_MAXAMT))), new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        distAmt.setText(""+ACTUALFARE_MAXAMT);
                                    }
                                });
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        save_exp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                /*datype_val = datype.getText().toString();
                if (datype_val.equals("--Select--")) {
                    customVariablesAndMethod.msgBox(context,"Please Select your DA TYPE");
                } else */
                if ( actual_fare_layout.getVisibility()==View.VISIBLE
                        && distAmt.getText().toString().equals("")
                        && !ACTUALFAREYN_MANDATORY.equalsIgnoreCase("N")) {
                    customVariablesAndMethod.msgBox(context,"Please Enter the Actual Fare....");
                }else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"EXP_ATCH_YN","N").equals("Y")
                        &&  actual_fare_layout.getVisibility()==View.VISIBLE
                        && attach_txt.getText().toString().equals("* Attach Picture....")) {
                    customVariablesAndMethod.msgBox(context,"Please Attach supporting File for Actual Fare....");
                }else if(cbohelp.get_DA_ACTION_exp_head().size()>0  && actual_DA_layout.getVisibility() == View.VISIBLE
                        && !da_root.getText().toString().isEmpty() && !da_root.getText().toString().equals("0")){
                    AppAlert.getInstance().Alert(context, "Already Applied for DA...",
                            "Please make DA amount Rs 0.", new OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                }else if ( actual_fare_layout.getVisibility()==View.VISIBLE ){

                        progress1.setMessage("Please Wait..\nuploading Image");
                        progress1.setCancelable(false);
                        progress1.show();
                        exp_id = "-1";
                        my_Amt = distAmt.getText().toString();
                        my_rem = "Actual Fare";
                        if(!attach_txt.getText().toString().equals("* Attach Picture....")) {
                            filename = attach_txt.getText().toString();
                            File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator +  filename);
                            new up_down_ftp().uploadFile(file2,ExpenseRoot.this);
                        }else{
                            filename = "";
                            other_expense_commit();
                        }




                }else {

                    expense_commit();

                }
            }
        });

        attachnew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                exp_id="-1";
                //capture_Image();
                PopupMenu menu = new PopupMenu(v.getContext(), v);
                menu.getMenu().add("Camera");
                menu.getMenu().add("Gallery");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Camera")){
                            if (ContextCompat.checkSelfPermission(ExpenseRoot.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ExpenseRoot.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //takePictureButton.setEnabled(false);
                                ActivityCompat.requestPermissions(ExpenseRoot.this, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, ExpenseRoot.this.REQUEST_CAMERA);
                                Toast.makeText(ExpenseRoot.this, "Please allow the permission", Toast.LENGTH_LONG).show();

                            }else {

                                capture_Image();
                            }
                        }else{
                            if (ContextCompat.checkSelfPermission(ExpenseRoot.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //takePictureButton.setEnabled(false);
                                ActivityCompat.requestPermissions(ExpenseRoot.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, ExpenseRoot.this.GALLERY_ACTIVITY_CODE);
                                Toast.makeText(ExpenseRoot.this, "Please allow the permission", Toast.LENGTH_LONG).show();

                            }else {
                                open_galary();

                            }
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
    }

    private void onClickManualDaType() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        final RecyclerView itemlist_filter = new RecyclerView(context);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(itemlist_filter);
        myDialog.setView(layout);
        aDA arrayAdapter = new aDA(context, DA_Types);

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(arrayAdapter);
        arrayAdapter.setOnClickListner(new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                datype_val = DA_Types.get(position).getCode();
                btn_DaType.setText(DA_Types.get(position).getName());
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DA_TYPE",datype_val);
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"da_val","" + DA_Types.get(position).getDAAmount());
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"distance_val","" + DA_Types.get(position).getTAAmount());
                init_DA_type(DA_layout);
                myalertDialog.dismiss();
            }
        });


        myalertDialog = myDialog.show();
    }
    private void expense_commit(){

        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "da_val",da_root.getText().toString().isEmpty()? "0" : da_root.getText().toString());

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cbohelp.getCompanyCode());
        request.put("iDcrId", dcr_id);
        request.put("sDaType", datype_val);
        request.put("iDistanceId", dist_id3);
        request.put("iDA_VALUE", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "da_val","0"));
        request.put("DA_TYPE_SAVEYN",manual_DA_layout.getVisibility()== View.GONE ? "N": "Y");

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(ExpenseRoot.this,mHandler).customMethodForAllServices(request,"DCR_COMMITEXP_3",MESSAGE_INTERNET_DCR_COMMITEXP,tables);

        //End of call to service
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ACTUALFARE",distAmt.getText().toString());
        distAmt.setText("");

        if (actual_fare_layout.getVisibility()!=View.VISIBLE){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ACTUALFARE","0");
        }
    }

    @Override
    public void Edit_Expense(String who, String hed, String amt, String rem, String path, String hed_id) {
        if (!hed_id.equalsIgnoreCase("-1")) {
            Add_expense(who, hed, amt, rem, path, hed_id);
        }
    }

    @Override
    public void delete_Expense(final String hed_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Start of call to service
                exp_id=hed_id;
                HashMap<String,String> request=new HashMap<>();
                request.put("sCompanyFolder",cbohelp.getCompanyCode());
                request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
                request.put("iDCR_ID", dcr_id);
                request.put("iID", hed_id);

                ArrayList<Integer> tables=new ArrayList<>();
                tables.add(0);

                progress1.setMessage("Please Wait..");
                progress1.setCancelable(false);
                progress1.show();

                new CboServices(ExpenseRoot.this,mHandler).customMethodForAllServices(request,"DCREXPDELETEMOBILE_1",MESSAGE_INTERNET_DCR_DELETEEXP,tables);

                //End of call to service

            }
        })
                .setCancelable(false)
                .setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        dialog.setMessage("Are you sure, you want to delete");
        dialog.show();
    }

    private void init_DA_type(TableLayout stk) {

        stk.removeAllViews();
        TableRow tbrow0 = new TableRow(context);
        //tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText("DA. Type");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.BLACK);
        tv0.setTypeface(null, Typeface.NORMAL);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "DA_TYPE"));
        tv1.setGravity(Gravity.RIGHT);
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(null, Typeface.NORMAL);
        tbrow0.addView(tv1);
        stk.addView(tbrow0);

        TableRow tbrow1 = new TableRow(context);
        //tbrow1.setBackgroundColor(0xff125688);
        TextView tv10 = new TextView(context);
        tv10.setText("DA. Value");
        tv10.setPadding(5, 5, 5, 0);
        tv10.setTextColor(Color.BLACK);
        tv10.setTypeface(null, Typeface.NORMAL);
        tv10.setLayoutParams(params);
        tbrow1.addView(tv10);
        TextView tv11 = new TextView(context);
        tv11.setText(context.getResources().getString(R.string.rs) + " " + customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "da_val","0"));
        tv11.setPadding(5, 5, 5, 0);
        tv11.setTextColor(Color.BLACK);
        tv11.setGravity(Gravity.RIGHT);
        tv11.setTypeface(null, Typeface.NORMAL);
        tbrow1.addView(tv11);
        stk.addView(tbrow1);

        Float Dis_val =0f;
        if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "ACTUALFAREYN","").equalsIgnoreCase("Y")) {
            TableRow tbrow2 = new TableRow(context);
            //tbrow2.setBackgroundColor(0xff125688);
            TextView tv21 = new TextView(context);
            tv21.setText("TA. Value");
            tv21.setPadding(5, 5, 5, 0);
            tv21.setTextColor(Color.BLACK);
            tv21.setTypeface(null, Typeface.NORMAL);
            tv21.setLayoutParams(params);
            tbrow2.addView(tv21);
            TextView tv22 = new TextView(context);
            tv22.setText(context.getResources().getString(R.string.rs) + " " + customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "distance_val", "0"));
            tv22.setGravity(Gravity.RIGHT);
            tv22.setPadding(5, 5, 5, 0);
            tv22.setTextColor(Color.BLACK);
            tv22.setTypeface(null, Typeface.NORMAL);
            tbrow2.addView(tv22);
            stk.addView(tbrow2);
            Dis_val = Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "distance_val","0"));
        }

        TableRow tbrow4 = new TableRow(context);
        //tbrow4.setBackgroundColor(0xff125688);
        TextView tv40 = new TextView(context);
        tv40.setText("Other Value");
        tv40.setPadding(5, 5, 5, 0);
        tv40.setTextColor(Color.BLACK);
        tv40.setTypeface(null, Typeface.NORMAL);
        tv40.setLayoutParams(params);
        tbrow4.addView(tv40);
        TextView tv41 = new TextView(context);


        Float other = 0f;
        //datanum.put("amount", c.getString(c.getColumnIndex("amount")));
        for (int i = 0; i <data.size();i++){
            other+=Float.parseFloat(data.get(i).get("amount"));
        }

        tv41.setText(context.getResources().getString(R.string.rs)+" "+other);
        tv41.setPadding(5, 5, 5, 0);
        tv41.setTextColor(Color.BLACK);
        tv41.setGravity(Gravity.RIGHT);
        tv41.setTypeface(null, Typeface.NORMAL);
        tbrow4.addView(tv41);
        stk.addView(tbrow4);

        Float net_value = Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "da_val","0"))
                + Dis_val
                + other;


        TableRow tbrow3 = new TableRow(context);
        //tbrow3.setBackgroundColor(0xff125688);
        TextView tv31 = new TextView(context);
        tv31.setText("Total Expenses");
        tv31.setPadding(5, 5, 5, 0);
        tv31.setTextColor(Color.BLACK);
        tv31.setTypeface(null, Typeface.BOLD);
        tv31.setLayoutParams(params);
        tbrow3.addView(tv31);
        TextView tv32 = new TextView(context);

        tv32.setText(context.getResources().getString(R.string.rs) + " " + net_value);

        tv32.setGravity(Gravity.RIGHT);
        tv32.setPadding(5, 5, 5, 0);
        tv32.setTextColor(Color.BLACK);
        tv32.setTypeface(null, Typeface.BOLD);
        tbrow3.addView(tv32);
        stk.addView(tbrow3);

        TableRow tbrow5 = new TableRow(context);
        tbrow5.setPadding(2,2,2,2);
        tbrow5.setBackgroundColor(0xff125688);
        stk.addView(tbrow5);

    }




    public void Add_expense(final String who, final String hed, final String amt, final String rem, final String path, final String hed_id){
        filename="";
        AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseRoot.this);

        builder.setPositiveButton("ADD", null)
                .setCancelable(false)
                .setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.add_exp, null);
        exphead = (Spinner) dialogLayout.findViewById(R.id.exp_head_root);
        exhAmt = (EditText)  dialogLayout.findViewById(R.id.ex_head_root);
        TextView head = (TextView)  dialogLayout.findViewById(R.id.head);
        textRemark = (TextView) dialogLayout.findViewById(R.id.text_remark);
        final TextView ex_head_root_txt = (TextView) dialogLayout.findViewById(R.id.ex_head_root_txt);
        rem_final = (EditText) dialogLayout.findViewById(R.id.exp_remark_root);
        final CheckBox add_attachment = (CheckBox) dialogLayout.findViewById(R.id.add_attachment);
        final RadioGroup attach_option = (RadioGroup) dialogLayout.findViewById(R.id.attach_option);
        attach_img= (ImageView) dialogLayout.findViewById(R.id.attach_img);
        attach_img.setVisibility(View.GONE);
        final String[] ext = {path};
        attach_option.setVisibility(View.GONE);
        final String[] DA_ACTION = {"0"};

        final mExpHead[] expHead = {null};
        final Boolean[] keyPressed = {true};

        add_attachment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!exp_id.equals("")) {
                    if (add_attachment.isChecked()) {
                        attach_option.setVisibility(View.VISIBLE);
                    } else {
                        attach_option.setVisibility(View.GONE);
                    }
                }else{
                    customVariablesAndMethod.msgBox(context,"Please select the Expense head");
                    add_attachment.setChecked(false);
                }
            }
        });



        attach_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id=attach_option.getCheckedRadioButtonId();
                if (id == R.id.attach) {
                    if (ContextCompat.checkSelfPermission(ExpenseRoot.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //takePictureButton.setEnabled(false);
                        ActivityCompat.requestPermissions(ExpenseRoot.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, ExpenseRoot.this.GALLERY_ACTIVITY_CODE);
                        Toast.makeText(ExpenseRoot.this, "Please allow the permission", Toast.LENGTH_LONG).show();

                    }else {
                        open_galary();

                    }

                }else if (id == R.id.cam){

                    if (ContextCompat.checkSelfPermission(ExpenseRoot.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ExpenseRoot.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //takePictureButton.setEnabled(false);
                        ActivityCompat.requestPermissions(ExpenseRoot.this, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, ExpenseRoot.this.REQUEST_CAMERA);
                        Toast.makeText(ExpenseRoot.this, "Please allow the permission", Toast.LENGTH_LONG).show();

                    }else {

                        capture_Image();
                    }

                }

            }

        });

        exphead.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                exp_id = ((TextView) arg1.findViewById(R.id.spin_id)).getText().toString();
                exp_hed= ((TextView) arg1.findViewById(R.id.spin_name)).getText().toString();
                DA_ACTION[0] = ((SpinnerModel)adapter.data.get(arg2)).getPANE_TYPE();
                filename="";

                attach_img.setImageDrawable(null);
                add_attachment.setChecked(false);

                expHead[0] = cbohelp.getEXP_Head(exp_id);
                exhAmt.setText("");
                Boolean allreadyAdded = false;
                if (who.equals("0")){
                    if (cbohelp.get_ExpenseTypeAdded(expHead[0].getEXP_TYPE_STR()).size() >0
                            && expHead[0].getEXP_TYPE() != eExpense.None) {
                        allreadyAdded = true;

                    }
                }
                if (!allreadyAdded) {
                    if (exp_id.equals("3119")) {
                        exhAmt.setHint("K.M.");
                        textRemark.setText("K.M. Remark.");
                        ex_head_root_txt.setText("K.M.");
                    } else {
                        exhAmt.setHint("Amt.");
                        textRemark.setText("Exp Remark.");
                        ex_head_root_txt.setText("Amount");

                    }
                }else{
                    AppAlert.getInstance().Alert(context, "Alert!!!",
                            expHead[0].getEXP_TYPE().name() +" allready submitted in another Head",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    exphead.setSelection(0);
                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



        exhAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // if (expHead[0] != null) {
                    Double amt = s.toString().trim().isEmpty() ? 0D : Double.parseDouble(s.toString());
                    Double maxAmt = expHead[0].getMAX_AMT();
                    if (maxAmt == 0) {
                        maxAmt = amt;
                    }
                    if (keyPressed[0]) {
                        keyPressed[0] = false;
                        if (amt > maxAmt) {
                            Double finalMaxAmt = maxAmt;
                            AppAlert.getInstance().Alert(context, "Alert!!!",
                                    "You are not allowed to enter more then " + maxAmt,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            exhAmt.setText("" + finalMaxAmt);
                                        }
                                    });

                        }
                        keyPressed[0] = true;
                    }
               // }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog.setView(dialogLayout);
        dialog.setTitle("Add Other Expences");

        if(who.equals("0")) {
            adapter = new SpinAdapter(getApplicationContext(), R.layout.spin_row, cbohelp.get_ExpenseHeadNotAdded());
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            exphead.setAdapter(adapter);
            head.setVisibility(View.GONE);
        }else{
            head.setVisibility(View.VISIBLE);
            exphead.setVisibility(View.GONE);
            head.setText(hed);
            exp_id=hed_id;
            expHead[0] = cbohelp.getEXP_Head(exp_id);
            exp_hed=hed;
            exhAmt.setText(amt);
            rem_final.setText(rem);
            ext[0] = path.substring(path.lastIndexOf("/")+1 );
            File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + ext[0]);
            if(file2.exists() && !ext[0].equals("")){
                previewCapturedImage(file2.getPath());
            }
        }
        dialog.show();

        final String finalExt = ext[0];
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                my_Amt = exhAmt.getText().toString();
                my_rem = rem_final.getText().toString();


                //mycon.msgBox(dist_id3);
                if (exp_id.equals("")) {
                    customVariablesAndMethod.msgBox(context,"First Select the Expense Head...");
                } else if (my_Amt.trim().isEmpty()) {
                    customVariablesAndMethod.msgBox(context,"Please Enter the Expense Amt....");
                } else if (Double.valueOf(my_Amt)==0) {
                    customVariablesAndMethod.msgBox(context,"Expense Amt. can't be zero...");
                } else if (my_rem.trim().isEmpty()) {
                    customVariablesAndMethod.msgBox(context,"Please Enter the Remark....");
                } else if (DA_ACTION[0].equals("1") && actual_DA_layout.getVisibility() == View.VISIBLE
                        && !da_root.getText().toString().equals("0") && !da_root.getText().toString().isEmpty()) {
                    AppAlert.getInstance().Alert(context, "Already Applied for DA...",
                            "Please make DA amount Rs 0.", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }else if (expHead[0].getATTACHYN() != 0
                        && filename.equalsIgnoreCase("")
                        && path.equalsIgnoreCase("")) {
                    customVariablesAndMethod.msgBox(context,"Please add an attachment....");
                }else {


                    if (!filename.equals("")){
                        progress1.setMessage("Please Wait..\nuploading Image");
                        progress1.setCancelable(false);
                        progress1.show();
                        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                        new up_down_ftp().uploadFile(file2,ExpenseRoot.this);

                    }else{
                        filename= finalExt;
                        other_expense_commit();
                    }
                    /*if(!path.equals("")){
                        filename= finalExt;
                    }*/
                    dialog.dismiss();

                    /*if (filename.equals("")) {
                        other_expense_commit();
                    }*/

                }
            }
        });
    }


    private void other_expense_commit(){
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cbohelp.getCompanyCode());
        request.put("iDcrId", dcr_id);
        request.put("iExpHeadId", exp_id);
        request.put("iAmount", my_Amt);
        request.put("sRemark", my_rem);
        request.put("sFileName", filename);

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(ExpenseRoot.this,mHandler).customMethodForAllServices(request,"DCREXPCOMMITMOBILE_2",MESSAGE_INTERNET_SAVE_EXPENSE,tables);

        //End of call to service
//        if (!exp_id.equals("-1")) {
//            exhAmt.setText("");
//            rem_final.setText("");
//            //dialog.dismiss();
//        }else{
//            expense_commit();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void open_galary(){
        Intent gallery_Intent = new Intent(ExpenseRoot.this, GalleryUtil.class);
        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                    //return true;
                }
            }
            filename = PA_ID+"_"+dcr_id+"_"+exp_id+"_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
            output = new File(dir, filename);


//            fileTemp = ImageUtils.getOutputMediaFile();
            ContentValues values = new ContentValues(1);
            //values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put( MediaStore.Images.ImageColumns.DATA, output.getPath() );
            Uri fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            if (fileTemp != null) {
            // fileUri = Uri.fromFile(output);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_CAMERA);
//            } else {
//                Toast.makeText(this, getString(R.string.error_create_image_file), Toast.LENGTH_LONG).show();
//            }
        } else {
            Toast.makeText(this, getString(R.string.error_no_camera), Toast.LENGTH_LONG).show();
        }
    }

    private void capture_Image(){
        captureImage();
     /*   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                //return true;
            }
        }
         filename = PA_ID+"_"+dcr_id+"_"+exp_id+".jpg";
        output = new File(dir, filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        startActivityForResult(intent, REQUEST_CAMERA);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if(resultCode == Activity.RESULT_OK){
                picturePath = data.getStringExtra("picturePath");
                //perform Crop on the Image Selected from Gallery
                filename = PA_ID+"_"+dcr_id+"_"+exp_id+".jpg";
                Toast.makeText(this, picturePath, Toast.LENGTH_LONG).show();
                /*performCrop(picturePath);*/
                moveFile(picturePath);
            }
        }

        if (requestCode == RESULT_CROP ) {
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert selectedBitmap != null;
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                        //return true;
                    } else {
                        try {
                            File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                            file2.createNewFile();
                            FileOutputStream fo = new FileOutputStream(file2);
                            fo.write(bytes.toByteArray());
                            fo.close();
                            Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                            attach_img.setImageBitmap(myBitmap);

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }else {
                    try {
                        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);

                        file2.createNewFile();
                        FileOutputStream fo = new FileOutputStream(file2);
                        fo.write(bytes.toByteArray());
                        fo.close();
                        Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                        attach_img.setImageBitmap(myBitmap);

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        if (requestCode == REQUEST_CAMERA ) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                /*Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");*/

                File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO"+File.separator+ filename);
               /* FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file1);
                    Log.i("in save()", "after outputstream");
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
*/

                if (file1.exists()){
                    Log.d("INSIDE :"," VALUE ===== "+exp_id+"   "+filename);
                    /*performCrop(file1.getPath());*/
                    if (exp_id.equals("-1")){
                        attach_txt.setText(filename);
                    }else{
                        previewCapturedImage(file1.getPath());
                    }

                }

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "image capture cancelled ", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                capture_Image();
                //Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == this.GALLERY_ACTIVITY_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                open_galary();
                //Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void moveFile( String inputFileUri) {

        InputStream in = null;
        OutputStream out = null;
        try {
            File dir = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO");
            //create output directory if it doesn't exist
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputFileUri);
            out = new FileOutputStream(dir+File.separator + filename);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            if (exp_id.equals("-1")){
                attach_txt.setText(filename);
            }else{
                previewCapturedImage(dir+File.separator + filename);
            }


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    private void previewCapturedImage(String picUri) {
        try {
            // hide video preview

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(picUri,
                    options);
            attach_img.setVisibility(View.VISIBLE);
            attach_img.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }



    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
                    if ((null != msg.getData())) {

                        parser1(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_SAVE_EXPENSE:
                    if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
                    if ((null != msg.getData())) {

                        parser2(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_DCR_COMMITEXP:
                    if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
                    if ((null != msg.getData())) {

                        parser3(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_DCR_DELETEEXP:
                    if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
                    if ((null != msg.getData())) {

                        parser4(msg.getData());

                    }
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

    private void parser4(Bundle result) {
        if (result != null) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject object = jsonArray1.getJSONObject(0);
                String value2 = object.getString("DCR_ID");

                cbohelp.delete_Expense_withID(exp_id);
                data=cbohelp.get_Expense();
                sm = new Expenses_Adapter(context, data);
                da_root.setEnabled(cbohelp.get_DA_ACTION_exp_head().size()==0);

                mylist.setAdapter(sm);

                init_DA_type(DA_layout);

                customVariablesAndMethod.msgBox(context," Exp. Deleted Sucessfully");


            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();
    }

    private void parser3(Bundle result) {
        if (result != null) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject object = jsonArray1.getJSONObject(0);
                String value2 = object.getString("DCR_ID");

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "da_val",da_root.getText().toString().isEmpty()? "0" : da_root.getText().toString());

                chm_ok = getmydata().get(0);
                stk_ok = getmydata().get(1);
                exp_ok = getmydata().get(2);


                if (exp_ok.equals("")) {
                    cbohelp.insertfinalTest(chm_ok, stk_ok, "2");
                } else {
                    cbohelp.updatefinalTest(chm_ok, stk_ok, "2");
                }
                customVariablesAndMethod.msgBox(context,"Expense Saved Sucessfully...");
                finish();


            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();
    }

    private void parser2(Bundle result) {
        if (result != null) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject object = jsonArray1.getJSONObject(0);
                value = object.getString("DCRID");
                id= object.getString("ID");

                cbohelp.insert_Expense(exp_id,exp_hed,my_Amt,my_rem,filename,id,customVariablesAndMethod.currentTime(context));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "da_val",da_root.getText().toString().isEmpty()? "0" : da_root.getText().toString());
                if (!exp_id.equals("-1")){
                    data=cbohelp.get_Expense();
                    sm = new Expenses_Adapter(ExpenseRoot.this, data);
                    da_root.setEnabled(cbohelp.get_DA_ACTION_exp_head().size()==0);
                    mylist.setAdapter(sm);
                    init_DA_type(DA_layout);

                    customVariablesAndMethod.msgBox(context, " Exp. Added Sucessfully");
                    exp_id="";
                    exp_hed="";
                    my_Amt="";
                    my_rem ="";
                    filename="";
                    id="";
                }else{
                    expense_commit();
                }

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();
    }

    public void parser1(Bundle result) {
        if (result!=null ) {

            try {

                ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
                newlist.add(new SpinnerModel("--Select--", "","0"));

                cbohelp.delete_EXP_Head();

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                    newlist.add(new SpinnerModel(jsonObject1.getString("FIELD_NAME"), jsonObject1.getString("ID"),
                            jsonObject1.getString("DA_ACTION")));

                    cbohelp.Insert_EXP_Head(jsonObject1.getString("FIELD_NAME"), jsonObject1.getString("ID"),
                            jsonObject1.getString("MANDATORYYN_NEW"), jsonObject1.getString("DA_ACTION"),
                            jsonObject1.getString("EXP_TYPE"), jsonObject1.getString("ATTACHYN"),
                            jsonObject1.getString("MAX_AMT"), jsonObject1.getString("TAMST_VALIDATEYN"));

                }
                if (newlist.size() > 0) {
                    adapter = new SpinAdapter(getApplicationContext(), R.layout.spin_row, newlist);
                    //adapter3.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);


                } else {
                    customVariablesAndMethod.msgBox(context,"No ExpHead found...");

                }

                data = new ArrayList<Map<String, String>>();
                data.clear();
                /*String table1 = result.getString("Tables1");
                JSONArray jsonArray2 = new JSONArray(table1);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject object = jsonArray2.getJSONObject(i);
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("exp_head_id", object.getString("EXP_HEAD_ID"));
                    datanum.put("exp_head", object.getString("EXP_HEAD"));
                    datanum.put("amount", object.getString("AMOUNT"));
                    datanum.put("remark", object.getString("REMARK"));
                    datanum.put("FILE_NAME", object.getString("FILE_NAME"));
                    datanum.put("ID", object.getString("ID"));
                    data.add(datanum);

                }*/



                rootdata.clear();
                JSONObject object = null;
                String table2 = result.getString("Tables2");
                JSONArray jsonArray3 = new JSONArray(table2);
                for (int i = 0; i < jsonArray3.length(); i++) {
                    object = jsonArray3.getJSONObject(i);

                    rootdata.add((object.getString("DA_TYPE")));
                    rootdata.add((object.getString("FARE")));
                    rootdata.add((object.getString("ACTUALFAREYN")));
                    ROUTE_CLASS = object.getString("ROUTE_CLASS");
                    ACTUALDA_FAREYN = object.getString("ACTUALDA_FAREYN");
                    ACTUALFAREYN_MANDATORY  = object.getString("ACTUALFAREYN_MANDATORY");
                    ACTUALFARE_MAXAMT  = object.getDouble("ACTUALFARE_MAXAMT");
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ACTUALFAREYN",object.getString("ACTUALFAREYN"));



                    String MyDaType = object.getString("DA_TYPE_NEW");
                    String da_val = object.getString("DA_RATE_NEW");
                    /*Float rate = Float.parseFloat(one.getString("FARE_RATE"));
                    Float kms = Float.parseFloat(one.getString("KM"));

                    if (MyDaType.equals("L")) {
                        da_val = one.getString("DA_L_RATE");
                    } else if (MyDaType.equals("EX") || MyDaType.equals("EXS")) {
                        da_val = one.getString("DA_EX_RATE");
                    } else if (MyDaType.equals("NSD") || MyDaType.equals("NS")) {
                        da_val = one.getString("DA_NS_RATE");
                    }
                    String distance_val = "0";
                    if (MyDaType.equals("EX") || MyDaType.equals("NSD")) {
                        distance_val = "" + (kms * rate * 2);

                    } else {
                        distance_val = "" + (kms * rate);
                    }*/

                    datype_val = MyDaType;
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DA_TYPE",MyDaType);
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"da_val",da_val);
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"distance_val",object.getString("TA_AMT_NEW"));
                }


                DA_Types.clear();
                String table3 = result.getString("Tables3");
                JSONArray jsonArray4 = new JSONArray(table3);
                for (int i = 0; i < jsonArray4.length(); i++) {
                    JSONObject object1 = jsonArray4.getJSONObject(i);
                    mDA da = new mDA();
                    da.setCode(object1.getString("FIELD_CODE"));
                    da.setName(object1.getString("FIELD_NAME"));
                    da.setMultipleFactor(object1.getDouble("FARE_MULT_BY"));

                    if (object != null) {
                        da.setTA_Km(object.getDouble("KM_SINGLE_SIDE"));
                        da.setTA_Rate(object.getDouble("FARE_RATE"));
                        switch (da.getCode()) {
                            case "L":
                                da.setDAAmount(object.getDouble("DA_L_RATE"));
                                break;
                            case "EX":
                            case "EXS":
                                da.setDAAmount(object.getDouble("DA_EX_RATE"));
                                break;
                            case "NS":
                            case "NSD":
                                da.setDAAmount(object.getDouble("DA_NS_RATE"));
                                break;
                        }
                    }
                    if (datype_val.equalsIgnoreCase(da.getCode())){
                        btn_DaType.setText(da.getName());
                    }
                    DA_Types.add(da);
                }



                if (object != null){
                    if (object.getString("DA_TYPE_MANUALYN").equalsIgnoreCase("Y")){
                        manual_DA_layout.setVisibility(View.VISIBLE);
                    }else{
                        manual_DA_layout.setVisibility(View.GONE);
                    }
                }



                data=cbohelp.get_Expense();
                sm = new Expenses_Adapter(ExpenseRoot.this, data);
                mylist.setAdapter(sm);
                init_DA_type(DA_layout);


                routeStausTxt.setText(ROUTE_CLASS);
                if (ROUTE_CLASS.trim().isEmpty()){
                    routeStausTxt.setVisibility(View.GONE);
                }else{
                    routeStausTxt.setVisibility(View.VISIBLE);
                }

                da_root.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "da_val","0"));

                da_root.setEnabled(cbohelp.get_DA_ACTION_exp_head().size()==0);
                if (ACTUALDA_FAREYN.equalsIgnoreCase("y")){
                    actual_DA_layout.setVisibility(View.VISIBLE);
                }else{
                    actual_DA_layout.setVisibility(View.GONE);
                }

                if (rootdata.size() > 0) {
                    datype.setText(rootdata.get(0));
                    distanse.setText(rootdata.get(1));


                    if(rootdata.get(2).equalsIgnoreCase("y")){
                        distAmt.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"ACTUALFARE",""));
                        actual_fare_layout.setVisibility(View.VISIBLE);
                    }else{
                        actual_fare_layout.setVisibility(View.GONE);
                        distAmt.setText("");
                    }

                } else {
                    customVariablesAndMethod.msgBox(context,"No RootData found");
                }
                progress1.dismiss();
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }


    @Override
    public void started(Integer responseCode, String message, String description) {

    }

    @Override
    public void progess(Integer responseCode, Long FileSize, Float value, String description) {

    }

    @Override
    public void complete(Integer responseCode, String message, String description) {
        progress1.dismiss();
        other_expense_commit();
    }

    @Override
    public void aborted(Integer responseCode, String message, String description) {
        progress1.dismiss();
        customVariablesAndMethod.getAlert(context,message,description);
    }

    @Override
    public void failed(Integer responseCode, String message, String description) {
        progress1.dismiss();
        customVariablesAndMethod.getAlert(context,message,description);
    }
}
