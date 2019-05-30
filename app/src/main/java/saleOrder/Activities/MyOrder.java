package saleOrder.Activities;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.View.iOrder;
import saleOrder.Adaptor.OrderListPageViewAdaptor;
import saleOrder.Model.mParty;
import saleOrder.ViewModel.vmOrder;

public class MyOrder extends AppCompatActivity implements iOrder {

    vmOrder viewModel;
    Activity context;
    mParty party;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        context = this;
        party = ((mParty) getIntent().getSerializableExtra("party"));
        viewModel = ViewModelProviders.of(this).get(vmOrder.class);
        viewModel.setView(context,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                newOrder();
                return true;
            default:
                finish();
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void getReferencesById() {

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        //create tabs title
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Approved"));
        tabLayout.addTab(tabLayout.newTab().setText("Billed"));
        tabLayout.addTab(tabLayout.newTab().setText("All"));

        //attach tab layout with ViewPager
        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


        //create and set ViewPager adapter
        OrderListPageViewAdaptor adapter = new OrderListPageViewAdaptor(getSupportFragmentManager(),tabLayout,party);
        viewPager.setAdapter(adapter);


        //change selected tab when viewpager changed page
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //change viewpager page when tab selected
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public String getPartyID() {
        return party.getId();
    }

    @Override
    public String getCompanyCode() {
        return MyCustumApplication.getInstance().getUser().getCompanyCode();
    }

    @Override
    public String getActivityTitle() {
        return party.getName();
    }

    @Override
    public String getAppYN() {
        return "";
    }

    @Override
    public void setTile(String s) {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }



        TextView title = toolbar.findViewById(R.id.title);
        TextView subTitle = toolbar.findViewById(R.id.subTitle);
        title.setText(s);
        subTitle.setText(party.getHeadQtr());
    }

    @Override
    public void onOrderListChanged(ArrayList<mOrder> arrayList) {

    }

    @Override
    public void onOrderChanged(mOrder mOrder) {

    }

    @Override
    public void showOderDetail(mOrder mOrder) {

    }

    @Override
    public void newOrder() {
        Intent intent = new Intent(context, CartActivity.class);
        intent.putExtra("order",new mOrder().setPartyId(getPartyID()));
        intent.putExtra("party",party);
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        OrderListPageViewAdaptor adapter = new OrderListPageViewAdaptor(getSupportFragmentManager(),tabLayout,party);
        viewPager.setAdapter(adapter);

    }

}
