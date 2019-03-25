package com.cbo.cbomobilereporting.ui_new.dcr_activities.Customer;

import android.app.Activity;
import android.database.Cursor;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.util.ArrayList;

import saleOrder.CBOViewModel;
import utils.adapterutils.Dcr_Workwith_Model;
import utils.adapterutils.PobModel;
import utils.adapterutils.SpinnerModel;

/**
 * Created by cboios on 10/03/19.
 */

public class vmCustomerCall extends CBOViewModel<iCustomerCall> {


    private mCustomerCall customerCall = new mCustomerCall();
    CBO_DB_Helper cbohelp;

    @Override
    public void onUpdateView(Activity context, iCustomerCall view) {
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
            view.setLeads(customerCall.getLeads());
            view.setProduct(customerCall.getPOBs());
            view.setGift(customerCall.getGifts());
        }
    }

    private SpinnerModel callModel;

    public SpinnerModel getCallmodel() {
        return callModel;
    }

    public void setCallmodel(SpinnerModel callmodel) {
        callModel = callmodel;
        mCustomerCall customerCall = (mCustomerCall) new mCustomerCall()
                .setId(callmodel.getId())
                .setName(callmodel.getName());

        setCustomer(customerCall);
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
}
