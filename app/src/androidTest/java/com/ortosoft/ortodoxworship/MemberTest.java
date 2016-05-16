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

    @SmallTest
    public void test_create_new_member() throws Exception {
        String name = "111";
        String desc = "111_111";

        Member member = new Member(name, desc);

        assertEquals(name, member.get_name());
        assertEquals(desc, member.get_description());
    }

}
