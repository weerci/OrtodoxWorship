package com.ortosoft.ortodoxworship.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by admin on 17.05.2016.
 */
public class Connect {
    // region Fields

    private static SQLiteDatabase mDataBase;
    private static Connect mConnect;

    // endregion

    private Connect()
    {
        mDataBase = SQLiteDatabase.openDatabase(SQLiteWorship.DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    // Статический конструктор для класса
    public static Connect Item(){
        if (mConnect == null) {
            mConnect = new Connect();
        }
        return  mConnect;
    }

    public SQLiteDatabase get_db() {
        return mDataBase;
    }


}
