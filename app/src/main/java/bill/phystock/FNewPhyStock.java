package bill.phystock;

import android.app.Activity;
import android.content.Context;
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

import bill.Cart.ICompanyCart;
import bill.ItemFilter.CompanyItemFilter;
import bill.NewOrder.IFBillNewOrder;
import bill.NewOrder.mBillItem;
import bill.NewOrder.vmBillitem;
import bill.mBillOrder;
import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;
import utils_new.AppAlert;

public class FNewPhyStock extends Fragment implements IFBillNewOrder {

    private static final int NEW_ORDER_ITEM_FILTER = 10;
    TextView filterTxt;
    AppCompatActivity context;
    EditText QtyTxt, FreeQty, dis1;
    Button Add;
    Boolean keyPressed = true;
    LinearLayout mainLayout, detailLayout, freeQtyLayout, schemeLayout;
    TextView RateTxt, AmtTxt, schemeTxt, batchTxt, packTxt, stockTxt, discAmtTxt, MRPTxt;
    private vmBillitem viewModel;
    private Boolean freeQtyNA = false;

    public Boolean getFreeQtyNA() {
        return freeQtyNA;
    }

    public void setFreeQtyNA(Boolean freeQtyNA) {
        this.freeQtyNA = freeQtyNA;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fnew_phy_stock, container, false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case NEW_ORDER_ITEM_FILTER:

                    mBillItem item = (mBillItem) data.getSerializableExtra("item");
                    //setItem(item);
                    selectBatch(item, true);
                    break;
                default:

            }
        }
    }


    public void HideFragment() {
        mainLayout.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = (AppCompatActivity) getActivity();

        filterTxt = view.findViewById(R.id.filterTxt);
        QtyTxt = view.findViewById(R.id.Qty);
        Add = view.findViewById(R.id.add);
        mainLayout = view.findViewById(R.id.mainLayout);
        detailLayout = view.findViewById(R.id.detail_layout);

        freeQtyLayout = view.findViewById(R.id.FreeQtyLayout);
        schemeLayout = view.findViewById(R.id.schemeLayout);
        FreeQty = view.findViewById(R.id.FreeQty);
        schemeTxt = view.findViewById(R.id.scheme);
        batchTxt = view.findViewById(R.id.batch);

        packTxt = view.findViewById(R.id.pack);
        stockTxt = view.findViewById(R.id.stock);
        MRPTxt = view.findViewById(R.id.mrp);

        discAmtTxt = view.findViewById(R.id.discAmt);
        dis1 = view.findViewById(R.id.Discount1);


        RateTxt = view.findViewById(R.id.rate);
        AmtTxt = view.findViewById(R.id.amount);


        viewModel = ViewModelProviders.of(this).get(vmBillitem.class);
        viewModel.setDefaultQty(0.0);
        viewModel.setView(context, this);


        filterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ICompanyCart) {
                    viewModel.setOrder(((ICompanyCart) context).getOrder());
                }
                Intent intent = new Intent(context, CompanyItemFilter.class);
                intent.putExtra("order", viewModel.getOrder());
                intent.putExtra("syncItem", false);//!viewModel.isLoaded());
                startActivityForResult(intent, NEW_ORDER_ITEM_FILTER);
            }
        });


        batchTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBillItem item = viewModel.getItem();
                selectBatch(item, false);
                setFocusQty(false);
            }
        });

        FreeQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //getItem().setFreeQty(s.toString().trim().isEmpty() ? 0D: Double.parseDouble(s.toString()) );

                String text = s.toString();
                if (text.equals(".")) {
                    text = "";
                }

                Double qty = text.trim().isEmpty() ? 0D : Double.parseDouble(text);
                /*if (getItem().getStock() != -1 &&  qty > (getItem().getStock() - getItem().getQty())){
                    qty = getItem().getStock()- getItem().getQty();
                    Double finalQty = qty;
                    AppAlert.getInstance().Alert(context, "Stock !!!", "Stock not avilable",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FreeQty.setText(""+finalQty);
                                    setFreeQty(finalQty);
                                }
                            });
                }*/

                getItem().setFreeQty(qty);

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

                String text = s.toString();
                if (text.equals(".")) {
                    text = "";
                }

                Double qty = text.trim().isEmpty() ? 0D : Double.parseDouble(text);
                getItem().setQty(qty);
               /* if (getItem().getStock() != -1 &&  qty > (getItem().getStock() - getItem().getFreeQty())){
                    qty = getItem().getStock() - getItem().getFreeQty();
                    Double finalQty = qty;
                    AppAlert.getInstance().Alert(context, "Stock !!!", "Stock not avilable",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    QtyTxt.setText(""+finalQty);
                                    getItem().setQty(finalQty);
                                    updateAmt(getItem().getTotAmt());
                                    setFreeQty(getItem().getFreeQty());
                                    setDiscAmt(getItem().getAmt() - getItem().getNetAmt());
                                }
                            });
                }else {*/
                //getItem().setQty(qty);
                updateAmt(getItem().getTotAmt());
                setFreeQty(getItem().getFreeQty());
                setDiscAmt(getItem().getAmt() - getItem().getNetAmt());
                setStockMissmatch(getItem().getStock() - getItem().getQty());
                /* }*/

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
                if (getItem().getMiscDiscount().size() == 0)
                    return;

                String text = s.toString();
                if (text.equals(".")) {
                    text = "";
                }

                mDiscount discount = getItem().getMiscDiscount().get(0);
                final Double[] dis = {text.trim().isEmpty() ? 0.0 : Double.parseDouble(text.trim())};
                if (keyPressed) {
                    keyPressed = false;
                    if (discount.getMax() < dis[0]) {
                        AppAlert.getInstance().Alert(context,
                                "Alert!!!", "Maximum discount allowed is : " + discount.getMax(),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dis[0] = discount.getMax();
                                        dis1.setText("" + dis[0]);
                                    }
                                });
                    }
                    getItem().getMiscDiscount().get(0).setPercent(dis[0]);
                    getItem().setManualDiscount(getItem().getManualDiscount());
                    updateAmt(getItem().getTotAmt());
                    setDiscAmt(getItem().getAmt() - getItem().getNetAmt());
                    keyPressed = true;
                } else {

                    getItem().setManualDiscount(getItem().getManualDiscount());
                    updateAmt(getItem().getTotAmt());
                    setDiscAmt(getItem().getAmt() - getItem().getNetAmt());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getItem().getId().equals("0") && getItem().getQty() != 0) {

                    getItem().CalculateTotalAmount();
                    if (context instanceof iPhyStock) {

                        ((iPhyStock) context).onItemAdded(getItem());
                    }
                    setItem(new mBillItem().setName(""));
                } else if (getItem().getQty() == 0) {
                    AppAlert.getInstance().getAlert(context, "No Qty. !!!", "Please enter Qty. ... ");
                    setFocusQty(true);
                } else {
                    AppAlert.getInstance().getAlert(context, "No Item !!!", "Please select an Item to add in the cart... ");
                }


            }
        });


        //filterTxt.performClick();

    }


    public void openItemFilter(mBillOrder order) {
        filterTxt.performClick();
    }

    @Override
    public String getCompanyCode() {
        return MyCustumApplication.getInstance().getUser().getCompanyCode();
    }


    @Override
    public String getPartyId() {
        return viewModel.getOrder().getPartyId();
    }

    @Override
    public String getUserId() {
        return MyCustumApplication.getInstance().getUser().getID();
    }

    @Override
    public mBillItem getItem() {
        return viewModel.getItem();
    }

    @Override
    public void setItem(mBillItem item) {


        if (getPartyId() == null) {
            if (context instanceof iPhyStock) {
                viewModel.setOrder(((iPhyStock) context).getOrder());
            }
        }


        //Add this later
        /*if (item.getMiscDiscount().size() == 0) {
            //viewModel.updateDiscount(item);
            item = viewModel.updateDiscount(item);
        }*/

        if (!item.getId().equalsIgnoreCase("0") && viewModel.getOrder().getStatus().equalsIgnoreCase("E")) {
            item.setStock(item.getQty() + item.getFreeQty());
            viewModel.updateStock(item);
        } else {
            viewModel.setItem(item);
        }


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
    public void setItemHint(String hint) {
        filterTxt.setHint(hint);
    }

    @Override
    public void setBatch(String batch) {
        batchTxt.setText(batch);
    }

    @Override
    public void selectBatch(mBillItem item, Boolean selectForcefully) {
        viewModel.showBatchForSelection(context, item, selectForcefully);
    }


    @Override
    public void setQty(Double Qty) {
        QtyTxt.setText(Qty == 0D ? "" : String.format("%.0f", Qty));
    }


    @Override
    public void updateDeal(mDeal deal) {

        if (getFreeQtyNA()) {
            freeQtyLayout.setVisibility(View.INVISIBLE);
            return;
        }

        if (deal.getType() == eDeal.NA) {
            freeQtyLayout.setVisibility(View.GONE);
            return;
        }
        if (deal.getType() == eDeal.None) {
            schemeLayout.setVisibility(View.INVISIBLE);
            return;
        }
        freeQtyLayout.setVisibility(View.VISIBLE);
        if (deal.getFreeQty() != 0D) {
            schemeTxt.setText(deal.getQty() + " + " + deal.getFreeQty());
        }

    }

    @Override
    public void setFreeQty(Double freeQty) {
        FreeQty.setText(freeQty == 0D ? "" : String.format("%.0f", freeQty));
    }


    @Override
    public void setPack(String pack) {
//        packTxt.setText(pack);
    }


    @Override
    public void setStock(Double stock) {
        stockTxt.setText("" + stock);
    }

    public void setStockMissmatch(Double stock) {
        packTxt.setText("" + stock);
    }

    @Override
    public void setMRP(Double MRP) {
        MRPTxt.setText(String.format("%.0f", MRP));
    }

    @Override
    public void setDisc1(String disc) {
        dis1.setText(disc);
    }

    @Override
    public void setDiscAmt(Double discAmt) {
        discAmtTxt.setText(String.format("%.2f", discAmt));
    }

    @Override
    public void setFocusQty(Boolean focusQty) {
        Add.setFocusable(!focusQty);
        Add.setFocusableInTouchMode(!focusQty);///add this line
        if (focusQty) {
            //QtyTxt.setText(""+viewModel.getItem().getQty());
            QtyTxt.selectAll();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            QtyTxt.requestFocus();
        } else {
            Add.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Add.getWindowToken(), 0);
        }
    }

    @Override
    public void updateRate(Double rate) {

        RateTxt.setText(String.format("%.2f", rate));
    }

    @Override
    public void updateAmt(Double amt) {
        AmtTxt.setText(String.format("%.2f", amt));
    }


}
