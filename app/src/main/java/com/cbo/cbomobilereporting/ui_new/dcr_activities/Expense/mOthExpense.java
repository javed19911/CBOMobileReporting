package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class mOthExpense implements Serializable {
    private int Id= 0;
    private mExpHead expHead = new mExpHead(0,"");
    private Double km = 0D;
    private Double amount = 0D;
    private String Remark ="";
    private String Attachment = "";
    private String time = "";
    private Boolean editable = true;

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



    public String getAttachmentName(){
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for(String path :  getAttachmentArr()) {
            File file = new File(path);
            if (count != 0) {
                sb.append("|^");
            }
            ++count;
            sb.append(file.getName());
        }
        return sb.toString();
    }
    public ArrayList<String> getAttachmentArr() {
        return this.getAttachment().isEmpty() ? new ArrayList() : new ArrayList(Arrays.asList(this.Attachment.split("\\|\\^")));
    }


    public String getTime() {
        return time;
    }

    public boolean IsEditable(){
        return editable;
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

    public mOthExpense setAttachment(ArrayList<String> attachment) {
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for(String file : attachment) {
            if (count != 0) {
                sb.append("|^");
            }
            ++count;
            sb.append(file);
        }
        this.Attachment = sb.toString();
        return this;
    }

    public mOthExpense setTime(String time) {
        this.time = time;
        return this;
    }

    public mOthExpense setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }
}
