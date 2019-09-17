package Bill.CompanySelecter;


import java.util.ArrayList;

import Bill.BillReport.mCompany;

public interface ICompany {
    void getReferencesById();
    void onPartyListUpdated(ArrayList<mCompany> billlist);
    void setFDATE();
    void setTDATE();

}
