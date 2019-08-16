package com.cbo.cbomobilereporting.ui_new.dcr_activities.Location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.Model.mAddress;

import java.util.ArrayList;

import locationpkg.Const;
import locationpkg.FusedLocationSingleton;
import utils.LatLngToAddress;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;

public class CentroidLocation extends AppCompatActivity {

    RecyclerView locationList;
    aCentroidLocation adaptor;
    Button startTest;
    TextView centroid,progess_msg;
    Custom_Variables_And_Method customVariableandMethod;
    Context context;
    Boolean isLocationUpdatedStarted = false;

    private ArrayList<mCentroidLocation> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centroid_location);
        context = this;
        customVariableandMethod = Custom_Variables_And_Method.getInstance();

        startTest = findViewById(R.id.start_test);
        centroid = findViewById(R.id.loction);
        progess_msg = findViewById(R.id.progess_msg);
        locationList = findViewById(R.id.locationList);

        adaptor = new aCentroidLocation(this,locations);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        locationList.setLayoutManager(mLayoutManager1);
        locationList.setItemAnimator(new DefaultItemAnimator());
        locationList.setAdapter(adaptor);

        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocationUpdatedStarted = !isLocationUpdatedStarted;
                locations.clear();
                adaptor.notifyDataSetChanged();
                if (isLocationUpdatedStarted) {
                    startTest.setText("Stop Location Test");
                    startLocationUpdates();
                }else{
                    startTest.setText("Start Location Test");
                    stopLocationUpdates();
                }
            }
        });

    }



    public void onResume() {
        super.onResume();
        if (isLocationUpdatedStarted) {
            startTest.setText("Stop Location Test");
            startLocationUpdates();
        }else{
            startTest.setText("Start Location Test");
           // locations.clear();
           /* adaptor = new aCentroidLocation(context,locations);
            locationList.setAdapter(adaptor);*/
            adaptor.notifyDataSetChanged();
            stopLocationUpdates();
        }
    }

    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void setLoction(Location loction){
        centroid.setText(loction.getLatitude() +","+ loction.getLongitude());
    }

    private void update_progess(String msg){
        progess_msg.setText(msg);
    }




    private void startLocationUpdates(){

      /*  ArrayList<String> app = getListOfFakeLocationApps(this);
        if(app.size() >0 ){
            AppAlert.getInstance().SystemAlert("Alert!!!","UnAuthorized Location Provider found\n" +
                    "Please UnInstall the infacting application\n" +
                    app.toString(),null);
            finish();
            return;
        }*/

        // start location updates
        FusedLocationSingleton.getInstance().startLocationUpdates();
        // register observer for location updates
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this).registerReceiver(mLocationUpdated,
                new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE));
    }

    private void stopLocationUpdates(){
        // stop location updates
        FusedLocationSingleton.getInstance().stopLocationUpdates();
        // unregister observer
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocationUpdated);
    }


    private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contex, Intent intent) {
            Location location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE);
            locations.add(new mCentroidLocation(location));
            getAddress(locations.get(locations.size()-1),false);
            /*adaptor = new aCentroidLocation(context,locations);
            locationList.setAdapter(adaptor);*/
            adaptor.notifyDataSetChanged();
            setLoction(location);
            update_progess(locations.size() + "/10");

            if (locations.size() >= 10) {
                isLocationUpdatedStarted = false;
                startTest.setText("Start Location Test");
                stopLocationUpdates();
                if (IsBunkLoctions()) {
                    AppAlert.getInstance().getAlert(context, "Alert!!!", "Is Seems that your location is changing to fast...");
                }
/*
                double km = customVariableandMethod.DistanceBetween(customVariableandMethod.getObject(context, "LastCallLocation", Location.class),location );

                //var IsLastLocationMadeTwoHoursEarlier = customVariableandMethod.IsLocationOlderThan(context, "LastCallLocation", 2 * 60 * 60 * 1000);
                long time_difference = customVariableandMethod.GetLocationTimeDifference(customVariableandMethod.getObject(context, "LastCallLocation", Location.class));
                //if(time_difference > 2 * 60 * 60 * 1000){
                double estimated_time_taken=km/1.0;             //1km per min allowed
                double real_time_taken = (time_difference)/60000;
                if (real_time_taken > estimated_time_taken){
                    SetLocation(location);
                }else{
                    if (NoOfTry == 0){
                        ShowAlert("Please Switch Off your Gps...")
                    }else{
                        // check if the location is not bunk locations
                        if (IsBunkLoctions()) {
                            ShowAlert("Please Restart your Mobile...")
                        }
                    }
                }*/
            }


        }
    }

        ;


        private Boolean IsBunkLoctions() {
            int count = 0;
            mCentroidLocation centroid = getCentroid();
            if (centroid != null) {
                for (mCentroidLocation loc : locations) {
                    double km = loc.getGeoLocation().distanceTo(centroid.getGeoLocation());
                    loc.setKm(km);
                    if (km <= 200 && km > 0) {
                        count++;
                    }
                }

                adaptor.notifyDataSetChanged();
               /* adaptor = new aCentroidLocation(context,locations);
                locationList.setAdapter(adaptor);*/

                if (count > (locations.size() / 2)) {
                    getAddress(centroid,true);
                    setLoction(centroid.getGeoLocation());
                    update_progess("This is your current validated best location");
                    return false;
                }

            }
            return true;
        }

        private mCentroidLocation getCentroid() {
            mCentroidLocation centroid = null;
            double centroidX = 0D;
            double centroidY = 0D;
            for (mCentroidLocation loc : locations) {
                centroidX += loc.getGeoLocation().getLatitude();
                centroidY += loc.getGeoLocation().getLongitude();

            }
            if (locations.size() > 0) {
                ArrayList<mCentroidLocation> mlocs = (ArrayList<mCentroidLocation>) locations.clone();
                centroid = mlocs.get(0);
                centroid.getGeoLocation().setLatitude(centroidX / locations.size());
                centroid.getGeoLocation().setLongitude(centroidY / locations.size());
            }

            return centroid;
        }


        private void getAddress(mCentroidLocation  location,boolean isFinal){
            new LatLngToAddress(context, location.getLatLong())
                    .execute(new LatLngToAddress.ILatLngToAddress() {
                        @Override
                        public void onSucess(mAddress address) {
                            location.setAddress(address);
                            if (isFinal){
                                update_progess("This is your current validated best location\nAddress is :-\n"+address.getFORMATED_ADDRESS());
                            }
                        }

                        @Override
                        public void onError(String message) {
                            AppAlert.getInstance().getAlert(context,"Error!!!",message);
                        }
                    });
        }
    }

