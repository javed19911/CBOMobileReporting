package saleOrder.Fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.Activities.ItemFilterActivity;
import saleOrder.Enum.eItem;
import saleOrder.ViewModel.vmFCart;
import saleOrder.ViewModel.vmItem;
import saleOrder.Views.iCart;
import saleOrder.Views.iFNewOrder;
import utils_new.AppAlert;

public class FNewOrder  extends Fragment implements iFNewOrder {

    TextView filterTxt;
    Activity context;
    private static final int NEW_ORDER_ITEM_FILTER = 10;
    private vmItem viewModel;
    EditText QtyTxt,Manualdiscount,managerDiscount;
    Button Add;
    mOrder order;
    Boolean keyPressed = true;
    LinearLayout mainLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_new_order_view, container, false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case NEW_ORDER_ITEM_FILTER:
                    viewModel.setManagerDiscount(null);
                    mItem item = (mItem) data.getSerializableExtra ("item");
                    item.setMangerDiscount(viewModel.getManagerDiscount().setPercent(0D));
                    setItem(item);
                    break;
                default:

            }
        }
    }


    public void HideFragment(){

        mainLayout.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        context = getActivity();

        filterTxt = view.findViewById(R.id.filterTxt);
        QtyTxt = view.findViewById(R.id.Qty);
        Manualdiscount = view.findViewById(R.id.Manualdiscount);
        managerDiscount = view.findViewById(R.id.managerDiscount);
        Add = view.findViewById(R.id.add);
        mainLayout = view.findViewById(R.id.mainLayout);

        viewModel = ViewModelProviders.of (this).get (vmItem.class);
        viewModel.setView (context, this);

         order = new mOrder();


        filterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof iCart) {
                    order = (((iCart) context).getOrder());
                }
                Intent intent = new Intent (context, ItemFilterActivity.class);
                intent.putExtra ("order", order);
                intent.putExtra("itemType", eItem.PRODUCT);
                intent.putExtra("syncItem", !viewModel.isLoaded());
                startActivityForResult (intent, NEW_ORDER_ITEM_FILTER);
            }
        });

        QtyTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getItem().setQty(s.toString().trim().isEmpty() ? 0D: Double.parseDouble(s.toString()) );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        managerDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDiscount discount =  getItem().getMangerDiscount();
                final Double[] dis = {s.toString().trim().isEmpty() ? 0.0 : Double.parseDouble(s.toString().trim())};
                if (keyPressed ) {
                    keyPressed = false;
                    if(discount.getMax() < dis[0]){
                        AppAlert.getInstance().Alert(context,
                                "Alert!!!", "Maximum discount allowed is : " + discount.getMax(),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dis[0] = discount.getMax();
                                        managerDiscount.setText(""+ dis[0]);
                                    }
                                });
                    }
                    getItem().setMangerDiscount(discount.setPercent(dis[0]));
                    keyPressed = true;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Manualdiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mDiscount discount =  getItem().getManualDiscount();
                final Double[] dis = {s.toString().trim().isEmpty() ? 0.0 : Double.parseDouble(s.toString().trim())};
                if (keyPressed ) {
                    keyPressed = false;
                    if(discount.getMax() < dis[0]){
                        //
                        AppAlert.getInstance().Alert(context,
                                "Alert!!!", "Maximum discount allowed is : " + discount.getMax(),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dis[0] = discount.getMax();
                                        Manualdiscount.setText(""+ dis[0]);
                                    }
                                });

                    }
                    getItem().setManualDiscount(discount.setPercent(dis[0]));
                    keyPressed = true;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        Add.setFocusable(true);
//        Add.setFocusableInTouchMode(true);///add this line
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getItem().getId().equals("0")) {
                    viewModel.updateDiscount();
                    if (context instanceof iCart) {
                        ((iCart) context).onItemAdded(getItem());
                    }
                }else{
                    AppAlert.getInstance().getAlert(context,"No Item !!!","Please select an Item to add in the cart... ");
                }
                viewModel.setManagerDiscount(null);
                setItem(new mItem().setName(""));
                /*Add.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Add.getWindowToken(), 0);*/
            }
        });

    }


    @Override
    public String getCompanyCode() {
        return MyCustumApplication.getInstance().getUser().getCompanyCode();
    }

    @Override
    public String getPartyId() {
        return order.getPartyId();
    }

    @Override
    public String getUserId() {
        return MyCustumApplication.getInstance ().getUser ().getID ();
    }

    @Override
    public void setItem(mItem item) {
        if (getPartyId() == null) {
            if (context instanceof iCart) {
                order = (((iCart) context).getOrder());
            }
        }

        viewModel.setItem(item);

        /*if (!item.getId().trim().equalsIgnoreCase("0")) {
            QtyTxt.requestFocus();
            QtyTxt.selectAll();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }*/

    }

    @Override
    public mItem getItem() {
        return viewModel.getItem();
    }

    @Override
    public void setItemName(String name) {
        filterTxt.setText(name);
    }

    @Override
    public void setManualDiscount(mDiscount discount) {
        Manualdiscount.setEnabled(discount.getMax() != 0);
        Manualdiscount.setText(discount.getPercent() == 0D ?"":""+discount.getPercent());
        if (discount.getPercent() == 0 && discount.getMax() == 0){
            Manualdiscount.setVisibility(View.GONE);
        }else{
            Manualdiscount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setMiscDiscount(ArrayList<mDiscount> discounts) {

    }


    @Override
    public void setManagerDiscount(mDiscount discount) {
        managerDiscount.setEnabled(discount.getMax() != 0);
        managerDiscount.setText(discount.getPercent() == 0D ?"": ""+discount.getPercent());
        if (discount.getPercent() == 0 && discount.getMax() == 0){
            managerDiscount.setVisibility(View.GONE);
        }else{
            managerDiscount.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void setQty(Double Qty) {
        QtyTxt.setText(Qty == 0D ?"": ""+Qty);
    }

    @Override
    public void setFocusQty(Boolean focusQty) {
        Add.setFocusable(!focusQty);
        Add.setFocusableInTouchMode(!focusQty);///add this line
        if (focusQty) {
            //QtyTxt.setText(""+viewModel.getItem().getQty());
            QtyTxt.selectAll();
            QtyTxt.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(QtyTxt, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }else{
            viewModel.getOrderItem(context);
            Add.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Add.getWindowToken(), 0);
        }
    }
}
