package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;

public class OthExpenseDB extends DBHelper {

    private ExpHeadDB expHeadDB;

    public OthExpenseDB(Context c) {
        super(c);
        expHeadDB = new ExpHeadDB(c);
    }

    public ExpHeadDB getExpHeadDB(){
        /*if (expHeadDB == null){
            expHeadDB = new ExpHeadDB(context);
        }*/
        return  expHeadDB;
    }

    @Override
    public String getTableQuery() {
        return "CREATE TABLE " + getTable() + "(id Integer PRIMARY KEY AUTOINCREMENT," +
                "exp_head_id text,exp_head text,amount text,remark text," +
                "FILE_NAME text,exp_ID text,time text)";
    }

    @Override
    public String getTable() {
        return "Expenses";
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

    public void insert(mOthExpense othExpense) {
        //try {

            getDatabase().delete(getTable(), "exp_head_id='" + othExpense.getExpHead().getId() + "'", null);
            ContentValues cv = new ContentValues();
            cv.put("exp_head_id", othExpense.getExpHead().getId());
            cv.put("exp_head", othExpense.getExpHead().getName());
            cv.put("amount", othExpense.getAmount());
            cv.put("remark", othExpense.getRemark());
            cv.put("FILE_NAME", othExpense.getAttachment());
            cv.put("exp_ID", othExpense.getId());
            cv.put("time", othExpense.getTime());
            getDatabase().insert(getTable(), null, cv);
        //}finally {
            //sd.close();
        //}

    }


    public ArrayList<mOthExpense> get() {
        ArrayList<mOthExpense> data = new ArrayList<mOthExpense>();
        String query = "Select * from " + getTable() ;



        Cursor c = getDatabase().rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    mOthExpense othExpense = new mOthExpense();
                    othExpense.setId(c.getInt(c.getColumnIndex("exp_ID")))
                            .setAttachment(c.getString(c.getColumnIndex("FILE_NAME")))
                            .setRemark(c.getString(c.getColumnIndex("remark")))
                            .setAmount(c.getDouble(c.getColumnIndex("amount")));
                    mExpHead expHead = expHeadDB.get(c.getInt(c.getColumnIndex("exp_head_id")));
                    if (expHead != null) {
                        othExpense.setExpHead(expHead);
                        data.add(othExpense);
                    }

                } while (c.moveToNext());
            }
        }finally {
            c.close();
        }
        return data;
    }

    public ArrayList<mExpHead> get_DA_ACTION_exp_head() {
        ArrayList<mExpHead> expHeads = new ArrayList<mExpHead>();

        String query ="Select * from " + expHeadDB.getTable() + " where DA_ACTION='1' and  FIELD_ID  IN(SELECT exp_head_id FROM "+ getTable()+")";
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


    public void delete(mOthExpense othExpense){
        getDatabase().delete(getTable(),"exp_ID='"+othExpense.getId()+"'",null);
    }
}
