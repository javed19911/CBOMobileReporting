package utils.adapterutils;

/**
 * Created by Akshit on 4/17/2015.
 */
public class LeaveModel {

    String leaveHeadId;
    String leaveHead;
    String balQty;
    String reqQty;
    String leaveId;

   public LeaveModel(String leaveHeadId, String leaveHead, String balQty, String reqQty, String leaveId){

      this.leaveHeadId =leaveHeadId;
       this.leaveHead = leaveHead;
       this.balQty = balQty;
       this.reqQty = reqQty;
       this.leaveId = leaveId;
   }
    public LeaveModel(){

    }
     public String getLeaveHeadId(){
         return leaveHeadId;
     }

    public void setLeaveHeadId(String leaveHeadId) {
        this.leaveHeadId = leaveHeadId;
    }

    public String getLeaveHead() {
        return leaveHead;
    }
    public void setLeaveHead(String leaveHead) {
        this.leaveHead = leaveHead;
    }


    public String getBalQty() {
        return balQty;
    }

    public void setBalQty(String balQty) {
        this.balQty = balQty;
    }

    public String getReqQty() {
        return reqQty;
    }


    public void setReqQty(String reqQty) {
        this.reqQty = reqQty;
    }


    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }
}
