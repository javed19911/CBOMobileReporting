package com.cbo.cbomobilereporting.ui;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.flurry.android.FlurryAgent;


import org.json.JSONArray;
import org.json.JSONObject;

import async.CBORootTask;
import services.ServiceHandler;
import services.TaskListener;
import utils.adapterutils.DcrRootAdapter;
import utils.adapterutils.RootModel;
import utils_new.Custom_Variables_And_Method;

public class DcrRoot extends AppCompatActivity{
	ListView mylist;
	Button done;
	ResultSet rs;
	Context context;
	Custom_Variables_And_Method customVariablesAndMethod;
	int PA_ID;
	String mr_id1,mr_id2,mr_id3;
	DcrRootAdapter adapter;
	CBO_DB_Helper cbohelp;
	ArrayList<String>data,data1;
	StringBuilder sb,sb2;
	String sAllYn;
	ArrayList<RootModel>list=new ArrayList<RootModel>();
	ProgressDialog pd;
	
	String name;
    ServiceHandler myServiceHandler;
	

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
		}
		else if(mr_size==2)
		{
			mr_id1=getMrId().get(0);
			mr_id2=getMrId().get(1);
			mr_id3="0";
		}
		else if(mr_size>2)
		{
			mr_id1=getMrId().get(0);
			mr_id2=getMrId().get(1);
			mr_id3=getMrId().get(2);
		}
		else
		{
			mr_id1="0";
			mr_id2="0";
			mr_id3="0";
		}
	}
	
	public void setData()
	{
		adapter=new DcrRootAdapter(DcrRoot.this,new Doback().doInBackground(),false );
		mylist.setAdapter(adapter);
	}
	
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.dcr_root);
        FlurryAgent.logEvent("Dcr Root");




		Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
		TextView textView =(TextView) findViewById(R.id.hadder_text_1);
		setSupportActionBar(toolbar);

		textView.setText("Route List");
		if (getSupportActionBar() != null){

			getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
		}


		mylist=(ListView)findViewById(R.id.dcr_root_list);
		done=(Button)findViewById(R.id.dcr_root_save);
		context=this;
		customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
		cbohelp=new CBO_DB_Helper(context);
		PA_ID= Custom_Variables_And_Method.PA_ID;
		data=new ArrayList<String>();
		data1=new ArrayList<String>();
	//	mr_id=getIntent().getExtras().getString("mrid");
		sb=new StringBuilder();
		sb2=new StringBuilder();
        myServiceHandler = new ServiceHandler(context);

		Intent intent=getIntent();
		sAllYn=intent.getStringExtra("sAllYn");
		
		/*adapter=new DcrRootAdapter(DcrRoot.this,getdata());
		mylist.setAdapter(adapter);*/
		DcrRoot.this.setRootDataToUI(DcrRoot.this);
		//new Doback().execute();
		Custom_Variables_And_Method.work_with_area_id="";
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

				Intent i=new Intent();
				i.putExtra("route_name", adapter.name);
				i.putExtra("route_id", adapter.id);
				
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
	
	class Doback extends AsyncTask<Void, String, List<RootModel>> {
		

		@Override
		protected List<RootModel> doInBackground(Void...params) {
			// TODO Auto-generated method stub
			list.clear();
			getMrId();
			setMrids();
			
	    	try{
				Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DATE");
                String response_DCRAREADDL_ROUTE = myServiceHandler.getResponse_DCRAREADDL_ROUTE(cbohelp.getCompanyCode(),
                        ""+Custom_Variables_And_Method.PA_ID,""+mr_id1,""+mr_id2,""+mr_id3,""+0,""+0,Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT,sAllYn);

                JSONObject jsonObject = new JSONObject(response_DCRAREADDL_ROUTE);
                JSONArray jsonArray =jsonObject.getJSONArray("Tables0");
                if ((response_DCRAREADDL_ROUTE.equals("ERROR"))||(response_DCRAREADDL_ROUTE == null)){
					customVariablesAndMethod.msgBox(context,"Technical Error..");
                }

                else {

                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    list.add(new RootModel(object.getString("ROUTE_NAME"),object.getString("DISTANCE_ID")));
                }}

	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}				
		return list;				
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(DcrRoot.this);
			pd.setTitle("CBO");
			pd.setMessage("Processing......."+"\n"+"please wait");
			pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}

		@Override
		protected void onPostExecute(List<RootModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//adapter.setList(list);
			setData();
			//mylist.setAdapter(adapter);
			//getData();
			pd.dismiss();
			
			
		}
	}

    public void setRootDataToUI( final Activity context){
         final CBORootTask rootTask=new CBORootTask(context);
            DcrRoot.this.getMrId();
            DcrRoot.this.setMrids();
            rootTask.setListener(new TaskListener<String>() {
            ProgressDialog commitDialog;

            @Override
            public void onStarted() {
                try {
                    commitDialog = new ProgressDialog(context);
                    commitDialog.setMessage("Please Wait..");
                    commitDialog.setCanceledOnTouchOutside(false);
                    commitDialog.setCancelable(false);
                    commitDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinished(String result) {
                if ((result == null) || (result.contains("[ERROR]"))) {
                    commitDialog.dismiss();
					customVariablesAndMethod.msgBox(context,result);
                } else {
                    list=rootTask.setDataToRootList(result,list);
                    adapter=new DcrRootAdapter(DcrRoot.this,list ,false);
                    mylist.setAdapter(adapter);
                    commitDialog.dismiss();
                }

            }

        });

		Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DATE");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            rootTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,""+PA_ID,mr_id1,mr_id2,mr_id3,Custom_Variables_And_Method.work_val,Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT,sAllYn);
        }
        else
        {
            rootTask.execute(""+PA_ID,mr_id1,mr_id2,mr_id3,Custom_Variables_And_Method.work_val,Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT,sAllYn);
        }
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
