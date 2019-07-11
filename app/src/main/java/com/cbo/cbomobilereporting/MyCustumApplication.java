package com.cbo.cbomobilereporting;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.databaseHelper.DCR.mDCR;
import com.cbo.cbomobilereporting.databaseHelper.User.UserDB;
import com.cbo.cbomobilereporting.databaseHelper.User.mUser;
import com.cbo.cbomobilereporting.emp_tracking.MyLoctionService;
import com.cbo.cbomobilereporting.ui.LoginFake;
import com.cbo.cbomobilereporting.ui_new.for_all_activities.CustomWebView;
import com.cbo.cbomobilereporting.ui_new.report_activities.Msg_ho;

import utils.CBOUtils.Constants;
import utils_new.Custom_Variables_And_Method;


public class MyCustumApplication extends MultiDexApplication {
    private static MyCustumApplication instance;
    private mUser user = null;
    private mDCR dcr = null;
    static String TAG = "MyCustumApplication";
    public UserDB userDB = null;
    public String FirebaseSyncYN = "Y";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //FirebaseSyncYN = getDataFrom_FMCG_PREFRENCE("FIREBASE_SYNCYN","Y");
        /*if (isLiveTrackingOn()){
            FirebaseSyncYN = "Y";
        }*/
        userDB = new UserDB();
        getbattrypercentage();
        registerActivityLifecycleCallbacks(new AppLifecycleTracker());
        //new ExceptionHandler(this);
        getUser();
    }

    public mUser getUser() {
        if (user != null) {
            if (!user.getID().trim().equalsIgnoreCase("0")
                    && !user.getCompanyCode().trim().equalsIgnoreCase("")) {
                return user;
            }
        }


        CBO_DB_Helper cbohelp = new CBO_DB_Helper(getInstance());

        user = new mUser(cbohelp.getPaid(), cbohelp.getCompanyCode())
                .setDCRId(cbohelp.getDCR_ID_FromLocal())
                .setName(cbohelp.getPaName())
                .setHQ(cbohelp.getHeadQtr())
                .setDesgination(cbohelp.getDESIG())
                .setDesginationID(cbohelp.getPUB_DESIG())
                .setLocation(Custom_Variables_And_Method.getInstance().getObject(getInstance(),"currentBestLocation_Validated", Location.class));
        getDEVICE_ID();


        updateUser();
        return user;
    }

    public void updateUser(){
        if (user == null){
            getUser();
        }
        if (!user.getID().trim().equalsIgnoreCase("0")
                && !user.getCompanyCode().trim().equalsIgnoreCase("")) {
            userDB.insert(user);
        }
    }

    public mDCR getDCR(){
        if (dcr != null){
            return dcr;
        }

        dcr = new mDCR();
//                .setId(getDataFrom_FMCG_PREFRENCE("DCR_ID","0"))
//                .setDate(getDataFrom_FMCG_PREFRENCE("DCR_ID","0"));

        return dcr;
    }

    public String getDataFrom_FMCG_PREFRENCE(String key,String default_value){
        return Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),key,default_value);
    }
    public void setDataInTo_FMCG_PREFRENCE(String key,String value){
         Custom_Variables_And_Method.getInstance().setDataInTo_FMCG_PREFRENCE(getInstance(),key,value);
    }


    public Boolean IsSubmitDCR_WithoutCalls(){
        return getDataFrom_FMCG_PREFRENCE("working_code","W").contains("NR")
                && getDataFrom_FMCG_PREFRENCE("working_code","W").contains("X");
    }

    @SuppressLint("MissingPermission")
    private void getDEVICE_ID() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID;
        try {
            //DEVICE_ID=telephonyManager.getDeviceId();
            if (telephonyManager.getDeviceId() != null) {
                DEVICE_ID = telephonyManager.getDeviceId();
            }else{
                DEVICE_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }catch (Exception e){
            DEVICE_ID ="0";
        }

        String DEVICE_MODEL = android.os.Build.MODEL;
        String BRAND = android.os.Build.BRAND;

        if (BRAND == null){
            BRAND ="";
        }
        if (DEVICE_MODEL == null){
            DEVICE_MODEL ="MODEL";
        }
        if(DEVICE_ID ==null){
            DEVICE_ID ="0";
        }

        user.setIMEI(DEVICE_ID +"'!'"+ BRAND +""+ DEVICE_MODEL);
        user.setBRAND(BRAND);
        user.setOS(BRAND +" - "+ DEVICE_MODEL+ " : "+Build.VERSION.SDK_INT );


    }

    private void getbattrypercentage() {


        BroadcastReceiver br = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //context.unregisterReceiver(this);
                int current_level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (current_level >= 0 && scale > 0) {
                    level = (current_level * 100) / scale;
                }
                if (user!= null) {
                    user.setBattery( "" + level);
                    updateUser();
                }
            }
        };
        IntentFilter batrylevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(br, batrylevelFilter);
    }


    public static MyCustumApplication getInstance(){
        return instance;
    }


    public void Logout(Activity context){
        stopLoctionService(false);
        Intent intent = new Intent(getApplicationContext(), LoginFake.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        context.finish();
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if(appDir.exists()){
            String[] children = appDir.list();
            for(String s : children){
                if(!s.equals("lib")){
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s +" DELETED");
                }
            }
        }
    }

    public static boolean deleteData(File data){

         if (data != null && data.isDirectory()){
             String[] dataChild = data.list();

         for (int i= 0; i<dataChild.length;i++){
             boolean success = deleteDir(new File(data,dataChild[i]));
             if (!success){
                 return false;
             }
         }


         }

        return data.delete();
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }


        return dir.delete();
    }

    public String getTaniviaTrakerMenuName(){
        CBO_DB_Helper cbohelp = new CBO_DB_Helper(getInstance());
        if(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),"Tenivia_NOT_REQUIRED").equals("N")) {
            return cbohelp.getMenu("DCR", "D_DR_RX").get("D_DR_RX");
        }

        if(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),"Rx_NOT_REQUIRED").equals("N")) {
            return cbohelp.getMenu("DCR", "D_RX_GEN").get("D_RX_GEN");
        }

        if(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),"Rx_NA_NOT_REQUIRED").equals("N")) {
            return cbohelp.getMenu("DCR", "D_RX_GEN_NA").get("D_RX_GEN_NA");
        }
        return "";
    }

    public String getTaniviaTrakerMenuCode(){
        CBO_DB_Helper cbohelp = new CBO_DB_Helper(getInstance());
        if(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),"Tenivia_NOT_REQUIRED").equals("N")) {
            return "D_DR_RX";
        }

        if(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),"Rx_NOT_REQUIRED").equals("N")) {
            return "D_RX_GEN";
        }

        if(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),"Rx_NA_NOT_REQUIRED").equals("N")) {
            return "D_RX_GEN_NA";
        }
        return "";
    }
    public static String getServeiceURL() {
        String URL= "http://www.cboservices.com/mobilerpt.asmx";
        URL= Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),"WEBSERVICE_URL",URL);
        if(URL.equals("")){
            URL= "http://www.cboservices.com/mobilerpt.asmx";
        }
        return URL;
    }

    public void LoadURL(String title,String url) {
        LoadURL( title, url,0);
    }
    public void LoadURL(String title,String url,int showAs){

        // showAs normal or notification



        if (url.toLowerCase().contains("https://play.google.com/store/apps/details?id=com.cbo.cbomobilereporting")) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.cbo.cbomobilereporting&hl=en"));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

        }if (url.toLowerCase().contains("play.google.com/store/apps/")) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

        }else if ( (url.toLowerCase().contains(".pdf"))){

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse(url), "application/pdf");
            startActivity(intent);


        }else {

            /*if(!url.toLowerCase().contains("http://") && !url.toLowerCase().contains("emulated/0")){
                url="http://"+url;
            }else if(url.toLowerCase().contains("emulated/0")){
                url="file:///"+url;
            }

            Custom_Variables_And_Method.GLOBAL_LATLON =  Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
            Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT=Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),"DCR_DATE");
            if(!url.contains("emulated/0") && !url.isEmpty()){
                if ( url.contains("?")) {
                    url = url + "&LAT_LONG=" + Custom_Variables_And_Method.GLOBAL_LATLON ;
                }else{
                    url = url + "?LAT_LONG=" + Custom_Variables_And_Method.GLOBAL_LATLON ;
                }
            }

            //customVariablesAndMethod.getAlert(context,"Url",url);
            String ALLOWED_URI_CHARS = "@#&=*-_.,:!?()/~'%";
            String url1 = Uri.encode(url, ALLOWED_URI_CHARS);
*/
            if (showAs>0){
                Intent intent1=new Intent(getInstance(),Msg_ho.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("msg",""+showAs);
                intent1.putExtra("msg_ho", url);
                startActivity(intent1);
                return;
            }

            /*CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            builder.setToolbarColor(getResources().getColor( R.color.colorPrimaryDark));
            customTabsIntent.launchUrl(getInstance(),
                    Uri.parse(url1));*/

            Intent i = new Intent(getInstance(), CustomWebView.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("A_TP", url);
            i.putExtra("Title", title);
            startActivity(i);
        }

    }

    public void ShowAutoStart(){
        try {

            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            }else if ("Letv".equalsIgnoreCase(manufacturer)) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
                startActivity(intent);
            }else if ("Honor".equalsIgnoreCase(manufacturer)) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                startActivity(intent);
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
               // intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.coloros.safecenter",
                            "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                    startActivity(intent);
                } catch (Exception e) {
                    try {
                        Intent intent = new Intent();
                        intent.setClassName("com.oppo.safe",
                                "com.oppo.safe.permission.startup.StartupAppListActivity");
                        startActivity(intent);

                    } catch (Exception ex) {
                        try {
                            Intent intent = new Intent();
                            intent.setClassName("com.coloros.safecenter",
                                    "com.coloros.safecenter.startupapp.StartupAppListActivity");
                            startActivity(intent);
                        } catch (Exception exx) {

                        }
                    }
                }
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                //intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.iqoo.secure",
                            "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
                    startActivity(intent);
                } catch (Exception e) {
                    try {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                                "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                        startActivity(intent);
                    } catch (Exception ex) {
                        try {
                            Intent intent = new Intent();
                            intent.setClassName("com.iqoo.secure",
                                    "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                            startActivity(intent);
                        } catch (Exception exx) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

//            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//            if  (list.size() > 0) {
//                startActivity(intent);
//            }
        } catch (Exception e) {
            //logException(e);
        }
    }


    public Boolean isLiveTrackingOn(){

        //return false;
        String fmcg_Live_Km =Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),"live_km");

        return  (fmcg_Live_Km.equalsIgnoreCase("Y"))||
                (fmcg_Live_Km.equalsIgnoreCase("5"))||
                (fmcg_Live_Km.equalsIgnoreCase("Y5"));


    }

    class AppLifecycleTracker implements ActivityLifecycleCallbacks {

        private int numStarted = 0;
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            user = null;
            getUser();
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (numStarted == 0) {
                // app went to foreground

                startLoctionService();
            }
            numStarted++;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {



        }

        @Override
        public void onActivityStopped(Activity activity) {
            numStarted--;
            if (numStarted == 0) {
                // app went to background
                stopLoctionService(false);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    public void startLoctionService() {
        startLoctionService(false);
    }


    public void startLoctionService(boolean forcefully) {
        if(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(getInstance(),"Final_submit","N").equals("N") || forcefully) {
            Intent intent = new Intent(getInstance(), MyLoctionService.class);
            intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "Running on Android O");
                startForegroundService(intent);
                //startService(intent);
            } else {
                Log.d(TAG, "Running on Android N or lower");
                startService(intent);
            }
        }else{
            stopLoctionService();
        }
    }
    public void stopLoctionService() {
        stopLoctionService(true);
    }

    public void stopLoctionService(boolean forcefully) {

        if (!isLiveTrackingOn() || forcefully) {
            Intent intent = new Intent(getInstance(), MyLoctionService.class);
            intent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "Running on Android O");
                stopService(intent);
                //startForegroundService(intent);
                //startService(intent);
            } else {
                Log.d(TAG, "Running on Android N or lower");
                startService(intent);
                stopService(intent);
            }
        }
    }


}
