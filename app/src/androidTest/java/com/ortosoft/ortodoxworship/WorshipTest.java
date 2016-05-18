package com.ortosoft.ortodoxworship;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.ortosoft.ortodoxworship.Model.Worship;

/**
 * Created by dima on 16.05.2016.
 */
public class WorshipTest extends ApplicationTestCase<Application> {
    public WorshipTest() {
        super(Application.class);
    }

    @SmallTest
    public void test_get_worship_by_name() throws Exception
    {
        String worshipName = "morning";
        Worship worship = Worship.FindByName(worshipName);
        assertEquals(worshipName, worship.get_name());
    }

}
