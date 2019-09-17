package services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import async.CBOFinalTask_New;
import utils_new.Custom_Variables_And_Method;

public class Sync_service extends Service {
    CBO_DB_Helper cbohelp;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    MyCustomMethod customMethod;

    private  static final int MESSAGE_INTERNET_SYNC=1;
    public static String ReplyYN="N";

    private CBOFinalTask_New cboFinalTask_new;
    Map<String, String> dcr_latCommit = new HashMap<>();
    Map<String, String> dcr_Commititem = new HashMap<>();
    Map<String, String> dcr_Commit_rx = new HashMap<>();
    Map<String, String> dcr_CommitDr = new HashMap<>();
    Map<String, String> dcr_ChemistCommit = new HashMap<>();
    Map<String, String> dcr_StkCommit = new HashMap<>();
    Map<String, String> dcr_CommitDr_Reminder = new HashMap<>();
    Map<String, String> Lat_Long_Reg = new HashMap<>();
    Map<String, String> dcr_Dairy = new HashMap<>();


    String sb_DCRLATCOMMIT_KM, sb_DCRLATCOMMIT_LOC_LAT, sb_sDCRLATCOMMIT_IN_TIME, sDCRLATCOMMIT_ID, sDCRLATCOMMIT_LOC;
    String sDCRITEM_DR_ID, sDCRITEM_ITEMIDIN, sDCRITEM_ITEM_ID_ARR, sDCRITEM_QTY_ARR, sDCRITEM_ITEM_ID_GIFT_ARR, sDCRITEM_QTY_GIFT_ARR, sDCRITEM_POB_QTY, sDCRITEM_POB_VALUE, sDCRITEM_VISUAL_ARR, sDCRITEM_NOC_ARR;
    String sDCRDR_DR_ID, sDCRDR_WW1, sDCRDR_WW2, sDCRDR_WW3, sDCRDR_LOC, sDCRDR_IN_TIME, sDCRDR_BATTERY_PERCENT, sDCRDR_REMARK, sDCRDR_KM, sDCRDR_SRNO,sDCRDR_FILE,sDCRDR_CALLTYPE,sDR_REF_LAT_LONG;
    String sDCRCHEM_CHEM_ID, sDCRCHEM_POB_QTY, sDCRCHEM_POB_AMT, sDCRCHEM_ITEM_ID_ARR, sDCRCHEM_QTY_ARR, sDCRCHEM_LOC, sDCRCHEM_IN_TIME, sDCRCHEM_SQTY_ARR, sDCRCHEM_ITEM_ID_GIFT_ARR, sDCRCHEM_QTY_GIFT_ARR, sDCRCHEM_BATTERY_PERCENT, sDCRCHEM_KM, sDCRCHEM_SRNO,sDCRCHEM_REMARK,sDCRCHEM_FILE,sCHEM_REF_LAT_LONG;
    String sDCRSTK_STK_ID, sDCRSTK_POB_QTY, sDCRSTK_POB_AMT, sDCRSTK_ITEM_ID_ARR, sDCRSTK_QTY_ARR, sDCRSTK_LOC, sDCRSTK_IN_TIME, sDCRSTK_SQTY_ARR, sDCRSTK_ITEM_ID_GIFT_ARR, sDCRSTK_QTY_GIFT_ARR, sDCRSTK_BATTERY_PERCENT, sDCRSTK_KM, sDCRSTK_SRNO,sDCRSTK_REMARK,sDCRSTK_FILE,sSTK_REF_LAT_LONG;
    String sDCRRC_IN_TIME, sDCRRC_LOC, sDCRRC_DR_ID, sDCRRC_KM, sDCRRC_SRNO,sDCRRC_BATTERY_PERCENT,sDCRRC_REMARK,sDCRRC_FILE,sRC_REF_LAT_LONG,sCHEM_STATUS,sCOMPETITOR_REMARK;
    String sDCR_DR_RX, sDCR_ITM_RX;
    String sFinalKm;
    String DCS_ID_ARR, LAT_LONG_ARR, DCS_TYPE_ARR, DCS_ADD_ARR, DCS_INDES_ARR;


    String sDAIRY_ID, sSTRDAIRY_CPID,sDCRDAIRY_LOC,sDCRDAIRY_IN_TIME,sDCRDAIRY_BATTERY_PERCENT,sDCRDAIRY_REMARK,sDCRDAIRY_KM,sDCRDAIRY_SRNO,sDAIRY_REF_LAT_LONG;
    String sDCRDAIRYITEM_DAIRY_ID,sDCRDAIRYITEM_ITEM_ID_ARR,sDCRDAIRYITEM_QTY_ARR,sDCRDAIRYITEM_ITEM_ID_GIFT_ARR,sDCRDAIRYITEM_QTY_GIFT_ARR;
    String sDCRDAIRYITEM_POB_QTY,sDAIRY_FILE,sDCRDAIRY_INTERSETEDYN;

    String DCRSTK_RATE, DCRDR_RATE, DCRCHEM_RATE;

    public void DCR_sync_all(){
        try {

            Boolean IsGPRS_ON =Custom_Variables_And_Method.internetConneted(context);
            String fmcg_Live_Km = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"live_km");


            if ((fmcg_Live_Km.equalsIgnoreCase("Y") || fmcg_Live_Km.equalsIgnoreCase("5") || fmcg_Live_Km.equalsIgnoreCase("Y5")) && IsGPRS_ON ){

                customMethod.backgroundData();
                dcr_latCommit = customMethod.dataToServer("0");

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

            if (  IsGPRS_ON ) {
                dcr_Commit_rx = cboFinalTask_new.drRx_Save("0");
            }
            if ((dcr_Commit_rx.isEmpty()) || (dcr_Commit_rx.size() == 0)) {

                sDCR_DR_RX = "";
                sDCR_ITM_RX = "";


            } else {

                sDCR_DR_RX = dcr_Commit_rx.get("sDCRRX_DR_ARR");
                sDCR_ITM_RX = dcr_Commit_rx.get("sDCRRX_ITEMID_ARR");


            }

            if (  IsGPRS_ON ) {
                dcr_Commititem = cboFinalTask_new.drItemSave("0");
            }

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
                sDCRITEM_NOC_ARR="";
                DCRDR_RATE = "";

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
                DCRDR_RATE = dcr_Commititem.get("sb_DCRDR_RATE");

            }

            if (  IsGPRS_ON ) {
                dcr_CommitDr = cboFinalTask_new.dcr_doctorSave("0");
            }

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
                sDR_REF_LAT_LONG = "";
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
                sDR_REF_LAT_LONG = dcr_CommitDr.get("sb_sDR_REF_LAT_LONG");
            }

            if (  IsGPRS_ON ) {
                dcr_ChemistCommit = cboFinalTask_new.dcr_chemSave("0");
            }

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
                DCRCHEM_RATE = "";
                sCHEM_REF_LAT_LONG = "";

                sCHEM_STATUS="";
                sCOMPETITOR_REMARK="";

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
                DCRCHEM_RATE = dcr_ChemistCommit.get("sb_DCRCHEM_RATE");
                sCHEM_REF_LAT_LONG = dcr_ChemistCommit.get("sb_sCHEM_REF_LAT_LONG");

                sCHEM_STATUS= dcr_ChemistCommit.get("sCHEM_STATUS");
                sCOMPETITOR_REMARK= dcr_ChemistCommit.get("sCOMPETITOR_REMARK");

            }


            if (IsGPRS_ON ) {
                dcr_StkCommit = cboFinalTask_new.dcr_stkSave("0");
            }

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
                sSTK_REF_LAT_LONG = "";
                DCRSTK_RATE = "";

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
                sSTK_REF_LAT_LONG = dcr_StkCommit.get("sb_sSTK_REF_LAT_LONG");
                DCRSTK_RATE = dcr_StkCommit.get("sb_DCRSTK_RATE");
            }


            if (  IsGPRS_ON ) {
                dcr_CommitDr_Reminder = cboFinalTask_new.dcr_DrReminder("0");
            }

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


            if (  IsGPRS_ON ) {
                Lat_Long_Reg = cboFinalTask_new.get_Lat_Long_Reg("0");
            }

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

            if (  IsGPRS_ON ) {
                dcr_Dairy = cboFinalTask_new.get_phdairy_dcr("0");
            }

            if ((dcr_Dairy.isEmpty()) || (dcr_Dairy.size() == 0)) {

                sDAIRY_ID= "";
                sSTRDAIRY_CPID ="";
                sDCRDAIRY_LOC = "";
                sDCRDAIRY_IN_TIME= "";
                sDCRDAIRY_BATTERY_PERCENT= "";
                sDCRDAIRY_REMARK= "";
                sDCRDAIRY_KM= "";
                sDCRDAIRY_SRNO= "";
                sDCRDAIRYITEM_DAIRY_ID= "";
                sDCRDAIRYITEM_ITEM_ID_ARR= "";
                sDCRDAIRYITEM_QTY_ARR= "";
                sDCRDAIRYITEM_ITEM_ID_GIFT_ARR= "";
                sDCRDAIRYITEM_QTY_GIFT_ARR= "";
                sDCRDAIRYITEM_POB_QTY= "";
                sDAIRY_FILE= "";
                sDCRDAIRY_INTERSETEDYN= "";
                sDAIRY_REF_LAT_LONG = "";
            } else {

                sDAIRY_ID = dcr_Dairy.get("sDAIRY_ID");
                sSTRDAIRY_CPID  = dcr_Dairy.get("sSTRDAIRY_CPID");
                sDCRDAIRY_LOC  = dcr_Dairy.get("sDCRDAIRY_LOC");
                sDCRDAIRY_IN_TIME = dcr_Dairy.get("sDCRDAIRY_IN_TIME");
                sDCRDAIRY_BATTERY_PERCENT = dcr_Dairy.get("sDCRDAIRY_BATTERY_PERCENT");
                sDCRDAIRY_REMARK = dcr_Dairy.get("sDCRDAIRY_REMARK");
                sDCRDAIRY_KM = dcr_Dairy.get("sDCRDAIRY_KM");
                sDCRDAIRY_SRNO = dcr_Dairy.get("sDCRDAIRY_SRNO");
                sDCRDAIRYITEM_DAIRY_ID = dcr_Dairy.get("sDAIRY_ID");
                sDCRDAIRYITEM_ITEM_ID_ARR = dcr_Dairy.get("sDCRDAIRYITEM_ITEM_ID_ARR");
                sDCRDAIRYITEM_QTY_ARR = dcr_Dairy.get("sDCRDAIRYITEM_QTY_ARR");
                sDCRDAIRYITEM_ITEM_ID_GIFT_ARR = dcr_Dairy.get("sDCRDAIRYITEM_ITEM_ID_GIFT_ARR");
                sDCRDAIRYITEM_QTY_GIFT_ARR = dcr_Dairy.get("sDCRDAIRYITEM_QTY_GIFT_ARR");
                sDCRDAIRYITEM_POB_QTY = dcr_Dairy.get("sDCRDAIRYITEM_POB_QTY");
                sDAIRY_FILE = dcr_Dairy.get("sDAIRY_FILE");
                sDCRDAIRY_INTERSETEDYN = dcr_Dairy.get("sDCRDAIRY_INTERSETEDYN");
                sDAIRY_REF_LAT_LONG = dcr_Dairy.get("sDAIRY_REF_LAT_LONG");
            }

            if (IsGPRS_ON && (!dcr_Commit_rx.isEmpty() || dcr_Commit_rx.size() > 0 ||
                    !dcr_CommitDr_Reminder.isEmpty() || dcr_CommitDr_Reminder.size() > 0  ||
                    !dcr_StkCommit.isEmpty() || dcr_StkCommit.size()> 0 ||
                    !dcr_ChemistCommit.isEmpty() || dcr_ChemistCommit.size() > 0 ||
                    !dcr_CommitDr.isEmpty() || dcr_CommitDr.size() > 0 ||
                    !dcr_Commititem.isEmpty() || dcr_Commititem.size() > 0 ||
                    !dcr_latCommit.isEmpty() || dcr_latCommit.size() > 0 ||
                    !Lat_Long_Reg.isEmpty() || Lat_Long_Reg.size() > 0 ||
                    !dcr_Dairy.isEmpty() || dcr_Dairy.size() > 0)){

                //Start of call to service

                HashMap<String, String> request = new HashMap<>();
                request.put("sCompanyFolder", cbohelp.getCompanyCode());
                request.put("iDcrId", "" + Custom_Variables_And_Method.DCR_ID);
                request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);

                request.put("sDCRDR_DR_ID", sDCRDR_DR_ID);
                request.put("sDCRDR_WW1", sDCRDR_WW1);
                request.put("sDCRDR_WW2", sDCRDR_WW2);
                request.put("sDCRDR_WW3", sDCRDR_WW3);
                request.put("sDCRDR_LOC", sDCRDR_LOC);
                request.put("sDCRDR_IN_TIME", sDCRDR_IN_TIME);
                request.put("sDCRDR_BATTERY_PERCENT", sDCRDR_BATTERY_PERCENT);
                request.put("sDCRDR_REMARK", sDCRDR_REMARK);
                request.put("sDCRDR_KM", sDCRDR_KM);
                request.put("sDCRDR_SRNO", sDCRDR_SRNO);
                request.put("sDCRDR_CALLTYPE", sDCRDR_CALLTYPE);
                request.put("sDCRDR_FILE", sDCRDR_FILE);

                request.put("sDCRITEM_DR_ID", sDCRITEM_DR_ID);
                request.put("sDCRITEM_ITEMIDIN", sDCRITEM_ITEMIDIN);
                request.put("sDCRITEM_ITEM_ID_ARR", sDCRITEM_ITEM_ID_ARR);
                request.put("sDCRITEM_QTY_ARR", sDCRITEM_QTY_ARR);
                request.put("sDCRITEM_ITEM_ID_GIFT_ARR", sDCRITEM_ITEM_ID_GIFT_ARR);
                request.put("sDCRITEM_QTY_GIFT_ARR", sDCRITEM_QTY_GIFT_ARR);
                request.put("sDCRITEM_POB_QTY", sDCRITEM_POB_QTY);
                request.put("sDCRITEM_POB_VALUE", sDCRITEM_POB_VALUE);
                request.put("sDCRITEM_VISUAL_ARR", sDCRITEM_VISUAL_ARR);
                request.put("sDCRITEM_NOC_ARR", sDCRITEM_NOC_ARR);

                request.put("sDCRRX_DR_ARR", sDCR_DR_RX);
                request.put("sDCRRX_ITEMID_ARR", sDCR_ITM_RX);

                request.put("sDCRCHEM_CHEM_ID", sDCRCHEM_CHEM_ID);
                request.put("sDCRCHEM_POB_QTY", sDCRCHEM_POB_QTY);
                request.put("sDCRCHEM_POB_AMT", sDCRCHEM_POB_AMT);
                request.put("sDCRCHEM_ITEM_ID_ARR", sDCRCHEM_ITEM_ID_ARR);
                request.put("sDCRCHEM_QTY_ARR", sDCRCHEM_QTY_ARR);
                request.put("sDCRCHEM_LOC", sDCRCHEM_LOC);
                request.put("sDCRCHEM_IN_TIME", sDCRCHEM_IN_TIME);
                request.put("sDCRCHEM_SQTY_ARR", sDCRCHEM_SQTY_ARR);
                request.put("sDCRCHEM_ITEM_ID_GIFT_ARR", sDCRCHEM_ITEM_ID_GIFT_ARR);
                request.put("sDCRCHEM_QTY_GIFT_ARR", sDCRCHEM_QTY_GIFT_ARR);
                request.put("sDCRCHEM_BATTERY_PERCENT", sDCRCHEM_BATTERY_PERCENT);
                request.put("sDCRCHEM_KM", sDCRCHEM_KM);
                request.put("sDCRCHEM_SRNO", sDCRCHEM_SRNO);
                request.put("sDCRCHEM_REMARK", sDCRCHEM_REMARK);
                request.put("sDCRCHEM_FILE", sDCRCHEM_FILE);

                request.put("sDCRSTK_STK_ID", sDCRSTK_STK_ID);
                request.put("sDCRSTK_POB_QTY", sDCRSTK_POB_QTY);
                request.put("sDCRSTK_POB_AMT", sDCRSTK_POB_AMT);
                request.put("sDCRSTK_ITEM_ID_ARR", sDCRSTK_ITEM_ID_ARR);
                request.put("sDCRSTK_QTY_ARR", sDCRSTK_QTY_ARR);
                request.put("sDCRSTK_LOC", sDCRSTK_LOC);
                request.put("sDCRSTK_IN_TIME", sDCRSTK_IN_TIME);
                request.put("sDCRSTK_SQTY_ARR", sDCRSTK_SQTY_ARR);
                request.put("sDCRSTK_ITEM_ID_GIFT_ARR", sDCRSTK_ITEM_ID_GIFT_ARR);
                request.put("sDCRSTK_QTY_GIFT_ARR", sDCRSTK_QTY_GIFT_ARR);
                request.put("sDCRSTK_BATTERY_PERCENT", sDCRSTK_BATTERY_PERCENT);
                request.put("sDCRSTK_KM", sDCRSTK_KM);
                request.put("sDCRSTK_SRNO", sDCRSTK_SRNO);
                request.put("sDCRSTK_REMARK", sDCRSTK_REMARK);
                request.put("sDCRSTK_FILE", sDCRSTK_FILE);

                request.put("sDCRRC_DR_ID", sDCRRC_DR_ID);
                request.put("sDCRRC_LOC", sDCRRC_LOC);
                request.put("sDCRRC_IN_TIME", sDCRRC_IN_TIME);
                request.put("sDCRRC_KM", sDCRRC_KM);
                request.put("sDCRRC_SRNO", sDCRRC_SRNO);
                request.put("sDCRRC_BATTERY_PERCENT", sDCRRC_BATTERY_PERCENT);
                request.put("sDCRRC_REMARK", sDCRRC_REMARK);
                request.put("sDCRRC_FILE", sDCRRC_FILE);

                request.put("sDCRLATCOMMIT_ID", sDCRLATCOMMIT_ID);
                request.put("sDCRLATCOMMIT_IN_TIME", sb_sDCRLATCOMMIT_IN_TIME);
                request.put("sDCRLATCOMMIT_LOC_LAT", sb_DCRLATCOMMIT_LOC_LAT);
                request.put("sDCRLATCOMMIT_LOC", sDCRLATCOMMIT_LOC);
                request.put("sDCRLATCOMMIT_KM", sb_DCRLATCOMMIT_KM);


                request.put("DCS_ID_ARR", DCS_ID_ARR);
                request.put("LAT_LONG_ARR", LAT_LONG_ARR);
                request.put("DCS_TYPE_ARR", DCS_TYPE_ARR);
                request.put("DCS_ADD_ARR", DCS_ADD_ARR);
                request.put("DCS_INDES_ARR", DCS_INDES_ARR);


                request.put("sDAIRY_ID", sDAIRY_ID);
                request.put("sSTRDAIRY_CPID", sSTRDAIRY_CPID);
                request.put("sDCRDAIRY_LOC", sDCRDAIRY_LOC);
                request.put("sDCRDAIRY_IN_TIME", sDCRDAIRY_IN_TIME);
                request.put("sDCRDAIRY_BATTERY_PERCENT", sDCRDAIRY_BATTERY_PERCENT);


                request.put("sDCRDAIRY_REMARK", sDCRDAIRY_REMARK);
                request.put("sDCRDAIRY_KM", sDCRDAIRY_KM);

                request.put("sDCRDAIRY_SRNO", sDCRDAIRY_SRNO);
                request.put("sDCRDAIRYITEM_DAIRY_ID", sDCRDAIRYITEM_DAIRY_ID);
                request.put("sDCRDAIRYITEM_ITEM_ID_ARR", sDCRDAIRYITEM_ITEM_ID_ARR);
                request.put("sDCRDAIRYITEM_QTY_ARR", sDCRDAIRYITEM_QTY_ARR);
                request.put("sDCRDAIRYITEM_ITEM_ID_GIFT_ARR", sDCRDAIRYITEM_ITEM_ID_GIFT_ARR);
                request.put("sDCRDAIRYITEM_QTY_GIFT_ARR", sDCRDAIRYITEM_QTY_GIFT_ARR);
                request.put("sDCRDAIRYITEM_POB_QTY", sDCRDAIRYITEM_POB_QTY);
                request.put("sDAIRY_FILE", sDAIRY_FILE);
                request.put("sDCRDAIRY_INTERSETEDYN", sDCRDAIRY_INTERSETEDYN);


                request.put("SDCR_DATE", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DATE"));
                request.put("sDR_REF_LAT_LONG", sDR_REF_LAT_LONG);
                request.put("sCHEM_REF_LAT_LONG", sCHEM_REF_LAT_LONG);
                request.put("sSTK_REF_LAT_LONG", sSTK_REF_LAT_LONG);
                request.put("sDAIRY_REF_LAT_LONG", sDAIRY_REF_LAT_LONG);
                request.put("sRC_REF_LAT_LONG", sRC_REF_LAT_LONG);


                request.put("sSTKITEM_RATE", DCRSTK_RATE);
                request.put("sDRITEM_RATE", DCRDR_RATE);
                request.put("sCHEMITEM_RATE", DCRCHEM_RATE);


                request.put("sCHEM_STATUS", sCHEM_STATUS);
                request.put("sCOMPETITOR_REMARK", sCOMPETITOR_REMARK);

                ArrayList<Integer> tables = new ArrayList<>();
                if (ReplyYN.equals("N")){
                    tables.add(-2);
                }else {
                    tables.add(-1);
                }

                new CboServices(this, mHandler).customMethodForAllServices(request, "DCR_SYNC_MOBILE_ALL_6", MESSAGE_INTERNET_SYNC, tables);

                //End of call to service
            }


        } catch (Exception e) {
           // response="ERROR " +"apk   "+e;
            Log.e("MYAPP", "Sync: " + e.toString());
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ReplyYN="N";
            String DoneYN="N";
            switch (msg.what) {
                case MESSAGE_INTERNET_SYNC:
                    DoneYN="Y";
                    if ((null != msg.getData())) {

                        parser_sync(msg.getData());

                    }
                    break;
                case 99:
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));

                    }
                    break;
                default:

            }
            sendMessagetoUI(DoneYN);
        }
    };

    private void parser_sync(Bundle result) {
        if (result!=null ) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    if(c.getString("STATUS").equals("OK")){
                        if(!dcr_CommitDr.isEmpty() || dcr_CommitDr.size() > 0) {
                            String [] dr_id_array=dcr_CommitDr.get("sb_sDCRDR_DR_ID").replace("^","@").split("@");
                            for (int j=0;j<dr_id_array.length;j++) {
                                cbohelp.updateDrKilo("0", dr_id_array[j]);
                            }
                        }
                        if(!dcr_Commititem.isEmpty() || dcr_Commititem.size() > 0) {
                            String [] dr_id_array=dcr_Commititem.get("sb_sDCRITEM_DR_ID").replace("^","@").split("@");
                            for (int j=0;j<dr_id_array.length;j++) {
                                cbohelp.updateDr_item(dr_id_array[j]);
                            }
                        }
                        if(!dcr_CommitDr_Reminder.isEmpty() || dcr_CommitDr_Reminder.size() > 0) {
                            String [] dr_id_array=dcr_CommitDr_Reminder.get("sb_sDCRRC_DR_ID").replace("^","@").split("@");
                            for (int j=0;j<dr_id_array.length;j++) {
                                cbohelp.updateKm_RC("0", dr_id_array[j]);
                            }
                        }
                        if(!dcr_ChemistCommit.isEmpty() || dcr_ChemistCommit.size() > 0) {
                            String [] dr_id_array=dcr_ChemistCommit.get("sb_sDCRCHEM_CHEM_ID").replace("^","@").split("@");
                            for (int j=0;j<dr_id_array.length;j++) {
                                cbohelp.updateChemistKilo("0", dr_id_array[j]);
                            }
                        }
                        if(!dcr_StkCommit.isEmpty() || dcr_StkCommit.size() > 0) {
                            String [] dr_id_array=dcr_StkCommit.get("sb_sDCRSTK_STK_ID").replace("^","@").split("@");
                            for (int j=0;j<dr_id_array.length;j++) {
                                cbohelp.updateStk_Km("0", dr_id_array[j]);
                            }
                        }
                        if(!dcr_latCommit.isEmpty() || dcr_latCommit.size() > 0) {
                            String [] dr_id_array= dcr_latCommit.get("sDCRLATCOMMIT_ID").replace("^","@").split("@");
                            for (int j=0;j<dr_id_array.length;j++) {
                                cbohelp.latLon10_updated(dr_id_array[j]);
                            }
                        }

                        if(!Lat_Long_Reg.isEmpty() || Lat_Long_Reg.size() > 0) {
                            String [] dr_id_array= Lat_Long_Reg.get("DCS_ID_ARR").replace("^","@").split("@");
                            for (int j=0;j<dr_id_array.length;j++) {
                                cbohelp.updatedLat_Long_Reg(dr_id_array[j]);
                            }
                        }

                        if(!dcr_Dairy.isEmpty() || dcr_Dairy.size() > 0) {
                            String [] dr_id_array= dcr_Dairy.get("sDAIRY_ID").replace("^","@").split("@");
                            for (int j=0;j<dr_id_array.length;j++) {
                                cbohelp.updatedphdairy_dcr(dr_id_array[j]);
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
    }


    private void sendMessagetoUI(String DoneYN) {
        //Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("SyncComplete");
        // You can also include some extra data.
        intent.putExtra("message", DoneYN );
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.e("servic binded", "service binded");
        return null;
    }

    public void onCreate() {
        super.onCreate();
        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cbohelp = new CBO_DB_Helper(getApplicationContext());
        cboFinalTask_new = new CBOFinalTask_New(context);
        customMethod=new MyCustomMethod(context);
        Log.e("service started", "service started");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e("service stop", "service stop");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        cbohelp = new CBO_DB_Helper(getApplicationContext());



        Runnable r7 = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    DCR_sync_all();
                }

            }
        };


        new Thread(r7).start();
        stopSelf();
        return Service.START_STICKY;
    }


}
