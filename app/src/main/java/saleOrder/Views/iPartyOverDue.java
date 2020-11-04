package saleOrder.Views;


import java.util.ArrayList;

import saleOrder.Model.mOverDue;
import saleOrder.Model.mParty;

public interface iPartyOverDue {

    void getReferencesById();
    void setTitle(String title);
    mParty getParty();
    String getCompanyCode();
    String getUserID();
    void onOverDueListChanged(ArrayList<mOverDue> overDues);
    void onSendResponse();
}
