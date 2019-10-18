package bill.Dashboard;

import java.io.Serializable;
import java.util.ArrayList;

public class mDashboardNew implements Serializable {

    private String DOC_TYPE="";
    private String GROUP_NAME="";
    private ArrayList<String> COL_NAME= new ArrayList<>();
    private ArrayList<String> COL_VALUE= new ArrayList<>();
    private String BG_COLOR="#FFFFFF";


    ///getter


    public String getDOC_TYPE() {
        return DOC_TYPE;
    }

    public String getGROUP_NAME() {
        return GROUP_NAME;
    }

    public ArrayList<String> getCOL_NAME() {
        return COL_NAME;
    }

    public ArrayList<String> getCOL_VALUE() {
        return COL_VALUE;
    }

    public String getBG_COLOR() {
        return BG_COLOR;
    }


    /// settter


    public void setDOC_TYPE(String DOC_TYPE) {
        this.DOC_TYPE = DOC_TYPE;
    }

    public void setGROUP_NAME(String GROUP_NAME) {
        this.GROUP_NAME = GROUP_NAME;
    }

    public void setCOL_NAME(ArrayList<String> COL_NAME) {
        this.COL_NAME = COL_NAME;
    }

    public void setCOL_VALUE(ArrayList<String> COL_VALUE) {
        this.COL_VALUE = COL_VALUE;
    }

    public void setBG_COLOR(String BG_COLOR) {
        this.BG_COLOR = BG_COLOR;
    }
}
