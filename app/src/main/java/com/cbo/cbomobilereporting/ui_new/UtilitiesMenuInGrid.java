package com.cbo.cbomobilereporting.ui_new;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.databaseHelper.Sync.SyncAllDataFirebase;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DCR_Summary_new;
import com.cbo.cbomobilereporting.ui_new.utilities_activities.CaptureSignatureMain;
import com.cbo.cbomobilereporting.ui_new.utilities_activities.DivisionWise_Map;
import com.cbo.cbomobilereporting.ui_new.utilities_activities.DocPhotos;
import com.cbo.cbomobilereporting.ui_new.utilities_activities.PersonalInfo;
import com.cbo.cbomobilereporting.ui_new.utilities_activities.SyncFirebaseActivity;
import com.cbo.cbomobilereporting.ui_new.utilities_activities.Upload_Photo;
import com.cbo.cbomobilereporting.ui_new.utilities_activities.VisualAdsDownload.VisualAdsDownloadActivity;
import com.uenics.javed.CBOLibrary.Response;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import services.ServiceHandler;
import utils.adapterutils.Util_Grid_Adapter;
import com.cbo.cbomobilereporting.MyCustumApplication;
import utils.networkUtil.NetworkUtil;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.Service_Call_From_Multiple_Classes;

public class UtilitiesMenuInGrid extends Fragment {


    View v;
    Activity context;
    GridView gridView;
    String fmcgYN;
    NetworkUtil networkUtil;
    Custom_Variables_And_Method customVariablesAndMethod;
    int PA_ID;
    String pa_name = "";
    CBO_DB_Helper cbohelper;
    int count = 0;
    int chem_count = 0;
    CBO_DB_Helper cboDbHelper;
    ServiceHandler serviceHandler;
    String my_URL, visualAidYN;
    MyCustomMethod customMethod;
    ArrayList<String> listOfAllTab;
    ArrayList<String> getKeyList = new ArrayList<>();
    Map<String, String> keyValue = new LinkedHashMap<String, String>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.grid_menu_forall, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();


        gridView = (GridView) v.findViewById(R.id.grid_view_example);
        networkUtil = new NetworkUtil(context);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        fmcgYN =customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"fmcg_value");
        serviceHandler = new ServiceHandler(context);
        customMethod = new MyCustomMethod(context);
        cboDbHelper = new CBO_DB_Helper(context);
        PA_ID = Integer.parseInt(cboDbHelper.getPaid());
        pa_name = pa_name + cboDbHelper.getPaName();
        my_URL = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Login_Url");
        visualAidYN = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"VisualAid_YN");
        addDataInList();
        Custom_Variables_And_Method.CURRENTTAB=((ViewPager_2016) getActivity()).getTabIndex();

        //listOfAllTab.remove("Show Demo Kilometer");

       /* if ((cboDbHelper.getCompanyCode().equalsIgnoreCase("precia"))){
            listOfTab.remove("Map View");
        }
        if (visualAidYN.equalsIgnoreCase("Y")) {
            listOfTab.remove("DownLoad VisualAid");
            listOfTab.remove("Show VisualAid");
        }*/

        gridView.setAdapter(new Util_Grid_Adapter(context, listOfAllTab, getKeyList));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //TextView tagText = (TextView) view.findViewById(R.id.text_src);
                //String nameOnClick = tagText.getText().toString();
                String nameOnClick = getKeyList.get(position);

                String url=new CBO_DB_Helper(getActivity()).getMenuUrl("UTILITY",nameOnClick);
                if(url!=null && !url.equals("")) {
                    /*Intent i = new Intent(getActivity(), CustomWebView.class);
                    i.putExtra("A_TP", url);
                    i.putExtra("Title", listOfAllTab.get(position));
                    startActivity(i);*/
                    MyCustumApplication.getInstance().LoadURL(listOfAllTab.get(position),url);
                }else {
                    switch (nameOnClick) {

                        case "U_UPPIC": {

                            onClickUploadPic();
                            break;
                        }
                        case "U_UPDOWN": {
                            onClickUploadDownLoad();
                            break;
                        }
                        case "U_CAPSIGN": {
                            onClickCaptureSign();
                            break;
                        }

                        case "U_MAP": {

                            onClickMapView();
                            break;
                        }

                        case "U_WEB": {
                            onClickWebView();
                            break;
                        }
                        case "U_DOWNAID": {
                            onClickDownVisual();
                            break;
                        }

                        case "U_SHOWAID": {
                            onClickShowVisual();
                            break;
                        }
                        case "U_RESET": {

                            onClickResetDayplan();
                            break;
                        }
                        case "U_PI": {

                            onClickPI();
                            break;
                        }
                        case "U_SYNC": {

                            //onClickPI();
                            startActivity(new Intent(getActivity(), SyncFirebaseActivity.class));

                            break;
                        }
                        case "Show Demo Kilometer": {

                            // mycon.msgBox(mycon.currentTime());
                            // onClickDemoKilometer();
                        }
                        default: {
                            url = new CBO_DB_Helper(getActivity()).getMenuUrl("UTILITY", getKeyList.get(position));
                            if (url != null && !url.equals("")) {
                                /*Intent i = new Intent(getActivity(), CustomWebView.class);
                                i.putExtra("A_TP", url);
                                i.putExtra("Title", listOfAllTab.get(position));
                                startActivity(i);*/
                                MyCustumApplication.getInstance().LoadURL(listOfAllTab.get(position),url);
                            } else {
                                Toast.makeText(getActivity(), "Page Under Development", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                }

            }
        });


    }

    public void addDataInList() {

        keyValue = cboDbHelper.getMenu("UTILITY","");
        keyValue.put("U_SYNC","Sync data for Support");
        listOfAllTab = new ArrayList<String>();
        for (String key : keyValue.keySet()) {
            getKeyList.add(key);
        }
        for (int i = 0; i < keyValue.size(); i++) {
            listOfAllTab.add(keyValue.get(getKeyList.get(i)));

        }


    }
    ///////////onClickPI//////////////

    private void onClickPI() {

        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {
            startActivity(new Intent(getActivity(), PersonalInfo.class));


        }

    }


    //////////////

    ///////////////////onClickUpload Pic
    private void onClickUploadPic() {

        Cursor c = cboDbHelper.getFTPDATA();
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {
            if (c.moveToFirst()) {
                startActivity(new Intent(getActivity(), Upload_Photo.class));
            } else {

                customVariablesAndMethod.msgBox(context,"Please Upload/Download First....");
            }
        }
    }

    ///////////////////onClickUpload Pic
    private void onClickUploadDownLoad() {


        /*if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {
            new Doback_phitem().execute();
        }*/

        Service_Call_From_Multiple_Classes service =  new Service_Call_From_Multiple_Classes();

        service.getListForLocal(context, new Response() {
            @Override
            public void onSuccess(Bundle bundle) {

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"MAIL_STATUS","N");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DOB_DOA_notification_date","01/01/1970");

                if (Custom_Variables_And_Method.DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "dcr_date_real").equals("")) {
                    customVariablesAndMethod.msgBox(context, "Data Downloded Sucessfully...");
                    MyCustumApplication.getInstance().Logout(context);
                }else{

                    service.DownloadAll(context, new Response() {
                        @Override
                        public void onSuccess(Bundle bundle) {
                             customVariablesAndMethod.msgBox(context, "Data Downloded Sucessfully...");
                             MyCustumApplication.getInstance().Logout(context);
                        }

                        @Override
                        public void onError(String s, String s1) {
                            AppAlert.getInstance().getAlert(context,s,s1);
                        }
                    });
                }
            }

            @Override
            public void onError(String message, String description) {
                AppAlert.getInstance().getAlert(context,message,description);
            }
        });

    }

    ///////////////////onClickUpload Pic
    private void onClickResetDayplan() {


        if (networkUtil.internetConneted(context)) {
            if (Custom_Variables_And_Method.DCR_ID.equals("0")) {
                customVariablesAndMethod.msgBox(context,"Please Plan your Dcr Day..");
            } else {
                askForReset();
            }
        } else {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        }


    }



    ///////////////////onClickUpload Pic
    private void onClickCaptureSign() {

        startActivity(new Intent(getActivity(), CaptureSignatureMain.class));

    }

    ///////////////////onClickUpload Pic
    private void onClickMapView() {

        if (networkUtil.internetConneted(context)) {
            startActivity(new Intent(getActivity(), DivisionWise_Map.class));

        } else {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        }


    }

    ///////////////////onClickUpload Pic
    private void onClickWebView() {


        my_URL = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"Login_Url");
        cboDbHelper = new CBO_DB_Helper(getActivity());
        Cursor c = cboDbHelper.getFTPDATA();

        if (networkUtil.internetConneted(context)) {

            if (my_URL.equals(null) || my_URL.equals("Y")) {

                customVariablesAndMethod.msgBox(context,"Please Upload/DownLoad First...");

            } else {

                if (c.moveToFirst()) {
                    Intent i = new Intent(Intent.ACTION_VIEW,

                            //below uri send to play Store
                            Uri.parse(my_URL));

                    startActivity(i);
                } else {
                    customVariablesAndMethod.msgBox(context,"Upload Download First.....");
                }
            }
        } else {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        }


    }

    ///////////////////onClickUpload Pic
    private void onClickDownVisual() {


        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {
            //startActivity(new Intent(getActivity(), VisualAid_Download.class));
            startActivity(new Intent(getActivity(), VisualAdsDownloadActivity.class));

        }


    }
    ///////////////////onClickUpload Pic

    private void onClickShowVisual() {


        Intent i = new Intent(getActivity(), DocPhotos.class);
        i.putExtra("who",1);
        startActivity(i);


    }


   /*public class Doback_phitem extends AsyncTask<Void, Integer, Long> {
        ProgressDialog pd;
        private int progress;
        long val = 0;

        @Override
        protected Long doInBackground(Void... params) {

            cbohelper = new CBO_DB_Helper(getActivity());
            cbohelper.delete_phitem();
            //cbohelper.delete_phdoctoritem();
            //cbohelper.delete_phdoctor();
            cbohelper.delete_phallmst();
            cbohelper.delete_phparty();
            cbohelper.delete_phrelation();
            cbohelper.delete_phitemspl();
            cbohelper.deleteFTPTABLE();
            //cbohelper.deleteApprisalValues();
            //cbohelper.deleteChemist();
            //getcount();

            try {
                String response;
                serviceHandler = new ServiceHandler(getActivity());
                cboDbHelper = new CBO_DB_Helper(getActivity());

                response = serviceHandler.getResultFrom_GetItemListForLocal(cboDbHelper.getCompanyCode(), "" + PA_ID);
                if (!response.contains("[ERROR]")) {
                    Log.v("Result1", "Invalid" + response);

                    JSONObject comleteRsponse = new JSONObject(response);
                    JSONArray completeTable = comleteRsponse.getJSONArray("Tables");
                    JSONObject zero = completeTable.getJSONObject(0);
                    JSONObject one = completeTable.getJSONObject(1);
                    JSONObject two = completeTable.getJSONObject(2);
                    JSONObject three = completeTable.getJSONObject(3);
                    JSONObject four = completeTable.getJSONObject(4);
                    JSONObject five = completeTable.getJSONObject(5);
                    JSONObject six = completeTable.getJSONObject(6);
                    JSONObject seven = completeTable.getJSONObject(7);
                    JSONObject eight = completeTable.getJSONObject(8);
                    JSONObject nine = completeTable.getJSONObject(9);
                    JSONObject ten = completeTable.getJSONObject(11);

                    JSONArray jsonArray0 = zero.getJSONArray("Tables0");
                    JSONArray jsonArray1 = one.getJSONArray("Tables1");
                    JSONArray jsonArray2 = two.getJSONArray("Tables2");
                    JSONArray jsonArray3 = three.getJSONArray("Tables3");
                    JSONArray jsonArray4 = four.getJSONArray("Tables4");
                    JSONArray jsonArray5 = five.getJSONArray("Tables5");
                    JSONArray jsonArray6 = six.getJSONArray("Tables6");
                    JSONArray jsonArray7 = seven.getJSONArray("Tables7");
                    JSONArray jsonArray8 = eight.getJSONArray("Tables8");
                    JSONArray jsonArray9 = nine.getJSONArray("Tables9");
                    JSONArray jsonArray10 = ten.getJSONArray("Tables11");

                    for (int a = 0; a < jsonArray0.length(); a++) {
                        JSONObject jasonObj1 = jsonArray0.getJSONObject(a);
                        val = cbohelper.insertProducts(jasonObj1.getString("ITEM_ID"), jasonObj1.getString("ITEM_NAME"),
                                Double.parseDouble(jasonObj1.getString("STK_RATE")), jasonObj1.getString("GIFT_TYPE"),
                                jasonObj1.getString("SHOW_ON_TOP"),jasonObj1.getString("SHOW_YN"),
                                jasonObj1.getInt("SPL_ID"));
                        Log.e("%%%%%%%%%%%%%%%", "item insert");

                    }

                    *//*for (int b = 0; b<jsonArray2.length();b++){
                        JSONObject jasonObj2 = jsonArray2.getJSONObject(b);
                        val=cbohelper.insertDoctorData(jasonObj2.getString("DR_ID"), jasonObj2.getString("ITEM_ID"),jasonObj2.getString("item_name"));
                        Log.e("%%%%%%%%%%%%%%%", "doctor insert");

                    }*//*

                    for (int c = 0; c < jsonArray3.length(); c++) {
                        JSONObject jsonObject3 = jsonArray3.getJSONObject(c);
                        val = cbohelper.insert_phallmst(jsonObject3.getInt("ID"), jsonObject3.getString("TABLE_NAME"), jsonObject3.getString("FIELD_NAME"), jsonObject3.getString("REMARK"));
                        Log.e("%%%%%%%%%%%%%%%", "allmst_insert");
                    }

                    for (int d = 0; d < jsonArray4.length(); d++) {
                        JSONObject jsonObject4 = jsonArray4.getJSONObject(d);
                        val = cbohelper.insert_phparty(jsonObject4.getInt("PA_ID"), jsonObject4.getString("PA_NAME"),
                                jsonObject4.getInt("DESIG_ID"), jsonObject4.getString("CATEGORY"),
                                jsonObject4.getInt("HQ_ID"), jsonObject4.getString("PA_LAT_LONG"),
                                jsonObject4.getString("PA_LAT_LONG2"), jsonObject4.getString("PA_LAT_LONG3"),
                                jsonObject4.getString("SHOWYN"));
                        Log.e("%%%%%%%%%%%%%%%", "party_insert");
                    }

                    for (int e = 0; e < jsonArray5.length(); e++) {

                        JSONObject jsonObject5 = jsonArray5.getJSONObject(e);
                        val = cbohelper.insert_phrelation(jsonObject5.getInt("PA_ID"), jsonObject5.getInt("UNDER_ID"), jsonObject5.getInt("RANK"));
                        Log.e("%%%%%%%%%%%%%%%", "relation_insert");

                    }

                    for (int f = 0; f < jsonArray6.length(); f++) {

                        JSONObject jsonObject6 = jsonArray6.getJSONObject(f);
                        val = cbohelper.insert_phitempl(jsonObject6.getString("ITEM_ID"), jsonObject6.getString("DR_SPL_ID"), jsonObject6.getInt("SRNO"));
                        Log.e("%%%%%%%%%%%%%%%", "" + jsonObject6.getInt("SRNO"));


                    }

                    for (int f = 0; f < jsonArray7.length(); f++) {
                        JSONObject jsonObject7 = jsonArray7.getJSONObject(f);
                        val = cbohelper.insert_FtpData(jsonObject7.getString("WEB_IP"), jsonObject7.getString("WEB_USER"), jsonObject7.getString("WEB_PWD"), jsonObject7.getString("WEB_PORT"), jsonObject7.getString("WEB_ROOT_PATH"),
                                jsonObject7.getString("WEB_IP_DOWNLOAD"), jsonObject7.getString("WEB_USER_DOWNLOAD"), jsonObject7.getString("WEB_PWD_DOWNLOAD"), jsonObject7.getString("WEB_PORT_DOWNLOAD"));
                        Log.e("%%%%%%%%%%%%%%%", "ftp_insert");
                    }

                    for (int g = 0; g < jsonArray9.length(); g++) {
                        JSONObject jsonObject9 = jsonArray9.getJSONObject(g);
                        count = jsonObject9.getInt("NO_DR");
                        chem_count = jsonObject9.getInt("NO_CHEM");
                    }

                    JSONObject jsonObjectLoginUrl = jsonArray10.getJSONObject(0);
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"Login_Url", jsonObjectLoginUrl.getString("LOGIN_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DR_ADDNEW_URL", jsonObjectLoginUrl.getString("DR_ADDNEW_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHEM_ADDNEW_URL", jsonObjectLoginUrl.getString("CHEM_ADDNEW_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DRSALE_ADDNEW_URL", jsonObjectLoginUrl.getString("DRSALE_ADDNEW_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"TP_ADDNEW_URL", jsonObjectLoginUrl.getString("TP_ADDNEW_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHALLAN_ACK_URL", jsonObjectLoginUrl.getString("CHALLAN_ACK_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"SECONDARY_SALE_URL", jsonObjectLoginUrl.getString("SECONDARY_SALE_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"TP_APPROVE_URL", jsonObjectLoginUrl.getString("TP_APPROVE_URL"));

                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"PERSONAL_INFORMATION_URL", jsonObjectLoginUrl.getString("PERSONAL_INFORMATION_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHANGE_PASSWORD_URL", jsonObjectLoginUrl.getString("CHANGE_PASSWORD_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CIRCULAR_URL", jsonObjectLoginUrl.getString("CIRCULAR_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DECLARATION_OF_SAVING_URL", jsonObjectLoginUrl.getString("DECLARATION_OF_SAVING_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"SALARY_SLIP_URL", jsonObjectLoginUrl.getString("SALARY_SLIP_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"FORM16_URL", jsonObjectLoginUrl.getString("FORM16_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ROUTE_MASTER_URL", jsonObjectLoginUrl.getString("ROUTE_MASTER_URL"));
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"HOLIDAY_URL", jsonObjectLoginUrl.getString("HOLIDAY_URL"));

                    SharedPreferences.Editor editor = context.getSharedPreferences(Custom_Variables_And_Method.FMCG_PREFRENCE, context.MODE_PRIVATE).edit();
                    JSONObject jsonObject1 = completeTable.getJSONObject(12);
                    JSONObject jsonObject2 = completeTable.getJSONObject(13);
                    JSONArray table0 = jsonObject1.getJSONArray("Tables12");
                    JSONArray table1 = jsonObject2.getJSONArray("Tables13");
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
                        editor.putString("REG_ADDRESS_KM", c.getString("REG_ADDRESS_KM"));
                        editor.putString("DR_DIVISION_FILTER_YN", c.getString("DR_DIVISION_FILTER_YN"));

                        editor.commit();

                    }

                    cboDbHelper.deleteMenu();
                    for (int i = 0; i < table1.length(); i++) {
                        JSONObject object = table1.getJSONObject(i);
                        String menu = object.getString("MAIN_MENU");
                        String menu_code = object.getString("MENU_CODE");
                        String menu_name = object.getString("MENU_NAME");
                        String menu_url = object.getString("URL");
                        String main_menu_srno = object.getString("MAIN_MENU_SRNO");
                        cboDbHelper.insertMenu(menu, menu_code, menu_name, menu_url, main_menu_srno);
                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //cbohelper.close();

            return val;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setTitle("CBO");
            pd.setMessage("Downloading Miscellaneous data.." + "\n" + "please wait");
            //pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(false);

            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

			*//*--- show download progress on main UI thread---*//*

            pd.setProgress((values[0]) * 100 / count);

            //pd.setText((values[0]*100)/count +" % downloaded");
            pd.setMessage((values[0] * 100) / count + " % downloaded");

            super.onProgressUpdate(values);
            Log.e("mycount", "" + count);

        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            //mycon.msgBox(""+result);
            //mycon.msgBox(""+chem_count);

            if (result > 0) {
                //new Doback_phDoctorMaster().execute();
                customVariablesAndMethod.msgBox(context,"Data Downloded Sucessfully...");
            }
            pd.dismiss();


        }

    }*/


    public void askForReset() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogLayout = inflater.inflate(R.layout.update_available_alert_view, null);
        final TextView Alert_title = (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message = (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive = (Button) dialogLayout.findViewById(R.id.positive);
        final Button Alert_Nagative = (Button) dialogLayout.findViewById(R.id.nagative);

        if (IscallsFound()){
            Alert_Nagative.setText("Continue..");
            Alert_message.setText("Some Calls found !!!!\nAre you sure to Reset your Dcr\nAll Calls will be Deleted...");
        }else {
            Alert_Nagative.setText("Reset");
            Alert_message.setText("Are Sure to Reset your DCR ?");
        }

        Alert_Positive.setText("Cancel");
        Alert_title.setText("Reset DCR!!!");


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Alert_Nagative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IscallsFound()){
                    Intent i = new Intent(context, DCR_Summary_new.class);
                    i.putExtra("who", 2);
                    startActivity(i);
                    dialog.dismiss();
                }else {
                    //new ResetTask().execute();
                    new Service_Call_From_Multiple_Classes().resetDCR(context, new Response() {
                        @Override
                        public void onSuccess(Bundle bundle) {
                            //customVariablesAndMethod.msgBox(context,"Data Downloded Sucessfully...");
                        }

                        @Override
                        public void onError(String message, String description) {
                            AppAlert.getInstance().getAlert(context,message,description);
                        }
                    });
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean IscallsFound() {
        int result=cboDbHelper.getmenu_count("phdcrdr_rc");
        result+=cboDbHelper.getmenu_count("tempdr");
        result+=cboDbHelper.getmenu_count("chemisttemp");
        result+=cboDbHelper.getmenu_count("phdcrstk");
        result+=cboDbHelper.getmenu_count("NonListed_call");
        result+=cboDbHelper.getmenu_count("Tenivia_traker");
        return result>0;
    }

    //////////////////////////////
    /*class ResetTask extends AsyncTask<String, String, String> {

        ProgressDialog myProgress;

        String result;        // CBO_DB_Helper cboHelpCompany;

        String resetSatus;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgress = new ProgressDialog(getActivity());
            myProgress.setTitle("CBO");
            myProgress.setMessage("Please Wait...");
            myProgress.setCanceledOnTouchOutside(false);
            myProgress.setCancelable(false);
            myProgress.show();


        }


        @Override
        protected String doInBackground(String... params) {


            try {
                cbohelper = new CBO_DB_Helper(getActivity());

                String companyCode = cbohelper.getCompanyCode();

                serviceHandler = new ServiceHandler(getActivity());

                result = serviceHandler.getResponse_ResetTask(companyCode, Custom_Variables_And_Method.DCR_ID);

                cbohelper.deleteLogin();
                //cbohelper.close();
            } catch (Exception e) {
                return "ERROR apk "+e;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if ((s == null) || (s.toLowerCase().contains("error"))) {
                customVariablesAndMethod.msgBox(context,"Failed to Reset Please Try Again");
                myProgress.dismiss();


            } else {
                try {

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("Tables0");
                    JSONObject c = jsonArray.getJSONObject(0);
                    resetSatus = c.getString("DCRID");


                } catch (JSONException e) {

                }
                if (resetSatus.equals("RESET")) {
                    myProgress.dismiss();
                    customVariablesAndMethod.msgBox(context,"Dcr Day Successfully Reset ");
                    new CBOFinalTasks(getActivity()).releseResources();

                    ((CustomActivity) context).stopLoctionService();

                    customMethod.stopAlarm10Minute();
                    customMethod.stopAlarm10Sec();
                    customMethod.stopDOB_DOA_Remainder();
                    new CustomTextToSpeech().stopTextToSpeech();

                    cbohelper.deleteLoginDetail();
                    cbohelper.deleteLogin();
                    cbohelper.deleteUserDetail();
                    cbohelper.deleteUserDetail2();
                    cbohelper.deleteDCRDetails();
                    cbohelper.deleteTempDr();
                    cbohelper.deletedcrFromSqlite();
                    cbohelper.deleteResigned();
                    cbohelper.deleteDoctorRc();
                    cbohelper.deleteDoctorItem();
                    cbohelper.deleteDoctorItemPrescribe();
                    cbohelper.deleteDoctor();
                    cbohelper.deleteFinalDcr();
                    cbohelper.deleteTempChemist();
                    cbohelper.deleteChemistSample();
                    cbohelper.deleteChemistRecordsTable();
                    cbohelper.deleteStockistRecordsTable();
                    cbohelper.deleteTempStockist();
                    cbohelper.deleteDoctormore();

                    cbohelper.delete_Expense();
                    cbohelper.delete_Nonlisted_calls();
                    cbohelper.deleteDcrAppraisal();
                    cbohelper.delete_tenivia_traker();
                    cbohelper.notificationDeletebyID(null);
                    cbohelper.delete_Lat_Long_Reg();
                    cbohelper.delete_phdairy_dcr(null);
                    cbohelper.delete_Item_Stock();

                    Custom_Variables_And_Method.DCR_ID = "0";
                    MyCustumApplication.getInstance().clearApplicationData();

                    Intent i = new Intent(getActivity(), LoginMain.class);

                    startActivity(i);
                    getActivity().finish();
                } else {
                    myProgress.dismiss();
                    customVariablesAndMethod.msgBox(context,"Day plan First......");
                }

            }

        }


    }*/

}
