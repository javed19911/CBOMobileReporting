package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.ExpenseRoot;
import com.uenics.javed.CBOLibrary.CboProgressDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import services.ServiceHandler;
import utils.adapterutils.Expenses_Adapter;
import utils.adapterutils.SpinAdapter;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.up_down_ftp;

public class Expense extends CustomActivity implements IExpense,aExpense.Expense_interface,up_down_ftp.AdapterCallback{


    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    private final int REQUEST_CAMERA=201;
    String picturePath="";
    private File output=null;
    String filename="";
    TableLayout DA_layout;

    EditText daAmt,da_root, distAmt;
    Button save_exp,add_exp;
    ListView mylist;
    TextView datype,distAmt1, distanse, attach_txt,routeStausTxt;
    aExpense sm;
    LinearLayout actual_fare_layout,actual_DA_layout;
    ImageView attachnew;

    vmExpense viewModel;
    TextView title,subTitle;
    private static final int OTHER_EXPENSE = 10;
    CboProgressDialog cboProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        viewModel = ViewModelProviders.of(this).get(vmExpense.class);
        viewModel.setView(context,this);
    }

    @Override
    public String getCompanyCode() {
        return MyCustumApplication.getInstance().getUser().getCompanyCode();
    }

    @Override
    public String getDCRId() {
        return MyCustumApplication.getInstance().getUser().getDCRId();
    }


    @Override
    public String getUserId() {
        return MyCustumApplication.getInstance().getUser().getID();
    }

    @Override
    public void getReferencesById() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        title  = toolbar.findViewById(R.id.title);

        mylist = (ListView) findViewById(R.id.list_exp_root);

        actual_fare_layout = findViewById(R.id.actual_fare_layout);
        actual_DA_layout = findViewById(R.id.actual_DA_layout);

        datype = (TextView) findViewById(R.id.da_type_root);
        distanse = (TextView) findViewById(R.id.da_distance_root);

        daAmt = (EditText) findViewById(R.id.ex_da_root);
        da_root = (EditText) findViewById(R.id.da_root);
        distAmt = (EditText) findViewById(R.id.ex_dis_root);
        distAmt1 = (TextView) findViewById(R.id.ex_dis_root1);
        save_exp = (Button) findViewById(R.id.save_back1_root);


        attachnew = findViewById(R.id.attachnew);
        attach_txt = findViewById(R.id.attach_txt);
        routeStausTxt = findViewById(R.id.ROUTE_CLASS);

        add_exp = findViewById(R.id.add_exp);
        DA_layout = findViewById(R.id.DA_layout);


        add_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnAddExpense(viewModel.getExpense(), new mOthExpense());

            }
        });

        daAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.getExpense().setDA_Amt(s.toString().trim().isEmpty() ? 0D: Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        distAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.getExpense().setTA_Amt(s.toString().trim().isEmpty() ? 0D: Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        save_exp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if ( actual_fare_layout.getVisibility()==View.VISIBLE && distAmt.getText().toString().equals("")) {
                    customVariablesAndMethod.msgBox(context,"Please Enter the Actual Fare....");
                }else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"EXP_ATCH_YN","N").equals("Y") &&  actual_fare_layout.getVisibility()==View.VISIBLE && attach_txt.getText().toString().equals("* Attach Picture....")) {
                    customVariablesAndMethod.msgBox(context,"Please Attach supporting File for Actual Fare....");
                }/*else if(cbohelp.get_DA_ACTION_exp_head().size()>0  && actual_DA_layout.getVisibility() == View.VISIBLE
                        && !da_root.getText().toString().isEmpty() && !da_root.getText().toString().equals("0")){
                    AppAlert.getInstance().Alert(context, "Already Applied for DA...",
                            "Please make DA amount Rs 0.", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                }*/else if ( actual_fare_layout.getVisibility()==View.VISIBLE ){


                    if(!attach_txt.getText().toString().equals("* Attach Picture....")) {
                        cboProgressDialog = new CboProgressDialog(context, "Please Wait..\nuploading Image");
                        cboProgressDialog.show();
                        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator +  viewModel.getExpense().getAttachment());
                        new up_down_ftp().uploadFile(file2,context);
                    }else{
                        viewModel.expense_commit_attachment(context);
                    }




                }else {
                  viewModel.expense_commit(context);

                }
            }
        });
    }

    private void init_DA_type(TableLayout stk) {

        stk.removeAllViews();
        TableRow tbrow0 = new TableRow(context);
        //tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText("DA. Type");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.BLACK);
        tv0.setTypeface(null, Typeface.NORMAL);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "DA_TYPE"));
        tv1.setGravity(Gravity.RIGHT);
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(null, Typeface.NORMAL);
        tbrow0.addView(tv1);
        stk.addView(tbrow0);

        TableRow tbrow1 = new TableRow(context);
        //tbrow1.setBackgroundColor(0xff125688);
        TextView tv10 = new TextView(context);
        tv10.setText("DA. Value");
        tv10.setPadding(5, 5, 5, 0);
        tv10.setTextColor(Color.BLACK);
        tv10.setTypeface(null, Typeface.NORMAL);
        tv10.setLayoutParams(params);
        tbrow1.addView(tv10);
        TextView tv11 = new TextView(context);
        tv11.setText(context.getResources().getString(R.string.rs) + " " + customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "da_val","0"));
        tv11.setPadding(5, 5, 5, 0);
        tv11.setTextColor(Color.BLACK);
        tv11.setGravity(Gravity.RIGHT);
        tv11.setTypeface(null, Typeface.NORMAL);
        tbrow1.addView(tv11);
        stk.addView(tbrow1);

        Float Dis_val =0f;
        if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "ACTUALFAREYN","").equalsIgnoreCase("Y")) {
            TableRow tbrow2 = new TableRow(context);
            //tbrow2.setBackgroundColor(0xff125688);
            TextView tv21 = new TextView(context);
            tv21.setText("TA. Value");
            tv21.setPadding(5, 5, 5, 0);
            tv21.setTextColor(Color.BLACK);
            tv21.setTypeface(null, Typeface.NORMAL);
            tv21.setLayoutParams(params);
            tbrow2.addView(tv21);
            TextView tv22 = new TextView(context);
            tv22.setText(context.getResources().getString(R.string.rs) + " " + customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "distance_val", "0"));
            tv22.setGravity(Gravity.RIGHT);
            tv22.setPadding(5, 5, 5, 0);
            tv22.setTextColor(Color.BLACK);
            tv22.setTypeface(null, Typeface.NORMAL);
            tbrow2.addView(tv22);
            stk.addView(tbrow2);
            Dis_val = Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "distance_val","0"));
        }

        TableRow tbrow4 = new TableRow(context);
        //tbrow4.setBackgroundColor(0xff125688);
        TextView tv40 = new TextView(context);
        tv40.setText("Other Value");
        tv40.setPadding(5, 5, 5, 0);
        tv40.setTextColor(Color.BLACK);
        tv40.setTypeface(null, Typeface.NORMAL);
        tv40.setLayoutParams(params);
        tbrow4.addView(tv40);
        TextView tv41 = new TextView(context);


        Double other = 0D;
        //datanum.put("amount", c.getString(c.getColumnIndex("amount")));
        for (int i = 0; i <viewModel.getExpense().getOthExpenses().size();i++){
            other+= viewModel.getExpense().getOthExpenses().get(i).getAmount();
        }

        tv41.setText(context.getResources().getString(R.string.rs)+" "+other);
        tv41.setPadding(5, 5, 5, 0);
        tv41.setTextColor(Color.BLACK);
        tv41.setGravity(Gravity.RIGHT);
        tv41.setTypeface(null, Typeface.NORMAL);
        tbrow4.addView(tv41);
        stk.addView(tbrow4);

        Double net_value = Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "da_val","0"))
                + Dis_val
                + other;


        TableRow tbrow3 = new TableRow(context);
        //tbrow3.setBackgroundColor(0xff125688);
        TextView tv31 = new TextView(context);
        tv31.setText("Total Expenses");
        tv31.setPadding(5, 5, 5, 0);
        tv31.setTextColor(Color.BLACK);
        tv31.setTypeface(null, Typeface.BOLD);
        tv31.setLayoutParams(params);
        tbrow3.addView(tv31);
        TextView tv32 = new TextView(context);

        tv32.setText(context.getResources().getString(R.string.rs) + " " + net_value);

        tv32.setGravity(Gravity.RIGHT);
        tv32.setPadding(5, 5, 5, 0);
        tv32.setTextColor(Color.BLACK);
        tv32.setTypeface(null, Typeface.BOLD);
        tbrow3.addView(tv32);
        stk.addView(tbrow3);

        TableRow tbrow5 = new TableRow(context);
        tbrow5.setPadding(2,2,2,2);
        tbrow5.setBackgroundColor(0xff125688);
        stk.addView(tbrow5);

    }


    @Override
    public String getActivityTitle() {
        return "Expense";
    }

    @Override
    public void setTitle(String header) {
        title.setText(header);
    }

    @Override
    public void setRouteStatus(String Status) {
        routeStausTxt.setText(Status);
        if (Status.trim().isEmpty()){
            routeStausTxt.setVisibility(View.GONE);
        }else{
            routeStausTxt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setDA(String DA) {
        da_root.setText(DA);
    }

    @Override
    public void setDAType(String type) {
        datype.setText(type);
    }

    @Override
    public void updateDAView() {
        init_DA_type(DA_layout);
    }

    @Override
    public void setDistance(String Distance) {
        distanse.setText(Distance);
    }

    @Override
    public void enableDA(Boolean enable) {
        da_root.setEnabled(enable);
    }

    @Override
    public void ActualDAReqd(Boolean required) {
        if (required){
            actual_DA_layout.setVisibility(View.VISIBLE);
        }else{
            actual_DA_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void ActualFareReqd(Boolean required) {
        if(required){
            distAmt.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"ACTUALFARE",""));
            actual_fare_layout.setVisibility(View.VISIBLE);
        }else{
            actual_fare_layout.setVisibility(View.GONE);
            distAmt.setText("");
        }
    }

    @Override
    public void OnAddExpense(mExpense expense,mOthExpense othExpense) {
        Intent intent = new Intent (context, OtherExpense.class);
        intent.putExtra ("expense", expense);
        intent.putExtra ("othExpense", othExpense);
        startActivityForResult (intent, OTHER_EXPENSE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case OTHER_EXPENSE:
                    mOthExpense othExpense = (mOthExpense) data.getSerializableExtra ("othExpense");
                    OnOtherExpenseAdded(othExpense);
                    break;
                default:

            }
        }
    }

    @Override
    public void OnOtherExpenseUpdated(ArrayList<mOthExpense> othExpenses) {
        sm = new aExpense(context, othExpenses);
        mylist.setAdapter(sm);
    }

    @Override
    public void OnExpenseCommit() {

    }

    @Override
    public void OnExpenseCommited() {

    }

    @Override
    public void OnOtherExpenseAdded(mOthExpense othExpense) {
        viewModel.UpdateExpense(othExpense);
    }

    @Override
    public void Edit_Expense(mOthExpense othExpense) {
        OnAddExpense(viewModel.getExpense(),othExpense);
    }

    @Override
    public void delete_Expense(mOthExpense othExpense) {
        AppAlert.getInstance().DecisionAlert(context, "Delete!!!",
                "Are you sure, you want to delete?",
                new AppAlert.OnClickListener() {
                    @Override
                    public void onPositiveClicked(View item, String result) {
                        viewModel.deleteExpense(context,othExpense);
                    }

                    @Override
                    public void onNegativeClicked(View item, String result) {

                    }
                });
    }

    @Override
    public void started(Integer responseCode, String message, String description) {

    }

    @Override
    public void progess(Integer responseCode, Long FileSize, Float value, String description) {

    }

    @Override
    public void complete(Integer responseCode, String message, String description) {
        cboProgressDialog.dismiss();
        viewModel.expense_commit_attachment(context);
    }

    @Override
    public void aborted(Integer responseCode, String message, String description) {
        cboProgressDialog.dismiss();
        AppAlert.getInstance().getAlert(context,message,description);
    }

    @Override
    public void failed(Integer responseCode, String message, String description) {
        cboProgressDialog.dismiss();
        AppAlert.getInstance().getAlert(context,message,description);
    }
}
