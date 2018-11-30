package utils.networkUtil;

import android.content.Context;
import android.content.SharedPreferences;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import services.ServiceHandler;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Kuldeep.Dwivedi on 12/6/2014.
 */
public class AppPrefrences {
    private Context mContext;
    ServiceHandler serviceHandler;
    public static String AppId = "076829893341962";
    CBO_DB_Helper cboDbHelper;

    public AppPrefrences(Context mContext) {
        this.mContext = mContext;

    }

    public void setDataForFMCG() {
        String response = "";
        cboDbHelper = new CBO_DB_Helper(mContext);
        serviceHandler = new ServiceHandler(mContext);
        SharedPreferences.Editor editor = mContext.getSharedPreferences(Custom_Variables_And_Method.FMCG_PREFRENCE, mContext.MODE_PRIVATE).edit();
        response = serviceHandler.getResponse_FMCGDDL(cboDbHelper.getCompanyCode(),""+Custom_Variables_And_Method.PA_ID);

        String mtres = response;
        String mtred = response;
        if (response != null && !response.contains("[ERROR]")) {
            try {
                cboDbHelper.deleteMenu();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray rows = jsonObject.getJSONArray("Tables");
                JSONObject jsonObject1 = rows.getJSONObject(0);
                JSONObject jsonObject2 = rows.getJSONObject(1);
                JSONArray table0 = jsonObject1.getJSONArray("Tables0");
                JSONArray table1 = jsonObject2.getJSONArray("Tables1");
                for (int i = 0; i < table0.length(); i++) {
                    JSONObject c = table0.getJSONObject(i);
                    editor.putString("fmcg_value", c.getString("FMCG"));
                    editor.putString("root_needed", c.getString("ROUTE"));
                    editor.putString("gps_needed", c.getString("GPRSYN"));
                    editor.putString("version", c.getString("VER"));
                    editor.putString("doryn", c.getString("DORYN"));
                    editor.putString("dosyn", c.getString("DOSYN"));
                    editor.putString("internet", c.getString("INTERNET_RQD"));
                    editor.putString("live_km", c.getString("LIVE_KM"));
                    editor.putString("leave_yn", c.getString("LEAVEYN"));
                    editor.putString("WEBSERVICE_URL", c.getString("WEBSERVICE_URL"));
                    editor.putString("WEBSERVICE_URL_ALTERNATE", c.getString("WEBSERVICE_URL_ALTERNATE"));
                    editor.putString("FLASHYN", c.getString("FLASHYN"));
                   // editor.putString("FLASHYN", c.getString("FLASHYN"));
                    editor.putString("DCR_REMARK_NA", c.getString("DCR_REMARK_NA"));
                    editor.putString("DCR_DR_REMARKYN", c.getString("DCR_DR_REMARKYN"));
                    editor.putString("ROUTEDIVERTYN", c.getString("ROUTEDIVERTYN"));
                    editor.putString("DCR_ADDAREANA", c.getString("DCR_ADDAREANA"));
                    editor.putString("VISUALAIDPDFYN", c.getString("VISUALAIDPDFYN"));
                    editor.putString("SAMPLE_POB_MANDATORY", c.getString("SAMPLE_POB_MANDATORY"));
                    editor.putString("REMARK_WW_MANDATORY", c.getString("REMARK_WW_MANDATORY"));
                    editor.putString("SAMPLE_POB_INPUT_MANDATORY", c.getString("SAMPLE_POB_INPUT_MANDATORY"));
                    editor.putString("MISSED_CALL_OPTION", c.getString("MISSED_CALL_OPTION"));
                    editor.putString("APPRAISALMANDATORY", c.getString("APPRAISALMANDATORY"));
                    editor.putString("USER_NAME", c.getString("USER_NAME"));
                    editor.putString("PASSWORD", c.getString("PASSWORD"));
                    editor.putString("VISUALAID_DRSELITEMYN", c.getString("VISUALAID_DRSELITEMYN"));
                    editor.putString("DOB_REMINDER_HOUR", c.getString("DOB_REMINDER_HOUR"));
                    editor.putString("SYNCDRITEMYN", c.getString("SYNCDRITEMYN"));
                    editor.putString("GEO_FANCING_KM", c.getString("GEO_FANCING_KM"));
                    editor.putString("FIRST_CALL_LOCK_TIME", c.getString("FIRST_CALL_LOCK_TIME"));
                    editor.putString("mark", c.getString("FLASH_MESSAGE"));
                    editor.putString("NOC_HEAD", c.getString("NOC_HEAD"));
                    editor.putString("USER_PIC", c.getString("USER_PIC"));
                    editor.putString("DCR_LETREMARK_LENGTH", c.getString("DCR_LETREMARK_LENGTH"));
                    editor.putString("SAMPLEMAXQTY", c.getString("SAMPLEMAXQTY"));
                    editor.putString("POBMAXQTY", c.getString("POBMAXQTY"));
                    editor.putString("ASKUPDATEYN", c.getString("ASKUPDATEYN"));
                    editor.putString("MOBILEDATAYN", c.getString("MOBILEDATAYN"));
                    editor.putString("CALLWAITINGTIME", c.getString("CALLWAITINGTIME"));
                    editor.putString("COMPANY_PIC", c.getString("COMPANY_PIC"));
                    editor.putString("RE_REG_KM", c.getString("RE_REG_KM"));
                    editor.putString("ERROR_EMAIL", c.getString("ERROR_EMAIL"));
                    editor.putString("DIVERT_REMARKYN", c.getString("DIVERT_REMARKYN"));
                    editor.putString("NLC_PIC_YN", c.getString("NLC_PIC_YN"));
                    editor.putString("RX_MAX_QTY", c.getString("RX_MAX_QTY"));
                    editor.putString("SHOW_ADD_REGYN", c.getString("SHOW_ADD_REGYN"));
                    editor.putString("EXP_ATCH_YN", c.getString("EXP_ATCH_YN"));
                    editor.putString("FARMERADDFIELDYN", c.getString("FARMERADDFIELDYN"));
                    editor.putString("NO_DR_CALL_REQ", c.getString("NO_DR_CALL_REQ"));
                    editor.putString("DR_RX_ENTRY_YN", c.getString("DR_RX_ENTRY_YN"));
                    editor.putString("RETAILERCHAINYN", c.getString("RETAILERCHAINYN"));
                    editor.putString("DCR_SUBMIT_TIME", c.getString("DCR_SUBMIT_TIME"));
                    editor.putString("DCR_SUBMIT_SPEACH", c.getString("DCR_SUBMIT_SPEACH"));
                    editor.putString("ALLOWED_APP", c.getString("ALLOWED_APP"));
                    editor.putString("DCRGIFT_QTY_VALIDATE", c.getString("DCRGIFT_QTY_VALIDATE"));
                    editor.putString("SAMPLE_BTN_CAPTION", c.getString("SAMPLE_BTN_CAPTION"));
                    editor.putString("GIFT_BTN_CAPTION", c.getString("GIFT_BTN_CAPTION"));
                    editor.putString("DIVERTWWYN", c.getString("DIVERTWWYN"));
                    editor.putString("PIN_ALLOWED_MSG", c.getString("PIN_ALLOWED_MSG"));
                    editor.putString("CMC3_GALLERY_REQ", c.getString("CMC3_GALLERY_REQ"));
                    editor.putString("DR_COLOR", c.getString("DR_COLOR"));
                    editor.putString("DCRPPNA", c.getString("DCRPPNA"));
                    editor.putString("DR_SALE_URL", c.getString("DR_SALE_URL"));
                    editor.commit();

                }


                for (int i = 0; i < table1.length(); i++) {
                    JSONObject object = table1.getJSONObject(i);
                    String menu = object.getString("MAIN_MENU");
                    String menu_code = object.getString("MENU_CODE");
                    String menu_name = object.getString("MENU_NAME");
                    String menu_url = object.getString("URL");
                    String main_menu_srno = object.getString("MAIN_MENU_SRNO");
                    cboDbHelper.insertMenu(menu, menu_code, menu_name,menu_url,main_menu_srno);
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void setDataforFMCGandMenu(String response) {
        cboDbHelper = new CBO_DB_Helper(mContext);
        SharedPreferences.Editor editor = mContext.getSharedPreferences(Custom_Variables_And_Method.FMCG_PREFRENCE, mContext.MODE_PRIVATE).edit();

        if (response != null && !response.contains("ERROR")) {
            try {
                cboDbHelper.deleteMenu();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray rows = jsonObject.getJSONArray("Tables");
                JSONObject jsonObject1 = rows.getJSONObject(0);
                JSONObject jsonObject2 = rows.getJSONObject(1);
                JSONArray table0 = jsonObject1.getJSONArray("Tables0");
                JSONArray table1 = jsonObject2.getJSONArray("Tables1");
                for (int i = 0; i < table0.length(); i++) {
                    JSONObject c = table0.getJSONObject(i);
                    editor.putString("fmcg_value", c.getString("FMCG"));
                    editor.putString("root_needed", c.getString("ROUTE"));
                    editor.putString("gps_needed", c.getString("GPRSYN"));
                    editor.putString("version", c.getString("VER"));

                    cboDbHelper.deleteVersion();
                    cboDbHelper.insertVersionInLocal(c.getString("VER"));

                    editor.putString("doryn", c.getString("DORYN"));
                    editor.putString("dosyn", c.getString("DOSYN"));
                    editor.putString("internet", c.getString("INTERNET_RQD"));
                    editor.putString("live_km", c.getString("LIVE_KM"));
                    editor.putString("leave_yn", c.getString("LEAVEYN"));
                    editor.putString("WEBSERVICE_URL", c.getString("WEBSERVICE_URL"));
                    editor.putString("WEBSERVICE_URL_ALTERNATE", c.getString("WEBSERVICE_URL_ALTERNATE"));
                    editor.putString("FLASHYN", c.getString("FLASHYN"));
                    //editor.putString("FLASHYN", c.getString("FLASHYN"));
                    editor.putString("DCR_REMARK_NA", c.getString("DCR_REMARK_NA"));
                    editor.putString("DCR_DR_REMARKYN", c.getString("DCR_DR_REMARKYN"));
                    editor.putString("ROUTEDIVERTYN", c.getString("ROUTEDIVERTYN"));
                    editor.putString("DCR_ADDAREANA", c.getString("DCR_ADDAREANA"));
                    editor.putString("VISUALAIDPDFYN", c.getString("VISUALAIDPDFYN"));
                    editor.putString("SAMPLE_POB_MANDATORY", c.getString("SAMPLE_POB_MANDATORY"));
                    editor.putString("REMARK_WW_MANDATORY", c.getString("REMARK_WW_MANDATORY"));
                    editor.putString("SAMPLE_POB_INPUT_MANDATORY", c.getString("SAMPLE_POB_INPUT_MANDATORY"));
                    editor.putString("MISSED_CALL_OPTION", c.getString("MISSED_CALL_OPTION"));
                    editor.putString("APPRAISALMANDATORY", c.getString("APPRAISALMANDATORY"));
                    editor.putString("USER_NAME", c.getString("USER_NAME"));
                    editor.putString("PASSWORD", c.getString("PASSWORD"));
                    editor.putString("VISUALAID_DRSELITEMYN", c.getString("VISUALAID_DRSELITEMYN"));
                    editor.putString("DOB_REMINDER_HOUR", c.getString("DOB_REMINDER_HOUR"));
                    editor.putString("SYNCDRITEMYN", c.getString("SYNCDRITEMYN"));
                    editor.putString("GEO_FANCING_KM", c.getString("GEO_FANCING_KM"));
                    editor.putString("FIRST_CALL_LOCK_TIME", c.getString("FIRST_CALL_LOCK_TIME"));
                    editor.putString("mark", c.getString("FLASH_MESSAGE"));
                    editor.putString("NOC_HEAD", c.getString("NOC_HEAD"));
                    editor.putString("USER_PIC", c.getString("USER_PIC"));
                    editor.putString("DCR_LETREMARK_LENGTH", c.getString("DCR_LETREMARK_LENGTH"));
                    editor.putString("SAMPLEMAXQTY", c.getString("SAMPLEMAXQTY"));
                    editor.putString("POBMAXQTY", c.getString("POBMAXQTY"));
                    editor.putString("ASKUPDATEYN", c.getString("ASKUPDATEYN"));
                    editor.putString("MOBILEDATAYN", c.getString("MOBILEDATAYN"));
                    editor.putString("CALLWAITINGTIME", c.getString("CALLWAITINGTIME"));
                    editor.putString("COMPANY_PIC", c.getString("COMPANY_PIC"));
                    editor.putString("RE_REG_KM", c.getString("RE_REG_KM"));
                    editor.putString("ERROR_EMAIL", c.getString("ERROR_EMAIL"));
                    editor.putString("DIVERT_REMARKYN", c.getString("DIVERT_REMARKYN"));
                    editor.putString("NLC_PIC_YN", c.getString("NLC_PIC_YN"));
                    editor.putString("RX_MAX_QTY", c.getString("RX_MAX_QTY"));
                    editor.putString("SHOW_ADD_REGYN", c.getString("SHOW_ADD_REGYN"));
                    editor.putString("EXP_ATCH_YN", c.getString("EXP_ATCH_YN"));
                    editor.putString("FARMERADDFIELDYN", c.getString("FARMERADDFIELDYN"));
                    editor.putString("NO_DR_CALL_REQ", c.getString("NO_DR_CALL_REQ"));
                    editor.putString("DR_RX_ENTRY_YN", c.getString("DR_RX_ENTRY_YN"));
                    editor.putString("RETAILERCHAINYN", c.getString("RETAILERCHAINYN"));
                    editor.putString("DCR_SUBMIT_TIME", c.getString("DCR_SUBMIT_TIME"));
                    editor.putString("DCR_SUBMIT_SPEACH", c.getString("DCR_SUBMIT_SPEACH"));
                    editor.putString("ALLOWED_APP", c.getString("ALLOWED_APP"));
                    editor.putString("DCRGIFT_QTY_VALIDATE", c.getString("DCRGIFT_QTY_VALIDATE"));
                    editor.putString("SAMPLE_BTN_CAPTION", c.getString("SAMPLE_BTN_CAPTION"));
                    editor.putString("GIFT_BTN_CAPTION", c.getString("GIFT_BTN_CAPTION"));
                    editor.putString("DIVERTWWYN", c.getString("DIVERTWWYN"));
                    editor.putString("PIN_ALLOWED_MSG", c.getString("PIN_ALLOWED_MSG"));
                    editor.putString("CMC3_GALLERY_REQ", c.getString("CMC3_GALLERY_REQ"));
                    editor.putString("DR_COLOR", c.getString("DR_COLOR"));
                    editor.putString("DCRPPNA", c.getString("DCRPPNA"));
                    editor.putString("DR_SALE_URL", c.getString("DR_SALE_URL"));
                    editor.commit();

                }

                for (int i = 0; i < table1.length(); i++) {
                    JSONObject object = table1.getJSONObject(i);
                    String menu = object.getString("MAIN_MENU");
                    String menu_code = object.getString("MENU_CODE");
                    String menu_name = object.getString("MENU_NAME");
                    String menu_url = object.getString("URL");
                    String main_menu_srno = object.getString("MAIN_MENU_SRNO");
                    cboDbHelper.insertMenu(menu, menu_code, menu_name, menu_url, main_menu_srno);
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String getRouteValue(Context mContext) {
        String result = "";
        SharedPreferences pref = mContext.getSharedPreferences(Custom_Variables_And_Method.FMCG_PREFRENCE, mContext.MODE_PRIVATE);
        result = pref.getString("root_needed", null);
        return result;
    }

}
