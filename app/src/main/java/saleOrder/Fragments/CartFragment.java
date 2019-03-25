package saleOrder.Fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import saleOrder.Adaptor.CartAdapter;
import saleOrder.ItemFilterActivity;
import saleOrder.Views.iCart;
import saleOrder.Views.iFCart;
import saleOrder.vmFCart;

public class CartFragment extends Fragment implements iFCart {


    private static final int ITEM_FILTER = 0;
    TextView cartSubTotal, cartDiscount, cartTotal, cartTotal_out, saveOrder, itemFilter;
    Activity context;
    private RecyclerView itemlist_filter;
    private CartAdapter cartAdapter;
    private vmFCart viewModel;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ITEM_FILTER:

                    viewModel.setOrder ((mOrder) data.getSerializableExtra ("order"));
                    cartAdapter.update (viewModel.getOrder ());
                    CalculateTotal ();
                    break;
                default:

            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_cart_view, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        cartTotal = view.findViewById(R.id.cartTotal);
        cartDiscount = view.findViewById(R.id.cartDiscount);
        cartSubTotal = view.findViewById(R.id.cartSubTotal);
        cartTotal_out = view.findViewById(R.id.cartTotal_out);



        itemlist_filter = (RecyclerView) view.findViewById(R.id.cart_list);
        itemFilter = view.findViewById(R.id.itemFilter);

        // itemfrag=view.findViewById (R.id.itemfrag);


        context = getActivity ();
        viewModel = ViewModelProviders.of (this).get (vmFCart.class);

        viewModel.setOrder ((mOrder) context.getIntent ().getSerializableExtra ("order"));
        viewModel.setView (context, this);

        updateTotal ();

        cartAdapter = new CartAdapter (context, viewModel.getOrder ());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager (context, LinearLayoutManager.VERTICAL, false);
        itemlist_filter.setLayoutManager (mLayoutManager1);
        itemlist_filter.setItemAnimator (new DefaultItemAnimator ());
        itemlist_filter.setAdapter (cartAdapter);


        itemFilter = view.findViewById (R.id.itemFilter);
        itemFilter.setOnClickListener (v -> {
            Intent intent = new Intent (context, ItemFilterActivity.class);
            intent.putExtra ("order", viewModel.getOrder ());
            startActivityForResult (intent, ITEM_FILTER);

        });
        cartAdapter.setOnClickListner (new RecycleViewOnItemClickListener () {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (view.getId () == R.id.add_to_cart) {
                    CalculateTotal ();
                }
            }
        });
        saveOrder = view.findViewById(R.id.saveOrder);
        saveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.getOrder().getStatus().equalsIgnoreCase("C")){
                    context.     onBackPressed();
                }else{
                    viewModel.orderCommit(context,"COD");
                }
            }
        });

    }


    @Override
    public void getReferencesById() {
    }

    @Override
    public String getCompanyCode() {
        return MyCustumApplication.getInstance ().getUser ().getCompanyCode ();
    }

    @Override
    public String getUserId() {
        return MyCustumApplication.getInstance ().getUser ().getID ();
    }

    @Override
    public void setTile() {

        String title = "";

        if (viewModel.getOrder ().getDocId ().equalsIgnoreCase ("0")) {
            //itemFilter.performClick ();
            title = "New Order";

        } else if (viewModel.getOrder ().getStatus ().equalsIgnoreCase ("C")) {
            itemFilter.setVisibility (View.GONE);
            title = "Order :- " + viewModel.getOrder ().getDocNo ();
            saveOrder.setText ("<< Back");
        } else if (!viewModel.getOrder ().getDocId ().equalsIgnoreCase ("0") &&
                viewModel.getOrder ().getStatus ().equalsIgnoreCase ("P")) {

            title = "Order :- " + viewModel.getOrder ().getDocNo ();
        }

        if (context instanceof iCart) {
            ((iCart) context).setTitle (title);

        }
    }


    @Override
    public void CalculateTotal() {
        Double netAmt = 0.0;
        for (mItem orderItem : viewModel.getOrder ().getItems ()) {
            netAmt = netAmt + Double.parseDouble (orderItem.getAmt ());
        }
        viewModel.getOrder ().setNetAmt ("" + netAmt);
        updateTotal ();
    }

    @Override
    public void updateTotal() {
        String total = viewModel.getOrder ().getNetAmt (); //cart_db_helper.getTotalAmount();

        cartDiscount.setText (AddToCartView.toCurrency (0));
        cartSubTotal.setText (AddToCartView.toCurrency (total));
        cartTotal_out.setText (AddToCartView.toCurrency (total));
        cartTotal.setText (AddToCartView.toCurrency (total));

    }


}
