package saleOrder.ViewModel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.MyOrderAPIService;
import saleOrder.Views.iFCart;
import utils_new.AppAlert;

/**
 * Created by cboios on 06/03/19.
 */

public class vmFCart extends CBOViewModel<iFCart> {

    private mOrder order = new mOrder();
    @Override
    public void onUpdateView(AppCompatActivity context, iFCart view) {
        view.getReferencesById();
        view.setTile();
    }

    public mOrder getOrder(){
        return order;
    }
    public void setOrder(mOrder order){
        this.order = order;
    }


    public void orderCommit(AppCompatActivity context, String Pay_mode){

        String itemString = "",qtyString = "",rateString = "",amtString = "";
        String sTax_Percent = "",sTax_Percent1 = "",sTax_Amt = "",sTax_Amt1 = "";
        String sDisc_Amt = "";
        String sDisc_Percent = "",sDisc_Percent1= "",sDisc_Percent2= "",
                sDisc_Percent3= "",sDisc_Percent4= "",sDisc_Percent5= "";
        String sRemark ="",sDealId = "",sDealOn = "",sDealQty = "",sFreeQty ="";

        for(mItem item : order.getItems()){
            if (item.getQty() != 0.0) {
                itemString = item.getId() + "," + itemString;
                qtyString = item.getQty() + "," + qtyString;
                rateString = item.getRate() + "," + rateString;
                amtString = String.format("%.2f", item.getAmt()) + "," + amtString;

                sTax_Percent = item.getGST().getCGST()  + "," + sTax_Percent;
                sTax_Percent1 =  item.getGST().getSGST() + "," + sTax_Percent1;
                sTax_Amt =   item.getCGSTAmt()  + "," + sTax_Amt;
                sTax_Amt1 =   item.getSGSTAmt()  + "," + sTax_Amt1;
                sDisc_Amt = (item.getAmt() - item.getNetAmt()) + "," +  sDisc_Amt;
                sDisc_Percent = item.getMiscDiscount().get(0).getPercent() + "," + sDisc_Percent;
                sDisc_Percent1 = item.getMiscDiscount().get(1).getPercent() + "," + sDisc_Percent1;
                sDisc_Percent2 = item.getMiscDiscount().get(2).getPercent() + "," + sDisc_Percent2;
                sDisc_Percent3 = item.getMiscDiscount().get(3).getPercent() + "," + sDisc_Percent3;
                sDisc_Percent4 = item.getMangerDiscount().getPercent() + "," + sDisc_Percent4;
                sDisc_Percent5 = item.getManualDiscount().getPercent() + "," + sDisc_Percent5;
                sRemark = item.getRemark() + "|^" + sRemark;

                sDealId = item.getDeal().getId() + "," + sDealId;
                sDealOn = item.getDeal().getQty() + "," + sDealOn;
                sDealQty = item.getDeal().getFreeQty() + "," + sDealQty;
                sFreeQty = item.getFreeQty() + "," + sFreeQty;
            }
        }



        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", view.getCompanyCode());
        request.put("iPaId", order.getPartyId());
        request.put("iOrdId", order.getDocId());
        request.put("sItemIdStr", itemString);
        request.put("sQtyStr", qtyString);
        request.put("sRateStr", rateString);
        request.put("sAmountStr", amtString);
        request.put("iNetAmt", ""+ String.format("%.2f", order.getTotAmt())  );
        request.put("sPymtMode", Pay_mode);
        request.put("iLogin_PA_ID", view.getUserId());

        request.put("sDisc_Amt",sDisc_Amt);
        request.put("sTax_Percent", sTax_Percent);
        request.put("sTax_Percent1",sTax_Percent1 );
        request.put("sTax_Amt", sTax_Amt);
        request.put("sTax_Amt1", sTax_Amt1);
        request.put("sDisc_Percent", sDisc_Percent);
        request.put("sDisc_Percent1", sDisc_Percent1);
        request.put("sDisc_Percent2", sDisc_Percent2);
        request.put("sDisc_Percent3", sDisc_Percent3);
        request.put("sDisc_Percent4", sDisc_Percent4);
        request.put("sDisc_Percent5", sDisc_Percent5);
        request.put("sRemark", sRemark);

        request.put("sDealId", sDealId);
        request.put("sDealOn", sDealOn);
        request.put("sDealQty", sDealQty);
        request.put("sFreeQty", sFreeQty);


        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context)
                .execute(new ResponseBuilder("OrderCommit_1",request)
                        .setDescription("Please Wait..." +
                                "\nBooking your Order...")
                        .setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle bundle) throws Exception {
                                AppAlert.getInstance().Alert(context, "Success!!!",
                                        "Order Successfully Placed...", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                context.finish();
                                            }
                                        });
                            }

                            @Override
                            public void onResponse(Bundle bundle) throws Exception {

                            }

                            @Override
                            public void onError(String s, String s1) {
                                AppAlert.getInstance().getAlert(context,s,s1);
                            }
                        }));

        //End of call to service
    }

    private mItem GetOrderItemWhere(mItem item){

        if (getOrder() == null)
            return null;

        if (getOrder().getItems().size() > 0) {

            for (mItem orderItem : getOrder().getItems()) {
                if (orderItem.getId().equals(item.getId())) {
                    return orderItem;
                }
            }
        }
        return null;
    }

    public void addItem(mItem item){
        mItem orderItem = GetOrderItemWhere(item);
        if (orderItem != null){
            getOrder().getItems().remove(orderItem);
        }
        if (item.getQty() != 0.0) {
            getOrder().getItems().add(item);
        }
        if (view != null){
            view.updateOrder(getOrder());
        }
    }

}
