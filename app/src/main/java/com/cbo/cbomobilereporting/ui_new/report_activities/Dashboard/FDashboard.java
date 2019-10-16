package com.cbo.cbomobilereporting.ui_new.report_activities.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.adapterutils.ExpandableDashboardAdapter;
import utils_new.UnderlineTextView;

public class FDashboard extends Fragment implements iDashboard {

    private vmDashboard mViewModel;
    UnderlineTextView previous,next;
    TextView month_txt;
    ExpandableListView doctor;
    ExpandableDashboardAdapter listAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fdashboard_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(vmDashboard.class);
        mViewModel.setView((AppCompatActivity) getContext(),this);
    }

    @Override
    public void getReferencesById() {
        doctor = (ExpandableListView) getView().findViewById(R.id.summary_list);
        previous= (UnderlineTextView) getView().findViewById(R.id.previous);
        next= (UnderlineTextView) getView().findViewById(R.id.next);
        month_txt= (TextView) getView().findViewById(R.id.month);

        previous.setText("<<");
        next.setText(">>");

    }

    @Override
    public void setOnClickListeners() {

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.showPrevious(getContext());
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.showNext(getContext());
            }
        });

        doctor.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                /*if (expandableListView.isGroupExpanded(groupPosition)){
                    visible_status.set(groupPosition,0);
                }else{
                    visible_status.set(groupPosition,1);
                }*/
                return false;
            }
        });

    }

    @Override
    public String getCompanyCode() {
        return MyCustumApplication.getInstance().getUser().getCompanyCode();
    }

    @Override
    public String getUserId() {
        return MyCustumApplication.getInstance().getUser().getID();
    }

    @Override
    public void setNextBtnVisibility(Boolean visible) {
        next.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void setPreBtnVisibility(Boolean visible) {
        previous.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void setMonth(String Month) {
        month_txt.setText(Month);
    }

    @Override
    public void setDashboard(HashMap<String, ArrayList<Map<String, String>>> dashboard_list, List<String> header, String Month) {
        listAdapter = new ExpandableDashboardAdapter(doctor,getContext(), header, dashboard_list,Month);
        doctor.setAdapter(listAdapter);
        doctor.setGroupIndicator(null);

//        for(int i=0; i < listAdapter.getGroupCount(); i++) {
//            if (visible_status.get(i)==1) {
//                doctor.expandGroup(i);
//            }
//        }
    }

    @Override
    public void onDashboardUpdated(HashMap<String, ArrayList<Map<String, String>>> dashboard_list) {
        listAdapter.notifyDataSetChanged();
    }
}
