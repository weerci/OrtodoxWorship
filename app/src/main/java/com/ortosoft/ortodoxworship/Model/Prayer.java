package com.ortosoft.ortodoxworship.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ortosoft.ortodoxworship.db.Connect;

/**
 * Created by dima on 16.05.2016.
 */
public class Prayer {
    private long _id;

    public Prayer(long id, String name, String body, String comment) {
        _id = id;
        _name = name;
        _body = body;
        _comment = comment;
    }

    public long get_id() {
        return _id;
    }
    public void set_id(long _id) {
        this._id = _id;
    }


    private String _name;
    public String getName() {
        return _name;
    }
    public void setName(String name) {
        _name = name;
    }

    private String _body;
    public String getBody() {
        return _body;
    }
    public void setBody(String body) {
        _body = body;
    }

    private String _comment;
    public String getComment() {
        return _comment;
    }
    public void setComment(String comment) {
        _comment = comment;
    }

}
