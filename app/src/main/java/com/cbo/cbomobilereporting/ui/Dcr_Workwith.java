package com.cbo.cbomobilereporting.ui;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;


import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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


import org.json.JSONArray;
import org.json.JSONObject;

import services.ServiceHandler;
import services.TaskListener;
import utils.adapterutils.Dcr_Workwith_Adapter;
import utils.adapterutils.Dcr_Workwith_Model;
import utils_new.Custom_Variables_And_Method;

public class Dcr_Workwith extends Activity
	{
		ListView mylist;
		Button done;
        EditText filter;
		ResultSet rs;
        Custom_Variables_And_Method customVariablesAndMethod;
        Context context;
		int PA_ID;
		CBO_DB_Helper cbohelp;
		ArrayAdapter<Dcr_Workwith_Model>adapter;
		ArrayList<String>data,data1;
		StringBuilder sb,sb2;
		ArrayList<Dcr_Workwith_Model>list=new ArrayList<Dcr_Workwith_Model>();
        ServiceHandler serviceHandler;

        Dcr_Workwith_Model[]TitleName;
        ArrayList<Dcr_Workwith_Model>array_sort;
        int textlength=0;
		String dcr_date;
		String[] selected_list;
		

		public void onCreate(Bundle b)
		{
			super.onCreate(b);
			//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
			setContentView(R.layout.dcr_workwith);
			mylist=(ListView)findViewById(R.id.workwith_list);
			done=(Button)findViewById(R.id.workwith_save);
            filter=(EditText)findViewById(R.id.myfilter);

            context=this;
            customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
			cbohelp=new CBO_DB_Helper(context);

            serviceHandler=new ServiceHandler(context);
			PA_ID= Custom_Variables_And_Method.PA_ID;
			data=new ArrayList<String>();
			data1=new ArrayList<String>();
			sb=new StringBuilder();
			sb2=new StringBuilder();
			Intent intent=getIntent();
			dcr_date=intent.getStringExtra("sDCR_DATE");

			selected_list=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_with_name").replace("+",",").split(",");
			new Doback().execute(PA_ID);

            filter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    textlength = filter.getText().length();
                    array_sort.clear();
                    for (int i = 0; i < TitleName.length; i++) {
                        if (textlength <= TitleName[i].getName().length()) {

                            if (TitleName[i].getName().toLowerCase().contains(filter.getText().toString().toLowerCase().trim())) {
                                array_sort.add(TitleName[i]);
                            }
                        }
                    }
                    mylist.setAdapter(new Dcr_Workwith_Adapter(Dcr_Workwith.this,array_sort,selected_list));

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
			
			done.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					cbohelp.deleteDRWorkWith();
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
						try{
						
						long val=cbohelp.insertDrWorkWith(data1.get(i), data.get(i));
						Log.e("added dr work with", ""+val);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					
					Intent i=new Intent();
					i.putExtra("workwith_id", sb.toString());
					i.putExtra("workwith_name", sb2.toString());
					
					SharedPreferences.Editor editor=Dcr_Workwith.this.getSharedPreferences(Custom_Variables_And_Method.FMCG_PREFRENCE, MODE_PRIVATE).edit();
					editor.putString("work_with_name", sb2.toString());
					editor.putString("work_with_id", sb.toString());
					editor.commit();

					setResult(RESULT_OK, i);
	    	    	finish();
					
				}
			});
		}


		
		public void onBackPressed(){
			 Intent i=new Intent();
		    	i.putExtra("val", "");
		    	setResult(RESULT_CANCELED, i);
			 super.onBackPressed();
		 }
		
		class Doback extends AsyncTask<Integer, String, ArrayList<Dcr_Workwith_Model>> {
			ProgressDialog pd;
            String result;

			@Override
			protected ArrayList<Dcr_Workwith_Model> doInBackground(Integer... params) {
				// TODO Auto-generated method stub
				list.clear();
		    	try{
                    result=serviceHandler.getResponse_WORKWITH(cbohelp.getCompanyCode(),""+Custom_Variables_And_Method.PA_ID,""+dcr_date,
							"","","","","","","0","");
                    if((result!=null)&&(!result.contains("ERROR")))
                    {

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray rows = jsonObject.getJSONArray("Tables0");
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject c = rows.getJSONObject(i);
                            list.add(new Dcr_Workwith_Model(c.getString("PA_NAME"),c.getString("PA_ID")));
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
				pd = new ProgressDialog(Dcr_Workwith.this);
				pd.setTitle("CBO");
				pd.setMessage("Processing......."+"\n"+"please wait");
				pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
				pd.show();
			}

			@Override
			protected void onPostExecute(ArrayList<Dcr_Workwith_Model> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
                if(result.size()!=0){

                    try {
                        TitleName = new Dcr_Workwith_Model[result.size()];
                        for (int i = 0; i < result.size(); i++) {
                            TitleName[i] = result.get(i);
                        }

                        array_sort = new ArrayList<Dcr_Workwith_Model>(Arrays.asList(TitleName));
                        adapter=new Dcr_Workwith_Adapter(Dcr_Workwith.this,array_sort,selected_list);
                        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                        mylist.setAdapter(adapter);

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
				pd.dismiss();
				
				
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
