package utils.adapterutils;

/**
 * Created by welcome on 3/22/2015.
 */
public class RptModel {
    String date;
    String with;
    String ttldr;
    String ttlchm;
    String ttlstk;
    String ttlexp;
    String ttlMissedCall;
    String ttlDrRiminder,ttlNonDoctor,ttlTenivia;
    String dairyCount,PolutaryCount;
    String remark;
    boolean blinkRemark = false;

    public RptModel(String mdate,String mwith,String mdr,String mchm,String mstk,String mexp,String ttlNonDistributor,String ttlNonRetailer){
        this.date=mdate;
        this.with=mwith;
        this.ttldr=mdr;
        this.ttlchm=mchm;
        this.ttlstk=mstk;
        this.ttlexp=mexp;
        this.ttlMissedCall = ttlNonRetailer;
        this.ttlDrRiminder = ttlNonDistributor;
    }
    public RptModel(){

    }

      public String getDate(){
          return date;
      }
    public void setDtae(String date){
        this.date=date;
    }
    public String getWith(){
        return  with;
    }
    public void setWith(String with){
        this.with=with;
    }
    public String getTtldr(){
        return ttldr;
    }
    public void setTtldr(String dr){
        this.ttldr=dr;
    }
    public String getTtlchm(){
        return ttlchm;
    }
    public void setTtlchm(String chm){
        this.ttlchm=chm;
    }
    public String getTtlstk(){
        return ttlstk;
    }
    public void setTtlstk(String stk){
        this.ttlstk=stk;
    }
    public String getTtlexp(){
        return ttlexp;
    }
    public void setTtlexp(String exp){
        this.ttlexp=exp;
    }

    public String getTtlMissedCall() {
        return ttlMissedCall;
    }

    public String getTtlDrRiminder() {
        return ttlDrRiminder;
    }

    public String getTtlTenivia() {
        return ttlTenivia;
    }
    public String getTtlNonDoctor() {
        return ttlNonDoctor;
    }

    public void setTtlMissedCall(String ttlMissedCall) {
        this.ttlMissedCall = ttlMissedCall;
    }

    public void setTtlDrRiminder(String ttlDrRiminder) {
        this.ttlDrRiminder = ttlDrRiminder;
    }
    public void setTtlTenivia(String ttlTenivia) {
        this.ttlTenivia = ttlTenivia;
    }
    public void setTtlNonDoctor(String ttlNonDoctor) {
        this.ttlNonDoctor = ttlNonDoctor;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRemark() {
        return this.remark ;
    }

    public String getDairyCount() {
        return dairyCount;
    }

    public void setDairyCount(String dairyCount) {
        this.dairyCount = dairyCount;
    }

    public String getPolutaryCount() {
        return PolutaryCount;
    }

    public void setPolutaryCount(String polutaryCount) {
        PolutaryCount = polutaryCount;
    }

    public boolean isBlinkRemark() {
        return blinkRemark;
    }

    public void setBlinkRemark(boolean blinkRemark) {
        this.blinkRemark = blinkRemark;
    }
}
