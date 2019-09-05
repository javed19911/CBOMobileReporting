package com.cbo.cbomobilereporting.ui_new.dcr_activities.DCRCall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.databaseHelper.Call.Db.ChemRcCallDB;
import com.cbo.cbomobilereporting.databaseHelper.Call.Db.DrRcCallDB;
import com.cbo.cbomobilereporting.databaseHelper.Call.mDrRCCall;
import com.cbo.cbomobilereporting.databaseHelper.Location.LocationDB;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils.CallActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils.CallBuilder;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.Response;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import locationpkg.Const;
import services.CboServices;
import services.MyAPIService;
import services.Sync_service;
import utils.adapterutils.ExpandableListAdapter;
import utils.adapterutils.SpinnerModel;
import utils.networkUtil.NetworkUtil;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.GPS_Timmer_Dialog;
import utils_new.Service_Call_From_Multiple_Classes;

public  class DCRCallActivity extends CustomActivity implements ExpandableListAdapter.Summary_interface, IChemRcCall {

    vmChemRcCall viewModel;
    TextView hader_text;
    private final int CALL_ACTIVITY=0;



    EditText loc,remarkTxt;
    Button add,drname;
    Custom_Variables_And_Method customVariablesAndMethod;

    CBO_DB_Helper cbohelp;
    LinearLayout layout;
    String live_km;

    ExpandableListView summary_layout;
    Button tab_call,tab_summary;
    HashMap<String, HashMap<String, ArrayList<String>>> summary_list=new HashMap<>();
    HashMap<String, ArrayList<String>> rc_chem_list_summary=new HashMap<>();
    ExpandableListAdapter listAdapter;
    LinearLayout call_layout,detail_layout;
    TableLayout stk,doc_detail;
    private Location currentBestLocation;
    ImageView spinImg;
    private  static final int GPS_TIMMER=4,MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL=1,MESSAGE_INTERNET_SEND_FCM=2;


    ///firebase DB
    ChemRcCallDB chemRcCallDB;
    LocationDB locationDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_new);
        viewModel = ViewModelProviders.of(this).get(vmChemRcCall.class);
        viewModel.setView(context,this);
    }

    @Override
    public void getRefrenceByid() {

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        hader_text = (TextView) findViewById(R.id.hadder_text_1);



        setSupportActionBar(toolbar);
        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loc=(EditText)findViewById(R.id.loc_remdr);
        add=(Button)findViewById(R.id.add_dr_rem);
        drname=(Button)findViewById(R.id.rem_drname);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp=new CBO_DB_Helper(getApplicationContext());
        layout=(LinearLayout)findViewById(R.id.layout_remcall);
        remarkTxt=(EditText) findViewById(R.id.remak);
        live_km = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"live_km");

        //get batterry level
        customVariablesAndMethod.getbattrypercentage(context);

        call_layout = (LinearLayout) findViewById(R.id.call_layout);
        detail_layout = (LinearLayout) findViewById(R.id.detail_layout);
        detail_layout.setBackgroundColor(Color.TRANSPARENT);
        summary_layout = (ExpandableListView) findViewById(R.id.summary_layout);
        tab_call= (Button) findViewById(R.id.call);
        tab_summary= (Button) findViewById(R.id.summary);

        stk= (TableLayout) findViewById(R.id.last_pob);
        doc_detail= (TableLayout) findViewById(R.id.doc_detail);


        locationDB = new LocationDB();
        chemRcCallDB = new ChemRcCallDB();



        drname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChemistSelect();

            }
        });



        spinImg=(ImageView) findViewById(R.id.spinner_img_remider_call);
        spinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drname.performClick();

            }
        });

        loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);

        if(Custom_Variables_And_Method.location_required.equals("Y")) {
            layout.setVisibility(View.VISIBLE);
        }




        remarkTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.getChemist().setRemark(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (Custom_Variables_And_Method.GLOBAL_LATLON.equalsIgnoreCase("0.0,0.0")) {

                    Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
                }

                ArrayList<String>ChemRc=new ArrayList<String>();
                if(loc.getText().toString().equals(""))
                {
                    loc.setText("UnKnown Location");
                }


                if (viewModel.getChemist().getId().equalsIgnoreCase("0")){

                    customVariablesAndMethod.msgBox(context,"Please Select Chemist First");


                }else if (viewModel.getChemist().IsRemarkMandatory() &&  viewModel.getChemist().getRemark().isEmpty()) {
                    customVariablesAndMethod.msgBox(context,"Please enter remak");

                }else{

                    ChemRc=cbohelp.getChemRc();
                    if(ChemRc.contains(viewModel.getChemist().getId())) {
                        customVariablesAndMethod.msgBox(context,viewModel.getChemist().getName() +" allready added ");
                    } else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false)) {
                        customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                        LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                                new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                    }else {

                        submitRC_Chem();

                    }


                }}
        });





        rc_chem_list_summary=cbohelp.getCallDetail("phdcrchem_rc","","1");
        //expense_list=new CBO_DB_Helper(context).getCallDetail("tempdr");

        summary_list=new LinkedHashMap<>();
        summary_list.put("Chemist Reminder",rc_chem_list_summary);

        final ArrayList<String> header_title=new ArrayList<>();
        //final List<Integer> visible_status=new ArrayList<>();
        for(String main_menu:summary_list.keySet()){
            header_title.add(main_menu);
            //visible_status.add(0);
        }



        listAdapter = new ExpandableListAdapter(summary_layout,this, header_title, summary_list);

        // setting list adapter
        summary_layout.setAdapter(listAdapter);
        summary_layout.setGroupIndicator(null);
        for(int i=0; i < listAdapter.getGroupCount(); i++)
            summary_layout.expandGroup(i);
        //doctor.expandGroup(1);

        summary_layout.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
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

        tab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_layout.setVisibility(View.VISIBLE);
                summary_layout.setVisibility(View.GONE);
                tab_call.setBackgroundResource(R.drawable.tab_selected);
                tab_summary.setBackgroundResource(R.drawable.tab_deselected);
            }
        });

        tab_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                summary_layout.setVisibility(View.VISIBLE);
                call_layout.setVisibility(View.GONE);
                tab_call.setBackgroundResource(R.drawable.tab_deselected);
                tab_summary.setBackgroundResource(R.drawable.tab_selected);
            }
        });
    }








    private void submitRC_Chem(){
        customVariablesAndMethod.SetLastCallLocation(context);

        setAddressToUI();

        submitChemRcInLocal();

        new Service_Call_From_Multiple_Classes().SendFCMOnCall(context, mHandler, MESSAGE_INTERNET_SEND_FCM,"C",viewModel.getChemist().getId(),Custom_Variables_And_Method.GLOBAL_LATLON);

    }



    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case GPS_TIMMER:
                    spinImg.setEnabled(true);
                    drname.setEnabled(true);
                    openChemistList(null);
                    break;

                case MESSAGE_INTERNET_SEND_FCM:
                    CBOServices.checkConnection(context, new Response() {
                        @Override
                        public void onSuccess(Bundle bundle) {
                            if (live_km.equalsIgnoreCase("Y")||(live_km.equalsIgnoreCase("Y5"))){
                                MyCustomMethod myCustomMethod= new MyCustomMethod(context);
                                myCustomMethod.stopAlarm10Minute();
                                myCustomMethod.startAlarmIn10Minute();
                            }else {
                                startService(new Intent(context, Sync_service.class));
                            }
                        }

                        @Override
                        public void onError(String s, String s1) {

                        }
                    });

                    MyCustumApplication.getInstance().Logout((Activity) context);
                    break;
                case 99:
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    break;


            }
        }
    };





    public void submitChemRcInLocal()
    {

        currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation",Location.class);

        String locExtra="";

        if (currentBestLocation!=null) {
            locExtra = "Lat_Long " + currentBestLocation.getLatitude() + "," + currentBestLocation.getLongitude() + ", Accuracy " + currentBestLocation.getAccuracy() + ", Time " + currentBestLocation.getTime() + ", Speed " + currentBestLocation.getSpeed() + ", Provider " + currentBestLocation.getProvider();
        }



            viewModel.getChemist().setSrno(customVariablesAndMethod.srno(context))
                    .setLOC_EXTRA(locExtra)
                    .setTime(customVariablesAndMethod.currentTime(context));

            String dcrid=Custom_Variables_And_Method.DCR_ID;

            try{

                long val=cbohelp.insertChemRem(dcrid, viewModel.getChemist().getId(), viewModel.getChemist().getCallLatLong()+"!^"+loc.getText().toString(),
                        customVariablesAndMethod.currentTime(context),Custom_Variables_And_Method.GLOBAL_LATLON,
                        "0","0",viewModel.getChemist().getSrno(),Custom_Variables_And_Method.BATTERYLEVEL,
                        viewModel.getChemist().getRemark(),"",locExtra,viewModel.getChemist().getRefLatLong());
                Log.e("dr reminder added", ""+val);
                if(val>0)
                {
                    Log.e("reminder saved in local", ""+val);
                }

                chemRcCallDB.insert(viewModel.getChemist());
                locationDB.insert(viewModel.getChemist());

            }catch(Exception e){

            }
    }


    //======================================================oncreate finish===============================================
    public void setAddressToUI() {
        String networkStatus= NetworkUtil.getConnectivityStatusString(getApplicationContext());
        if (networkStatus.equals("Not connected to Internet")) {
            loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
        } else {
            if (Custom_Variables_And_Method.global_address != null || Custom_Variables_And_Method.global_address != "") {
                //loc.setText(Custom_Variables_And_Method.global_address);
                loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
            } else {
                loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
            }

        }
    }





    public void onBackPressed(){
        finish();
        super.onBackPressed();
    }

    @Override
    public void Edit_Call(final String Dr_id, String Dr_name) {
        /*LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        final Button Alert_Nagative= (Button) dialogLayout.findViewById(R.id.nagative);
        Alert_Nagative.setText("Cancel");
        Alert_Positive.setText("Edit");
        Alert_title.setText("Edit!!!");
        Alert_message.setText("Do you want to edit "+Dr_name+" ?");


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                call_layout.setVisibility(View.VISIBLE);
                summary_layout.setVisibility(View.GONE);
                tab_call.setBackgroundResource(R.drawable.tab_selected);
                tab_summary.setBackgroundResource(R.drawable.tab_deselected);

                viewModel.setCallmodel(new SpinnerModel(Dr_name,Dr_id));
                dialog.dismiss();
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

*/

    }

    @Override
    public void delete_Call(final String Dr_id, final String Dr_name) {

        AppAlert.getInstance().setPositiveTxt("Delete").DecisionAlert(context, "Delete!!!", "Do you Really want to delete " + Dr_name + " ?", new AppAlert.OnClickListener() {
            @Override
            public void onPositiveClicked(View item, String result) {

                //Start of call to service

                HashMap<String,String> request=new HashMap<>();
                request.put("sCompanyFolder",  MyCustumApplication.getInstance ().getUser ().getCompanyCode ());
                request.put("iPaId",  MyCustumApplication.getInstance ().getUser ().getID ());
                request.put("iDCR_ID",  MyCustumApplication.getInstance ().getUser ().getDCRId ());
                request.put("iDR_ID", Dr_id);
                request.put("sTableName", "CHEMISTRC");


                ArrayList<Integer> tables=new ArrayList<>();
                tables.add(0);

                new MyAPIService(context)
                        .execute(new ResponseBuilder("DRCHEMDELETE_MOBILE",request)
                                .setDescription("Please Wait..." +
                                        "\nDeleting "+Dr_name+" from DCR...")
                                .setResponse(new CBOServices.APIResponse() {
                                    @Override
                                    public void onComplete(Bundle bundle) throws Exception {
                                        String table0 = bundle.getString("Tables0");
                                        JSONArray jsonArray1 = new JSONArray(table0);
                                        JSONObject object = jsonArray1.getJSONObject(0);

                                        if (object.getString("DCRID").equalsIgnoreCase("1")) {

                                            mChemistRc chemistRc = new mChemistRc();
                                            chemistRc.setId(Dr_id);
                                            chemRcCallDB.delete(chemistRc );
                                            cbohelp.delete_ChemistRemainder_from_local_all(Dr_id);
                                            customVariablesAndMethod.msgBox(context,Dr_name+" sucessfully Deleted.");
                                            finish();
                                        }else{
                                            AppAlert.getInstance().getAlert(context,"Alert",object.getString("DCRID"));
                                        }
                                    }

                                    @Override
                                    public void onResponse(Bundle bundle) throws Exception {

                                    }

                                    @Override
                                    public void onError(String s, String s1) {
                                        AppAlert.getInstance().getAlert(context,s,s1);
                                    }
                                }));

                //End of call to service




            }

            @Override
            public void onNegativeClicked(View item, String result) {
            }
        });


    }

    @Override
    public CallBuilder getCallBuilder() {
        return new CallBuilder()
                .setShowDistance(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(context,"IsBackDate","1").equals("1"))
                .setType(CallBuilder.CallType.ChemistRiminder)
                .settitle(getActivityTitle());
    }



    @Override
    public void setActivityTitle(String title) {
        hader_text.setText( title);
    }

    @Override
    public String getActivityTitle() {

        return getIntent().getStringExtra("title");
    }

    @Override
    public void onChemistSelect() {
        spinImg.setEnabled(false);
        drname.setEnabled(false);
        new GPS_Timmer_Dialog(context,mHandler,"Scanning Doctors...",GPS_TIMMER).show();
    }

    public void openChemistList(ArrayList<SpinnerModel> chemist) {
        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra("builder",getCallBuilder());
        startActivityForResult(intent,CALL_ACTIVITY);
    }

    @Override
    public void onChemistSelected(mChemistRc chemistRc) {

    }

    @Override
    public void setName(String name) {
        drname.setText(name);
    }

    @Override
    public void remarkReqd(Boolean required) {
        remarkTxt.setVisibility(required ? View.VISIBLE:View.GONE);
    }

    @Override
    public void setRemark(String remark) {
        remarkTxt.setText(remark);
    }

    @Override
    public void save(mChemistRc chemistRc) {

    }


    private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contex, Intent intent) {
            Location location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE);
            submitRC_Chem();
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);

        }
    };




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {


            case CALL_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    viewModel.setCallmodel((SpinnerModel) data.getSerializableExtra("item"));
                }else{
                    viewModel.setCallmodel(new SpinnerModel("--- Select ---","0"));
                }
                break;
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

