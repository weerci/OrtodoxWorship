package com.ortosoft.ortodoxworship.common;

/**
 * Created by admin on 17.05.2016 at 03 at 03: 04.
 * Класс для работы с таблицами связками используется при восстановлении данных
 * пользователей при обновлении базы
 */
public class Pair {
    private final long _id1;
    private final long _id2;

    public long get_id1() {
        return _id1;
    }

/*    public void set_id1(long _id1) {
        this._id1 = _id1;
    }*/

    public long get_id2() {
        return _id2;
    }

//    public void set_id2(long _id2) {
//        this._id2 = _id2;
//    }

    public Pair(long id1, long id2)
    {
        _id1 = id1;
        _id2 = id2;
    }
}
