package com.cbo.cbomobilereporting.ui;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.cbo.cbomobilereporting.ui_new.report_activities.Logged_UnLogged;
import com.flurry.android.FlurryAgent;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.ServiceHandler;
import utils.ExceptionHandler;
import utils.MyConnection;
import utils_new.Custom_Variables_And_Method;

public class Rpt_Detail extends AppCompatActivity{
	ListView mylist;
	Button back;
	Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
	SimpleAdapter sm;
	ResultSet rs;
	String mydate="";
	int PA_ID;
	LinearLayout mobloc;
	double rpt_time;
	List<Map<String,String>>data=null;
    ServiceHandler myServiceHandler;
    CBO_DB_Helper myCboDbHelper;

	public void onCreate(Bundle b){
		super.onCreate(b);
		//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.rpt_detail);
        FlurryAgent.logEvent("Rpt_Detail");
        Toolbar  toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
        textView.setText("DCR Reports");

        setSupportActionBar(toolbar);
       		if (getSupportActionBar() != null){

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

            }




        mylist=(ListView)findViewById(R.id.rpt_list);
		back=(Button)findViewById(R.id.rpt_list_bk);
		mobloc=(LinearLayout)findViewById(R.id.mob_loc);
		context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
		PA_ID=Integer.parseInt(Custom_Variables_And_Method.EMP_ID);
		rpt_time=Double.parseDouble(Custom_Variables_And_Method.RPT_TIME.trim());
        myServiceHandler = new ServiceHandler(context);
        myCboDbHelper =new CBO_DB_Helper(Rpt_Detail.this);


		mydate=Custom_Variables_And_Method.RPT_DATE;


		if(Custom_Variables_And_Method.location_required.equals("Y"))
		{

            new MyExpenseList().execute();
		}
		else
		{

            new RptList().execute();
		}

		int[] colors = {0, 0xFFFF0000, 0};
		mylist.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		mylist.setDividerHeight(2);


		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Logged_UnLogged.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
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
		 Intent i=new Intent(getApplicationContext(),ViewPager_2016.class);
	    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	startActivity(i);
            finish();
		 super.onBackPressed();
	 }

	public void onRestart()
	{
		super.onRestart();

	}
    class MyExpenseList extends AsyncTask<String,String,String>{
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            data=new ArrayList<Map<String,String>>();
            data.clear();
            String responseDCRCALLDETMOBILEGRID;
            try{
                responseDCRCALLDETMOBILEGRID = myServiceHandler.getResponse_DCRCALLDETMOBILEGRID(myCboDbHelper.getCompanyCode(),
                        ""+PA_ID,""+mydate,""+mydate,"0",""+rpt_time,"");
            }catch (Exception e){
                return "ERROR apk "+e;
            }


            return responseDCRCALLDETMOBILEGRID;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if ((s!= null)&&(!s.toLowerCase().contains("error"))){
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray =jsonObject.getJSONArray("Tables");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("Tables0");
                    for (int i=0; i<jsonArray1.length();i++){
                        JSONObject object = jsonArray1.getJSONObject(i);
                        Map<String,String>datanum=new HashMap<String,String>();
                        datanum.put("type", object.getString("TYPE"));
                        datanum.put("name", object.getString("DR_NAME"));
                        datanum.put("time", object.getString("TIME"));
                        datanum.put("rarea", object.getString("AREA"));
                        datanum.put("marea", object.getString("AREA_MOBILE"));
                        //MyConnection.Massege_Id=rs.getString("ID");
                        data.add(datanum);


                    }
                    String [] from={"type","name","time","rarea","marea"};
                    int [] to={R.id.type,R.id.emp_name,R.id.emp_time,R.id.emp_act_area,R.id.emp_rpt_area};
                    sm=new SimpleAdapter(Rpt_Detail.this,data,R.layout.rpt_row,from,to);
                    mylist.setAdapter(sm);
                    if(data.isEmpty())
                    {
                        back.setVisibility(View.GONE);
                        AlertDialog.Builder builder1=new AlertDialog.Builder(Rpt_Detail.this);
                        builder1.setTitle("Empty List");
                        builder1.setMessage(" No Reports Found....");
                        builder1.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                Intent i=new Intent(Rpt_Detail.this,Logged_UnLogged.class);
                                startActivity(i);
                            }
                        });
                        builder1.show();
                    }

                }catch (JSONException e){
                    Log.v("Resultjaved", "service " + e.toString());
                    customVariablesAndMethod.msgBox(context,"Exception  Found.....");
                    pd.dismiss();
                }




            }else{
                customVariablesAndMethod.msgBox(context,"Nothing Found.....");
            }
           pd.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(Rpt_Detail.this);
            pd.setTitle("CBO");
            pd.setMessage("Processing...");
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.show();

        }
    }



    class RptList extends AsyncTask<String,String,String>{
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            data=new ArrayList<Map<String,String>>();
            data.clear();
            String responseDCRCALLDETMOBILEGRID;
            try{
                responseDCRCALLDETMOBILEGRID = myServiceHandler.getResponse_DCRCALLDETMOBILEGRID(myCboDbHelper.getCompanyCode(),
                        ""+PA_ID,""+mydate,""+mydate,"0",""+rpt_time,"");
            }catch (Exception e){
                return "ERROR apk "+e;
            }

            return responseDCRCALLDETMOBILEGRID;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if ((s!= null)&&(!s.toLowerCase().contains("error"))){
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray =jsonObject.getJSONArray("Tables");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("Tables0");
                    for (int i=0; i<jsonArray1.length();i++){
                        JSONObject object = jsonArray1.getJSONObject(i);
                        Map<String,String>datanum=new HashMap<String,String>();
                        datanum.put("type", object.getString("TYPE"));
                        datanum.put("name", object.getString("DR_NAME"));
                        datanum.put("time", object.getString("TIME"));
                        datanum.put("rarea", object.getString("AREA"));
                        datanum.put("marea", object.getString("AREA_MOBILE"));
                        //MyConnection.Massege_Id=rs.getString("ID");
                        data.add(datanum);


                    }
                    String [] from={"type","name","time","rarea"};
                    int [] to={R.id.type_noloc,R.id.emp_name_noloc,R.id.emp_time_noloc,R.id.emp_act_area_noloc};
                    sm=new SimpleAdapter(Rpt_Detail.this,data,R.layout.rpt_row_noloc,from,to);
                    mylist.setAdapter(sm);
                    if(data.isEmpty())
                    {
                        back.setVisibility(View.GONE);
                        AlertDialog.Builder builder1=new AlertDialog.Builder(Rpt_Detail.this);
                        builder1.setTitle("Empty List");
                        builder1.setMessage(" No Reports Found....");
                        builder1.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                Intent i=new Intent(getApplicationContext(),Logged_UnLogged.class);
                                startActivity(i);
                            }
                        });
                        builder1.show();
                    }

                }catch (JSONException e){
                    Log.v("Resultjaved", "service " + e.toString());
                    customVariablesAndMethod.msgBox(context,"Exception  Found.....");
                    pd.dismiss();
                }




            }else{
                customVariablesAndMethod.msgBox(context,"Nothing Found.....");
                pd.dismiss();
            }
            pd.dismiss();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(Rpt_Detail.this);
            pd.setTitle("CBO");
            pd.setMessage("Processing...");
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item != null){

    finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
