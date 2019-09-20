package bill.NewOrder;

import java.io.Serializable;

import cbomobilereporting.cbo.com.cboorder.Model.mItem;

public class mBillItem  implements Serializable {
    private String Id = "0";
    private  String Name="";
    private String BATCH_NO="";
    private String MFG_DATE="";
    private String EXP_DATE="";
    private String PACK="";
    private Double MRP_RATE=0.0D;
    private Double SALE_RATE=0.0D;
    private Double Qty = 0.0;
    private  Double SGST_PERCENT=0.0;
    private  Double CGST_PERCENT=0.0;
    private  Double SGSTAmt =0.0;
    private  Double CGSTAmt =0.0;
    private Double Amt = 0.0D;
    private Double TotAmt = 0.0D;





    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public Double getSGST_PERCENT() {
        return SGST_PERCENT;
    }

    public Double getCGST_PERCENT() {
        return CGST_PERCENT;
    }

    public Double getQty() {
        return Qty;
   }

    public Double getAmt() {
        return this.Amt;
    }

    public mBillItem setQty(Double qty) {
        this.Qty = qty;
        this.setAmt(qty * getSALE_RATE());
        this.CalculateTotalAmount();

        return this;
    }

    public void CalculateTotalAmount() {
        CGSTAmt = getAmt() * (getCGST_PERCENT()*0.01);
        SGSTAmt = getAmt() * (getSGST_PERCENT()*0.01);

        this.TotAmt = getAmt() + this.SGSTAmt + this.CGSTAmt;
    }

    public Double getTotAmt() {
        return this.TotAmt;
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


    public mBillItem setId(String id) {
        Id = id;return this;
    }

    public mBillItem setName(String name) {
        Name = name;
        return this;
    }

    public mBillItem setSGST_PERCENT(Double SGST_PERCENT) {
        this.SGST_PERCENT = SGST_PERCENT;
        return this;
    }

    public mBillItem setCGST_PERCENT(Double CGST_PERCENT) {
        this.CGST_PERCENT = CGST_PERCENT;
        return this;
    }

    public mBillItem setAmt(Double amt) {
        this.Amt = amt;
        return this;
    }

    public void setBATCH_NO(String BATCH_NO) {
        this.BATCH_NO = BATCH_NO;
    }

    public void setMFG_DATE(String MFG_DATE) {
        this.MFG_DATE = MFG_DATE;
    }

    public void setEXP_DATE(String EXP_DATE) {
        this.EXP_DATE = EXP_DATE;
    }

    public void setPACK(String PACK) {
        this.PACK = PACK;
    }

    public void setMRP_RATE(Double MRP_RATE) {
        this.MRP_RATE = MRP_RATE;
    }

    public void setSALE_RATE(Double SALE_RATE) {
        this.SALE_RATE = SALE_RATE;
    }




}
