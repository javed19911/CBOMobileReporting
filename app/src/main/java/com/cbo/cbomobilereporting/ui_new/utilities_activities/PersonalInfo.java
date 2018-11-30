package com.cbo.cbomobilereporting.ui_new.utilities_activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.for_all_activities.CustomWebView;
import com.cbo.cbomobilereporting.ui_new.personal_activities.Add_Delete_Leave;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import utils.adapterutils.Personal_info_Adapter;
import utils.networkUtil.NetworkUtil;
import utils_new.Custom_Variables_And_Method;


/**
 * Created by AKSHIT on 6/17/16.
 */
public class PersonalInfo extends AppCompatActivity {

    ImageView imgChangePWD, imSalarySlip, imgCircular, imgDeclaration, imgPI, imgFrom16, imgHoliday;
    LinearLayout imgLeave;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    NetworkUtil networkUtil;
    GridView gridView;

    ArrayList<String> listOfAllTab;
    ArrayList<String> getKeyList = new ArrayList<>();
    Map<String, String> keyValue = new LinkedHashMap<String, String>();

    CBO_DB_Helper cboDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            textView.setText("Personal Information");
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }
        context = PersonalInfo.this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

        networkUtil = new NetworkUtil(context);
        cboDbHelper = new CBO_DB_Helper(context);

        addDataInList();
        gridView = (GridView) findViewById(R.id.grid_view_example);


        gridView.setAdapter(new Personal_info_Adapter(context, listOfAllTab, getKeyList));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //TextView tagText = (TextView) view.findViewById(R.id.text_src);
                //String nameOnClick = tagText.getText().toString();
                String nameOnClick = getKeyList.get(position);
                switch (nameOnClick) {

                    case "CP": {

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
                    }
                    case "LEAVE": {

                        onClickLeaveReq();
                        break;
                    }


                }

            }
        });

    }

    private void onClickChngPass(Integer position) {
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
            i.putExtra("SALARY_SLIP_URL",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SALARY_SLIP_URL"));
            i.putExtra("Title", listOfAllTab.get(position));
            startActivity(i);
        }
    }

    private void onClickCircular(Integer position) {
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent i = new Intent(context, CustomWebView.class);
            i.putExtra("CIRCULAR_URL",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"CIRCULAR_URL"));
            i.putExtra("Title", listOfAllTab.get(position));
            startActivity(i);
        }
    }

    private void onClickDecSav(Integer position) {
        if (!networkUtil.internetConneted(context)) {
            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent i = new Intent(context, CustomWebView.class);
            i.putExtra("DECLARATION_OF_SAVING_URL",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DECLARATION_OF_SAVING_URL"));
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
            i.putExtra("FORM16_URL",customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FORM16_URL"));
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
    }

    private void onClickLeaveReq() {

        if (!networkUtil.internetConneted(context)) {

            customVariablesAndMethod.Connect_to_Internet_Msg(context);

        } else {
            Intent leaveRequestActivity = new Intent(context, Add_Delete_Leave.class);

            startActivity(leaveRequestActivity);
        }
    }


    public void addDataInList() {

        keyValue = cboDbHelper.getMenu("PERSONAL_INFO","");
        listOfAllTab = new ArrayList<String>();
        for (String key : keyValue.keySet()) {
            getKeyList.add(key);
        }
        for (int i = 0; i < keyValue.size(); i++) {
            listOfAllTab.add(keyValue.get(getKeyList.get(i)));

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            //startActivity(new Intent(PersonalInfo.this, LoginFake.class));
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
