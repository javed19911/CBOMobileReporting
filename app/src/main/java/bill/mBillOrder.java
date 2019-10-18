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



    public String getId() {
        return this.Id;
    }

    public mBillOrder setId(String id) {
        this.Id = id;
        return this;
    }

    public String getPartyId() {
        return this.PartyId;
    }

    public mBillOrder setPartyId(String partyId) {
        this.PartyId = partyId;
        return this;
    }

    public String getPartyName() {
        return this.PartyName;
    }

    public mBillOrder setPartyName(String partyName) {
        this.PartyName = partyName;
        return this;
    }

    public String getDocId() {
        return this.DocId;
    }

    public mBillOrder setDocId(String docId) {
        this.DocId = docId;
        return this;
    }

    public String getDocNo() {
        return this.DocNo;
    }

    public mBillOrder setDocNo(String docNo) {
        this.DocNo = docNo;
        return this;
    }

    public String getDocDate() {
        return this.DocDate;
    }

    public mBillOrder setDocDate(String docDate) {
        this.DocDate = docDate;
        return this;
    }

    public Double getAmt() {
        return this.Amt;
    }

    public mBillOrder setAmt(Double amt) {
        this.Amt = amt;
        return this;
    }

    public Double getNetAmt() {
        return this.NetAmt;
    }

    public mBillOrder setNetAmt(Double netAmt) {

        this.NetAmt = netAmt;
        return this;
    }

    public Double getCGSTAmt() {
        return this.CGSTAmt;
    }

    public mBillOrder setCGSTAmt(Double CGSTAmt) {
        this.CGSTAmt = CGSTAmt;
        return this;
    }

    public Double getSGSTAmt() {
        return this.SGSTAmt;
    }

    public mBillOrder setSGSTAmt(Double SGSTAmt) {
        this.SGSTAmt = SGSTAmt;
        return this;
    }

    public Double getTotAmt() {
        return this.TotAmt;
    }

    public mBillOrder setTotAmt(Double totAmt) {
        Round_amt = Math.round(totAmt) - totAmt;
        this.TotAmt = Math.round(totAmt) + 0.0;
        return this;
    }

    public Double getRouAmt() {
        return this.Round_amt;
    }

    public String getPayMode() {
        return this.PayMode;
    }

    public mBillOrder setPayMode(String payMode) {
        this.PayMode = payMode;
        return this;
    }

    public String getStatus() {
        return this.Status;
    }

    public mBillOrder setStatus(String status) {
        this.Status = status;
        return this;
    }

    public String getGrNo() {
        return this.GrNo;
    }

    public mBillOrder setGrNo(String grNo) {
        this.GrNo = grNo;
        return this;
    }

    public String getTransport() {
        return this.Transport;
    }

    public mBillOrder setTransport(String transport) {
        this.Transport = transport;
        return this;
    }

    public String getGrDate() {
        return this.GrDate;
    }

    public mBillOrder setGrDate(String grDate) {
        this.GrDate = grDate;
        return this;
    }

    public String getBillNo() {
        return this.BillNo;
    }

    public mBillOrder setBillNo(String billNo) {
        this.BillNo = billNo;
        return this;
    }

    public String getBillDate() {
        return this.BillDate;
    }

    public mBillOrder setBillDate(String billDate) {
        this.BillDate = billDate;
        return this;
    }

    public String getBillAmt() {
        return this.BillAmt;
    }

    public mBillOrder setBillAmt(String billAmt) {
        this.BillAmt = billAmt;
        return this;
    }

    public ArrayList<mBillItem> getItems() {
        return this.items;
    }

    public mBillOrder setItems(ArrayList<mBillItem> items) {
        this.items = items;
        return this;
    }

    public int getBilledHO() {
        return this.BilledHO;
    }

    public mBillOrder setBilledHO(int billedHO) {
        this.BilledHO = billedHO;
        return this;
    }

    public String getApproved() {
        return this.Approved;
    }

    public mBillOrder setApproved(String approved) {
        this.Approved = approved;
        return this;
    }

    public String getAttachment() {
        return this.Attachment;
    }

    public mBillOrder setAttachment(String attachment) {
        this.Attachment = attachment;
        return this;
    }

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

    public mBillOrder setGST(mTax GST) {
        this.GST = GST;
        return this;
    }


    public mBillOrder setRemark(String remark) {
        Remark = remark;
        return this;
    }
}
