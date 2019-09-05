package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import com.cbo.cbomobilereporting.MyCustumApplication;

import java.io.Serializable;
import java.util.ArrayList;

import utils_new.Custom_Variables_And_Method;

public class mExpense implements Serializable {
    private String DA_TYPE;
    private String DA_TYPE_Display = "";
    private String FARE = "";
    private String ACTUALFAREYN;
    private String ACTUALDA_FAREYN = "";
    private String ROUTE_CLASS ="";
    private String ACTUALFAREYN_MANDATORY = "";
    private Double ACTUALFARE_MAXAMT = 0D;
    private String DA_TYPE_MANUALYN ;
    private String TA_TYPE_MANUALYN ;
    private String MANUAL_TAYN_MANDATORY ;
    private String MANUAL_TAYN_KM ="";
    private String MANUAL_TAYN_STATION ="";
    private String DISTANCE_TYPE_MANUALYN = "";
    private String MANUAL_DAYN ;
    private mDA selected_da = new mDA();
    private mDistance selected_distance = new mDistance();
    private ArrayList<mExpHead> expHeads = new ArrayList();
    private ArrayList<mDA> DAs = new ArrayList();
    private ArrayList<mDistance> distances = new ArrayList();
    private ArrayList<mOthExpense> othExpenses = new ArrayList();
    private ArrayList<mOthExpense> TA_Expenses = new ArrayList();
    private ArrayList<mOthExpense> DA_Expenses = new ArrayList();
    private ArrayList<mRate> rates = new ArrayList();
    private Double DA_Amt;
    private Double TA_Amt;
    private Double Rate =0D;
    private Double Km = 0D;
    private String Station ="";
    private String Attachment = "";


    ///getter


    public String getDA_TYPE() {
        if (DA_TYPE == null){
            DA_TYPE = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DA_TYPE","-");
        }
        return DA_TYPE;
    }

    public String getDA_TYPE_Display() {
        return DA_TYPE_Display;
    }

    public mDA getSelectedDA(){
        return selected_da;
    }

    public mDistance getSelectedDistance(){
        return selected_distance;
    }

    public String getFARE() {
        return FARE;
    }

    public String getACTUALFAREYN() {
        if (ACTUALFAREYN == null){
            ACTUALFAREYN = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("ACTUALFAREYN","");
        }
        return ACTUALFAREYN;
    }

    public String getACTUALDA_FAREYN() {
        return ACTUALDA_FAREYN;
    }

    public String getROUTE_CLASS() {
        return ROUTE_CLASS;
    }

    public String getACTUALFAREYN_MANDATORY() {
        return ACTUALFAREYN_MANDATORY;
    }

    public Double getACTUALFARE_MAXAMT() {
        return ACTUALFARE_MAXAMT;
    }

    public String getDA_TYPE_MANUALYN() {
        if (DA_TYPE_MANUALYN == null){
            DA_TYPE_MANUALYN = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("DA_TYPE_MANUALYN","");
        }
        return DA_TYPE_MANUALYN;
    }

    public String getTA_TYPE_MANUALYN() {
        if (TA_TYPE_MANUALYN == null){
            TA_TYPE_MANUALYN = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("TA_TYPE_MANUALYN","");
        }
        return TA_TYPE_MANUALYN;
    }

    public String getMANUAL_TAYN_MANDATORY(){

        if (MANUAL_TAYN_MANDATORY == null){
            MANUAL_TAYN_MANDATORY = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("MANUAL_TAYN_MANDATORY","");
        }
        return MANUAL_TAYN_MANDATORY;
    }

    public String getMANUAL_TAYN_KM() {
        return MANUAL_TAYN_KM;
    }

    public String getMANUAL_TAYN_STATION() {
        return MANUAL_TAYN_STATION;
    }

    public String getDISTANCE_TYPE_MANUALYN() {
        return DISTANCE_TYPE_MANUALYN;
    }

    public ArrayList<mDA> getDAs() {
        return DAs;
    }

    public ArrayList<mDistance> getDistances() {
        return distances;
    }

    public ArrayList<mExpHead> getExpHeads() {
        return expHeads;
    }

    public ArrayList<mOthExpense> getOthExpenses() {
        return othExpenses;
    }

    public ArrayList<mOthExpense> getTA_Expenses() {
        return TA_Expenses;
    }

    public ArrayList<mOthExpense> getDA_Expenses() {
        return DA_Expenses;
    }

    public ArrayList<mRate> getRates() {
        return rates;
    }

    public Double getRate(){return Rate;}

    public Double getKm(){return Km;}

    public Double getDA_Amt() {
        if (DA_Amt == null){
            DA_Amt = Double.parseDouble(MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("da_val","0"));
        }
        return DA_Amt;
    }

    public Double getTA_Amt() {
        if (TA_Amt == null){
            TA_Amt = Double.parseDouble(MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("distance_val","0"));
        }
        return TA_Amt;
    }

    public String getStation() {
        return Station;
    }

    public String getMANUAL_DAYN(){
        if (MANUAL_DAYN == null){
            MANUAL_DAYN = MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("MANUAL_DAYN","");
        }
        return MANUAL_DAYN;
    }

    public String getAttachment() {
        return Attachment;
    }

    /// setter


    public mExpense setDA_TYPE(String DA_TYPE) {
        this.DA_TYPE = DA_TYPE;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("DA_TYPE",DA_TYPE);
        return this;
    }

    public mExpense setDA_TYPE_Display(String DA_TYPE_Display) {
        this.DA_TYPE_Display = DA_TYPE_Display;
        return this;
    }

    public mExpense setFARE(String FARE) {
        this.FARE = FARE;
        return this;
    }

    public mExpense setACTUALFAREYN(String ACTUALFAREYN) {
        this.ACTUALFAREYN = ACTUALFAREYN;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("ACTUALFAREYN",ACTUALFAREYN);
        return this;
    }

    public mExpense setACTUALDA_FAREYN(String ACTUALDA_FAREYN) {
        this.ACTUALDA_FAREYN = ACTUALDA_FAREYN;
        return this;
    }

    public mExpense setROUTE_CLASS(String ROUTE_CLASS) {
        this.ROUTE_CLASS = ROUTE_CLASS;
        return this;
    }

    public mExpense setACTUALFAREYN_MANDATORY(String ACTUALFAREYN_MANDATORY) {
        this.ACTUALFAREYN_MANDATORY = ACTUALFAREYN_MANDATORY;
        return this;
    }

    public mExpense setACTUALFARE_MAXAMT(Double ACTUALFARE_MAXAMT) {
        this.ACTUALFARE_MAXAMT = ACTUALFARE_MAXAMT;
        return this;
    }

    public mExpense setDA_TYPE_MANUALYN(String DA_TYPE_MANUALYN) {
        this.DA_TYPE_MANUALYN = DA_TYPE_MANUALYN;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("DA_TYPE_MANUALYN",DA_TYPE_MANUALYN);
        return this;
    }

    public mExpense setTA_TYPE_MANUALYN(String TA_TYPE_MANUALYN) {
        this.TA_TYPE_MANUALYN = TA_TYPE_MANUALYN;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("TA_TYPE_MANUALYN",TA_TYPE_MANUALYN);
        return this;
    }

    public mExpense setMANUAL_TAYN_MANDATORY(String MANUAL_TAYN_MANDATORY){
        this.MANUAL_TAYN_MANDATORY = MANUAL_TAYN_MANDATORY;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("MANUAL_TAYN_MANDATORY",MANUAL_TAYN_MANDATORY);
        return this;
    }

    public mExpense setMANUAL_TAYN_KM(String MANUAL_TAYN_KM) {
        this.MANUAL_TAYN_KM = MANUAL_TAYN_KM;
        return this;
    }

    public mExpense setMANUAL_TAYN_STATION(String MANUAL_TAYN_STATION) {
        this.MANUAL_TAYN_STATION = MANUAL_TAYN_STATION;
        return this;
    }

    public mExpense setDISTANCE_TYPE_MANUALYN(String DISTANCE_TYPE_MANUALYN) {
        this.DISTANCE_TYPE_MANUALYN = DISTANCE_TYPE_MANUALYN;
        return this;
    }
    public mExpense setDAs(ArrayList<mDA> DAs) {
        this.DAs = DAs;
        return this;
    }

    public void setDistances(ArrayList<mDistance> distances) {
        this.distances = distances;
    }

    public mExpense setExpHeads(ArrayList<mExpHead> expHeads) {
        this.expHeads = expHeads;
        return this;
    }

    public mExpense setOthExpenses(ArrayList<mOthExpense> othExpenses) {
        this.othExpenses = othExpenses;
        return this;
    }

    public mExpense setTA_Expenses(ArrayList<mOthExpense> TA_Expenses) {
        this.TA_Expenses = TA_Expenses;
        return this;
    }

    public mExpense setDA_Expenses(ArrayList<mOthExpense> DA_Expenses) {
        this.DA_Expenses = DA_Expenses;
        return this;
    }

    public mExpense setDA_Amt(Double DA_Amt) {
        this.DA_Amt = DA_Amt;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("da_val",""+DA_Amt);
        return this;
    }

    public mExpense setDA(mDA da){
        selected_da = da;
        return this;
    }

    public mExpense setDistance(mDistance distance){
        selected_distance = distance;
        return this;
    }
    public mExpense setRates(ArrayList<mRate> rates) {
        this.rates = rates;
        return this;
    }

    public mExpense setTA_Amt(Double TA_Amt) {
        this.TA_Amt = TA_Amt;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("distance_val",""+TA_Amt);
        return this;
    }

    public mExpense setRate(Double rate) {
        this.Rate = rate;
        return this;
    }

    public mExpense setKm(Double km) {
        this.Km = km;
        return this;
    }

    public mExpense setMANUAL_DAYN(String MANUAL_DAYN) {
        this.MANUAL_DAYN = MANUAL_DAYN;
        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE("MANUAL_DAYN",MANUAL_DAYN);
        return this;
    }

    public mExpense setStation(String station) {
        this.Station = station;
        return this;
    }

    public mExpense setAttachment(String attachment) {
        Attachment = attachment;
        return this;
    }

    public mRate getRateFor(Double km){
        mRate rate = new mRate();

        for (mRate rate1 : rates){
            if (km>=rate1.getFKm() && km<= rate1.getTKm()){
                return rate1;
            }
        }

        return rate;
    }
}
