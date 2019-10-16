package com.cbo.cbomobilereporting.ui_new;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
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
import utils.networkUtil.NetworkUtil;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.Service_Call_From_Multiple_Classes;

public class UtilitiesMenuInGrid extends Fragment {


    View v;
    AppCompatActivity context;
    GridView gridView;
    String fmcgYN;
    NetworkUtil networkUtil;
    Custom_Variables_And_Method customVariablesAndMethod;
    int PA_ID;
    String pa_name = "";
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
        context = (AppCompatActivity) getActivity();


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
        if (MyCustumApplication.getInstance().getUser().getLoggedInAsSupport()){
            AppAlert.getInstance().getAlert(context,"Logged-In as Support!!!","You are not allowed to Reset DCR....");
        }else if (networkUtil.internetConneted(context)) {
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
