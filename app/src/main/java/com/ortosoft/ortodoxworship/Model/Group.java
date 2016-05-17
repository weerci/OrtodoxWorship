package com.ortosoft.ortodoxworship.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ortosoft.ortodoxworship.common.WorshipConst;
import com.ortosoft.ortodoxworship.common.WorshipErrors;
import com.ortosoft.ortodoxworship.db.Connect;
import com.ortosoft.ortodoxworship.db.SQLiteWorship;

import java.util.ArrayList;

/**
 * Created by admin on 11.05.2016.
 * Класс реализует функционла работы с группами людей, содержит код доступа к базе данных
 */
public class Group {

    public Group(long id, String name)
    {
        _id = id;
        _name = name;
    }
    public Group(String name) {
        _name = name;
    }

    private long _id = WorshipConst.EMPTY_ID;
    public long get_id(){
        return _id;
    }

    private String _name;
    public String get_name() {
        return _name;
    }
    public void set_name(String name){
        _name = name;
    }

    // Группа сохраняется в базе данных
    public long SaveOrUpdate() throws WorshipErrors
    {
        if (_name == null) {
            throw WorshipErrors.Item(1000, null);
        }

        SQLiteDatabase db = Connect.Item().get_db();
        ContentValues cv = new ContentValues();
        cv.put(TableGroup.COLUMN_NAME, _name);

        try {
            if (_id == WorshipConst.EMPTY_ID)
                _id = db.insertOrThrow(TableGroup.NAME, null, cv);
            else
                TableGroup.Update(_id, _name, db);
        } catch (SQLException e) {
            if (TableGroup.CheckUnique(e.getMessage()))
                throw WorshipErrors.Item(1001, null);
            else
                throw e;
        }

        return _id;
    }
    // Удаляется выбранная группа
    public static void Delete(Group group) throws WorshipErrors
    {
        SQLiteDatabase db = Connect.Item().get_db();
        try {
            TableGroup.Delete(group, db);
        } catch (Exception e) {
            throw e;
        }
    }
    // Удвляется список групп
    public static void Delete(Group[] groups) throws WorshipErrors
    {
        SQLiteDatabase db = Connect.Item().get_db();

        db.beginTransaction();
        try {
            for (Group g : groups)
                TableGroup.Delete(g, db);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        }
        finally {
            db.endTransaction();
        }
    }

    // Находится группа по переданному имени
    public static Group FindByName(String name)
    {
        SQLiteDatabase db = Connect.Item().get_db();
        Cursor mCursor = db.query(TableGroup.NAME, null, TableGroup.COLUMN_NAME + " = ?", new String[] { name }, null, null, null);

        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()){
                long found_id = mCursor.getLong(TableGroup.COLUMN_ID_NUM);
                String found_name  = mCursor.getString(TableGroup.COLUMN_NAME_NUM);
                return new Group(found_id, found_name);
            } else {
                return null;
            }
        } finally {
            mCursor.close();
        }
    }
    // Получение всех групп
    public static ArrayList<Group> FindAll()
    {
        SQLiteDatabase db = Connect.Item().get_db();
        Cursor mCursor = db.query(TableGroup.NAME, null, null, null, null, null, TableGroup.COLUMN_NAME);
        ArrayList<Group> arrayList = new ArrayList<>();

        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()){
                do{
                long found_id = mCursor.getLong(TableGroup.COLUMN_ID_NUM);
                String found_name  = mCursor.getString(TableGroup.COLUMN_NAME_NUM);
                arrayList.add(new Group(found_id, found_name));
                } while (mCursor.moveToNext());
            }
        } finally {
            mCursor.close();
        }
        return arrayList;
    }

    // Описание таблицы GROUPS
    public static class TableGroup {
        public static final String NAME = "groups";

        // Названия столбцов
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";

        // Номера столбцов
        public static final int COLUMN_ID_NUM = 0;
        public static final int COLUMN_NAME_NUM = 1;

        private static void Update(long id, String name, SQLiteDatabase db){
            String sql = String.format("update %1$s set %2$s = '%3$s' where %4$s = %5$s", NAME, COLUMN_NAME, name, COLUMN_ID, id);
            db.execSQL(sql);
        }
        private static void Delete(Group group, SQLiteDatabase db){
            String sql = String.format("delete from %1$s where %2$s = %3$s", NAME, COLUMN_ID, group.get_id());
            db.execSQL(sql);
        }
        private static boolean CheckUnique(String msg)
        {
            return msg.startsWith("UNIQUE constraint");
        }
    }
}