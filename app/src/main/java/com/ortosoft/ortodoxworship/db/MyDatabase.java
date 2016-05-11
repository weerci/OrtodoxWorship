package com.ortosoft.ortodoxworship.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by admin on 11.05.2016.
 */
public class MyDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ortoworship.db";
    private static final int DATABASE_VERSION = 1;

    private ArrayList<String> mQueries = new ArrayList<>();

    public MyDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mQueries.add("CREATE TABLE groups (_id INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL, name TEXT NOT NULL); ");
        mQueries.add("CREATE UNIQUE INDEX idx_groups_name ON groups (name); ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            for (String s : mQueries) {
                db.execSQL(s);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
