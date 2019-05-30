package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;

public class ExpHeadDB extends DBHelper {
    public ExpHeadDB(Context c) {
        super(c);
    }

    @Override
    public String getTableQuery() {
        return "CREATE TABLE " + getTable() + "(MainId Integer PRIMARY KEY AUTOINCREMENT,FIELD_NAME text,FIELD_ID text," +
                "MANDATORY text,DA_ACTION text,EXP_TYPE text,ATTACHYN text,MAX_AMT float,TAMST_VALIDATEYN text)";
    }

    @Override
    public String getTable() {
        return "Expenses_head";
    }

    @Override
    public int getTableVersion() {
        return 1;
    }

    @Override
    public void onTableCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(getTableQuery());
    }

    @Override
    public void onTableUpdate(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void insert(mExpHead expHead){

        ContentValues contentValues = new ContentValues();
        contentValues.put("FIELD_ID", expHead.getId());
        contentValues.put("FIELD_NAME", expHead.getName());
        contentValues.put("MANDATORY", expHead.getMANDATORY());
        contentValues.put("DA_ACTION", expHead.getDA_ACTION());
        contentValues.put("EXP_TYPE", expHead.getEXP_TYPE().name());
        contentValues.put("ATTACHYN", expHead.getATTACHYN());
        contentValues.put("MAX_AMT", expHead.getMAX_AMT());
        contentValues.put("TAMST_VALIDATEYN", expHead.getMasterValidate());
        getDatabase().insert(getTable(), null, contentValues);


    }

    public ArrayList<mExpHead> get() {
        return get(eExpanse.None);
    }

    public ArrayList<mExpHead> get(eExpanse type) {
        ArrayList<mExpHead> expHeads = new ArrayList<mExpHead>();

        String query ="Select * from " + getTable() + " where (EXP_TYPE='"+type.name()+"' )";
        if (type == eExpanse.None){
            query =  "Select * from " + getTable();
        }
        Cursor c = getDatabase().rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    mExpHead expHead=new mExpHead(c.getInt(c.getColumnIndex("FIELD_ID")),
                            c.getString(c.getColumnIndex("FIELD_NAME")))
                            .setEXP_TYPE(eExpanse.valueOf(c.getString(c.getColumnIndex("EXP_TYPE"))))
                            .setATTACHYN(c.getInt(c.getColumnIndex("ATTACHYN")))
                            .setDA_ACTION(c.getInt(c.getColumnIndex("DA_ACTION")))
                            .setMANDATORY(c.getInt(c.getColumnIndex("MANDATORY")))
                            .setMAX_AMT(c.getDouble(c.getColumnIndex("MAX_AMT")))
                            .setMasterValidate(c.getInt(c.getColumnIndex("TAMST_VALIDATEYN")));



                    expHeads.add(expHead);
                } while (c.moveToNext());
            }
        }finally {
            c.close();
        }
        return expHeads;
    }

    public mExpHead get(int Id) {
        mExpHead expHead = null;

        String query ="Select * from " + getTable() + " where FIELD_ID='"+Id+"'";

        Cursor c = getDatabase().rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    expHead = new mExpHead(c.getInt(c.getColumnIndex("FIELD_ID")),
                            c.getString(c.getColumnIndex("FIELD_NAME")))
                            .setEXP_TYPE(eExpanse.valueOf(c.getString(c.getColumnIndex("EXP_TYPE"))))
                            .setATTACHYN(c.getInt(c.getColumnIndex("ATTACHYN")))
                            .setDA_ACTION(c.getInt(c.getColumnIndex("DA_ACTION")))
                            .setMANDATORY(c.getInt(c.getColumnIndex("MANDATORY")))
                            .setMAX_AMT(c.getDouble(c.getColumnIndex("MAX_AMT")))
                            .setMasterValidate(c.getInt(c.getColumnIndex("TAMST_VALIDATEYN")));




                } while (c.moveToNext());
            }
        }finally {
            c.close();
        }
        return expHead;
    }


}
