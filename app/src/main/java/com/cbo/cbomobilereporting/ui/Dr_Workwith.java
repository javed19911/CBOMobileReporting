package com.cbo.cbomobilereporting.ui;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.flurry.android.FlurryAgent;


import utils.ExceptionHandler;
import utils.MyConnection;
import utils.adapterutils.Dcr_Workwith_Adapter;
import utils.adapterutils.Dcr_Workwith_Model;
import utils_new.Custom_Variables_And_Method;

public class Dr_Workwith extends Activity
	{
		ListView mylist;
		Button done;
		ResultSet rs;
		int PA_ID;
		ArrayAdapter<Dcr_Workwith_Model>adapter;

		ArrayList<String>data,data1;
		StringBuilder sb,sb2;
		CBO_DB_Helper cbohelp;
		ArrayList<Dcr_Workwith_Model>list=new ArrayList<Dcr_Workwith_Model>();
		Context context;
		String DAIRY_ID = "";

		
		
		public void onCreate(Bundle b)
		{
			super.onCreate(b);
			//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
			setContentView(R.layout.doctor_workwith);
            FlurryAgent.logEvent("Dr_Workwith");
			mylist=(ListView)findViewById(R.id.dr_workwith_list);
			done=(Button)findViewById(R.id.dr_workwith_save);
			PA_ID= Custom_Variables_And_Method.PA_ID;
			data=new ArrayList<String>();
			data1=new ArrayList<String>();
			sb=new StringBuilder();
			sb2=new StringBuilder();

			context = this;
			DAIRY_ID = getIntent().getStringExtra("DAIRY_ID") != null ? getIntent().getStringExtra("DAIRY_ID"):"";
			new Doback().execute(PA_ID);
			
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
						sb2.append(data1.get(i)).append(",");
						sb.append(data.get(i)).append(",");
						
					}
					Intent i=new Intent();
					i.putExtra("workwith_id", sb.toString());
					i.putExtra("workwith_name", sb2.toString());
					setResult(RESULT_OK, i);
	    	    	finish();
					
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


		
		class Doback extends AsyncTask<Integer, String, ArrayList<Dcr_Workwith_Model>> {
			ProgressDialog pd;

			@Override
			protected ArrayList<Dcr_Workwith_Model> doInBackground(Integer... params) {
				// TODO Auto-generated method stub
				list.clear();
		    	try{
		    		if(DAIRY_ID.equals("")) {
							cbohelp=new CBO_DB_Helper(getApplicationContext());
							Cursor c=cbohelp.getDR_Workwith();
							if(c.moveToFirst())
						  {
							  do
							  {
								  list.add(new Dcr_Workwith_Model(c.getString(c.getColumnIndex("workwith")),c.getString(c.getColumnIndex("wwid"))));
							  }	while(c.moveToNext());
							 
						  }
							cbohelp.close();
					} else {
						cbohelp=new CBO_DB_Helper(getApplicationContext());
						Cursor c=cbohelp.get_phdairy_person(DAIRY_ID);
						if(c.moveToFirst())
						{
							do
							{
								list.add(new Dcr_Workwith_Model(c.getString(c.getColumnIndex("PERSON_NAME")),c.getString(c.getColumnIndex("PERSON_ID"))));
							}	while(c.moveToNext());

						}
						cbohelp.close();
			    		 /*Statement smt=mycon.connection().createStatement();
			    		 rs=smt.executeQuery("WORKWITH "+PA_ID+",'','','','','','',''");
			    		 while(rs.next())
			    		 {
			    			 list.add(new Dcr_Workwith_Model(rs.getString("PA_NAME"),rs.getString("PA_ID")));
			    		 }
						 rs.close();
						 smt.close();*/
					}
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
				
			return list;
				
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pd = new ProgressDialog(Dr_Workwith.this);
				pd.setTitle("CBO");
				pd.setMessage("Processing......."+"\n"+"please wait");
				pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
				pd.show();
			}

			@Override
			protected void onPostExecute(ArrayList<Dcr_Workwith_Model> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				String[] selected_list={};
				adapter=new Dcr_Workwith_Adapter((Activity) context,result,selected_list);
				mylist.setAdapter(adapter);
				pd.dismiss();
				
				
			}
		}

		@Override
		public void onBackPressed() {
			Intent i=new Intent();
			i.putExtra("workwith_id", sb.toString());
			i.putExtra("workwith_name", sb2.toString());
			setResult(RESULT_CANCELED, i);
			finish();

			super.onBackPressed();
		}
	}
