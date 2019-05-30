package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
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
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;

public class vmExpense  extends CBOViewModel<IExpense> {

    private CBO_DB_Helper cbohelp;
    private mExpense expense;
    private OthExpenseDB othExpenseDB;

    @Override
    public void onUpdateView(Activity context, IExpense view) {
        cbohelp = new CBO_DB_Helper(context);
        othExpenseDB = new OthExpenseDB(context);

        expense = new mExpense();


        if (view != null){
            view.getReferencesById();
            view.setTitle(view.getActivityTitle());
            getExpDLL(context);
        }
    }

    public void setExpense(mExpense expense){
        if (view != null){
            view.setRouteStatus(expense.getROUTE_CLASS());
            view.setDA(MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE( "da_val","0"));
            view.setDAType(expense.getDA_TYPE());
            view.setDistance(expense.getFARE());
            view.enableDA(othExpenseDB.get_DA_ACTION_exp_head().size() == 0);
            view.ActualDAReqd(expense.getACTUALDA_FAREYN().equalsIgnoreCase("Y"));
            view.ActualFareReqd(expense.getACTUALFAREYN().equalsIgnoreCase("Y"));
            view.OnOtherExpenseUpdated(getExpense().getOthExpenses());
            view.updateDAView();

        }
    }

    public mExpense getExpense(){
        return expense;
    }

    public void UpdateExpense(mOthExpense othExpense){
        //expense.getOthExpenses().add(othExpense);
        expense.setOthExpenses(othExpenseDB.get());
        view.enableDA(othExpenseDB.get_DA_ACTION_exp_head().size() == 0);
        view.OnOtherExpenseUpdated(getExpense().getOthExpenses());
        view.updateDAView();
    }

    private void getExpDLL(Activity context){
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",view.getCompanyCode());
        request.put("iPaId",view.getUserId());
        request.put("iDcrId", view.getDCRId());

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);
        tables.add(1);
        tables.add(2);

        new MyAPIService(context).execute(new ResponseBuilder("DCREXPDDLALLROUTE_MOBILE",request)
                .setTables(tables)
                .setDescription("Please wait.....\n" +
                        "Loading expenses for the planed DCR....")
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        setExpense(expense);
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {

                        parserExpDDl(bundle);
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                }));


        //End of call to service
    }


    public void parserExpDDl(Bundle result) throws JSONException {


                cbohelp.delete_EXP_Head();
                othExpenseDB.getExpHeadDB().delete();

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                expense.getExpHeads().clear();

                expense.getExpHeads().add(new mExpHead(0,"---Select Expense Head---"));
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                    mExpHead expHead = new mExpHead(jsonObject1.getInt("ID"),jsonObject1.getString("FIELD_NAME"))
                            .setDA_ACTION(jsonObject1.getInt("DA_ACTION"))
                            .setMANDATORY(jsonObject1.getInt("MANDATORY"))
                            .setEXP_TYPE(eExpanse.getExp(jsonObject1.getInt("EXP_TYPE")))
                            .setMAX_AMT(jsonObject1.getDouble("MAX_AMT"))
                            .setATTACHYN(jsonObject1.getInt("ATTACHYN"))
                            .setMasterValidate(jsonObject1.getInt("TAMST_VALIDATEYN"));

                    expense.getExpHeads().add(expHead);

                    othExpenseDB.getExpHeadDB().insert(expHead);

                    cbohelp.Insert_EXP_Head(jsonObject1.getString("FIELD_NAME"), jsonObject1.getString("ID"),
                            jsonObject1.getString("MANDATORY"), jsonObject1.getString("DA_ACTION"),
                            jsonObject1.getString("EXP_TYPE"), jsonObject1.getString("ATTACHYN"),
                            jsonObject1.getString("MAX_AMT"), jsonObject1.getString("TAMST_VALIDATEYN"));

                }


                String table2 = result.getString("Tables2");
                JSONArray jsonArray3 = new JSONArray(table2);
                for (int i = 0; i < jsonArray3.length(); i++) {
                    JSONObject object = jsonArray3.getJSONObject(i);


                    expense.setDA_TYPE(object.getString("DA_TYPE"))
                            .setFARE(object.getString("FARE"))
                            .setACTUALDA_FAREYN(object.getString("ACTUALDA_FAREYN"))
                            .setACTUALFAREYN(object.getString("ACTUALFAREYN"))
                            .setROUTE_CLASS(object.getString("ROUTE_CLASS"));
                }


                expense.setOthExpenses(othExpenseDB.get());




    }


    public void deleteExpense(Activity context,mOthExpense othExpense){
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",view.getCompanyCode());
        request.put("iPA_ID",view.getUserId());
        request.put("iDCR_ID", view.getDCRId());
        request.put("iID", ""+othExpense.getId());

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyAPIService(context).execute(new ResponseBuilder("DCREXPDELETEMOBILE_1",request)
                .setTables(tables)
                .setDescription("Please wait.....\n" +
                        "Deleting the expense from your planed DCR....")
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        Custom_Variables_And_Method.getInstance().msgBox(context, " Exp. Deleted Sucessfully");
                        UpdateExpense(othExpense);
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {

                        parser2(bundle,othExpense);
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                }));


        //End of call to service
    }


    private void parser2(Bundle result,mOthExpense othExpense) throws JSONException {

        String table0 = result.getString("Tables0");
        JSONArray jsonArray1 = new JSONArray(table0);

        JSONObject object = jsonArray1.getJSONObject(0);
                /*String value = object.getString("DCRID");
                String id= object.getString("ID");*/


                othExpenseDB.delete(othExpense);


        cbohelp.delete_Expense_withID(""+othExpense.getId());





    }


    public void expense_commit(Activity context){

        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE( "da_val",""+getExpense().getDA_Amt());

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",view.getCompanyCode());
        request.put("iDcrId", view.getDCRId());
        request.put("sDaType", "");
        request.put("iDistanceId", "");
        request.put("iDA_VALUE", MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("da_val","0"));

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyAPIService(context).execute(new ResponseBuilder("DCR_COMMITEXP_2",request)
                .setTables(tables)
                .setDescription("Please wait.....\n" +
                        "Deleting the expense from your planed DCR....")
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        Custom_Variables_And_Method.getInstance().msgBox(context, " Expense Saved Sucessfully...");
                        context.finish();
                        Custom_Variables_And_Method.getInstance().setDataInTo_FMCG_PREFRENCE(context,"ACTUALFARE",""+getExpense().getTA_Amt());


                        if (!getExpense().getACTUALFAREYN().equalsIgnoreCase("Y")){
                            Custom_Variables_And_Method.getInstance().setDataInTo_FMCG_PREFRENCE(context,"ACTUALFARE","0");
                        }
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {

                        parser3(bundle);
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                }));


        //End of call to service

    }

    private void parser3(Bundle result) throws JSONException {
        String table0 = result.getString("Tables0");
        JSONArray jsonArray1 = new JSONArray(table0);

        JSONObject object = jsonArray1.getJSONObject(0);
        String value2 = object.getString("DCR_ID");

        //customVariablesAndMethod.msgBox(context,"Expense Saved Sucessfully...");



    }


    public void expense_commit_attachment(Activity context){
        //Start of call to service

        mOthExpense othExpense = new mOthExpense()
                .setExpHead(new mExpHead(-1,""))
                .setRemark("Actual Fare")
                .setAttachment(getExpense().getAttachment())
                .setAmount(getExpense().getTA_Amt());

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",view.getCompanyCode());
        request.put("iDcrId", view.getDCRId());
        request.put("iExpHeadId", ""+othExpense.getExpHead().getId());
        request.put("iAmount", ""+ othExpense.getAmount());
        request.put("sRemark", othExpense.getRemark());
        request.put("sFileName", othExpense.getAttachment());

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyAPIService(context).execute(new ResponseBuilder("DCREXPCOMMITMOBILE_2",request)
                .setTables(tables)
                .setDescription("Please wait.....\n" +
                        "Processing your expense request....")
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        expense_commit(context);
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {
                        expense_attachment_parser2(bundle,othExpense);

                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                }));



    }


    private void expense_attachment_parser2(Bundle result,mOthExpense othExpense) throws JSONException {

        String table0 = result.getString("Tables0");
        JSONArray jsonArray1 = new JSONArray(table0);

        JSONObject object = jsonArray1.getJSONObject(0);
                /*String value = object.getString("DCRID");
                String id= object.getString("ID");*/

        othExpenseDB.insert(othExpense.setId(object.getInt("ID"))
                .setTime(Custom_Variables_And_Method.getInstance().currentTime(MyCustumApplication.getInstance())));

        cbohelp.insert_Expense(""+othExpense.getExpHead().getId(),
                othExpense.getExpHead().getName(),""+othExpense.getAmount(),
                othExpense.getRemark(),othExpense.getAttachment(),
                ""+othExpense.getId(),othExpense.getTime());

        //MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("da_val",""+getExpense().getDA_Amt());



        //customVariablesAndMethod.msgBox(context, " Exp. Added Sucessfully");


    }
}
