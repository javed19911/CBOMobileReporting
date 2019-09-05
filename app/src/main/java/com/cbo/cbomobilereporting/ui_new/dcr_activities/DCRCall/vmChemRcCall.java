package com.cbo.cbomobilereporting.ui_new.dcr_activities.DCRCall;

import android.app.Activity;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import saleOrder.ViewModel.CBOViewModel;
import utils.adapterutils.SpinnerModel;

public class vmChemRcCall extends CBOViewModel<IChemRcCall> {
   private mChemistRc chemist;
    CBO_DB_Helper cbohelp;
    @Override
    public void onUpdateView(Activity context, IChemRcCall view) {
        if(view!= null) {
            view.getRefrenceByid();
            view.setActivityTitle(view.getActivityTitle());
            setChemist((mChemistRc) new mChemistRc().setId("0").setName("--- Select ---"));
        }
    }



    public mChemistRc getChemist() {
        return chemist;
    }

    public void setChemist(mChemistRc chemist) {
        this.chemist = chemist;

        if(view!=null){
            view.setName(chemist.getName());
            view.setRemark(chemist.getRemark());
            view.remarkReqd(chemist.IsRemarkReqd());

        }
    }

    public void setCallmodel(SpinnerModel mcallmodel) {
        mChemistRc chemistCall = (mChemistRc) new mChemistRc()
                .setId(mcallmodel.getId())
                .setName(mcallmodel.getName())
                .setCallLatLong(mcallmodel.getLAT_LONG())
                .setRefLatLong(mcallmodel.getREF_LAT_LONG());


        setChemist(chemistCall);

    }
}
