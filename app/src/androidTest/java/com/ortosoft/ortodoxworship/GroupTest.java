package com.ortosoft.ortodoxworship;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.ortosoft.ortodoxworship.Model.Group;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class GroupTest extends ApplicationTestCase<Application> {
    public GroupTest() {
        super(Application.class);
    }

    @SmallTest
    public void test_create_new_group() throws Exception {
        String name = "111";
        Group group = new Group(name);

        assertEquals(group.get_name(), "111");
    }

    /*    @Test
    public void find_group_by_name()throws Exception {
        String name = "222";
        Group group = new Group(name);
        Group found_group = Group.FindByName(name);

        assertEquals(found_group.get_name(), name);

    }*/
}