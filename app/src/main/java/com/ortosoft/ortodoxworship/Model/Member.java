package com.ortosoft.ortodoxworship.Model;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ortosoft.ortodoxworship.common.State;
import com.ortosoft.ortodoxworship.common.WorshipConst;
import com.ortosoft.ortodoxworship.common.WorshipErrors;
import com.ortosoft.ortodoxworship.db.SQLiteWorship;

/**
 * Created by admin on 16.05.2016.
 */
public class Member
{
    private long _id = WorshipConst.EMPTY_ID;
    private long get_id() { return _id; }

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

    // region Constructors
    public Member(String name, String description, State.IsBaptized isBaptized, State.IsDead isDead) {
        _name = name;
        _comment = description;
        _isBaptized = isBaptized;
        _isIsDead = isDead;
    }
    public Member(String name, String description) {
        _name = name;
        _comment = description;
    }
    // endregion

    public static void Delete(Member member)
    {
        SQLiteDatabase db = SQLiteWorship.Item().get_db();
        try {
            TableMember.Delete(member, db);
        } catch (Exception e) {
            throw e;
        }
    }

    public long SaveOrUpdate() throws WorshipErrors
    {
        if (_name == null) {
            throw WorshipErrors.Item(1002, null);
        }

        SQLiteDatabase db = SQLiteWorship.Item().get_db();
        ContentValues cv = new ContentValues();
        cv.put(TableMember.COLUMN_NAME, _name);
        cv.put(TableMember.COLUMN_COMMENT, _comment);
        cv.put(TableMember.COLUMN_BAPTIZED, State.BaptizedToInt(_isBaptized));
        cv.put(TableMember.COLUMN_IS_DEAD, State.DeadToInt(_isIsDead));

        try {
            if (_id == WorshipConst.EMPTY_ID)
                _id = db.insertOrThrow(TableMember.NAME, null, cv);
            else
                TableMember.Update(_id, _name, _comment, State.BaptizedToInt(_isBaptized), State.DeadToInt(_isIsDead), db);
        } catch (SQLException e) {
            throw e;
        }

        return _id;
    }

    private static class TableMember {
        public static final String NAME = "members";

        // Названия столбцов
        private static final String COLUMN_ID = "_id";
        private static final String COLUMN_NAME = "name";
        private static final String COLUMN_COMMENT = "comment";
        private static final String COLUMN_IS_DEAD = "is_dead";
        private static final String COLUMN_BAPTIZED = "baptized";

        // Номера столбцов
        private static final int COLUMN_ID_NUM = 0;
        private static final int COLUMN_NAME_NUM = 1;
        private static final int COLUMN_COMMENT_NUM = 2;
        private static final int COLUMN_IS_DEAD_NUM = 3;
        private static final int COLUMN_BAPTIZED_NUM = 4;

        private static void Update(long id, String name, String comment, int isBaptized, int isDead, SQLiteDatabase db){
            String sql = String.format("update %1$s set %2$s = '%3$s', %6$s = '%7$s', %8$s = %9$s, %10$s = %11$s, %12$s = %13$s where %4$s = %5$s",
                    NAME, COLUMN_NAME, name, COLUMN_ID, id, COLUMN_COMMENT, comment, COLUMN_BAPTIZED, isBaptized, COLUMN_IS_DEAD, isDead);
            db.execSQL(sql);
        }

        private static void Delete(Member member, SQLiteDatabase db){
            String sql = String.format("delete from %1$s where %2$s = %3$s", NAME, COLUMN_ID, member.get_id());
            db.execSQL(sql);
        }

    }
}
