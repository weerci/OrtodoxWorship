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

    private long _id = WorshipConst.EMPTY_ID;

    public Worship(long _id, String _name) {
        this._id = _id;
        this._name = _name;
    }

    public long get_id() {
        return _id;
    }

    private String _name;
    public String get_name() {
        return _name;
    }

    private HashMap<Long, Member> _membersOfWorship = new HashMap<>();
    public HashMap<Long, Member> get_membersOfWorship() {
        return _membersOfWorship;
    }

    private HashMap<Long, Prayer> _prayers = new HashMap<>();
    public HashMap<Long, Prayer> get_prayers() { return _prayers; }

    private Prayer _currentPrayer;
    public Prayer get_currentPrayer() {
        return _currentPrayer;
    }
    public void set_currentPrayer(Prayer _currentPrayer) {
        this._currentPrayer = _currentPrayer;
    }

    private Language _currentLanguage;
    public Language get_currentLanguage() {
        return _currentLanguage;
    }
    public void set_currentLanguage(Language _currentLanguage) {
        this._currentLanguage = _currentLanguage;
    }

    // Находит молитвословие по его имени
    public static Worship FindByName(String name, Language lang)
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
    }

    public static class TableWorshipsMembers {
        // Название таблицы
        public static final String NAME = "worships_members";

        // Название столбцов
        public static final String COLUMN_ID_MEMBER = "id_worship";
        public static final String COLUMN_ID_GROUP = "id_member";
        public static final String COLUMN_BY_ORD = "by_ord";

        // Номера столбцов
        public static final int NUM_COLUMN_ID_WORSHIP = 0;
        public static final int NUM_COLUMN_ID_MEMBER = 1;
        public static final int NUM_COLUMN_BY_ORD = 2;
    }

}
