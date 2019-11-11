package com.cbo.cbomobilereporting.databaseHelper.Call.Local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;

public class DayPlanAlertDB extends DBHelper {

    public DayPlanAlertDB(Context c) {
        super(c);
    }

    @Override
    public String getTableQuery() {
        return "CREATE TABLE " + getTable() +
                "(MainId Integer PRIMARY KEY AUTOINCREMENT,DCR_DATE text, " +
                "DAYPLAN_REMINDER_FTIME text,DAYPLAN_REMINDER_TTIME text," +
                "DAYPLAN_REMINDER_INTERVAL text,DAYPLAN_REMINDER_VOICE text)";
    }

    @Override
    public String getTable() {
        return "DayPlanAlertDB";
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


    public void insert(String DCR_DATE, String DAYPLAN_REMINDER_FTIME,
                       String DAYPLAN_REMINDER_TTIME,String DAYPLAN_REMINDER_INTERVAL,
                       String DAYPLAN_REMINDER_VOICE){

        ContentValues contentValues = new ContentValues();
        contentValues.put("DCR_DATE", DCR_DATE);
        contentValues.put("DAYPLAN_REMINDER_FTIME", DAYPLAN_REMINDER_FTIME);
        contentValues.put("DAYPLAN_REMINDER_TTIME",DAYPLAN_REMINDER_TTIME);
        contentValues.put("DAYPLAN_REMINDER_INTERVAL", DAYPLAN_REMINDER_INTERVAL);
        contentValues.put("DAYPLAN_REMINDER_VOICE", DAYPLAN_REMINDER_VOICE);
        getDatabase().insert(getTable(), null, contentValues);

    }


    public  ArrayList<Map<String, String>> get() {
        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();

        String query ="Select * from " + getTable() ;

        Cursor c = getDatabase().rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {


                    Map<String, String> datanum1 = new HashMap<String, String>();
                    datanum1.put("DCR_DATE",  c.getString(c.getColumnIndex("DCR_DATE")));
                    datanum1.put("DAYPLAN_REMINDER_FTIME",  c.getString(c.getColumnIndex("DAYPLAN_REMINDER_FTIME")));
                    datanum1.put("DAYPLAN_REMINDER_TTIME", c.getString(c.getColumnIndex("DAYPLAN_REMINDER_TTIME")));
                    datanum1.put("DAYPLAN_REMINDER_INTERVAL",  c.getString(c.getColumnIndex("DAYPLAN_REMINDER_INTERVAL")));
                    datanum1.put("DAYPLAN_REMINDER_VOICE",  c.getString(c.getColumnIndex("DAYPLAN_REMINDER_VOICE")));
                    data.add(datanum1);

                } while (c.moveToNext());
            }
        }finally {
            c.close();
        }
        return data;
    }
}
