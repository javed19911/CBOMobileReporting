package com.cbo.cbomobilereporting.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;

import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.FingerprintHandler;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DCR_Summary_new;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.FinalSubmitDcr_new;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsStates;

import services.Sync_service;

import com.cbo.cbomobilereporting.MyCustumApplication;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import utils.networkUtil.AppPrefrences;
import utils.networkUtil.NetworkUtil;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.Service_Call_From_Multiple_Classes;

public class LoginFake extends CustomActivity implements  LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public final static int REQUEST_CODE = 10101;
    public EditText pin;
    Button login;

    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cbohelp;
    TextView reset_pin;
    String GPS_NEEDED;
    Context context;
    TextView version;
    MyCustomMethod myCustomMethod;
    String live_km;
    private final int REQUEST_CHECK_SETTINGS = 1000;
    View view;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    public static final int REQUEST_PERMISSION = 1,REQUEST_FINGERPRINT_PERMISSION =2;
    private byte[] byteArray =null;
    Bundle extras;
    Boolean longClick=false;
    public String MyPin ="";


    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "CBOFingerPrint";
    private Cipher cipher;

    FingerprintHandler helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_pin_2016);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        context = LoginFake.this;

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance(context);

        cbohelp = customVariablesAndMethod.get_cbo_db_instance();
        //cbohelp.getDCR_ID_FromLocal();
        myCustomMethod = new MyCustomMethod(context);
        pin = (EditText) findViewById(R.id.pin_again22);
        login = (Button) findViewById(R.id.submit_login22_enter_pin);
        version = (TextView) findViewById(R.id.version_code);
        reset_pin = (TextView) findViewById(R.id.reset_pin_enter_pin);
        version.setText("Version :" + Custom_Variables_And_Method.VERSION);
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"MethodCallFinal", "N");
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Tracking", "N");

        Custom_Variables_And_Method.PA_ID = Integer.parseInt(cbohelp.getPaid());
        Custom_Variables_And_Method.PA_NAME = cbohelp.getPaName();
        Custom_Variables_And_Method.HEAD_QTR = cbohelp.getHeadQtr();
        Custom_Variables_And_Method.DESIG = cbohelp.getDESIG();
        Custom_Variables_And_Method.pub_desig_id = cbohelp.getPUB_DESIG();
        Custom_Variables_And_Method.COMPANY_NAME = cbohelp.getCOMP_NAME();
        Custom_Variables_And_Method.WEB_URL = cbohelp.getWEB_URL();
        Custom_Variables_And_Method.location_required = cbohelp.getLocationDetail();
        Custom_Variables_And_Method.VISUAL_REQUIRED = cbohelp.getVisualDetail();
        Custom_Variables_And_Method.DCR_ID = cbohelp.getDCR_ID_FromLocal();
        Custom_Variables_And_Method.COMPANY_CODE = cbohelp.getCompanyCode();
        live_km =customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"live_km");

        extras = getIntent().getExtras();
        File COMPANY_PIC= new File( Environment.getExternalStorageDirectory()+"/cbo/profile/company.png" );
        if( null != extras && extras.getByteArray("picture") !=null  ) {

            byteArray=extras.getByteArray("picture");
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
            if  (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"COMPANY_PICYN","").equalsIgnoreCase("Y")
                    && COMPANY_PIC.exists()) {

                byte bytes[] = new byte[(int) COMPANY_PIC.length()];
                BufferedInputStream bis = null;
                DataInputStream dis = null;
                try {
                    bis = new BufferedInputStream(new FileInputStream(COMPANY_PIC));
                    dis = new DataInputStream(bis);
                    dis.readFully(bytes);

                    //byteArray = bytes;
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ImageView image = (ImageView) findViewById(R.id.center_logo);
                    image.setImageBitmap(bmp);
                } catch (FileNotFoundException e) {
                    //byteArray = null;
                    e.printStackTrace();
                } catch (IOException e) {
                    //byteArray = null;
                    e.printStackTrace();
                }catch (Exception e){
                    //byteArray = null;
                    e.printStackTrace();
                }

            }
        }

        if (Custom_Variables_And_Method.DCR_ID.equals("")) {
            Custom_Variables_And_Method.DCR_ID = "0";
        }
        Custom_Variables_And_Method.pub_area = cbohelp.getPUB_AREA();
        Custom_Variables_And_Method.pub_area = cbohelp.getPUB_AREA();
        getDetailsForOffline();


        /*AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) ContextCompat.getDrawable(context, R.drawable.ic_menu_animatable);
        ImageView imageView= (ImageView) findViewById(R.id.center_logo);
        imageView.setImageDrawable(drawable);
        drawable.start();*/

        //Checking play service is available or not
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (googleAPI.isUserResolvableError(resultCode)) {
                //Displaying message that play service is not installed
                customVariablesAndMethod.msgBox(context, "Google Play Service is not install/enabled in this device!");
                googleAPI.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST,new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                finish();
                            }
                        }).show();

                //If play service is not supported
                //Displaying an error message
            }else {
                customVariablesAndMethod.msgBox(context, "This device does not support for Google Play Service!");
            }

            //If play service is available
        }


        String network_status = NetworkUtil.getConnectivityStatusString(context);
        if (!network_status.equals("Not connected to Internet")) {

            //check for notification
           // myCustomMethod.notification_check();

            if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Final_submit","N").equals("N")) {
                if (live_km.equalsIgnoreCase("Y") || (live_km.equalsIgnoreCase("Y5"))) {
                    MyCustomMethod myCustomMethod = new MyCustomMethod(context);
                    myCustomMethod.stopAlarm10Minute();
                    myCustomMethod.startAlarmIn10Minute();
                } else {
                    Sync_service.ReplyYN="N";
                    startService(new Intent(context, Sync_service.class));
                    new Thread(converAddress).start();
                }
            }

            //}
        }


/*
        WebView gif= (WebView) findViewById(R.id.center_logo1);
        //gif.loadUrl("https://mir-s3-cdn-cf.behance.net/project_modules/disp/2d37a419461997.562dad20d157d.gif");
        gif.loadUrl("file:///android_asset/test.gif");*/

        login.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                view = v;
                if (checkDrawOverlayPermission()) {
                    //new SyncAllDataFirebase(context);
                    LoginFake(false);

                    //new CustomTextToSpeech().setTextToSpeech("1");

                    /*Intent intent = new Intent(context, AttachImage.class);
                    startActivity(intent);*/

                    /*Intent intent = new Intent(context, Doctor_registration_GPS.class);
                    intent.putExtra("id",0);
                    intent.putExtra("name","hg");
                    intent.putExtra("type","S");
                    startActivity(intent);*/
                }
            }
        });


        reset_pin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), FogetPin.class);
                startActivity(i);
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initFingerprintManager();
        }


    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initFingerprintManager(){

        try{
            MyPin = cbohelp.getPin();
            // Initializing both Android Keyguard Manager and Fingerprint Manager
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            if (fingerprintManager == null){
                return;
            }
            // Check whether the device has a Fingerprint sensor.
            if(!fingerprintManager.isHardwareDetected()){
                /**
                 * An error message will be displayed if the device does not contain the fingerprint hardware.
                 * However if you plan to implement a default authentication method,
                 * you can redirect the user to a default authentication activity from here.
                 * Example:
                 * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                 * startActivity(intent);
                 */
                //textView.setText("Your Device does not have a Fingerprint Sensor");
            }else {
                // Checks whether fingerprint permission is set on manifest
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    //textView.setText("Fingerprint authentication permission not enabled");
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[] { Manifest.permission.USE_FINGERPRINT},
                            REQUEST_FINGERPRINT_PERMISSION);
                }else{
                    // Check whether at least one fingerprint is registered
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        //textView.setText("Register at least one fingerprint in Settings");
                    }else{
                        // Checks whether lock screen security is enabled or not
                        assert keyguardManager != null;
                        if (!keyguardManager.isKeyguardSecure()) {
                            // textView.setText("Lock screen security not enabled in Settings");
                        }else{
                            generateKey();


                            if (cipherInit()) {
                                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                helper = new FingerprintHandler(this);
                                helper.startAuth(fingerprintManager, cryptoObject);

                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            helper = null;
            Log.d("FingerPrint error",e.getLocalizedMessage());
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private class GetFmcg extends AsyncTask<Void, Void, String> {
        ProgressDialog commitDialog;

        Boolean isInternetConnected = false;

        @Override
        protected String doInBackground(Void... voids) {
            if (isInternetConnected) {
                new AppPrefrences(context).setDataForFMCG();
            }
            return "done";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                isInternetConnected = Custom_Variables_And_Method.internetConneted(context);
                commitDialog = new ProgressDialog(context);
                commitDialog.setMessage("Please Wait..");
                commitDialog.setCanceledOnTouchOutside(false);
                commitDialog.setCancelable(false);
                commitDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            commitDialog.dismiss();
            LoginFake(true);
        }
    }



    //19.2494793,73.1319805
    //19.2369817,73.12641


    public void LoginFake(Boolean SkipValidation){

        //initFingerprintManager();

        if (helper!= null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                helper.stopAuth();
            }
        }

        String PIN_ALLOWED_MSG =  customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"PIN_ALLOWED_MSG","");
        longClick=false;
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,
                "ShowSystemAlert","Y");
        //if (!customVariablesAndMethod.isBackgroundServiceRunning(context)) {
        //startService(new Intent(context, MyLoctionService.class));
        //startLoctionService();
        //}

        // int a = customVariablesAndMethod.isTimeAutomatic(context) ? Log.d("time" ,"Auto time Enabled") : Log.d("time" ,"Auto time Disabled") ;
        if (pin.getText().toString().toLowerCase().equals("auto")) {

            pin.setText("");
            MyCustumApplication.getInstance().ShowAutoStart();

        }else if (pin.getText().toString().equals("")) {
          // Double dis = DistanceCalculator.distance(19.3095644,72.8590536,19.3052383,72.8615262,"K");
           // Double dis = DistanceCalculator.distance(0,0,19.2369817,73.12641,"K");
            customVariablesAndMethod.snackBar("Enter your Pin First...." , view);
        } else if (pin.getText().toString().equals(cbohelp.getPin())) {

            if(!PIN_ALLOWED_MSG.equals("")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    initFingerprintManager();
                }

                if (!SkipValidation){
                    new GetFmcg().execute();
                }else{
                    AppAlert.getInstance().Alert(context, "Alert !!!",
                            PIN_ALLOWED_MSG,
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                }

                return;
            }

            String network_status = NetworkUtil.getConnectivityStatusString(context);
            if (!network_status.equals("Not connected to Internet")) {

                //check for notification
                myCustomMethod.notification_check();


                if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Final_submit","N").equals("N")) {
                    if (live_km.equalsIgnoreCase("Y") || (live_km.equalsIgnoreCase("Y5"))) {
                        MyCustomMethod myCustomMethod = new MyCustomMethod(context);
                        myCustomMethod.stopAlarm10Minute();
                        myCustomMethod.startAlarmIn10Minute();
                    } else {
                        startService(new Intent(context, Sync_service.class));
                        new Thread(converAddress).start();
                    }
                }

                //}
            }

            String dor = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"doryn","N");
            String dos = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dosyn","N");
            Integer appVersion = Integer.parseInt(Custom_Variables_And_Method.VERSION);
            Integer dbVersion = Integer.parseInt(Custom_Variables_And_Method.checkVersion);
            Integer dbVersion1 = Integer.parseInt(cbohelp.getNewVersion());
            String gpsYN = checkForGPSUsers();

            int mode=new MyCustomMethod(context).getLocationMode(context);

            if(ContextCompat.checkSelfPermission(context,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                //takePictureButton.setEnabled(false);
                ActivityCompat.requestPermissions((Activity) context, new String[] { Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                }, REQUEST_PERMISSION);
                //Toast.makeText(this, "Please allow the permission", Toast.LENGTH_LONG).show();

            }else {

                if (gpsYN.equals("Y") &&
                        (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(context,
                                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )){
                    ActivityCompat.requestPermissions((Activity) context, new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, REQUEST_PERMISSION);

                }else if (gpsYN.equals("Y") && (!myCustomMethod.checkGpsEnable() || mode != 3)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        initFingerprintManager();
                    }
                    customVariablesAndMethod.msgBox(context,"Please Swicth ON your GPS");
                    if (mode !=0){
                        customVariablesAndMethod.RequestGPSFromSetting(context);
                    }else{
                        customVariablesAndMethod.getGpsSetting(context);
                    }

                } else if ((dor != null) && (dos != null)) {
                    if (dor.equals("Y")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            initFingerprintManager();
                        }
                        customVariablesAndMethod.msgBox(context,"Please contact your Administrator");
                    } else if (dos.equals("Y")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            initFingerprintManager();
                        }
                        customVariablesAndMethod.msgBox(context,"Please contact your Administrator");
                    } else if (dbVersion > appVersion) {

                        startActivity(new Intent(context, Load_New.class));
                        finish();
                    } else {

                        if ((cbohelp.getNewVersion().equals("")) || (!(dbVersion1 > appVersion)))

                        {

                                   /* if (Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
                                        startActivity(new Intent(getApplicationContext(), PersonalInfo.class));
                                        finish();
                                    } else {*/

                            startLoctionService();
                            /*String work_type_Selected= customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_type_Selected","w");
                            switch (work_type_Selected){
                                case "l":
                                    Intent intent = new Intent(context, FinalSubmitDcr_new.class);
                                    intent.putExtra("Back_allowed","N");
                                    startActivity(intent);
                                    break;
                                case "n":
                                    Intent intent1 = new Intent(context, NonWorking_DCR.class);
                                    intent1.putExtra("Back_allowed","N");
                                    startActivity(intent1);

                                    break;
                                default:*/
                                    Custom_Variables_And_Method.GPS_STATE_CHANGED_TIME=customVariablesAndMethod.get_currentTimeStamp();
                                    startActivity(new Intent(context, ViewPager_2016.class));
                                    finish();
                            //}
                            //}
                        } else {
                            startActivity(new Intent(context, Load_New.class));
                            finish();

                        }
                    }
                } else {


                    checkForGPSUsers();

                    if (GPS_NEEDED.equals("") || GPS_NEEDED.equals(null) || GPS_NEEDED.equals("N")) {
                        if ((cbohelp.getNewVersion().equals("")) || (!(dbVersion1 > appVersion)))

                        {
                                    /*if (Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
                                        startActivity(new Intent(getApplicationContext(), PersonalInfo.class));
                                        finish();
                                    } else {*/

                            startLoctionService();
                          /*  String work_type_Selected= customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_type_Selected","w");
                            switch (work_type_Selected){
                                case "l":
                                    Intent intent = new Intent(context, FinalSubmitDcr_new.class);
                                    intent.putExtra("Back_allowed","N");
                                    startActivity(intent);
                                    break;
                                case "n":
                                    Intent intent1 = new Intent(context, NonWorking_DCR.class);
                                    intent1.putExtra("Back_allowed","N");
                                    startActivity(intent1);

                                    break;
                                default:*/
                                    startActivity(new Intent(context, ViewPager_2016.class));
                                    finish();
                            //}
                            //}
                        } else {
                            startActivity(new Intent(getApplicationContext(), Load_New.class));

                        }
                    } else if (GPS_NEEDED.equals("Y")) {

                        dcrForGPSUsers();
                    } else if ((cbohelp.getNewVersion().equals("")) || (Custom_Variables_And_Method.VERSION.equals(cbohelp.getNewVersion()))) {

                                /*if (Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
                                    startActivity(new Intent(getApplicationContext(), PersonalInfo.class));
                                    finish();
                                } else {*/
                       /* String work_type_Selected= customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_type_Selected","w");
                        switch (work_type_Selected){
                            case "l":
                                Intent intent = new Intent(context, FinalSubmitDcr_new.class);
                                intent.putExtra("Back_allowed","N");
                                startActivity(intent);
                                break;
                            case "n":
                                Intent intent1 = new Intent(context, NonWorking_DCR.class);
                                intent1.putExtra("Back_allowed","N");
                                startActivity(intent1);

                                break;
                            default:*/
                                startActivity(new Intent(context, ViewPager_2016.class));
                                finish();
                        //}
                        //}
                    } else {
                        startActivity(new Intent(getApplicationContext(), Load_New.class));

                    }
                }
            }
        } else {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View dialogLayout = inflater.inflate(R.layout.alert_view, null);
            final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
            final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
            final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
            final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.negative);

            Alert_Nagative.setVisibility(View.VISIBLE);
            if (IscallsFound() && !MyCustumApplication.getInstance().getUser().getLoggedInAsSupport()){
                Alert_Nagative.setText("Forgot pin ?");
            }else {
                Alert_Nagative.setText("Logout!");
            }

            Alert_Positive.setText("Try Again");
            Alert_title.setText("Invalid Pin!!!");
            Alert_message.setText("You entered a wrong pin\n Re-Enter your pin and try again");

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

            final AlertDialog dialog = builder1.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setView(dialogLayout);
            Alert_Positive.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    pin.setText("");
                    customVariablesAndMethod.msgBox(context,"Please Enter Correct Pin And Try Again");
                    dialog.dismiss();

                }
            });
            Alert_Nagative.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (IscallsFound() && !MyCustumApplication.getInstance().getUser().getLoggedInAsSupport()){
                        Intent i = new Intent(getApplicationContext(), FogetPin.class);
                        startActivity(i);
                    }else {
                        reset_pin_delete_all_calls();
                    }
                    dialog.dismiss();
                }
            });
            Alert_message.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClick=true;
                    customVariablesAndMethod.msgBox(context,"Logout Unlocked");
                    return true;
                }
            });
            Alert_title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (longClick) {
                        if (IscallsFound()){
                            Intent i = new Intent(getApplicationContext(), DCR_Summary_new.class);
                            i.putExtra("who", 1);
                            startActivity(i);
                            dialog.dismiss();
                        }else {
                            reset_pin_delete_all_calls();
                        }

                    }
                    return true;
                }
            });
            dialog.show();
        }
    }

    private boolean IscallsFound() {
        int result=cbohelp.getmenu_count("phdcrdr_rc");
        result+=cbohelp.getmenu_count("tempdr");
        result+=cbohelp.getmenu_count("chemisttemp");
        result+=cbohelp.getmenu_count("phdcrstk");
        result+=cbohelp.getmenu_count("NonListed_call");
        result+=cbohelp.getmenu_count("Tenivia_traker");
        return result>0;
    }


    private void reset_pin_delete_all_calls(){

        new Service_Call_From_Multiple_Classes().resetDCRNow(context);

        Intent i = new Intent(context, LoginMain.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        stopLoctionService();
        startActivity(i);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initFingerprintManager();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    public void onBackPressed() {

        if (getIntent().getBooleanExtra("EXIT", false)) {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        }
        customVariablesAndMethod.deleteFmcg_ByKey(context, "logo");
        super.onBackPressed();

    }

    public void onRestart() {
        super.onRestart();
        cbohelp = new CBO_DB_Helper(getApplicationContext());
    }

    public void getDetailsForOffline() {
        Cursor c = cbohelp.getDatabaseDetail();
        if (c.moveToFirst()) {
            do {

                Custom_Variables_And_Method.ip = c.getString(c.getColumnIndex("ols_ip"));
                Custom_Variables_And_Method.user = c.getString(c.getColumnIndex("ols_db_user"));
                Custom_Variables_And_Method.pwd = c.getString(c.getColumnIndex("ols_db_password"));
                Custom_Variables_And_Method.db = c.getString(c.getColumnIndex("ols_db_name"));

            } while (c.moveToNext());
        }

        Cursor c2 = cbohelp.getOtherUserDetail();
        if (c2.moveToFirst()) {
            do {
                Custom_Variables_And_Method.location_required = c2.getString(c2.getColumnIndex("location_required"));
                Custom_Variables_And_Method.VISUAL_REQUIRED = c2.getString(c2.getColumnIndex("visual_required"));
            } while (c2.moveToNext());
        }

        Custom_Variables_And_Method.WORKING_TYPE = "Working";
        Custom_Variables_And_Method.CHEMIST_NOT_VISITED = "";
        Custom_Variables_And_Method.STOCKIST_NOT_VISITED = "";

    }


    //////////////  Code for gps Setting ////////
    public String checkForGPSUsers() {
        SharedPreferences pref = context.getSharedPreferences(Custom_Variables_And_Method.FMCG_PREFRENCE, MODE_PRIVATE);
        GPS_NEEDED = pref.getString("gps_needed", null);
        if (GPS_NEEDED == null) {
            GPS_NEEDED = "N";
        }
        return GPS_NEEDED;
    }




    private void dcrForGPSUsers() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS Enabled
            showSettings();

        } else {
            if ((cbohelp.getNewVersion().equals("")) || (Custom_Variables_And_Method.VERSION.equals(cbohelp.getNewVersion()))) {
//                if (Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
//                    startActivity(new Intent(getApplicationContext(), PersonalInfo.class));
//                    finish();
//                } else {
//                    if (Custom_Variables_And_Method.pub_desig_id.equalsIgnoreCase("11")) {
//                        startActivity(new Intent(getApplicationContext(), PersonalInfo.class));
//                        finish();
//                    } else {

                        /*String work_type_Selected= customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_type_Selected","w");
                        switch (work_type_Selected){
                            case "l":
                                Intent intent = new Intent(context, FinalSubmitDcr_new.class);
                                intent.putExtra("Back_allowed","N");
                                startActivity(intent);
                                break;
                            case "n":
                                Intent intent1 = new Intent(context, NonWorking_DCR.class);
                                intent1.putExtra("Back_allowed","N");
                                startActivity(intent1);

                                break;
                            default:*/
                                startActivity(new Intent(context, ViewPager_2016.class));
                                finish();
                        //}
//                    }
//                }
            } else {
                startActivity(new Intent(getApplicationContext(), Load_New.class));

            }

        }
    }


    public void showSettings() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("CBO");
        builder1.setIcon(R.drawable.setting);
        builder1.setMessage("Please Switch-on your GPS");
        builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                Intent callGPSSettingIntent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        builder1.show();
    }

    Runnable converAddress = new Runnable() {
        @Override
        public void run() {
            try {
                myCustomMethod.convertAddress();
            } catch (Exception e) {

            }

        }
    };





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //capture_Image();
                LoginFake(false);
                customVariablesAndMethod.msgBox(context,"Permission granted");
            }
        }else if (requestCode ==  REQUEST_FINGERPRINT_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    initFingerprintManager();
                }
            }

        }
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
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        loginMethod(view);
                        // All required changes were successfully made
                        // mycon.msgBox("You Press Ok..");
                        //onLocationChanged();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        // mycon.msgBox("You Press Cancel..");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        //startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        String latLong = location.getLatitude() + "," + location.getLongitude();

        if (customVariablesAndMethod.IsValidLocation(context,latLong,0)) {
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLatLong", latLong);
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "last_location_update_time_in_minites",customVariablesAndMethod.get_currentTimeStamp());
            Custom_Variables_And_Method.GLOBAL_LATLON =  latLong;
            customVariablesAndMethod.putObject(context,"currentBestLocation_Validated",location);

        }else{
            Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void loginMethod(View v) {




    }




    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}