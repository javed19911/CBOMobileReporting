package com.cbo.cbomobilereporting.ui_new.dcr_activities.Recipt;

import java.io.Serializable;

public  class mRecipt implements Serializable {


    private int Id= 0;



    public int getId() {
        return Id;
    }

    public mRecipt setId(int id) {
        Id = id;
        return this;
    }


    private String A_Paid;//loginid
    private String reciept_no;//ID-edit,delete
    private String party_name;//Pa_name
    private String party_id;//PAID
    private Double amount;//AMOUNT
    private String remark;//REMARK
    private String Doc_Date;//DATE
    private String reciepent_no;//DOC_NO-user


    public String getDoc_Date() {
        return Doc_Date;
    }

    public mRecipt setDoc_Date(String doc_Date)
    {
        Doc_Date = doc_Date;
        return    this;
    }

    public String getReciepent_no() {
        return reciepent_no;
    }

    public mRecipt setReciepent_no(String reciepent_no) {

        this.reciepent_no = reciepent_no;
        return this;
    }

    public String getA_Paid() {
        return A_Paid;

    }

    public mRecipt setA_Paid(String a_Paid) {
        this. A_Paid = a_Paid;
        return this;
    }


    public String getReciept_no() {
        return reciept_no;
    }

  public mRecipt setReciept_no(String reciept_no) {
    this.reciept_no = reciept_no;
    return  this;

    }

    public String getParty_name() {
        return party_name;
    }

    public mRecipt setParty_name(String party_name) {

        this.party_name = party_name;
        return  this;
    }


    public String getParty_id() {
        return party_id;
    }

    public mRecipt setParty_id(String party_id) {
        this.party_id = party_id;
        return  this;
    }

    public Double getAmount() {
        return amount;
    }

    public mRecipt setAmount(Double amount) {


        this.amount = amount;
        return  this;
    }

    public String getRemark() {
        return remark;
    }

    public mRecipt setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public mRecipt() {
    }




}
