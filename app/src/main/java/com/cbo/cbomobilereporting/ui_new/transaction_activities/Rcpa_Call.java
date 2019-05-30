package com.cbo.cbomobilereporting.ui_new.transaction_activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.Chm_Sample_Dialog;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by RAM on 10/14/15.
 */
public class Rcpa_Call extends AppCompatActivity {


    Button btDrName,btChemName,btShow,btCancel,btCal;
    SpinnerModel[]TitleName;
    SpinnerModel[]TitleName_chem;
    ArrayList<SpinnerModel>array_sort;
    ArrayList<SpinnerModel>array_sort_chem;
    private AlertDialog myalertDialog=null;
    String doc_name,dr_id;
    int textLength = 0;
    EditText edtDate;
    int year_x,month_x,day_x    ;
    static  final int Dialog_id=0;
    ArrayList<SpinnerModel>docList;
    ArrayList<SpinnerModel>chemist;
    CBO_DB_Helper cbohelp;
    String chm_id,chm_name,dateMMDDYY;
     int textlength;
    ImageView chem_img,dr_img;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rcpa_call);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
        textView.setText("RCPA Call");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }


        btDrName = (Button) findViewById(R.id.bt_rcpa_call_doctor);
        btChemName = (Button) findViewById(R.id.bt_rcpa_call_chemist);
        btShow = (Button) findViewById(R.id.bt_rcpa_show);
        btCancel = (Button) findViewById(R.id.bt_rcpa_back);
        btCal = (Button) findViewById(R.id.bt_rcpa_calender_btn);
        edtDate = (EditText) findViewById(R.id.bt_rcpa_call_date);
        dr_img= (ImageView) findViewById(R.id.spinner_img_dr);
        chem_img= (ImageView) findViewById(R.id.spinner_img_chem);

        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();


       final Calendar calendar= Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);
        cbohelp = new CBO_DB_Helper(this);
        btDrName.setText("---Select Here---");
        btChemName.setText("---Select Here---");

       new DoctorData().execute();

       btDrName.setOnClickListener(new View.OnClickListener() {


           @Override
           public void onClick(View v) {
               select_dr_name();
           }

       });
        btChemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                select_chem_name();
            }
        });

        dr_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_dr_name();
            }

        });
        chem_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                select_chem_name();
            }
        });



              edtDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                showDialog(Dialog_id);
                return false;
            }
        });


        btCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Dialog_id);
            }
        });

        btCal.setVisibility(View.GONE);


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String checkDate,checkDrName,checkChemistName;
                checkDate = edtDate.getText().toString();
                checkDrName = btDrName.getText().toString();
                checkChemistName = btChemName.getText().toString();

                /*if (checkDate.equals("--- Select Date ---")){
                    customVariablesAndMethod.msgBox(context,"Please Select Date");
                }
                else*/

                if (checkDrName.equals("---Select Here---") && checkChemistName.equals("---Select Here---")) {
                    customVariablesAndMethod.msgBox(context, "Please Select Doctor/Chemist");
                    /*if (checkDrName.equals("---Select Here---")) {
                        customVariablesAndMethod.msgBox(context, "Please Select Doctor");
                    } else if (checkChemistName.equals("---Select Here---")) {
                        customVariablesAndMethod.msgBox(context, "Please Select Chemist");

                    }*/
                }else {
                    /*
                    Intent i = new Intent(getApplicationContext(),Chm_Sample.class);
                    i.putExtra("intent_fromRcpaCAll","intent_fromRcpaCAll");
                    i.putExtra("dr_id",dr_id);
                    i.putExtra("chm_id",chm_id);
                    i.putExtra("dateMMDDYY",dateMMDDYY);
                    startActivity(i);*/

                    Bundle b=new Bundle();
                    b.putString("intent_fromRcpaCAll","intent_fromRcpaCAll");
                    b.putString("dr_id", dr_id);
                    b.putString("chm_id",chm_id);
                    b.putString("dateMMDDYY", dateMMDDYY);
                    new Chm_Sample_Dialog(context,null,b,0).Show();

                }
              }
        });


    }

    protected Dialog onCreateDialog(int id){
        if((id==Dialog_id)){
            return new DatePickerDialog(this,daypickerlistner,year_x,month_x,day_x);

        }
        return null;
    }
    private  DatePickerDialog.OnDateSetListener daypickerlistner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            year_x=year;
            month_x= monthOfYear;
            day_x=dayOfMonth;
           int month=month_x+1;

            edtDate.setText(day_x+"/"+month+"/"+year_x);

            dateMMDDYY =(month+"/"+day_x+"/"+year_x);
        }
    };


    private void select_dr_name(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(Rcpa_Call.this);
        final EditText editText = new EditText(Rcpa_Call.this);
        final ListView listview=new ListView(Rcpa_Call.this);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
        array_sort=new ArrayList<SpinnerModel>(Arrays.asList(TitleName));


        LinearLayout layout = new LinearLayout(Rcpa_Call.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter=new SpinAdapter(Rcpa_Call.this
                , R.layout.spin_row,array_sort);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                dr_id=((TextView)view.findViewById(R.id.spin_id)).getText().toString();
                doc_name=((TextView)view.findViewById(R.id.spin_name)).getText().toString();
                btDrName.setText(doc_name);
            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textLength = editText.getText().length();
                array_sort.clear();
                for (int i= 0;i<textLength; i++){

                    if (TitleName[i].getName().toLowerCase().contains(editText.getText().toString().toLowerCase().trim())){
                        array_sort.add(TitleName[i]);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        myalertDialog=myDialog.show();
    }


    private void select_chem_name(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(Rcpa_Call.this);
        //ArrayList<SpinnerModel>mydata=new ArrayList<SpinnerModel>();
        //mydata=getDoctor(PA_ID);
        final EditText editText = new EditText(Rcpa_Call.this);
        final ListView listview=new ListView(Rcpa_Call.this);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
        array_sort_chem=new ArrayList<SpinnerModel> (Arrays.asList(TitleName_chem));
        LinearLayout layout = new LinearLayout(Rcpa_Call.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter=new SpinAdapter(Rcpa_Call.this,R.layout.spin_row,array_sort_chem);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                chm_id=((TextView)view.findViewById(R.id.spin_id)).getText().toString();
                chm_name=((TextView)view.findViewById(R.id.spin_name)).getText().toString();
                Custom_Variables_And_Method.CHEMIST_ID=chm_id;
                btChemName.setText(chm_name);

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
                //getDoctor(PA_ID).clear_2();
                array_sort_chem.clear();
                for (int i = 0; i < TitleName_chem.length; i++) {
                    if (textlength <= TitleName_chem[i].getName().length()) {

                        if (TitleName_chem[i].getName().toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                            array_sort_chem.add(TitleName_chem[i]);
                        }
                    }
                }
                listview.setAdapter(new SpinAdapter(Rcpa_Call.this,R.layout.spin_row,array_sort_chem));
            }
        });

        myalertDialog=myDialog.show();
    }

    class DoctorData extends AsyncTask<ArrayList<SpinnerModel>,String,HashMap<String,ArrayList<SpinnerModel>>> {
        ProgressDialog pd;

        HashMap<String,ArrayList<SpinnerModel>> map = new HashMap<String, ArrayList<SpinnerModel>>();
        ArrayList<SpinnerModel>list1 = new ArrayList<SpinnerModel>();
        ArrayList<SpinnerModel>list2 = new ArrayList<SpinnerModel>();

        @Override
        protected HashMap<String,ArrayList<SpinnerModel>> doInBackground(ArrayList<SpinnerModel>... params) {

            docList=new ArrayList<SpinnerModel>();
            Cursor c=cbohelp.getDoctorListLocal();
            // chemist.add(new SpinnerModel("--Select--",""));
            if(c.moveToFirst())
            {
                do
                {
                    docList.add(new SpinnerModel(c.getString(c.getColumnIndex("dr_name")),c.getString(c.getColumnIndex("dr_id"))));
                }while(c.moveToNext());

            }


            chemist=new ArrayList<SpinnerModel>();
                c=cbohelp.getChemistListLocal();
            // chemist.add(new SpinnerModel("--Select--",""));
            if(c.moveToFirst())
            {
                do{
                    //data1.add(c.getString(c.getColumnIndex("chem_name"))+"\n\n\n"+c.getString(c.getColumnIndex("chem_id")));
                    chemist.add(new SpinnerModel(c.getString(c.getColumnIndex("chem_name")), c.getString(c.getColumnIndex("chem_id"))));
                }while(c.moveToNext());
            }

         map.put("docList",docList);
         map.put("chemistList",chemist);


            return map;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Rcpa_Call.this);
            pd.setTitle("CBO");
            pd.setMessage("Processing......."+"\n"+"please wait");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(HashMap<String,ArrayList<SpinnerModel>> s) {
            super.onPostExecute(s);

            list1=  s.get("docList");
            list2=  s.get("chemistList");


            try {
                TitleName = new SpinnerModel[list1.size()];
                for (int i = 0; i <= list1.size(); i++) {
                    TitleName[i] = list1.get(i);
                }

                array_sort = new ArrayList<SpinnerModel>(Arrays.asList(TitleName));
                //drname.setText("---Select---");
            }catch(Exception e){
                e.printStackTrace();
            }

            try {

                TitleName_chem = new SpinnerModel[list2.size()];

                for (int i = 0; i <= list2.size(); i++) {
                    TitleName_chem[i] = list2.get(i);
                }

                array_sort_chem = new ArrayList<SpinnerModel>(Arrays.asList(TitleName_chem));

            }catch (Exception e){

            }

            pd.dismiss();

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

