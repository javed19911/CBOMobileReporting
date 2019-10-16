package com.cbo.cbomobilereporting.ui_new.dcr_activities.Recipt;

/*public class vmAddRecipt {
}*/

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import saleOrder.ViewModel.CBOViewModel;
import services.MyAPIService;
import utils.model.DropDownModel;
import utils_new.AppAlert;
import utils_new.CustomDatePicker;

public class vmAddRecipt  extends CBOViewModel<IReciptEntry> {


  ArrayList<DropDownModel> Partylist = new ArrayList<DropDownModel>();

  Context context;
  IReciptEntry iReciptEntry;
  mRecipt mRecipt = new mRecipt();

  private String SelectedDtte;
  private String  ID;


    @Override
    public void onUpdateView(AppCompatActivity context, IReciptEntry view) {

        if (view != null){
            view.getReferencesById();
            //view.SetRecpientBy();
            view.setRecpientBy( MyCustumApplication.getInstance().getUser().getName());//chnage here

        }

    }

    public void setRecipt(mRecipt recieptmodel) {
        this.mRecipt = recieptmodel;

        if (view != null && mRecipt.getId() != 0) {
            view.setReceiptTitle("Edit Receipt");
            view.setRecieptNo(mRecipt.getReciept_no());
            view.setPartyname(mRecipt.getParty().getName());

            view.setDate(CustomDatePicker.formatDate(mRecipt.getDoc_Date(),CustomDatePicker.ShowFormat));

            view.setAmount(mRecipt.getAmount());
            view.setRemark(mRecipt.getRemark());
            view.setRecpientBy(MyCustumApplication.getInstance().getUser().getName());

        } else if (view != null) {
            view.setReceiptTitle("Add Receipt");
            getDDLResult(context);
        }
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public vmAddRecipt() {


  }




    public String getSelecteddate() {
        return SelectedDtte;
    }

    public void setSelecteddate(Date todate) {
        SelectedDtte = CustomDatePicker.formatDate(todate, CustomDatePicker.CommitFormat);
        //if (iReciptEntry != null)
            //iReciptEntry.onDateChanged(todate);
    }



  public void setListener(Context context, IReciptEntry IReciptEntry) {
    this.context = context;
    this.iReciptEntry = IReciptEntry;
  }

  public mParty getParty() {

    return getRecipt().getParty();
  }

  public void setParty(mParty mParty) {
    getRecipt().setParty(mParty);
  }

  public mRecipt getRecipt() {
    return mRecipt;
  }


    public void RecieptCommit(Context context) {


        //hashmap getting value chnqaged
        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iID",Integer.toString(getRecipt().getId()));
        request.put("iDOC_NO", getRecipt().getReciept_no()); // DDL//Always get
        request.put("sDOC_DATE", CustomDatePicker.formatDate( getRecipt().getDoc_Date(),CustomDatePicker.CommitFormat));
        request.put("iAMOUNT", ""+getRecipt().getAmount());
        request.put("sREMARK", getRecipt().getRemark());
        request.put("iAPA_ID", MyCustumApplication.getInstance().getUser().getID());
        request.put("iPaId", getRecipt().getParty().getID()); // DDL



        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        new MyAPIService(context)
                .execute(
                        new ResponseBuilder("RCPT_COMMIT_MOBILE", request)
                                .setTables(tables)
                                .setDescription("Please Wait..")
                                .setResponse(
                                        new CBOServices.APIResponse() {
                                            @Override
                                            public void onComplete(Bundle message) throws JSONException {

                                                iReciptEntry.onRecipetSubmited();//Call on SendResponse


                                            }

                                            @Override
                                            public void onResponse(Bundle response) throws JSONException {
                                                paser5(response);
                                            }

                                            @Override
                                            public void onError(String s, String s1) {
                                                AppAlert.getInstance().getAlert(context,s,s1);                                          }
                                        }));

    }



    public void getDDLResult(Context context) {
        HashMap<String, String> request = new HashMap<>();

        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());
        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        tables.add(1);

        new MyAPIService(context)
                .execute(
                        new ResponseBuilder("RCPT_DDL_MOBILE", request)
                                .setMultiTable(true)
                                .setTables(tables)
                                .setResponse(
                                        new CBOServices.APIResponse() {
                                            @Override
                                            public void onComplete(Bundle bundle) {
                                                if (iReciptEntry != null) {
                                                    iReciptEntry.onParylistUpdated(Partylist);
                                                    iReciptEntry.setRecieptNo(mRecipt.getReciept_no());
                                                    view.setDate(CustomDatePicker.formatDate(mRecipt.getDoc_Date(),CustomDatePicker.ShowFormat));
                                                }

                                            }

                                            @Override
                                            public void onResponse(Bundle bundle) throws Exception {
                                                parser4(bundle);
                                            }

                                            @Override
                                            public void onError(String s, String s1) {
                                                AppAlert.getInstance().getAlert(context,s,s1);                                          }
                                        }));

    }



  private void parser4(Bundle response) throws Exception {

    String table0 = response.getString("Tables0");
    JSONArray jsonArray = null;
      jsonArray = new JSONArray(table0);
      Partylist.clear();
      //  Partylist.add(new DropDownModel("--Select--", "0"));
      for (int i = 0; i < jsonArray.length(); i++) {

        JSONObject c = jsonArray.getJSONObject(i);
        Partylist.add(new DropDownModel(c.getString("PA_NAME"), c.getString("PA_ID")));
      }

      String table1 = response.getString("Tables1");
      JSONArray row2 = new JSONArray(table1);

      for (int i = 0; i < row2.length(); i++) {
        JSONObject c = row2.getJSONObject(i);

          getRecipt().setReciept_no(c.getString("DOC_NO"));
      }
      getRecipt().setDoc_Date(CustomDatePicker.getDate( CustomDatePicker.currentDate(),CustomDatePicker.CommitFormat));



  }

  

  private void paser5(Bundle response) {

    String table0 = response.getString("Tables0");
    JSONArray row = null;
    try {
      row = new JSONArray(table0);

      for (int i = 0; i < row.length(); i++) {
        JSONObject c = row.getJSONObject(i);


      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }


}
