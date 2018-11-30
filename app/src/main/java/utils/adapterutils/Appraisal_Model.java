package utils.adapterutils;

import android.widget.Spinner;

public class Appraisal_Model {
    String cus_name,sno,cus_id,spinner,spin_id;

    public Appraisal_Model(){}

    public Appraisal_Model(String sno,String cus_name,String cus_id,String spin_id) {

        this.cus_name = cus_name;
        this.cus_id = cus_id;
        this.sno = sno;
        this.spin_id =spin_id;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getCus_id() {
        return cus_id;
    }

    public void setCus_id(String cus_id) {
        this.cus_id = cus_id;
    }

    public String getSpinner() {
        return spinner;
    }

    public void setSpinner(String spinner) {
        this.spinner = spinner;
    }

    public String getSpin_id() {
        return spin_id;
    }

    public void setSpin_id(String spin_id) {
        this.spin_id = spin_id;
    }
}
