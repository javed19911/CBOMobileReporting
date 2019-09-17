package Bill.ItemFilter;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import Bill.NewOrder.mBillItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.Enum.eItem;

public class CompanyItemFilter extends AppCompatActivity implements IitemNewOrder, aCompanyItemFilter.aNewItemFilter_interface {

    androidx.appcompat.widget.Toolbar toolbar;
    private RecyclerView itemlist_filter;
    private aCompanyItemFilter itemAdapter;
    private vmBill_ItemFilter viewModel;
    TextView itemincart;
    Activity context;
    private  ArrayList<mBillItem>Billlist= new ArrayList<mBillItem>();

    private eItem itemType = eItem.MEDICINE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comnpany_new_order);
        context = this;
        viewModel = ViewModelProviders.of(this).get(vmBill_ItemFilter.class);
        //chnage here
       // viewModel.setOrder((mOrder) getIntent().getSerializableExtra("order"));
       // viewModel.setSync(getIntent().getBooleanExtra("syncItem",true));

       /* if (getIntent().getSerializableExtra("itemType") != null){
            itemType = (eItem) getIntent().getSerializableExtra("itemType");
        }
*/
        viewModel.setView(context,this);
    }



    @Override
    public void getReferencesById() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemincart = findViewById(R.id.itemincart);

        ImageView clearQry = findViewById(R.id.clearQry);
        itemlist_filter = (RecyclerView) findViewById(R.id.itemList);
        itemAdapter = new aCompanyItemFilter(this, Billlist,itemType);
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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
  /*      itemAdapter.setOnClickListner(new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                if (itemType == eItem.PRODUCT){
                    Intent intent = new Intent();
                    intent.putExtra("item",itemAdapter.getItems().get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                }else  if (view.getId() == R.id.add_to_cart) {
                   // viewModel.addItem(itemAdapter.getItems().get(position));
                }
            }
        });*/
        LinearLayout goToCart = findViewById(R.id.go_to_cart);
        goToCart.setOnClickListener(view -> onBackPressed());
        if (itemType == eItem.PRODUCT) {
            goToCart.setVisibility(View.GONE);
        }
    }

    @Override
    public String getPartyID() {
        return null;
    }

    @Override
    public String getUserID() {
        return null;
    }
/*
    @Override
    public String getPartyID() {

        return viewModel.getOrder().getPartyId();
    }

    @Override
    public String getUserID() {
        return MyCustumApplication.getInstance().getUser().getID();
    }*/


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
    public void onOrderChanged(mOrder order) {
        itemincart.setText(order.getItems().size() + " Items in cart");

    }


    @Override
    public void onBackPressed() {
       /* if (itemType != eItem.PRODUCT) {
            Intent intent = new Intent();
           //intent.putExtra("item",b);
            setResult(RESULT_OK, intent);
            finish();
        }else{
            setResult(RESULT_CANCELED, null);
            finish();
        }*/
       finish();
    }

    @Override
    public void OnClick_Recipt(mBillItem billItem) {
        if (itemType != eItem.PRODUCT) {
            Intent intent = new Intent();
            intent.putExtra("item", billItem);
            setResult(RESULT_OK, intent);
            finish();
        }else{
            setResult(RESULT_CANCELED, null);
            finish();
        }


    }
}

