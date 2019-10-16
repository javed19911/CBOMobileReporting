package com.cbo.cbomobilereporting.ui_new.report_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.LayoutZoomer;
import com.cbo.cbomobilereporting.ui_new.Model.mSPO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import services.CboServices;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils.model.DropDownModel;
import utils_new.CustomDatePicker;
import utils_new.CustomDialog.Spinner_Dialog;
import utils_new.Custom_Variables_And_Method;


public class Spo_Report extends AppCompatActivity {
    Button btShowReport, btBack, btFrom, btTo, btName, fromdatebtn, todatebtn,Display_Unit,HQ_txt;
    ImageView imgFrorm, imgTo,img_followdate,img_nextfollowdate,bt_unit,HQ_btn;
    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    ProgressDialog pd;

    String uid, uname, mName;
    public  String mIdFrom, mIdTo;
    public  String mUnitID,mUnitName;
    public  String HqID,HqName;
    private AlertDialog myalertDialog = null;

    ArrayList<SpinnerModel> nameList = new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel> mothList = new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel>unitList= new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel>HqList= new ArrayList<SpinnerModel>();
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;
    CBO_DB_Helper cboDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.spo_report);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);


        hader_text.setText("SPO Report");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }
        context = Spo_Report.this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        progress1 = new ProgressDialog(this);
        cboDbHelper=new CBO_DB_Helper(this);


        fromdatebtn = (Button) findViewById(R.id.fromdatebtn);
        todatebtn = (Button) findViewById(R.id.todatebtn);
        img_followdate = (ImageView) findViewById(R.id.spinner_img_followdate);
        img_nextfollowdate = (ImageView) findViewById(R.id.spinner_img_nextfollowdate);

        btShowReport = (Button) findViewById(R.id.bt_show_spo_rpt_details);
        btBack = (Button) findViewById(R.id.bt_back_rpt_list);
        btName = (Button) findViewById(R.id.bt_spo_rptt_name);
        btFrom = (Button) findViewById(R.id.bt_spo_rptt_from);
        Display_Unit=findViewById (R.id.Display_Unit);
        bt_unit = findViewById(R.id.bt_unit);
        btTo = (Button) findViewById(R.id.bt_spo_month_to);
        imgFrorm = (ImageView) findViewById(R.id.bt_spo_rptt_from_img);
        imgTo = (ImageView) findViewById(R.id.bt_spo_rptt_to_img);

        HQ_txt = findViewById(R.id.Hq_txt);
        HQ_btn = findViewById(R.id.Hq_btn);

        pd = new ProgressDialog(context);


        btName.setText("---Select---");
        btFrom.setText("---Select---");
        btTo.setText("---Select---");



        fromdatebtn.setText(CustomDatePicker.currentDate( CustomDatePicker.ShowFirstDayOfMonthFormat));
        mIdFrom=(CustomDatePicker.currentDate( CustomDatePicker.CommitFirstDayOfMonthFormat));
        todatebtn.setText(CustomDatePicker.currentDate( CustomDatePicker.ShowFormat));
        mIdTo=(CustomDatePicker.currentDate( CustomDatePicker.CommitFormat));


        //Spo_Report.this.backGroundData(Spo_Report.this);

        //Start of call to service



        fromdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new CustomDatePicker(context,
                            CustomDatePicker.getDate(MyCustumApplication.getInstance().
                                    getDataFrom_FMCG_PREFRENCE("FY_FDATE",null), CustomDatePicker.CommitFormat),
                            CustomDatePicker.getDate(todatebtn.getText().toString(), CustomDatePicker.ShowFormat)
                    ).Show(CustomDatePicker.getDate(fromdatebtn.getText().toString(),  CustomDatePicker.ShowFormat)
                            , new CustomDatePicker.ICustomDatePicker() {
                                @Override
                                public void onDateSet(Date date) {
                                    fromdatebtn.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormat));
                                    mIdFrom=(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
                                }
                            });

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        todatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new CustomDatePicker(context,
                            CustomDatePicker.getDate(fromdatebtn.getText().toString(),  CustomDatePicker.ShowFormat),
                            CustomDatePicker.getDate(MyCustumApplication.getInstance().
                                    getDataFrom_FMCG_PREFRENCE("FY_TDATE",null), CustomDatePicker.CommitFormat))
                            .Show(CustomDatePicker.getDate(todatebtn.getText().toString(),  CustomDatePicker.ShowFormat)
                                    , new CustomDatePicker.ICustomDatePicker() {
                                        @Override
                                        public void onDateSet(Date date) {
                                            todatebtn.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormat));
                                            mIdTo=(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
                                        }
                                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        img_followdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromdatebtn.performClick();
            }
        });
        img_nextfollowdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todatebtn.performClick();
            }
        });

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cboDbHelper.getCompanyCode());
        request.put("sPaId", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sMonthType","");

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);
        tables.add(1);
        tables.add(2);
        tables.add (4);
        tables.add (5);

        progress1.setMessage("Please Wait..\n" +
                " Fetching data");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(this,mHandler).customMethodForAllServices(request,"TEAMMONTHDIVISION_MOBILE",MESSAGE_INTERNET,tables);

        //End of call to service

        btShowReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Custom_Variables_And_Method.extraFrom=btFrom.getText().toString();
                Custom_Variables_And_Method.extraTo =btTo.getText().toString();*/
                Intent spoDetails = new Intent(getApplicationContext(), LayoutZoomer.class);

                mSPO spo = new mSPO(mIdFrom,mIdTo,mUnitID);
                spo.setHqId(HqID);
                spoDetails.putExtra("mSPO", spo);
                startActivity(spoDetails);

            }
        });
        Display_Unit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                setUnit();
            }
        });

        bt_unit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                setUnit();
            }
        });

        HQ_txt.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                setHq();
            }
        });

        HQ_btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                HQ_txt.performClick();
            }
        });


     /*   imgFrorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtFrom();
            }
        });
        imgTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBtTo();
            }
        });*/


        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });



        btName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                final ListView listView = new ListView(context);
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(listView);
                alert.setView(linearLayout);
                SpinAdapter spinAdapter = new SpinAdapter(context, R.layout.spin_row, nameList);
                listView.setAdapter(spinAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        myalertDialog.dismiss();

                        uid = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                        uname = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();

                        btName.setText(uname);
                        btName.setPadding(1, 0, 5, 0);
                    }
                });

                myalertDialog = alert.show();

            }
        });





/*
        btFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBtFrom();
            }
        });
        btTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBtTo();

            }
        });*/
    }

    private void setUnit() {

        new Spinner_Dialog(context, unitList, new Spinner_Dialog.OnItemClickListener() {
            @Override
            public void ItemSelected(DropDownModel item) {
                mUnitID = item.getId();
                mUnitName = item.getName();
                Display_Unit.setText(mUnitName);
                Display_Unit.setPadding(1, 0, 5, 0);
            }
        }).show();
    }


    private void setHq() {

        new Spinner_Dialog(context, HqList, new Spinner_Dialog.OnItemClickListener() {
            @Override
            public void ItemSelected(DropDownModel item) {
                HqID = item.getId();
                HqName = item.getName();
                HQ_txt.setText(HqName);
                HQ_txt.setPadding(1, 0, 5, 0);
            }
        }).show();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                      //  parser_worktype(msg.getData());
                        getUnitType(msg.getData ());

                    }
                    break;
                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                    }
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };

    private void getUnitType(Bundle result) {

        if (result!=null ) {

            try {
                String table4 = result.getString("Tables4");
                JSONArray jsonArray2 = new JSONArray(table4);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);
                    unitList.add(new SpinnerModel(c.getString("PA_NAME"),c.getString("PA_ID")));
                }

                if (unitList.size() >0){
                    mUnitID=unitList.get(0).getId ();
                    mUnitName=unitList.get(0).getName();

                    Display_Unit.setText(mUnitName);
                    Display_Unit.setPadding(1, 0, 5, 0);
                    //  btTo.setPadding(1, 0, 5, 0);
                }


                String table5 = result.getString("Tables5");
                JSONArray jsonArray3 = new JSONArray(table5);
                for (int i = 0; i < jsonArray3.length(); i++) {
                    JSONObject c = jsonArray3.getJSONObject(i);
                    HqList.add(new SpinnerModel(c.getString("PA_NAME"),c.getString("PA_ID")));
                }

                if (HqList.size() > 0 ){
                    if (HqList.size() != 1) {
                        HqList.add(0, new SpinnerModel("ALL", "0"));
                    }
                    HqID = HqList.get(0).getId();
                    HqName = HqList.get(0).getName();
                    HQ_txt.setText(HqName);
                    HQ_txt.setPadding(1, 0, 5, 0);
                    //  btTo.setPadding(1, 0, 5, 0);
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

    public void parser_worktype(Bundle result) {
        if (result!=null ) {

            try {



                String table2 = result.getString("Tables2");
                JSONArray jsonArray2 = new JSONArray(table2);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);
                    mothList.add(new SpinnerModel(c.getString("MONTH_NAME"),c.getString("MONTH")));
                }
                String date= customVariablesAndMethod.currentDate().substring(0,2);
                for (int i=0;i<mothList.size();i++){
                    if (mothList.get(i).getId().substring(0,2).equals(date)){

                        mIdTo=mothList.get(i).getId();
                        mIdFrom=mothList.get(i).getId();

                        mName=mothList.get(i).getName();
                     //   btTo.setText(mName);
                      //  btTo.setPadding(1, 0, 5, 0);
                    //    btFrom.setText(mName);
                      //  btFrom.setPadding(1, 0, 5, 0);
                        break;
                    }
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


    private void setBtFrom() {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final ListView listView = new ListView(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(listView);
        alert.setView(linearLayout);
        SpinAdapter spinAdapter = new SpinAdapter(context, R.layout.spin_row, mothList);

        listView.setAdapter(spinAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                myalertDialog.dismiss();

                mIdFrom = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                mName = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
                btFrom.setText(mName);
                btFrom.setPadding(1, 0, 5, 0);


            }
        });
        myalertDialog = alert.show();


    }

    private void setBtTo() {


        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final ListView listView = new ListView(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(listView);
        alert.setView(linearLayout);
        SpinAdapter spinAdapter = new SpinAdapter(context, R.layout.spin_row, mothList);

        listView.setAdapter(spinAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                myalertDialog.dismiss();

                mIdTo = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                mName = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
                btTo.setText(mName);
                btTo.setPadding(1, 0, 5, 0);


            }
        });
        myalertDialog = alert.show();
    }
}
