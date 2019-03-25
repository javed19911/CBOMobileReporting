package saleOrder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.Views.iFCart;
import utils_new.AppAlert;

/**
 * Created by cboios on 06/03/19.
 */

public class vmFCart extends CBOViewModel<iFCart> {

    private mOrder order = new mOrder();;
    @Override
    public void onUpdateView(Activity context, iFCart view) {
        view.getReferencesById();
        view.setTile();
    }

    public mOrder getOrder(){
        return order;
    }
    public void setOrder(mOrder order){
        this.order = order;
    }


    public void orderCommit(Activity context, String Pay_mode){

        String itemString = "",qtyString = "",rateString = "",amtString = "";

        for(mItem item : order.getItems()){
            if (!item.getQty().trim().equalsIgnoreCase("0")) {
                itemString = item.getId() + "," + itemString;
                qtyString = item.getQty() + "," + qtyString;
                rateString = item.getRate() + "," + rateString;
                amtString = item.getAmt() + "," + amtString;
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
        request.put("iNetAmt", order.getNetAmt());
        request.put("sPymtMode", Pay_mode);
        request.put("iLogin_PA_ID", view.getUserId());

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context)
                .execute(new ResponseBuilder("OrderCommit",request)
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

}
