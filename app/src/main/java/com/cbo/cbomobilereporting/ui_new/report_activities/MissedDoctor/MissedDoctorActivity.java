package com.cbo.cbomobilereporting.ui_new.report_activities.MissedDoctor;

import android.app.ProgressDialog;
import android.app.SearchManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentActivity;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.F_TeamMonthDivision;

import java.util.ArrayList;

import utils_new.AppAlert;

public class MissedDoctorActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{


    VM_MissedGrid_Mobile vm_missedGrid_mobile;
    public static String lastPaId;
    public ProgressDialog progress1;
    F_TeamMonthDivision fragmentRight;
    private Menu menu;
    AppBarLayout appBarLayout;
    Context context;
    MissedDocAdapter missedDocAdapter;
    RecyclerView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed_doctor);
        listView = findViewById(R.id.rpt_list_missed) ;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        TextView total_crm = findViewById(R.id.total_crm);
        TextView total_calls = findViewById(R.id.total_calls);
        TextView total_doctor = findViewById(R.id.total_doctor);
        LinearLayout tot_lay = findViewById(R.id.tot_lay);

        context = this;

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        appBarLayout=findViewById(R.id.app_bar) ;

        fragmentRight = (F_TeamMonthDivision) getSupportFragmentManager().findFragmentById(R.id.dcrfragment);
        vm_missedGrid_mobile= ViewModelProviders.of((FragmentActivity) context).get(VM_MissedGrid_Mobile.class);
        vm_missedGrid_mobile.setFragment(fragmentRight);

        textView.setText(vm_missedGrid_mobile.getTitle());
        tot_lay.setVisibility(View.GONE);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    showOption(R.id.action_info);
                    isShow = true;
                } else if (isShow) {
                    hideOption(R.id.action_info);
                    isShow = false;
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(fragmentRight.getName().getId()!=null && fragmentRight.getName(). getId()!=""  && fragmentRight.getMonth().getId()!=null && fragmentRight.getMonth(). getId()!="" && fragmentRight.getMissedFilter().getId()!=null && fragmentRight.getMissedFilter(). getId()!="" )

                tot_lay.setVisibility(View.GONE);
              vm_missedGrid_mobile.GET_MISSEDGRID_LIST(context, new VM_MissedGrid_Mobile.OnResultlistener() {
                  @Override
                  public void onSuccess(ArrayList<mMissedGrid> mMissedGrids) {
                      missedDocAdapter =
                              new MissedDocAdapter(context, mMissedGrids,
                                      (view1, position, isLongClick) ->
                                              vm_missedGrid_mobile.openDetail(context,vm_missedGrid_mobile.getList().get(position)));

                      RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                      listView.setLayoutManager(mLayoutManager);
                      listView.setItemAnimator(new DefaultItemAnimator());
                      listView.setAdapter(missedDocAdapter);
                      ViewCompat.setNestedScrollingEnabled(
                              listView, false);
                      //   appBarLayout.setExpanded(false);
                      appBarLayout.setExpanded(false, true);

                      total_doctor.setText(""+vm_missedGrid_mobile.getTotalDoctors());
                      total_calls.setText(""+vm_missedGrid_mobile.getTotalCall());
                      total_crm.setText(""+vm_missedGrid_mobile.getTotalMissed());
                      tot_lay.setVisibility(View.VISIBLE);
                  }

                  @Override
                  public void onError(String Title,String error) {
                      AppAlert.getInstance().getAlert(context,Title,error);
                  }
              });

         }
        });



        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {

            if (appBarLayout.getTop() < 0)
                appBarLayout.setExpanded(true);
            else
                appBarLayout.setExpanded(false);
            return true;
        }else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false);


        hideOption(R.id.action_info);

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
            if (missedDocAdapter != null)
                missedDocAdapter.filter(query);
        }
    }


    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (missedDocAdapter != null)
            missedDocAdapter.filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (missedDocAdapter != null)
            missedDocAdapter.filter(newText);
        return false;
    }
}
