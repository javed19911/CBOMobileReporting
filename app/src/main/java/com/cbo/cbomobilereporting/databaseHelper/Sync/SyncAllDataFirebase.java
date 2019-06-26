package com.cbo.cbomobilereporting.databaseHelper.Sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uenics.javed.CBOLibrary.CboProgressDialog;

import java.util.HashMap;
import java.util.Map;

import FirebaseDatabase.FirebsaeDB;

import static utils_new.Custom_Variables_And_Method.FMCG_PREFRENCE;
import static utils_new.Custom_Variables_And_Method.db;

public class SyncAllDataFirebase {

    Context context;
    CBO_DB_Helper db_helper;
    SyncDB syncDB;
    CboProgressDialog cboProgressDialog ;

    public SyncAllDataFirebase(Context context) {
        this.context = context;
        db_helper = new CBO_DB_Helper(context);
        syncDB = new SyncDB();



    }

    public void upload(){
        syncDB.delete(null);

        syncDB.insert(getAllPreferences(),"Preferences");
        syncDB.insert(getAllTableData(),"DB_OLD");
    }

    public Map<String, ?> getAllPreferences(){
        Map<String, ?> allEntries = context.getSharedPreferences(FMCG_PREFRENCE, Context.MODE_PRIVATE).getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
       /* Map<String, Map<String, ?>> pref = new HashMap<>();
        pref.put("Firebase", allEntries);*/
        return allEntries;
    }

    public Map<String, ?> getAllTableData(){
        String qry = "SELECT name FROM sqlite_master WHERE type='table' and name not in ('android_metadata', 'cbo_login')";
        SQLiteDatabase db = db_helper.getWritableDatabase();
        //Map<String, Map<String, ?>> Database = new HashMap<>();
        Map<String, Map<String, ?>> Table = new HashMap<>();
        //Database.put("DB_OLD", Table);
        Cursor c = null;
        Cursor cTable = null;
        String tableName;
        try {
            c = db.rawQuery(qry, null);
            if (c.moveToFirst()) {
                do {
                    tableName = c.getString(c.getColumnIndex("name"));
                    Map<String, Map<String, ?>> TableData = new HashMap<>();
                    Table.put(tableName, TableData);
                    Log.d("map values", " Table Start : " + tableName);


                    int rowCount = 0;
                    cTable = db.rawQuery("Select * from " + tableName , null);
                    if (cTable.moveToFirst()) {
                        do {

                            Map<String, String> row = new HashMap<>();
                            Log.d("map values", " -------------------------------------------------------------------");
                            for (int i =0 ; i < cTable.getColumnCount(); i++) {
                                row.put(cTable.getColumnName(i), cTable.getString(i));
                                Log.d("map values", cTable.getColumnName(i) + ": " + cTable.getString(i));
                            }
                            Log.d("map values", " -------------------------------------------------------------------");
                            TableData.put(""+rowCount, row);
                            rowCount++;
                        } while (cTable.moveToNext());
                    }


                    Log.d("map values", " Table End : " + tableName);
                } while (c.moveToNext());
            }
        }finally {
            if (cTable != null) {
                cTable.close();
            }
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return Table;
    }


    public void download(){
        Log.d("map values", "Download started......");
        cboProgressDialog = new CboProgressDialog(context, "Please Wait..\nDownloading data for Support ");
        cboProgressDialog.show();
        syncDB.getDbRef(new FirebsaeDB.ILogin() {
            @Override
            public void onSuccess(DatabaseReference DBRef) {
                Log.d("map values", "Login success...... at ref\n" + DBRef.toString());
                DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("map values", dataSnapshot.toString());
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Log.d("map values", "getting datas......");
                            switch (postSnapshot.getKey()){
                                case "Preferences" :
                                    cboProgressDialog.setDescription("Please Wait..\nDownloading data for Support \nDownloading User Settings...");
                                    for (DataSnapshot preferences : postSnapshot.getChildren()) {
                                        Log.d("map values", preferences.getKey() + ": " + preferences.getValue());
                                        MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE(preferences.getKey(),preferences.getValue().toString());
                                    }
                                    break;
                                case "DB_OLD":

                                    SQLiteDatabase db = null;
                                    try {
                                         db = db_helper.getWritableDatabase();
                                        for (DataSnapshot table : postSnapshot.getChildren()) {

                                            String qry = "DELETE FROM " + table.getKey();
                                            db.execSQL(qry);

                                            Log.d("map values", " Table Start : " + table.getKey());
                                            cboProgressDialog.setDescription("Please Wait..\nDownloading data for Support \nDownloading User Data...\n" + table.getKey());


                                            for (DataSnapshot row : table.getChildren()) {
                                                ContentValues cv = new ContentValues();
                                                Log.d("map values", " -------------------------------------------------------------------");
                                                for (DataSnapshot column : row.getChildren()) {
                                                    Log.d("map values", column.getKey() + ": " + column.getValue());
                                                    cv.put(column.getKey(), column.getValue().toString());
                                                }
                                                Log.d("map values", " -------------------------------------------------------------------");
                                                db.insert(table.getKey(), null, cv);
                                            }

                                            Log.d("map values", " Table Ends : " + table.getKey());
                                        }
                                    }finally {
                                        assert db != null;
                                        db.close();
                                    }
                                    break;
                            }


                        }
                        cboProgressDialog.setDescription("Please Wait..\nDownloading completed !!!!");
                        DBRef.removeEventListener(this);
                        cboProgressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        DBRef.removeEventListener(this);
                        System.out.println(databaseError.getMessage());
                        Log.d("map values", "error :" + databaseError.getMessage());
                        cboProgressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onError(String title, String description) {
                Log.d("map values", "Login error......\n"+description);
            }
        });
    }
}
