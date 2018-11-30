package com.cbo.cbomobilereporting.ui_new.dcr_activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui.LoginMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import async.CBOFinalTasks;
import services.CboServices;
import utils.adapterutils.ExpandableListAdapter;
import utils.clearAppData.MyCustumApplication;
import utils_new.CustomTextToSpeech;
import utils_new.Custom_Variables_And_Method;

import static java.lang.Thread.sleep;

public class DCR_Summary_new extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;

    ExpandableListView doctor;
    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cboDbHelper;
    Button back,logout;


    HashMap<String, HashMap<String, ArrayList<String>>> summary_list=new HashMap<>();
    HashMap<String, ArrayList<String>> doctor_list=new HashMap<>();
    HashMap<String, ArrayList<String>> stockist_list=new HashMap<>();
    HashMap<String, ArrayList<String>> chemist_list=new HashMap<>();
    HashMap<String, ArrayList<String>> reminder_list=new HashMap<>();
    HashMap<String, ArrayList<String>> expense_list=new HashMap<>();
    HashMap<String, ArrayList<String>> nonListed_call=new HashMap<>();
    HashMap<String, ArrayList<String>> appraisal=new HashMap<>();
    HashMap<String, ArrayList<String>> tenivia_traker=new HashMap<>();
    HashMap<String, ArrayList<String>> Dairy=new HashMap<>();
    HashMap<String, ArrayList<String>> Poultry=new HashMap<>();

    ExpandableListAdapter listAdapter;

    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcr__summary_new);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);


        hader_text.setText("DCR Summary");
        setSupportActionBar(toolbar);
        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance(context);
        cboDbHelper=customVariablesAndMethod.get_cbo_db_instance();
        progress1 = new ProgressDialog(context);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLE_POB_MANDATORY").equals("N")) {
            Custom_Variables_And_Method.SAMPLE_POB_MANDATORY="N";
        } else {
            Custom_Variables_And_Method.SAMPLE_POB_MANDATORY="Y";
        }


        doctor_list=cboDbHelper.getCallDetail("tempdr","","0");
        chemist_list=cboDbHelper.getCallDetail("chemisttemp","","0");
        stockist_list=cboDbHelper.getCallDetail("phdcrstk","","0");
        reminder_list=cboDbHelper.getCallDetail("phdcrdr_rc","","0");
        expense_list=cboDbHelper.getCallDetail("Expenses","","0");
        nonListed_call=cboDbHelper.getCallDetail("nonListed_call","","0");
        appraisal=cboDbHelper.getCallDetail("appraisal","","0");
        tenivia_traker=cboDbHelper.getCallDetail("tenivia_traker","","0");
        Dairy=cboDbHelper.getCallDetail("Dairy","","0");
        Poultry=cboDbHelper.getCallDetail("Poultry","","0");

        /*Map<String, String> keyValue = cboDbHelper.getMenu("DCR","");
        for (String key : keyValue.keySet()) {
            if (keyValue.containsKey("")){

            }


        }*/


        summary_list=new LinkedHashMap<>();
        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Doctor_NOT_REQUIRED").equals("N")){
            summary_list.put(cboDbHelper.getMenu("DCR", "D_DRCALL").get("D_DRCALL"),doctor_list);
        }

        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHEMIST_NOT_REQUIRED").equals("N")){
            summary_list.put(cboDbHelper.getMenu("DCR", "D_CHEMCALL").get("D_CHEMCALL"),chemist_list);
        }
        if( customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"STOCKIST_NOT_REQUIRED").equals("N")){
            summary_list.put(cboDbHelper.getMenu("DCR", "D_STK_CALL").get("D_STK_CALL"),stockist_list);
        }
        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Doctor_RC_NOT_REQUIRED").equals("N")){
            summary_list.put(cboDbHelper.getMenu("DCR", "D_RCCALL").get("D_RCCALL"),reminder_list);
        }
        if( customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Dairy_NOT_REQUIRED").equals("N")){
            summary_list.put(cboDbHelper.getMenu("DCR", "D_DAIRY").get("D_DAIRY"),Dairy);
        }
        if( customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Polutary_NOT_REQUIRED").equals("N")){
            summary_list.put(cboDbHelper.getMenu("DCR", "D_POULTRY").get("D_POULTRY"),Poultry);
        }
        if( customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"NonListed_NOT_REQUIRED").equals("N")){
            summary_list.put(cboDbHelper.getMenu("DCR", "D_NLC_CALL").get("D_NLC_CALL"),nonListed_call);
        }
        if( customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Appraisal_NOT_REQUIRED").equals("N")){
            summary_list.put(cboDbHelper.getMenu("DCR", "D_AP").get("D_AP"),appraisal);
        }
        if( customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Tenivia_NOT_REQUIRED").equals("N")){
            summary_list.put(cboDbHelper.getMenu("DCR", "D_DR_RX").get("D_DR_RX"),tenivia_traker);
        }
        if( customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Expense_NOT_REQUIRED").equals("N")){
            summary_list.put(cboDbHelper.getMenu("DCR", "D_EXP").get("D_EXP"),expense_list);
        }




        doctor = (ExpandableListView) findViewById(R.id.summary_list);
        back= (Button) findViewById(R.id.back);
        logout= (Button) findViewById(R.id.Logout);

        final int who=getIntent().getIntExtra("who",0);
        if (who==1){
            logout.setVisibility(View.VISIBLE);
        }else if(who==2){
            logout.setVisibility(View.VISIBLE);
            logout.setText("Reset DCR");
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (who==2) {
                    askForReset();

                }else{
                    cboDbHelper.deleteLogin();
                    cboDbHelper.deleteLoginDetail();
                    cboDbHelper.deleteFTPTABLE();
                    cboDbHelper.delete_Mail("");
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "WEBSERVICE_URL", "");
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DOB_DOA_notification_date", "");
                    new MyCustomMethod(context).stopDOB_DOA_Remainder();


                    cboDbHelper.DropDatabase(context);

                    Intent i = new Intent(getApplicationContext(), LoginMain.class);
                    startActivity(i);
                    finish();
                }
            }
        });


        final ArrayList<String> header_title=new ArrayList<>();
        //final List<Integer> visible_status=new ArrayList<>();
        for(String main_menu:summary_list.keySet()){
            header_title.add(main_menu);
            //visible_status.add(0);
        }



        listAdapter = new ExpandableListAdapter(doctor,this, header_title, summary_list);

        // setting list adapter
        doctor.setAdapter(listAdapter);
        doctor.setGroupIndicator(null);
       /* for(int i=0; i < listAdapter.getGroupCount()-1; i++)
            doctor.expandGroup(i);*/
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



    public void askForReset() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
        final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
        final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.nagative);


            Alert_Nagative.setText("Continue..");
            Alert_message.setText("Some Calls found !!!!\nAre you sure to Reset your Dcr\nAll Calls will be Deleted...");

        Alert_Positive.setText("Cancel");
        Alert_title.setText("Reset DCR!!!");


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Alert_Nagative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Custom_Variables_And_Method.internetConneted(context)) {
                    //Start of call to service

                    HashMap<String,String> request=new HashMap<>();
                    request.put("sCompanyFolder",cboDbHelper.getCompanyCode());
                    request.put("DCRID",cboDbHelper.getDCR_ID_FromLocal());

                    ArrayList<Integer> tables=new ArrayList<>();
                    tables.add(0);

                    progress1.setMessage("Please Wait..");
                    progress1.setCancelable(false);
                    progress1.show();

                    new CboServices(context,mHandler).customMethodForAllServices(request,"DcrReset_1",MESSAGE_INTERNET,tables);

                    //End of call to service
                }else{
                    customVariablesAndMethod.Connect_to_Internet_Msg(context);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
                    if ((null != msg.getData())) {

                        parser(msg.getData());

                    }
                    break;

                case 99:
                    if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}

            }
        }
    };

    private void parser(Bundle result) {
        if (result != null) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject c = jsonArray1.getJSONObject(0);

                if (c.getString("DCRID").equals("RESET")) {
                    progress1.dismiss();
                    customVariablesAndMethod.msgBox(context,"Dcr Day Successfully Reset ");
                    MyCustomMethod customMethod;
                    customMethod=new MyCustomMethod(context);
                    new CBOFinalTasks(context).releseResources();

                    customMethod.stopAlarm10Minute();
                    customMethod.stopAlarm10Sec();
                    customMethod.stopDOB_DOA_Remainder();
                    new CustomTextToSpeech().stopTextToSpeech();

                    cboDbHelper.deleteLoginDetail();
                    cboDbHelper.deleteLogin();
                    cboDbHelper.deleteUserDetail();
                    cboDbHelper.deleteUserDetail2();
                    cboDbHelper.deleteDCRDetails();
                    cboDbHelper.deleteTempDr();
                    cboDbHelper.deletedcrFromSqlite();
                    cboDbHelper.deleteResigned();
                    cboDbHelper.deleteDoctorRc();
                    cboDbHelper.deleteDoctorItem();
                    cboDbHelper.deleteDoctorItemPrescribe();
                    cboDbHelper.deleteDoctor();
                    cboDbHelper.deleteFinalDcr();
                    cboDbHelper.deleteTempChemist();
                    cboDbHelper.deleteChemistSample();
                    cboDbHelper.deleteChemistRecordsTable();
                    cboDbHelper.deleteStockistRecordsTable();
                    cboDbHelper.deleteTempStockist();
                    cboDbHelper.deleteDoctormore();

                    cboDbHelper.delete_Expense();
                    cboDbHelper.delete_Nonlisted_calls();
                    cboDbHelper.deleteDcrAppraisal();
                    cboDbHelper.delete_tenivia_traker();
                    cboDbHelper.notificationDeletebyID(null);
                    cboDbHelper.delete_Lat_Long_Reg();
                    cboDbHelper.delete_phdairy_dcr(null);
                    cboDbHelper.delete_Item_Stock();

                    Custom_Variables_And_Method.DCR_ID = "0";
                    MyCustumApplication.getInstance().clearApplicationData();

                    cboDbHelper.DropDatabase(context);

                    Intent i = new Intent(context, LoginMain.class);

                    startActivity(i);
                    finish();
                } else {
                    progress1.dismiss();
                    customVariablesAndMethod.msgBox(context,"Day plan First......");
                }


            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();
    }
}
