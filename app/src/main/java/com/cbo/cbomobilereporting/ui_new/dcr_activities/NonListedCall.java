package com.cbo.cbomobilereporting.ui_new.dcr_activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import services.CboServices;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.Custom_Variables_And_Method;
import utils_new.up_down_ftp;

/**
 * Created by AKSHIT on 02/05/2016.
 */
public class NonListedCall extends AppCompatActivity  implements up_down_ftp.AdapterCallback{

    Spinner spinCat;
    EditText name_edt, address_edt, mobile_edt, callDetail_edt,potential,area,email,other_remark,business_dtl,ref_by;
    String sDrName, sAdd1, sMobileNo, sRemark;
    LinearLayout qfl_layout, spl_layout;
    Button qfl_btn, spl_btn, submit,class_btn;
    ImageView imgSpinCat,attach;
    SpinAdapter SpinAdapter;
    ArrayList<SpinnerModel> mylist = new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel> drSpl_ID = new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel> drClass_ID = new ArrayList<SpinnerModel>();
    ArrayList<SpinnerModel> drQfl_Id = new ArrayList<SpinnerModel>();
    CBO_DB_Helper cboDbHelper;
    public String drQflData;
    LinearLayout drSpecilityLayout;
    private AlertDialog myalertDialog = null;
    String spl_Name = "", spl_Id = "";
    String qfl_Name = "", qfl_Id = "";
    String class_Name = "", class_Id = "";
    Custom_Variables_And_Method customVariablesAndMethod;
    String sDocType,docType_name;
    String fmCGYN = "N";
    Context context;

    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET_Spinner=1,MESSAGE_INTERNET_SUBMIT=2;

    private final int RESULT_CROP = 400;
    private final int REQUEST_CAMERA=201;
    private File output=null;
    String filename="";
    TextView attach_txt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nonlisted_call);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder);
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

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cboDbHelper=new CBO_DB_Helper(context);
        progress1=new ProgressDialog(context);

        qfl_btn = (Button) findViewById(R.id.qulification_btn);
        spl_btn = (Button) findViewById(R.id.spicility_btn);
        class_btn = (Button) findViewById(R.id.class_btn);
        qfl_layout = (LinearLayout) findViewById(R.id.qfl_layout);
        spl_layout = (LinearLayout) findViewById(R.id.spl_layout);
        name_edt = (EditText) findViewById(R.id.name_edt);
        attach= (ImageView) findViewById(R.id.attach);
        attach_txt= (TextView) findViewById(R.id.attach_txt);

        address_edt = (EditText) findViewById(R.id.address_edt);
        potential = (EditText) findViewById(R.id.potential_edt);
        area = (EditText) findViewById(R.id.area_edt);
        mobile_edt = (EditText) findViewById(R.id.mobile_edt);
        callDetail_edt = (EditText) findViewById(R.id.call_detail_edt);

        email = (EditText) findViewById(R.id.email);
        ref_by = (EditText) findViewById(R.id.reffered_by);
        business_dtl = (EditText) findViewById(R.id.business_details);
        other_remark = (EditText) findViewById(R.id.other_remark);

        drSpecilityLayout = (LinearLayout) findViewById(R.id.visuality_selection);
        drSpecilityLayout.setVisibility(View.GONE);
        submit = (Button) findViewById(R.id.submit_btn);
        fmCGYN =customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"fmcg_value");
        imgSpinCat = (ImageView) findViewById(R.id.spinner_category);

       /* if (fmCGYN.equalsIgnoreCase("Y")) {
            mylist.add(new SpinnerModel("Retailer"));
            mylist.add(new SpinnerModel("Distributor"));
            mylist.add(new SpinnerModel("C & F"));

        } else {
            mylist.add(new SpinnerModel("Doctor"));
            if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHEMIST_NOT_REQUIRED").equals("N")){
                mylist.add(new SpinnerModel("Chemist"));
            }
            if( customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"STOCKIST_NOT_REQUIRED","N")){
                mylist.add(new SpinnerModel("Stockist"));
            }
            mylist.add(new SpinnerModel("C & F"));
        }

        if (cboDbHelper.getCompanyCode().toUpperCase().equals("MOHINI")){
            mylist.add(new SpinnerModel("Dealer"));
        }*/

       /* SpinAdapter = new SpinAdapter(this, R.layout.spin_row, mylist);
        SpinAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinCat.setAdapter(SpinAdapter);*/

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickSubmit();

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

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions(NonListedCall.this, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CAMERA);
                    Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

                }else {

                    capture_Image();
                }
            }
        });


        imgSpinCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinCat.performClick();
            }
        });

        spl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSpiclicity();

            }
        });

        class_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickClass();

            }
        });

        qfl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickQfl();
            }
        });


        spinCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                docType_name=mylist.get(position).getName();
                sDocType=mylist.get(position).getId();
                Intent i=null;
                switch (sDocType) {
                    case "D":
                        drSpecilityLayout.setVisibility(View.VISIBLE);
                        other_remark.setVisibility(View.GONE);
                        other_remark.setText("");
                        break;
                    case "O":
                        drSpecilityLayout.setVisibility(View.GONE);
                        other_remark.setVisibility(View.VISIBLE);
                        break;
                    case "CNF":
                    case "DEL":
                        i = new Intent(context, NonListedCall_CNF.class);
                        i.putExtra("sDocType",sDocType);
                        i.putExtra("sDocName",docType_name);
                        startActivity(i);
                        finish();
                        break;
                    default:
                        drSpecilityLayout.setVisibility(View.GONE);
                        other_remark.setVisibility(View.GONE);
                        other_remark.setText("");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cboDbHelper.getCompanyCode());
        request.put("iPaId", "" + Custom_Variables_And_Method.PA_ID);

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        tables.add(1);
        tables.add(2);
        tables.add(3);
        tables.add(4);

        progress1.setMessage("Please Wait.. \n Fetching data");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(this, mHandler).customMethodForAllServices(request, "DCRNLCDDL_MOBILE", MESSAGE_INTERNET_Spinner, tables);

        //End of call to service


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {

            finish();
        }
        return super.onContextItemSelected(item);


    }

    private void onClickSpiclicity() {


        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        final ListView listview = new ListView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter = new SpinAdapter(this, R.layout.spin_row, drSpl_ID);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                spl_Id = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                spl_Name = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
                spl_btn.setText(spl_Name);
                spl_btn.setPadding(1, 0, 5, 0);

            }
        });
        myalertDialog = myDialog.show();

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
        attach_txt.setText(filename);
       /* Uri mCropImagedUri;
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
        }*/
        return false;
    }

    private void performCrop(String picUri) {

        attach_txt.setText(filename);
     /*   try {
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
        }*/
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

    private void onClickClass() {


        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        final ListView listview = new ListView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter = new SpinAdapter(this, R.layout.spin_row, drClass_ID);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                class_Id = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                class_Name = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
                class_btn.setText(class_Name);
                spl_btn.setPadding(1, 0, 5, 0);

            }
        });
        myalertDialog = myDialog.show();

    }

    private void onClickQfl() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        final ListView listview = new ListView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(listview);
        myDialog.setView(layout);
        SpinAdapter arrayAdapter = new SpinAdapter(this, R.layout.spin_row, drQfl_Id);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();
                //String strName=TitleName[position];


                qfl_Id = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                qfl_Name = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
                qfl_btn.setText(qfl_Name);

            }
        });
        myalertDialog = myDialog.show();
    }


    private void onClickSubmit() {

        sDrName = name_edt.getText().toString();
        sAdd1 = address_edt.getText().toString();
        sMobileNo = mobile_edt.getText().toString();
        sRemark = callDetail_edt.getText().toString();

        if (sDrName.equals("")) {
            customVariablesAndMethod.msgBox(context,"Name Can't be Empty...");
        } else if (sAdd1.equals("")) {
            customVariablesAndMethod.msgBox(context,"Address Can't be Empty...");
        } else if (sMobileNo.equals("") || sMobileNo.length()<10) {
            customVariablesAndMethod.msgBox(context,"Mobile No. is always of 10 digit...");
        } else if (sRemark.equals("")) {
            customVariablesAndMethod.msgBox(context,"Call Details Can't be Empty...");
        } else if (sDocType==null ) {
            customVariablesAndMethod.msgBox(context,"Error Please open the page again...");
        }  else if ( sDocType.equals("O") &&  other_remark.getText().toString().equals("")) {
            customVariablesAndMethod.msgBox(context,"Category Can't be Empty...");
        } else if ( customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"NLC_PIC_YN","N").equals("Y") && filename.equals("")) {
            customVariablesAndMethod.msgBox(context,"Please Attach a Picture....");
        }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,false)) {
            customVariablesAndMethod.msgBox(context,"Verifing Your Location");
        }else {

            //new SubmitNonListed().execute();

            if (!filename.equals("")){

                progress1.setMessage("Please Wait.. \n Uploading Picture....");
                progress1.setCancelable(false);
                progress1.show();

                File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                new up_down_ftp().uploadFile(file2,context);

            }else{
                SubmitNLC();
            }

        }
    }





    private void SubmitNLC(){
        //Start of call to service

        HashMap<String, String> request = new HashMap<>();

        request.put("sCompanyFolder", cboDbHelper.getCompanyCode());
        request.put("iPaId", "" + Custom_Variables_And_Method.PA_ID);
        request.put("iDcrId", "" + Custom_Variables_And_Method.DCR_ID);
        request.put("sDocType", sDocType);

        request.put("sDrName", sDrName);
        request.put("sAdd1", sAdd1);

        request.put("sAdd2", "");
        request.put("sAdd3", "");

        request.put("sMobileNo", "" + sMobileNo);
        request.put("sRemark", sRemark);
        request.put("iSplId", "" + spl_Id);
        request.put("iQflId", "" + qfl_Id);

        request.put("iSrno", "");
        request.put("iInTime", "" + customVariablesAndMethod.currentTime(context));

        request.put("sClass", "" + class_Name);
        request.put("iPotencyAmt", potential.getText().toString());
        request.put("sArea",area.getText().toString());

        request.put("sOTHER_REMARK",  other_remark.getText().toString());
        request.put("sEMAIL",  email.getText().toString());
        request.put("sBUSINESS_DETAILS", business_dtl.getText().toString());
        request.put("sREF_BY",ref_by.getText().toString());

        request.put("sNLC_LOC",customVariablesAndMethod.get_best_latlong(context));
        request.put("sNLC_FILE",filename);

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait.. \n Processing....");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(this, mHandler).customMethodForAllServices(request, "DCRNLC_Commit_3", MESSAGE_INTERNET_SUBMIT, tables);

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
        customVariablesAndMethod.SetLastCallLocation(context);
        cboDbHelper.insert_NonListed_Call(docType_name,sDrName,sAdd1,sMobileNo,sRemark,spl_Id,spl_Name,qfl_Id,qfl_Name,"",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON),customVariablesAndMethod.currentTime(context),class_Name,potential.getText().toString(),area.getText().toString());
        customVariablesAndMethod.msgBox(context,"Successfully Submitted");
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
                    drSpl_ID.add(new SpinnerModel(c.getString("SPL"),c.getString("SPL_ID")));
                }

                String table1 = result.getString("Tables1");
                JSONArray jsonArray2 = new JSONArray(table1);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);
                    drQfl_Id.add(new SpinnerModel(c.getString("QFL"),c.getString("QFL_ID")));
                }

                String table2 = result.getString("Tables3");
                JSONArray jsonArray3 = new JSONArray(table2);
                for (int i = 0; i < jsonArray3.length(); i++) {
                    JSONObject c = jsonArray3.getJSONObject(i);
                    drClass_ID.add(new SpinnerModel(c.getString("FIELD_NAME"),c.getString("ID")));
                }

                String table4 = result.getString("Tables4");
                JSONArray jsonArray4 = new JSONArray(table4);
                for (int i = 0; i < jsonArray4.length(); i++) {
                    JSONObject c = jsonArray4.getJSONObject(i);
                    //drClass_ID.add(new SpinnerModel(c.getString("FIELD_NAME"),c.getString("ID")));
                    mylist.add(new SpinnerModel(c.getString("PA_NAME"),c.getString("PA_ID")));
                }

                SpinAdapter = new SpinAdapter(this, R.layout.spin_row, mylist);
                SpinAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spinCat.setAdapter(SpinAdapter);

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

   /* @Override
    public void upload_complete(final String IsCompleted) {
        //pd.dismiss();
        progress1.dismiss();
        if (IsCompleted.equals("S")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {

                }
            });
        }else if (IsCompleted.equals("Y")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    SubmitNLC();
                }
            });
        }else if (IsCompleted.contains("ERROR")) {
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
        SubmitNLC();
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
