package com.cbo.cbomobilereporting.ui_new.report_activities.DCRReport;

import android.app.ProgressDialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.F_TeamMonthDivision;

import java.util.ArrayList;

/**
 * Created by Akshit.Udainiya on 2/15/2015.
 */
public class DcrReportsNew extends AppCompatActivity{
    Context context;
    RecyclerView listView;
    VM_DCR_Report vm_dcr_report;
    DcrNewAdapter rptAdapter;
    public static String lastPaId;
    public ProgressDialog progress1;
    F_TeamMonthDivision fTeamMonthDivision;
    private Menu menu;
    AppBarLayout appBarLayout;

    private DcrRawAdapter mAdapter;
    private ArrayList<mDcrGrid> movieList = new ArrayList<>();

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.dcrreportsnew);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);

        textView.setText("DCR Reports");
        context = this;

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
       appBarLayout=(AppBarLayout)findViewById(R.id.app_bar) ;

        fTeamMonthDivision = (F_TeamMonthDivision) getSupportFragmentManager().findFragmentById(R.id.dcrfragment);
        vm_dcr_report = ViewModelProviders.of(DcrReportsNew.this).get(VM_DCR_Report.class);
        vm_dcr_report.setFragment(fTeamMonthDivision);
        listView =findViewById(R.id.dcr_report_content);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    vm_dcr_report.getDCRReports(context, fTeamMonthDivision.getViewModel().getUser().getId(), fTeamMonthDivision.getViewModel().getMonth().getId(), new VM_DCR_Report.OnResultListener() {

                        @Override
                        public void onSuccess(ArrayList<mDCR_Report> item) {

                            rptAdapter = new DcrNewAdapter(context, item,fTeamMonthDivision.getViewModel().getUser().getId());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (context);
                            listView.setLayoutManager(mLayoutManager);
                            listView.setItemAnimator(new DefaultItemAnimator ());
                            listView.setAdapter(rptAdapter);



                         /*   GridLayoutManager manager = new GridLayoutManager(DcrReportsActivity.this, 1, GridLayoutManager.VERTICAL, false);
                            listView.setLayoutManager(manager);
                            listView.setItemAnimator(new DefaultItemAnimator());
                            listView.setAdapter(rptAdapter);*/


                            ViewCompat.setNestedScrollingEnabled(
                                    listView, false);
                         //   appBarLayout.setExpanded(false);
                            appBarLayout.setExpanded(false,true);



                            /*mAdapter = new DcrRawAdapter(movieList);
                            GridLayoutManager manager = new GridLayoutManager(DcrReportsActivity.this, 3, GridLayoutManager.VERTICAL, false);
                            listview2.setLayoutManager(manager);
                            listview2.setItemAnimator(new DefaultItemAnimator());
                            listview2.setAdapter(mAdapter);
                            prepareMovieData();*/
                        }

                        @Override
                        public void onError(String Title, String error) {

                        }


                    });
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
                    showOption(R.id.action_info);
                    // collapsingToolbarLayout.setTitle("Title");
                    isShow = true;
                } else if (isShow) {
                    hideOption(R.id.action_info);
                    // collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* if (item != null) {

            finish();
        }*/
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
        getMenuInflater().inflate(R.menu.menu_dcrreport, menu);
        hideOption(R.id.action_info);
        return true;
    }



    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }
}
