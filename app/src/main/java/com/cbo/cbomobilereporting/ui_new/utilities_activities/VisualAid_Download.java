package com.cbo.cbomobilereporting.ui_new.utilities_activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.flurry.android.FlurryAgent;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.ServiceHandler;
import utils.ExceptionHandler;
import utils.MyConnection;
import utils_new.Custom_Variables_And_Method;
import utils_new.up_down_ftp;

public class VisualAid_Download extends AppCompatActivity implements up_down_ftp.AdapterCallback
	{
		TextView percent,tname,comp_name,msg;
		Button save;
		ProgressBar pd;
		Bitmap bmImg;
		private ImageLoaderListener listener;
		CBO_DB_Helper cbohelp;
		Custom_Variables_And_Method customVariablesAndMethod;
		ArrayList<String>mydatanew=new ArrayList<String>();
		ArrayList<String>download_file=new ArrayList<String>();
		ResultSet rs;
		int PA_ID;
		int count=0;
		int myno1,myno2;
		String pa_name="",msg_text;
		ServiceHandler myServiceHandler;
		CBO_DB_Helper mycboDbHelp;
		Context context;


		String dest_file_path = "test.pdf";
		int downloadedSize = 0, totalsize;
		String download_file_url = "http://cboinfotech.co.in/VISUALAID/1.pdf";
		float per = 0;
		private int next_image_index=0;


		public void showDialog()
		{
			AlertDialog.Builder builder1=new AlertDialog.Builder(VisualAid_Download.this);
			builder1.setTitle("Download Compleate");
			builder1.setMessage(" All Visual Aid Downloaded Sucessfully....");
			builder1.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
					VisualAid_Download.this.finish();
				}
				});
			builder1.setCancelable(false);
			builder1.show();
		}

		
		public void onCreate(Bundle b)
		{
			super.onCreate(b);
			//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
			setContentView(R.layout.visualaid_download);

			Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
			TextView textView =(TextView) findViewById(R.id.hadder_text_1);
			setSupportActionBar(toolbar);
			if (getSupportActionBar() != null){
				textView.setText("VisualAid Download");
				getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
			}


			percent=(TextView)findViewById(R.id.per);
			save=(Button)findViewById(R.id.save_visual_download2);
			//update=(Button)findViewById(R.id.chk_update);
			pd=(ProgressBar)findViewById(R.id.pb);
			tname=(TextView)findViewById(R.id.visual_empname);
			comp_name=(TextView)findViewById(R.id.visual_compname);
			msg=(TextView)findViewById(R.id.msgbox);
			cbohelp=new CBO_DB_Helper(getApplicationContext());

			PA_ID= Custom_Variables_And_Method.PA_ID;
			pa_name=Custom_Variables_And_Method.PA_NAME;


			context=this;
			myServiceHandler = new ServiceHandler(context);
			customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
			
			if(!pa_name.equals(Custom_Variables_And_Method.PA_NAME))
			{
				tname.setText("       user Logged-Out");
				
				comp_name.setText("");
				customVariablesAndMethod.msgBox(context,"Connection Reset..."+"\n"+"please login again from login screen");
				
			}
			else
			{
					
			tname.setText("Welcome  "+ pa_name);
			comp_name.setText(Custom_Variables_And_Method.COMPANY_NAME);
			}
			
			percent.setVisibility(View.GONE);
			pd.setVisibility(View.GONE);
			msg.setVisibility(View.GONE);
		
			save.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
				if(exists(Custom_Variables_And_Method.WEB_URL+"/visualaid/").equals("404")) {
						customVariablesAndMethod.msgBox(context,"No Data Found For Download..");
				} else {
					
					percent.setVisibility(View.VISIBLE);
					pd.setVisibility(View.VISIBLE);
					percent.setText("Downloading will be start shortly.....");
					msg.setVisibility(View.VISIBLE);
					msg.setGravity(Gravity.CENTER);
					msg.setText(" Please don't use Back Button While Downloading Visual Aids.....");
					File f=new File(Environment.getExternalStorageDirectory(),"cbo/product");
					deleteDirectory(f);
					//getcount();
					// new Get_count().execute();
					save.setVisibility(View.GONE);
					//new imgDownloadInBackGround().execute(PA_ID);
					new Doback().execute();
					}
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
			 //Intent i=new Intent(getApplicationContext(),MainMenu.class);
		    	//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    //	startActivity(i);
			 super.onBackPressed();
		 }

		
		public String exists(String URLName)
		{
			int code=0;
			 HttpURLConnection connection = null;
			 try{         
			     URL myurl = new URL(URLName);        
			     connection = (HttpURLConnection) myurl.openConnection(); 
			     //Set request to header to reduce load as Subirkumarsao said.       
			     connection.setRequestMethod("HEAD");         
			     code =code+ connection.getResponseCode();        
			     System.out.println("" + code); 
			 } catch(Exception e) {
			 e.printStackTrace();
			 }
			 return""+code;
		  } 
		
		

		public void downloadToFolder(String fileUrl)
		{
			
				URL myFileUrl = null;
		        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		        StrictMode.setThreadPolicy(policy);
		        try {
		            myFileUrl = new URL(fileUrl);
		        } catch (Exception e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		        try {
		            HttpURLConnection conn = (HttpURLConnection) myFileUrl
		                    .openConnection();
		            conn.setDoInput(true);
		            conn.connect();
		            InputStream is = conn.getInputStream();
		            Log.i("im connected", fileUrl);
		            bmImg = BitmapFactory.decodeStream(is);

		        } catch (Exception e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
					Log.d("javed pdf test",e.toString());
		        }
		}


		File downloadFile(String id,String name) {
			File file = null;
			// set the path where we want to save the file
			File SDCardRoot = Environment.getExternalStorageDirectory();

			new File(SDCardRoot + "/cbo").mkdir();
			// create a new file, to save the downloaded file
			file = new File(SDCardRoot + "/cbo/"+id+".pdf");
			//file = new File(SDCardRoot, dest_file_path);


			URL myFileUrl = null;
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			try {

				myFileUrl = new URL(Custom_Variables_And_Method.WEB_URL + "/visualaid/" + id + ".pdf");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				HttpURLConnection conn = (HttpURLConnection) myFileUrl
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				Log.i("im connected", myFileUrl.toString());
				FileOutputStream fileOutput = new FileOutputStream(file);
				// create a buffer...
				byte[] buffer = new byte[1024 * 1024];
				int bufferLength = 0;

				totalsize = conn.getContentLength();

				while ((bufferLength = is.read(buffer)) > 0) {
					fileOutput.write(buffer, 0, bufferLength);
					downloadedSize += bufferLength;
					per = ((float) downloadedSize / totalsize) * 100;
					msg_text=name+"is being Downloded...\n"+(int) per+"% compleded";

					Log.d("javed pdf test","Total PDF File size  : "
							+ (totalsize / 1024)
							+ " KB\n\nDownloading PDF " + (int) per
							+ "% complete");
				}
				// close the output stream when complete //
				fileOutput.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("javed pdf test",e.toString());
			}

			return file;
		}




		void saveImage(String id,String next_index)
		{
	        try {

				File filename;
				downloadToFolder(Custom_Variables_And_Method.WEB_URL + "/visualaid/" + id + ".jpg");

				String path = Environment.getExternalStorageDirectory().toString();
	            Log.i("in save()", "after mkdir");
	           
	            new File(path + "/cbo").mkdir();
	            filename = new File(path + "/cbo/"+id+next_index+".jpg");
	            Log.i("in save()", "after file");
	            FileOutputStream out = new FileOutputStream(filename);
	            Log.i("in save()", "after outputstream");
	            bmImg.compress(Bitmap.CompressFormat.JPEG, 90, out);
				FileOutputStream fo = new FileOutputStream(filename);
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				bmImg.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
				fo.write(bytes.toByteArray());
				fo.close();
	            out.flush();
	            out.close();
	            Log.i("in save()", "after outputstream closed");
	            MediaStore.Images.Media.insertImage(getContentResolver(),
						filename.getAbsolutePath(), filename.getName(),
						filename.getName());
	           
	         /*   Toast.makeText(getApplicationContext(),
	                    "File is Saved in  " + filename, Toast.LENGTH_LONG).show();*/
				next_image_index+=1;
				if(!(exists(Custom_Variables_And_Method.WEB_URL+"/visualaid/"+ id + "_"+next_image_index+ ".jpg").equals("404"))) {

					saveImage(id,"_"+next_image_index);

				}else{
					next_image_index=0;
				}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
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

		/*@Override
		public void upload_complete(String IsCompleted) {
		*//*	if (IsCompleted.equals("S")) {
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
						//new Upload_Photo.UploadPhotoInBackGround().execute();
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
			}*//*
		}*/

		@Override
		public void started(Integer responseCode, String message, String description) {

		}

		@Override
		public void progess(Integer responseCode, Long FileSize, Float value, String description) {

		}

		@Override
		public void complete(Integer responseCode, String message, String description) {

		}

		@Override
		public void aborted(Integer responseCode, String message, String description) {

		}

		@Override
		public void failed(Integer responseCode, String message, String description) {

		}


		class Doback extends AsyncTask<Void, Integer, String> {
			
			private int progress;
			
			
			@Override
			protected String doInBackground(Void... params) {
				String load_img="";
				String response_VISUALAID_DOWNLOAD;
				try {
					response_VISUALAID_DOWNLOAD = myServiceHandler.getResponse_VISUALAID_DOWNLOAD(cbohelp.getCompanyCode(), "" + PA_ID);
				}catch (Exception e){
					return "ERROR apk "+ e;
				}


                  if((response_VISUALAID_DOWNLOAD !=null)&&(!response_VISUALAID_DOWNLOAD.toLowerCase().contains("[error]"))){
                      try {
                          JSONObject jsonObject = new JSONObject(response_VISUALAID_DOWNLOAD);
                          JSONArray jsonArray = jsonObject.getJSONArray("Tables0");
                          count = jsonArray.length();
						  String visual_pdf=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"VISUALAIDPDFYN");
						  String ext=".jpg";
						  if(visual_pdf.equals("Y")){
							  ext=".pdf";
						  }
                          for (int i =0;i<jsonArray.length();i++){
                              JSONObject object = jsonArray.getJSONObject(i);
							  download_file.clear();
                              mydatanew.add(object.getString("FILE_NAME"));
							  download_file.add(object.getString("FILE_NAME"));

							  msg_text=object.getString("ITEM_NAME")+" is being Downloaded...";

							  if (object.getString("ITEM_NAME").equals("CATALOG")){
								  download_file.clear();
								  download_file.add(object.getString("ITEM_NAME"));
							  }

							  publishProgress(progress);
							  progress+=1;
							  Log.e("progress", ""+progress);



							  if(object.getString("ITEM_NAME").equals("CATALOG")){
								  new up_down_ftp().download_Directory(context);

							  }else {
                                  load_img=object.getString("FILE_NAME");
								  msg_text=object.getString("ITEM_NAME")+" is being Downloaded...";
								  if(visual_pdf.equals("Y")){
									  //downloadFile(load_img,object.getString("ITEM_NAME"));
									  new up_down_ftp().download_visual_aids(context,download_file);
								  }else if(!(exists(Custom_Variables_And_Method.WEB_URL+"/visualaid/"+object.getString("FILE_NAME")+ext).equals("404"))) {
									  //saveImage(load_img,"");
									  new up_down_ftp().download_visual_aids(context,download_file);
								  }
                              }




                          }


                      }catch (JSONException json){
                          //mycon.msgBox("Exception Found......");
                      }
                  }
                    else {
					  return "ERROR service "+ response_VISUALAID_DOWNLOAD;
					  // mycon.msgBox("Nothing Found......");
                  }

				//new up_down_ftp().download_visual_aids(context,mydatanew);

					/*while (progress < mydatanew.size()) {

						saveImage(mydatanew.get(progress),"");
						progress += 1;

						publishProgress(progress*100/mydatanew.size());
					}
*/

					//cbohelp.close();
				return null;
			}


			

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				progress = 0;
				percent.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "Start Downloading..", Toast.LENGTH_LONG).show();
				super.onPreExecute();
			
			}
			@Override
			protected void onProgressUpdate(Integer... values) {

				/*--- show download progress on main UI thread---*/
				
				pd.setProgress((values[0])*100/count);
				
				percent.setText((values[0]*100)/count +" % downloaded");
				msg.setText(msg_text);
				super.onProgressUpdate(values);
				Log.e("mycount", ""+count);
				
			}

			@Override
			protected void onPostExecute(String result) {

				super.onPostExecute(result);
				if ((result==null)) {

					if (listener != null) {
						listener.onImageDownloaded(bmImg);
					}
					Toast.makeText(getApplicationContext(), "Download Compleate", Toast.LENGTH_LONG).show();
					msg.setText("");
					SystemClock.sleep(2000);
					showDialog();

				}
				else{
					customVariablesAndMethod.msgBox(context,"Nothing Found..");
				}
			}
		}

		public interface ImageLoaderListener {
			
			void onImageDownloaded(Bitmap bmp);
			
			}
		

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if (item != null){
				finish();
			}


			return super.onOptionsItemSelected(item);
		}
	}
