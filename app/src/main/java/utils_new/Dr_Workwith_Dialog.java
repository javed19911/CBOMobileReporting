package utils_new;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import java.util.ArrayList;
import utils.adapterutils.Dcr_Workwith_Adapter;
import utils.adapterutils.Dcr_Workwith_Model;

public class Dr_Workwith_Dialog {


    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    Handler h1;
    Integer response_code;
    Bundle Msg;


    ListView mylist;
    Button done;
    EditText filter;
    Dialog dialog;

    int PA_ID;
    ArrayAdapter<Dcr_Workwith_Model> adapter;

    ArrayList<String>data,data1;
    StringBuilder sb,sb2;
    CBO_DB_Helper cbohelp;
    ArrayList<Dcr_Workwith_Model>list=new ArrayList<Dcr_Workwith_Model>();
    String DAIRY_ID = "";

    public Dr_Workwith_Dialog(@NonNull Context context, Handler hh, Bundle Msg, Integer response_code) {
        this.context = context;
        h1=hh;
        this.response_code=response_code;
        this.Msg=Msg;
    }


    public void show() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.doctor_workwith, null, false);

        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.White_new);
        window.setGravity(Gravity.CENTER);

        mylist=(ListView) view.findViewById(R.id.dr_workwith_list);
        done=(Button) view.findViewById(R.id.dr_workwith_save);
        PA_ID= Custom_Variables_And_Method.PA_ID;
        data=new ArrayList<String>();
        data1=new ArrayList<String>();
        sb=new StringBuilder();
        sb2=new StringBuilder();

        DAIRY_ID = Msg!= null ? Msg.getString("DAIRY_ID"):"";
        new Doback().execute(PA_ID);

        done.setOnClickListener(new View.OnClickListener() {

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
               /* Intent i=new Intent();
                i.putExtra("workwith_id", sb.toString());
                i.putExtra("workwith_name", sb2.toString());
                setResult(RESULT_OK, i);
                finish();*/

                Bundle i = new Bundle();
                i.putString("workwith_id", sb.toString());
                i.putString("workwith_name", sb2.toString());

                threadMsg(i);
                dialog.dismiss();

            }
        });

        dialog.show();

    }


    class Doback extends AsyncTask<Integer, String, ArrayList<Dcr_Workwith_Model>> {
        ProgressDialog pd;

        @Override
        protected ArrayList<Dcr_Workwith_Model> doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            list.clear();
            try{
                if(DAIRY_ID.equals("")) {
                    cbohelp=new CBO_DB_Helper(context);
                    Cursor c=cbohelp.getDR_Workwith();
                    if(c.moveToFirst())
                    {
                        do
                        {
                            list.add(new Dcr_Workwith_Model(c.getString(c.getColumnIndex("workwith")),c.getString(c.getColumnIndex("wwid"))));
                        }	while(c.moveToNext());

                    }
                    cbohelp.close();
                } else {
                    cbohelp=new CBO_DB_Helper(context);
                    Cursor c=cbohelp.get_phdairy_person(DAIRY_ID);
                    if(c.moveToFirst())
                    {
                        do
                        {
                            list.add(new Dcr_Workwith_Model(c.getString(c.getColumnIndex("PERSON_NAME")),c.getString(c.getColumnIndex("PERSON_ID"))));
                        }	while(c.moveToNext());

                    }
                    cbohelp.close();
			    		 /*Statement smt=mycon.connection().createStatement();
			    		 rs=smt.executeQuery("WORKWITH "+PA_ID+",'','','','','','',''");
			    		 while(rs.next())
			    		 {
			    			 display_item_list.add(new Dcr_Workwith_Model(rs.getString("PA_NAME"),rs.getString("PA_ID")));
			    		 }
						 rs.close();
						 smt.close();*/
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
            pd = new ProgressDialog(context);
            pd.setTitle("CBO");
            pd.setMessage("Processing......."+"\n"+"please wait");
            pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Dcr_Workwith_Model> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            String[] selected_list={};
            adapter=new Dcr_Workwith_Adapter((Activity) context,result,selected_list,false);
            mylist.setAdapter(adapter);
            pd.dismiss();


        }
    }

    private void threadMsg(Bundle Msg) {
        Message msgObj = h1.obtainMessage(response_code);
       /* Bundle b = new Bundle();
        b.putString("Error",Msg);*/
        msgObj.setData(Msg);
        h1.sendMessage(msgObj);
    }

}
