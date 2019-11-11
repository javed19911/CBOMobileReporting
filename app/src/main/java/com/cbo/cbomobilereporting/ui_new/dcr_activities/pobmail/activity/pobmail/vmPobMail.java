package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.pobmail;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.databaseHelper.Call.Local.PobMailDb;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.ChemistFragment;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mChemist;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mItem;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mStockist;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import saleOrder.ViewModel.CBOViewModel;
import services.MyAPIService;

public class vmPobMail extends CBOViewModel<iPobMail> {

    ArrayList<mStockist> stockists = new ArrayList<>();
    ArrayList<mItem> items = new ArrayList<>();
    private ArrayList<mChemist> chemests = null;
    private String chemIds="";
    private IPobStoKist ipobMail;

    @Override
    public void onUpdateView(AppCompatActivity context, iPobMail view) {
        if (view != null) {
            view.getReferencesById();
            view.getActivityTitle();
            view.setTile(view.getActivityTitle());

        }

    }

    public void getChemestList(AppCompatActivity context, ChemistFragment chemistFragment, String filterType) {

        if (chemests != null) {
            getChemistFor(chemistFragment,filterType);
        } else {

            //Start of call to service
            HashMap<String, String> request = new HashMap<>();
            request.put("sCompanyFolder", "demotest");
            request.put("iLOGIN_PA_ID", "11189");
            request.put("iDCR_ID", "");
            request.put("sCHEM_ID_IN", getChemIds(context));
            ArrayList<Integer> tables = new ArrayList<>();
            tables.add(0);

            new MyAPIService(context).execute(new ResponseBuilder("DCRPOBMAIL_DDL", request)
                    .setTables(tables)
                    /*.setShowProgess(orders.size() == 0 )*/
                    .setResponse(new CBOServices.APIResponse() {
                        @Override
                        public void onComplete(Bundle bundle) throws Exception {

                            getChemistFor(chemistFragment,filterType);
                        }

                        @Override
                        public void onResponse(Bundle bundle) throws Exception {
                            if (bundle != null) {
                                String table0 = bundle.getString("Tables0");
                                JSONArray jsonArray1 = new JSONArray(table0);
                                for (int i = 0; i < chemests.size(); i++) {
                                    setStockist(chemests.get(i), jsonArray1);
                                }
                            }

//                            view.onChemistListUpdated(chemistFragment,chemests,filterType);
                        }

                        @Override
                        public void onError(String s, String s1) {


                        }
                    }));
        }

//
    }


    private void parser2(Bundle result, Context context) throws JSONException {


    }

    private void setStockist(mChemist chemest, JSONArray jsonArray1) throws JSONException {
        chemest.getStockists().clear();
        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
            if (chemest.getID() == Integer.parseInt(jsonObject2.getString("CHEM_ID"))) {
                mStockist stockist = new mStockist();
                stockist.setID(Integer.parseInt(jsonObject2.getString("STK_ID")));
                stockist.setEMAIL(jsonObject2.getString("EMAIL"));
                stockist.setNAME(jsonObject2.getString("STK_NAME"));
                chemest.getStockists().add(stockist);
            }
        }

    }

    public String getChemIds(Context context) {

        if (chemests == null) {
            getChemestListFromDb(context);
        }
        for (int i = 0; i < chemests.size(); i++) {
            if (chemIds.equals("")) {
                chemIds = "" + chemests.get(i).getID();
            } else {
                chemIds = chemIds + "," + chemests.get(i).getID();
            }
        }

        return chemIds;
    }

    private void getChemistFor(ChemistFragment chemistFragment, String filterType){
        view.onChemistListUpdated(chemistFragment,getChemestData(chemests,filterType),filterType);
       /* if(filterType.equalsIgnoreCase("P")){
            view.onChemistListUpdated(chemistFragment,getChemestData(),"P");
        }else if (filterType.equalsIgnoreCase("C")){
            view.onChemistListUpdated(chemistFragment,getChemestData(chemests,filterType),"C");
        }else {
            view.onChemistListUpdated(chemistFragment,chemests,"All");
        }*/
    }

    private ArrayList<mChemist> getChemestData( ArrayList<mChemist> chemestsList,String filterType) {
        if (filterType.equalsIgnoreCase("ALL"))
            return chemestsList;
        int is_send = filterType.equalsIgnoreCase("C")?1:0;

        ArrayList<mChemist> chemests1=new ArrayList<>();
        for (int i=0;i<chemestsList.size();i++){

            if(chemestsList.get(i).getIS_SEND()==is_send){
                chemests1.add(chemestsList.get(i));
            }
        }
        return chemests1;
    }

    private ArrayList<mChemist> getChemestListFromDb(Context context) {
        /*chemests = new ArrayList<>();
        chemests.clear();
        mChemist che = new mChemist();
        che.setID(4);
        che.setAmount("1200");
        che.setRate("200");
        che.setQTY("2");
        che.setItemId("1");
        che.setNAME("chem4");
        chemests.add(che);
        mChemist che1 = new mChemist();
        che1.setID(5);
        che1.setAmount("1200");
        che1.setRate("200");
        che1.setQTY("2");
        che1.setItemId("1");
        che1.setNAME("chem4");
        che1.setNAME("chem5");
        che1.setIS_SEND(1);
        chemests.add(che1);*/
        chemests =  PobMailDb.getInstance(context).getChemist();
        return chemests;
    }

    public void onUpdateChemist(ChemistFragment chemistFragment, String filterType, mChemist selectStockist) {

        for (int i=0;i<chemests.size();i++){
            if (selectStockist.getID()==chemests.get(i).getID()) {
                chemests.set(i, selectStockist);
            }
        }
        getChemistFor(chemistFragment,filterType);


    }
}
