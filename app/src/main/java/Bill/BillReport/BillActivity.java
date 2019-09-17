package Bill.BillReport;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import Bill.CompanySelecter.CompanyActivity;
import utils_new.CustomDatePicker;

public class BillActivity     extends CustomActivity
        implements SwipeRefreshLayout.OnRefreshListener ,
        IBill, aBill.Bill_interface, SearchView.OnQueryTextListener {
private  vmBill vmBill;
private Toolbar toolbar;
private TextView textView;
private AppBarLayout appBarLayout;
private RecyclerView  billrecyclerView;
private aBill billadapter;
private FloatingActionButton fab;
private FBillFilter fBillFilter;
private SwipeRefreshLayout swipeRefressLayoutRecycler;
public  CollapsingToolbarLayout collapsingToolbarLayout;
private  Menu menu;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_view);
        vmBill = ViewModelProviders.of(BillActivity.this).get(vmBill.class);



    }

    @Override
    protected void onStart() {
        super.onStart();
        vmBill.setView(context,this);
    }

    @Override
    public void getReferencesById() {
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout=(AppBarLayout)findViewById(R.id.app_bar);
        textView= (TextView) findViewById(R.id.hadder_text_1);
        billrecyclerView =findViewById(R.id.bill_report_content);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        swipeRefressLayoutRecycler = findViewById(R.id.swipeRefressLayoutRecycler);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setOnClickListener(view ->onBackPressed());

        }

        collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        fBillFilter = (FBillFilter) getSupportFragmentManager().findFragmentById(R.id.billfragment);
        vmBill = ViewModelProviders.of(BillActivity.this).get( vmBill.class);
        vmBill.setFragment(fBillFilter);


        fab .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vmBill.getmComponey(context,fBillFilter.getViewModel().getComponeyId(),
                        CustomDatePicker.formatDate(fBillFilter.getViewModel().getFDATE(),CustomDatePicker.CommitFormat),
                        CustomDatePicker.formatDate(fBillFilter.getViewModel().getFDATE(),CustomDatePicker.CommitFormat));

                ViewCompat.setNestedScrollingEnabled(
                        billrecyclerView, false);
                //   appBarLayout.setExpanded(false);
                appBarLayout.setExpanded(false,true);
            }

        });

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                      showOption(R.id.action_filter);
                    // collapsingToolbarLayout.setTitle("Title");
                    isShow = true;
                } else if (isShow) {

                     hideOption(R.id.action_filter);
                    // collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.bill_report_menu, menu);
        this.menu = menu;
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        hideOption(R.id.action_filter);
        return true;
    }
    private void hideOption(int id) {
        if(menu==null){
            return;
        }
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_filter) {

            if (appBarLayout.getTop() < 0)
                appBarLayout.setExpanded(true);
            else
                appBarLayout.setExpanded(false);
            return true;
        } else  if (id == R.id.add) {

            Intent intent = new Intent(context, CompanyActivity.class);
            intent.putExtra("Companies",fBillFilter.getViewModel().getPartylist());
            startActivity(intent);
        } else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void showOption(int id) {
        if(menu==null){
            return;
        }
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }
    @Override
    public void setActvityTitle(String title) {
        textView.setText(title);
    }

    @Override
    public String getActivityTitle() {
        return "Bill Report";
    }

    @Override
    public void onBillListlistchange(ArrayList<mBill> billlist) {
        billadapter = new aBill(context, billlist);
        billrecyclerView.setLayoutManager(new LinearLayoutManager(context));
        billrecyclerView.setItemAnimator(new DefaultItemAnimator());
        billrecyclerView.setAdapter(billadapter);
        swipeRefressLayoutRecycler.setRefreshing(false);



    }

    @Override
    public void onBillDeleted(Context context) {
        vmBill.getmComponey(context,fBillFilter.getViewModel().getComponeyId(),
                CustomDatePicker.formatDate(fBillFilter.getViewModel().getFDATE(),CustomDatePicker.CommitFormat),
                CustomDatePicker.formatDate(fBillFilter.getViewModel().getFDATE(),CustomDatePicker.CommitFormat));
    }

    @Override
    public void setCompanyName(String CompanyName) { }

    @Override
    public String getCompanyName() {
        return null;
    }

    @Override
    public void setBillTitle(String titile) { }

    @Override
    public void setmCompany(mCompany company) {
        vmBill.getmComponey(context,"23,22,4,5,1,6,26,7,8,25,29,27,24,19,2,21,9,31,30,18,10,11,16,3,17,34,33,32,35,12,13,14,20,15",
                CustomDatePicker.formatDate(fBillFilter.getViewModel().getFDATE(),CustomDatePicker.CommitFormat),
                CustomDatePicker.formatDate(fBillFilter.getViewModel().getFDATE(),CustomDatePicker.CommitFormat));
    }

    @Override
    public boolean onQueryTextSubmit(String query) { return false; }

    @Override
    public boolean onQueryTextChange(String newText) { return false; }

    @Override
    public void onRefresh() {
        swipeRefressLayoutRecycler.setRefreshing(true);
        vmBill.getmComponey(context,fBillFilter.getViewModel().getComponeyId(),
                CustomDatePicker.formatDate(fBillFilter.getViewModel().getFDATE(),CustomDatePicker.CommitFormat),
                CustomDatePicker.formatDate(fBillFilter.getViewModel().getFDATE(),CustomDatePicker.CommitFormat));
    }



    @Override
    public void Edit_Bill(mBill mbills) {
        customVariablesAndMethod.msgBox(context,"Under Devlopment");
    }

    @Override
    public void Delete_Bill(mBill mbills) {
        customVariablesAndMethod.msgBox(context,"Under Devlopment");
    }

    @Override
    public void OnClick_Bill(mBill mbills) {
        customVariablesAndMethod.msgBox(context,"Under Devlopment");
    }
}
