package com.cbo.cbomobilereporting.ui_new.dcr_activities.TabbedActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import saleOrder.Fragments.OrderListFragment;

public class aTabbedCall extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, OrderListFragment> mPageReferenceMap= new HashMap<>();
    public aTabbedCall(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
       return mFragmentList.get(position);
    }


    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public void destroyItem (ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


    @Override
    public int getCount() {
        return mFragmentTitleList.size();
    }
}
