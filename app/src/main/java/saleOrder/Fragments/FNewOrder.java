package saleOrder.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import saleOrder.Activities.ItemFilterActivity;
import saleOrder.Enum.eItem;
import saleOrder.ViewModel.vmItem;
import saleOrder.Views.iCart;
import saleOrder.Views.iFNewOrder;
import utils_new.AppAlert;

public class FNewOrder  extends Fragment implements iFNewOrder {

    TextView filterTxt;
    AppCompatActivity context;
    private static final int NEW_ORDER_ITEM_FILTER = 10;
    private vmItem viewModel;
    EditText QtyTxt,Manualdiscount,managerDiscount,remarkTxt,FreeQty;
    EditText dis1,dis2,dis3,dis4;
    Button Add;
    mOrder order;
    Boolean keyPressed = true;
    LinearLayout mainLayout,detailLayout,freeQtyLayout;
    TextView RateTxt,AmtTxt,schemeTxt;
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

        context = (AppCompatActivity) getActivity();

        filterTxt = view.findViewById(R.id.filterTxt);
        QtyTxt = view.findViewById(R.id.Qty);
        Manualdiscount = view.findViewById(R.id.Manualdiscount);
        managerDiscount = view.findViewById(R.id.managerDiscount);
        Add = view.findViewById(R.id.add);
        mainLayout = view.findViewById(R.id.mainLayout);
        detailLayout = view.findViewById(R.id.detail_layout);

        freeQtyLayout = view.findViewById(R.id.FreeQtyLayout);
        FreeQty = view.findViewById(R.id.FreeQty);
        schemeTxt = view.findViewById(R.id.scheme);


        RateTxt = view.findViewById(R.id.rate);
        AmtTxt = view.findViewById(R.id.amount);
        remarkTxt = view.findViewById(R.id.remark);

        dis1 = view.findViewById(R.id.Discount1);
        dis2 = view.findViewById(R.id.discount2);
        dis3 = view.findViewById(R.id.Discount3);
        dis4 = view.findViewById(R.id.discount4);

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

        FreeQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getItem().setFreeQty(s.toString().trim().isEmpty() ? 0D: Double.parseDouble(s.toString()) );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        QtyTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getItem().setQty(s.toString().trim().isEmpty() ? 0D: Double.parseDouble(s.toString()) );
                updateAmt(getItem().getAmt());
                setFreeQty(getItem().getFreeQty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        remarkTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getItem().setRemark(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dis1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDiscount discount =  getItem().getMiscDiscount().get(0);
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
                                        dis1.setText(""+ dis[0]);
                                    }
                                });
                    }
                    getItem().getMiscDiscount().get(0).setPercent(dis[0]);
                    keyPressed = true;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        dis2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDiscount discount =  getItem().getMiscDiscount().get(1);
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
                                        dis2.setText(""+ dis[0]);
                                    }
                                });
                    }
                    getItem().getMiscDiscount().get(1).setPercent(dis[0]);
                    keyPressed = true;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dis3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDiscount discount =  getItem().getMiscDiscount().get(2);
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
                                        dis3.setText(""+ dis[0]);
                                    }
                                });
                    }
                    getItem().getMiscDiscount().get(2).setPercent(dis[0]);
                    keyPressed = true;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dis4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDiscount discount =  getItem().getMiscDiscount().get(3);
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
                                        dis4.setText(""+ dis[0]);
                                    }
                                });
                    }
                    getItem().getMiscDiscount().get(3).setPercent(dis[0]);
                    keyPressed = true;
                }

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
                    //if (getEditableDiscount().trim().isEmpty()) {
                      viewModel.updateDiscount(getItem());
                    //}
                    if (getEditableDiscount().contains("1")){
                        getItem().getMiscDiscount().get(0).setPercent(dis1.getText().toString().trim().isEmpty() ? 0.0 : Double.parseDouble(dis1.getText().toString().trim()));
                    }

                    if (getEditableDiscount().contains("2")){
                        getItem().getMiscDiscount().get(1).setPercent(dis2.getText().toString().trim().isEmpty() ? 0.0 : Double.parseDouble(dis2.getText().toString().trim()));
                    }

                    if (getEditableDiscount().contains("3")){
                        getItem().getMiscDiscount().get(2).setPercent(dis3.getText().toString().trim().isEmpty() ? 0.0 : Double.parseDouble(dis3.getText().toString().trim()));
                    }

                    if (getEditableDiscount().contains("4")){
                        getItem().getMiscDiscount().get(3).setPercent(dis4.getText().toString().trim().isEmpty() ? 0.0 : Double.parseDouble(dis4.getText().toString().trim()));
                    }

                    getItem().CalculateTotalAmount();

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
        item.setNoOfDiscountAlowed(getNoOfDiscountAllowed());
        item.setRemarkReqd(IsRemarkReqd());

        if (getPartyId() == null) {
            if (context instanceof iCart) {
                order = (((iCart) context).getOrder());
            }
        }

        if (item.getMiscDiscount().size() == 0) {
            //viewModel.updateDiscount(item);
            item = viewModel.updateDiscount(item);
        }


        if (!getEditableDiscount().contains("1")){
            item.getMiscDiscount().get(0).setMax(0D);
        }

        if (!getEditableDiscount().contains("2")){
            item.getMiscDiscount().get(1).setMax(0D);
        }

        if (!getEditableDiscount().contains("3")){
            item.getMiscDiscount().get(2).setMax(0D);
        }

        if (!getEditableDiscount().contains("4")){
            item.getMiscDiscount().get(3).setMax(0D);
        }

        if (item.getNoOfDiscountAlowed()<6){
            item.getManualDiscount().setMax(0D);
        }
        if (item.getNoOfDiscountAlowed()<5){
            item.getMangerDiscount().setMax(0D);
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
    public void setAddText(String text) {
        Add.setText(text);
    }

    @Override
    public int getNoOfDiscountAllowed() {
        return Integer.parseInt( MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("ORD_DISC_TYPE","6"));
    }

    @Override
    public String getEditableDiscount() {
        return MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("ORD_DISC_EDITCOLS","");
    }

    @Override
    public void setDetaileLayoutEnabled(Boolean enabled) {
        detailLayout.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setItemName(String name) {
        filterTxt.setText(name);
    }

    @Override
    public void ManualDiscountEnabled(Boolean enabled) {
        Manualdiscount.setEnabled(enabled);
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
        mDiscount discount = discounts.get(0);
        dis1.setEnabled(discount.getMax() != 0);
        dis1.setText(discount.getPercent() == 0D ?"":""+discount.getPercent());
        if (discount.getPercent() == 0 && discount.getMax() == 0){
            dis1.setVisibility(View.GONE);
        }else{
            dis1.setVisibility(View.VISIBLE);
        }


        discount = discounts.get(1);
        dis2.setEnabled(discount.getMax() != 0);
        dis2.setText(discount.getPercent() == 0D ?"":""+discount.getPercent());
        if (discount.getPercent() == 0 && discount.getMax() == 0){
            dis2.setVisibility(View.GONE);
        }else{
            dis2.setVisibility(View.VISIBLE);
        }


        discount = discounts.get(2);
        dis3.setEnabled(discount.getMax() != 0);
        dis3.setText(discount.getPercent() == 0D ?"":""+discount.getPercent());
        if (discount.getPercent() == 0 && discount.getMax() == 0){
            dis3.setVisibility(View.GONE);
        }else{
            dis3.setVisibility(View.VISIBLE);
        }

        discount = discounts.get(3);
        dis4.setEnabled(discount.getMax() != 0);
        Manualdiscount.setText(discount.getPercent() == 0D ?"":""+discount.getPercent());
        if (discount.getPercent() == 0 && discount.getMax() == 0){
            dis4.setVisibility(View.GONE);
        }else{
            dis4.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void ManagerDiscountEnabled(Boolean enabled) {
        managerDiscount.setEnabled(enabled);
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
    public Boolean IsRemarkReqd() {
        return MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("SALE_ORDER_REMARKYN","N").equalsIgnoreCase("Y");
    }

    @Override
    public void setRemarkEnabled(Boolean required) {
        remarkTxt.setHint(getRemarkTitle());
        remarkTxt.setVisibility(required? View.VISIBLE:View.GONE);
    }

    @Override
    public String getRemarkTitle() {
        return MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("SALE_ORDER_REMARK_TITLE","Remark");
    }


    @Override
    public void setRemark(String remark) {
        remarkTxt.setText(remark);
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

    @Override
    public void updateRate(Double rate) {
        RateTxt.setText(" X " +  AddToCartView.toCurrency(String.format("%.2f", rate)));
    }

    @Override
    public void updateAmt(Double amt) {
        AmtTxt.setText(AddToCartView.toCurrency(String.format("%.2f", amt)));
    }

    @Override
    public void updateDeal(mDeal deal) {
        if (deal.getType() == eDeal.NA){
            freeQtyLayout.setVisibility(View.GONE);
            return;
        }
        freeQtyLayout.setVisibility(View.VISIBLE);
        if (deal.getFreeQty() != 0D) {
            schemeTxt.setText("*Scheme : " + deal.getQty() + " + " + deal.getFreeQty());
        }

    }

    @Override
    public void setFreeQty(Double freeQty) {
        FreeQty.setText( freeQty == 0D ? "" : "" + freeQty);
    }

    @Override
    public void updateDiscountAmt(Double DisAmt) {

    }

    @Override
    public void updateDiscountStr(String DisStr) {

    }

    @Override
    public void updateTAX(mTax GST) {

    }

    @Override
    public void updateNetAmount(Double NetAmt) {

    }
}
