package async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import services.ServiceHandler;
import services.TaskListener;
import utils.networkUtil.AppPrefrences;
import utils_new.CustomTextToSpeech;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit on 1/2/2016.
 */
public class CommitTask_New extends AsyncTask<String, String, String> {


    private ServiceHandler mServiceHandler;
    public Context mContext;
    private TaskListener<String> mListener;
    CBO_DB_Helper cbohelp;
    String routeValue;
    /*MyConnection mycon;*/
    Custom_Variables_And_Method customVariablesAndMethod;
    private CBOFinalTask_New cboFinalTask_new;
    MyCustomMethod customMethod;
    Map<String, String> dcr_latCommit = new HashMap<>();
    Map<String, String> dcr_Commititem = new HashMap<>();
    Map<String, String> dcr_Commit_rx = new HashMap<>();
    Map<String, String> dcr_CommitDr = new HashMap<>();
    Map<String, String> dcr_ChemistCommit = new HashMap<>();
    Map<String, String> dcr_StkCommit = new HashMap<>();
    Map<String, String> dcr_CommitDr_Reminder = new HashMap<>();
    Map<String, String> Lat_Long_Reg = new HashMap<>();


    String sb_DCRLATCOMMIT_KM, sb_DCRLATCOMMIT_LOC_LAT, sb_sDCRLATCOMMIT_IN_TIME, sDCRLATCOMMIT_ID, sDCRLATCOMMIT_LOC;
    String sDCRITEM_DR_ID, sDCRITEM_ITEMIDIN, sDCRITEM_ITEM_ID_ARR, sDCRITEM_QTY_ARR, sDCRITEM_ITEM_ID_GIFT_ARR, sDCRITEM_QTY_GIFT_ARR, sDCRITEM_POB_QTY, sDCRITEM_POB_VALUE, sDCRITEM_VISUAL_ARR,sDCRITEM_NOC_ARR;
    String sDCRDR_DR_ID, sDCRDR_WW1, sDCRDR_WW2, sDCRDR_WW3, sDCRDR_LOC, sDCRDR_IN_TIME, sDCRDR_BATTERY_PERCENT, sDCRDR_REMARK, sDCRDR_KM, sDCRDR_SRNO,sDCRDR_FILE,sDCRDR_CALLTYPE;
    String sDCRCHEM_CHEM_ID, sDCRCHEM_POB_QTY, sDCRCHEM_POB_AMT, sDCRCHEM_ITEM_ID_ARR, sDCRCHEM_QTY_ARR, sDCRCHEM_LOC, sDCRCHEM_IN_TIME, sDCRCHEM_SQTY_ARR, sDCRCHEM_ITEM_ID_GIFT_ARR, sDCRCHEM_QTY_GIFT_ARR, sDCRCHEM_BATTERY_PERCENT, sDCRCHEM_KM, sDCRCHEM_SRNO,sDCRCHEM_REMARK,sDCRCHEM_FILE;
    String sDCRSTK_STK_ID, sDCRSTK_POB_QTY, sDCRSTK_POB_AMT, sDCRSTK_ITEM_ID_ARR, sDCRSTK_QTY_ARR, sDCRSTK_LOC, sDCRSTK_IN_TIME, sDCRSTK_SQTY_ARR, sDCRSTK_ITEM_ID_GIFT_ARR, sDCRSTK_QTY_GIFT_ARR, sDCRSTK_BATTERY_PERCENT, sDCRSTK_KM, sDCRSTK_SRNO,sDCRSTK_REMARK,sDCRSTK_FILE;
    String sDCRRC_IN_TIME, sDCRRC_LOC, sDCRRC_DR_ID, sDCRRC_KM, sDCRRC_SRNO,sDCRRC_BATTERY_PERCENT,sDCRRC_REMARK,sDCRRC_FILE;
    String sDCR_DR_RX, sDCR_ITM_RX;
    String sFinalKm;
    String DCS_ID_ARR, LAT_LONG_ARR, DCS_TYPE_ARR, DCS_ADD_ARR, DCS_INDES_ARR;

    public AppPrefrences appPrefrences;

    public CommitTask_New(Activity context) {
        mContext = context;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        customMethod = new MyCustomMethod(context);
        appPrefrences = new AppPrefrences(context);

        cbohelp = new CBO_DB_Helper(context);
        mServiceHandler = new ServiceHandler(context);
        cboFinalTask_new = new CBOFinalTask_New(context);
        // bgDataService = new MyLoctionService(context);
    }


    @Override
    protected String doInBackground(String... params) {
        String response = null;


        try {

            String fmcg_Live_Km = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(mContext,"live_km");


            if (fmcg_Live_Km.equalsIgnoreCase("5") || fmcg_Live_Km.equalsIgnoreCase("Y5")) {


                customMethod.stopAlarm10Sec();
                new CustomTextToSpeech().stopTextToSpeech();
                customMethod.stopAlarm10Minute();
                customMethod.backgroundData();
                routeValue = appPrefrences.getRouteValue(mContext);
                dcr_latCommit = customMethod.dataToServer(null);

            }


            if ((dcr_latCommit.isEmpty()) || (dcr_latCommit.size() == 0)) {

                sb_DCRLATCOMMIT_KM = "";
                sb_DCRLATCOMMIT_LOC_LAT = "";
                sb_sDCRLATCOMMIT_IN_TIME = "";
                sDCRLATCOMMIT_ID = "";
                sDCRLATCOMMIT_LOC = "";
            } else {

                sb_DCRLATCOMMIT_KM = dcr_latCommit.get("sb_DCRLATCOMMIT_KM");
                sb_DCRLATCOMMIT_LOC_LAT = dcr_latCommit.get("sb_DCRLATCOMMIT_LOC_LAT");
                sb_sDCRLATCOMMIT_IN_TIME = dcr_latCommit.get("sb_sDCRLATCOMMIT_IN_TIME");
                sDCRLATCOMMIT_ID = dcr_latCommit.get("sDCRLATCOMMIT_ID");
                sDCRLATCOMMIT_LOC = dcr_latCommit.get("sDCRLATCOMMIT_LOC");
            }

            dcr_Commit_rx = cboFinalTask_new.drRx_Save(null);
            if ((dcr_Commit_rx.isEmpty()) || (dcr_Commit_rx.size() == 0)) {

                sDCR_DR_RX = "";
                sDCR_ITM_RX = "";


            } else {

                sDCR_DR_RX = dcr_Commit_rx.get("sDCRRX_DR_ARR");
                sDCR_ITM_RX = dcr_Commit_rx.get("sDCRRX_ITEMID_ARR");


            }


            dcr_Commititem = cboFinalTask_new.drItemSave(null);
            if ((dcr_Commititem.isEmpty()) || (dcr_Commititem.size() == 0)) {

                sDCRITEM_DR_ID = "";
                sDCRITEM_ITEMIDIN = "";
                sDCRITEM_ITEM_ID_ARR = "";
                sDCRITEM_QTY_ARR = "";
                sDCRITEM_ITEM_ID_GIFT_ARR = "";
                sDCRITEM_QTY_GIFT_ARR = "";
                sDCRITEM_POB_QTY = "";
                sDCRITEM_POB_VALUE = "";
                sDCRITEM_VISUAL_ARR = "";
                sDCRITEM_NOC_ARR = "";

            } else {
                sDCRITEM_DR_ID = dcr_Commititem.get("sb_sDCRITEM_DR_ID");
                sDCRITEM_ITEMIDIN = dcr_Commititem.get("sb_sDCRITEM_ITEMIDIN");
                sDCRITEM_ITEM_ID_ARR = dcr_Commititem.get("sb_sDCRITEM_ITEM_ID_ARR");
                sDCRITEM_QTY_ARR = dcr_Commititem.get("sb_sDCRITEM_QTY_ARR");
                sDCRITEM_ITEM_ID_GIFT_ARR = dcr_Commititem.get("sb_sDCRITEM_ITEM_ID_GIFT_ARR");
                sDCRITEM_QTY_GIFT_ARR = dcr_Commititem.get("sb_sDCRITEM_QTY_GIFT_ARR");
                sDCRITEM_POB_QTY = dcr_Commititem.get("sb_sDCRITEM_POB_QTY");
                sDCRITEM_POB_VALUE = dcr_Commititem.get("sb_sDCRITEM_POB_VALUE");
                sDCRITEM_VISUAL_ARR = dcr_Commititem.get("sb_sDCRITEM_VISUAL_ARR");
                sDCRITEM_NOC_ARR = dcr_Commititem.get("sb_sDCRITEM_NOC_ARR");

            }
            dcr_CommitDr = cboFinalTask_new.dcr_doctorSave(null);
            if ((dcr_CommitDr.isEmpty()) || (dcr_CommitDr.size() == 0)) {
                sDCRDR_DR_ID = "";
                sDCRDR_WW1 = "";
                sDCRDR_WW2 = "";
                sDCRDR_WW3 = "";
                sDCRDR_LOC = "";
                sDCRDR_IN_TIME = "";
                sDCRDR_BATTERY_PERCENT = "";
                sDCRDR_REMARK = "";
                sDCRDR_KM = "";
                sDCRDR_SRNO="";
                sDCRDR_FILE="";
                sDCRDR_CALLTYPE="";
            } else {
                sDCRDR_DR_ID = dcr_CommitDr.get("sb_sDCRDR_DR_ID");
                sDCRDR_WW1 = dcr_CommitDr.get("sb_sDCRDR_WW1");
                sDCRDR_WW2 = dcr_CommitDr.get("sb_sDCRDR_WW2");
                sDCRDR_WW3 = dcr_CommitDr.get("sb_sDCRDR_WW3");
                sDCRDR_LOC = dcr_CommitDr.get("sb_sDCRDR_LOC");
                sDCRDR_IN_TIME = dcr_CommitDr.get("sb_sDCRDR_IN_TIME");
                sDCRDR_BATTERY_PERCENT = dcr_CommitDr.get("sb_sDCRDR_BATTERY_PERCENT");
                sDCRDR_REMARK = dcr_CommitDr.get("sb_sDCRDR_Remark");
                sDCRDR_KM = dcr_CommitDr.get("sb_sDCRDR_KM");
                sDCRDR_SRNO=dcr_CommitDr.get("sb_sDCRDR_SRNO");
                sDCRDR_FILE=dcr_CommitDr.get("sb_sDCRDR_FILE");
                sDCRDR_CALLTYPE=dcr_CommitDr.get("sb_sDCRDR_CALLTYPE");
            }

            dcr_ChemistCommit = cboFinalTask_new.dcr_chemSave(null);
            if ((dcr_ChemistCommit.isEmpty()) || (dcr_ChemistCommit.size() == 0)) {
                sDCRCHEM_CHEM_ID = "";
                sDCRCHEM_POB_QTY = "";
                sDCRCHEM_POB_AMT = "";
                sDCRCHEM_ITEM_ID_ARR = "";
                sDCRCHEM_QTY_ARR = "";
                sDCRCHEM_LOC = "";
                sDCRCHEM_IN_TIME = "";
                sDCRCHEM_SQTY_ARR = "";
                sDCRCHEM_ITEM_ID_GIFT_ARR = "";
                sDCRCHEM_QTY_GIFT_ARR = "";
                sDCRCHEM_BATTERY_PERCENT = "";
                sDCRCHEM_KM = "";
                sDCRCHEM_SRNO="";
                sDCRCHEM_REMARK="";
                sDCRCHEM_FILE="";

            } else {
                sDCRCHEM_CHEM_ID = dcr_ChemistCommit.get("sb_sDCRCHEM_CHEM_ID");
                sDCRCHEM_POB_QTY = dcr_ChemistCommit.get("sb_sDCRCHEM_POB_QTY");
                sDCRCHEM_POB_AMT = dcr_ChemistCommit.get("sb_sDCRCHEM_POB_AMT");
                sDCRCHEM_ITEM_ID_ARR = dcr_ChemistCommit.get("sb_sDCRCHEM_ITEM_ID_ARR");
                sDCRCHEM_QTY_ARR = dcr_ChemistCommit.get("sb_sDCRCHEM_QTY_ARR");
                sDCRCHEM_LOC = dcr_ChemistCommit.get("sb_sDCRCHEM_LOC");
                sDCRCHEM_IN_TIME = dcr_ChemistCommit.get("sb_sDCRCHEM_IN_TIME");
                sDCRCHEM_SQTY_ARR = dcr_ChemistCommit.get("sb_sDCRCHEM_SQTY_ARR");
                sDCRCHEM_ITEM_ID_GIFT_ARR = dcr_ChemistCommit.get("sb_sDCRCHEM_ITEM_ID_GIFT_ARR");
                sDCRCHEM_QTY_GIFT_ARR = dcr_ChemistCommit.get("sb_sDCRCHEM_QTY_GIFT_ARR");
                sDCRCHEM_BATTERY_PERCENT = dcr_ChemistCommit.get("sb_sDCRCHEM_BATTERY_PERCENT");
                sDCRCHEM_KM = dcr_ChemistCommit.get("sb_sDCRCHEM_KM");
                sDCRCHEM_SRNO=dcr_ChemistCommit.get("sb_sDCRCHEM_SRNO");
                sDCRCHEM_REMARK= dcr_ChemistCommit.get("sb_sDCRCHEM_REMARK");
                sDCRCHEM_FILE= dcr_ChemistCommit.get("sb_sDCRCHEM_FILE");
            }


            dcr_StkCommit = cboFinalTask_new.dcr_stkSave(null);
            if ((dcr_StkCommit.isEmpty()) || (dcr_StkCommit.size() == 0)) {

                sDCRSTK_STK_ID = "";

                sDCRSTK_POB_QTY = "";
                sDCRSTK_POB_AMT = "";
                sDCRSTK_ITEM_ID_ARR = "";
                sDCRSTK_QTY_ARR = "";
                sDCRSTK_LOC = "";
                sDCRSTK_IN_TIME = "";
                sDCRSTK_SQTY_ARR = "";
                sDCRSTK_ITEM_ID_GIFT_ARR = "";
                sDCRSTK_QTY_GIFT_ARR = "";
                sDCRSTK_BATTERY_PERCENT = "";
                sDCRSTK_KM = "";
                sDCRSTK_SRNO = "";
                sDCRSTK_REMARK="";
                sDCRSTK_FILE="";

            } else {
                sDCRSTK_STK_ID = dcr_StkCommit.get("sb_sDCRSTK_STK_ID");
                sDCRSTK_POB_QTY = dcr_StkCommit.get("sb_sDCRSTK_POB_QTY");
                sDCRSTK_POB_AMT = dcr_StkCommit.get("sb_sDCRSTK_POB_AMT");
                sDCRSTK_ITEM_ID_ARR = dcr_StkCommit.get("sb_sDCRSTK_ITEM_ID_ARR");
                sDCRSTK_QTY_ARR = dcr_StkCommit.get("sb_sDCRSTK_QTY_ARR");
                sDCRSTK_LOC = dcr_StkCommit.get("sb_sDCRSTK_LOC");
                sDCRSTK_IN_TIME = dcr_StkCommit.get("sb_sDCRSTK_IN_TIME");
                sDCRSTK_SQTY_ARR = dcr_StkCommit.get("sb_sDCRSTK_SQTY_ARR");
                sDCRSTK_ITEM_ID_GIFT_ARR = dcr_StkCommit.get("sb_sDCRSTK_ITEM_ID_GIFT_ARR");
                sDCRSTK_QTY_GIFT_ARR = dcr_StkCommit.get("sb_sDCRSTK_QTY_GIFT_ARR");
                sDCRSTK_BATTERY_PERCENT = dcr_StkCommit.get("sb_sDCRSTK_BATTERY_PERCENT");
                sDCRSTK_KM = dcr_StkCommit.get("sb_sDCRSTK_KM");
                sDCRSTK_SRNO = dcr_StkCommit.get("sb_sDCRSTK_SRNO");
                sDCRSTK_REMARK= dcr_StkCommit.get("sb_sDCRSTK_REMARK");
                sDCRSTK_FILE= dcr_StkCommit.get("sb_sDCRSTK_FILE");
            }


            dcr_CommitDr_Reminder = cboFinalTask_new.dcr_DrReminder(null);
            if ((dcr_CommitDr_Reminder.isEmpty()) || (dcr_CommitDr_Reminder.size() == 0)) {

                sDCRRC_IN_TIME = "";
                sDCRRC_LOC = "";
                sDCRRC_DR_ID = "";
                sDCRRC_KM = "";
                sDCRRC_SRNO = "";
                sDCRRC_BATTERY_PERCENT="";
                sDCRRC_REMARK="";
                sDCRRC_FILE="";
            } else {

                sDCRRC_DR_ID = dcr_CommitDr_Reminder.get("sb_sDCRRC_DR_ID");
                sDCRRC_LOC = dcr_CommitDr_Reminder.get("sb_sDCRRC_LOC");
                sDCRRC_IN_TIME = dcr_CommitDr_Reminder.get("sb_sDCRRC_IN_TIME");
                sDCRRC_KM = dcr_CommitDr_Reminder.get("sb_sDCRRC_KM");
                sDCRRC_SRNO = dcr_CommitDr_Reminder.get("sb_sDCRRC_SRNO");
                sDCRRC_BATTERY_PERCENT = dcr_CommitDr_Reminder.get("sb_sDCRRC_BATTERY_PERCENT");
                sDCRRC_REMARK=dcr_CommitDr_Reminder.get("sb_sDCRRC_REMARK");
                sDCRRC_FILE=dcr_CommitDr_Reminder.get("sb_sDCRRC_FILE");
            }

            Lat_Long_Reg = cboFinalTask_new.get_Lat_Long_Reg("0");
            if ((Lat_Long_Reg.isEmpty()) || (Lat_Long_Reg.size() == 0)) {

                DCS_ID_ARR = "";
                LAT_LONG_ARR = "";
                DCS_TYPE_ARR = "";
                DCS_ADD_ARR = "";
                DCS_INDES_ARR = "";
            } else {

                DCS_ID_ARR = Lat_Long_Reg.get("DCS_ID_ARR");
                LAT_LONG_ARR = Lat_Long_Reg.get("LAT_LONG_ARR");
                DCS_TYPE_ARR = Lat_Long_Reg.get("DCS_TYPE_ARR");
                DCS_ADD_ARR = Lat_Long_Reg.get("DCS_ADD_ARR");
                DCS_INDES_ARR = Lat_Long_Reg.get("DCS_INDES_ARR");
            }

            //customMethod.getDataFromFromAllTables();
            //sFinalKm = mycon.getDataFrom_FMCG_PREFRENCE("final_km");
           // ArrayList<String> array=customMethod.kmWithWayPoint();
            sFinalKm ="0"; // array.get(0);
            String sAPI_Pattern="0";  // array.get(1);

            if (Custom_Variables_And_Method.DCR_ID.equals("0")) {

                Custom_Variables_And_Method.DCR_ID =customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(mContext,"DCR_ID");
            }

            String ACTUALFARE=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(mContext,"ACTUALFARE");
            if (ACTUALFARE.equals(""))
                ACTUALFARE=""+0;

            Custom_Variables_And_Method.GCMToken=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(mContext,"GCMToken");

            response = mServiceHandler.getSoapResponse_DCRCommitFinal_New(cbohelp.getCompanyCode(), Custom_Variables_And_Method.DCR_ID,
                    "1", "1", "", "", "0.0", "0", ACTUALFARE, "NA", "99999", params[0], params[1], "99",
                    routeValue, sDCRLATCOMMIT_ID, sb_sDCRLATCOMMIT_IN_TIME
                    , sb_DCRLATCOMMIT_LOC_LAT, sDCRLATCOMMIT_LOC, sb_DCRLATCOMMIT_KM,
                    sDCRITEM_DR_ID, sDCRITEM_ITEMIDIN, sDCRITEM_ITEM_ID_ARR, sDCRITEM_QTY_ARR, sDCRITEM_ITEM_ID_GIFT_ARR, sDCRITEM_QTY_GIFT_ARR,
                    sDCRITEM_POB_QTY, sDCRITEM_POB_VALUE, sDCRITEM_VISUAL_ARR,sDCRITEM_NOC_ARR, sDCRDR_DR_ID, sDCRDR_WW1, sDCRDR_WW2, sDCRDR_WW3, sDCRDR_LOC,
                    sDCRDR_IN_TIME, sDCRDR_BATTERY_PERCENT, sDCRDR_REMARK, sDCRDR_KM, sDCRDR_SRNO,sDCRDR_FILE,sDCRDR_CALLTYPE,

                    sDCRCHEM_CHEM_ID, sDCRCHEM_POB_QTY, sDCRCHEM_POB_AMT, sDCRCHEM_ITEM_ID_ARR,
                    sDCRCHEM_QTY_ARR, sDCRCHEM_LOC, sDCRCHEM_IN_TIME, sDCRCHEM_SQTY_ARR, sDCRCHEM_ITEM_ID_GIFT_ARR, sDCRCHEM_QTY_GIFT_ARR
                    , sDCRCHEM_BATTERY_PERCENT, sDCRCHEM_KM,sDCRCHEM_SRNO,sDCRCHEM_REMARK,sDCRCHEM_FILE,

                    sDCRSTK_STK_ID, sDCRSTK_POB_QTY, sDCRSTK_POB_AMT, sDCRSTK_ITEM_ID_ARR, sDCRSTK_QTY_ARR, sDCRSTK_LOC,
                    sDCRSTK_IN_TIME, sDCRSTK_SQTY_ARR, sDCRSTK_ITEM_ID_GIFT_ARR, sDCRSTK_QTY_GIFT_ARR, sDCRSTK_BATTERY_PERCENT, sDCRSTK_KM,sDCRSTK_SRNO,sDCRSTK_REMARK,sDCRSTK_FILE,

                    sDCRRC_DR_ID, sDCRRC_LOC, sDCRRC_IN_TIME, sDCRRC_KM,sDCRRC_SRNO,sDCRRC_BATTERY_PERCENT,sDCRRC_REMARK,sDCRRC_FILE
                    , sDCR_DR_RX, sDCR_ITM_RX, sFinalKm,sAPI_Pattern,Custom_Variables_And_Method.BATTERYLEVEL,Custom_Variables_And_Method.GCMToken
                    , DCS_ID_ARR, LAT_LONG_ARR, DCS_TYPE_ARR, DCS_ADD_ARR, DCS_INDES_ARR);
            Log.d("Response: ", "> " + response);

            return response;


        } catch (Exception e) {
            response="ERROR " +"apk   "+e;
        }


        return response;
    }

    @Override
    protected void onPreExecute() {
        if (mListener != null) {
            mListener.onStarted();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (mListener != null) {
            mListener.onFinished(result);
        }
    }

    public void setListener(TaskListener<String> listener) {
        mListener = listener;
    }

    public String getCommitResponseFromJson(String result) {
        String response = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray rows = jsonObject.getJSONArray("Tables0");
            for (int i = 0; i < rows.length(); i++) {
                JSONObject c = rows.getJSONObject(i);
                response = c.getString("DCR_ID");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }


}
