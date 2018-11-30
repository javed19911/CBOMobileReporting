package utils.adapterutils;


public class AddDelLeaveModel {

    String id;
    String docNo;
    String docDate;
    String fDate;
    String tDate;
    String days;
    String purpose;
    String approval;
  /*public AddDelLeaveModel(String id,  String docNo,String docDate,String fDate, String tDate, String days, String purpose,String approval){

      this.id = id;
      this.docNo = docNo;
      this.docDate = docDate;
      this.fDate = fDate;
      this.tDate = tDate;
      this.days = days;
      this.purpose= purpose;
      this.approval= approval;
  }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public String getfDate() {
        return fDate;
    }

    public void setfDate(String fDate) {
        this.fDate = fDate;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String gettDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }
}
