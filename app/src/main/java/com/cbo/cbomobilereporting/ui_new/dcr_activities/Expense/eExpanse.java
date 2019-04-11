package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

public enum eExpanse {
    None,
    TA,
    DA;

    private static eExpanse[] list = eExpanse.values();

    public static eExpanse getExp(int i) {
        return list[i];
    }
}
