package com.cbo.cbomobilereporting.emp_tracking;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.databaseHelper.Location.LocationDB;
import com.cbo.cbomobilereporting.databaseHelper.Location.mLocation;
import com.cbo.cbomobilereporting.databaseHelper.User.mUser;
import com.cbo.cbomobilereporting.ui_new.SplashScreen_2016;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


import utils.CBOUtils.Constants;

import com.cbo.cbomobilereporting.MyCustumApplication;
import utils_new.Custom_Variables_And_Method;

public class MyLoctionService extends Service implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 7;
    private static final String LOG_TAG = "MyLoctionService";
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    CBO_DB_Helper myDbUtil;
    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    MyCustomMethod mCos;
    LocationDB locationDB =null;

    Boolean serviceStarted = false;
    Runnable dataFromOnLocationChange = new Runnable() {
        @Override
        public void run() {

            updateUI();
        }
    };
    Runnable r1 =
            new Runnable() {
                @Override
                public void run() {
                    backgroundData();
                }
            };


    public MyLoctionService() {
        super();
        //super(MyLoctionService.class.getName());

    }


    @Override
    public int onStartCommand(Intent intent,int flag,int startId) {
        super.onStartCommand(intent,flag, startId);

        startForgroungService(intent);


        return START_STICKY;
    }

    private void startForgroungService(Intent intent){
        if (intent == null) {
            Log.i(LOG_TAG, "null : "+ intent);
            return;
        }
        if (intent.getAction() == null) {
            Log.i(LOG_TAG, "null : "+ intent);
            return;
        }
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            /*if (!mGoogleApiClient.isConnected())
                return;*/


            if (!serviceStarted) {
                Log.i(LOG_TAG, "Received Start Foreground Intent ");
                Intent notificationIntent = new Intent(this, SplashScreen_2016.class);
                notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                        notificationIntent, 0);

          /*  Intent previousIntent = new Intent(this, MyLoctionService.class);
            previousIntent.setAction(Constants.ACTION.PREV_ACTION);
            PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                    previousIntent, 0);

            Intent playIntent = new Intent(this, MyLoctionService.class);
            playIntent.setAction(Constants.ACTION.PLAY_ACTION);
            PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                    playIntent, 0);

            Intent nextIntent = new Intent(this, MyLoctionService.class);
            nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
            PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                    nextIntent, 0);
*/

                String CHANNEL_ID = "Location Service";// The id of the channel.
                CharSequence name = "CBO Location Notification"; //getString(R.string.channel_name);// The user-visible name of the channel.


                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.cbo_icon);
                NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("CBO Mobile Reporting")
                        .setContentText("Running......")
                        .setSmallIcon(R.drawable.cbo_noti)
                        .setTicker("CBO")
                        .setOngoing(true)
                        .setLargeIcon(Bitmap.createScaledBitmap(largeIcon, 128, 128, false))
                        .setChannelId(CHANNEL_ID)
                        .setContentIntent(pendingIntent);
                    /*.addAction(android.R.drawable.ic_media_previous,
                            "Previous", ppreviousIntent)
                    .addAction(android.R.drawable.ic_media_play, "Play",
                    pplayIntent)
                    .addAction(android.R.drawable.ic_media_next, "Next",
                            pnextIntent);*/


                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int color = 0x125688;
                    noBuilder.setColor(color);
                    noBuilder.setSmallIcon(R.drawable.cbo_noti);
                }

                //Random random = new Random();
                //int m = random.nextInt(9999 - 1000) + 1000;
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Sets an ID for the notification, so it can be updated.
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW);
                    // Sets whether notifications posted to this channel should display notification lights
                    mChannel.enableLights(true);
                    // Sets whether notification posted to this channel should vibrate.
                    mChannel.enableVibration(true);
                    // Sets the notification light color for notifications posted to this channel
                    mChannel.setLightColor(Color.GREEN);
                    // Sets whether notifications posted to this channel appear on the lockscreen or not
                    mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                    mChannel.setSound(null, null);
                    notificationManager.createNotificationChannel(mChannel);
                }

                Notification notification = noBuilder.build();

                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                        notification);
                serviceStarted = true;

                if (!mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();

                locationDB = new LocationDB();
                //locationDB.delete(null);
            }
        } else if (intent.getAction().equals(Constants.ACTION.LIVE_TRACKING_ACTION)) {

            if (!serviceStarted) {
                intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startForgroungService(intent);
            }

            Log.i(LOG_TAG, "LIVE tracking");
            String tracking = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Tracking","N");

            if (tracking.equals("Y")) {

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Tracking","Y");
                try {
                    new Thread(r1).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {


            /////******************************************************************
            /// do not relay on this code its ab error



            /**/

            /*if (serviceStarted) {
                Log.i(LOG_TAG, "Received Stop Foreground Intent");
                serviceStarted = false;
                *//*if (!mGoogleApiClient.isConnected()) {
                    intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startForgroungService(intent);
                }
                serviceStarted = false;*//*

            }*/

            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopLocationUpdates();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (serviceStarted) {
                    serviceStarted = false;
                    stopForeground(true);
                }
                //stopSelf();
            } else {
                stopSelf();
            }
        }


    }
    /*@Override
    protected void onHandleIntent(Intent intent) {


        String tracking = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Tracking");

        if (tracking.equals("Y")) {

            try {
                new Thread(r1).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        myDbUtil = new CBO_DB_Helper(this);
        mCos = new MyCustomMethod(this);

       /* myCon = new MyConnection(this);*/

        init();

    }

    private void init(){

        if (!isGooglePlayServicesAvailable()) {
            stopSelf();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {

            Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();

            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("onConnected : ", "" + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d("Location update", "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Connection failed", "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {

        mCurrentLocation = location;
        //Log.d("OnLocationChange", "hello" );
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
         try {
            new Thread(dataFromOnLocationChange).start();
              }
           catch (Exception e){
                 e.printStackTrace();
          }


    }

    public void updateUI() {

        if (null != mCurrentLocation) {

            try {



                //customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"shareLatLong",msg);

                Double gpslat = mCurrentLocation.getLatitude();
                Double gpslon = mCurrentLocation.getLongitude();

                String msg = gpslat + "," + gpslon;


                String lat = String.valueOf(gpslat);
                String lon = String.valueOf(gpslon);

                String lastLatLong = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong");
                //Location currentBestLocation_Validated=customVariablesAndMethod.getObject(context,"currentBestLocation_Validated",Location.class);
                //Location currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation",Location.class);
                //if (isBetterLocation(mCurrentLocation,currentBestLocation)) {


                    //Long Locatime = mCurrentLocation.getTime();

                    /*Calendar myCal = Calendar.getInstance();
                    myCal.setTimeInMillis(Locatime);
                    int myMin = myCal.get(Calendar.MINUTE);
                    int myhour = myCal.get(Calendar.HOUR_OF_DAY);
                    String MyTime = myhour + "." + myMin;*/
                    String MyTime = customVariablesAndMethod.currentTime(context);

                    if (lastLatLong.equals("")) {
                        //lastLatLong = "0.0,0.0";
                        lastLatLong = msg;
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLatLong", msg);
                        //customVariablesAndMethod.putObject(context,"currentBestLocation_Validated",mCurrentLocation);

                    }
                    /*if (currentBestLocation_Validated==null) {
                        customVariablesAndMethod.putObject(context,"currentBestLocation_Validated",mCurrentLocation);
                    }*/
               // if (currentBestLocation==null) {
                    //lastLatLong = "0.0,0.0";
                    customVariablesAndMethod.putObject(context,"currentBestLocation",mCurrentLocation);




                //}
                    if (lastLatLong.equals(msg)) {
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLat", "" + lat);
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLon", "" + lon);
                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareMyTime", "" + MyTime);
                        //customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "last_location_update_time_in_minites", customVariablesAndMethod.get_currentTimeStamp());
                        Custom_Variables_And_Method.GLOBAL_LATLON = msg;
                        Log.d("Data From.....", "" + lat + "..." + lon + "..." + MyTime);
                    } else {
                        if (customVariablesAndMethod.IsValidLocation(context, msg,0)) {
                            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLat", "" + lat);
                            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLon", "" + lon);
                            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareMyTime", "" + MyTime);
                            Log.d("OnLocationChange", "" + lat + "," + lon);
                            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLatLong", msg);
                            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "last_location_update_time_in_minites", customVariablesAndMethod.get_currentTimeStamp());
                            Custom_Variables_And_Method.GLOBAL_LATLON = msg;





                            // ======================================insert in firebase database============================

                            mUser mUser = MyCustumApplication.getInstance().getUser();
                            Location last_location1 = mUser.getLocation(); //customVariablesAndMethod.getObject(context,"currentBestLocation_Validated",Location.class);
                            Double km = DistanceCalculator.distance(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()
                                    ,  last_location1.getLatitude(), last_location1.getLongitude(), "K");
                            if (km>0.02) {
                                Log.d("Location update", "Location updated on firebase ..............: ");

                                mUser.setLocation(mCurrentLocation);
                                mLocation mlocation = new mLocation(mCurrentLocation).setDCR_ID(mUser.getDCRId());
                                locationDB.insert(mlocation);
                                MyCustumApplication.getInstance().updateUser();
                            }


                            //=======================================================

                            customVariablesAndMethod.putObject(context,"currentBestLocation_Validated",mCurrentLocation);
                        } else {
                            Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLatLong");
                        }
                        //System.out.println(msg);

                    }
                //}


            } catch (Exception e) {

            }

        } else {
            Log.d("", "location is null ...............");
        }
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);
        }
        Log.d("", "Location update stopped .......................");
    }



    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public void backgroundData() {

        String shareLat, shareLon, shareMyTime;



        shareLat = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLat",null);
        shareLon = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLon");
        shareMyTime = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareMyTime");

        Log.e("Data for One Minute....", ""+shareLat+"______"+shareLon);
        // shareMyTime1= myCon.getDataFrom_FMCG_PREFRENCE("shareMyTime");
        if (shareLat != null) {

            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"shareLat1", shareLat);
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"shareLon1", shareLon);
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"shareMyTime1", shareMyTime);
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"myKm1", "" + 0.0);


        }


    }

   /* @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopLocationUpdates();
        stopForeground(true);
        //stop service
        stopSelf();
        super.onTaskRemoved(rootIntent);
    }
*/


}