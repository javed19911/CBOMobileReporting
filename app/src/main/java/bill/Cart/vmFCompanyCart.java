package bill.Cart;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import bill.NewOrder.mBillItem;
import bill.mBillOrder;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.MyOrderAPIService;
import saleOrder.ViewModel.CBOViewModel;
import utils_new.AppAlert;

public class vmFCompanyCart  extends CBOViewModel<IFCompanycart> {
    private mBillOrder order = new mBillOrder();;
    @Override
    public void onUpdateView(Activity context, IFCompanycart view) {
        view.getReferencesById();
        view.setTile();


    }

    public mBillOrder getOrder(){
        return order;
    }
    public void setOrder(mBillOrder order){
        this.order = order;
    }


    public void orderCommit(Activity context, String Pay_mode){

        String itemString = "",qtyString = "",rateString = "",amtString = "";
        String sTax_Percent = "",sTax_Percent1 = "",sTax_Amt = "",sTax_Amt1 = "";
        String sDisc_Amt = "";
        String sDisc_Percent = "",sDisc_Percent1= "",sDisc_Percent2= "",
                sDisc_Percent3= "",sDisc_Percent4= "",sDisc_Percent5= "";
        String sRemark ="",sDealId = "",sDealOn = "",sDealQty = "",sFreeQty ="";

        for(mBillItem item : order.getItems()){

            if (item.getQty() != 0.0) {
                itemString = item.getId() + "," + itemString;
                qtyString = item.getQty() + "," + qtyString;
                rateString = item.getSALE_RATE() + "," + rateString;
                amtString = String.format("%.2f", item.getAmt()) + "," + amtString;

//                sTax_Percent = item.getGST().getCGST()  + "," + sTax_Percent;
//                sTax_Percent1 =  item.getGST().getSGST() + "," + sTax_Percent1;
//                sTax_Amt =   item.get  + "," + sTax_Amt;
//                sTax_Amt1 =   item.getSGSTAmt()  + "," + sTax_Amt1;

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

    private mBillItem GetOrderItemWhere(mBillItem item){

        if (getOrder() == null)
            return null;

        if (getOrder().getItems().size() > 0) {

            for (mBillItem orderItem : getOrder().getItems()) {
                if (orderItem.getId().equals(item.getId())) {
                    return orderItem;
                }
            }
        }
        return null;
    }

    public void addItem(mBillItem item){
        mBillItem orderItem = GetOrderItemWhere(item);
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
