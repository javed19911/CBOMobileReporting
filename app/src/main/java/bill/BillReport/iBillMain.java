package bill.BillReport;

public interface iBillMain {

    public void getBills();
    public boolean isFromDateRequired();
    public boolean isToDateRequired();
    public boolean isShowPopup();
    public String getDocType();
    public boolean IsAllRequiredInFilter();


}