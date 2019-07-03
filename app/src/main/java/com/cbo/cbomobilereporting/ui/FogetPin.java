package com.cbo.cbomobilereporting.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import services.CboServices;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;


public class FogetPin extends CustomActivity {
    private static final int MY_PERMISSIONS_REQUEST_DEVICE_ID = 22;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 11;
    EditText loginId, loginpwd, companyCode, pin, repin;
    CBO_DB_Helper cbohelp;
    String mylog, mypass, pincode, repincode;
    String company_code;
    String networkStatus;
    int count = 0;
    int chem_count = 0;

    Custom_Variables_And_Method customVariablesAndMethod;
    NetworkUtil mynetwork;
    Button login_text;
    Context context;
    MyCustomMethod customMethod;
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET_LOGIN=1,MESSAGE_INTERNET_UTILITES=2;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.login_main_2016);

        context = FogetPin.this;
        progress1 = new ProgressDialog(this);
        loginId = (EditText) findViewById(R.id.userid_2016);
        loginpwd = (EditText) findViewById(R.id.pass_2016);
        companyCode = (EditText) findViewById(R.id.ccode_2016);
        pin = (EditText) findViewById(R.id.pin_2016);
        repin = (EditText) findViewById(R.id.repin_2016);


        customVariablesAndMethod=Custom_Variables_And_Method.getInstance(context);

        cbohelp =customVariablesAndMethod.get_cbo_db_instance();
        mynetwork = new NetworkUtil(getApplicationContext());
        login_text = (Button) findViewById(R.id.submit);
        customMethod = new MyCustomMethod(context);


        loginId.setText(cbohelp.getUserName());
        companyCode.setText(cbohelp.getCompanyCode());
        loginId.setEnabled(false);
        companyCode.setEnabled(false);
        login_text.setText("Reset Pin");

        
        Bundle extras = getIntent().getExtras();

        if( null != extras && extras.getByteArray("picture") !=null  ) {

            byte[] byteArray = extras.getByteArray("picture");
            assert byteArray != null;
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ImageView image = (ImageView) findViewById(R.id.center_logo);
            image.setImageBitmap(bmp);
            String logo = Base64.encodeToString(byteArray, Base64.DEFAULT);
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"logo",logo);

        }else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"logo",null)!= null){
            byte[] byteArray = Base64.decode(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"logo",null), Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ImageView image = (ImageView) findViewById(R.id.center_logo);
            image.setImageBitmap(bmp);
        }else {
                customVariablesAndMethod.deleteFmcg_ByKey(context, "logo");
        }

        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(FogetPin.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(FogetPin.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(FogetPin.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(FogetPin.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(FogetPin.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions(FogetPin.this, new String[] { Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 22);
                   // LoginMain.this.checkAndRequestPermission();
                }else {
                    mylog = loginId.getText().toString();
                    mypass = loginpwd.getText().toString();
                    company_code = companyCode.getText().toString();
                    pincode = pin.getText().toString();
                    repincode = repin.getText().toString();

                   // if (!customVariablesAndMethod.isBackgroundServiceRunning(context)) {
                        //startService(new Intent(context, MyLoctionService.class));
                    //startLoctionService();
                   // }


                    networkStatus = NetworkUtil.getConnectivityStatusString(getApplicationContext());

                    if (networkStatus.equals("Not connected to Internet")) {
                        customVariablesAndMethod.Connect_to_Internet_Msg(context);
                    } else if (mylog.equals("") || mypass.equals("") || company_code.equals("") || (pincode.equals("")) || repincode.equals("")) {
                        customVariablesAndMethod.snackBar("Field can't be empty.", v);
                    } else {

                        if (!pincode.equals(repincode)) {
                            customVariablesAndMethod.snackBar("Pin Not Matched", v);
                        } else

                        {
                            Login();
                        }
                    }
                }
            }
        });
    }



    public void Login(){

        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", company_code);
        request.put("USERNAME", mylog);
        request.put("PASSWORD",mypass);
        request.put("MobileID", MyCustumApplication.getInstance().getUser().getIMEI());
        request.put("MobileVersion",MyCustumApplication.getInstance().getUser().getAppVersion());

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(-1);  // to get all the tables

        progress1.setMessage("Please Wait.. \n Login in progress...");
        progress1.setCancelable(false);
        progress1.show();


        new CboServices(this, mHandler).customMethodForAllServices(request, "LOGIN_MOBILE_1", MESSAGE_INTERNET_LOGIN, tables);

        //End of call to service
    }



    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_LOGIN:
                    //progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_login(msg.getData());

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



    private void parser_login(Bundle result) {
        if (result!=null ) {

            try {

                // table 0-3 for login
                // table 4-8 for future use
                // table 9-10 for getDCRID


                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    if (c.getString("STATUS").equals("Y")) {
                       //process_login_data(result);
                        cbohelp.insertdata(company_code, mylog, mypass, pin.getText().toString());
                        progress1.dismiss();
                        finish();
                    }else if (c.getString("STATUS").equals("L")) {
                        startActivity(new Intent(getApplicationContext(), Load_New.class));
                        progress1.dismiss();
                    }else{
                        customVariablesAndMethod.getAlert(context,"Alert !!!",c.getString("STATUS"));
                        progress1.dismiss();
                    }
                }



            } catch (JSONException e) {
                progress1.dismiss();
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        //progress1.dismiss();
    }




    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    //mycon.msgBox("Permission Granted For Location");

                    //startService(new Intent(this, MyLoctionService.class));
                    //startLoctionService();


                } else {
                    customVariablesAndMethod.msgBox(context,"For Acessing Service you Need to Allow Permission");


                }
            }
            case MY_PERMISSIONS_REQUEST_DEVICE_ID: {

                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    // mycon.msgBox("Permission Granted For Device IMEI.......");

                } else {
                    customVariablesAndMethod.msgBox(context,"For Acessing Service Need to Allow Permission");
                }


            }
        }


    }


}
