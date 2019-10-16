package bill.NewOrder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import bill.Cart.Batch_Dialog;
import bill.mBillOrder;
import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Enum.eDiscount;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;
import saleOrder.MyOrderAPIService;
import saleOrder.ViewModel.CBOViewModel;
import utils_new.AppAlert;
import utils_new.CustomDatePicker;

public class vmBillitem  extends CBOViewModel<IFBillNewOrder> {
    private mBillItem item = new mBillItem();
    private BillBatchDB billBatchDB;
    private mBillOrder order;

    @Override
    public void onUpdateView(AppCompatActivity context, IFBillNewOrder view) {

        billBatchDB = new BillBatchDB(context);
        order = new mBillOrder();
        view.setDetaileLayoutEnabled(false);

    }


    public mBillOrder getOrder() {
        return order;
    }

    public void setOrder(mBillOrder order) {
        this.order = order;
    }

    public mBillItem getItem(){
        return item;
    }



    public void setItem(mBillItem item){



        view.setDetaileLayoutEnabled(!item.getId().equalsIgnoreCase("0"));

        this.item = item;
        if ( item.getQty() == 0D) {
            //updateDiscount();

            item.setQty(1.0);
            view.setAddText("ADD");
        }else {
            view.setAddText("Update");
        }


        Double fQty = item.getFreeQty();

        view.setItemName(item.getName());
        view.setItemHint(getOrder().getItems().size()>0 ?"Search for next Items/Products":"Search Items/Products");
        view.setBatch(item.getBATCH_NO());
        view.setQty(item.getQty());

        item.setFreeQty(fQty);
        view.setFreeQty(item.getFreeQty());
        view.setPack(item.getPACK());
        view.setStock(item.getStock());
        view.setMRP(item.getMRP_RATE());

        if (item.getMiscDiscount().size()>0) {
            view.setDisc1("" + item.getMiscDiscount().get(0).getPercent());
        }
        view.setDiscAmt(item.getAmt()- item.getNetAmt());

        view.updateRate(Double.valueOf(item.getSALE_RATE()));
        view.updateDeal(item.getDeal());
        view.setFocusQty(!item.getId().equalsIgnoreCase("0"));
    }


    public void updateStock(mBillItem item){
        ArrayList<mBillBatch> batches = billBatchDB.batches(item,item.getBATCH_ID());
        if (batches.size()>0){
            mBillBatch batch = batches.get(0);
            batch.setSTOCK(batch.getSTOCK() + item.getStock());
            updateItemWithBatch(item,batch);
        }

        setItem(item);
    }



    public void showBatchForSelection(Context context,mBillItem item,Boolean selectForcefully){
        ArrayList<mBillBatch> batches = billBatchDB.batches(item);

        if (batches.size() == 1 & selectForcefully){
            setBatch(item,batches.get(0));
            return;
        }
        if (batches.size() == 0){
            AppAlert.getInstance().getAlert(context,"Stock !!!!","No Stock avilable....");
            return;
        }

        new Batch_Dialog(context,batches , new Batch_Dialog.OnItemClickListener() {
            @Override
            public void ItemSelected(mBillBatch batch) {
                if (view != null){
                    setBatch(item,batch);
                }
            }
        }).show(item.getName());
    }

    private void updateItemWithBatch( mBillItem item,mBillBatch batch){
        item.setBATCH_NO(batch.getBATCH_NO())
                .setBATCH_ID(batch.getBATCH_ID())
                .setSALE_RATE(batch.getSALE_RATE())
                .setMRP_RATE(batch.getMRP_RATE())
                .setMFG_DATE(batch.getMFG_DATE())
                .setEXP_DATE(batch.getEXP_DATE())
                .setPACK(batch.getPACK())
                .setStock(batch.getSTOCK());
    }

    private void setBatch( mBillItem item,mBillBatch batch){


        updateItemWithBatch(item,batch);


        mBillItem orderItem = GetOrderItemWhere(item);
        if (orderItem != null){
            item.setQty(orderItem.getQty())
                    .setFreeQty(orderItem.getFreeQty())
                    .setAmt(orderItem.getAmt());


            batch.getMiscDiscount().get(0).setPercent(orderItem.getMiscDiscount().get(0).getPercent());
        }

        item.setMiscDiscount(batch.getMiscDiscount());

        view.setItem(item);
    }


    private mBillItem GetOrderItemWhere( mBillItem item){

        if (order == null)
            return null;

        if (order.getItems().size() > 0) {

            for (mBillItem orderItem : order.getItems()) {
                if (orderItem.getBATCH_ID().equals(item.getBATCH_ID())) {
                    return orderItem;
                }
            }
        }
        return null;
    }






}

