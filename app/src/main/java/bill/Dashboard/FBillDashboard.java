package bill.Dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;


public class FBillDashboard extends Fragment implements iDashboard {

    private vmDashboard viewModel;
    private aDashboard dashboardAdaptor;
    private aDashboardNew dashboardAdaptorNew;
    private RecyclerView dashbordrecyclerView;
    private SwipeRefreshLayout swipeRefressLayoutRecycler;

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
        if (!getUserVisibleHint()) {
            return;
        }

        viewModel.getDashbord(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fbill_dashboard, container, false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(FBillDashboard.this).get(vmDashboard.class);
        viewModel.setView((AppCompatActivity) getActivity(),this);
    }


    @Override
    public void getReferencesById() {

        dashbordrecyclerView = getView().findViewById(R.id.dashboard_list);
        swipeRefressLayoutRecycler = getView().findViewById(R.id.swipeRefressLayoutRecycler);
    }

    @Override
    public void setOnClickListeners() {

        swipeRefressLayoutRecycler.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefressLayoutRecycler.setRefreshing(true);
                viewModel.getDashbord(getActivity());
            }
        });

    }

    @Override
    public String getActivityTitle() {
        return "";
    }

    @Override
    public void setTitle(String activityTitle) {

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
    public void onListUpdated(ArrayList<mDashboard> dashboards) {
        dashboardAdaptor = new aDashboard(getActivity(), dashboards);
        dashbordrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dashbordrecyclerView.setItemAnimator(new DefaultItemAnimator());
        dashbordrecyclerView.setAdapter(dashboardAdaptor);
        swipeRefressLayoutRecycler.setRefreshing(false);
    }

    @Override
    public void onListUpdatedNew(ArrayList<mDashboardNew> dashboards) {
        dashboardAdaptorNew = new aDashboardNew(getActivity(), dashboards);
        dashbordrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dashbordrecyclerView.setItemAnimator(new DefaultItemAnimator());
        dashbordrecyclerView.setAdapter(dashboardAdaptorNew);
        swipeRefressLayoutRecycler.setRefreshing(false);
    }
}
