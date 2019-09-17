package Bill.BillReport;

import java.io.Serializable;

import utils.model.DropDownModel;


public class mCompany extends DropDownModel {


    public mCompany() {
        super("", "0");
    }

    public mCompany(String name, String id) {
        super(name, id);
    }
}



