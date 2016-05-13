package com.ortosoft.ortodoxworship.db;

/**
 * Created by admin on 11.05.2016.
 */
public class Tables {

    public static class Group {
        // Название таблицы
        public static final String TABLE_NAME = "groups";

        // Название столбцов
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";

        // Номера столбцов
        public static final int NUM_COLUMN_ID = 0;
        public static final int NUM_COLUMN_NAME = 1;

        public static String get_ddl() {
            return String.format(
                    "CREATE TABLE %1$s (%2$s INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL, %3$s TEXT NOT NULL); "+
                    "CREATE UNIQUE INDEX idx_%1$s_%3$s ON %1$s (%3$s); ", TABLE_NAME, COLUMN_ID, COLUMN_NAME);
        }
    }

}
