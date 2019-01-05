package utils_new;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.DcrRoot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import async.CBORootTask;
import services.ServiceHandler;
import services.TaskListener;
import utils.adapterutils.DcrRootAdapter;
import utils.adapterutils.Dcr_Workwith_Adapter;
import utils.adapterutils.Dcr_Workwith_Model;
import utils.adapterutils.RootModel;

/**
 * Created by pc24 on 28/11/2017.
 */

public class Route_Dialog extends AlertDialog {

    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    Handler h1;
    Integer response_code;
    Bundle Msg;
    ServiceHandler myServiceHandler;

    ListView mylist;
    Button done;


    int PA_ID;
    String mr_id1,mr_id2,mr_id3;
    DcrRootAdapter adapter;
    CBO_DB_Helper cbohelp;
    ArrayList<String>data,data1;
    StringBuilder sb,sb2;
    String sAllYn;
    ArrayList<RootModel>list=new ArrayList<RootModel>();

    String name;
    ProgressBar progess;



    public Route_Dialog(@NonNull Context context, Handler hh, Bundle Msg, Integer response_code) {
        super(context);
        this.context = context;
        h1=hh;
        this.response_code=response_code;
        this.Msg=Msg;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dcr_root);

        getWindow().setBackgroundDrawable(null);

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp=new CBO_DB_Helper(context);
        myServiceHandler=new ServiceHandler(context);

        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
        textView.setText( Msg.getString("header"));

        mylist=(ListView)findViewById(R.id.dcr_root_list);
        done=(Button)findViewById(R.id.dcr_root_save);

        progess=(ProgressBar)findViewById(R.id.progess);

        PA_ID= Custom_Variables_And_Method.PA_ID;
        data=new ArrayList<String>();
        data1=new ArrayList<String>();

        sb=new StringBuilder();
        sb2=new StringBuilder();


        sAllYn=Msg.getString("sAllYn");

        setRootDataToUI((Activity) context);
        Custom_Variables_And_Method.work_with_area_id="";
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (adapter != null) {
                    Bundle i = new Bundle();
                    i.putString("route_name", adapter.name);
                    i.putString("route_id", adapter.id);
                    threadMsg(i);
                }
                dismiss();

            }
        });
    }

    private void threadMsg(Bundle Msg) {
        Message msgObj = h1.obtainMessage(response_code);
       /* Bundle b = new Bundle();
        b.putString("Error",Msg);*/
        msgObj.setData(Msg);
        h1.sendMessage(msgObj);
    }

   /* public ArrayList<String>getMrId()
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
    }*/

    private void setMrids()
    {

        String[] selected_list=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_with_id","").replace("+",",").split(",");

        int mr_size=selected_list.length;
        if(mr_size==1)
        {
            mr_id1=selected_list[0];
            mr_id2="0";
            mr_id3="0";
        }
        else if(mr_size==2)
        {
            mr_id1=selected_list[0];
            mr_id2=selected_list[1];
            mr_id3="0";
        }
        else if(mr_size>2)
        {
            mr_id1=selected_list[0];
            mr_id2=selected_list[1];
            mr_id3=selected_list[2];
        }
        else
        {
            mr_id1="0";
            mr_id2="0";
            mr_id3="0";
        }
    }


    public void setRootDataToUI( final Activity context){
        final CBORootTask rootTask=new CBORootTask(context);
        //getMrId();
        setMrids();
        rootTask.setListener(new TaskListener<String>() {
            @Override
            public void onStarted() {
                try {
                    progess.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinished(String result) {
                progess.setVisibility(View.GONE);
                if ((result == null) || (result.contains("[ERROR]"))) {
                    customVariablesAndMethod.msgBox(context,result);
                } else {
                    list=rootTask.setDataToRootList(result,list);
                    adapter=new DcrRootAdapter(context,list );
                    mylist.setAdapter(adapter);
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


}
