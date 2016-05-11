package com.ortosoft.ortodoxworship;

import com.ortosoft.ortodoxworship.Model.Group;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by admin on 11.05.2016.
 */
public class GroupsTest {

    @Test
    public void create_new_group() throws Exception {
        String name = "111";
        Group group = new Group(name);

        assertEquals(group.get_name(), "111");
    }

    @Test
    public void find_group_by_name()throws Exception {
        String name = "222";
        Group group = new Group(name);
        Group found_group = Group.FindByName(name);

        assertEquals(found_group.get_name(), name);
    }
}
