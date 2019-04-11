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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import saleOrder.Activities.CartAttachment;
import saleOrder.Adaptor.CartAdapter;
import saleOrder.Activities.ItemFilterActivity;
import saleOrder.Enum.eItem;
import saleOrder.Views.iCart;
import saleOrder.Views.iFCart;
import saleOrder.ViewModel.vmFCart;
import utils_new.AppAlert;

public class CartFragment extends Fragment implements iFCart {


    private static final int ITEM_FILTER = 0;
    private static final int ATTACHMENT = 10;
    TextView cartSubTotal, cartDiscount, cartNetAmount, cartTotal_out, saveOrder, itemFilter;
    Activity context;
    private RecyclerView itemlist_filter;
    private CartAdapter cartAdapter;
    private vmFCart viewModel;
    private eItem itemType = eItem.MEDICINE;

    private TextView Total_amt,SGST_amt,CGST_amt;
    private TextView centralTaxName,LocalTaxName;
    private LinearLayout centralTax,LocalTax;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ITEM_FILTER:
                    updateOrder((mOrder) data.getSerializableExtra ("order"));
                    if (context instanceof iCart) {
                        ((iCart) context).updateOrder(viewModel.getOrder());
                    }
                    break;
                case ATTACHMENT:
                    context.finish();
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

        cartNetAmount = view.findViewById(R.id.cartNetAmount);
        cartDiscount = view.findViewById(R.id.cartDiscount);
        cartSubTotal = view.findViewById(R.id.cartSubTotal);
        cartTotal_out = view.findViewById(R.id.cartTotal_out);

        Total_amt = view.findViewById(R.id.cartTotal);
        centralTax = view.findViewById(R.id.centralTax);
        LocalTax = view.findViewById(R.id.LocalTax);
        centralTaxName = view.findViewById(R.id.centralTaxName);
        LocalTaxName = view.findViewById(R.id.LocalTaxName);
        SGST_amt = view.findViewById(R.id.SGST_amt);
        CGST_amt = view.findViewById(R.id.CGST_amt);



        itemlist_filter = (RecyclerView) view.findViewById(R.id.cart_list);
        itemFilter = view.findViewById(R.id.itemFilter);
        saveOrder = view.findViewById(R.id.saveOrder);

        context = getActivity ();

        if(getArguments().getSerializable("itemType") != null){
            itemType =(eItem) getArguments().getSerializable("itemType");
        }


        viewModel = ViewModelProviders.of (this).get (vmFCart.class);

        viewModel.setOrder ((mOrder) context.getIntent ().getSerializableExtra ("order"));
        viewModel.setView (context, this);

        itemFilter.setOnClickListener (v -> {
            Intent intent = new Intent (context, ItemFilterActivity.class);
            intent.putExtra ("order", viewModel.getOrder ());
            intent.putExtra("syncItem", !viewModel.isLoaded());
            startActivityForResult (intent, ITEM_FILTER);

        });


        saveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderCommit();
            }
        });



        cartAdapter = new CartAdapter (context, viewModel.getOrder (),itemType);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager (context, LinearLayoutManager.VERTICAL, false);
        itemlist_filter.setLayoutManager (mLayoutManager1);
        itemlist_filter.setItemAnimator (new DefaultItemAnimator ());
        itemlist_filter.setAdapter (cartAdapter);

        cartAdapter.setOnClickListner (new RecycleViewOnItemClickListener () {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (view.getId () == R.id.delete) {
                    CalculateTotal ();
                }else if (itemType == eItem.PRODUCT){
                    if (view.getId () == R.id.edit) {
                        if (context instanceof iCart) {
                            ((iCart) context).onItemEdit(viewModel.getOrder ().getItems().get(position));
                        }
                    }

                }else if (view.getId () == R.id.add_to_cart) {
                    CalculateTotal ();
                }
            }
        });

        if (context instanceof iCart) {
            updateOrder(((iCart) context).getOrder());
        }


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

    /*@Override
    public void setOrder(mOrder order) {
        //viewModel.setOrder ((mOrder) context.getIntent ().getSerializableExtra ("order"));
        viewModel.setOrder(order);


        updateTotal ();

    }*/

    @Override
    public void updateOrder(mOrder order) {
        viewModel.setOrder (order);
        cartAdapter.update (viewModel.getOrder ());
        CalculateTotal ();
    }

    @Override
    public void addItem(mItem item) {
        viewModel.addItem(item);
        if (context instanceof iCart) {
            ((iCart) context).updateOrder (viewModel.getOrder ());
        }
    }

    @Override
    public void setTile() {

        String title = "";

        if (viewModel.getOrder ().getDocId ().equalsIgnoreCase ("0")) {
            //itemFilter.performClick ();
            title = "New Order";

        } else if (viewModel.getOrder ().getStatus ().equalsIgnoreCase ("C")
                || viewModel.getOrder().getBilledHO() == 1
                || viewModel.getOrder().getApproved().equalsIgnoreCase("Y")) {
            itemFilter.setVisibility (View.GONE);
            title = "Order No. :- " + viewModel.getOrder ().getDocNo ();
            saveOrder.setText ("<< Back");
        } else if (!viewModel.getOrder ().getDocId ().equalsIgnoreCase ("0") &&
                viewModel.getOrder ().getStatus ().equalsIgnoreCase ("P")) {

            title = "Order No. :- " + viewModel.getOrder ().getDocNo ();
        }

        if (itemType == eItem.PRODUCT){
            itemFilter.setVisibility(View.GONE);
        }

        if (context instanceof iCart) {
            ((iCart) context).setTitle (title);

        }
    }


    @Override
    public void CalculateTotal() {
        Double netAmt = 0.0;
        Double Amt = 0.0;
        Double CGST = 0.0;
        Double SGST = 0.0;
        for (mItem orderItem : viewModel.getOrder ().getItems ()) {
            netAmt = netAmt + orderItem.getNetAmt ();
            Amt = Amt + orderItem.getAmt ();
            CGST = CGST + orderItem.getCGSTAmt ();
            SGST = SGST + orderItem.getSGSTAmt ();
        }
        viewModel.getOrder ().setNetAmt ( netAmt);
        viewModel.getOrder ().setSGSTAmt ( SGST);
        viewModel.getOrder ().setCGSTAmt ( CGST);
        viewModel.getOrder ().setAmt ( Amt);
        viewModel.getOrder ().setTotAmt ( netAmt+SGST+CGST);
        updateTotal ();
    }

    @Override
    public void updateTotal() {
        String Total = String.format("%.2f",viewModel.getOrder ().getNetAmt ());
        String subTotal = String.format("%.2f",viewModel.getOrder ().getAmt ());
        String discount = String.format("%.2f",viewModel.getOrder ().getAmt () - viewModel.getOrder ().getNetAmt ());


        CGST_amt.setText(AddToCartView.toCurrency ( String.format("%.2f",viewModel.getOrder ().getCGSTAmt ())));
        SGST_amt.setText(AddToCartView.toCurrency ( String.format("%.2f",viewModel.getOrder ().getSGSTAmt ())));
        cartDiscount.setText (AddToCartView.toCurrency (discount));
        cartSubTotal.setText (AddToCartView.toCurrency (subTotal));

        cartNetAmount.setText (AddToCartView.toCurrency (Total));
        Total_amt.setText (AddToCartView.toCurrency ( String.format("%.2f",viewModel.getOrder ().getTotAmt ())));

        cartTotal_out.setText (Total_amt.getText().toString());

        if (viewModel.getOrder ().getSGSTAmt () == 0){
            LocalTax.setVisibility(View.GONE);
            centralTaxName.setText(eTax.IGST.name());
        }else{
            LocalTax.setVisibility(View.VISIBLE);
            centralTaxName.setText(eTax.CGST.name());
        }

    }

    @Override
    public void orderCommit() {
        if (viewModel.getOrder().getStatus().equalsIgnoreCase("C")
                || viewModel.getOrder().getBilledHO() == 1
                || viewModel.getOrder().getApproved().equalsIgnoreCase("Y")){
            context.     onBackPressed();
        }else if( viewModel.getOrder().getItems().size() == 0){
            AppAlert.getInstance().getAlert(context,"No Item !!!!","Please add atleast one iten to the cart... ");
        }else{
            viewModel.orderCommit(context,"COD");
        }
    }

    @Override
    public void addAttachment() {
        Intent intent = new Intent (context, CartAttachment.class);
        intent.putExtra ("order", viewModel.getOrder());
        startActivityForResult (intent, ATTACHMENT);
    }


}
