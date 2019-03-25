package saleOrder;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.Fragments.CartFragment;
import saleOrder.Views.iCart;

public class CartActivity extends AppCompatActivity implements iCart {


    TextView title;
    FrameLayout cartFrame;

    private vmCart viewModel;
    Activity context;
    CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        context = this;
        cartFragment = new CartFragment();
        cartFragment.setArguments(getIntent().getExtras());
        viewModel = ViewModelProviders.of(this).get(vmCart.class);
        viewModel.setOrder((mOrder) getIntent().getSerializableExtra("order"));
        viewModel.setView(context,this);


    }

    @Override
    public void getReferencesById() {

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        title  = toolbar.findViewById(R.id.title);

        FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction();
        transaction.add(R.id.cartFragment,cartFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void setTitle(String header) {
        title.setText(header);
    }

    @Override
    public void onItemAdded(mItem item) {

    }


}
