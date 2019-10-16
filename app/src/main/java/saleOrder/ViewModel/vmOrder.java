package saleOrder.ViewModel;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cbomobilereporting.cbo.com.cboorder.DBHelper.OrderDB;
import cbomobilereporting.cbo.com.cboorder.DBHelper.OrderDetailDB;
import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Enum.eDiscount;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;
import cbomobilereporting.cbo.com.cboorder.View.iOrder;
import saleOrder.MyOrderAPIService;
import utils_new.AppAlert;

/**
 * Created by cboios on 04/03/19.
 */

public class vmOrder extends CBOViewModel<iOrder> {


    private OrderDB orderDB;
    private OrderDetailDB orderDetailDB;
    private String Status="P";

    private ArrayList<mOrder> orders = new ArrayList<>();

    public vmOrder() {

    }

    @Override
    public void onUpdateView(AppCompatActivity context, iOrder view) {
        if (view != null){
            orderDB = new OrderDB(context);
            orderDetailDB = new OrderDetailDB(context);
            view.getReferencesById();
            //setStatus("P");
            view.setTile(view.getActivityTitle());
            //getOrderListAPI(context,"ALL");
        }
    }

    public void setStatus(String status){
        Status = status;
        if (view != null) {
            orders = orderDB.Orders(Status,view.getAppYN(), view.getPartyID());
            view.onOrderListChanged(orders);
        }
    }

    public ArrayList<mOrder> getOrders() {
        return orders;
    }

    public void getOrderListAPI(AppCompatActivity context, String sStatus){

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", view.getCompanyCode());
        request.put("iPaId", view.getPartyID() );
        request.put("sStatus",  sStatus);
        request.put("iLOGIN_PA_ID",  view.getUserID());

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("OrderGridMain1",request)
                .setTables(tables)
                /*.setShowProgess(orders.size() == 0 )*/
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        setStatus(Status);
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {

                        parser2(bundle);
                    }

                    @Override
                    public void onError(String s, String s1) {

                    }
                }));


        //End of call to service
    }


    private void parser2(Bundle result) throws JSONException {
        if (result!=null ) {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                //if (jsonArray1.length() > 0) {
                    //dbHelper.deleteOrder();
                    orderDB.delete();
                //}
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);

                    mOrder order = new mOrder()
                            .setPartyId(jsonObject2.getString("PA_ID"))
                            .setPartyName(jsonObject2.getString("PA_NAME"))
                            .setDocId(jsonObject2.getString("ID"))
                            .setDocNo(jsonObject2.getString("DOC_NO"))
                            .setDocDate(jsonObject2.getString("DOC_DATE"))
                            .setNetAmt(jsonObject2.getDouble("NET_AMT"))
                            .setPayMode(jsonObject2.getString("PYMT_MODE"))
                            .setStatus(jsonObject2.getString("STATUS"))
                            .setBillNo(jsonObject2.getString("BILL_NO"))
                            .setBillDate(jsonObject2.getString("BILL_DATE"))
                            .setBillAmt(jsonObject2.getString("BILL_AMT"))
                            .setGrDate(jsonObject2.getString("GR_DATE"))
                            .setGrNo(jsonObject2.getString("GR_NO"))
                            .setTransport(jsonObject2.getString("TRANSPORT"))
                            .setApproved(jsonObject2.getString("APPYN"))
                            .setBilledHO(jsonObject2.getInt("HOYN"))
                            .setAttachment(jsonObject2.getString("FILE_PATH"));


                    orderDB.insert(order);


                }


            }
    }


    public void getOrderDetail(AppCompatActivity context, final mOrder order){

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", view.getCompanyCode());
        request.put("iOrdId", order.getDocId() );


        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("OrderGridMainDet",request)
                .setTables(tables)
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {

                        if (view != null) {
                            view.showOderDetail(order);
                        }
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {

                        parserOrderDet(bundle,order);
                    }

                    @Override
                    public void onError(String s, String s1) {

                    }
                }));


        //End of call to service
    }

    public void DeleteOrder(AppCompatActivity context, final mOrder order){

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", view.getCompanyCode());
        request.put("iOrdId", order.getDocId() );


        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("OrderDelete",request)
                .setTables(tables)
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle result) throws Exception {
                        String table0 = result.getString("Tables0");
                        JSONArray jsonArray1 = new JSONArray(table0);
                        String msg = jsonArray1.getJSONObject(0).getString("STATUS");
                        if (msg.equalsIgnoreCase("OK")){
                            orderDB.delete("DOC_ID='"+order.getDocId()+"'");
                            setStatus(Status);
                        }else{
                            AppAlert.getInstance().getAlert(context,"Alert!!!",msg);
                        }



                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {


                    }

                    @Override
                    public void onError(String s, String s1) {

                    }
                }));


        //End of call to service
    }
    public void parserOrderDet(Bundle result, mOrder order) throws JSONException {
        if (result!=null ) {
            String table0 = result.getString("Tables0");
            JSONArray jsonArray1 = new JSONArray(table0);
            order.getItems().clear();


            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                ArrayList<mDiscount> discounts = new ArrayList<>();
                discounts.add(new mDiscount().setType(eDiscount.QI).setPercent(jsonObject2.getDouble("DISC_PERCENT")));
                discounts.add(new mDiscount().setType(eDiscount.VI).setPercent(jsonObject2.getDouble("DISC_PERCENT1")));
                discounts.add(new mDiscount().setType(eDiscount.P).setPercent(jsonObject2.getDouble("DISC_PERCENT2")));
                discounts.add(new mDiscount().setType(eDiscount.PG).setPercent(jsonObject2.getDouble("DISC_PERCENT3")));
                mItem item = new mItem()
                        .setId(jsonObject2.getString("ITEM_ID"))
                        .setName(jsonObject2.getString("ITEM_NAME"))
                        .setRate(jsonObject2.getDouble("RATE"))
                        .setMRP(jsonObject2.getString("RATE"))
                        .setQty(jsonObject2.getDouble("QTY"))
                        .setPack(jsonObject2.getString("PACK"))
                        .setAmt(jsonObject2.getDouble("AMOUNT"))
                        .setMiscDiscount(discounts)
                        .setGropuID(jsonObject2.getInt("ITEM_GROUP_ID"))
                        .setRemark(jsonObject2.getString("PREMARK"))
                        .setNoOfDiscountAlowed(Integer.parseInt( MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("ORD_DISC_TYPE","6")))
                        .setRemarkReqd(MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("SALE_ORDER_REMARKYN","N").equalsIgnoreCase("Y"))
                        .setMangerDiscount(new mDiscount().setType(eDiscount.M).setPercent(jsonObject2.getDouble("DISC_PERCENT4")) )
                        .setManualDiscount(new mDiscount().setPercent(jsonObject2.getDouble("DISC_PERCENT5")) );

                mTax GST = new mTax(eTax.getTax(jsonObject2.getInt("GST_TYPE")));
                GST.setSGST(jsonObject2.getDouble("TAX_PERCENT1"))
                        .setCGST(jsonObject2.getDouble("TAX_PERCENT"));

                item.setGST(GST).setQty(jsonObject2.getDouble("QTY"));

                mDeal deal = new mDeal();
               deal.setType(eDeal.get(jsonObject2.getString("DEAL_TYPE")))
                       .setId(jsonObject2.getInt("DEAL_ID"))
                        .setFreeQty(jsonObject2.getDouble("DEAL_QTY"))
                        .setQty(jsonObject2.getDouble("DEAL_ON"));
                item.setDeal(deal);

                item.setFreeQty(jsonObject2.getDouble("FREE_QTY"));

                //orderDetailDB.insert(view.getPartyID(),order.getDocId(),item);
                order.getItems().add(item);
        }

        }

    }

}
