package utils.adapterutils;

/**
 * Created by om shanti om on 4/25/15.
 */
public class MarketingSales_model {
    public String remark_model="";
    public String amount_model="";


    public MarketingSales_model(String remark_model,String amount_model){

        this.remark_model =remark_model;
        this.amount_model = amount_model;
    }

    public MarketingSales_model(){

    }


    public String getRemark_model() {
        return remark_model;
    }

    public void setRemark_model(String remark_model) {
        this.remark_model = remark_model;
    }

    public String getAmount_model() {
        return amount_model;
    }

    public void setAmount_model(String amount_model) {
        this.amount_model = amount_model;
    }
}
