package com.cbo.cbomobilereporting.ui_new.dcr_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.area.Dcr_Open_New;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.DCR_Root_new;
import com.uenics.javed.CBOLibrary.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import services.ServiceHandler;
import utils.networkUtil.NetworkUtil;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.Service_Call_From_Multiple_Classes;


public class GetDCR extends AppCompatActivity {
    TextView dcr_date, area, work_with, myTitleText, route;
    Button back, Dcr_day_update;
    String dcr_date1, my_network;

    Custom_Variables_And_Method customVariablesAndMethod;
    String DCR_ID = "";
    int PA_ID;
    ImageView ref;
    String routeYN;
    CBO_DB_Helper cbo_helper;
    ServiceHandler myServiceHandler;
    ArrayList<String> res = new ArrayList<String>();
    Context context;

    private  static final int MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL=1;


    public ArrayList<String> getData(String id) {

        try {

            String responseGetDcr = myServiceHandler.getResponse_getDcr(cbo_helper.getCompanyCode(), "" + Custom_Variables_And_Method.DCR_ID);

            if(!responseGetDcr.equals("ERROR")) {
                JSONObject jsonObject = new JSONObject(responseGetDcr);
                JSONArray jsonArray = jsonObject.getJSONArray("Tables0");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                res.add(jsonObject1.getString("DCR_DATE"));
                res.add(jsonObject1.getString("AREA"));
                res.add(jsonObject1.getString("WORKWITH1"));
                res.add(jsonObject1.getString("WORKWITH2"));
                res.add(jsonObject1.getString("WORKWITH3"));
                res.add(jsonObject1.getString("WW1"));
                res.add(jsonObject1.getString("WW2"));
                res.add(jsonObject1.getString("WW3"));
                res.add(jsonObject1.getString("ROUTE_NAME"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void setData() {
        if (res.size() == 0) {
            customVariablesAndMethod.msgBox(context,"DCR Details can not be seen...." + "\n" + "please Replan.....");

        } else {
            dcr_date.setText(res.get(0));
            area.setText(res.get(1));
            work_with.setText(res.get(2) + "\n" + res.get(3) + "\n" + res.get(4));
            route.setText("" + res.get(8));


        }

    }


    public void onCreate(Bundle b) {
        super.onCreate(b);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.get_dcr);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder_ref);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text__ref);


        hader_text.setText("Dcr Details");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        context=this;

        dcr_date = (TextView) findViewById(R.id.dcr_date_id);
        area = (TextView) findViewById(R.id.dcr_area_id);
        route = (TextView) findViewById(R.id.dcr_route_getDcr);
        work_with = (TextView) findViewById(R.id.dcr_work_id);
        back = (Button) findViewById(R.id.dcr_stsbk);
        ref = (ImageView) findViewById(R.id.ref_header);
        Dcr_day_update = (Button) findViewById(R.id.Dcr_day_update);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        PA_ID = Custom_Variables_And_Method.PA_ID;
        myServiceHandler = new ServiceHandler(context);
        cbo_helper = new CBO_DB_Helper(context);
        my_network = NetworkUtil.getConnectivityStatusString(context);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.route_layout);
        routeYN = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"root_needed");

        if (!routeYN.equalsIgnoreCase("Y")) {
            linearLayout.setVisibility(View.GONE);
        }

        Custom_Variables_And_Method.DCR_ID = cbo_helper.getDCR_ID_FromLocal();
        DCR_ID = Custom_Variables_And_Method.DCR_ID;

        new Doback().execute(DCR_ID);
        //setWorkWithInLocal();
        ref.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Vibrator vbr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if (my_network.equals("Not connected to Internet")) {
                    customVariablesAndMethod.Connect_to_Internet_Msg(context);
                } else {
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DcrPlantimestamp", customVariablesAndMethod.get_currentTimeStamp());
                   // new Service_Call_From_Multiple_Classes().DownloadAll(context,hh,MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL);
                    new Service_Call_From_Multiple_Classes().DownloadAll(context, new Response() {
                        @Override
                        public void onSuccess(Bundle bundle) {
                            customVariablesAndMethod.msgBox(context,"Data Up-dated Sucessfully...");
                        }

                        @Override
                        public void onError(String s, String s1) {
                            AppAlert.getInstance().getAlert(context,s,s1);
                        }
                    });
                    //new getDoctor_Chemist().execute();
                    vbr.vibrate(100);

                }


            }
        });


        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();

            }
        });

        Dcr_day_update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkforCalls()) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
                    final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
                    final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
                    final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
                    final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.nagative);
                    Alert_Nagative.setText("Cancel");
                    Alert_Positive.setText("Replan");
                    Alert_title.setText("Call Found");
                    Alert_message.setText("Are you sure to Replan ? \nSome Calls found in your Day Summary.\n" +
                            "Else Reset your Day Plan from Utility");

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

                    final AlertDialog dialog = builder1.create();

                    dialog.setView(dialogLayout);
                    Alert_Positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openForReplan();
                        }
                    });
                    Alert_Nagative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();

                } else {
                    openForReplan();
                }
            }
        });

    }

    private final Handler hh = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL:

                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,"Data Up-dated Sucessfully...");
                    }
                    break;
                case 99:

                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                    }
                    break;
                default:


            }
        }
    };

    private Boolean checkforCalls(){
        int result=0;
        result+=cbo_helper.getmenu_count("phdcrdr_rc");
        result+=cbo_helper.getmenu_count("tempdr");
        result+=cbo_helper.getmenu_count("chemisttemp");
        //result+=cbo_helper.getmenu_count("phdcrstk");
        if (result==0){
            return false;
        }else {
            return true;
        }
    }

    private void openForReplan(){
        if ( customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"root_needed","Y").equalsIgnoreCase("Y")) {
            Intent intent=new Intent(context, DCR_Root_new.class);
            intent.putExtra("plan_type","r");
            startActivity(intent);
            finish();
        } else {
            Intent intent=new Intent(context, Dcr_Open_New.class);
            intent.putExtra("plan_type","r");
            startActivity(intent);
            finish();
        }
    }

    class Doback extends AsyncTask<String, String, ArrayList<String>> {
        ProgressDialog pd;

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            // TODO Auto-generated method stub

            ArrayList<String> res = getData(params[0]);
            return res;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(GetDCR.this);
            pd.setTitle("CBO");
            pd.setMessage("Processing......." + "\n" + "please wait");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            setData();
            pd.dismiss();

        }
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
	


