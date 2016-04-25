package com.bitschupfa.sw16.yaq.database.helper;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "yaq.db";
    private static final int DATABASE_VERSION = 1;
    private static DBHelper instance_ = null;

    private SQLiteDatabase dataBase;
    private final Context dbContext;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.dbContext = context;
        // checking database and open it if exists
        if (checkDataBase()) {
            openDataBase();
        } else
        {
            try {
                this.getReadableDatabase();
                copyDataBase();
                this.close();
                openDataBase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    public static DBHelper instance(Context myContext) {

        if(instance_ == null) {
            instance_ = new DBHelper(myContext);
        }
        return instance_;
    }

    private void copyDataBase() throws IOException{
        AssetManager assetManager = dbContext.getAssets();
        InputStream myInput = assetManager.open(DATABASE_NAME);
        File databasePath = dbContext.getDatabasePath(DATABASE_NAME);
        String databasePathName = databasePath.getPath();
        OutputStream myOutput = new FileOutputStream(databasePathName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private void openDataBase() throws SQLException {
        File databasePath = dbContext.getDatabasePath(DATABASE_NAME);
        String databasePathName = databasePath.getPath();
        dataBase = SQLiteDatabase.openDatabase(databasePathName, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        boolean exist = false;
        try {
            File databasePath = dbContext.getDatabasePath(DATABASE_NAME);
            String databasePathName = databasePath.getPath();
            checkDB = SQLiteDatabase.openDatabase(databasePathName, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.v("db log", "database does't exist");
        }

        if (checkDB != null) {
            exist = true;
            checkDB.close();
        }
        return exist;
    }

    public SQLiteDatabase getDatabase(){
        return this.dataBase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}