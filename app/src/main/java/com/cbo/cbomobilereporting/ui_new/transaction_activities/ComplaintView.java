package com.cbo.cbomobilereporting.ui_new.transaction_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
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
 * Created by Akshit Udainiya on 6/12/15.
 */
public class ComplaintView extends AppCompatActivity {
    ListView listView;
    Button buttonBack;
    SimpleAdapter simpleAdapter;
    ServiceHandler myServiceHandler1;
    CBO_DB_Helper myCboHelper;
    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    ArrayList<Map<String,String>> data = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_grid);
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView =(TextView) findViewById(R.id.hadder_text_1);

        textView.setText("Complaint View");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }



        listView =(ListView) findViewById(R.id.list_complaint);
        buttonBack = (Button) findViewById(R.id.back_complaintGrid);
        context = ComplaintView.this;
         myServiceHandler1 = new ServiceHandler(context);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        myCboHelper = new CBO_DB_Helper(context);

        new ComplaintGridBackGround().execute();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    class ComplaintGridBackGround extends AsyncTask<String,String,String>{
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle("CBO");
            pd.setMessage("Processing......");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseComplaintGrid;
            try {
                responseComplaintGrid = myServiceHandler1.getResponse_ComplaintGrid(myCboHelper.getCompanyCode(), "" + Custom_Variables_And_Method.PA_ID);
            }catch (Exception e){
                responseComplaintGrid="ERROR apk "+ e;
            }
            return responseComplaintGrid;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if ((s!=null) && (!s.toLowerCase().contains("error"))){
                try {
                    data.clear();
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("Tables0");
                  for (int i =0;i<jsonArray.length();i++){
                      JSONObject object =jsonArray.getJSONObject(i);
                      Map<String,String> dataMap = new HashMap<String, String>();
                      dataMap.put("cno",object.getString("DOC_NO"));
                      dataMap.put("cdate",object.getString("DOC_DATE"));
                      dataMap.put("retailerName",object.getString("RETAILER_NAME"));
                      dataMap.put("REMARK",object.getString("REMARK"));
                      dataMap.put("FILE_NAME",object.getString("FILE_NAME"));

                      data.add(dataMap);
                  }
                    pd.dismiss();
                    String[] from = {"cno","cdate","retailerName","REMARK"};
                    int[] to = {R.id.text_compbg, R.id.txt_date, R.id.txt_retailerName, R.id.txt_remark};
                    simpleAdapter = new SimpleAdapter(context,data, R.layout.complaint_view,from,to);
                    listView.setAdapter(simpleAdapter);

                }
                catch (JSONException json){

                    pd.dismiss();
                }
              }
            else {
                pd.dismiss();
                customVariablesAndMethod.msgBox(context,"Nothing Found...");
            }
            pd.dismiss();

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
