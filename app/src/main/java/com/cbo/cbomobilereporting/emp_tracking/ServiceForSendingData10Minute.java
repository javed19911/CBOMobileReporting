package com.cbo.cbomobilereporting.emp_tracking;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import services.ServiceHandler;
import services.Sync_service;
import utils.MyConnection;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;

public class ServiceForSendingData10Minute extends IntentService {


    Map<String, ArrayList<String>> completeData = new HashMap<String, ArrayList<String>>();
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<String> lat = new ArrayList<String>();
    ArrayList<String> lon = new ArrayList<String>();


    Map<String, ArrayList<String>> completeData10 = new HashMap<String, ArrayList<String>>();
    ArrayList<String> id10 = new ArrayList<String>();
    ArrayList<String> time10 = new ArrayList<String>();
    ArrayList<String> lat10 = new ArrayList<String>();
    ArrayList<String> lon10 = new ArrayList<String>();
    ArrayList<String> km10 = new ArrayList<String>();
    double roundedKm;

    CBO_DB_Helper myDbUtil;
    ServiceHandler webServicve;
    /*MyConnection myCon;*/
    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    Double mykm, km, totalKmIn10Minute;
    MyCustomMethod customMethod;
    String network_status;
    private Location currentBestLocation;


    public ServiceForSendingData10Minute() {
        super(ServiceForSendingData10Minute.class.getName());
    }


    @Override
    public void onCreate() {
        super.onCreate();


        myDbUtil = new CBO_DB_Helper(this);
        webServicve = new ServiceHandler(this);
        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        customMethod = new MyCustomMethod(this);
        network_status = NetworkUtil.getConnectivityStatusString(this);

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        new Thread(r1).start();
    }


    Runnable r1 = new Runnable() {
        @Override
        public void run() {

            dataToServer();

        }
    };

    public void dataToServer() {


        String lat_s, lon_s, time_s, km_s;

        customMethod.stopAlarm10Sec();
        String methodCall = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"MethodCallFinal");

       /* lat_s =  customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLat1");
        lon_s =  customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLon1");
        km_s = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"myKm1");*/
        webServicve = new ServiceHandler(this);
        currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation_Validated",Location.class);

        if (currentBestLocation != null) {


            String LocExtra="";
            if (currentBestLocation!=null) {
                LocExtra = "Lat_Long " + currentBestLocation.getLatitude() + "," + currentBestLocation.getLongitude() + ", Accuracy " + currentBestLocation.getAccuracy() + ", Time " + currentBestLocation.getTime() + ", Speed " + currentBestLocation.getSpeed() + ", Provider " + currentBestLocation.getProvider();
            }

            if (network_status.equals("Not connected to Internet")) {
                myDbUtil.insertData_latLon10(""+currentBestLocation.getLatitude(),""+ currentBestLocation.getLongitude(), "" + customVariablesAndMethod.currentTime(context), "0.0" ,LocExtra);


            } else {
                try {

                    myDbUtil.insertData_latLon10(""+currentBestLocation.getLatitude(), ""+ currentBestLocation.getLongitude(), "" +customVariablesAndMethod.currentTime(context), "0.0" ,LocExtra);
                    startService(new Intent(context, Sync_service.class));



                } catch (Exception e) {
                }
            }

        }


        //  }
        if (methodCall.equalsIgnoreCase("N")) {
            customMethod.startAlarm10Sec();
        }



    }




}