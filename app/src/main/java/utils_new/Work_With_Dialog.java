package utils_new;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import services.ServiceHandler;
import utils.adapterutils.Dcr_Workwith_Adapter;
import utils.adapterutils.Dcr_Workwith_Model;

/**
 * Created by pc24 on 28/11/2017.
 */

public class Work_With_Dialog {
        //extends AlertDialog {

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
    CBO_DB_Helper cbohelp;
    ArrayAdapter<Dcr_Workwith_Model> adapter;
    ArrayList<String>data,data1,data2,data3;
    StringBuilder sb,sb2,sb3,sb4;
    ArrayList<Dcr_Workwith_Model>list=new ArrayList<Dcr_Workwith_Model>();
    ServiceHandler serviceHandler;

    Dcr_Workwith_Model[]TitleName;
    ArrayList<Dcr_Workwith_Model>array_sort;
    int textlength=0;
    String dcr_date;
    String[] selected_list,independent_list;
    ProgressBar progess;

    /*public Work_With_Dialog(@NonNull Context context, String Msg) {
        super(context);
        this.context = context;
    }
    public Work_With_Dialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }*/

    public Work_With_Dialog(@NonNull Context context, Handler hh, Bundle Msg, Integer response_code) {
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

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dcr_workwith);
//
//        getWindow().setBackgroundDrawable(null);
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dcr_workwith, null, false);

        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.White_new);
        window.setGravity(Gravity.CENTER);

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp=new CBO_DB_Helper(context);
        serviceHandler=new ServiceHandler(context);

        mylist=(ListView) view.findViewById(R.id.workwith_list);
        done=(Button) view.findViewById(R.id.workwith_save);
        filter=(EditText) view.findViewById(R.id.myfilter);
        progess=(ProgressBar) view.findViewById(R.id.progess);

        Toolbar toolbar =(Toolbar) view.findViewById(R.id.toolbar_hadder);
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }

        if (!Custom_Variables_And_Method.pub_desig_id.equals(""+1)) {
            LinearLayout header = (LinearLayout) view.findViewById(R.id.header);
            header.setVisibility(View.VISIBLE);
        }

        TextView textView =(TextView) view.findViewById(R.id.hadder_text_1);
        textView.setText( Msg.getString("header"));

        PA_ID= Custom_Variables_And_Method.PA_ID;

        data=new ArrayList<String>();
        data1=new ArrayList<String>();
        data2=new ArrayList<String>();
        data3=new ArrayList<String>();

        sb=new StringBuilder();
        sb2=new StringBuilder();
        sb3=new StringBuilder();
        sb4=new StringBuilder();

        dcr_date=Msg.getString("sDCR_DATE");

        selected_list=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_with_name").replace("+",",").split(",");
        independent_list=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_with_individual_name").replace("+",",").split(",");

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
                mylist.setAdapter(new Dcr_Workwith_Adapter((Activity) context,array_sort,selected_list,independent_list));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cbohelp.deleteDRWorkWith();
                for(int i=0;i<list.size();i++){
                    boolean check=list.get(i).isSelected();
                    boolean check1=list.get(i).isindependentSelected();
                    if(check){
                        data.add(list.get(i).getId());
                        data1.add(list.get(i).getName());
                    }else if(check1){
                        data2.add(list.get(i).getId());
                        data3.add(list.get(i).getName());
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

                cbohelp.insertDrWorkWith("Independent", ""+PA_ID);

                for(int i=0;i<data2.size();i++) {
                    sb2.append(data3.get(i)).append(",");
                    sb.append(data2.get(i)).append(",");

                    sb3.append(data3.get(i)).append(",");
                    sb4.append(data2.get(i)).append(","); //id inde
                }

                Bundle i = new Bundle();
                i.putString("workwith_id", sb.toString());
                i.putString("workwith_name", sb2.toString());
                i.putString("work_with_individual_name", sb3.toString());
                i.putString("work_with_individual_id", sb4.toString());

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"work_with_name", sb2.toString());
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"work_with_individual_name", sb3.toString());
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"work_with_id", sb.toString());
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"work_with_individual_id", sb4.toString());
                //setResult(RESULT_OK, i);
                threadMsg(i);
               // dismiss();
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

    class Doback extends AsyncTask<Integer, String, ArrayList<Dcr_Workwith_Model>> {
        ProgressDialog pd;
        String result;

        @Override
        protected ArrayList<Dcr_Workwith_Model> doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            list.clear();
            try{
                result=serviceHandler.getResponse_WORKWITH(cbohelp.getCompanyCode(),""+Custom_Variables_And_Method.PA_ID,""+dcr_date,
                        "","","","","","",Msg.getString("DIVERTWWYN"),Msg.getString("sWorking_Type"));
                if((result!=null)&&(!result.contains("ERROR")))
                {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray rows = jsonObject.getJSONArray("Tables0");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject c = rows.getJSONObject(i);
                        list.add(new Dcr_Workwith_Model(c.getString("PA_NAME"),c.getString("PA_ID"),
                                c.getString("RESIGYN"), c.getString("LEAVEYN"),
                                Msg.getString("PlanType").equals("p")? c.getString("WORKWITHYN") : "0"));
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
            /*pd = new ProgressDialog(context);
            pd.setMessage("Processing......."+"\n"+"please wait");
            pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();*/
            progess.setVisibility(View.VISIBLE);
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
                    adapter=new Dcr_Workwith_Adapter((Activity) context,array_sort,selected_list,independent_list);
                    //adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    mylist.setAdapter(adapter);

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
            progess.setVisibility(View.GONE);


        }
    }

}
