package utils.adapterutils;

/**
 * Created by RAM on 6/15/15.
 */
public class PrescribeModel {

    String prodId;
    String productName;
    String pob;
    String qty;

    public PrescribeModel(String prodId,String productName,String pob,String qty){
        this.pob = pob;
        this.prodId = prodId;
        this.productName = productName;
        this.qty = qty;

    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getPob() {
        return pob;
    }

    public void setPob(String pob) {
        this.pob = pob;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
