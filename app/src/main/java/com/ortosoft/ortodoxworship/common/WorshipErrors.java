package com.ortosoft.ortodoxworship.common;

import com.ortosoft.ortodoxworship.App;
import com.ortosoft.ortodoxworship.R;
import android.support.v4.util.LongSparseArray;

/**
 * Created by admin on 13.05.2016 at 03: 04.
 * Реализация обработки ошибок
 */
public class WorshipErrors extends Exception {
    private final long _id;
/*    private final String _desc;
    private final Throwable _throwable;*/

    private WorshipErrors(long id){
        super(WorshipErrors.get(id), null);

        _id = id;
/*        _desc = WorshipErrors.get(id);
        _throwable = null;*/
    }

    // Создает экземпляр исключения
    public static WorshipErrors Item(long id)
    {
        return new WorshipErrors(id);
    }

    // Возвращает номер исключения из WorshipErrors
    public long get_id(){
        return _id;
    }

// --Commented out by Inspection START (07.06.2016 2:26):
//    // Возвращает описание ошибки
//    public String get_desc(){
//        return _desc;
//    }
// --Commented out by Inspection STOP (07.06.2016 2:26)

// --Commented out by Inspection START (07.06.2016 2:25):
//    // Возвращает внутреннее исключение
//    public Throwable get_throwable() { return _throwable; }
// --Commented out by Inspection STOP (07.06.2016 2:25)

    // Список ошибок возможных в программе
    private static final LongSparseArray<String> WorshipErrors;
    static{
        WorshipErrors = new LongSparseArray<>();
        WorshipErrors.put(1000, App.getContext().getString(R.string.error_group_name));
        WorshipErrors.put(1001, App.getContext().getString(R.string.error_unique_group_name));
        WorshipErrors.put(1002, App.getContext().getString(R.string.error_unique_group_name));
    }

}
