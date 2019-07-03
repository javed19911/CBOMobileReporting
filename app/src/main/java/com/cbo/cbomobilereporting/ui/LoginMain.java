package com.cbo.cbomobilereporting.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.cbo.cbomobilereporting.ui_new.utilities_activities.PersonalInfo;
import com.google.android.gms.location.LocationSettingsStates;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.Response;
import com.uenics.javed.CBOLibrary.ResponseBuilder;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import services.CboServices;
import services.MyAPIService;
import utils.networkUtil.NetworkUtil;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.Service_Call_From_Multiple_Classes;


public class LoginMain extends CustomActivity {
    private static final int MY_PERMISSIONS_REQUEST_DEVICE_ID = 22;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 11;
    public final static int REQUEST_CODE = 10101;
    EditText loginId, loginpwd, companyCode, pin, repin;
    CBO_DB_Helper cbohelp;
    String mylog, mypass, pincode, repincode;
    String company_code;
    String networkStatus;

    Custom_Variables_And_Method customVariablesAndMethod;
    NetworkUtil mynetwork;
    Button login_text;
    Context context;
    MyCustomMethod customMethod;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.login_main_2016);

        context = this;
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

        customVariablesAndMethod.deleteFmcg_ByKey(context,"WEBSERVICE_URL");
        MyCustumApplication.getInstance().getUser().setLoggedInAsSupport(false);

        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               preLogin(v);
            }
        });
    }

    public void preLogin(View v){
        if (!checkDrawOverlayPermission()) {
            //allow permission
        }else if(ContextCompat.checkSelfPermission(LoginMain.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(LoginMain.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(LoginMain.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(LoginMain.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(LoginMain.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            //takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(LoginMain.this, new String[] { Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSIONS_REQUEST_DEVICE_ID);
            // LoginMain.this.checkAndRequestPermission();
        }else {
            mylog = loginId.getText().toString();
            mypass = loginpwd.getText().toString();
            company_code = companyCode.getText().toString();
            pincode = pin.getText().toString();
            repincode = repin.getText().toString();

            // if (!customVariablesAndMethod.isBackgroundServiceRunning(context)) {
                //startService(new Intent(context, MyLoctionService.class));
            //startLoctionService(true);
            // }



            networkStatus = NetworkUtil.getConnectivityStatusString(getApplicationContext());

            if (networkStatus.equals("Not connected to Internet")) {
                customVariablesAndMethod.snackBar("Not connected to Internet", v);
            } else if (mylog.equals("") || mypass.equals("") || company_code.equals("") || (pincode.equals("")) || repincode.equals("")) {
                customVariablesAndMethod.snackBar("Field can't be empty.", v);
            } else {

                if (!pincode.equals(repincode)) {
                    customVariablesAndMethod.snackBar("Pin Not Matched", v);
                } else

                {
                    Custom_Variables_And_Method.COMPANY_CODE = companyCode.getText().toString();
                    cbohelp.deleteLoginDetail();
                    cbohelp.deleteDoctorItemPrescribe();
                    cbohelp.deleteUserDetail();
                    cbohelp.deleteUserDetail2();
                    cbohelp.deleteMenu();
                    cbohelp.deleteResigned();
                    cbohelp.deleteDoctorItem();
                    cbohelp.deleteFinalDcr();
                    cbohelp.deleteDCRDetails();
                    cbohelp.deletedcrFromSqlite();
                    cbohelp.deleteTempChemist();
                    cbohelp.deleteChemistSample();
                    cbohelp.deleteChemistRecordsTable();
                    cbohelp.deleteStockistRecordsTable();
                    cbohelp.deleteTempStockist();
                    cbohelp.deleteTempDr();
                    cbohelp.deleteDoctorRc();
                    cbohelp.deleteDoctor();
                    cbohelp.deleteDoctormore();

                    cbohelp.delete_phitem();
                    cbohelp.delete_phdoctoritem();
                    //cbohelper.delete_phdoctor();
                    cbohelp.delete_phallmst();
                    cbohelp.delete_phparty();
                    cbohelp.delete_phrelation();
                    cbohelp.delete_phitemspl();
                    cbohelp.deleteFTPTABLE();


                    cbohelp.delete_Expense();
                    cbohelp.delete_Nonlisted_calls();
                    cbohelp.deleteDcrAppraisal();
                    cbohelp.delete_tenivia_traker();
                    cbohelp.notificationDeletebyID(null);
                    cbohelp.delete_Lat_Long_Reg();
                    cbohelp.delete_phdairy_dcr(null);
                    cbohelp.delete_Item_Stock();

                    customVariablesAndMethod.deleteFmcg_ByKey(context,"DCR_ID");
                    customVariablesAndMethod.deleteFmcg_ByKey(context,"DcrPlantime");
                    customVariablesAndMethod.deleteFmcg_ByKey(context,"D_DR_RX_VISITED");
                    customVariablesAndMethod.deleteFmcg_ByKey(context,"CHEMIST_NOT_VISITED");
                    customVariablesAndMethod.deleteFmcg_ByKey(context,"STOCKIST_NOT_VISITED");
                    customVariablesAndMethod.deleteFmcg_ByKey(context,"dcr_date_real");
                    customVariablesAndMethod.deleteFmcg_ByKey(context,"Dcr_Planed_Date");
                    customVariablesAndMethod.deleteFmcg_ByKey(context,"CUR_DATE");

                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DA_TYPE","0");
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"da_val","0");
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"distance_val","0");
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CALL_UNLOCK_STATUS","");

                    Custom_Variables_And_Method.user_name = mylog;
                    //cbohelp.close();

                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"WEBSERVICE_URL","");

                    Login();
                }
            }
        }


    }


    public void Login(){

        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", company_code);
        request.put("USERNAME", mylog);
        request.put("PASSWORD",mypass);
        request.put("MobileID",MyCustumApplication.getInstance().getUser().getIMEI());
        request.put("MobileVersion",MyCustumApplication.getInstance().getUser().getAppVersion());
        request.put("GCM_TOCKEN", MyCustumApplication.getInstance().getUser().getGCMToken());



        new MyAPIService(context)
                .execute(new ResponseBuilder("LOGIN_MOBILE_2", request)
                        .setDescription("Please Wait.. \n Login in progress...").setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) {

                                parser_login(message);
                            }

                            @Override
                            public void onResponse(Bundle response) {

                            }

                            @Override
                            public void onError(String message, String description) {
                                AppAlert.getInstance().getAlert(context,message,description);
                            }


                        })
                );
    }




    //8372amit
    //8372
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
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "ShowSystemAlert","Y");

                        process_login_data(result);

                    }else if (c.getString("STATUS").equals("L")) {
                        startActivity(new Intent(getApplicationContext(), Load_New.class));
                        //progress1.dismiss();
                    }else if (!c.getString("MOBILE_ID_ORIGINAL").equals("") && !c.getString("MOBILE_VERSION_ORIGINAL").equals("")) {
                        String MOBILE_ID_ORIGINAL = c.getString("MOBILE_ID_ORIGINAL");
                        if (c.getInt("MOBILE_VERSION_ORIGINAL")> Integer.parseInt( MyCustumApplication.getInstance().getUser().getAppVersion())){
                            AppAlert.getInstance().DecisionAlert(context, "Alert !!!", "User is currently Logged-In with some other device\n" +
                                            "To Login as Support...\nPlease update the Your App...",
                                    new AppAlert.OnClickListener() {
                                        @Override
                                        public void onPositiveClicked(View item, String result) {
                                            Intent i = new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("https://play.google.com/store/apps/details?id=com.cbo.cbomobilereporting&hl=en"));
                                            context.startActivity(i);
                                        }

                                        @Override
                                        public void onNegativeClicked(View item, String result) {

                                        }
                                    });
                        }else if (c.getInt("MOBILE_VERSION_ORIGINAL")< Integer.parseInt( MyCustumApplication.getInstance().getUser().getAppVersion())){
                            AppAlert.getInstance().getAlert(context,"Alert !!!","User is currently Logged-In with some other device\n" +
                                    "To Login as Support...\nPlease ask the user to update his App from Play Store....");
                        }else{
                            AppAlert.getInstance().setPositiveTxt("Support LogIn?").DecisionAlert(context, "Alert !!!", "User is currently Logged-In with some other device\n" +
                                            "Do You want to Login for Support?",
                                    new AppAlert.OnClickListener() {
                                        @Override
                                        public void onPositiveClicked(View item, String result) {
                                            MyCustumApplication.getInstance().getUser().setLoggedInAsSupport(true);
                                            MyCustumApplication.getInstance().getUser().setIMEIasSupport(MOBILE_ID_ORIGINAL);
                                            Login();
                                        }

                                        @Override
                                        public void onNegativeClicked(View item, String result) {

                                        }
                                    });
                        }
                        //AppAlert.getInstance().getAlert(context,"Alert !!!",c.getString("STATUS"));
                        //progress1.dismiss();
                    }else{
                        AppAlert.getInstance().getAlert(context,"Alert !!!",c.getString("STATUS"));
                        //progress1.dismiss();
                    }
                }



            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        //progress1.dismiss();
    }

    private void process_login_data(Bundle result)  {

        //for login
        if (result!=null ) {

            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);

                   // Custom_Variables_And_Method.PA_ID = Integer.parseInt(c.getString("PA_ID"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "UAN_NO", c.getString("UAN_NO"));
                    Custom_Variables_And_Method.PA_ID = Integer.parseInt(c.getString("PA_ID"));
                    Custom_Variables_And_Method.PA_NAME = c.getString("PA_NAME");
                    Custom_Variables_And_Method.HEAD_QTR = c.getString("HEAD_QTR");
                    Custom_Variables_And_Method.DESIG = c.getString("DESIG");
                    Custom_Variables_And_Method.pub_desig_id = c.getString("DESIG_ID");
                    Custom_Variables_And_Method.COMPANY_NAME = c.getString("COMPANY_NAME");
                    Custom_Variables_And_Method.WEB_URL = c.getString("WEB_URL");

                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "FM_PA_ID", c.getString("PA_ID"));

                    long val = cbohelp.insertUserDetails(Custom_Variables_And_Method.PA_ID, Custom_Variables_And_Method.PA_NAME, Custom_Variables_And_Method.HEAD_QTR, Custom_Variables_And_Method.DESIG, Custom_Variables_And_Method.pub_desig_id, Custom_Variables_And_Method.COMPANY_NAME, Custom_Variables_And_Method.WEB_URL);
                    Log.e("login details", "" + val);

                }
                String table1 = result.getString("Tables1");
                JSONArray jsonArray2 = new JSONArray(table1);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);
                    Custom_Variables_And_Method.location_required = c.getString("LOCATION_REQUIRED");
                    Custom_Variables_And_Method.VISUAL_REQUIRED = c.getString("show_visual_aid");

                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "VisualAid_YN", c.getString("show_visual_aid"));
                    long valid = cbohelp.insertUserDetail22(Custom_Variables_And_Method.location_required, Custom_Variables_And_Method.VISUAL_REQUIRED);
                    Log.e("location and l detail", "" + valid);
                }

                //for getDCRID

                String table9 = result.getString("Tables9");
                JSONArray jsonArray10 = new JSONArray(table9);
                for (int k = 0; k < jsonArray10.length(); k++) {
                    JSONObject obj = jsonArray10.getJSONObject(k);
                    Custom_Variables_And_Method.DCR_ID = obj.getString("DCR_ID");
                    Custom_Variables_And_Method.pub_area = obj.getString("AREA");
                    Custom_Variables_And_Method.WORKING_TYPE = "Working";
                    long val = cbohelp.insertDcrDetails(Custom_Variables_And_Method.DCR_ID, Custom_Variables_And_Method.pub_area);
                    long value = cbohelp.putDcrId(Custom_Variables_And_Method.DCR_ID);
                }


                cbohelp.insertdata(company_code, mylog, mypass, pin.getText().toString());
                cbohelp.deleteVersion();
                cbohelp.insertVersionInLocal(Custom_Variables_And_Method.VERSION);

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"work_type_Selected","w");

                //progress1.dismiss();

               new Service_Call_From_Multiple_Classes().getListForLocal(context, new Response() {
                   @Override
                   public void onSuccess(Bundle bundle) {

                        customMethod.notification_check();
                        if (Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
                            startActivity(new Intent(getApplicationContext(), PersonalInfo.class));

                        } else {
                        startActivity(new Intent(getApplicationContext(), ViewPager_2016.class));

                        }

                        finish();
                   }

                   @Override
                   public void onError(String message, String description) {
                       AppAlert.getInstance().getAlert(context,message,description);
                   }
               });



            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }
        }
    }


    public void onBackPressed() {

        customVariablesAndMethod.deleteFmcg_ByKey(context, "logo");
        super.onBackPressed();

    }


    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent();
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
        switch (requestCode) {
            case REQUEST_CODE :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        Toast.makeText(context, "Please allow the permission to Login", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
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
                    preLogin(login_text);
                } else {
                    customVariablesAndMethod.msgBox(context,"For Acessing Service Need to Allow Permission");
                }


            }
        }


    }


}
