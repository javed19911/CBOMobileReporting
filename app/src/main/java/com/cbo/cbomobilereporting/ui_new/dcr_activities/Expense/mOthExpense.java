package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import java.io.Serializable;

public class mOthExpense implements Serializable {
    private int Id= 0;
    private mExpHead expHead = new mExpHead(0,"");
    private Double km = 0D;
    private Double amount = 0D;
    private String Remark ="";
    private String Attachment = "";
    private String time = "";

    /// getter


    public int getId() {
        return Id;
    }

    public mExpHead getExpHead() {
        return expHead;
    }

    public Double getKm(){ return km;}

    public Double getAmount() {
        return amount;
    }

    public String getRemark() {
        return Remark;
    }

    public String getAttachment() {
        return Attachment;
    }

    public String getTime() {
        return time;
    }

    ///setter

    public mOthExpense setId(int id) {
        Id = id;
        return this;
    }

    public mOthExpense setExpHead(mExpHead expHead) {
        this.expHead = expHead;
        return this;
    }

    public mOthExpense setKm(Double km) {
        this.km = km;
        return this;
    }

    public mOthExpense setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public mOthExpense setRemark(String remark) {
        Remark = remark;
        return this;
    }

    public mOthExpense setAttachment(String attachment) {
        Attachment = attachment;
        return this;
    }

    public mOthExpense setTime(String time) {
        this.time = time;
        return this;
    }
}
