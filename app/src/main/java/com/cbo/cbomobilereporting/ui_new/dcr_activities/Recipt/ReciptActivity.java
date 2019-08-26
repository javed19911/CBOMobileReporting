package com.cbo.cbomobilereporting.ui_new.dcr_activities.Recipt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import services.MyAPIService;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;

public class ReciptActivity
        extends CustomActivity
        implements SwipeRefreshLayout.OnRefreshListener ,
        IRecipt, aRecipt.Recipt_interface ,SearchView.OnQueryTextListener{

    private    Context mContext;
    private RecyclerView recieptrecyclerview;
    private aRecipt recieptadapter;

    private Toolbar toolbar;
    private TextView title, SubTitle;
    private SwipeRefreshLayout swipeRefressLayoutRecycler;
    private vmRecpiet vmRecpiet;
    public  static  final  int ADDRECIEPTRESULT=100;
    private FloatingActionButton fab_filterReport;
    private TextView totalReciptAmt;

    private  Menu menu;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.reciept_activity);
        vmRecpiet = ViewModelProviders.of(ReciptActivity.this).get(vmRecpiet.class);
        vmRecpiet.setListener(context,this);
        vmRecpiet.setView(context,this);
    }



    @Override
    public void onRefresh() {

        swipeRefressLayoutRecycler.setRefreshing(true);
        vmRecpiet.GetALLRecpietList(context);
    }

    @Override
    public void onRecieptlistchanged(ArrayList<mRecipt> reciptArrayList) {

        recieptadapter = new aRecipt(mContext, reciptArrayList);
        recieptrecyclerview.setLayoutManager(new LinearLayoutManager(mContext));

        recieptrecyclerview.setItemAnimator(new DefaultItemAnimator());
        recieptrecyclerview.setAdapter(recieptadapter);
        swipeRefressLayoutRecycler.setRefreshing(false);


    }

    @Override
    public void onRecpieptDeleted(Context context) {

        vmRecpiet.GetALLRecpietList(context);
        customVariablesAndMethod.msgBox(context, "Deleted Successfully");
    }

    @Override
    public String getActivityTitle() {
        return getIntent().getStringExtra("title");
    }

    @Override
    public void setActivityTitle(String Activitytitle) {
        title.setText(Activitytitle);
        SubTitle.setVisibility(View.GONE);
    }

    @Override
    public void getReferencesById() {

        mContext=this;
        swipeRefressLayoutRecycler = findViewById(R.id.swipeRefressLayoutRecycler);
        fab_filterReport = findViewById(R.id.fab_filterReport);
        recieptrecyclerview=findViewById(R.id.recieptlist);
        totalReciptAmt = findViewById(R.id.totalReciptAmt);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        title = toolbar.findViewById(R.id.title);
        SubTitle = toolbar.findViewById(R.id.subTitle);


        fab_filterReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddReciept(new mRecipt());
            }
        });
        swipeRefressLayoutRecycler.setOnRefreshListener(this);





//        recieptrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
////                if (dy > 0 || dy < 0 && fab_filterReport.isShown())
////                    fab_filterReport.hide();
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE)
//                    fab_filterReport.show();
//                else
//                    fab_filterReport.hide();
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipt_menu, menu);
        this.menu = menu;
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }



    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            if (recieptadapter != null)
                recieptadapter.filter(query);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (recieptadapter != null) {
            recieptadapter.filter(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (recieptadapter != null) {
            recieptadapter.filter(newText);
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            AddReciept(new mRecipt());
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void OnTotalUpdated(Double total) {
        totalReciptAmt.setText(AddToCartView.toCurrency(String.format("%.2f",total)));
    }

    @Override
    public void Edit_Recipt(mRecipt mrecipt) {
        AddReciept(mrecipt);
    }

    @Override
    public void Delete_Recipt(mRecipt mrecipt) {

        OnDeleteDetail(mrecipt);
    }

    @Override
    public void OnClick_Recipt(mRecipt mrecipt) {

    }


    private void OnDeleteDetail(mRecipt recipt) {
        AppAlert.getInstance().DecisionAlert(mContext, "Delete!!!",
                "Are you sure, you want to delete?",
                new AppAlert.OnClickListener() {
                    @Override
                    public void onPositiveClicked(View item, String result) {
                        vmRecpiet.DeletePartyData(mContext,recipt);
                    }

                    @Override
                    public void onNegativeClicked(View item, String result) {

                    }
                });
    }



    private void AddReciept( mRecipt rcptdata) {
        Intent intent = new Intent (mContext, AddRecpiet.class);
        intent.putExtra("mRecipt",rcptdata);
        startActivityForResult (intent, ADDRECIEPTRESULT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ADDRECIEPTRESULT:
                    vmRecpiet.GetALLRecpietList(mContext);
                    customVariablesAndMethod.msgBox(context, "Updated Successfully");
                    break;
                default:


            }
        }
    }

}
