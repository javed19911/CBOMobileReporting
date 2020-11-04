package com.cbo.cbomobilereporting.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import services.CboServices;
import utils.adapterutils.Doctor_Visit_Adapter;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit on 3/4/2015.
 */
public class DrVisitedList extends AppCompatActivity {
    ListView listView;
    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cboDbHelper;
    Context context;
    ArrayList<Map<String,String>> data=new ArrayList<Map<String, String>>();
    Doctor_Visit_Adapter doctor_visit_adapter;
    String dr_id,monthId, monthName,userId;
    TextView  currentMonth,month ;
    public ProgressDialog progress1;
    private static final int MESSAGE_INTERNET_DOCTOR_VISIT = 1;

    public void onCreate(Bundle b){
        super.onCreate(b);
      setContentView(R.layout.dr_visited_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);

        textView.setText("Doctor Wise Visit");
        if (getSupportActionBar()!= null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }


        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        progress1 = new ProgressDialog(context);
        cboDbHelper = new CBO_DB_Helper(context);

        listView=(ListView)findViewById(R.id.rpt_list_dr);

        dr_id=getIntent().getExtras().getString("dr_id");
        monthId=getIntent().getExtras().getString("monthId");
        monthName = getIntent().getExtras().getString("monthName");
        currentMonth = (TextView) findViewById(R.id.tv_curren_month_name);
        month = (TextView) findViewById(R.id.tv_month_dr_visit);
        userId = getIntent().getExtras().getString("userId");


        if(currentMonth !=null) {
            currentMonth.setText(monthName);
        }

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cboDbHelper.getCompanyCode());
        request.put("iPaId", userId);
        request.put("sMONTH",monthId);
        request.put("iDRID", dr_id);


        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait.. \n" +
                "Checking your DCR Status");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(context,mHandler).customMethodForAllServices(request,"DOCTOR_VISIT_1",MESSAGE_INTERNET_DOCTOR_VISIT,tables);

        //End of call to service

    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_DOCTOR_VISIT:
                    //progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_doctor_visit(msg.getData());

                    }
                    break;

                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };

    private void parser_doctor_visit(Bundle result) {
        if (result!=null ) {

            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray0 = new JSONArray(table0);


                    for (int j = 0; j < jsonArray0.length(); j++) {

                        Map<String,String>datanum=new HashMap<String,String>();
                        JSONObject c = jsonArray0.getJSONObject(j);
                        datanum.put("DR_NAME", c.getString("DR_NAME"));
                        datanum.put("FREQ", c.getString("FREQ"));
                        datanum.put("CALL_DATE", c.getString("CALL_DATE"));
                        datanum.put("DR_CODE", c.getString("DR_CODE"));
                        datanum.put("REMARK", c.getString("REMARK"));

                        datanum.put("NO_CALL", c.getString("NO_CALL"));
                        datanum.put("MISSEDCALL", c.getString("MISSEDCALL"));
                        datanum.put("CLASS", c.getString("CLASS"));
                        datanum.put("AREA", c.getString("AREA"));
                        datanum.put("DR_SALE", c.getString("DR_SALE"));

                        datanum.put("LASTCALL", c.getString("LASTCALL"));
                        datanum.put("DR_CAMP", c.getString("DR_CAMP"));
                        datanum.put("HOSPITAL_NAME", c.getString("HOSPITAL_NAME"));
                        datanum.put("SPRODUCT", c.getString("SPRODUCT"));

                        data.add(datanum);
                    }

                doctor_visit_adapter=new Doctor_Visit_Adapter(DrVisitedList.this,data);
                listView.setAdapter(doctor_visit_adapter);
                progress1.dismiss();
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if (item != null){

           finish();
       }

        return super.onOptionsItemSelected(item);
    }
}