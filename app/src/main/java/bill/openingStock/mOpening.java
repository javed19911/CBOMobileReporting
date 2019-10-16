package bill.openingStock;

import java.io.Serializable;
import java.util.Date;

public class mOpening implements Serializable {

    private int ID = 0;
    private String POSTING_ID = "";
    private Date DOC_DATE;
    private String DOC_NO = "";
    private String NO_ITEM = "";
    private String ENTRY_BY_ID = "";
    private String COMPANY_NAME;
    private String ENTRY_BY = "";
    private String DOC_DATE_ORDER = "";
    private Boolean edit = false;
    private Boolean delete = false;


    public String getENTRY_BY_ID() {
        return ENTRY_BY_ID;
    }

    public void setENTRY_BY_ID(String ENTRY_BY_ID) {
        this.ENTRY_BY_ID = ENTRY_BY_ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPOSTING_ID() {
        return POSTING_ID;
    }

    public void setPOSTING_ID(String POSTING_ID) {
        this.POSTING_ID = POSTING_ID;
    }

    public Date getDOC_DATE() {
        return DOC_DATE;
    }

    public void setDOC_DATE(Date DOC_DATE) {
        this.DOC_DATE = DOC_DATE;
    }

    public String getDOC_NO() {
        return DOC_NO;
    }

    public void setDOC_NO(String DOC_NO) {
        this.DOC_NO = DOC_NO;
    }

    public String getNO_ITEM() {
        return NO_ITEM;
    }

    public void setNO_ITEM(String NO_ITEM) {
        this.NO_ITEM = NO_ITEM;
    }

    public String getENTRY_BY() {
        return ENTRY_BY;
    }

    public void setENTRY_BY(String ENTRY_BY) {
        this.ENTRY_BY = ENTRY_BY;
    }

    public String getDOC_DATE_ORDER() {
        return DOC_DATE_ORDER;
    }

    public void setDOC_DATE_ORDER(String DOC_DATE_ORDER) {
        this.DOC_DATE_ORDER = DOC_DATE_ORDER;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public String getCOMPANY_NAME() {
        return COMPANY_NAME;
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }
}
