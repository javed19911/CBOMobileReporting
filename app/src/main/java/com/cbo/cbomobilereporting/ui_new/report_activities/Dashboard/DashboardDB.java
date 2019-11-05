package com.cbo.cbomobilereporting.ui_new.report_activities.Dashboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;

public class DashboardDB extends DBHelper {

    public DashboardDB(Context c) {
        super(c);
    }

    @Override
    public String getTableQuery() {
        return "CREATE TABLE " + getTable() +
                "(MainId Integer PRIMARY KEY AUTOINCREMENT, " +
                "CATEGORY text,REMARK text,AMOUNT text,AMOUNT_CUMM text,URL text)";
    }

    @Override
    public String getTable() {
        return "DashboardDB";
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

    public void insert(Map<String, String> dashboard,String Category){

        ContentValues contentValues = new ContentValues();
        contentValues.put("CATEGORY", Category);
        contentValues.put("REMARK", dashboard.get("REMARK"));
        contentValues.put("AMOUNT", dashboard.get("AMOUNT"));
        contentValues.put("AMOUNT_CUMM", dashboard.get("AMOUNT_CUMM"));
        contentValues.put("URL", dashboard.get("URL"));
        getDatabase().insert(getTable(), null, contentValues);

    }


    public ArrayList<Map<String, String>> get(String Category) {
        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();

        String query ="Select * from " + getTable() + " where (CATEGORY='"+Category+"' )";
        if (Category.isEmpty()){
            query =  "Select * from " + getTable();
        }
        Cursor c = getDatabase().rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {

                    Map<String, String> datanum1 = new HashMap<String, String>();

                    datanum1.put("REMARK",  c.getString(c.getColumnIndex("REMARK")));
                    datanum1.put("AMOUNT",  c.getString(c.getColumnIndex("AMOUNT")));
                    datanum1.put("AMOUNT_CUMM", c.getString(c.getColumnIndex("AMOUNT_CUMM")));
                    datanum1.put("URL",  c.getString(c.getColumnIndex("URL")));
                    data.add(datanum1);

                } while (c.moveToNext());
            }
        }finally {
            c.close();
        }
        return data;
    }
}
