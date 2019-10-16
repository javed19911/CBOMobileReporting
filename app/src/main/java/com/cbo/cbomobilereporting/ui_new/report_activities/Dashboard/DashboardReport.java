package com.cbo.cbomobilereporting.ui_new.report_activities.Dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import utils.adapterutils.ExpandableDashboardAdapter;
import utils_new.Custom_Variables_And_Method;
import utils_new.UnderlineTextView;

public class DashboardReport extends AppCompatActivity {
    TextView month_txt;
    Custom_Variables_And_Method customVariablesAndMethod;

    Context context;
    CBO_DB_Helper cbohelp;
    ExpandableListView doctor;
    Button back;
    UnderlineTextView previous,next;
    int month=0,visible=0;

    ArrayList<Map<String, String>> data1 = new ArrayList<Map<String, String>>();
    ArrayList<Map<String, String>> data2 = new ArrayList<Map<String, String>>();
    HashMap<String, ArrayList<Map<String, String>>> summary_list=new HashMap<>();
    ExpandableDashboardAdapter listAdapter;
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET_Dash=1;
    List<Integer> visible_status=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.primarya_sec_sales);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        textView.setText("Dashboard Reports");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }
        context = DashboardReport.this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp = new CBO_DB_Helper(context);
        progress1 = new ProgressDialog(this);
        doctor = (ExpandableListView) findViewById(R.id.summary_list);
        previous= (UnderlineTextView) findViewById(R.id.previous);
        next= (UnderlineTextView) findViewById(R.id.next);
        month_txt= (TextView) findViewById(R.id.month);
        back= (Button) findViewById(R.id.back);


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
                month--;
                update_page();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month++;
                update_page();
            }
        });

        visible_status.add(0);
        visible_status.add(0);

        update_page();

        doctor.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
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



    }

    private void update_page() {


        switch (getDate("MM")){
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

        if (month>=0){
            month=0;
            next.setVisibility(View.INVISIBLE);
        }else{
            next.setVisibility(View.VISIBLE);
        }

        month_txt.setText(getDate("MMM-yyyy"));
        //new BackGroudTask().execute();

        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cbohelp.getCompanyCode());
        request.put("iPaId", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sMONTH",getDate("MM/dd/yyyy"));

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        tables.add(1);

        progress1.setMessage("Please Wait.. \n Fetching data");
        progress1.setCancelable(false);
        progress1.show();
        new CboServices(this, mHandler).customMethodForAllServices(request, "DashBoardFinal_1", MESSAGE_INTERNET_Dash, tables);

        //End of call to service

    }


    private String getDate(String date_format){
        SimpleDateFormat format = new SimpleDateFormat(date_format);//"yyyy.MM.dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        System.out.println(format.format(cal.getTime()));
        return format.format(cal.getTime());
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

    private void parser_mail(Bundle result) {
        if (result!=null ) {

            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                data1.clear();
                data2.clear();

                for (int j = 0; j < jsonArray1.length(); j++) {
                    Map<String, String> datanum1 = new HashMap<String, String>();
                    JSONObject jobj = jsonArray1.getJSONObject(j);

                    datanum1.put("REMARK", jobj.getString("REMARK"));
                    datanum1.put("AMOUNT", jobj.getString("AMOUNT"));
                    datanum1.put("AMOUNT_CUMM", jobj.getString("AMOUNT_CUMM"));
                    datanum1.put("URL", jobj.getString("URL"));
                    data1.add(datanum1);

                }
                String table1 = result.getString("Tables1");
                JSONArray jsonArray2 = new JSONArray(table1);
                for (int k = 0; k < jsonArray2.length(); k++) {
                    Map<String, String> datanum2 = new HashMap<String, String>();
                    JSONObject jobj = jsonArray2.getJSONObject(k);
                    datanum2.put("REMARK", jobj.getString("REMARK"));
                    datanum2.put("AMOUNT", jobj.getString("AMOUNT"));
                    datanum2.put("AMOUNT_CUMM", jobj.getString("AMOUNT_CUMM"));
                    datanum2.put("URL", jobj.getString("URL"));
                    data2.add(datanum2);

                }

                summary_list=new LinkedHashMap<>();
                summary_list.put("Marketing",data1);
                summary_list.put("Sales",data2);
                final List<String> header_title=new ArrayList<>();
                for(String main_menu:summary_list.keySet()){
                    header_title.add(main_menu);

                }

                listAdapter = new ExpandableDashboardAdapter(doctor,context, header_title, summary_list,getDate("MMM"));
                doctor.setAdapter(listAdapter);
                doctor.setGroupIndicator(null);

                for(int i=0; i < listAdapter.getGroupCount(); i++) {
                    if (visible_status.get(i)==1) {
                        doctor.expandGroup(i);
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

}
