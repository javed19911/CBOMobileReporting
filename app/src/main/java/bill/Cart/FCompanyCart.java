package bill.Cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import bill.ItemFilter.CompanyItemFilter;
import bill.NewOrder.mBillItem;
import bill.mBillOrder;
import bill.stockEntry.IOpen;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import utils_new.AppAlert;
import utils_new.interfaces.RecycleViewOnItemClickListener;

public class FCompanyCart extends Fragment implements IFCompanycart {


    private static final int ITEM_FILTER = 0;
    private static final int ATTACHMENT = 10;
    private static final int ADD_CUSTOMER = 10;
    TextView cartSubTotal, cartDiscount, cartNetAmount, cartTotal_out, saveOrder, itemFilter;
    AppCompatActivity context;
    private RecyclerView itemlist_filter;
    private aBillCart cartAdapter;
    private vmFCompanyCart viewModel;

    private TextView Total_amt,SGST_amt,CGST_amt,roundAmt;
    private TextView centralTaxName,LocalTaxName,expand;
    private LinearLayout centralTax,LocalTax,billdet,billdet_inside,cartTotLayout,header;


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
                case ADD_CUSTOMER:
                    viewModel.setCustomer((mCustomer) data.getSerializableExtra("customer"));
                    updateOrder((mBillOrder) data.getSerializableExtra ("order"));
                    if (context instanceof ICompanyCart) {
                        ((ICompanyCart) context).updateOrder(viewModel.getOrder());
                    }
                    orderCommit();
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
        roundAmt = view.findViewById(R.id.roundAmt);

        billdet = view.findViewById(R.id.billdet);
        billdet_inside = view.findViewById(R.id.billdet_inside);
        cartTotLayout = view.findViewById(R.id.cartTotLayout);
        header = view.findViewById(R.id.header);


        itemlist_filter = (RecyclerView) view.findViewById(R.id.cart_list);
        itemFilter = view.findViewById(R.id.itemFilter);
        expand = view.findViewById(R.id.expand);

        itemFilter.setVisibility(View.GONE);


        saveOrder = view.findViewById(R.id.saveOrder);

        context = (AppCompatActivity) getActivity ();


        viewModel = ViewModelProviders.of (this).get (vmFCompanyCart.class);
        viewModel.setCustomer((mCustomer) context.getIntent ().getSerializableExtra("customer"));

        viewModel.setOrder ((mBillOrder) context.getIntent ().getSerializableExtra ("order"));
        viewModel.setView (context, this);

        itemFilter.setOnClickListener (v -> {
            Intent intent = new Intent (context, CompanyItemFilter.class);
            intent.putExtra ("order", viewModel.getOrder ());
            intent.putExtra("syncItem", !viewModel.isLoaded());
            startActivityForResult (intent, ITEM_FILTER);

        });

        cartTotLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (billdet_inside.getVisibility()== View.VISIBLE){
                   billdet_inside.setVisibility(View.GONE);
                   expand.setText("+");
               }else{
                   billdet_inside.setVisibility(View.VISIBLE);
                   expand.setText("-");
               }
            }
        });


        saveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //orderCommit();
                if (!viewModel.getOrder ().getDocId ().equalsIgnoreCase ("0")
                        && viewModel.getOrder ().getStatus ().equalsIgnoreCase ("V")){
                    context.onBackPressed();
                }else if( viewModel.getOrder().getItems().size() == 0){
                    AppAlert.getInstance().getAlert(context,"No Item !!!!","Please add atleast one item to the cart... ");
                }else {
                    AddCustomer();
                }
            }
        });


        cartAdapter = new aBillCart (context, viewModel.getOrder ());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        itemlist_filter.setLayoutManager (mLayoutManager1);
        itemlist_filter.setItemAnimator (new DefaultItemAnimator());
        itemlist_filter.setAdapter (cartAdapter);

        cartAdapter.setOnClickListner (new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (view.getId () == R.id.delete) {
                    CalculateTotal ();
                }else if (view.getId () == R.id.edit) {
                    if (context instanceof ICompanyCart) {
                        ((ICompanyCart) context).onItemEdit(viewModel.getOrder ().getItems().get(position));
                    }
                }else if (view.getId () == R.id.add_to_cart) {
                    CalculateTotal ();
                }
            }
        });

        if (context instanceof ICompanyCart) {
            updateOrder(((ICompanyCart) context).getOrder());
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



    @Override
    public void updateOrder(mBillOrder order) {
        viewModel.setOrder (order);
        cartAdapter.update (viewModel.getOrder ());
        header.setVisibility(order.getItems().size()>0 ?View.VISIBLE:View.GONE);
        billdet.setVisibility(order.getItems().size()>0 ?View.VISIBLE:View.GONE);
        CalculateTotal ();
    }

    @Override
    public void addItem(mBillItem item) {
        viewModel.addItem(item);
        if (context instanceof ICompanyCart) {

            ((ICompanyCart) context).updateOrder (viewModel.getOrder ());
        }
    }

    @Override
    public void setOrder(mBillOrder order) {

        viewModel.setOrder(order);


        updateTotal ();

    }



    @Override
    public void setTile() {

        String title = "";
        //title = "New Order";
        if (viewModel.getOrder ().getDocId ().equalsIgnoreCase ("0")) {
            //itemFilter.performClick ();
            title = "New Bill";

        }else if (viewModel.getOrder ().getStatus ().equalsIgnoreCase ("E")) {
            itemFilter.setVisibility (View.GONE);
            title = "Bill No. :- " + viewModel.getOrder ().getBillNo ();

        }else  {
            itemFilter.setVisibility (View.GONE);
            title = "Bill No. :- " + viewModel.getOrder ().getBillNo ();
            saveOrder.setText ("<< Back");
        }

        if (context instanceof ICompanyCart) {
            ((ICompanyCart) context).setTitle (title);

        }
    }

    @Override
    public void AddCustomer() {

        Intent intent = new Intent (context, AddCustomer.class);
        intent.putExtra("customer",viewModel.getCustomer());
        intent.putExtra("order",viewModel.getOrder());
        intent.putExtra("PayModes",getArguments().getSerializable("PayModes"));
        startActivityForResult (intent, ADD_CUSTOMER);
    }

    @Override
    public void customerCommit(mCustomer customer) {

    }


    @Override
    public void orderCommit() {

        if (!viewModel.getOrder ().getDocId ().equalsIgnoreCase ("0")
            && viewModel.getOrder ().getStatus ().equalsIgnoreCase ("V")){
            context.onBackPressed();
        }else if( viewModel.getOrder().getItems().size() == 0){
            AppAlert.getInstance().getAlert(context,"No Item !!!!","Please add atleast one item to the cart... ");
        }else{
            viewModel.orderCommit(context,"CASH");
        }
    }

    @Override
    public void CalculateTotal() {
        Double netAmt = 0.0;
        Double Amt = 0.0;
        Double CGST = 0.0;
        Double SGST = 0.0;
        for (mBillItem orderItem : viewModel.getOrder ().getItems ()) {
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
        header.setVisibility(viewModel.getOrder ().getItems().size()>0 ?View.VISIBLE:View.GONE);
        billdet.setVisibility(viewModel.getOrder ().getItems().size()>0 ?View.VISIBLE:View.GONE);
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
        roundAmt.setText ( String.format("%.2f",viewModel.getOrder ().getRouAmt ()));
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
    public void addAttachment() {
//        Intent intent = new Intent (context, CartAttachment.class);
//        intent.putExtra ("order", viewModel.getOrder());
//        startActivityForResult (intent, ATTACHMENT);
    }


}

