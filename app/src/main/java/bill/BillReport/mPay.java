package bill.BillReport;

import java.io.Serializable;

public class mPay implements Serializable {
    private int id = 0;
    private String name ="";

    public mPay(int id, String name) {
        this.id = id;
        this.name = name;
    }


    //getter

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    ///setter

    public mPay setId(int id) {
        this.id = id;
        return this;
    }

    public mPay setName(String name) {
        this.name = name;
        return this;
    }
}
