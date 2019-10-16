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
                "FILE_NAME text,exp_ID text,time text,km text,editable Integer)";
    }

    @Override
    public String getTable() {
        return "Expenses";
    }

    @Override
    public int getTableVersion() {
        return 3;
    }

    @Override
    public void onTableCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(getTableQuery());
    }

    @Override
    public void onTableUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE " + this.getTable() + " ADD COLUMN km text DEFAULT '0'");
            case 2:
                db.execSQL("ALTER TABLE " + this.getTable() + " ADD COLUMN editable Integer DEFAULT 1");
            default:
        }
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
            cv.put("km", othExpense.getKm());
            cv.put("editable", othExpense.IsEditable()? 1: 0);
            getDatabase().insert(getTable(), null, cv);
        //}finally {
            //sd.close();
        //}

    }


    public ArrayList<mOthExpense> get(eExpense For_DA_TA) {
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
                            .setAmount(c.getDouble(c.getColumnIndex("amount")))
                            .setKm(c.getDouble(c.getColumnIndex("km")))
                            .setEditable(c.getInt(c.getColumnIndex("editable")) == 1 );

                    mExpHead expHead = expHeadDB.get(c.getInt(c.getColumnIndex("exp_head_id")));
                    if ((expHead != null && (For_DA_TA == expHead.getSHOW_IN_TA_DA()))
                            || (expHead != null && eExpense.SERVER == expHead.getSHOW_IN_TA_DA() && For_DA_TA == eExpense.None)
                            /*|| (expHead == null && For_DA_TA == eExpense.None)*/) {

                        /*if (expHead == null){
                            expHead = new mExpHead(c.getInt(c.getColumnIndex("exp_head_id"))
                                    ,c.getString(c.getColumnIndex("exp_head")));
                        }*/

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
                            .setEXP_TYPE(eExpense.valueOf(c.getString(c.getColumnIndex("EXP_TYPE"))))
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

    public void deleteDefaultTypes(){
        String query ="Select exp_head_id from " + getTable() + " LEFT JOIN "+expHeadDB.getTable()+" ON exp_head_id = FIELD_ID where SHOW_IN_TA_DA = '"+eExpense.SERVER.name()+"'";
        getDatabase().delete(getTable(),"exp_head_id in ("+query+")",null);
    }

    public boolean IsExpHeadGroupAdded(String headtype_group) {
        ArrayList<mExpHead> expHeads = new ArrayList<mExpHead>();


        String query ="Select * from " + expHeadDB.getTable() + " LEFT JOIN "+getTable()+" ON exp_head_id = FIELD_ID where exp_head_id IS NOT NULL ";//+
        //"AND HEADTYPE_GROUP NOT IN (Select a.HEADTYPE_GROUP from " + getTable() + " as a LEFT JOIN "+"Expenses"+" as b ON b.exp_head_id = a.FIELD_ID where b.exp_head_id IS NOT NULL AND a.HEADTYPE_GROUP != 0)";

        String WhereClause = "";

        WhereClause += " AND  HEADTYPE_GROUP = '"+headtype_group+"'";


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
        return expHeads.size()>0 && !headtype_group.equalsIgnoreCase("0");
    }



    public ArrayList<mExpHead> getMandatoryPendingExpHead() {
        ArrayList<mExpHead> expHeads = new ArrayList<mExpHead>();

        String query ="Select * from " + expHeadDB.getTable() + " where MANDATORY='1' and  FIELD_ID NOT IN(SELECT exp_head_id FROM "+ getTable()+")";


        Cursor c = getDatabase().rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    mExpHead expHead = new mExpHead(c.getInt(c.getColumnIndex("FIELD_ID")),
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

                    expHeads.add(expHead);


                } while (c.moveToNext());
            }
        }finally {
            c.close();
        }
        return expHeads;
    }

}
