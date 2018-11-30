package com.cbo.cbomobilereporting.ui;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import services.Up_Dwn_interface;
import utils.ExceptionHandler;
import utils.MyConnection;
import utils.adapterutils.GiftModel;
import utils.adapterutils.MyAdapter;
import utils.adapterutils.MyAdapter2;
import utils.networkUtil.NetworkUtil;
import utils.upload_download;
import utils_new.Custom_Variables_And_Method;


public class Chemist_Gift extends Activity implements Up_Dwn_interface {
	ListView mylist;
	Button save;
	Custom_Variables_And_Method customVariablesAndMethod;
	Context context;
	int PA_ID=0;
	ResultSet rs;
	String val1="";
	String val2="";
	ArrayAdapter<GiftModel>adapter;
	CBO_DB_Helper cbohelp;
	List<GiftModel>list=new ArrayList<GiftModel>();
	ArrayList<String>item_qty,item_id,item_name;
	StringBuilder sb1,sb2;
	String gift_name="",gift_qty="";
	
	public void onCreate(Bundle b){
		super.onCreate(b);
		//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setContentView(R.layout.chemist_gift);
        FlurryAgent.logEvent("Chemist Gift");

		mylist=(ListView)findViewById(R.id.chm_gift_list);
		save=(Button)findViewById(R.id.chm_gift_save);
		context=this;
		customVariablesAndMethod=Custom_Variables_And_Method.getInstance(context);

		PA_ID= Custom_Variables_And_Method.PA_ID;
		item_qty=new ArrayList<String>();
		item_id=new ArrayList<String>();
		item_name=new ArrayList<String>();
		sb1=new StringBuilder();
		sb2=new StringBuilder();
		cbohelp=customVariablesAndMethod.get_cbo_db_instance();

		Bundle getExtra = getIntent().getExtras();
		if (getExtra != null) {
			gift_name = getExtra.getString("gift_name");
			gift_qty = getExtra.getString("gift_qty");
		}

		getModelLocal();
		if (list.size()>0) {
			adapter = new MyAdapter2(this, list);
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

		}else{
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

				item_qty.clear();
				item_id.clear();
				item_name.clear();

            	for(int i=0;i<list.size();i++){
            		if(!list.get(i).getScore().equals("") && !list.get(i).getScore().equals("")){
						item_id.add(list.get(i).getId());
						item_qty.add(list.get(i).getScore());
						item_name.add(list.get(i).getName());
            		}
            		
            	}
            	
                
                for(int i=0;i<item_id.size();i++){
                	
                	sb3.append(item_id.get(i)).append(",");
                	sb2.append(item_qty.get(i)).append(",");
					sb1.append(item_name.get(i)).append(",");
                }
            	Intent i=new Intent();
    	    	i.putExtra("giftid", sb3.toString());
    	    	i.putExtra("giftqan", sb2.toString());
				i.putExtra("giftname", sb1.toString());
    	    	
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
        FlurryAgent.onEndSession(this);
        super.onStop();
    }


	 
	 private List<GiftModel> getModelLocal() {
		 list.clear();

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
		getModelLocal();
		adapter = new MyAdapter2(this, list);
		mylist.setAdapter(adapter);
	}
}
