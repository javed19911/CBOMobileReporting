package com.cbo.cbomobilereporting.ui_new;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.report_activities.DOB_DOA;
import com.cbo.cbomobilereporting.ui_new.report_activities.DashboardReport;
import com.cbo.cbomobilereporting.ui_new.report_activities.DcrReports;
import com.cbo.cbomobilereporting.ui_new.report_activities.DrWiseVisit;
import com.cbo.cbomobilereporting.ui_new.report_activities.Logged_UnLogged;
import com.cbo.cbomobilereporting.ui_new.report_activities.MissedDoctor.MissedDoctorActivity;
import com.cbo.cbomobilereporting.ui_new.report_activities.Msg_ho;
import com.cbo.cbomobilereporting.ui_new.report_activities.Spo_Report;
import com.cbo.cbomobilereporting.ui_new.report_activities.TpReports;
import com.cbo.cbomobilereporting.ui_new.for_all_activities.CustomWebView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import utils.adapterutils.ReportMenu_Grid_Adapter;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by AKSHIT on 10/05/2016.
 */
public class ReportMenuInGrid extends Fragment {

    View v;
    Context context;
    NetworkUtil networkUtil;
    Custom_Variables_And_Method customVariablesAndMethod;
    ResultSet rs;

    GridView gridView;
    CBO_DB_Helper cboDbHelper;
    ArrayList<String> listOfAllTab;
    ArrayList<String> getKeyList = new ArrayList<>();
    Map<String,String> keyValue = new LinkedHashMap<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return v = inflater.inflate(R.layout.grid_menu_forall, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        networkUtil = new NetworkUtil(context);
        cboDbHelper = new CBO_DB_Helper(context);

        addTabInList();
        Custom_Variables_And_Method.CURRENTTAB=((ViewPager_2016) getActivity()).getTabIndex();

        gridView = (GridView) v.findViewById(R.id.grid_view_example);

        gridView.setAdapter(new ReportMenu_Grid_Adapter(context, listOfAllTab,getKeyList));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // TextView textView = (TextView) view.findViewById(R.id.text_src);
               // String itemLebel = textView.getText().toString();
                String itemLebel = getKeyList.get(position);
                String url=new CBO_DB_Helper(getActivity()).getMenuUrl("REPORTS",itemLebel);
                if(url!=null && !url.equals("")) {
                    Intent i = new Intent(getActivity(), CustomWebView.class);
                    i.putExtra("A_TP", url);
                    i.putExtra("Title", listOfAllTab.get(position));
                    startActivity(i);
                }else {
                    switch (itemLebel) {

                        case "R_LOGUNL": {
                            onClickLoggedUnlogged();
                            break;
                        }
                        case "R_DCRRPT": {
                            onClickDcrReports();

                            break;
                        }
                        case "R_DASH": {
                            onClickPrimarySales();

                            break;
                        }

                        case "R_DRWISE": {

                         /*   onClickDrWise();

                            break;
                        }
                        case "R_MISSDR": {*/
                            onClickMissedCall();
                            break;
                        }

                        case "R_TP": {

                            onClickTPReport();
                            break;
                        }

                        case "R_SPO": {

                            onClickSPOReport();
                            break;
                        }
                        case "DOB_DOA": {

                            onClickdob_doa();
                            break;
                        }
                        case "MSG_HO": {

                            onClickmsg_ho();
                            break;
                        }
                        default: {
                            url = new CBO_DB_Helper(getActivity()).getMenuUrl("REPORTS", getKeyList.get(position));
                            if (url != null && !url.equals("")) {
                                Intent i = new Intent(getActivity(), CustomWebView.class);
                                i.putExtra("A_TP", url);
                                i.putExtra("Title", listOfAllTab.get(position));
                                startActivity(i);
                            } else {
                                customVariablesAndMethod.msgBox(context, "Page Under Development");
                            }
                        }

                    }
                }


            }


        });


    }

    private void onClickmsg_ho() {
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {

            Intent i = new Intent(getActivity(), Msg_ho.class);
            //i.putExtra("msg_ho","http://www.yahoo.com");
           // Log.d("javed", new CBO_DB_Helper(context).getMenuUrl("REPORTS","MSG_HO"));
            i.putExtra("msg_ho", new CBO_DB_Helper(context).getMenuUrl("REPORTS","MSG_HO"));
            //i.putExtra("msg","1");
            startActivity(i);

            //mycon.msgBox("Under development.....");
        }

    }

    //////Add Data In List///
    private void addTabInList() {

        keyValue = cboDbHelper.getMenu("REPORTS","");
        listOfAllTab = new ArrayList<String>();
        for (String key : keyValue.keySet()) {
            getKeyList.add(key);
        }
        for (int i = 0; i < keyValue.size(); i++) {
            listOfAllTab.add(keyValue.get(getKeyList.get(i)));

        }

    }


    ///////////////////Dcr Reports/////////
    private void onClickLoggedUnlogged() {


        if (!networkUtil.internetConneted(context)) {

            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent i = new Intent(getActivity(), Logged_UnLogged.class);
            startActivity(i);
        }
    }

    ///////////////////Dcr Reports/////////
    private void onClickMissedCall() {


        if (!networkUtil.internetConneted(context)) {

            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent i = new Intent(getActivity(), MissedDoctorActivity.class);
            startActivity(i);
        }
    }

    ///////////////////Dcr Reports/////////
    private void onClickDrWise() {
        if (!networkUtil.internetConneted(getActivity())) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {
            Intent i = new Intent(getActivity(), DrWiseVisit.class);
            startActivity(i);
        }

    }

    ///////////////////Dcr Reports/////////
    private void onClickDcrReports() {

        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {
            Intent i = new Intent(getActivity(), DcrReports.class);
            startActivity(i);
        }

    }

    ///////////////////Dcr Reports/////////
    private void onClickPrimarySales() {


        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {

            Intent i = new Intent(getActivity(), DashboardReport.class);
            startActivity(i);
        }


    }

    ///////////////////Dcr Reports/////////
    private void onClickTPReport() {

        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {

            Intent i = new Intent(getActivity(), TpReports.class);

            startActivity(i);
        }


    }

    ///////////////////Dcr Reports/////////
    private void onClickSPOReport() {

        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {

            Intent spoReport = new Intent(getActivity(), Spo_Report.class);
            startActivity(spoReport);

            //mycon.msgBox("Under development.....");
        }


    }


    private void onClickdob_doa() {

        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {

            Intent spoReport = new Intent(getActivity(), DOB_DOA.class);
            startActivity(spoReport);

            //mycon.msgBox("Under development.....");
        }


    }


}
