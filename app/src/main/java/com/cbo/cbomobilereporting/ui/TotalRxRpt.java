package com.cbo.cbomobilereporting.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import services.CboServices;
import utils.adapterutils.ExpandableListAdapter;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit.Udainiya on 3/15/2015.
 */
public class TotalRxRpt extends AppCompatActivity {
    private static final int MESSAGE_INTERNET =1 ;
    ListView mylist;
    String mDate,Title;
    //String mPAID="";
    ResultSet rs;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    Button back;
    ExpandableListAdapter listAdapter;
    ExpandableListView doctor;
    HashMap<String, HashMap<String, ArrayList<String>>> summary_list;
    HashMap<String, ArrayList<String>> doctor_list=new HashMap<>();
    ArrayList<String> header_title;
    CBO_DB_Helper cboDbHelper;
    public ProgressDialog progress1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.total_dr);


        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
        textView.setText("Doctor");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }

        back =(Button) findViewById(R.id.bt_dr_back);


        mDate=TotalRxRpt.this.getIntent().getExtras().getString("date");
        Title=TotalRxRpt.this.getIntent().getExtras().getString("Title");
        // mPAID=TotalDrRpt.this.getIntent().getExtras().getString("mPAID");
        textView.setText(Title);
        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cboDbHelper=new CBO_DB_Helper(context);
        progress1 = new ProgressDialog(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        doctor = (ExpandableListView) findViewById(R.id.summary_list);


        summary_list=new LinkedHashMap<>();
        summary_list.put(Title,doctor_list);

        header_title=new ArrayList<>();
        for(String main_menu:summary_list.keySet()){
            header_title.add(main_menu);
        }


        //TotalRxRpt.this.totalDoctorReport(TotalRxRpt.this);

        //Start of call to service
        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cboDbHelper.getCompanyCode());
        request.put("iPaId", "" + CBOReportView.lastPaId);
        request.put("sDCR_DATE", mDate);
        //request.put("sDCR_DATE", "07/26/2017");

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(TotalRxRpt.this,mHandler).customMethodForAllServices(request,"GETDCRRX_VIEW",MESSAGE_INTERNET,tables);

        //End of call to service

        //TotalDrRpt.this.rptList(context);
       /* */

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

    private void parser1(Bundle result) {
        if (result != null) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray rows = new JSONArray(table0);

                ArrayList<String> nameList,timeList,sample_name,sample_qty,sample_pob,sample_noc,remark,gift_name,gift_qty,dr_class_list
                        ,dr_potential_list,dr_area_list,dr_work_with_list,dr_crm_count_list, dr_camp_group_list;
                ArrayList<String> visible_status=new ArrayList<>();
                nameList=new ArrayList();
                timeList=new ArrayList();
                sample_name=new ArrayList();
                sample_qty=new ArrayList();
                sample_pob=new ArrayList();
                sample_noc=new ArrayList();
                remark=new ArrayList();

                gift_name=new ArrayList();
                gift_qty=new ArrayList();

                dr_class_list=new ArrayList<String>();
                dr_potential_list=new ArrayList<String>();
                dr_area_list=new ArrayList<String>();
                dr_crm_count_list=new ArrayList<String>();
                dr_camp_group_list=new ArrayList<String>();
                dr_work_with_list=new ArrayList<String>();

                String dr_gift_name="";
                String dr_gift_qty="";


                for (int i = 0; i<rows.length();i++){
                    JSONObject c = rows.getJSONObject(i);

                    nameList.add(c.getString("DR_NAME"));
                    timeList.add(c.getString("IN_TIME"));
                    sample_name.add("");
                    sample_qty.add("");
                    sample_pob.add("");
                    sample_noc.add("0");
                    remark.add(c.getString("REMARK"));
                    visible_status.add("1");
                    if (c.getString("REMARK").equals("")) {
                        gift_name.add(c.getString("QTY_CAPTION") + "," + c.getString("AMOUN_CAPTION"));
                        gift_qty.add(c.getString("QTY") + "," + c.getString("AMOUNT"));
                    }else{
                        gift_name.add(dr_gift_name);
                        gift_qty.add(dr_gift_qty);
                    }

                    dr_area_list.add("");
                    dr_class_list.add("");
                    dr_potential_list.add("");
                    dr_crm_count_list.add("");
                    dr_camp_group_list.add("");

                    dr_work_with_list.add("");

                }


                //commitDialog.dismiss();
                doctor_list.clear();
                doctor_list.put("name",nameList);
                doctor_list.put("time",timeList);
                doctor_list.put("sample_name",sample_name);
                doctor_list.put("sample_qty",sample_qty);
                doctor_list.put("sample_pob",sample_pob);
                doctor_list.put("sample_noc",sample_noc);
                doctor_list.put("visible_status",visible_status);
                doctor_list.put("remark",remark);

                doctor_list.put("gift_name",gift_name);
                doctor_list.put("gift_qty",gift_qty);

                doctor_list.put("class", dr_class_list);
                doctor_list.put("potential", dr_potential_list);
                doctor_list.put("dr_crm", dr_crm_count_list);
                doctor_list.put("dr_camp_group", dr_camp_group_list);

                doctor_list.put("area", dr_area_list);
                doctor_list.put("workwith", dr_work_with_list);

                listAdapter = new ExpandableListAdapter(doctor,context, header_title, summary_list);

                // setting list adapter
                doctor.setAdapter(listAdapter);
                doctor.setGroupIndicator(null);
                for(int i=0; i < listAdapter.getGroupCount(); i++)
                    doctor.expandGroup(i);
                //doctor.expandGroup(1);

                doctor.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {

                        summary_list.get(header_title.get(groupPosition)).get("visible_status").get(childPosition);
                        if (summary_list.get(header_title.get(groupPosition)).get("visible_status").get(childPosition).equals("1")){
                            summary_list.get(header_title.get(groupPosition)).get("visible_status").set(childPosition,"0");
                        }else {
                            summary_list.get(header_title.get(groupPosition)).get("visible_status").set(childPosition,"1");
                        }
                        listAdapter.notifyDataSetChanged();
                        return false;
                    }
                });


            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

      if (item != null ){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
