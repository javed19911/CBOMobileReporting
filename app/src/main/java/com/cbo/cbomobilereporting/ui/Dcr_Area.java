package com.cbo.cbomobilereporting.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import services.CboServices;
import services.ServiceHandler;
import utils.MyConnection;
import utils.adapterutils.Dcr_Workwith_Adapter;
import utils.adapterutils.Dcr_Workwith_Model;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.Custom_Variables_And_Method;

public class Dcr_Area extends AppCompatActivity
	{
		ListView mylist;
		Button done;
        EditText filter;
		ResultSet rs;
		Custom_Variables_And_Method customVariablesAndMethod;
		int PA_ID;
		String mr_id1,mr_id2,mr_id3,mr_id4,mr_id5,mr_id6,mr_id7,mr_id8;
		ArrayAdapter<Dcr_Workwith_Model>adapter;
		CBO_DB_Helper cbohelp;
		ArrayList<String>data,data1;
		StringBuilder sb,sb2;
		String mr_id="";
		Context context;
		List<Dcr_Workwith_Model>list=new ArrayList<Dcr_Workwith_Model>();

        Dcr_Workwith_Model[]TitleName;
        ArrayList<Dcr_Workwith_Model>array_sort;
        int textlength=0;
		String sAllYn;
		public ProgressDialog progress1;
		private  static final int MESSAGE_INTERNET_AREA=1;
		String[] selected_list;

		
		
		public ArrayList<String>getMrId()
		{
			ArrayList<String>mrid=new ArrayList<String>();
			Cursor c=cbohelp.getDR_Workwith();
			if(c.moveToFirst())
			{
				do{
					mrid.add(c.getString(c.getColumnIndex("wwid")));
				}while(c.moveToNext());
			}
			return mrid;
		}
		
		public void setMrids()
		{
			int mr_size=getMrId().size();
			if(mr_size==1)
			{
				mr_id1=getMrId().get(0);
				mr_id2="0";
				mr_id3="0";
                mr_id4="0";
                mr_id5="0";
                mr_id6="0";
                mr_id7="0";
                mr_id8="0";
			}
			else if(mr_size==2)
			{
				mr_id1=getMrId().get(0);
				mr_id2=getMrId().get(1);
				mr_id3="0";
                mr_id4="0";
                mr_id5="0";
                mr_id6="0";
                mr_id7="0";
                mr_id8="0";
			}
			else if(mr_size==3)
			{
				mr_id1=getMrId().get(0);
				mr_id2=getMrId().get(1);
				mr_id3=getMrId().get(2);
                mr_id4="0";
                mr_id5="0";
                mr_id6="0";
                mr_id7="0";
                mr_id8="0";
			}
			else if(mr_size==4)
			{
                mr_id1=getMrId().get(0);
                mr_id2=getMrId().get(1);
                mr_id3=getMrId().get(2);
                mr_id4=getMrId().get(3);
                mr_id5="0";
                mr_id6="0";
                mr_id7="0";
                mr_id8="0";
			}
            else if(mr_size==5)
            {
                mr_id1=getMrId().get(0);
                mr_id2=getMrId().get(1);
                mr_id3=getMrId().get(2);
                mr_id4=getMrId().get(3);
                mr_id5=getMrId().get(4);
                mr_id6="0";
                mr_id7="0";
                mr_id8="0";
            }
            else if(mr_size==6)
            {
                mr_id1=getMrId().get(0);
                mr_id2=getMrId().get(1);
                mr_id3=getMrId().get(2);
                mr_id4=getMrId().get(3);
                mr_id5=getMrId().get(4);
                mr_id6=getMrId().get(5);
                mr_id7="0";
                mr_id8="0";
            }
            else if(mr_size==7)
            {
                mr_id1=getMrId().get(0);
                mr_id2=getMrId().get(1);
                mr_id3=getMrId().get(2);
                mr_id4=getMrId().get(3);
                mr_id5=getMrId().get(4);
                mr_id6=getMrId().get(5);
                mr_id7=getMrId().get(6);
                mr_id8="0";
            }
            else if(mr_size>7)
            {
                mr_id1=getMrId().get(0);
                mr_id2=getMrId().get(1);
                mr_id3=getMrId().get(2);
                mr_id4=getMrId().get(3);
                mr_id5=getMrId().get(4);
                mr_id6=getMrId().get(5);
                mr_id7=getMrId().get(6);
                mr_id8=getMrId().get(7);
            }
		}
		
		
		public void onCreate(Bundle b)
		{
			super.onCreate(b);
			//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
			setContentView(R.layout.dcr_area);
            FlurryAgent.logEvent("Dcr Area");

			context=this;
			array_sort=new ArrayList<>();

			Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
			TextView textView =(TextView) findViewById(R.id.hadder_text_1);
			setSupportActionBar(toolbar);

			textView.setText("Area List");
			if (getSupportActionBar() != null){

				getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
			}


			mylist=(ListView)findViewById(R.id.dcr_area_list);
			done=(Button)findViewById(R.id.dcr_area_save);
            filter=(EditText)findViewById(R.id.myfilter);
			customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
			cbohelp=new CBO_DB_Helper(getApplicationContext());
			progress1 = new ProgressDialog(this);
			PA_ID= Custom_Variables_And_Method.PA_ID;
			data=new ArrayList<String>();
			data1=new ArrayList<String>();
			sb=new StringBuilder();
			sb2=new StringBuilder();

			Intent intent=getIntent();
			sAllYn=intent.getStringExtra("sAllYn");
			Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DATE");

			getMrId();
			setMrids();
			selected_list=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"area_name").replace("+",",").split(",");

			//Start of call to service

			HashMap<String, String> request = new HashMap<>();
			request.put("sCompanyFolder", cbohelp.getCompanyCode());
			request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
			request.put("iMR_ID1", mr_id1);
			request.put("iMR_ID2", mr_id2);
			request.put("iMR_ID3", mr_id3);
			request.put("iMR_ID4", mr_id4);
			request.put("iMR_ID5", mr_id5);
			request.put("iMR_ID6", mr_id6);
			request.put("iMR_ID7", mr_id7);
			request.put("iMR_ID8", mr_id8);
			request.put("sWorkType", Custom_Variables_And_Method.work_val);
			request.put("sDcrDate", Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT);
			request.put("iDivertYn", sAllYn);

			ArrayList<Integer> tables = new ArrayList<>();
			tables.add(0);

			progress1.setMessage("Please Wait.. \n Fetching Area");
			progress1.setCancelable(false);
			progress1.show();

			new CboServices(this, mHandler).customMethodForAllServices(request, "DCRAREADDL_2", MESSAGE_INTERNET_AREA, tables);

			//End of call to service

			Custom_Variables_And_Method.work_with_area_id="";

           filter.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

               }

               @Override
               public void onTextChanged(CharSequence s, int start, int before, int count) {

                   textlength = filter.getText().length();
                   //getDoctor(PA_ID).clear_2();
                   array_sort.clear();
                   for (int i = 0; i < TitleName.length; i++) {
                       if (textlength <= TitleName[i].getName().length()) {

                           if (TitleName[i].getName().toLowerCase().contains(filter.getText().toString().toLowerCase().trim())) {
                               array_sort.add(TitleName[i]);
                           }
                       }
                   }
                   mylist.setAdapter(new Dcr_Workwith_Adapter(Dcr_Area.this,array_sort,selected_list));

               }

               @Override
               public void afterTextChanged(Editable s) {

               }
           });
			
			done.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					for(int i=0;i<list.size();i++){
						boolean check=list.get(i).isSelected();
						if(check){
							data.add(list.get(i).getId());
							data1.add(list.get(i).getName());
						}
						else
						{
							data.remove(list.get(i));
							data1.remove(list.get(i));
						}
					}
					for(int i=0;i<data.size();i++){
						sb2.append(data1.get(i)).append("+");
						sb.append(data.get(i)).append(",");
						
					}
					Intent i=new Intent();
					i.putExtra("area", sb.toString());
					i.putExtra("area_name", sb2.toString());
					//mycon.msgBox(sb.toString());
					
					SharedPreferences.Editor editor=Dcr_Area.this.getSharedPreferences(Custom_Variables_And_Method.FMCG_PREFRENCE, MODE_PRIVATE).edit();
					editor.putString("area_name", sb2.toString());
					editor.putString("area_id", sb.toString());
					editor.commit();
					
					//MyConnection.work_with_area=sb2.toString();
					//MyConnection.work_with_area_id=sb.toString();
					
					setResult(RESULT_OK, i);
	    	    	finish();
	    	    	//mycon.msgBox(sb2.toString());
					
				}
			});
		}
        @Override
        protected void onStart() {
            super.onStart();
        }

        @Override
        protected void onStop() {
            super.onStop();
        }
		
		public void onBackPressed(){
			 Intent i=new Intent();
		    	i.putExtra("val", "");
		    	setResult(RESULT_CANCELED, i);

			 super.onBackPressed();
		 }


		private final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case MESSAGE_INTERNET_AREA:
						progress1.dismiss();
						if ((null != msg.getData())) {

							parser_area(msg.getData());

						}
						break;
					case 99:
						progress1.dismiss();
						if ((null != msg.getData())) {
							customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
							//Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
						}
						break;
					default:
						progress1.dismiss();

				}
			}
		};

		public void parser_area(Bundle result) {
			if (result!=null ) {

				try {

					ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
					newlist.add(new SpinnerModel("--Select--", ""));

					String table0 = result.getString("Tables0");
					JSONArray jsonArray1 = new JSONArray(table0);
					for (int i = 0; i < jsonArray1.length(); i++) {
						JSONObject c = jsonArray1.getJSONObject(i);
						list.add(new Dcr_Workwith_Model(c.getString("AREA"),""+i));

					}
					TitleName = new Dcr_Workwith_Model[list.size()];
					for (int i = 0; i < list.size(); i++) {
						TitleName[i] = list.get(i);
					}

					array_sort = (ArrayList<Dcr_Workwith_Model>) list;
					adapter=new Dcr_Workwith_Adapter(Dcr_Area.this,array_sort,selected_list);
					adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
					mylist.setAdapter(adapter);

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


		@Override
		public boolean onOptionsItemSelected(MenuItem item) {

			if (item != null){

				Intent i=new Intent();
				i.putExtra("val", "");
				setResult(RESULT_CANCELED, i);
				finish();
			}


			return super.onOptionsItemSelected(item);
		}
	}
