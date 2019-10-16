package com.cbo.cbomobilereporting.ui_new.utilities_activities;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.Sync.SyncAllDataFirebase;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import services.MyAPIService;
import utils_new.AppAlert;

public class SyncFirebaseActivity extends AppCompatActivity {

    Button upload,download,requestBtn;
    ImageView close;
    TextView msg;
    Context context;
    SyncAllDataFirebase syncAllDataFirebase;
    Boolean LogOutOnBackPressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_firebase);


        upload = findViewById(R.id.upload);
        requestBtn = findViewById(R.id.request);
        download = findViewById(R.id.download);
        close = findViewById(R.id.close);
        msg = findViewById(R.id.msg);

        context = this;
        syncAllDataFirebase =  new SyncAllDataFirebase(context);


        if (MyCustumApplication.getInstance().getUser().getLoggedInAsSupport()){
            upload.setVisibility(View.GONE);
        }else{
            requestBtn.setVisibility(View.GONE);
            download.setVisibility(View.GONE);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                syncAllDataFirebase.upload();
            }
        });

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //msg.setVisibility(View.VISIBLE);
                //Start of call to service

                HashMap<String, String> request = new HashMap<>();
                request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
                request.put("sPA_ID", MyCustumApplication.getInstance().getUser().getID());
                request.put("sMessage","[{\"msgtyp\":\"FIREBASE_SYNC\"},{\"tilte\":\"Hello Form CBO\"},{\"msg\":\"Test Msg form CBO\"},{\"url\":\"\"},{\"logo\":\"\"}]");
                request.put("iDESIG_ID", "");
                request.put("iKEY","");


                download.setEnabled(false);

                new MyAPIService(context)
                        .execute(new ResponseBuilder("GCM_MessagePush_Domain", request)
                                .setDescription("Please Wait.. \n Requesting for APP Data...").setResponse(new CBOServices.APIResponse() {
                                    @Override
                                    public void onComplete(Bundle message) {

                                       // parser_login(message);
                                        requestBtn.setVisibility(View.GONE);
                                        enableDownloadButton(30);
                                    }

                                    @Override
                                    public void onResponse(Bundle response) {

                                    }

                                    @Override
                                    public void onError(String message, String description) {
                                        AppAlert.getInstance().getAlert(context,message,description);
                                        download.setEnabled(true);
                                    }


                                })
                        );
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                syncAllDataFirebase.download();
            }
        });

        if (getIntent() != null){
            LogOutOnBackPressed = getIntent().getBooleanExtra("LogOut",false);
            if (LogOutOnBackPressed){
                requestBtn.performClick();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (download.isEnabled() || download.getVisibility() == View.GONE) {
            if (LogOutOnBackPressed) {
                MyCustumApplication.getInstance().Logout((AppCompatActivity) context);
            } else {
                super.onBackPressed();
            }
        }

    }

    private void enableDownloadButton(int sec){
        int secs[] ={sec};
        download.setText("Please Wait (" + sec + ")");
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (0 == secs[0]) {
                            download.setEnabled(true);
                            msg.setVisibility(View.GONE);
                            requestBtn.setVisibility(View.VISIBLE);
                            download.setText("Download APP Data");
                            download.performClick();
                        }else{
                            enableDownloadButton(secs[0] - 1);
                        }
                    }
                });
            }
        }, 1000);
    }
}
