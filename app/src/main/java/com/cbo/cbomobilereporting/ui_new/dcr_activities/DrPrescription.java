package com.cbo.cbomobilereporting.ui_new.dcr_activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import services.ServiceHandler;
import utils.adapterutils.DrPres_Adapter;
import utils.adapterutils.DrPres_Model;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by AKSHIT on 6/27/16.
 */
public class DrPrescription extends AppCompatActivity {

    ListView mylist;
    Button save;
    Custom_Variables_And_Method customVariablesAndMethod;
    int PA_ID = 0;
    ResultSet rs;
    CBO_DB_Helper cbohelp;
    String item_id = "", item_name = "";
    ArrayAdapter<DrPres_Model> adapter;
    List<DrPres_Model> list = new ArrayList<DrPres_Model>();
    ArrayList<String> data1;
    boolean check;
    Context context;
    SpinnerModel[] TitleName;
    ArrayList<SpinnerModel> docList;
    ArrayList<SpinnerModel> array_sort;
    private AlertDialog myalertDialog = null;
    String dr_id, mymonth;
    String doc_name;
    TextView drNameBt;
    ImageView drNmaeImg;
    int textlength = 0;
    StringBuilder sbId, sbQty,sbamt, sbQty_caption,sbamt_caption;
    ServiceHandler serviceHandler;
    static final int DATE_DIALOG_ID = 1;
    private String[] arrMonth = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    int hour, minute, mYear, mMonth, mDay;
    TextView mydate;
    String dateText;
    Boolean qtyInput = false;
    private HashMap<String, ArrayList<String>> tenivia_traker;
    CheckBox no_prescription;
    Calendar myCalendar;
    ImageView date_name_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dr_prescription);


        context = DrPrescription.this;
        save = (Button) findViewById(R.id.bt_save);
        mylist = (ListView) findViewById(R.id.dr_sample_list);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp = new CBO_DB_Helper(context);
        drNameBt = (TextView) findViewById(R.id.drpres_name);
        drNmaeImg = (ImageView) findViewById(R.id.drpres_name_img);
        serviceHandler = new ServiceHandler(context);
        mylist.setItemsCanFocus(true);
        drNameBt.setText("-- Select --");
        no_prescription= (CheckBox) findViewById(R.id.no_prescription);
        date_name_img= (ImageView) findViewById(R.id.date_name_img);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        mydate = (TextView) findViewById(R.id.date_prescribe);
        final Date date = new Date();
        mydate.setText(customVariablesAndMethod.convetDateddMMyyyy(date));
        mydate.setKeyListener(null);
        mydate.setCursorVisible(false);
        mydate.setPressed(false);
        mydate.setFocusable(false);

        if (getSupportActionBar() != null) {
            //textView.setText("Dr Prescription");
            String head=cbohelp.getMenu("DCR", "D_DR_RX").get("D_DR_RX");
            textView.setText(head);

            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        new Doback().execute();
        new DoctorData().execute();

        tenivia_traker=cbohelp.getCallDetail("tenivia_traker","","0");

         myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date_picker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };



        mydate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                final DatePickerDialog   mDatePicker =new DatePickerDialog(DrPrescription.this, date_picker,myCalendar.get(Calendar.YEAR),  myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                //mDatePicker.setTitle("Please select date");
                // TODO Hide Future Date Here
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                // TODO Hide Past Date Here
                //  mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                mDatePicker.show();

            }
        });
        date_name_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                final DatePickerDialog   mDatePicker =new DatePickerDialog(DrPrescription.this, date_picker,myCalendar.get(Calendar.YEAR),  myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                //mDatePicker.setTitle("Please select date");
                // TODO Hide Future Date Here
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                // TODO Hide Past Date Here
                //  mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                mDatePicker.show();

            }
        });


        no_prescription.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
                    final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
                    final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
                    final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
                    final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.nagative);
                    Alert_Nagative.setText("Cancel");
                    Alert_Positive.setText("OK");
                    Alert_title.setText("ALERT !!!");
                    Alert_message.setText("Are you sure to save as\n\"No prescription for the day\"");

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

                    final AlertDialog dialog = builder1.create();

                    dialog.setView(dialogLayout);
                    Alert_Positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Save_RX(1);
                            dialog.dismiss();
                        }
                    });
                    Alert_Nagative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_prescription.setChecked(false);
                            dialog.dismiss();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save_RX(0);
            }
        });


        drNameBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDrName();
            }
        });

        drNmaeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDrName();
            }
        });

    }

    private void updateLabel(){
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mydate.setText(sdf.format(myCalendar.getTime()));
    }

    private void Save_RX(int who){
        saveData(who);
        dateText = mydate.getText().toString();
        if (drNameBt.getText().toString().equals("-- Select --")) {
            customVariablesAndMethod.msgBox(context,"Please Select Name First...");

        } else {
            if (!dateText.equals("-- Date --")) {
                if (!tenivia_traker.isEmpty() && tenivia_traker.get("name").contains(doc_name)){
                    customVariablesAndMethod.getAlert(context,"Call Found","You have made your call to "+doc_name);
                }else if (qtyInput) {
                    new SaveTask().execute();
                } else {
                    customVariablesAndMethod.msgBox(context,"Please fill atlest One Qty...");
                }

            } else {

                customVariablesAndMethod.msgBox(context,"Please Select Date First...");
            }
        }
        // mycon.msgBox("" + sbId + sbQty);

    }

    public void getData() {
        adapter = new DrPres_Adapter(this, list);

        if (adapter.getCount() != 0) {
            mylist.setAdapter(adapter);
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(DrPrescription.this);
            builder1.setTitle("Empty List..");
            builder1.setMessage(" No Data In List..");
            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder1.show();
        }


    }


    class Doback extends AsyncTask<Integer, String, List<DrPres_Model>> {
        ProgressDialog pd;

        @Override
        protected List<DrPres_Model> doInBackground(Integer... params) {

            list.clear();

            final String METHOD_NAME = "DCRRX_ITEM";
            SoapObject soapObject = new SoapObject(ServiceHandler.NAMESPACE, METHOD_NAME);
            soapObject.addProperty("sCompanyFolder", cbohelp.getCompanyCode());
            soapObject.addProperty("iPA_ID", Custom_Variables_And_Method.PA_ID);

            String result = serviceHandler.customMethodForAllServices(soapObject, METHOD_NAME);

            if ((result != null) || (!result.contains("[ERROR]"))) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Tables0");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        list.add(new DrPres_Model(jsonObject1.getString("QTY_CAPTION"), jsonObject1.getString("ITEM_ID"), jsonObject1.getString("AMOUN_CAPTION")));
                    }


                } catch (JSONException js) {
                }


            }

            return list;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(DrPrescription.this);
            pd.setTitle("CBO");
            pd.setMessage("Processing......." + "\n" + "please wait");
            pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
            pd.show();
        }

        @Override
        protected void onPostExecute(List<DrPres_Model> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            getData();

            pd.dismiss();


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    class DoctorData extends AsyncTask<ArrayList<SpinnerModel>, String, ArrayList<SpinnerModel>> {
        ProgressDialog pd;

        @Override
        protected ArrayList<SpinnerModel> doInBackground(ArrayList<SpinnerModel>... params) {
            docList = new ArrayList<SpinnerModel>();
            try {
                final String METHOD_NAME = "DCRRXDR";
                SoapObject soapObject = new SoapObject(ServiceHandler.NAMESPACE, METHOD_NAME);
                soapObject.addProperty("sCompanyFolder", cbohelp.getCompanyCode());
                soapObject.addProperty("iPA_ID", Custom_Variables_And_Method.PA_ID);

                String result = serviceHandler.customMethodForAllServices(soapObject, METHOD_NAME);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("Tables0");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                    docList.add(new SpinnerModel(jsonObject1.getString("DR_NAME"), jsonObject1.getString("DR_ID")));
                }


            } catch (Exception e) {
            }
            return docList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setTitle("CBO");
            pd.setMessage("Processing......." + "\n" + "please wait");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<SpinnerModel> s) {
            super.onPostExecute(s);

            try {
                pd.dismiss();
                if ((!s.isEmpty()) || (s.size() < 0)) {
                    TitleName = new SpinnerModel[s.size()];
                    for (int i = 0; i <= s.size(); i++) {
                        TitleName[i] = s.get(i);


                    }

                    array_sort = new ArrayList<SpinnerModel>(Arrays.asList(TitleName));
                } else {
                    customVariablesAndMethod.getAlert(context,"Empty list","No Doctor Found...");


                    drNameBt.setClickable(false);
                    drNmaeImg.setClickable(false);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onClickDrName() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        final EditText editText = new EditText(context);
        final ListView listview = new ListView(context);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
        array_sort = new ArrayList<SpinnerModel>(Arrays.asList(TitleName));
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter = new SpinAdapter(context, R.layout.spin_row, array_sort);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();


                dr_id = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                doc_name = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
                drNameBt.setText(doc_name);

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
                array_sort.clear();
                for (int i = 0; i < TitleName.length; i++) {
                    if (textlength <= TitleName[i].getName().length()) {

                        if (TitleName[i].getName().toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                            array_sort.add(TitleName[i]);
                        }
                    }
                }
                try {
                    listview.setAdapter(new SpinAdapter(context, R.layout.spin_row, array_sort));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        myalertDialog = myDialog.show();
    }

    ///////////////
    private void saveData(int who) {
        qtyInput = false;
        sbId = new StringBuilder();
        sbQty = new StringBuilder();
        sbamt = new StringBuilder();
        sbQty_caption = new StringBuilder();
        sbamt_caption = new StringBuilder();
        if (who==1){
            qtyInput = true;
            sbId.append("-1");
            sbQty.append("0");
            sbamt.append("0");
            sbQty_caption.append("");
            sbamt_caption.append("");
        }else {
            for (int i = 0; i < list.size(); i++) {

                String itemQty = list.get(i).getQty();
                String itemamt = list.get(i).getamt();
                String itemQty_caption = list.get(i).getName();
                String itemamt_caption = list.get(i).getName_amt();
                String itemId = list.get(i).getId();

                if (!itemQty.trim().isEmpty() && Integer.valueOf(itemQty) != 0 ){//&& !itemamt.trim().isEmpty()) {
                    qtyInput = true;
                    if (sbId.length() == 0) {
                        sbId.append(itemId);
                        sbQty.append(itemQty);
                        sbamt.append(itemamt);
                        sbQty_caption.append(itemQty_caption);
                        sbamt_caption.append(itemamt_caption);
                    } else {
                        sbId.append(",").append(itemId);
                        sbQty.append(",").append(itemQty);
                        sbamt.append(",").append(itemamt);
                        sbQty_caption.append(",").append(itemQty_caption);
                        sbamt_caption.append(",").append(itemamt_caption);
                    }
                }
            }
        }

    }

    private class SaveTask extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(context);
            pd.setTitle("CBO");
            pd.setMessage("Processing");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);

            pd.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if ((!s.contains("ERROR")) || (!s.equals(""))) {
                String remark="";
                if (sbId.toString().equals("-1")){
                    remark="No prescription for the day";
                }
                cbohelp.Insert_tenivia_traker(dr_id, doc_name, sbQty.toString()
                        ,  sbamt.toString(), sbQty_caption.toString(), sbId.toString(), sbamt_caption.toString(),  customVariablesAndMethod.currentTime(context), remark);
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"D_DR_RX_VISITED","Y");
                customVariablesAndMethod.msgBox(context,"Successfuly Submitted...");
                finish();
            } else {

                customVariablesAndMethod.msgBox(context,"Error Occured...");
            }

            pd.dismiss();

        }

        @Override
        protected String doInBackground(String... params) {

            final String METHOD_NAME = "DCRRX_COMMIT_1";
            SoapObject soapObject = new SoapObject(ServiceHandler.NAMESPACE, METHOD_NAME);
            soapObject.addProperty("sCompanyFolder", cbohelp.getCompanyCode());
            soapObject.addProperty("iPA_ID", Custom_Variables_And_Method.PA_ID);
            soapObject.addProperty("DOC_DATE", customVariablesAndMethod.currentDate());
            soapObject.addProperty("iDCRID", Custom_Variables_And_Method.DCR_ID);
            soapObject.addProperty("iDR_ID", "" + dr_id);
            soapObject.addProperty("sITEMID", sbId.toString());
            soapObject.addProperty("sQTY", sbQty.toString());
            soapObject.addProperty("sAMOUNT", sbamt.toString());

            String result = serviceHandler.customMethodForAllServices(soapObject, METHOD_NAME);

            return result;
        }
    }


}
