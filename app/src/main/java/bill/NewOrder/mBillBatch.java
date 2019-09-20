package bill.NewOrder;

public class mBillBatch {

    private String ITEM_ID="0";
    private String BATCH_ID="0";
    private String BATCH_NO="0";
    private String MFG_DATE="0";
    private String EXP_DATE="0";
    private String PACK="0";
    private Double MRP_RATE=0.0;
    private Double SALE_RATE=0.0;



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
}
