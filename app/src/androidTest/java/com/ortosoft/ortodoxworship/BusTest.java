package com.ortosoft.ortodoxworship;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.Model.Member;
import com.ortosoft.ortodoxworship.bus.BusGroup;
import com.ortosoft.ortodoxworship.common.State;
import com.ortosoft.ortodoxworship.db.Connect;


/**
 * Created by dima on 24.05.2016.
 */
public class BusTest extends ApplicationTestCase<Application> {

    public BusTest() {
        super(Application.class);
    }

    @SmallTest
    public void test_register_event_for_insert_update_delete_group() throws Exception {

        SQLiteDatabase db = Connect.Item().get_db();
        db.beginTransaction();

        try {
            // Создаем группы
            Group group = new Group("123");
            Group group1 = new Group("321");
            group.SaveOrUpdate();
            group1.SaveOrUpdate();

            // Добавляем группы пользователю
            Member member = new Member("111", "222", State.IsBaptized.yes, State.IsDead.no);
            member.AddToGroup(group);
            member.AddToGroup(group1);
            member.SaveOrUpdate();

            // Объект member регистрируется как слушатель
            BusGroup.addToBus(member);

        } finally {
            db.endTransaction();
        }


    }

}
