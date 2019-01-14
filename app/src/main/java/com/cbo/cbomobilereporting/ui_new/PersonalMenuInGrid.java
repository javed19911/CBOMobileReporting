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
import com.cbo.cbomobilereporting.ui_new.personal_activities.Add_Delete_Leave;
import com.cbo.cbomobilereporting.ui_new.report_activities.Msg_ho;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import utils.adapterutils.Personal_info_Adapter;
import utils.clearAppData.MyCustumApplication;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by javed on 28/10/2016.
 */
public class PersonalMenuInGrid extends Fragment {

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

        gridView.setAdapter(new Personal_info_Adapter(context, listOfAllTab,getKeyList));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // TextView textView = (TextView) view.findViewById(R.id.text_src);
               // String itemLebel = textView.getText().toString();



                String itemLebel = getKeyList.get(position);

                String url=new CBO_DB_Helper(getActivity()).getMenuUrl("PERSONAL_INFO",itemLebel);
                if(url!=null && !url.equals("")) {
                   /* Intent i = new Intent(getActivity(), CustomWebView.class);
                    i.putExtra("A_TP", url);
                    i.putExtra("Title", listOfAllTab.get(position));
                    startActivity(i);*/
                    MyCustumApplication.getInstance().LoadURL(listOfAllTab.get(position),url);
                }else {

                    switch (itemLebel) {

                        /*case "CP": {

                            onClickChngPass(position);
                            break;
                        }
                        case "SS": {
                            onClickSalarySlip(position);
                            break;
                        }
                        case "CIR": {
                            onClickCircular(position);
                            break;
                        }

                        case "DECL": {

                            onClickDecSav(position);
                            break;
                        }

                        case "IP": {
                            onClickPI(position);
                            break;
                        }
                        case "FORM16": {
                            onClickForm16(position);
                            break;
                        }

                        case "HL": {
                            onClickHolidayList(position);
                            break;
                        }*/
                        case "LEAVE": {

                            onClickLeaveReq();
                            break;
                        }
                        default: {
                            url = new CBO_DB_Helper(getActivity()).getMenuUrl("PERSONAL_INFO", getKeyList.get(position));
                            if (url != null && !url.equals("")) {
                                /*Intent i = new Intent(getActivity(), CustomWebView.class);
                                i.putExtra("A_TP", url);
                                i.putExtra("Title", listOfAllTab.get(position));
                                startActivity(i);*/
                                MyCustumApplication.getInstance().LoadURL(listOfAllTab.get(position),url);
                            } else {
                                customVariablesAndMethod.msgBox(context, "Page Under Development");
                            }
                        }


                    }

                }

            }


        });


    }



    //////Add Data In List///
    private void addTabInList() {

        keyValue = cboDbHelper.getMenu("PERSONAL_INFO","");
        listOfAllTab = new ArrayList<String>();
        for (String key : keyValue.keySet()) {
            getKeyList.add(key);
        }
        for (int i = 0; i < keyValue.size(); i++) {
            listOfAllTab.add(keyValue.get(getKeyList.get(i)));

        }
        //getKeyList.add("MSG_HO");
       // listOfAllTab.add("MSG_HO");
        /*listOfTab = new ArrayList<String>();
        listOfTab.add("Logged/Unlogged");
        listOfTab.add("Dcr Reports");
        listOfTab.add("Dashboard");
        listOfTab.add("Dr. Wise Visit");
        listOfTab.add("T.P. Report");
        listOfTab.add("S.P.O. Report");*/
    }


    /*private void onClickChngPass(Integer position) {
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent i = new Intent(context, CustomWebView.class);
            i.putExtra("CHANGE_PASSWORD_URL", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CHANGE_PASSWORD_URL"));
            i.putExtra("Title", listOfAllTab.get(position));
            startActivity(i);
        }
    }

    private void onClickSalarySlip(Integer position) {
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent i = new Intent(context, CustomWebView.class);
            i.putExtra("SALARY_SLIP_URL", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SALARY_SLIP_URL"));
            i.putExtra("Title", listOfAllTab.get(position));
            startActivity(i);
        }
    }

    private void onClickCircular(Integer position) {
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent i = new Intent(context, CustomWebView.class);
            i.putExtra("CIRCULAR_URL", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CIRCULAR_URL"));
            i.putExtra("Title", listOfAllTab.get(position));
            startActivity(i);
        }
    }

    private void onClickDecSav(Integer position) {
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent i = new Intent(context, CustomWebView.class);
            i.putExtra("DECLARATION_OF_SAVING_URL", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DECLARATION_OF_SAVING_URL"));
            i.putExtra("Title", listOfAllTab.get(position));
            startActivity(i);
        }
    }

    private void onClickPI(Integer position) {
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent i = new Intent(context, CustomWebView.class);
            i.putExtra("PERSONAL_INFORMATION_URL", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"PERSONAL_INFORMATION_URL"));
            i.putExtra("Title", listOfAllTab.get(position));
            startActivity(i);
        }
    }

    private void onClickForm16(Integer position) {
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent i = new Intent(context, CustomWebView.class);
            i.putExtra("FORM16_URL", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FORM16_URL"));
            i.putExtra("Title", listOfAllTab.get(position));
            startActivity(i);
        }
    }

    private void onClickHolidayList(Integer position) {
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent i = new Intent(context, CustomWebView.class);
            i.putExtra("HOLIDAY_URL", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"HOLIDAY_URL"));
            i.putExtra("Title", listOfAllTab.get(position));
            startActivity(i);
        }
    }*/

    private void onClickLeaveReq() {

        if (!networkUtil.internetConneted(context)) {

            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent leaveRequestActivity = new Intent(context, Add_Delete_Leave.class);

            startActivity(leaveRequestActivity);
        }
    }


}
