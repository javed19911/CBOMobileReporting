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

import saleOrder.Model.mOverDue;
import saleOrder.MyOrderAPIService;
import saleOrder.Views.iPartyOverDue;

public class vmPartyOverDue extends CBOViewModel<iPartyOverDue>{


    ArrayList<mOverDue> overDues = new ArrayList<>();

    @Override
    public void onUpdateView(AppCompatActivity context, iPartyOverDue view) {
        view.getReferencesById();
        view.setTitle("");
    }

    public ArrayList<mOverDue> getOverDues(AppCompatActivity context){

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", view.getCompanyCode());
        request.put("iPA_ID", view.getParty().getId() );
        request.put("iLOGIN_PA_ID",  view.getUserID());

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("PartyOverDue",request)
                .setTables(tables)
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        //setStatus(Status);
                        if (overDues.size()> 0) {
                            view.onOverDueListChanged(overDues);
                        }else{
                            view.onSendResponse();
                        }
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {

                        parserOverDue(bundle);
                    }

                    @Override
                    public void onError(String s, String s1) {

                    }
                }));


        //End of call to service
        return overDues;
    }


    private void parserOverDue(Bundle result) throws JSONException {
        if (result!=null ) {

            overDues.clear();

            String table0 = result.getString("Tables0");
            JSONArray jsonArray1 = new JSONArray(table0);

            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);

                mOverDue overDue = new mOverDue()
                        .setBAL_AMT(jsonObject2.getDouble("BAL_AMT"))
                        .setDOC_DATE(jsonObject2.getString("DOC_DATE"))
                        .setDOC_NO(jsonObject2.getString("DOC_NO"))
                        .setDUE_DATE(jsonObject2.getString("DUE_DATE_ACTUAL"));


                overDues.add(overDue);


            }


        }
    }

}
