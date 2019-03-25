package com.cbo.cbomobilereporting.ui_new.Model;

/**
 * Created by cboios on 10/03/19.
 */

public class mWorkwith {
    private String id;
    private String name;
    private WorkWithType type = WorkWithType.Present;

    //getter

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public WorkWithType getType() {
        return type;
    }

    //setter

    public mWorkwith setId(String id) {
        this.id = id;
        return this;
    }

    public mWorkwith setName(String name) {
        this.name = name;
        return this;
    }

    public mWorkwith setType(WorkWithType type) {
        this.type = type;
        return this;
    }
}

enum WorkWithType{
    Present,
    Absent;
}