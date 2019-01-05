package com.cbo.cbomobilereporting.ui_new.dcr_activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import locationpkg.Const;
import services.CboServices;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.Custom_Variables_And_Method;
import utils_new.GPS_Timmer_Dialog;
import utils_new.up_down_ftp;

public class NonListedCall_CNF extends AppCompatActivity implements up_down_ftp.AdapterCallback{


    Spinner spinCat;
    EditText name_edt,organisation_edt, address_edt, mobile_edt, callDetail_edt,DL,tin,smpl_dtl_edt,requirment_edt,courier_edt;
    String sPerson, sAdd1, sMobileNo, sRemark,sOrganization_Name;
    LinearLayout qfl_layout, spl_layout;
    Button price_btn, dispatch_btn, submit,bussiness_btn;
    ImageView imgSpinCat,attach;
    SpinAdapter SpinAdapter;
    ArrayList<SpinnerModel> mylist = new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel> Price_List = new ArrayList<SpinnerModel>();
    CBO_DB_Helper cboDbHelper;
    private AlertDialog myalertDialog = null;
    String price_Name = "", price_Id = "";
    Custom_Variables_And_Method customVariablesAndMethod;
    String sDocType,docType_name;
    Context context;


    int yy,mm,dd;
    Boolean flag=false;
    RadioGroup smpl_dtl;
    private final int RESULT_CROP = 400;
    private final int REQUEST_CAMERA=201;
    private File output=null;
    String filename="";
    TextView attach_txt;


    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET_Spinner=1,MESSAGE_INTERNET_SUBMIT=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_listed_call__cnf);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            textView.setText("Non Listed Calls");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }

        spinCat = (Spinner) findViewById(R.id.category_spinner);

        context=this;

        customVariablesAndMethod= Custom_Variables_And_Method.getInstance();
        cboDbHelper=new CBO_DB_Helper(context);
        progress1=new ProgressDialog(context);


        bussiness_btn = (Button) findViewById(R.id.bussiness_btn);
        dispatch_btn = (Button) findViewById(R.id.dispatch_btn);
        price_btn = (Button) findViewById(R.id.price_btn);
        smpl_dtl= (RadioGroup) findViewById(R.id.smpl_dtl);
        attach= (ImageView) findViewById(R.id.attach);
        attach_txt= (TextView) findViewById(R.id.attach_txt);

        requirment_edt= (EditText) findViewById(R.id.requirment_edt);
        smpl_dtl_edt= (EditText) findViewById(R.id.smpl_dtl_edt);
        name_edt = (EditText) findViewById(R.id.name_edt);
        organisation_edt= (EditText) findViewById(R.id.organisation_edt);
        address_edt = (EditText) findViewById(R.id.address_edt);
        DL = (EditText) findViewById(R.id.Dl_edt);
        tin = (EditText) findViewById(R.id.tin_edt);
        courier_edt = (EditText) findViewById(R.id.courier_edt);
        mobile_edt = (EditText) findViewById(R.id.mobile_edt);
        callDetail_edt = (EditText) findViewById(R.id.call_detail_edt);

        submit = (Button) findViewById(R.id.submit_btn);
        imgSpinCat = (ImageView) findViewById(R.id.spinner_category);

        Intent intent=getIntent();
        sDocType = intent.getStringExtra("sDocType");
        docType_name= intent.getStringExtra("sDocName");
        mylist.add(new SpinnerModel(docType_name));


        SpinAdapter = new SpinAdapter(this, R.layout.spin_row, mylist);
        SpinAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinCat.setAdapter(SpinAdapter);

        Calendar cal = Calendar.getInstance();
        yy=cal.get(Calendar.YEAR);
        mm=cal.get(Calendar.MONTH);
        dd=cal.get(Calendar.DAY_OF_MONTH);



        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cboDbHelper.getCompanyCode());
        request.put("iPaId", "" + Custom_Variables_And_Method.PA_ID);

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait.. \n Fetching data");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(this, mHandler).customMethodForAllServices(request, "DCR_NLC_CNF_DDL", MESSAGE_INTERNET_Spinner, tables);

        //End of call to service

        price_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPriceList();

            }
        });

        bussiness_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_DARK,mDateSetListener,yy,mm,dd);
                }
                flag=false;
                datePickerDialog.show();

            }
        });

        dispatch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_DARK,mDateSetListener,yy,mm,dd);
                }
                flag=true;
                datePickerDialog.show();

            }
        });

        smpl_dtl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.yes:
                        smpl_dtl_edt.setVisibility(View.VISIBLE);
                        break;
                    case R.id.no:
                        smpl_dtl_edt.setVisibility(View.GONE);
                        smpl_dtl_edt.setText("");
                        break;
                }

            }
        });

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions(NonListedCall_CNF.this, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CAMERA);
                    Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

                }else {

                    capture_Image();
                }
            }
        });

        mobile_edt.addTextChangedListener(new TextWatcher() {
            String text="";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text=charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mobile_edt.getText().length()>10){
                    mobile_edt.setText(text);
                    customVariablesAndMethod.msgBox(context,"Mobile No. can not be more then 10 digits");
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickSubmit(false);

            }
        });

    }

    private void onClickPriceList() {


        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        final ListView listview = new ListView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter = new SpinAdapter(this, R.layout.spin_row, Price_List);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                price_Id = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                price_Name = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
                price_btn.setText(price_Name);
                price_btn.setPadding(1, 0, 5, 0);

            }
        });
        myalertDialog = myDialog.show();

    }

    private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contex, Intent intent) {
            Location location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);
            //new GPS_Timmer_Dialog(context,mHandler,"Final Submit in Process...",GPS_TIMMER).show();
            onClickSubmit(true);
        }
    };

    private void onClickSubmit(boolean Skip_Verification) {

        sPerson = name_edt.getText().toString();
        sOrganization_Name=organisation_edt.getText().toString();
        sAdd1 = address_edt.getText().toString();
        sMobileNo = mobile_edt.getText().toString();
        sRemark = callDetail_edt.getText().toString();

        if (sOrganization_Name.equals("")) {
            customVariablesAndMethod.msgBox(context,"Orgamization Name Can't be Empty...");
        } else if (sPerson.equals("")) {
            customVariablesAndMethod.msgBox(context,"Contact Name Can't be Empty...");
        } else if (sAdd1.equals("")) {
            customVariablesAndMethod.msgBox(context,"Address Can't be Empty...");
        } else if (sMobileNo.equals("") || sMobileNo.length()<10) {
            customVariablesAndMethod.msgBox(context,"Mobile No. is always of 10 digit...");
        } else if (sRemark.equals("")) {
            customVariablesAndMethod.msgBox(context,"Remark Can't be Empty...");
        } else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,Skip_Verification)) {
            customVariablesAndMethod.msgBox(context,"Verifing Your Location");
            LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                    new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
        }else {


            if (!filename.equals("")){
                progress1.setMessage("Please Wait..");
                progress1.setCancelable(false);
                progress1.show();
                File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                new up_down_ftp().uploadFile(file2,context);

            }else{
                SubmitCNF();
            }




        }
    }


    private void SubmitCNF(){
        //Start of call to service

        HashMap<String, String> request = new HashMap<>();

        request.put("sCompanyFolder", cboDbHelper.getCompanyCode());
        request.put("iPaId", "" + Custom_Variables_And_Method.PA_ID);
        request.put("iDcrId", "" + Custom_Variables_And_Method.DCR_ID);
        request.put("sDOC_TYPE", sDocType);

        request.put("sOrganization_Name", sOrganization_Name);
        request.put("sAdd1", sAdd1);

        request.put("sAdd2", "");
        request.put("sAdd3", "");
        request.put("sPerson", sPerson);
        request.put("sMobileNo",sMobileNo);

        request.put("sDL_NO", DL.getText().toString());
        request.put("sTIN_NO", tin.getText().toString());

        if (bussiness_btn.getText().toString().equals("---MM/DD/YYYY---")){
            request.put("sBUSS_START_DATE","");
        }else {
            request.put("sBUSS_START_DATE", bussiness_btn.getText().toString());
        }
        request.put("sREQUIREMENT", requirment_edt.getText().toString());
        request.put("sRemark", sRemark);

        request.put("sFILE_NAME",filename);
        request.put("iPRICE_LIST_ID", price_Id);
        request.put("iSAMPLE_YN", smpl_dtl_edt.getText().toString());

        if (dispatch_btn.getText().toString().equals("---MM/DD/YYYY---")){
            request.put("sDISPATCH_DATE","");
        }else {
            request.put("sDISPATCH_DATE", dispatch_btn.getText().toString());
        }
        request.put("sCOURIOR_NAME", courier_edt.getText().toString());

        request.put("sNLC_LOC",customVariablesAndMethod.get_best_latlong(context));

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait.. \n Fetching data");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(this, mHandler).customMethodForAllServices(request, "DCRNLC_CNF_Commit_1", MESSAGE_INTERNET_SUBMIT, tables);

        //End of call to service
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_Spinner:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_spinner(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_SUBMIT:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_submit(msg.getData());

                    }
                    break;

                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                    }
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };

    private void parser_submit(Bundle result) {
        //cboDbHelper.insert_NonListed_Call(docType_name,sPerson,sAdd1,sMobileNo,sRemark,spl_Id,spl_Name,qfl_Id,qfl_Name,"",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON),customVariablesAndMethod.currentTime(),class_Name,potential.getText().toString(),area.getText().toString());
        cboDbHelper.insert_NonListed_Call(docType_name,sOrganization_Name,sAdd1,sMobileNo,sRemark,filename,"c&f","",sPerson,"",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON),customVariablesAndMethod.currentTime(context),DL.getText().toString(),tin.getText().toString(),bussiness_btn.getText().toString());
        customVariablesAndMethod.msgBox(context,"Successfully Submitted");
        customVariablesAndMethod.SetLastCallLocation(context);
        progress1.dismiss();
        finish();
    }


    private void parser_spinner(Bundle result) {
        if (result!=null ) {

            try {

                ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
                newlist.add(new SpinnerModel("--Select--", ""));

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    Price_List.add(new SpinnerModel(c.getString("PA_NAME"),c.getString("PA_ID")));
                }


                progress1.dismiss();
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {

            finish();
        }
        return super.onContextItemSelected(item);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                capture_Image();
                //Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CROP ) {
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                    Bitmap selectedBitmap;
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Bundle extras = data.getExtras();
                        selectedBitmap = extras.getParcelable("data");


                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    assert selectedBitmap != null;
                    selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
                    if (!dir.exists()) {
                        if (!dir.mkdirs()) {
                            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                            //return true;
                        } else {
                            try {
                                File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                                file2.createNewFile();
                                FileOutputStream fo = new FileOutputStream(file2);
                                fo.write(bytes.toByteArray());
                                fo.close();
                                Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                                //attach_img.setImageBitmap(myBitmap);
                                attach_txt.setText(filename);

                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);

                            file2.createNewFile();
                            FileOutputStream fo = new FileOutputStream(file2);
                            fo.write(bytes.toByteArray());
                            fo.close();
                            Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                            //attach_img.setImageBitmap(myBitmap);
                            attach_txt.setText(filename);

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }else{
                        attach_txt.setText(filename);
                    }

            }
        }
        if (requestCode == REQUEST_CAMERA ) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO"+File.separator+ filename);

                if (file1.exists()){
                    //performCrop(file1.getPath());
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        performCrop(file1.getPath());
                    } else {
                        performCropImage(file1.getPath());
                    }
                }

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

    private boolean performCropImage(String picUri) {
        Uri mCropImagedUri;
        try {
            if (picUri != null) {
                //call the standard crop action intent (the user device may not support it)
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                //indicate image type and Uri
                File f = new File(picUri);

//            fileTemp = ImageUtils.getOutputMediaFile();
                ContentValues values = new ContentValues(1);
                //values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                values.put( MediaStore.Images.ImageColumns.DATA, f.getPath() );
                Uri  contentUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                // Uri contentUri = Uri.fromFile(f);

                cropIntent.setDataAndType(contentUri, "image");
                //set crop properties
                cropIntent.putExtra("crop", "true");
                //indicate aspect of desired crop
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                cropIntent.putExtra("scale", true);
                //indicate output X and Y
                cropIntent.putExtra("outputX", 700);
                cropIntent.putExtra("outputY", 700);
                //retrieve data on return
                cropIntent.putExtra("return-data", true);

                filename = Custom_Variables_And_Method.PA_ID+"_"+Custom_Variables_And_Method.DCR_ID+"_"+new Date().getTime()+ ".jpg";
                File f1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO"+File.separator+ filename);
                try {
                    f1.createNewFile();
                } catch (IOException ex) {
                    Log.e("io", ex.getMessage());
                }
                mCropImagedUri = Uri.fromFile(f);
                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
                //start the activity - we handle returning in onActivityResult
                startActivityForResult(cropIntent, RESULT_CROP);
                return true;
            }
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
            attach_txt.setText(filename);

            return false;
        }
        return false;
    }

    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);

//            fileTemp = ImageUtils.getOutputMediaFile();
            ContentValues values = new ContentValues(1);
            //values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put( MediaStore.Images.ImageColumns.DATA, f.getPath() );
            Uri  contentUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 700);
            cropIntent.putExtra("outputY", 700);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
            attach_txt.setText(filename);
        }
    }


    private void capture_Image(){
        captureImage();
       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                //return true;
            }
        }
        filename = Custom_Variables_And_Method.PA_ID+"_"+Custom_Variables_And_Method.DCR_ID+"_"+new Date().getTime()+ ".jpg";
        output = new File(dir, filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        startActivityForResult(intent, REQUEST_CAMERA);*/
    }


    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                    //return true;
                }
            }
            filename =  Custom_Variables_And_Method.PA_ID+"_"+Custom_Variables_And_Method.DCR_ID+"_"+new Date().getTime()+ ".jpg";
            output = new File(dir, filename);

//            fileTemp = ImageUtils.getOutputMediaFile();
            ContentValues values = new ContentValues(1);
            //values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put( MediaStore.Images.ImageColumns.DATA, output.getPath() );
            Uri  fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            if (fileTemp != null) {
            // fileUri = Uri.fromFile(output);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_CAMERA);
//            } else {
//                Toast.makeText(this, getString(R.string.error_create_image_file), Toast.LENGTH_LONG).show();
//            }
        } else {
            Toast.makeText(this, getString(R.string.error_no_camera), Toast.LENGTH_LONG).show();
        }
    }



    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    yy = year;
                    mm = monthOfYear;
                    dd = dayOfMonth;
                    updateDisplay();
                }
            };

    // updates the date in the TextView
    private void updateDisplay() {

        StringBuilder s=new StringBuilder()
                // Month is 0 based so add 1
                .append(mm + 1).append("/")
                .append(dd).append("/")
                .append(yy).append(" ");


        if(flag){
            dispatch_btn.setText(s);
        }else{
            bussiness_btn.setText(s);
        }
    }


    /*@Override
    public void upload_complete(String IsCompleted) {

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
        SubmitCNF();
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
}
