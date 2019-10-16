package com.cbo.cbomobilereporting.ui_new;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.emp_tracking.MyLoctionService;

import utils.CBOUtils.Constants;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by cboios on 20/11/18.
 */

public abstract class CustomActivity extends AppCompatActivity {


    public enum activityType{
        ACTIVITY(0),
        DIALOG(1);

        int value;
        activityType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }


    }

    private String TAG = "CustomActivity";

    activityType activitytype = activityType.ACTIVITY;
    public CustomActivity context;
    public Custom_Variables_And_Method customVariablesAndMethod;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();

        if(getIntent().getBooleanExtra("isScreenCaptureAllowed",false) ) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        if(getIntent().getIntExtra("isAlertDialog",activityType.ACTIVITY.getValue()) == activityType.DIALOG.getValue()){
            activitytype = activityType.DIALOG;
            setTheme(R.style.Appdilogtheme);
        }
    }

    public boolean isDiologActivity(){
        return activitytype == activityType.DIALOG;
    }
    public void startActivityAsDialog(Intent intent) {
        intent.putExtra("isAlertDialog",activityType.DIALOG.getValue());
        super.startActivity(intent);
    }
    /*@Override
    public void setContentView(@LayoutRes int layoutResID) {
        LayoutInflater inflater = LayoutInflater.from(this);

        //to get the MainLayout
        View view = inflater.inflate(container_destacado, null);

        View inflatedLayout= inflater.inflate(layoutResID, (ViewGroup) view, false);
        containerDestacado.addView(inflatedLayout);
       super.setContentView(layoutResID);
    }*/

    @Override
    protected void onResume(){
        super.onResume();
        if(isDiologActivity()){
            ImageView close = findViewById(R.id.close);
            LinearLayout main = findViewById(R.id.main);
            close.setVisibility(View.VISIBLE);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(25, -15, 0, 0);
            main.setLayoutParams(params);

        }/*else{
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
            }
        }*/
    }


    public void createForgroundService(Intent intent) {
        intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "Running on Android O");
            startForegroundService(intent);
        }else{
            Log.d(TAG, "Running on Android N or lower");
            startService(intent);
        }
    }
    public void startLoctionService() {
        startLoctionService(false);
    }
    public void startLoctionService(boolean forcefully) {
        if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Final_submit","N").equals("N") || forcefully) {
            Intent intent = new Intent(this, MyLoctionService.class);
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
            Intent intent = new Intent(this, MyLoctionService.class);
            intent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "Running on Android O");
                stopService(intent);
                //startService(intent);
            } else {
                Log.d(TAG, "Running on Android N or lower");
                stopService(intent);
            }
        }
    }

    public Boolean isLiveTrackingOn(){

        //return false;
        String fmcg_Live_Km =customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"live_km");

        return  (fmcg_Live_Km.equalsIgnoreCase("Y"))||
                (fmcg_Live_Km.equalsIgnoreCase("5"))||
                (fmcg_Live_Km.equalsIgnoreCase("Y5"));


    }

}
