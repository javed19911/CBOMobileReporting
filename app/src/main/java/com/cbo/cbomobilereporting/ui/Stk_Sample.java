package com.cbo.cbomobilereporting.ui;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.flurry.android.FlurryAgent;


import services.Up_Dwn_interface;
import utils.ExceptionHandler;
import utils.MyConnection;
import utils.adapterutils.GiftModel;
import utils.adapterutils.MyAdapter;
import utils.networkUtil.NetworkUtil;
import utils.upload_download;
import utils_new.Custom_Variables_And_Method;


public class Stk_Sample extends AppCompatActivity implements Up_Dwn_interface {
    ListView mylist;
    Button save;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    int PA_ID = 0;
    ResultSet rs;
    CBO_DB_Helper cbohelp;
    ArrayAdapter<GiftModel> adapter;
    List<GiftModel> list = new ArrayList<GiftModel>();
    ArrayList<String> data1, data2, data3,data5,item_list;
    StringBuilder sb2, sb3, sb4,sb5,item_list_string;
    double mainval = 0.0;
    EditText search;

    String sample_name="",sample_pob="",sample_sample="";

    public void onCreate(Bundle b) {
        super.onCreate(b);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.stk_sample);
        FlurryAgent.logEvent("Stk_Sample");

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);
        hader_text.setText("Stockist Sample");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

        search= (EditText) findViewById(R.id.search);
        mylist = (ListView) findViewById(R.id.stk_sample_list);
        mylist.setItemsCanFocus(true);
        mylist.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        save = (Button) findViewById(R.id.stk_sample_save);
        PA_ID = Custom_Variables_And_Method.PA_ID;
        data1 = new ArrayList<String>();
        data2 = new ArrayList<String>();
        data3 = new ArrayList<String>();
        data5 = new ArrayList<String>();
        item_list = new ArrayList<String>();
        cbohelp = new CBO_DB_Helper(getApplicationContext());
        sb2 = new StringBuilder();
        sb3 = new StringBuilder();
        sb4 = new StringBuilder();
        sb5 = new StringBuilder();
        item_list_string = new StringBuilder();

        Bundle getExtra = getIntent().getExtras();

        if (getExtra != null) {
            sample_name = getExtra.getString("sample_name");
            sample_pob = getExtra.getString("sample_pob");
            sample_sample = getExtra.getString("sample_sample");
        }

         getModelLocal();

        if (list.size()>0) {
            adapter = new MyAdapter(this, list);
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

        }else{
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
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

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                for (int l = 0; l < list.size(); l++) {
                    if (!search.getText().toString().equals("") && search.getText().length() <= list.get(l).getName().length()) {
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

                String total_pob="";
                boolean count=false,check;

                for (int i = 0; i < list.size(); i++) {
                    check = list.get(i).isSelected();
                    if (check ) {
                        count=true;
                        break;
                    }
                }

                if (count) {
                    item_list.clear();
                    data1.clear();
                    data2.clear();
                    data3.clear();
                    data5.clear();
                    for (int i = 0; i < list.size(); i++) {
                        check = list.get(i).isSelected();

                        if (check && !list.get(i).getScore().equals("")) {
                            total_pob = list.get(i).getScore();
                            break;
                        }
                    }
                    try {

                        for (int i = 0; i < list.size(); i++) {
                            check = list.get(i).isSelected();
                            if (check) {
                                data1.add(list.get(i).getId());
                                item_list.add(list.get(i).getName());
                                //pob
                                if (list.get(i).getScore() == null || list.get(i).getScore().equals("")) {
                                    data2.add("0");
                                } else {
                                    data2.add(list.get(i).getScore());
                                }

                                data3.add(list.get(i).getRate());
                                //sample
                                if (list.get(i).getSample() == null || list.get(i).getSample().equals("")) {
                                    data5.add("0");
                                } else {
                                    data5.add(list.get(i).getSample());
                                }
                            } else {
                                item_list.remove(check);
                                data1.remove(check);
                                data2.remove(check);
                                data3.remove(check);
                                data5.remove(check);
                            }

                        }


                        for (int i = 0; i < data1.size(); i++) {

                            sb3.append(data1.get(i)).append(",");
                            item_list_string.append(item_list.get(i)).append(",");
                            sb2.append(data2.get(i)).append(",");
                            sb4.append(data3.get(i)).append(",");
                            sb5.append(data5.get(i)).append(",");
                        }

                        String rateval = (String) sb4.toString();
                        if (data3.isEmpty() || data1.isEmpty()) {
                            mainval = 0.0;
                            rateval += "" + 0.0 + ",";
                        }
                        String rateval1[] = rateval.split(",");
                        Double[] intarray = new Double[rateval1.length];
                        for (int i = 0; i < rateval1.length; i++) {
                            intarray[i] = Double.parseDouble(rateval1[i]);
                        }

                        String pobval = sb2.toString();
                        if (data3.isEmpty() || data1.isEmpty()) {
                            mainval = 0.0;
                            pobval += "" + 0.0 + ",";
                        }
                        String pobval1[] = pobval.split(",");
                        Double pobarray[] = new Double[pobval1.length];
                        for (int i = 0; i < pobval1.length; i++) {
                            pobarray[i] = Double.parseDouble(pobval1[i]);
                        }
                        for (int i = 0; i < intarray.length; i++) {
                            if (data3.isEmpty() || data1.isEmpty()) {

                                pobval += "" + 0.0 + ",";
                                rateval += "" + 0.0 + ",";
                                pobarray[i] = 0.0;
                                intarray[i] = 0.0;
                                mainval += 0.0;
                            }

                            try {
                                mainval += (pobarray[i] * intarray[i]);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        customVariablesAndMethod.msgBox(context, "Wrong Input Please Try Again..");
                    }
                }
                if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLE_POB_MANDATORY").equals("Y") && total_pob.equals("")) {
                    customVariablesAndMethod.msgBox(context,"POB can't be blank");
                    sb2 = new StringBuilder();
                    sb3 = new StringBuilder();
                    sb4 = new StringBuilder();
                    sb5 = new StringBuilder();
                    item_list_string = new StringBuilder();
                }else {

                    Intent i = new Intent();
                    i.putExtra("val", sb3.toString());
                    i.putExtra("val2", sb2.toString());
                    i.putExtra("sampleQty", sb5.toString());
                    i.putExtra("resultpob", mainval);
                    i.putExtra("resultList", item_list_string.toString());

                    setResult(RESULT_OK, i);
                    finish();
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


    private List<GiftModel> getModelLocal() {
        list.clear();
        String ItemIdNotIn = "0";
        Cursor c = cbohelp.getAllProducts(ItemIdNotIn);
        if (c.moveToFirst()) {
            do {
                list.add(new GiftModel(c.getString(c.getColumnIndex("item_name")), c.getString(c.getColumnIndex("item_id")), c.getString(c.getColumnIndex("stk_rate"))));
            } while (c.moveToNext());
        }

        return list;
    }

    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("val", "");
        i.putExtra("val2", "");
        setResult(RESULT_CANCELED, i);
        finish();
        super.onBackPressed();
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

    @Override
    public void onDownloadComplete() {
        getModelLocal();
        adapter = new MyAdapter(this, list);
        mylist.setAdapter(adapter);
    }
}
