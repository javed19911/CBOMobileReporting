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
import com.cbo.cbomobilereporting.ui.LayoutZoomer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import services.CboServices;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.Custom_Variables_And_Method;


public class Spo_Report extends AppCompatActivity {
    Button btShowReport, btBack, btFrom, btTo, btName;
    ImageView imgFrorm, imgTo;
    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    ProgressDialog pd;

    String uid, uname, mName;
    public static String mIdFrom, mIdTo;
    private AlertDialog myalertDialog = null;

    ArrayList<SpinnerModel> nameList = new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel> mothList = new ArrayList<SpinnerModel>();
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;
    CBO_DB_Helper cboDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.spo_report);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_hadder);
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

        btShowReport = (Button) findViewById(R.id.bt_show_spo_rpt_details);
        btBack = (Button) findViewById(R.id.bt_back_rpt_list);
        btName = (Button) findViewById(R.id.bt_spo_rptt_name);
        btFrom = (Button) findViewById(R.id.bt_spo_rptt_from);
        btTo = (Button) findViewById(R.id.bt_spo_month_to);
        imgFrorm = (ImageView) findViewById(R.id.bt_spo_rptt_from_img);
        imgTo = (ImageView) findViewById(R.id.bt_spo_rptt_to_img);
        pd = new ProgressDialog(context);


        btName.setText("---Select---");
        btFrom.setText("---Select---");
        btTo.setText("---Select---");


        //Spo_Report.this.backGroundData(Spo_Report.this);

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cboDbHelper.getCompanyCode());
        request.put("sPaId", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sMonthType","");

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

        btShowReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Custom_Variables_And_Method.extraFrom=btFrom.getText().toString();
                Custom_Variables_And_Method.extraTo =btTo.getText().toString();
                Intent spoDetails = new Intent(getApplicationContext(), LayoutZoomer.class);

                spoDetails.putExtra("uid", uid);
                spoDetails.putExtra("mIdFrom", mIdFrom);
                spoDetails.putExtra("mIdTo", mIdTo);
                startActivity(spoDetails);

            }
        });
        imgFrorm.setOnClickListener(new View.OnClickListener() {
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
        });


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
        });
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
                        btTo.setText(mName);
                        btTo.setPadding(1, 0, 5, 0);
                        btFrom.setText(mName);
                        btFrom.setPadding(1, 0, 5, 0);
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
