package com.ortosoft.ortodoxworship.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.ortosoft.ortodoxworship.App;
import com.ortosoft.ortodoxworship.R;

import java.util.ArrayList;

/**
 * Created by admin on 11.05.2016.
 */
public class SQLiteWorship extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ortoworship.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteWorship _sqLiteWorship;
    private SQLiteDatabase _db;

    String[] _script;

    private SQLiteWorship(){
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        _script = App.getContext().getResources().getStringArray(R.array.script_ddl);
    }

    // Создание единственного экземпляра класса
    public static SQLiteWorship Item(){
        if (_sqLiteWorship == null) {
            _sqLiteWorship = new SQLiteWorship();
            _sqLiteWorship._db = _sqLiteWorship.getWritableDatabase();
        }
        return  _sqLiteWorship;
    }

    // Возвращается база данных
    public SQLiteDatabase get_db(){
        return _db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            for (String s : _script) {
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

    @Override
    public void onConfigure(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }
}
