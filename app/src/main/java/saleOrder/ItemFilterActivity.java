package saleOrder;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Adaptor.MedicineAdapter;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.View.iNewOrder;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;

public class ItemFilterActivity extends AppCompatActivity implements iNewOrder {

    android.support.v7.widget.Toolbar toolbar;
    private RecyclerView itemlist_filter;
    private MedicineAdapter medicineAdapter;
    private vmNewOrder viewModel;
    TextView itemincart;
    Activity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        context = this;
        viewModel = ViewModelProviders.of(this).get(vmNewOrder.class);
        viewModel.setOrder((mOrder) getIntent().getSerializableExtra("order"));
        viewModel.setView(context,this);
    }



    @Override
    public void getReferencesById() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemincart = findViewById(R.id.itemincart);

        ImageView clearQry = findViewById(R.id.clearQry);
        itemlist_filter = (RecyclerView) findViewById(R.id.itemList);
        medicineAdapter = new MedicineAdapter(this, viewModel.getItems());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(medicineAdapter);

        TextView filterTxt = toolbar.findViewById(R.id.filterTxt);
        filterTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                viewModel.setFilterQry(s.toString());
                viewModel.getOrderItem(context,false);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        clearQry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterTxt.setText("");
            }
        });
        medicineAdapter.setOnClickListner(new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (view.getId() == R.id.add_to_cart) {
                    viewModel.addItem(medicineAdapter.getItems().get(position));
                }
            }
        });
        LinearLayout goToCart = findViewById(R.id.go_to_cart);
        goToCart.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public String getPartyID() {
        return viewModel.getOrder().getPartyId();
    }

    @Override
    public String getCompanyCode() {
        return "Demo";
    }

    @Override
    public String getActivityTitle() {
        return "New Order";
    }

    @Override
    public void setTile(String title) {

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }

    @Override
    public void onItemsChanged(ArrayList<mItem> items) {
        medicineAdapter.update(items);
    }

    @Override
    public void onOrderChanged(mOrder order) {
        itemincart.setText(order.getItems().size() + " Items in cart");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("order",viewModel.getOrder());
        setResult(RESULT_OK, intent);
        finish();
    }
}
