package com.cbo.cbomobilereporting.databaseHelper.Call;

import java.util.ArrayList;

/**
 * Created by cboios on 23/01/19.
 */

public abstract class mCall {
    private String Title = null;
    private String srno = "0";
    private String Id = "-1";
    private String Name ;
    private String Area ;
    private String Battery;

    private String Remark ;
    private String workwith ;

    private String time ;
    private String Dcr_id ;
    private String Dcr_date ;


    private String POBAmt ;
    private String sample_id_Arr ;
    private String sample_name_Arr;
    private String sample_qty_Arr;
    private String sample_pob_Arr ;
    private String sample_rate_Arr ;


    private String gift_id_Arr;
    private String gift_name_Arr ;
    private String gift_qty_Arr;



    private String LOC_EXTRA ;
    private String Ref_latlong ;
    private String LatLong ;
    private ArrayList<String > attachment = null;

    private String timeSpent;


    public mCall(String title) {
        Title = title;
    }

    ///getters
    public String getTitle() {
        return Title;
    }

    public String getSrno() {
        return srno;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getArea() {
        return Area;
    }

    public String getRemark() {
        return Remark;
    }

    public String getWorkwith() {
        return workwith;
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



    public String getSample_id_Arr() {
        return sample_id_Arr;
    }

    public String getSample_name_Arr() {
        return sample_name_Arr;
    }

    public String getSample_qty_Arr() {
        return sample_qty_Arr;
    }

    public String getSample_pob_Arr() {
        return sample_pob_Arr;
    }



    public String getSample_rate_Arr() {
        return sample_rate_Arr;
    }



    public String getGift_id_Arr() {
        return gift_id_Arr;
    }

    public String getGift_name_Arr() {
        return gift_name_Arr;
    }

    public String getGift_qty_Arr() {
        return gift_qty_Arr;
    }



    public String getLOC_EXTRA() {
        return LOC_EXTRA;
    }

    public String getRef_latlong() {
        return Ref_latlong;
    }

    public ArrayList<String> getAttachment() {
        return attachment;
    }

    public String getLatLong() {
        return LatLong;
    }

    public String getPOBAmt() {
        return POBAmt;
    }

    public String getBattery() {
        return Battery;
    }

    public String getTimeSpent() {
        return timeSpent;
    }


    ///setters


    public mCall setTitle(String title) {
        Title = title;
        return this;
    }

    public mCall setSrno(String srno) {
        this.srno = srno;
        return this;
    }

    public mCall setId(String id) {
        Id = id;
        return this;
    }

    public mCall setName(String name) {
        Name = name;
        return this;
    }

    public mCall setArea(String area) {
        Area = area;
        return this;
    }

    public mCall setRemark(String remark) {
        Remark = remark;
        return this;
    }

    public mCall setWorkwith(String workwith) {
        this.workwith = workwith;
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



    public mCall setSample_id_Arr(String sample_id_Arr) {
        this.sample_id_Arr = sample_id_Arr;
        return this;
    }

    public mCall setSample_name_Arr(String sample_name_Arr) {
        this.sample_name_Arr = sample_name_Arr;
        return this;
    }

    public mCall setSample_qty_Arr(String sample_qty_Arr) {
        this.sample_qty_Arr = sample_qty_Arr;
        return this;
    }

    public mCall setSample_pob_Arr(String sample_pob_Arr) {
        this.sample_pob_Arr = sample_pob_Arr;
        return this;
    }
    public mCall setSample_rate_Arr(String sample_rate_Arr) {
        this.sample_rate_Arr = sample_rate_Arr;
        return this;
    }

    public mCall setGift_id_Arr(String gift_id_Arr) {
        this.gift_id_Arr = gift_id_Arr;
        return this;
    }

    public mCall setGift_name_Arr(String gift_name_Arr) {
        this.gift_name_Arr = gift_name_Arr;
        return this;
    }

    public mCall setGift_qty_Arr(String gift_qty_Arr) {
        this.gift_qty_Arr = gift_qty_Arr;
        return this;
    }

    public mCall setLOC_EXTRA(String LOC_EXTRA) {
        this.LOC_EXTRA = LOC_EXTRA;
        return this;
    }

    public mCall setRef_latlong(String ref_latlong) {
        Ref_latlong = ref_latlong;
        return this;
    }

    public mCall setAttachment(ArrayList<String> attachment) {
        this.attachment = attachment;
        return this;
    }

    public mCall setLatLong(String latLong) {
        LatLong = latLong;
        return this;
    }

    public mCall setPOBAmt(String POBAmt) {
        this.POBAmt = POBAmt;
        return this;
    }

    public mCall setBattery(String battery) {
        Battery = battery;
        return this;
    }

    public mCall setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
        return this;
    }

}
