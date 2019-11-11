package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending;

import java.io.Serializable;
import java.util.ArrayList;

public class mChemist implements Serializable {

    private int ID = 0;
    private String NAME = "";
    private int IS_SEND = 0;
    private mStockist selectedStokist = null;
    private ArrayList<mStockist> stockists = new ArrayList<>();
    private String itemId;
    private String QTY;
    private String rate;
    private String Remark;
    private String amount;

    public int getID() {
        return ID;
    }

    public mChemist setID(int ID) {
        this.ID = ID;
        return this;
    }

    public String getNAME() {
        return NAME;
    }

    public mChemist setNAME(String NAME) {
        this.NAME = NAME;
        return this;
    }

    public ArrayList<mStockist> getStockists() {
        return stockists;
    }

    public mChemist setStockists(ArrayList<mStockist> stockists) {
        this.stockists = stockists;
        return this;
    }


    public mStockist getSelectedStokist() {
        return selectedStokist;
    }

    public mChemist setSelectedStokist(mStockist selectedStokist) {
        this.selectedStokist = selectedStokist;
        return this;
    }

    public int getIS_SEND() {
        return IS_SEND;
    }

    public mChemist setIS_SEND(int IS_SEND) {
        this.IS_SEND = IS_SEND;
        return this;
    }

    public int isIS_SEND() {
        return IS_SEND;
    }

    public String getItemId() {
        return itemId;
    }

    public mChemist setItemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getRate() {
        return rate;
    }

    public mChemist setRate(String rate) {
        this.rate = rate;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public mChemist setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getQTY() {
        return QTY;
    }

    public mChemist setQTY(String QTY) {
        this.QTY = QTY;
        return this;
    }

    public String getRemark() {
        return Remark;
    }

    public mChemist setRemark(String remark) {
        Remark = remark;
        return this;
    }

}
