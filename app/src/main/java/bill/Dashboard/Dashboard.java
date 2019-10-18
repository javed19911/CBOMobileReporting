package bill.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;

import java.util.ArrayList;

public class Dashboard extends CustomActivity implements iDashboard {

    androidx.appcompat.widget.Toolbar toolbar;
    private vmDashboard viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
       /* viewModel = ViewModelProviders.of(Dashboard.this).get(vmDashboard.class);
        viewModel.setView(context,this);*/
       getReferencesById();
       setTitle(getActivityTitle());
    }

    @Override
    public void getReferencesById() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*dashbordrecyclerView =findViewById(R.id.dashboard_list);
        swipeRefressLayoutRecycler = findViewById(R.id.swipeRefressLayoutRecycler);*/

    }

    @Override
    public void setOnClickListeners() {


    }

    @Override
    public String getActivityTitle() {
        return getIntent().getStringExtra("title");
    }

    @Override
    public void setTitle(String activityTitle) {
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        TextView title = toolbar.findViewById(R.id.title);
        title.setText(activityTitle);
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

    }

    @Override
    public void onListUpdatedNew(ArrayList<mDashboardNew> dashboards) {

    }
}
