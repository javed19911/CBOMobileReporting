package com.cbo.cbomobilereporting.ui;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;

import services.ServiceHandler;
import utils.MyConnection;
import utils.adapterutils.MailAdapter;
import utils.adapterutils.MailModel;
import utils_new.Custom_Variables_And_Method;

public class MailTo_PPL extends Activity{
	ListView mylist;
	Button done;
	ResultSet rs;
	int PA_ID;
	ArrayAdapter<MailModel>adapter;
	ArrayList<String>data,data1;
	StringBuilder sb,sb2;
	List<MailModel>list=new ArrayList<MailModel>();
    CBO_DB_Helper myCboDbHelper;
    ServiceHandler myServiceHandler;
	Context context;
	
	public void getData(){

           adapter=new MailAdapter(this,list);
		// adapter=new MailAdapter(this,new Doback().doInBackground(PA_ID));
			mylist.setAdapter(adapter);
		 
	 }
	
	public void onCreate(Bundle b){
		super.onCreate(b);
		//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.mail_toppl);
        FlurryAgent.logEvent("MailTo_PPL");
		mylist=(ListView)findViewById(R.id.mailto_list);
		done=(Button)findViewById(R.id.btn_mailtosave);
		PA_ID= Custom_Variables_And_Method.PA_ID;
		data=new ArrayList<String>();
		data1=new ArrayList<String>();
		sb=new StringBuilder();
		sb2=new StringBuilder();
		context=this;
        myCboDbHelper = new CBO_DB_Helper(MailTo_PPL.this);
        myServiceHandler = new ServiceHandler(context);
		
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
				i.putExtra("mailto", sb.toString());
				i.putExtra("mailtoname", sb2.toString());
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

	public void onBackPressed(){
		 Intent i=new Intent();
	    	i.putExtra("val", "");
	    	setResult(RESULT_CANCELED, i);
		 super.onBackPressed();
	 }
	
	class Doback extends AsyncTask<Integer, String, List<MailModel>> {
		ProgressDialog pd;

		@Override
		protected List<MailModel> doInBackground(Integer... params) {
			// TODO Auto-generated method stub

	    	try{
             String response_MAILTODDL;

                response_MAILTODDL = myServiceHandler.getResponse_MAILTODDL(myCboDbHelper.getCompanyCode(),""+Custom_Variables_And_Method.PA_ID);
				if (!response_MAILTODDL.contains("ERROR")) {
					JSONObject jsonObject = new JSONObject(response_MAILTODDL);
					JSONArray jsonArray = jsonObject.getJSONArray("Tables0");
					list.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);
						list.add(new MailModel(object.getString("PA_NAME"), object.getString("PA_ID")));
					}

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
			pd = new ProgressDialog(MailTo_PPL.this);
			pd.setTitle("CBO");
			pd.setMessage("Processing......."+"\n"+"please wait");
			pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
			pd.show();
		}

		@Override
		protected void onPostExecute(List<MailModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			getData();
			pd.dismiss();
			
			
		}
	}


}
