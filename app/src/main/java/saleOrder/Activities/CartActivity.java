package saleOrder.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.cbo.cbomobilereporting.R;
import com.google.android.material.appbar.AppBarLayout;

import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.Enum.eItem;
import saleOrder.Fragments.CartFragment;
import saleOrder.Fragments.FNewOrder;
import saleOrder.Model.mParty;
import saleOrder.ViewModel.vmCart;
import saleOrder.Views.iCart;
import utils_new.AppAlert;

public class CartActivity extends AppCompatActivity implements iCart {


    TextView title,subTitle;

    private vmCart viewModel;
    AppCompatActivity context;
    CartFragment cartFragment;
    FNewOrder newOrder;
    AppBarLayout appBarLayout;
    mParty party;
    Boolean orderChanged = false;
    MenuItem additem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        context = this;
        cartFragment = new CartFragment();
        Bundle data = new Bundle();//Use bundle to pass data
        data.putSerializable("itemType", eItem.PRODUCT);
        cartFragment.setArguments(data);
        viewModel = ViewModelProviders.of(this).get(vmCart.class);
        updateOrder((mOrder) getIntent().getSerializableExtra("order"));
        party = ((mParty) getIntent().getSerializableExtra("party"));
        viewModel.setView(context,this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        additem = menu.findItem(R.id.add);
        if  (subTitle.getText().toString().equalsIgnoreCase("New Order") && additem != null){
            additem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                cartFragment.addAttachment();
                return true;
            default:
                onBackPressed();
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void getReferencesById() {

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        appBarLayout=findViewById(R.id.app_bar) ;
        title  = toolbar.findViewById(R.id.title);
        subTitle = toolbar.findViewById(R.id.subTitle);

        newOrder = (FNewOrder) getSupportFragmentManager().findFragmentById(R.id.newOrderFragment);
        FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction();
        transaction.add(R.id.cartFragment,cartFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        if (viewModel.getOrder().getStatus ().equalsIgnoreCase ("C")
                || viewModel.getOrder().getBilledHO() == 1
                || viewModel.getOrder().getApproved().equalsIgnoreCase("Y")){
            newOrder.HideFragment();
        }
        onItemEdit(new mItem().setName(""));

    }

    @Override
    public void setTitle(String header) {
        if (party.getHeadQtr().equalsIgnoreCase("")){
            title.setText(party.getName());
        }else{
            title.setText(party.getName() + "("+ party.getHeadQtr()+")");
        }

        subTitle.setText(header);
        if  (header.equalsIgnoreCase("New Order") && additem != null){
            additem.setVisible(false);
        }
    }

    @Override
    public void onItemAdded(mItem item) {
        orderChanged = true;
        cartFragment.addItem(item);
    }

    @Override
    public void onItemEdit(mItem item) {
        appBarLayout.setExpanded(true);
        newOrder.setItem(item);
    }

    @Override
    public mOrder getOrder() {
        return viewModel.getOrder();
    }

    @Override
    public void updateOrder(mOrder order) {
        viewModel.setOrder(order);
    }

    @Override
    public void onBackPressed() {
        if (orderChanged && !viewModel.getOrder().getStatus().equalsIgnoreCase("C")){
            AppAlert.getInstance()
                    .setPositiveTxt("Discard")
                    .setNagativeTxt("Cancel")
                    .DecisionAlert(context, "Alert !!!",
                    "Are you sure to discard the changes in the order?",
                    new AppAlert.OnClickListener() {
                        @Override
                        public void onPositiveClicked(View item, String result) {
                            finish();
                        }

                        @Override
                        public void onNegativeClicked(View item, String result) {
                            //cartFragment.orderCommit();
                        }
                    });
        }else {
            finish();
        }
    }
}
