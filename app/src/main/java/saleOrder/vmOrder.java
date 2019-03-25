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

import cbomobilereporting.cbo.com.cboorder.DBHelper.OrderDB;
import cbomobilereporting.cbo.com.cboorder.DBHelper.OrderDetailDB;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.View.iOrder;

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
    public void onUpdateView(Activity context, iOrder view) {
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
            orders = orderDB.Orders(Status, view.getPartyID());
            view.onOrderListChanged(orders);
        }
    }

    public ArrayList<mOrder> getOrders() {
        return orders;
    }

    public void getOrderListAPI(Activity context, String sStatus){

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", view.getCompanyCode());
        request.put("iPaId", view.getPartyID() );
        request.put("sStatus",  sStatus);

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("OrderGridMain",request)
                .setTables(tables)
                .setShowProgess(orders.size() == 0 )
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
                if (jsonArray1.length() > 0) {
                    //dbHelper.deleteOrder();
                    orderDB.delete();
                }
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);

                    mOrder order = new mOrder()
                            .setPartyId(view.getPartyID())
                            .setDocId(jsonObject2.getString("ID"))
                            .setDocNo(jsonObject2.getString("DOC_NO"))
                            .setDocDate(jsonObject2.getString("DOC_DATE"))
                            .setNetAmt(jsonObject2.getString("NET_AMT"))
                            .setPayMode(jsonObject2.getString("PYMT_MODE"))
                            .setStatus(jsonObject2.getString("STATUS"))
                            .setBillNo(jsonObject2.getString("BILL_NO"))
                            .setBillDate(jsonObject2.getString("BILL_DATE"))
                            .setBillAmt(jsonObject2.getString("BILL_AMT"))
                            .setGrDate(jsonObject2.getString("GR_DATE"))
                            .setGrNo(jsonObject2.getString("GR_NO"))
                            .setTransport(jsonObject2.getString("TRANSPORT"));

                    orderDB.insert(order);


                }


            }
    }


    public void getOrderDetail(Activity context, final mOrder order){

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

    public void parserOrderDet(Bundle result, mOrder order) throws JSONException {
        if (result!=null ) {
            String table0 = result.getString("Tables0");
            JSONArray jsonArray1 = new JSONArray(table0);
            order.getItems().clear();

            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                mItem item = new mItem()
                        .setId(jsonObject2.getString("ITEM_ID"))
                        .setName(jsonObject2.getString("ITEM_NAME"))
                        .setRate(jsonObject2.getString("RATE"))
                        .setMRP(jsonObject2.getString("RATE"))
                        .setQty(jsonObject2.getString("QTY"))
                        .setAmt(jsonObject2.getString("AMOUNT"));

                //orderDetailDB.insert(view.getPartyID(),order.getDocId(),item);
                order.getItems().add(item);
            }

        }

    }

}
