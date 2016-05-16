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
        public static final String NAME = "members_groups";

        // Названия столбцов
        private static final String COLUMN_ID_MEMBER = "id_members";
        private static final String COLUMN_ID_GROUP = "id_groups";

        // Номера столбцов
        private static final int COLUMN_ID_MEMBER_NUM = 0;
        private static final int COLUMN_ID_GROUP_NUM = 1;

        // Удаляются все записи связанные с конкретным пользователем
        public static void DeleteByMember(long idMember, SQLiteDatabase db)
        {
            String sql = String.format("delete from %1$s where %2$s = %3$s", NAME, COLUMN_ID_MEMBER, idMember);
            db.execSQL(sql);
        }

        // Удаляются все записи связанные с конкретной группой
        public static void DeleteByGroup(long idGroup, SQLiteDatabase db)
        {
            String sql = String.format("delete from %1$s where %2$s = %3$s", NAME, COLUMN_ID_GROUP, idGroup);
            db.execSQL(sql);
        }

        // Создается запись о принадлежности человека группе
        public static void Insert(long idMember, long idGroup, SQLiteDatabase db)
        {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ID_MEMBER, idMember);
            cv.put(COLUMN_ID_GROUP, idGroup);

            db.insertOrThrow(NAME, null, cv);
        }

        // Выбирается список групп ассоциированных с пользователем
        public static ArrayList<Group> LoadGroupOfMember(long _id, SQLiteDatabase db)
        {
            ArrayList<Group> arrayList = new ArrayList<>();
            String sql = String.format("select g.* from members_groups mg left join groups g on mg.id_groups = g._id where mg.id_members = %1$d", _id);
            Cursor mCursor = db.rawQuery(sql, new String [] {});
            try {
                mCursor.moveToFirst();
                if (!mCursor.isAfterLast()) {
                    do {
                        long id = mCursor.getLong(Group.TableGroup.COLUMN_ID_NUM);
                        String name = mCursor.getString(Group.TableGroup.COLUMN_NAME_NUM);
                        arrayList.add(new Group(id, name));
                    } while (mCursor.moveToNext());
                }
            } finally {
                mCursor.close();
            }
            return arrayList;
        }

    }
}
