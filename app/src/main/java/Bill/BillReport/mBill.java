package Bill.BillReport;

import com.cbo.cbomobilereporting.ui_new.dcr_activities.Recipt.mParty;

import java.io.Serializable;
import java.util.Date;

public class mBill implements Serializable {

    private int ID = 0;
    private String COMPANY_NAME;
    private String POSTING_ID;
    private String BILL_PRINT;
    private Double NET_AMT;
    private Date DOC_DATE;


    public mBill() {

    }

    public int getID() {
        return ID;
    }

    public String getCOMPANY_NAME() {
        return COMPANY_NAME;
    }

    public String getPOSTING_ID() {
        return POSTING_ID;
    }

    public String getBILL_PRINT() {
        return BILL_PRINT;
    }

    public Double getNET_AMT() {
        return NET_AMT;
    }

    public Date getDOC_DATE() {
        return DOC_DATE;
    }


    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public void setPOSTING_ID(String POSTING_ID) {
        this.POSTING_ID = POSTING_ID;
    }

    public void setBILL_PRINT(String BILL_PRINT) {
        this.BILL_PRINT = BILL_PRINT;
    }

    public void setNET_AMT(Double NET_AMT) {
        this.NET_AMT = NET_AMT;
    }

    public void setDOC_DATE(Date DOC_DATE) {
        this.DOC_DATE = DOC_DATE;
    }
}
