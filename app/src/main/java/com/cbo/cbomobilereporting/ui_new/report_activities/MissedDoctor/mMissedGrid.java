package com.cbo.cbomobilereporting.ui_new.report_activities.MissedDoctor;

import android.graphics.Color;

public class mMissedGrid {

    private  String id;
    private  String Name;
    private  String Color = "";
    private  String Status;
    private  String  Code;
    private  String  BG_COLOR;
    private int CRMYN = 0;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public mMissedGrid(String id, String name, String color, String status) {
        this.id = id;
        Name = name;
        Color = color;
        Status = status;
    }

    public mMissedGrid() {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getColor() {
        if(Color.trim().equals(""))
            Color = "#000000";
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getBG_COLOR() {
        if(BG_COLOR.trim().equals(""))
            BG_COLOR = "#FFFFFFFF";
        return BG_COLOR;
    }

    public void setBG_COLOR(String BG_COLOR) {

        this.BG_COLOR = BG_COLOR;
    }

    public int getCRMYN() {
        return CRMYN;
    }

    public void setCRMYN(int CRMYN) {
        this.CRMYN = CRMYN;
    }
}
