package utils.adapterutils;

public class DrPres_Model {

    public String name = "";
    public String id = "";
   public String qty ="";
    public String amt = "";
    public String name_amt ="";
    public DrPres_Model(String name, String id,String name_amt) {
        this.name = name;
        this.id = id;
        this.name_amt = name_amt;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getName_amt() {
        return name_amt;
    }

    public void setName_amt(String name_amt) {
        this.name_amt = name_amt;
    }

    public String getamt() {
        return amt;
    }

    public void setamt(String amt) {
        this.amt = amt;
    }
}
