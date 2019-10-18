package bill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import bill.NewOrder.mBillItem;
import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;
import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDeal;

public class Deal extends DBHelper {

    //    private eDeal Type;
    public Deal(Context c) {
        super(c);
    }

    @Override
    public String getTableQuery() {
        return "CREATE TABLE " + getTable() + "(Id Integer PRIMARY KEY AUTOINCREMENT," +
                "MAIN_ID Integer,DEAL_ID Integer,NAME text,QTY text,FREE_QTY text," +
                "F_DATE text,T_DATE text)";
    }

    @Override
    public String getTable() {
        return "DEAL";
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

    public void insert(String doc_id, ArrayList<mDeal> deals) {
        for (mDeal deal : deals) {
            insert(doc_id, deal);
        }
    }

    public void insert(String doc_id, mDeal deal) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("type", deal.getType().name());
        contentValues.put("MAIN_ID", doc_id);
        contentValues.put("DEAL_ID", deal.getId());
        contentValues.put("NAME", deal.getName());
        contentValues.put("QTY", deal.getQty());
        contentValues.put("FREE_QTY", deal.getQty());
        contentValues.put("F_DATE", deal.getFDate());
        contentValues.put("T_DATE", deal.getTDate());

        getDatabase().insert(getTable(), null, contentValues);

    }


    public ArrayList<mDeal> gets(String doc_id) {
        ArrayList<mDeal> deals = new ArrayList<mDeal>();
        String query = "Select * from " + getTable() + " where MAIN_ID ='" + doc_id + "'";
        Cursor c = getDatabase().rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    mDeal deal = new mDeal();
                    deal.setType(eDeal.valueOf(c.getString(c.getColumnIndex("type"))));
                    deal.setId(c.getInt(c.getColumnIndex("DEAL_ID")));
                    deal.setName(c.getString(c.getColumnIndex("NAME")));
                    deal.setQty(c.getDouble(c.getColumnIndex("QTY")));
                    deal.setFreeQty(c.getDouble(c.getColumnIndex("FREE_QTY")));
                    deal.setFDate(c.getString(c.getColumnIndex("F_DATE")));
                    deal.setTDate(c.getString(c.getColumnIndex("T_DATE")));
                    deals.add(deal);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return deals;
    }

    public mDeal get(String doc_id) {
        ArrayList<mDeal> deals = new ArrayList<mDeal>();
        deals = gets(doc_id);
        if (deals.size() > 0) {
            return deals.get(0);
        }
        return null;
    }

}

