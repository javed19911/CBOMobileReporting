package com.cbo.cbomobilereporting.ui_new.mail_activities.popup_noti;

import java.io.Serializable;

public class PopUpModel implements Serializable {

    private String TEXT_MSG = "";
    private String PAGE_CODE = "";
    private String DCR_CODE = "";
    private String TNAME;



    public void setTVALUE(String TVALUE) {
        this.TVALUE = TVALUE;
    }

    private String TVALUE;

    public PopUpModel( String PAGE_CODE, String DCR_CODE, String TNAME, String TVALUE) {
        this.PAGE_CODE = PAGE_CODE;
        this.DCR_CODE = DCR_CODE;
        this.TVALUE = TVALUE;
        this.TNAME = TNAME;
    }

    public String getTEXT_MSG() {
        return TEXT_MSG;
    }

    public void setTEXT_MSG(String TEXT_MSG) {
        this.TEXT_MSG = TEXT_MSG;
    }

    public String getPAGE_CODE() {
        return PAGE_CODE;
    }

    public void setPAGE_CODE(String PAGE_CODE) {
        this.PAGE_CODE = PAGE_CODE;
    }

    public String getDCR_CODE() {
        return DCR_CODE;
    }

    public void setDCR_CODE(String DCR_CODE) {
        this.DCR_CODE = DCR_CODE;
    }
    public String getTNAME() {
        return TNAME;
    }

    public void setTNAME(String TNAME) {
        this.TNAME = TNAME;
    }

    public String getTVALUE() {
        return TVALUE;
    }

}
