package com.cbo.cbomobilereporting.ui_new.dcr_activities.ExpenseNew;

import android.os.Parcel;
import android.os.Parcelable;

public class Mexpenses implements Parcelable {

    private String Ta_amt;
    private String DA_amt;
    private String exp_head;
    private  String exp_head_id;
    private String remark;
    private  String filename;


    public String getExp_head_id() {
        return exp_head_id;
    }

    public void setExp_head_id(String exp_head_id) {
        this.exp_head_id = exp_head_id;
    }

    public String getExp_head() {
        return exp_head;
    }

    public void setExp_head(String exp_head) {
        this.exp_head = exp_head;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Mexpenses() {
    }


    public String getTa_amt() {
        return Ta_amt;
    }

    public void setTa_amt(String ta_amt) {
        Ta_amt = ta_amt;
    }

    public String getDA_amt() {
        return DA_amt;
    }

    public void setDA_amt(String DA_amt) {
        this.DA_amt = DA_amt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Ta_amt);
        dest.writeString(this.DA_amt);
        dest.writeString(this.exp_head);
        dest.writeString(this.exp_head_id);
        dest.writeString(this.remark);
        dest.writeString(this.filename);
    }

    protected Mexpenses(Parcel in) {
        this.Ta_amt = in.readString();
        this.DA_amt = in.readString();
        this.exp_head = in.readString();
        this.exp_head_id = in.readString();
        this.remark = in.readString();
        this.filename = in.readString();
    }

    public static final Creator<Mexpenses> CREATOR = new Creator<Mexpenses>() {
        @Override
        public Mexpenses createFromParcel(Parcel source) {
            return new Mexpenses(source);
        }

        @Override
        public Mexpenses[] newArray(int size) {
            return new Mexpenses[size];
        }
    };
}
