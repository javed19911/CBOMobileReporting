package bill.NewOrder;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;

public class mBillBatch {

    private String ITEM_ID="0";
    private String BATCH_ID="0";
    private String BATCH_NO="0";
    private String MFG_DATE="0";
    private String EXP_DATE="0";
    private String PACK="0";
    private Double MRP_RATE=0.0;
    private Double SALE_RATE=0.0;
    private Double STOCK = 0.0;
    private mDeal Deal = new mDeal();
    private mDiscount ManualDiscount = new mDiscount("Manual Discount");
    private mDiscount MangerDiscount = new mDiscount("Manager Discount");
    private ArrayList<mDiscount> MiscDiscount = new ArrayList();



    ///setter

    public String getITEM_ID() {
        return ITEM_ID;
    }

    public String getBATCH_ID() {
        return BATCH_ID;
    }

    public String getBATCH_NO() {
        return BATCH_NO;
    }

    public String getMFG_DATE() {
        return MFG_DATE;
    }

    public String getEXP_DATE() {
        return EXP_DATE;
    }

    public String getPACK() {
        return PACK;
    }

    public Double getMRP_RATE() {
        return MRP_RATE;
    }

    public Double getSALE_RATE() {
        return SALE_RATE;
    }

    public Double getSTOCK() {
        return STOCK;
    }

    public mDeal getDeal() {
        return this.Deal;
    }


    public mDiscount getMangerDiscount() {
        return this.MangerDiscount;
    }

    public ArrayList<mDiscount> getMiscDiscount() {
        return this.MiscDiscount;
    }

    public mDiscount getManualDiscount() {
        return this.ManualDiscount;
    }

    ///setter

    public mBillBatch setITEM_ID(String ITEM_ID) {
        this.ITEM_ID = ITEM_ID;
        return  this;
    }

    public mBillBatch setBATCH_ID(String BATCH_ID) {
        this.BATCH_ID = BATCH_ID;
        return  this;
    }

    public mBillBatch setBATCH_NO(String BATCH_NO) {
        this.BATCH_NO = BATCH_NO;
        return  this;
    }

    public mBillBatch setMFG_DATE(String MFG_DATE) {
        this.MFG_DATE = MFG_DATE;
        return  this;
    }

    public mBillBatch setEXP_DATE(String EXP_DATE) {
        this.EXP_DATE = EXP_DATE;
        return  this;
    }

    public mBillBatch setPACK(String PACK) {
        this.PACK = PACK;
        return  this;
    }

    public mBillBatch setMRP_RATE(Double MRP_RATE) {
        this.MRP_RATE = MRP_RATE;
        return  this;
    }

    public mBillBatch setSALE_RATE(Double SALE_RATE) {
        this.SALE_RATE = SALE_RATE;
        return  this;
    }

    public mBillBatch setSTOCK(Double STOCK) {
        this.STOCK = STOCK;
        return  this;
    }

    public mBillBatch setDeal(mDeal deal) {
        this.Deal = deal;
        return this;
    }

    public mBillBatch setMangerDiscount(mDiscount mangerDiscount) {
        this.MangerDiscount = mangerDiscount;

        return this;
    }

    public mBillBatch setMiscDiscount(ArrayList<mDiscount> miscDiscount) {
        this.MiscDiscount = miscDiscount;
        return this;
    }

    public mBillBatch setManualDiscount(mDiscount manualDiscount) {
        this.ManualDiscount = manualDiscount;
        return this;
    }
}
