package com.cbo.cbomobilereporting.ui_new.report_activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import services.ServiceHandler;
import utils.adapterutils.DOB_DOA_doc_Adapter;
import utils.adapterutils.DOB_DOA_emp_Adapter;
import utils.adapterutils.MarketingSales_model;
import utils_new.Custom_Variables_And_Method;

public class DOB_DOA extends AppCompatActivity {

    ServiceHandler serviceHandler;
    String response1,G_from,G_to;
    ListView employe, doctor;
    TextView d_from, d_to;
    LinearLayout L_from,L_to,e_msg,d_msg,hed,main;
    int yy,mm,dd;
    Boolean flag=false;
    Button go,back;
    ImageView close;

    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cbohelp;
    ProgressDialog progressDialog;

    String message="0";
    MarketingSales_model marketingSales_model;

    ArrayList<Map<String, String>> data1 = new ArrayList<Map<String, String>>();
    ArrayList<Map<String, String>> data2 = new ArrayList<Map<String, String>>();
    DOB_DOA_emp_Adapter marketingAdapter;
    DOB_DOA_doc_Adapter salesAdapter;
    SimpleDateFormat month_date1;

    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getIntent().getStringExtra("msg")==null){
            message="0";
        }else{
            message=getIntent().getStringExtra("msg");
        }
        if(message.equals("1") || message.equals("2")){
            setTheme(R.style.Appdilogtheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dob__do);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        hader_text.setText("DOB/Anniversary");
        setSupportActionBar(toolbar);

        context = DOB_DOA.this;

        d_from = (TextView) findViewById(R.id.from);
        d_to = (TextView) findViewById(R.id.to);
        employe = (ListView) findViewById(R.id.listView1_marketing);
        doctor = (ListView) findViewById(R.id.listView2_sales);

        L_from = (LinearLayout) findViewById(R.id.L_from);
        L_to = (LinearLayout) findViewById(R.id.L_to);
        hed = (LinearLayout) findViewById(R.id.hed);
        main = (LinearLayout) findViewById(R.id.main);

        e_msg = (LinearLayout) findViewById(R.id.e_msg);
        d_msg = (LinearLayout) findViewById(R.id.d_msg);

        close= (ImageView) findViewById(R.id.close);
        go= (Button) findViewById(R.id.go);
        back= (Button) findViewById(R.id.back);

        serviceHandler = new ServiceHandler(context);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp = new CBO_DB_Helper(context);
        marketingSales_model = new MarketingSales_model();

        Calendar cal = Calendar.getInstance();
        month_date1 = new SimpleDateFormat("M/dd/yyyy");
        SimpleDateFormat month_date = new SimpleDateFormat("dd/M/yyyy");
        String dat = month_date.format(cal.getTime());
        yy=cal.get(Calendar.YEAR);
        mm=cal.get(Calendar.MONTH);
        dd=cal.get(Calendar.DAY_OF_MONTH);
        if(getIntent().getStringExtra("msg")==null){
            message="0";
        }else{
            message=getIntent().getStringExtra("msg");
        }

        if(Custom_Variables_And_Method.PA_ID==0){
            Custom_Variables_And_Method.PA_ID = Integer.parseInt(cbohelp.getPaid());
            Custom_Variables_And_Method.PA_NAME = cbohelp.getPaName();
            Custom_Variables_And_Method.HEAD_QTR = cbohelp.getHeadQtr();
            Custom_Variables_And_Method.DESIG = cbohelp.getDESIG();
            Custom_Variables_And_Method.pub_desig_id = cbohelp.getPUB_DESIG();
            Custom_Variables_And_Method.COMPANY_NAME = cbohelp.getCOMP_NAME();
            Custom_Variables_And_Method.WEB_URL = cbohelp.getWEB_URL();
            Custom_Variables_And_Method.location_required = cbohelp.getLocationDetail();
            Custom_Variables_And_Method.VISUAL_REQUIRED = cbohelp.getVisualDetail();
            Custom_Variables_And_Method.DCR_ID = cbohelp.getDCR_ID_FromLocal();
        }



        d_from.setText(dat);
        d_to.setText( dat);

        G_from=month_date1.format(cal.getTime());
        G_to=month_date1.format(cal.getTime());

        L_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    datePickerDialog = new DatePickerDialog(DOB_DOA.this, AlertDialog.THEME_HOLO_DARK,mDateSetListener,yy,mm,dd);
                }
                flag=false;
                datePickerDialog.show();
            }
        });
        L_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    datePickerDialog = new DatePickerDialog(DOB_DOA.this, AlertDialog.THEME_HOLO_DARK,mDateSetListener,yy,mm,dd);
                }
                flag=true;
                datePickerDialog.show();
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                message="0";
                try {
                    Date T_date = format.parse(G_to);
                    Date F_date = format.parse(G_from);

                    if(T_date.compareTo(F_date)>=0 ){
                        new BackGroudTask().execute();
                    }else{
                        customVariablesAndMethod.msgBox(context,"From_date can't be greater than To_date");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

        e_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile= "";
                for(int i=0;i<data1.size();i++){
                    if(i==0){
                        mobile= data1.get(0).get("MOBILE");
                    }else{
                        mobile= mobile+";"+data1.get(i).get("MOBILE");
                    }
                }
                new MyCustomMethod(context).sendSMS(mobile,"Any Employee");

            }
        });
        d_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile= "";
                for(int i=0;i<data2.size();i++){
                    if(i==0){
                        mobile= data2.get(0).get("MOBILE");
                    }else{
                        mobile= mobile+";"+data2.get(i).get("MOBILE");
                    }
                }
                new MyCustomMethod(context).sendSMS(mobile,"Any Doctor");

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if( message.equals("2")){
            data1.clear();
            data1=cbohelp.get_DOB_DOA("E");
            data2=cbohelp.get_DOB_DOA("D");

            if(data1.size()<1){
                Map<String, String> datanum1 = new HashMap<String, String>();
                datanum1.put("PA_NAME", "No Data Found");
                datanum1.put("DOB", "");
                datanum1.put("DOA", "");
                datanum1.put("MOBILE", "");
                data1.add(datanum1);
                e_msg.setVisibility(View.GONE);
            }else{
                e_msg.setVisibility(View.VISIBLE);
            }
            if(data2.size()<1){
                Map<String, String> datanum2 = new HashMap<String, String>();
                datanum2.put("DR_NAME", "No Data Found");
                datanum2.put("DOB", "");
                datanum2.put("DOA", "");
                datanum2.put("MOBILE", "");
                data2.add(datanum2);
                d_msg.setVisibility(View.GONE);
            }else{
                d_msg.setVisibility(View.VISIBLE);
            }
            marketingAdapter = new DOB_DOA_emp_Adapter(DOB_DOA.this, data1);
            employe.setAdapter(marketingAdapter);

            salesAdapter = new DOB_DOA_doc_Adapter(DOB_DOA.this, data2);
            doctor.setAdapter(salesAdapter);


        }else {
            new BackGroudTask().execute();
        }

    }
    @Override
    protected void onResume(){
        super.onResume();
        if(message.equals("1") || message.equals("2")){
            hed.setVisibility(View.GONE);
            //toolbar.setVisibility(View.GONE);
            close.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(25, -15, 0, 0);
            main.setLayoutParams(params);

        }else{
            hed.setVisibility(View.VISIBLE);
            close.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
            }
        }
    }



    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    yy = year;
                    mm = monthOfYear;
                    dd = dayOfMonth;
                    updateDisplay();
                }
            };

    // updates the date in the TextView
    private void updateDisplay() {

        StringBuilder s=new StringBuilder()
                // Month is 0 based so add 1

                .append(dd).append("/")
                .append(mm + 1).append("/")
                .append(yy).append(" ");
        StringBuilder s1=new StringBuilder()
                // Month is 0 based so add 1

                .append(mm + 1).append("/")
                .append(dd).append("/")
                .append(yy).append(" ");

        if(flag){

            G_to=s1.toString();
            d_to.setText(s);
        }else{
            d_to.setText(s);
            d_from.setText(s);
            G_from=s1.toString();
            G_to=s1.toString();
        }
    }





    class BackGroudTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            try {
                response1 = serviceHandler.get_DOB_DOA(cbohelp.getCompanyCode(), "" + Custom_Variables_And_Method.PA_ID, G_from, G_to);
            }catch (Exception e){
                return "[ERROR] apk "+e;
            }
            return response1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DOB_DOA.this);
            progressDialog.setTitle("CBO");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null || s.contains("[ERROR]")) {
                progressDialog.dismiss();
                customVariablesAndMethod.msgBox(context," data Error");
            } else {
                try {
                    data1.clear();
                    data2.clear();
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray rows = jsonObject.getJSONArray("Tables");
                    if(message.equals("1")){
                        cbohelp.delete_DOB_DOA();
                    }

                    for (int i = 0; i < 1; i++) {
                        JSONObject first = rows.getJSONObject(0);
                        JSONObject second = rows.getJSONObject(1);
                        JSONArray one = first.getJSONArray("Tables0");
                        JSONArray two = second.getJSONArray("Tables1");
                        for (int j = 0; j < one.length(); j++) {
                            JSONObject jobj = one.getJSONObject(j);
                            Map<String, String> datanum1 = new HashMap<String, String>();
                            datanum1.put("PA_NAME", jobj.getString("PA_NAME"));
                            datanum1.put("DOB", jobj.getString("DOB"));
                            datanum1.put("DOA", jobj.getString("DOA"));
                            datanum1.put("MOBILE", jobj.getString("MOBILE"));
                            data1.add(datanum1);

                            if(message.equals("1")){
                                cbohelp.insert_DOB_DOA(jobj.getString("PA_NAME"),jobj.getString("DOB"),jobj.getString("DOA"),jobj.getString("MOBILE"),"E");
                            }

                        }
                        for (int k = 0; k < two.length(); k++) {
                            Map<String, String> datanum2 = new HashMap<String, String>();
                            JSONObject jobj = two.getJSONObject(k);
                            datanum2.put("DR_NAME", jobj.getString("DR_NAME"));
                            datanum2.put("DOB", jobj.getString("DOB"));
                            datanum2.put("DOA", jobj.getString("DOA"));
                            datanum2.put("MOBILE", jobj.getString("MOBILE"));
                            data2.add(datanum2);
                            if(message.equals("1")){
                                cbohelp.insert_DOB_DOA(jobj.getString("DR_NAME"),jobj.getString("DOB"),jobj.getString("DOA"),jobj.getString("MOBILE"),"D");
                            }

                        }

                    }
                    if(data1.size()<1){
                        Map<String, String> datanum1 = new HashMap<String, String>();
                        datanum1.put("PA_NAME", "No Data Found");
                        datanum1.put("DOB", "");
                        datanum1.put("DOA", "");
                        datanum1.put("MOBILE", "");
                        data1.add(datanum1);
                        e_msg.setVisibility(View.GONE);
                    }else{
                        e_msg.setVisibility(View.VISIBLE);
                    }
                    if(data2.size()<1){
                        Map<String, String> datanum2 = new HashMap<String, String>();
                        datanum2.put("DR_NAME", "No Data Found");
                        datanum2.put("DOB", "");
                        datanum2.put("DOA", "");
                        datanum2.put("MOBILE", "");
                        data2.add(datanum2);
                        d_msg.setVisibility(View.GONE);
                    }else{
                        d_msg.setVisibility(View.VISIBLE);
                    }
                    marketingAdapter = new DOB_DOA_emp_Adapter(DOB_DOA.this, data1);
                    employe.setAdapter(marketingAdapter);

                    salesAdapter = new DOB_DOA_doc_Adapter(DOB_DOA.this, data2);
                    doctor.setAdapter(salesAdapter);
                    Log.d("Result",data1.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        if(message.equals("1")){
            finish();
        }else{
            Intent i = new Intent(DOB_DOA.this, ViewPager_2016.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("Id", Custom_Variables_And_Method.CURRENTTAB);
            startActivity(i);
            finish();
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }



}
