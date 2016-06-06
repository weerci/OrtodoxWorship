package com.ortosoft.ortodoxworship;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.ortosoft.ortodoxworship.Model.Prayer;
import com.ortosoft.ortodoxworship.Model.Worship;

import java.util.HashMap;

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

    @SmallTest
    public void test_select_prayers_by_language() throws Exception {
        String worshipName = "morning";
        Worship worship = Worship.FindByName(worshipName);

        HashMap<Long, Prayer> prayerHashMap = worship.get_prayers(Worship.Language.cks);
        assertEquals(11, prayerHashMap.size());

        prayerHashMap = worship.get_prayers(Worship.Language.rus);
        assertEquals(2, prayerHashMap.size());

        prayerHashMap = worship.get_prayers(Worship.Language.eng);
        assertEquals(0, prayerHashMap.size());
    }
}
