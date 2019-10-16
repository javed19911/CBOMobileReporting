package bill.BillReport;

import java.io.Serializable;
import java.util.Date;

import bill.Cart.mCustomer;

public class mBill implements Serializable {

    private int ID = 0;
    private String partyId = "0";
    private String partyName ;
    private String COMPANY_NAME;
    private String POSTING_ID;
    private String BILL_PRINT;
    private Double NET_AMT;
    private Date DOC_DATE;
    private String DOC_NO;
    private String party_mobile="";
    private String PayMode="";

    private String TaxAmt ="";
    private String amt="";

    private String DOB="";
    private String DOA="";
    private String GST_NO="";

    private Boolean edit=false;
    private Boolean delete = false;


    public mBill() {

    }


    /// getter
    public int getID() {
        return ID;
    }

    public String getPartyId() {
        return partyId;
    }

    public String getPartyName() {
        return partyName;
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

    public String getParty_mobile() {
        return party_mobile;
    }

    public String getPayMode() {
        return PayMode;
    }

    public String getDOC_NO() {
        return DOC_NO;
    }

    public String getTaxAmt() {
        return TaxAmt;
    }

    public String getAmt() {
        return amt;
    }

    public Boolean getEdit() {
        return edit;
    }

    public Boolean getDelete() {
        return delete;
    }

    public String getDOB() {
        return DOB;
    }

    public String getDOA() {
        return DOA;
    }

    public String getGST_NO() {
        return GST_NO;
    }

    ///setter

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
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

    public void setParty_mobile(String party_mobile) {
        this.party_mobile = party_mobile;
    }

    public void setPayMode(String payMode) {
        PayMode = payMode;
    }

    public void setDOC_NO(String DOC_NO) {
        this.DOC_NO = DOC_NO;
    }

    public void setTaxAmt(String taxAmt) {
        TaxAmt = taxAmt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }


    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setDOA(String DOA) {
        this.DOA = DOA;
    }

    public void setGST_NO(String GST_NO) {
        this.GST_NO = GST_NO;
    }

}
