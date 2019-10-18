package bill.BillReport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import bill.Cart.CompanyCartActivity;
import bill.Cart.mCustomer;
import bill.Outlet.mOutlet;
import bill.mBillOrder;
import utils_new.AppAlert;

public class DashboardBill extends CustomActivity implements
    IBill, aDashboardBill.Bill_interface,iBillMain, SearchView.OnQueryTextListener {
        private  vmBill vmBill;
        private Toolbar toolbar;
        private TextView textView,Totamt;
        private RecyclerView billrecyclerView;
        private aDashboardBill billadapter;
        private SwipeRefreshLayout swipeRefressLayoutRecycler;



        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard_bill);
            vmBill = ViewModelProviders.of(DashboardBill.this).get(vmBill.class);
            vmBill.setOutlet((mOutlet) getIntent().getSerializableExtra("outlet"));


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
            textView= (TextView) findViewById(R.id.hadder_text_1);
            billrecyclerView =findViewById(R.id.bill_report_content);
            swipeRefressLayoutRecycler = findViewById(R.id.swipeRefressLayoutRecycler);
            Totamt = findViewById(R.id.Totamt);


            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(view ->onBackPressed());

            }



            swipeRefressLayoutRecycler.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefressLayoutRecycler.setRefreshing(true);
                    getBills();
                }
            });




            getBills();
        }



        public void getBills(){

                vmBill.getBills(context,vmBill.getOutlet());


        }

    @Override
    public boolean isFromDateRequired() {
        return true;
    }

    @Override
    public boolean isToDateRequired() {
        return true;
    }

    @Override
    public boolean isShowPopup() {
        return false;
    }

    @Override
    public String getDocType() {
        return "";
    }

    @Override
    public boolean IsAllRequiredInFilter() {
        return true;
    }


    @Override
        protected void onResume() {
            super.onResume();
            if (vmBill.isLoaded()) {
                getBills();
            }
        }

        @Override
        public void setActvityTitle(String title) {
            textView.setText(title);
        }

        @Override
        public String getActivityTitle() {
            return getIntent().getStringExtra("title");
        }


        @Override
        public void onBillListlistchange(ArrayList<mBill> billlist) {
            billadapter = new aDashboardBill(context, billlist);
            billrecyclerView.setLayoutManager(new LinearLayoutManager(context));
            billrecyclerView.setItemAnimator(new DefaultItemAnimator());
            billrecyclerView.setAdapter(billadapter);
            swipeRefressLayoutRecycler.setRefreshing(false);



        }

        @Override
        public void updateTotBillAmt(Double totamt) {
            Totamt.setText(String.format("%.2f", totamt));
        }

        @Override
        public void onBillDeleted(Context context) {

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
            getBills();

        }

        @Override
        public void showBillDetail(mBillOrder order, mCustomer customer) {
            Intent intent = new Intent(context, CompanyCartActivity.class);
            intent.putExtra("order", order);
            intent.putExtra("customer", customer);
//            intent.putExtra("PayModes",fBillFilter.getPayModes());
            intent.putExtra("PayModes",new ArrayList<mPay>());
            startActivity(intent);
        }

        @Override
        public boolean onQueryTextSubmit(String query) { return false; }

        @Override
        public boolean onQueryTextChange(String newText) { return false; }





        @Override
        public void Edit_Bill(mBill mbill) {
            vmBill.getBillDet(context,mbill,"E");

        }

        @Override
        public void Delete_Bill(mBill mbill) {
            AppAlert.getInstance().DecisionAlert(context,
                    "Delete !!!", "Are you sure to delete\nBill : "+mbill.getBILL_PRINT()+"\nBill Amt. :- "+mbill.getNET_AMT()+" ?",
                    new AppAlert.OnClickListener() {
                        @Override
                        public void onPositiveClicked(View item, String result) {

                            vmBill.deleteBill(context,mbill);
                        }

                        @Override
                        public void onNegativeClicked(View item, String result) {

                        }
                    });

        }

        @Override
        public void OnClick_Bill(mBill mbill) {
            vmBill.getBillDet(context,mbill,"V");
        }
    }

