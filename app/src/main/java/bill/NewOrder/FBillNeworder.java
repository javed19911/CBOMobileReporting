package bill.NewOrder;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import bill.Cart.ICompanyCart;
import bill.ItemFilter.CompanyItemFilter;
import bill.mBillOrder;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import utils_new.AppAlert;

public class FBillNeworder extends Fragment implements IFBillNewOrder {

    TextView filterTxt;
    Activity context;
    private static final int NEW_ORDER_ITEM_FILTER = 10;
    private vmBillitem viewModel;
    EditText QtyTxt;
    Button Add;
    mBillOrder order;
    Boolean keyPressed = true;
    LinearLayout mainLayout,detailLayout;
    TextView RateTxt,AmtTxt;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_order_view, container, false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case NEW_ORDER_ITEM_FILTER:

                    mBillItem item = (mBillItem) data.getSerializableExtra ("item");
                    //setItem(item);
                    selectBatch(item);
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
        Add = view.findViewById(R.id.add);
        mainLayout = view.findViewById(R.id.mainLayout);
        detailLayout = view.findViewById(R.id.detail_layout);

        RateTxt = view.findViewById(R.id.rate);
        AmtTxt = view.findViewById(R.id.amount);


        viewModel = ViewModelProviders.of (this).get (vmBillitem.class);
        viewModel.setView (context, this);

        order = new mBillOrder();

        filterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ICompanyCart) {
                    order = (((ICompanyCart) context).getOrder());
                }
                Intent intent = new Intent (context, CompanyItemFilter.class);
                intent.putExtra("syncItem", false );//!viewModel.isLoaded());
                startActivityForResult (intent, NEW_ORDER_ITEM_FILTER);
            }
        });



        QtyTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // //Chnge here
                getItem().setQty(s.toString().trim().isEmpty() ? 0D: Double.parseDouble(s.toString()) );
                updateAmt(getItem().getAmt());
               // setFreeQty(getItem().getFreeQty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getItem().getId().equals("0")) {

                    getItem().CalculateTotalAmount();
                    if (context instanceof ICompanyCart) {

                        ((ICompanyCart) context).onItemAdded(getItem());
                    }
                }else{
                    AppAlert.getInstance().getAlert(context,"No Item !!!","Please select an Item to add in the cart... ");
                }
                setItem(new mBillItem().setName(""));

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
    public void setItem(mBillItem item) {


        if (getPartyId() == null) {
            if (context instanceof ICompanyCart) {
                order = (((ICompanyCart) context).getOrder());
            }
        }

        
        //Add this later
        /*if (item.getMiscDiscount().size() == 0) {
            //viewModel.updateDiscount(item);
            item = viewModel.updateDiscount(item);
        }*/



        viewModel.setItem(item);


    }



     @Override
      public mBillItem getItem() {
          return viewModel.getItem();
      }

    @Override
    public void setAddText(String text) {
        Add.setText(text);
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
    public void selectBatch(mBillItem item) {
        viewModel.showBatchForSelection(context,item);
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
            InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(QtyTxt, InputMethodManager.HIDE_IMPLICIT_ONLY);
            QtyTxt.requestFocus();
        }else{
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


}
