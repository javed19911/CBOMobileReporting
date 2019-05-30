package com.cbo.cbomobilereporting.ui_new.report_activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.DrVisitedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import services.CboServices;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils.model.DropDownModel;
import utils_new.Custom_Variables_And_Method;
import utils_new.Dr_Workwith_Dialog;


public class DrWiseVisit extends AppCompatActivity {
    EditText loc,workwithdr;
    //Spinner drname;
    Button drname,back,month,mrname;
    Button submit,getdr;
    Custom_Variables_And_Method customVariablesAndMethod;
    int PA_ID;

    SpinAdapter adapter;
    LinearLayout locLayout;
    ResultSet rs;
    CBO_DB_Helper cbohelp;

    String name="";
    Context context = DrWiseVisit.this;

    ArrayList<SpinnerModel>mylist=new ArrayList<SpinnerModel>();
    AlertDialog.Builder builder1;

    protected ArrayList<String> work_with = new ArrayList<String>();
    SpinnerModel []TitleName;
    ArrayList<DropDownModel>array_sort;
    private AlertDialog myalertDialog =null;
    int textlength=0;

    ArrayList<SpinnerModel> rptName = new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel>monthname = new ArrayList<SpinnerModel>();
    public String monthData;

    String monthName="" , monthId = "";
    String userName = "", userId = "";
    String last_mr_id="",dr_id="",doc_name="";
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1, MESSAGE_INTERNET_DOCTOR=2,WORK_WITH_DIALOG=0;








    public ArrayList<SpinnerModel>getDoctor(int id)
    {
        ArrayList<SpinnerModel>getdc=new ArrayList<SpinnerModel>();
        try
        {
            // getdc.add(new SpinnerModel("--Select--","0"));
            cbohelp=new CBO_DB_Helper(context);
            Cursor c=cbohelp.getDoctorListLocal();
            if(c.moveToFirst())
            {
                do
                {
                    getdc.add(new SpinnerModel(c.getString(c.getColumnIndex("dr_name")),c.getString(c.getColumnIndex("dr_id"))));
                }while(c.moveToNext());

            }
            cbohelp.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return getdc;

    }


    public void onCreate(Bundle b){
        super.onCreate(b);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.dr_wise_visit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);

        textView.setText("Doctor Wise Visit");
        if (getSupportActionBar()!= null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }
        loc=(EditText)findViewById(R.id.loc);

        workwithdr=(EditText)findViewById(R.id.workwithdcal);
        drname=(Button)findViewById(R.id.drname);
        back=(Button)findViewById(R.id.bkfinal_button);

        month = (Button) findViewById(R.id.bt_month_previoud_dr_visit);
        mrname = (Button) findViewById(R.id.mrname);

        getdr=(Button)findViewById(R.id.getdcal);
        submit=(Button)findViewById(R.id.add);
        locLayout=(LinearLayout) findViewById(R.id.layout2);

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp=new CBO_DB_Helper(context);
        progress1 = new ProgressDialog(this);
        PA_ID=Custom_Variables_And_Method.PA_ID;

        month.setText("--Select Month--");
        mrname.setText("--Select MR--");
        drname.setText("--Select Doctor--");

        ImageView spinImgName =(ImageView) findViewById(R.id.spinner_img_drvisit_name);
        ImageView spinImgMRName =(ImageView) findViewById(R.id.spinner_img_mr_name);
        ImageView spinImgMonth =(ImageView) findViewById(R.id.spinner_img_drvisit_month);

        spinImgName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onDrNameOnClick();
                getDoctor();
            }
        });

        spinImgMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthOnClick();
            }
        });
        spinImgMRName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameOnclick();
            }
        });


        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cbohelp.getCompanyCode());
        request.put("sPaId", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sMonthType","");

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);
        tables.add(1);
        tables.add(2);

        progress1.setMessage("Please Wait..\n" +
                " Fetching data");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(this,mHandler).customMethodForAllServices(request,"TEAMMONTHDIVISION_MOBILE",MESSAGE_INTERNET,tables);

        //End of call to service


       //====================================================================================================================================
        drname.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //onDrNameOnClick();
                getDoctor();
            }
        });


        mrname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameOnclick();
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                monthOnClick();
            }
        });



        getdr.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //showSelectColoursDialog1();
               /* Intent i=new Intent(DrWiseVisit.this,Dr_Workwith.class);
                startActivityForResult(i, 0);*/
                new Dr_Workwith_Dialog(context,mHandler,null,WORK_WITH_DIALOG).show();
            }
        });



        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(month.getText().toString().equals("--Select Month--")){
                    customVariablesAndMethod.msgBox(context,"Select Month First");
                } else if(mrname.getText().toString().equals("--Select MR--")){
                    customVariablesAndMethod.msgBox(context,"Select Doctor First");
                }
                else if(drname.getText().toString().equals("--Select Doctor--")){
                    customVariablesAndMethod.msgBox(context,"Select Doctor First");
                }
                else{
                    Intent intent = new Intent(DrWiseVisit.this,DrVisitedList.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    intent.putExtra("monthId",monthId);
                    intent.putExtra("dr_id",dr_id);
                    intent.putExtra("monthName",monthName);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
            }}
        });

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


    }

    public void onResume()
    {
        super.onResume();
        if(loc.getText().toString().equals(""))
        {
            loc.setText(Custom_Variables_And_Method.global_address);
            if(loc.getText().toString().equals("")||loc.getText().toString().equals(null)){
                loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
            }
        }

    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    void  getDoctor(){
        if (!userId.equals("") &&!last_mr_id.equals(userId)) {
            last_mr_id=userId;
            //Start of call to service

            HashMap<String, String> request = new HashMap<>();
            request.put("sCompanyFolder", cbohelp.getCompanyCode());
            request.put("sPaId", "" + userId);

            ArrayList<Integer> tables = new ArrayList<>();
            tables.add(0);

            progress1.setMessage("Please Wait..\n" +
                    " Fetching Doctors");
            progress1.setCancelable(false);
            progress1.show();

            new CboServices(this, mHandler).customMethodForAllServices(request, "GetDoctorByMR", MESSAGE_INTERNET_DOCTOR, tables);

            //End of call to service
        }else if (userId.equals("")){
            customVariablesAndMethod.msgBox(context,"Please Select MR First");
        }else{
            onDrNameOnClick();
        }
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
                case WORK_WITH_DIALOG:
                    Bundle b1 = msg.getData();
                    /*work_with_name = b1.getString("workwith_name");
                    work_with_id = b1.getString("workwith_id");
                    workwithdr.setText("" + work_with_name);*/
                    break;
                case MESSAGE_INTERNET_DOCTOR:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_doctor(msg.getData());

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
                    rptName.add(new SpinnerModel(c.getString("PA_NAME"),c.getString("PA_ID")));
                }
                if (rptName.size()==1){
                    userId=rptName.get(0).getId();
                    userName=rptName.get(0).getName();
                    mrname.setText(userName);
                    mrname.setPadding(1,0,5,0);
                    //getDoctor();
                }

                String table2 = result.getString("Tables2");
                JSONArray jsonArray2 = new JSONArray(table2);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);
                    monthname.add(new SpinnerModel(c.getString("MONTH_NAME"),c.getString("MONTH")));
                }
                String date= customVariablesAndMethod.currentDate().substring(0,2);
                for (int i=0;i<monthname.size();i++){
                    if (monthname.get(i).getId().substring(0,2).equals(date)){
                        monthId=monthname.get(i).getId();
                        monthName=monthname.get(i).getName();
                        month.setText(monthName);
                        break;
                    }
                }

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

    public void parser_doctor(Bundle result) {
        if (result!=null ) {

            try {
               array_sort=new ArrayList<>();
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    array_sort.add(new DropDownModel(c.getString("DR_NAME"),c.getString("DR_ID")).setColour(c.getString("COLOR")));
                }
                if (array_sort.size()==1){
                    dr_id=array_sort.get(0).getId();
                    doc_name=array_sort.get(0).getName();
                    drname.setText(userName);
                    drname.setPadding(1,0,5,0);
                }

                onDrNameOnClick();
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


    private void onDrNameOnClick(){

        AlertDialog.Builder myDialog = new AlertDialog.Builder(DrWiseVisit.this);
        //array_sort=getDoctor(PA_ID);
        final EditText editText = new EditText(DrWiseVisit.this);
        final ListView listview=new ListView(DrWiseVisit.this);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
        LinearLayout layout = new LinearLayout(DrWiseVisit.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter=new SpinAdapter(DrWiseVisit.this,R.layout.spin_row,array_sort);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                dr_id=((TextView)view.findViewById(R.id.spin_id)).getText().toString();
                doc_name=((TextView)view.findViewById(R.id.spin_name)).getText().toString();
                drname.setText(doc_name);

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textlength = editText.getText().length();
                ArrayList<DropDownModel> array_sort1=new ArrayList<>();
                for (int i = 0; i < array_sort.size(); i++) {
                    if (textlength <= array_sort.get(i).getName().length()) {

                        if (array_sort.get(i).getName().toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                            array_sort1.add(array_sort.get(i));
                        }
                    }
                }
                listview.setAdapter(new SpinAdapter(DrWiseVisit.this,R.layout.spin_row,array_sort1));
            }
        });

        myalertDialog=myDialog.show();
    }

    private void nameOnclick() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        final ListView listview = new ListView(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter = new SpinAdapter(context, R.layout.spin_row, rptName);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                userId = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                userName = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
                mrname.setText(userName);
                mrname.setPadding(1, 0, 5, 0);

            }
        });
        myalertDialog = myDialog.show();
    }

    private void monthOnClick(){

        AlertDialog.Builder myDialog = new AlertDialog.Builder(DrWiseVisit.this);
        final ListView listview=new ListView(DrWiseVisit.this);
        LinearLayout layout = new LinearLayout(DrWiseVisit.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter=new SpinAdapter(DrWiseVisit.this,R.layout.spin_row,monthname);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                monthId=((TextView)view.findViewById(R.id.spin_id)).getText().toString();
                monthName=((TextView)view.findViewById(R.id.spin_name)).getText().toString();
                month.setText(monthName);

            }
        });
        myalertDialog=myDialog.show();
    }
}

