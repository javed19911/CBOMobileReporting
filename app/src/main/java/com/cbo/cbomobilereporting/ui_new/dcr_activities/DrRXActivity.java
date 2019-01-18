package com.cbo.cbomobilereporting.ui_new.dcr_activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.mMissedFilter;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

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

import services.MyAPIService;
import services.ServiceHandler;
import utils.CBOUtils.SystemArchitecture;
import utils.adapterutils.DrPres_Adapter;
import utils.adapterutils.DrPres_Model;
import utils.adapterutils.GiftModel;
import utils.adapterutils.MyAdapter2;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils.model.DropDownModel;
import utils_new.AppAlert;
import utils_new.CustomDialog.Spinner_Dialog;
import utils_new.Custom_Variables_And_Method;

public class DrRXActivity extends AppCompatActivity {

    ListView mylist;
    Button save;
    Custom_Variables_And_Method customVariablesAndMethod;
    int PA_ID = 0;
    ResultSet rs;
    CBO_DB_Helper cbohelp;
    DrPres_Adapter adapter;
    List<DrPres_Model> list = new ArrayList<DrPres_Model>();
    Context context;
    ArrayList<SpinnerModel> docList;
    String dr_id, mymonth;
    String doc_name;
    TextView drNameBt;
    ImageView drNmaeImg;
    int textlength = 0;
    StringBuilder sbId, sbQty,sbamt, sbQty_caption,sbamt_caption;
    ServiceHandler serviceHandler;
    int hour, minute, mYear, mMonth, mDay;
    TextView mydate;
    Boolean qtyInput = false;
    CheckBox no_prescription;
    ImageView date_name_img;
    private HashMap<String, ArrayList<String>> tenivia_traker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_rx);


        context = this;
        save = (Button) findViewById(R.id.bt_save);
        mylist = (ListView) findViewById(R.id.dr_sample_list);
        customVariablesAndMethod= Custom_Variables_And_Method.getInstance();
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
            String head=cbohelp.getMenu("DCR", "D_RX_GEN").get("D_RX_GEN");
            textView.setText(head);

            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        getDoctorList();
        new Doback().execute();

        tenivia_traker=cbohelp.getCallDetail("tenivia_traker","","0");





        no_prescription.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    AppAlert.getInstance().DecisionAlert(context, "ALERT !!!",
                            "Are you sure to save as\n\"No prescription for the day\"",
                            new AppAlert.OnClickListener() {
                                @Override
                                public void onPositiveClicked(View item, String result) {
                                    Save_RX(1);
                                }

                                @Override
                                public void onNegativeClicked(View item, String result) {
                                    no_prescription.setChecked(false);
                                }
                            });
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


    private void Save_RX(int who){
        saveData(who);
        if (drNameBt.getText().toString().equals("-- Select --")) {
            customVariablesAndMethod.msgBox(context,"Please Select Name First...");
        } else if (!tenivia_traker.isEmpty() && tenivia_traker.get("name").contains(doc_name)){
            customVariablesAndMethod.getAlert(context,"Call Found","You have made your call to "+doc_name);
        }else if (qtyInput) {
            SaveTask();
        } else {
            customVariablesAndMethod.msgBox(context,"Please fill atlest One Qty...");
        }


    }

    public void getData() {
        adapter = new DrPres_Adapter(this, list);

        if (adapter.getCount() != 0) {
            mylist.setAdapter(adapter);
        } else {
            AppAlert.getInstance().getAlert(context,"Empty List.."," No Data In List..");
        }
    }


    class Doback extends AsyncTask<Integer, String, List<DrPres_Model>> {
        ProgressDialog pd;

        @Override
        protected List<DrPres_Model> doInBackground(Integer... params) {

            list.clear();

            Cursor c = cbohelp.getAllProducts("0");
            if (c.moveToFirst()) {
                do {

                    list.add(new DrPres_Model(c.getString(c.getColumnIndex("item_name")),
                            c.getString(c.getColumnIndex("item_id")),
                            ""));
                   /* list.add(new DrPres_Model(c.getString(c.getColumnIndex("item_name")), c.getString(c.getColumnIndex("item_id")), "",
                                c.getInt(c.getColumnIndex("STOCK_QTY")), c.getInt(c.getColumnIndex("BALANCE")),c.getInt(c.getColumnIndex("SPL_ID"))));
*/

                } while (c.moveToNext());
            }


            return list;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(context);
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


     public void getDoctorList() {
         docList = new ArrayList<SpinnerModel>();
         Cursor c = cbohelp.getDoctorName();
         docList.add(new SpinnerModel("--Select--", "0"));
         if (c.moveToFirst()) {
             do {
                 docList.add(new SpinnerModel(c.getString(c.getColumnIndex("dr_name")), c.getString(c.getColumnIndex("dr_id"))));

             } while (c.moveToNext());
         }
         cbohelp.close();
    }


    private void onClickDrName() {
        new Spinner_Dialog(context, docList, item -> {
            dr_id = item.getId();
            doc_name = item.getName();
            drNameBt.setText(doc_name);
        }).show();
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
            dr_id = "-1";
            drNameBt.setText("No Prescription");
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

    private void SaveTask() {

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cbohelp.getCompanyCode());
        request.put("iPA_ID", ""+Custom_Variables_And_Method.PA_ID);
        request.put("DOC_DATE", customVariablesAndMethod.currentDate());
        request.put("iDCRID", Custom_Variables_And_Method.DCR_ID);
        request.put("iDR_ID", "" + dr_id);
        request.put("sITEMID", sbId.toString());
        request.put("sQTY", sbQty.toString());
        request.put("sAMOUNT", sbamt.toString());
         new MyAPIService(context)
                .execute(new ResponseBuilder("DCRRX_COMMIT_1", request)
                        .setDescription("Please Wait..").setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) {

                                String remark="";
                                if (sbId.toString().equals("-1")){
                                    remark="No prescription for the day";
                                }
                                cbohelp.Insert_tenivia_traker(dr_id, doc_name, sbQty.toString()
                                        ,  sbamt.toString(), sbQty_caption.toString(), sbId.toString(), sbamt_caption.toString(),  customVariablesAndMethod.currentTime(context), remark);
                                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"D_DR_RX_VISITED","Y");
                                customVariablesAndMethod.msgBox(context,"Successfuly Submitted...");
                                finish();
                            }

                            @Override
                            public void onResponse(Bundle response) {

                            }

                            @Override
                            public void onError(String message, String description) {
                                AppAlert.getInstance().getAlert(context,message,description);
                            }
                        })
                );


    }
}