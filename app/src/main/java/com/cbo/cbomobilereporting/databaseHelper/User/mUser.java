package com.cbo.cbomobilereporting.databaseHelper.User;

public class mUser {
    String CompanyName="";
    String Paid="";
    String Name="";
    String Desgination="";
    String Workwith="";
    String Area="";

    public mUser() {

    }

    public mUser(String companyName, String paid, String name, String desgination, String workwith, String area) {
        CompanyName = companyName;
        Paid = paid;
        Name = name;
        Desgination = desgination;
        Workwith = workwith;
        Area=area;
    }


    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getPaid() {
        return Paid;
    }

    public void setPaid(String paid) {
        Paid = paid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesgination() {
        return Desgination;
    }

    public void setDesgination(String desgination) {
        Desgination = desgination;
    }

    public String getWorkwith() {
        return Workwith;
    }

    public void setWorkwith(String workwith) {
        Workwith = workwith;
    }
}
