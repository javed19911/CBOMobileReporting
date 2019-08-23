package com.cbo.cbomobilereporting.ui_new.dcr_activities.Recipt;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.aExpenseHead;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.eExpense;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mExpense;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mOthExpense;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.vmOtherExpenses;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.CboProgressDialog;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import services.MyAPIService;
import utils.model.DropDownModel;
import utils_new.AppAlert;
import utils_new.CustomDatePicker;
import utils_new.CustomDialog.Spinner_Dialog;
import utils_new.Custom_Variables_And_Method;

public class  AddRecpiet extends CustomActivity implements IReciptEntry {

    EditText partdropdwon;
    Button selecteddate;
    ImageView imgpartdropdwon,selecteddateimg;
    EditText  rcpt_no,rcpt_amt, rcpt_remark,rcpt_by;
    Button Add, Cancel;
    TextView  textRemark,rcpt_title;
    vmAddRecipt vmAddRecipt;
    ArrayList<DropDownModel> Partylist = new ArrayList<DropDownModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reciept);

        vmAddRecipt = ViewModelProviders.of(this).get(vmAddRecipt.class);
        vmAddRecipt.setListener(context,this);
        vmAddRecipt.setView(context,this);


        vmAddRecipt.setRecipt((mRecipt) getIntent().getSerializableExtra("mRecipt"));




    }

    @Override
    public void getReferencesById() {

        rcpt_title = findViewById(R.id.receipt_title);
        partdropdwon = findViewById(R.id.partdropdwon);
        textRemark = findViewById(R.id.text_remark);
        rcpt_no = findViewById(R.id.rcpt_no);
        rcpt_remark = findViewById(R.id.rcpt_remark);
        rcpt_by = findViewById(R.id.rcpt_by);
        rcpt_amt = findViewById(R.id.rcpt_amt);
        selecteddate=findViewById(R.id.selecteddate);
        selecteddateimg= findViewById(R.id.selecteddateimg);

        Add = findViewById(R.id.save);
        Cancel = findViewById(R.id.cancel);

        rcpt_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("."))
                    s="0.0";
                Double amt = s.toString().trim().isEmpty() ? 0D: Double.parseDouble(s.toString());

                vmAddRecipt.getRecipt().setAmount(amt);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        rcpt_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                vmAddRecipt.getRecipt().setRemark(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });       /* selecteddate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            new CustomDatePicker(
                                    context, null, null)
                                    .Show(
                                            CustomDatePicker.getDate(
                                                    CustomDatePicker.currentDate(), CustomDatePicker.CommitFormat),
                                            new CustomDatePicker.ICustomDatePicker() {
                                                @Override
                                                public void onDateSet(Date date) {
                                                    try {
                                                        vmAddRecipt.getmRecipt().setDoc_Date( CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
                                                        selecteddate.setText(CustomDatePicker.formatDate(CustomDatePicker.getDate(vmAddRecipt.getmRecipt().getDoc_Date(),CustomDatePicker.CommitFormat),CustomDatePicker.ShowFormat));

                                                        ;
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });*/

     /*   selecteddate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            new CustomDatePicker(
                                    context,
                                   null,
                                    null)
                                    .Show(
                                            CustomDatePicker.getDate(
                                                   CustomDatePicker.currentDate(), CustomDatePicker.CommitFormat),
                                            new CustomDatePicker.ICustomDatePicker() {
                                                @Override
                                                public void onDateSet(Date date) {

                                                   vmAddRecipt.setSelecteddate(date);

                                                }
                                            });
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });*/

        partdropdwon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Spinner_Dialog(context, Partylist, new Spinner_Dialog.OnItemClickListener() {
                    @Override
                    public void ItemSelected(DropDownModel item) {

                        vmAddRecipt.getRecipt().setParty_id(item.getId()).setParty_name(item.getName());
                        partdropdwon.setText(item.getName());
                        //  partdropdwon.setPadding(1, 0, 5, 0);


                    }
                }).show();
            }
        });


        /*selecteddateimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecteddate.performClick();
            }
        });

        imgpartdropdwon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                partdropdwon.performClick();
            }
        });*/

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(validate()){

                    vmAddRecipt.GetPartySubmit(context);


                }



            }
        });


    }

    @Override
    public void setReceiptTitle(String title) {
        rcpt_title.setText(title);
    }

    @Override
    public void onParylistUpdated(ArrayList<DropDownModel> partylist) {
        Partylist.addAll(partylist);
    }


    @Override
    public void onRecipetSubmited() {

        onSendResponse();
    }


    @Override
    public void setRecieptNo(String DocNo) {
        vmAddRecipt.getRecipt().setReciept_no(DocNo);
        rcpt_no.setText(DocNo);
        rcpt_no.setEnabled(false);
    }
    //added editext insted of button on spinner
    @Override
    public void setPartyname(String partyname) {
        partdropdwon.setText(partyname);
        if(vmAddRecipt.getRecipt().getId()==0){
            partdropdwon.setEnabled(false);
            partdropdwon.setClickable(true);
        }else{
            partdropdwon.setEnabled(false);
            partdropdwon.setClickable(false);
        }

    }

    @Override
    public void setDate(String date) {
        try {
            selecteddate.setText(CustomDatePicker.formatDate( CustomDatePicker.getDate(date,CustomDatePicker.CommitFormat), CustomDatePicker.ShowFormat));
            if(vmAddRecipt.getRecipt().getId()==0){
                selecteddate.setClickable(true);
                selecteddateimg.setClickable(true);
            }else{
                selecteddate.setClickable(false);
                selecteddateimg.setClickable(false);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setAmount(Double amount) {
        rcpt_amt.setText(String.format("%.2f",amount));
        vmAddRecipt.getRecipt().setAmount(amount);
    }

    @Override
    public void setRemark(String remark) {
        rcpt_remark.setText(remark);
        vmAddRecipt.getRecipt().setRemark(remark);
    }

    @Override
    public void setRecpientBy(String recpientBy) {
        rcpt_by.setText(recpientBy);
        rcpt_by.setEnabled(false);
    }



    private boolean validate() {
       if(rcpt_no.getText().toString().isEmpty()){
            customVariablesAndMethod.msgBox(context,"Please Enter Recpiet No  first");
            return  false;
        } else if(partdropdwon.getText().toString().isEmpty()){
            customVariablesAndMethod.msgBox(context,"Please Select Party name first");
            return  false;
        }else   if(rcpt_amt.getText().toString().isEmpty()){
           customVariablesAndMethod.msgBox(context,"Please  Enter Amount first");
           return  false;
       } /*else if(rcpt_remark.getText().toString().isEmpty()){
            customVariablesAndMethod.msgBox(context,"Please Enter Remark  first");
            return  false;
        } */else {

            return  true;

        }



    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void onSendResponse() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }




}
