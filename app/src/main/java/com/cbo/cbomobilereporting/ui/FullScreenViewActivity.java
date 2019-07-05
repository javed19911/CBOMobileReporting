package com.cbo.cbomobilereporting.ui;



import java.util.ArrayList;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;

import android.widget.Button;


import com.cbo.cbomobilereporting.R;

import utils.Utils;
import utils.adapterutils.FullScreenImageAdapter;
import utils.adapterutils.ImageLoader;

public class FullScreenViewActivity extends Activity{

	private Utils utils;
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;
	Button save;
	String pic_id="";
	int ITEM_ID_INDEX=0;
	ImageLoader myimg;
	String sample_id="";
	ArrayList<String>show_id=new ArrayList<String>();

	ArrayList<String>path=new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.activity_fullscreen_view);
		

		viewPager = (ViewPager) findViewById(R.id.pager);
		//myimg=new ImageLoader(getApplicationContext());
		
		//save=(Button)findViewById(R.id.save);
		utils = new Utils(getApplicationContext());
		//save.clearAnimation();
		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);
		sample_id=i.getStringExtra("samid");
		
		//pic_id=getIntent().getExtras().getString("myid");
		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
				utils.getFilePaths());
		//adapter=new FullScreenImageAdapter(FullScreenViewActivity.this, loader.getFilePath(pic_id));

		viewPager.setAdapter(adapter);

		// displaying selected image first
		viewPager.setCurrentItem(position);
		/*
		
		viewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//String name=utils.getResources().getResourceName(R.id.pager);
				path=myimg.getFilePath(sample_id);
						//utils.getFilePaths();
				
				return false;
			}
		});
		*/
		
		//String name=getResources().getResourceName(position);
		//Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_LONG).show();
	}
	
	/*@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	*/
	}
