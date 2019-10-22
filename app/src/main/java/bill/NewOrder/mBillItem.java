package bill.NewOrder;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;

public class mBillItem  implements Serializable {
    private String Id = "0";
    private  String Name="";
    private String BATCH_ID="0";
    private String BATCH_NO="";
    private String MFG_DATE="";
    private String EXP_DATE="";
    private String PACK="";
    private Double MRP_RATE=0.0D;
    private Double SALE_RATE=0.0D;
    private Double Qty = 0.0;
    private Double FreeQty = 0.0D;
    private Double NetAmt = 0.0D;
    private Double Stock =-1.0;
    private mTax GST =  new mTax(eTax.NONE);
    private  Double SGSTAmt =0.0;
    private  Double CGSTAmt =0.0;
    private mDeal Deal = new mDeal();
    private Double Amt = 0.0D;
    private mDiscount ManualDiscount = new mDiscount("Manual Discount");
    private mDiscount MangerDiscount = new mDiscount("Manager Discount");
    private ArrayList<mDiscount> MiscDiscount = new ArrayList();
    private Double TotAmt = 0.0D;

    public mBillItem() {
    }

/// getter

    @PropertyName("Id")
    public String getId() {
        return Id;
    }

    @PropertyName("Name")
    public String getName() {
        return Name;
    }

    @PropertyName("Qty")
    public Double getQty() {
        return Qty;
   }

    @PropertyName("FreeQty")
    public Double getFreeQty() {
        return this.FreeQty;
    }

    @PropertyName("Amt")
    public Double getAmt() {
        return this.Amt;
    }

    @PropertyName("NetAmt")
    public Double getNetAmt() {
        return this.NetAmt;
    }

    @PropertyName("GST")
    public mTax getGST() {
        return this.GST;
    }

    @PropertyName("Deal")
    public mDeal getDeal() {
        return this.Deal;
    }

    public Double CalcutatePercent(Double x, Double percent) {
        return x * percent * 0.01D;
    }

    @PropertyName("CGSTAmt")
    public Double getCGSTAmt() {
        return this.CGSTAmt;
    }

    @PropertyName("SGSTAmt")
    public Double getSGSTAmt() {
        return this.SGSTAmt;
    }

    @PropertyName("TotAmt")
    public Double getTotAmt() {
        return this.TotAmt;
    }

    @PropertyName("BATCH_ID")
    public String getBATCH_ID() {
        return BATCH_ID;
    }

    @PropertyName("BATCH_NO")
    public String getBATCH_NO() {
        return BATCH_NO;
    }

    @PropertyName("Id")
    public String getMFG_DATE() {
        return MFG_DATE;
    }

    @PropertyName("EXP_DATE")
    public String getEXP_DATE() {
        return EXP_DATE;
    }

    @PropertyName("PACK")
    public String getPACK() {
        return PACK;
    }

    @PropertyName("MRP_RATE")
    public Double getMRP_RATE() {
        return MRP_RATE;
    }

    @PropertyName("SALE_RATE")
    public Double getSALE_RATE() {
        return SALE_RATE;
    }

    @PropertyName("MangerDiscount")
    public mDiscount getMangerDiscount() {
        return this.MangerDiscount;
    }

    @PropertyName("MiscDiscount")
    public ArrayList<mDiscount> getMiscDiscount() {
        return this.MiscDiscount;
    }

    @PropertyName("ManualDiscount")
    public mDiscount getManualDiscount() {
        return this.ManualDiscount;
    }

    @PropertyName("Stock")
    public Double getStock(){
        return Stock;
    }

    public String getDiscountStr() {
        StringBuilder discounts = new StringBuilder();
        int count = 0;

        for (mDiscount discount : getMiscDiscount()){
            if (count != 0){
                discounts.append(",");
            }
            discounts.append(discount.getPercent()).append("%");
            count++;
        }


       /* if (this.getNoOfDiscountAlowed() >= 5) {
            discounts.append(",").append(this.getMangerDiscount().getPercent()).append("%");
        }

        if (this.getNoOfDiscountAlowed() >= 6) {
            discounts.append(",").append(this.getManualDiscount().getPercent()).append("%");
        }*/

        return discounts.toString();
    }


    /// setter

    @PropertyName("Id")
    public mBillItem setId(String id) {
        Id = id;return this;
    }

    @PropertyName("Name")
    public mBillItem setName(String name) {
        Name = name;
        return this;
    }

    @PropertyName("GST")
    public mBillItem setGST(mTax GST) {
        this.GST = GST;
        return this;
    }

    @PropertyName("Amt")
    public mBillItem setAmt(Double amt) {
        this.Amt = amt;
        return this;
    }

    @PropertyName("BATCH_ID")
    public mBillItem setBATCH_ID(String BATCH_ID) {
        this.BATCH_ID = BATCH_ID;
        return this;
    }

    @PropertyName("BATCH_NO")
    public mBillItem setBATCH_NO(String BATCH_NO) {
        this.BATCH_NO = BATCH_NO;
        return this;
    }

    @PropertyName("MFG_DATE")
    public mBillItem setMFG_DATE(String MFG_DATE) {
        this.MFG_DATE = MFG_DATE;
        return this;
    }

    @PropertyName("EXP_DATE")
    public mBillItem setEXP_DATE(String EXP_DATE) {
        this.EXP_DATE = EXP_DATE;
        return this;
    }

    @PropertyName("PACK")
    public mBillItem setPACK(String PACK) {
        this.PACK = PACK;
        return this;
    }

    @PropertyName("MRP_RATE")
    public mBillItem setMRP_RATE(Double MRP_RATE) {
        this.MRP_RATE = MRP_RATE;
        return this;
    }

    @PropertyName("SALE_RATE")
    public mBillItem setSALE_RATE(Double SALE_RATE) {
        this.SALE_RATE = SALE_RATE;
        return this;
    }


    @PropertyName("Qty")
    public mBillItem setQty(Double qty) {
        this.Qty = qty;
        this.setAmt(qty * getSALE_RATE());
        this.CalculateTotalAmount();
        this.setFreeQty(this.calculateFreeQty(qty));
        return this;
    }

    @PropertyName("Stock")
    public mBillItem setStock(Double stock) {
        this.Stock = stock;
        return this;
    }

    @PropertyName("FreeQty")
    public mBillItem setFreeQty(Double freeQty) {
        this.FreeQty = freeQty;
        return this;
    }

    public Double calculateFreeQty(Double qty) {
        new DecimalFormat("0");
        Double FQty = 0.0D;
        switch(this.getDeal().getType()) {
            case All:
                FQty = qty / this.getDeal().getQty() * this.getDeal().getFreeQty();
                break;
            case full:
                FQty = 0.0D + (double)((int)(qty / this.getDeal().getQty())) * this.getDeal().getFreeQty();
                break;
            case Half:
                FQty = 0.0D + (double)((int)(qty / this.getDeal().getQty())) * (this.getDeal().getFreeQty() / 2.0D);
                break;
            case Exact:
                FQty = qty.equals(this.getDeal().getQty()) ? this.getDeal().getFreeQty() : 0.0D;
        }

        return FQty;
    }




    @PropertyName("Deal")
    public mBillItem setDeal(mDeal deal) {
        this.Deal = deal;
        return this;
    }

    @PropertyName("MangerDiscount")
    public mBillItem setMangerDiscount(mDiscount mangerDiscount) {
        this.MangerDiscount = mangerDiscount;
        this.CalculateTotalAmount();

        return this;
    }

    @PropertyName("MiscDiscount")
    public mBillItem setMiscDiscount(ArrayList<mDiscount> miscDiscount) {
        this.MiscDiscount = miscDiscount;
        this.CalculateTotalAmount();
        return this;
    }

    @PropertyName("ManualDiscount")
    public mBillItem setManualDiscount(mDiscount manualDiscount) {
        this.ManualDiscount = manualDiscount;
        this.CalculateTotalAmount();
        return this;
    }

    private void CalculateNetAmount() {
        Double Amt = this.getAmt();
        Amt = Amt * (1.0D - this.getManualDiscount().getPercent() * 0.01D);
        Amt = Amt * (1.0D - this.getMangerDiscount().getPercent() * 0.01D);

        for (mDiscount discount : getMiscDiscount()){
            Amt = Amt * (1.0D - discount.getPercent() * 0.01D);
        }

        this.NetAmt = Amt;
    }

    public void CalculateTotalAmount() {
        this.CalculateNetAmount();
        this.calculateTax();
        Double Amt = this.getNetAmt();
        Amt = Amt + this.SGSTAmt + this.CGSTAmt;
        this.TotAmt = Amt;
    }

    private void calculateTax() {
        this.SGSTAmt = this.CalcutatePercent(this.getNetAmt(), this.GST.getSGST());
        this.CGSTAmt = this.CalcutatePercent(this.getNetAmt(), this.GST.getCGST());
    }


}
