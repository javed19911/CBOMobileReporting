package Bill.NewOrder;

public class mBillItem_Batch {

    private String ITEM_ID="0";
    private String BATCH_ID="0";
    private String BATCH_NO="0";
    private String MFG_DATE="0";
    private String EXP_DATE="0";
    private String PACK="0";
    private String MRP_RATE="0";
    private String SALE_RATE="0";


    public mBillItem_Batch( ) {

    }

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

    public String getMRP_RATE() {
        return MRP_RATE;
    }

    public String getSALE_RATE() {
        return SALE_RATE;
    }

    public mBillItem_Batch setITEM_ID(String ITEM_ID) {
        this.ITEM_ID = ITEM_ID;return  this;
    }

    public mBillItem_Batch setBATCH_ID(String BATCH_ID) {
        this.BATCH_ID = BATCH_ID;return  this;
    }

    public mBillItem_Batch setBATCH_NO(String BATCH_NO) {
        this.BATCH_NO = BATCH_NO;return  this;
    }

    public mBillItem_Batch setMFG_DATE(String MFG_DATE) {
        this.MFG_DATE = MFG_DATE;return  this;
    }

    public mBillItem_Batch setEXP_DATE(String EXP_DATE) {
        this.EXP_DATE = EXP_DATE;return  this;
    }

    public mBillItem_Batch setPACK(String PACK) {
        this.PACK = PACK;return  this;
    }

    public mBillItem_Batch setMRP_RATE(String MRP_RATE) {
        this.MRP_RATE = MRP_RATE;return  this;
    }

    public mBillItem_Batch setSALE_RATE(String SALE_RATE) {
        this.SALE_RATE = SALE_RATE;return  this;
    }
}
