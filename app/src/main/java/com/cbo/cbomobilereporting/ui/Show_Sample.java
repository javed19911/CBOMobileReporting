package com.cbo.cbomobilereporting.ui;

import java.io.File;
import java.util.ArrayList;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.flurry.android.FlurryAgent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;


import utils.adapterutils.ViewPagerAdapter;
import utils_new.Custom_Variables_And_Method;

public class Show_Sample extends AppCompatActivity implements ViewPager.OnPageChangeListener{
	private static final String TAG = "Touch";
	ZoomControls zoom;
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    int ITEM_ID_INDEX=1;
    int LAST_INDEX;
	int image_index=0;
    
    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE,who=1;
    CBO_DB_Helper cbohelp;
    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
	//ImageView sample;
	
	MediaPlayer mPlayer;
    ImageButton save,back_img,next_img;
	ImageButton cancel;
	String rowid="";
	String item_name="";
	String pic_id="";
	ArrayList<String>show_id=new ArrayList<String>();
	ArrayList<String>show_name=new ArrayList<String>();
	private String sample_name="",sample_pob="",sample_sample="";

	protected View view;
	private ViewPager intro_images;
	private LinearLayout pager_indicator;
	private int dotsCount;
	private ImageView[] dots;
	private ViewPagerAdapter mAdapter;
	TextView hader_text;
	androidx.appcompat.widget.Toolbar toolbar;

	boolean showControl=false,show_back=true,show_next=true,show_save=true;


	private ArrayList<String> imageArray=null;

    String Dr_Id="";
	
	final int stub_id = R.drawable.no_image;
	
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.show_sample);
		//sample=(ImageView)findViewById(R.id.img_sample);
		toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder);
		hader_text = (TextView) findViewById(R.id.hadder_text_1);

		setSupportActionBar(toolbar);

		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
		}

		save= findViewById(R.id.save_sample_btn);
		back_img=findViewById(R.id.back_img);
		next_img=findViewById(R.id.next_img);
        cancel=findViewById(R.id.cancel);

		pic_id=getIntent().getExtras().getString("myid");
		show_id=getIntent().getExtras().getStringArrayList("samid");
		show_name=getIntent().getExtras().getStringArrayList("sam_name");
		LAST_INDEX=(show_id.size()-1);
		 zoom = new ZoomControls(Show_Sample.this);
		cbohelp=new CBO_DB_Helper(getApplicationContext());
//		String Row_Id=getIntent().getExtras().getString("rowid");
//		ITEM_ID_INDEX=Integer.parseInt(Row_Id);
		ITEM_ID_INDEX=getIntent().getExtras().getInt("rowid");
		mPlayer = MediaPlayer.create(Show_Sample.this, R.raw.ring);

        Dr_Id = getIntent().getStringExtra("dr_id");
		who=getIntent().getExtras().getInt("who");
		if (who==1){
			save.setVisibility(View.INVISIBLE);
		}else{
			Bundle getExtra = getIntent().getExtras();
			if (getExtra != null) {
				sample_name = getExtra.getString("sample_name");
				sample_pob = getExtra.getString("sample_pob");
				sample_sample = getExtra.getString("sample_sample");
			}
		}

		imageArray= new ArrayList<>();

		if(ITEM_ID_INDEX<=0) {
			prepareImageArray(pic_id,ITEM_ID_INDEX);

			//DisplayImage(sample, pic_id);
			//usingSimpleImage(sample);
			back_img.setVisibility(View.INVISIBLE);
			next_img.setVisibility(View.VISIBLE);
			save.setVisibility(View.VISIBLE);
			//usingSimpleImage(sample);
			setReference();
		} else if(ITEM_ID_INDEX==LAST_INDEX) {
			prepareImageArray(pic_id,ITEM_ID_INDEX);

			//DisplayImage(sample, pic_id);
			//usingSimpleImage(sample);
			next_img.setVisibility(View.INVISIBLE);
			back_img.setVisibility(View.VISIBLE);
			save.setVisibility(View.VISIBLE);
			//usingSimpleImage(sample);
			setReference();
		} else {
			prepareImageArray(pic_id,ITEM_ID_INDEX);

			back_img.setVisibility(View.VISIBLE);
			next_img.setVisibility(View.VISIBLE);
			save.setVisibility(View.VISIBLE);
			//DisplayImage(sample, pic_id);
			//usingSimpleImage(sample);
			//usingSimpleImage(sample);
			setReference();
		}
			
		
		
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveAndNextImages();
				
			}
		});


		next_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showNextImage();
			}
		});

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                makeFullScreen();
            }
        });


		back_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					
					showPreviousImage();
			}
		});

		
	}

	public void prepareImageArray(String id,int index){
		if (hader_text != null) {
			hader_text.setText(show_name.get(index));
		}
		imageArray.clear();
		File path = new File(Environment.getExternalStorageDirectory(), "cbo/product/"+id+".jpg");
		File path1 = new File(Environment.getExternalStorageDirectory(), "cbo/product/"+id+"_"+ Custom_Variables_And_Method.pub_doctor_spl_code +".jpg");

		if(path1.exists()) {
			imageArray.add(path1.toString());
			bind_imageArray("cbo/product/"+id+"_"+ Custom_Variables_And_Method.pub_doctor_spl_code);
		}else if(path.exists()){
			imageArray.add(path.toString());
			bind_imageArray("cbo/product/"+id);
		}else {
			imageArray.add("no_image");
		}

	}

	public void bind_imageArray(String path){
		image_index+=1;
		File path1 = new File(Environment.getExternalStorageDirectory(),path+"_"+image_index+".jpg");
		if(path1.exists()) {
			imageArray.add(path1.toString());
			bind_imageArray(path);
		}else{
			image_index=0;
		}
	}

	public void setReference() {
		intro_images = (ViewPager) findViewById(R.id.pager_introduction);
		pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);


		mAdapter = new ViewPagerAdapter(Show_Sample.this, imageArray);
		intro_images.setAdapter(mAdapter);
		intro_images.setCurrentItem(0);
		intro_images.setOnPageChangeListener(this);
		setUiPageViewController();

		show_back= (back_img.getVisibility() == View.VISIBLE);
		show_next= (next_img.getVisibility() == View.VISIBLE);
		show_save= who == 1 || (save.getVisibility() == View.VISIBLE);
		showControl=false;

		makeFullScreen();

        if (dotsCount==1 && cancel.getVisibility() != View.VISIBLE){
            makeFullScreen();
        }
		pager_indicator.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
                makeFullScreen();
			}
		});
	}


	public void makeFullScreen(){


        if (!showControl) {
			showControl=true;
            if (getSupportActionBar() != null) {
                toolbar.setVisibility(View.GONE);
            }
           // control.setVisibility(View.GONE);
        }else{
			showControl=false;
            if (getSupportActionBar() != null) {
                toolbar.setVisibility(View.VISIBLE);
            }
           //control.setVisibility(View.VISIBLE);
        }

        if (show_back && back_img.getVisibility() == View.VISIBLE){
			back_img.setVisibility(View.INVISIBLE);
		}else  if(show_back){
			back_img.setVisibility(View.VISIBLE);
		}

		if (show_next && next_img.getVisibility() == View.VISIBLE){
			next_img.setVisibility(View.INVISIBLE);
		}else if(show_next){
			next_img.setVisibility(View.VISIBLE);
		}

		if (show_save && (who == 1 || (save.getVisibility() == View.VISIBLE))){
			save.setVisibility(View.INVISIBLE);
		}else  if(show_save) {
			save.setVisibility(View.VISIBLE);
		}

        if (cancel.getVisibility() == View.VISIBLE){
            cancel.setVisibility(View.INVISIBLE);
        }else{
            cancel.setVisibility(View.VISIBLE);
        }

    }

	private void setUiPageViewController() {

		dotsCount = mAdapter.getCount();
		dots = new ImageView[dotsCount];
		pager_indicator.removeAllViews();
		for (int i = 0; i < dotsCount; i++) {
			dots[i] = new ImageView(this);
			dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT
			);

			params.setMargins(4, 0, 4, 0);

			pager_indicator.addView(dots[i], params);
		}

		dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
	}




	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		for (int i = 0; i < dotsCount; i++) {
			dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
		}
        if (dotsCount-1==position && cancel.getVisibility() != View.VISIBLE){
            makeFullScreen();
        }else if(dotsCount-1 != position && cancel.getVisibility() == View.VISIBLE){
            makeFullScreen();
        }
		dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

	}

	@Override
	public void onPageScrollStateChanged(int state) {

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
	
	
	public void DisplayImage( ImageView imageView,String id){
		 
		 File path = new File(Environment.getExternalStorageDirectory(), "cbo/"+id+".jpg");
		 File path1 = new File(Environment.getExternalStorageDirectory(), "cbo/"+id+"_"+ Custom_Variables_And_Method.pub_doctor_spl_code +".jpg");
		
		
		 
		 if(path1.exists()) {
			 Bitmap b = BitmapFactory.decodeFile(path1.toString());
			 imageView.setImageBitmap(b);
			 Custom_Variables_And_Method.doctor_image_name=path1.getName();
		 }else if(path.exists()){
			 Bitmap b = BitmapFactory.decodeFile(path.toString());
			 imageView.setImageBitmap(b);
			 Custom_Variables_And_Method.doctor_image_name=path.getName();
		 }else {
	            //queuePhoto(url, imageView);
	            imageView.setImageResource(stub_id);
	           // save.setVisibility(View.GONE);
				Custom_Variables_And_Method.doctor_image_name="no_image";
		 }
		 
	 }
	

	/*public void usingSimpleImage(ImageView imageView) {
		ImageAttacher mAttacher = new ImageAttacher(imageView);
		ImageAttacher.MAX_ZOOM = 3.0f; // triple the current Size
		ImageAttacher.MIN_ZOOM = 1.0f; // same as the  current Size
		MatrixChangeListener mMaListener = new MatrixChangeListener();
		mAttacher.setOnMatrixChangeListener(mMaListener);
		PhotoTapListener mPhotoTap = new PhotoTapListener();
		mAttacher.setOnPhotoTapListener(mPhotoTap);
	}

	private class PhotoTapListener implements OnPhotoTapListener {

	        @Override
	        public void onPhotoTap(View view, float x, float y) {
	        }
	    }

	    private class MatrixChangeListener implements OnMatrixChangedListener {

	        @Override
	        public void onMatrixChanged(RectF rect) {

	        }
	    }
	    */
	    public void saveAndNextImages() {
	    	//String DR_ID=Custom_Variables_And_Method.DR_ID;
			ArrayList<String>doclist=cbohelp.getDoctorList();
			ArrayList<String>docitems=cbohelp.getDoctorAllItems();

			if(doclist.contains(Dr_Id)&&(docitems.contains(show_id.get(ITEM_ID_INDEX)))) {
				cbohelp.updateVisuals(Dr_Id, show_id.get(ITEM_ID_INDEX), show_name.get(ITEM_ID_INDEX), "0", "0", "0","1");
			} else {
				cbohelp.insertVisuals(Dr_Id, show_id.get(ITEM_ID_INDEX), show_name.get(ITEM_ID_INDEX), "0", "0", "0","1");
			}

			ITEM_ID_INDEX=ITEM_ID_INDEX+1;
			if (ITEM_ID_INDEX>LAST_INDEX){
				ITEM_ID_INDEX = 0;
			}

			if(ITEM_ID_INDEX==0) {
				prepareImageArray(show_id.get(ITEM_ID_INDEX),ITEM_ID_INDEX);
				//DisplayImage(sample, show_id.get(ITEM_ID_INDEX+1));
				//usingSimpleImage(sample);
				back_img.setVisibility(View.INVISIBLE);
				next_img.setVisibility(View.VISIBLE);
				setReference();
			}else if((ITEM_ID_INDEX>0)&&(ITEM_ID_INDEX<LAST_INDEX)) {
				prepareImageArray(show_id.get(ITEM_ID_INDEX),ITEM_ID_INDEX);

				//DisplayImage(sample, show_id.get(ITEM_ID_INDEX+1));
				//usingSimpleImage(sample);
				back_img.setVisibility(View.VISIBLE);
				next_img.setVisibility(View.VISIBLE);
				save.setVisibility(View.VISIBLE);
				setReference();
			}else if(ITEM_ID_INDEX==LAST_INDEX) {
				prepareImageArray(show_id.get(ITEM_ID_INDEX),ITEM_ID_INDEX);
				next_img.setVisibility(View.INVISIBLE);
				save.setVisibility(View.VISIBLE);
				back_img.setVisibility(View.VISIBLE);
				setReference();
			}

			mPlayer.start();

			cbohelp.close();
	    }
	    
	    public void showNextImage() {
	    	ITEM_ID_INDEX=ITEM_ID_INDEX+1;
			if((ITEM_ID_INDEX>0)&&(ITEM_ID_INDEX<LAST_INDEX)) {
				prepareImageArray(show_id.get(ITEM_ID_INDEX),ITEM_ID_INDEX);

				//DisplayImage(sample, show_id.get(ITEM_ID_INDEX));
				//usingSimpleImage(sample);
				back_img.setVisibility(View.VISIBLE);
				next_img.setVisibility(View.VISIBLE);
				save.setVisibility(View.VISIBLE);
				setReference();
			} else if(ITEM_ID_INDEX==LAST_INDEX) {
				prepareImageArray(show_id.get(ITEM_ID_INDEX),ITEM_ID_INDEX);
				//DisplayImage(sample, show_id.get(ITEM_ID_INDEX));
				//usingSimpleImage(sample);
				next_img.setVisibility(View.INVISIBLE);
				back_img.setVisibility(View.VISIBLE);
				save.setVisibility(View.VISIBLE);
				setReference();
			}
			
			mPlayer.start();
	    }

	    public void showPreviousImage() {
	    	ITEM_ID_INDEX=ITEM_ID_INDEX-1;
			if((ITEM_ID_INDEX<LAST_INDEX)&&(ITEM_ID_INDEX>0)) {
				prepareImageArray(show_id.get(ITEM_ID_INDEX),ITEM_ID_INDEX);

				//DisplayImage(sample, show_id.get(ITEM_ID_INDEX));
				//usingSimpleImage(sample);
				next_img.setVisibility(View.VISIBLE);
				save.setVisibility(View.VISIBLE);
				setReference();
			}else if(ITEM_ID_INDEX==0) {
				prepareImageArray(show_id.get(ITEM_ID_INDEX),ITEM_ID_INDEX);

				 //DisplayImage(sample, show_id.get(ITEM_ID_INDEX));
				// usingSimpleImage(sample);
				back_img.setVisibility(View.INVISIBLE);
				next_img.setVisibility(View.VISIBLE);
				//save.setVisibility(View.VISIBLE);
				setReference();
			}else if(ITEM_ID_INDEX==LAST_INDEX) {
				prepareImageArray(show_id.get(ITEM_ID_INDEX),ITEM_ID_INDEX);

					 //DisplayImage(sample, show_id.get(ITEM_ID_INDEX));
					 //usingSimpleImage(sample);
					back_img.setVisibility(View.VISIBLE);
					next_img.setVisibility(View.VISIBLE);
					//save.setVisibility(View.VISIBLE);
				setReference();
			}

			mPlayer.start();
		
	    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	}
