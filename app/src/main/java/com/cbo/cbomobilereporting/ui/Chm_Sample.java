package com.cbo.cbomobilereporting.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.ChemistCall;
import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import services.CboServices;
import services.Up_Dwn_interface;
import utils.ExceptionHandler;
import utils.MyConnection;
import utils.adapterutils.GiftModel;
import utils.adapterutils.MyAdapter;
import utils.adapterutils.RCPA_Adapter;
import utils.networkUtil.NetworkUtil;
import utils.upload_download;
import utils_new.Custom_Variables_And_Method;


public class Chm_Sample extends AppCompatActivity implements Up_Dwn_interface{
    ListView mylist;
    Button save;
    Custom_Variables_And_Method customVariablesAndMethod;
    int PA_ID = 0;
    ResultSet rs;
    ArrayAdapter<GiftModel> adapter;
    List<GiftModel> list = new ArrayList<GiftModel>();
    ArrayList<String> id,score,sample,rate,item_list;//data1, data2, data3, data5;
    StringBuilder sb3, sb2, sb4, sb5,item_list_string;
    double mainval = 0.0;
    CBO_DB_Helper cbohelp;
    String callFromRcpa = "", drId, chemId, rcpaDate;
    Context context;
    String sample_name="",sample_pob="",sample_sample="";
    EditText search;

    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;

    public void getData() {
        if (callFromRcpa.equals("intent_fromRcpaCAll")) {
            adapter = new RCPA_Adapter(this, new Doback().doInBackground(PA_ID));
        }else {
            adapter = new MyAdapter(this, new Doback().doInBackground(PA_ID));
        }


        if (adapter.getCount() != 0) {
            mylist.setAdapter(adapter);
            String[] sample_name1= sample_name.split(",");
            String[] sample_qty1= sample_sample.split(",");
            String[] sample_pob1= sample_pob.split(",");

            for (int i=0;i<sample_name1.length;i++){
                for (int j=0;j<list.size();j++) {
                    if (sample_name1[i].equals(list.get(j).getName())) {
                        list.get(j).setScore(sample_pob1[i]);
                        list.get(j).setSample(sample_qty1[i]);
                    }
                }
            }

        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Chm_Sample.this);
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

        setContentView(R.layout.chm_sample);
        FlurryAgent.logEvent("Chemist Sample");


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);



        hader_text.setText("Chemsit Sample");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }


        Bundle getExtra = getIntent().getExtras();
        context=this;

        if (getExtra != null) {

            callFromRcpa = getExtra.getString("intent_fromRcpaCAll");
            assert callFromRcpa != null;
            if (callFromRcpa.equals("intent_fromRcpaCAll")) {
                drId = getExtra.getString("dr_id");
                chemId = getExtra.getString("chm_id");
                rcpaDate = getExtra.getString("dateMMDDYY");
            }else{

                if (!callFromRcpa.equals("Chem")){
                    hader_text.setText(callFromRcpa + " Sample");
                }
                sample_name = getExtra.getString("sample_name");
                sample_pob = getExtra.getString("sample_pob");
                sample_sample = getExtra.getString("sample_sample");
            }


        } else {
            callFromRcpa = "intent not found";

        }


        if (callFromRcpa.equals("intent_fromRcpaCAll")) {

            hader_text.setText("Prescribe");
        }

        search= (EditText) findViewById(R.id.search);
        mylist = (ListView) findViewById(R.id.chm_sample_list);
        mylist.setItemsCanFocus(true);
        mylist.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        save = (Button) findViewById(R.id.chm_sample_save);

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

        progress1 = new ProgressDialog(this);
        cbohelp = new CBO_DB_Helper(getApplicationContext());
        PA_ID = Custom_Variables_And_Method.PA_ID;

        id = new ArrayList<String>();
        score = new ArrayList<String>();
        sample = new ArrayList<String>();
        rate = new ArrayList<String>();
        item_list = new ArrayList<String>();
        sb3 = new StringBuilder();
        sb2 = new StringBuilder();
        sb4 = new StringBuilder();
        sb5 = new StringBuilder();
        item_list_string = new StringBuilder();
        mainval += 0.0;



        new Doback().execute(PA_ID);
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

                if (callFromRcpa.equals("intent_fromRcpaCAll")) {
                    StringBuilder sbItemId = new StringBuilder();
                    StringBuilder sbQty = new StringBuilder();
                    StringBuilder sbProduct = new StringBuilder();
                    StringBuilder sbRemark = new StringBuilder();

                    int j = 0;
                    for (int i = 0; i < list.size(); i++) {
                        boolean check = list.get(i).isSelected();
                        if (check) {
                            if (j == 0) {
                                if (list.get(i).getScore()!=null && !list.get(i).getScore().equals("")){
                                    sbItemId.append(list.get(i).getId());
                                    sbQty.append(list.get(i).getScore());
                                    sbProduct.append(list.get(i).getSample());
                                    sbRemark.append(list.get(i).getRate());
                                }
                            } else {
                                if (list.get(i).getScore()!=null && !list.get(i).getScore().equals("")){
                                    sbItemId.append("^").append(list.get(i).getId());
                                    sbQty.append("^").append(list.get(i).getScore());
                                    sbProduct.append("^").append(list.get(i).getSample());
                                    sbRemark.append("^").append(list.get(i).getRate());
                                }
                            }

                            j = j + 1;
                        } else {

                            id.remove(check);
                            score.remove(check);
                            rate.remove(check);
                            mainval = 0.0;
                        }


                    }
                    if (sbItemId.length() > 0) {

                        //Start of call to service

                        HashMap<String,String> request=new HashMap<>();
                        request.put("sCompanyFolder",cbohelp.getCompanyCode());
                        request.put("iPA_ID", ""+Custom_Variables_And_Method.PA_ID);
                        request.put("iDCR_ID",Custom_Variables_And_Method.DCR_ID);
                        request.put("iDR_ID", drId);
                        request.put("iCHEM_ID",chemId);

                        request.put("sMONTH","");
                        request.put("iITEM_ID", sbItemId.toString());
                        request.put("sITEM_NAME",sbProduct.toString());
                        request.put("sQTY", sbQty.toString());
                        request.put("sREMARK",sbRemark.toString());

                        ArrayList<Integer> tables=new ArrayList<>();
                        tables.add(0);

                        progress1.setMessage("Please Wait..\n" +
                                " Fetching data");
                        progress1.setCancelable(false);
                        progress1.show();

                        new CboServices(context,mHandler).customMethodForAllServices(request,"RCPA_COMMIT",MESSAGE_INTERNET,tables);

                        //End of call to service

                        //String myDcrId = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_ID");
                        //cbohelp.insertDataRcpa("" + myDcrId, "" + Custom_Variables_And_Method.PA_ID, drId, chemId, rcpaDate, sbItemId.toString(), sbQty.toString());

                        //startActivity(new Intent(getApplicationContext(), ViewPager_2016.class));
                    }


                    //customVariablesAndMethod.msgBox(context,"Successfully Submitted....");
                } else {

                    for (int i = 0; i < list.size(); i++) {
                        boolean check = list.get(i).isSelected();
                        if (check) {

                            id.add(list.get(i).getId());
                            item_list.add(list.get(i).getName());

                            if (list.get(i).getScore()==null || list.get(i).getScore().equals("")){
                                score.add("0");
                            }else {
                                score.add(list.get(i).getScore());
                            }

                            if (list.get(i).getSample()==null || list.get(i).getSample().equals("")){
                                sample.add("0");
                            }else {
                                sample.add(list.get(i).getSample());
                            }

                           // data2.add(list.get(i).getSample());
                            rate.add(list.get(i).getRate());

                            /*ArrayList<String> mychmid = cbohelp.getChemistIdForsample();

                            if (!(mychmid.contains(Custom_Variables_And_Method.CHEMIST_ID))) {


                                cbohelp.insertChemistSample(Custom_Variables_And_Method.CHEMIST_ID, list.get(i).getId(), list.get(i).getName(), list.get(i).getScore(), list.get(i).getSample());
                                Log.e("^^^^^^^^", Custom_Variables_And_Method.CHEMIST_ID + "," + list.get(i).getId() + "," + list.get(i).getName() + "," + list.get(i).getScore());
                            }*/

                        } else {
                            item_list.remove(check);
                            id.remove(check);
                            score.remove(check);
                            sample.remove(check);
                            rate.remove(check);
                            mainval = 0.0;
                        }

                    }


                    for (int i = 0; i < id.size(); i++) {

                        sb3.append(id.get(i)).append(",");
                        sb2.append(score.get(i)).append(",");
                        sb5.append(sample.get(i)).append(",");
                        item_list_string.append(item_list.get(i)).append(",");
                        sb4.append(rate.get(i)).append(",");
                        //sb4.append(data3.get(i)).append(",");
                    }

                    try {
                        String rateval = sb4.toString();

                        if (rate.isEmpty() || id.isEmpty()) {
                            mainval = 0.0;
                            rateval += "" + 0.0 + ",";
                        }
                        String rateval1[] = rateval.split(",");

                        Double[] intarray = new Double[rateval1.length];
                        for (int i = 0; i < rateval1.length; i++) {
                            intarray[i] = Double.parseDouble(rateval1[i]);
                        }


                        String pobval = sb2.toString();
                        if (rate.isEmpty() || id.isEmpty()) {
                            mainval = 0.0;
                            pobval += "" + 0.0 + ",";
                        }
                        String sampleQty = sb5.toString();
                        if (sample.isEmpty() || id.isEmpty()) {
                            mainval = 0.0;
                            pobval += "" + 0.0 + ",";
                        }
                        String pobval1[] = pobval.split(",");
                        Double pobarray[] = new Double[pobval1.length];
                        for (int i = 0; i < pobval1.length; i++) {
                            pobarray[i] = Double.parseDouble(pobval1[i]);
                        }
                        if (rate.isEmpty() || id.isEmpty()) {

                            mainval += 0.0;

                        }
                        for (int i = 0; i < intarray.length; i++) {
                            if (rate.isEmpty() || id.isEmpty()) {

                                pobval += "" + 0.0 + ",";
                                rateval += "" + 0.0 + ",";
                                pobarray[i] = 0.0;
                                intarray[i] = 0.0;
                                mainval += 0.0;
                            }

                            try {
                                mainval += (pobarray[i] * intarray[i]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    } catch (Exception e) {

                    }


                    Intent i = new Intent();
                    i.putExtra("val", sb3.toString());//id
                    i.putExtra("val2", sb2.toString());//score or pob
                    i.putExtra("sampleQty", sb5.toString());// sample
                    i.putExtra("resultpob", mainval);
                    i.putExtra("resultList", item_list_string.toString());

                    setResult(RESULT_OK, i);
                    finish();

                }
            }
        });
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_worktype(msg.getData());

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

    public void parser_worktype(Bundle result) {
        if (result!=null ) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    //rptName.add(new SpinnerModel(c.getString("PA_NAME"),c.getString("PA_ID")));
                    //cbohelp.updateLatLong(lat_long,dr_id,doc_type);
                }
                customVariablesAndMethod.msgBox(context,"Successfully Submitted....");
                finish();
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
        setResult(RESULT_CANCELED, i);
        super.onBackPressed();
    }

    @Override
    public void onDownloadComplete() {
        getData();
    }

    class Doback extends AsyncTask<Integer, String, List<GiftModel>> {
        ProgressDialog pd;

        @Override
        protected List<GiftModel> doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            list.clear();
            String ItemIdNotIn = "0";
            Cursor c = cbohelp.getAllProducts(ItemIdNotIn);
            if (c.moveToFirst()) {
                do {
                    if (callFromRcpa.equals("intent_fromRcpaCAll")) {
                        list.add(new GiftModel(c.getString(c.getColumnIndex("item_name")), c.getString(c.getColumnIndex("item_id")), ""));
                    }else {
                        list.add(new GiftModel(c.getString(c.getColumnIndex("item_name")), c.getString(c.getColumnIndex("item_id")), c.getString(c.getColumnIndex("stk_rate"))));
                    }

                } while (c.moveToNext());
            }
            return list;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(Chm_Sample.this);
            pd.setTitle("CBO");
            pd.setMessage("Processing......." + "\n" + "please wait");
            pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(List<GiftModel> result) {
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
            setResult(RESULT_CANCELED, i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
