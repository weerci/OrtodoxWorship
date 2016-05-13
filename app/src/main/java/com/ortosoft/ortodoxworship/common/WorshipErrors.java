package com.ortosoft.ortodoxworship.common;

import com.ortosoft.ortodoxworship.App;
import com.ortosoft.ortodoxworship.R;

import java.util.HashMap;

/**
 * Created by admin on 13.05.2016.
 */
public class WorshipErrors extends Exception {
    private long _id;
    private String _desc;

    protected WorshipErrors(long id){
        super(WorshipErrors.get(id));

        _id = id;
        _desc = WorshipErrors.get(id);
    }

    // Создает экземпляр исключения
    public static WorshipErrors Item(long id)
    {
        return new WorshipErrors(id);
    }

    // Возвращает номер исключения из WorshiErrors
    public long get_id(){
        return _id;
    }

    // Возвращает описание ошибки
    public String get_desc(){
        return _desc;
    }


    // Список ошибок возможных в программе
    public static final HashMap<Long, String> WorshipErrors;
    static
    {
        WorshipErrors = new HashMap<Long, String>();
        WorshipErrors.put(new Long(1000), App.getContext().getString(R.string.error_group_name));
    }

}
