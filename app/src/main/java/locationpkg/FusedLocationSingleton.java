package locationpkg;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;


/**
 * Created by cboios on 10/05/18.
 */

public class FusedLocationSingleton implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    /***********************************************************************************************
     * properties
     **********************************************************************************************/
    private static FusedLocationSingleton mInstance = null;
    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public final static int FAST_LOCATION_FREQUENCY = 5 * 1000;
    public final static int LOCATION_FREQUENCY = 5 * 1000;
    private final int REQUEST_CHECK_SETTINGS = 1000;

    Context context;

    /***********************************************************************************************
     * methods
     **********************************************************************************************/
    /**
     * constructor
     */
    private FusedLocationSingleton() {
        context = MyCustumApplication.getInstance();
        buildGoogleApiClient();
    }

    /**
     * destructor
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        stopLocationUpdates();
    }

    public static FusedLocationSingleton getInstance() {
        if (null == mInstance) {
            mInstance = new FusedLocationSingleton();
        }
        return mInstance;
    }

    ///////////// 1

    /**
     * builds a GoogleApiClient
     */
    private synchronized void buildGoogleApiClient() {
        // setup googleapi client
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        // setup location updates
        configRequestLocationUpdate();
    }

    ///////////// 2

    /**
     * config request location update
     */
    private void configRequestLocationUpdate() {
        mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_FREQUENCY)
                .setFastestInterval(FAST_LOCATION_FREQUENCY);
        CheckGPSSetting();
    }


    public void CheckGPSSetting(){
        ////
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                                     @Override
                                     public void onResult(LocationSettingsResult result) {
                                         final Status status = result.getStatus();
                                         final LocationSettingsStates state = result.getLocationSettingsStates();
                                         switch (status.getStatusCode()) {
                                             case LocationSettingsStatusCodes.SUCCESS:
                                                 // All location settings are satisfied. The client can initialize location
                                                 // requests here.
                                                 //Toast.makeText(context,"SUCCESS",Toast.LENGTH_LONG).show();
                                                 break;
                                             case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                                 // Location settings are not satisfied. But could be fixed by showing the user
                                                 // a dialog.
                                                 // Show the dialog by calling startResolutionForResult(),
                                                 // and check the result in onActivityResult().

                                                 LocalBroadcastManager.getInstance(context).registerReceiver(mLocationSetting,
                                                         new IntentFilter(Const.INTENT_FILTER_LOCATION_SETTING));

                                                 //Toast.makeText(context,"Ask GPS Setting",Toast.LENGTH_LONG).show();
                                                 //PendingIntent pI = status.getResolution();
                                                 //mGoogleApiClient.getContext().startActivity(new Intent(mGoogleApiClient.getContext(), SetGPS_Setting.class)
                                                        // .putExtra("resolution", pI).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                 // status.startResolutionForResult( context, REQUEST_CHECK_SETTINGS);
                                                 break;
                                             case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                                 // Location settings are not satisfied. However, we have no way to fix the
                                                 // settings so we won't show the dialog.
                                                 // mycon.msgBox("you are here");
                                                 Toast.makeText(context,"Please Swicth ON your GPS from Settings",Toast.LENGTH_LONG).show();
                                                 //msgBox(context,"Please Swicth ON your GPS from Settings");
                                                 break;
                                         }
                                     }
                                 }


        );
    }


    /***********************************************************************************************
     * local broadcast receiver
     **********************************************************************************************/
    /**
     * handle location Setting Changed
     */

    BroadcastReceiver mLocationSetting = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            buildGoogleApiClient();
        }
    };



  //////////// 3

    /**
     * request location updates
     */
    private void requestLocationUpdates() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, Const.MY_PERMISSIONS_REQUEST_LOCATION);
        }else {
            CheckGPSSetting();
//            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//            try {
//                Log.d(TAG ,"Removing Test providers")
//                lm.removeTestProvider(LocationManager.GPS_PROVIDER);
//            } catch (IllegalArgumentException error) {
//                Log.d(TAG,"Got exception in removing test  provider");
//            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    this
            );
        }
    }

    /**
     * start location updates
     */
    public void startLocationUpdates() {
        // connect and force the updates
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    /**
     * removes location updates from the FusedLocationApi
     */
    public void stopLocationUpdates() {
        // stop updates, disconnect from google api
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationSetting);
        }

    }

    /**
     * get last available location
     * @return last known location
     */
    public Location getLastLocation() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            // return last location
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, Const.MY_PERMISSIONS_REQUEST_LOCATION);
                return null;
            }else {
                return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }
        } else {
            startLocationUpdates(); // start the updates
            return null;
        }
    }

    /***********************************************************************************************
     * GoogleApiClient Callbacks
     **********************************************************************************************/
    @Override
    public void onConnected(Bundle bundle) {
        // do location updates
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // connection to Google Play services was lost for some reason
        if (null != mGoogleApiClient) {
            mGoogleApiClient.connect(); // attempt to establish a new connection
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /***********************************************************************************************
     * Location Listener Callback
     **********************************************************************************************/
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            // send location in broadcast
            Intent intent = new Intent(Const.INTENT_FILTER_LOCATION_UPDATE);
            intent.putExtra(Const.LBM_EVENT_LOCATION_UPDATE, location);
            LocalBroadcastManager.getInstance(MyCustumApplication.getInstance()).sendBroadcast(intent);
        }
    }

}
