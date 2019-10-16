package saleOrder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.View.iNewOrder;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import saleOrder.Adaptor.ItemAdapter;
import saleOrder.Enum.eItem;
import saleOrder.ViewModel.vmNewOrder;

public class ItemFilterActivity extends AppCompatActivity implements iNewOrder {

    androidx.appcompat.widget.Toolbar toolbar;
    private RecyclerView itemlist_filter;
    private ItemAdapter itemAdapter;
    private vmNewOrder viewModel;
    TextView itemincart;
    AppCompatActivity context;
    private eItem itemType = eItem.MEDICINE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        context = this;
        viewModel = ViewModelProviders.of(this).get(vmNewOrder.class);
        viewModel.setOrder((mOrder) getIntent().getSerializableExtra("order"));
        viewModel.setSync(getIntent().getBooleanExtra("syncItem",true));

        if (getIntent().getSerializableExtra("itemType") != null){
            itemType = (eItem) getIntent().getSerializableExtra("itemType");
        }

        viewModel.setView(context,this);
    }



    @Override
    public void getReferencesById() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemincart = findViewById(R.id.itemincart);

        ImageView clearQry = findViewById(R.id.clearQry);
        itemlist_filter = (RecyclerView) findViewById(R.id.itemList);
        itemAdapter = new ItemAdapter(this, viewModel.getItems(),itemType);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(itemAdapter);

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
        itemAdapter.setOnClickListner(new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

               if (itemType == eItem.PRODUCT){
                    Intent intent = new Intent();
                    intent.putExtra("item",itemAdapter.getItems().get(position));
                    setResult(RESULT_OK, intent);
                    finish();
               }else  if (view.getId() == R.id.add_to_cart) {
                   viewModel.addItem(itemAdapter.getItems().get(position));
               }
            }
        });
        LinearLayout goToCart = findViewById(R.id.go_to_cart);
        goToCart.setOnClickListener(view -> onBackPressed());
        if (itemType == eItem.PRODUCT) {
            goToCart.setVisibility(View.GONE);
        }
    }

    @Override
    public String getPartyID() {
        return viewModel.getOrder().getPartyId();
    }

    @Override
    public String getUserID() {
        return MyCustumApplication.getInstance().getUser().getID();
    }


    @Override
    public String getCompanyCode() {
        return MyCustumApplication.getInstance().getUser().getCompanyCode();
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
        itemAdapter.update(items);
    }

    @Override
    public void onOrderChanged(mOrder order) {
        itemincart.setText(order.getItems().size() + " Items in cart");

    }

    @Override
    public void onBackPressed() {
        if (itemType != eItem.PRODUCT) {
            Intent intent = new Intent();
            intent.putExtra("order", viewModel.getOrder());
            setResult(RESULT_OK, intent);
            finish();
        }else{
            setResult(RESULT_CANCELED, null);
            finish();
        }
    }
}
