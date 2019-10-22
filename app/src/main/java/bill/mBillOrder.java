package bill;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import bill.NewOrder.mBillItem;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;

public class mBillOrder implements Serializable {

    private String Id = "0";
    private String PartyId;
    private String PartyName = "";
    private String DocId = "0";
    private String DocNo;
    private String DocDate;
    private Double Amt = 0.0D;
    private Double NetAmt = 0.0D;
    private String PayMode;
    private String Status = "V";
    private String GrNo;
    private String Transport;
    private String GrDate;
    private String BillNo;
    private String BillDate;
    private String BillAmt;
    private ArrayList<mBillItem> items = new ArrayList();
    private mTax GST;
    private Double CGSTAmt;
    private Double SGSTAmt;
    private Double TotAmt;
    private Double Round_amt = 0.0;
    private int BilledHO;
    private String Approved;
    private String Attachment;
    private String Remark;



    public mBillOrder() {
        this.GST = new mTax(eTax.NONE);
        this.CGSTAmt = 0.0D;
        this.SGSTAmt = 0.0D;
        this.TotAmt = 0.0D;
        this.BilledHO = 0;
        this.Approved = "N";
        this.Attachment = "";
    }


    @PropertyName("Id")
    public String getId() {
        return this.Id;
    }

    @PropertyName("Id")
    public mBillOrder setId(String id) {
        this.Id = id;
        return this;
    }

    @PropertyName("PartyId")
    public String getPartyId() {
        return this.PartyId;
    }

    @PropertyName("PartyId")
    public mBillOrder setPartyId(String partyId) {
        this.PartyId = partyId;
        return this;
    }

    @PropertyName("PartyName")
    public String getPartyName() {
        return this.PartyName;
    }

    @PropertyName("PartyName")
    public mBillOrder setPartyName(String partyName) {
        this.PartyName = partyName;
        return this;
    }

    @PropertyName("DocId")
    public String getDocId() {
        return this.DocId;
    }

    @PropertyName("DocId")
    public mBillOrder setDocId(String docId) {
        this.DocId = docId;
        return this;
    }

    @PropertyName("DocNo")
    public String getDocNo() {
        return this.DocNo;
    }

    @PropertyName("DocNo")
    public mBillOrder setDocNo(String docNo) {
        this.DocNo = docNo;
        return this;
    }

    @PropertyName("DocDate")
    public String getDocDate() {
        return this.DocDate;
    }

    @PropertyName("DocDate")
    public mBillOrder setDocDate(String docDate) {
        this.DocDate = docDate;
        return this;
    }

    @PropertyName("Amt")
    public Double getAmt() {
        return this.Amt;
    }

    @PropertyName("Amt")
    public mBillOrder setAmt(Double amt) {
        this.Amt = amt;
        return this;
    }

    @PropertyName("NetAmt")
    public Double getNetAmt() {
        return this.NetAmt;
    }

    @PropertyName("NetAmt")
    public mBillOrder setNetAmt(Double netAmt) {

        this.NetAmt = netAmt;
        return this;
    }

    @PropertyName("CGSTAmt")
    public Double getCGSTAmt() {
        return this.CGSTAmt;
    }

    @PropertyName("CGSTAmt")
    public mBillOrder setCGSTAmt(Double CGSTAmt) {
        this.CGSTAmt = CGSTAmt;
        return this;
    }

    @PropertyName("SGSTAmt")
    public Double getSGSTAmt() {
        return this.SGSTAmt;
    }

    @PropertyName("SGSTAmt")
    public mBillOrder setSGSTAmt(Double SGSTAmt) {
        this.SGSTAmt = SGSTAmt;
        return this;
    }

    @PropertyName("TotAmt")
    public Double getTotAmt() {
        return this.TotAmt;
    }

    @PropertyName("TotAmt")
    public mBillOrder setTotAmt(Double totAmt) {
        Round_amt = Math.round(totAmt) - totAmt;
        this.TotAmt = Math.round(totAmt) + 0.0;
        return this;
    }

    @PropertyName("Round_amt")
    public Double getRouAmt() {
        return this.Round_amt;
    }

    @PropertyName("PayMode")
    public String getPayMode() {
        return this.PayMode;
    }

    @PropertyName("PayMode")
    public mBillOrder setPayMode(String payMode) {
        this.PayMode = payMode;
        return this;
    }

    @PropertyName("Status")
    public String getStatus() {
        return this.Status;
    }

    @PropertyName("Status")
    public mBillOrder setStatus(String status) {
        this.Status = status;
        return this;
    }

    @PropertyName("GrNo")
    public String getGrNo() {
        return this.GrNo;
    }

    @PropertyName("GrNo")
    public mBillOrder setGrNo(String grNo) {
        this.GrNo = grNo;
        return this;
    }

    @PropertyName("Transport")
    public String getTransport() {
        return this.Transport;
    }

    @PropertyName("Transport")
    public mBillOrder setTransport(String transport) {
        this.Transport = transport;
        return this;
    }

    @PropertyName("GrDate")
    public String getGrDate() {
        return this.GrDate;
    }

    @PropertyName("GrDate")
    public mBillOrder setGrDate(String grDate) {
        this.GrDate = grDate;
        return this;
    }

    @PropertyName("BillNo")
    public String getBillNo() {
        return this.BillNo;
    }

    @PropertyName("BillNo")
    public mBillOrder setBillNo(String billNo) {
        this.BillNo = billNo;
        return this;
    }

    @PropertyName("BillDate")
    public String getBillDate() {
        return this.BillDate;
    }

    @PropertyName("BillDate")
    public mBillOrder setBillDate(String billDate) {
        this.BillDate = billDate;
        return this;
    }

    @PropertyName("BillAmt")
    public String getBillAmt() {
        return this.BillAmt;
    }

    @PropertyName("BillAmt")
    public mBillOrder setBillAmt(String billAmt) {
        this.BillAmt = billAmt;
        return this;
    }

    @PropertyName("items")
    public ArrayList<mBillItem> getItems() {
        return this.items;
    }

    @PropertyName("items")
    public mBillOrder setItems(ArrayList<mBillItem> items) {
        this.items = items;
        return this;
    }

    @PropertyName("BilledHO")
    public int getBilledHO() {
        return this.BilledHO;
    }

    @PropertyName("BilledHO")
    public mBillOrder setBilledHO(int billedHO) {
        this.BilledHO = billedHO;
        return this;
    }

    @PropertyName("Approved")
    public String getApproved() {
        return this.Approved;
    }

    @PropertyName("Approved")
    public mBillOrder setApproved(String approved) {
        this.Approved = approved;
        return this;
    }

    @PropertyName("Attachment")
    public String getAttachment() {
        return this.Attachment;
    }

    @PropertyName("Attachment")
    public mBillOrder setAttachment(String attachment) {
        this.Attachment = attachment;
        return this;
    }

    @PropertyName("Remark")
    public String getRemark() {
        return Remark;
    }


    public mBillOrder setAttachment(List<String> attachment) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        Iterator var4 = attachment.iterator();

        while (var4.hasNext()) {
            String file = (String) var4.next();
            if (count != 0) {
                sb.append("|^");
            }

            ++count;
            sb.append(file);
        }

        this.Attachment = sb.toString();
        return this;
    }

    public List<String> getAttachmentArr() {
        return this.getAttachment().isEmpty() ? new ArrayList() : new ArrayList(Arrays.asList(this.Attachment.split("\\|\\^")));
    }

    @PropertyName("GST")
    public mBillOrder setGST(mTax GST) {
        this.GST = GST;
        return this;
    }


    @PropertyName("Remark")
    public mBillOrder setRemark(String remark) {
        Remark = remark;
        return this;
    }
}
