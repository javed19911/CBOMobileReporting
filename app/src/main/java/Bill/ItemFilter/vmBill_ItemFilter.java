package Bill.ItemFilter;

import android.app.Activity;
import android.os.Bundle;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Bill.NewOrder.BillItemBatch;
import Bill.NewOrder.BillItemDB;
import Bill.NewOrder.mBillItem;
import cbomobilereporting.cbo.com.cboorder.DBHelper.ItemDB;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import saleOrder.ViewModel.CBOViewModel;
import services.MyAPIService;
import utils_new.AppAlert;

public class vmBill_ItemFilter extends CBOViewModel<IitemNewOrder> {

    private ArrayList<mBillItem>billItems=new ArrayList<>();
    private BillItemDB billItemDB;
    private BillItemBatch billItemBatch;
    private String filterqry="";
    private String Kit_Type = "";
    private Boolean syncItem = true;

    @Override
    public void onUpdateView(Activity context, IitemNewOrder view) {
        if (view != null){
            //chnage herer

            view.getReferencesById();
            billItemDB =new BillItemDB(context);
            billItemBatch = new BillItemBatch(context);
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
        return filterqry;
    }

    public void setFilterQry(String filterQry) {
        this.filterqry = filterQry;
    }

    public ArrayList<mBillItem> getItems() {
        return billItems;
    }
/*

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
*/

    private void getOrderItem(Activity context){
        getOrderItem(context,syncItem);
    }
    public void getOrderItem(final Activity context, Boolean SyncYN){

        billItems = billItemDB.items(getFilterQry(),getKit_Type());

      /*  for (mBillItem item : billItems) {
            mItem orderItem = GetOrderItemWhere(item);
            if (orderItem != null) {
                item.setQty(orderItem.getQty())
                        .setAmt(orderItem.getAmt());

            }
        }
*/
        if (view != null){

            view.onItemsChanged(billItems);
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



    private void getOrderItem1(Activity context) {
        HashMap<String, String> request = new HashMap<>();

        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        tables.add(1);


        new MyAPIService(context)
                .execute(
                        new ResponseBuilder("BILL_ITEMGRID_MOBILE", request)
                                 .setMultiTable(true)
                                .setTables(tables)
                                .setResponse(
                                        new CBOServices.APIResponse() {
                                            @Override
                                            public void onComplete(Bundle bundle) {
                                                if (view != null) {
                                                    view.onItemsChanged(billItems);

                                                }

                                            }

                                            @Override
                                            public void onResponse(Bundle bundle) throws Exception {
                                                parser1(bundle);
                                                //"COMPANY_ID":"23","COMPANY_NAME":"ABN Medical Specialties"
                                            }

                                            @Override
                                            public void onError(String s, String s1) {
                                                AppAlert.getInstance().getAlert(context,s,s1);                                          }
                                        }));




    }

    private void parser1(Bundle result) throws JSONException {
        if (result!=null ) {
            String table0 = result.getString("Tables0");
            JSONArray jsonArray = new JSONArray(table0);

            if (jsonArray.length() > 0) {
                //itemDB.delete();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                mBillItem item = new mBillItem()
                        //"ITEM_ID":"1","ITEM_NAME":"Ab-Lol","SGST_PERCENT":"0","CGST_PERCENT":"0"
                        .setId(jsonObject2.getString("ITEM_ID"))
                        .setName(jsonObject2.getString("ITEM_NAME"));
                billItems.add(item);
                       /* .setGST()
                       // .setRate(jsonObject2.getDouble("RATE"))
                        .setMRP(jsonObject2.getString("MRP_RATE"))
                        .setPack(jsonObject2.getString("PACK"))
                        .setType(jsonObject2.getString("KIT_TYPE"))
                        .setGropuID(jsonObject2.getInt("ITEM_GROUP_ID"));*/

//chnage here
                /*mTax GST = new mTax(eTax.getTax(jsonObject2.getInt("GST_TYPE")));
                GST.setSGST(jsonObject2.getDouble("SGST_PERCENT"))
                        .setCGST(jsonObject2.getDouble("CGST_PERCENT"));*/

                /*if (GST.getType() == eTax.CENTRAL){
                    GST.getTaxs().add(new mTaxComponent(eTax.IGST).setTax(jsonObject2.getDouble("TAX_PERCENT1")));
                }else{
                    GST.getTaxs().add(new mTaxComponent(eTax.CGST).setTax(jsonObject2.getDouble("TAX_PERCENT1")));
                    GST.getTaxs().add(new mTaxComponent(eTax.SGST).setTax(jsonObject2.getDouble("TAX_PERCENT2")));
                }*/
               // item.setGST(GST);

//chnge here
               /* mDeal deal = new mDeal();
                deal.setType(eDeal.valueOf(jsonObject2.getString("DEAL_TYPE")))
                        .setFreeQty(jsonObject2.getDouble("DEAL_QTY"))
                        .setQty(jsonObject2.getDouble("DEAL_ON"));
                item.setDeal(deal);*/

              //  itemDB.insert(item);

            }
/*
            String table1 = result.getString("Tables1");
         JSONArray jsonArra2 = new JSONArray(table1);
*//*
            if (jsonArray.length() > 0) {
               // discountDB.delete();
            }*//*
            for (int i = 0; i < jsonArra2.length(); i++) {
                JSONObject jsonObject2 = jsonArra2.getJSONObject(i);
                //ITEM_ID":"1","BATCH_ID":"1","BATCH_NO":"T-13331","MFG_DATE":"01/08/2013","EXP_DATE":"31/07/2015","PACK":"1X 10","MRP_RATE":"77","SALE_RATE":"77"
                mBillItem itemDetail = new mBillItem()
                        //.sesettType(eDiscount.valueOf(jsonObject2.getString("DOC_TYPE")))
                        .setITEM_ID(jsonObject2.getString("ITEM_ID"))
                        .setBATCH_ID(jsonObject2.getString("BATCH_ID"))
                        .setBATCH_NO(jsonObject2.getString("BATCH_NO"))
                        .setMFG_DATE(jsonObject2.getString("MFG_DATE"))
                        .setEXP_DATE(jsonObject2.getString("EXP_DATE"))
                        .setPACK(jsonObject2.getString("PACK"))
                        .setMRP_RATE(jsonObject2.getString("MRP_RATE"))

                        .setSALE_RATE(jsonObject2.getString("SALE_RATE"));
                billItems.add(itemDetail);*/

                        //cgnafger here
              //  discountDB.insert(discount);


            }
        }

    }



