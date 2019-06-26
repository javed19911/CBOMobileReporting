package com.cbo.cbomobilereporting.ui_new.utilities_activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.Sync.SyncAllDataFirebase;

public class SyncFirebaseActivity extends AppCompatActivity {

    Button upload,download;
    Context context;
    SyncAllDataFirebase syncAllDataFirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_firebase);


        upload = findViewById(R.id.upload);
        download = findViewById(R.id.download);

        context = this;
        syncAllDataFirebase =  new SyncAllDataFirebase(context);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                syncAllDataFirebase.upload();
            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                syncAllDataFirebase.download();
            }
        });

    }
}
