package com.cbo.cbomobilereporting.ui_new.dcr_activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import services.CboServices;
import utils.adapterutils.SpinnerModel;
import utils.adapterutils.TPList_Adapter;
import utils_new.Custom_Variables_And_Method;

public class PospondFarmerMeeting extends AppCompatActivity {

    private Custom_Variables_And_Method customVariablesAndMethod;
    private CBO_DB_Helper cbohelp;
    private Context context;
    private ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1,MESSAGE_INTERNET_POSTPOND = 2;
    private TPList_Adapter adapter;
    ListView TpListView;
    Button Back;

    private ArrayList<SpinnerModel> tpList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pospond_farmer_meeting);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);
        hader_text.setText("Select the Postpond Activity Date....");
        setSupportActionBar(toolbar);

        context = this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp=new CBO_DB_Helper(context);
        progress1 = new ProgressDialog(context);

        tpList = new ArrayList<>();


        TpListView = findViewById(R.id.TP_list);
        Back = findViewById(R.id.back);

        adapter = new TPList_Adapter((AppCompatActivity) context,tpList);
        TpListView.setAdapter(adapter);



        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cbohelp.getCompanyCode());
        request.put("iPaId", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sMONTH", "");

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);


        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(context, mHandler).customMethodForAllServices(request, "FARMER_TPDATE_DDL", MESSAGE_INTERNET, tables);

        //End of call to service

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowAlertToPostpondFarmerMeetingCalls(tpList.get(i)) ;
            }
        });
    }


    private void ShowAlertToPostpondFarmerMeetingCalls(SpinnerModel data){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //customVariablesAndMethod.msgBox(context,"Please Visit atleast One " +cboDbHelper.getMenu("DCR", "D_FAR").get("D_FAR"));
        final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
        final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
        final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.nagative);
        Alert_Nagative.setText("Cancel");
        Alert_Positive.setText("Ok");
        Alert_title.setText("Postpond Activity ?");
        Alert_message.setText("Are you sure to postpond Today's activity to " + data.getName());

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                //Start of call to service

                HashMap<String, String> request = new HashMap<>();
                request.put("sCompanyFolder", cbohelp.getCompanyCode());
                request.put("iRAPTPDET_ID", data.getId());
                request.put("iDCR_ID", "" + Custom_Variables_And_Method.DCR_ID);

                ArrayList<Integer> tables = new ArrayList<>();
                tables.add(0);


                progress1.setMessage("Please Wait..");
                progress1.setCancelable(false);
                progress1.show();

                new CboServices(context, mHandler).customMethodForAllServices(request, "FARMER_TPCM3_UPDATE", MESSAGE_INTERNET_POSTPOND, tables);

                //End of call to service
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
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    if ((null != msg.getData())) {

                        parser_tp_list(msg.getData());
                        progress1.dismiss();
                    }
                    break;
                case MESSAGE_INTERNET_POSTPOND:
                    if ((null != msg.getData())) {

                        parser_tp_update(msg.getData());
                        progress1.dismiss();
                    }
                    break;
                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                   progress1.dismiss();
            }
        }
    };

    public void parser_tp_list(Bundle result) {
        if (result!=null ) {

            try {

                ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
                newlist.add(new SpinnerModel("--Select--", ""));

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    tpList.add(new SpinnerModel(c.getString("DATE"),c.getString("TPDET_ID")));
                }

                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",context.getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);

        progress1.dismiss();

    }

    public void parser_tp_update(Bundle result) {
        if (result!=null ) {

            try {

                ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
                newlist.add(new SpinnerModel("--Select--", ""));

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    if (c.getString("DCRID").equals("1"))
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "FARMERREGISTERYN","" );

                }

               finish();

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",context.getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);

        progress1.dismiss();

    }


}
