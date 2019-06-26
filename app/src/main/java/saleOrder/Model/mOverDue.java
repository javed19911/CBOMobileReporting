package saleOrder.Model;

public class mOverDue {
    private String DOC_NO = "";
    private String DOC_DATE = "";
    private String DUE_DATE = "";
    private double BAL_AMT = 0f;


    ///getter


    public String getDOC_NO() {
        return DOC_NO;
    }

    public String getDOC_DATE() {
        return DOC_DATE;
    }

    public String getDUE_DATE() {
        return DUE_DATE;
    }

    public double getBAL_AMT() {
        return BAL_AMT;
    }


    ///setter


    public mOverDue setDOC_NO(String DOC_NO) {
        this.DOC_NO = DOC_NO;
        return this;
    }

    public mOverDue setDOC_DATE(String DOC_DATE) {
        this.DOC_DATE = DOC_DATE;
        return this;
    }

    public mOverDue setDUE_DATE(String DUE_DATE) {
        this.DUE_DATE = DUE_DATE;
        return this;
    }

    public mOverDue setBAL_AMT(double BAL_AMT) {
        this.BAL_AMT = BAL_AMT;
        return this;
    }
}
