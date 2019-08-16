package com.cbo.cbomobilereporting.ui_new.dcr_activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.databaseHelper.Call.Db.DrRcCallDB;
import com.cbo.cbomobilereporting.databaseHelper.Call.mDrRCCall;
import com.cbo.cbomobilereporting.databaseHelper.Location.LocationDB;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Doctor_registration_GPS;
import com.flurry.android.FlurryAgent;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.Response;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;


import locationpkg.Const;
import services.MyAPIService;
import services.Sync_service;
import utils.adapterutils.ExpandableListAdapter;
import utils.adapterutils.SpinAdapter_new;
import utils.adapterutils.SpinnerModel;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.networkUtil.NetworkUtil;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.GPS_Timmer_Dialog;
import utils_new.Report_Registration;
import utils_new.SendAttachment;
import utils_new.Service_Call_From_Multiple_Classes;

public class ReminderCall extends AppCompatActivity implements ExpandableListAdapter.Summary_interface{

	EditText loc,remark;
	Button add,drname;
	//Spinner drname;
	Custom_Variables_And_Method customVariablesAndMethod;
	int PA_ID;
	String dr_id="",doc_name="",dr_name_reg="",dr_id_reg = "",dr_id_index = "",dr_name;
	String  rc_time;
	CBO_DB_Helper cbohelp;
	ResultSet rs;
	SpinAdapter_new adapter;
	LinearLayout layout;
	ArrayList<SpinnerModel>mylist=new ArrayList<SpinnerModel>();
	ArrayList<SpinnerModel>doclist;
    Boolean value;
	NetworkUtil networkUtil;
	String live_km;
	Context context;

	ExpandableListView summary_layout;
	Button tab_call,tab_summary;
	HashMap<String, HashMap<String, ArrayList<String>>> summary_list=new HashMap<>();
	HashMap<String, ArrayList<String>> rc_doctor_list_summary=new HashMap<>();
	HashMap<String, ArrayList<String>> rc_doctor_list=new HashMap<>();
	ExpandableListAdapter listAdapter;
	LinearLayout call_layout,detail_layout;
	TableLayout stk,doc_detail;
	ArrayList<SpinnerModel>getdc;
	private Location currentBestLocation;
	int showRegistrtion=1;
	private AlertDialog myalertDialog = null;
	ArrayList<SpinnerModel> array_sort;
	SpinnerModel[] TitleName;
	ImageView spinImg;
    int textlength = 0;
	Report_Registration alertdFragment;
	private  static final int GPS_TIMMER=4,MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL=1,MESSAGE_INTERNET_SEND_FCM=2;
	public ProgressDialog progress1;
	boolean IsRefreshedClicked = true;

	String latLong = "";
	String ref_latLong = "";
	Service_Call_From_Multiple_Classes service ;


	///firebase DB
	mDrRCCall mdrRCCall;
	DrRcCallDB drRcCallDB;
	LocationDB locationDB;

	public ArrayList<SpinnerModel>getDoctorFromLocal(int id)
	{
		getdc=new ArrayList<SpinnerModel>();
		 try
		  {
			 //getdc.add(new SpinnerModel("--Select--","0"));

				  cbohelp=new CBO_DB_Helper(getApplicationContext());
				  Cursor c=cbohelp.getRcDoctorListLocal();
				  if(c.moveToFirst())
				  {
					  do
					  {
						  getdc.add(new SpinnerModel(c.getString(c.getColumnIndex("dr_name")),
								  c.getString(c.getColumnIndex("dr_id")), c.getString(c.getColumnIndex("LAST_VISIT_DATE")),
								  c.getString(c.getColumnIndex("CLASS")), c.getString(c.getColumnIndex("POTENCY_AMT")),
								  c.getString(c.getColumnIndex("ITEM_NAME")), c.getString(c.getColumnIndex("ITEM_POB")),
								  c.getString(c.getColumnIndex("ITEM_SALE")), c.getString(c.getColumnIndex("DR_AREA")),
								  c.getString(c.getColumnIndex("PANE_TYPE")), c.getString(c.getColumnIndex("DR_LAT_LONG")),
								  c.getString(c.getColumnIndex("FREQ")), c.getString(c.getColumnIndex("NO_VISITED")),
								  c.getString(c.getColumnIndex("DR_LAT_LONG2")), c.getString(c.getColumnIndex("DR_LAT_LONG3")),
								  c.getString(c.getColumnIndex("COLORYN")), c.getString(c.getColumnIndex("CALLYN")),
								  c.getString(c.getColumnIndex("CRM_COUNT")), c.getString(c.getColumnIndex("DRCAPM_GROUP")),
								  c.getString(c.getColumnIndex("APP_PENDING_YN")),c.getString(c.getColumnIndex("DRLAST_PRODUCT"))));

					  }while(c.moveToNext());
					 
				  }

		  }
		  	catch(Exception e)
		  	{
			  e.printStackTrace();
		  	}
		 return getdc;
		 
	}
	

	
	public void onCreate(Bundle b){
		super.onCreate(b);
		//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.reminder_card);
        FlurryAgent.logEvent("ReminderCall");

		androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder);
		TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);


		hader_text.setText("DR. Reminder");
		setSupportActionBar(toolbar);
		if (getSupportActionBar() !=null){
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
		}

		loc=(EditText)findViewById(R.id.loc_remdr);
		add=(Button)findViewById(R.id.add_dr_rem);
		//drname=(Spinner)findViewById(R.id.rem_drname);
		drname=(Button)findViewById(R.id.rem_drname);
		context=this;
		progress1 = new ProgressDialog(this);
		service =  new Service_Call_From_Multiple_Classes();
		customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
		PA_ID=Custom_Variables_And_Method.PA_ID;
		cbohelp=new CBO_DB_Helper(getApplicationContext());
		layout=(LinearLayout)findViewById(R.id.layout_remcall);
		remark=(EditText) findViewById(R.id.remak);
		networkUtil = new NetworkUtil(this);
		live_km = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"live_km");

		//get batterry level
		customVariablesAndMethod.getbattrypercentage(context);

		call_layout = (LinearLayout) findViewById(R.id.call_layout);
		detail_layout = (LinearLayout) findViewById(R.id.detail_layout);
		detail_layout.setBackgroundColor(Color.TRANSPARENT);
		summary_layout = (ExpandableListView) findViewById(R.id.summary_layout);
		tab_call= (Button) findViewById(R.id.call);
		tab_summary= (Button) findViewById(R.id.summary);

		stk= (TableLayout) findViewById(R.id.last_pob);
		doc_detail= (TableLayout) findViewById(R.id.doc_detail);


		locationDB = new LocationDB();
		drRcCallDB = new DrRcCallDB();


		//rc_time=getTime();

		drname.setText("---Select---");

		spinImg=(ImageView) findViewById(R.id.spinner_img_remider_call);
		spinImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//drname.performClick();
				onClickSpinnerLoad();
			}
		});

		showRegistrtion=Integer.parseInt(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"IsBackDate","1"));
		loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);

		//new Doback2().execute(PA_ID);


		if(Custom_Variables_And_Method.location_required.equals("Y")) {
			layout.setVisibility(View.VISIBLE);
		}

		if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DR_REMARKYN").equalsIgnoreCase("y")) {
			remark.setVisibility(View.VISIBLE);
		}else{
			remark.setVisibility(View.GONE);
		}

		drname.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickSpinnerLoad();
			}
		});

		/*drname.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int position, long arg3) {
				// TODO Auto-generated method stub
				dr_id=((TextView)v.findViewById(R.id.spin_id)).getText().toString();
				dr_name=((TextView)v.findViewById(R.id.spin_name)).getText().toString();

				if (dr_id!=null && !dr_id.equals("0")) {
					detail_layout.setBackgroundResource(R.drawable.custom_square_transparent_bg);
					String[] sample_name = getdc.get(position).getITEM_NAME().split(",");
					String[] sample_qty = getdc.get(position).getITEM_SALE().split(",");
					String[] sample_pob = getdc.get(position).getITEM_POB().split(",");
					rc_doctor_list=cbohelp.getCallDetail("phdcrdr_rc",dr_id,"0");
					remark.setText(rc_doctor_list.get("remark").get(0));
					last_pob_layout(sample_name, sample_qty, sample_pob);

					Doc_Detail(getdc.get(position).getCLASS(), getdc.get(position).getPOTENCY_AMT(), getdc.get(position).getLastVisited(), getdc.get(position).getAREA());
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

				
			}
		});*/
		
		
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (Custom_Variables_And_Method.GLOBAL_LATLON.equalsIgnoreCase("0.0,0.0")) {

					Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
				}

				ArrayList<String>docrc=new ArrayList<String>();
				if(loc.getText().toString().equals(""))
				{
					loc.setText("UnKnown Location");
				}

				
				String address=loc.getText().toString();


			if (doc_name.equalsIgnoreCase("--select--")){

				customVariablesAndMethod.msgBox(context,"Please Select Dr. Name First");


			}else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"REMARK_WW_MANDATORY").contains("R") &&  remark.getText().toString().equals("")) {
				customVariablesAndMethod.msgBox(context,"Please enter remak");


			}else{

					docrc=cbohelp.getDrRc();
					if(docrc.contains(dr_id)) {
						customVariablesAndMethod.msgBox(context,doc_name +" allready added ");
					} else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false)) {
						customVariablesAndMethod.msgBox(context,"Verifing Your Location");
						IsRefreshedClicked = false;
						LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
								new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
					}else {

						submitRC_Dr();

					}
					

			}}
		});





		rc_doctor_list_summary=cbohelp.getCallDetail("phdcrdr_rc","","1");
		//expense_list=new CBO_DB_Helper(context).getCallDetail("tempdr");

		summary_list=new LinkedHashMap<>();
		summary_list.put("Doctor Reminder",rc_doctor_list_summary);

		final ArrayList<String> header_title=new ArrayList<>();
		//final List<Integer> visible_status=new ArrayList<>();
		for(String main_menu:summary_list.keySet()){
			header_title.add(main_menu);
			//visible_status.add(0);
		}



		listAdapter = new ExpandableListAdapter(summary_layout,this, header_title, summary_list);

		// setting list adapter
		summary_layout.setAdapter(listAdapter);
		summary_layout.setGroupIndicator(null);
		for(int i=0; i < listAdapter.getGroupCount(); i++)
			summary_layout.expandGroup(i);
		//doctor.expandGroup(1);

		summary_layout.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {

				summary_list.get(header_title.get(groupPosition)).get("visible_status").get(childPosition);
				if (summary_list.get(header_title.get(groupPosition)).get("visible_status").get(childPosition).equals("1")){
					summary_list.get(header_title.get(groupPosition)).get("visible_status").set(childPosition,"0");
				}else {
					summary_list.get(header_title.get(groupPosition)).get("visible_status").set(childPosition,"1");
				}
				listAdapter.notifyDataSetChanged();
				return false;
			}
		});

		tab_call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				call_layout.setVisibility(View.VISIBLE);
				summary_layout.setVisibility(View.GONE);
				tab_call.setBackgroundResource(R.drawable.tab_selected);
				tab_summary.setBackgroundResource(R.drawable.tab_deselected);
			}
		});

		tab_summary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				summary_layout.setVisibility(View.VISIBLE);
				call_layout.setVisibility(View.GONE);
				tab_call.setBackgroundResource(R.drawable.tab_deselected);
				tab_summary.setBackgroundResource(R.drawable.tab_selected);
			}
		});

	}


	private void submitRC_Dr(){
		customVariablesAndMethod.SetLastCallLocation(context);

		setAddressToUI();
		submitDoctorRcInLocal();
		//submitDoctorRcInServer();

		new Service_Call_From_Multiple_Classes().SendFCMOnCall(context, mHandler, MESSAGE_INTERNET_SEND_FCM,"D",dr_id,Custom_Variables_And_Method.GLOBAL_LATLON);

	}

	private void onClickSpinnerLoad(){
		spinImg.setEnabled(false);
		drname.setEnabled(false);
		new GPS_Timmer_Dialog(context,mHandler,"Scanning Doctors...",GPS_TIMMER).show();
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

				case GPS_TIMMER:
					spinImg.setEnabled(true);
					drname.setEnabled(true);
					new Doback2().execute(PA_ID);
					break;
				case MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL:
					onDownloadAllResponse();
					break;
				case MESSAGE_INTERNET_SEND_FCM:
					if (networkUtil.internetConneted(ReminderCall.this)){
						if (live_km.equalsIgnoreCase("Y")||(live_km.equalsIgnoreCase("Y5"))){
							MyCustomMethod myCustomMethod= new MyCustomMethod(context);
							myCustomMethod.stopAlarm10Minute();
							myCustomMethod.startAlarmIn10Minute();
						}else {
							startService(new Intent(context, Sync_service.class));
						}
					}

					/*Intent intent = new Intent(getApplicationContext(), LoginFake.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					intent.putExtra("EXIT", true);
					startActivity(intent);
					finish();*/

					MyCustumApplication.getInstance().Logout((Activity) context);
					break;
				case 99:
					if ((null != msg.getData())) {
						customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
						//Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
					}
					break;


			}
		}
	};


	private void onDownloadAllResponse(){
		Custom_Variables_And_Method.GPS_STATE_CHANGED=true;
		Custom_Variables_And_Method.GPS_STATE_CHANGED_TIME=customVariablesAndMethod.get_currentTimeStamp();
		new GPS_Timmer_Dialog(context,mHandler,"Scanning Doctors...",GPS_TIMMER).show();

	}


	public void submitDoctorRcInLocal()
	{

		currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation",Location.class);

		String locExtra="";

		if (currentBestLocation!=null) {
			locExtra = "Lat_Long " + currentBestLocation.getLatitude() + "," + currentBestLocation.getLongitude() + ", Accuracy " + currentBestLocation.getAccuracy() + ", Time " + currentBestLocation.getTime() + ", Speed " + currentBestLocation.getSpeed() + ", Provider " + currentBestLocation.getProvider();
		}

		 if(dr_id.equals("0"))
			{
				customVariablesAndMethod.msgBox(context,"Please Select Doctor from List");
			}
			else
			{


				mdrRCCall.setRemark(remark.getText().toString())
						.setSrno(customVariablesAndMethod.srno(context))
						.setLOC_EXTRA(locExtra)
						.setTime(customVariablesAndMethod.currentTime(context));

				String dcrid=Custom_Variables_And_Method.DCR_ID;
				
				try{
				
					long val=cbohelp.insertDrRem(dcrid, dr_id, latLong+"!^"+loc.getText().toString(),
							customVariablesAndMethod.currentTime(context),Custom_Variables_And_Method.GLOBAL_LATLON,
							"0","0",mdrRCCall.getSrno(),Custom_Variables_And_Method.BATTERYLEVEL,
							remark.getText().toString(),"",locExtra,ref_latLong);
					Log.e("dr reminder added", ""+val);
					if(val>0)
					{
						Log.e("reminder saved in local", ""+val);
					}

					drRcCallDB.insert(mdrRCCall);
					locationDB.insert(mdrRCCall);

				}catch(Exception e){
					
				}
				
		}
	}


	//======================================================oncreate finish===============================================
	public void setAddressToUI() {
		String networkStatus= NetworkUtil.getConnectivityStatusString(getApplicationContext());
		if (networkStatus.equals("Not connected to Internet")) {
			loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
		} else {
			if (Custom_Variables_And_Method.global_address != null || Custom_Variables_And_Method.global_address != "") {
				//loc.setText(Custom_Variables_And_Method.global_address);
				loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
			} else {
				loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
			}

		}
	}

	private void last_pob_layout(String[] sample_name, String[] sample_pob, String[] sale_qty) {


		stk.removeAllViews();
		TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);

		for (int i = 0; i < sample_name.length; i++) {
			if (!sample_pob[i].equals("") ) {
				TableRow tbrow = new TableRow(context);
				TextView t1v = new TextView(context);
				t1v.setText("POB-" + sample_name[i]);
				t1v.setTextSize(11);
				t1v.setPadding(5, 5, 5, 0);
				t1v.setTextColor(Color.BLACK);
				t1v.setLayoutParams(params);
				tbrow.addView(t1v);

				TextView t3v = new TextView(context);
				t3v.setText(sample_pob[i]);
				t3v.setPadding(5, 5, 5, 0);
				t3v.setTextSize(11);
				t3v.setTextColor(Color.BLACK);
				t3v.setGravity(Gravity.CENTER);
				tbrow.addView(t3v);
				stk.addView(tbrow);
			}
		}

		for (int i = 0; i < sample_name.length; i++) {
			if (!sale_qty[i].equals("")) {
				TableRow tbrow = new TableRow(context);
				TextView t1v = new TextView(context);
				t1v.setText("Sale-" + sample_name[i]);
				t1v.setPadding(5, 5, 5, 0);
				t1v.setTextSize(11);
				t1v.setTextColor(Color.BLACK);
				t1v.setLayoutParams(params);
				tbrow.addView(t1v);

				TextView t3v = new TextView(context);
				t3v.setText(sale_qty[i]);
				t3v.setPadding(5, 5, 5, 0);
				t3v.setTextSize(11);
				t3v.setTextColor(Color.BLACK);
				t3v.setGravity(Gravity.CENTER);
				tbrow.addView(t3v);
				stk.addView(tbrow);
			}
		}
	}

	private void Doc_Detail(String doc_class, String doc_potential, String doc_last_visited,String area, String CRM_COUNT,
							String DRCAPM_GROUP,String lastProduct) {
		doc_detail.removeAllViews();

		//tbrow0.setBackgroundColor(0xff125688);
		TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);

		if (!area.equals("")) {
			TableRow tbrow00 = new TableRow(context);
			TextView tv00 = new TextView(context);
			tv00.setText("Area");
			tv00.setTextSize(11);
			tv00.setPadding(5, 5, 5, 0);
			tv00.setTextColor(Color.BLACK);
			tv00.setTypeface(null, Typeface.BOLD);
			tv00.setLayoutParams(params);
			tbrow00.addView(tv00);

			TextView tv01 = new TextView(context);
			tv01.setText(area);
			tv01.setTextSize(11);
			tv01.setPadding(5, 5, 5, 0);
			tv01.setTextColor(Color.BLACK);
			tv01.setGravity(Gravity.RIGHT);
			tv01.setTypeface(null, Typeface.NORMAL);
			tv01.setLayoutParams(params);
			tbrow00.addView(tv01);

			doc_detail.addView(tbrow00);
		}

		if (!doc_class.equals("") ) {
			TableRow tbrow0 = new TableRow(context);
			TextView tv0 = new TextView(context);
			tv0.setText("Class");
			tv0.setTextSize(11);
			tv0.setPadding(5, 5, 5, 0);
			tv0.setTextColor(Color.BLACK);
			tv0.setTypeface(null, Typeface.BOLD);
			tv0.setLayoutParams(params);
			tbrow0.addView(tv0);

			TextView tv1 = new TextView(context);
			tv1.setText(doc_class);
			tv1.setTextSize(11);
			tv1.setPadding(5, 5, 5, 0);
			tv1.setTextColor(Color.BLACK);
			tv1.setGravity(Gravity.RIGHT);
			tv1.setTypeface(null, Typeface.NORMAL);
			tv1.setLayoutParams(params);
			tbrow0.addView(tv1);

			doc_detail.addView(tbrow0);
		}

		if (!doc_potential.equals("")) {
			TableRow tbrow01 = new TableRow(context);
			TextView tv01 = new TextView(context);
			tv01.setText("Potential");
			tv01.setTextSize(11);
			tv01.setPadding(5, 5, 5, 0);
			tv01.setTextColor(Color.BLACK);
			tv01.setTypeface(null, Typeface.BOLD);
			tv01.setLayoutParams(params);
			tbrow01.addView(tv01);

			TextView tv11 = new TextView(context);
			tv11.setText(doc_potential);
			tv11.setTextSize(11);
			tv11.setPadding(5, 5, 5, 0);
			tv11.setTextColor(Color.BLACK);
			tv11.setGravity(Gravity.RIGHT);
			tv11.setTypeface(null, Typeface.NORMAL);
			tv11.setLayoutParams(params);
			tbrow01.addView(tv11);

			doc_detail.addView(tbrow01);
		}

		if (!doc_last_visited.equals("")) {

			TableRow tbrow02 = new TableRow(context);
			TextView tv02 = new TextView(context);
			tv02.setText("Last Visited");
			tv02.setTextSize(11);
			tv02.setPadding(5, 5, 5, 0);
			tv02.setTextColor(Color.BLACK);
			tv02.setTypeface(null, Typeface.BOLD);
			tv02.setLayoutParams(params);
			tbrow02.addView(tv02);

			TextView tv12 = new TextView(context);
			tv12.setText(doc_last_visited);
			tv12.setPadding(5, 5, 5, 0);
			tv12.setTextSize(11);
			tv12.setTextColor(Color.BLACK);
			tv12.setGravity(Gravity.RIGHT);
			tv12.setTypeface(null, Typeface.NORMAL);
			tv12.setLayoutParams(params);
			tbrow02.addView(tv12);

			doc_detail.addView(tbrow02);
		}

		if (!CRM_COUNT.equals("")) {

			TableRow tbrow02 = new TableRow(context);
			TextView tv02 = new TextView(context);
			tv02.setText("Dr CRM");
			tv02.setTextSize(11);
			tv02.setPadding(5, 5, 5, 0);
			tv02.setTextColor(Color.BLACK);
			tv02.setTypeface(null, Typeface.BOLD);
			tv02.setLayoutParams(params);
			tbrow02.addView(tv02);

			TextView tv12 = new TextView(context);
			tv12.setText(CRM_COUNT);
			tv12.setPadding(5, 5, 5, 0);
			tv12.setTextSize(11);
			tv12.setTextColor(Color.BLACK);
			tv12.setGravity(Gravity.RIGHT);
			tv12.setTypeface(null, Typeface.NORMAL);
			tv12.setLayoutParams(params);
			tbrow02.addView(tv12);

			doc_detail.addView(tbrow02);
		}

		if (!DRCAPM_GROUP.equals("")) {

			TableRow tbrow02 = new TableRow(context);
			TextView tv02 = new TextView(context);
			tv02.setText("Campaign Group");
			tv02.setTextSize(11);
			tv02.setPadding(5, 5, 5, 0);
			tv02.setTextColor(Color.BLACK);
			tv02.setTypeface(null, Typeface.BOLD);
			tv02.setLayoutParams(params);
			tbrow02.addView(tv02);

			TextView tv12 = new TextView(context);
			tv12.setText(DRCAPM_GROUP);
			tv12.setPadding(5, 5, 5, 0);
			tv12.setTextSize(11);
			tv12.setTextColor(Color.BLACK);
			tv12.setGravity(Gravity.RIGHT);
			tv12.setTypeface(null, Typeface.NORMAL);
			tv12.setLayoutParams(params);
			tbrow02.addView(tv12);

			doc_detail.addView(tbrow02);
		}


		if (!lastProduct.equals("")) {

			TableRow tbrow02 = new TableRow(context);
			TextView tv02 = new TextView(context);
			tv02.setText("Last Product");
			tv02.setTextSize(11);
			tv02.setPadding(5, 5, 5, 0);
			tv02.setTextColor(Color.BLACK);
			tv02.setTypeface(null, Typeface.BOLD);
			tv02.setLayoutParams(params);
			tbrow02.addView(tv02);

			TextView tv12 = new TextView(context);
			tv12.setText(lastProduct);
			tv12.setPadding(5, 5, 5, 0);
			tv12.setTextSize(11);
			tv12.setTextColor(Color.BLACK);
			tv12.setGravity(Gravity.RIGHT);
			tv12.setTypeface(null, Typeface.NORMAL);
			tv12.setLayoutParams(params);
			tbrow02.addView(tv12);

			doc_detail.addView(tbrow02);
		}



	}

	public void onBackPressed(){
		    finish();
		 super.onBackPressed();
	 }

	@Override
	public void Edit_Call(final String Dr_id, String Dr_name) {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
		final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);
		final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
		final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
		final Button Alert_Nagative= (Button) dialogLayout.findViewById(R.id.nagative);
		Alert_Nagative.setText("Cancel");
		Alert_Positive.setText("Edit");
		Alert_title.setText("Edit!!!");
		Alert_message.setText("Do you want to edit "+Dr_name+" ?");


		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


		final AlertDialog dialog = builder1.create();

		dialog.setView(dialogLayout);
		Alert_Positive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				doclist=getDoctorFromLocal(0);
				call_layout.setVisibility(View.VISIBLE);
				summary_layout.setVisibility(View.GONE);
				tab_call.setBackgroundResource(R.drawable.tab_selected);
				tab_summary.setBackgroundResource(R.drawable.tab_deselected);

				for (int i = 0; i < doclist.size(); i++) {
					if (doclist.get(i).getId().equals(Dr_id)) {
						//drname.setSelection(i);

						mdrRCCall = (mDrRCCall) new mDrRCCall()
								.setId(dr_id)
								.setName(doc_name)
								.setDcr_id(MyCustumApplication.getInstance().getUser().getDCRId())
								.setDcr_date(MyCustumApplication.getInstance().getUser().getDCRDate());
						PopulateDr_Rc(Dr_id,i);
					}
				}
				dialog.dismiss();
			}
		});
		Alert_Nagative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		dialog.setCancelable(false);
		dialog.show();



	}

	@Override
	public void delete_Call(final String Dr_id, final String Dr_name) {

		AppAlert.getInstance().setPositiveTxt("Delete").DecisionAlert(context, "Delete!!!", "Do you Really want to delete " + Dr_name + " ?", new AppAlert.OnClickListener() {
			@Override
			public void onPositiveClicked(View item, String result) {

				//Start of call to service

				HashMap<String,String> request=new HashMap<>();
				request.put("sCompanyFolder",  MyCustumApplication.getInstance ().getUser ().getCompanyCode ());
				request.put("iPaId",  MyCustumApplication.getInstance ().getUser ().getID ());
				request.put("iDCR_ID",  MyCustumApplication.getInstance ().getUser ().getDCRId ());
				request.put("iDR_ID", Dr_id);
				request.put("sTableName", "DOCTORRC");


				ArrayList<Integer> tables=new ArrayList<>();
				tables.add(0);

				new MyAPIService(context)
						.execute(new ResponseBuilder("DRCHEMDELETE_MOBILE",request)
								.setDescription("Please Wait..." +
										"\nDeleting "+Dr_name+" from DCR...")
								.setResponse(new CBOServices.APIResponse() {
									@Override
									public void onComplete(Bundle bundle) throws Exception {
										String table0 = bundle.getString("Tables0");
										JSONArray jsonArray1 = new JSONArray(table0);
										JSONObject object = jsonArray1.getJSONObject(0);

										if (object.getString("DCRID").equalsIgnoreCase("1")) {

											mdrRCCall = (mDrRCCall) new mDrRCCall().setId(Dr_id);
											drRcCallDB.delete(mdrRCCall);
											cbohelp.delete_DoctorRemainder_from_local_all(Dr_id);
											customVariablesAndMethod.msgBox(context,Dr_name+" sucessfully Deleted.");
											finish();
										}else{
											AppAlert.getInstance().getAlert(context,"Alert",object.getString("DCRID"));
										}
									}

									@Override
									public void onResponse(Bundle bundle) throws Exception {

									}

									@Override
									public void onError(String s, String s1) {
										AppAlert.getInstance().getAlert(context,s,s1);
									}
								}));

				//End of call to service




			}

			@Override
			public void onNegativeClicked(View item, String result) {
			}
		});


	}
	

	class Doback2 extends AsyncTask<Integer, Void, ArrayList<SpinnerModel>> {
		ProgressDialog pd;
		@Override
		protected ArrayList<SpinnerModel> doInBackground(Integer... params) {

			doclist=getDoctorFromLocal(params[0]);
			return doclist;
			
								
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ReminderCall.this);
			pd.setTitle("CBO");
			pd.setMessage("Processing......."+"\n"+"Please Wait");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}

		@Override
		protected void onPostExecute(ArrayList<SpinnerModel> s) {
			// TODO Auto-generated method stub
			super.onPostExecute(s);


			setAddressToUI();
			try {
				if ((!s.isEmpty()) || (s.size() < 0)) {
					TitleName = new SpinnerModel[s.size()];
					for (int i = 0; i <= s.size()-1; i++) {
						TitleName[i] = s.get(i);


					}

					array_sort = new ArrayList<SpinnerModel>(Arrays.asList(TitleName));

					Intent intent=getIntent();
					if (intent.getStringExtra("id")!=null) {

						for (int i = 0; i < doclist.size(); i++) {

							if (doclist.get(i).getId().equals(intent.getStringExtra("id").trim())) {
								dr_id = doclist.get(i).getId();
								doc_name = doclist.get(i).getName().split("-")[0];
								drname.setText(doc_name);
								if (intent.getStringExtra("remark") != null) {
									remark.setVisibility(View.VISIBLE);
									remark.setText(intent.getStringExtra("remark"));
								}
							}
						}
					}else{
						onClickDrName();
					}
				} else {
					customVariablesAndMethod.getAlert(context,"Data not found","No Doctor in Planned Dcr...");

					spinImg.setClickable(false);
					drname.setClickable(false);
					//  spinImg.setClickable(false);
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
			pd.dismiss();
		}
	}


	private void onClickDrName() {

		IsRefreshedClicked = true;
		AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
		final EditText editText = new EditText(context);
		final ListView listview = new ListView(context);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0,  R.mipmap.ref3, 0);
		array_sort = new ArrayList<SpinnerModel>(Arrays.asList(TitleName));
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(editText);
		layout.addView(listview);
		myDialog.setView(layout);
		SpinAdapter_new arrayAdapter = new SpinAdapter_new(context, R.layout.spin_row, array_sort,showRegistrtion);
		listview.setAdapter(arrayAdapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				myalertDialog.dismiss();

				mdrRCCall = null;
				SpinnerModel model = array_sort.get(position);

				dr_id = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
				doc_name = ((TextView) view.findViewById(R.id.spin_name)).getText().toString().split("-")[0];
				drname.setText(doc_name);
				latLong = "";
				ref_latLong = "";
				if (!model.getAPP_PENDING_YN().equalsIgnoreCase("0")){
					drname.setText("---Select---");
					dr_id="";
					doc_name="";
					if (!customVariablesAndMethod.IsGPS_GRPS_ON(context)) {
						//customVariablesAndMethod.Connect_to_Internet_Msg(context);
						AppAlert.getInstance().setNagativeTxt("Cancel").setPositiveTxt("Check").DecisionAlert(context,
								"Approval Pending !!!", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,
										"DCRDRADDAREA_APP_MSG","Your Additional Area Approval is Pending... \nYou Additional Area must be approved first !!!\n" +
												"Please contact your Head-Office for APPROVAL"),
								new AppAlert.OnClickListener() {
									@Override
									public void onPositiveClicked(View item, String result) {
										new Service_Call_From_Multiple_Classes().CheckIfCallsUnlocked(context,"ADDAREA");
									}

									@Override
									public void onNegativeClicked(View item, String result) {

									}
								});

					} else {
						new Service_Call_From_Multiple_Classes().CheckIfCallsUnlocked(context,"ADDAREA");
					}
				}else if (((TextView) view.findViewById(R.id.distance)).getText().toString().equals("Registration pending...")){

					if (!customVariablesAndMethod.IsGPS_GRPS_ON(context)) {
						customVariablesAndMethod.Connect_to_Internet_Msg(context);
						drname.setText("---Select---");
						dr_id="";
						doc_name="";
					} else {
						Intent intent = new Intent(context, Doctor_registration_GPS.class);
						intent.putExtra("id",dr_id);
						intent.putExtra("name",doc_name);
						intent.putExtra("type","D");
						startActivity(intent);
						finish();
					}
				}else if(((TextView) view.findViewById(R.id.distance)).getText().toString().contains("Km Away")) {
					//new Report_Registration().getAlertWithReport();
					//new Report_Registration().getAlertWithReport(context,"Not In Range","You are "+((TextView) view.findViewById(R.id.distance)).getText().toString()+" from "+doc_name,true);
					FragmentManager fm = getSupportFragmentManager();
					alertdFragment = new Report_Registration();
					String km=((TextView) view.findViewById(R.id.distance)).getText().toString();
					alertdFragment.setAlertLocation(array_sort.get(position).getLoc(),array_sort.get(position).getLoc2(),array_sort.get(position).getLoc3());
					alertdFragment.setAlertData("Not In Range","You are "+km+" from "+doc_name);
					alertdFragment.show(fm, "Alert Dialog Fragment");
					km=km.replace("Km Away","").trim();

					dr_id_reg = dr_id;
					dr_id_index = "";
					dr_name_reg=doc_name;
					if(array_sort.get(position).getLoc2().equals("")  && Float.parseFloat(km)< Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"RE_REG_KM","5"))){
						dr_id_index = "2";
					}else if(array_sort.get(position).getLoc3().equals("")  && Float.parseFloat(km)< Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"RE_REG_KM","5"))){
						dr_id_index = "3";
					}

					drname.setText("---Select---");
					dr_id="";
					doc_name="";


					mdrRCCall = (mDrRCCall) new mDrRCCall()
							.setId(model.getId())
							.setName(model.getName())
							.setArea(model.getAREA())
							.setDcr_id(MyCustumApplication.getInstance().getUser().getDCRId())
							.setDcr_date(MyCustumApplication.getInstance().getUser().getDCRDate())
							.setRef_latlong(model.getREF_LAT_LONG())
							.setLatLong(arrayAdapter.latLong)
							.setBattery(MyCustumApplication.getInstance().getUser().getBattery());

					mdrRCCall.setDrColour(model.getColour())
							.setDrClass(model.getCLASS())
							.setDr_CRM(model.getCRM_COUNT())
							.setDrLastVisited(model.getLastVisited())
							.setDrPotential(model.getPOTENCY_AMT())
							.setDRCAPM_GROUP(model.getDRCAPM_GROUP());

				}else if( Integer.parseInt(array_sort.get(position).getFREQ()) != 0 && Integer.parseInt(array_sort.get(position).getFREQ()) < Integer.parseInt(array_sort.get(position).getNO_VISITED()) ) {
					customVariablesAndMethod.getAlert(context,"Visit Freq. Exceeded",("For "+doc_name +"@ Allowed Freq. : " + array_sort.get(position).getFREQ() + "@ Visited       : "+array_sort.get(position).getNO_VISITED()).split("@"));
					drname.setText("---Select---");
					dr_id="";
					doc_name="";
				}else {

					mdrRCCall = (mDrRCCall) new mDrRCCall()
							.setId(model.getId())
							.setName(model.getName())
							.setArea(model.getAREA())
							.setDcr_id(MyCustumApplication.getInstance().getUser().getDCRId())
							.setDcr_date(MyCustumApplication.getInstance().getUser().getDCRDate())
							.setRef_latlong(model.getREF_LAT_LONG())
							.setLatLong(arrayAdapter.latLong)
							.setBattery(MyCustumApplication.getInstance().getUser().getBattery());

					mdrRCCall.setDrColour(model.getColour())
							.setDrClass(model.getCLASS())
							.setDr_CRM(model.getCRM_COUNT())
							.setDrLastVisited(model.getLastVisited())
							.setDrPotential(model.getPOTENCY_AMT())
							.setDRCAPM_GROUP(model.getDRCAPM_GROUP());

					latLong = arrayAdapter.latLong;
					ref_latLong = array_sort.get(position).getREF_LAT_LONG();
                    PopulateDr_Rc(dr_id,position);
				}

			}
		});
		editText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s,
										  int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				textlength = editText.getText().length();
				array_sort.clear();
				for (int i = 0; i < TitleName.length; i++) {
					if (textlength <= TitleName[i].getName().length()) {

						if (TitleName[i].getName().toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
							array_sort.add(TitleName[i]);
						}
					}
				}
				try {
					listview.setAdapter(new SpinAdapter_new(context, R.layout.spin_row, array_sort,showRegistrtion));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		editText.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int DRAWABLE_LEFT = 0;
				final int DRAWABLE_TOP = 1;
				final int DRAWABLE_RIGHT = 2;
				final int DRAWABLE_BOTTOM = 3;

				if(event.getAction() == MotionEvent.ACTION_UP) {
					if(event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
						// your action here
						myalertDialog.dismiss();
						if(!customVariablesAndMethod.checkIfCallLocationValid(context,true,false)) {
							customVariablesAndMethod.msgBox(context,"Verifing Your Location");
							IsRefreshedClicked = true;
							LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
									new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
						}else{
							//new Service_Call_From_Multiple_Classes().DownloadAll(context, mHandler, MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL);
							service.DownloadAll(context, new Response() {
								@Override
								public void onSuccess(Bundle bundle) {
									onDownloadAllResponse();
								}

								@Override
								public void onError(String s, String s1) {
									AppAlert.getInstance().getAlert(context,s,s1);
								}
							});
						}

						Vibrator vbr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						vbr.vibrate(100);
						return true;
					}
				}
				return false;
			}
		});
		myalertDialog = myDialog.show();
	}

	private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
		@Override
		public void onReceive(Context contex, Intent intent) {
			Location location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE);
			if ( IsRefreshedClicked ) {
				//new Service_Call_From_Multiple_Classes().DownloadAll(context, mHandler, MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL);
				service.DownloadAll(context, new Response() {
					@Override
					public void onSuccess(Bundle bundle) {
						onDownloadAllResponse();
					}

					@Override
					public void onError(String s, String s1) {
						AppAlert.getInstance().getAlert(context,s,s1);
					}
				});
			}else{
				submitRC_Dr();
			}
			LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);

		}
	};

	public void PopulateDr_Rc(String dr_id,int position){
        if (dr_id!=null && !dr_id.equals("0")) {
            detail_layout.setBackgroundResource(R.drawable.custom_square_transparent_bg);
            String[] sample_name = getdc.get(position).getITEM_NAME().split(",");
            String[] sample_qty = getdc.get(position).getITEM_SALE().split(",");
            String[] sample_pob = getdc.get(position).getITEM_POB().split(",");
            rc_doctor_list=cbohelp.getCallDetail("phdcrdr_rc",dr_id,"0");
            remark.setText(rc_doctor_list.get("remark").get(0));
            last_pob_layout(sample_name, sample_qty, sample_pob);

            Doc_Detail(getdc.get(position).getCLASS(), getdc.get(position).getPOTENCY_AMT(), getdc.get(position).getLastVisited(),
					getdc.get(position).getAREA(), array_sort.get(position).getCRM_COUNT(),
					array_sort.get(position).getDRCAPM_GROUP(),array_sort.get(position).getDRLAST_PRODUCT());
        }
    }



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

			case Report_Registration.REQUEST_CAMERA :
				if (resultCode == RESULT_OK) {


					File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO"+File.separator+ alertdFragment.filename);

					if(!dr_id_index.equals("") && ( cbohelp.getDrRc().size()==0 || !cbohelp.getDrRc().contains(dr_id_reg)) && cbohelp.getCallDetail("tempdr", dr_id_reg, "0").get("time").get(0).equals("")){
						cbohelp.updateLatLong(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON),dr_id_reg,"D",dr_id_index);


						dr_id = dr_id_reg;
						doc_name=dr_name_reg;
						drname.setText(doc_name);

						if (Custom_Variables_And_Method.internetConneted(context)) {
							LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, new IntentFilter("SyncComplete"));
							Sync_service.ReplyYN="Y";
							progress1.setMessage("Please Wait..\n" +
									dr_name_reg +" is being Registered");
							progress1.setCancelable(false);
							progress1.show();
							startService(new Intent(context, Sync_service.class));
						}else{
							customVariablesAndMethod.getAlert(context,"Registered",dr_name_reg+" Successfully Re-Registered("+dr_id_index+")");
						}
					}else if (file1.exists() && Custom_Variables_And_Method.internetConneted(context)){
						Location currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation",Location.class);
						new SendAttachment((Activity) context).execute(Custom_Variables_And_Method.COMPANY_CODE+": Out of Range Error report",context.getResources().getString(R.string.app_name)+"\n Company Code :"+Custom_Variables_And_Method.COMPANY_CODE+"\n DCR ID :"+Custom_Variables_And_Method.DCR_ID+"\n PA ID : "+Custom_Variables_And_Method.PA_ID+"\n App version : "+Custom_Variables_And_Method.VERSION+"\n Message : "+alertdFragment.Alertmassege+
								"\nLocation-timestamp : "+currentBestLocation.getTime()+"\nLocation-Lat : "+currentBestLocation.getLatitude()+
								"\nLocation-long : "+currentBestLocation.getLongitude()+"\n time : " +customVariablesAndMethod.currentTime(context)+"\nlatlong : "+ customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON),alertdFragment.compressImage(file1));

					}else{
						customVariablesAndMethod.Connect_to_Internet_Msg(context);
					}

				} else if (resultCode == RESULT_CANCELED) {
					// user cancelled Image capture
					Toast.makeText(context,
							"image capture cancelled ", Toast.LENGTH_SHORT)
							.show();
				} else {
					// failed to capture image
					Toast.makeText(context,
							"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
							.show();
				}
				break;
		}


	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item != null){

			finish();
     		}
    return super.onOptionsItemSelected(item);
	}


	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context contex, Intent intent) {
			if (progress1 != null) {
				progress1.dismiss();
			}
			LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
			LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
			if (intent.getStringExtra("message").equals("Y")) {
                customVariablesAndMethod.getAlert(context,"Registered",dr_name_reg+" Successfully Re-Registered("+dr_id_index+")");
			}
		}
	};

}
