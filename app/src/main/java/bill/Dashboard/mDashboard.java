package bill.Dashboard;

import java.io.Serializable;

public class mDashboard implements Serializable {
    private String DOC_TYPE="";
    private String DOC_CAPTION="";
    private String TOTAL_SALE="";
    private String CASH_SALE="";
    private String OTHER_SALE="";
    private String NO_BILL="";
    private String BG_COLOR="#FFFFFF";

    //getter


    public String getDOC_TYPE() {
        return DOC_TYPE;
    }

    public String getDOC_CAPTION() {
        return DOC_CAPTION;
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

    public String getBG_COLOR() {
        return BG_COLOR;
    }


    //setter


    public void setDOC_TYPE(String DOC_TYPE) {
        this.DOC_TYPE = DOC_TYPE;
    }

    public void setDOC_CAPTION(String DOC_CAPTION) {
        this.DOC_CAPTION = DOC_CAPTION;
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

    public void setBG_COLOR(String BG_COLOR) {
        this.BG_COLOR = BG_COLOR;
    }
}
