package com.ortosoft.ortodoxworship;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * Created by dima on 17.05.2016 at 03: 03.
 * Прежде чем запускать прочие инструментальные тесты, нужно запустить этот,
 * он создает экземпляр приложения, которым потом пользуются все другие тесты
 */
@SuppressWarnings("unused")
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
