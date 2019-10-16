package bill.Outlet;

import java.io.Serializable;

public class mOutlet implements Serializable {
    private String COMPANY_ID ="0";
    private String COMPANY_NAME ="";
    private String TOTAL_SALE ="";
    private String CASH_SALE ="";
    private String OTHER_SALE ="";
    private String NO_BILL ="";
    private String FDATE ="";
    private String TDATE ="";




    ///getter


    public String getCOMPANY_ID() {
        return COMPANY_ID;
    }

    public String getCOMPANY_NAME() {
        return COMPANY_NAME;
    }

    public String getTOTAL_SALE() {
        return TOTAL_SALE;
    }

    public String getCASH_SALE() {
        return CASH_SALE;
    }

    public String getOTHER_SALE() {
        return OTHER_SALE;
    }

    public String getNO_BILL() {
        return NO_BILL;
    }

    public String getFDATE() {
        return FDATE;
    }

    public String getTDATE() {
        return TDATE;
    }



    ///setter

    public void setCOMPANY_ID(String COMPANY_ID) {
        this.COMPANY_ID = COMPANY_ID;
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public void setTOTAL_SALE(String TOTAL_SALE) {
        this.TOTAL_SALE = TOTAL_SALE;
    }

    public void setCASH_SALE(String CASH_SALE) {
        this.CASH_SALE = CASH_SALE;
    }

    public void setOTHER_SALE(String OTHER_SALE) {
        this.OTHER_SALE = OTHER_SALE;
    }

    public void setNO_BILL(String NO_BILL) {
        this.NO_BILL = NO_BILL;
    }

    public void setFDATE(String FDATE) {
        this.FDATE = FDATE;
    }

    public void setTDATE(String TDATE) {
        this.TDATE = TDATE;
    }
}
