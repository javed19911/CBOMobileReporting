package bill.ItemFilter;

import android.app.Activity;
import android.os.Bundle;

import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import bill.NewOrder.BillBatchDB;
import bill.NewOrder.BillItemDB;
import bill.NewOrder.mBillItem;
import bill.NewOrder.mBillBatch;
import saleOrder.MyOrderAPIService;
import saleOrder.ViewModel.CBOViewModel;

public class vmBill_ItemFilter extends CBOViewModel<IitemNewOrder> {

    private ArrayList<mBillItem>billItems=new ArrayList<>();
    private BillItemDB billItemDB;
    private String filterqry="";
    private Boolean syncItem = true;


    private BillItemDB billDB ;
    private BillBatchDB billbatchDB;

    @Override
    public void onUpdateView(Activity context, IitemNewOrder view) {

        billDB = new BillItemDB(context);
        billbatchDB = new BillBatchDB(context);

        if (view != null){
            view.getReferencesById();
            billItemDB =new BillItemDB(context);
            //orderDB = new OrderDB(context);
            //setStatus("P");
            view.setTile(view.getActivityTitle());
            //change here
           /* if (getOrder() != null){
                view.onOrderChanged(getOrder());
            }*/
            getOrderItem(context);
        }
    }



    public void setSync(Boolean syncItem){
        this.syncItem = syncItem;
    }


    public String getFilterQry() {
        return filterqry;
    }

    public void setFilterQry(String filterQry) {
        this.filterqry = filterQry;
    }

    public ArrayList<mBillItem> getItems() {
        return billItems;
    }


    private void getOrderItem(Activity context){
        getOrderItem(context,syncItem);
    }
    public void getOrderItem(final Activity context, Boolean SyncYN){

        billItems = billItemDB.items(getFilterQry());

        if (view != null){

            view.onItemsChanged(billItems);
        }


        if (SyncYN) {
            //Start of call to service

            HashMap<String, String> request = new HashMap<>();
            request.put("sCompanyFolder", view.getCompanyCode());
            request.put("iPA_ID", view.getPartyID());

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
                            if (view != null){

                                view.onItemsChanged(billItems);
                            }
                        }

                        @Override
                        public void onResponse(Bundle bundle) throws Exception {

                            parser1(bundle);
                        }

                        @Override
                        public void onError(String s, String s1) {

                        }
                    }));



            //End of call to service


        }
    }


    public void parser1(Bundle result) throws JSONException {
        if (result!=null ) {
            String table0 = result.getString("Tables0");
            JSONArray jsonArray = new JSONArray(table0);

            if (jsonArray.length() > 0) {
                billDB.delete();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                ////"ITEM_ID":"1","ITEM_NAME":"Ab-Lol","SGST_PERCENT":"0","CGST_PERCENT":"0"
                mBillItem item = new mBillItem()
                        .setId(jsonObject2.getString("ITEM_ID"))
                        .setName(jsonObject2.getString("ITEM_NAME"))
                        .setCGST_PERCENT(jsonObject2.getDouble("CGST_PERCENT"))
                        .setSGST_PERCENT(jsonObject2.getDouble("SGST_PERCENT"));
                billDB.insert(item);

            }

            String table1 = result.getString("Tables1");
            jsonArray = new JSONArray(table1);

            if (jsonArray.length() > 0) {
                billbatchDB.delete();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                mBillBatch item_batch = new mBillBatch()
                        .setITEM_ID(jsonObject2.getString("ITEM_ID"))
                        .setBATCH_ID(jsonObject2.getString("BATCH_ID"))
                        .setBATCH_NO(jsonObject2.getString("BATCH_NO"))
                        .setMFG_DATE(jsonObject2.getString("MFG_DATE"))
                        .setEXP_DATE(jsonObject2.getString("EXP_DATE"))
                        .setPACK(jsonObject2.getString("PACK"))
                        .setMRP_RATE(jsonObject2.getString("MRP_RATE"))

                        .setSALE_RATE(jsonObject2.getString("SALE_RATE"));

                billbatchDB.insert(item_batch);

            }
        }

    }


}



