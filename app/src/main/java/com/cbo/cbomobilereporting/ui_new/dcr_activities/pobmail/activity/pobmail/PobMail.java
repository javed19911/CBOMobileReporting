package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.pobmail;

import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.ChemistFragment;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mChemist;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mStockist;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class PobMail extends CustomActivity implements iPobMail, IPobStoKist {


    private vmPobMail viewModel;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final int STOKIST_FILTER = 0;
    private mStockist selectStockist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pov_mail);
        context = this;
        viewModel = ViewModelProviders.of(this).get(vmPobMail.class);
        viewModel.setView(context, this);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case STOKIST_FILTER:
//                    selectStockist = (mStockist) data.getSerializableExtra("stokist");
//                    Toast.makeText(context, selectStockist.getNAME(), Toast.LENGTH_SHORT).show();
//
//                    break;
//                default:
//
//            }
//        }
//    }

    @Override
    public void getReferencesById() {

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        //create tabs title
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));
        tabLayout.addTab(tabLayout.newTab().setText("All"));

        //attach tab layout with ViewPager
        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


        //create and set ViewPager adapter
        PobListPagerAdapter adapter = new PobListPagerAdapter(getSupportFragmentManager(), tabLayout);
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
    public String getActivityTitle() {
        return null;
    }

    @Override
    public void setTile(String var1) {
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        TextView title = toolbar.findViewById(R.id.title);
        title.setText("Pob Mail");
    }

    @Override
    public void onChemistListUpdated(ChemistFragment chemistFragment, ArrayList<mChemist> chemestsList, String filterType) {
        chemistFragment.oPendingListChange(chemestsList);
    }

    @Override
    public void onResume() {
        super.onResume();
//        PobListPagerAdapter adapter = new PobListPagerAdapter(getSupportFragmentManager(), tabLayout);
//        viewPager.setAdapter(adapter);

    }



    @Override
    public void getChemestList(ChemistFragment chemistFragment, String filterType) {
        viewModel.getChemestList(this,chemistFragment, filterType);
    }

    @Override
    public void updateChemistList(ChemistFragment chemistFragment, String filterType, mChemist selectStockist) {
        viewModel.onUpdateChemist(chemistFragment,filterType,selectStockist);
    }
}
