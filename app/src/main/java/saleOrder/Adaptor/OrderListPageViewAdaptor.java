package saleOrder.Adaptor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

import saleOrder.Fragments.OrderListFragment;
import saleOrder.Model.mParty;


/**
 * Created by cboios on 12/07/18.
 */

public class OrderListPageViewAdaptor extends FragmentPagerAdapter {

    TabLayout tabLayout;
    mParty party;
    Boolean ShowParty = false;
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer,OrderListFragment> mPageReferenceMap= new HashMap<>();
    public OrderListPageViewAdaptor(FragmentManager fm, TabLayout tb, mParty party,Boolean ShowParty) {
        super(fm);
        tabLayout = tb;
        this.party = party;
        this.ShowParty = ShowParty;
    }

    @Override
    public Fragment getItem(int position) {
        OrderListFragment myFragment = new OrderListFragment();
        mPageReferenceMap.put(position, myFragment);
        Bundle data = new Bundle();//Use bundle to pass data
        data.putString("OrderType", tabLayout.getTabAt(position).getText().toString());
        data.putSerializable("party", party);
        data.putSerializable("ShowParty", ShowParty);
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
