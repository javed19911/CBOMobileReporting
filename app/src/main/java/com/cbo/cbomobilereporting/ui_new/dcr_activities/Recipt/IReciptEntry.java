package com.cbo.cbomobilereporting.ui_new.dcr_activities.Recipt;

import java.util.ArrayList;
import java.util.Date;

import utils.model.DropDownModel;

public interface IReciptEntry {
    void onParylistUpdated(ArrayList<DropDownModel> partylist);



    void setReceiptTitle(String title);
    void onRecipetSubmited();

    void getReferencesById();

    void setRecieptNo(String recieptNo);

    void setPartyname(String partyname);

    void setDate(String date);

    void setAmount(Double amount);

    void setRemark(String remark);

    void setRecpientBy(String recpientBy);
}
