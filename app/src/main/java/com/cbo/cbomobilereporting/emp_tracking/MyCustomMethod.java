package com.cbo.cbomobilereporting.emp_tracking;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import receivers.DOB_DOA_broadcastreciever;
import receivers.MyBroadcastReceiver;
import services.ServiceHandler;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;


public class MyCustomMethod {

    Context context;
    Timer mTimer;
    Custom_Variables_And_Method customVariablesAndMethod;
    AlarmManager alarmManager;
    Intent intent10Minute, intentFor10Sec, intent_DOB_DOA_Remainder;
    PendingIntent pendingIntent10Minute, pendingIntentFor10Sec, pendingIntent_DOB_DOA_Remainder;
    String latLong = "";
    CBO_DB_Helper cboDbHelper;


    String notification_url = "";
    private Location currentBestLocation;


    public MyCustomMethod(Context context) {

        this.context = context;
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent10Minute = new Intent(context, ReciverFor10minuteData.class);
        intentFor10Sec = new Intent(context, LocationBroadcast.class);
        pendingIntentFor10Sec = PendingIntent.getBroadcast(context, 0, intentFor10Sec, 0);
        pendingIntent10Minute = PendingIntent.getBroadcast(context, 0, intent10Minute, 0);
        cboDbHelper = new CBO_DB_Helper(context);
        intent_DOB_DOA_Remainder = new Intent(context, MyBroadcastReceiver.class);
        pendingIntent_DOB_DOA_Remainder = PendingIntent.getBroadcast(
                context.getApplicationContext(), 234324243, intent_DOB_DOA_Remainder, 0);

    }

    public void startAlarm10Sec() {

        int InterWal = 1000 * 60 * 1;
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), InterWal, pendingIntentFor10Sec);

    }

    public void startDOB_DOA_Remainder() {
        String hour = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "DOB_REMINDER_HOUR", "0");
        if (!hour.equals("0")) {
            int i = (60 * 60 * 1000) * Integer.parseInt(hour);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + (i),
                    (i), pendingIntent_DOB_DOA_Remainder);
        }
    }

    public void stopDOB_DOA_Remainder() {

        alarmManager.cancel(pendingIntent_DOB_DOA_Remainder);
    }


    public void stopAlarm10Sec() {

        alarmManager.cancel(pendingIntentFor10Sec);
    }

    public void startAlarmIn10Minute() {


        int INTERWAL10MINUTE = 1000 * 60 * 5;

        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), INTERWAL10MINUTE, pendingIntent10Minute);

        //   myCon.msgBox("Alarm Repeat for every 10 Minute....");

    }

    public void stopAlarm10Minute() {

        alarmManager.cancel(pendingIntent10Minute);
    }


    public void stopTimer10Minute() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    public void convertAddress() {

        Custom_Variables_And_Method.global_address = customVariablesAndMethod.getAddressByLatLong(context, customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLatLong"));


    }


    //Setting Data in OneMinute SharePrefrance
    public void insertDataInOnces_Minute(String lat, String lon, String km, String time) {


        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLat1", lat);
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLon1", lon);
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareMyTime1", time);
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "myKm1", "" + km);


    }

    public void backgroundData() {

        Double mKm_tot = 0.0;
        String shareLat, shareLon, shareMyTime;
        String shareLat1, shareLon1, shareMyTime1;

        shareLat = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLat");
        shareLon = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLon");
        shareMyTime = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareMyTime");

        shareLat1 = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLat1");
        shareLon1 = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLon1");

        if (shareLat1.equalsIgnoreCase("N")) {

            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLat1", shareLat);
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLon1", shareLon);
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "myKm1", "0.0");
        }
        // shareMyTime1= myCon.getDataFrom_FMCG_PREFRENCE("shareMyTime");
        if (shareLat != null) {

            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLat1", shareLat);
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLon1", shareLon);
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareMyTime1", shareMyTime);
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "myKm1", "" + 0.0);
            // }

            // }


        }


    }


    public Map<String, String> dataToServer(String updated) {


        Map<String, ArrayList<String>> completeData10 = new HashMap<String, ArrayList<String>>();
        CBO_DB_Helper myDbUtil = new CBO_DB_Helper(context);
        String lat_s, lon_s, time_s, km_s;

        //stopAlarm10Sec();
        String methodCall = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "MethodCallFinal");

        lat_s = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLat1");
        lon_s = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLon1");
        time_s = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareMyTime1");
        km_s = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "myKm1");
        StringBuilder sb_sDCRLATCOMMIT_IN_TIME = new StringBuilder();
        StringBuilder sb_DCRLATCOMMIT_LOC_LAT = new StringBuilder();
        StringBuilder sb_DCRLATCOMMIT_KM = new StringBuilder();
        StringBuilder sDCRLATCOMMIT_ID = new StringBuilder();
        StringBuilder sDCRLATCOMMIT_LOC = new StringBuilder();
        Map<String, String> finalDataFrom10Minute = new HashMap<String, String>();
        ArrayList<String> id10 = new ArrayList<String>();
        ArrayList<String> time10 = new ArrayList<String>();
        ArrayList<String> lat10 = new ArrayList<String>();
        ArrayList<String> lon10 = new ArrayList<String>();
        ArrayList<String> km10 = new ArrayList<String>();
        ArrayList<String> loc10 = new ArrayList<String>();


        if (lat_s != null) {


            try {

                currentBestLocation = customVariablesAndMethod.getObject(context, "currentBestLocation_Validated", Location.class);
                String locExtra = "";
                if (currentBestLocation != null) {
                    locExtra = "Lat_Long " + currentBestLocation.getLatitude() + "," + currentBestLocation.getLongitude() + ", Accuracy " + currentBestLocation.getAccuracy() + ", Time " + currentBestLocation.getTime() + ", Speed " + currentBestLocation.getSpeed() + ", Provider " + currentBestLocation.getProvider();
                }
                myDbUtil.insertData_latLon10(lat_s, lon_s, time_s, "" + km_s, locExtra);


                completeData10 = myDbUtil.getDataFromlatLon10(updated);

                id10 = completeData10.get("myId");
                time10 = completeData10.get("myTime");
                lat10 = completeData10.get("myLat");
                lon10 = completeData10.get("myMyLon");
                km10 = completeData10.get("myKm");
                loc10 = completeData10.get("myLocExtra");
                int timeIndex10;
                String separator = "";
                String mkm, mtime, mlat_lon, mid, mloc;
                for (int i = 0; i < time10.size(); i++) {

                    mlat_lon = lat10.get(i) + "," + lon10.get(i);
                    mkm = km10.get(i);
                    mtime = time10.get(i);
                    mid = id10.get(i);
                    mloc = mlat_lon + "@" + loc10.get(i) + "!^" + "0";

                    if (i == 0) {
                        sb_DCRLATCOMMIT_KM.append(mkm);
                        sb_DCRLATCOMMIT_LOC_LAT.append(mlat_lon);
                        sb_sDCRLATCOMMIT_IN_TIME.append(mtime);
                        sDCRLATCOMMIT_ID.append(mid);
                        sDCRLATCOMMIT_LOC.append(mloc);
                    } else {
                        sb_DCRLATCOMMIT_KM.append("^").append(mkm);
                        sb_DCRLATCOMMIT_LOC_LAT.append("^").append(mlat_lon);
                        sb_sDCRLATCOMMIT_IN_TIME.append("^").append(mtime);
                        sDCRLATCOMMIT_ID.append("^").append(mid);
                        sDCRLATCOMMIT_LOC.append("^").append(mloc);

                    }
                }
                finalDataFrom10Minute.put("sb_DCRLATCOMMIT_KM", "" + sb_DCRLATCOMMIT_KM);
                finalDataFrom10Minute.put("sb_DCRLATCOMMIT_LOC_LAT", "" + sb_DCRLATCOMMIT_LOC_LAT);
                finalDataFrom10Minute.put("sb_sDCRLATCOMMIT_IN_TIME", "" + sb_sDCRLATCOMMIT_IN_TIME);
                finalDataFrom10Minute.put("sDCRLATCOMMIT_ID", "" + sDCRLATCOMMIT_ID);
                finalDataFrom10Minute.put("sDCRLATCOMMIT_LOC", "" + sDCRLATCOMMIT_LOC);
            } catch (Exception e) {
            }
        }


        //  }
        if (methodCall.equalsIgnoreCase("N")) {
            startAlarm10Sec();
        }
        return finalDataFrom10Minute;
    }

    ///////Check For Enable or Not ////////////
    public Boolean checkGpsEnable() {

        String allowedLocationProviders = checkGpsEnableNewMethod();
        Boolean gps = checkGpsEnableOldMethod();

        if (gps || ((!allowedLocationProviders.equals("")) && (!allowedLocationProviders.equals("network")))) {

            return true;

        } else {

            return false;

        }
    }

    private Boolean checkGpsEnableOldMethod() {

        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Boolean gps;
        try {
            gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            return false;
        }
        return gps;
    }

    public String checkGpsEnableNewMethod() {

        String allowedLocationProviders;

        try {
            allowedLocationProviders =
                    Settings.System.getString(context.getContentResolver(),
                            Settings.System.LOCATION_PROVIDERS_ALLOWED);
        } catch (Exception e) {

            return "";
        }
        if ((!allowedLocationProviders.equals(""))) {
            return allowedLocationProviders;
        } else {
            return "";
        }
    }


    public int getLocationMode(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }


        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


            if (TextUtils.isEmpty(locationProviders)) {
                locationMode = Settings.Secure.LOCATION_MODE_OFF;
            } else if (locationProviders.contains(LocationManager.GPS_PROVIDER) && locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                locationMode = Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;
            } else if (locationProviders.contains(LocationManager.GPS_PROVIDER)) {
                locationMode = Settings.Secure.LOCATION_MODE_SENSORS_ONLY;
            } else if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                locationMode = Settings.Secure.LOCATION_MODE_BATTERY_SAVING;
            }

        }

        return locationMode;
    }

    public void CallDilog(final String Mobile, final String Name) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        if (Mobile == null || Mobile.equals("")) {
            alertDialogBuilder.setMessage("No Mobile number found for " + Name);
        } else {
            alertDialogBuilder.setMessage("Call to \n" + Name + " : " + Mobile);
        }

        alertDialogBuilder.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(DOB_DOA.this,"You clicked yes button", Toast.LENGTH_LONG).show();
                if (Mobile != null || !Mobile.equals(""))
                    call(Mobile);
            }
        });

        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void call(String Mobile) {
        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Mobile));
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            context.startActivity(in);
        }

        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMS(final String Mobile,final String Name) {

        if(Mobile==null || Mobile.equals("")){
            Toast.makeText(context, "No Mobile number found for " + Name, Toast.LENGTH_LONG).show();
        }else{
            Log.i("Send SMS", "");
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);

            smsIntent.setData(Uri.parse("smsto:"));
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address"  , Mobile);
            smsIntent.putExtra("sms_body"  , "");

            try {
                context.startActivity(smsIntent);
                //finish();
                Log.i("Finished sending SMS...", "");
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void notification_check() {
        String flag,flag1,MAIL_STATUS ="N";
        Boolean result=false;
        Date date = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        String date1 = format1.format(date);

        MAIL_STATUS = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"MAIL_STATUS","N");
        flag = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DOB_DOA_notification_date","01/01/1970");
        flag1 = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FLASHYN","N");

        if (!flag.equals(date1)) {
            String network_status = NetworkUtil.getConnectivityStatusString(context);
            if(!network_status.equals("Not connected to Internet")) {
                new BackGroudTask().execute();
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DOB_DOA_notification_date", date1);
            }

        }
        if (!flag1.equalsIgnoreCase("N")) {
            String network_status = NetworkUtil.getConnectivityStatusString(context);
            if(!network_status.equals("Not connected to Internet")) {
                sendMessage("1");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"FLASHYN", "N");
            }
        }

        if (!MAIL_STATUS.equalsIgnoreCase("N")) {
            sendMessage("4");
        }

    }
    class BackGroudTask extends AsyncTask<String, String, String> {

        CBO_DB_Helper cbohelp;
        ProgressDialog progressDialog;
        ServiceHandler serviceHandler;
        String response1;
        @Override
        protected String doInBackground(String... params) {
            try {
                Date date = new Date();
                SimpleDateFormat format1 = new SimpleDateFormat("M/dd/yyyy");
                String date1 = format1.format(date);
                //date1="9/20/2016";
                response1 = serviceHandler.get_POPUP_MOBILE(cbohelp.getCompanyCode(), "" + Custom_Variables_And_Method.PA_ID, date1, date1);
            }catch (Exception e){
                return "ERROR apk "+e;
            }
            return response1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            serviceHandler=new ServiceHandler(context);
            cbohelp=new CBO_DB_Helper(context);
           /* progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("CBO");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();*/
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null || s.contains("ERROR")) {
                // progressDialog.dismiss();
                customVariablesAndMethod.msgBox(context," data Error");
            } else {
                try {
                    //progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray rows = jsonObject.getJSONArray("Tables");
                    JSONObject first = rows.getJSONObject(0);
                    JSONArray one = first.getJSONArray("Tables0");

                    for (int j = 0; j < one.length(); j++) {
                        JSONObject jobj = one.getJSONObject(j);
                        if(jobj.getString("DOC_TYPE").equals("EMPDOB") ){
                            if(jobj.getString("YN").equals("Y") ) {
                                sendMessage("0");
                            }
                        }else{
                            if(jobj.getString("YN").equals("Y") && jobj.getString("BACKALLOWEDYN").equals("Y")) {

                                sendMessage("5",jobj.getString("URL"));
                            }else if(jobj.getString("YN").equals("Y") ) {

                                sendMessage("3",jobj.getString("URL"));
                            }
                        }
                    }

                    JSONObject Second = rows.getJSONObject(1);
                    JSONArray two = Second.getJSONArray("Tables1");

                    for (int j = 0; j < two.length(); j++) {
                        JSONObject jobj = two.getJSONObject(j);
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"MAIL_STATUS",jobj.getString("MAIL_STATUS"));
                        if(!jobj.getString("MAIL_STATUS").equals("N") ){
                            sendMessage("4");
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }



    }
    public void sendMessage(String from) {
        sendMessage(from,"");
    }
    public void sendMessage(String from, String notification_url) {
        Log.d("sender", "Broadcasting message");

        if (from.equals("0")){
            startDOB_DOA_Remainder();
        }

        Intent intent = new Intent(context, DOB_DOA_broadcastreciever.class);
        intent.putExtra("from",from);
        intent.putExtra("url",notification_url);
        //intent.setAction("DOB_DOA_notification_flag");
        context.sendBroadcast(intent);
    }

}