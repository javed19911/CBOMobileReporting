package com.cbo.cbomobilereporting.ui_new.dcr_activities.Recipt;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import saleOrder.ViewModel.CBOViewModel;
import services.MyAPIService;
import utils_new.AppAlert;
import utils_new.CustomDatePicker;

public class vmRecpiet extends CBOViewModel<IRecipt> {
    private ArrayList<mRecipt>reciptArrayList= new ArrayList<mRecipt>();
    private mRecipt recipt;



    @Override
    public void onUpdateView(Activity context, IRecipt view) {
        if(view!=null){
            view.getReferencesById();
            view.setActivityTitle(view.getActivityTitle());
        }
        GetALLRecpietList(context);
    }




    public mRecipt getRecipt() {
        return recipt;
    }

    public void setRecipt(mRecipt mRecipt) {
        this.recipt = mRecipt;
    }




    public Double getTotalRecieptAmt(){
        Double tot =0D;

        for (mRecipt recipt : reciptArrayList){
            tot += recipt.getAmount();
        }

        return tot;
    }



    public  void GetALLRecpietList(Context context){


        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPA_ID", MyCustumApplication.getInstance().getUser().getID());



        new MyAPIService(context)

                .execute(new ResponseBuilder("RCPT_GRID_MOBILE", request)
                        .setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) {
                                if (view != null) {
                                    view.onRecieptlistchanged(reciptArrayList);
                                    view.OnTotalUpdated(getTotalRecieptAmt());
                                }

                            }

                            @Override
                            public void onResponse(Bundle response) throws Exception {
                                parser2(response);
                            }

                            @Override
                            public void onError(String s, String s1) {
                                AppAlert.getInstance().getAlert(context,s, s1);

                            }
                        })
                );
    }


    //added Party id as PAID in mReciept model
    private void parser2(Bundle result) throws Exception {
        {
            String table0 = result.getString("Tables0");
            JSONArray row = new JSONArray(table0);

            reciptArrayList.clear();
            for (int i = 0; i < row.length(); i++) {
                JSONObject c = row.getJSONObject(i);
                mRecipt recieptmodel=new mRecipt();
                recieptmodel.setId(c.getInt("ID"));
                mParty party = new mParty(c.getString("PA_ID"),c.getString("PA_NAME"));
                recieptmodel.setParty(party);
                recieptmodel.setRemark(c.getString("REMARK"));
                recieptmodel.setAmount(c.getDouble("AMOUNT"));
                recieptmodel.setDoc_Date(CustomDatePicker.getDate( c.getString("DOC_DATE"),CustomDatePicker.ShowFormatOld));
                recieptmodel.setReciept_no(c.getString("DOC_NO"));

                reciptArrayList.add(recieptmodel);


            }


        }


    }



    public  void  DeletePartyData(Context context,mRecipt recipt){
        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("ID",""+recipt.getId());

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        new MyAPIService(context)
                .execute(new ResponseBuilder("RCPT_DELETE_MOBILE", request)
                        .setTables(tables)
                        .setDescription("Please Wait..")
                        .setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) throws JSONException {


                                if (view != null)
                                    view.onRecpieptDeleted(context);




                            }

                            @Override
                            public void onResponse(Bundle response) throws JSONException {
                                parser1(response);
                            }

                            @Override
                            public void onError(String s, String s1) {
                                AppAlert.getInstance().getAlert(context,s, s1);

                            }


                        })
                );



    }



    public void parser1(Bundle result) throws JSONException {

        String table0 = result.getString("Tables0");
        JSONArray row = null;
            row = new JSONArray(table0);

            for (int i = 0; i < row.length(); i++) {
                JSONObject c = row.getJSONObject(i);




            }




    }





}
