package com.ortosoft.ortodoxworship.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ortosoft.ortodoxworship.App;
import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.Model.Prayer;
import com.ortosoft.ortodoxworship.Model.Worship;
import com.ortosoft.ortodoxworship.R;
import com.ortosoft.ortodoxworship.db.Connect;

import java.util.HashMap;

/**
 * Created by dima on 06.06.2016.
 */
public class LanguageHash {

    private long _id;

    public LanguageHash(long _id) {
        this._id = _id;
    }

    private HashMap<Worship.Language, HashMap<Long, Prayer>> _prayersHash = new HashMap();
    public HashMap<Long, Prayer> Prayers(Worship.Language language){
        HashMap<Long, Prayer> prayers = _prayersHash.get(language);
        if (prayers == null){
            _prayersHash.put(language, selectPrayersByLanguage(_id, language));
            prayers = _prayersHash.get(language);
        }
        return prayers;
    }

    private HashMap<Long, Prayer> selectPrayersByLanguage(long id, Worship.Language language){
        HashMap<Long, Prayer> prayersMap = new HashMap<>();

        SQLiteDatabase db = Connect.Item().get_db();
        String sql = String.format(QueryWorship.NAME, language.toString(), _id);

        Cursor mCursor = db.rawQuery(sql, new String [] {});
        try {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                do {
                    long id_prayer = mCursor.getLong(QueryWorship.NUM_COLUMN_ID_PRAYER);
                    String name = mCursor.getString(QueryWorship.NUM_COLUMN_LANG_NAME);
                    String body = mCursor.getString(QueryWorship.NUM_COLUMN_BODY);
                    String comment = mCursor.getString(QueryWorship.NUM_COLUMN_COMMENT);
                    prayersMap.put(id_prayer, new Prayer(id_prayer, name, body, comment));
                } while (mCursor.moveToNext());
            }
        } finally {
            mCursor.close();
        }
        return prayersMap;

    }

    public static class QueryWorship {

        // Название ресурса из файла scripts.xml
        public static final String NAME = "select pw.id_prayer, pr.name as lang_name, pr.comment, pr.body\n" +
                "    from prayers p left join worships_prayers pw on p._id = pw.id_prayer join prayers_%1$s pr on pr._id = p._id\n" +
                "    where pw.id_worship = %2$d\n" +
                "    order by pw.by_ord";

        // Название столбцов, должны совпадать с названиями столбцов из ресурска select_worship
        public static final String COLUMN_ID_PRAYER = "id_prayer";
        public static final String COLUMN_LANG_NAME = "lang_name";
        public static final String COLUMN_COMMENT = "comment";
        public static final String COLUMN_BODY = "body";

        // Номера столбцов
        public static final int NUM_COLUMN_ID_PRAYER = 0;
        public static final int NUM_COLUMN_LANG_NAME = 1;
        public static final int NUM_COLUMN_COMMENT = 2;
        public static final int NUM_COLUMN_BODY = 3;

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
