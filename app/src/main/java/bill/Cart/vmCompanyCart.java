package bill.Cart;

import android.app.Activity;
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

import bill.NewOrder.BillBatchDB;
import bill.NewOrder.BillItemDB;
import bill.NewOrder.mBillBatch;
import bill.NewOrder.mBillItem;
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

public class vmCompanyCart extends CBOViewModel<ICompanyCart> {

    private mBillOrder order;

    private BillBatchDB billBatchDB;
    private BillItemDB billDB ;

    @Override
    public void onUpdateView(AppCompatActivity context, ICompanyCart view) {

        billBatchDB = new BillBatchDB(context);
        billDB = new BillItemDB(context);

        view.getReferencesById();
        view.getActvityttitle();
        view.setTitle(view.getActvityttitle());

    }

    public mBillOrder getOrder(){
        return order;
    }
    public void setOrder(mBillOrder order){
        this.order = order;
    }



    public void getOrderItem(final AppCompatActivity context, Boolean SyncYN){




        if (SyncYN) {
            //Start of call to service

            HashMap<String, String> request = new HashMap<>();
            request.put("sCompanyFolder", view.getCompanyCode());
            request.put("iPA_ID", view.getUserId());
            request.put("iCOMPANY_ID", getOrder().getPartyId());
            try {
                request.put("DOC_DATE", CustomDatePicker.formatDate(CustomDatePicker.getDate(order.getDocDate(),CustomDatePicker.ShowFormatOld) ,CustomDatePicker.CommitFormat));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ArrayList<Integer> tables = new ArrayList<>();
            tables.add(0);
            tables.add(1);

            new MyOrderAPIService(context).execute(new ResponseBuilder("BILL_ITEMGRID_MOBILE", request)
                    .setTables(tables)
                    .setMultiTable(true)
                    /*.setShowProgess(billItems.size() == 0 )*/
                    .setResponse(new CBOServices.APIResponse() {
                        @Override
                        public void onComplete(Bundle bundle) throws Exception {
                            view.onItemSynced();
                        }

                        @Override
                        public void onResponse(Bundle bundle) throws Exception {

                            parser1(bundle);
                        }

                        @Override
                        public void onError(String s, String s1) {
                            AppAlert.getInstance().getAlert(context,s,s1);
                        }
                    }));



            //End of call to service


        }
    }


    public void parser1(Bundle result) throws JSONException {
        if (result!=null ) {
            String table0 = result.getString("Tables0");
            JSONArray jsonArray = new JSONArray(table0);

            //if (jsonArray.length() > 0) {
            billDB.delete();
            //}
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                mBillItem item = new mBillItem()
                        .setId(jsonObject2.getString("ITEM_ID"))
                        .setName(jsonObject2.getString("ITEM_NAME"))
                        .setStock(jsonObject2.getDouble("STOCK_QTY"));


                mTax GST = new mTax(eTax.getTax(jsonObject2.getInt("GST_TYPE")));
                GST.setSGST(jsonObject2.getDouble("SGST_PERCENT"))
                        .setCGST(jsonObject2.getDouble("CGST_PERCENT"));

                item.setGST(GST);
                billDB.insert(item);

            }

            String table1 = result.getString("Tables1");
            jsonArray = new JSONArray(table1);

            //if (jsonArray.length() > 0) {
            billBatchDB.delete();
            //}
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                mBillBatch item_batch = new mBillBatch()
                        .setITEM_ID(jsonObject2.getString("ITEM_ID"))
                        .setBATCH_ID(jsonObject2.getString("BATCH_ID"))
                        .setBATCH_NO(jsonObject2.getString("BATCH_NO"))
                        .setMFG_DATE(jsonObject2.getString("MFG_DATE"))
                        .setEXP_DATE(jsonObject2.getString("EXP_DATE"))
                        .setPACK(jsonObject2.getString("PACK"))
                        .setMRP_RATE(jsonObject2.getDouble("MRP_RATE"))
                        .setSALE_RATE(jsonObject2.getDouble("SALE_RATE"))
                        .setSTOCK(jsonObject2.getDouble("STOCK_QTY"));

                mDeal deal = new mDeal();
                deal.setType(eDeal.get(jsonObject2.getString("DEAL_TYPE") ))
                        .setFreeQty(jsonObject2.getDouble("DEAL_QTY"))
                        .setQty(jsonObject2.getDouble("DEAL_ON"));
                item_batch.setDeal(deal);

                item_batch.getMiscDiscount().clear();

                item_batch.getMiscDiscount().add(new mDiscount().setType(eDiscount.QI).setPercent(jsonObject2.getDouble("DIS_PERCENT1")));
                item_batch.getMiscDiscount().add(new mDiscount().setType(eDiscount.VI).setPercent(jsonObject2.getDouble("DIS_PERCENT2")));
                item_batch.getMiscDiscount().add(new mDiscount().setType(eDiscount.P).setPercent(jsonObject2.getDouble("DIS_PERCENT3")));


                billBatchDB.insert(item_batch);

            }
        }

    }

}
