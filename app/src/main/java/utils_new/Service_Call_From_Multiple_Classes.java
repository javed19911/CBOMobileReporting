package utils_new;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.FinalSubmitDcr_new;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import services.CboServices;
import utils.CBOUtils.SystemArchitecture;

/**
 * Created by pc24 on 21/12/2017.
 */

public class Service_Call_From_Multiple_Classes {
    Integer response_code;
    Handler mHandler;
    ProgressDialog progress1;
    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cbo_helper;

    private  static final int MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL=1,MESSAGE_INTERNET_SEND_FCM_= 2;

     public void DownloadAll(Context context,Handler mHandler, final Integer response_code){

         this.response_code=response_code;
         this.mHandler = mHandler;
         this.context = context;
         progress1 = new ProgressDialog(context);
         customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
         new SystemArchitecture(context).getDEVICE_ID(context);
         Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
         cbo_helper = new CBO_DB_Helper(context);

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cbo_helper.getCompanyCode());
        request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
        request.put("sDcrId",Custom_Variables_And_Method.DCR_ID);
        request.put("sRouteYn", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"root_needed"));
        request.put("sGCM_TOKEN", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"GCMToken"));
        request.put("sMobileId", SystemArchitecture.COMPLETE_DEVICE_INFO);
        request.put("sVersion", Custom_Variables_And_Method.VERSION);

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);
        tables.add(1);
        tables.add(2);
        tables.add(3);
        tables.add(4);
        tables.add(5);
        tables.add(6);
        tables.add(7);
        tables.add(8);
        tables.add(9);
        tables.add(10);
        tables.add(11);

        progress1.setMessage("Please Wait..\n" +
                " Fetching your Utilitis for the day");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(context,hh).customMethodForAllServices(request,"DCRCOMMIT_DOWNLOADALL",MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL,tables);

        //End of call to service

    }


    public void SendFCMOnCall(Context context,Handler mHandler, final Integer response_code,String DocType,String Id,String latlong){


        this.response_code=response_code;
        this.mHandler = mHandler;
        this.context = context;
        progress1 = new ProgressDialog(context);
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
        cbo_helper = new CBO_DB_Helper(context);

        if(!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "FCMHITCALLYN","").contains(DocType)){
            threadMsg("Ok");
            return;
        }
        if (latlong.equals("")){
            latlong = cbo_helper.getLatLong(DocType,Id);
        }

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cbo_helper.getCompanyCode());
        request.put("sPA_ID",  customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"work_with_id","0,")+"0"  );
        request.put("sMessage","[{\"msgtyp\":\"CALL\"},{\"tilte\":\""+ Id +"\"},{\"msg\":\""+ DocType +"\"},{\"url\":\""+ latlong +"\"},{\"logo\":\"\"}]");
        request.put("iDESIG_ID", "");
        request.put("iKEY", "");


        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(context,hh).customMethodForAllServices(request,"GCM_MessagePush_Domain",MESSAGE_INTERNET_SEND_FCM_,tables);
        //End of call to service

    }


    private void threadMsg(String msg) {
        Message msgObj = mHandler.obtainMessage(response_code);
        Bundle b = new Bundle();
        b.putString("msg",msg);
        msgObj.setData(b);
        mHandler.sendMessage(msgObj);
    }


    private final Handler hh = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_DCRCOMMIT_DOWNLOADALL:

                    if ((null != msg.getData())) {

                        parser_DCRCOMMIT_DOWNLOADALL(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_SEND_FCM_:

                    if ((null != msg.getData())) {

                        parser_FCM(msg.getData());

                    }
                    break;
                case 99:
                    if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                    }
                    break;
                default:
                    if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}

            }
        }
    };


    public void parser_DCRCOMMIT_DOWNLOADALL(Bundle result) {

        if (result!=null ) {

            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject one = jsonArray1.getJSONObject(0);

                String MyDaType = one.getString("DA_TYPE");
                String da_val="0";
                Float rate = Float.parseFloat(one.getString("FARE_RATE"));
                Float kms = Float.parseFloat(one.getString("KM"));

                if (MyDaType.equals("L")) {
                    da_val=one.getString("DA_L_RATE");
                } else if (MyDaType.equals("EX") || MyDaType.equals("EXS")) {
                    da_val=one.getString("DA_EX_RATE");
                } else if (MyDaType.equals("NSD") || MyDaType.equals("NS")) {
                    da_val=one.getString("DA_NS_RATE");
                }
                String distance_val="0";
                if (MyDaType.equals("EX") || MyDaType.equals("NSD")) {
                    distance_val="" + (kms * rate * 2);

                } else {
                    distance_val="" + (kms * rate);
                }

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DA_TYPE",MyDaType);
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"da_val",da_val);
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"distance_val",distance_val);

                String table1 = result.getString("Tables1");
                cbo_helper.delete_phdoctor();
                JSONArray jsonArray2 = new JSONArray(table1);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);
                    cbo_helper.insert_phdoctor(c.getInt("DR_ID"), c.getString("DR_NAME"), "", "",c.getInt("SPL_ID"),c.getString("LASTCALL"),
                            c.getString("CLASS"), c.getString("PANE_TYPE"),c.getString("POTENCY_AMT"),
                            c.getString("ITEM_NAME"), c.getString("ITEM_POB"), c.getString("ITEM_SALE"),c.getString("AREA"),c.getString("DR_LAT_LONG")
                            , c.getString("FREQ"),c.getString("NO_VISITED") , c.getString("DR_LAT_LONG2"),c.getString("DR_LAT_LONG3"),c.getString("COLORYN")
                            ,c.getString("CRM_COUNT"),c.getString("DRCAPM_GROUP"),c.getString("SHOWYN"));

                }

                String table2 = result.getString("Tables2");
                cbo_helper.deleteChemist();
                JSONArray jsonArray3 = new JSONArray(table2);
                for (int i = 0; i < jsonArray3.length(); i++) {
                    JSONObject c = jsonArray3.getJSONObject(i);
                    cbo_helper.insert_Chemist(c.getInt("CHEM_ID"), c.getString("CHEM_NAME"),
                            "", "",c.getString("LAST_VISIT_DATE"),c.getString("DR_LAT_LONG") ,
                            c.getString("DR_LAT_LONG2"),c.getString("DR_LAT_LONG3"),c.getString("SHOWYN"));

                }

                String table3 = result.getString("Tables3");
                JSONArray jsonArray4 = new JSONArray(table3);
                cbo_helper.deleteDcrAppraisal();
                for (int i = 0; i < jsonArray4.length(); i++) {
                    JSONObject c = jsonArray4.getJSONObject(i);
                    cbo_helper.setDcrAppraisal(c.getString("PA_ID"), c.getString("PA_NAME"),c.getString("DR_CALL"), c.getString("DR_AVG"),c.getString("CHEM_CALL"), c.getString("CHEM_AVG"), "0", "", "", "", "", "","");

                }

                String table4 = result.getString("Tables4");
                JSONArray jsonArray5 = new JSONArray(table4);
                cbo_helper.delete_phdoctoritem();
                for (int b = 0; b<jsonArray5.length();b++){
                    JSONObject jasonObj2 = jsonArray5.getJSONObject(b);
                    cbo_helper.insertDoctorData(jasonObj2.getInt("DR_ID"), jasonObj2.getInt("ITEM_ID"),jasonObj2.getString("ITEM_NAME"));
                }

                String table5 = result.getString("Tables5");
                JSONArray jsonArray6 = new JSONArray(table5);
                cbo_helper.delete_Doctor_Call_Remark();
                for (int b = 0; b<jsonArray6.length();b++){
                    JSONObject jasonObj2 = jsonArray6.getJSONObject(b);
                    cbo_helper.insertDoctorCallRemark( jasonObj2.getString("PA_ID"),jasonObj2.getString("PA_NAME"));
                }


                String table6 = result.getString("Tables6");
                JSONArray jsonArray7 = new JSONArray(table6);
                cbo_helper.delete_phparty();
                for (int b = 0; b<jsonArray7.length();b++){
                    JSONObject jasonObj2 = jsonArray7.getJSONObject(b);
                    cbo_helper.insert_phparty(jasonObj2.getInt("PA_ID"), jasonObj2.getString("PA_NAME"),
                            jasonObj2.getInt("DESIG_ID"), jasonObj2.getString("CATEGORY"),
                            jasonObj2.getInt("HQ_ID"), jasonObj2.getString("PA_LAT_LONG"),
                            jasonObj2.getString("PA_LAT_LONG2"), jasonObj2.getString("PA_LAT_LONG3")
                            ,jasonObj2.getString("SHOWYN"));
                }

                String table7 = result.getString("Tables7");
                JSONArray jsonArray8 = new JSONArray(table7);
                cbo_helper.delete_phdairy();
                for (int b = 0; b<jsonArray8.length();b++){
                    JSONObject jasonObj2 = jsonArray8.getJSONObject(b);
                    cbo_helper.insert_phdairy(jasonObj2.getInt("ID"), jasonObj2.getString("DAIRY_NAME"),jasonObj2.getString("DOC_TYPE"),
                            "", jasonObj2.getString("DAIRY_LAT_LONG"),jasonObj2.getString("DAIRY_LAT_LONG2"),jasonObj2.getString("DAIRY_LAT_LONG3"));
                }


                String table8 = result.getString("Tables8");
                JSONArray jsonArray9 = new JSONArray(table8);
                cbo_helper.delete_phdairy_person();
                for (int b = 0; b<jsonArray9.length();b++){
                    JSONObject jasonObj2 = jsonArray9.getJSONObject(b);
                    cbo_helper.insert_phdairy_person( jasonObj2.getInt("DAIRY_ID"),jasonObj2.getInt("PERSON_ID"),jasonObj2.getString("PERSON_NAME"));
                }

                String table9 = result.getString("Tables9");
                JSONArray jsonArray10 = new JSONArray(table9);
                cbo_helper.delete_phdairy_reason();
                for (int b = 0; b<jsonArray10.length();b++){
                    JSONObject jasonObj2 = jsonArray10.getJSONObject(b);
                    cbo_helper.insert_phdairy_reason( jasonObj2.getInt("PA_ID"),jasonObj2.getString("PA_NAME"));
                }

                String table10 = result.getString("Tables10");
                JSONArray jsonArray11 = new JSONArray(table10);
                cbo_helper.delete_Item_Stock();
                for (int b = 0; b<jsonArray11.length();b++){
                    JSONObject jasonObj2 = jsonArray11.getJSONObject(b);
                    cbo_helper.insert_Item_Stock( jasonObj2.getString("ITEM_ID"),jasonObj2.getInt("STOCK_QTY"));
                }

                String table11 = result.getString("Tables11");
                JSONArray jsonArray12 = new JSONArray(table11);
                cbo_helper.delete_STk_Item();
                for (int b = 0; b<jsonArray12.length();b++){
                    JSONObject jasonObj2 = jsonArray12.getJSONObject(b);
                    cbo_helper.insert_STk_Item( jasonObj2.getString("STK_ID"),jasonObj2.getString("ITEM_ID"),jasonObj2.getString("RATE"));
                }

                /*switch (work_type_Selected){
                    case "w":
                        finish();
                        break;
                    case "l":
                        Intent intent = new Intent(getApplicationContext(), FinalSubmitDcr_new.class);
                        startActivity(intent);
                        break;
                    case "n":
                        setReultForNonWork();
                        break;
                }
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"work_type_Selected",work_type_Selected);*/
                threadMsg("OK");
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",context.getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
        //Log.d("MYAPP", "objects are1: " + result);


    }



    public void parser_FCM(Bundle result) {

        if (result!=null ) {

            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject one = jsonArray1.getJSONObject(0);
                String MyDaType = one.getString("DCRID");
                threadMsg("OK");
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(context,"Missing field error",context.getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        if(progress1 != null && progress1.isShowing()){ progress1.dismiss();}
        //Log.d("MYAPP", "objects are1: " + result);


    }

}
