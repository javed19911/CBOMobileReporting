package bill.Cart;

import java.io.Serializable;

public class mCustomer implements Serializable {
    private int Id=0;
    private String Name = "";
    private String Mobile ="";
    private String DOB="";
    private String DOA="";
    private String City="";
    private String GST_NO="";

    //getter

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getDOB() {
        return DOB;
    }

    public String getDOA() {
        return DOA;
    }

    public String getCity() {
        return City;
    }


    public String getGST_NO() {
        return GST_NO;
    }

    ///setter


    public mCustomer setId(int id) {
        Id = id;
        return this;
    }

    public mCustomer setName(String name) {
        Name = name;
        return this;
    }

    public mCustomer setMobile(String mobile) {
        Mobile = mobile;
        return this;
    }

    public mCustomer setDOB(String DOB) {
        this.DOB = DOB;
        return this;
    }

    public mCustomer setDOA(String DOA) {
        this.DOA = DOA;
        return this;
    }

    public mCustomer setCity(String city) {
        City = city;
        return this;
    }

    public mCustomer setGST_NO(String GST_NO) {
        this.GST_NO = GST_NO;
        return this;
    }
}
