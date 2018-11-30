package com.cbo.cbomobilereporting.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.google.android.gms.location.LocationSettingsStates;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import services.CboServices;
import utils.CBOUtils.SystemArchitecture;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit on 5/9/2015.
 */
public class LoginMain extends CustomActivity {
    private static final int MY_PERMISSIONS_REQUEST_DEVICE_ID = 22;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 11;
    public final static int REQUEST_CODE = 10101;
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

        context = LoginMain.this;
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
            startLoctionService();
            // }


            new SystemArchitecture(context).getDEVICE_ID(context);

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

        Custom_Variables_And_Method.GCMToken=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"GCMToken");
        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", company_code);
        request.put("USERNAME", mylog);
        request.put("PASSWORD",mypass);
        request.put("MobileID",SystemArchitecture.COMPLETE_DEVICE_INFO);
        request.put("MobileVersion",Custom_Variables_And_Method.VERSION);
        request.put("GCM_TOCKEN", Custom_Variables_And_Method.GCMToken);

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(-1);  // to get all the tables

        progress1.setMessage("Please Wait.. \n Login in progress...");
        progress1.setCancelable(false);
        progress1.show();


        new CboServices(this, mHandler).customMethodForAllServices(request, "LOGIN_MOBILE_2", MESSAGE_INTERNET_LOGIN, tables);

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
                case MESSAGE_INTERNET_UTILITES:
                    //progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_utilites(msg.getData());

                    }
                    break;

                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error")+ customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"WEBSERVICE_URL","javed"));
                    }
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };

    private void parser_utilites(Bundle result) {
        if (result != null) {

            try {

                // table 0-11 for getitemlistforlocal
                // table 12-13 for fmgcddl_2


                //getItemforLocal


                JSONArray jsonArray11 = new JSONArray(result.getString("Tables0"));
                JSONArray jsonArray12 = new JSONArray(result.getString("Tables1"));
                JSONArray jsonArray13 = new JSONArray(result.getString("Tables2"));
                JSONArray jsonArray14 = new JSONArray(result.getString("Tables3"));
                JSONArray jsonArray15 = new JSONArray(result.getString("Tables4"));
                JSONArray jsonArray16 = new JSONArray(result.getString("Tables5"));
                JSONArray jsonArray17 = new JSONArray(result.getString("Tables6"));
                JSONArray jsonArray18 = new JSONArray(result.getString("Tables7"));
                JSONArray jsonArray19 = new JSONArray(result.getString("Tables8"));
                JSONArray jsonArray20 = new JSONArray(result.getString("Tables9"));
                JSONArray jsonArray22 = new JSONArray(result.getString("Tables11"));

                for (int a = 0; a < jsonArray11.length(); a++) {
                    JSONObject jasonObj1 = jsonArray11.getJSONObject(a);
                    cbohelp.insertProducts(jasonObj1.getString("ITEM_ID"), jasonObj1.getString("ITEM_NAME"), Double.parseDouble(jasonObj1.getString("STK_RATE")), jasonObj1.getString("GIFT_TYPE"),jasonObj1.getString("SHOW_ON_TOP"),jasonObj1.getString("SHOW_YN"));
                    Log.e("%%%%%%%%%%%%%%%", "item insert");

                }
                                /*for (int b = 0; b<jsonArray2.length();b++){
                                    JSONObject jasonObj2 = jsonArray2.getJSONObject(b);
                                    val=cbohelper.insertDoctorData(jasonObj2.getString("DR_ID"), jasonObj2.getString("ITEM_ID"),jasonObj2.getString("item_name"));
                                    Log.e("%%%%%%%%%%%%%%%", "doctor insert");

                                }*/
                for (int c = 0; c < jsonArray14.length(); c++) {

                    JSONObject jsonObject3 = jsonArray14.getJSONObject(c);
                    cbohelp.insert_phallmst(jsonObject3.getInt("ID"), jsonObject3.getString("TABLE_NAME"), jsonObject3.getString("FIELD_NAME"), jsonObject3.getString("REMARK"));
                    Log.e("%%%%%%%%%%%%%%%", "allmst_insert");
                }

                for (int d = 0; d < jsonArray15.length(); d++) {

                    JSONObject jsonObject4 = jsonArray15.getJSONObject(d);
                    cbohelp.insert_phparty(jsonObject4.getInt("PA_ID"), jsonObject4.getString("PA_NAME"),
                            jsonObject4.getInt("DESIG_ID"), jsonObject4.getString("CATEGORY"), jsonObject4.getInt("HQ_ID")
                            , jsonObject4.getString("PA_LAT_LONG"), jsonObject4.getString("PA_LAT_LONG2"),
                            jsonObject4.getString("PA_LAT_LONG3"), jsonObject4.getString("SHOWYN"));
                    Log.e("%%%%%%%%%%%%%%%", "party_insert");

                }
                for (int e = 0; e < jsonArray16.length(); e++) {

                    JSONObject jsonObject5 = jsonArray16.getJSONObject(e);
                    cbohelp.insert_phrelation(jsonObject5.getInt("PA_ID"), jsonObject5.getInt("UNDER_ID"), jsonObject5.getInt("RANK"));
                    Log.e("%%%%%%%%%%%%%%%", "relation_insert");

                }

                for (int f = 0; f < jsonArray17.length(); f++) {

                    JSONObject jsonObject6 = jsonArray17.getJSONObject(f);
                    cbohelp.insert_phitempl(jsonObject6.getString("ITEM_ID"), jsonObject6.getString("DR_SPL_ID"), jsonObject6.getInt("SRNO"));
                    Log.e("%%%%%%%%%%%%%%%", "" + jsonObject6.getInt("SRNO"));


                }
                for (int f = 0; f < jsonArray18.length(); f++) {

                    JSONObject jsonObject7 = jsonArray18.getJSONObject(f);

                    cbohelp.insert_FtpData(jsonObject7.getString("WEB_IP"), jsonObject7.getString("WEB_USER"), jsonObject7.getString("WEB_PWD"), jsonObject7.getString("WEB_PORT"), jsonObject7.getString("WEB_ROOT_PATH"));
                    Log.e("%%%%%%%%%%%%%%%", "ftp_insert");
                }
                for (int g = 0; g < jsonArray20.length(); g++) {
                    JSONObject jsonObject9 = jsonArray20.getJSONObject(g);
                    count = jsonObject9.getInt("NO_DR");
                    chem_count = jsonObject9.getInt("NO_CHEM");
                }

                JSONObject jsonObjectLoginUrl = jsonArray22.getJSONObject(0);
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "Login_Url", jsonObjectLoginUrl.getString("LOGIN_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DR_ADDNEW_URL", jsonObjectLoginUrl.getString("DR_ADDNEW_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "CHEM_ADDNEW_URL", jsonObjectLoginUrl.getString("CHEM_ADDNEW_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DRSALE_ADDNEW_URL", jsonObjectLoginUrl.getString("DRSALE_ADDNEW_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "TP_ADDNEW_URL", jsonObjectLoginUrl.getString("TP_ADDNEW_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "CHALLAN_ACK_URL", jsonObjectLoginUrl.getString("CHALLAN_ACK_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "SECONDARY_SALE_URL", jsonObjectLoginUrl.getString("SECONDARY_SALE_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "TP_APPROVE_URL", jsonObjectLoginUrl.getString("TP_APPROVE_URL"));

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "PERSONAL_INFORMATION_URL", jsonObjectLoginUrl.getString("PERSONAL_INFORMATION_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "CHANGE_PASSWORD_URL", jsonObjectLoginUrl.getString("CHANGE_PASSWORD_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "CIRCULAR_URL", jsonObjectLoginUrl.getString("CIRCULAR_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DECLARATION_OF_SAVING_URL", jsonObjectLoginUrl.getString("DECLARATION_OF_SAVING_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "SALARY_SLIP_URL", jsonObjectLoginUrl.getString("SALARY_SLIP_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "FORM16_URL", jsonObjectLoginUrl.getString("FORM16_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "ROUTE_MASTER_URL", jsonObjectLoginUrl.getString("ROUTE_MASTER_URL"));
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "HOLIDAY_URL", jsonObjectLoginUrl.getString("HOLIDAY_URL"));


                // fmcgddl_2
                SharedPreferences.Editor editor = context.getSharedPreferences(Custom_Variables_And_Method.FMCG_PREFRENCE, context.MODE_PRIVATE).edit();
                JSONArray jsonArray23 = new JSONArray(result.getString("Tables12"));
                JSONArray jsonArray24 = new JSONArray(result.getString("Tables13"));
                for (int i = 0; i < jsonArray23.length(); i++) {
                    JSONObject c = jsonArray23.getJSONObject(i);
                    editor.putString("fmcg_value", c.getString("FMCG"));
                    editor.putString("root_needed", c.getString("ROUTE"));
                    editor.putString("gps_needed", c.getString("GPRSYN"));
                    editor.putString("version", c.getString("VER"));
                    editor.putString("doryn", c.getString("DORYN"));
                    editor.putString("dosyn", c.getString("DOSYN"));
                    editor.putString("internet", c.getString("INTERNET_RQD"));
                    editor.putString("live_km", c.getString("LIVE_KM"));
                    editor.putString("leave_yn", c.getString("LEAVEYN"));
                    editor.putString("WEBSERVICE_URL", c.getString("WEBSERVICE_URL"));
                    editor.putString("WEBSERVICE_URL_ALTERNATE", c.getString("WEBSERVICE_URL_ALTERNATE"));
                    editor.putString("FLASHYN", c.getString("FLASHYN"));
                    //editor.putString("FLASHYN", c.getString("FLASHYN"));
                    editor.putString("DCR_REMARK_NA", c.getString("DCR_REMARK_NA"));
                    editor.putString("DCR_DR_REMARKYN", c.getString("DCR_DR_REMARKYN"));
                    editor.putString("ROUTEDIVERTYN", c.getString("ROUTEDIVERTYN"));
                    editor.putString("DCR_ADDAREANA", c.getString("DCR_ADDAREANA"));
                    editor.putString("VISUALAIDPDFYN", c.getString("VISUALAIDPDFYN"));
                    editor.putString("SAMPLE_POB_MANDATORY", c.getString("SAMPLE_POB_MANDATORY"));
                    editor.putString("REMARK_WW_MANDATORY", c.getString("REMARK_WW_MANDATORY"));
                    editor.putString("SAMPLE_POB_INPUT_MANDATORY", c.getString("SAMPLE_POB_INPUT_MANDATORY"));
                    editor.putString("MISSED_CALL_OPTION", c.getString("MISSED_CALL_OPTION"));
                    editor.putString("APPRAISALMANDATORY", c.getString("APPRAISALMANDATORY"));
                    editor.putString("USER_NAME", c.getString("USER_NAME"));
                    editor.putString("PASSWORD", c.getString("PASSWORD"));
                    editor.putString("VISUALAID_DRSELITEMYN", c.getString("VISUALAID_DRSELITEMYN"));
                    editor.putString("DOB_REMINDER_HOUR", c.getString("DOB_REMINDER_HOUR"));
                    editor.putString("SYNCDRITEMYN", c.getString("SYNCDRITEMYN"));
                    editor.putString("GEO_FANCING_KM", c.getString("GEO_FANCING_KM"));
                    editor.putString("FIRST_CALL_LOCK_TIME", c.getString("FIRST_CALL_LOCK_TIME"));
                    editor.putString("mark", c.getString("FLASH_MESSAGE"));
                    editor.putString("NOC_HEAD", c.getString("NOC_HEAD"));
                    editor.putString("USER_PIC", c.getString("USER_PIC"));
                    editor.putString("DCR_LETREMARK_LENGTH", c.getString("DCR_LETREMARK_LENGTH"));
                    editor.putString("SAMPLEMAXQTY", c.getString("SAMPLEMAXQTY"));
                    editor.putString("POBMAXQTY", c.getString("POBMAXQTY"));
                    editor.putString("ASKUPDATEYN", c.getString("ASKUPDATEYN"));
                    editor.putString("MOBILEDATAYN", c.getString("MOBILEDATAYN"));
                    editor.putString("CALLWAITINGTIME", c.getString("CALLWAITINGTIME"));
                    editor.putString("COMPANY_PIC", c.getString("COMPANY_PIC"));
                    editor.putString("RE_REG_KM", c.getString("RE_REG_KM"));
                    editor.putString("ERROR_EMAIL", c.getString("ERROR_EMAIL"));
                    editor.putString("DIVERT_REMARKYN", c.getString("DIVERT_REMARKYN"));
                    editor.putString("NLC_PIC_YN", c.getString("NLC_PIC_YN"));
                    editor.putString("RX_MAX_QTY", c.getString("RX_MAX_QTY"));
                    editor.putString("SHOW_ADD_REGYN", c.getString("SHOW_ADD_REGYN"));
                    editor.putString("EXP_ATCH_YN", c.getString("EXP_ATCH_YN"));
                    editor.putString("FARMERADDFIELDYN", c.getString("FARMERADDFIELDYN"));
                    editor.putString("NO_DR_CALL_REQ", c.getString("NO_DR_CALL_REQ"));
                    editor.putString("DR_RX_ENTRY_YN", c.getString("DR_RX_ENTRY_YN"));
                    editor.putString("RETAILERCHAINYN", c.getString("RETAILERCHAINYN"));
                    editor.putString("DCR_SUBMIT_TIME", c.getString("DCR_SUBMIT_TIME"));
                    editor.putString("DCR_SUBMIT_SPEACH", c.getString("DCR_SUBMIT_SPEACH"));
                    editor.putString("ALLOWED_APP", c.getString("ALLOWED_APP"));
                    editor.putString("DCRGIFT_QTY_VALIDATE", c.getString("DCRGIFT_QTY_VALIDATE"));
                    editor.putString("SAMPLE_BTN_CAPTION", c.getString("SAMPLE_BTN_CAPTION"));
                    editor.putString("GIFT_BTN_CAPTION", c.getString("GIFT_BTN_CAPTION"));
                    editor.putString("DIVERTWWYN", c.getString("DIVERTWWYN"));
                    editor.putString("PIN_ALLOWED_MSG", c.getString("PIN_ALLOWED_MSG"));
                    editor.putString("CMC3_GALLERY_REQ", c.getString("CMC3_GALLERY_REQ"));
                    editor.putString("DR_COLOR", c.getString("DR_COLOR"));
                    editor.putString("DCRPPNA", c.getString("DCRPPNA"));
                    editor.putString("DR_SALE_URL", c.getString("DR_SALE_URL"));
                    editor.commit();

                }

                cbohelp.deleteMenu();
                for (int i = 0; i < jsonArray24.length(); i++) {
                    JSONObject object = jsonArray24.getJSONObject(i);
                    String menu = object.getString("MAIN_MENU");
                    String menu_code = object.getString("MENU_CODE");
                    String menu_name = object.getString("MENU_NAME");
                    String menu_url = object.getString("URL");
                    String main_menu_srno = object.getString("MAIN_MENU_SRNO");
                    cbohelp.insertMenu(menu, menu_code, menu_name, menu_url, main_menu_srno);
                }

                    /*Custom_Variables_And_Method.ip = "0";
                    Custom_Variables_And_Method.user = "0";
                    Custom_Variables_And_Method.pwd = "0";
                    Custom_Variables_And_Method.db = "0";

                    cbohelp.insertLoginDetail(company_code, ols_ip, ols_db_name, ols_db_user, ols_db_password, version_new);*/

                cbohelp.insertdata(company_code, mylog, mypass, pin.getText().toString());
                cbohelp.deleteVersion();
                cbohelp.insertVersionInLocal(Custom_Variables_And_Method.VERSION);
                customMethod.notification_check();
                /*if (Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
                    startActivity(new Intent(getApplicationContext(), PersonalInfo.class));

                } else {*/
                    startActivity(new Intent(getApplicationContext(), ViewPager_2016.class));

                //}

                finish();
            } catch (JSONException e) {
                progress1.dismiss();
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }
        }
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
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,
                                "ShowSystemAlert","Y");
                       process_login_data(result);
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


                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"work_type_Selected","w");

                //progress1.dismiss();
                //Start of call to service

                HashMap<String, String> request = new HashMap<>();
                request.put("sCompanyFolder", company_code);
                request.put("iPaId", ""+ Custom_Variables_And_Method.PA_ID );

                ArrayList<Integer> tables = new ArrayList<>();
                tables.add(-1);  // to get all the tables

                progress1.setMessage("Downloading Miscellaneous data.." + "\n" + "please wait");
               // progress1.setCancelable(false);
               // progress1.show();


                new CboServices(this, mHandler).customMethodForAllServices(request, "GetItemListForLocal", MESSAGE_INTERNET_UTILITES, tables);

                //End of call to service



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
                    startLoctionService();


                } else {
                    customVariablesAndMethod.msgBox(context,"For Acessing Service you Need to Allow Permission");


                }
            }
            case MY_PERMISSIONS_REQUEST_DEVICE_ID: {

                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    new SystemArchitecture(context).getDEVICE_ID(context);
                    // mycon.msgBox("Permission Granted For Device IMEI.......");
                    preLogin(login_text);
                } else {
                    customVariablesAndMethod.msgBox(context,"For Acessing Service Need to Allow Permission");
                }


            }
        }


    }


}
