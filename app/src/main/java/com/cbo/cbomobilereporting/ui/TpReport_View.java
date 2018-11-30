package com.cbo.cbomobilereporting.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import async.TpReportTask;
import services.TaskListener;
import utils.MyConnection;
import utils.adapterutils.TP_Adapter;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit on 3/20/2015.
 */
public class TpReport_View extends AppCompatActivity {
    ListView listView;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
    TP_Adapter sm;
    String lastPaId, monthId;
    Button back;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tp_reports_list);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);


        hader_text.setText("T.P. Report View");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }


          listView = (ListView) findViewById(R.id.tp_report_listView);
        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        lastPaId = getIntent().getExtras().getString("nameId");
        monthId = getIntent().getExtras().getString("monthId");
        back = (Button) findViewById(R.id.bt_tp_rpt);


        TpReport_View.this.tpReportMethod(TpReport_View.this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void tpReportMethod(final Activity context) {

        final TpReportTask tpReportTask = new TpReportTask(context);
        tpReportTask.setListner(new TaskListener<String>() {
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

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray row = jsonObject.getJSONArray("Tables0");
                        for (int i = 0; i < row.length(); i++) {
                            Map<String, String> map = new HashMap<String, String>();

                            JSONObject c = row.getJSONObject(i);

                            map.put("date",c.getString("TP_DATE"));
                            map.put("work_with",c.getString("WORK_WITH"));
                            map.put("station",c.getString("STATION"));
                            map.put("station_remark",c.getString("STATION_REMARK"));
                            map.put("doctor",c.getString("DOCTOR"));
                            map.put("area",c.getString("AREA"));
                            map.put("class",c.getString("CLASS"));
                            map.put("potential",c.getString("POTENTIAL"));

                            data.add(map);




                        }
                        sm = new TP_Adapter(TpReport_View.this,data);
                        listView.setAdapter(sm);
                        progressDialog.dismiss();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            tpReportTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,lastPaId ,monthId);

        } else {
            tpReportTask.execute(lastPaId ,monthId);
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




