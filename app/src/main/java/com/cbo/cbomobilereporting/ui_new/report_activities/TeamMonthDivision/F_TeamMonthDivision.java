package com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.M_TeamMonthDivision;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.mDivision;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.mMissedFilter;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.mMonth;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model.mUser;

import services.CboServices;
import utils_new.AppAlert;
import utils_new.CustomError;
import utils_new.Custom_Variables_And_Method;

public class F_TeamMonthDivision extends Fragment implements VM_TeamMonthDivision.ITeam {
    public Button name, month, missed_type, division;
    VM_TeamMonthDivision dcrmodel;
    CBO_DB_Helper cboDbHelper;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    public ProgressDialog progress1;
    ImageView spinImgName,spinImgMonth,img_missed_type;
    mMissedFilter missedFilter;
    CustomError customError;
    F_TeamMonthDivision f_teamMonthDivision;

    @Override
    public void onStart() {
        super.onStart();
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
        progress1 = new ProgressDialog(getContext());
        cboDbHelper = new CBO_DB_Helper(getContext());
        dcrmodel= ViewModelProviders.of(getActivity()).get(VM_TeamMonthDivision.class);
        dcrmodel.setListner(this);


        name.setOnClickListener(v -> dcrmodel.onClickName(context));
        month.setOnClickListener(v -> dcrmodel.onClickMonth(context));
        missed_type.setOnClickListener(v -> dcrmodel.onMissedTypeClicked(context));
        img_missed_type.setOnClickListener(v -> dcrmodel.onMissedTypeClicked(context));

        spinImgName.setOnClickListener(v -> dcrmodel.onClickName(context));

        spinImgMonth.setOnClickListener(v -> dcrmodel.onClickMonth(context));
        dcrmodel.GET_NAME_MONTH(context, new VM_TeamMonthDivision.OnResultlistener() {
            @Override
            public void onSuccess(M_TeamMonthDivision teamMonthDivision) {
                //GET_NAME_MONTH_VALUE();
            }

            @Override
            public void onError(String Title,String error) {
                AppAlert.getInstance().getAlert(context,Title,error);
            }
        });




        }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.fragment_team_month_division, container, false);

        name = retView.findViewById(R.id.rptt_name);
        month = retView.findViewById(R.id.month_name);
        missed_type = retView.findViewById(R.id.missed_type);
        spinImgName =  retView.findViewById(R.id.spinner_img_dcr_rpt_name);
        spinImgMonth = retView.findViewById(R.id.spinner_img_dcr_rpt_month);
        img_missed_type =  retView.findViewById(R.id.img_missed_type);
        context = getContext();
        name.setText("---Select---");
        month.setText("---Select Month---");
        missed_type.setText("---Select Type---");
        return  retView;
    }

    public VM_TeamMonthDivision getViewModel(){
        return dcrmodel;
    }

    @Override
    public void OnMonthSelected(mMonth mmonth) {
        month.setText(mmonth.getName());
    }

    @Override
    public void OnNameSelected(mUser muser) {
        name.setText(muser.getName());
    }

    @Override
    public void OnMissedTypeSelected(mMissedFilter missedFilter) {

        missed_type.setText(missedFilter.getName());
    }



    @Override
    public void OnDivisionSelected(mDivision division) {

    }




}
