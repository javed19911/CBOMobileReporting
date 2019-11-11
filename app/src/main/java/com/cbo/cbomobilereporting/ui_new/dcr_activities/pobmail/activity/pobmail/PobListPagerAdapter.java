package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.pobmail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.ChemistFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;


public class PobListPagerAdapter extends FragmentPagerAdapter {

    TabLayout tabLayout;
    Boolean ShowParty = false;
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, ChemistFragment> mPageReferenceMap= new HashMap<>();
    public PobListPagerAdapter(FragmentManager fm, TabLayout tb) {
        super(fm);
        tabLayout = tb;
    }

    @Override
    public Fragment getItem(int position) {
        ChemistFragment myFragment = new ChemistFragment();
        mPageReferenceMap.put(position, myFragment);
        Bundle data = new Bundle();//Use bundle to pass data
        data.putString("OrderType", tabLayout.getTabAt(position).getText().toString());
//        data.putSerializable("party", party);
//        data.putSerializable("ShowParty", ShowParty);
        myFragment.setArguments(data);

        return myFragment;
    }


    @Override
    public void destroyItem (ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }


    public ChemistFragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }

    @Override
    public int getCount() {
        return tabLayout.getTabCount();
    }
}

