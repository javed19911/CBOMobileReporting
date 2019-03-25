package saleOrder.Adaptor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

import saleOrder.Fragments.OrderListFragment;
import saleOrder.mParty;


/**
 * Created by cboios on 12/07/18.
 */

public class OrderListPageViewAdaptor extends FragmentPagerAdapter {

    TabLayout tabLayout;
    mParty party;
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer,OrderListFragment> mPageReferenceMap= new HashMap<>();
    public OrderListPageViewAdaptor(FragmentManager fm, TabLayout tb, mParty party) {
        super(fm);
        tabLayout = tb;
        this.party = party;
    }

    @Override
    public Fragment getItem(int position) {
        OrderListFragment myFragment = new OrderListFragment();
        mPageReferenceMap.put(position, myFragment);
        Bundle data = new Bundle();//Use bundle to pass data
        data.putString("OrderType", tabLayout.getTabAt(position).getText().toString());
        data.putSerializable("party", party);
        myFragment.setArguments(data);
        return myFragment;
    }


    @Override
    public void destroyItem (ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }


    public OrderListFragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }

    @Override
    public int getCount() {
        return tabLayout.getTabCount();
    }
}
