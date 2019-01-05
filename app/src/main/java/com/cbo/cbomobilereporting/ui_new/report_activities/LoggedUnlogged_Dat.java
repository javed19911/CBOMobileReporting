package com.cbo.cbomobilereporting.ui_new.report_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ParseException;
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
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import services.CboServices;
import utils.adapterutils.ExpandableListAdapter;
import utils_new.Custom_Variables_And_Method;
import utils_new.UnderlineTextView;

public class LoggedUnlogged_Dat extends AppCompatActivity{

    TextView month_txt;
    Custom_Variables_And_Method customVariablesAndMethod;

    Context context;
    CBO_DB_Helper cbohelp;
    ExpandableListView doctor;
    Button back;
    ProgressDialog commitDialog;
    UnderlineTextView previous,next;
    int day=0,visible=0;

    HashMap<String, HashMap<String, ArrayList<String>>> summary_list;
    HashMap<String, ArrayList<String>> doctor_list=new HashMap<>();
    ArrayList<String> header_title;
    //LoggedUnloggedAdapter loggedUnloggedAdapter;
    ExpandableListAdapter listAdapter;
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET_Dash=1;
    List<Integer> visible_status=new ArrayList<>();
    Bundle Msg=null;
    Calendar cal = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.primarya_sec_sales);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        textView.setText("");
    //  cal = Calendar.getInstance( );

        Msg = this.getIntent().getExtras();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }
        context = LoggedUnlogged_Dat.this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp = new CBO_DB_Helper(context);
        progress1 = new ProgressDialog(this);
        doctor = (ExpandableListView) findViewById(R.id.summary_list);
        previous= (UnderlineTextView) findViewById(R.id.previous);
        next= (UnderlineTextView) findViewById(R.id.next);
        month_txt= (TextView) findViewById(R.id.month);
        back= (Button) findViewById(R.id.back);
        commitDialog = new ProgressDialog(context);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        previous.setText("<<");
        next.setText(">>");

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day--;
                update_page();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day++;
                update_page();
            }
        });

        visible_status.add(0);
        visible_status.add(0);

        cal = Calendar.getInstance( );

        if(Msg!=null){
            textView.setText(Msg.getString("title"));
            SimpleDateFormat format0 = new SimpleDateFormat("MM/dd/yyyy");
            try {
                String DATE = Msg.getString("date");
                cal.setTime( format0.parse(DATE));

            }  catch (java.text.ParseException e) {

                cal = Calendar.getInstance( );
            }



        }
        update_page();

      /*  doctor.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                if (expandableListView.isGroupExpanded(groupPosition)){
                    visible_status.set(groupPosition,0);
                }else{
                    visible_status.set(groupPosition,1);
                }
                return false;
            }
        });
*/


    }

    private void update_page() {


        switch (getDate("dd-MMM-yyyy")){
            case "04":
                previous.setVisibility(View.INVISIBLE);
                next.setVisibility(View.VISIBLE);
                break;
            case "03":
                next.setVisibility(View.INVISIBLE);
                previous.setVisibility(View.VISIBLE);
                break;
            default:
                next.setVisibility(View.VISIBLE);
                previous.setVisibility(View.VISIBLE);
        }

        if (day>=0){
            day=0;
            next.setVisibility(View.INVISIBLE);
        }else{

            next.setVisibility(View.VISIBLE);
        }
        month_txt.setText(getDate("dd-MMM-yyyy"));


      // new BackGroudTask().execute();

        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cbohelp.getCompanyCode());
        request.put("iPa_Id", Msg.getString("emp_id"));
        request.put("sDATE",getDate("MM/dd/yyyy"));
        request.put("sTime", Msg.getString("time"));

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        tables.add(1);

        progress1.setMessage("Please Wait.. \n Fetching data");
        progress1.setCancelable(false);
        progress1.show();
        new CboServices(this, mHandler).customMethodForAllServices(request, "LOGGED_UNLOGGED", MESSAGE_INTERNET_Dash, tables);

        //End of call to service

    }
    private String getDate(String date_format){
        SimpleDateFormat format = new SimpleDateFormat(date_format);//"yyyy.MM.dd HH:mm");
        Calendar cal1 = (Calendar) cal.clone();
        cal1.add(Calendar.DAY_OF_MONTH, day);
        System.out.println(format.format(cal1.getTime()));
        return format.format(cal1.getTime());
    }



    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_Dash:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_mail(msg.getData());

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
     private void  parser_mail(Bundle result){

         if(result != null) {

             try {
                 String table0 = result.getString("Tables0");
                 JSONArray rows = new JSONArray(table0);

                 String table1 = result.getString("Tables1");
                 JSONArray rows1 = new JSONArray(table1);

                 summary_list = new LinkedHashMap<>();
                 summary_list.put("Logged", getDoctor_list(rows));
                 summary_list.put("Un-Logged", getDoctor_list(rows1));

                 final ArrayList<String> header_title = new ArrayList<>();
                 header_title.addAll(summary_list.keySet());

                 listAdapter = new ExpandableListAdapter(doctor, context, header_title, summary_list);

                 // setting list adapter
                 doctor.setAdapter(listAdapter);
                 doctor.setGroupIndicator(null);
                 for (int i = 0; i < listAdapter.getGroupCount(); i++)
                     doctor.expandGroup(i);
                 //doctor.expandGroup(1);

                 doctor.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                     @Override
                     public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {

                         summary_list.get(header_title.get(groupPosition)).get("visible_status").get(childPosition);
                         if (summary_list.get(header_title.get(groupPosition)).get("visible_status").get(childPosition).equals("1")) {
                             summary_list.get(header_title.get(groupPosition)).get("visible_status").set(childPosition, "0");
                         } else {
                             summary_list.get(header_title.get(groupPosition)).get("visible_status").set(childPosition, "1");
                         }
                         listAdapter.notifyDataSetChanged();
                         return false;
                     }
                 });

             } catch (Exception e) {
                 commitDialog.dismiss();
                 e.printStackTrace();
             }
         }

     }

     private HashMap<String, ArrayList<String>> getDoctor_list(JSONArray rows) throws JSONException {

         HashMap<String, ArrayList<String>> doctor_list=new HashMap<>();
         try{
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

                 nameList.add(c.getString("PA_NAME"));
                 timeList.add(c.getString("IN_TIME"));
                 sample_name.add("");
                 sample_qty.add("");
                 sample_pob.add("");
                 sample_noc.add("");
                 remark.add("     Designation : "+c.getString("DESIG_NAME")
                         + "\n     HeadQuarter : "+c.getString("HEAD_QTR"));
                 visible_status.add("0");
                 gift_name.add(dr_gift_name);
                 gift_qty.add(dr_gift_qty);

                 dr_area_list.add("");
                 dr_class_list.add("");
                 dr_potential_list.add("");
                 dr_crm_count_list.add("");
                 dr_camp_group_list.add("");

                 dr_work_with_list.add("");

             }


             commitDialog.dismiss();
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

         }catch (Exception e){
             throw e;
         }
        return  doctor_list;
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


}
