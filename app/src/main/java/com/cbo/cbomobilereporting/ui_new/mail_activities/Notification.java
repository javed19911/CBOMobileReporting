package com.cbo.cbomobilereporting.ui_new.mail_activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;

import net.sourceforge.jtds.jdbc.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import utils.MyConnection;
import utils.adapterutils.Notification_Adapter;
import utils_new.Custom_Variables_And_Method;

public class Notification extends AppCompatActivity {
    ListView listView;
    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    Notification_Adapter notification_adapter;
    HashMap<String, ArrayList<String>> data;
    CBO_DB_Helper db_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        setSupportActionBar(toolbar);

        textView.setText("Notification");
        db_helper= new CBO_DB_Helper(this);
        data=db_helper.getNotificationMsg();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        if (data.get("Title").size()>0){
            notification_adapter= new Notification_Adapter(this,data);
            listView= (ListView) findViewById(R.id.not_list);
            listView.setAdapter(notification_adapter);
        }else {
            customVariablesAndMethod.msgBox(context,"No Notification to show");
            finish();
        }

        notification_adapter= new Notification_Adapter(this,data);
        listView= (ListView) findViewById(R.id.not_list);
        listView.setAdapter(notification_adapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {

                finish();

        }

        return super.onOptionsItemSelected(item);
    }
    public void deleteRow(int position){
       /* Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Toast.makeText(this,"delete clicked "+position+" last week date= "+cal.getTime(),Toast.LENGTH_LONG).show();*/
        HashMap<String,ArrayList<String>> keyValue = data;

        for (String key : keyValue.keySet()) {
            data.get(key).remove(position);
        }
        notification_adapter.notifyDataSetChanged();
    }

    public void updateNotification(int position) {
        notification_adapter.notifyDataSetChanged();
    }
}
