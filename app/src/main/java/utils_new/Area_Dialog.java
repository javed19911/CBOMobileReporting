package utils_new;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import services.CboServices;
import utils.adapterutils.Dcr_Workwith_Adapter;
import utils.adapterutils.Dcr_Workwith_Model;
import utils.adapterutils.SpinnerModel;

/**
 * Created by pc24 on 28/11/2017.
 */

public class Area_Dialog {
        //extends AlertDialog {

    Handler h1;
    Integer response_code;
    Bundle Msg;



    ProgressBar progess;



    private ListView mylist;
    private EditText filter;
    ResultSet rs;
    private Custom_Variables_And_Method customVariablesAndMethod;
    private String mr_id1,mr_id2,mr_id3,mr_id4,mr_id5,mr_id6,mr_id7,mr_id8;
    private CBO_DB_Helper cbohelp;
    private ArrayList<String>data,data1;
    private StringBuilder sb,sb2;
    String mr_id="";
    Context context;
    private List<Dcr_Workwith_Model> list=new ArrayList<Dcr_Workwith_Model>();

    private Dcr_Workwith_Model[]TitleName;
    private ArrayList<Dcr_Workwith_Model>array_sort;
    private int textlength=0;
    private  static final int MESSAGE_INTERNET_AREA=1;
    private String[] selected_list;

    Dialog dialog;

    /*private ArrayList<String>getMrId()
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
    }
*/
    private void setMrids()
    {
        String[] selected_list=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_with_id","").replace("+",",").split(",");

        int mr_size=selected_list.length;
        //int mr_size=getMrId().size();
        if(mr_size==1)
        {
            mr_id1=selected_list[0];
            mr_id2="0";
            mr_id3="0";
            mr_id4="0";
            mr_id5="0";
            mr_id6="0";
            mr_id7="0";
            mr_id8="0";
        }
        else if(mr_size==2)
        {
            mr_id1=selected_list[0];
            mr_id2=selected_list[1];
            mr_id3="0";
            mr_id4="0";
            mr_id5="0";
            mr_id6="0";
            mr_id7="0";
            mr_id8="0";
        }
        else if(mr_size==3)
        {
            mr_id1=selected_list[0];
            mr_id2=selected_list[1];
            mr_id3=selected_list[2];
            mr_id4="0";
            mr_id5="0";
            mr_id6="0";
            mr_id7="0";
            mr_id8="0";
        }
        else if(mr_size==4)
        {
            mr_id1=selected_list[0];
            mr_id2=selected_list[1];
            mr_id3=selected_list[2];
            mr_id4=selected_list[3];
            mr_id5="0";
            mr_id6="0";
            mr_id7="0";
            mr_id8="0";
        }
        else if(mr_size==5)
        {
            mr_id1=selected_list[0];
            mr_id2=selected_list[1];
            mr_id3=selected_list[2];
            mr_id4=selected_list[3];
            mr_id5=selected_list[4];
            mr_id6="0";
            mr_id7="0";
            mr_id8="0";
        }
        else if(mr_size==6)
        {
            mr_id1=selected_list[0];
            mr_id2=selected_list[1];
            mr_id3=selected_list[2];
            mr_id4=selected_list[3];
            mr_id5=selected_list[4];
            mr_id6=selected_list[5];
            mr_id7="0";
            mr_id8="0";
        }
        else if(mr_size==7)
        {
            mr_id1=selected_list[0];
            mr_id2=selected_list[1];
            mr_id3=selected_list[2];
            mr_id4=selected_list[3];
            mr_id5=selected_list[4];
            mr_id6=selected_list[5];
            mr_id7=selected_list[6];
            mr_id8="0";
        }
        else if(mr_size>7)
        {
            mr_id1=selected_list[0];
            mr_id2=selected_list[1];
            mr_id3=selected_list[2];
            mr_id4=selected_list[3];
            mr_id5=selected_list[4];
            mr_id6=selected_list[5];
            mr_id7=selected_list[6];
            mr_id8=selected_list[7];
        }
    }


    public Area_Dialog(@NonNull Context context, Handler hh, Bundle Msg, Integer response_code) {
        //super(context);
        this.context = context;
        h1=hh;
        this.response_code=response_code;
        this.Msg=Msg;
    }

    public void show() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dcr_area, null, false);

        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.White_new);
        window.setGravity(Gravity.CENTER);

        TextView textView =(TextView) view.findViewById(R.id.hadder_text_1);
        textView.setText( Msg.getString("header"));

        mylist=(ListView) view.findViewById(R.id.dcr_area_list);
        Button done = (Button) view.findViewById(R.id.dcr_area_save);
        filter=(EditText) view.findViewById(R.id.myfilter);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp=new CBO_DB_Helper(context);
        data=new ArrayList<String>();
        data1=new ArrayList<String>();
        sb=new StringBuilder();
        sb2=new StringBuilder();

        progess=(ProgressBar)view.findViewById(R.id.progess);

        String sAllYn = Msg.getString("sAllYn");
        Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DATE");

        setMrids();

        selected_list=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"area_name").replace("+",",").split(",");

        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cbohelp.getCompanyCode());
        request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
        request.put("iMR_ID1", mr_id1);
        request.put("iMR_ID2", mr_id2);
        request.put("iMR_ID3", mr_id3);
        request.put("iMR_ID4", mr_id4);
        request.put("iMR_ID5", mr_id5);
        request.put("iMR_ID6", mr_id6);
        request.put("iMR_ID7", mr_id7);
        request.put("iMR_ID8", mr_id8);
        request.put("sWorkType", Custom_Variables_And_Method.work_val);
        request.put("sDcrDate", Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT);
        request.put("iDivertYn", sAllYn);

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        progess.setVisibility(View.VISIBLE);
       /* progress1.setMessage("Please Wait.. \n Fetching Area");
        progress1.setCancelable(false);
        progress1.show();*/

        new CboServices(context, mHandler).customMethodForAllServices(request, "DCRAREADDL_2", MESSAGE_INTERNET_AREA, tables);

        //End of call to service

        Custom_Variables_And_Method.work_with_area_id="";


       /* InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(filter, InputMethodManager.SHOW_IMPLICIT);*/

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                textlength = filter.getText().length();
                //getDoctor(PA_ID).clear_2();
                array_sort.clear();
                for (Dcr_Workwith_Model aTitleName : TitleName) {
                    if (textlength <= aTitleName.getName().length()) {

                        if (aTitleName.getName().toLowerCase().contains(filter.getText().toString().toLowerCase().trim())) {
                            array_sort.add(aTitleName);
                        }
                    }
                }
                mylist.setAdapter(new Dcr_Workwith_Adapter((Activity) context,array_sort,selected_list));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        assert done != null;
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
                    sb2.append(data1.get(i)).append("+");
                    sb.append(data.get(i)).append(",");

                }


                Bundle i = new Bundle();
                i.putString("area", sb.toString());
                i.putString("area_name", sb2.toString());

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"area_name", sb2.toString());
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"area_id", sb.toString());
                //setResult(RESULT_OK, i);
                threadMsg(i);
                dialog.dismiss();

            }
        });
        dialog.show();

    }

  //  @Override
 /*   protected void onCreate(Bundle savedInstanceState) {

      *//*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dcr_area);
        getWindow().setBackgroundDrawable(null);*//*

      *//*  Window window = getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.90), (int) (size.y * 0.75));//WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
*//*
       // getWindow().setSoftInputMode(
               // WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
        textView.setText( Msg.getString("header"));

        mylist=(ListView)findViewById(R.id.dcr_area_list);
        Button done = (Button) findViewById(R.id.dcr_area_save);
        filter=(EditText)findViewById(R.id.myfilter);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp=new CBO_DB_Helper(context);
        data=new ArrayList<String>();
        data1=new ArrayList<String>();
        sb=new StringBuilder();
        sb2=new StringBuilder();

        progess=(ProgressBar)findViewById(R.id.progess);

        String sAllYn = Msg.getString("sAllYn");
        Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DATE");

        setMrids();

        selected_list=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"area_name").replace("+",",").split(",");

        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cbohelp.getCompanyCode());
        request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
        request.put("iMR_ID1", mr_id1);
        request.put("iMR_ID2", mr_id2);
        request.put("iMR_ID3", mr_id3);
        request.put("iMR_ID4", mr_id4);
        request.put("iMR_ID5", mr_id5);
        request.put("iMR_ID6", mr_id6);
        request.put("iMR_ID7", mr_id7);
        request.put("iMR_ID8", mr_id8);
        request.put("sWorkType", Custom_Variables_And_Method.work_val);
        request.put("sDcrDate", Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT);
        request.put("iDivertYn", sAllYn);

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        progess.setVisibility(View.VISIBLE);
       *//* progress1.setMessage("Please Wait.. \n Fetching Area");
        progress1.setCancelable(false);
        progress1.show();*//*

        new CboServices(context, mHandler).customMethodForAllServices(request, "DCRAREADDL_2", MESSAGE_INTERNET_AREA, tables);

        //End of call to service

        Custom_Variables_And_Method.work_with_area_id="";


       *//* InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(filter, InputMethodManager.SHOW_IMPLICIT);*//*

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                textlength = filter.getText().length();
                //getDoctor(PA_ID).clear_2();
                array_sort.clear();
                for (Dcr_Workwith_Model aTitleName : TitleName) {
                    if (textlength <= aTitleName.getName().length()) {

                        if (aTitleName.getName().toLowerCase().contains(filter.getText().toString().toLowerCase().trim())) {
                            array_sort.add(aTitleName);
                        }
                    }
                }
                mylist.setAdapter(new Dcr_Workwith_Adapter((Activity) context,array_sort,selected_list));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        assert done != null;
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
                    sb2.append(data1.get(i)).append("+");
                    sb.append(data.get(i)).append(",");

                }


                Bundle i = new Bundle();
                i.putString("area", sb.toString());
                i.putString("area_name", sb2.toString());

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"area_name", sb2.toString());
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"area_id", sb.toString());
                //setResult(RESULT_OK, i);
                threadMsg(i);
                dismiss();

            }
        });
    }*/

   /* @Override
    protected void onStart() {
        super.onStart();
        *//* InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*//*
    }*/

    private void threadMsg(Bundle Msg) {
        Message msgObj = h1.obtainMessage(response_code);
       /* Bundle b = new Bundle();
        b.putString("Error",Msg);*/
        msgObj.setData(Msg);
        h1.sendMessage(msgObj);
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_AREA:
                    if ((null != msg.getData())) {

                        parser_area(msg.getData());

                    }
                    break;
                case 99:
                    progess.setVisibility(View.GONE);
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    progess.setVisibility(View.GONE);

            }
        }
    };

    public void parser_area(Bundle result) {
        if (result!=null ) {

            try {

                ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
                newlist.add(new SpinnerModel("--Select--", ""));

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    list.add(new Dcr_Workwith_Model(c.getString("AREA"),""+i));

                }
                TitleName = new Dcr_Workwith_Model[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    TitleName[i] = list.get(i);
                }

                array_sort = new ArrayList<Dcr_Workwith_Model>(Arrays.asList(TitleName));
                ArrayAdapter<Dcr_Workwith_Model> adapter = new Dcr_Workwith_Adapter((Activity) context, array_sort, selected_list);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                mylist.setAdapter(adapter);

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",context.getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progess.setVisibility(View.GONE);

    }

}
