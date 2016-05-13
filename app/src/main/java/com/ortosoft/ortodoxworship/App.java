package com.ortosoft.ortodoxworship;

import android.app.Application;
import android.content.Context;

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
        App._context = getApplicationContext();
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(_context, true));
        super.onCreate();
    }

}
