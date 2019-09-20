package bill.Cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import bill.ItemFilter.CompanyItemFilter;
import bill.NewOrder.mBillItem;
import bill.mBillOrder;
import saleOrder.Adaptor.CartAdapter;
import saleOrder.Enum.eItem;

public class FCompanyCart extends Fragment implements IFCompanycart {


    private static final int ITEM_FILTER = 0;
    private static final int ATTACHMENT = 10;
    TextView cartSubTotal, cartDiscount, cartNetAmount, cartTotal_out, saveOrder, itemFilter;
    Activity context;
    private RecyclerView itemlist_filter;
    private CartAdapter cartAdapter;
    private vmFCompanyCart viewModel;
    private eItem itemType = eItem.MEDICINE;

    private TextView Total_amt,SGST_amt,CGST_amt;
    private TextView centralTaxName,LocalTaxName;
    private LinearLayout centralTax,LocalTax;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ITEM_FILTER:
                    updateOrder((mBillOrder) data.getSerializableExtra ("order"));
                    if (context instanceof ICompanyCart) {
                        ((ICompanyCart) context).updateOrder(viewModel.getOrder());
                    }
                    break;
                default:

            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment__company_cart_view, container, false);
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


        viewModel = ViewModelProviders.of (this).get (vmFCompanyCart.class);

        viewModel.setOrder ((mBillOrder) context.getIntent ().getSerializableExtra ("order"));
        viewModel.setView (context, this);

        itemFilter.setOnClickListener (v -> {
            Intent intent = new Intent (context, CompanyItemFilter.class);
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


//chnage here
       /* cartAdapter = new CartAdapter (context, viewModel.getOrder (),itemType);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        itemlist_filter.setLayoutManager (mLayoutManager1);
        itemlist_filter.setItemAnimator (new DefaultItemAnimator());
        itemlist_filter.setAdapter (cartAdapter);*/

     /*   cartAdapter.setOnClickListner (new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (view.getId () == R.id.delete) {
                    CalculateTotal ();
                }else if (itemType == eItem.PRODUCT){
                    if (view.getId () == R.id.edit) {
                        if (context instanceof iCart) {
                            //vhnge here
                           // ((iCart) context).onItemEdit(viewModel.getOrder ().getItems().get(position));
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
*/

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
    public void setOrder(mBillOrder order) {

    }

    @Override
    public void updateOrder(mBillOrder order) {

    }

    @Override
    public void addItem(mBillItem item) {
        viewModel.addItem(item);
        if (context instanceof ICompanyCart) {

            ((ICompanyCart) context).updateOrder (viewModel.getOrder ());
        }
    }

    /*@Override
    public void setOrder(mOrder order) {
        //viewModel.setOrder ((mOrder) context.getIntent ().getSerializableExtra ("order"));
        viewModel.setOrder(order);


        updateTotal ();

    }*/



    @Override
    public void setTile() {

        String title = "";
        title = "New Order";
     /*   if (viewModel.getOrder ().getDocId ().equalsIgnoreCase ("0")) {
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
        }else {
            title = "New Order";

        }*/

        if (itemType == eItem.PRODUCT){
            itemFilter.setVisibility(View.GONE);
        }

        if (context instanceof ICompanyCart) {
            ((ICompanyCart) context).setTitle (title);

        }
    }





    @Override
    public void orderCommit() {
    Toast.makeText(context, "UnderDevlopment", Toast.LENGTH_SHORT).show();
       /* if (viewModel.getOrder().getStatus().equalsIgnoreCase("C")
                || viewModel.getOrder().getBilledHO() == 1
                || viewModel.getOrder().getApproved().equalsIgnoreCase("Y")){
            context.     onBackPressed();
        }else if( viewModel.getOrder().getItems().size() == 0){
            AppAlert.getInstance().getAlert(context,"No Item !!!!","Please add atleast one item to the cart... ");
        }else{
            viewModel.orderCommit(context,"COD");
        }*/
    }

    @Override
    public void CalculateTotal() {

    }

    @Override
    public void updateTotal() {

    }


    @Override
    public void addAttachment() {
//        Intent intent = new Intent (context, CartAttachment.class);
//        intent.putExtra ("order", viewModel.getOrder());
//        startActivityForResult (intent, ATTACHMENT);
    }


}

