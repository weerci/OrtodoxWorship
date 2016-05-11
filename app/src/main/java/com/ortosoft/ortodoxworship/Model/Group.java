package com.ortosoft.ortodoxworship.Model;

/**
 * Created by admin on 11.05.2016.
 */
public class Group {
    private final String _name;

    public String get_name() {
        return _name;
    }

    public Group(String name) {
        _name = name;
    }

    public static Group FindByName(String name) {
        return new Group("empty_name");
    }
}