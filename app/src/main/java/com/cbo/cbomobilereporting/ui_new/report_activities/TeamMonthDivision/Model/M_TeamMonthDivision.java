package com.cbo.cbomobilereporting.ui_new.report_activities.TeamMonthDivision.Model;

import java.util.ArrayList;

public class M_TeamMonthDivision {
    ArrayList<mMonth> months = new ArrayList<>();
    ArrayList<mUser> users= new ArrayList<>();
    ArrayList<mDivision> divisions= new ArrayList<>();
    ArrayList<mMissedFilter> missed_call_Filter= new ArrayList<>();

    public ArrayList<mMonth> getMonths() {
        return months;
    }

    public void setMonths(ArrayList<mMonth> months) {
        this.months = months;
    }

    public ArrayList<mUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<mUser> users) {
        this.users = users;
    }

    public ArrayList<mDivision> getDivisions() {
        return divisions;
    }

    public void setDivisions(ArrayList<mDivision> divisions) {
        this.divisions = divisions;
    }

    public ArrayList<mMissedFilter> getMissed_call_Filter() {
        return missed_call_Filter;
    }

    public void setMissed_call_Filter(ArrayList<mMissedFilter> missed_call_Filter) {
        this.missed_call_Filter = missed_call_Filter;
    }
}
