package com.ortosoft.ortodoxworship.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ortosoft.ortodoxworship.db.Connect;

/**
 * Created by dima on 16.05.2016.
 */
public class Prayer {
    private long _id;
    public long get_id() {
        return _id;
    }
    public void set_id(long _id) {
        this._id = _id;
    }


    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private String body;
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    private String comment;
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public static class TablePrayers {
        public static final String NAME = "prayers";

        // Названия столбцов
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_COMMENT = "comment";

        // Номера столбцов
        public static final int COLUMN_ID_NUM = 0;
        public static final int COLUMN_NAME_NUM = 1;
        public static final int COLUMN_BODY_NUM = 2;
        public static final int COLUMN_COMMENT_NUM = 3;

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

        public static int CountOfRows()
        {
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
