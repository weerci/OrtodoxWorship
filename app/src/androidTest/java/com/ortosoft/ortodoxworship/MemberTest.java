package com.ortosoft.ortodoxworship;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.Model.Member;
import com.ortosoft.ortodoxworship.Model.Worship;
import com.ortosoft.ortodoxworship.common.State;
import com.ortosoft.ortodoxworship.db.Connect;
import com.ortosoft.ortodoxworship.db.SQLiteWorship;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 14.05.2016.
 */
public class MemberTest extends ApplicationTestCase<Application> {
    public MemberTest() {
        super(Application.class);
    }

    @SmallTest
    public void test_create_new_member() throws Exception {
        String name = "111";
        String comment = "111_111";
        Member member = new Member(name, comment);

        assertEquals(name, member.get_name());
        assertEquals(comment, member.get_comment());
        assertEquals(State.IsBaptized.unknown, member.get_isBaptized());
        assertEquals(State.IsDead.unknown, member.get_isIsDead());

        name = "222";
        comment = "222";

        member.set_name(name);
        member.set_comment(comment);
        member.set_isBaptized(State.IsBaptized.yes);
        member.set_isIsDead(State.IsDead.yes);

        assertEquals(name, member.get_name());
        assertEquals(comment, member.get_comment());
        assertEquals(State.IsBaptized.yes, member.get_isBaptized());
        assertEquals(State.IsDead.yes, member.get_isIsDead());

        Member member_found = Member.FindById(member.get_id());
        assertNull(member_found);
    }

    @SmallTest
    public void test_update_find_member() throws Exception {
        String name = "111";
        String comment = "111_111";
        Member member = new Member(name, comment);

        SQLiteDatabase db = Connect.Item().get_db();
        db.beginTransaction();

        try {
            member.SaveOrUpdate();

            name = "new_name";
            comment = "new_comment";
            State.IsBaptized isBaptized = State.IsBaptized.no;
            State.IsDead isDead = State.IsDead.no;

            member.set_isIsDead(isDead);
            member.set_isBaptized(isBaptized);
            member.set_name(name);
            member.set_comment(comment);
            member.SaveOrUpdate();

            Member found_member = Member.FindById(member.get_id());

            assertNotNull(found_member);
            assertEquals(name, member.get_name());
            assertEquals(comment, member.get_comment());
            assertEquals(isBaptized, member.get_isBaptized());
            assertEquals(isDead, member.get_isIsDead());
        } finally {
            db.endTransaction();
        }
    }

    @SmallTest
    public void test_select_all_member_and_delete_several() throws Exception
    {
        SQLiteDatabase db = Connect.Item().get_db();
        db.beginTransaction();
        try {
            // Тестируем сохранение
            Member[] array_created = new Member[] {
                    new Member("1", "1", State.IsBaptized.no, State.IsDead.no),
                    new Member("2", "2", State.IsBaptized.no, State.IsDead.no),
                    new Member("3", "3", State.IsBaptized.yes, State.IsDead.yes),
                    new Member("4", "4", State.IsBaptized.yes, State.IsDead.yes) };
            for (Member m : array_created) {
                m.SaveOrUpdate();
            }

            ArrayList<Member> list_found = Member.FindAll();
            assertEquals(array_created.length, list_found.size());

            // Тестируем удаление
            Member.Delete(array_created);
            list_found = Member.FindAll();
            assertEquals(0, list_found.size());

        } finally {
            db.endTransaction();
        }

    }

    @SmallTest
    public void test_add_remove_member_to_group() throws Exception
    {
        SQLiteDatabase db = Connect.Item().get_db();
        db.beginTransaction();

        try {
            Group group = new Group("123");
            Group group1 = new Group("321");
            group.SaveOrUpdate();
            group1.SaveOrUpdate();

            Member member = new Member("111", "222", State.IsBaptized.yes, State.IsDead.no);
            member.AddToGroup(group);
            member.AddToGroup(group1);
            member.SaveOrUpdate();

            Member find_member = Member.FindById(member.get_id());
            HashMap<Long, Group> list_groups = find_member.get_listOfGroup();

            assertEquals(2, list_groups.size());
            assertEquals(list_groups.get(group.get_id()).get_name(), group.get_name());
            assertEquals(list_groups.get(group1.get_id()).get_name(), group1.get_name());

            member.RemoveFromGroup(group1);
            member.SaveOrUpdate();
            find_member = Member.FindById(member.get_id());
            list_groups = find_member.get_listOfGroup();
            assertEquals(1, list_groups.size());
            assertEquals(list_groups.get(group.get_id()).get_name(), group.get_name());
            assertEquals(1, Member.TableMembersGroups.CountOfRows());

        } finally {
            db.endTransaction();
        }
    }

    @SmallTest
    public void test_delete_member_bounded_with_group() throws Exception
    {
        SQLiteDatabase db = Connect.Item().get_db();
        db.beginTransaction();

        try {
            Member member = new Member("111", "222", State.IsBaptized.no, State.IsDead.no);
            Group group = new Group("4444");
            group.SaveOrUpdate();

            member.AddToGroup(group);
            member.SaveOrUpdate();

            // Пытаемся удалить member, с которым связаны группы
            Member.Delete(member);
            assertEquals(0, Member.TableMember.CountOfRows());
            assertEquals(1, Group.TableGroup.CountOfRows());
            assertEquals(0, Member.TableMembersGroups.CountOfRows());

        } finally {
            db.endTransaction();
        }
    }

    @SmallTest
    public void test_delete_group_bounded_with_member() throws Exception
    {
        SQLiteDatabase db = Connect.Item().get_db();
        db.beginTransaction();

        try {
            Member member = new Member("111", "222", State.IsBaptized.no, State.IsDead.no);
            Group group = new Group("4444");
            group.SaveOrUpdate();

            member.AddToGroup(group);
            member.SaveOrUpdate();

            // Пытаемся удалить member, с которым связаны группы
            Group.Delete(group);
            assertEquals(1, Member.TableMember.CountOfRows());
            assertEquals(0, Group.TableGroup.CountOfRows());
            assertEquals(0, Member.TableMembersGroups.CountOfRows());

        } finally {
            db.endTransaction();
        }
    }

    @SmallTest
    public void test_add_remove_member_to_worship() throws Exception
    {
        SQLiteDatabase db = Connect.Item().get_db();
        db.beginTransaction();

        try {
            Worship worshipMorning = Worship.FindByName("morning");
            Worship worshipEvening = Worship.FindByName("evening");
            assertNotNull(worshipMorning);
            assertNotNull(worshipEvening);

            Member member = new Member("1111", "22222", State.IsBaptized.no, State.IsDead.no);
            member.AddToWorship(worshipMorning);
            member.AddToWorship(worshipEvening);
            member.SaveOrUpdate();

            Member find_member= Member.FindById(member.get_id());
            ArrayList<Worship> list_worship = find_member.get_listOfWorship();
            assertEquals(2, list_worship.size());

            member.RemoveFromWorship(worshipEvening);
            member.SaveOrUpdate();
            find_member = Member.FindById(member.get_id());
            list_worship = find_member.get_listOfWorship();
            assertEquals(1, list_worship.size());
            assertEquals(list_worship.get(0).get_name(), worshipMorning.get_name());
            assertEquals(1, Worship.TableWorshipsMembers.CountOfRows());

        } finally {
            db.endTransaction();
        }
    }

    @SmallTest
    public void test_delete_member_bounded_with_worship() throws Exception
    {
        SQLiteDatabase db = Connect.Item().get_db();
        db.beginTransaction();

        try {
            Member member = new Member("111", "222", State.IsBaptized.no, State.IsDead.no);
            Worship worshipMorning = Worship.FindByName("morning");
            Worship worshipEvening = Worship.FindByName("evening");

            member.AddToWorship(worshipMorning);
            member.AddToWorship(worshipEvening);
            member.SaveOrUpdate();

            // Пытаемся удалить member, с которым связаны группы
            Member.Delete(member);
            assertEquals(0, Worship.TableWorshipsMembers.CountOfRows());
            assertEquals(0, Member.TableMember.CountOfRows());

        } finally {
            db.endTransaction();
        }
    }
}

