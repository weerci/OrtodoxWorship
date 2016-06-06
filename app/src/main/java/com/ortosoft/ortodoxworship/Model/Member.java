package com.ortosoft.ortodoxworship.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ortosoft.ortodoxworship.bus.BusGroup;
import com.ortosoft.ortodoxworship.bus.BusMember;
import com.ortosoft.ortodoxworship.bus.EventGroup;
import com.ortosoft.ortodoxworship.common.State;
import com.ortosoft.ortodoxworship.common.WorshipConst;
import com.ortosoft.ortodoxworship.common.WorshipErrors;
import com.ortosoft.ortodoxworship.db.Connect;
import com.ortosoft.ortodoxworship.db.SQLiteWorship;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 16.05.2016.
 */
public class Member implements EventGroup {

    private long _id = WorshipConst.EMPTY_ID;
    public long get_id() { return _id; }

    private String _name;
    public String get_name() {
        return _name;
    }
    public void set_name(String _name) {
        this._name = _name;
    }

    private String _comment;
    public String get_comment() {
        return _comment;
    }
    public void set_comment(String _description) {
        this._comment = _description;
    }

    private State.IsBaptized _isBaptized = State.IsBaptized.unknown;
    public State.IsBaptized get_isBaptized() {
        return _isBaptized;
    }
    public void set_isBaptized(State.IsBaptized _isBaptized) {
        this._isBaptized = _isBaptized;
    }

    private State.IsDead _isIsDead = State.IsDead.unknown;
    public State.IsDead get_isIsDead() {
        return _isIsDead;
    }
    public void set_isIsDead(State.IsDead _isIsDead) {
        this._isIsDead = _isIsDead;
    }

    private HashMap<Long, Group> _mapOfGroup = new HashMap<>();
    public HashMap<Long,Group> get_listOfGroup() {return _mapOfGroup; }

    private ArrayList<Worship> _listOfWorship = new ArrayList<>();
    public ArrayList<Worship> get_listOfWorship() {
        return _listOfWorship;
    }

    // region Constructors
    public Member(long id, String name, String comment, State.IsBaptized isBaptized, State.IsDead isDead){
        this(name, comment, isBaptized, isDead);
        _id = id;
    }
    public Member(String name, String comment, State.IsBaptized isBaptized, State.IsDead isDead) {
        this(name, comment);
        _isBaptized = isBaptized;
        _isIsDead = isDead;
    }
    public Member(String name, String comment) {
        _name = name;
        _comment = comment;
        BusGroup.Item().AddToBus(this);
    }

    // endregion

    private void load_groups(){
        _mapOfGroup = TableMembersGroups.LoadGroupOfMember(_id, Connect.Item().get_db());
    }
    private void load_worship(){
        _listOfWorship = TableMembersGroups.LoadWorshipOfMember(_id, Connect.Item().get_db());
    }

    // Удаление человека
    public static void Delete(Member member){
        SQLiteDatabase db = Connect.Item().get_db();
        try {
            TableMember.Delete(member, db);
            BusMember.Item().EventDeleteMember(member);
            BusGroup.Item().RemoveFromBus(member);
        } catch (Exception e) {
            throw e;
        }
    }
    // Удвляется список людей
    public static void Delete(Member[] members) throws WorshipErrors{
        SQLiteDatabase db = Connect.Item().get_db();

        db.beginTransaction();
        try {
            for (Member m : members) {
                TableMember.Delete(m, db);
                BusMember.Item().EventDeleteMember(m);
                BusGroup.Item().RemoveFromBus(m);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        }
        finally {
            db.endTransaction();
        }
    }

    @Override
    public void finalize() throws Throwable {
        BusGroup.Item().RemoveFromBus(this);
        super.finalize();
    }

    // В базе ищется человек по заданному идентификатору
    public static Member FindById(long id)
    {
        SQLiteDatabase db = Connect.Item().get_db();
        Cursor mCursor = db.query(TableMember.NAME, null, TableMember.COLUMN_ID + " = ?", new String[] { String.valueOf(id) }, null, null, null);

        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()){
                long found_id = mCursor.getLong(TableMember.COLUMN_ID_NUM);
                String found_name  = mCursor.getString(TableMember.COLUMN_NAME_NUM);
                String found_comment  = mCursor.getString(TableMember.COLUMN_COMMENT_NUM);
                State.IsBaptized found_baptized  = State.IntToBaptized(mCursor.getInt(TableMember.COLUMN_BAPTIZED_NUM));
                State.IsDead found_dead  = State.IntToDead(mCursor.getInt(TableMember.COLUMN_IS_DEAD_NUM));
                Member member = new Member(found_id, found_name, found_comment, found_baptized, found_dead);
                member.load_groups();
                member.load_worship();
                return member;
            } else {
                return null;
            }
        } finally {
            mCursor.close();
        }
    }
    // Находятся все люди в базе
    public static ArrayList<Member> FindAll()
    {
        SQLiteDatabase db = Connect.Item().get_db();
        Cursor mCursor = db.query(TableMember.NAME, null, null, null, null, null, TableMember.COLUMN_NAME);
        ArrayList<Member> arrayList = new ArrayList<>();

        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()){
                do{
                    long found_id = mCursor.getLong(TableMember.COLUMN_ID_NUM);
                    String found_name  = mCursor.getString(TableMember.COLUMN_NAME_NUM);
                    String found_comment  = mCursor.getString(TableMember.COLUMN_COMMENT_NUM);
                    State.IsBaptized found_baptized  = State.IntToBaptized(mCursor.getInt(TableMember.COLUMN_BAPTIZED_NUM));
                    State.IsDead found_dead  = State.IntToDead(mCursor.getInt(TableMember.COLUMN_IS_DEAD_NUM));
                    Member member = new Member(found_id, found_name, found_comment, found_baptized, found_dead);
                    member.load_groups();
                    member.load_worship();
                    arrayList.add(member);
                } while (mCursor.moveToNext());
            }
        } finally {
            mCursor.close();
        }
        return arrayList;

    }

    // Человек сохраняется или его данные обновляются в базе данных
    public long SaveOrUpdate() throws WorshipErrors{
        if (_name == null) {
            throw WorshipErrors.Item(1002, null);
        }

        SQLiteDatabase db = Connect.Item().get_db();
        ContentValues cv = new ContentValues();
        cv.put(TableMember.COLUMN_NAME, _name);
        cv.put(TableMember.COLUMN_COMMENT, _comment);
        cv.put(TableMember.COLUMN_BAPTIZED, State.BaptizedToInt(_isBaptized));
        cv.put(TableMember.COLUMN_IS_DEAD, State.DeadToInt(_isIsDead));

        db.beginTransaction();
        try {

            try {
                if (_id == WorshipConst.EMPTY_ID) {
                    _id = db.insertOrThrow(TableMember.NAME, null, cv);
                    BusGroup.Item().AddToBus(this);
                }
                else {
                    TableMember.Update(_id, _name, _comment, State.BaptizedToInt(_isBaptized), State.DeadToInt(_isIsDead), db);
                    BusMember.Item().EventUpdateMember(this);
                }
            } catch (SQLException e) {
                throw e;
            }

            if (_mapOfGroup.size() > 0){
                TableMembersGroups.DeleteByMember(_id, db);
                for (Group g: _mapOfGroup.values())
                    TableMembersGroups.Insert(_id, g.get_id(), db);
            }

            if (_listOfWorship.size() > 0) {
                Worship.TableWorshipsMembers.DeleteByMember(_id, db);
                for (Worship w: _listOfWorship)
                    Worship.TableWorshipsMembers.Insert(w.get_id(), _id, db);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return _id;
    }

    // Добавление человека в группу
    public void AddToGroup(Group group)
    {
        _mapOfGroup.put(group.get_id(), group);
    }
    // Удаляем человека из группы
    public void RemoveFromGroup(Group group)
    {
        _mapOfGroup.remove(group.get_id());
    }
    // Привязка человека к молитвословию
    public void AddToWorship(Worship worship) {_listOfWorship.add(worship); }
    // Удаление человека из молитвословия
    public void RemoveFromWorship(Worship worship){
        _listOfWorship.remove(worship);
    }

    // region Implements EventGroup
    @Override
    public void OnAddedGroup(Group group) {

    }
    @Override
    public void OnUpdatedGroup(Group group) {
        Group g =_mapOfGroup.get(group.get_id());
        if (g != null) {
            g.set_name(group.get_name());
        }
    }
    @Override
    public void OnDeleteGroup(Group group) {
        _mapOfGroup.remove(group.get_id());
    }
    // endregion

    public static class TableMember {
        public static final String NAME = "members";

        // Названия столбцов
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COMMENT = "comment";
        public static final String COLUMN_IS_DEAD = "is_dead";
        public static final String COLUMN_BAPTIZED = "baptized";

        // Номера столбцов
        public static final int COLUMN_ID_NUM = 0;
        public static final int COLUMN_NAME_NUM = 1;
        public static final int COLUMN_COMMENT_NUM = 2;
        public static final int COLUMN_IS_DEAD_NUM = 3;
        public static final int COLUMN_BAPTIZED_NUM = 4;

        public static void Update(long id, String name, String comment, int isBaptized, int isDead, SQLiteDatabase db){
            String sql = String.format("update %1$s set %2$s = '%3$s', %6$s = '%7$s', %8$s = %9$s, %10$s = %11$s where %4$s = %5$s",
                    NAME, COLUMN_NAME, name, COLUMN_ID, id, COLUMN_COMMENT, comment, COLUMN_BAPTIZED, isBaptized, COLUMN_IS_DEAD, isDead);
            db.execSQL(sql);
        }
        public static void Delete(Member member, SQLiteDatabase db){
            String sql = String.format("delete from %1$s where %2$s = %3$s", NAME, COLUMN_ID, member.get_id());
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
    public static class TableMembersGroups {
        public static final String NAME = "members_groups";

        // Названия столбцов
        private static final String COLUMN_ID_MEMBER = "id_members";
        private static final String COLUMN_ID_GROUP = "id_groups";

        // Номера столбцов
        private static final int COLUMN_ID_MEMBER_NUM = 0;
        private static final int COLUMN_ID_GROUP_NUM = 1;

        // Удаляются все записи связанные с конкретным пользователем
        public static void DeleteByMember(long idMember, SQLiteDatabase db){
            String sql = String.format("delete from %1$s where %2$s = %3$s", NAME, COLUMN_ID_MEMBER, idMember);
            db.execSQL(sql);
        }

        // Удаляются все записи связанные с конкретной группой
        public static void DeleteByGroup(long idGroup, SQLiteDatabase db){
            String sql = String.format("delete from %1$s where %2$s = %3$s", NAME, COLUMN_ID_GROUP, idGroup);
            db.execSQL(sql);
        }

        // Создается запись о принадлежности человека группе
        public static void Insert(long idMember, long idGroup, SQLiteDatabase db){
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ID_MEMBER, idMember);
            cv.put(COLUMN_ID_GROUP, idGroup);

            db.insertOrThrow(NAME, null, cv);
        }

        // Выбирается список групп ассоциированных с пользователем
        public static HashMap<Long, Group> LoadGroupOfMember(long _id, SQLiteDatabase db){
            HashMap<Long, Group> mapList = new HashMap<>();
            String sql = String.format("select g.* from members_groups mg left join groups g on mg.id_groups = g._id where mg.id_members = %1$d", _id);
            Cursor mCursor = db.rawQuery(sql, new String [] {});
            try {
                mCursor.moveToFirst();
                if (!mCursor.isAfterLast()) {
                    do {
                        long id = mCursor.getLong(Group.TableGroup.COLUMN_ID_NUM);
                        String name = mCursor.getString(Group.TableGroup.COLUMN_NAME_NUM);
                        mapList.put(id, new Group(id, name));
                    } while (mCursor.moveToNext());
                }
            } finally {
                mCursor.close();
            }
            return mapList;
        }

        // Выбирается список молитвословий ассоциированных с пользователем
        public static ArrayList<Worship> LoadWorshipOfMember(long _id, SQLiteDatabase db){
            ArrayList<Worship> arrayList = new ArrayList<>();
            String sql = String.format("select w.* from worships_members wm left join worships w on wm.id_worship = w._id where wm.id_member = %1$d", _id);
            Cursor mCursor = db.rawQuery(sql, new String [] {});
            try {
                mCursor.moveToFirst();
                if (!mCursor.isAfterLast()) {
                    do {
                        long id = mCursor.getLong(Group.TableGroup.COLUMN_ID_NUM);
                        String name = mCursor.getString(Group.TableGroup.COLUMN_NAME_NUM);
                        arrayList.add(new Worship(id, name));
                    } while (mCursor.moveToNext());
                }
            } finally {
                mCursor.close();
            }
            return arrayList;
        }

        public static int CountOfRows() {
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
