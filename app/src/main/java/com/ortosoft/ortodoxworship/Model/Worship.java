package com.ortosoft.ortodoxworship.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ortosoft.ortodoxworship.common.WorshipConst;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dima on 16.05.2016.
 */
public class Worship {

    private long _id = WorshipConst.EMPTY_ID;
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
    public static Worship FindByName(String worshipName, Language lang)
    {
        return null;
    }

    public enum Language { latin, greek, rus, eng }

    public static class TableWorship {
        // Название таблицы
        public static final String TABLE_NAME = "worship";

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
