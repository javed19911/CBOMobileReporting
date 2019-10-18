package bill.Cart;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import bill.NewOrder.mBillItem;
import bill.mBillOrder;
import saleOrder.MyOrderAPIService;
import saleOrder.ViewModel.CBOViewModel;
import utils_new.AppAlert;
import utils_new.CustomDatePicker;

public class vmFCompanyCart  extends CBOViewModel<IFCompanycart> {
    private mBillOrder order = new mBillOrder();;
    private mCustomer customer = new mCustomer();
    @Override
    public void onUpdateView(AppCompatActivity context, IFCompanycart view) {
        view.getReferencesById();
        view.setTile();


    }

    public mCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(mCustomer customer) {
        this.customer = customer;
    }

    public mBillOrder getOrder(){
        return order;
    }
    public void setOrder(mBillOrder order){
        this.order = order;
    }

    public void orderCommit(AppCompatActivity context, String Pay_mode){
        orderCommitAll(context, "BILL_COMMIT_MOBILE",  Pay_mode);
    }

    public void orderCommitAll(AppCompatActivity context, String MethodName, String Pay_mode) {

        String itemString = "", qtyString = "", rateString = "", amtString = "", stock_diff = "", stock_str = "";
        String sTax_Percent = "", sTax_Percent1 = "", sTax_Amt = "", sTax_Amt1 = "";
        String sDisc_Amt = "";
        String sDisc_Percent = "", sDisc_Percent1 = "", sDisc_Percent2 = "";
        String sDealId = "", sDealOn = "", sDealQty = "", sFreeQty = "";

        for (mBillItem item : order.getItems()) {

            if (item.getQty() != 0.0) {
                itemString = item.getBATCH_ID() + "," + itemString;
                qtyString = item.getQty() + "," + qtyString;

                double diff=item.getStock() - item.getQty();
                stock_diff = diff + "," + stock_diff;
                stock_str = item.getStock() + "," + stock_str;
                rateString = item.getSALE_RATE() + "," + rateString;
                amtString = String.format("%.2f", item.getAmt()) + "," + amtString;

                sTax_Percent = item.getGST().getCGST() + "," + sTax_Percent;
                sTax_Percent1 = item.getGST().getSGST() + "," + sTax_Percent1;
                sTax_Amt = item.getCGSTAmt() + "," + sTax_Amt;
                sTax_Amt1 = item.getSGSTAmt() + "," + sTax_Amt1;

                sDisc_Amt = (item.getAmt() - item.getNetAmt()) + "," + sDisc_Amt;
                sDisc_Percent = item.getMiscDiscount().get(0).getPercent() + "," + sDisc_Percent;
                sDisc_Percent1 = item.getMiscDiscount().get(1).getPercent() + "," + sDisc_Percent1;
                sDisc_Percent2 = item.getMiscDiscount().get(2).getPercent() + "," + sDisc_Percent2;


                sDealId = item.getDeal().getId() + "," + sDealId;
                sDealOn = item.getDeal().getQty() + "," + sDealOn;
                sDealQty = item.getDeal().getFreeQty() + "," + sDealQty;
                sFreeQty = item.getFreeQty() + "," + sFreeQty;

            }
        }


//Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", view.getCompanyCode());
        request.put("iCOMPANY_ID", order.getPartyId());
        request.put("iPOSTING_ID", order.getDocId());
        request.put("BATCH_ID_STR", itemString);
        request.put("QTY_STR", qtyString);

        request.put("STOCK_STR", stock_str);
        request.put("STOCK_DIFF_STR", stock_diff);

        request.put("RATE_STR", rateString);
        request.put("AMOUNT_STR", amtString);
        request.put("NET_AMT_STR", "" + String.format("%.2f", order.getTotAmt()));
        request.put("sPymtMode", Pay_mode);
        request.put("LOGIN_PA_ID", view.getUserId());

        request.put("DISC_AMT_STR", sDisc_Amt);
        request.put("TAX_PERCENT_STR", sTax_Percent);
        request.put("TAX_PERCENT2_STR", sTax_Percent1);
        request.put("TAX_AMT_STR", sTax_Amt);
        request.put("TAX_AMT2_STR", sTax_Amt1);
        request.put("DISC_PERCENT_STR", sDisc_Percent);
        request.put("DISC_PERCENT2_STR", sDisc_Percent1);
        request.put("DISC_PERCENT3_STR", sDisc_Percent2);

        request.put("sDealId", sDealId);
        request.put("sDealOn", sDealOn);
        request.put("sDealQty", sDealQty);
        request.put("FREE_QTY_STR", sFreeQty);
        request.put("PAYMENT_MODE", order.getPayMode());
        try {
            request.put("DOC_DATE", CustomDatePicker.formatDate(CustomDatePicker.getDate(order.getDocDate(), CustomDatePicker.ShowFormatOld), CustomDatePicker.CommitFormat));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        request.put("MOBILE", getCustomer().getMobile());
        request.put("CUSTOMER_NAME", getCustomer().getName());
        request.put("DOB", getCustomer().getDOB());
        request.put("DOA", getCustomer().getDOA());
        request.put("ROUND_AMT", "" + order.getRouAmt());
        request.put("GST_NO", getCustomer().getGST_NO());
        request.put("REMARK", order.getRemark());


        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(2);

        new MyOrderAPIService(context)
                .execute(new ResponseBuilder(MethodName, request)
                        .setDescription("Please Wait...")
                        .setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle result) throws Exception {
                                String table1 = result.getString("Tables2");
                                JSONArray jsonArray = new JSONArray(table1);
                                JSONObject jsonObject2 = jsonArray.getJSONObject(0);

                                if (MethodName.equalsIgnoreCase("BILL_COMMIT_MOBILE")) {
                                    AppAlert.getInstance().Alert(context, "Success!!!",
                                            order.getStatus().equalsIgnoreCase("V") ?
                                                    ("Bill generated Successfully ...\nBill No. : " + jsonObject2.getString("BILL_NO")) :
                                                    ("Bill Updated Successfully ...\nBill No. : " + jsonObject2.getString("BILL_NO")), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    context.finish();
                                                }
                                            });
                                } else {
                                    AppAlert.getInstance().Alert(context, "Success!!!",
                                            order.getStatus().equalsIgnoreCase("V") ?
                                                    ("Transaction has been Saved Successfully ...\nDocument No. : " + jsonObject2.getString("BILL_NO")) :
                                                    ("Transaction has been Updated Successfully ...\nDocument No. : " + jsonObject2.getString("BILL_NO")), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    context.finish();
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onResponse(Bundle bundle) throws Exception {

                            }

                            @Override
                            public void onError(String s, String s1) {
                                AppAlert.getInstance().getAlert(context, s, s1);
                            }
                        }));
//End of call to service
    }

    private mBillItem GetOrderItemWhere(mBillItem item){

        if (getOrder() == null)
            return null;

        if (getOrder().getItems().size() > 0) {

            for (mBillItem orderItem : getOrder().getItems()) {
                if (orderItem.getBATCH_ID().equals(item.getBATCH_ID())) {
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
