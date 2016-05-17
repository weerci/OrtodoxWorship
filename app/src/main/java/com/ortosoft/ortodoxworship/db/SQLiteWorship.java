package com.ortosoft.ortodoxworship.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ortosoft.ortodoxworship.App;
import com.ortosoft.ortodoxworship.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by admin on 11.05.2016.
 */
public class SQLiteWorship extends SQLiteOpenHelper {

    //region static fields
    private static final String DB_NAME = "remember.db";
    private static final String DB_FOLDER = "/data/data/"+ App.getInstance().getPackageName() + "/databases/";
    public static final String DB_PATH = DB_FOLDER + DB_NAME;
    private static final String DB_ASSETS_PATH = "db/" + DB_NAME;
    private static final int DB_VERSION = 7;
    private static final int DB_FILES_COPY_BUFFER_SIZE = 8192;
    //endregion

    // region Fields

    private static boolean _dbExist = false;
    private static boolean _versionIsCorrect = false;

    // endregion
    public static void Initialize() throws IOException, ClassNotFoundException {
        Recovery rec = new Recovery();

        checkDb();
        if (!_dbExist){
            copyDbFromAssets();
        } else if(_dbExist && !_versionIsCorrect)
        {
            Log.d("Start recovery", "Start recovery");
            Recovery recovery = new Recovery();
            recovery.LoadFromBase();
            copyDbFromAssets();
            recovery.SaveToBase();
        }
    }

    public SQLiteWorship() {
        super(App.getContext(), DB_NAME, null, DB_VERSION);
    }
    private static void copyDbFromAssets() throws IOException {

        Context appContext = App.getInstance().getApplicationContext();
        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            inStream = new BufferedInputStream(appContext.getAssets().open(DB_ASSETS_PATH), DB_FILES_COPY_BUFFER_SIZE);
            File dbDir = new File(DB_FOLDER);
            if (dbDir.exists() == false)
                dbDir.mkdir();
            outStream = new BufferedOutputStream(new FileOutputStream(DB_PATH),
                    DB_FILES_COPY_BUFFER_SIZE);

            byte[] buffer = new byte[DB_FILES_COPY_BUFFER_SIZE];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
        }
        catch (Exception e)
        {
            Log.d("copy", "что то пошло не так");
        }
        finally {
            outStream.flush();
            outStream.close();
            inStream.close();
        }
    }
    private static void checkDb() {
        SQLiteDatabase checkDB = null;
        try {
            File dbFile = App.getContext().getDatabasePath(DB_NAME);
            _dbExist = dbFile.exists();
            if (_dbExist) {
                checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
                _versionIsCorrect = checkDB.getVersion() == DB_VERSION;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (checkDB != null)
                checkDB.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        throw new SQLiteException(
                "Метод onCreate для базы. Этот метод не должен никогда вызываться!!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException(
                "Метод onUpgrade для базы. Этот метод не должен никогда вызываться!!!");
    }

}
