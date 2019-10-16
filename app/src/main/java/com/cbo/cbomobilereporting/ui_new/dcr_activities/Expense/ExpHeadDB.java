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
                "MANDATORY text,DA_ACTION text,EXP_TYPE text,ATTACHYN text,MAX_AMT float,TAMST_VALIDATEYN text," +
                "SHOW_IN_TA_DA text,KMYN text,HEADTYPE_GROUP text)";
    }

    @Override
    public String getTable() {
        return "Expenses_head";
    }

    @Override
    public int getTableVersion() {
        return 4;
    }

    @Override
    public void onTableCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(getTableQuery());
    }

    @Override
    public void onTableUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE " + this.getTable() + " ADD COLUMN SHOW_IN_TA_DA text DEFAULT '0'");
            case 2:
                db.execSQL("ALTER TABLE " + this.getTable() + " ADD COLUMN KMYN text DEFAULT '0'");
            case 3:
                db.execSQL("ALTER TABLE " + this.getTable() + " ADD COLUMN HEADTYPE_GROUP text DEFAULT '0'");
                default:
        }
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
        contentValues.put("SHOW_IN_TA_DA", expHead.getSHOW_IN_TA_DA().name());
        contentValues.put("KMYN", expHead.getKMYN());
        contentValues.put("HEADTYPE_GROUP", expHead.getHEADTYPE_GROUP());

        getDatabase().insert(getTable(), null, contentValues);


    }

    public ArrayList<mExpHead> get() {
        return get(eExpense.None);
    }

    public ArrayList<mExpHead> get(eExpense type) {
        ArrayList<mExpHead> expHeads = new ArrayList<mExpHead>();

        String query ="Select * from " + getTable() + " where (EXP_TYPE='"+type.name()+"' )";
        if (type == eExpense.None){
            query =  "Select * from " + getTable();
        }
        Cursor c = getDatabase().rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    mExpHead expHead=new mExpHead(c.getInt(c.getColumnIndex("FIELD_ID")),
                            c.getString(c.getColumnIndex("FIELD_NAME")))
                            .setEXP_TYPE(eExpense.valueOf(c.getString(c.getColumnIndex("EXP_TYPE"))))
                            .setSHOW_IN_TA_DA(eExpense.valueOf(c.getString(c.getColumnIndex("SHOW_IN_TA_DA"))))
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

    public ArrayList<mExpHead> get_NotAdded(eExpense SHOW_IN_TA_DA) {
        ArrayList<mExpHead> expHeads = new ArrayList<mExpHead>();


        String query ="Select * from " + getTable() + " LEFT JOIN "+"Expenses"+" ON exp_head_id = FIELD_ID where exp_head_id IS NULL ";//+
                //"AND HEADTYPE_GROUP NOT IN (Select a.HEADTYPE_GROUP from " + getTable() + " as a LEFT JOIN "+"Expenses"+" as b ON b.exp_head_id = a.FIELD_ID where b.exp_head_id IS NOT NULL AND a.HEADTYPE_GROUP != 0)";

        String WhereClause = "";

        WhereClause += " AND  SHOW_IN_TA_DA = '"+SHOW_IN_TA_DA.name()+"'";


        Cursor c = getDatabase().rawQuery(query+ WhereClause, null);
        try {
            if (c.moveToFirst()) {
                do {
                    mExpHead expHead=new mExpHead(c.getInt(c.getColumnIndex("FIELD_ID")),
                            c.getString(c.getColumnIndex("FIELD_NAME")))
                            .setEXP_TYPE(eExpense.valueOf(c.getString(c.getColumnIndex("EXP_TYPE"))))
                            .setSHOW_IN_TA_DA(eExpense.valueOf(c.getString(c.getColumnIndex("SHOW_IN_TA_DA"))))
                            .setATTACHYN(c.getInt(c.getColumnIndex("ATTACHYN")))
                            .setDA_ACTION(c.getInt(c.getColumnIndex("DA_ACTION")))
                            .setMANDATORY(c.getInt(c.getColumnIndex("MANDATORY")))
                            .setMAX_AMT(c.getDouble(c.getColumnIndex("MAX_AMT")))
                            .setKMYN(c.getString(c.getColumnIndex("KMYN")))
                            .setHEADTYPE_GROUP(c.getString(c.getColumnIndex("HEADTYPE_GROUP")))
                            .setMasterValidate(c.getInt(c.getColumnIndex("TAMST_VALIDATEYN")));

                    //if (SHOW_IN_TA_DA != eExpense.TA || SHOW_IN_TA_DA == expHead.getSHOW_IN_TA_DA()) {
                        expHeads.add(expHead);
                    //}
                } while (c.moveToNext());
            }
        }finally {
            c.close();
        }
        return expHeads;
    }

    public ArrayList<mExpHead> get(String HEADTYPE_GROUP) {
        ArrayList<mExpHead> expHeads = new ArrayList<mExpHead>();


        String query ="Select * from " + getTable() + " where ";//+
        //"AND HEADTYPE_GROUP NOT IN (Select a.HEADTYPE_GROUP from " + getTable() + " as a LEFT JOIN "+"Expenses"+" as b ON b.exp_head_id = a.FIELD_ID where b.exp_head_id IS NOT NULL AND a.HEADTYPE_GROUP != 0)";

        String WhereClause = "";

        WhereClause += " HEADTYPE_GROUP = '"+HEADTYPE_GROUP+"'";


        Cursor c = getDatabase().rawQuery(query+ WhereClause, null);
        try {
            if (c.moveToFirst()) {
                do {
                    mExpHead expHead=new mExpHead(c.getInt(c.getColumnIndex("FIELD_ID")),
                            c.getString(c.getColumnIndex("FIELD_NAME")))
                            .setEXP_TYPE(eExpense.valueOf(c.getString(c.getColumnIndex("EXP_TYPE"))))
                            .setSHOW_IN_TA_DA(eExpense.valueOf(c.getString(c.getColumnIndex("SHOW_IN_TA_DA"))))
                            .setATTACHYN(c.getInt(c.getColumnIndex("ATTACHYN")))
                            .setDA_ACTION(c.getInt(c.getColumnIndex("DA_ACTION")))
                            .setMANDATORY(c.getInt(c.getColumnIndex("MANDATORY")))
                            .setMAX_AMT(c.getDouble(c.getColumnIndex("MAX_AMT")))
                            .setKMYN(c.getString(c.getColumnIndex("KMYN")))
                            .setHEADTYPE_GROUP(c.getString(c.getColumnIndex("HEADTYPE_GROUP")))
                            .setMasterValidate(c.getInt(c.getColumnIndex("TAMST_VALIDATEYN")));

                    //if (SHOW_IN_TA_DA != eExpense.TA || SHOW_IN_TA_DA == expHead.getSHOW_IN_TA_DA()) {
                    expHeads.add(expHead);
                    //}
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
                            .setEXP_TYPE(eExpense.valueOf(c.getString(c.getColumnIndex("EXP_TYPE"))))
                            .setSHOW_IN_TA_DA(eExpense.valueOf(c.getString(c.getColumnIndex("SHOW_IN_TA_DA"))))
                            .setATTACHYN(c.getInt(c.getColumnIndex("ATTACHYN")))
                            .setDA_ACTION(c.getInt(c.getColumnIndex("DA_ACTION")))
                            .setMANDATORY(c.getInt(c.getColumnIndex("MANDATORY")))
                            .setMAX_AMT(c.getDouble(c.getColumnIndex("MAX_AMT")))
                            .setKMYN(c.getString(c.getColumnIndex("KMYN")))
                            .setHEADTYPE_GROUP(c.getString(c.getColumnIndex("HEADTYPE_GROUP")))
                            .setMasterValidate(c.getInt(c.getColumnIndex("TAMST_VALIDATEYN")));




                } while (c.moveToNext());
            }
        }finally {
            c.close();
        }
        return expHead;
    }




}
