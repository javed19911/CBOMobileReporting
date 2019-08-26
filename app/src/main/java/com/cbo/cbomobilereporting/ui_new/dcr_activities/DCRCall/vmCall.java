package com.cbo.cbomobilereporting.ui_new.dcr_activities.DCRCall;

import android.app.Activity;
import android.content.Context;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import saleOrder.ViewModel.CBOViewModel;
import utils.adapterutils.SpinnerModel;

public class vmCall extends CBOViewModel<ICall> {
   private mChemistRc chemist= new mChemistRc();
   private ICall chemReminder;
   private Context context;
    String LeadSummaryLink = "";

    CBO_DB_Helper cbohelp;
    @Override
    public void onUpdateView(Activity context, ICall view) {
        if(view!= null){
            view.getRefrenceByid();
            view.getActivityTitle();
            view.setActivityTitle(view.getActivityTitle());
        }
    }

    public void setListener(Context context, ICall reminder) {
        this.context = context;
        this.chemReminder = reminder;
    }



    public mChemistRc getChemist() {
        return chemist;
    }

    public void setChemist(mChemistRc chemist) {
        this.chemist = chemist;

        if(view!=null){
            view.setChemistName(chemist);

        }
    }
    private SpinnerModel callModel;
    public void setCallmodel(Activity context, SpinnerModel mcallmodel) {

        callModel = mcallmodel;
        LeadSummaryLink = "";
        mChemistRc chemistCall = (mChemistRc) new mChemistRc()
                .setId(mcallmodel.getId())
                .setName(mcallmodel.getName());


        setChemist(chemistCall);

    }
}
