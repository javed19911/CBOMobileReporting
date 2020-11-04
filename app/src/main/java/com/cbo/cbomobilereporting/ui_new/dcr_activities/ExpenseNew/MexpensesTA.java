package com.cbo.cbomobilereporting.ui_new.dcr_activities.ExpenseNew;

import android.os.Parcel;
import android.os.Parcelable;

public  class MexpensesTA implements Parcelable {
     private String Ta_amt;
     private String  Ta_exp_head;
     private  String  Ta_exp_head_id;
     private String  Ta_remark;
     private  String  Ta_filename;




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Ta_amt);
        dest.writeString(this.Ta_exp_head);
        dest.writeString(this.Ta_exp_head_id);
        dest.writeString(this.Ta_remark);
        dest.writeString(this.Ta_filename);
    }

    public MexpensesTA() {
    }

    protected MexpensesTA(Parcel in) {
        this.Ta_amt = in.readString();
        this.Ta_exp_head = in.readString();
        this.Ta_exp_head_id = in.readString();
        this.Ta_remark = in.readString();
        this.Ta_filename = in.readString();
    }

    public static final Parcelable.Creator<MexpensesTA> CREATOR = new Parcelable.Creator<MexpensesTA>() {
        @Override
        public MexpensesTA createFromParcel(Parcel source) {
            return new MexpensesTA(source);
        }

        @Override
        public MexpensesTA[] newArray(int size) {
            return new MexpensesTA[size];
        }
    };

    public String getTa_amt() {
        return Ta_amt;
    }

    public void setTa_amt(String ta_amt) {
        Ta_amt = ta_amt;
    }

    public String getTa_exp_head() {
        return Ta_exp_head;
    }

    public void setTa_exp_head(String ta_exp_head) {
        Ta_exp_head = ta_exp_head;
    }

    public String getTa_exp_head_id() {
        return Ta_exp_head_id;
    }

    public void setTa_exp_head_id(String ta_exp_head_id) {
        Ta_exp_head_id = ta_exp_head_id;
    }

    public String getTa_remark() {
        return Ta_remark;
    }

    public void setTa_remark(String ta_remark) {
        Ta_remark = ta_remark;
    }

    public String getTa_filename() {
        return Ta_filename;
    }

    public void setTa_filename(String ta_filename) {
        Ta_filename = ta_filename;
    }
}
