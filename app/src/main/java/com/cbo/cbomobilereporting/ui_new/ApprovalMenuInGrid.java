package com.cbo.cbomobilereporting.ui_new;

import android.content.Context;
import android.content.Intent;
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
import com.cbo.cbomobilereporting.ui_new.approval_activities.Remainder.RemainderActivity;
import com.cbo.cbomobilereporting.ui_new.for_all_activities.CustomWebView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import utils.adapterutils.Approval_Adapter;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by javed on 28/10/2016.
 */
public class ApprovalMenuInGrid extends Fragment {

    View v;
    Context context;
    NetworkUtil networkUtil;
    Custom_Variables_And_Method customVariablesAndMethod;
    ResultSet rs;

    GridView gridView;
    CBO_DB_Helper cboDbHelper;
    ArrayList<String> listOfAllTab;
    ArrayList<Integer> count;
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
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance(context);
        networkUtil = new NetworkUtil(context);
        cboDbHelper = customVariablesAndMethod.get_cbo_db_instance();

        addTabInList();
        Custom_Variables_And_Method.CURRENTTAB=((ViewPager_2016) getActivity()).getTabIndex();

        gridView = (GridView) v.findViewById(R.id.grid_view_example);

        gridView.setAdapter(new Approval_Adapter(context, listOfAllTab,getKeyList,count));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // TextView textView = (TextView) view.findViewById(R.id.text_src);
               // String itemLebel = textView.getText().toString();



                String itemLebel = getKeyList.get(position);
                String url=new CBO_DB_Helper(getActivity()).getMenuUrl("APPROVAL",itemLebel);
                if(url!=null && !url.equals("")) {
                    Intent i = new Intent(getActivity(), CustomWebView.class);
                    i.putExtra("A_TP", url);
                    i.putExtra("Title", listOfAllTab.get(position));
                    startActivity(i);
                }else {
                    switch (itemLebel) {

                        case "A_TP": {

                            onClickmsg_ho(position);
                            break;
                        }case "A_REM": {

                            onClickRemainder(position);
                            break;
                        }default: {
                            url = cboDbHelper.getMenuUrl("APPROVAL", getKeyList.get(position));
                            if (url != null && !url.equals("")) {
                                Intent i = new Intent(getActivity(), CustomWebView.class);
                                i.putExtra("A_TP1", url);
                                i.putExtra("Menu_code", getKeyList.get(position));
                                i.putExtra("Title", listOfAllTab.get(position));
                                startActivity(i);
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					        context.startActivity(browserIntent);

                                //String url = ¨https://paul.kinlan.me/¨;
                          /*  CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            CustomTabsIntent customTabsIntent = builder.build();
                            customTabsIntent.launchUrl(getActivity(), Uri.parse(url));*/

                            } else {
                                Toast.makeText(context, "Page Under Development", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                }


            }


        });


    }

    private void onClickRemainder(Integer position) {
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {

            Intent i = new Intent(context, RemainderActivity.class);
            //i.putExtra("isAlertDialog", CustomActivity.activityType.DIALOG.getValue());
            i.putExtra("Title", listOfAllTab.get(position));

            startActivity(i);

            /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cboDbHelper.getMenuUrl("APPROVAL","A_TP")));
            context.startActivity(browserIntent);*/

            //mycon.msgBox("Under development.....");
        }

    }

    private void onClickmsg_ho(Integer position) {
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);
        } else {

            Intent i = new Intent(context, CustomWebView.class);
            i.putExtra("A_TP1", cboDbHelper.getMenuUrl("APPROVAL","A_TP"));
            i.putExtra("Menu_code", getKeyList.get(position));
            i.putExtra("Title", listOfAllTab.get(position));

            startActivity(i);

            /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cboDbHelper.getMenuUrl("APPROVAL","A_TP")));
            context.startActivity(browserIntent);*/

            //mycon.msgBox("Under development.....");
        }

    }

    //////Add Data In List///
    private void addTabInList() {

        keyValue = cboDbHelper.getMenu("APPROVAL","");
        listOfAllTab = new ArrayList<String>();
        count = new ArrayList<Integer>();
        for (String key : keyValue.keySet()) {
            getKeyList.add(key);
            count.add(get_count(key));
        }
        for (int i = 0; i < keyValue.size(); i++) {
            listOfAllTab.add(keyValue.get(getKeyList.get(i)));

        }

        //customVariablesAndMethod.getAlert(context,"Update Approval count","Do you want to update approval count");
    }

    private int get_count(String menu){
        int result=0;
        if(!menu.equals("")){
            result=Integer.parseInt(cboDbHelper.get_Approval_count(menu));
        }
        return result;
    }



}
