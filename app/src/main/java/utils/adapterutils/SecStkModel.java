package utils.adapterutils;

/**
 * Created by welcome on 3/29/2015.
 */
public class SecStkModel {
    String productName;
    String productId;
    String balanceValue;
    String freeValue =null;
    String sal_id;

    public SecStkModel(){
        String productName;
        String productId;
        String balanceValue;
        String freeValue ="";
        String sal_id;

    }
    public String getProductName(){
        return productName;
    }
    public void setProductName(String productName){
        this.productName=productName;
    }
    public String getProductId(){
        return productId;
    }
    public void setProductId(String productId){
        this.productId=productId;
    }
    public String getBalanceValue(){
        return balanceValue;
    }
    public void setBalanceValue(String balanceValue){
        this.balanceValue=balanceValue;
    }

    public String getFreeValue() {
        return freeValue;
    }

    public void setFreeValue(String freeValue) {
        this.freeValue = freeValue;
    }


    public String getSal_id() {
        return sal_id;
    }

    public void setSal_id(String sal_id) {
        this.sal_id = sal_id;
    }
}
