package saleOrder.Model;

import java.io.Serializable;

/**
 * Created by cboios on 06/03/19.
 */

public class mParty implements Serializable {

    private String Id = "0";
    private String Name= "All";
    private String Balance= "0";
    private String HeadQtr= "";
    private String Division= "";;
    private String Phone= "";;

    //getter


    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getBalance() {
        return Balance;
    }

    public String getHeadQtr() {
        return HeadQtr;
    }

    public String getDivision() {
        return Division;
    }

    public String getPhone() {
        return Phone;
    }

    //setter

    public mParty setId(String id) {
        Id = id;
        return this;
    }

    public mParty setName(String name) {
        Name = name;
        return this;
    }

    public mParty setBalance(String balance) {
        Balance = balance;
        return this;
    }

    public mParty setHeadQtr(String headQtr) {
        HeadQtr = headQtr;
        return this;
    }

    public mParty setDivision(String division) {
        Division = division;
        return this;
    }

    public mParty setPhone(String phone) {
        Phone = phone;
        return this;
    }
}
