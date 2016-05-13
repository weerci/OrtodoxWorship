package com.ortosoft.ortodoxworship.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ortosoft.ortodoxworship.common.WorshipConst;
import com.ortosoft.ortodoxworship.common.WorshipErrors;
import com.ortosoft.ortodoxworship.db.SQLiteWorship;

/**
 * Created by admin on 11.05.2016.
 * Класс реализует функционла работы с группами людей, содержит код доступа к базе данных
 */
public class Group {
    private long _id = WorshipConst.EMPTY_ID;
    private String _name;

    // Создает группу с заданным именем
    public Group(String name) {
        _name = name;
    }

    // Используется для создания экземпляра из базы данных
    private Group(long id, String name)
    {
        _id = id;
        _name = name;
    }

    public long get_id(){
        return _id;
    }

    // Возвращает имя группы
    public String get_name() {
        return _name;
    }

    // Устанавливается имя группы
    public void set_name(String name){
        _name = name;
    }

    // Группа сохраняется в базе данных
    public long SaveOrUpdate() throws WorshipErrors
    {
        if (_name == null) {
            throw WorshipErrors.Item(1000, null);
        }

        SQLiteDatabase db = SQLiteWorship.Item().get_db();
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
    public static void Delete(Group group){
        SQLiteDatabase db = SQLiteWorship.Item().get_db();
        db.delete(TableGroup.NAME, TableGroup.COLUMN_ID + " = ?", new String[] { String.valueOf(group.get_id())});
    }

    // Находится группа по переданному имени
    public static Group FindByName(String name) {
        SQLiteDatabase db = SQLiteWorship.Item().get_db();
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

    // Описание таблицы GROUPS
    private static class TableGroup {
        public static final String NAME = "groups";

        // Названия столбцов
        private static final String COLUMN_ID = "_id";
        private static final String COLUMN_NAME = "name";

        // Номера столбцов
        private static final int COLUMN_ID_NUM = 0;
        private static final int COLUMN_NAME_NUM = 1;

        private static final void Update(long id, String name, SQLiteDatabase db){
            String sql = String.format("update %1$s set %2$s = '%3$s' where %4$s = %5$s", NAME, COLUMN_NAME, name, COLUMN_ID, id);
            db.execSQL(sql);
        }

        private static boolean CheckUnique(String msg)
        {
            return msg.startsWith("UNIQUE constraint");
        }
    }
}