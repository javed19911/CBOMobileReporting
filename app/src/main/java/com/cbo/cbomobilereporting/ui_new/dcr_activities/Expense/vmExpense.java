package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.NonWorking_DCR;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Enum.CallType;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import saleOrder.ViewModel.CBOViewModel;
import services.CboServices;
import services.MyAPIService;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;

public class vmExpense  extends CBOViewModel<IExpense> {

    private CBO_DB_Helper cbohelp;
    private mExpense expense;
    private OthExpenseDB othExpenseDB;
    private Boolean IsFinalSubmit = false;
    private String FinalRemark ="";
    private Custom_Variables_And_Method customVariablesAndMethod;

    @Override
    public void onUpdateView(Activity context, IExpense view) {
        cbohelp = new CBO_DB_Helper(context);
        othExpenseDB = new OthExpenseDB(context);
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();

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
            view.setDA(""+expense.getDA_Amt());
            //view.setDAType(expense.getDA_TYPE_Display());
            view.setManualDA(getExpense().getSelectedDA());
            if (getExpense().getSelectedDA().getCode().equalsIgnoreCase("L")) {
                view.setTADetail("Not Applicable");
            }else {
                view.setTADetail(getExpense().getKm() + "Kms * " + AddToCartView.toCurrency(String.format("%.2f", getExpense().getRate())) + "/km");
            }
            //view.setDistance(expense.getFARE());
            //view.enableDA(othExpenseDB.get_DA_ACTION_exp_head().size() == 0);
            //view.ActualDAReqd(expense.getACTUALDA_FAREYN().equalsIgnoreCase("Y"));
            view.ManualDA_TypeReqd(expense.getDA_TYPE_MANUALYN().equalsIgnoreCase("Y"));
            if (getExpense().getDA_TYPE_MANUALYN().equalsIgnoreCase("Y")){
                setSelectedDA(getExpense().getSelectedDA());
            }
            view.ManualDAReqd(expense.getMANUAL_DAYN().equalsIgnoreCase("1"));
            view.ManualTAReqd(expense.getTA_TYPE_MANUALYN().equalsIgnoreCase("1"));
            view.ManualDistanceReqd(expense.getDISTANCE_TYPE_MANUALYN().equalsIgnoreCase("1"));
            view.ManualStationReqd(expense.getMANUAL_TAYN_STATION().equalsIgnoreCase("1"));
           // view.ActualFareReqd(expense.getACTUALFAREYN().equalsIgnoreCase("Y"));
            view.OnOtherExpenseUpdated(getExpense().getOthExpenses());
            view.OnTAExpenseUpdated(getExpense().getTA_Expenses());
            view.OnDAExpenseUpdated(getExpense().getDA_Expenses());
            view.OnFinalRemarkReqd(IsFormForFinalSubmit());
            view.updateDAView();

        }
    }

    public void setForFinalSubmit(String FinalSubmitYN){
        IsFinalSubmit = FinalSubmitYN.equalsIgnoreCase("Y");
    }

    public Boolean IsFormForFinalSubmit(){
        return IsFinalSubmit;
    }

    public String getFinalRemark() {
        return FinalRemark;
    }

    public void setFinalRemark(String finalRemark) {
        FinalRemark = finalRemark;
    }

    public mExpense getExpense(){
        return expense;
    }

    public void UpdateExpense(mOthExpense othExpense){
        //expense.getOthExpenses().add(othExpense);
        expense.setOthExpenses(othExpenseDB.get(eExpense.None));
        expense.setTA_Expenses(othExpenseDB.get(eExpense.TA));
        expense.setDA_Expenses(othExpenseDB.get(eExpense.DA));
        view.enableDA(othExpenseDB.get_DA_ACTION_exp_head().size() == 0);
        view.OnOtherExpenseUpdated(getExpense().getOthExpenses());
        view.OnTAExpenseUpdated(getExpense().getTA_Expenses());
        view.OnDAExpenseUpdated(getExpense().getDA_Expenses());
        view.updateDAView();
    }


    public void setSelectedDA(mDA da){
        getExpense().setDA(da);

        getExpense().setDistance(new mDistance());
        getExpense().setDA_TYPE(da.getCode());
        getExpense().setTA_Amt(da.getTAAmount());
        getExpense().setDA_Amt(da.getDAAmount());

        getExpense().setDISTANCE_TYPE_MANUALYN(da.getMANUAL_DISTANCEYN());
        getExpense().setTA_TYPE_MANUALYN(da.getMANUAL_TAYN());
        getExpense().setMANUAL_TAYN_MANDATORY(da.getMANUAL_TAYN_MANDATORY());

        if (view != null){
            view.setManualDA(da);
            if (da.getCode().equalsIgnoreCase("L")) {
                view.setTADetail("Not Applicable");
            }

            view.ManualTAReqd(expense.getTA_TYPE_MANUALYN().equalsIgnoreCase("1"));
            view.ManualDistanceReqd(expense.getDISTANCE_TYPE_MANUALYN().equalsIgnoreCase("1"));
            view.updateDAView();
        }

        setSelectedDistance(getExpense().getSelectedDistance());

        //updateDAView();

    }

    public void setSelectedDistance(mDistance distance){
        getExpense().setDistance(distance);
        getExpense().getSelectedDA().setTA_Km(distance.getKm());
        getExpense().getSelectedDA().setTA_Rate(getExpense().getRateFor(getExpense().getSelectedDA().getTA_Km()).getRate());
        getExpense().setTA_Amt( getExpense().getSelectedDA().getTAAmount());

        if (!getExpense().getTA_TYPE_MANUALYN().equalsIgnoreCase("1")) {
            getExpense().setTA_TYPE_MANUALYN(distance.getMANUAL_TAYN());
            getExpense().setMANUAL_TAYN_MANDATORY(distance.getMANUAL_TAYN_MANDATORY());
        }

        if (view != null){
            view.setTADetail(getExpense().getSelectedDA().getTA_Km() + "Kms * " + AddToCartView.toCurrency(String.format("%.2f", getExpense().getSelectedDA().getTA_Rate())) + "/km");
            view.setManualTA(distance);
            view.ManualTAReqd(expense.getTA_TYPE_MANUALYN().equalsIgnoreCase("1"));
            view.ManualDistanceReqd(expense.getDISTANCE_TYPE_MANUALYN().equalsIgnoreCase("1"));
            view.updateDAView();
        }
    }

    private void FinalSubmit(Activity context) {
        //Start of call to service

        String ACTUALFARE = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("ACTUALFARE","");
        if (ACTUALFARE.equals(""))
            ACTUALFARE = "" + 0;

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iDCRID", MyCustumApplication.getInstance().getUser().getDCRId());
        request.put("iNOCHEMIST", "1");
        request.put("sNOSTOCKIST", "1");
        request.put("sCHEMISTREMARK", "");
        request.put("sSTOCKISTREMARK", "");
        request.put("iPOB", "0.0");
        request.put("iPOBQTY", "0");
        request.put("iACTUALFAREAMT", ACTUALFARE);
        request.put("sDATYPE", "NA");
        request.put("iDISTANCE_ID", "99999");
        request.put("sREMARK", getFinalRemark());
        request.put("sLOC2", "" + MyCustumApplication.getInstance().getUser().getLocation() + "!^" + MyCustumApplication.getInstance().getUser().getLocation());
        request.put("iOUTTIME", "99");

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        new MyAPIService(context).execute(new ResponseBuilder("DCR_COMMITFINAL_1",request)
                .setTables(tables)
                .setDescription("Please wait.....\n" +
                        "DCR Submit in progress....")
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                       // setExpense(expense);
                        parserFinalSubmit(bundle,context);
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {

                        //parserFinalSubmit(bundle,context);
                    }

                    @Override
                    public void onError(String s, String s1) {
                        AppAlert.getInstance().getAlert(context,s,s1);
                    }
                }));

        //End of call to service


    }

    private void parserFinalSubmit(Bundle result,Activity context) throws JSONException{
                customVariablesAndMethod.SetLastCallLocation(context);

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject object = jsonArray1.getJSONObject(0);
                String value2 = object.getString("ID");

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DA_TYPE", "0");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "da_val", "0");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "distance_val", "0");

                customVariablesAndMethod.msgBox(context, " DCR Sucessfully Submitted");
                cbohelp.delete_Expense();
                othExpenseDB.delete();
                cbohelp.delete_Nonlisted_calls();
                cbohelp.deleteDcrAppraisal();
                cbohelp.deleteFinalDcr();
                cbohelp.deleteDCRDetails();
                cbohelp.deletedcrFromSqlite();

                Custom_Variables_And_Method.CHEMIST_NOT_VISITED = "";
                Custom_Variables_And_Method.STOCKIST_NOT_VISITED = "";
                /*Intent i = new Intent(NonWorking_DCR.this, LoginFake.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "Final_submit", "Y");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "work_type_Selected", "w");

                MyCustumApplication.getInstance().Logout((Activity) context);


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
        tables.add(3);
        tables.add(4);

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
                            .setMANDATORY(jsonObject1.getInt("MANDATORYYN_NEW"))
                            .setEXP_TYPE(eExpense.getExp(jsonObject1.getInt("CHECKDUPLICATE_HEADTYPE")))
                            .setSHOW_IN_TA_DA(eExpense.getExp(jsonObject1.getInt("SHOWIN_TA_OTHER")))
                            .setMAX_AMT(jsonObject1.getDouble("MAX_AMT"))
                            .setATTACHYN(jsonObject1.getInt("ATTACHYN"))
                            .setKMYN(jsonObject1.getString("KMYN"))
                            .setHEADTYPE_GROUP(jsonObject1.getString("CHECKDUPLICATE_HEADTYPE"))
                            .setMasterValidate(jsonObject1.getInt("TAMST_VALIDATEYN"));

                    expense.getExpHeads().add(expHead);

                    othExpenseDB.getExpHeadDB().insert(expHead);

                    cbohelp.Insert_EXP_Head(jsonObject1.getString("FIELD_NAME"), jsonObject1.getString("ID"),
                            jsonObject1.getString("MANDATORYYN_NEW"), jsonObject1.getString("DA_ACTION"),
                            jsonObject1.getString("CHECKDUPLICATE_HEADTYPE"), jsonObject1.getString("ATTACHYN"),
                            jsonObject1.getString("MAX_AMT"), jsonObject1.getString("TAMST_VALIDATEYN"));

                }


                String table2 = result.getString("Tables2");
                JSONArray jsonArray3 = new JSONArray(table2);
                JSONObject object = null;
                for (int i = 0; i < jsonArray3.length(); i++) {
                    object = jsonArray3.getJSONObject(i);


                    expense.setDA_TYPE(object.getString("DA_TYPE_NEW"))
                            .setDA_TYPE_Display(object.getString("DA_TYPE"))
                            .setDA_TYPE_MANUALYN(object.getString("DA_TYPE_MANUALYN"))
                            .setFARE(object.getString("FARE"))
                            .setTA_TYPE_MANUALYN(object.getString("MANUAL_TAYN"))
                            .setKm(object.getDouble("KM_NEW"))
                            .setRate(object.getDouble("FARE_RATE_NEW"))
                            .setMANUAL_DAYN(object.getString("MANUAL_DAYN"))
                            .setMANUAL_TAYN_KM(object.getString("MANUAL_TAYN_KM"))
                            .setMANUAL_TAYN_STATION(object.getString("MANUAL_TAYN_STATION"))
                            .setMANUAL_TAYN_MANDATORY(object.getString("MANUAL_TAYN_MANDATORY"))
                            .setDA_Amt(object.getDouble("DA_RATE_NEW"))
                            .setTA_Amt(object.getDouble("TA_AMT_NEW"))
                            .setACTUALDA_FAREYN(object.getString("ACTUALDA_FAREYN"))
                            .setACTUALFAREYN(object.getString("ACTUALFAREYN"))
                            .setROUTE_CLASS(object.getString("ROUTE_CLASS"))
                            .setACTUALFARE_MAXAMT(object.getDouble("ACTUALFARE_MAXAMT"))
                            .setACTUALFAREYN_MANDATORY(object.getString("ACTUALFAREYN_MANDATORY"));
                }


                if (object != null) {
                    expense.getRates().clear();
                    expense.getRates().add(new mRate().setRate(object.getDouble("FARE_RATE")));
                    if (object.getDouble("FARE_RATE_KM") !=0 && object.getDouble("FARE_RATE1_KM") !=0){
                        expense.getRates().get(0).setTKm(object.getDouble("FARE_RATE_KM"));
                    }
                    expense.getRates().add(new mRate().setRate(object.getDouble("FARE_RATE1")));
                    if (object.getDouble("FARE_RATE1_KM") !=0 && object.getDouble("FARE_RATE2_KM") !=0){
                        expense.getRates().get(1).setFKm(object.getDouble("FARE_RATE1_KM"));
                        expense.getRates().get(1).setTKm(object.getDouble("FARE_RATE2_KM"));
                    }
                    expense.getRates().add(new mRate().setRate(object.getDouble("FARE_RATE2")));
                    if (object.getDouble("FARE_RATE2_KM") !=0 && object.getDouble("FARE_RATE3_KM") !=0){
                        expense.getRates().get(2).setFKm(object.getDouble("FARE_RATE1_KM"));
                        expense.getRates().get(2).setTKm(object.getDouble("FARE_RATE2_KM"));
                    }
                    expense.getRates().add(new mRate().setRate(object.getDouble("FARE_RATE3")));
                }


                expense.getDAs().clear();
                String table3 = result.getString("Tables3");
                JSONArray jsonArray4 = new JSONArray(table3);
                for (int i = 0; i < jsonArray4.length(); i++) {
                    JSONObject object1 = jsonArray4.getJSONObject(i);
                    mDA da = new mDA();
                    da.setCode(object1.getString("FIELD_CODE"));
                    da.setName(object1.getString("FIELD_NAME"));
                    da.setMultipleFactor(object1.getDouble("FARE_MULT_BY"));
                    da.setMANUAL_DISTANCEYN(object1.getString("MANUAL_DISTANCEYN"));
                    da.setMANUAL_TAYN(object1.getString("MANUAL_TAYN"));
                    da.setMANUAL_TAYN_MANDATORY(object1.getString("MANUAL_TAYN_MANDATORY"));

                    if (object != null) {
                        da.setTA_Km(object.getDouble("KM_SINGLE_SIDE"));
                        da.setTA_Rate(object.getDouble("FARE_RATE"));
                        switch (da.getCode()) {
                            case "L":
                                da.setDAAmount(object.getDouble("DA_L_RATE"));
                                break;
                            case "EX":
                            case "EXS":
                                da.setDAAmount(object.getDouble("DA_EX_RATE"));
                                break;
                            case "NS":
                            case "NSD":
                                da.setDAAmount(object.getDouble("DA_NS_RATE"));
                                break;
                        }
                    }

                    if (expense.getDA_TYPE().equalsIgnoreCase(da.getCode())){
                        //view.setManualDA(da);
                        getExpense().setDA(da);
                    }

                    expense.getDAs().add(da);
                }

                expense.getDistances().clear();
                String table4 = result.getString("Tables4");
                JSONArray jsonArray5 = new JSONArray(table4);
                for (int i = 0; i < jsonArray5.length(); i++) {
                    JSONObject object1 = jsonArray5.getJSONObject(i);
                    mDistance distance = new mDistance();
                    distance.setId(object1.getString("DISTANCE_ID"));
                    distance.setName(object1.getString("STATION_NAME"));
                    distance.setKm(object1.getDouble("KM"));
                    distance.setMANUAL_TAYN(object1.getString("MANUAL_TAYN"));
                    distance.setMANUAL_TAYN_MANDATORY(object1.getString("MANUAL_TAYN_MANDATORY"));

                    expense.getDistances().add(distance);
                }

                expense.setOthExpenses(othExpenseDB.get(eExpense.None));
                expense.setTA_Expenses(othExpenseDB.get(eExpense.TA));
                expense.setDA_Expenses(othExpenseDB.get(eExpense.DA));



    }




    public void deleteExpense(Activity context,mOthExpense othExpense){
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",view.getCompanyCode());
        request.put("iPA_ID",view.getUserId());
        request.put("iDCR_ID", view.getDCRId());
        request.put("iID", ""+othExpense.getId());
        request.put("ISSUPPORTUSER", MyCustumApplication.getInstance().getUser().getLoggedInAsSupport()?"Y":"N");

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyAPIService(context).execute(new ResponseBuilder("DCREXPDELETEMOBILE_2",request)
                .setTables(tables)
                .setDescription("Please wait.....\n" +
                        "Deleting the expense from your planed DCR....")
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        String table0 = bundle.getString("Tables0");
                        JSONArray jsonArray1 = new JSONArray(table0);

                        JSONObject object = jsonArray1.getJSONObject(0);
                        String value2 = object.getString("INVALIDMSG");
                        if (object.getString("STATUS").equalsIgnoreCase("1")) {
                            Custom_Variables_And_Method.getInstance().msgBox(context, " Exp. Deleted Successfully");
                            UpdateExpense(othExpense);
                        }else{
                            AppAlert.getInstance().Alert(context, "Alert!!", value2,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                        }
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
        String value2 = object.getString("INVALIDMSG");
        if (object.getString("STATUS").equalsIgnoreCase("1")) {

            othExpenseDB.delete(othExpense);


            cbohelp.delete_Expense_withID("" + othExpense.getId());
        }





    }


    public void expense_commit(Activity context){

        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE( "da_val",""+getExpense().getDA_Amt());

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",view.getCompanyCode());
        request.put("iDcrId", view.getDCRId());
        request.put("sDaType", getExpense().getDA_TYPE());
        request.put("iDistanceId", "");
        request.put("iDA_VALUE", ""+getExpense().getDA_Amt());
        request.put("DA_TYPE_SAVEYN",getExpense().getDA_TYPE_MANUALYN());
        request.put("sStation", getExpense().getStation());
        request.put("ISSUPPORTUSER", MyCustumApplication.getInstance().getUser().getLoggedInAsSupport()?"Y":"N");

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyAPIService(context).execute(new ResponseBuilder("DCR_COMMITEXP_4",request)
                .setTables(tables)
                .setDescription("Please wait.....\n" +
                        "Finalizing the expense for your planed DCR....")
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {

                        String table0 = bundle.getString("Tables0");
                        JSONArray jsonArray1 = new JSONArray(table0);

                        JSONObject object = jsonArray1.getJSONObject(0);
                        String value2 = object.getString("DCR_ID");
                        if (value2.equalsIgnoreCase("1")) {
                            if (IsFormForFinalSubmit()) {
                                FinalSubmit(context);
                            } else {

                                cbohelp.markAsCalled(CallType.EXPENSE, true);

                                Custom_Variables_And_Method.getInstance().msgBox(context, " Expense Saved Sucessfully...");
                                context.finish();
                                Custom_Variables_And_Method.getInstance().setDataInTo_FMCG_PREFRENCE(context, "ACTUALFARE", "" + getExpense().getTA_Amt());


                                if (!getExpense().getACTUALFAREYN().equalsIgnoreCase("Y")) {
                                    Custom_Variables_And_Method.getInstance().setDataInTo_FMCG_PREFRENCE(context, "ACTUALFARE", "0");
                                }
                            }
                        }else{
                            AppAlert.getInstance().Alert(context, "Alert!!", value2,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
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
