package com.cbo.cbomobilereporting.ui_new.dcr_activities.DCRCall;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils.mCall;

public class mChemistRc extends mCall {


    public mChemistRc() {
        super("ChemistRc");
        setWorkwithreqd(false);
        setDcr_id(MyCustumApplication.getInstance().getUser().getDCRId());
        setDcr_date(MyCustumApplication.getInstance().getUser().getDCRDate());
        setRemarkReqd(MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DCR_DR_REMARKYN","").equalsIgnoreCase("y"));
        setRemarkMandatory(IsRemarkReqd() && MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("REMARK_WW_MANDATORY","").contains("R"));
        setItemReqd(false);
        setGiftReqd(false);
    }


}
