package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import saleOrder.ViewModel.CBOViewModel;
import services.MyAPIService;
import utils.adapterutils.Expenses_Adapter;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;

public class vmOtherExpenses extends CBOViewModel<IOtherExpense> {
    private mExpense expense;
    private mOthExpense othExpense = new mOthExpense();
    private String newAttachment = "";
    private OthExpenseDB othExpenseDB = null;
    private CBO_DB_Helper cbohelp = null;
    @Override
    public void onUpdateView(Activity context, IOtherExpense view) {
        othExpenseDB = new OthExpenseDB(context);
        cbohelp = new CBO_DB_Helper(context);
        if (view != null){
            view.getReferencesById();
        }
    }

    public void setExpense(mExpense expense){
        this.expense = expense;
        /*if (view != null) {
            view.loadExpenseHead(expense.getExpHeads());
        }*/
    }

    public mExpense getExpense(){
        return expense;
    }

    public void setOthExpense(mOthExpense othExpense){
        this.othExpense = othExpense;
        if (view != null) {
            if (othExpense.getId() == 0){
                ArrayList<mExpHead> expHeads = new ArrayList<>();
                expHeads.add(othExpense.getExpHead());
                view.loadExpenseHead(expHeads);
            }else{
                view.loadExpenseHead(expense.getExpHeads());
            }



        }

    }

    public mOthExpense getOthExpense(){
        return othExpense;
    }

    public String getNewAttachment() {
        return newAttachment;
    }

    public void setNewAttachment(String newAttachment) {
        this.newAttachment = newAttachment;
    }

    public void setExpenseHead(mExpHead expenseHead){
        getOthExpense().setExpHead(expenseHead);
        if (view != null) {

            if (expenseHead.getId() == 3119) {
                view.setAmtHint("K.M.");
                view.setRemarkCaption("K.M. Remark.");
                view.setAmountCaption("K.M.");
            } else {
                view.setAmtHint("Amt.");
                view.setRemarkCaption("Exp Remark.");
                view.setAmountCaption("Amount");

            }

        }
    }


    public void other_expense_commit(Context context){
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",view.getCompanyCode());
        request.put("iDcrId", view.getDCRId());
        request.put("iExpHeadId", ""+getOthExpense().getExpHead().getId());
        request.put("iAmount", ""+ getOthExpense().getAmount());
        request.put("sRemark", getOthExpense().getRemark());
        request.put("sFileName", getOthExpense().getAttachment());

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyAPIService(context).execute(new ResponseBuilder("DCREXPCOMMITMOBILE_2",request)
                .setTables(tables)
                .setDescription("Please wait.....\n" +
                        "Processing your expense request....")
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        Custom_Variables_And_Method.getInstance().msgBox(context, " Exp. Added Sucessfully");
                        if (view!= null){
                            view.onSendResponse(getOthExpense());
                        }
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {
                        parser2(bundle);

                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                }));



    }


    private void parser2(Bundle result) throws JSONException {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject object = jsonArray1.getJSONObject(0);
                /*String value = object.getString("DCRID");
                String id= object.getString("ID");*/

                othExpenseDB.insert(getOthExpense().setId(object.getInt("ID"))
                .setTime(Custom_Variables_And_Method.getInstance().currentTime(MyCustumApplication.getInstance())));

                cbohelp.insert_Expense(""+getOthExpense().getExpHead().getId(),
                        getOthExpense().getExpHead().getName(),""+getOthExpense().getAmount(),
                        getOthExpense().getRemark(),getOthExpense().getAttachment(),
                        ""+getOthExpense().getId(),getOthExpense().getTime());

                MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("da_val",""+getExpense().getDA_Amt());



                    //customVariablesAndMethod.msgBox(context, " Exp. Added Sucessfully");


        }

}
