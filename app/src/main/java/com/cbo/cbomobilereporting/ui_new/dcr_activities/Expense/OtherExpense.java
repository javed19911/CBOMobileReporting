package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.AttachImage;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.uenics.javed.CBOLibrary.CboProgressDialog;

import java.io.File;
import java.util.ArrayList;

import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.cboUtils.CBOImageView;
import utils_new.up_down_ftp;

public class OtherExpense extends CustomActivity implements IOtherExpense,up_down_ftp.AdapterCallback {

    Spinner exphead;
    EditText  exhAmt, rem_final,Km;
    Button Add, Cancel;
    TextView  textRemark,head,ex_head_root_txt,rate,title;
    ImageView attach_img;
    CheckBox add_attachment;
    LinearLayout KmLayout,amtLayout;
    aExpenseHead adapter;
    Boolean keyPressed = true;

    private final int REQUEST_CAMERA=201;

    vmOtherExpenses viewModel;
    CboProgressDialog cboProgressDialog = null;
    CBOImageView cboImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_expense);

        viewModel = ViewModelProviders.of(this).get(vmOtherExpenses.class);
        viewModel.setView(context,this);
        viewModel.setExpense((mExpense) getIntent().getSerializableExtra("expense"));
        viewModel.setExpense_type((eExpense) getIntent().getSerializableExtra("eExpense"));
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
        title = findViewById(R.id.ExpTitle);
        exphead = findViewById(R.id.exp_head_root);
        exhAmt = findViewById(R.id.ex_head_root);
        head = findViewById(R.id.head);
        textRemark = findViewById(R.id.text_remark);
        ex_head_root_txt = findViewById(R.id.ex_head_root_txt);
        rem_final = findViewById(R.id.exp_remark_root);
        add_attachment = findViewById(R.id.add_attachment);
        attach_img= findViewById(R.id.attach_img);
        attach_img.setVisibility(View.GONE);
        KmLayout = findViewById(R.id.kmLayout);
        amtLayout = findViewById(R.id.amtLayout);
        Km = findViewById(R.id.km);
        rate = findViewById(R.id.rate);
        cboImageView = findViewById(R.id.attachment);
        //final String[] ext = {path};

        Add = findViewById(R.id.save);
        Cancel = findViewById(R.id.cancel);

        exphead.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub

                mExpHead expHead = viewModel.getExpense().getExpHeads().get(position);
                /*if (viewModel.getOthExpense().getId() != 0){
                    expHead = viewModel.getOthExpense().getExpHead();
                }*/
                viewModel.setExpenseHead( context,expHead);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        add_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add_attachment.isChecked()) {
                    addAttachment();
                }else{
                    viewModel.getOthExpense().setAttachment("");
                    viewModel.setNewAttachment("");
                    attach_img.setVisibility(View.GONE);
                }
            }
        });


        cboImageView.setListener(new CBOImageView.iCBOImageView() {
            @Override
            public void OnAddClicked() {
                cboImageView.addAttachment(context);
            }

            @Override
            public void OnAdded() {
                OnUpdated(cboImageView.getDataList());
            }

            @Override
            public void OnDeleted(String file) {
                OnUpdated(cboImageView.getDataList());
            }

            @Override
            public void OnUpdated(ArrayList<String> files) {
                viewModel.setAttachment(files);
                setAttachment(files);
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

        Km.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("."))
                    s="0.0";
                Double km_new = s.toString().trim().isEmpty() ? 0D: Double.parseDouble(s.toString());
                Double rate_new = viewModel.getExpense().getRateFor(km_new).getRate();
                rate.setText(""+ rate_new);
                Double amt = km_new * rate_new;
                exhAmt.setEnabled(amt<=0);
                if (!s.toString().isEmpty() && amt<=0){
                    Km.setText("");
                }
                if(amt>0) {
                    exhAmt.setText("" + amt);
                }

                viewModel.getOthExpense().setKm(km_new);
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
                if (s.toString().equalsIgnoreCase("."))
                    s="0.0";
                Km.setEnabled(s.toString().isEmpty() || !Km.getText().toString().isEmpty());
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
                } /*else if (viewModel.getOthExpense().getExpHead().getDA_ACTION() == 1
                        && viewModel.getExpense().getACTUALDA_FAREYN().equalsIgnoreCase("Y")
                        && viewModel.getExpense().getDA_Amt() != 0) {

                    AppAlert.getInstance().Alert(context, "Already Applied for DA...",
                            "Please make DA amount Rs 0.", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onBackPressed();
                                }
                            });
                }*/else if (viewModel.getOthExpense().getExpHead().getATTACHYN() != 0
                        && viewModel.getNewAttachment().equalsIgnoreCase("")
                        && viewModel.getOthExpense().getAttachment().equalsIgnoreCase("")) {
                    customVariablesAndMethod.msgBox(context,"Please add an attachment....");
                }else {


                    if (!viewModel.getNewAttachment().equals("")){
                        cboProgressDialog = new CboProgressDialog(context, "Please Wait..\nuploading Image");
                        cboProgressDialog.show();
                        //File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + viewModel.getNewAttachment());
                        new up_down_ftp().uploadFile(cboImageView.filesToUpload(), context);

                    }else{
                        viewModel.other_expense_commit(context);
                    }

                }
            }
        });


    }

    @Override
    public void setTitle() {
        if (viewModel.getExpense_type()== eExpense.TA) {
            title.setText("Add TA");
        }else if(viewModel.getExpense_type()== eExpense.DA){
            title.setText("Add DA");
        }
    }

    @Override
    public void KmLayoutRequired(Boolean required) {
        if (required){
            KmLayout.setVisibility(View.VISIBLE);
            amtLayout.setVisibility(View.GONE);
        }else{
            KmLayout.setVisibility(View.GONE);
            amtLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadExpenseHead(ArrayList<mExpHead> expHeads) {
        adapter = new aExpenseHead(getApplicationContext(), R.layout.spin_row, expHeads);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        exphead.setAdapter(adapter);
    }

    @Override
    public void addAttachment() {
        String filenameTemp = Custom_Variables_And_Method.PA_ID+"_"+ Custom_Variables_And_Method.DCR_ID+"_OthExp_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
        //choosePhoto = new ChoosePhoto(context, REQUEST_CAMERA, ChoosePhoto.ChooseFrom.all);
        Intent intent = new Intent(context, AttachImage.class);
        intent.putExtra("Output_FileName",filenameTemp);
        intent.putExtra("SelectFrom", AttachImage.ChooseFrom.all);
        startActivityForResult(intent,REQUEST_CAMERA);
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
    public void setRemark(String remark) {
        rem_final.setText(remark);
    }

    @Override
    public void setAmount(Double amount) {
        if (amount == 0){
            exhAmt.setText("");
        }else{
            exhAmt.setText(""+ amount);
        }

    }

    @Override
    public void setKm(Double km) {
        Km.setText(""+ km);
    }

    @Override
    public void setRate(Double rate) {

    }

    @Override
    public void setAttachment(ArrayList<String> files) {
        /*if (!path.isEmpty()) {
            add_attachment.setChecked(true);
            File OutputFile = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + path);
            previewCapturedImage(OutputFile.getPath());
        }*/

       cboImageView.updateDataList(files);
    }

    @Override
    public void onSendResponse(mOthExpense othExpense) {
        Intent intent = new Intent();
        intent.putExtra("othExpense",othExpense);
        intent.putExtra("eExpense",viewModel.getExpense_type());
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
        viewModel.getOthExpense().setAttachment(viewModel.getNewAttachment());
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CBOImageView.REQUEST_CAMERA:
                    cboImageView.onActivityResult(requestCode, resultCode, data);
                    break;
                case REQUEST_CAMERA:
                    File OutputFile = (File) data.getSerializableExtra("Output");
                    viewModel.setNewAttachment(OutputFile.getName());
                    previewCapturedImage(OutputFile.getPath());
                    break;

                default:
                    super.onActivityResult(requestCode, resultCode, data);

            }
        }
    }

    private void previewCapturedImage(String picUri) {
        try {
            // hide video preview

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(picUri,
                    options);
            attach_img.setVisibility(View.VISIBLE);
            attach_img.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
