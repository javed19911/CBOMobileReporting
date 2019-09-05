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
    private String Title ;
    private String Dcr_id ;
    private String Dcr_date ;
    private String id = "0";
    private String srno = "0";
    private String name = "";
    private String Battery;
    private String time ;
    private Boolean workwithreqd = false;
    private ArrayList<Dcr_Workwith_Model> workwiths = new ArrayList<>();
    private String remark="";
    private Boolean remarkReqd = false;
    private Boolean remarkMandatory = false;
    private Boolean itemReqd = true;
    private ArrayList<PobModel> POBs = new ArrayList<>();
    private Boolean giftReqd = true;
    private ArrayList<PobModel> gifts = new ArrayList<>();
    private String LOC_EXTRA ;
    private String Ref_latlong="0.0,0.0";
    private String LatLong="0.0,0.0";
    private String lastVisited = "";
    private ArrayList<String> attachments = new ArrayList<>();

    //getter

    public String getTitle() {
        return Title;
    }

    public String getId() {
        return id;
    }

    public String getSrno() {
        return srno;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDcr_id() {
        return Dcr_id;
    }

    public String getDcr_date() {
        return Dcr_date;
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

    public Boolean IsRemarkReqd() {
        return remarkReqd;
    }

    public Boolean IsRemarkMandatory() {
        return remarkMandatory;
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

    public String getRefLatLong() {
        return Ref_latlong;
    }

    public String getCallLatLong() {
        return LatLong;
    }

    public String getLastVisited() {
        return lastVisited;
    }

    public String getLOC_EXTRA() {
        return LOC_EXTRA;
    }

    public String getBattery() {
        return Battery;
    }


    //setter

    public mCall(String title) {
        Title = title;
    }

    public mCall setId(String id) {
        this.id = id;
        return this;
    }


    public mCall setSrno(String srno) {
        this.srno = srno;
        return this;
    }


    public mCall setName(String name) {
        this.name = name;
        return this;
    }


    public mCall setTime(String time) {
        this.time = time;
        return this;
    }

    public mCall setDcr_id(String dcr_id) {
        Dcr_id = dcr_id;
        return this;
    }

    public mCall setDcr_date(String dcr_date) {
        Dcr_date = dcr_date;
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

    public mCall setRemarkReqd(Boolean RemarkReqd) {
        this.remarkReqd = RemarkReqd;
        return this;
    }

    public mCall setRemarkMandatory(Boolean remarkMandatory) {
        this.remarkMandatory = remarkMandatory;
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

    public mCall setRefLatLong(String refLatLong) {
        this.Ref_latlong = refLatLong;
        return this;
    }

    public mCall setCallLatLong(String callLatLong) {
        this.LatLong = callLatLong;
        return this;
    }

    public mCall setLastVisited(String lastVisited) {
        this.lastVisited = lastVisited;
        return this;
    }

    public mCall setLOC_EXTRA(String LOC_EXTRA) {
        this.LOC_EXTRA = LOC_EXTRA;
        return this;
    }

    public mCall setBattery(String battery) {
        Battery = battery;
        return this;
    }
}
