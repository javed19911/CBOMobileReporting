package bill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;
import cbomobilereporting.cbo.com.cboorder.Enum.eDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;


public class Discount extends DBHelper {

    //    private eDeal Type;
    public Discount(Context c) {
        super(c);
    }


    @Override
    public String getTableQuery() {
        return "CREATE TABLE " + getTable() + "(Id Integer PRIMARY KEY AUTOINCREMENT," +
                "MAIN_ID Integer,DISCOUNT_ID Integer,SEC_ID Integer,NAME text,MAX text,PERCENT text," +
                "FORM text,TO text)";
    }

    @Override
    public String getTable() {
        return "DISCOUNT";
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

    public void insert(String doc_id, mDiscount discount) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("type", discount.getType().name());
        contentValues.put("MAIN_ID", doc_id);
        contentValues.put("DISCOUNT_ID", discount.getId());
        contentValues.put("NAME", discount.getName());
        contentValues.put("MAX", discount.getMax());
        contentValues.put("PERCENT", discount.getPercent());
        contentValues.put("FORM", discount.getFrom());
        contentValues.put("TO", discount.getTo());

        getDatabase().insert(getTable(), null, contentValues);

    }


    public ArrayList<mDiscount> get(String doc_id) {
        ArrayList<mDiscount> discounts = new ArrayList<mDiscount>();
        String query = "Select * from " + getTable() + " where MAIN_ID ='" + doc_id + "'";
        Cursor c = getDatabase().rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    mDiscount discount = new mDiscount();
                    discount.setType(eDiscount.valueOf(c.getString(c.getColumnIndex("type"))));
                    discount.setId(c.getInt(c.getColumnIndex("DISCOUNT_ID")));
                    discount.setName(c.getString(c.getColumnIndex("NAME")));
                    discount.setMax(c.getDouble(c.getColumnIndex("MAX")));
                    discount.setPercent(c.getDouble(c.getColumnIndex("PERCENT")));
                    discount.setFrom(c.getDouble(c.getColumnIndex("FORM")));
                    discount.setTo(c.getDouble(c.getColumnIndex("TO")));
                    discounts.add(discount);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return discounts;
    }
    public mDiscount gets(String doc_id) {
        ArrayList<mDiscount> discounts = new ArrayList<mDiscount>();
        discounts = get(doc_id);
        if (discounts.size() > 0) {
            return discounts.get(0);
        }
        return null;
    }

}


