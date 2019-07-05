package com.cbo.cbomobilereporting.ui_new;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import utils_new.Custom_Variables_And_Method;

public class SystemAlertActivity extends AppCompatActivity {

    Context context;
    TextView title,message,Pa_id;
    Button ok;

    Custom_Variables_And_Method customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_alert);
        this.setFinishOnTouchOutside(false);
        context = this;

        title = findViewById(R.id.alert_title);
        message = findViewById(R.id.message);
        Pa_id = findViewById(R.id.PA_ID);
        ok = findViewById(R.id.ok);

        title.setText(getIntent().getStringExtra("title"));
        message.setText(getIntent().getStringExtra("message"));
        Pa_id.setText(""+Custom_Variables_And_Method.PA_ID);



        //customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ShowSystemAlert","N");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,
                        "ShowSystemAlert","Y");
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ShowSystemAlert","Y");
    }
}
