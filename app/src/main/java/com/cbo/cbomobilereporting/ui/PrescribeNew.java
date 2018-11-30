package com.cbo.cbomobilereporting.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.flurry.android.FlurryAgent;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utils.ExceptionHandler;
import utils.MyConnection;
import utils.adapterutils.PobModel;
import utils.adapterutils.PrescribeAdapter;
import utils_new.Custom_Variables_And_Method;


public class PrescribeNew extends Activity {
    ListView mylist;
    Button save;

    int PA_ID=0;
    ResultSet rs;
    CBO_DB_Helper cbohelp;
    String item_id="",item_name="",item_qty="",item_pob="";
    ArrayAdapter<PobModel>adapter;
    List<PobModel>list=new ArrayList<PobModel>();
    ArrayList<String>data1,data2,data3,data4,data5;
    StringBuilder itemid,itemname,itemqty,itempob;
    StringBuilder sb2,sb3,sb4,sb5;
    double mainval=0.0;
    Context context;

    public void getData (){

        adapter=new PrescribeAdapter(this,new DoBack().doInBackground(PA_ID));

        if(adapter.getCount()!=0)
        {
            mylist.setAdapter(adapter);
        }
        else
        {
            AlertDialog.Builder builder1=new AlertDialog.Builder(context);
            builder1.setTitle("Empty List..");
            builder1.setMessage(" No Data In List.."+"\n"+"Please Download Data From Constants Page.....");
            builder1.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                    //Intent i=new Intent(getApplicationContext(),MyUtil.class);
                    //startActivity(i);
                }
            });
            builder1.show();
        }

    }


    public void onCreate(Bundle b){
        super.onCreate(b);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        final boolean customTitleSupported =
                requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.prescribed);
        FlurryAgent.logEvent("Dr_Sample");
        if (customTitleSupported) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                    R.layout.mytitlebar);
            getWindow().setBackgroundDrawableResource(R.color.titlebackgroundcolor);
        }
        final TextView myTitleText = (TextView) findViewById(R.id.left_text);
        if (myTitleText != null) {
            myTitleText.setText("PRESCRIBES"+"- Dr. "+ Custom_Variables_And_Method.DR_NAME);
            myTitleText.setGravity(Gravity.LEFT);
        }
        context = PrescribeNew.this;
        mylist=(ListView)findViewById(R.id.list_prescribe);
        save=(Button)findViewById(R.id.submit_Prescribedr);
        cbohelp=new CBO_DB_Helper(getApplicationContext());

        PA_ID=Custom_Variables_And_Method.PA_ID;
        data1=new ArrayList<String>();
        data2=new ArrayList<String>();
        data3=new ArrayList<String>();
        data4=new ArrayList<String>();
        data5=new ArrayList<String>();
        sb2=new StringBuilder();
        sb3=new StringBuilder();
        sb4=new StringBuilder();
        sb5=new StringBuilder();
        itemid=new StringBuilder();
        itemname=new StringBuilder();
        itemqty=new StringBuilder();
        itempob=new StringBuilder();

        new DoBack().execute(PA_ID);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String Qty="";
                String POB="";
                String Rate="";
                //String visual_id="";
                for(int i=0;i<list.size();i++){
                    boolean check=list.get(i).isSelected();

                    if ((!list.get(i).getPob().toString().equals(""))&&(!list.get(i).getPob().toString().equals("0")))


                    {
                        data1.add(list.get(i).getId());
                        data2.add(list.get(i).getScore());
                        data5.add(list.get(i).getName());
                        data3.add(list.get(i).getPob());
                        data4.add(list.get(i).getRate());
                        Qty=list.get(i).getScore();
                        POB=list.get(i).getPob();
                        Rate=list.get(i).getRate();
                        if(Qty.equals(""))
                        {
                            Qty="0";
                        }
                        if(POB.equals(""))
                        {
                            POB="0";
                        }
                        if(Rate.equals(""))
                        {
                            Rate="0";
                        }
                        ArrayList<String>doclist=cbohelp.getDoctorList();
                        ArrayList<String>docitems=cbohelp.getDoctorAllItems();
                        ArrayList<String>visual_id=cbohelp.getDoctorVisualId();
                        ArrayList<Integer>actlist=getdoclist();

                        //if(doclist.contains(MyConnection.DR_ID)&&(docitems.contains(list.get(i).getId())))
                        if(actlist.contains(Integer.parseInt(Custom_Variables_And_Method.DR_ID)))
                        {
                            if(visual_id.contains("1"))
                            {
                                //cbohelp.updateVisuals(MyConnection.DR_ID, list.get(i).getId(), list.get(i).getName(), Qty, POB, Rate,"1");
                                cbohelp.insertdataPrescribe(Custom_Variables_And_Method.DR_ID, list.get(i).getId(), list.get(i).getName(), Qty, POB, Rate, "1");
                            }
                            else
                            {
                                //cbohelp.updateData(MyConnection.DR_ID, list.get(i).getId(), list.get(i).getName(), Qty, POB, Rate,"0");
                                Log.e("no updation in sample", "no update");
                                cbohelp.insertdataPrescribe(Custom_Variables_And_Method.DR_ID, list.get(i).getId(), list.get(i).getName(), Qty, POB, Rate, "0");
                            }

                        }

                        else
                        {
                            cbohelp.insertdataPrescribe(Custom_Variables_And_Method.DR_ID, list.get(i).getId(), list.get(i).getName(), Qty, POB, Rate, "0");
                        }
                    }
                    else {
                        data1.remove(check);
                        data2.remove(check);
                        data3.remove(check);
                        data4.remove(check);
                    }


                }

                for(int k=0;k<data1.size();k++)
                {
                    itemid.append(data1.get(k)).append(",");
                    itemname.append(data5.get(k)).append(",");
                    itemqty.append(data2.get(k)).append(",");
                    itempob.append(data3.get(k)).append(",");
                }
                item_id=itemid.toString();
                item_name=itemname.toString();
                item_qty=itemqty.toString();
                item_pob=itempob.toString();

                cbohelp.close();

                Intent i=new Intent();
                i.putExtra("val", "");
                i.putExtra("val2", "");
                i.putExtra("val3", "");
                i.putExtra("resultpob", mainval);
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
        i.putExtra("val2", "");
        i.putExtra("val3", "");
        setResult(RESULT_OK, i);
        super.onBackPressed();
    }

    public ArrayList<Integer>getdoclist()
    {
        ArrayList<Integer>myno=new ArrayList<Integer>();
        ArrayList<String>doclist=cbohelp.getDoctorList();
        for(int i=0;i<doclist.size();i++)
        {
            myno.add(Integer.parseInt(doclist.get(i)));
        }
        return myno;
    }

    class DoBack extends AsyncTask<Integer,String,List<PobModel>>{
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setTitle("CBO");
            pd.setMessage("Processing.....");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected List<PobModel> doInBackground(Integer... params) {

            list.clear();
            int i=0;
            String itemIdnotIn = "0";
            Cursor c1 = cbohelp.getSampleProductsDcrDrItems(Custom_Variables_And_Method.DCR_ID);
            if (c1.moveToFirst()){
                do {
                    list.add(new PobModel(c1.getString(c1.getColumnIndex("item_name")),c1.getString(c1.getColumnIndex("item_id")),c1.getString(c1.getColumnIndex("item_id"))));
                    list.get(i).setSelected(true);
                    itemIdnotIn =itemIdnotIn+c1.getString(c1.getColumnIndex("item_id"));
                    i = i+1;
                }while (c1.moveToNext());

            }
            Cursor c=cbohelp.getAllProducts(itemIdnotIn);

            if (c.moveToFirst()){
                do{
                    list.add(new PobModel(c.getString(c.getColumnIndex("item_name")),c.getString(c.getColumnIndex("item_id")),c.getString(c.getColumnIndex("stk_rate"))));
                }while(c.moveToNext());
            }
            cbohelp.close();



            return list;
        }

        @Override
        protected void onPostExecute(List<PobModel> pobModels) {
            super.onPostExecute(pobModels);

            if ((pobModels !=null)||(pobModels.size() !=0)){
                getData();
                pd.dismiss();
            }
        }
    }

}
