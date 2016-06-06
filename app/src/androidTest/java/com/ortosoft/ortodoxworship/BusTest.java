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
@SuppressWarnings("ConstantConditions")
public class BusTest extends ApplicationTestCase<Application> {

    public BusTest() {
        super(Application.class);
    }
    SQLiteDatabase db = Connect.Item().get_db();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        db.beginTransaction();
    }

    @Override
    protected void tearDown() throws Exception {
        db.endTransaction();
        super.tearDown();
    }

    @SmallTest
    public void test_event_for_delete_group() throws Exception {
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

        assertEquals(2, member.get_listOfGroup().size());

        // Удаляем группу
        Group.Delete(group);
        assertEquals(1, member.get_listOfGroup().size());
    }

    @SmallTest
    public void test_event_for_update_group() throws Exception {
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

        // Загружаем member из базы, что бы объекты групп member не ссылались на одну область памяти
        Member new_member = Member.FindById(member.get_id());
        assertEquals(group.get_name(), new_member.get_listOfGroup().get(group.get_id()).get_name());

        String new_name = "33334";
        group.set_name(new_name);
        group.SaveOrUpdate();
        assertEquals(group.get_name(), new_member.get_listOfGroup().get(group.get_id()).get_name());
    }

    @SmallTest
    public void test_event_for_delete_member() throws Exception {
        Member member = new Member("111","2222", State.IsBaptized.yes, State.IsDead.no);
        Member member1 = new Member("2222","3333", State.IsBaptized.yes, State.IsDead.no);
        member.SaveOrUpdate();
        member1.SaveOrUpdate();

        Group group = new Group("1111");
        group.AddMember(member);
        group.AddMember(member1);

        Group group1 = new Group("2222");
        group1.AddMember(member);
        group1.AddMember(member1);

        group.SaveOrUpdate();
        group1.SaveOrUpdate();

        Member.Delete(member);
        assertEquals(1, group.get_members().size());
        assertEquals(1, group1.get_members().size());
        assertEquals(2, Member.TableMembersGroups.CountOfRows());

    }

    @SmallTest
    public void test_event_for_update_member() throws Exception {
        String name = "111";
        String comment = "111";
        State.IsBaptized isBaptized = State.IsBaptized.no;
        State.IsDead isDead = State.IsDead.no;

        Member member = new Member(name, comment, isBaptized, isDead);
        member.SaveOrUpdate();

        String nameGroup = "222";
        Group group = new Group(nameGroup);
        group.AddMember(member);
        group.SaveOrUpdate();

        Group group1 = Group.FindByName(nameGroup);

        name = "222";
        comment = "222";
        isBaptized = State.IsBaptized.yes;
        isDead = State.IsDead.yes;

        member.set_name(name);
        member.set_comment(comment);
        member.set_isBaptized(isBaptized);
        member.set_isIsDead(isDead);
        member.SaveOrUpdate();

        assertEquals(name, group1.get_members().get(member.get_id()).get_name());
        assertEquals(comment, group1.get_members().get(member.get_id()).get_comment());
        assertEquals(isBaptized, group1.get_members().get(member.get_id()).get_isBaptized());
        assertEquals(isDead, group1.get_members().get(member.get_id()).get_isIsDead());


    }
}
