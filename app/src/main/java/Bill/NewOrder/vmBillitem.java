package Bill.NewOrder;

import android.app.Activity;
import android.os.Bundle;

import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.CboProgressDialog;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cbomobilereporting.cbo.com.cboorder.DBHelper.discountDB;
import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Enum.eDiscount;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;
import saleOrder.MyOrderAPIService;
import saleOrder.ViewModel.CBOViewModel;
import saleOrder.Views.iFNewOrder;
import services.MyAPIService;

public class vmBillitem  extends CBOViewModel<IFBillNewOrder> {
    private mBillItem item = new mBillItem();
    private mBillItem_Batch item_batch= new mBillItem_Batch();
    private BillItemDB billDB ;
    private BillItemBatch billbatchDB;

    @Override
    public void onUpdateView(Activity context, IFBillNewOrder view) {
       billDB = new BillItemDB(context);
        billbatchDB = new BillItemBatch(context);
        view.setFocusQty(false);
     //   view.setDetaileLayoutEnabled();



        //view.getReferencesById();
        //view.setItem(new mBillItem().setName(""));
    }


    /*public mDiscount getManagerDiscount() {
        if (managerDiscount == null){
            managerDiscount = billDB.getManagerDiscount(view.getUserId());
        }
        return managerDiscount;
    }

    public void setManagerDiscount(mDiscount discount){
        managerDiscount = discount;
    }

    public mDiscount getPartyDiscount(){
        if (partyDiscount == null){
          //  partyDiscount = billDB.getPartyDiscount(view.getPartyId());
        }
        return partyDiscount;
    }

    public ArrayList<mDiscount> getItemDiscounts(){
        //chnge here 
       *//* if (discounts == null){
            discounts = billDB.getItemDiscount(view.getPartyId(),item);
        }
        if (getPartyDiscount().getType() == eDiscount.P ){
            discounts.add(getPartyDiscount());
        }

        discounts = ArrangeDiscount(discounts);

        if (checkIfDiscountPresent(discounts,eDiscount.QI).getPercent() !=0){

            removeDiscount(discounts,eDiscount.QG);
        }else{
            removeDiscount(discounts,eDiscount.QI);
        }

        if (checkIfDiscountPresent(discounts,eDiscount.VI).getPercent() !=0){
            removeDiscount(discounts,eDiscount.VG);
        }else{
            removeDiscount(discounts,eDiscount.VI);
        }*//*
        return discounts;
    }

    private ArrayList<mDiscount> ArrangeDiscount( ArrayList<mDiscount> discounts){
        ArrayList<mDiscount> ordereddiscounts = new ArrayList<>();

        ArrayList<eDiscount> discountOrder = new ArrayList<>();
        discountOrder.add(eDiscount.QI);
        discountOrder.add(eDiscount.QG);
        discountOrder.add(eDiscount.VI);
        discountOrder.add(eDiscount.VG);
        discountOrder.add(eDiscount.P);
        discountOrder.add(eDiscount.PG);

        for (eDiscount discountType : discountOrder){
            mDiscount discount = checkIfDiscountPresent(discounts,discountType);
            if (discount != null){
                ordereddiscounts.add(discount);
            }else{
                ordereddiscounts.add(new mDiscount().setType(discountType));
            }
        }
        return ordereddiscounts;
    }

    private mDiscount checkIfDiscountPresent(ArrayList<mDiscount> discounts, eDiscount type){
        for (mDiscount discount : discounts){
            if (discount.getType() == type){
                return discount;
            }
        }
        return null;
    }


    private Boolean removeDiscount(ArrayList<mDiscount> discounts, eDiscount type){
        for (mDiscount discount : discounts){
            if (discount.getType() == type){
                discounts.remove(discount);
                return true;
            }
        }
        return false;
    }
*/
    public mBillItem getItem(){
        return item;
    }

   /* public mBillItem updateDiscount(mBillItem item){
        discounts = null;
        item.setMiscDiscount(getItemDiscounts());

        if (item.getMangerDiscount().getMax() == 100){
            //setManagerDiscount(null);
            item.getMangerDiscount().setMax(getManagerDiscount().getMax());
        }

        if (item.getNoOfDiscountAlowed()<6){
            item.getManualDiscount().setMax(0D);
        }
        if (item.getNoOfDiscountAlowed()<5){
            item.getMangerDiscount().setMax(0D);
        }

        if (item.getNoOfDiscountAlowed()<4){
            item.getMiscDiscount().get(3).setPercent(0D).setMax(0D);
        }

        if (item.getNoOfDiscountAlowed()<3){
            item.getMiscDiscount().get(2).setPercent(0D).setMax(0D);
        }

        if (item.getNoOfDiscountAlowed()<2){
            item.getMiscDiscount().get(1).setPercent(0D).setMax(0D);
        }

        if (item.getNoOfDiscountAlowed()<1){
            item.getMiscDiscount().get(0).setPercent(0D).setMax(0D);
        }
        return item;
    }*/

    public void setItem(mBillItem item){

        view.setDetaileLayoutEnabled(!item.getId().equalsIgnoreCase("0"));

        this.item = item;
        if ( item.getQty() == 0D) {
            //updateDiscount();
            view.setAddText("ADD");
        }else {
            view.setAddText("Update");
        }


        view.setItemName(item.getName());
        view.setQty(item.getQty());
        view.updateRate(Double.valueOf(item.getRate()));
        view.setFocusQty(!item.getId().equalsIgnoreCase("0"));
    }



    public void getOrderItem(final Activity context){

        if (!isLoaded()) {
            //Start of call to service

            HashMap<String, String> request = new HashMap<>();
            request.put("sCompanyFolder", view.getCompanyCode());
            request.put("iPA_ID", view.getPartyId());

            ArrayList<Integer> tables = new ArrayList<>();
            tables.add(0);
            tables.add(1);

            new MyAPIService(context).execute(new ResponseBuilder("BILL_ITEMGRID_MOBILE", request)
                    .setTables(tables)
                    .setMultiTable(true)
                    /*.setShowProgess(items.size() == 0 )*/
                    .setResponse(new CBOServices.APIResponse() {
                        @Override
                        public void onComplete(Bundle bundle) throws Exception {
                           // getOrderItem(context,false);
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
                        .setCGST_PERCENT(jsonObject2.getString("CGST_PERCENT"))
                        .setSGST_PERCENT(jsonObject2.getString("SGST_PERCENT"));



             /*   mTax GST = new mTax(eTax.getTax(jsonObject2.getInt("GST_TYPE")));
                GST.setSGST(jsonObject2.getDouble("TAX_PERCENT2"))
                        .setCGST(jsonObject2.getDouble("TAX_PERCENT1"));

                *//*if (GST.getType() == eTax.CENTRAL){
                    GST.getTaxs().add(new mTaxComponent(eTax.IGST).setTax(jsonObject2.getDouble("TAX_PERCENT1")));
                }else{
                    GST.getTaxs().add(new mTaxComponent(eTax.CGST).setTax(jsonObject2.getDouble("TAX_PERCENT1")));
                    GST.getTaxs().add(new mTaxComponent(eTax.SGST).setTax(jsonObject2.getDouble("TAX_PERCENT2")));
                }*//*
                item.setGST(GST);

                mDeal deal = new mDeal();
                deal.setType(eDeal.get(jsonObject2.getString("DEAL_TYPE") ))
                        .setId(jsonObject2.getInt("DEAL_ID"))
                        .setFreeQty(jsonObject2.getDouble("DEAL_QTY"))
                        .setQty(jsonObject2.getDouble("DEAL_ON"));
                item.setDeal(deal);*/

               billDB.insert(item);

            }

            String table1 = result.getString("Tables1");
            jsonArray = new JSONArray(table1);

            if (jsonArray.length() > 0) {
                billbatchDB.delete();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                mBillItem_Batch item_batch = new mBillItem_Batch()
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

