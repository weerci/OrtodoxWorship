package com.ortosoft.ortodoxworship.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ortosoft.ortodoxworship.bus.BusGroup;
import com.ortosoft.ortodoxworship.common.State;
import com.ortosoft.ortodoxworship.common.WorshipConst;
import com.ortosoft.ortodoxworship.common.WorshipErrors;
import com.ortosoft.ortodoxworship.db.Connect;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 11.05.2016.
 * Класс реализует функционла работы с группами людей, содержит код доступа к базе данных
 */
public class Group  {

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

    private HashMap<Long, Member> _members = new HashMap<>();
    public HashMap<Long, Member> get_members() {
        return _members;
    }

    // Добавляет нового человека в список группы
    public void AddMember(Member member){
        _members.put(member.get_id(), member);
    }
    // Удаляет человека из группы
    public void RemoveMember(Member member){
        _members.remove(member.get_id());
    }

    private void load_members() {
        _members = TableGroup.LoadMembersOfGroup(_id, Connect.Item().get_db());
    }

    // Группа сохраняется в базе данных
    public long SaveOrUpdate() throws WorshipErrors {
        if (_name == null) {
            throw WorshipErrors.Item(1000, null);
        }

        SQLiteDatabase db = Connect.Item().get_db();
        ContentValues cv = new ContentValues();
        cv.put(TableGroup.COLUMN_NAME, _name);

        try {
            if (_id == WorshipConst.EMPTY_ID)
                _id = db.insertOrThrow(TableGroup.NAME, null, cv);
            else{
                TableGroup.Update(_id, _name, db);
                BusGroup.Item().EventUpdateGroup(this);
            }

            if (_members.size() > 0){
                Member.TableMembersGroups.DeleteByGroup (_id, db);
                for (Member m: _members.values())
                    Member.TableMembersGroups.Insert(m.get_id(), _id, db);
            }

        } catch (SQLException e) {
            if (TableGroup.CheckUnique(e.getMessage()))
                throw WorshipErrors.Item(1001, null);
            else
                throw e;
        }

        return _id;
    }
    // Удаляется выбранная группа
    public static void Delete(Group group) throws WorshipErrors {
        SQLiteDatabase db = Connect.Item().get_db();
        try {
            TableGroup.Delete(group, db);
            BusGroup.Item().EventDeleteGroup(group);
        } catch (Exception e) {
            throw e;
        }
    }
    // Удвляется список групп
    public static void Delete(Group[] groups) throws WorshipErrors {
        SQLiteDatabase db = Connect.Item().get_db();

        db.beginTransaction();
        try {
            for (Group g : groups) {
                TableGroup.Delete(g, db);
                BusGroup.Item().EventDeleteGroup(g);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        }
        finally {
            db.endTransaction();
        }
    }

    // Находится группа по переданному имени
    public static Group FindByName(String name) {
        SQLiteDatabase db = Connect.Item().get_db();
        Cursor mCursor = db.query(TableGroup.NAME, null, TableGroup.COLUMN_NAME + " = ?", new String[] { name }, null, null, null);

        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()){
                long found_id = mCursor.getLong(TableGroup.COLUMN_ID_NUM);
                String found_name  = mCursor.getString(TableGroup.COLUMN_NAME_NUM);
                Group group =  new Group(found_id, found_name);
                group.load_members();
                return group;
            } else {
                return null;
            }
        } finally {
            mCursor.close();
        }
    }
    // Получение всех групп
    public static ArrayList<Group> FindAll() {
        SQLiteDatabase db = Connect.Item().get_db();
        Cursor mCursor = db.query(TableGroup.NAME, null, null, null, null, null, TableGroup.COLUMN_NAME);
        ArrayList<Group> arrayList = new ArrayList<>();

        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()){
                do{
                long found_id = mCursor.getLong(TableGroup.COLUMN_ID_NUM);
                String found_name  = mCursor.getString(TableGroup.COLUMN_NAME_NUM);
                Group group = new Group(found_id, found_name);
                group.load_members();
                arrayList.add(group);
                } while (mCursor.moveToNext());
            }
        } finally {
            mCursor.close();
        }
        return arrayList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (_id != group._id) return false;
        return _name.equals(group._name);
    }
    @Override
    public int hashCode() {
        int result = (int) (_id ^ (_id >>> 32));
        result = 31 * result + _name.hashCode();
        return result;
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
        private static boolean CheckUnique(String msg){
            return msg.startsWith("UNIQUE constraint");
        }
        public static HashMap<Long, Member> LoadMembersOfGroup(long _id, SQLiteDatabase db){
            HashMap<Long, Member> hashMap = new HashMap<>();
            String sql = String.format("select m.* from members_groups mg left join members m on mg.id_members = m._id where mg.id_groups = %1$d", _id);
            Cursor mCursor = db.rawQuery(sql, new String [] {});
            try {
                mCursor.moveToFirst();
                if (!mCursor.isAfterLast()) {
                    do {
                        long id = mCursor.getLong(Member.TableMember.COLUMN_ID_NUM);
                        String name = mCursor.getString(Member.TableMember.COLUMN_NAME_NUM);
                        String comment = mCursor.getString(Member.TableMember.COLUMN_COMMENT_NUM);
                        State.IsBaptized isBaptized = State.IntToBaptized(mCursor.getInt(Member.TableMember.COLUMN_BAPTIZED_NUM));
                        State.IsDead isDead = State.IntToDead(mCursor.getInt(Member.TableMember.COLUMN_IS_DEAD_NUM));
                        hashMap.put(id, new Member(id, name, comment, isBaptized, isDead));
                    } while (mCursor.moveToNext());
                }
            } finally {
                mCursor.close();
            }
            return hashMap;
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