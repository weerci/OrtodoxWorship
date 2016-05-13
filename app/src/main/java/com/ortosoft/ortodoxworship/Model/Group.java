package com.ortosoft.ortodoxworship.Model;

import com.ortosoft.ortodoxworship.common.WorshipErrors;

/**
 * Created by admin on 11.05.2016.
 * Класс реализует функционла работы с группами людей, содержит код доступа к базе данных
 */
public class Group {
    private final String _name;

    // Создает группу с заданным именем
    public Group(String name) {
        _name = name;
    }

    // Группа сохраняется в базе данных
    public long SaveOrUpdate() throws WorshipErrors
    {
        if (_name == null) {
            throw WorshipErrors.Item(1000);
        }

        return
    }

    public String get_name() {
        return _name;
    }


    public static Group FindByName(String name) {
        return new Group("empty_name");
    }

    private final String insert = "insert into group ()";
}