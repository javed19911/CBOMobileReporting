package com.cbo.cbomobilereporting.ui_new.dcr_activities.TabbedActivity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DoctorSample.DoctorSampleFragment;
import com.google.android.material.tabs.TabLayout;

public class TabbedCallActivity extends AppCompatActivity implements iTabbedCall{

    vmTabbedCall viewModel;
    AppCompatActivity context;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabed_call);
        context = this;
        viewModel = ViewModelProviders.of(this).get(vmTabbedCall.class);
        viewModel.setView(context,this);
    }

    @Override
    public void getReferencesById() {

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        //attach tab layout with ViewPager
        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


        //create and set ViewPager adapter
        aTabbedCall adapter = new aTabbedCall(getSupportFragmentManager());
        adapter.addFragment(new DoctorSampleFragment(), "Call");
        adapter.addFragment(new DoctorSampleFragment(), "Unplanned");
        adapter.addFragment(new DoctorSampleFragment(), "Summary");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


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
        return "Doctor Sample";
    }

    @Override
    public void setTile(String s) {
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }



        TextView title = toolbar.findViewById(R.id.title);
        TextView subTitle = toolbar.findViewById(R.id.subTitle);
        title.setText(s);
    }
}
