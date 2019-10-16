package com.cbo.cbomobilereporting.ui;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.R;


public class AnotherActivity  extends AppCompatActivity {
	TextView rpt;
	
	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.another_activity);
		rpt=(TextView)findViewById(R.id.error_rpt);
		rpt.setText(getIntent().getStringExtra("error"));

	}


}
