package com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils;

import com.cbo.cbomobilereporting.ui_new.Model.mWorkwith;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import utils.adapterutils.Dcr_Workwith_Model;
import utils.adapterutils.PobModel;

/**
 * Created by cboios on 10/03/19.
 */

public class mCall implements Serializable {
    private String id = "0";
    private String name = "";
    private Boolean workwithreqd = false;
    private ArrayList<Dcr_Workwith_Model> workwiths = new ArrayList<>();
    private String remark="";
    private Boolean itemReqd = true;
    private ArrayList<PobModel> POBs = new ArrayList<>();
    private Boolean giftReqd = true;
    private ArrayList<PobModel> gifts = new ArrayList<>();
    private String refLatLong="0.0,0.0";
    private String callLatLong="0.0,0.0";
    private String lastVisited = "";
    private ArrayList<String> attachments = new ArrayList<>();

    //getter

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getWorkwithreqd() {
        return workwithreqd;
    }

    public ArrayList<Dcr_Workwith_Model> getWorkwiths() {
        return workwiths;
    }

    public String getRemark() {
        return remark;
    }



    public ArrayList<PobModel> getPOBs() {
        return POBs;
    }

    public Boolean getItemReqd() {
        return itemReqd;
    }

    public Boolean getGiftReqd() {
        return giftReqd;
    }

    public ArrayList<PobModel> getGifts() {
        return gifts;
    }

    public ArrayList<String> getAttachments() {
        return attachments;
    }


    //setter

    public mCall setId(String id) {
        this.id = id;
        return this;
    }

    public mCall setName(String name) {
        this.name = name;
        return this;
    }

    public mCall setWorkwithreqd(Boolean workwithreqd) {
        this.workwithreqd = workwithreqd;
        return this;
    }

    public mCall setWorkwiths(ArrayList<Dcr_Workwith_Model> workwiths) {
        this.workwiths = workwiths;
        return this;
    }

    public mCall setRemark(String remark) {
        this.remark = remark;
        return this;
    }


    public mCall setPOBs(ArrayList<PobModel> POBs) {
        this.POBs = POBs;
        return this;
    }

    public mCall setItemReqd(Boolean itemReqd) {
        this.itemReqd = itemReqd;
        return this;
    }

    public mCall setGiftReqd(Boolean giftReqd) {
        this.giftReqd = giftReqd;
        return this;
    }

    public mCall setGifts(ArrayList<PobModel> gifts) {
        this.gifts = gifts;
        return this;
    }

    public mCall setAttachments(ArrayList<String> attachments) {
        this.attachments = attachments;
        return this;
    }
}
