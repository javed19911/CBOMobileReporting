package com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Doctor_registration_GPS;
import com.uenics.javed.CBOLibrary.CboProgressDialog;
import com.uenics.javed.CBOLibrary.Response;

import java.io.File;

import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import services.Sync_service;
import utils.adapterutils.SpinnerModel;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.Report_Registration;
import utils_new.SendAttachment;
import utils_new.Service_Call_From_Multiple_Classes;

public class CallActivity extends CustomActivity implements iCall {

    androidx.appcompat.widget.Toolbar toolbar;
    private RecyclerView itemlist_filter;
    private aCall callAdaptor;
    private vmCall viewModel;
    AppCompatActivity context;
    SpinnerModel Selectedmodel;
    String dr_id_index= "";

    Report_Registration alertdFragment;
    CboProgressDialog cboProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        context = this;
        viewModel = ViewModelProviders.of(this).get(vmCall.class);
        viewModel.setBuilder((CallBuilder) getIntent().getSerializableExtra("builder"));
        viewModel.setView(context,this);
    }

    @Override
    public void getReferencesById() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ImageView clearQry = findViewById(R.id.clearQry);
        itemlist_filter = (RecyclerView) findViewById(R.id.itemList);
        callAdaptor = new aCall(this, viewModel.getItems(),viewModel.getBuilder().getShowDistance());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(callAdaptor);

        TextView filterTxt = findViewById(R.id.filterTxt);
        filterTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                callAdaptor.filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        clearQry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterTxt.setText("");
            }
        });
        callAdaptor.setOnClickListner(new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean b) {

                Selectedmodel = callAdaptor.getItemAt(position);

                if (!Selectedmodel.getAPP_PENDING_YN().equalsIgnoreCase("0")){
                    if (!customVariablesAndMethod.IsGPS_GRPS_ON(context)) {
                        //customVariablesAndMethod.Connect_to_Internet_Msg(context);
                        AppAlert.getInstance().setNagativeTxt("Cancel").setPositiveTxt("Check").DecisionAlert(context,
                                "Approval Pending !!!", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,
                                        "DCRDRADDAREA_APP_MSG","Your Additional Area Approval is Pending... \nYou Additional Area must be approved first !!!\n" +
                                                "Please contact your Head-Office for APPROVAL"),
                                new AppAlert.OnClickListener() {
                                    @Override
                                    public void onPositiveClicked(View item, String result) {
                                        new Service_Call_From_Multiple_Classes().CheckIfCallsUnlocked(context,"ADDAREA");
                                    }

                                    @Override
                                    public void onNegativeClicked(View item, String result) {

                                    }
                                });

                    } else {
                        new Service_Call_From_Multiple_Classes().CheckIfCallsUnlocked(context,"ADDAREA");
                    }
                }else if (((TextView) view.findViewById(R.id.distance)).getText().toString().equals("Registration pending...")){
                    if (!customVariablesAndMethod.IsGPS_GRPS_ON(context)) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);
                    } else {
                        Intent intent = new Intent(context, Doctor_registration_GPS.class);
                        intent.putExtra("id",Selectedmodel.getId());
                        intent.putExtra("name",Selectedmodel.getName());
                        intent.putExtra("type",viewModel.getBuilder().getType().getValue());
                        startActivity(intent);
                        //finish();
                    }
                }else if(((TextView) view.findViewById(R.id.distance)).getText().toString().contains("Km Away")) {
                    //getAlert(context,"Not In Range","You are "+((TextView) view.findViewById(R.id.distance)).getText().toString()+" from "+doc_name,true);

                    FragmentManager fm = getSupportFragmentManager();
                    alertdFragment = new Report_Registration();
                    String km=((TextView) view.findViewById(R.id.distance)).getText().toString();
                    alertdFragment.setAlertLocation(Selectedmodel.getLoc(),Selectedmodel.getLoc2(),Selectedmodel.getLoc3());
                    alertdFragment.setAlertData("Not In Range","You are "+km+" from "+Selectedmodel.getName());
                    alertdFragment.show(fm, "Alert Dialog Fragment");
                    km=km.replace("Km Away","").trim();

                    dr_id_index = "";
                    if(Selectedmodel.getLoc2().equals("") && Float.parseFloat(km)< Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"RE_REG_KM","5"))){
                        dr_id_index = "2";
                    }else if(Selectedmodel.getLoc3().equals("") && Float.parseFloat(km)< Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"RE_REG_KM","5"))){
                        dr_id_index = "3";
                    }


                }else if( Integer.parseInt(Selectedmodel.getFREQ()) != 0 && Integer.parseInt(Selectedmodel.getFREQ()) <= Integer.parseInt(Selectedmodel.getNO_VISITED()) ) {
                    customVariablesAndMethod.getAlert(context,"Visit Freq. Exceeded",("For "+Selectedmodel.getName() +"@ Allowed Freq. : " + Selectedmodel.getFREQ() + "@ Visited       : "+Selectedmodel.getNO_VISITED()).split("@"));


                }else {
                    onSendRespose(Selectedmodel);
                }
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        callAdaptor.update(viewModel.getItems());
    }

    @Override
    public void setTile() {
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }



        TextView title = toolbar.findViewById(R.id.title);
        title.setText(viewModel.getBuilder().getTitle().replace("Call",""));
    }
    @Override
    public void onSendRespose(SpinnerModel model) {
        Intent intent = new Intent();
        intent.putExtra("item",model);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Report_Registration.REQUEST_CAMERA :
                if (resultCode == RESULT_OK) {


                    File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO"+File.separator+ alertdFragment.filename);


                    CBO_DB_Helper cbo_db_helper = new CBO_DB_Helper(context);

                    if(!dr_id_index.equals("")){
                        cbo_db_helper.updateLatLong(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong", Custom_Variables_And_Method.GLOBAL_LATLON), Selectedmodel.getId(),viewModel.getBuilder().getType().getValue(),dr_id_index);
                        /*dr_id = dr_id_reg;
                        doc_name=dr_name_reg;
                        drname.setText(doc_name);*/

                        if (Custom_Variables_And_Method.internetConneted(context)) {
                            LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, new IntentFilter("SyncComplete"));
                            Sync_service.ReplyYN="Y";
                            cboProgressDialog = new CboProgressDialog(this.context, "Please Wait..\n" +
                                    Selectedmodel.getName() +" is being Registered");
                            cboProgressDialog.show();

                            startService(new Intent(context, Sync_service.class));
                        }else{
                            customVariablesAndMethod.getAlert(context,"Registered",Selectedmodel.getName()+" Successfully Re-Registered("+dr_id_index+")");

                        }
                    }else if (file1.exists() && Custom_Variables_And_Method.internetConneted(context)){
                        Location currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation",Location.class);
                        new SendAttachment((AppCompatActivity) context).execute(Custom_Variables_And_Method.COMPANY_CODE+": Out of Range Error report",context.getResources().getString(R.string.app_name)+"\n Company Code :"+Custom_Variables_And_Method.COMPANY_CODE+"\n DCR ID :"+Custom_Variables_And_Method.DCR_ID+"\n PA ID : "+Custom_Variables_And_Method.PA_ID+"\n App version : "+Custom_Variables_And_Method.VERSION+"\n massege : "+alertdFragment.Alertmassege+
                                "\nLocation-timestamp : "+currentBestLocation.getTime()+"\nLocation-Lat : "+currentBestLocation.getLatitude()+
                                "\nLocation-long : "+currentBestLocation.getLongitude()+"\n time : " +customVariablesAndMethod.currentTime(context)+"\nlatlong : "+ customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON),alertdFragment.compressImage(file1));

                    }else{
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);
                    }


                    /**/



                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(context,
                            "image capture cancelled ", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(context,
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }


    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context contex, Intent intent) {
            if (cboProgressDialog != null) {
                cboProgressDialog.dismiss();
            }
            //onSendRespose(Selectedmodel);
            callAdaptor.update(viewModel.getItems());
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
            if (intent.getStringExtra("message").equals("Y")) {
                customVariablesAndMethod.getAlert(context,"Registered",Selectedmodel.getName()+" Successfully Re-Registered("+dr_id_index+")");
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.call_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sync) {
            new Service_Call_From_Multiple_Classes()
                    .DownloadAll(context, new Response() {
                        @Override
                        public void onSuccess(Bundle bundle) {
                            callAdaptor.update(viewModel.getItems());
                        }

                        @Override
                        public void onError(String s, String s1) {
                            AppAlert.getInstance().getAlert(context,s,s1);
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }
}
