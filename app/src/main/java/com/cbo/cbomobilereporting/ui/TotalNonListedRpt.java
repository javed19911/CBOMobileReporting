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
import java.util.List;
import java.util.Map;

import async.NLC_Rpt_Task;
import async.Stk_Rpt_Task;
import services.TaskListener;
import utils.adapterutils.ExpandableListAdapter;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit.Udainiya on 3/15/2015.
 */
public class TotalNonListedRpt extends AppCompatActivity {
    ListView mylist;
    String mDate, newDate;
    //String mPAID="";
    SimpleAdapter sm = null;
    ResultSet rs;
   ArrayList<Map<String,String>> data =new ArrayList<Map<String, String>>();
    Custom_Variables_And_Method customVariablesAndMethod;
    int counter = 1;
    ProgressDialog progressDialog;
    Context context;

    ExpandableListAdapter listAdapter;
    ExpandableListView doctor;
    HashMap<String, HashMap<String, ArrayList<String>>> summary_list;
    HashMap<String, ArrayList<String>> doctor_list=new HashMap<>();
    ArrayList<String> header_title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.total_stockist_rpt);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
        textView.setText("Non-Listed Call");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }
        Button back;

        back = (Button) findViewById(R.id.bt_stk_back);
        mDate = TotalNonListedRpt.this.getIntent().getExtras().getString("date");
        // mPAID=TotalDrRpt.this.getIntent().getExtras().getString("mPAID");
        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        doctor = (ExpandableListView) findViewById(R.id.summary_list);


        summary_list=new LinkedHashMap<>();
        summary_list.put("Non-Listed Call",doctor_list);

        header_title=new ArrayList<>();
        for(String main_menu:summary_list.keySet()){
            header_title.add(main_menu);
        }


        TotalNonListedRpt.this.totalStkReport(TotalNonListedRpt.this);

    }


    public void totalStkReport(final Activity context) {

        final NLC_Rpt_Task stk_rpt_task = new NLC_Rpt_Task(context);
        stk_rpt_task.setListener(new TaskListener<String>() {
            @Override
            public void onStarted() {
                try {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Please Wait.........");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();


                } catch (Exception e) {

                }
            }

            @Override
            public void onFinished(String result) {

                if ((result == null) || (result.contains("[ERROR]"))) {
                    progressDialog.dismiss();
                    customVariablesAndMethod.msgBox(context,"Sorry No Result Found");

                } else {
                    try {
                        data.clear();
                        ArrayList<String> nameList,timeList,sample_name,sample_qty,sample_pob,sample_noc,remark,gift_name,gift_qty,dr_class_list,dr_potential_list,dr_area_list
                                ,dr_work_with_list,dr_crm_count_list, dr_camp_group_list;
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

                        dr_class_list=new ArrayList();
                        dr_potential_list=new ArrayList();
                        dr_area_list=new ArrayList();
                        dr_crm_count_list=new ArrayList<String>();
                        dr_camp_group_list=new ArrayList<String>();
                        dr_work_with_list=new ArrayList<String>();


                        String dr_gift_name="";
                        String dr_gift_qty="";

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray rows = jsonObject.getJSONArray("Tables0");

                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject c = rows.getJSONObject(i);
                            nameList.add(c.getString("DR_NAME")+" ("+c.getString("DOC_TYPE")+")");
                            timeList.add(c.getString("IN_TIME"));
                            sample_name.add(c.getString("QFL")+" ("+c.getString("SPL")+")");
                            sample_qty.add(c.getString("ADDRESS"));
                            sample_pob.add(c.getString("MOBILE_NO"));
                            sample_noc.add("0");
                            visible_status.add("1");
                            remark.add(c.getString("REMARK"));

                            dr_class_list.add(c.getString("CLASS"));
                            dr_potential_list.add(c.getString("POTENCY_AMT"));
                            dr_area_list.add(c.getString("NLC_AREA"));

                            gift_name.add(dr_gift_name);
                            gift_qty.add(dr_gift_qty);

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


                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }


                }


            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            stk_rpt_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mDate);

        } else {
            stk_rpt_task.execute(mDate);
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


