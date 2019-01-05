package com.cbo.cbomobilereporting.ui;





import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


import com.cbo.cbomobilereporting.R;
import com.flurry.android.FlurryAgent;

import utils.ExceptionHandler;

public class Load_New extends AppCompatActivity{
	Button load;
	TextView tv;
	
	public void onCreate(Bundle b){
		super.onCreate(b);

		setContentView(R.layout.load_new);

		//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
		TextView textView =(TextView) findViewById(R.id.hadder_text_1);

		setSupportActionBar(toolbar);
         		if (getSupportActionBar() != null){
					textView.setText("Load New");
					getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
					getSupportActionBar().setDisplayHomeAsUpEnabled(true);
					getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
				}

		load=(Button)findViewById(R.id.load_web);
		tv=(TextView)findViewById(R.id.ver_text);
		tv.setText("You are using old version of MobileReporting \n  Please UnInstall this Version and Download new .\n For New Version Click Below Button.. ");
		
		load.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_VIEW,

                Uri.parse("https://play.google.com/store/apps/details?id=com.cbo.cbomobilereporting&hl=en"));
                  startActivity(i);
					load.setVisibility(View.GONE);
			}
		});
	}

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, "M3GXGNKRRC8F9VPNYYY4");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

	public void onBackPressed(){
		 super.onBackPressed();
	 }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item !=null){

			finish();
		}

		return super.onOptionsItemSelected(item);
	}
}
