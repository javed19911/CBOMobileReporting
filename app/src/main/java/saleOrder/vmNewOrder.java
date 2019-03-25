package saleOrder;

import android.app.Activity;
import android.os.Bundle;

import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cbomobilereporting.cbo.com.cboorder.DBHelper.ItemDB;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.View.iNewOrder;

/**
 * Created by cboios on 04/03/19.
 */

public class vmNewOrder extends CBOViewModel<iNewOrder> {

    private mOrder order = new mOrder();;
    private String Kit_Type = "";
    private String filterQry ="";
    private ItemDB itemDB;
    private ArrayList<mItem> items;

    @Override
    public void onUpdateView(Activity context, iNewOrder view) {
        if (view != null){
            itemDB = new ItemDB(context);
            view.getReferencesById();
            //orderDB = new OrderDB(context);
            //setStatus("P");
            view.setTile(view.getActivityTitle());
            if (getOrder() != null){
                view.onOrderChanged(getOrder());
            }
            getOrderItem(context);
        }
    }


    public mOrder getOrder(){
        return order;
    }
    public void setOrder(mOrder order){
        this.order = order;
        if (view != null){
            view.onOrderChanged(getOrder());
        }
    }

    public String getKit_Type() {
        return Kit_Type;
    }

    public void setKit_Type(String kit_Type) {
        if (kit_Type.equals("Option")){
            kit_Type = "";
        }
        Kit_Type = kit_Type;
    }

    public String getFilterQry() {
        return filterQry;
    }

    public void setFilterQry(String filterQry) {
        this.filterQry = filterQry;
    }

    public ArrayList<mItem> getItems() {
        return items;
    }

    private Integer GetOrderItemPosition(mItem item){
        int position =0;
        if (getOrder().getItems().size() > 0) {

            //if (getOrder().getItems().contains(item)) {

                for (mItem orderItem : getOrder().getItems()) {
                    if (orderItem.getId().equals(item.getId())) {
                        return position;
                    } else {
                        position++;
                    }
                }
            //}
        }
        return -1;
    }

    private mItem GetOrderItemWhere(mItem item){

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
        if (!item.getQty().equalsIgnoreCase("0")) {
            getOrder().getItems().add(item);
        }
        if (view != null){
            view.onOrderChanged(getOrder());
        }
    }

    private void getOrderItem(Activity context){
        getOrderItem(context,true);
    }
    public void getOrderItem(final Activity context, Boolean SyncYN){

        items = itemDB.items(getFilterQry(),getKit_Type());

        for (mItem item : items) {
            mItem orderItem = GetOrderItemWhere(item);
            if (orderItem != null) {
               item.setQty(orderItem.getQty())
                       .setAmt(orderItem.getAmt());

            }
        }

        if (view != null){

            view.onItemsChanged(items);
        }

//        New_Order_Multiple_Adaptor order_item_list_adaptor = new New_Order_Multiple_Adaptor(context, adaptor_data_OrderItem);
//        listView.setAdapter(order_item_list_adaptor);

        if (SyncYN) {
            //Start of call to service

            HashMap<String,String> request=new HashMap<>();
            request.put("sCompanyFolder", view.getCompanyCode());
            request.put("iPA_ID", view.getPartyID() );

            ArrayList<Integer> tables=new ArrayList<>();
            tables.add(0);

            new MyOrderAPIService(context).execute(new ResponseBuilder("ItemGrid",request)
                    .setTables(tables)
                    .setShowProgess(items.size() == 0 )
                    .setResponse(new CBOServices.APIResponse() {
                        @Override
                        public void onComplete(Bundle bundle) throws Exception {
                            getOrderItem(context,false);
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


//    public void parser2(Bundle result) throws JSONException {
//        if (result!=null ) {
//
//                String table0 = result.getString("Tables0");
//                JSONArray jsonArray1 = new JSONArray(table0);
//
//                String order_no;
//                for (int i = 0; i < jsonArray1.length(); i++) {
//                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
//                    order_no=jsonObject2.getString("ORD_ID");
//                    shareclass.save(this,"order_no",order_no);
//                    insertOrderDetailToLocal(order_no);
//                    paymentSuccess();
//                }
//
//
//
//        }
//
//
//    }


    public void parser1(Bundle result) throws JSONException {
        if (result!=null ) {
            String table0 = result.getString("Tables0");
            JSONArray jsonArray1 = new JSONArray(table0);

            if (jsonArray1.length() > 0) {
                itemDB.delete();
            }
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                mItem item = new mItem()
                        .setId(jsonObject2.getString("ITEM_ID"))
                        .setName(jsonObject2.getString("ITEM_NAME"))
                        .setRate(jsonObject2.getString("RATE"))
                        .setMRP(jsonObject2.getString("MRP_RATE"))
                        .setPack(jsonObject2.getString("PACK"))
                        .setType(jsonObject2.getString("KIT_TYPE"));
                itemDB.insert(item);

            }
        }

    }
}
