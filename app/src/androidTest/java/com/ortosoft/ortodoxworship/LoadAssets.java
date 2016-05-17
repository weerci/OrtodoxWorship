package com.ortosoft.ortodoxworship;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.ortosoft.ortodoxworship.Model.Member;

/**
 * Created by dima on 17.05.2016.
 */
public class LoadAssets extends ApplicationTestCase<Application> {
    public LoadAssets() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
    }

}
