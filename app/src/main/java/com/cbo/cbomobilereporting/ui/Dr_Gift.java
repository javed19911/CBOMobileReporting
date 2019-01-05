package com.cbo.cbomobilereporting.ui;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.flurry.android.FlurryAgent;


import services.Up_Dwn_interface;
import utils.ExceptionHandler;
import utils.MyConnection;
import utils.adapterutils.GiftModel;
import utils.adapterutils.MyAdapter;
import utils.adapterutils.MyAdapter2;
import utils.networkUtil.NetworkUtil;
import utils.upload_download;
import utils_new.Custom_Variables_And_Method;


public class Dr_Gift extends Activity implements Up_Dwn_interface {
	ListView mylist;
	Button save;
	int PA_ID=0;
	int cnt;
	boolean check;
	ResultSet rs;
	String val1="";
	String val2="";
	ArrayAdapter<GiftModel>adapter;
	List<GiftModel>list=new ArrayList<GiftModel>();
	CBO_DB_Helper cbohelp;
	ArrayList<String>data1,data2,data3;
	StringBuilder sb1,sb2;
	String gift_name="",gift_qty="";
	Custom_Variables_And_Method customVariablesAndMethod;
	Context context;
	
	public void onCreate(Bundle b){
		super.onCreate(b);

		setContentView(R.layout.dr_gift);
        FlurryAgent.logEvent("Dr_Gift");

		customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
		context=this;

		mylist=(ListView)findViewById(R.id.dr_gift_list);
		save=(Button)findViewById(R.id.dr_gift_save);
		PA_ID= Custom_Variables_And_Method.PA_ID;
		data1=new ArrayList<String>();
		data2=new ArrayList<String>();
		data3=new ArrayList<String>();
		sb1=new StringBuilder();
		sb2=new StringBuilder();
		cbohelp=new  CBO_DB_Helper(getApplicationContext());

		Bundle getExtra = getIntent().getExtras();
		if (getExtra != null) {
			gift_name = getExtra.getString("gift_name");
			gift_qty = getExtra.getString("gift_qty");
		}

		adapter=new MyAdapter2(this,getModel());
		if(adapter.getCount()!=0) {
			mylist.setAdapter(adapter);
			String[] sample_name1= gift_name.split(",");
			String[] sample_qty1= gift_qty.split(",");

			for (int i=0;i<sample_name1.length;i++){
				for (int j=0;j<list.size();j++) {
					if (sample_name1[i].equals(list.get(j).getName())) {
						list.get(j).setScore(sample_qty1[i]);
					}
				}
			}
		}
		else
		{
			AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
			builder1.setTitle("CBO");
			builder1.setIcon(R.drawable.alert1);
			builder1.setMessage(" No Data In List.." + "\n" + "Please Download Data.....");
			builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					NetworkUtil networkUtil = new NetworkUtil(context);
					if (!networkUtil.internetConneted(context)) {
						customVariablesAndMethod.Connect_to_Internet_Msg(context);
					} else {

						new upload_download(context);
					}

				}
			});
			builder1.show();
		}
		
		
		
		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StringBuilder sb3=new StringBuilder();
				String POB="0";
				String Rate="x";
				String Qty="";
				cbohelp.deletedata(Custom_Variables_And_Method.DR_ID,Rate);
            	for( cnt=0;cnt<list.size();cnt++){

            			Qty=list.get(cnt).getScore();

						if(!list.get(cnt).getScore().equals("") && !list.get(cnt).getScore().equals("")){
							cbohelp.insertdata(Custom_Variables_And_Method.DR_ID, list.get(cnt).getId(), list.get(cnt).getName(), Qty, POB, Rate,"0","0");
						}

            			

            			sb3.append(list.get(cnt).getId()).append(",");
                    	sb2.append(Qty).append(",");
                	

                	
            	}
            	// mycon.msgBox(sb3.toString());
                // mycon.msgBox(sb2.toString());
      	    	Intent i=new Intent();
      	    	i.putExtra("giftid", sb3.toString());
      	    	i.putExtra("giftqan", sb2.toString());
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


	 private List<GiftModel> getModel() {
		 list.clear();
			//int i=0;
	    	String ItemIdNotIn="0";
	    	//cbohelp.giftDelete();
	    	
		Cursor c1=cbohelp.getAllGifts(ItemIdNotIn);
			 if(c1.moveToFirst()){
				 do{
					 list.add(new GiftModel(c1.getString(c1.getColumnIndex("item_name")), c1.getString(c1.getColumnIndex("item_id")), ""));

				 }while(c1.moveToNext());
			 }
	       	        return list;
	    }
	 
	 
	 public void onBackPressed(){
		 Intent i=new Intent();
	    	i.putExtra("giftid", "");
	    	i.putExtra("giftqan", "");
	    	setResult(RESULT_CANCELED, i);
	    	finish();
		 
		 super.onBackPressed();
	 }


	@Override
	public void onDownloadComplete() {
		adapter=new MyAdapter2(this,getModel());
		mylist.setAdapter(adapter);
	}
}
