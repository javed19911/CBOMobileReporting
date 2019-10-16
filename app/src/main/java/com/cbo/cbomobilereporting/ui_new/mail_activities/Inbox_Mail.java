package com.cbo.cbomobilereporting.ui_new.mail_activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.CboServices;
import utils.adapterutils.OutboxMail_Adapter;
import utils_new.Custom_Variables_And_Method;


public class Inbox_Mail extends AppCompatActivity {
    ListView mylist;
    Button back;
    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cbohelp;
    int PA_ID;
    CBO_DB_Helper myCboDbHelper;
    Context context;
    ArrayList<Map<String, String>> data = null;
    OutboxMail_Adapter outboxMail_adapter=null;

    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET_Mail=1,MESSAGE_INTERNET_SUBMIT_WORKING=2,MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL=3;

    String AllowBack="N";

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.inbox_mail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);

        textView.setText("Inbox");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        context = Inbox_Mail.this;
        progress1 = new ProgressDialog(this);
        cbohelp = new CBO_DB_Helper(context);
        data=new ArrayList<Map<String,String>>();
        mylist = (ListView) findViewById(R.id.mailinbox_list);
        back = (Button) findViewById(R.id.btn_inboxbk);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        PA_ID = Custom_Variables_And_Method.PA_ID;

        AllowBack = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"MAIL_STATUS","N");

        int[] colors = {0, 0xFFFF0000, 0};
        mylist.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
        mylist.setDividerHeight(2);

        myCboDbHelper = new CBO_DB_Helper(Inbox_Mail.this);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                FinshActivity();
            }
        });

        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cbohelp.getCompanyCode());
        request.put("iPaId", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sMailType","i");
        request.put("sFDate","");
        String max_id=cbohelp.getMaxMailId("i");
        request.put("iFid",max_id);

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait.. \n Fetching mails");
        progress1.setCancelable(false);

        if (max_id.equals("0") && AllowBack.equalsIgnoreCase("YD")) {
            progress1.show();
        }

        data=cbohelp.get_Mail("i","");
        outboxMail_adapter=new OutboxMail_Adapter(Inbox_Mail.this,data);
        mylist.setAdapter(outboxMail_adapter);

        new CboServices(this, mHandler).customMethodForAllServices(request, "MAILGRID_1", MESSAGE_INTERNET_Mail, tables);

        //End of call to service


    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_Mail:
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
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject object = jsonArray1.getJSONObject(i);
                    String files = object.getString("FILE_NAME"); //+"|^"+object.getString("FILE_NAME2")+"|^"+object.getString("FILE_NAME3")
                    if (files.isEmpty()){
                        files = object.getString("FILE_NAME2");
                    }else{
                        files = files + "|^" + object.getString("FILE_NAME2");
                    }
                    if (files.isEmpty()){
                        files = object.getString("FILE_NAME3");
                    }else{
                        files = files + "|^" + object.getString("FILE_NAME3");
                    }

                    cbohelp.insert_Mail(object.getInt("ID"),object.getString("FROM_PA_ID"),object.getString("FROM_PA_NAME"),object.getString("FWD_DATE"),object.getString("FWD_TIME"),
                            object.getString("IS_READ"),object.getString("MAIL_TYPE"),object.getString("CC"),object.getString("SUBJECT"),object.getString("REMARK")
                            ,object.getString("FILE_HEADING")+","+object.getString("FILE_HEADING2")+","+object.getString("FILE_HEADING3")
                            ,files);
                }
                data=cbohelp.get_Mail("i","");
                outboxMail_adapter=new OutboxMail_Adapter(Inbox_Mail.this,data);
                mylist.setAdapter(outboxMail_adapter);

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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            FinshActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FinshActivity();
    }


    public void FinshActivity(){

        if (CheckIFAllMailsRead()) {
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"MAIL_STATUS","N");
            finish();
        } else{
            customVariablesAndMethod.getAlert(context,"Unread mails Found !!!","Please Read All the Mails..");
        }
    }

    public boolean CheckIFAllMailsRead(){
        if(!AllowBack.equalsIgnoreCase("Y")){
            return true;
        }else if(cbohelp.getNoOfUnreadMail("i") == 0){
            return true;
        }

       return  false;
    }
}
