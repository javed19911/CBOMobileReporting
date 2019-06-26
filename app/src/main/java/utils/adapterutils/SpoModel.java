package utils.adapterutils;

import java.io.Serializable;

/**
 * Created by RAM on 9/4/15.
 */
public class SpoModel  implements Serializable {

   private String id,consignee,salAmt,saleReturn,breageExpiry,creditNotOrther
            ,netSales,priSales,recipt,outStanding,stockAmt,target_Amount,Spo_bill_url = "";




    public SpoModel(String id,String consignee,String salAmt,String saleReturn,String breageExpiry,String creditNotOrther,
                    String netSales,String recipt,String outStanding,String stockAmt,String target_Amount){
        this.id = id;
        this.consignee = consignee;
        this.salAmt = salAmt;
        this.saleReturn = saleReturn;
        this.breageExpiry = breageExpiry;
        this.creditNotOrther = creditNotOrther;
        this.netSales = netSales;
        this.recipt = recipt;
        this.outStanding = outStanding;
        this.stockAmt = stockAmt;
        this.target_Amount = target_Amount;


    }
    public SpoModel(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getSalAmt() {
        return salAmt;
    }

    public void setSalAmt(String salAmt) {
        this.salAmt = salAmt;
    }

    public String getBreageExpiry() {
        return breageExpiry;
    }

    public void setBreageExpiry(String breageExpiry) {
        this.breageExpiry = breageExpiry;
    }

    public String getCreditNotOrther() {
        return creditNotOrther;
    }

    public void setCreditNotOrther(String creditNotOrther) {
        this.creditNotOrther = creditNotOrther;
    }

    public String getNetSales() {
        return netSales;
    }

    public void setNetSales(String netSales) {
        this.netSales = netSales;
    }

    public String getSecSales() {
        return priSales;
    }

    public void setSecSales(String priSales) {
        this.priSales = priSales;
    }

    public String getSaleReturn() {
        return saleReturn;
    }

    public void setSaleReturn(String saleReturn) {
        this.saleReturn = saleReturn;
    }

    public String getRecipt() {
        return recipt;
    }

    public void setRecipt(String recipt) {
        this.recipt = recipt;
    }

    public String getOutStanding() {
        return outStanding;
    }

    public void setOutStanding(String outStanding) {
        this.outStanding = outStanding;
    }

    public String getStockAmt() {
        return stockAmt;
    }

    public void setStockAmt(String stockAmt) {
        this.stockAmt = stockAmt;
    }

    public String getTarget_Amount() {
        return target_Amount;
    }

    public void setTarget_Amount(String target_Amount) {
        this.target_Amount = target_Amount;
    }

    public String getSpo_bill_url() {
        return Spo_bill_url;
    }

    public void setSpo_bill_url(String url) {
        Spo_bill_url = url;
    }
}
