package com.cbo.cbomobilereporting.ui_new.report_activities;

import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;


import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.ServiceHandler;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.Custom_Variables_And_Method;

public class Logged_UnLogged extends AppCompatActivity {

    int hour, minute, mYear, mMonth, mDay;
    TextView ccname;
    Button show, back,btn_viewtime,btn_view;
    Spinner status, employee;
    LinearLayout loglayout, logtime, logemp;
    EditText time, mydate;
    static final int TIME_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID = 1;

    Custom_Variables_And_Method customVariablesAndMethod;
    SpinAdapter adapter;
    //CheckBox mCheck;
    int PA_ID;
    List<String> data1;
    String company_name = "", emp_id = "", mymonth = "", log_status = "";
    ArrayList<SpinnerModel> mylist = new ArrayList<SpinnerModel>();
    //private String[] arrMonth = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    ServiceHandler myServiceHandler;
    CBO_DB_Helper myCboDbHelper;
    Context context;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.logged_unlogged);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        final TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);

        textView.setText("Logged & Unlogged Report ");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }

        ccname = (TextView) findViewById(R.id.logged_ccname);
        show = (Button) findViewById(R.id.show);
        back = (Button) findViewById(R.id.logged_bk);
        time = (EditText) findViewById(R.id.time_upto);
        mydate = (EditText) findViewById(R.id.date_upto);
        loglayout = (LinearLayout) findViewById(R.id.log_layout);
        logtime = (LinearLayout) findViewById(R.id.logged_time);
        logemp = (LinearLayout) findViewById(R.id.logged_emp);
        status = (Spinner) findViewById(R.id.sts);
        employee = (Spinner) findViewById(R.id.emp);
        btn_viewtime = findViewById(R.id.btn_viewtime);
        btn_view = findViewById(R.id.btn_view);

        context=this;

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

        PA_ID = Custom_Variables_And_Method.PA_ID;
        myServiceHandler = new ServiceHandler(context);
        //mCheck = (CheckBox) findViewById(R.id.ck_logged);
        myCboDbHelper = new CBO_DB_Helper(context);
        final LinearLayout checkBoxLayout = (LinearLayout) findViewById(R.id.chekbox_layout);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

       /* ArrayList<SpinnerModel> data1 = new ArrayList<SpinnerModel>();

        data1.add(new SpinnerModel("--Select--", ""));
        data1.add(new SpinnerModel("Logged", ""));
        data1.add(new SpinnerModel("UnLogged", ""));*/


        ImageView spinImgName = (ImageView) findViewById(R.id.spinner_img_logunlog_emp);
        //ImageView spinImgStatus = (ImageView) findViewById(R.id.spinner_img_logun_status);


        spinImgName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employee.performClick();
            }
        });

       /* spinImgStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.performClick();

            }
        });*/


       /* adapter = new SpinAdapter(getApplicationContext(), R.layout.spin_row, data1);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        status.setAdapter(adapter);*/

        /*status.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View v,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                log_status = ((TextView) v.findViewById(R.id.spin_name)).getText().toString();
                if (log_status.equals("UnLogged")) {
                    logtime.setVisibility(View.GONE);
                    logemp.setVisibility(View.GONE);
                    checkBoxLayout.setVisibility(View.GONE);
                } else {
                    checkBoxLayout.setVisibility(textView.VISIBLE);
                    logtime.setVisibility(View.VISIBLE);
                    logemp.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });*/

        new GetEmployeeDetail().execute();


        //	getEmployeeDetail(PA_ID);
        employee.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View v,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                emp_id = ((TextView) v.findViewById(R.id.spin_id)).getText().toString();
                //Custom_Variables_And_Method.EMP_ID = emp_id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        mydate.setText(customVariablesAndMethod.currentDate());
        mydate.setEnabled(false);
        time.setEnabled(false);

        btn_view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                showDialog(DATE_DIALOG_ID);
                return true;
            }
        });
        btn_viewtime.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                showDialog(TIME_DIALOG_ID);
                return true;
            }
        });

        mydate.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                showDialog(DATE_DIALOG_ID);
                return true;
            }
        });
        time.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                showDialog(TIME_DIALOG_ID);
                return true;
            }
        });

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        show.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               /* if (log_status.equals("UnLogged")) {
                    unloggedReports();
                } else if (mCheck.isChecked()) {
                    Intent i = new Intent(getApplicationContext(), Comp_Logged_rpt.class);
                    startActivity(i);
                } else {
                    myReports();

                }*/

                Bundle bundle= new Bundle();
                Intent i = new Intent(getApplicationContext(), LoggedUnlogged_Dat.class);
                bundle.putString("title", "Logged & Unlogged Report ");
                bundle.putString("date", mydate.getText().toString());
                bundle.putString("time", time.getText().toString());
                bundle.putString("emp_id", emp_id);
                i.putExtras(bundle);
                startActivity(i);

            }
        });

    }




    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(
                        this, mTimeSetListener, hour, minute, true);
            case DATE_DIALOG_ID:
                return new DatePickerDialog(
                        this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear+1;
                    mDay = dayOfMonth;
                   // mymonth = getmonth();

                    String sdate = mMonth + "/" + LPad(mDay + "", "0", 2) + "/" + mYear;
                    mydate.setText(sdate);
                    Custom_Variables_And_Method.RPT_DATE = sdate;
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                    hour = hourOfDay;
                    minute = minuteOfHour;
                    String stime = LPad("" + hour, "0", 2) + "." + LPad("" + minute, "0", 2);
                    time.setText(stime);
                    Custom_Variables_And_Method.RPT_TIME = stime;
                }
            };

    private static String LPad(String schar, String spad, int len) {
        String sret = schar;
        for (int i = sret.length(); i < len; i++) {
            sret = spad + sret;
        }
        return new String(sret);
    }

/*
    public String getmonth() {
        String month = "";
        mymonth = arrMonth[mMonth];
        if (mymonth.equals("Jan")) {
            month = "01";
        }
        if (mymonth.equals("Feb")) {
            month = "02";
        }
        if (mymonth.equals("Mar")) {
            month = "03";
        }
        if (mymonth.equals("Apr")) {
            month = "04";
        }
        if (mymonth.equals("May")) {
            month = "05";
        }
        if (mymonth.equals("Jun")) {
            month = "06";
        }
        if (mymonth.equals("Jul")) {
            month = "07";
        }
        if (mymonth.equals("Aug")) {
            month = "08";
        }
        if (mymonth.equals("Sep")) {
            month = "09";
        }
        if (mymonth.equals("Oct")) {
            month = "10";
        }
        if (mymonth.equals("Nov")) {
            month = "11";
        }
        if (mymonth.equals("Dec")) {
            month = "12";
        }

        return month;
    }
*/


    class GetEmployeeDetail extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if ((s != null) && (!s.toLowerCase().contains("[error]"))) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("Tables0");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        mylist.add(new SpinnerModel(i==0 ? "All" :jsonObject1.getString("PA_NAME"), jsonObject1.getString("PA_ID")));

                    }
                    if(mylist.size()==2){
                        mylist.remove(0);
                    }
                } catch (JSONException json) {
                    pd.dismiss();
                    customVariablesAndMethod.msgBox(context,"Exception Found..");

                }
                adapter = new SpinAdapter(getApplicationContext(), R.layout.spin_row, mylist);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

                employee.setAdapter(adapter);
            } else {
                customVariablesAndMethod.msgBox(context,"Nothing Found..... ");
                pd.dismiss();
            }
            pd.dismiss();

        }

        @Override
        protected String doInBackground(String... params) {
            String responseTEAMALL;
            try{
                responseTEAMALL = myServiceHandler.TEAMALL(myCboDbHelper.getCompanyCode(), "" + PA_ID, "0");
            }catch (Exception e){
                return "ERROR apk "+e;
            }


            return responseTEAMALL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Logged_UnLogged.this);
            pd.setTitle("CBO");
            pd.setMessage("Processing...");
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.show();
        }
    }


   /* public void myReports() {
        if (log_status.equals("--Select--")) {
            customVariablesAndMethod.msgBox(context,"Select Status Please...");
        } else if (emp_id.equals("")) {
            customVariablesAndMethod.msgBox(context,"Select Employee Please...");
        } else if (mydate.getText().toString().equals("")) {
            customVariablesAndMethod.msgBox(context,"Set Date Please...");
        } else if (time.getText().toString().equals("")) {
            customVariablesAndMethod.msgBox(context,"Set Time Please...");
        } else {
            Intent i = new Intent(getApplicationContext(), Rpt_Detail.class);
            startActivity(i);
        }
    }

    public void unloggedReports() {
        if (mydate.getText().toString().equals("")) {
            customVariablesAndMethod.msgBox(context,"Set Date Please...");
        } else {
            Intent i = new Intent(getApplicationContext(), Rpt_Unlogged.class);
            startActivity(i);
        }
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }




 /*   private void onClickName() {

        Toast.makeText(getApplicationContext(), "This ", Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        textView.setText("Logged unlogged Report ");
    }

    private void onClickStatus() {

        Toast.makeText(getApplicationContext(), "This", Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);

        textView.setVisibility(View.GONE);
        textView.setText("Ohk");

    }
*/

}
