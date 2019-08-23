package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.Controls;
import com.cbo.cbomobilereporting.ui_new.AttachImage;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.uenics.javed.CBOLibrary.CboProgressDialog;

import java.io.File;
import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.cboUtils.CBOOtherExpense;
import utils_new.cboUtils.CBOPOB;
import utils_new.cboUtils.CBOStation;
import utils_new.cboUtils.DASpinner;
import utils_new.interfaces.RecycleViewOnItemClickListener;
import utils_new.up_down_ftp;

public class Expense extends CustomActivity implements IExpense,aExpense.Expense_interface,up_down_ftp.AdapterCallback{


    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    private final int REQUEST_CAMERA=201;
    TableLayout DA_layout;

    EditText daAmt,da_root, distAmt;
    Button save_exp;
    TextView datype,distAmt1, distanse, attach_txt,routeStausTxt;
    LinearLayout actual_fare_layout,actual_DA_layout,manual_DA_Type_layout,manual_Distance_layout;
    ImageView attachnew;

    Button btn_DaType,btn_Distance;
    ImageView DaType_img,Distance_img;

    vmExpense viewModel;
    TextView title,subTitle,station,final_remark,total_exp;
    private static final int OTHER_EXPENSE = 10;
    CboProgressDialog cboProgressDialog = null;
    AlertDialog myalertDialog = null;
    CBOOtherExpense OthExp,TAExp,DAExp;
    //CBOStation ManualStation;
    LinearLayout ManualStation,final_remark_layout;
    //DASpinner manual_DA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        viewModel = ViewModelProviders.of(this).get(vmExpense.class);
        if (getIntent().getStringExtra("FinalSubmit") != null){
            viewModel.setForFinalSubmit(getIntent().getStringExtra("FinalSubmit"));
        }
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
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        title  = toolbar.findViewById(R.id.title);
        total_exp = findViewById(R.id.total_exp);



        /*DA_ListView =  findViewById(R.id.list_exp_root);
        TA_ListView =  findViewById(R.id.list_exp_TA);*/

        actual_fare_layout = findViewById(R.id.actual_fare_layout);
        actual_DA_layout = findViewById(R.id.actual_DA_layout);
        //manual_DA = findViewById(R.id.manual_DA);
        manual_DA_Type_layout = findViewById(R.id.manual_DA_Type_layout);
        manual_Distance_layout = findViewById(R.id.manual_Distance_layout);

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

        DA_layout = findViewById(R.id.DA_layout);


        final_remark_layout = findViewById(R.id.final_remark_layout);
        final_remark = findViewById(R.id.final_remark);
        final_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setFinalRemark(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ManualStation = findViewById(R.id.ManualStation);
        station = findViewById(R.id.station);
        station.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.getExpense().setStation(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        OthExp = findViewById(R.id.OtherExp);
        OthExp.setContext(context,eExpense.None);
        OthExp.setListener(new CBOOtherExpense.Expense_interface() {
            @Override
            public void AddExpenseClicked(eExpense expense_type) {
                OnAddExpense(viewModel.getExpense(), new mOthExpense(),expense_type);
            }

            @Override
            public void Edit_ExpenseClicked(mOthExpense othExpense, eExpense exp_type) {
                Edit_Expense(othExpense,exp_type);
            }

            @Override
            public void delete_ExpenseClicked(mOthExpense othExpense, eExpense exp_type) {
                delete_Expense(othExpense,exp_type);
            }
        });


        TAExp = findViewById(R.id.TAExp);
        TAExp.setContext(context,eExpense.TA);
        TAExp.setListener(new CBOOtherExpense.Expense_interface() {
            @Override
            public void AddExpenseClicked(eExpense expense_type) {
                OnAddExpense(viewModel.getExpense(), new mOthExpense(),expense_type);
            }

            @Override
            public void Edit_ExpenseClicked(mOthExpense othExpense, eExpense exp_type) {
                Edit_Expense(othExpense,exp_type);
            }

            @Override
            public void delete_ExpenseClicked(mOthExpense othExpense, eExpense exp_type) {
                delete_Expense(othExpense,exp_type);
            }
        });

        DAExp = findViewById(R.id.DAExp);
        DAExp.setContext(context,eExpense.DA);
        DAExp.setListener(new CBOOtherExpense.Expense_interface() {
            @Override
            public void AddExpenseClicked(eExpense expense_type) {
                OnAddExpense(viewModel.getExpense(), new mOthExpense(),expense_type);
            }

            @Override
            public void Edit_ExpenseClicked(mOthExpense othExpense, eExpense exp_type) {
                Edit_Expense(othExpense,exp_type);
            }

            @Override
            public void delete_ExpenseClicked(mOthExpense othExpense, eExpense exp_type) {
                delete_Expense(othExpense,exp_type);
            }
        });


        DaType_img = findViewById(R.id.DaType_img);
        DaType_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickManualDaType();
            }
        });

        btn_DaType = findViewById(R.id.btn_DaType);
        btn_DaType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickManualDaType();
            }
        });


        Distance_img = findViewById(R.id.Distance_img);
        Distance_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickManualDistance();
            }
        });

        btn_Distance = findViewById(R.id.btn_Distance);
        btn_Distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickManualDistance();
            }
        });


       /* attachnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filenameTemp = Custom_Variables_And_Method.PA_ID+"_"+ Custom_Variables_And_Method.DCR_ID+"_exp_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
                //choosePhoto = new ChoosePhoto(context, REQUEST_CAMERA, ChoosePhoto.ChooseFrom.all);
                Intent intent = new Intent(context, AttachImage.class);
                intent.putExtra("Output_FileName",filenameTemp);
                intent.putExtra("SelectFrom", AttachImage.ChooseFrom.all);
                startActivityForResult(intent,REQUEST_CAMERA);
            }
        });

        da_root.addTextChangedListener(new TextWatcher() {
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
                if (viewModel.getExpense().getACTUALFARE_MAXAMT()>0){
                    if ((distAmt.getText().toString().trim().isEmpty() ? 0D :Double.parseDouble( distAmt.getText().toString())) > viewModel.getExpense().getACTUALFARE_MAXAMT()){
                        AppAlert.getInstance().Alert(context, "Alert!!!",
                                "Your AcutaFare cannot exceed " + AddToCartView.toCurrency(String.format("%.2f", (viewModel.getExpense().getACTUALFARE_MAXAMT()))), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        viewModel.getExpense().setTA_Amt(viewModel.getExpense().getACTUALFARE_MAXAMT());
                                    }
                                });
                    }else{
                        viewModel.getExpense().setTA_Amt(s.toString().trim().isEmpty() ? 0D: Double.parseDouble(s.toString()));
                    }
                }else{
                    viewModel.getExpense().setTA_Amt(s.toString().trim().isEmpty() ? 0D: Double.parseDouble(s.toString()));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/



        save_exp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if ( actual_fare_layout.getVisibility()==View.VISIBLE && distAmt.getText().toString().equals("")
                        && !viewModel.getExpense().getACTUALFAREYN_MANDATORY().equalsIgnoreCase("N")) {
                    customVariablesAndMethod.msgBox(context,"Please Enter the Actual Fare....");
                }else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"EXP_ATCH_YN","N").equals("Y")
                        &&  actual_fare_layout.getVisibility()==View.VISIBLE
                        && attach_txt.getText().toString().equals("* Attach Picture....")) {
                    customVariablesAndMethod.msgBox(context,"Please Attach supporting File for Actual Fare....");
                }else if(viewModel.getExpense().getMANUAL_TAYN_STATION().equalsIgnoreCase("1") &&
                            viewModel.getExpense().getStation().isEmpty()){

                    AppAlert.getInstance().Alert(context, "Working Station!!!", "Please enter your Working Station ...",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });


                } else if(viewModel.getExpense().getTA_TYPE_MANUALYN().equalsIgnoreCase("1")
                    && viewModel.getExpense().getMANUAL_TAYN_MANDATORY().equalsIgnoreCase("1")
                    && TAExp.getDataList().size()==0){

                    AppAlert.getInstance().Alert(context, "Add TA !!", "Please select atleast one TA head...",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TAExp.onClickListener();
                                }
                            });

                }/*else if(cbohelp.get_DA_ACTION_exp_head().size()>0  && actual_DA_layout.getVisibility() == View.VISIBLE
                        && !da_root.getText().toString().isEmpty() && !da_root.getText().toString().equals("0")){
                    AppAlert.getInstance().Alert(context, "Already Applied for DA...",
                            "Please make DA amount Rs 0.", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                }*/else if(viewModel.IsFormForFinalSubmit()  && viewModel.getFinalRemark().isEmpty()){
                    AppAlert.getInstance().Alert(context, "DCR Remark !!", "Please enter DCR remark...",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final_remark.requestFocus();
                                }
                            });
                }
                else if ( actual_fare_layout.getVisibility()==View.VISIBLE ){


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



    private void onClickManualDaType() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        final RecyclerView itemlist_filter = new RecyclerView(context);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(itemlist_filter);
        myDialog.setView(layout);
        aDA arrayAdapter = new aDA(context, viewModel.getExpense().getDAs());

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(arrayAdapter);
        arrayAdapter.setOnClickListner(new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                mDA da = viewModel.getExpense().getDAs().get(position);
                viewModel.setSelectedDA(da);


                /*btn_DaType.setText(da.getName());

                if (da.getCode().equalsIgnoreCase("L")) {
                    ManualDistanceReqd(false);
                } else {
                    ManualDistanceReqd(true);
                }*/

                /*viewModel.getExpense().setDistance(new mDistance());
                viewModel.getExpense().setDA_TYPE(da.getCode());
                viewModel.getExpense().setTA_Amt(da.getTAAmount());
                viewModel.getExpense().setDA_Amt(da.getDAAmount());

                updateDAView();*/
                myalertDialog.dismiss();
            }
        });


        myalertDialog = myDialog.show();
    }


    private void onClickManualDistance() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        final RecyclerView itemlist_filter = new RecyclerView(context);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(itemlist_filter);
        myDialog.setView(layout);
        aDistance arrayAdapter = new aDistance(context, viewModel.getExpense().getDistances());

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(arrayAdapter);
        arrayAdapter.setOnClickListner(new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                mDistance distance = viewModel.getExpense().getDistances().get(position);
                viewModel.setSelectedDistance(distance);
               /* btn_Distance.setText(distance.getName());

                viewModel.getExpense().setDistance(distance);
                viewModel.getExpense().getSelectedDA().setTA_Km(distance.getKm());
                viewModel.getExpense().setTA_Amt(viewModel.getExpense().getSelectedDA().getTAAmount());
                updateDAView();*/
                myalertDialog.dismiss();
            }
        });


        myalertDialog = myDialog.show();
    }




    private void init_DA_type(TableLayout stk) {

        stk.removeAllViews();

        Double other = 0D;
        //datanum.put("amount", c.getString(c.getColumnIndex("amount")));
        for (int i = 0; i <viewModel.getExpense().getOthExpenses().size();i++){
            other+= viewModel.getExpense().getOthExpenses().get(i).getAmount();
            if (viewModel.getExpense().getOthExpenses().get(i).getExpHead().getDA_ACTION() == 1){
                viewModel.getExpense().setDA_Amt(0D);
                setDA("");
            }
        }


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

        Double Dis_val =0D;
        if (!(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "ACTUALFAREYN","").equalsIgnoreCase("Y")
            || viewModel.getExpense().getTA_TYPE_MANUALYN().equalsIgnoreCase("1"))) {
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
            Dis_val = Double.parseDouble(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "distance_val","0"));
        }else if (viewModel.getExpense().getTA_TYPE_MANUALYN().equalsIgnoreCase("1")){
            Dis_val =0D;
            for (int i = 0; i <viewModel.getExpense().getTA_Expenses().size();i++){
                Dis_val+= viewModel.getExpense().getTA_Expenses().get(i).getAmount();
            }

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
            tv22.setText(context.getResources().getString(R.string.rs) + " " + Dis_val);
            tv22.setGravity(Gravity.RIGHT);
            tv22.setPadding(5, 5, 5, 0);
            tv22.setTextColor(Color.BLACK);
            tv22.setTypeface(null, Typeface.NORMAL);
            tbrow2.addView(tv22);
            stk.addView(tbrow2);



            //Dis_val = Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "distance_val","0"));
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



        /*for (int i = 0; i <viewModel.getExpense().getTA_Expenses().size();i++){
            other+= viewModel.getExpense().getTA_Expenses().get(i).getAmount();
        }*/

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


        TableRow tbrow5 = new TableRow(context);
        tbrow5.setPadding(2,2,2,2);
        tbrow5.setBackgroundColor(0xff125688);
        stk.addView(tbrow5);

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

        TableRow tbrow6 = new TableRow(context);
        tbrow6.setPadding(2,2,2,2);
        tbrow6.setBackgroundColor(0xff125688);
        stk.addView(tbrow6);

    }


    @Override
    public String getActivityTitle() {
        if (viewModel.IsFormForFinalSubmit()){
           return "Final Submit";
        }
        return "Expense";
    }

    @Override
    public void setTitle(String header) {
        title.setText(header);
    }

    @Override
    public boolean IsRouteWise() {
        return Controls.getInstance().IsRouteWise();
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
    public void setManualDA(mDA da) {
        //manual_DA.setDAs(viewModel.getExpense().getDAs());
        if (!da.getCode().isEmpty()) {
            btn_DaType.setText(da.getName());
            DAExp.setOtherDetail(da.getName());
            DAExp.setDefaultAmount(viewModel.getExpense().getDA_Amt());

            //manual_DA.set
        }
    }

    @Override
    public void setManualTA(mDistance distance) {
        btn_Distance.setText(distance.getName());
        //DAExp.setOtherDetail(da.getName());
        TAExp.setDefaultAmount(viewModel.getExpense().getTA_Amt());
    }

    @Override
    public void setDAType(String type) {
        datype.setText(type);
    }

    @Override
    public void setTADetail(String detail) {
        TAExp.setOtherDetail(detail);
        TAExp.setDefaultAmount(viewModel.getExpense().getTA_Amt());
    }

    @Override
    public void updateDAView() {
        //init_DA_type(DA_layout);
        total_exp.setText(AddToCartView.toCurrency(String.format("%.2f",DAExp.getAmount() + TAExp.getAmount() + OthExp.getAmount() )));
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

        //DAExp.setAddBtnReqd(required);

        if (required){
            actual_DA_layout.setVisibility(View.VISIBLE);
        }else{
            actual_DA_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void ManualDA_TypeReqd(Boolean required) {
        if (required){
            manual_DA_Type_layout.setVisibility(View.VISIBLE);
        }else{
            manual_DA_Type_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void ManualDAReqd(Boolean required) {
        DAExp.setAddBtnReqd(required);
    }

    @Override
    public void ManualTAReqd(Boolean required) {

        TAExp.setAddBtnReqd(required);

        /*if (required){
            TAExp.setVisibility(View.VISIBLE);
        }else{
            TAExp.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void ManualStationReqd(Boolean required) {
        if (required){
            ManualStation.setVisibility(View.VISIBLE);
        }else{
            ManualStation.setVisibility(View.GONE);
        }
    }

    @Override
    public void ManualDistanceReqd(Boolean required) {

        if (required){
            manual_Distance_layout.setVisibility(View.VISIBLE);
            if (viewModel.getExpense().getSelectedDA().getCode().equalsIgnoreCase("L")) {
                manual_Distance_layout.setVisibility(View.GONE);
            } else {
                manual_Distance_layout.setVisibility(View.VISIBLE);
            }
        }else{
            manual_Distance_layout.setVisibility(View.GONE);
        }

    }

    @Override
    public void ActualFareReqd(Boolean required) {
        if(required){
            distAmt.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"ACTUALFARE",""));
            actual_fare_layout.setVisibility(View.VISIBLE);
        }else{
            actual_fare_layout.setVisibility(View.GONE);
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ACTUALFARE","");
            //distAmt.setText("");
        }
    }

    @Override
    public void OnAddExpense(mExpense expense, mOthExpense othExpense, eExpense expense_type) {
        Intent intent = new Intent (context, OtherExpense.class);
        intent.putExtra ("expense", expense);
        intent.putExtra ("othExpense", othExpense);
        intent.putExtra ("eExpense", expense_type);
        startActivityForResult (intent, OTHER_EXPENSE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case OTHER_EXPENSE:
                    mOthExpense othExpense = (mOthExpense) data.getSerializableExtra ("othExpense");
                    if ((eExpense) data.getSerializableExtra ("eExpense") == eExpense.TA){
                        OnTAExpenseAdded(othExpense);
                    }else {
                        OnOtherExpenseAdded(othExpense);
                    }
                    break;
                case REQUEST_CAMERA :
                    File OutputFile = (File) data.getSerializableExtra("Output");
                    viewModel.getExpense().setAttachment(OutputFile.getName());
                    attach_txt.setText(OutputFile.getName());
                    //previewCapturedImage(OutputFile.getPath());
                    break;
                default:

            }
        }
    }


    @Override
    public void OnOtherExpenseAdded(mOthExpense othExpense) {
        viewModel.UpdateExpense(othExpense);
    }

    @Override
    public void OnOtherExpenseUpdated(ArrayList<mOthExpense> othExpenses) {

        OthExp.updateDataList(othExpenses);
    }

    @Override
    public void OnTAExpenseAdded(mOthExpense othExpense) {
        viewModel.UpdateExpense(othExpense);
    }

    @Override
    public void OnTAExpenseUpdated(ArrayList<mOthExpense> othExpenses) {
        TAExp.updateDataList(othExpenses);
    }

    @Override
    public void OnDAExpenseAdded(mOthExpense DAExpense) {
        viewModel.UpdateExpense(DAExpense);
    }

    @Override
    public void OnDAExpenseUpdated(ArrayList<mOthExpense> DAExpenses) {
        DAExp.updateDataList(DAExpenses);
    }

    @Override
    public void OnFinalRemarkReqd(Boolean required) {
        if (required){
            final_remark_layout.setVisibility(View.VISIBLE);
        }else{
            final_remark_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnExpenseCommit() {

    }

    @Override
    public void OnExpenseCommited() {

    }

    @Override
    public void Edit_Expense(mOthExpense othExpense, eExpense exp_type) {
        OnAddExpense(viewModel.getExpense(),othExpense,exp_type);
    }

    @Override
    public void delete_Expense(mOthExpense othExpense, eExpense exp_type) {
        AppAlert.getInstance().DecisionAlert(context, "Delete!!!",
                "Are you sure to delete\n"+ othExpense.getExpHead().getName() +"?",
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
