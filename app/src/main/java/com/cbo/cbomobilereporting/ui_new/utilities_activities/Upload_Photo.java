package com.cbo.cbomobilereporting.ui_new.utilities_activities;


import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.LoginFake;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.ExpenseRoot;
import com.flurry.android.FlurryAgent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import services.ServiceHandler;
import utils_new.Custom_Variables_And_Method;
import utils_new.up_down_ftp;

public class Upload_Photo extends AppCompatActivity implements up_down_ftp.AdapterCallback{
        String mLatLong;
		Button click,upload,back;
		ImageView image;
		Context context;
		Custom_Variables_And_Method customVariablesAndMethod;
		CBO_DB_Helper cbohelp;
		private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
		private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
		public static final int MEDIA_TYPE_IMAGE = 1;
		private Uri fileUri;
		ArrayList<String>checkList;
        String myPa_Id;
        ServiceHandler myServices;
		static File mediaFile;
		static ProgressDialog pd;
		private File output=null;
		String filename="";
		

		 public ArrayList<String>getFtpSize()
		 {
			 ArrayList<String>ftpsize=new ArrayList<String>();
			 Cursor c=cbohelp.getFTPDATA();
			 if(c.moveToFirst())
			 {
				 do{
					 ftpsize.add(c.getString(c.getColumnIndex("ftpip")));
					 ftpsize.add(c.getString(c.getColumnIndex("username")));
				 }while(c.moveToNext());
			 }
			 return ftpsize;
		 }
		

		
		 public  boolean deleteDirectory(File path)
			{
			    if( path.exists() ) {
			      File[] files = path.listFiles();
			      if (files == null) {
			          return true;
			      }
			      for(int i=0; i<files.length; i++) {
			         if(files[i].isDirectory()) {
			           deleteDirectory(files[i]);
			         }
			         else {
			           files[i].delete();
			         }
			      }
			    }
			    return( path.delete() );
			  }
		
		
		public void onCreate(Bundle b)
		{
			super.onCreate(b);

			setContentView(R.layout.upload_photo);

           Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
			TextView textView =(TextView) findViewById(R.id.hadder_text_1);
			setSupportActionBar(toolbar);


			textView.setText("Upload Photo");

            if (getSupportActionBar() != null){

				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
			}
			image=(ImageView)findViewById(R.id.upload_pic);
			click=(Button)findViewById(R.id.camera);
			upload=(Button)findViewById(R.id.upload_btn);
			back=(Button)findViewById(R.id.upload_back);
			context=this;
			customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
			cbohelp=new CBO_DB_Helper(getApplicationContext());
            myServices = new ServiceHandler(context);
			myPa_Id = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FM_PA_ID");
			upload.setVisibility(View.GONE);

			checkList=getFtpSize();

			click.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					File f = new File(Environment.getExternalStorageDirectory(), "CBO/"+IMAGE_DIRECTORY_NAME);
					/*File f = new File(Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);*/
					deleteDirectory(f);
					capture_Image();
				}
			});
			
			
			upload.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					
					if(checkList.size()==0)
					{
						AlertDialog.Builder builder1=new AlertDialog.Builder(Upload_Photo.this);
						builder1.setTitle("Upload Error...");
						builder1.setIcon(R.drawable.warn);
						builder1.setMessage(" No Details found for upload"+"\n"+"Please Download Data From Constants Page.....");
						builder1.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
								finish();
							}
							});
						builder1.show();
					}
				
					else
					{
						pd = new ProgressDialog(Upload_Photo.this);
						pd.setMessage("Uploading Image......."+"\n"+"please wait");
						pd.setCancelable(false);
						pd.setCanceledOnTouchOutside(false);
						pd.show();
						//File kfile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/Hello Camera/"+myfile+"");
						new up_down_ftp().uploadFile(output,context);
						//new UploadPhotoInBackGround().execute();
					}
					
					
					
				}
			});
			back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}


	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (intent.resolveActivity(getPackageManager()) != null) {

			File dir = new File(Environment.getExternalStorageDirectory(), "CBO/"+IMAGE_DIRECTORY_NAME);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
					//return true;
				}
			}
			Date date=new Date();
			filename = myPa_Id+"_"+Custom_Variables_And_Method.DCR_ID+"_"+date.getTime()+ ".jpg";
			output = new File(dir, filename);

//            fileTemp = ImageUtils.getOutputMediaFile();
			ContentValues values = new ContentValues(1);
			//values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
			values.put( MediaStore.Images.ImageColumns.DATA, output.getPath() );
			 fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            if (fileTemp != null) {
			// fileUri = Uri.fromFile(output);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//            } else {
//                Toast.makeText(this, getString(R.string.error_create_image_file), Toast.LENGTH_LONG).show();
//            }
		} else {
			Toast.makeText(this, getString(R.string.error_no_camera), Toast.LENGTH_LONG).show();
		}
	}

	private void capture_Image(){
		captureImage();
     /*   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                //return true;
            }
        }
         filename = PA_ID+"_"+dcr_id+"_"+exp_id+".jpg";
        output = new File(dir, filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/
	}

		/*private void captureImage() {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

			// start the image capture Intent
			startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
		}*/
	    

	    private void previewCapturedImage() {
			try {
				// hide video preview
				
				// bimatp factory
				BitmapFactory.Options options = new BitmapFactory.Options();

				// downsizing image as it throws OutOfMemory Exception for larger
				// images
				options.inSampleSize = 8;

				final Bitmap bitmap = BitmapFactory.decodeFile(output.getPath(),
						options);

				image.setImageBitmap(bitmap);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	    

	    
	    @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// if the result is capturing Image
			if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
				if (resultCode == RESULT_OK) {
					// successfully captured the image
					// display it in image view
					previewCapturedImage();
					click.setVisibility(View.GONE);
					upload.setVisibility(View.VISIBLE);
				} else if (resultCode == RESULT_CANCELED) {
					// user cancelled Image capture
					customVariablesAndMethod.msgBox(context,"User cancelled image capture");
				} else {
					// failed to capture image
					customVariablesAndMethod.msgBox(context,"Sorry! Failed to capture image");
				}
			
			}
		}

	@Override
	public void started(Integer responseCode, String message, String description) {

	}

	@Override
	public void progess(Integer responseCode, Long FileSize, Float value, String description) {

	}

	@Override
	public void complete(Integer responseCode, String message, String description) {
		pd.dismiss();
		new UploadPhotoInBackGround().execute();
	}

	@Override
	public void aborted(Integer responseCode, String message, String description) {
		pd.dismiss();
		customVariablesAndMethod.getAlert(context,message,description);
	}

	@Override
	public void failed(Integer responseCode, String message, String description) {
		pd.dismiss();
		customVariablesAndMethod.getAlert(context,message,description);

	}

	/*@Override
	public void upload_complete(final String IsCompleted) {
		pd.dismiss();
		if (IsCompleted.equals("S")) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable() {

				@Override
				public void run() {
					//new UploadPhotoInBackGround().execute();
				}
			});
		}else if (IsCompleted.equals("Y")) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable() {

				@Override
				public void run() {
					new UploadPhotoInBackGround().execute();
				}
			});
		}else if (IsCompleted.contains("ERROR")) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable() {

				@Override
				public void run() {
					//new UploadPhotoInBackGround().execute();
					String folder=IsCompleted.substring(6);
					customVariablesAndMethod.getAlert(context,"Folder not found",folder+"   Invalid path \nPlease contact your administrator");
				}
			});
		}else {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable() {

				@Override
				public void run() {
					customVariablesAndMethod.msgBox(context,"UPLOAD FAILED \n Please try again");
				}
			});
		}

	}*/


	class UploadPhotoInBackGround extends AsyncTask<Void, Void, String> {
			protected String doInBackground(Void... params) {
				mLatLong = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
				customVariablesAndMethod.IsValidLocation(context,mLatLong,0);
                Date myDate = new Date();
                String myDate1 = customVariablesAndMethod.convetDateMMddyyyy(myDate);
				mLatLong = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
				String address = customVariablesAndMethod.getAddressByLatLong(context,mLatLong);
                String response = myServices.getResponseMobileImageCommit(""+cbohelp.getCompanyCode(),"0",myPa_Id,myDate1,filename,mLatLong,address);

				return response;
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pd.setMessage("Processing......."+"\n"+"please wait");
				pd.show();


			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (!result.contains("ERROR")) {
					customVariablesAndMethod.msgBox(context,"File Successfully Sent....");
					finish();
				}
				pd.dismiss();
			}
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {

			if (item !=null){
				finish();
			}

			return super.onOptionsItemSelected(item);
		}
	}
