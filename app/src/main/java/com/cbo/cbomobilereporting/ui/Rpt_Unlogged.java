package com.cbo.cbomobilereporting.ui;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.cbo.cbomobilereporting.ui_new.report_activities.Logged_UnLogged;
import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.ServiceHandler;
import utils_new.Custom_Variables_And_Method;

public class Rpt_Unlogged extends AppCompatActivity {
    ListView mylist;
    Button back;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    SimpleAdapter sm;
    ResultSet rs;
    String mydate = "";
    int PA_ID;
    int cnt = 1;
    ServiceHandler myServiceHandler;
    CBO_DB_Helper myCboDbHelper;

    List<Map<String, String>> data = null;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.rpt_unlogged);
        FlurryAgent.logEvent("Rpt_Unlogged");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        textView.setText("UnLogged Report");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }

        mylist = (ListView) findViewById(R.id.rpt_list_unlogged);
        back = (Button) findViewById(R.id.rpt_list_bk_unlogged);

        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        PA_ID = Custom_Variables_And_Method.PA_ID;
        mydate = Custom_Variables_And_Method.RPT_DATE;
        myServiceHandler = new ServiceHandler(context);
        myCboDbHelper = new CBO_DB_Helper(Rpt_Unlogged.this);

        new MyExpenseList().execute();
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Rpt_Unlogged.this, Logged_UnLogged.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });


    }

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
        Intent i = new Intent(getApplicationContext(), ViewPager_2016.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    public void onRestart() {
        super.onRestart();

    }


    class MyExpenseList extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Rpt_Unlogged.this);
            pd.setTitle("CBO");
            pd.setMessage("Processing...");
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if ((s != null) && (!s.toLowerCase().contains("error"))) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("Tables0");

                    data = new ArrayList<Map<String, String>>();
                    data.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("cnt", "" + cnt + "-" + "\n");
                        datanum.put("name", object.getString("PA_NAME"));
                        datanum.put("hq", object.getString("HEAD_QTR"));
                        datanum.put("id", object.getString("PA_ID"));
                        cnt++;
                        data.add(datanum);

                    }
                    String[] from = {"cnt", "name", "hq", "id"};
                    int[] to = {R.id.cnt, R.id.name_unlogged, R.id.hq_unlogged, R.id.id_unlogged_name};
                    sm = new SimpleAdapter(Rpt_Unlogged.this, data, R.layout.rpt_unlogged_row, from, to);
                    mylist.setAdapter(sm);
                    pd.dismiss();
                } catch (JSONException json) {
                }
            } else {
                customVariablesAndMethod.msgBox(context,"Nothing Found....");
            }

        }

        @Override
        protected String doInBackground(String... params) {

            String responseDCRUNLOGGED_MOBILE;
            try{
                responseDCRUNLOGGED_MOBILE = myServiceHandler.getResponse_DCRUNLOGGED_MOBILE(myCboDbHelper.getCompanyCode(), "" + PA_ID, mydate);
            }catch (Exception e){
                return "ERROR apk "+e;
            }

            return responseDCRUNLOGGED_MOBILE;
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
