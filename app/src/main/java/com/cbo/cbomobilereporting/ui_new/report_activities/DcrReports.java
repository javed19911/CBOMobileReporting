package com.cbo.cbomobilereporting.ui_new.report_activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.CBOReportView;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DrCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import services.CboServices;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinAdapter_new;
import utils.adapterutils.SpinnerModel;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit.Udainiya on 2/15/2015.
 */
public class DcrReports extends AppCompatActivity{
    Button name,month,done,back;
    ArrayList<SpinnerModel>rptName=new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel>rptNameCopy=new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel>monthname=new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel>monthnameCopy=new ArrayList<SpinnerModel>();
    CBO_DB_Helper cboDbHelper;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    private AlertDialog myalertDialog=null;
    String userName="",userId="";
    String monthName="",monthId="";
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;



    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.cbo_reports);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
        textView.setText("DCR Reports");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }
        name=(Button)findViewById(R.id.rptt_name);
        month=(Button)findViewById(R.id.month_name);
        done=(Button)findViewById(R.id.bt_done_rpt);
        back=(Button) findViewById(R.id.bt_back_rpt_list);
        context=this;

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        progress1 = new ProgressDialog(this);
        cboDbHelper=new CBO_DB_Helper(DcrReports.this);

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cboDbHelper.getCompanyCode());
        request.put("sPaId", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sMonthType","");

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);
        tables.add(1);
        tables.add(2);

        progress1.setMessage("Please Wait..\n" +
                " Fetching data");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(this,mHandler).customMethodForAllServices(request,"TEAMMONTHDIVISION_MOBILE",MESSAGE_INTERNET,tables);

        //End of call to service



        ImageView spinImgName = (ImageView) findViewById(R.id.spinner_img_dcr_rpt_name);
        ImageView spinImgMonth = (ImageView) findViewById(R.id.spinner_img_dcr_rpt_month);
        spinImgName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickName();
            }
        });

        spinImgMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMonth();
            }
        });




        name.setText("---Select---");
        month.setText("---Select---");


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickName();            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onClickMonth();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("---Select---")){
                    customVariablesAndMethod.msgBox(context,"Select Name");
                }
                else if(month.getText().toString().equals("---Select---")){
                    customVariablesAndMethod.msgBox(context,"Select Month");
                }
                else{
                    Intent intent=new Intent(DcrReports.this,CBOReportView.class);
                    intent.putExtra("nameId",userId);
                    intent.putExtra("monthId",monthId);
                    startActivity(intent);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item != null){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_worktype(msg.getData());

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

    public void parser_worktype(Bundle result) {
        if (result!=null ) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    rptName.add(new SpinnerModel(c.getString("PA_NAME"),c.getString("PA_ID")));
                }

                rptNameCopy.addAll(rptName);

                if (rptName.size()==1){
                    userId=rptName.get(0).getId();
                    userName=rptName.get(0).getName();
                    name.setText(userName);
                    name.setPadding(1,0,5,0);
                }

                String table2 = result.getString("Tables2");
                JSONArray jsonArray2 = new JSONArray(table2);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);
                    monthname.add(new SpinnerModel(c.getString("MONTH_NAME"),c.getString("MONTH")));
                }

                monthnameCopy.addAll(monthname);
                String date= customVariablesAndMethod.currentDate().substring(0,2);
                for (int i=0;i<monthname.size();i++){
                    if (monthname.get(i).getId().substring(0,2).equals(date)){
                        monthId=monthname.get(i).getId();
                        monthName=monthname.get(i).getName();
                        month.setText(monthName);
                        break;
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

    private void onClickName(){


        AlertDialog.Builder myDialog = new AlertDialog.Builder(DcrReports.this);
        final EditText editText = new EditText(DcrReports.this);
        final ListView listview=new ListView(DcrReports.this);
        LinearLayout layout = new LinearLayout(DcrReports.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter=new SpinAdapter(DcrReports.this,R.layout.spin_row,rptNameCopy);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                userId=((TextView)view.findViewById(R.id.spin_id)).getText().toString();
                userName=((TextView)view.findViewById(R.id.spin_name)).getText().toString();
                name.setText(userName);
                name.setPadding(1,0,5,0);

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
               int textlength = editText.getText().length();
                rptName.clear();
                for (int i = 0; i < rptNameCopy.size(); i++) {
                    if (textlength <= rptNameCopy.get(i).getName().length()) {

                        if (rptNameCopy.get(i).getName().toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                            rptName.add(rptNameCopy.get(i));
                        }
                    }
                }
                try {
                    //arrayAdapter.notifyDataSetChanged();
                    listview.setAdapter(new SpinAdapter(DcrReports.this, R.layout.spin_row, rptName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        myalertDialog=myDialog.show();

    }

    private void onClickMonth(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(DcrReports.this);
        final EditText editText = new EditText(DcrReports.this);
        final ListView listview=new ListView(DcrReports.this);
        LinearLayout layout = new LinearLayout(DcrReports.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter=new SpinAdapter(DcrReports.this,R.layout.spin_row,monthnameCopy);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                monthId=((TextView)view.findViewById(R.id.spin_id)).getText().toString();
                monthName=((TextView)view.findViewById(R.id.spin_name)).getText().toString();
                month.setText(monthName);

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                int textlength = editText.getText().length();
                monthname.clear();
                for (int i = 0; i < monthnameCopy.size(); i++) {
                    if (textlength <= monthnameCopy.get(i).getName().length()) {

                        if (monthnameCopy.get(i).getName().toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                            monthname.add(monthnameCopy.get(i));
                        }
                    }
                }
                try {
                   // arrayAdapter.notifyDataSetChanged();
                    listview.setAdapter(new SpinAdapter(DcrReports.this, R.layout.spin_row, monthname));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        myalertDialog=myDialog.show();
    }
}
