package com.cbo.cbomobilereporting.ui_new.Model;

import java.io.Serializable;

public class mSPO implements Serializable {

    public enum eSPO{
        NONE(""),
        CONSIGNEE("C"),
        HEADQUATER("H"),
        STOCKIST("P"),
        BILL("B");

        String value = "";
        eSPO(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    String FDate = null;
    String TDate = null;
    eSPO sType = eSPO.NONE;
    String ConsigneeId = "0";
    String HqId = "0";
    String StkId = "0";
    String CurrencyType = null;

    public mSPO(String FDate, String TDate, String currencyType) {
        this.FDate = FDate;
        this.TDate = TDate;
        CurrencyType = currencyType;
    }


    //getter


    public String getFDate() {
        return FDate;
    }

    public String getTDate() {
        return TDate;
    }

    public eSPO getType() {
        return sType;
    }

    public String getConsigneeId() {
        return ConsigneeId;
    }

    public String getHqId() {
        return HqId;
    }

    public String getStkId() {
        return StkId;
    }

    public String getCurrencyType() {
        return CurrencyType;
    }


    // setter

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }

    public void setTDate(String TDate) {
        this.TDate = TDate;
    }

    public void setType(eSPO sType) {
        this.sType = sType;
    }

    public void setConsigneeId(String consigneeId) {
        ConsigneeId = consigneeId;
    }

    public void setHqId(String hqId) {
        HqId = hqId;
    }

    public void setStkId(String stkId) {
        StkId = stkId;
    }

    public void setCurrencyType(String currencyType) {
        CurrencyType = currencyType;
    }
}
