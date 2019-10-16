package saleOrder.ViewModel;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.DBHelper.ItemDB;
import cbomobilereporting.cbo.com.cboorder.DBHelper.discountDB;
import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Enum.eDiscount;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;
import cbomobilereporting.cbo.com.cboorder.View.iNewOrder;

/**
 * Created by cboios on 04/03/19.
 */

public class vmNewOrder extends CBOViewModel<iNewOrder> {

    private mOrder order = new mOrder();;
    private String Kit_Type = "";
    private String filterQry ="";
    private ItemDB itemDB;
    private discountDB discountDB;
    private ArrayList<mItem> items;
    private Boolean syncItem = true;

    @Override
    public void onUpdateView(AppCompatActivity context, iNewOrder view) {
        if (view != null){
            itemDB = new ItemDB(context);
            discountDB = new discountDB(context);
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

    public void setSync(Boolean syncItem){
        this.syncItem = syncItem;
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
            view.onOrderChanged(getOrder());
        }
    }

    private void getOrderItem(AppCompatActivity context){
        getOrderItem(context,syncItem);
    }
    public void getOrderItem(final AppCompatActivity context, Boolean SyncYN){

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

       /* if (SyncYN) {
            //Start of call to service

            HashMap<String,String> request=new HashMap<>();
            request.put("sCompanyFolder", view.getCompanyCode());
            request.put("iPA_ID",  view.getPartyID() );
            request.put("iLOGIN_PA_ID",  view.getUserID() );

            ArrayList<Integer> tables=new ArrayList<>();
            tables.add(0);
            tables.add(1);

            new MyOrderAPIService(context).execute(new ResponseBuilder("ItemGrid",request)
                    .setTables(tables)
                    *//*.setShowProgess(items.size() == 0 )*//*
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


        }*/
    }




    public void parser1(Bundle result) throws JSONException {
        if (result!=null ) {
            String table0 = result.getString("Tables0");
            JSONArray jsonArray = new JSONArray(table0);

            if (jsonArray.length() > 0) {
                itemDB.delete();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                mItem item = new mItem()
                        .setId(jsonObject2.getString("ITEM_ID"))
                        .setName(jsonObject2.getString("ITEM_NAME"))
                        .setRate(jsonObject2.getDouble("RATE"))
                        .setMRP(jsonObject2.getString("MRP_RATE"))
                        .setPack(jsonObject2.getString("PACK"))
                        .setType(jsonObject2.getString("KIT_TYPE"))
                        .setGropuID(jsonObject2.getInt("ITEM_GROUP_ID"));


                mTax GST = new mTax(eTax.getTax(jsonObject2.getInt("GST_TYPE")));
                GST.setSGST(jsonObject2.getDouble("TAX_PERCENT2"))
                        .setCGST(jsonObject2.getDouble("TAX_PERCENT1"));

                /*if (GST.getType() == eTax.CENTRAL){
                    GST.getTaxs().add(new mTaxComponent(eTax.IGST).setTax(jsonObject2.getDouble("TAX_PERCENT1")));
                }else{
                    GST.getTaxs().add(new mTaxComponent(eTax.CGST).setTax(jsonObject2.getDouble("TAX_PERCENT1")));
                    GST.getTaxs().add(new mTaxComponent(eTax.SGST).setTax(jsonObject2.getDouble("TAX_PERCENT2")));
                }*/
                item.setGST(GST);


                mDeal deal = new mDeal();
                deal.setType(eDeal.valueOf(jsonObject2.getString("DEAL_TYPE")))
                        .setFreeQty(jsonObject2.getDouble("DEAL_QTY"))
                        .setQty(jsonObject2.getDouble("DEAL_ON"));
                item.setDeal(deal);

                itemDB.insert(item);

            }

            String table1 = result.getString("Tables1");
            jsonArray = new JSONArray(table1);

            if (jsonArray.length() > 0) {
                discountDB.delete();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                mDiscount discount = new mDiscount()
                        .setType(eDiscount.valueOf(jsonObject2.getString("DOC_TYPE")))
                        .setId(jsonObject2.getInt("ITEM_ID"))
                        .setSecId(jsonObject2.getInt("ITEM_ID1"))
                        .setFrom(jsonObject2.getDouble("FQTY"))
                        .setTo(jsonObject2.getDouble("TQTY"))
                        .setPercent(jsonObject2.getDouble("DISC_PERCENT"));

                discountDB.insert(discount);

            }
        }

    }
}
