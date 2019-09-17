package Bill.NewOrder;

import java.io.Serializable;

import cbomobilereporting.cbo.com.cboorder.Model.mItem;

public class mBillItem  extends  mItem implements Serializable {
    private String Id = "0";
    private  String Name="";
    private  String SGST_PERCENT="0";
    private  String CGST_PERCENT="0";
    private  Double Rate =70.0;
    private  Double SGSTAmt =0.0;
    private  Double CGSTAmt =0.0;

    private Double TotAmt = 0.0D;

    private String BATCH_NO="0";
    private String MFG_DATE="0";
    private String EXP_DATE="0";
    private String PACK="0";
    private String MRP_RATE="0";
    private String SALE_RATE="0";
    private Double Qty = 0.0;


    private mBillItem_Batch mBillItem_batch;

    public mBillItem( ) {

    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getSGST_PERCENT() {
        return SGST_PERCENT;
    }

    public String getCGST_PERCENT() {
        return CGST_PERCENT;
    }

    public Double getRate() {
        return Rate;
    }

    public Double getQty() {
        return Qty;
   }
    public mBillItem setQty(Double qty) {
        this.Qty = qty;
        this.setAmt(qty * this.getRate());
        this.CalculateTotalAmount();
       // this.setFreeQty(this.calculateFreeQty(qty));

        return this;
    }
    public void CalculateTotalAmount() {
       //this.CalculateNetAmount();
      // this.calculateTax();
       // Double Amt = this.getNetAmt();


        this.TotAmt = getAmt() + this.SGSTAmt + this.CGSTAmt;
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

    public String getMRP_RATE() {
        return MRP_RATE;
    }

    public String getSALE_RATE() {
        return SALE_RATE;
    }


    public mBillItem setId(String id) {
        Id = id;return this;
    }

    public mBillItem setName(String name) {
        Name = name;return this;
    }

    public mBillItem setSGST_PERCENT(String SGST_PERCENT) {
        this.SGST_PERCENT = SGST_PERCENT;return this;
    }

    public mBillItem setCGST_PERCENT(String CGST_PERCENT) {
        this.CGST_PERCENT = CGST_PERCENT;return this;
    }

   /* public mBillItem setQty(Double qty) {
        Qty = qty;
        return  this;
    }
*/


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

    public void setMRP_RATE(String MRP_RATE) {
        this.MRP_RATE = MRP_RATE;
    }

    public void setSALE_RATE(String SALE_RATE) {
        this.SALE_RATE = SALE_RATE;
    }

    public void setmBillItem_batch(mBillItem_Batch mBillItem_batch) {
        this.mBillItem_batch = mBillItem_batch;
    }


}
