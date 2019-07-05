package com.cbo.cbomobilereporting.ui;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.mail_activities.CreateMail1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.CboServices;
import utils.adapterutils.MailDetails_Adapter;
import com.cbo.cbomobilereporting.MyCustumApplication;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;

public class Inbox_Msg extends AppCompatActivity{
	TextView remark,from,subject,date_time,from_label;
    Custom_Variables_And_Method customVariablesAndMethod;
	ImageView attach;
	LinearLayout edit,delete,reply,forward;
	ResultSet rs;
	int PA_ID;
	String Msg_Id,mail_type;
    int ID;
	String MyMsg="";
    CBO_DB_Helper myCboDbHelper;
    Context context;
    ArrayList<Map<String, String>> data = null;
	private  static final int MESSAGE_INTERNET_Mail=1;
	public ProgressDialog progress1;
	ListView mylist;
	
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.mail_msg);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        textView.setText("Mail Details");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        context = Inbox_Msg.this;
		progress1 = new ProgressDialog(this);
        myCboDbHelper = new CBO_DB_Helper(context);
		remark=(TextView)findViewById(R.id.remark);
		subject=(TextView)findViewById(R.id.subject);
		from=(TextView)findViewById(R.id.from);
		date_time=(TextView)findViewById(R.id.date_time);
		from_label=(TextView)findViewById(R.id.from_label);
		attach=(ImageView) findViewById(R.id.attach);

		edit=(LinearLayout) findViewById(R.id.edit);
		delete=(LinearLayout) findViewById(R.id.delete);
		forward=(LinearLayout) findViewById(R.id.forward);
		reply=(LinearLayout) findViewById(R.id.reply);

		mylist = (ListView) findViewById(R.id.mailinbox_list);


		customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
		PA_ID= Custom_Variables_And_Method.PA_ID;
        Intent intent=getIntent();
		Msg_Id=intent.getStringExtra("mail_id");
		mail_type=intent.getStringExtra("mail_type");
        data=myCboDbHelper.get_Mail(mail_type,Msg_Id);

		if (data.get(0).get("FILE_NAME").equals("")){
			attach.setVisibility(View.GONE);
		}

		switch (data.get(0).get("category").toLowerCase()){
			case "i":
				myCboDbHelper.update_Mail(Msg_Id,"1");//mark as read
				edit.setVisibility(View.GONE);
				delete.setVisibility(View.GONE);
				PopulateMail_History(Msg_Id);
				break;
			case "r":
			case "f":
			case "s":
				from_label.setText("To");
				edit.setVisibility(View.GONE);
				reply.setVisibility(View.GONE);
				delete.setVisibility(View.GONE);
				PopulateMail_History(Msg_Id);
				break;
			case "d":
				from_label.setText("To");
				reply.setVisibility(View.GONE);
				forward.setVisibility(View.GONE);
				break;
		}

		remark.setText(data.get(0).get("REMARK"));
		subject.setText(data.get(0).get("sub"));
		from.setText(data.get(0).get("from"));
		date_time.setText(data.get(0).get("date")+"-"+data.get(0).get("time"));

		attach.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final String[] aT1 = {data.get(0).get("FILE_NAME")};
				if(!aT1[0].contains("http://") && !data.get(0).get("category").equals("d")){
					aT1[0] ="http://"+ aT1[0];
				}else if(data.get(0).get("category").equals("d")){
					aT1[0]=(new File(Environment.getExternalStorageDirectory(), "cbo/"+aT1[0]).toString());
				}

				/*Intent i = new Intent(context, CustomWebView.class);
				i.putExtra("A_TP1", aT1[0]);
				i.putExtra("Menu_code", "");
				i.putExtra("Title", "Attachment");
				context.startActivity(i);*/
				MyCustumApplication.getInstance().LoadURL("Attachment",aT1[0]);
			}
		});

		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String networkStatus= NetworkUtil.getConnectivityStatusString(context);
				if(networkStatus.equals("Not connected to Internet")) {
					customVariablesAndMethod.Connect_to_Internet_Msg(context);
				} else {
					Intent intent=new Intent(context,CreateMail1.class);
					intent.putExtra("mail_id",data.get(0).get("id"));
					intent.putExtra("mail_type","E");
					startActivity(intent);
					finish();
				}
			}
		});

		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
				final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
				final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
				final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
				final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.nagative);
				Alert_Nagative.setText("Cancel");
				Alert_Positive.setText("Delete");
				Alert_title.setText("Delete !!!");
				Alert_message.setText("Are you sure to Delete the Draft? ");

				AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

				final AlertDialog dialog = builder1.create();

				dialog.setView(dialogLayout);
				Alert_Positive.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						myCboDbHelper.delete_Mail(data.get(0).get("id"));
						finish();
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
		});

		reply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String networkStatus= NetworkUtil.getConnectivityStatusString(context);
				if(networkStatus.equals("Not connected to Internet")) {
					customVariablesAndMethod.Connect_to_Internet_Msg(context);
				} else {
					Intent intent=new Intent(context,CreateMail1.class);
					intent.putExtra("mail_id",data.get(0).get("id"));
					intent.putExtra("mail_type","R");
					startActivity(intent);
				}
			}
		});

		forward.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String networkStatus= NetworkUtil.getConnectivityStatusString(context);
				if(networkStatus.equals("Not connected to Internet")) {
					customVariablesAndMethod.Connect_to_Internet_Msg(context);
				} else {
					Intent intent=new Intent(context,CreateMail1.class);
					intent.putExtra("mail_id",data.get(0).get("id"));
					intent.putExtra("mail_type","F");
					startActivity(intent);
				}
			}
		});

	}

	private void PopulateMail_History(String mail_id){
		//Start of call to service

		HashMap<String, String> request = new HashMap<>();
		request.put("sCompanyFolder", myCboDbHelper.getCompanyCode());
		request.put("iPaId", "" + Custom_Variables_And_Method.PA_ID);
		request.put("sMailType","");
		request.put("iId",mail_id);


		ArrayList<Integer> tables = new ArrayList<>();
		tables.add(0);
		tables.add(1);

		progress1.setMessage("Please Wait.. \n Fetching mails");
		progress1.setCancelable(false);
		progress1.show();


		new CboServices(this, mHandler).customMethodForAllServices(request, "MailPopulate", MESSAGE_INTERNET_Mail, tables);

		//End of call to service
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
				String table0 = result.getString("Tables1");
				JSONArray jsonArray1 = new JSONArray(table0);
				ArrayList<Map<String, String>> data1 = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray1.length(); i++) {
					JSONObject object = jsonArray1.getJSONObject(i);

					Map<String,String>datanum=new HashMap<String,String>();
					datanum.put("from",object.getString("FROM_PA_NAME"));
					datanum.put("from_id","0");//object.getString("FROM_PA_ID"));
					datanum.put("time",object.getString("MAIL_TIME"));
					datanum.put("sub", object.getString("TO_PA_NAME"));
					datanum.put("date",object.getString("FWD_DATE"));
					datanum.put("mail_id","");// object.getString("ID"));
					datanum.put("id","");//  object.getString("ID"));
					datanum.put("category", object.getString("CC"));

					datanum.put("FILE_NAME","");// object.getString("FILE_NAME"));
					datanum.put("REMARK", object.getString("REPLY_REMARK"));
					datanum.put("IS_READ","0");//object.getString("IS_READ"));
					data1.add(datanum);

				}

				if (!data1.isEmpty()){
					//data=myCboDbHelper.get_Mail("i","");
					mylist.setVisibility(View.VISIBLE);
					MailDetails_Adapter outboxMail_adapter=null;
					outboxMail_adapter=new MailDetails_Adapter(Inbox_Msg.this,data1);
					mylist.setAdapter(outboxMail_adapter);

				}else {
					mylist.setVisibility(View.GONE);
				}


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
	public void onBackPressed(){
		 finish();
		 super.onBackPressed();
	 }



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item != null) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
