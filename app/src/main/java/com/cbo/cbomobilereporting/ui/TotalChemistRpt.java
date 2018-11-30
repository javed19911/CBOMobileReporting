package com.cbo.cbomobilereporting.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import async.Che_RptTask;
import services.TaskListener;
import utils.adapterutils.ExpandableListAdapter;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit.Udainiya on 3/15/2015.
 */
public class TotalChemistRpt extends AppCompatActivity {
    ListView mylist;
    String mDate,newDate;
    //String mPAID="";
    SimpleAdapter sm =null;
    ResultSet rs;
    ArrayList<Map<String,String>> data= new ArrayList<Map<String, String>>();
    Custom_Variables_And_Method customVariablesAndMethod;
    int counter=1;
    ProgressDialog progressDialog;
    Context context;
    Button back;
    SimpleAdapter simpleAdapter;

    ExpandableListAdapter listAdapter;
    ExpandableListView doctor;
    HashMap<String, HashMap<String, ArrayList<String>>> summary_list;
    HashMap<String, ArrayList<String>> doctor_list=new HashMap<>();
    ArrayList<String> header_title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_chemist_rpt);


        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

        String head="Chemist";
        //String Hide_status = Constants.getSIDE_STATUS(context);
        String Hide_status = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"fmcg_value");
        if (Hide_status != null) {
            if (Hide_status.equals("N") || Hide_status.equals("")) {
                head="Chemist";
            } else {
                head="Retailer";
            }

        }

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
        textView.setText(head);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }

        back =(Button) findViewById(R.id.bt_chem_back);


        mDate=TotalChemistRpt.this.getIntent().getExtras().getString("date");
        // mPAID=TotalDrRpt.this.getIntent().getExtras().getString("mPAID");


       back.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              finish();
          }
      });

        doctor = (ExpandableListView) findViewById(R.id.summary_list);


        summary_list=new LinkedHashMap<>();
        summary_list.put(head,doctor_list);

        header_title=new ArrayList<>();
        for(String main_menu:summary_list.keySet()){
            header_title.add(main_menu);
        }



     TotalChemistRpt.this.totalchemistReport(TotalChemistRpt.this);

    }


    public void totalchemistReport(final Activity context){

        final Che_RptTask che_rptTask = new Che_RptTask(context);
        che_rptTask.setListener(new TaskListener<String>() {
            @Override
            public void onStarted() {
                try {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Please Wait.........");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();


                }catch (Exception e){

                }
            }

            @Override
            public void onFinished(String result) {

                if((result == null) || (result.contains("[ERROR]"))){
                    progressDialog.dismiss();
                    customVariablesAndMethod.msgBox(context,"Sorry No Result Found");

                }
                else {
                    try {
                        data.clear();


                        ArrayList<String> nameList,timeList,sample_name,sample_qty,sample_pob,sample_noc,remark,gift_name,gift_qty,dr_class_list
                                ,dr_potential_list,dr_area_list,dr_work_with_list,dr_crm_count_list, dr_camp_group_list;
                        ArrayList<String> visible_status=new ArrayList<>();
                        nameList=new ArrayList();
                        timeList=new ArrayList();
                        sample_name=new ArrayList();
                        sample_qty=new ArrayList();
                        sample_pob=new ArrayList();
                        remark=new ArrayList();
                        sample_noc=new ArrayList();

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

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray row = jsonObject.getJSONArray("Tables0");
                        for(int i = 0; i<row.length();i++){

                            JSONObject c =row.getJSONObject(i);

                            nameList.add(c.getString("chem_NAME"));
                            timeList.add(c.getString("IN_TIME"));
                            sample_name.add(c.getString("PRODUCT"));
                            sample_qty.add(c.getString("QTY"));
                            sample_pob.add(c.getString("POB_QTY"));
                            sample_noc.add("0");
                            remark.add(c.getString("REMARK"));
                            visible_status.add("1");

                            gift_name.add(dr_gift_name);
                            gift_qty.add(dr_gift_qty);

                            dr_area_list.add("");
                            dr_class_list.add("");
                            dr_potential_list.add("");
                            dr_crm_count_list.add("");
                            dr_camp_group_list.add("");

                            dr_work_with_list.add("");

                        }

                        progressDialog.dismiss();

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
                        doctor_list.put("area", dr_area_list);
                        doctor_list.put("dr_crm", dr_crm_count_list);
                        doctor_list.put("dr_camp_group", dr_camp_group_list);
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


                    }catch (Exception e){
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }


                }


            }
        });


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ){

            che_rptTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mDate);

        }else {
            che_rptTask.execute(mDate);
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
