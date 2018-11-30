package com.cbo.cbomobilereporting.ui;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;


public class AnotherActivity  extends Activity{
	TextView rpt;
	
	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.another_activity);
		rpt=(TextView)findViewById(R.id.error_rpt);

	}


}
