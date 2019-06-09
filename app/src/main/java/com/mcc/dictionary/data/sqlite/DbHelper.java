package com.mcc.dictionary.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper dbHelper = null;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dictionary.db";


    static DbHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DbHelper(context);
        }
        return dbHelper;
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstants.SQL_CREATE_FAVOURITE_ENTRIES);
        db.execSQL(DbConstants.SQL_CREATE_HISTORY_ENTRIES);
        //add word
        db.execSQL(DbConstants.SQL_CREATE_WORD_LISTS_ENTRIES);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbConstants.SQL_DELETE_FAVOURITE_ENTRIES);
        db.execSQL(DbConstants.SQL_DELETE_HISTORY_ENTRIES);
        // add word
        db.execSQL(DbConstants.SQL_DELETE_WORD_LISTS_ENTRIES);


        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



}