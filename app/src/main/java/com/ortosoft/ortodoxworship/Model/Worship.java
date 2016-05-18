package com.ortosoft.ortodoxworship.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ortosoft.ortodoxworship.common.WorshipConst;
import com.ortosoft.ortodoxworship.db.Connect;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dima on 16.05.2016.
 */
public class Worship {

    public Worship(long id, String name) {
        _id = id;
        _name = name;
    }

    private long _id = WorshipConst.EMPTY_ID;
    public long get_id() {
        return _id;
    }

    private String _name;
    public String get_name() {
        return _name;
    }
    public void set_name(String _name) {
        this._name = _name;
    }

    // Находит молитвословие по его имени
    public static Worship FindByName(String name)
    {
        SQLiteDatabase db = Connect.Item().get_db();
        Cursor mCursor = db.query(TableWorship.NAME, null, TableWorship.COLUMN_NAME + " = ?", new String[] { name }, null, null, null);

        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()){
                long found_id = mCursor.getLong(TableWorship.NUM_COLUMN_ID);
                String found_name  = mCursor.getString(TableWorship.NUM_COLUMN_NAME);
                return new Worship(found_id, found_name);
            } else {
                return null;
            }
        } finally {
            mCursor.close();
        }
    }

    public enum Language { latin, greek, rus, eng }

    public static class TableWorship {
        // Название таблицы
        public static final String NAME = "worships";

        // Название столбцов
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";

        // Номера столбцов
        public static final int NUM_COLUMN_ID = 0;
        public static final int NUM_COLUMN_NAME = 1;

        public static int CountOfRows(){
            SQLiteDatabase db = Connect.Item().get_db();
            Cursor mCursor = db.query(NAME, null, null, null, null, null, null);

            try {
                return mCursor.getCount();
            } finally {
                mCursor.close();
            }
        }
    }
    public static class TableWorshipsMembers {
        // Название таблицы
        public static final String NAME = "worships_members";

        // Название столбцов
        public static final String COLUMN_ID_WORSHIP = "id_worship";
        public static final String COLUMN_ID_MEMBER = "id_member";

        // Номера столбцов
        public static final int NUM_COLUMN_ID_WORSHIP = 0;
        public static final int NUM_COLUMN_ID_MEMBER = 1;

        public static void Insert(long idWorship,  long idMember, SQLiteDatabase db){
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ID_WORSHIP, idWorship);
            cv.put(COLUMN_ID_MEMBER, idMember);

            db.insertOrThrow(NAME, null, cv);
        }
        // Удаляются все записи связанные с конкретным пользователем
        public static void DeleteByMember(long idMember, SQLiteDatabase db){
            String sql = String.format("delete from %1$s where %2$s = %3$s", NAME, COLUMN_ID_MEMBER, idMember);
            db.execSQL(sql);
        }
        public static int CountOfRows(){
            SQLiteDatabase db = Connect.Item().get_db();
            Cursor mCursor = db.query(NAME, null, null, null, null, null, null);

            try {
                return mCursor.getCount();
            } finally {
                mCursor.close();
            }
        }

    }

}
