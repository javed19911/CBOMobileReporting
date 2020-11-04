package com.cbo.cbomobilereporting.ui_new.dcr_activities.lead;

import com.cbo.cbomobilereporting.ui_new.dcr_activities.Enum.CallType;

import java.io.Serializable;
import java.util.ArrayList;

import utils.adapterutils.PobModel;

/**
 * Created by cboios on 11/03/19.
 */

public class SamplePOBBuilder implements Serializable{
    public enum ItemType {
        POB,
        GIFT,
        LEAD;
    }



    private ItemType type = ItemType.POB;
    private CallType callType = CallType.DOCTOR;
    private String title = "Select Items....";
    private String lookForId ="";
    private ArrayList<PobModel>  items = new ArrayList<>();

    //getter


    public ItemType getType() {
        return type;
    }

    public CallType getCallType() {
        return callType;
    }

    public String getTitle() {
        return title;
    }
    public String getLookForId() {
        return lookForId;
    }

    public ArrayList<PobModel> getItems() {
        return items;
    }

    //setter


    public SamplePOBBuilder setType(ItemType type) {
        this.type = type;
        return this;
    }


    public SamplePOBBuilder setTitle(String title) {
        this.title = title;
        return this;
    }
    public SamplePOBBuilder setLookForId(String lookForId) {
        this.lookForId = lookForId;
        return this;
    }

    public SamplePOBBuilder setItems(ArrayList<PobModel> items) {
        this.items = items;
        return this;
    }

    public SamplePOBBuilder setCallType(CallType callType) {
        this.callType = callType;
        return this;
    }
}
