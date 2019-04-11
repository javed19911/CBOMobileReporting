package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.ExpenseRoot;
import com.uenics.javed.CBOLibrary.CboProgressDialog;

import java.io.File;
import java.util.ArrayList;

import utils.adapterutils.SpinAdapter;
import utils_new.AppAlert;
import utils_new.up_down_ftp;

public class OtherExpense extends CustomActivity implements IOtherExpense,up_down_ftp.AdapterCallback {

    Spinner exphead;
    EditText  exhAmt, rem_final;
    Button Add, Cancel;
    TextView  textRemark,head,ex_head_root_txt;
    ImageView attach_img;
    CheckBox add_attachment;
    RadioGroup attach_option;
    aExpenseHead adapter;
    Boolean keyPressed = true;



    vmOtherExpenses viewModel;
    CboProgressDialog cboProgressDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_expense);

        viewModel = ViewModelProviders.of(this).get(vmOtherExpenses.class);
        viewModel.setView(context,this);
        viewModel.setExpense((mExpense) getIntent().getSerializableExtra("expense"));
        viewModel.setOthExpense((mOthExpense) getIntent().getSerializableExtra("othExpense"));
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
        exphead = findViewById(R.id.exp_head_root);
        exhAmt = findViewById(R.id.ex_head_root);
        head = findViewById(R.id.head);
        textRemark = findViewById(R.id.text_remark);
        ex_head_root_txt = findViewById(R.id.ex_head_root_txt);
        rem_final = findViewById(R.id.exp_remark_root);
        add_attachment = findViewById(R.id.add_attachment);
        attach_option = findViewById(R.id.attach_option);
        attach_img= findViewById(R.id.attach_img);
        attach_img.setVisibility(View.GONE);
        //final String[] ext = {path};
        attach_option.setVisibility(View.GONE);

        Add = findViewById(R.id.save);
        Cancel = findViewById(R.id.cancel);

        exphead.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
                viewModel.setExpenseHead(viewModel.getExpense().getExpHeads().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



        rem_final.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.getOthExpense().setRemark(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        exhAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Double amt = s.toString().trim().isEmpty() ? 0D: Double.parseDouble(s.toString());
                Double maxAmt =  viewModel.getOthExpense().getExpHead().getMAX_AMT();
                if (maxAmt ==0 ){
                    maxAmt = amt;
                }
                if (keyPressed) {
                    keyPressed = false;
                    if (amt <= maxAmt) {
                        viewModel.getOthExpense().setAmount(amt);
                    } else {
                        Double finalMaxAmt = maxAmt;
                        viewModel.getOthExpense().setAmount(maxAmt);
                        AppAlert.getInstance().Alert(context, "Alert!!!",
                                "You are only allowed to more then " + maxAmt,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        exhAmt.setText("" + finalMaxAmt);
                                    }
                                });

                    }
                    keyPressed = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.getOthExpense().getExpHead().getId() == 0) {
                    customVariablesAndMethod.msgBox(context,"First Select the Expense Head...");
                } else if (viewModel.getOthExpense().getAmount() == 0D) {
                    customVariablesAndMethod.msgBox(context,"Please Enter the Expense Amt....");
                }  else if (viewModel.getOthExpense().getRemark().trim().isEmpty()) {
                    customVariablesAndMethod.msgBox(context,"Please Enter the Remark....");
                } else if (viewModel.getOthExpense().getExpHead().getDA_ACTION() == 1
                        && viewModel.getExpense().getACTUALDA_FAREYN().equalsIgnoreCase("Y")
                        && viewModel.getExpense().getDA_Amt() != 0) {

                    AppAlert.getInstance().Alert(context, "Already Applied for DA...",
                            "Please make DA amount Rs 0.", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onBackPressed();
                                }
                            });
                }else {


                    if (!viewModel.getNewAttachment().equals("")){
                        cboProgressDialog = new CboProgressDialog(context, "Please Wait..\nuploading Image");
                        cboProgressDialog.show();
                        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + viewModel.getNewAttachment());
                        new up_down_ftp().uploadFile(file2, context);

                    }else{
                        viewModel.other_expense_commit(context);
                    }

                }
            }
        });


    }

    @Override
    public void loadExpenseHead(ArrayList<mExpHead> expHeads) {
        adapter = new aExpenseHead(getApplicationContext(), R.layout.spin_row, expHeads);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        exphead.setAdapter(adapter);
    }

    @Override
    public void setRemarkCaption(String caption) {
        textRemark.setText(caption);
    }

    @Override
    public void setAmountCaption(String caption) {
        ex_head_root_txt.setText(caption);
    }

    @Override
    public void setAmtHint(String hint) {
        exhAmt.setHint(hint);
    }

    @Override
    public void onSendResponse(mOthExpense othExpense) {
        Intent intent = new Intent();
        intent.putExtra("othExpense",othExpense);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
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
        viewModel.other_expense_commit(context);
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
