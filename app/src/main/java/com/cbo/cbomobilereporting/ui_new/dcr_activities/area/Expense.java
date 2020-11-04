package com.cbo.cbomobilereporting.ui_new.dcr_activities.area;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.ExpenseNew.Mexpenses;
import com.flurry.android.FlurryAgent;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.CboServices;
import services.ServiceHandler;
import utils.adapterutils.Expenses_Adapter;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinAdapter2;
import utils.adapterutils.SpinnerModel;
import utils_new.Custom_Variables_And_Method;
import utils_new.GalleryUtil;
import utils_new.up_down_ftp;


public class Expense extends AppCompatActivity implements Expenses_Adapter.Expense_interface, up_down_ftp.AdapterCallback {
	Spinner datype,distance,exphead;
	EditText daAmt,distAmt,exhAmt,rem,rem_final;

	TextView textRemark;
	ImageView attach_img;
	String filename="",DA_typ="";
	private final int GALLERY_ACTIVITY_CODE=200;
	private final int RESULT_CROP = 400;
	private final int REQUEST_CAMERA=201;
	String picturePath="";
	ImageView image_capture1;
	private File output=null;


	ArrayList<Map<String, String>> data = null;

	public ProgressDialog progress1;
	private  static final int MESSAGE_INTERNET=1,MESSAGE_INTERNET_SAVE_EXPENSE=2,MESSAGE_INTERNET_DCR_COMMITEXP=3,MESSAGE_INTERNET_DCR_DELETEEXP=4;

	Button save,save_exp,add_exp;
	ListView mylist;
	Expenses_Adapter sm;
	LinearLayout mainlayout;
	SpinAdapter2 adapter;
	SpinAdapter adapter2, adapter1;
	int PA_ID;
	CBO_DB_Helper cbohelp;
	String paid="",ttl_distance="",exp_id="",exp_hed = "",my_Amt="",my_rem="",id="";
	String DistRate="",datype_val="";
	String dcr_id="";
	String datype_local="",datype_ex="",datype_ns="";
	String dist_id3="";
	ArrayList<String>station,exp_head;
	String chm_ok="",stk_ok="",exp_ok="";
	ArrayList<SpinnerModel>mylist2=new ArrayList<SpinnerModel>();
    ServiceHandler myServiceHandler;
	Custom_Variables_And_Method customVariablesAndMethod;
	Context context;
    String value;
	TableLayout DA_layout;

    public ArrayList<String>getmydata()
	{
		ArrayList<String>raw=new ArrayList<String>();
		StringBuilder chm=new StringBuilder();
		StringBuilder stk=new StringBuilder();
		StringBuilder exp=new StringBuilder();
		Cursor c=cbohelp.getFinalSubmit();
		if(c.moveToFirst())
		{
			do{
				chm.append(c.getString(c.getColumnIndex("chemist")));
				stk.append(c.getString(c.getColumnIndex("stockist")));
				exp.append(c.getString(c.getColumnIndex("exp")));
			}while(c.moveToNext());
			
		}
		raw.add(chm.toString());
		raw.add(stk.toString());
		raw.add(exp.toString());
		return raw;
	}
	
	public void onCreate(Bundle b){
		super.onCreate(b);
		//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setContentView(R.layout.expense);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        FlurryAgent.logEvent("Expense");

		androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder);
		TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);
		hader_text.setText("Expanse");
		setSupportActionBar(toolbar);
		if (getSupportActionBar() !=null){
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
		}

		context=this;
		progress1 = new ProgressDialog(this);
		customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

		mainlayout=(LinearLayout)this.findViewById(R.id.layout1);
		datype=(Spinner)findViewById(R.id.da_type);
		distance=(Spinner)findViewById(R.id.da_distance);
		daAmt=(EditText)findViewById(R.id.ex_da);
		distAmt=(EditText)findViewById(R.id.ex_dis);
		save_exp=(Button)findViewById(R.id.save_back1);
		mylist=(ListView)findViewById(R.id.list_exp);

		add_exp = (Button) findViewById(R.id.add_exp);
		DA_layout = (TableLayout) findViewById(R.id.DA_layout);

		cbohelp=new CBO_DB_Helper(context);

		dcr_id=Custom_Variables_And_Method.DCR_ID;
		PA_ID=Custom_Variables_And_Method.PA_ID;
		paid=""+PA_ID;


        myServiceHandler = new ServiceHandler(context);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		
		
		
		ArrayList<SpinnerModel>data1=new ArrayList<SpinnerModel>();
		
		data1.add(new SpinnerModel("--Select--"));
		data1.add(new SpinnerModel("Local"));
		data1.add(new SpinnerModel("Ex-Station Double Side"));
		data1.add(new SpinnerModel("Ex-Station Single Side"));
		data1.add(new SpinnerModel("Out-Station Double Side"));
		data1.add(new SpinnerModel("Out-Station Single Side"));
		adapter=new SpinAdapter2(getApplicationContext(),R.layout.spin_row2,data1);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		datype.setAdapter(adapter);

		//Start of call to service

		HashMap<String,String> request=new HashMap<>();
		request.put("sCompanyFolder",cbohelp.getCompanyCode());
		request.put("iPA_ID",paid);

		ArrayList<Integer> tables=new ArrayList<>();
		tables.add(0);
		tables.add(1);
		tables.add(2);

		progress1.setMessage("Please Wait..");
		progress1.setCancelable(false);
		progress1.show();

		new CboServices(this,mHandler).customMethodForAllServices(request,"ExpenseEntryDDL_Mobile",MESSAGE_INTERNET,tables);

		//End of call to service
		
		//================================================================================================================
		datype.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				 datype_val=((TextView)arg1.findViewById(R.id.spin_name2)).getText().toString();

				distAmt.setText("");
				dist_id3="";
				distance.setSelection(0);

				if((datype_val.equals("Local"))||(datype_val.equals("--Select--"))){
			        mainlayout.setVisibility(View.GONE);
			        
			    } else {
			        mainlayout.setVisibility(View.VISIBLE);
			    }

				if(datype_val.equals("--Select--"))
				{
					daAmt.setText("");
				}
				else if(datype_val.equals("Local")){
					
						daAmt.setText(datype_local);
					DA_typ="L";
					
				}
				else if(datype_val.equals("Ex-Station Double Side")||datype_val.equals("Ex-Station Single Side")){
					
							daAmt.setText(datype_ex);
					DA_typ="EX";
						
				}
				else{
					
							daAmt.setText(datype_ns);
					DA_typ="NS";
						
				}

				SetDA_Details();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		station=new ArrayList<String>();

		
		distance.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				//Toast.makeText(arg0.getContext(), arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_LONG).show();
				dist_id3=((TextView)arg1.findViewById(R.id.spin_id2)).getText().toString();
				
				if(datype_val.equals("Ex-Station Double Side")||datype_val.equals("Out-Station Double Side")){
					
					//String ttl_distance=arg0.getItemAtPosition(arg2).toString();
					 ttl_distance=((TextView)arg1.findViewById(R.id.spin_name2)).getText().toString();
					if(!ttl_distance.contains("----->")){
						distAmt.setText("");
					} else {
						String Distance1[]=ttl_distance.split("----->");

                        try{String ActDistance=Distance1[2];
                            String Act_dist1[]=ActDistance.split("K");
                            String MyDistance=Act_dist1[0].trim();
                            float fare_rate=Float.parseFloat(DistRate);
                            float in=Float.parseFloat(MyDistance);
                            float res =in*2*fare_rate;
                            String MyData=""+res;

                            distAmt.setText(MyData);}
                        catch (Exception e){
							customVariablesAndMethod.msgBox(context,""+e);
                        }

					}
				}
				else if(datype_val.equals("Ex-Station Single Side")||datype_val.equals("Out-Station Single Side")){
					
					//ttl_distance=arg0.getItemAtPosition(arg2).toString();
					ttl_distance=((TextView)arg1.findViewById(R.id.spin_name2)).getText().toString();
					if(!ttl_distance.contains("----->")){
						distAmt.setText("");
					} else {
					String Distance1[]=ttl_distance.split("----->");
					
					String ActDistance=Distance1[2];
					String Act_dist1[]=ActDistance.split("K");
					String MyDistance=Act_dist1[0].trim();
					float fare_rate=Float.parseFloat(DistRate);
					int in=Integer.parseInt(MyDistance);
					float res=in*1*fare_rate;
					String MyData=""+res;

					distAmt.setText(MyData);
					}
				}

				SetDA_Details();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	//======================================================================================================================



        exp_head=new ArrayList<String>();


		add_exp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//capture_Image();
				Add_expense("0","","","","","");
			}
		});

		data=cbohelp.get_Expense();
		sm = new Expenses_Adapter(context, data);
		mylist.setAdapter(sm);


		init_DA_type(DA_layout);
		
		save_exp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(datype_val.equals("--Select--")) {

					customVariablesAndMethod.msgBox(context,"Please Select your DA TYPE");
				}else if(mainlayout.getVisibility()==View.VISIBLE && dist_id3.equals("")) {

					customVariablesAndMethod.msgBox(context,"Please Select your Distance TYPE");
				} else {

					SetDA_Details();

					//Start of call to service

					HashMap<String,String> request=new HashMap<>();
					request.put("sCompanyFolder",cbohelp.getCompanyCode());
					request.put("iDcrId", dcr_id);
					request.put("sDaType", datype_val);
					request.put("iDistanceId", dist_id3);

					ArrayList<Integer> tables=new ArrayList<>();
					tables.add(0);

					progress1.setMessage("Please Wait..");
					progress1.setCancelable(false);
					progress1.show();

					new CboServices(Expense.this,mHandler).customMethodForAllServices(request,"DCR_COMMITEXP_1",MESSAGE_INTERNET_DCR_COMMITEXP,tables);

					//End of call to service
				}
			}
		});
		
		
	}

	private void init_DA_type(TableLayout stk) {

		stk.removeAllViews();
		TableRow tbrow0 = new TableRow(context);
		//tbrow0.setBackgroundColor(0xff125688);
		TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
		TextView tv0 = new TextView(context);
		tv0.setText("DA. Type");
		tv0.setPadding(5, 5, 5, 0);
		tv0.setTextColor(Color.BLACK);
		tv0.setTypeface(null, Typeface.NORMAL);
		tv0.setLayoutParams(params);
		tbrow0.addView(tv0);
		TextView tv1 = new TextView(context);
		tv1.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "DA_TYPE"));
		tv1.setGravity(Gravity.RIGHT);
		tv1.setPadding(5, 5, 5, 0);
		tv1.setTextColor(Color.BLACK);
		tv1.setTypeface(null, Typeface.NORMAL);
		tbrow0.addView(tv1);
		stk.addView(tbrow0);

		TableRow tbrow1 = new TableRow(context);
		//tbrow1.setBackgroundColor(0xff125688);
		TextView tv10 = new TextView(context);
		tv10.setText("DA. Value");
		tv10.setPadding(5, 5, 5, 0);
		tv10.setTextColor(Color.BLACK);
		tv10.setTypeface(null, Typeface.NORMAL);
		tv10.setLayoutParams(params);
		tbrow1.addView(tv10);
		TextView tv11 = new TextView(context);
		tv11.setText(context.getResources().getString(R.string.rs) + " " + customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "da_val","0"));
		tv11.setPadding(5, 5, 5, 0);
		tv11.setTextColor(Color.BLACK);
		tv11.setGravity(Gravity.RIGHT);
		tv11.setTypeface(null, Typeface.NORMAL);
		tbrow1.addView(tv11);
		stk.addView(tbrow1);

		TableRow tbrow2 = new TableRow(context);
		//tbrow2.setBackgroundColor(0xff125688);
		TextView tv21 = new TextView(context);
		tv21.setText("TA. Value");
		tv21.setPadding(5, 5, 5, 0);
		tv21.setTextColor(Color.BLACK);
		tv21.setTypeface(null, Typeface.NORMAL);
		tv21.setLayoutParams(params);
		tbrow2.addView(tv21);
		TextView tv22 = new TextView(context);
		tv22.setText(context.getResources().getString(R.string.rs) + " " + customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "distance_val","0"));
		tv22.setGravity(Gravity.RIGHT);
		tv22.setPadding(5, 5, 5, 0);
		tv22.setTextColor(Color.BLACK);
		tv22.setTypeface(null, Typeface.NORMAL);
		tbrow2.addView(tv22);
		stk.addView(tbrow2);

		TableRow tbrow4 = new TableRow(context);
		//tbrow4.setBackgroundColor(0xff125688);
		TextView tv40 = new TextView(context);
		tv40.setText("Other Value");
		tv40.setPadding(5, 5, 5, 0);
		tv40.setTextColor(Color.BLACK);
		tv40.setTypeface(null, Typeface.NORMAL);
		tv40.setLayoutParams(params);
		tbrow4.addView(tv40);
		TextView tv41 = new TextView(context);


		Float other = 0f;
		//datanum.put("amount", c.getString(c.getColumnIndex("amount")));
		for (int i = 0; i <data.size();i++){
			other+=Float.parseFloat(data.get(i).get("amount"));
		}

		tv41.setText(context.getResources().getString(R.string.rs)+" "+other);
		tv41.setPadding(5, 5, 5, 0);
		tv41.setTextColor(Color.BLACK);
		tv41.setGravity(Gravity.RIGHT);
		tv41.setTypeface(null, Typeface.NORMAL);
		tbrow4.addView(tv41);
		stk.addView(tbrow4);

		Float net_value = Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "da_val","0"))
				+ Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "distance_val","0"))
				+ other;


			TableRow tbrow3 = new TableRow(context);
			//tbrow3.setBackgroundColor(0xff125688);
			TextView tv31 = new TextView(context);
			tv31.setText("Total Expenses");
			tv31.setPadding(5, 5, 5, 0);
			tv31.setTextColor(Color.BLACK);
			tv31.setTypeface(null, Typeface.BOLD);
			tv31.setLayoutParams(params);
			tbrow3.addView(tv31);
			TextView tv32 = new TextView(context);

			tv32.setText(context.getResources().getString(R.string.rs) + " " + net_value);

			tv32.setGravity(Gravity.RIGHT);
			tv32.setPadding(5, 5, 5, 0);
			tv32.setTextColor(Color.BLACK);
			tv32.setTypeface(null, Typeface.BOLD);
			tbrow3.addView(tv32);
			stk.addView(tbrow3);

		TableRow tbrow5 = new TableRow(context);
		tbrow5.setPadding(2,2,2,2);
		tbrow5.setBackgroundColor(0xff125688);
		stk.addView(tbrow5);

	}


	private  void SetDA_Details(){
		String DA_amt=daAmt.getText().toString();
		if (DA_amt.equals("")){
			DA_amt="0";
		}
		String Dis_amt=distAmt.getText().toString();
		if (Dis_amt.equals("")){
			Dis_amt="0";
		}
		customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DA_TYPE",DA_typ);
		customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"da_val",DA_amt);
		customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"distance_val",Dis_amt);

		init_DA_type(DA_layout);
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


	private void open_galary(){
		Intent gallery_Intent = new Intent(context, GalleryUtil.class);
		startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
	}


	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (intent.resolveActivity(getPackageManager()) != null) {

			File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
					//return true;
				}
			}
			filename = PA_ID+"_"+dcr_id+"_"+exp_id+"_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
			output = new File(dir, filename);


//            fileTemp = ImageUtils.getOutputMediaFile();
			ContentValues values = new ContentValues(1);
			//values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
			values.put( MediaStore.Images.ImageColumns.DATA, output.getPath() );
			Uri fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            if (fileTemp != null) {
			// fileUri = Uri.fromFile(output);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			startActivityForResult(intent, REQUEST_CAMERA);
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
        startActivityForResult(intent, REQUEST_CAMERA);*/
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GALLERY_ACTIVITY_CODE) {
			if(resultCode == Activity.RESULT_OK){
				picturePath = data.getStringExtra("picturePath");
				//perform Crop on the Image Selected from Gallery
				filename = PA_ID+"_"+dcr_id+"_"+exp_id+".jpg";
				Toast.makeText(this, picturePath, Toast.LENGTH_LONG).show();
                /*performCrop(picturePath);*/
				moveFile(picturePath);
			}
		}

		if (requestCode == RESULT_CROP ) {
			if(resultCode == Activity.RESULT_OK){
				Bundle extras = data.getExtras();
				Bitmap selectedBitmap = extras.getParcelable("data");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				assert selectedBitmap != null;
				selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
				File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
				if (!dir.exists()) {
					if (!dir.mkdirs()) {
						Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
						//return true;
					} else {
						try {
							File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
							file2.createNewFile();
							FileOutputStream fo = new FileOutputStream(file2);
							fo.write(bytes.toByteArray());
							fo.close();
							Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
							attach_img.setImageBitmap(myBitmap);

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else {
					try {
						File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);

						file2.createNewFile();
						FileOutputStream fo = new FileOutputStream(file2);
						fo.write(bytes.toByteArray());
						fo.close();
						Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
						attach_img.setImageBitmap(myBitmap);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		if (requestCode == REQUEST_CAMERA ) {
			if (resultCode == RESULT_OK) {
				// successfully captured the image
				// display it in image view

				/*Bundle extras = data.getExtras();
				Bitmap imageBitmap = (Bitmap) extras.get("data");*/

				File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO"+File.separator+ filename);
				/*FileOutputStream out = null;
				try {
					out = new FileOutputStream(file1);
					Log.i("in save()", "after outputstream");
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

				} catch (IOException e) {
					e.printStackTrace();
				}finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/

				if (file1.exists()){
                    /*performCrop(file1.getPath());*/
					previewCapturedImage(file1.getPath());
				}

			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"image capture cancelled ", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == this.REQUEST_CAMERA) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
				// && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				capture_Image();
				//Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
			}
		}
		if (requestCode == this.GALLERY_ACTIVITY_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
				// && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				open_galary();
				//Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
			}
		}
	}
	private void moveFile( String inputFileUri) {

		InputStream in = null;
		OutputStream out = null;
		try {
			File dir = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO");
			//create output directory if it doesn't exist
			if (!dir.exists())
			{
				dir.mkdirs();
			}


			in = new FileInputStream(inputFileUri);
			out = new FileOutputStream(dir+File.separator + filename);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;

			// write the output file
			out.flush();
			out.close();
			out = null;

			previewCapturedImage(dir+File.separator + filename);

		}

		catch (FileNotFoundException fnfe1) {
			Log.e("tag", fnfe1.getMessage());
		}
		catch (Exception e) {
			Log.e("tag", e.getMessage());
		}

	}

	private void previewCapturedImage(String picUri) {
		try {
			// hide video preview

			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// downsizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(picUri,
					options);
			attach_img.setVisibility(View.VISIBLE);
			attach_img.setImageBitmap(bitmap);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}


	public void Add_expense(String who, String hed, String amt, String rem, final String path, String hed_id){
		filename="";
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setPositiveButton("ADD", null)
				.setCancelable(false)
				.setNegativeButton("Cancel", null);
		final AlertDialog dialog = builder.create();
		LayoutInflater inflater = getLayoutInflater();
		View dialogLayout = inflater.inflate(R.layout.add_exp, null);
		exphead = (Spinner) dialogLayout.findViewById(R.id.exp_head_root);
		exhAmt = (EditText)  dialogLayout.findViewById(R.id.ex_head_root);
		textRemark = (TextView) dialogLayout.findViewById(R.id.text_remark);
		final TextView ex_head_root_txt = (TextView) dialogLayout.findViewById(R.id.ex_head_root_txt);
		rem_final = (EditText) dialogLayout.findViewById(R.id.exp_remark_root);
		final CheckBox add_attachment = (CheckBox) dialogLayout.findViewById(R.id.add_attachment);
		final RadioGroup attach_option = (RadioGroup) dialogLayout.findViewById(R.id.attach_option);
		attach_img= (ImageView) dialogLayout.findViewById(R.id.attach_img);
		attach_img.setVisibility(View.GONE);
		TextView head = (TextView)  dialogLayout.findViewById(R.id.head);

		String ext=path;
		attach_option.setVisibility(View.GONE);

		add_attachment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!exp_id.equals("")) {
					if (add_attachment.isChecked()) {
						attach_option.setVisibility(View.VISIBLE);
					} else {
						attach_option.setVisibility(View.GONE);
					}
				}else{
					customVariablesAndMethod.msgBox(context,"Please select the Expense head");
					add_attachment.setChecked(false);
				}
			}
		});
		attach_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				int id=attach_option.getCheckedRadioButtonId();
				if (id == R.id.attach) {
					if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
						//takePictureButton.setEnabled(false);
						ActivityCompat.requestPermissions(Expense.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, GALLERY_ACTIVITY_CODE);
						Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

					}else {
						open_galary();

					}

				}else if (id == R.id.cam){

					if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
						//takePictureButton.setEnabled(false);
						ActivityCompat.requestPermissions(Expense.this, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CAMERA);
						Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

					}else {

						capture_Image();
					}

				}

			}

		});

		exphead.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				// TODO Auto-generated method stub
				exp_id = ((TextView) arg1.findViewById(R.id.spin_id)).getText().toString();
				exp_hed= ((TextView) arg1.findViewById(R.id.spin_name)).getText().toString();
				filename="";
				attach_img.setImageDrawable(null);
				add_attachment.setChecked(false);

				if (exp_id.equals("3119")) {
					exhAmt.setHint("K.M.");
					textRemark.setText("K.M. Remark.");
					ex_head_root_txt.setText("K.M.");
				} else {
					exhAmt.setHint("Amt.");
					textRemark.setText("Exp Remark.");
					ex_head_root_txt.setText("Amount");

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		dialog.setView(dialogLayout);
		dialog.setTitle("Add Other Expences");
		dialog.show();

		if(who.equals("0")) {
			exphead.setAdapter(adapter2);
			head.setVisibility(View.GONE);
		}else{

			head.setVisibility(View.VISIBLE);
			exphead.setVisibility(View.GONE);
			head.setText(hed);
			exp_id=hed_id;
			exp_hed=hed;
			exhAmt.setText(amt);
			rem_final.setText(rem);
			ext = path.substring(path.lastIndexOf("/")+1 );
			File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + ext);
			if(file2.exists() && !ext.equals("")){
				previewCapturedImage(file2.getPath());
			}
		}


		final String finalExt = ext;
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				my_Amt = exhAmt.getText().toString();
				my_rem = rem_final.getText().toString();


				//mycon.msgBox(dist_id3);
				if (exp_id.equals("")) {
					customVariablesAndMethod.msgBox(context,"First Select the Expense Head...");
				} else if (my_Amt.trim().isEmpty()) {
					customVariablesAndMethod.msgBox(context,"Please Enter the Expense Amt....");
				} else if (Integer.valueOf(my_Amt)==0) {
					customVariablesAndMethod.msgBox(context,"Expense Amt. can't be zero...");
				} else if (my_rem.trim().isEmpty()) {
					customVariablesAndMethod.msgBox(context,"Please Enter the Remark....");
				} else {


					if (!filename.equals("")){
						progress1.setMessage("Please Wait..\nuploading Image");
						progress1.setCancelable(false);
						progress1.show();
						File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
						new up_down_ftp().uploadFile(file2,context);

					}
					if(!path.equals("")){
						filename= finalExt;
					}
					dialog.dismiss();

					if (filename.equals("")) {
						expense_commit();
					}


				}
			}
		});
	}

	private void expense_commit(){
		//Start of call to service

		HashMap<String,String> request=new HashMap<>();
		request.put("sCompanyFolder",cbohelp.getCompanyCode());
		request.put("iDcrId", dcr_id);
		request.put("iExpHeadId", exp_id);
		request.put("iAmount", my_Amt);
		request.put("sRemark", my_rem);
		request.put("sFileName", filename);

		ArrayList<Integer> tables=new ArrayList<>();
		tables.add(0);

		progress1.setMessage("Please Wait..");
		progress1.setCancelable(false);
		progress1.show();

		new CboServices(Expense.this,mHandler).customMethodForAllServices(request,"DCREXPCOMMITMOBILE_2",MESSAGE_INTERNET_SAVE_EXPENSE,tables);

		//End of call to service
		exhAmt.setText("");
		rem_final.setText("");
		//dialog.dismiss();
	}


	@Override
	public void Edit_Expense(String who, String hed, String amt, String rem, String path, String hed_id) {
		Add_expense(who,hed,amt,rem,path,hed_id);
	}

	@Override
	public void delete_Expense(final String hed_id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Start of call to service
				exp_id=hed_id;
				HashMap<String,String> request=new HashMap<>();
				request.put("sCompanyFolder",cbohelp.getCompanyCode());
				request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
				request.put("iDCR_ID", dcr_id);
				request.put("iID", hed_id);

				ArrayList<Integer> tables=new ArrayList<>();
				tables.add(0);

				progress1.setMessage("Please Wait..");
				progress1.setCancelable(false);
				progress1.show();

				new CboServices(Expense.this,mHandler).customMethodForAllServices(request,"DCREXPDELETEMOBILE_1",MESSAGE_INTERNET_DCR_DELETEEXP,tables);

				//End of call to service
			}
		})
				.setCancelable(false)
				.setNegativeButton("Cancel", null);
		final AlertDialog dialog = builder.create();
		dialog.setMessage("Are you sure, you want to delete");
		dialog.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item != null){

			finish();
		}

		return super.onOptionsItemSelected(item);
	}


	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_INTERNET:
					if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
					if ((null != msg.getData())) {

						parser1(msg.getData());

					}
					break;
				case MESSAGE_INTERNET_SAVE_EXPENSE:
					if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
					if ((null != msg.getData())) {

						parser2(msg.getData());

					}
					break;
				case MESSAGE_INTERNET_DCR_COMMITEXP:
					if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
					if ((null != msg.getData())) {

						parser3(msg.getData());

					}
					break;
				case MESSAGE_INTERNET_DCR_DELETEEXP:
					if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
					if ((null != msg.getData())) {

						parser4(msg.getData());

					}
					break;
				case 99:
					if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
					if ((null != msg.getData())) {
						customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
						//Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}

			}
		}
	};


	private void parser4(Bundle result) {
		if (result != null) {

			try {

				String table0 = result.getString("Tables0");
				JSONArray jsonArray1 = new JSONArray(table0);

				JSONObject object = jsonArray1.getJSONObject(0);
				String value2 = object.getString("DCR_ID");

				cbohelp.delete_Expense_withID(exp_id);
				data=cbohelp.get_Expense();
				sm = new Expenses_Adapter(context, data);
				mylist.setAdapter(sm);

				init_DA_type(DA_layout);

				customVariablesAndMethod.msgBox(context," Exp. Deleted Sucessfully");


			} catch (JSONException e) {
				Log.d("MYAPP", "objects are: " + e.toString());
				CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
				e.printStackTrace();
			}

		}
		//Log.d("MYAPP", "objects are1: " + result);
		progress1.dismiss();
	}


	private void parser3(Bundle result) {
		if (result != null) {

			try {

				String table0 = result.getString("Tables0");
				JSONArray jsonArray1 = new JSONArray(table0);

				JSONObject object = jsonArray1.getJSONObject(0);
				String value2 = object.getString("DCR_ID");


				chm_ok = getmydata().get(0);
				stk_ok = getmydata().get(1);
				exp_ok = getmydata().get(2);


				if (exp_ok.equals("")) {
					cbohelp.insertfinalTest(chm_ok, stk_ok, "2");
				} else {
					cbohelp.updatefinalTest(chm_ok, stk_ok, "2");
				}
				customVariablesAndMethod.msgBox(context,"Expense Saved Sucessfully...");
				finish();


			} catch (JSONException e) {
				Log.d("MYAPP", "objects are: " + e.toString());
				CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
				e.printStackTrace();
			}

		}
		//Log.d("MYAPP", "objects are1: " + result);
		progress1.dismiss();
	}

	private void parser2(Bundle result) {
		if (result != null) {

			try {

				String table0 = result.getString("Tables0");
				JSONArray jsonArray1 = new JSONArray(table0);

				JSONObject object = jsonArray1.getJSONObject(0);
				value = object.getString("DCRID");
				id= object.getString("ID");

				cbohelp.insert_Expense(exp_id,exp_hed,my_Amt,my_rem,filename,id,customVariablesAndMethod.currentTime(context));








				data=cbohelp.get_Expense();
				sm = new Expenses_Adapter(Expense.this, data);
				mylist.setAdapter(sm);

				init_DA_type(DA_layout);

				customVariablesAndMethod.msgBox(context, " Exp. Added Sucessfully");
				exp_id="";
				exp_hed="";
				my_Amt="";
				my_rem ="";
				filename="";
				id="";

			} catch (JSONException e) {
				Log.d("MYAPP", "objects are: " + e.toString());
				CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
				e.printStackTrace();
			}

		}
		//Log.d("MYAPP", "objects are1: " + result);
		progress1.dismiss();
	}

	public void parser1(Bundle result) {
		if (result!=null ) {

			try {

				ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
				newlist.add(new SpinnerModel("--Select--", ""));

				cbohelp.delete_EXP_Head();

				String table0 = result.getString("Tables2");
				JSONArray jsonArray1 = new JSONArray(table0);
				for (int i = 0; i < jsonArray1.length(); i++) {
					JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
					newlist.add(new SpinnerModel(jsonObject1.getString("FIELD_NAME"), jsonObject1.getString("ID")));

				}
				if (newlist.size() > 0) {
					adapter2 = new SpinAdapter(getApplicationContext(), R.layout.spin_row, newlist);
					adapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);


				} else {
					customVariablesAndMethod.msgBox(context,"No ExpHead found...");

				}

                String table1 = result.getString("Tables0");
                JSONArray jsonArray2 = new JSONArray(table1);
				for (int i = 0; i < jsonArray2.length(); i++) {
					JSONObject jsonObject1 = jsonArray2.getJSONObject(i);
					DistRate = jsonObject1.getString("FARE_RATE");
					datype_local = jsonObject1.getString("DA_L");
					datype_ex = jsonObject1.getString("DA_EX");
					datype_ns = jsonObject1.getString("DA_NS");
				}

				mylist2.clear();
				String table2 = result.getString("Tables1");
				JSONArray jsonArray3 = new JSONArray(table2);
				for (int i=0; i<jsonArray3.length();i++){
					JSONObject jsonObject2 =jsonArray3.getJSONObject(i);
					mylist2.add(new SpinnerModel(jsonObject2.getString("STATION_NAME"),jsonObject2.getString("DISTANCE_ID")));
				}
				adapter=new SpinAdapter2(context,R.layout.spin_row2,mylist2);
				adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
				distance.setAdapter(adapter);

				progress1.dismiss();

			} catch (JSONException e) {
				Log.d("MYAPP", "objects are: " + e.toString());
				CboServices.getAlert(this,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
				e.printStackTrace();
			}

		}
		//Log.d("MYAPP", "objects are1: " + result);
		progress1.dismiss();

	}

	/*@Override
	public void upload_complete(final String IsCompleted) {
		progress1.dismiss();
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
					expense_commit();
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

	@Override
	public void started(Integer responseCode, String message, String description) {

	}

	@Override
	public void progess(Integer responseCode, Long FileSize, Float value, String description) {

	}

	@Override
	public void complete(Integer responseCode, String message, String description) {
		progress1.dismiss();
		expense_commit();
	}

	@Override
	public void aborted(Integer responseCode, String message, String description) {
		progress1.dismiss();
		customVariablesAndMethod.getAlert(context,message,description);
	}

	@Override
	public void failed(Integer responseCode, String message, String description) {
		progress1.dismiss();
		customVariablesAndMethod.getAlert(context,message,description);
	}
}

