package com.cbo.cbomobilereporting.ui_new.utilities_activities;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.cbo.cbomobilereporting.BuildConfig;
import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.Show_Sample;
import com.flurry.android.FlurryAgent;
import com.uenics.javed.CBOLibrary.Response;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utils.adapterutils.DocSampleModel;
import utils.adapterutils.LazyAdapter;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.Service_Call_From_Multiple_Classes;

public class DocPhotos extends AppCompatActivity {
	ListView mylist;
	Custom_Variables_And_Method customVariablesAndMethod;
	Context context;
	int PA_ID=0;
	ResultSet rs;
	String id="";
	String Dr_Id="";
	CBO_DB_Helper cbohelp;
	String rowid="";
	ArrayAdapter<DocSampleModel>adapter;
	ArrayList<String>sample_id=new ArrayList<String>();
	ArrayList<String>sample_name=new ArrayList<String>();
	List<DocSampleModel>list=new ArrayList<DocSampleModel>();
	AlertDialog.Builder builder1;
	AlertDialog dialog;
	int who=1;
    private String sample_name_Stored="",sample_pob="",sample_sample="";


    public void onCreate(Bundle b){
		super.onCreate(b);
		//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.doc_photos);
        FlurryAgent.logEvent("Doc Photos");

		androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder);
		TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);
		setSupportActionBar(toolbar);
		hader_text.setText("Visual Ads");

		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
		}

		mylist=(ListView)findViewById(R.id.doc_sample_list);
		context=this;
		customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
		cbohelp=new CBO_DB_Helper(getApplicationContext());
		PA_ID= Custom_Variables_And_Method.PA_ID;
		Dr_Id = getIntent().getStringExtra("dr_id");
		who=getIntent().getIntExtra("who",1);
        if (who==0){
            Bundle getExtra = getIntent().getExtras();
            if (getExtra != null) {
                sample_name_Stored = getExtra.getString("sample_name");
                sample_pob = getExtra.getString("sample_pob");
                sample_sample = getExtra.getString("sample_sample");
            }
        }


		String splcode;
		splcode="";
		Cursor c=cbohelp.getDoctorSpecialityCodeByDrId(Dr_Id);
		if(c.moveToFirst())
		{
			do
			{
				splcode=c.getString(c.getColumnIndex("remark"));
			}while(c.moveToNext());
		}
		Custom_Variables_And_Method.pub_doctor_spl_code=splcode;

		adapter=new LazyAdapter(this, bindVisualAid());

		if(adapter.getCount()!=0) {
			mylist.setAdapter(adapter);
		} else {
			builder1=new AlertDialog.Builder(DocPhotos.this);
			builder1.setTitle("Empty List..");
			builder1.setMessage(" No Data In List.."+"\n"+"Please Download Data .....");
			builder1.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_BACK)
						onBackPressed();
					return false;
				}
			});
			builder1.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
					//Intent i=new Intent(getApplicationContext(),MyUtil.class);
					//startActivity(i);
					//finish();
					/*NetworkUtil networkUtil = new NetworkUtil(getApplicationContext());
					if (!networkUtil.internetConneted(getApplicationContext())) {
						customVariablesAndMethod.Connect_to_Internet_Msg(context);
					} else {
						startActivityForResult(new Intent(getApplicationContext(), VisualAid_Download.class),1);

					}*/

					new Service_Call_From_Multiple_Classes().getListForLocal(context, new Response() {
						@Override
						public void onSuccess(Bundle bundle) {
							customVariablesAndMethod.msgBox(context, "Data Downloded Sucessfully...");

						}

						@Override
						public void onError(String message, String description) {
							AppAlert.getInstance().getAlert(context,message,description);
						}
					});
				}
				});
			dialog = builder1.create();
			dialog.show();
		}


		
		mylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id3) {
				// TODO Auto-generated method stub
				id=((TextView)v.findViewById(R.id.dcr_workwith_id)).getText().toString();
				String item_name=((TextView)v.findViewById(R.id.dcr_workwith_name)).getText().toString();
				rowid=((TextView)v.findViewById(R.id.list_row_id)).getText().toString();
				String visual_pdf=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"VISUALAIDPDFYN");
				File file =new File(Environment.getExternalStorageDirectory(), "cbo/product/"+item_name+"/"+"Menu"+".html");
				if(file.exists()){

					/*Intent i = new Intent(context, CustomWebView.class);
					i.putExtra("A_TP1", file.toString());
					i.putExtra("Menu_code", "");
					i.putExtra("Title", item_name);
					startActivity(i);*/

					MyCustumApplication.getInstance().LoadURL(item_name,file.toString());

					/*Intent browserIntent = new Intent(Intent.ACTION_VIEW);
					browserIntent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "cbo/Catalog/"+"Menu"+".html")), "text/html");
					//browserIntent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "cbo/"+"test"+".html")), "text/html");
					context.startActivity(browserIntent);*/
				}else if(visual_pdf.equals("Y"))
				{
					String ext=list.get(position).get_file_ext();
					File path1 = new File(Environment.getExternalStorageDirectory(), "cbo/product/"+id+"_"+ Custom_Variables_And_Method.pub_doctor_spl_code +ext);
					File path2 = new File(Environment.getExternalStorageDirectory(), "cbo/product/"+id+ext);
					Uri path;
					if(path1.exists())
					{
						if (ext.equals(".pdf")){

							if (Build.VERSION.SDK_INT >= 24) {
								path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",path2);
							} else {
								path = Uri.fromFile(path2);
							}

							// Setting the intent for pdf reader
							Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
							pdfIntent.setDataAndType(path, "application/pdf");
							pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
							pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

							try {
								startActivity(pdfIntent);
							} catch (ActivityNotFoundException e) {
								customVariablesAndMethod.msgBox(context,"PDF Reader application is not installed in your device");
							}
							/*path = Uri.fromFile(path1);
							try {
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setDataAndType(path, "application/pdf");
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							} catch (ActivityNotFoundException e) {
								customVariablesAndMethod.msgBox(context,"PDF Reader application is not installed in your device");
							}*/
						}else {
							/*Intent i = new Intent(context, CustomWebView.class);
							i.putExtra("A_TP1", path1.toString());
							i.putExtra("Menu_code", "");
							i.putExtra("Title", item_name);
							startActivity(i);*/
							MyCustumApplication.getInstance().LoadURL(item_name,path1.toString());
						}

					}else if(path2.exists()){

						if (ext.equals(".pdf")){

							if (Build.VERSION.SDK_INT >= 24) {
								path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",path2);
							} else {
								path = Uri.fromFile(path2);
							}

							// Setting the intent for pdf reader
							Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
							pdfIntent.setDataAndType(path, "application/pdf");
							pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
							pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

							try {
								startActivity(pdfIntent);
							} catch (ActivityNotFoundException e) {
								customVariablesAndMethod.msgBox(context,"PDF Reader application is not installed in your device");
							}
							/*path = Uri.fromFile(path2);
							try {
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setDataAndType(path, "application/pdf");
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							} catch (ActivityNotFoundException e) {
								customVariablesAndMethod.msgBox(context,"PDF Reader application is not installed in your device");
							}*/
						}else {
							/*Intent i = new Intent(context, CustomWebView.class);
							i.putExtra("A_TP1", path2.toString());
							i.putExtra("Menu_code", "");
							i.putExtra("Title", item_name);
							startActivity(i);*/
							MyCustumApplication.getInstance().LoadURL(item_name,path2.toString());
						}

					}else{
						customVariablesAndMethod.msgBox(context,"File does not exists");
					}

				}else{
					Intent i=new Intent(getApplicationContext(),Show_Sample.class);
					i.putExtra("samid", sample_id);
					i.putExtra("sam_name", sample_name);
                    i.putExtra("dr_id",Dr_Id);
					i.putExtra("myid", id);
					i.putExtra("title", item_name);
					i.putExtra("rowid", position);
					i.putExtra("who", who);

                    i.putExtra("sample_name_Stored", sample_name_Stored);
                    i.putExtra("sample_pob", sample_pob);
                    i.putExtra("sample_sample", sample_sample);

					startActivity(i);
				}
				//mycon.msgBox(rowid);
				
			}
		});
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
			case 1:
				finish();
				startActivity(new Intent(DocPhotos.this, DocPhotos.class));
				break;
			default:
				super.onActivityResult(requestCode, resultCode, data);
		}
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

	@Override
	public void onBackPressed() {
		if (dialog!=null && dialog.isShowing()) {
			dialog.dismiss();
		}
		if(who==0) {
			Intent i = new Intent();
			i.putExtra("val", "");
			i.putExtra("val2", "");
			i.putExtra("val3", "");
			i.putExtra("resultpob", "0");
			setResult(RESULT_OK, i);
		}
		finish();
		super.onBackPressed();

	}
	
	private List<DocSampleModel> bindVisualAid()
	{
    	list.clear();
    	String ItemIdNotIn ="'0'";

		String path = Environment.getExternalStorageDirectory().toString()+"/cbo/product";
		File directory = new File(path);
		File[] files = directory.listFiles();


    	cbohelp=new CBO_DB_Helper(getApplicationContext());


		int cnt = 0;
		Cursor c = cbohelp.getAllVisualAdd(ItemIdNotIn,"Y");
		//Cursor c=myitem.getSelected();
		//rs=stmt.getResultSet();
		if (c.moveToFirst()) {
			do {
				//Boolean isHighlited=Dr_Item_list.contains(c.getString(c.getColumnIndex("item_id")));
				list.add(new DocSampleModel(c.getString(c.getColumnIndex("item_name")), c.getString(c.getColumnIndex("item_id")), "" + cnt,false));  //Dr_Item_list.contains(c.getString(c.getColumnIndex("item_id")))
				sample_id.add(c.getString(c.getColumnIndex("item_id")));
				sample_name.add(c.getString(c.getColumnIndex("item_name")));
				cnt = cnt + 1;
			} while (c.moveToNext());
		}

		c.close();

		 int cnt3=0;
		 Cursor c3=cbohelp.getphitemSpl(Custom_Variables_And_Method.DOCTOR_SPL_ID );
		int cnt2=0;
		Cursor c1=cbohelp.getSelectedFromDr(Dr_Id);
		//ArrayList<String> Dr_Item_list=new ArrayList<>();
		if(c1.moveToFirst()){
			do{
				//Dr_Item_list.add(""+c1.getInt(c1.getColumnIndex("item_id")));
				list.add(new DocSampleModel(c1.getString(c1.getColumnIndex("item_name")),""+c1.getInt(c1.getColumnIndex("item_id")),""+cnt2,true));
				sample_id.add(""+c1.getInt(c1.getColumnIndex("item_id")));
				sample_name.add(c1.getString(c1.getColumnIndex("item_name")));
				ItemIdNotIn=ItemIdNotIn+",'" + c1.getString(c1.getColumnIndex("item_id")) + "'";
				cnt2 = cnt2+1;
			}while(c1.moveToNext());
		}
		if (c1.moveToFirst() && who==0 && customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"VISUALAID_DRSELITEMYN","N").equals("Y")) {


			/*if(c1.moveToFirst()){
				do{
					list.add(new DocSampleModel(c1.getString(c1.getColumnIndex("item_name")),""+c1.getInt(c1.getColumnIndex("item_id")),""+cnt2,true));
					sample_id.add(""+c1.getInt(c1.getColumnIndex("item_id")));
					sample_name.add(c1.getString(c1.getColumnIndex("item_name")));
					ItemIdNotIn=ItemIdNotIn+"," + c1.getString(c1.getColumnIndex("item_id"));
					cnt2 = cnt2+1;
				}while(c1.moveToNext());
			}*/
		}else if(c3.moveToFirst() && who==0) {
			list.clear();
			 do{
				 list.add(new DocSampleModel(c3.getString(c3.getColumnIndex("item_name")),c3.getString(c3.getColumnIndex("item_id")),""+cnt3,false));
				 sample_id.add(c3.getString(c3.getColumnIndex("item_id")));
				 sample_name.add(c3.getString(c3.getColumnIndex("item_name")));
				 //ItemIdNotIn=ItemIdNotIn+"," + c3.getString(c3.getColumnIndex("item_id"));
				 cnt3 = cnt3+1;
			 }while(c3.moveToNext());

		 } else {


				//cbohelp = new CBO_DB_Helper(getApplicationContext());
				 cnt = 0;
				 c = cbohelp.getAllVisualAdd(ItemIdNotIn,"N");
				//Cursor c=myitem.getSelected();
				//rs=stmt.getResultSet();
				if (c.moveToFirst()) {
					do {
						//Boolean isHighlited=Dr_Item_list.contains(c.getString(c.getColumnIndex("item_id")));
						list.add(new DocSampleModel(c.getString(c.getColumnIndex("item_name")), c.getString(c.getColumnIndex("item_id")), "" + cnt,false));  //Dr_Item_list.contains(c.getString(c.getColumnIndex("item_id")))
						sample_id.add(c.getString(c.getColumnIndex("item_id")));
						sample_name.add(c.getString(c.getColumnIndex("item_name")));
						cnt = cnt + 1;
					} while (c.moveToNext());
				}

				c.close();

		 }
		 c3.close();
		c1.close();

		if (files!=null) {
			for (File file1 : files) {
				String file = file1.toString();
				if (file.contains(".")) {
					String file_name = file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf("."));
					if (file.contains(".") && sample_id.contains(file_name)) {
						for (int j = 0; j < sample_id.size(); j++) {
							if (sample_id.get(j).equals(file_name)) {
								list.get(j).set_file_ext(file.substring(file.lastIndexOf(".")));
								break;
							}
						}
					}
				} else {
					if (sample_name.contains(file1.getName())) {
						for (int j = 0; j < sample_name.size(); j++) {
							if (sample_name.get(j).equals(file1.getName())) {
								list.get(j).set_file_ext(".html");
								break;
							}
						}
					}
				}
			}
		}

		if (who==0) {
			String[] sample_name1 = sample_name_Stored.split(",");
			/*String[] sample_qty1 = sample_sample.split(",");
			String[] sample_pob1 = sample_pob.split(",");*/

			for (int i = 0; i < sample_name1.length; i++) {
				for (int j = 0; j < list.size(); j++) {
					if (sample_name1[i].equals(list.get(j).getName())) {
						list.get(j).set_Checked(true);
					}
				}
			}
			sortList(list);
		}



       	return list;
    }

    private List<DocSampleModel> sortList(List<DocSampleModel> list){
    	int pointer = 0;
		/*sample_id.clear();
		sample_name.clear();*/
		if (!MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("VISUALAID_SELECTEDONLY","").equalsIgnoreCase("Y")) {
			for (int index = 0; index < list.size(); index++) {
				DocSampleModel sampleModel = list.get(index);
				if (sampleModel.isHighlighted()) {
					pointer++;
				} else if (sampleModel.get_Checked() && index != pointer) {
					list.add(pointer, sampleModel);
					sample_id.add(pointer, sampleModel.getId());
					sample_name.add(pointer, sampleModel.getName());
					pointer++;
					list.remove(index + 1);
					sample_id.remove(index + 1);
					sample_name.remove(index + 1);
				}


			}
		}else{
			for (int index = 0; index < list.size(); index++) {
				DocSampleModel sampleModel = list.get(index);
				if (!(sampleModel.isHighlighted() || sampleModel.get_Checked())) {
					list.remove(index );
					sample_id.remove(index);
					sample_name.remove(index--);
				}
			}

		}

    	return list;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item != null) {
			if(who==0) {
				Intent i = new Intent();
				i.putExtra("val", "");
				i.putExtra("val2", "");
				i.putExtra("val3", "");
				i.putExtra("resultpob", "0");
				setResult(RESULT_OK, i);
			}
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	

	}
