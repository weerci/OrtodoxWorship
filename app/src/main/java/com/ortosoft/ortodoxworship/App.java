package com.ortosoft.ortodoxworship;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.ortosoft.ortodoxworship.db.SQLiteWorship;

import java.io.IOException;

/**
 * Created by admin on 11.05.2016.
 */
public class App extends Application {
    private static App _instance;
    private static Context _context;

    public App() {_instance = this;}


    public static App getInstance() {return _instance;}
    public static Context getContext() {return _context;}

    @Override
    public void onCreate() {
        super.onCreate();
        App._context = getApplicationContext();

        try {
            SQLiteWorship.Initialize();
            Log.d("It's ok", "really, really");
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
