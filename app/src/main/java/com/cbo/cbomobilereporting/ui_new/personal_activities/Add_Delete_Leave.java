package com.cbo.cbomobilereporting.ui_new.personal_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.Leave_Request;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import services.ServiceHandler;
import utils.adapterutils.AddDelLeaveModel;
import utils.adapterutils.AddDeleteLeaveAdapter;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit Udainiya on 4/24/15.
 */
public class Add_Delete_Leave extends AppCompatActivity {
    Context context;
    Button newLeavebt, backbt;
    ProgressDialog progressDialog;
    Custom_Variables_And_Method customVariablesAndMethod;
    ServiceHandler serviceHandler;
    CBO_DB_Helper cboDbHelper;
    ListView listView;
    SimpleAdapter simpleAdapter;
    ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
    ArrayList<AddDelLeaveModel> list = new ArrayList<AddDelLeaveModel>();
    AddDelLeaveModel addDelLeaveModel;
    AddDeleteLeaveAdapter addDeleteLeaveAdapter;
    String leavePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(Add_Delete_Leave.this));
        setContentView(R.layout.add_delete_leave);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);

        textView.setText("Leave Request");


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }


        newLeavebt = (Button) findViewById(R.id.newLeave_add_delete);
        backbt = (Button) findViewById(R.id.back_add_delete);
        listView = (ListView) findViewById(R.id.add_delete_list);
        context = Add_Delete_Leave.this;
        cboDbHelper = new CBO_DB_Helper(getApplicationContext());
        serviceHandler = new ServiceHandler(context);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();


        newLeavebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (leavePermission != null) {
                    customVariablesAndMethod.getAlert(context,"Sorry",leavePermission);

                }else {
                    Intent startLeaveRequest = new Intent(Add_Delete_Leave.this, Leave_Request.class);
                    startActivity(startLeaveRequest);
                }
            }
        });
        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
                    /*Intent i = new Intent(Add_Delete_Leave.this, ViewPager_2016.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("Id", Custom_Variables_And_Method.CURRENTTAB);
                    startActivity(i);*/
                    finish();
                }else{
                    finish();
                }

            }
        });


        new AddDeleteTask().execute();


    }

    class AddDeleteTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String response;
            try{
            response = serviceHandler.get_PopulateleaveDetailByPA_ID(cboDbHelper.getCompanyCode(), "" + Custom_Variables_And_Method.PA_ID);
            }catch (Exception e){
                return "ERROR apk "+e;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if ((s == null) || (s.contains("ERROR"))) {
                progressDialog.dismiss();
                customVariablesAndMethod.msgBox(context,"Sorry No Result Found");


            } else {
                try {

                    list.clear();

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray rows = jsonObject.getJSONArray("Tables");
                    JSONObject jsonObject1 = rows.getJSONObject(0);
                    JSONArray tables0 = jsonObject1.getJSONArray("Tables0");
                    JSONObject jsonObject2 = rows.getJSONObject(1);
                    JSONArray tables1 =jsonObject2.getJSONArray("Tables1");
                    if (tables1.length()>0){
                        leavePermission = "Can't Add new Leave \n Pending Leave found for Approval.";
                    }

                    for (int i = 0; i < tables0.length(); i++) {
                        JSONObject c = tables0.getJSONObject(i);
                        addDelLeaveModel = new AddDelLeaveModel();

                        String leaveId = c.getString("ID");
                        addDelLeaveModel.setId(leaveId);
                        String docNo = c.getString("DOC_NO");
                        addDelLeaveModel.setDocNo(docNo);
                        String doc_date = c.getString("DOC_DATE");
                        addDelLeaveModel.setDocDate(doc_date);
                        String fdate = c.getString("FDATE");
                        addDelLeaveModel.setfDate(fdate);
                        String tdate = c.getString("TDATE");
                        addDelLeaveModel.settDate(tdate);
                        String purpose = c.getString("PURPOSE");
                        addDelLeaveModel.setPurpose(purpose);
                        String approval_ho = c.getString("APPROVAL_HO");
                        addDelLeaveModel.setApproval(approval_ho);
                        String no_day = c.getString("NO_DAY");
                        addDelLeaveModel.setDays(no_day);


                        list.add(addDelLeaveModel);
                    }
                    addDeleteLeaveAdapter = new AddDeleteLeaveAdapter(context, list);
                    listView.setAdapter(addDeleteLeaveAdapter);


                    progressDialog.dismiss();


                } catch (Exception e) {
                    progressDialog.dismiss();

                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Add_Delete_Leave.this);
            progressDialog.setTitle("CBO");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {
            if (!Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
                Intent i = new Intent(Add_Delete_Leave.this, ViewPager_2016.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("Id", Custom_Variables_And_Method.CURRENTTAB);
                startActivity(i);
                finish();
            }else{
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
            Intent i = new Intent(Add_Delete_Leave.this, ViewPager_2016.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("Id", Custom_Variables_And_Method.CURRENTTAB);
            startActivity(i);
            finish();
        }else{
            finish();
        }
        super.onBackPressed();
    }
}




