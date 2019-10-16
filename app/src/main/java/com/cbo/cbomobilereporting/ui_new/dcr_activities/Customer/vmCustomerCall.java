package com.cbo.cbomobilereporting.ui_new.dcr_activities.Customer;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

import saleOrder.ViewModel.CBOViewModel;
import services.MyAPIService;
import utils.adapterutils.Dcr_Workwith_Model;
import utils.adapterutils.PobModel;
import utils.adapterutils.SpinnerModel;

/**
 * Created by cboios on 10/03/19.
 */

public class vmCustomerCall extends CBOViewModel<iCustomerCall> {


    private mCustomerCall customerCall = new mCustomerCall();
    CBO_DB_Helper cbohelp;
    ArrayList<String> remarkList = null;
    ArrayList<String> statusList = null;
    String LeadSummaryLink = "";


    @Override
    public void onUpdateView(AppCompatActivity context, iCustomerCall view) {
        cbohelp = new CBO_DB_Helper(context);
        view.getReferencesById();
        view.setTile();
        view.setLoctionUI();
    }

    public mCustomerCall getCustomer(){
        return customerCall;
    }

    public void setCustomer(mCustomerCall customerCall){
        this.customerCall = customerCall;
        if(view != null){
            view.setLoctionUI();
            view.setCustomer(customerCall);
            view.setWorkWith(customerCall.getWorkwiths());
            view.setRemark(customerCall.getRemark());
            view.setStatus(customerCall.getStatus());
            view.setCompetitor_Product (customerCall.getCompetitor_Product ());
            view.setLeads(customerCall.getLeads());
            view.setProduct(customerCall.getPOBs());
            view.setGift(customerCall.getGifts());
            view.setAttachments(customerCall.getAttachments());
            IsLeadSummaryRequired();
        }
    }

    private SpinnerModel callModel;

    public SpinnerModel getCallmodel() {
        return callModel;
    }

    public void setCallmodel(AppCompatActivity context, SpinnerModel callmodel) {
        callModel = callmodel;
        LeadSummaryLink = "";
        mCustomerCall customerCall = (mCustomerCall) new mCustomerCall()
                .setId(callmodel.getId())
                .setName(callmodel.getName());

        if (!cbohelp.getMenuUrl("TRANSACTION", "T_LEADM").trim().isEmpty() ){
            getLeadSummaryURL(context,callmodel);
        }
        setCustomer(customerCall);
    }

    private void IsLeadSummaryRequired(){
        /*if (LeadSummaryLink== null){
            LeadSummaryLink = cbohelp.getMenuUrl("TRANSACTION", "T_LEADM");
        }*/
        if(view != null){
            view.setLead_SummaryVisibility(!LeadSummaryLink.trim().isEmpty());
        }
    }

    public String  getLeadSummaryLink(){
        return LeadSummaryLink;
    }

    public void setWorkWith(ArrayList<Dcr_Workwith_Model> workWiths){
        getCustomer().setWorkwiths(workWiths);
        if(view != null){
            view.setWorkWith(customerCall.getWorkwiths());
        }
    }
    public void setLeads(ArrayList<PobModel> leads){
        getCustomer().setLeads(leads);
        if(view != null){
            view.setLeads(customerCall.getLeads());
        }
    }

    public void setPOBs(ArrayList<PobModel> pobs){
        getCustomer().setPOBs(pobs);
        if(view != null){
            view.setProduct(customerCall.getPOBs());
        }
    }

    public void setAttachment(String attachment){
        customerCall.getAttachments().clear();
        if(attachment != null) {
            customerCall.getAttachments().add(attachment);
        }
        if(view != null){
            view.setAttachments(customerCall.getAttachments());
        }
    }

    public ArrayList<String> getRemarkList(){
        if (remarkList == null) {
            remarkList = cbohelp.get_Doctor_Call_Remark();
        }
        return remarkList;
    }

    public ArrayList<String> getStatusList(){
        if (statusList == null) {
            statusList = cbohelp.get_Doctor_Call_Status_List();
        }
        return statusList;
    }

    public void setRemark(String remark){
        getCustomer().setRemark(remark);
        if(view != null){
            view.setRemark(remark);
        }

    }

    public void setStatus(String status){
        getCustomer().setStatus(status);
        if(view != null){
            view.setStatus(status);
        }

    }
    public  void  setCompetitor_Product( String  competitor_product){

        getCustomer ().setCompetitor_Product (competitor_product);
        if(view!=null){
            view.setCompetitor_Product (competitor_product);
        }
    }

    public void customers(){

        if (view != null) {
            ArrayList<SpinnerModel> chemist = new ArrayList<SpinnerModel>();
            Cursor c = cbohelp.getChemistListLocal();

            if (c.moveToFirst()) {
                do {
                    chemist.add(new SpinnerModel(c.getString(c.getColumnIndex("chem_name")), c.getString(c.getColumnIndex("chem_id"))));
                } while (c.moveToNext());
            }
            view.openCustomerList(chemist);
        }

    }

    public void getLeadSummaryURL(AppCompatActivity context, SpinnerModel callmodel){

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", view.getCompanyCode());
        request.put("iCHEM_ID", callmodel.getId() );
        request.put("iLOGIN_PA_ID", view.getUserId() );


        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyAPIService(context).execute(new ResponseBuilder("DCRLEAD_PENDING_URL_1",request)
                .setTables(tables)
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        String table0 = bundle.getString("Tables0");
                        JSONArray jsonArray = new JSONArray(table0);
                        LeadSummaryLink = jsonArray.getJSONObject(0).getString("LEAD_URL");
                        IsLeadSummaryRequired();
                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {


                    }

                    @Override
                    public void onError(String s, String s1) {

                    }
                }));


        //End of call to service
    }
}
