package com.cbo.cbomobilereporting.ui_new.dcr_activities.WorkWith;

import com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils.CallBuilder;

import java.io.Serializable;
import java.util.ArrayList;

import utils.adapterutils.Dcr_Workwith_Model;

/**
 * Created by cboios on 11/03/19.
 */

public class WorkWithBuilder implements Serializable{
    public enum WorkWithType{
        DCR("DCR"),
        Doctor("D"),
        DoctorRiminder("Dr"),
        Chemist("C"),
        Stockist("S"),
        Dairy("DA"),
        Poultry("P");

        private String value;
        WorkWithType(String value){
            this.value = value;
        }
        public String getValue(){
            return value;
        }
    }



    private WorkWithType type = WorkWithType.Doctor;
    private Boolean showIndedendentWorkWith = false;
    private String title = "Select Party....";
    private String lookForId ="";
    private ArrayList<Dcr_Workwith_Model>  workwithModels = new ArrayList<>();

    //getter


    public WorkWithType getType() {
        return type;
    }

    public Boolean getShowIndedendentWorkWith() {
        return showIndedendentWorkWith;
    }

    public String getTitle() {
        return title;
    }
    public String getLookForId() {
        return lookForId;
    }

    public ArrayList<Dcr_Workwith_Model> getWorkwithModels() {
        return workwithModels;
    }

    //setter


    public WorkWithBuilder setType(WorkWithType type) {
        this.type = type;
        return this;
    }

    public WorkWithBuilder setShowIndedendentWorkWith(Boolean showIndedendentWorkWith) {
        this.showIndedendentWorkWith = showIndedendentWorkWith;
        return this;
    }

    public WorkWithBuilder setTitle(String title) {
        this.title = title;
        return this;
    }
    public WorkWithBuilder setLookForId(String lookForId) {
        this.lookForId = lookForId;
        return this;
    }

    public WorkWithBuilder setWorkwithModels(ArrayList<Dcr_Workwith_Model> workwithModels) {
        this.workwithModels = workwithModels;
        return this;
    }
}
