package com.cbo.cbomobilereporting.ui_new.dcr_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.Dcr_Area;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import services.CboServices;
import services.ServiceHandler;
import utils.MyConnection;
import utils.adapterutils.Apprisal_Spinner_Model;
import utils.adapterutils.Array_Adapter_work_feedback;
import utils.adapterutils.Appraisal_Model;

import utils.adapterutils.Dcr_Workwith_Adapter;
import utils.adapterutils.Dcr_Workwith_Model;
import utils.adapterutils.SpinnerModel;
import utils.adapterutils.Spinner_Apprisal_Adapter;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit Udainiya on 2/3/2016.
 */
public class Work_Feedback_Of_Managers extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<Apprisal_Spinner_Model> droplist = new ArrayList<Apprisal_Spinner_Model>();
    ArrayList<Map<String, String>> Appraisal_list=null;

    ProgressDialog pd;
    NetworkUtil networkUtil;
    Spinner_Apprisal_Adapter adapter;
    Spinner Spinner_name;
    String value_name = "";
    TextView button_go,button_submit;
    EditText et_observation_and_analysis,et_action_center;
    ListView list_v;
    Context context;
    LinearLayout layout_visible;
    CBO_DB_Helper cbohelp;
    ServiceHandler mServicehandeler;
    Custom_Variables_And_Method customVariablesAndMethod;
    String Dcr_Id,Aprasial_id;

    ArrayList<Appraisal_Model> appraisal_modelArrayList= new ArrayList<Appraisal_Model>();
    ArrayList<Apprisal_Spinner_Model> droplist_obersation = new ArrayList<Apprisal_Spinner_Model>();
    StringBuilder gridname = new StringBuilder();
    StringBuilder gridID = new StringBuilder();
    StringBuilder spinItemId = new StringBuilder();

    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET_AREA=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_feedback_of_managers);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);
        textView.setText("Appraisal Feedback");

        networkUtil = new NetworkUtil(this);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }

        context=Work_Feedback_Of_Managers.this;
        Spinner_name = (Spinner) findViewById(R.id.select_name_from_dropdown_2016);
        button_go = (TextView) findViewById(R.id.button_go_2016);
        button_submit=(TextView)findViewById(R.id.button_submit_2016);
        list_v = (ListView) findViewById(R.id.list_rating_2016);
        et_observation_and_analysis=(EditText)findViewById(R.id.et_observation_and_analysis_2016);
        et_action_center=(EditText)findViewById(R.id.et_action_center_2016);
        layout_visible = (LinearLayout) findViewById(R.id.linear_layout_items_after_data);
        mServicehandeler = new ServiceHandler(context);
        cbohelp = new CBO_DB_Helper(context);
        progress1 = new ProgressDialog(this);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        Dcr_Id = Custom_Variables_And_Method.DCR_ID;
        final TableLayout call_detail_layout = (TableLayout) findViewById(R.id.call_detail);

        layout_visible.setVisibility(View.GONE);

        cbohelp = new CBO_DB_Helper(context);

        ImageView spinner_img = (ImageView) findViewById(R.id.spinner_img);
        spinner_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Spinner_name.getSelectedItem() == null) { // user selected nothing...
                    Spinner_name.performClick();
                }
            }
        });

        droplist.add(new Apprisal_Spinner_Model("---Select---",""));
        //if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"APPRAISALMANDATORY").equals("Y")) {
            Appraisal_list = cbohelp.get_Appraisal("", "");
            for (int i = 0; i < Appraisal_list.size(); i++) {
                droplist.add(new Apprisal_Spinner_Model(Appraisal_list.get(i).get("PA_NAME"), (Appraisal_list.get(i).get("PA_ID"))));
            }
        /*}else{
            try{
                Cursor c=cbohelp.getDR_Workwith();
                if(c.moveToFirst())
                {
                    do
                    {
                        droplist.add(new Apprisal_Spinner_Model(c.getString(c.getColumnIndex("workwith")),c.getString(c.getColumnIndex("wwid"))));
                    }	while(c.moveToNext());
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }*/

        adapter = new Spinner_Apprisal_Adapter(context,droplist);
        Spinner_name.setAdapter(adapter);

        Spinner_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Aprasial_id=droplist.get(i).getId();
                layout_visible.setVisibility(View.GONE);
                list_v.setVisibility(View.GONE);
                if (i!=0) {
                    String[] call_name = {"Doctor", "Chemist"};
                    String[] call = {Appraisal_list.get(i-1).get("DR_CALL"), Appraisal_list.get(i-1).get("CHEM_CALL")};
                    String[] avg = {Appraisal_list.get(i-1).get("DR_AVG"), Appraisal_list.get(i-1).get("CHEM_AVG")};
                    init(call_detail_layout, call_name, call, avg);
                }else{
                    call_detail_layout.removeAllViews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Spinner_Apprisal_Adapter.Apprisal_Spinner_Name_Id.equals("")) {

                    layout_visible.setVisibility(View.GONE);

                } else {
                    if (!networkUtil.internetConneted(Work_Feedback_Of_Managers.this)) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);

                    } else {
                        //Start of call to service

                        HashMap<String, String> request = new HashMap<>();
                        request.put("sCompanyFolder", cbohelp.getCompanyCode());
                        request.put("sPaId", Spinner_Apprisal_Adapter.Apprisal_Spinner_Name_Id);
                        request.put("sDcrId",Dcr_Id);

                        ArrayList<Integer> tables = new ArrayList<>();
                        tables.add(0);

                        progress1.setMessage("Please Wait.. \n Fetching Data");
                        progress1.setCancelable(false);
                        progress1.show();

                        new CboServices(context, mHandler).customMethodForAllServices(request, "AppraisalDDL_Mobile", MESSAGE_INTERNET_AREA, tables);

                        //End of call to service
                        list_v.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!networkUtil.internetConneted(Work_Feedback_Of_Managers.this)) {
                    customVariablesAndMethod.Connect_to_Internet_Msg(context);

                } else {
                    new SubmitDataInBackgroudm().execute();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      value_name = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        value_name = "";
    }


    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    private void init(TableLayout stk1, String[] sample_name, String[] sample_qty, String[] sample_pob) {
        TableLayout stk=stk1;
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        TableRow tbrow0 = new TableRow(context);
        tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText("Call Type");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.WHITE);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(" No. Call ");
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextColor(Color.WHITE);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(context);
        tv2.setPadding(5, 5, 5, 0);
        tv2.setText(" Call Avg.");
        tv2.setTextColor(Color.WHITE);
        tv2.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv2);
        stk.removeAllViews();
        stk.addView(tbrow0);
        for (int i = 0; i < sample_name.length; i++) {
            TableRow tbrow = new TableRow(context);
            TextView t1v = new TextView(context);
            t1v.setText(sample_name[i]);
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);
            TextView t2v = new TextView(context);
            t2v.setText(sample_qty[i]);
            t2v.setPadding(5, 5, 5, 0);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(context);
            t3v.setText(sample_pob[i]);
            t3v.setPadding(5, 5, 5, 0);
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            stk.addView(tbrow);
        }
        if(sample_name.length==1 && sample_name[0].equals("0")){
            stk.removeAllViews();
        }

    }

    class SubmitDataInBackgroudm extends AsyncTask<String,String,String> {

        String sGRADE_STR,sGRADE_NAME_STR,sOBSERVATION,sACTION_TAKEN,sAPPRAISAL_NAME_STR,sAPPRAISAL_ID_STR;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(context);
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.show();
            sOBSERVATION=et_observation_and_analysis.getText().toString();
            sACTION_TAKEN=et_action_center.getText().toString();


        }

        @Override
        protected String doInBackground(String... params) {

            String result="";
            getData();
            sGRADE_STR=gridID.toString();
            sGRADE_NAME_STR=gridname.toString();
            sAPPRAISAL_ID_STR=spinItemId.toString();

            result = mServicehandeler.getResponseDCRAPPRAISAL_COMMIT(cbohelp.getCompanyCode(),Aprasial_id,
                        Custom_Variables_And_Method.DCR_ID,sGRADE_STR,sAPPRAISAL_ID_STR,sOBSERVATION,sACTION_TAKEN);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.contains("ERROR")) {
                cbohelp.update_Apraisal(Aprasial_id, "1", sGRADE_STR, sGRADE_NAME_STR,sAPPRAISAL_ID_STR, "",sOBSERVATION,sACTION_TAKEN);
                customVariablesAndMethod.msgBox(context,"Data Successfully Submitted..");
                finish();
            }
            pd.dismiss();


        }
    }



    public void getData(){


        String concat ="^";

        for (int i=0;i<appraisal_modelArrayList.size();i++){
            if (i==0){
                concat ="";
            }
            else {
                concat ="^";
            }
            gridID.append(concat).append(appraisal_modelArrayList.get(i).getCus_id());
            gridname.append(concat).append(appraisal_modelArrayList.get(i).getCus_name());
            spinItemId.append(concat).append(appraisal_modelArrayList.get(i).getSpin_id());
        }



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_AREA:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        parser_appraisal(msg.getData());
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

    public void parser_appraisal(Bundle result) {
        if (result!=null ) {

            try {

                ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
                newlist.add(new SpinnerModel("--Select--", ""));

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                appraisal_modelArrayList.clear();
                for (int i = 1; i <= jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i-1);
                    appraisal_modelArrayList.add(new Appraisal_Model(""+i,c.getString("GRADE_NAME"),c.getString("GRADE_ID"),"0"));
                }

                Array_Adapter_work_feedback adapter_work_feedback;
                adapter_work_feedback =new Array_Adapter_work_feedback(context,appraisal_modelArrayList);
                layout_visible.setVisibility(View.VISIBLE);
                list_v.setAdapter(adapter_work_feedback);
                setListViewHeightBasedOnChildren(list_v);

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
}
