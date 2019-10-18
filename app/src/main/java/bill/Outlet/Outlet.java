package bill.Outlet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;

import java.util.ArrayList;

import bill.Dashboard.aDashboard;
import bill.Dashboard.mDashboard;
import bill.Dashboard.mDashboardNew;
import bill.Dashboard.vmDashboard;

public class Outlet extends CustomActivity implements iOutlet{

    androidx.appcompat.widget.Toolbar toolbar;
    private vmOutlet viewModel;
    private aOutlet outletAdaptor;
    private RecyclerView outletrecyclerView;
    private SwipeRefreshLayout swipeRefressLayoutRecycler;
    private LinearLayout footer;
    TextView OthSale,TotSale,CashSale,bills;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet);
        viewModel = ViewModelProviders.of(Outlet.this).get(vmOutlet.class);
        viewModel.setDashboard((mDashboardNew) getIntent().getSerializableExtra("dashboard"));
        viewModel.setView(context,this);
    }

    @Override
    public void getReferencesById() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        footer = findViewById(R.id.footer);

        CashSale=findViewById(R.id.CashSale);
        TotSale=findViewById(R.id.TotSale);

        OthSale=findViewById(R.id.OthSale);
        bills = findViewById(R.id.bill_count);

        outletrecyclerView =findViewById(R.id.dashboard_list);
        swipeRefressLayoutRecycler = findViewById(R.id.swipeRefressLayoutRecycler);
    }

    @Override
    public void setOnClickListeners() {
        swipeRefressLayoutRecycler.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefressLayoutRecycler.setRefreshing(true);
                viewModel.getOutlet(context);
            }
        });
    }


    @Override
    public String getActivityTitle() {
        return viewModel.getDashboard().getGROUP_NAME();
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
    public void onListUpdated(ArrayList<mOutlet> outlets) {
        footer.setVisibility(outlets.size()>0? View.VISIBLE:View.GONE);
        outletAdaptor = new aOutlet(context, outlets);
        outletrecyclerView.setLayoutManager(new LinearLayoutManager(context));
        outletrecyclerView.setItemAnimator(new DefaultItemAnimator());
        outletrecyclerView.setAdapter(outletAdaptor);
        swipeRefressLayoutRecycler.setRefreshing(false);
    }

    @Override
    public void onTotalUpdated(mOutlet TotalOutlet) {
        TotSale.setText(TotalOutlet.getTOTAL_SALE());
        OthSale.setText(TotalOutlet.getOTHER_SALE());
        CashSale.setText(TotalOutlet.getCASH_SALE());
        bills.setText(TotalOutlet.getNO_BILL());
    }
}
