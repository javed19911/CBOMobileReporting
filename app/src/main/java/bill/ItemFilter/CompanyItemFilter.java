package bill.ItemFilter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;

import java.util.ArrayList;

import bill.NewOrder.mBillItem;
import bill.mBillOrder;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;

public class CompanyItemFilter extends CustomActivity implements IitemNewOrder {

    androidx.appcompat.widget.Toolbar toolbar;
    private RecyclerView itemlist_filter;
    private aCompanyItemFilter itemAdapter;
    private vmBill_ItemFilter viewModel;
    TextView itemincart;
    private  ArrayList<mBillItem>Billlist= new ArrayList<mBillItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comnpany_new_order);

        viewModel = ViewModelProviders.of(this).get(vmBill_ItemFilter.class);
        viewModel.setOrder((mBillOrder) getIntent().getSerializableExtra("order"));
        viewModel.setSync(getIntent().getBooleanExtra("syncItem",true));

        viewModel.setView(context,this);
    }



    @Override
    public void getReferencesById() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemincart = findViewById(R.id.itemincart);

        ImageView clearQry = findViewById(R.id.clearQry);
        itemlist_filter = (RecyclerView) findViewById(R.id.itemList);
        itemAdapter = new aCompanyItemFilter(this, Billlist);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(itemAdapter);
        itemAdapter.setListener(new aCompanyItemFilter.IitemFilter() {
            @Override
            public void OnItemSelected(mBillItem item) {
                Intent intent = new Intent();
                intent.putExtra("item",item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        TextView filterTxt = toolbar.findViewById(R.id.filterTxt);
        filterTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setFilterQry(s.toString());
                viewModel.getOrderItem(context,false);

               // itemAdapter  .filter(s.toString());
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

        LinearLayout goToCart = findViewById(R.id.go_to_cart);
        goToCart.setOnClickListener(view -> onBackPressed());

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
    public void onItemsChanged(ArrayList<mBillItem> items) {

        itemAdapter.update(items);
    }

    @Override
    public void onOrderChanged(mBillOrder order) {
        itemincart.setText(order.getItems().size() + " Items in cart");

    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        finish();
    }


}

