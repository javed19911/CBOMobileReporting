package com.cbo.cbomobilereporting.emp_tracking;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import utils.CBOUtils.Constants;
import utils.clearAppData.MyCustumApplication;
import utils_new.Custom_Variables_And_Method;


public class LocationBroadcast extends BroadcastReceiver {

    private SharedPreferences mPrefs;
    public static final String TAG = "LocationLoggerService";
    Custom_Variables_And_Method customVariablesAndMethod;


    @Override
    public void onReceive(Context context, Intent intent) {


        /*ComponentName comp = new ComponentName(context.getPackageName(), MyLoctionService.class.getName());
        ComponentName service = context.startService(new Intent().setComponent(comp));

        MyConnection myConnection = new MyConnection(context);
        // myConnection.msgBox("Service Start For Storing  Data in 1 Minute ");
        Log.d("Broadcast is Running","Service Start For Storing  Data in 1 Minute ");
    }*/
       /* if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
            customMethod = new MyCustomMethod(context);
            customMethod.startAlarm10Sec();
            customMethod.startAlarmIn10Minute();
            MyConnection myConnection = new MyConnection(context);
        //    myConnection.msgBox("Service Start After Boot ....... ");

        }else{*/

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Tracking","Y");

          ComponentName comp = new ComponentName(context.getPackageName(), MyLoctionService.class.getName());
            //ComponentName service = context.startService(new Intent().setComponent(comp));

        MyCustumApplication.getInstance().startLoctionService();
        /*Intent intent1 = new Intent().setComponent(comp);
        intent1.setAction(Constants.ACTION.LIVE_TRACKING_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "Running on Android O");
            context.startForegroundService(intent1);
        }else{
            Log.d(TAG, "Running on Android N or lower");
            context.startService(intent1);
        }*/

          //  myConnection.msgBox("Service Start For Storing  Data in 1 Minute.... ");
            Log.d("Broadcast is Running","Service Start For Storing  Data in 1 Minute ");
       // }

    }
}
