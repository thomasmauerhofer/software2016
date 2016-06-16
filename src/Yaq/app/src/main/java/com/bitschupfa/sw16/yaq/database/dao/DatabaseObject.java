package com.bitschupfa.sw16.yaq.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bitschupfa.sw16.yaq.database.helper.DBHelper;

import java.util.HashMap;

/**
 * Created by Patrik on 05.05.2016.
 */
public abstract class DatabaseObject {
    ContentValues contentValues;
    protected String tableName;

    public DatabaseObject() {
        contentValues = new ContentValues();
    }

    protected abstract void fillDatabaseContentValues(boolean initial);
    
    protected void editEntry(Context context, String idRowName, int id){
        fillDatabaseContentValues(false);
        SQLiteDatabase database = DBHelper.instance(context).getInsertionDatabase();
        database.update(tableName, contentValues, idRowName + " = ?", new String[] {""+id});
        database.close();
    }

    protected void deleteEntry(Context context, String idRowName, int id){
        SQLiteDatabase database = DBHelper.instance(context).getInsertionDatabase();
        database.delete(tableName, idRowName + " = ?", new String[]{"" + id});
    }

    private void insertIntoDatabase(SQLiteDatabase database, boolean initial){
        fillDatabaseContentValues(initial);

        Log.e("Database", "Inserted data into Table: " + tableName);
        database.insert(tableName, null, contentValues);
        StringBuilder stringBuilder = new StringBuilder();
        for(String key : contentValues.keySet()){
            stringBuilder.append(key+": ");
            stringBuilder.append(contentValues.getAsString(key)+" ");
        }
        Log.e("Database", "Inserted: "+stringBuilder.toString());
    }

    public void insertIntoDatabase(Context context) {
        SQLiteDatabase database = DBHelper.instance(context).getInsertionDatabase();
        insertIntoDatabase(database, false);
        database.close();
    }

    public void insertThisAsInitialBaselineIntoDatabase(SQLiteDatabase database){
        insertIntoDatabase(database, true);
    }
}
