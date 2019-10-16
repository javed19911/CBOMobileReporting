package com.cbo.cbomobilereporting.ui_new.mail_activities;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;




import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import services.CboServices;
import utils.adapterutils.DraftMail_Adapter;
import utils.adapterutils.OutboxMail_Adapter;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;

public class Outbox_Mail extends AppCompatActivity{
	ListView mylist;
	Button back;
	int PA_ID;
    CBO_DB_Helper myCbo_Help;
	FloatingActionButton compose;
	Custom_Variables_And_Method customVariablesAndMethod;
	Context context;
	public ProgressDialog progress1;
	private  static final int MESSAGE_INTERNET_Mail=1,MESSAGE_INTERNET_SUBMIT_WORKING=2,MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL=3;
	ArrayList<Map<String, String>> data = null;
	OutboxMail_Adapter outboxMail_adapter=null;
	DraftMail_Adapter draftMail_adapter=null;
	Intent intent;
	TextView textView;
	String mail_type ="s";

	public void onCreate(Bundle b){
		super.onCreate(b);

		 setContentView(R.layout.outbox_mail);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
		textView = (TextView) findViewById(R.id.hadder_text_1);

		textView.setText("Sent");
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null){
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
		}


		data=new ArrayList<Map<String,String>>();
		mylist=(ListView)findViewById(R.id.mailoutbox_list);
		back=(Button)findViewById(R.id.btn_outboxbk);
		compose = (FloatingActionButton) findViewById(R.id.compose);

		PA_ID=Custom_Variables_And_Method.PA_ID;
		int[] colors = {0, 0xFFFF0000, 0};
		mylist.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		mylist.setDividerHeight(2);
		context=this;
		progress1 = new ProgressDialog(this);
        myCbo_Help = new CBO_DB_Helper(context);
		intent=getIntent();

		customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

		compose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String networkStatus= NetworkUtil.getConnectivityStatusString(context);
				if(networkStatus.equals("Not connected to Internet")) {
					customVariablesAndMethod.Connect_to_Internet_Msg(context);
				} else {
					Intent i=new Intent(context,CreateMail1.class);
					startActivity(i);
				}
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();

			}
		});

//		if (intent.getStringExtra("mail_type").equals("d")){
//			textView.setText("Draft");
//			data = myCbo_Help.get_Mail("d","");
//			draftMail_adapter = new DraftMail_Adapter(Outbox_Mail.this, data);
//			mylist.setAdapter(draftMail_adapter);
//
//		}else {
//			//Start of call to service
//
//			HashMap<String, String> request = new HashMap<>();
//			request.put("sCompanyFolder", myCbo_Help.getCompanyCode());
//			request.put("iPaId", "" + Custom_Variables_And_Method.PA_ID);
//			request.put("sMailType", "s");
//			request.put("sFDate", "");
//			String max_id=intent.getStringExtra("mail_id");
//			if (max_id==null) {
//				max_id = myCbo_Help.getMaxMailId("s");
//			}
//			request.put("iFid", max_id);
//
//			ArrayList<Integer> tables = new ArrayList<>();
//			tables.add(0);
//
//			progress1.setMessage("Please Wait.. \n Fetching mails");
//			progress1.setCancelable(false);
//			if (max_id.equals("0")) {
//				progress1.show();
//			}
//			data = myCbo_Help.get_Mail("s","");
//			outboxMail_adapter = new OutboxMail_Adapter(Outbox_Mail.this, data);
//			mylist.setAdapter(outboxMail_adapter);
//
//			new CboServices(this, mHandler).customMethodForAllServices(request, "MAILGRID_1", MESSAGE_INTERNET_Mail, tables);
//
//			//End of call to service
//		}

	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_INTERNET_Mail:
					progress1.dismiss();
					if ((null != msg.getData())) {

						parser_mail(msg.getData());

					}
					break;

				case 99:
					progress1.dismiss();
					if ((null != msg.getData())) {
						customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
					}
					break;
				default:
					progress1.dismiss();

			}
		}
	};

	private void parser_mail(Bundle result) {
		if (result!=null ) {

			try {
				String table0 = result.getString("Tables0");
				JSONArray jsonArray1 = new JSONArray(table0);
				for (int i = 0; i < jsonArray1.length(); i++) {
					JSONObject object = jsonArray1.getJSONObject(i);
					String files = object.getString("FILE_NAME"); //+"|^"+object.getString("FILE_NAME2")+"|^"+object.getString("FILE_NAME3")
					if (files.isEmpty()){
						files = object.getString("FILE_NAME2");
					}else{
						files = files + "|^" + object.getString("FILE_NAME2");
					}
					if (files.isEmpty()){
						files = object.getString("FILE_NAME3");
					}else{
						files = files + "|^" + object.getString("FILE_NAME3");
					}
					myCbo_Help.insert_Mail(object.getInt("ID"),object.getString("TO_PA_ID"),object.getString("TO_PA_NAME"),object.getString("FWD_DATE"),object.getString("FWD_TIME"),
							object.getString("IS_READ"),object.getString("MAIL_TYPE"),object.getString("CC"),object.getString("SUBJECT"),object.getString("REMARK")
							,object.getString("FILE_HEADING")+","+object.getString("FILE_HEADING2")+","+object.getString("FILE_HEADING3")
							,files);
				}
				data=myCbo_Help.get_Mail(mail_type,"");
				outboxMail_adapter=new OutboxMail_Adapter(Outbox_Mail.this,data);
				mylist.setAdapter(outboxMail_adapter);

				progress1.dismiss();
			} catch (JSONException e) {
				Log.d("MYAPP", "objects are: " + e.toString());
				CboServices.getAlert(this,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
				e.printStackTrace();
			}

		}
		progress1.dismiss();
	}


	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	@Override
	public void onStart(){
		super.onStart();
		mail_type =intent.getStringExtra("mail_type");
		load_mail(mail_type);
//		if (intent.getStringExtra("mail_type").equals("d")) {
//			data = myCbo_Help.get_Mail("d","");
//			draftMail_adapter = new DraftMail_Adapter(Outbox_Mail.this, data);
//			mylist.setAdapter(draftMail_adapter);
//		}else {
//			data = myCbo_Help.get_Mail("s", "");
//			outboxMail_adapter=new OutboxMail_Adapter(Outbox_Mail.this,data);
//			mylist.setAdapter(outboxMail_adapter);
//		}


	}

	private void load_mail(String mail_type){

		this.mail_type = mail_type;
		if (mail_type.equals("d")){
			textView.setText("Draft");
			data = myCbo_Help.get_Mail("d","");
			draftMail_adapter = new DraftMail_Adapter(Outbox_Mail.this, data);
			mylist.setAdapter(draftMail_adapter);

		}else {
			if(mail_type.equals("r")){
				textView.setText("Reply/Forwarded");
			}else{
				textView.setText("Send");
			}

			//Start of call to service

			HashMap<String, String> request = new HashMap<>();
			request.put("sCompanyFolder", myCbo_Help.getCompanyCode());
			request.put("iPaId", "" + Custom_Variables_And_Method.PA_ID);
			request.put("sMailType", mail_type);
			request.put("sFDate", "");
			String max_id=intent.getStringExtra("mail_id");
			if (max_id==null) {
				max_id = myCbo_Help.getMaxMailId(mail_type);
			}
			request.put("iFid", max_id);

			ArrayList<Integer> tables = new ArrayList<>();
			tables.add(0);

			progress1.setMessage("Please Wait.. \n Fetching mails");
			progress1.setCancelable(false);
			if (max_id.equals("0")) {
				progress1.show();
			}
			data = myCbo_Help.get_Mail(mail_type,"");
			outboxMail_adapter = new OutboxMail_Adapter(Outbox_Mail.this, data);
			mylist.setAdapter(outboxMail_adapter);

			new CboServices(this, mHandler).customMethodForAllServices(request, "MAILGRID_1", MESSAGE_INTERNET_Mail, tables);

			//End of call to service
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.outbox_mail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		switch (id) {
			case R.id.Send:
				load_mail("s");
				break;
			case R.id.Reply_Forwarded:
				load_mail("r");
				break;
			case R.id.Draft:
				load_mail("d");
				break;
			default:
				finish();
		}
//     		if (item != null){
//			finish();
//		}
		return super.onOptionsItemSelected(item);
	}



}
