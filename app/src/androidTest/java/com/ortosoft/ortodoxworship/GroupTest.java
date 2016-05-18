package com.ortosoft.ortodoxworship;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.common.WorshipErrors;
import com.ortosoft.ortodoxworship.db.Connect;
import com.ortosoft.ortodoxworship.db.SQLiteWorship;

import junit.framework.Assert;

import java.util.ArrayList;

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

        SQLiteDatabase db = Connect.Item().get_db();
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

        } finally {
            db.endTransaction();
        }
    }

    @SmallTest
    public void test_load_duplicate_to_group() throws Exception{

        SQLiteDatabase db = Connect.Item().get_db();

        db.beginTransaction();
        try {
            // Тестирование вставки дубликатов
            try {
                Group[] array_created = new Group[] { new Group("111"), new Group("111"), new Group("111"),new Group("111") };
                for (Group g : array_created)
                    g.SaveOrUpdate();

                Assert.fail("WorshipErrors not throw");

            } catch (WorshipErrors worshipErrors) {
                assertEquals(1001, worshipErrors.get_id());
            }

            // Тестирование изменения записей, при которых возникают дубликаты
            try {
                Group[] array_created = new Group[] { new Group("111"), new Group("222"), new Group("333"),new Group("444") };
                for (Group g : array_created)
                    g.SaveOrUpdate();

                array_created[1].set_name("111");
                array_created[1].SaveOrUpdate();

                Assert.fail("WorshipErrors not throw");

            } catch (WorshipErrors worshipErrors) {
                assertEquals(1001, worshipErrors.get_id());
            }
        } finally {
            db.endTransaction();
        }
    }

    @SmallTest
    public void test_select_all_group_and_delete_several() throws Exception
    {
        SQLiteDatabase db = Connect.Item().get_db();
        db.beginTransaction();
        try {
            // Тестируем сохранение
            Group[] array_created = new Group[] { new Group("111"), new Group("222"), new Group("333"),new Group("444") };
            for (Group g : array_created) {
                g.SaveOrUpdate();
            }
            ArrayList<Group> list_found = Group.FindAll();
            assertEquals(array_created.length, list_found.size());

            // Тестируем удаление
            Group.Delete(array_created);
            list_found = Group.FindAll();
            assertEquals(0, list_found.size());

        } finally {
            db.endTransaction();
        }

    }

}