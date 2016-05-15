package com.bitschupfa.sw16.yaq.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bitschupfa.sw16.yaq.database.helper.DBHelper;

/**
 * Created by Patrik on 05.05.2016.
 */
public abstract class DatabaseObject {
    ContentValues contentValues;
    protected String tableName;

    public DatabaseObject(){
        contentValues = new ContentValues();
    }

    protected abstract void fillDatabaseContentValues();

    private void insertIntoDatabase(SQLiteDatabase database){
        fillDatabaseContentValues();

        Log.e("Database", "Table Name: " + tableName);
        database.insert(tableName, null, contentValues);
    }

    public void insertIntoDatabase(Context context){
        SQLiteDatabase database = DBHelper.instance(context).getInsertionDatabase();
        insertIntoDatabase(database);
        database.close();
    }

    public void insertThisAsInitialBaselineIntoDatabase(SQLiteDatabase database){
        insertIntoDatabase(database);
    }
}
