package com.cbo.cbomobilereporting.ui_new.report_activities.DCRReport;


public class mDCR_Report {
    String date;
    String with;
    String ttldr;
    String ttlchm;
    String ttlstk;
    String ttlexp;
    String ttlMissedCall;
    String ttlDrRiminder, ttlNonDoctor, ttlTenivia;
    String dairyCount, PolutaryCount;
    String remark;

    public String getRxCaps() {
        return rxCaps;
    }

    public void setRxCaps(String rxCaps) {
        this.rxCaps = rxCaps;
    }

    String rxCaps;
    boolean blinkRemark = false;


    public mDCR_Report() {

    }


    public String getDate() {
        return date;
    }

    public mDCR_Report setDate(String date) {
        this.date = date;
        return  this;
    }

    public String getWith() {
        return with;
    }

    public mDCR_Report setWith(String with) {
        this.with = with;
        return  this;
    }

    public String getTtldr() {
        return ttldr;
    }

    public mDCR_Report setTtldr(String dr) {
        this.ttldr = dr;
        return this;

    }

    public String getTtlchm() {
        return ttlchm;
    }

    public mDCR_Report setTtlchm(String chm) {
        this.ttlchm = chm;
        return  this;
    }

    public String getTtlstk() {
        return ttlstk;
    }

    public mDCR_Report setTtlstk(String stk) {
        this.ttlstk = stk;
        return  this;
    }

    public String getTtlexp() {
        return ttlexp;
    }

    public mDCR_Report setTtlexp(String exp) {
        this.ttlexp = exp;
        return  this;
    }

    public String getTtlMissedCall() {
        return ttlMissedCall;
    }

    public String getTtlDrRiminder() {
        return ttlDrRiminder;
    }

    public String getTtlTenivia() {
        return ttlTenivia;
    }

    public String getTtlNonDoctor() {
        return ttlNonDoctor;
    }

    public mDCR_Report setTtlMissedCall(String ttlMissedCall) {
        this.ttlMissedCall = ttlMissedCall;
        return  this;
    }

    public mDCR_Report setTtlDrRiminder(String ttlDrRiminder) {
        this.ttlDrRiminder = ttlDrRiminder;
        return  this;
    }

    public mDCR_Report setTtlTenivia(String ttlTenivia) {
        this.ttlTenivia = ttlTenivia;
        return  this;
    }

    public mDCR_Report setTtlNonDoctor(String ttlNonDoctor) {
        this.ttlNonDoctor = ttlNonDoctor;
        return  this;
    }

    public mDCR_Report setRemark(String remark) {
        this.remark = remark;
        return  this;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getDairyCount() {
        return dairyCount;
    }

    public mDCR_Report setDairyCount(String dairyCount) {
        this.dairyCount = dairyCount;
        return  this;
    }

    public String getPolutaryCount() {
        return PolutaryCount;
    }

    public mDCR_Report setPolutaryCount(String polutaryCount) {
        PolutaryCount = polutaryCount;
        return  this;
    }


    public boolean isBlinkRemark() {
        return blinkRemark;
    }

    public void setBlinkRemark(boolean blinkRemark) {
        this.blinkRemark = blinkRemark;
    }

}

