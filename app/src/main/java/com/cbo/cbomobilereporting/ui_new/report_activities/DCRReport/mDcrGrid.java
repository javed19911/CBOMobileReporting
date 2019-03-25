package com.cbo.cbomobilereporting.ui_new.report_activities.DCRReport;

public class mDcrGrid
{

    private String Code;
    private String Title;
    private String Call;

    public mDcrGrid(String title, String call) {

        Title = title;
        Call = call;
    }

    public String getCode() {
        return Code;
    }

    public mDcrGrid setCode(String code) {
        Code = code;
        return this;
    }

    public String getTitle() {
        return Title;
    }

    public mDcrGrid setTitle(String title) {
        Title = title;
        return this;
    }

    public String getCall() {
        return Call;
    }

    public mDcrGrid setCall(String call) {
        Call = call;
        return this;
    }
}
