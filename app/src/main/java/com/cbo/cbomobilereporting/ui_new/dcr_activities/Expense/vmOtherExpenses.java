package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import saleOrder.ViewModel.CBOViewModel;
import services.MyAPIService;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;

public class vmOtherExpenses extends CBOViewModel<IOtherExpense> {
    private mExpense expense;
    private eExpense expense_type = eExpense.None;
    private mOthExpense othExpense = new mOthExpense();
    private String newAttachment = "";
    private OthExpenseDB othExpenseDB = null;
    private ExpHeadDB expHeadDB = null;
    private CBO_DB_Helper cbohelp = null;
    @Override
    public void onUpdateView(AppCompatActivity context, IOtherExpense view) {
        othExpenseDB = new OthExpenseDB(context);
        expHeadDB = new ExpHeadDB(context);
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

    public eExpense getExpense_type() {
        return expense_type;
    }

    public void setExpense_type(eExpense expense_type) {
        this.expense_type = expense_type;
        if (view != null) {
            view.setTitle();
        }
    }

    public void setOthExpense(mOthExpense othExpense){
        this.othExpense = othExpense;
        ArrayList<mExpHead> expHeads = new ArrayList<>();
        if (view != null) {
            if (othExpense.getId() != 0){

                expHeads.add(othExpense.getExpHead());

            }else{
                expHeads = expHeadDB.get_NotAdded(getExpense_type());
                expHeads.add(0,new mExpHead(0,"---Select Exp. Head---"));
            }
            expense.setExpHeads(expHeads);
            view.loadExpenseHead(expense.getExpHeads());


        }

    }

    public mOthExpense getOthExpense(){
        return othExpense;
    }

    public String getNewAttachment() {
        return newAttachment;
    }

    public ArrayList<String> getNewAttachmentArr() {
        return this.getNewAttachment().isEmpty() ? new ArrayList() : new ArrayList(Arrays.asList(this.newAttachment.split("\\|\\^")));
    }

    public void setNewAttachment(String newAttachment) {
        this.newAttachment = newAttachment;
    }

    public void setAttachment(ArrayList<String> attachment) {
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for(String file : attachment) {
            if (count != 0) {
                sb.append("|^");
            }
            ++count;
            sb.append(file);
        }
        this.newAttachment = sb.toString();
    }

    public void setExpenseHead(Context context,mExpHead expenseHead){
        setNewAttachment("");
        getOthExpense().setExpHead(expenseHead);
        if (view != null) {

            Boolean allreadyAdded = false;
            if (othExpenseDB.IsExpHeadGroupAdded(expenseHead.getHEADTYPE_GROUP()) &&
                getExpense().getExpHeads().size() != 1) {
                view.loadExpenseHead(getExpense().getExpHeads());
                allreadyAdded = true;

            }
            view.KmLayoutRequired(getOthExpense().getExpHead().getKMYN().equalsIgnoreCase("1"));
            if (!allreadyAdded) {
                if (expenseHead.getId() == 3119) {
                    view.setAmtHint("K.M.");
                    view.setRemarkCaption("K.M. Remark.");
                    view.setAmountCaption("K.M.");
                } else {
                    view.setAmtHint("Amt.");
                    view.setRemarkCaption("Exp Remark.");
                    view.setAmountCaption("Amount");
                }

            }else{
                ArrayList<mExpHead> heads = expHeadDB.get(expenseHead.getHEADTYPE_GROUP());
                StringBuilder sb = new StringBuilder();
                for (mExpHead head : heads){
                   // if (!expenseHead.getName().equalsIgnoreCase(head.getName())){
                        sb.append(head.getName()).append("\n");
                    //}
                }
                AppAlert.getInstance().Alert(context, "Alert!!!",
                        "You can't select \n"+
                                expenseHead.getName()+
                                "\nbecause you can select any one head from the given heads\n"+
                                sb.toString(),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
            }

            view.setKm(getOthExpense().getKm());
            view.setAmount(getOthExpense().getAmount());
            view.setRemark(getOthExpense().getRemark());
            view.setRate(expenseHead.getRATE());
            view.setAttachment(getOthExpense().getAttachmentArr());
        }
    }


    public void other_expense_commit(AppCompatActivity context){
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",view.getCompanyCode());
        request.put("iDcrId", view.getDCRId());
        request.put("iExpHeadId", ""+getOthExpense().getExpHead().getId());
        request.put("iAmount", ""+ getOthExpense().getAmount());
        request.put("sRemark", getOthExpense().getRemark());
        request.put("sFileName", getOthExpense().getAttachmentName());
        request.put("TA_KM",""+ getOthExpense().getKm());
        request.put("TA_DA",""+ getExpense_type().ordinal());
        request.put("ISSUPPORTUSER", MyCustumApplication.getInstance().getUser().getLoggedInAsSupport()?"Y":"N");

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyAPIService(context).execute(new ResponseBuilder("DCREXPCOMMITMOBILE_3",request)
                .setTables(tables)
                .setDescription("Please wait.....\n" +
                        "Processing your expense request....")
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        String table0 = bundle.getString("Tables0");
                        JSONArray jsonArray1 = new JSONArray(table0);
                        JSONObject object = jsonArray1.getJSONObject(0);
                        if (object.getString("INVALIDMSG").isEmpty()) {

                            Custom_Variables_And_Method.getInstance().msgBox(context, (getExpense_type() == eExpense.None ? "Exp." : getExpense_type().name()) + "  Added Successfully");
                            if (view != null) {
                                view.onSendResponse(getOthExpense());
                            }
                        }else{
                            AppAlert.getInstance().Alert(context, "Alert!!", object.getString("INVALIDMSG"),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            context.onBackPressed();
                                        }
                                    });
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

                if (object.getString("INVALIDMSG").isEmpty()) {
                    othExpenseDB.insert(getOthExpense().setId(object.getInt("ID"))
                            .setTime(Custom_Variables_And_Method.getInstance().currentTime(MyCustumApplication.getInstance())));

                    cbohelp.insert_Expense("" + getOthExpense().getExpHead().getId(),
                            getOthExpense().getExpHead().getName(), "" + getOthExpense().getAmount(),
                            getOthExpense().getRemark(), getOthExpense().getAttachment(),
                            "" + getOthExpense().getId(), getOthExpense().getTime());

                    MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("da_val", "" + getExpense().getDA_Amt());

                }

                    //customVariablesAndMethod.msgBox(context, " Exp. Added Sucessfully");


        }

}
