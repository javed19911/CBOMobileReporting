package utils_new;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.util.ArrayList;
import java.util.Arrays;

import async.CBORootTask;
import services.ServiceHandler;
import services.TaskListener;
import utils.adapterutils.DcrRootAdapter;
import utils.adapterutils.RootModel;

/**
 * Created by pc24 on 28/11/2017.
 */

public class Route_Dialog {
        //extends AlertDialog {

    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    Handler h1;
    Integer response_code;
    Bundle Msg;
    ServiceHandler myServiceHandler;

    ListView mylist;
    Button done;
    EditText filter;


    int PA_ID;
    String mr_id1,mr_id2,mr_id3;
    DcrRootAdapter adapter;
    CBO_DB_Helper cbohelp;
    ArrayList<String>data,data1;
    StringBuilder sb,sb2;
    String sAllYn;
    Boolean  allowMultipleRoute = false;
    ArrayList<RootModel>list=new ArrayList<RootModel>();

    String name;
    ProgressBar progess;
    int textlength=0;
    RootModel[]TitleName;
    ArrayList<RootModel>array_sort;
    Dialog dialog;


    public Route_Dialog(@NonNull Context context, Handler hh, Bundle Msg, Integer response_code) {
        //super(context);
        this.context = context;
        h1=hh;
        this.response_code=response_code;
        this.Msg=Msg;
    }

    // @Override
    public void show() {
        onCreate(null);
        //super.show();
        /*InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
    }


    /*public Route_Dialog(@NonNull Context context, Handler hh, Bundle Msg, Integer response_code) {
        super(context);
        this.context = context;
        h1=hh;
        this.response_code=response_code;
        this.Msg=Msg;
    }*/

   /* @Override
    public void show() {
        super.show();
    }*/

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dcr_root);

        getWindow().setBackgroundDrawable(null);*/

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dcr_root, null, false);

        ((AppCompatActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.White_new);
        window.setGravity(Gravity.CENTER);

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp=new CBO_DB_Helper(context);
        myServiceHandler=new ServiceHandler(context);

        TextView textView =(TextView) view.findViewById(R.id.hadder_text_1);
        textView.setText( Msg.getString("header"));

        mylist=(ListView) view.findViewById(R.id.dcr_root_list);
        filter=(EditText) view.findViewById(R.id.myfilter);
        done=(Button)view.findViewById(R.id.dcr_root_save);

        progess=(ProgressBar)view.findViewById(R.id.progess);

        PA_ID= Custom_Variables_And_Method.PA_ID;
        data=new ArrayList<String>();
        data1=new ArrayList<String>();

        sb=new StringBuilder();
        sb2=new StringBuilder();


        sAllYn=Msg.getString("sAllYn");
        allowMultipleRoute = Msg.getBoolean("allowMultipleRoute");

        setRootDataToUI((AppCompatActivity) context);
        Custom_Variables_And_Method.work_with_area_id="";


        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                textlength = filter.getText().length();
                array_sort.clear();
                for (int i = 0; i < TitleName.length; i++) {
                    if (textlength <= TitleName[i].getRootName().length()) {

                        if (TitleName[i].getRootName().toLowerCase().contains(filter.getText().toString().toLowerCase().trim())) {
                            array_sort.add(TitleName[i]);
                        }
                    }
                }
                RootModel rootModel = adapter.rootModel;
                adapter = new DcrRootAdapter(context,array_sort ,allowMultipleRoute);
                adapter.rootModel = rootModel;
                mylist.setAdapter( adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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
                dialog.dismiss();

            }
        });

        dialog.show();
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


    public void setRootDataToUI( final AppCompatActivity context){
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
                    TitleName = new RootModel[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        TitleName[i] = list.get(i);
                    }

                    array_sort = new ArrayList<RootModel>(Arrays.asList(TitleName));
                    adapter=new DcrRootAdapter(context,array_sort ,allowMultipleRoute);
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
