package saleOrder.ViewModel;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import saleOrder.Model.mParty;
import saleOrder.MyOrderAPIService;
import saleOrder.Views.iClient;

/**
 * Created by cboios on 06/03/19.
 */

public class vmClient extends CBOViewModel<iClient> {
    private ArrayList<mParty> parties = new ArrayList<>();

    public ArrayList<mParty> getParties() {
        return parties;
    }

    public void setParties(ArrayList<mParty> parties) {
        this.parties = parties;
    }

    @Override
    public void onUpdateView(AppCompatActivity context, iClient view) {
        view.getReferencesById();
        view.setTile();

        getPartiesAPI(context);
    }


    private void getPartiesAPI(AppCompatActivity context){
        getPartiesAPI(context,true);
    }
    public void getPartiesAPI(final AppCompatActivity context, Boolean SyncYN){

//        items = itemDB.items(getFilterQry(),getKit_Type());
//
//        for (mItem item : items) {
//            mItem orderItem = GetOrderItemWhere(item);
//            if (orderItem != null) {
//                item.setQty(orderItem.getQty())
//                        .setAmt(orderItem.getAmt());
//
//            }
//        }
//
//        if (view != null){
//
//            view.onItemsChanged(items);
//        }

//        New_Order_Multiple_Adaptor order_item_list_adaptor = new New_Order_Multiple_Adaptor(context, adaptor_data_OrderItem);
//        listView.setAdapter(order_item_list_adaptor);

        if (SyncYN) {
            //Start of call to service

            HashMap<String,String> request=new HashMap<>();
            request.put("sCompanyFolder", view.getCompanyCode());
            request.put("iPA_ID", view.getUserId() );

            ArrayList<Integer> tables=new ArrayList<>();
            tables.add(0);

            new MyOrderAPIService(context).execute(new ResponseBuilder("LedgerGridParty",request)
                    .setTables(tables)
//                    .setShowProgess(items.size() == 0 )
                    .setResponse(new CBOServices.APIResponse() {
                        @Override
                        public void onComplete(Bundle bundle) throws Exception {
                            if (view != null) {
                                view.onPartyListUpdated(getParties());
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
            JSONArray jsonArray1 = new JSONArray(table0);

//            if (jsonArray1.length() > 0) {
//                itemDB.delete();
//            }

            getParties().clear();

            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                mParty party = new mParty()
                        .setId(jsonObject2.getString("PA_ID"))
                        .setName(jsonObject2.getString("PA_NAME"))
                        .setBalance(jsonObject2.getString("BALANCE"))
                        .setHeadQtr(jsonObject2.getString("HEAD_QTR"))
                        .setDivision(jsonObject2.getString("DIVISION_NAME"));
                //itemDB.insert(item);
                getParties().add(party);
            }

        }

    }

}
