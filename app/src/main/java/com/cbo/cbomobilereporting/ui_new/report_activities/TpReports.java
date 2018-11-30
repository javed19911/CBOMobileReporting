package com.cbo.cbomobilereporting.ui_new.report_activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.TpReport_View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import services.CboServices;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit on 3/19/2015.
 */
public class TpReports extends AppCompatActivity {


    Button name, month, done, back;
    ArrayList<SpinnerModel> rptName = new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel> monthname = new ArrayList<SpinnerModel>();
    CBO_DB_Helper cboDbHelper;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    private AlertDialog myalertDialog = null;
    String userName = "", userId = "";
    String monthName = "", monthId = "";

    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;



    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.tp_repots);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);


        hader_text.setText("T.P. Report");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        name = (Button) findViewById(R.id.rptt_name_tp);
        month = (Button) findViewById(R.id.month_name_tp);
        done = (Button) findViewById(R.id.bt_done_rpt_tp);
        back = (Button) findViewById(R.id.bt_back_rpt_list_tp);
        context=this;
        progress1 = new ProgressDialog(this);
        cboDbHelper = new CBO_DB_Helper(TpReports.this);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();


        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cboDbHelper.getCompanyCode());
        request.put("sPaId", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sMonthType","tp");

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);
        tables.add(1);
        tables.add(2);

        progress1.setMessage("Please Wait..\n" +
                " Fetching data");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(this,mHandler).customMethodForAllServices(request,"TEAMMONTHDIVISION_MOBILE",MESSAGE_INTERNET,tables);

        //End of call to service

        name.setText("---Select---");
        month.setText("---Select---");

        ImageView spinImgName = (ImageView) findViewById(R.id.spinner_img_tp_name);
        ImageView spinImgMonth = (ImageView) findViewById(R.id.spinner_img_tp_month);


        spinImgName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameOnclick();
            }
        });

        spinImgMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthOnclick();
            }
        });


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameOnclick();
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthOnclick();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("---Select---")) {
                    customVariablesAndMethod.msgBox(context,"Select Name");
                } else if (month.getText().toString().equals("---Select---")) {
                    customVariablesAndMethod.msgBox(context,"Select Month");
                } else {
                    Intent intent = new Intent(getApplicationContext(), TpReport_View.class);
                    intent.putExtra("nameId", userId);
                    intent.putExtra("monthId", monthId);
                    startActivity(intent);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_worktype(msg.getData());

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

    public void parser_worktype(Bundle result) {
        if (result!=null ) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    rptName.add(new SpinnerModel(c.getString("PA_NAME"),c.getString("PA_ID")));
                }
                if (rptName.size()==1){
                    userId=rptName.get(0).getId();
                    userName=rptName.get(0).getName();
                    name.setText(userName);
                    name.setPadding(1,0,5,0);
                }

                String table2 = result.getString("Tables2");
                JSONArray jsonArray2 = new JSONArray(table2);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);
                    monthname.add(new SpinnerModel(c.getString("MONTH_NAME"),c.getString("MONTH")));
                }
                String date= customVariablesAndMethod.currentDate().substring(0,2);
                for (int i=0;i<monthname.size();i++){
                    if (monthname.get(i).getId().substring(0,2).equals(date)){
                        monthId=monthname.get(i).getId();
                        monthName=monthname.get(i).getName();
                        month.setText(monthName);
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


    private void nameOnclick() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(TpReports.this);
        final ListView listview = new ListView(TpReports.this);
        LinearLayout layout = new LinearLayout(TpReports.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter = new SpinAdapter(TpReports.this, R.layout.spin_row, rptName);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                userId = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                userName = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
                name.setText(userName);
                name.setPadding(1, 0, 5, 0);

            }
        });
        myalertDialog = myDialog.show();
    }

    private void monthOnclick() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(TpReports.this);
        final ListView listview = new ListView(TpReports.this);
        LinearLayout layout = new LinearLayout(TpReports.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter = new SpinAdapter(TpReports.this, R.layout.spin_row, monthname);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                monthId = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                monthName = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
                month.setText(monthName);

            }
        });
        myalertDialog = myDialog.show();
    }
}
