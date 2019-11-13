package com.cbo.cbomobilereporting.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import services.MyAPIService;
import utils.adapterutils.ExpandableListAdapter;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;


public class TotalStockistRpt extends AppCompatActivity {
    ListView mylist;
    String mDate, newDate;
    String mPAID="";
    String DCR_ID="";
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
        textView.setText("Stockist");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }
        Button back;

        back = (Button) findViewById(R.id.bt_stk_back);
        mDate = TotalStockistRpt.this.getIntent().getExtras().getString("date");

        if (getIntent().getExtras().getString("DCR_ID") != null){
            DCR_ID = getIntent().getExtras().getString("DCR_ID");
        }

        if (getIntent().getExtras().getString("PAID") == null){
            mPAID = CBOReportView.lastPaId;
        }else{
            mPAID= getIntent().getExtras().getString("PAID");
        }

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
        summary_list.put("Stockist",doctor_list);

        header_title=new ArrayList<>();
        for(String main_menu:summary_list.keySet()){
            header_title.add(main_menu);
        }



        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPaId", mPAID);
        request.put("DCR_ID", DCR_ID);
        request.put("sDCR_DATE", mDate);

        new MyAPIService(context)
                .execute(new ResponseBuilder("STOCKIST_VIEW_1", request)
                        .setMultiTable(false)
                        .setDescription("Please Wait..")
                        .setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) throws JSONException {

                                data.clear();
                                ArrayList<String> nameList,timeList,sample_name,sample_qty,sample_pob,sample_rate,sample_noc,remark,gift_name,gift_qty,dr_class_list
                                        ,dr_potential_list,dr_area_list,dr_work_with_list,dr_crm_count_list, dr_camp_group_list;
                                ArrayList<String> visible_status=new ArrayList<>();
                                nameList=new ArrayList();
                                timeList=new ArrayList();
                                sample_name=new ArrayList();
                                sample_qty=new ArrayList();
                                sample_pob=new ArrayList();
                                sample_rate=new ArrayList();
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

                                String table0 = message.getString("Tables0");
                                JSONArray rows = new JSONArray(table0);

                                for (int i = 0; i < rows.length(); i++) {
                                    JSONObject c = rows.getJSONObject(i);
                                    nameList.add(c.getString("PA_NAME"));
                                    timeList.add(c.getString("IN_TIME"));
                                    sample_name.add(c.getString("PRODUCT"));
                                    sample_qty.add(c.getString("QTY"));
                                    sample_pob.add(c.getString("POB_QTY"));
                                    sample_rate.add(c.getString("RATE"));
                                    sample_noc.add("0");
                                    visible_status.add("1");
                                    remark.add(c.getString("REMARK"));

                                    gift_name.add(dr_gift_name);
                                    gift_qty.add(dr_gift_qty);

                                    dr_area_list.add("");
                                    dr_class_list.add("");
                                    dr_potential_list.add("");
                                    dr_crm_count_list.add("");
                                    dr_camp_group_list.add("");

                                    dr_work_with_list.add("");

                                }


                                doctor_list.clear();
                                doctor_list.put("name",nameList);
                                doctor_list.put("time",timeList);
                                doctor_list.put("sample_name",sample_name);
                                doctor_list.put("sample_qty",sample_qty);
                                doctor_list.put("sample_pob",sample_pob);
                                doctor_list.put("sample_rate",sample_rate);
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
                            }

                            @Override
                            public void onResponse(Bundle response) throws JSONException {

                            }

                            @Override
                            public void onError(String s, String s1) {
                                AppAlert.getInstance().getAlert(context,s, s1);

                            }


                        })
                );

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


