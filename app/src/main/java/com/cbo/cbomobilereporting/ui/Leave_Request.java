package com.cbo.cbomobilereporting.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import async.Edit_PopulateleaveDetailTask;
import async.LeaveRequestGridTask;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import services.ServiceHandler;
import services.TaskListener;
import utils.ExceptionHandler;
import utils.MyConnection;
import utils.adapterutils.LeaveModel;
import utils.adapterutils.SpinnerModel;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit on 4/14/2015.
 */
public class Leave_Request extends AppCompatActivity {

    EditText  purpOfLeave, leaveFromDate, leaveTodate, totalDays, con_du_Leave, time,
               balence1, balence2, balence3,balence4,balence5,balence6, request1, request2,
               request3,request4, request5, request6;
     Button    attachHere, submit, cancle, attachFromChamera;
    TextView reqDate, empName, leaveHe4ad1, leavehead2, leaveHead3,leaveHead4,leaveHead5,leaveHead6;
    LinearLayout imageLayout,layout1,layout2,layout3,layout4,layout5,layout6,layout7,layout8,attachmentLayout;
    float balLeave1 = 0.0f;float balLeave2 = 0.0f;float balLeave3 = 0.0f;
    float reqLeave1 = 0.0f;float reqLeave2 = 0.0f;float reqLeave3 = 0.0f;
     Context context;
    String pa_name = "",leaveIdExtra = "";
    String dupPURPOSE,dupDOC_DATE,dupFDATE,dupTDATE,dupNO_DAY,dupPHONE;

    long newTotal =0;

    Custom_Variables_And_Method customVariablesAndMethod;
    SimpleAdapter simpleAdapter;
    ServiceHandler mseServiceHandler;
    ArrayList<Map<String, String>> dataforLeaveTask = new ArrayList<Map<String, String>>();
    ArrayList<LeaveModel> dataLeaveModle;

    ArrayList<String> leavedata = new ArrayList<String>();
    ArrayList<String> leaveHeadId = new ArrayList<String>();
    ArrayList<String> balencedata = new ArrayList<String>();
    ArrayList<String> requestdata = new ArrayList<String>();
    ArrayList<String> checklist = new ArrayList<String>();

    ArrayList<String> dcrDates = new ArrayList<String>();


    static final int TIME_DIALOG_ID = 0;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    static final int DATE_DIALOG_ID_REQ_DATE = 1;
    static final int DATE_DIALOG_ID_FROM = 2;
    static final int DATE_DIALOG_ID_REQ_TO = 3;
    private static int RESULT_LOAD_IMAGE = 5;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    private Uri fileUri;
    String reqDateSet, toDateSet, fromDateSet, numberOfDay ;
    String company_name = "", emp_id = "", mymonth = "", log_status = "";
    int hour, minute, mYear, mMonth, mDay;
    ArrayList<SpinnerModel> mylist = new ArrayList<SpinnerModel>();
    private String[] arrMonth = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String CurrntDate;
    CBO_DB_Helper cbohelp;

///// ftp variabl
    String ftp_username="";
    String ftp_hostname="";
    String ftp_password="";

    String web_root_path="",path1 ="",path2 ="MAILFILES/MedicalFiles",path3,mainPath;
    String ftp_port="";
    int port;


    String response1, response2, response3, response4, leaveID;
    ProgressDialog progressDialog;

    @SuppressLint("NewApi")
    public void uploadFile(File fileName){

        getFtpDetail();
        FTPClient client = new FTPClient();
        //getFtpDetail();
        try {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            client.connect(ftp_hostname,port);
            client.login(ftp_username, ftp_password);
            client.setType(FTPClient.TYPE_BINARY);

            path1 = web_root_path;

          path3 = path1.substring(0,6);

            mainPath = path3.trim()+path2;
            client.changeDirectory(mainPath);

            //client.changeDirectory("/"+MyConnection.user_name+"/");

            client.upload(fileName, new MyTransferListener());

          //  client.rename(fileName.getName(),fileName.getName());
            client.rename(fileName.getName(), Custom_Variables_And_Method.user_name+"_"+fileName.getName());

               //code for Deleting uploaded directory or file
               File f = new File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

                       deleteDirectory(f);


        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
       setContentView(R.layout.leave_request);
        FlurryAgent.logEvent("Leave_Request");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
          textView.setText("Leave Request");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }


        Bundle extras = getIntent().getExtras();
         context = Leave_Request.this;
        reqDate = (TextView) findViewById(R.id.leave_req_Date_tv);
        empName = (TextView) findViewById(R.id.emp_name_req_tv);
        leaveHe4ad1 = (TextView) findViewById(R.id.leaveHeadPl);
        leavehead2 = (TextView) findViewById(R.id.leaveHeadLwv);
        leaveHead3 = (TextView) findViewById(R.id.leaveHead);
        leaveHead4 = (TextView) findViewById(R.id.leaveHeadPl1);
        leaveHead5 = (TextView) findViewById(R.id.leaveHeadPl2);
        leaveHead6 = (TextView) findViewById(R.id.leaveHeadPl3);

        purpOfLeave = (EditText) findViewById(R.id.purpose_req_edt);
        balence1 = (EditText) findViewById(R.id.bal_leave_qtyPl);
        balence2 = (EditText) findViewById(R.id.bal_leave_qtyLwv);
        balence3 = (EditText) findViewById(R.id.bal_leave_qty);
        balence4 = (EditText) findViewById(R.id.bal_leave_qtyPl1);
        balence5 = (EditText) findViewById(R.id.bal_leave_qtyPl2);
        balence6 = (EditText) findViewById(R.id.bal_leave_qtyPl3);


        request1 = (EditText) findViewById(R.id.requriedLeavePl);
        request2 = (EditText) findViewById(R.id.requriedLeaveLwv);
        request3 = (EditText) findViewById(R.id.requriedLeave);
        request4 = (EditText) findViewById(R.id.requriedLeavePl1);
        request5 = (EditText) findViewById(R.id.requriedLeavePl2);
        request6 = (EditText) findViewById(R.id.requriedLeavePl3);


        leaveFromDate = (EditText) findViewById(R.id.fromDate_req_edt);
        leaveTodate = (EditText) findViewById(R.id.todate_req_edt);
        totalDays = (EditText) findViewById(R.id.totaldays_req_edt);
        con_du_Leave = (EditText) findViewById(R.id.contact_req_edt);
        attachFromChamera = (Button) findViewById(R.id.attachHereChemraBt);
        attachHere = (Button) findViewById(R.id.attachHereBt);
        submit = (Button) findViewById(R.id.save_bt_req);
        cancle = (Button) findViewById(R.id.cancle_bt_req);
        imageLayout = (LinearLayout) findViewById(R.id.imageReqLayout);


        layout1 = (LinearLayout) findViewById(R.id.layout1_leavehead);
        layout2 = (LinearLayout) findViewById(R.id.layout2_leavehead);
        layout3 = (LinearLayout) findViewById(R.id.layout3_leavehead);
        layout4 = (LinearLayout) findViewById(R.id.layout4_leavehead);
        layout5 = (LinearLayout) findViewById(R.id.layout5_leavehead);
        layout6 = (LinearLayout) findViewById(R.id.layout6_leavehead);
        layout7 = (LinearLayout) findViewById(R.id.layout7_leavehead);
        layout8 = (LinearLayout) findViewById(R.id.layout8_leavehead);
        attachmentLayout = (LinearLayout) findViewById(R.id.attach_layout);
        attachmentLayout.setVisibility(View.GONE);


        imageLayout.setVisibility(View.GONE);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        mseServiceHandler = new ServiceHandler(this);
        // listView =(ListView) findViewById(R.id.leave_RequestList);
        dataLeaveModle = new ArrayList<LeaveModel>();

        if (extras != null) {
            leaveIdExtra = extras.getString("leaveIdExtra");
            submit.setVisibility(View.GONE);
        }
         else {

            leaveIdExtra = "Intent Not Found";
            submit.setVisibility(View.VISIBLE);
        }

        pa_name = pa_name + Custom_Variables_And_Method.PA_NAME;
        cbohelp = new CBO_DB_Helper(context);
        if (!pa_name.equals(Custom_Variables_And_Method.PA_NAME)) {
            Intent i = new Intent(context, LoginFake.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(i);
        } else {

            empName.setText("" + pa_name);
        }

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        // CurrntDate = ""+mMonth +"/"+mDay+"/"+mYear;


        SimpleDateFormat timeStampFormat = new SimpleDateFormat("MM/dd/yyyy");
        final Date myDate = new Date();
        String todayDate = timeStampFormat.format(myDate);

        //    Toast.makeText(getApplicationContext(),"Today Is:"+filename,Toast.LENGTH_LONG).show();

        reqDate.setText(todayDate);


     /*  reqDate.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                showDialog(DATE_DIALOG_ID_REQ_DATE);
                return true;
            }
        });*/


        leaveFromDate.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                showDialog(DATE_DIALOG_ID_FROM);
                return true;
            }
        });

        leaveTodate.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                showDialog(DATE_DIALOG_ID_REQ_TO);
                return true;
            }
        });

        attachHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
                    Intent i = new Intent(Leave_Request.this, ViewPager_2016.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("Id", Custom_Variables_And_Method.CURRENTTAB);
                    startActivity(i);
                    finish();
                }else{
                    finish();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 String bal1=  balence1.getText().toString();
                 String req1=  request1.getText().toString();
               if (req1.equals("")){
                   req1 = ""+0.0;
               }
                 String bal2=  balence2.getText().toString();
                 String req2=  request2.getText().toString();
                if (req2.equals("")){
                    req2 = ""+0.0;
                }
                 String bal3=  balence3.getText().toString();
                 String req3=  request3.getText().toString();
                if (req3.equals("")){
                    req3 = ""+0.0;
                }
                String bal4=  balence4.getText().toString();
                 String req4=  request4.getText().toString();

                   balLeave1 = Float.parseFloat(bal1);
                   balLeave2 = Float.parseFloat(bal2);
                   balLeave3 = Float.parseFloat(bal3);
               // float balLeave4 = Float.parseFloat(bal4);
                       reqLeave1 = Float.parseFloat(req1);
                       reqLeave2 = Float.parseFloat(req2);
                       reqLeave3 = Float.parseFloat(req3);
               // float reqLeave4 = Float.parseFloat(req4);

                float totalReqLeave = reqLeave1+reqLeave2+reqLeave3;
                float totalLeaveDays = newTotal;

                if (balLeave1 >= reqLeave1){

                    if (balLeave2  >= reqLeave2){
                        if (balLeave3  >= reqLeave3){
                            if (totalLeaveDays == totalReqLeave) {
                                if (totalLeaveDays !=0.0){

                                new checkSubmitLeave().execute();

                                }else {
                                    customVariablesAndMethod.msgBox(context,"Please Set the Leave First..");


                                }
                            }else {

                                customVariablesAndMethod.msgBox(context,"Please Check Required Leave Input."+"\n"+" Value of " +
                                        "Total of Required Day's Not Match With Total Day's");
                            }
                        }else {
                            customVariablesAndMethod.msgBox(context,"Please Check Required Leave Input."+"\n"+"Required Leave not more then Balance Leave");
                        }
                    }else {
                        customVariablesAndMethod.msgBox(context,"Please Check Required Leave Input."+"\n"+"Required Leave not more then Balance Leave");
                    }
                }
                else {

                    customVariablesAndMethod.msgBox(context,"Please Check Required Leave Input."+"\n"+"Required Leave not more then Balance Leave");     }

            }
        });

        attachFromChamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f = new File(Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

                deleteDirectory(f);
                clickImeage();
            }
        });


        Leave_Request.this.leaveRequstGridMethod(Leave_Request.this);


    }

    //method for capture Imeage


    private void clickImeage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    ///Method for deleting chamera directory path
    public boolean deleteDirectory(File path) {

        if (path.exists()) {

            File[] files = path.listFiles();

            if (files == null) {

                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);

                } else {
                    files[i].delete();
                }
            }
        }

        return path.delete();

    }

    ///Get Back with Image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageLayout.setVisibility(View.VISIBLE);
            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                customVariablesAndMethod.msgBox(context,"User cancelled image capture");
            } else {
                // failed to capture image
                customVariablesAndMethod.msgBox(context,"Sorry! Failed to capture image");
            }

        }


    }

    private void previewCapturedImage() {
        try {
            // hide video preview

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            imageLayout.setVisibility(View.VISIBLE);

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            ImageView imageView = (ImageView) findViewById(R.id.imgView);

            imageView.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(
                        this, mTimeSetListener, hour, minute, true);
            case DATE_DIALOG_ID_REQ_DATE:
                return new DatePickerDialog(
                        this, mDateSetListenerReq, mYear, mMonth, mDay);
            case DATE_DIALOG_ID_FROM:
                return new DatePickerDialog(
                        this, mDateSetListenerFrom, mYear, mMonth, mDay);

            case DATE_DIALOG_ID_REQ_TO:
                return new DatePickerDialog(
                        this, mDateSetListenerTo, mYear, mMonth, mDay);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListenerTo =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    mymonth = getmonth();

                    toDateSet = mymonth + "/" + LPad(mDay + "", "0", 2) + "/" + mYear;
                    //reqDate.setText(sdate);
                    //leaveFromDate.setText(sdate);
                    leaveTodate.setText(toDateSet);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

                    Date d1 = null;
                    Date d2 = null;

                    try {
                        d1 = simpleDateFormat.parse(fromDateSet);
                        d2 = simpleDateFormat.parse(toDateSet);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Days days = Days.daysBetween(start, end);
                    long difference = d2.getTime() - d1.getTime();
                    long differenceBack = difference / 1000;
                    long secs = differenceBack / 60;
                    differenceBack /= 60;
                    long min = differenceBack / 60;
                    differenceBack /= 60;
                    long hour = differenceBack / 24;

                    differenceBack /= 24;

                    newTotal = differenceBack + 1;

                  numberOfDay = ""+ newTotal;

                    if(newTotal <=0){

                        customVariablesAndMethod.msgBox(context,"Please Enter the Valid Date"+ "\n"+"Date should be higher than From Date");
                        totalDays.setText("");
                    }else {

////////Here we set the number of days after Counting
                        totalDays.setText(numberOfDay);
                    }

                    if (newTotal >= 4){

                        attachmentLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        attachmentLayout.setVisibility(View.GONE);

                    }
                    Custom_Variables_And_Method.RPT_DATE = toDateSet;
                }
            };

    private DatePickerDialog.OnDateSetListener mDateSetListenerReq =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    mymonth = getmonth();

                    reqDateSet = mymonth + "/" + LPad(mDay + "", "0", 2) + "/" + mYear;
                    reqDate.setText(reqDateSet);
                    // leaveFromDate.setText(sdate);
                    // leaveTodate.setText(sdate);
                    Custom_Variables_And_Method.RPT_DATE = reqDateSet;
                }
            };
    private DatePickerDialog.OnDateSetListener mDateSetListenerFrom =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    mymonth = getmonth();

                    fromDateSet = mymonth + "/" + LPad(mDay + "", "0", 2) + "/" + mYear;
                    // reqDate.setText(sdate);
                    leaveFromDate.setText(fromDateSet);
                    // leaveTodate.setText(sdate);

                    if(!leaveTodate.getText().toString().equals("")){
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

                        Date d1 = null;
                        Date d2 = null;

                        try {
                            d1 = simpleDateFormat.parse(fromDateSet);
                            d2 = simpleDateFormat.parse(toDateSet);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //Days days = Days.daysBetween(start, end);
                        long difference = d2.getTime() - d1.getTime();
                        long differenceBack = difference / 1000;
                        long secs = differenceBack / 60;
                        differenceBack /= 60;
                        long min = differenceBack / 60;
                        differenceBack /= 60;
                        long hour = differenceBack / 24;

                        differenceBack /= 24;

                        newTotal = differenceBack + 1;

                        numberOfDay = ""+ newTotal;

                        if(newTotal <=0){

                            customVariablesAndMethod.msgBox(context,"Please Enter the Valid Date"+ "\n"+"Date should be higher than From Date");
                            totalDays.setText("");
                        }else {

////////Here we set the number of days after Counting
                            totalDays.setText(numberOfDay);
                        }

                        if (newTotal >= 4){

                            attachmentLayout.setVisibility(View.VISIBLE);
                        }
                        else {
                            attachmentLayout.setVisibility(View.GONE);

                        }
                    }


                    Custom_Variables_And_Method.RPT_DATE = fromDateSet;
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                    hour = hourOfDay;
                    minute = minuteOfHour;
                    String stime = LPad("" + hour, "0", 2) + "." + LPad("" + minute, "0", 2);
                    time.setText(stime);
                    Custom_Variables_And_Method.RPT_TIME = stime;
                }
            };

    private static String LPad(String schar, String spad, int len) {
        String sret = schar;
        for (int i = sret.length(); i < len; i++) {
            sret = spad + sret;
        }
        return new String(sret);
    }

    public String getmonth() {
        String month = "";
        mymonth = arrMonth[mMonth];
        if (mymonth.equals("Jan")) {
            month = "01";
        }
        if (mymonth.equals("Feb")) {
            month = "02";
        }
        if (mymonth.equals("Mar")) {
            month = "03";
        }
        if (mymonth.equals("Apr")) {
            month = "04";
        }
        if (mymonth.equals("May")) {
            month = "05";
        }
        if (mymonth.equals("Jun")) {
            month = "06";
        }
        if (mymonth.equals("Jul")) {
            month = "07";
        }
        if (mymonth.equals("Aug")) {
            month = "08";
        }
        if (mymonth.equals("Sep")) {
            month = "09";
        }
        if (mymonth.equals("Oct")) {
            month = "10";
        }
        if (mymonth.equals("Nov")) {
            month = "11";
        }
        if (mymonth.equals("Dec")) {
            month = "12";
        }

        return month;
    }


    //=============================================================sync data=============================================================================================

    public void saveSession(String value1, String value2) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor myedit = pref.edit();
        myedit.putString("pa_name", value1);
        myedit.putString("comp_name", value2);
        myedit.commit();
    }

    public HashMap<String, String> getPrefData() {
        HashMap<String, String> user_data = new HashMap<String, String>();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        user_data.put("pa_name", pref.getString("pa_name", null));
        user_data.put("comp_name", pref.getString("comp_name", null));
        return user_data;
    }

/////////////////////////////////// LeaveRequstGrid Task//////////////////////////////

    public void leaveRequstGridMethod(final Activity context) {

        if (!leaveIdExtra.equals("Intent Not Found")){




            final Edit_PopulateleaveDetailTask leaveRequestGridTask = new Edit_PopulateleaveDetailTask(context);
            leaveRequestGridTask.setListner(new TaskListener<String>() {
                @Override
                public void onStarted() {
                    try {
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setTitle("CBO");
                        progressDialog.setMessage("Please Wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinished(String result) {

                    if ((result == null) || (result.contains("[ERROR]"))) {

                        progressDialog.dismiss();

                        customVariablesAndMethod.msgBox(context,"Data Not found");
                    } else {


                        try {

                            leavedata.clear();
                            balencedata.clear();
                            requestdata.clear();
                            leaveHeadId.clear();


                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray rows = jsonObject.getJSONArray("Tables");

                            for (int i = 0 ;i <rows.length(); i++){

                                JSONObject jsonObject1 = rows.getJSONObject(0);
                                JSONObject jsonObject2 = rows.getJSONObject(1);
                                JSONArray one = jsonObject1.getJSONArray("Tables0");
                                JSONArray  two = jsonObject2.getJSONArray("Tables1");

                                for (int j = 0; j<one.length(); j++) {

                                    JSONObject c = one.getJSONObject(j);

                                    leavedata.add(c.getString("LEAVE_HEAD"));
                                    leaveHeadId.add(c.getString("LEAVE_HEAD_ID"));
                                    balencedata.add(c.getString("BAL_QTY"));

                                    requestdata.add(c.getString("REQ_QTY"));


                                }



                                JSONObject d = two.getJSONObject(0);



                                dupPURPOSE = d.getString("PURPOSE");
                                dupDOC_DATE = d.getString("DOC_DATE");
                                dupFDATE = d.getString("FDATE");
                                dupTDATE = d.getString("TDATE");
                                dupNO_DAY = d.getString("NO_DAY");
                                dupPHONE = d.getString("PHONE");






                            }


                            if(leavedata.size()>0) {

                                layout1.setVisibility(View.VISIBLE);
                                leaveHe4ad1.setText(leavedata.get(0));
                                balence1.setText(balencedata.get(0));
                                request1.setText(requestdata.get(0));
                            }
                            if(leavedata.size()>1) {

                                layout2.setVisibility(View.VISIBLE);
                                leavehead2.setText(leavedata.get(1));
                                balence2.setText(balencedata.get(1));
                                request2.setText(requestdata.get(1));
                            }
                            if(leavedata.size()>2) {

                                layout3.setVisibility(View.VISIBLE);
                                leaveHead3.setText(leavedata.get(2));
                                balence3.setText(balencedata.get(2));
                                request3.setText(requestdata.get(2));
                            }

                            if(leavedata.size()>3){
                                layout4.setVisibility(View.VISIBLE);

                                leaveHead4.setText(leavedata.get(3));
                                balence4.setText(balencedata.get(3));
                                request4.setText(requestdata.get(3));
                            }
                            if(leavedata.size()>4){
                                layout5.setVisibility(View.VISIBLE);

                                leaveHead5.setText(leavedata.get(4));
                                balence5.setText(balencedata.get(4));
                                request5.setText(requestdata.get(4));
                            }
                            if(leavedata.size()>5){

                                layout6.setVisibility(View.VISIBLE);
                                leaveHead6.setText(leavedata.get(5));
                                balence6.setText(balencedata.get(5));
                                request6.setText(requestdata.get(5));
                            }



                            purpOfLeave.setText(dupPURPOSE);
                            leaveFromDate.setText(dupFDATE);
                            leaveTodate.setText(dupTDATE);
                            totalDays.setText(dupNO_DAY);
                            con_du_Leave.setText(dupPHONE);

                            progressDialog.dismiss();


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }

                    }

                }
            });
            leaveRequestGridTask.execute(leaveIdExtra);





        }

        else if(leaveIdExtra.contains("Intent Not Found")){

        final LeaveRequestGridTask leaveRequestGridTask = new LeaveRequestGridTask(context);
        leaveRequestGridTask.setListner(new TaskListener<String>() {
            @Override
            public void onStarted() {
                try {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setTitle("CBO");
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinished(String result) {

                if ((result == null) || (result.contains("[ERROR]"))) {

                    progressDialog.dismiss();

                    customVariablesAndMethod.msgBox(context,"Data Not found");
                } else {


                    try {
                        // dataforLeaveTask.clear_2();
                        JSONObject jsonObject = new JSONObject(result);
                        leavedata.clear();
                        balencedata.clear();
                        requestdata.clear();
                        leaveHeadId.clear();
                        JSONArray rows = jsonObject.getJSONArray("Tables0");
                        for (int i = 0; i < rows.length(); i++) {
                            // Map<String,String> datamember = new HashMap<String, String>();
                            JSONObject c = rows.getJSONObject(i);


                           /* leaveModel = new LeaveModel();

                            String leaveHeadeNo = c.getString("LEAVE_HEAD_ID");
                            leaveModel.setLeaveHeadId(leaveHeadeNo);
*/
                            // String leavehead = c.getString("LEAVE_HEAD");
                            leavedata.add(c.getString("LEAVE_HEAD"));
                            leaveHeadId.add(c.getString("LEAVE_HEAD_ID"));
                            // leaveModel.setLeaveHead(leavehead);

                            //String balqty = c.getString("BAL_QTY");
                            // leaveModel.setBalQty(balqty);
                            balencedata.add(c.getString("BAL_QTY"));

                            //String reqqty = c.getString("REQ_QTY");
                            //leaveModel.setReqQty(reqqty);
                            requestdata.add(c.getString("REQ_QTY"));

                           /* String leaveID = c.getString("ID");
                            leaveModel.setLeaveId(leaveID);

                            dataLeaveModle.add(leaveModel);*/

                            //  datamember.put("leaveheadno",c.getString("LEAVE_HEAD_ID"));
                            //datamember.put("leavehead",c.getString("LEAVE_HEAD"));
                            //datamember.put("balqty",c.getString("BAL_QTY"));
                            //datamember.put("reqqty",c.getString("REQ_QTY"));
                            //datamember.put("leaveID",c.getString("ID"));

                            // dataforLeaveTask.add(datamember);

                        }
                        //String from [] ={"leaveheadno","leavehead","balqty","reqqty","leaveID"} ;
                        //int to [] ={R.id.leaveHeadId,R.id.leaveHead,R.id.bal_leave_qty,R.id.requriedLeave,R.id.leaveId};
                        //simpleAdapter = new SimpleAdapter(Leave_Request.this,dataforLeaveTask,R.layout.leaverequest_view,from,to);
                        //listView.setAdapter(simpleAdapter);
                        //progressDialog.dismiss();
                     /* leaveAdapter = new LeaveAdapter(context,dataLeaveModle);
                        listView.setAdapter(leaveAdapter);*/

                        if(leavedata.size()>0) {

                            layout1.setVisibility(View.VISIBLE);

                            leaveHe4ad1.setText(leavedata.get(0));
                            balence1.setText(balencedata.get(0));
                            request1.setText(requestdata.get(0));
                        }
                        if(leavedata.size()>1) {

                            layout2.setVisibility(View.VISIBLE);

                            leavehead2.setText(leavedata.get(1));
                            balence2.setText(balencedata.get(1));
                            request2.setText(requestdata.get(1));
                        }
                        if(leavedata.size()>2) {

                            layout3.setVisibility(View.VISIBLE);

                            leaveHead3.setText(leavedata.get(2));
                            balence3.setText(balencedata.get(2));
                            request3.setText(requestdata.get(2));
                        }

                        if(leavedata.size()>3){
                            layout4.setVisibility(View.VISIBLE);

                            leaveHead4.setText(leavedata.get(3));
                            balence4.setText(balencedata.get(3));
                            request4.setText(requestdata.get(3));
                        }
                        if(leavedata.size()>4){
                            layout5.setVisibility(View.VISIBLE);

                            leaveHead5.setText(leavedata.get(4));
                            balence5.setText(balencedata.get(4));
                            request5.setText(requestdata.get(4));
                        }
                        if(leavedata.size()>5){

                            layout6.setVisibility(View.VISIBLE);
                            leaveHead6.setText(leavedata.get(5));
                            balence6.setText(balencedata.get(5));
                            request6.setText(requestdata.get(5));
                        }
                        progressDialog.dismiss();


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                }

            }
        });
        leaveRequestGridTask.execute();

    }}

    public class checkSubmitLeave extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            try {
                response1 = mseServiceHandler.getResponse_LeaveAddCheckDuplicateInDCR(cbohelp.getCompanyCode(), "" + Custom_Variables_And_Method.PA_ID, leaveFromDate.getText().toString(), leaveTodate.getText().toString());
                response2 = mseServiceHandler.getResponse_LeaveDuplicate(cbohelp.getCompanyCode(), "" + Custom_Variables_And_Method.PA_ID, leaveFromDate.getText().toString(), leaveTodate.getText().toString());
            }catch (Exception e){
                return "ERROR apk "+e;
            }
            return response1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Leave_Request.this);
            progressDialog.setTitle("CBO");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null && !s.contains("ERROR")) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray rows = jsonObject.getJSONArray("Tables0");
                    if (s.contains("DCR_DATE")) {
                        progressDialog.dismiss();
                        dcrDates.clear();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject c = rows.getJSONObject(i);
                            dcrDates.add(c.getString("DCR_DATE"));
                        }
                        showSettings(dcrDates);
                        // new SubmitLeave().execute();
                    } else if (response2.contains("ID")) {
                        progressDialog.dismiss();
                        customVariablesAndMethod.msgBox(context,"Leave already Submitted");
                    }else if (response2.contains("[ERROR]")) {
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        new SubmitLeave().execute();
                   /* dcrDates.clear_2();
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject c = rows.getJSONObject(i);
                        dcrDates.add(c.getString("DCR_DATE"));
                    }
                    showSettings(dcrDates);*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void showSettings(ArrayList<String> data) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Leave_Request.this);
            builder1.setTitle("Leave already Submitted");
            String listString = "";

            for (String s : data) {
                listString += s + "\n";
            }

            builder1.setMessage("Leave/DCR already submitted for the dates " + listString);
            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            });
            builder1.show();
        }

        class SubmitLeave extends AsyncTask<String, String, String> {

            StringBuilder sb_LeaveHeadId = new StringBuilder();
            StringBuilder sb_BalanceData = new StringBuilder();
            StringBuilder sb_RequestData = new StringBuilder();
            StringBuilder sb_Id = new StringBuilder();


            @Override
            protected String doInBackground(String... params) {


                int j =0;
                for(int i = 0;i<leavedata.size();i++){

                    if (j==0){

                        sb_LeaveHeadId.append(leaveHeadId.get(i));
                        sb_BalanceData.append(balencedata.get(i));
                        sb_RequestData.append(request1.getText().toString());
                        sb_Id.append("0");



                    }else {
                        sb_LeaveHeadId.append("^").append(leaveHeadId.get(i));
                        sb_BalanceData.append("^").append(balencedata.get(i));

                        sb_Id.append("^").append("0");




                    }

                    if (j==1){

                        sb_RequestData.append("^").append(request2.getText().toString());

                    }
                    if (j==2){

                        sb_RequestData.append("^").append(request3.getText().toString());
                    }
                    if (j==3){

                        sb_RequestData.append("^").append(request4.getText().toString());
                    }


                    j++;
                }


                try {
                    response3 = mseServiceHandler.getResponse_CommitAddLeave_1(cbohelp.getCompanyCode(), "" + Custom_Variables_And_Method.PA_ID, purpOfLeave.getText().toString(), "0",
                            totalDays.getText().toString(), leaveFromDate.getText().toString(), leaveTodate.getText().toString(), con_du_Leave.getText().toString(), "0", reqDate.getText().toString(), "", "", sb_LeaveHeadId.toString(), sb_Id.toString(), sb_BalanceData.toString(), sb_RequestData.toString());

                }catch (Exception e){
                    return "ERROR apk "+e;
                }

                return response3;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(Leave_Request.this);
                progressDialog.setTitle("CBO");
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // progressDialog.dismiss();
                if (s != null && !s.contains("ERROR")) {
                    customVariablesAndMethod.msgBox(context,"Leave Successfully Submit");


                    checklist = getFtpSize();

                    File fileCheck = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
                    if (fileCheck.isDirectory()) {

                        if (fileCheck.list().length > 0) {

                            if (checklist.size() == 0) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(Leave_Request.this);
                                builder.setTitle("Upload Pic.");
                                builder.setIcon(R.drawable.warn);
                                builder.setMessage("No detail Please Attach Again");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub

                                        finish();
                                    }
                                });
                                builder.show();
                            } else {
                                new UploadPhotoInBackGround().execute();
                            }

                        } else {

                            System.out.println("Directory is empty!");

                            customVariablesAndMethod.msgBox(context,"Your Leave is Successfully Submitted");
                        }

                    } else {

                        System.out.println("This is not a directory");

                    }

                    Intent i = new Intent(Leave_Request.this, ViewPager_2016.class);
                    i.putExtra("Id", 2);
                    startActivity(i);
                    finish();
                    progressDialog.dismiss();
                } else {

                }
            }
        }
    }
    public ArrayList<String>getFtpSize()
    {
        ArrayList<String>ftpsize=new ArrayList<String>();
        Cursor c=cbohelp.getFTPDATA();
        if(c.moveToFirst())
        {
            do{
                ftpsize.add(c.getString(c.getColumnIndex("ftpip")));
                ftpsize.add(c.getString(c.getColumnIndex("username")));
            }while(c.moveToNext());
        }
        return ftpsize;
    }

    public void getFtpDetail()
    {
        Cursor c=cbohelp.getFTPDATA();
        if(c.moveToFirst())
        {
            do{

                ftp_hostname=c.getString(c.getColumnIndex("ftpip"));
                ftp_username=c.getString(c.getColumnIndex("username"));
                ftp_password=c.getString(c.getColumnIndex("password"));
                ftp_port=c.getString(c.getColumnIndex("port"));
                web_root_path=c.getString(c.getColumnIndex("path"));
            }while(c.moveToNext());
        }
        port=Integer.parseInt(ftp_port);
        // c.close();
    }



    /*******  Used to file upload and show progress  **********/

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {


            // Transfer started
            customVariablesAndMethod.msgBox(context," Upload Started ...");
            //System.out.println(" Upload Started ...");
        }

        public void transferred(int length) {

            // Yet other length bytes has been transferred since the last time this
            // method was called
            customVariablesAndMethod.msgBox(context, " transferred ..." +Custom_Variables_And_Method.user_name+ length);
            //System.out.println(" transferred ..." + length);
        }

        public void completed() {


            // Transfer completed

            customVariablesAndMethod.msgBox(context, " completed ...");
            //System.out.println(" completed ..." );
        }

        public void aborted() {


            // Transfer aborted
            customVariablesAndMethod.msgBox(context," transfer aborted , please try again...");
            //System.out.println(" aborted ..." );
        }

        public void failed() {


            // Transfer failed
            System.out.println(" failed ..." );
        }

    }


    class UploadPhotoInBackGround extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;


        protected Void doInBackground(Void... params) {
           SystemClock.sleep(1000);
				/*
				String myfile=null;
		    	File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);
		    	for(File acfile:f.listFiles())
		    	{
		    		if(acfile.isFile())
		    		{
		    			myfile=acfile.getName();

		    		}
		    	}
		    	File kfile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/Hello Camera/"+myfile+"");
		    	uploadFile(kfile);
		    	*/
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(Leave_Request.this);
            pd.setTitle("CBO");
            pd.setMessage("Processing......."+"\n"+"please wait");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            String myfile=null;
            //getFtpDetail();
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);
            for(File acfile:f.listFiles())
            {
                if(acfile.isFile())
                {
                    myfile=acfile.getName();
               }
            }
            File kfile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/Hello Camera/"+myfile+"");

            uploadFile(kfile);

            //akshit
            deleteFile(myfile);
             pd.dismiss();

            Intent i = new Intent(Leave_Request.this,ViewPager_2016.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("Id",Custom_Variables_And_Method.CURRENTTAB);
            startActivity(i);
        }
    }


    @Override
    public void onBackPressed() {
        if (!Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
            Intent i = new Intent(Leave_Request.this, ViewPager_2016.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("Id", Custom_Variables_And_Method.CURRENTTAB);
            startActivity(i);
            finish();
        }else{
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      if (item != null){
          if (!Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
              Intent i = new Intent(Leave_Request.this, ViewPager_2016.class);
              i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
              i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              i.putExtra("Id", Custom_Variables_And_Method.CURRENTTAB);
              startActivity(i);
              finish();
          }else{
              finish();
          }
        }
        return super.onOptionsItemSelected(item);
    }
}