package com.ortosoft.ortodoxworship;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.Model.Member;
import com.ortosoft.ortodoxworship.common.State;

/**
 * Created by admin on 14.05.2016.
 */
public class MemberTest extends ApplicationTestCase<Application> {
    public MemberTest() {
        super(Application.class);
    }

    private Member member;
    private String name = "111";
    private String comment = "111_111";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        member = new Member(name, comment);
    }
    @Override
    protected void tearDown() throws Exception {
        Member.Delete(member);

        super.tearDown();
    }

    @SmallTest
    public void test_create_new_member() throws Exception {
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

    }

    @SmallTest
    public void test_update_member() throws Exception {
        member.SaveOrUpdate();
        assertEquals(name, member.get_name());
        assertEquals(comment, member.get_comment());
    }

}
