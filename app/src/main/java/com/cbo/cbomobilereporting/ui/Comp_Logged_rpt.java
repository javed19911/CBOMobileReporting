package com.cbo.cbomobilereporting.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import services.ServiceHandler;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit on 3/14/2016.
 */
public class Comp_Logged_rpt extends AppCompatActivity{

    ListView mListView;
    SimpleAdapter mSimpleAdapter;
    ServiceHandler mServiceHandler;
    Context context;
    CBO_DB_Helper cboDbHelper;
    Custom_Variables_And_Method customVariablesAndMethod;
   ArrayList<Map<String,String>> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    setContentView(R.layout.comp_logged_rpt);


        mListView = (ListView) findViewById(R.id.comp_logged_lsit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);
        context = Comp_Logged_rpt.this;
        mServiceHandler = new ServiceHandler(context);

        cboDbHelper = new CBO_DB_Helper(context);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

        list = new ArrayList<Map<String,String>>();

        if (getSupportActionBar() != null){
            textView.setText("Complete Logged Report");
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        new LoggedRpt_Task().execute();

    }



    private class LoggedRpt_Task extends AsyncTask<String,String,String>{

        ProgressDialog progressDialog;






        @Override
        protected String doInBackground(String... params) {
            String result;
            try {
                result = mServiceHandler.getResponse_DCRLOGGED_MOBILE(cboDbHelper.getCompanyCode(),""+ Custom_Variables_And_Method.EMP_ID,Custom_Variables_And_Method.RPT_DATE);
            }catch (Exception e){
                return "ERROR apk "+e;
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Processing....");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if ((s != null) && (!s.toLowerCase().contains("error"))){


                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("Tables0");

                    list.clear();
                    int j=1;
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Map<String,String> mapData = new HashMap<String,String>();

                        mapData.put("sno",""+j);
                        mapData.put("Emp_Name",jsonObject1.getString("PA_NAME"));
                        mapData.put("WORKING_TYPE",jsonObject1.getString("WORKING_TYP"));
                        mapData.put("joining_area",jsonObject1.getString("MOBILE_AREA"));
                        mapData.put("join_time",jsonObject1.getString("IN_TIME"));
                        mapData.put("route",jsonObject1.getString("ROUTE"));
                        mapData.put("WORK_WITH",jsonObject1.getString("WORK_WITH"));
                        mapData.put("Head_Qtr",jsonObject1.getString("HEAD_QTR"));

                        mapData.put("TP_PLANE",jsonObject1.getString("TP_PLANE"));
                        mapData.put("FIRST_CALL",jsonObject1.getString("FIRST_CALL"));
                        mapData.put("DCR_DATE",jsonObject1.getString("DCR_DATE"));
                        mapData.put("TOTAL_CALL",jsonObject1.getString("TOTAL_CALL"));
                        list.add(mapData);
                        j++;
                    }

                    String[] from ={"sno","Emp_Name","WORKING_TYPE","joining_area","join_time","route","WORK_WITH","Head_Qtr","TP_PLANE",
                    "FIRST_CALL","DCR_DATE","TOTAL_CALL"};
                    int[] to = {R.id.sno_logged,R.id.emp_name_logged,R.id.working_type,R.id.joining_area_logged,R.id.jointime_logged
                            ,R.id.route_logged,R.id.workwith_logged,R.id.head_qtr_logged,
                             R.id.tp_planed
                            ,R.id.first_all_time,R.id.dcr_time_date,R.id.calls,


                    };
                    mSimpleAdapter = new SimpleAdapter(context,list,R.layout.comp_logged_rpt_view,from,to);
                    mListView.setAdapter(mSimpleAdapter);
                }catch (Exception json){

                    customVariablesAndMethod.msgBox(context,"exption .."+""+json);

                }
            }else {

                customVariablesAndMethod.msgBox(context,"Nothing Found...");

            }
            progressDialog.dismiss();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item !=null){

            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
