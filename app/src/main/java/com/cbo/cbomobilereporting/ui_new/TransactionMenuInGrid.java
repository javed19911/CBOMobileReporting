package com.cbo.cbomobilereporting.ui_new;

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
import android.widget.GridView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.personal_activities.Add_Delete_Leave;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Complaint;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.ComplaintView;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Doctor_registration_GPS;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Farmer_registration_form;
import com.cbo.cbomobilereporting.ui_new.transaction_activities.Rcpa_Call;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import saleOrder.Activities.ClientActivity;
import utils.adapterutils.Transaction_Grid_Adapter;
import com.cbo.cbomobilereporting.MyCustumApplication;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by AKSHIT on 06/05/2016.
 */
public class TransactionMenuInGrid extends Fragment {


    NetworkUtil networkUtil;
    String fmcgYN;
    Context context;
    CBO_DB_Helper cboDbHelper;
    String leave_yn;
    View v;
    ArrayList<String> listOfAllTab;
    ArrayList<String> getKeyList = new ArrayList<>();
    Map<String,String> keyValue = new LinkedHashMap<>();
    Custom_Variables_And_Method customVariablesAndMethod;
    String MenuCode = "TRANSACTION";
    GridView gridView;



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
        cboDbHelper = new CBO_DB_Helper(context);
        networkUtil = new NetworkUtil(context);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        MenuCode = getArguments().getString("Code");


        fmcgYN = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"fmcg_value");
        leave_yn = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"leave_yn");
        addDataInList();
        Custom_Variables_And_Method.CURRENTTAB=((ViewPager_2016) getActivity()).getTabIndex();

        gridView.setAdapter(new Transaction_Grid_Adapter(context, listOfAllTab,getKeyList));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              String nameOnClick = getKeyList.get(position);
                String url=new CBO_DB_Helper(getActivity()).getMenuUrl(MenuCode,nameOnClick);
                if(url!=null && !url.equals("")) {
                    /*Intent i = new Intent(getActivity(), CustomWebView.class);
                    i.putExtra("A_TP", url);
                    i.putExtra("Title", listOfAllTab.get(position));
                    startActivity(i);*/
                    MyCustumApplication.getInstance().LoadURL(listOfAllTab.get(position),url);

                }else {
                    switch (nameOnClick) {

                        case "T_LR": {

                            onClickLeaveReq();
                            break;
                        }
                        /*case "T_SS": {
                            onClickSecSales(position);
                            break;
                        }*/
                        case "T_COMP": {
                            onClickComplaint();
                            break;
                        }

                        case "T_CV": {

                            onClickComplaintView();
                            break;
                        }

                        case "T_RCPA": {
                            onClickRCPA();
                            break;
                        }
                        case "T_FAR": {
                            if (Custom_Variables_And_Method.DCR_ID.equals("0")  || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                                customVariablesAndMethod.msgBox(context, "Please open your DCR Days first....");
                            } else {
                                onClickFarmerRegistor();
                            }
                            break;
                        }
                        case "T_SORD":
                            onClickSalesOrder();
                            break;

                        case "T_DRREG": {

                            onClickDoctorRegistration(position);

                            break;
                        }
                        case "T_ORDER": {

                            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.cbo.services.doctor");
                            if (intent != null) {
                                // We found the activity now start the activity
                                intent = new Intent(Intent.ACTION_VIEW);
                                intent.setAction("launch.me.action.LAUNCH_IT");
                                intent.putExtra("sCompanyFolder", cboDbHelper.getCompanyCode());
                                intent.putExtra("sUserName", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "USER_NAME", "4282"));
                                intent.putExtra("sPassword", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "PASSWORD", "cbo"));
                                intent.putExtra("expend_menu", "ORD");
                                startActivity(intent);

                            } else {
                                // Bring user to the market or let them choose an app?
                                intent = new Intent(Intent.ACTION_VIEW);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setData(Uri.parse("market://details?id=" + "com.cbo.services.doctor"));
                                startActivity(intent);
                            }

                            break;
                        }


                        default: {
                            url = new CBO_DB_Helper(context).getMenuUrl(MenuCode, getKeyList.get(position));
                            if (url != null && !url.equals("")) {
                                /*Intent i = new Intent(getActivity(), CustomWebView.class);
                                i.putExtra("A_TP", url);
                                i.putExtra("Title", listOfAllTab.get(position));
                                startActivity(i);*/
                                MyCustumApplication.getInstance().LoadURL(listOfAllTab.get(position),url);
                            } else {
                                Toast.makeText(context, "Page Under Development", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
    }
        });

    }


    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!getUserVisibleHint())
        {
            return;
        }

        addDataInList();
        //cboDbHelper.getDetailsForOffline();

        gridView.setAdapter(new Transaction_Grid_Adapter(context, listOfAllTab, getKeyList));

    }


    private void onClickSalesOrder() {

        Intent intent = new Intent(context, ClientActivity.class);
        startActivity(intent);

    }
////////////////////////
    public void addDataInList() {

        keyValue = cboDbHelper.getMenu(MenuCode,"");
        getKeyList = new ArrayList<>();
        listOfAllTab = new ArrayList<String>();
        for (String key : keyValue.keySet()) {
            getKeyList.add(key);
        }
        for (int i = 0; i < keyValue.size(); i++) {
            listOfAllTab.add(keyValue.get(getKeyList.get(i)));

        }
        /*getKeyList.add("T_ORDER");
        listOfAllTab.add("New Order");*/
    }


    ///////////onClickLeaveReq/////////////

    private void onClickLeaveReq() {


        if (!networkUtil.internetConneted(context)) {

            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent leaveRequestActivity = new Intent(getActivity(), Add_Delete_Leave.class);

            startActivity(leaveRequestActivity);
        }

    }

    ///////////

    ///////////onClickComplaint/////////////

    private void onClickComplaint() {

        if (!networkUtil.internetConneted(context)) {

            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent complainClass = new Intent(getActivity(), Complaint.class);
            startActivity(complainClass);
        }

    }

    ///////////

    ///////////onClickComplaintView/////////////

    private void onClickComplaintView() {
        if (!networkUtil.internetConneted(context)) {

            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent complaintView = new Intent(context, ComplaintView.class);
            startActivity(complaintView);
        }

    }

    ///////////

    ///////////onClickRCPA/////////////

    private void onClickRCPA() {
        String myDcrId = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_ID");
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            if ((!myDcrId.equals("Y")) && (!myDcrId.equals(null))) {
                Intent rcpaIntent = new Intent(getActivity(), Rcpa_Call.class);
                startActivity(rcpaIntent);
            } else {

                customVariablesAndMethod.msgBox(context,"Please DayPlan First....");

            }
        }

    }

    ///////////onClickFarmerRegistor/////////////

    private void onClickFarmerRegistor() {

        Cursor c = cboDbHelper.getFTPDATA();
        String myDcrId = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_ID");

        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            if ((!myDcrId.equals("N")) && (!myDcrId.equals(null)) && (!myDcrId.equals("0"))) {
                if (c.moveToFirst()) {
                    startActivity(new Intent(getActivity(), Farmer_registration_form.class));
                } else {

                    customVariablesAndMethod.msgBox(context,"Please Upload/Download First...");

                }
            } else {

                customVariablesAndMethod.msgBox(context,"Please DayPlan First....");

            }
        }
    }




    private void onClickDoctorRegistration(Integer position) {

        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            if (Custom_Variables_And_Method.DCR_ID.equals("0") || customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real").equals("")) {
                customVariablesAndMethod.msgBox(context,"Please open your DCR Days first.....");
            }else{
                Intent Intent = new Intent(getActivity(), Doctor_registration_GPS.class);
                startActivity(Intent);
            }
        }

    }

}
