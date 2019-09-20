package bill;

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
    private String Status = "";
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
    private int BilledHO;
    private String Approved;
    private String Attachment;

    public mBillOrder() {
        this.GST = new mTax(eTax.NONE);
        this.CGSTAmt = 0.0D;
        this.SGSTAmt = 0.0D;
        this.TotAmt = 0.0D;
        this.BilledHO = 0;
        this.Approved = "N";
        this.Attachment = "";
    }

    public String getId() {
        return this.Id;
    }

    public String getPartyId() {
        return this.PartyId;
    }

    public String getPartyName() {
        return this.PartyName;
    }

    public String getDocId() {
        return this.DocId;
    }

    public String getDocNo() {
        return this.DocNo;
    }

    public String getDocDate() {
        return this.DocDate;
    }

    public Double getAmt() {
        return this.Amt;
    }

    public Double getNetAmt() {
        return this.NetAmt;
    }

    public Double getCGSTAmt() {
        return this.CGSTAmt;
    }

    public Double getSGSTAmt() {
        return this.SGSTAmt;
    }

    public Double getTotAmt() {
        return this.TotAmt;
    }

    public String getPayMode() {
        return this.PayMode;
    }

    public String getStatus() {
        return this.Status;
    }

    public String getGrNo() {
        return this.GrNo;
    }

    public String getTransport() {
        return this.Transport;
    }

    public String getGrDate() {
        return this.GrDate;
    }

    public String getBillNo() {
        return this.BillNo;
    }

    public String getBillDate() {
        return this.BillDate;
    }

    public String getBillAmt() {
        return this.BillAmt;
    }

    public ArrayList<mBillItem> getItems() {
        return this.items;
    }

    public int getBilledHO() {
        return this.BilledHO;
    }

    public String getApproved() {
        return this.Approved;
    }

    public String getAttachment() {
        return this.Attachment;
    }

    public List<String> getAttachmentArr() {
        return this.getAttachment().isEmpty() ? new ArrayList() : new ArrayList(Arrays.asList(this.Attachment.split("\\|\\^")));
    }

    public mBillOrder setId(String id) {
        this.Id = id;
        return this;
    }

    public mBillOrder setPartyId(String partyId) {
        this.PartyId = partyId;
        return this;
    }

    public mBillOrder setPartyName(String partyName) {
        this.PartyName = partyName;
        return this;
    }

    public mBillOrder setDocId(String docId) {
        this.DocId = docId;
        return this;
    }

    public mBillOrder setDocNo(String docNo) {
        this.DocNo = docNo;
        return this;
    }

    public mBillOrder setDocDate(String docDate) {
        this.DocDate = docDate;
        return this;
    }

    public mBillOrder setNetAmt(Double netAmt) {
        this.NetAmt = netAmt;
        return this;
    }

    public mBillOrder setAmt(Double amt) {
        this.Amt = amt;
        return this;
    }

    public mBillOrder setPayMode(String payMode) {
        this.PayMode = payMode;
        return this;
    }

    public mBillOrder setStatus(String status) {
        this.Status = status;
        return this;
    }

    public mBillOrder setGrNo(String grNo) {
        this.GrNo = grNo;
        return this;
    }

    public mBillOrder setTransport(String transport) {
        this.Transport = transport;
        return this;
    }

    public mBillOrder setGrDate(String grDate) {
        this.GrDate = grDate;
        return this;
    }

    public mBillOrder setBillNo(String billNo) {
        this.BillNo = billNo;
        return this;
    }

    public mBillOrder setBillDate(String billDate) {
        this.BillDate = billDate;
        return this;
    }

    public mBillOrder setBillAmt(String billAmt) {
        this.BillAmt = billAmt;
        return this;
    }

    public mBillOrder setItems(ArrayList<mBillItem> items) {
        this.items = items;
        return this;
    }

    public mBillOrder setGST(mTax GST) {
        this.GST = GST;
        return this;
    }

    public mBillOrder setCGSTAmt(Double CGSTAmt) {
        this.CGSTAmt = CGSTAmt;
        return this;
    }

    public mBillOrder setSGSTAmt(Double SGSTAmt) {
        this.SGSTAmt = SGSTAmt;
        return this;
    }

    public mBillOrder setTotAmt(Double totAmt) {
        this.TotAmt = totAmt;
        return this;
    }

    public mBillOrder setBilledHO(int billedHO) {
        this.BilledHO = billedHO;
        return this;
    }

    public mBillOrder setApproved(String approved) {
        this.Approved = approved;
        return this;
    }

    public mBillOrder setAttachment(String attachment) {
        this.Attachment = attachment;
        return this;
    }

    public mBillOrder setAttachment(List<String> attachment) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        Iterator var4 = attachment.iterator();

        while(var4.hasNext()) {
            String file = (String)var4.next();
            if (count != 0) {
                sb.append("|^");
            }

            ++count;
            sb.append(file);
        }

        this.Attachment = sb.toString();
        return this;
    }
}
