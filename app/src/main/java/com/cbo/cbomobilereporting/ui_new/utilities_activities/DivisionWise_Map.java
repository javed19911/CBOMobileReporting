package com.cbo.cbomobilereporting.ui_new.utilities_activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.MapsActivity;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import services.MyAPIService;
import services.ServiceHandler;
import utils.adapterutils.SpinnerModel;
import utils.model.DropDownModel;
import utils_new.CustomDatePicker;
import utils_new.CustomDialog.Spinner_Dialog;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit on 11/2/2015.
 */
public class DivisionWise_Map extends AppCompatActivity {
    Button bt_ViewMap,bt_Back,bt_divisionName,bt_ManagerName,edt_Date;
    ImageView bt_cal;
    String userId_man,userName,userId_div;
    private AlertDialog myalertDialog=null;

    Custom_Variables_And_Method customVariablesAndMethod;
    CheckBox checkSatelite;
    ServiceHandler myService;
    ArrayList<SpinnerModel> rptName=new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel>monthname=new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel>divisionNameLsit=new ArrayList<SpinnerModel>();
    public String monthData,DivisionName;
    static final int Dialog_id = 0;
    int ak_year,ak_month,ak_day;
    String ak_DateMMDDYY;
    Context context;

    public static String mIddate;

    CBO_DB_Helper cboDbHelper;
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.division_wise_map);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);
        textView.setText("Map View");
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }


        context = DivisionWise_Map.this;
        final Calendar ak_cal =Calendar.getInstance();
        bt_Back = (Button)findViewById(R.id.bt_Back_division_wise);
        bt_ViewMap = (Button) findViewById(R.id.view_map_division);
        bt_divisionName = (Button) findViewById(R.id.bt_division_name);
        bt_ManagerName = (Button) findViewById(R.id.bt_managerName);
        edt_Date =(Button) findViewById(R.id.edt_div_wise_map_date);
        bt_cal =(ImageView) findViewById(R.id.bt_div_wise_map_calender_btn);
        checkSatelite =(CheckBox) findViewById(R.id.check_satelite);


        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cboDbHelper = new CBO_DB_Helper(this);
        progress1 = new ProgressDialog(this);


        edt_Date.setText(CustomDatePicker.currentDate( CustomDatePicker.ShowFormat));
        mIddate=(CustomDatePicker.currentDate( CustomDatePicker.CommitFormat));

       // edt_Date.setText("--Select Date--");
        bt_divisionName.setText("---Select---");
        bt_ManagerName.setText("---Select---");
        ak_day = ak_cal.get(Calendar.DAY_OF_MONTH);
        ak_month = ak_cal.get(Calendar.MONTH);
        ak_year = ak_cal.get(Calendar.YEAR);



        ImageView spinImg1 = (ImageView) findViewById(R.id.spinner_img_dision_map);
        spinImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                divisionClick();
            }
        });

        ImageView spinImg2 = (ImageView) findViewById(R.id.spinner_img_manager_map);
        spinImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managerOnclick();
            }
        });


        edt_Date.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                try {
                    new CustomDatePicker(context, null,
                            CustomDatePicker.getDate(edt_Date.getText().toString(), CustomDatePicker.ShowFormat)
                    ).Show(CustomDatePicker.getDate(edt_Date.getText().toString(),  CustomDatePicker.ShowFormat)
                            , new CustomDatePicker.ICustomDatePicker() {
                                @Override
                                public void onDateSet(Date date) {
                                    edt_Date.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormat));
                                    mIddate=(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
                                }
                            });

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        });
        bt_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_Date.performClick ();
              //  showDialog(Dialog_id);
            }
        });

        bt_divisionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             divisionClick();

            }
        });
        bt_ManagerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                managerOnclick();
            }
        });





        bt_ViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String name1=bt_divisionName.getText().toString();
               String name2= bt_ManagerName.getText().toString();
               // String date3 = edt_Date.getText().toString();
                Boolean akCheckMap = checkSatelite.isChecked();


                if (name1.equalsIgnoreCase("---Select---")){
                    userId_div ="0";
                }
                if (name2.equalsIgnoreCase("---Select---")){
                    userId_man = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FM_PA_ID");
                }


                if (mIddate.equalsIgnoreCase("--Select Date--")) {
                    customVariablesAndMethod.msgBox(context,"Please Select Date First");
                }else {
              Intent map_Activity = new Intent(getApplicationContext(),MapsActivity.class);
                map_Activity.putExtra("userId_man",userId_man);
                map_Activity.putExtra("userId_div",userId_div);
                map_Activity.putExtra("ak_DateMMDDYY",mIddate);
                    map_Activity.putExtra("akCheckMap",akCheckMap.toString());
                startActivity(map_Activity);}
            }
        });
        bt_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();

            }
        });

        getTeam();

    }


    public void getTeam(){
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("sPaId", MyCustumApplication.getInstance().getUser().getID());
        request.put("sMonthType","");

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);
        tables.add(1);
        tables.add(2);

        new MyAPIService(context)
                .execute(new ResponseBuilder("TEAMMONTHDIVISION_MOBILE",request)
                        .setTables(tables)
                        .setDescription("Please Wait..\n" +
                                "Fetching data")
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        if (rptName.size()==1){
                            userId_man=rptName.get(0).getId();
                            userName=rptName.get(0).getName();
                            bt_ManagerName.setText(userName);
                            bt_ManagerName.setPadding(1,0,5,0);
                        }

                        if (divisionNameLsit.size()==1){
                            userId_div=divisionNameLsit.get(0).getId();
                            userName=divisionNameLsit.get(0).getName();
                            bt_divisionName.setText(userName);
                            bt_divisionName.setPadding(1,0,5,0);
                        }
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {
                        parser_worktype(bundle);
                    }

                    @Override
                    public void onError(String s, String s1) {

                    }
                }));

        //End of call to service
    }




    public void parser_worktype(Bundle result) throws JSONException {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    rptName.add(new SpinnerModel(c.getString("PA_NAME"),c.getString("PA_ID")));
                }



                String table1 = result.getString("Tables1");
                JSONArray jsonArray3 = new JSONArray(table1);
                for (int i = 0; i < jsonArray3.length(); i++) {
                    JSONObject c = jsonArray3.getJSONObject(i);
                    divisionNameLsit.add(new SpinnerModel(c.getString("PA_NAME"),c.getString("PA_ID")));
                }



               /* String table2 = result.getString("Tables2");
                JSONArray jsonArray2 = new JSONArray(table2);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);
                    monthname.add(new SpinnerModel(c.getString("MONTH_NAME"),c.getString("MONTH")));
                }*/



    }


    protected Dialog onCreateDialog(int id){

        if (id==Dialog_id){

            return new DatePickerDialog(this,dayPickerListner,ak_year,ak_month,ak_day);

        }

        return null;

    }

    private DatePickerDialog.OnDateSetListener dayPickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            ak_year = year;
            ak_month = ++monthOfYear;
            ak_day = dayOfMonth;

             edt_Date.setText(ak_day+"/"+ak_month+"/"+ak_year);
            ak_DateMMDDYY = ak_month+"/"+ak_day+"/"+ak_year;
        }
    };

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

    public void divisionClick(){



        new Spinner_Dialog (context, divisionNameLsit, new Spinner_Dialog.OnItemClickListener() {
            @Override
            public void ItemSelected(DropDownModel item) {
                userId_div = item.getId();
                userName = item.getName();
                bt_divisionName.setText(userName);
                bt_divisionName.setPadding(1, 0, 5, 0);
            }
        }).show();

    }

    public void managerOnclick(){
        new Spinner_Dialog (context, rptName, new Spinner_Dialog.OnItemClickListener() {
            @Override
            public void ItemSelected(DropDownModel item) {
                userId_man = item.getId();
                userName = item.getName();
                bt_ManagerName.setText(userName);
                bt_ManagerName.setPadding(1, 0, 5, 0);
            }
        }).show();


    }
}
