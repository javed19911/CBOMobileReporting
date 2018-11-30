package com.cbo.cbomobilereporting.ui_new.transaction_activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.LoginFake;
import com.cbo.cbomobilereporting.ui_new.DcrmenuInGrid;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.ChemistCall;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.GetDCR;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.area.Dcr_Open_New;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.DCR_Root_new;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.ExpenseRoot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import camera_galary_pkg.ChoosePhoto;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import services.CboServices;
import services.ServiceHandler;
import utils.adapterutils.HLVAdapter;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.Chm_Sample_Dialog;
import utils_new.Custom_Variables_And_Method;
import utils_new.GPS_Timmer_Dialog;
import utils_new.up_down_ftp;

/**
 * Created by Shivam on 10/14/2015.
 */
public class Farmer_registration_form extends AppCompatActivity implements up_down_ftp.AdapterCallback  {


    EditText date, owner_name, owner_mob, farmer_attendence,
            group_meeting_place, product_detail, IH_attendence_mcc, directr_sale_to_farmer, order_book_for_mcc, remark_Metting;
    Button btn_ok, btn_close, btn_view, clickPicture;
    String string_date, string_owner_name, string_owner_mob, string_farmer_attendence,
            string_group_meeting_place, string_product_detail, string_IH_attendence_mcc,
            string_directr_sale_to_farmer, string_order_book_for_mcc, fRemark;
    CBO_DB_Helper db_helper;
    ImageView framerImage;
    Context context;
    int year_x, month_x, day_x;
    static final int Dialog_id = 0;
    String string_date_SERVER;
    Calendar calendar = Calendar.getInstance();
    Custom_Variables_And_Method customVariablesAndMethod;
    String mLatLong;

    ServiceHandler myServices;
    SpinAdapter SpinAdapter;

    LinearLayout expLayout,tolScrLayout,posScrLayout,negSrcLayout,callTypeLayout,
            product_detail_layout,product_sample_layout,add_attachment,not_for_RETAILERCHAINYN_layout,
            farmer_attendance,owner_layout;
    EditText exptxt,tolScr,posScr,negSrc;

    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    private final int REQUEST_CAMERA=201;
    String picturePath="";
    private File output=null;
    String filename="";
    String filenameTemp="";
    public ProgressDialog progress1;

    Spinner spinCat;
    ImageView imgSpinCat ;
    String docType_name = "",docType_id= "";
    Boolean ExtraFieldYN = false,RETAILERCHAINYN = false;

    String sample_id="",sample_name="",sample_pob="",sample_sample="";
    private  static final int PRODUCT_DILOG=5,REQUEST_ATTACHMENT= 6,MESSAGE_INTERNET_CAMPDRGROUPDDL=7;
    TableLayout stk;
    RecyclerView Attachment;

     ArrayList<SpinnerModel> mylist;
    private ChoosePhoto choosePhoto=null;


    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<File> alImage;


    /////////////////////
    private void captureImage(int responseCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                    //return true;
                }
            }
            filenameTemp = Custom_Variables_And_Method.PA_ID+"_"+Custom_Variables_And_Method.DCR_ID+"_Farmer_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
            output = new File(dir, filenameTemp);


//            fileTemp = ImageUtils.getOutputMediaFile();
            ContentValues values = new ContentValues(1);
            //values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put( MediaStore.Images.ImageColumns.DATA, output.getPath() );
            Uri fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            if (fileTemp != null) {
            // fileUri = Uri.fromFile(output);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, responseCode);
//            } else {
//                Toast.makeText(this, getString(R.string.error_create_image_file), Toast.LENGTH_LONG).show();
//            }
        } else {
            Toast.makeText(this, getString(R.string.error_no_camera), Toast.LENGTH_LONG).show();
        }
    }


    private void previewCapturedImage(String picUri) {
        try {
            // hide video preview

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            //options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(picUri,
                    options);
            framerImage.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ChoosePhoto.CHOOSE_PHOTO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {

                File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                        //return true;
                    }
                }
                filenameTemp = Custom_Variables_And_Method.PA_ID+"_"+Custom_Variables_And_Method.DCR_ID+"_Farmer_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
                output = new File(dir, filenameTemp);



                if (data != null && data.getData() != null) {
                    choosePhoto.handleGalleryResult(data, output);
                } else {
                    choosePhoto.handleCameraResult(choosePhoto.getCameraUri(), output);
                }


            }
        }else if (requestCode == ChoosePhoto.SELECTED_IMG_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                //mImgCamera.setImageURI(choosePhoto.getCropImageUrl());
                File file2 = new File(choosePhoto.getCropImageUrl().getPath());
               // previewCapturedImage(file2);
            }
        }else if (requestCode == REQUEST_CAMERA ) {
            if (resultCode == RESULT_OK) {
                filename = filenameTemp;
               // File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO"+File.separator+ filename);
                File file1 = new File(choosePhoto.getCropImageUrl().getPath());
                previewCapturedImage(file1.getPath());
                clickPicture.setVisibility(View.GONE);
                btn_ok.setVisibility(View.VISIBLE);

            } else if (resultCode == RESULT_CANCELED) {
                filename = "";
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "image capture cancelled ", Toast.LENGTH_SHORT)
                        .show();
            } else {
                filename = "";
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }else if (requestCode == REQUEST_ATTACHMENT ) {
            if (resultCode == RESULT_OK) {

               // File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO"+File.separator+ filenameTemp);
                File file1 = new File(choosePhoto.getCropImageUrl().getPath());
                alImage.add(file1);
                mAdapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "image capture cancelled ", Toast.LENGTH_SHORT)
                        .show();
            } else {

                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_registration_form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() !=null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }



        context = Farmer_registration_form.this;

        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);

        // farmerError=(TextView)findViewById(R.id.farmer_tv);
        btn_ok = (Button) findViewById(R.id.btn_save);
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_view = (Button) findViewById(R.id.btn_view);

        spinCat = (Spinner) findViewById(R.id.category_spinner);
        imgSpinCat = (ImageView) findViewById(R.id.spinner_category);

        date = (EditText) findViewById(R.id.date);
        owner_name = (EditText) findViewById(R.id.owner_name_of_mcc);
        owner_mob = (EditText) findViewById(R.id.owner_mob_no);
        farmer_attendence = (EditText) findViewById(R.id.no_of_farmer_attended);
        group_meeting_place = (EditText) findViewById(R.id.place_of_group_meeting);

        product_detail = (EditText) findViewById(R.id.product_detail);
        IH_attendence_mcc = (EditText) findViewById(R.id.ih_staff_attended_mcc);
        directr_sale_to_farmer = (EditText) findViewById(R.id.direct_sales_of_farmer);
        order_book_for_mcc = (EditText) findViewById(R.id.order_book_for_mcc);
        remark_Metting = (EditText) findViewById(R.id.farmar_remark);
        clickPicture = (Button) findViewById(R.id.camera_farmer);
        framerImage = (ImageView) findViewById(R.id.upload_pic_farmer);


        add_attachment =  (LinearLayout) findViewById(R.id.add_attachment);
        product_sample_layout =  (LinearLayout) findViewById(R.id.product_Sample_layout);
        product_detail_layout = (LinearLayout) findViewById(R.id.product_detail_layout);
        expLayout = (LinearLayout) findViewById(R.id.expLayout);
        tolScrLayout = (LinearLayout) findViewById(R.id.tolScrLayout);
        posScrLayout = (LinearLayout) findViewById(R.id.posScrLayout);
        negSrcLayout = (LinearLayout) findViewById(R.id.negSrcLayout);
        callTypeLayout = (LinearLayout) findViewById(R.id.callTypeLayout);
        not_for_RETAILERCHAINYN_layout = (LinearLayout) findViewById(R.id.not_for_RETAILERCHAINYN_layout);
        farmer_attendance = (LinearLayout) findViewById(R.id.farmer_attendance);
        owner_layout = (LinearLayout) findViewById(R.id.owner_layout);

        exptxt = (EditText) findViewById(R.id.exptxt);
        tolScr = (EditText) findViewById(R.id.tolScr);
        posScr = (EditText) findViewById(R.id.posScr);
        negSrc = (EditText) findViewById(R.id.negSrc);

        stk= (TableLayout) findViewById(R.id.promotion);

        Attachment = (RecyclerView)  findViewById(R.id.attachment);

        db_helper = new CBO_DB_Helper(context);

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        myServices = new ServiceHandler(this);
        progress1 = new ProgressDialog(this);


        mylist = new ArrayList<SpinnerModel>();
        mylist.add(new SpinnerModel("CMA","0"));
        mylist.add(new SpinnerModel("C3MC","0"));

        SpinAdapter = new SpinAdapter(this, R.layout.spin_row, mylist);
        SpinAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinCat.setAdapter(SpinAdapter);

        imgSpinCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinCat.performClick();
            }
        });

        ExtraFieldYN = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FARMERADDFIELDYN","N").equals("Y");
        if(!ExtraFieldYN){
            expLayout.setVisibility(View.GONE);
            tolScrLayout.setVisibility(View.GONE);
            posScrLayout.setVisibility(View.GONE);
            negSrcLayout.setVisibility(View.GONE);
            callTypeLayout.setVisibility(View.GONE);
            product_sample_layout.setVisibility(View.GONE);
            add_attachment.setVisibility(View.GONE);
            Attachment.setVisibility(View.GONE);
        }else{
            product_detail_layout.setVisibility(View.GONE);
        }

        RETAILERCHAINYN = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"RETAILERCHAINYN","N").equals("Y");
        if(RETAILERCHAINYN){
            not_for_RETAILERCHAINYN_layout.setVisibility(View.GONE);
            farmer_attendance.setVisibility(View.GONE);
            owner_layout.setVisibility(View.GONE);
            callTypeLayout.setVisibility(View.VISIBLE);
            product_sample_layout.setVisibility(View.VISIBLE);
            add_attachment.setVisibility(View.VISIBLE);
            Attachment.setVisibility(View.VISIBLE);
            getCamp();
        }

        alImage = new ArrayList<>();
        // Calling the RecyclerView
        Attachment = (RecyclerView) findViewById(R.id.attachment);
        Attachment.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        Attachment.setLayoutManager(mLayoutManager);

        mAdapter = new HLVAdapter(context, alImage);
        Attachment.setAdapter(mAdapter);

        spinCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                docType_name =mylist.get(position).getName();
                docType_id =mylist.get(position).getId();

                if(!RETAILERCHAINYN) {
                    switch (docType_name) {
                        case "C3MC":
                            expLayout.setVisibility(View.GONE);
                            tolScrLayout.setVisibility(View.VISIBLE);
                            posScrLayout.setVisibility(View.VISIBLE);
                            negSrcLayout.setVisibility(View.VISIBLE);
                            break;
                        case "CMA":
                            expLayout.setVisibility(View.GONE);
                            tolScrLayout.setVisibility(View.GONE);
                            posScrLayout.setVisibility(View.GONE);
                            negSrcLayout.setVisibility(View.GONE);
                            break;
                        default:
                            expLayout.setVisibility(View.GONE);
                            tolScrLayout.setVisibility(View.GONE);
                            posScrLayout.setVisibility(View.GONE);
                            negSrcLayout.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String head=db_helper.getMenu("DCR", "D_FAR").get("D_FAR");
        if(head == null)
            head=db_helper.getMenu("TRANSACTION", "T_FAR").get("T_FAR");

        textView.setText(head);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  sending_data_to_server();
                showDialog(Dialog_id);


            }
        });

        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDialog(Dialog_id);

                return false;
            }
        });

        tolScr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int a = Integer.parseInt("0"+tolScr.getText().toString().trim()) - Integer.parseInt("0"+posScr.getText().toString().trim());
                negSrc.setText(""+ a);
            }
        });

        posScr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int a = Integer.parseInt("0"+tolScr.getText().toString().trim()) - Integer.parseInt("0"+posScr.getText().toString().trim());
                negSrc.setText(""+ a);
            }
        });

        negSrc.setEnabled(false);

        add_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             /*   if (ContextCompat.checkSelfPermission(Farmer_registration_form.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Farmer_registration_form.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions(Farmer_registration_form.this, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, Farmer_registration_form.this.REQUEST_CAMERA);
                    Toast.makeText(Farmer_registration_form.this, "Please allow the permission", Toast.LENGTH_LONG).show();

                }else {

                    captureImage(REQUEST_ATTACHMENT);
                }*/
                choosePhoto = new ChoosePhoto(Farmer_registration_form.this,REQUEST_ATTACHMENT, ChoosePhoto.ChooseFrom.all);
            }
        });


        clickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*if (ContextCompat.checkSelfPermission(Farmer_registration_form.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Farmer_registration_form.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions(Farmer_registration_form.this, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, Farmer_registration_form.this.REQUEST_CAMERA);
                    Toast.makeText(Farmer_registration_form.this, "Please allow the permission", Toast.LENGTH_LONG).show();

                }else {*/
                    choosePhoto = new ChoosePhoto(Farmer_registration_form.this,REQUEST_CAMERA,
                            customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CMC3_GALLERY_REQ","Y").equals("Y") ?  ChoosePhoto.ChooseFrom.all : ChoosePhoto.ChooseFrom.camera);
                    //captureImage(REQUEST_CAMERA);
              //  }

            }
        });
        product_sample_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b=new Bundle();
                b.putString("intent_fromRcpaCAll","Select Product...");
                b.putString("sample_name", sample_name);
                b.putString("sample_pob",sample_pob);
                b.putString("sample_sample", sample_sample);

                b.putString("sample_name_previous", "");
                b.putString("sample_pob_previous","");
                b.putString("sample_sample_previous", "");

                new Chm_Sample_Dialog(context,mHandler,b,PRODUCT_DILOG).Show();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //try {
                    string_date = date.getText().toString();
                    string_owner_name = owner_name.getText().toString();
                    string_owner_mob = owner_mob.getText().toString();
                    string_farmer_attendence = farmer_attendence.getText().toString();
                    string_group_meeting_place = group_meeting_place.getText().toString();
                    string_product_detail = product_detail.getText().toString();
                    string_IH_attendence_mcc = IH_attendence_mcc.getText().toString();
                    string_directr_sale_to_farmer = directr_sale_to_farmer.getText().toString();
                    string_order_book_for_mcc = order_book_for_mcc.getText().toString();
                    fRemark = remark_Metting.getText().toString();


                    if (!RETAILERCHAINYN) {
                        if (string_date.equals("")) {
                            customVariablesAndMethod.msgBox(context, "please Input Date");
                        } else if (string_owner_name.equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter Owner name");
                        } else if (string_owner_mob.equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter Mobile number");
                        } else if (string_farmer_attendence.equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter Farmer attendence ");
                        } else if (string_group_meeting_place.equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter Group meeting place");
                        } else if (string_product_detail.equals("") && product_detail_layout.getVisibility() == View.VISIBLE) {
                            customVariablesAndMethod.msgBox(context, "Please enter Product details");
                        } else if (sample_id.equals("") && product_sample_layout.getVisibility() == View.VISIBLE) {
                            customVariablesAndMethod.msgBox(context, "Please enter Product details");
                        } else if (string_IH_attendence_mcc.equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter HO staff attendence");
                        } else if (string_directr_sale_to_farmer.equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter Farmer sale");
                        } else if (string_order_book_for_mcc.equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter Order book for MCC ");
                        }

                        //EditText exptxt,tolScr,posScr,negSrc;
                        else if (ExtraFieldYN && expLayout.getVisibility() == View.VISIBLE && exptxt.getText().toString().equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter Exp. Amt.");
                        } else if (ExtraFieldYN && docType_name.equals("C3MC") && tolScr.getText().toString().equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter Total Screening");
                        } else if (ExtraFieldYN && docType_name.equals("C3MC") && posScr.getText().toString().equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter No. Of Positive Screenig ");
                        } else if (ExtraFieldYN && docType_name.equals("C3MC") && negSrc.getText().toString().equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter No. Of Negative Screenig ");
                        } else if (filename.equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please Capture Picture First then Submit ");
                        } else if (ExtraFieldYN && alImage.size() < 2) {
                            customVariablesAndMethod.msgBox(context, "Add Atleast two attachment.....");
                        } else if (fRemark.isEmpty()) {
                            customVariablesAndMethod.msgBox(context, "Please Enter Remark.... ");
                        } else {
                            db_helper.Save(string_date_SERVER, string_owner_name, string_owner_mob, string_farmer_attendence, string_group_meeting_place, string_product_detail, string_IH_attendence_mcc, string_directr_sale_to_farmer, string_order_book_for_mcc, fRemark);
                            //new UploadPhotoInBackGround().execute();
                            progress1.setMessage("Please Wait..\nuploading Image");
                            progress1.setCancelable(false);
                            progress1.show();
                            File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                            File[] files = {file2}; //, alImage.get(0), alImage.get(1)};
                            new up_down_ftp().uploadFile(files, Farmer_registration_form.this);

                        }
                    } else {
                        if (string_date.equals("")) {
                            customVariablesAndMethod.msgBox(context, "please Input Date");
                        } else if (string_owner_mob.equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter Mobile number");
                        } else if (string_group_meeting_place.equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please enter Address");
                        } else if (sample_id.equals("") && product_sample_layout.getVisibility() == View.VISIBLE) {
                            customVariablesAndMethod.msgBox(context, "Please enter Product details");
                        } else if (filename.equals("")) {
                            customVariablesAndMethod.msgBox(context, "Please Capture Picture First then Submmit ");
                        } else if (alImage.size() < 2) {
                            customVariablesAndMethod.msgBox(context, "Add Atleast two attachment.....");
                        } else if (fRemark.isEmpty()) {
                            customVariablesAndMethod.msgBox(context, "Please Enter Remark.... ");
                        } else {
                            db_helper.Save(string_date_SERVER, string_owner_name, string_owner_mob, string_farmer_attendence, string_group_meeting_place, string_product_detail, string_IH_attendence_mcc, string_directr_sale_to_farmer, string_order_book_for_mcc, fRemark);
                            //new UploadPhotoInBackGround().execute();
                            progress1.setMessage("Please Wait..\nuploading Image");
                            progress1.setCancelable(false);
                            progress1.show();
                            File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                            File[] files = {file2, alImage.get(0), alImage.get(1)};
                            new up_down_ftp().uploadFile(files, Farmer_registration_form.this);

                        }
                    }

                /*}catch (Exception e){
                    e.printStackTrace();
                }*/
            }
        });
    }


    protected Dialog onCreateDialog(int id) {
        if ((id == Dialog_id)) {
            return new DatePickerDialog(this, daypickerlistner, year_x, month_x, day_x);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener daypickerlistner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            int month = monthOfYear + 1;
            date.setText(day_x + "/" + month + "/" + year_x);

            string_date_SERVER = (month + "-" + day_x + "-" + year_x);
        }
    };


    void getCamp(){
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",db_helper.getCompanyCode());
        request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(context,mHandler).customMethodForAllServices(request,"CAMPDRGROUPDDL",MESSAGE_INTERNET_CAMPDRGROUPDDL,tables);

        //End of call to service
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b1;
            switch (msg.what) {
                case MESSAGE_INTERNET_CAMPDRGROUPDDL:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_campagin(msg.getData());

                    }
                    break;
                case PRODUCT_DILOG:
                    b1 = msg.getData();

                    sample_id = b1.getString("val");//id
                    sample_name = b1.getString("resultList");
                    sample_pob = b1.getString("val2");//score or pob
                    sample_sample = b1.getString("sampleQty");// sample

                    String[] sample_name1 = sample_name.split(",");
                    String[] sample_qty1 = sample_pob.split(",");
                    String[] sample_pob1 = sample_sample.split(",");
                    init(sample_name1, sample_qty1, sample_pob1);

                    break;
                case 99:
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    break;


            }
        }
    };


    private void parser_campagin(Bundle result) {

        if (result!=null ) {

            try {

                String table1 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table1);
                mylist.clear();
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject obj = jsonArray1.getJSONObject(i);
                    mylist.add(new SpinnerModel(obj.getString("PA_NAME"),obj.getString("PA_ID")));

                }

                if (mylist.size()>0) {
                    docType_name = mylist.get(0).getName();
                    docType_id =mylist.get(0).getId();
                }

                SpinAdapter.notifyDataSetChanged();

                progress1.dismiss();
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }
    private void init(String[] sample_name, String[] sample_qty, String[] sample_pob) {
        //TableLayout stk= (TableLayout) findViewById(R.id.promotion);
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        TableRow tbrow0 = new TableRow(context);
        tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText("Product");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.WHITE);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(" Sample ");
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextColor(Color.WHITE);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(context);
        tv2.setPadding(5, 5, 5, 0);
        tv2.setText(" POB ");
        tv2.setTextColor(Color.WHITE);
        tv2.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv2);
        stk.removeAllViews();
        stk.setBackgroundResource(R.drawable.custom_square_transparent_bg);
        stk.addView(tbrow0);
        for (int i = 0; i < sample_name.length; i++) {
            TableRow tbrow = new TableRow(context);
            TextView t1v = new TextView(context);
            t1v.setText(sample_name[i]);
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);
            TextView t2v = new TextView(context);
            t2v.setText(sample_qty[i]);
            t2v.setPadding(5, 5, 5, 0);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(context);
            t3v.setText(sample_pob[i]);
            t3v.setPadding(5, 5, 5, 0);
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            stk.addView(tbrow);
        }




    }

   /* @Override
    public void upload_complete(final String IsCompleted) {

        if (IsCompleted.equals("S")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    //new UploadPhotoInBackGround().execute();
                }
            });
        }else if (IsCompleted.equals("Y")) {
            progress1.dismiss();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    new farmer_data_to_server().execute();

                }
            });
        }else if (IsCompleted.contains("ERROR")) {
            progress1.dismiss();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    //new UploadPhotoInBackGround().execute();
                    String folder=IsCompleted.substring(6);
                    customVariablesAndMethod.getAlert(context,"Folder not found",folder+"   Invalid path \nPlease contact your administrator");
                }
            });
        }else {
            progress1.dismiss();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    customVariablesAndMethod.msgBox(context,"UPLOAD FAILED \n Please try again");
                }
            });
        }
    }*/

    @Override
    public void started(Integer responseCode, String message, String description) {

    }

    @Override
    public void progess(Integer responseCode, Long FileSize, Float value, String description) {

    }

    @Override
    public void complete(Integer responseCode, String message, String description) {
        progress1.dismiss();
        new farmer_data_to_server().execute();
    }

    @Override
    public void aborted(Integer responseCode, String message, String description) {
        progress1.dismiss();
        customVariablesAndMethod.getAlert(context,message,description);
    }

    @Override
    public void failed(Integer responseCode, String message, String description) {
        progress1.dismiss();
        customVariablesAndMethod.getAlert(context,message,description);
    }

    //////////////////Farmer Data To server////////////////


    class farmer_data_to_server extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {

                try {

                    String myDcrId = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_ID");

                    Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
                    mLatLong = Custom_Variables_And_Method.GLOBAL_LATLON;

                    return  myServices.getResponse_DcrFarmerCommit("" + db_helper.getCompanyCode(), "" + Custom_Variables_And_Method.PA_ID, myDcrId, string_date_SERVER,
                            string_owner_name, string_owner_mob, string_farmer_attendence, string_group_meeting_place, string_product_detail, string_IH_attendence_mcc,
                            string_directr_sale_to_farmer, string_order_book_for_mcc, "0", fRemark, filename, mLatLong, "",docType_name,
                            exptxt.getText().toString(),tolScr.getText().toString(),posScr.getText().toString(),
                            negSrc.getText().toString(),sample_id,sample_pob,sample_sample,RETAILERCHAINYN ? alImage.get(0).getName(): "",
                            RETAILERCHAINYN ? alImage.get(1).getName(): "",docType_id);

                } catch (Exception e) {

                    return "[ERROR]";
                }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress1.setMessage("Processing......."+"\n"+"please wait");
            progress1.show();


        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progress1.dismiss();
            if (!result.contains("[ERROR]")) {
                customVariablesAndMethod.msgBox(context,"Registration Successfully ....");
                finish();
            }else{
                customVariablesAndMethod.getAlert(context,"Alert!!!","Something went Wrong....");
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ChoosePhoto.SELECT_PICTURE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                choosePhoto.showAlertDialog();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null){

         finish();

        }
        return super.onOptionsItemSelected(item);
    }

    /////////////////////////////////////////

}
