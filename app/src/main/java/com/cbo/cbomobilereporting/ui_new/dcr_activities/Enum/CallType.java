package com.cbo.cbomobilereporting.ui_new.dcr_activities.Enum;

public enum CallType {
    NONE(""),
    DOCTOR("Dr"),
    CHEMIST("C"),
    STOKIST("S"),
    DOCTOR_REMAINDER("RcDr"),
    DAIRY("D"),
    POUlTRY("P"),
    EXPENSE("E");

    private String value ="";
    CallType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
