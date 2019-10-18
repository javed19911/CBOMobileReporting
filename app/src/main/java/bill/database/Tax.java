package bill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;

public class Tax extends DBHelper {

    public Tax(Context c) {
        super(c);
    }

    @Override
    public String getTableQuery() {
        return "CREATE TABLE " + getTable() + "(Id Integer PRIMARY KEY AUTOINCREMENT,MAIN_ID Integer," +
                "type text,CGST text,SGST text)";
    }

    @Override
    public String getTable() {
        return "TAX";
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
    public void onTableUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {

            default:
        }
    }

    public void insert(String doc_id, mTax tax) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", tax.getType().name());
        contentValues.put("MAIN_ID", doc_id);
        contentValues.put("CGST", tax.getCGST());
        contentValues.put("SGST", tax.getSGST());
        getDatabase().insert(getTable(), null, contentValues);

    }


    public ArrayList<mTax> gets(String doc_id) {
        ArrayList<mTax> taxs = new ArrayList<mTax>();
        String query = "Select * from " + getTable() + " where MAIN_ID ='" + doc_id + "'";
        Cursor c = getDatabase().rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    mTax tax = new mTax(eTax.valueOf(c.getString(c.getColumnIndex("type"))));
                    tax.setCGST(c.getDouble(c.getColumnIndex("CGST")));
                    tax.setSGST(c.getDouble(c.getColumnIndex("SGST")));
                    taxs.add(tax);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return taxs;
    }


    public mTax get(String doc_id) {
        ArrayList<mTax> taxs = new ArrayList<mTax>();
        taxs = gets(doc_id);
        if (taxs.size() > 0) {
            return taxs.get(0);
        }
        return null;
    }


}
