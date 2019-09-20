package bill.CompanySelecter;


import java.util.ArrayList;
import java.util.Date;

import bill.BillReport.mCompany;

public interface ICompany {
    void onPartyListUpdated(ArrayList<mCompany> billlist);
    void setFDATE(Date date);
    void setTDATE(Date date);

}
