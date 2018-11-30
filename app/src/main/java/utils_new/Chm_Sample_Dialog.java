package utils_new;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import services.CboServices;
import services.Up_Dwn_interface;
import utils.adapterutils.GiftModel;
import utils.adapterutils.MyAdapter;
import utils.adapterutils.RCPA_Adapter;
import utils.networkUtil.NetworkUtil;
import utils.upload_download;

public class Chm_Sample_Dialog  implements Up_Dwn_interface {

    Handler h1;
    Integer response_code;
    Bundle Msg;

    ListView mylist;
    Button save;
    Custom_Variables_And_Method customVariablesAndMethod;
    int PA_ID = 0;
    ResultSet rs;
    ArrayAdapter<GiftModel> adapter;
    List<GiftModel> list = new ArrayList<GiftModel>();
    ArrayList<String> id,score,sample,rate,item_list;//data1, data2, data3, data5;
    StringBuilder sb3, sb2, sb4, sb5,item_list_string;
    double mainval = 0.0;
    CBO_DB_Helper cbohelp;
    String callFromRcpa = "", drId, chemId, rcpaDate;
    Context context;
    String sample_name="",sample_pob="",sample_sample="";
    String sample_name_previous="",sample_pob_previous="",sample_sample_previous="";
    EditText search;

    Dialog dialog;
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;


    public Chm_Sample_Dialog(@NonNull Context context, Handler hh, Bundle Msg, Integer response_code) {

        this.context = context;
        h1=hh;
        this.response_code=response_code;
        this.Msg=Msg;
    }


    public void Show() {
        //this.onNameChange = onNameChange;
         dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chm_sample, null, false);

        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.White_new);
        window.setGravity(Gravity.CENTER);



        TextView hader_text =(TextView) view.findViewById(R.id.hadder_text_1);
        hader_text.setText("Chemsit Sample");
       // hader_text.setText( Msg.getString("header"));


       // progess=(ProgressBar)view.findViewById(R.id.progess);



        if (Msg != null) {

            callFromRcpa = Msg.getString("intent_fromRcpaCAll");
            assert callFromRcpa != null;
            if (callFromRcpa.equals("intent_fromRcpaCAll")) {
                drId = Msg.getString("dr_id");
                chemId = Msg.getString("chm_id");
                rcpaDate = Msg.getString("dateMMDDYY");
            }else{

                if (callFromRcpa.contains("Select")){
                    hader_text.setText(callFromRcpa );
                }else if (!callFromRcpa.equals("Chem")){
                    hader_text.setText(callFromRcpa + " Sample");
                }
                sample_name = Msg.getString("sample_name");
                sample_pob = Msg.getString("sample_pob");
                sample_sample = Msg.getString("sample_sample");

                sample_name_previous = Msg.getString("sample_name_previous");
                sample_pob_previous = Msg.getString("sample_pob_previous");
                sample_sample_previous = Msg.getString("sample_sample_previous");
            }


        } else {
            callFromRcpa = "intent not found";

        }



        progress1 = new ProgressDialog(context);
        search= (EditText) view.findViewById(R.id.search);
        mylist = (ListView) view.findViewById(R.id.chm_sample_list);
        mylist.setItemsCanFocus(true);
        mylist.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        save = (Button) view.findViewById(R.id.chm_sample_save);

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();


        cbohelp = new CBO_DB_Helper(context);
        PA_ID = Custom_Variables_And_Method.PA_ID;

        id = new ArrayList<String>();
        score = new ArrayList<String>();
        sample = new ArrayList<String>();
        rate = new ArrayList<String>();
        item_list = new ArrayList<String>();
        sb3 = new StringBuilder();
        sb2 = new StringBuilder();
        sb4 = new StringBuilder();
        sb5 = new StringBuilder();
        item_list_string = new StringBuilder();
        mainval += 0.0;






        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                for (int l = 0; l < list.size(); l++) {
                    if (!search.getText().toString().equals("") &&  search.getText().length() <= list.get(l).getName().length()) {
                        if (list.get(l).getName().toLowerCase().contains(search.getText().toString().toLowerCase().trim())) {
                            //mylist.smoothScrollToPosition(l);
                            mylist.smoothScrollToPositionFromTop(l,l,500);
                            for (int j = l; j < list.size(); j++) {
                                if (search.getText().length() <= list.get(j).getName().length()) {
                                    if (list.get(j).getName().toLowerCase().contains(search.getText().toString().toLowerCase().trim())) {
                                        list.get(j).setHighlight(true);
                                    }else{
                                        list.get(j).setHighlight(false);
                                    }
                                }else{
                                    list.get(j).setHighlight(false);
                                }
                            }
                            break;
                        }else{
                            list.get(l).setHighlight(false);
                        }
                    }else{
                        list.get(l).setHighlight(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (callFromRcpa.equals("intent_fromRcpaCAll")) {
                    StringBuilder sbItemId = new StringBuilder();
                    StringBuilder sbQty = new StringBuilder();
                    StringBuilder sbProduct = new StringBuilder();
                    StringBuilder sbRemark = new StringBuilder();

                    int j = 0;
                    for (int i = 0; i < list.size(); i++) {
                        boolean check = list.get(i).isSelected();
                        if (check) {
                            if (j == 0) {
                                if (list.get(i).getScore()!=null && !list.get(i).getScore().equals("")){
                                    sbItemId.append(list.get(i).getId());
                                    sbQty.append(list.get(i).getScore());
                                    sbProduct.append(list.get(i).getSample());
                                    sbRemark.append(list.get(i).getRate());
                                }
                            } else {
                                if (list.get(i).getScore()!=null && !list.get(i).getScore().equals("")){
                                    sbItemId.append("^").append(list.get(i).getId());
                                    sbQty.append("^").append(list.get(i).getScore());
                                    sbProduct.append("^").append(list.get(i).getSample());
                                    sbRemark.append("^").append(list.get(i).getRate());
                                }
                            }

                            j = j + 1;
                        } else {

                            id.remove(check);
                            score.remove(check);
                            rate.remove(check);
                            mainval = 0.0;
                        }


                    }
                    if (sbItemId.length() > 0) {

                        //Start of call to service

                        HashMap<String,String> request=new HashMap<>();
                        request.put("sCompanyFolder",cbohelp.getCompanyCode());
                        request.put("iPA_ID", ""+Custom_Variables_And_Method.PA_ID);
                        request.put("iDCR_ID",Custom_Variables_And_Method.DCR_ID);
                        request.put("iDR_ID", drId);
                        request.put("iCHEM_ID",chemId);

                        request.put("sMONTH","");
                        request.put("iITEM_ID", sbItemId.toString());
                        request.put("sITEM_NAME",sbProduct.toString());
                        request.put("sQTY", sbQty.toString());
                        request.put("sREMARK",sbRemark.toString());

                        ArrayList<Integer> tables=new ArrayList<>();
                        tables.add(0);


                        progress1.setMessage("Please Wait..\n" +
                                " Fetching data");
                        progress1.setCancelable(false);
                        progress1.show();

                        //progess.setVisibility(View.VISIBLE);

                        new CboServices(context,mHandler).customMethodForAllServices(request,"RCPA_COMMIT",MESSAGE_INTERNET,tables);

                        //End of call to service

                        //String myDcrId = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_ID");
                        //cbohelp.insertDataRcpa("" + myDcrId, "" + Custom_Variables_And_Method.PA_ID, drId, chemId, rcpaDate, sbItemId.toString(), sbQty.toString());

                        //startActivity(new Intent(getApplicationContext(), ViewPager_2016.class));
                    }


                    //customVariablesAndMethod.msgBox(context,"Successfully Submitted....");
                } else {

                    for (int i = 0; i < list.size(); i++) {
                        boolean check = list.get(i).isSelected();
                        if (check) {

                            id.add(list.get(i).getId());
                            item_list.add(list.get(i).getName());

                            if (list.get(i).getScore()==null || list.get(i).getScore().equals("")){
                                score.add("0");
                            }else {
                                score.add(list.get(i).getScore());
                            }

                            if (list.get(i).getSample()==null || list.get(i).getSample().equals("")){
                                sample.add("0");
                            }else {
                                sample.add(list.get(i).getSample());
                            }

                            // data2.add(list.get(i).getSample());
                            rate.add(list.get(i).getRate());

                           /* ArrayList<String> mychmid = cbohelp.getChemistIdForsample();

                            if (!(mychmid.contains(Custom_Variables_And_Method.CHEMIST_ID))) {


                                cbohelp.insertChemistSample(Custom_Variables_And_Method.CHEMIST_ID, list.get(i).getId(), list.get(i).getName(), list.get(i).getScore(), list.get(i).getSample());
                                Log.e("^^^^^^^^", Custom_Variables_And_Method.CHEMIST_ID + "," + list.get(i).getId() + "," + list.get(i).getName() + "," + list.get(i).getScore());
                            }*/

                        } else {
                            item_list.remove(check);
                            id.remove(check);
                            score.remove(check);
                            sample.remove(check);
                            rate.remove(check);
                            mainval = 0.0;
                        }

                    }


                    for (int i = 0; i < id.size(); i++) {

                        sb3.append(id.get(i)).append(",");
                        sb2.append(score.get(i)).append(",");
                        sb5.append(sample.get(i)).append(",");
                        item_list_string.append(item_list.get(i)).append(",");
                        sb4.append(rate.get(i)).append(",");
                        //sb4.append(data3.get(i)).append(",");
                    }

                    try {
                        String rateval = sb4.toString();

                        if (rate.isEmpty() || id.isEmpty()) {
                            mainval = 0.0;
                            rateval += "" + 0.0 + ",";
                        }
                        String rateval1[] = rateval.split(",");

                        Double[] intarray = new Double[rateval1.length];
                        for (int i = 0; i < rateval1.length; i++) {
                            intarray[i] = Double.parseDouble(rateval1[i]);
                        }


                        String pobval = sb2.toString();
                        if (rate.isEmpty() || id.isEmpty()) {
                            mainval = 0.0;
                            pobval += "" + 0.0 + ",";
                        }
                        String sampleQty = sb5.toString();
                        if (sample.isEmpty() || id.isEmpty()) {
                            mainval = 0.0;
                            pobval += "" + 0.0 + ",";
                        }
                        String pobval1[] = pobval.split(",");
                        Double pobarray[] = new Double[pobval1.length];
                        for (int i = 0; i < pobval1.length; i++) {
                            pobarray[i] = Double.parseDouble(pobval1[i]);
                        }
                        if (rate.isEmpty() || id.isEmpty()) {

                            mainval += 0.0;

                        }
                        for (int i = 0; i < intarray.length; i++) {
                            if (rate.isEmpty() || id.isEmpty()) {

                                pobval += "" + 0.0 + ",";
                                rateval += "" + 0.0 + ",";
                                pobarray[i] = 0.0;
                                intarray[i] = 0.0;
                                mainval += 0.0;
                            }

                            try {
                                mainval += (pobarray[i] * intarray[i]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    } catch (Exception e) {

                    }



                    Bundle i = new Bundle();
                    i.putString("val", sb3.toString());
                    i.putString("val2", sb2.toString());
                    i.putString("sampleQty", sb5.toString());
                    i.putDouble("resultpob", mainval);
                    i.putString("resultRate", sb4.toString());
                    i.putString("resultList", item_list_string.toString());

                    threadMsg(i);
                    dialog.dismiss();


                }
            }
        });


        new Doback().execute(PA_ID,0);

    }



    @Override
    public void onDownloadComplete() {
        new Doback().execute(PA_ID,1);
    }

    class Doback extends AsyncTask<Integer, String, List<GiftModel>> {
        ProgressDialog pd;
        int who = 0;

        @Override
        protected List<GiftModel> doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            list.clear();
            String ItemIdNotIn = "0";
            who = params[1];
            Cursor c = cbohelp.getAllProducts(ItemIdNotIn);
            if (c.moveToFirst()) {
                do {
                    if (callFromRcpa.equals("intent_fromRcpaCAll")) {
                        list.add(new GiftModel(c.getString(c.getColumnIndex("item_name")), c.getString(c.getColumnIndex("item_id")), "",
                                c.getInt(c.getColumnIndex("STOCK_QTY")), c.getInt(c.getColumnIndex("BALANCE"))));
                    }else {
                        list.add(new GiftModel(c.getString(c.getColumnIndex("item_name")), c.getString(c.getColumnIndex("item_id")),
                                c.getString(c.getColumnIndex("stk_rate")),c.getInt(c.getColumnIndex("STOCK_QTY")), c.getInt(c.getColumnIndex("BALANCE"))));
                    }

                } while (c.moveToNext());
            }
            return list;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //progess.setVisibility(View.VISIBLE);

            pd = new ProgressDialog(context);
            pd.setTitle("CBO");
            pd.setMessage("Processing......." + "\n" + "please wait");
            pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(List<GiftModel> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (callFromRcpa.equals("intent_fromRcpaCAll")) {
                adapter = new RCPA_Adapter((Activity) context, result);
            }else {
                adapter = new MyAdapter((Activity) context, result);
            }

            pd.dismiss();


            if (adapter.getCount() != 0) {
                mylist.setAdapter(adapter);
                String[] sample_name1= sample_name.split(",");
                String[] sample_qty1= sample_sample.split(",");
                String[] sample_pob1= sample_pob.split(",");

                for (int i=0;i<sample_name1.length;i++){
                    for (int j=0;j<list.size();j++) {
                        if (sample_name1[i].equals(list.get(j).getName())) {
                            list.get(j).setScore(sample_pob1[i]);
                            list.get(j).setSample(sample_qty1[i]);

                           // list.get(j).setBalance( list.get(j).getBalance() + Integer.parseInt(sample_qty1[i]));
                        }
                    }
                }

                String[] sample_name1_previous= sample_name_previous.split(",");
                String[] sample_qty1_previous= sample_sample_previous.split(",");

                for (int i=0;i<sample_name1_previous.length;i++){
                    for (int j=0;j<list.size();j++) {
                        if (sample_name1_previous[i].equals(list.get(j).getName())) {
                            list.get(j).setBalance( list.get(j).getBalance() + Integer.parseInt(sample_qty1_previous[i]));
                        }
                    }
                }
                dialog.show();

            } else if(who == 0){
                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
                builder1.setTitle("CBO");
                builder1.setIcon(R.drawable.alert1);
                builder1.setMessage(" No Data In List.." + "\n" + "Please Download Data.....");
                builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NetworkUtil networkUtil = new NetworkUtil(context);
                        if (!networkUtil.internetConneted(context)) {
                            customVariablesAndMethod.Connect_to_Internet_Msg(context);
                        } else {

                            new upload_download(context,Chm_Sample_Dialog.this);
                        }

                    }
                });
                builder1.show();
            }

            //progess.setVisibility(View.GONE);


        }
    }

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
                case MESSAGE_INTERNET:
                    if ((null != msg.getData())) {

                        parser_worktype(msg.getData());

                    }
                    progress1.dismiss();
                    dialog.dismiss();
                    break;
                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };

    public void parser_worktype(Bundle result) {
        if (result!=null ) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    //rptName.add(new SpinnerModel(c.getString("PA_NAME"),c.getString("PA_ID")));
                    //cbohelp.updateLatLong(lat_long,dr_id,doc_type);
                }
                customVariablesAndMethod.msgBox(context,"Successfully Submitted....");
                //dil

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",context.getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);


    }

}
