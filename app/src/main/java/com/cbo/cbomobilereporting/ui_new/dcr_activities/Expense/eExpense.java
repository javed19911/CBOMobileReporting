package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

public enum eExpense {
    None,
    TA,
    DA,
    SERVER;

    private static eExpense[] list = eExpense.values();

    public static eExpense getExp(int i) {
        if (i>= list.length){
            i=0;
        }
        return list[i];
    }
}
