package com.ortosoft.ortodoxworship;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.db.SQLiteWorship;

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

    @SmallTest
    public void test_save_update_delete_find_group() throws Exception {

        SQLiteDatabase db = SQLiteWorship.Item().get_db();
        db.beginTransaction();
        try {
            // Тестируем сохранение
            String name = "222";
            Group group = new Group(name);
            group.SaveOrUpdate();
            Group found_group = Group.FindByName(name);
            assertEquals(name, found_group.get_name());

            // Тестируем изменение
            String name_1 = "333";
            group.set_name(name_1);
            group.SaveOrUpdate();
            found_group = Group.FindByName(name);
            Group found_group_new = Group.FindByName(name_1);

            assertNull(found_group);
            assertNotNull(found_group_new);

            // Тестируем удаление
            Group.Delete(found_group_new);
            found_group_new = Group.FindByName(name_1);
            assertNull(found_group_new);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

}