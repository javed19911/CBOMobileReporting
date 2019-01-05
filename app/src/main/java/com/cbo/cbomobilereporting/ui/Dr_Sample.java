package com.cbo.cbomobilereporting.ui;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.flurry.android.FlurryAgent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONException;

import services.CboServices;
import services.Up_Dwn_interface;
import utils.ExceptionHandler;
import utils.MyConnection;
import utils.adapterutils.PobAdapter;
import utils.adapterutils.PobModel;
import utils.networkUtil.NetworkUtil;
import utils.upload_download;
import utils_new.Custom_Variables_And_Method;
import utils_new.SendMailTask;


public class Dr_Sample extends AppCompatActivity implements Up_Dwn_interface{
    ListView mylist;
    Button save;
    Custom_Variables_And_Method customVariablesAndMethod;
    int PA_ID = 0;
    String dr_id="0";
    ResultSet rs;
    CBO_DB_Helper cbohelp;
    String item_id = "", item_name = "", item_qty = "", item_pob = "";
    ArrayAdapter<PobModel> adapter;
    List<PobModel> list = new ArrayList<PobModel>();
    ArrayList<String> data1, data2, data3, data4, data5;
    StringBuilder itemid, itemname, itemqty, itempob;
    StringBuilder sb2, sb3, sb4, sb5;
    double mainval = 0.0;
    String callFromRcpa;
    boolean check, check_Rx;
    Context context;
    EditText search;
    Boolean checkIfPOB_Entered=false;

    String sample_name="",sample_pob="",sample_sample="",sample_noc="";

    public void getData() {
        adapter = new PobAdapter(this, new Doback().doInBackground(dr_id),customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"NOC_HEAD","").isEmpty());

        if (adapter.getCount() != 0) {

            String[] sample_name1= sample_name.split(",");
            String[] sample_qty1= sample_sample.split(",");
            String[] sample_pob1= sample_pob.split(",");
            String[] sample_noc1= sample_noc.split(",");

            for (int i=0;i<sample_name1.length;i++){
                for (int j=0;j<list.size();j++) {
                    if (sample_name1[i].equals(list.get(j).getName())) {
                        list.get(j).setPob(sample_pob1[i]);
                        list.get(j).setScore(sample_qty1[i]);
                        list.get(j).setNOC(sample_noc1[i]);
                    }
                }
            }

            mylist.setAdapter(adapter);

        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Dr_Sample.this);
            builder1.setTitle("CBO");
            builder1.setIcon(R.drawable.alert1);
            builder1.setMessage(" No Data In List.." + "\n" + "Please Download Data.....");
            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NetworkUtil networkUtil = new NetworkUtil(context);
                    if (!networkUtil.internetConneted(context)) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);
                    } else {
                        new upload_download(context);
                    }
                }
            });
            builder1.show();
        }


    }


    public void onCreate(Bundle b) {
        super.onCreate(b);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.dr_sample);
        FlurryAgent.logEvent("Dr_Sample");

        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            textView.setText("Sample/POB");
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        TextView prescribe_text = (TextView) findViewById(R.id.check_text_prescribe);

        Bundle getExtra = getIntent().getExtras();

        if (getExtra != null) {
            sample_name = getExtra.getString("sample_name");
            sample_pob = getExtra.getString("sample_pob");
            sample_sample = getExtra.getString("sample_sample");
            sample_noc= getExtra.getString("sample_noc");
        }

        search= (EditText) findViewById(R.id.search);
        mylist = (ListView) findViewById(R.id.dr_sample_list);
        save = (Button) findViewById(R.id.dr_sample_save);
        cbohelp = new CBO_DB_Helper(getApplicationContext());
        mylist.setItemsCanFocus(true);
        mylist.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        PA_ID = Custom_Variables_And_Method.PA_ID;
        dr_id=Custom_Variables_And_Method.DR_ID;
        data1 = new ArrayList<String>();
        data2 = new ArrayList<String>();
        data3 = new ArrayList<String>();
        data4 = new ArrayList<String>();
        data5 = new ArrayList<String>();
        sb2 = new StringBuilder();
        sb3 = new StringBuilder();
        sb4 = new StringBuilder();
        sb5 = new StringBuilder();
        itemid = new StringBuilder();
        itemname = new StringBuilder();
        itemqty = new StringBuilder();
        itempob = new StringBuilder();

        if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DR_RX_ENTRY_YN","N").equals("N")){

            prescribe_text.setVisibility(View.GONE);

        }


        new Doback().execute(dr_id);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                for (int l = 0; l < list.size(); l++) {
                    if (!search.getText().toString().equals("") &&  search.getText().length() <= list.get(l).getName().length()) {
                        if (list.get(l).getName().toLowerCase().contains(search.getText().toString().toLowerCase().trim())) {
                            //mylist.smoothScrollToPosition(l);
                            mylist.smoothScrollToPositionFromTop(l,l,500);
                            for (int j = l; j < list.size(); j++) {
                                if (search.getText().length() <= list.get(j).getName().length()) {
                                    if (list.get(j).getName().toLowerCase().contains(search.getText().toString().toLowerCase().trim())) {
                                        list.get(j).setHighlight(true);
                                    }else{
                                        list.get(j).setHighlight(false);
                                    }
                                }else{
                                    list.get(j).setHighlight(false);
                                }
                            }
                            break;
                        }else{
                            list.get(l).setHighlight(false);
                        }
                    }else{
                        list.get(l).setHighlight(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try{
                    String total_pob="";
                    boolean count=false;
                    for (int i = 0; i < list.size(); i++) {
                        check = list.get(i).isSelected();
                        if (check ) {
                            count=true;
                            break;
                        }
                    }
                    if (count) {
                        for (int i = 0; i < list.size(); i++) {
                            check = list.get(i).isSelected();

                            if (check && !list.get(i).getPob().equals("")) {
                                total_pob = list.get(i).getPob();
                                break;
                            }
                        }
                    }
                    if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLE_POB_MANDATORY").equals("Y") && total_pob.equals("")) {
                        customVariablesAndMethod.msgBox(context,"POB can't be blank");
                    }else {
                        saveDoctorSample_CheckPob();
                    }
                } catch (Exception e) {
                    List toEmailList = Arrays.asList("mobilereporting@cboinfotech.com".split("\\s*,\\s*"));
                    new SendMailTask().execute("mobilereporting@cboinfotech.com",
                            "mreporting", toEmailList, Custom_Variables_And_Method.COMPANY_CODE + ": " + "Error in DR_sample", context.getResources().getString(R.string.app_name) + "\n Company Code :" + Custom_Variables_And_Method.COMPANY_CODE + "\n DCR ID :" + Custom_Variables_And_Method.DCR_ID + "\n PA ID : " + Custom_Variables_And_Method.PA_ID + "\n App version : " + Custom_Variables_And_Method.VERSION   + "\n Error Alert :" + "Error in DR_sample" + "\n" + e.toString());

                    CboServices.getAlert(context,"Error!!!", e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, "M3GXGNKRRC8F9VPNYYY4");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    public void onBackPressed() {

        Intent i = new Intent();
        i.putExtra("val", "");
        i.putExtra("val2", "");
        i.putExtra("val3", "");
        setResult(RESULT_CANCELED, i);
        finish();
        super.onBackPressed();
    }

    public ArrayList<Integer> getdoclist() {
        ArrayList<Integer> myno = new ArrayList<Integer>();
        ArrayList<String> doclist = cbohelp.getDoctorList();
        for (int i = 0; i < doclist.size(); i++) {
            myno.add(Integer.parseInt(doclist.get(i)));
        }
        return myno;
    }

    @Override
    public void onDownloadComplete() {
        getData();
    }

    class Doback extends AsyncTask<String, String, List<PobModel>> {
        ProgressDialog pd;

        @Override
        protected List<PobModel> doInBackground(String... params) {

            list.clear();
            int i = 0;
            String ItemIdNotIn = "0";

            Cursor c = cbohelp.getAllProducts(params[0]);
            if (c.moveToFirst()) {
                do {
                    list.add(new PobModel(c.getString(c.getColumnIndex("item_name")), c.getString(c.getColumnIndex("item_id")), c.getString(c.getColumnIndex("stk_rate")), c.getString(c.getColumnIndex("sn")),
                            c.getInt(c.getColumnIndex("STOCK_QTY")), c.getInt(c.getColumnIndex("BALANCE")),
                            c.getInt(c.getColumnIndex("SPL_ID"))));
                } while (c.moveToNext());
            }

            cbohelp.close();
            return list;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(Dr_Sample.this);
            pd.setTitle("CBO");
            pd.setMessage("Processing......." + "\n" + "please wait");
            pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
            //pd.show();
        }

        @Override
        protected void onPostExecute(List<PobModel> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            getData();

            pd.dismiss();


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {
            Intent i = new Intent();
            i.putExtra("val", "");
            i.putExtra("val2", "");
            i.putExtra("val3", "");
            i.putExtra("resultpob", mainval);
            setResult(RESULT_CANCELED, i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveDoctorPrescribe() {


    }


    public void saveDoctorSample_CheckPob() {

        String Qty = "";
        String POB = "";
        String Rate = "";
        String NOC = "";
        data1.clear();
        data2.clear();
        data3.clear();
        data4.clear();
        data5.clear();
        ArrayList<String> getPrescribeRx_Dr = new ArrayList<String>();
        StringBuilder sb_rx = new StringBuilder();
        int j= 0;
        String seprator ="";
        cbohelp.deletedata(Custom_Variables_And_Method.DR_ID,"");

        for (int i = 0; i < list.size(); i++) {
            check = list.get(i).isSelected();
            check_Rx = list.get(i).isSelected_Rx();
            if (check_Rx) {

                if (j==0){
                     seprator ="";
                }else {
                    seprator =",";

                }

                sb_rx.append(seprator).append(list.get(i).getId()).append(",");
                j++;

            }

            if (check) {
                checkIfPOB_Entered=true;
                data1.add(list.get(i).getId());
                data2.add(list.get(i).getScore());
                data5.add(list.get(i).getName());
                data3.add(list.get(i).getPob());
                data4.add(list.get(i).getRate());
                Qty = list.get(i).getScore();
                POB = list.get(i).getPob();
                Rate = list.get(i).getRate();
                NOC = list.get(i).getNOC();
                if (Qty.equals("")) {
                    Qty = "0";
                }
                if (POB.equals("")) {
                    POB = "0";
                }
                if (Rate.equals("")) {
                    Rate = "0";
                }
                if (NOC.equals("")) {
                    NOC = "0";
                }
                ArrayList<String> doclist = cbohelp.getDoctorList();
                ArrayList<String> docitems = cbohelp.getDoctorAllItems();
                ArrayList<String> visual_id = cbohelp.getDoctorVisualId();
                ArrayList<Integer> actlist = getdoclist();
                if (actlist.contains(Integer.parseInt(Custom_Variables_And_Method.DR_ID))) {
                    if (visual_id.contains("1")) {
                        //cbohelp.deletedata(Custom_Variables_And_Method.DR_ID, list.get(i).getId());
                        cbohelp.insertdata(Custom_Variables_And_Method.DR_ID, list.get(i).getId(), list.get(i).getName(), Qty, POB, Rate, "1",NOC);
                    } else {

                        Log.e("no updation in sample", "no update");
                        //cbohelp.deletedata(Custom_Variables_And_Method.DR_ID, list.get(i).getId());
                        cbohelp.insertdata(Custom_Variables_And_Method.DR_ID, list.get(i).getId(), list.get(i).getName(), Qty, POB, Rate, "0",NOC);
                    }

                } else {

                    cbohelp.insertdata(Custom_Variables_And_Method.DR_ID, list.get(i).getId(), list.get(i).getName(), Qty, POB, Rate, "0",NOC);
                }


            } else {

                data1.remove(check);
                data2.remove(check);
                data3.remove(check);
                adapter.notifyDataSetChanged();
            }


        }

        for (int k = 0; k < data1.size(); k++) {
            itemid.append(data1.get(k)).append(",");
            itemname.append(data5.get(k)).append(",");
            itemqty.append(data2.get(k)).append(",");
            itempob.append(data3.get(k)).append(",");
        }

        if (!sb_rx.toString().equals("")  ) {
            getPrescribeRx_Dr = cbohelp.getDr_Rx_id(null);
             if (getPrescribeRx_Dr.contains(Custom_Variables_And_Method.DR_ID)) {
                cbohelp.updateDr_Rx_Data(Custom_Variables_And_Method.DR_ID, "" + sb_rx);
            } else if (getPrescribeRx_Dr.size() >= 0) {
                cbohelp.insert_drRx_Data(Custom_Variables_And_Method.DR_ID, "" + sb_rx);
            }
        }
        item_id = itemid.toString();
        item_name = itemname.toString();
        item_qty = itemqty.toString();
        item_pob = itempob.toString();
        cbohelp.close();
        Intent i = new Intent();
        if (checkIfPOB_Entered) {
            i.putExtra("val", "");
            i.putExtra("val2", "");
            i.putExtra("val3", "");
            i.putExtra("resultpob", mainval);
            setResult(RESULT_OK, i);
        }else{
            setResult(RESULT_CANCELED, i);
        }
        finish();


    }
}
