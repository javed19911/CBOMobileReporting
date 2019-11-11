package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.databaseHelper.Call.Local.PobMailDb;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import saleOrder.ViewModel.CBOViewModel;
import services.MyAPIService;
import utils_new.AppAlert;

public class vmChemist extends CBOViewModel<iChemist> {

    ArrayList<mStockist> stockists = new ArrayList<>();
    ArrayList<mItem> items = new ArrayList<>();
    private String Status = "P";
    private ArrayList<mChemist> chemests = new ArrayList<>();

    public vmChemist() {

    }

    @Override
    public void onUpdateView(AppCompatActivity context, iChemist view) {
        if (view != null) {

            view.getReferencesById();
            view.setTile(view.getActivityTitle());
        }
    }

    public void setStatus(String status) {
        Status = status;

    }

    public ArrayList<mChemist> getChemist() {
        return chemests;
    }

    public void setData(ArrayList<mChemist> chemests) {
        this.chemests = chemests;
    }


    public void commitStokist(AppCompatActivity context,
                              String idcrId,
                              StringBuilder chemestIds,
                              StringBuilder stockistIds,
                              StringBuilder rateStr,
                              StringBuilder amountStr,
                              StringBuilder QuantityStr,
                              StringBuilder ItemIdStr) {

        //Start of call to service
        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", "demotest");
        request.put("iLOGIN_PA_ID", "11189");
        request.put("iDCR_ID", idcrId);
        request.put("sCHEM_ID", "" + chemestIds);
        request.put("sSTK_ID", "" + stockistIds);
        request.put("sITEM_ID", "" + ItemIdStr);
        request.put("sQTY", "" + QuantityStr);
        request.put("sRATE", "" + rateStr);
        request.put("sAMT", "" + amountStr);
        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        new MyAPIService(context).execute(new ResponseBuilder("DCRPOBMAIL_COMMIT", request)
                .setTables(tables)
                /*.setShowProgess(orders.size() == 0 )*/
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        String table0 = bundle.getString("Tables0");
                        JSONArray jsonArray1 = new JSONArray(table0);

                        JSONObject object = jsonArray1.getJSONObject(0);
                        String value2 = object.getString("STATUS");

                        if (value2.equalsIgnoreCase("Y")) {
                            AppAlert.getInstance().getAlert(context, "Alert", "Data Submit Successfully");
                        }else {
                            AppAlert.getInstance().getAlert(context, "Alert", value2);
                        }

                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {
                        parseJson(bundle, context);
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context, s, s1);

                    }
                }));


    }

    private void parseJson(Bundle result, AppCompatActivity context) throws Exception {

        String table0 = result.getString("Tables0");
        JSONArray jsonArray1 = new JSONArray(table0);

        JSONObject object = jsonArray1.getJSONObject(0);
        String value2 = object.getString("STATUS");

        if (value2.equalsIgnoreCase("Y")) {

            for (mChemist chemest : chemests) {
                if (chemest.getSelectedStokist() != null) {
                    PobMailDb.getInstance(context).insert(chemest);
                }
            }
        }
    }


    public void setChemest(ArrayList<mChemist> chemestList) {
        this.chemests = chemestList;
    }
}